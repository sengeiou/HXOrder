package com.happysnaker.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.happysnaker.config.OrderRabbitMqConfig;
import com.happysnaker.config.RedisCacheManager;
import com.happysnaker.exception.OrderAddException;
import com.happysnaker.exception.ReadWriterLockException;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.*;
import com.happysnaker.service.BaseService;
import com.happysnaker.service.OrderService;
import com.happysnaker.utils.JsonUtils;
import com.happysnaker.utils.Pair;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Configuration
@EnableRabbit
public class OrderServiceImpl extends BaseService implements OrderService {
    /**
     * 这个参数是前端显示流程的对应索引，可以通过对应的 consume_type 访问，通过 indexOf 方法查询索引
     * <p>consume_type 0 对应扫码点餐  经历流程：待点餐 - 确认中 - 待支付 - 已完成<br/>
     * <p>
     * consume_type 1 对应到店消费 经历流程：支付保证金 - 确认中 - 备餐中 - 待用餐 - 待支付 - 已完成<br/>
     * <p>
     * consume_type 2 对应到店自取 经历流程：待支付 - 确认中 - 备餐中 - 待取餐 - 已完成<br/>
     * <p>
     * consume_type 3 对应外卖 经历流程：待支付 - 确认中 - 备餐中 - 配送中 - 已完成<br/>
     * <p>
     * orderType: 0待点餐, 1待支付, 2确认中，3备餐中，4待用餐，5待取餐，6配送中，7已完成，8取消确认中，9已取消<br/>
     * </p>
     */
    private Map<Integer, String> statusMap;
    private RabbitTemplate rabbit;
    // 一次消费加多少积分
    int integral = 5;



    @Autowired
    public OrderServiceImpl(@Qualifier("myRabbitTemplate") RabbitTemplate rabbit) {
        this.rabbit = rabbit;
        statusMap = new HashMap<>();
        statusMap.put(0, "217");
        statusMap.put(1, "023417");
        statusMap.put(2, "12357");
        statusMap.put(3, "12367");
    }


    private JSONObject order2Json(Order f) {
        JSONObject obj = JsonUtils.getJSONObject(f);
        Store store = storeMapper.getStore(f.getStoreId());
        //注入店铺信息
        obj.put("storeName", store.getName());
        obj.put("storeImg", store.getImg());
        //注入流程图状态
        obj.put("nowStatus", statusMap.get(f.getConsumeType()).indexOf(String.valueOf(f.getOrderType())));
        return obj;
    }

    @Override
    public void updateOrderType(String orderId, int newStatus) throws UpdateException {
        int row = orderMapper.updateOrderType(orderId, newStatus);
        if (row == 0) {
            throw new UpdateException("更新订单状态失败: " + orderId);
        }
    }

    @Override
    public void updateFinalTime(String orderId, Timestamp ts) throws UpdateException {
        int row = orderMapper.updateOrderFinalTime(orderId, ts);
        if (row == 0) {
            throw new UpdateException("更新订单状态失败: " + orderId);
        }
    }

    @Override
    public String getUserOrders(String userId) {
        List<Order> list = orderMapper.queryOrdersByUserId(userId);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        Collections.sort(list, (o1, o2) -> {
            return o1.getCreateTime().getTime() > o2.getCreateTime().getTime() ? -1 : 1;
        });
        for (Order order : list) {
            order.setDishOrders(orderMapper.queryOrderDish(order.getId()));
            jsonArray.add(order2Json(order));
        }
        jsonObject.put("orders", jsonArray);
        return jsonObject.toJSONString();
    }


    @Override
    public boolean pay(String payId) throws UpdateException {
        String orderId = orderMapper.queryOrderId(payId);
        Order order = orderMapper.queryOrder(orderId);

        // 进行复杂的支付后，支付成功，更订单至下一状态
        int nextStatus = getNextStatus(order.getConsumeType(), order.getOrderType());
        updateOrderType(orderId, nextStatus);

        if (order.getConsumeType() == 1) {
            // 保证金按道理在生成支付订单的时候就已经计算好了，这里没办法模拟
            try {
                orderMapper.insertMargin(orderId, 0.2 * (order.getOriginalPrice() - order.getShopDiscount() - order.getCouponDiscount()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void cancelPay(String orderId) throws UpdateException {
        Order order = orderMapper.queryOrder(orderId);
        // 用户取消支付，更新至待支付状态或待支付保证金状态，然后利用消息队列完成取消
        order.setOrderType(order.getConsumeType() == 1 ? TO_BE_PAID_MARGIN_STATUS : TO_BE_PAID_STATUS);
        updateOrderType(orderId, order.getOrderType());
        // 发送消息到业务处
        System.out.println("发送取消消息！！！");
        rabbit.convertAndSend(OrderRabbitMqConfig.ORDER_PENDING_PAYMENT_ROUTEING_KEY, new OrderMessage(null, order));
    }

    private StringBuffer makeRandomString(String str, int length) {
        int n = str.length();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int r = (int) (Math.random() * n);
            sb.append(str.charAt(r));
        }
        return sb;
    }


    /**
     * 生成菜品库存哈希
     *
     * @param dishOrders
     * @return key -> dishId，val -> stock
     */
    private Map<Integer, Integer> getDishNumMap(List<Map<String, Object>> dishOrders) {
        Map<Integer, Integer> m = new HashMap<>(8);
        for (var map : dishOrders) {
            int dishId = (int) map.get("dishId");
            int dishNum = (int) map.get("dishNum");
            if (dishId >= 100000) {
                List<ComboDish> list = comboMapper.queryComboDishById(dishId);
                for (ComboDish cMap : list) {
                    // 套餐内置的菜品个数 * 套餐个数
                    m.put((Integer) cMap.getDishId(), m.getOrDefault(cMap.getDishId(), 0) + cMap.getDishNum() * dishNum);
                }
            } else {
                m.put(dishId, m.getOrDefault(dishId, 0) + dishNum);
            }
        }
        return m;
    }

    private boolean checkStock(Map<Integer, Integer> m, int storeId) {
        try {
            for (var it : m.entrySet()) {
                int id = it.getKey(), stock = it.getValue();
                System.out.println("id:" + id + " num:" + stock + " s:" + redisManager.getForHash(RedisCacheManager.getDishStockCacheKey(storeId), id));
                if ((int) redisManager.getForHash(RedisCacheManager.getDishStockCacheKey(storeId), id) < stock) {
                    return false;
                }
            }
            return true;
        } catch (NullPointerException e) {
            System.out.println("yy");
            redisManager.initRedisDishStockCache(dishMapper.queryDishInfo(storeId), storeId);
            return checkStock(m, storeId);
        }
    }


    /**
     * 添加订单要做的事有<br/>
     * <ul>
     *  <li> 计算订单ID<br/></li>
     *  <li>扣减库存，扣减库存必须保证库存充足<br/></li>
     * <li> 增加用户使用的折扣数目<br/></li>
     * <li>增加用户会员积分<br/></li>
     * <li> 扣减优惠券，目前不支持<br/></li>
     * <li>向 order 表 和 order_dish 表写订单数据<br/></li>
     *  <li>增加对应菜品销量<br/></li>
     * <li> 将菜品写入等待队列，以便计算新用户下单后需要等待的时常<br/></li>
     * </ul>
     */
    @Override
    public Map addUserOrder(String userId, Order order) throws OrderAddException, ReadWriterLockException {
        if (order == null) {
            return null;
        }
        // 这里应该还要对订单进行验证，因为购物车是前端做的，所以后端必须要重新验证，，购物车应该由后端处理数据，这里是吃了没经验的亏了，总而言之，这里应该从新验证一下订单是否合理，获取应该得放在控制层做，这里就懒得写了......
        if (!order.getIsNew() && !VerifyUtils.isNullOrEmpty(order.getId())) {
            //这是一个旧订单，由用户继续加餐而来
            System.out.println("旧");
            handleOldOrder(order);
            return null;
        }
        order.setIsNew(true);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        order.setCreateTime(timestamp);
        //用当前日期与随机3位生成订单编号
        String orderId = new StringBuffer(timestamp.toString().replaceAll("[^0-9]", "")).append(makeRandomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", 3)).toString();


        order.setId(orderId);
        order.setUserId(userId);


        // 测试用，删除 key 以便更新 redis
        redis.delete(RedisCacheManager.getDishStockCacheKey(order.getStoreId()));

        if (order.getConsumeType() == 2) {
            // 以当前时间戳生产取餐码，确保取餐吗唯一
            String code = VerifyUtils.BaseConversion(System.currentTimeMillis(), 32);
            order.setFetchMealCode(code);
        }


        //m 保存的是菜品ID与要下单的数量，主要是将套餐中的每个菜品与单点菜品合并
        Map<Integer, Integer> m = getDishNumMap(order.getDishOrders());
        if (!checkStock(m, order.getStoreId())) {
            throw new OrderAddException("库存不足");
        }
        // 扣减库存
        boolean hasDeduction = false;
        try {
            // 扣减 redis，发送消息，数据库层面会进行乐观锁判断
            for (var it : m.entrySet()) {
                int id = it.getKey(), stock = it.getValue();
                redis.opsForHash().increment(RedisCacheManager.getDishStockCacheKey(order.getStoreId()), id, -stock);
            }
            hasDeduction = true;
            // 订单算是生成成功，产生随机支付单号，发起支付
            order.setPayId(UUID.randomUUID().toString().replace("-", ""));
            OrderMessage om = new OrderMessage(m, order);

            System.out.println("扣减库存成功，上锁，发送消息到消息队列！");

            rabbit.convertAndSend(OrderRabbitMqConfig.ORDER_ADD_ROUTEING_KEY, om);

        } catch (NullPointerException e) {
            // 缓存不存在，重试，注意并发条件下会出错，这里不用回滚，因为我们要更新了
            e.printStackTrace();
            redisManager.initRedisDishStockCache(dishMapper.queryDishInfo(order.getStoreId()), order.getStoreId());
            return addUserOrder(userId, order);
        } catch (Exception e) {
            e.printStackTrace();
            if (hasDeduction) {
                m.put(-1, order.getStoreId());
                rabbit.convertAndSend(OrderRabbitMqConfig.ROLL_BACK_STOCK_ROUTEING_KEY, m);
            }
            throw  e;
        }
        // 返回支付单号给前端
        Map map = new HashMap(2);
        map.put("payId", order.getPayId());
        map.put("orderId", order.getId());
        return map;
    }

    /**
     * 处理旧订单
     *
     * @param order
     * @throws OrderAddException
     */
    public void handleOldOrder(Order order) throws OrderAddException, ReadWriterLockException {
        // 筛选出添加的菜
        List<Map<String, Object>> dishOrders = order.getDishOrders().stream().filter((item -> {
            return (Boolean) item.getOrDefault("isAdd", false);
        })).collect(Collectors.toList());
        Map<Integer, Integer> m = getDishNumMap(dishOrders);

        order.setDishOrders(dishOrders);

        if (!checkStock(m, order.getStoreId())) {
            throw new OrderAddException("库存不足");
        }
        // 扣减库存
        boolean hasDeduction = false;
        try {
            // 扣减库存
            for (var it : m.entrySet()) {
                int id = it.getKey(), stock = it.getValue();
                redis.opsForHash().increment(RedisCacheManager.getDishStockCacheKey(order.getStoreId()), id, -stock);
            }
            hasDeduction = true;
            // 库存扣减成功，订单算完成，发布消息
            order.setOrderType(CONFIRMING_STATUS);
            OrderMessage om = new OrderMessage(m, order);

            rabbit.convertAndSend(OrderRabbitMqConfig.ORDER_ADD_ROUTEING_KEY, om);
        } catch (NullPointerException e) {
            // 缓存不存在，重试
            e.printStackTrace();
            // 该方法是原子的，并且会继续判断缓存是否存在，避免多个线程竞争
            redisManager.initRedisDishStockCache(dishMapper.queryDishInfo(order.getStoreId()), order.getStoreId());
            handleOldOrder(order);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            if (hasDeduction) {
                m.put(-1, order.getStoreId());
                rabbit.convertAndSend(OrderRabbitMqConfig.ROLL_BACK_STOCK_ROUTEING_KEY, m);
            }
            throw  e;
        }


    }

    @Override
    public double getWaitingTime(int storeId) {
        long nowTs = System.currentTimeMillis();
        double waitingTime = 0;
//      将菜品添加至当前菜品队列中，以便计算等待时间，redis中缓存了菜品的制作时间
//      当缓存过期，需要更新缓存
        List queue = redis.opsForList().range(RedisCacheManager.DISH_WAITING_QUEUE_KEY, 0, -1);
        try {
            for (Object o : queue) {
                Pair<Integer, Double> pair = (Pair) o;
                int id = pair.getFirst();
                if (id >= 100000) {
                    continue;
                }
                double ts = pair.getSecond();
                // * 60000 转换为 时间戳形式
                double makeTime = (double) redisManager.getForHash(RedisCacheManager.DISH_MAKE_TIME_CACHE_KEY, id) * 60000;

                //暂时没有办法简单的删除队列中的元素
                if (ts + makeTime > nowTs) {
                    waitingTime += (ts + makeTime - nowTs);
                }

            }
            System.out.println(waitingTime / 60000);
        } catch (NullPointerException e) {
            e.printStackTrace();
            redisManager.initRedisDishMakeTimeCache(dishMapper.queryDishes());
            return getWaitingTime(storeId);
        }

        return waitingTime / 60000;
    }

    @Override
    public void deleteOrder(String id) {
        int row = orderMapper.logicalDelete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(OrderApplyTable at) {
        try {
            int row = orderMapper.insertCancelApply(at);
        } catch (Exception e) {
            e.printStackTrace();
            orderMapper.deleteCancelApply(at.getOrderId());
            orderMapper.insertCancelApply(at);
        }
        orderMapper.updateOrderType(at.getOrderId(), 8);
    }

    /**
     * 消费者，分布式情况下应该由多个消费者
     *
     * @param bytes
     * @param m
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = {OrderRabbitMqConfig.ORDER_ADD_QUEUE})
    public void doAddOrder(byte[] bytes, Message m, com.rabbitmq.client.Channel channel) throws Exception {
        System.out.println("尝试添加订单！！！！");
        channel.basicQos(1);
        OrderMessage om = null;
        try {
            om = (OrderMessage) JsonUtils.getObjectFromBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
        }
        // 防止消费, messageId 是UUID产生的，比较长，bitmap 塞不下
        if (!VerifyUtils.isNullOrEmpty((String) redis.opsForValue().get(RedisCacheManager.getOrderMessageCacheKey(om.getMessageId())))) {
            System.out.println("重复消费！");
            return;
        }

        com.happysnaker.pojo.Order order = om.getOrder();
        System.out.println("订单是+" + order);
        try {
            // 先扣减库存，看看能不能通过乐观锁
            addToWaitingAndDeductionInventoryQueue(om.getDishNumMap(), order.getStoreId());

            if (order.getIsNew()) {
                System.out.println("新订单，添加！");
                orderMapper.insertOrderInfo(order);
                orderMapper.insertOrderPay(order.getId(), order.getPayId());
            } else {
                // 旧订单价格可能改变，更新价格，并且让管理员重新确认，更新订单状态
                System.out.println("旧订单！！！");
                orderMapper.updateShopDiscount(order.getId(), order.getShopDiscount());
                orderMapper.updateShopOriginalPrice(order.getId(), order.getOriginalPrice());
                this.updateOrderType(order.getId(), CONFIRMING_STATUS);
            }
            //取餐凭证
            if (order.getConsumeType() == 2) {
                orderMapper.insertFetchMealCode(order.getId(), order.getFetchMealCode());
            }

            handleDishOrders(order.getId(), order.getUserId(), order.getStoreId(), order.getDishOrders());

            // 更新积分
            userMapper.updateUserPoints(order.getUserId(), integral);
            System.out.println("添加订单完成！");
            channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            e.printStackTrace();
            // 第三个参数 false，拒绝重新入队，那么这条消息将进入死信
            channel.basicNack(m.getMessageProperties().getDeliveryTag(), false, false);
            // 抛出异常，事务回滚
            throw e;

        } finally {
            redis.opsForValue().set(RedisCacheManager.getOrderMessageCacheKey(om.getMessageId()), "ok");
            redis.expire(RedisCacheManager.getOrderMessageCacheKey(om.getMessageId()), 60, TimeUnit.SECONDS);
        }
    }

    /**
     * 更新用户使用折扣、插入订单菜品表、更新销量
     */
    public void handleDishOrders(String orderId, String userId, int storeId, List<Map<String, Object>> dishOrders) throws IOException {
        for (var it : dishOrders) {
            int row = userMapper.updateUsedDiscountCount(userId, (int) it.get("dishId"), (int) it.get("usedCount"));
            if (row == 0) {
                userMapper.insertUsedDiscountCount(userId, (int) it.get("dishId"), (int) it.get("usedCount"));
            }
            orderMapper.insertOrderDish(orderId, it);
            dishMapper.updateDishSale((int) it.get("dishId"), (int) it.get("dishNum"));
            //在 Redis 中存储当日的销量，由定时任务在每日 0点 写入
            redis.opsForHash().increment(RedisCacheManager.getTodayDateKey(), storeId, (int) it.get("dishNum"));
        }
        System.out.println("正常返回");
    }

    /**
     * 将菜品添加至等待队列，并扣减库存
     */
    public void addToWaitingAndDeductionInventoryQueue(Map<Integer, Integer> DishNumMap, int storeId) throws IOException, OrderAddException {
        long nowTs = System.currentTimeMillis();
        for (var it : DishNumMap.entrySet()) {
            System.out.println("扣减订单" + it.getKey() + " stock == " + it.getValue());
            // 乐观锁查询
            int row = dishMapper.updateDishInventory(storeId, it.getKey(), -it.getValue());
            System.out.println("扣减完成" + row);
            if (row == 0) {
                // 不捕获，让事务回滚
                throw new OrderAddException("库存不足");
            }
            //将菜品添加至等待队列中，对于多个相同菜品，重复添加单独实例以便处理
            for (int i = 0; i < (int) it.getValue(); i++) {
                redis.opsForList().leftPush(RedisCacheManager.DISH_WAITING_QUEUE_KEY, new Pair<Integer, Double>((Integer) it.getKey(), (double) nowTs));
            }
        }
    }


    @RabbitListener(queues = {OrderRabbitMqConfig.ROLL_BACK_STOCK_QUEUE})
    public void rollBackStock(byte[] bytes, Message m, com.rabbitmq.client.Channel channel) throws Exception {
        Map<Integer, Integer> DishNumMap = (Map<Integer, Integer>) JsonUtils.getObjectFromBytes(bytes);
        int storeId = DishNumMap.get(-1);
        try {
            for (var it : DishNumMap.entrySet()) {
                int id = it.getKey(), stock = it.getValue();
                redis.opsForHash().increment(RedisCacheManager.getDishStockCacheKey(storeId), id, stock);
            }
            channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            e.printStackTrace();
            channel.basicNack(m.getMessageProperties().getDeliveryTag(),
                    false, false);
            throw e;
        } catch (NullPointerException e) {
            redisManager.initRedisDishStockCache(dishMapper.queryDishInfo(storeId), storeId);
            rollBackStock(bytes, m, channel);
            throw e;
        }
    }

    @RabbitListener(queues = {OrderRabbitMqConfig.ORDER_ADD_DEAD_QUEUE})
    public void deadOrderMessageHandler(byte[] bytes, Message m, com.rabbitmq.client.Channel channel) throws IOException {
        channel.basicQos(1);
        OrderMessage om = null;
        try {
            om = (OrderMessage) JsonUtils.getObjectFromBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
        }
        //取消订单
        orderMapper.updateOrderType(om.getOrder().getId(), 9);
        com.happysnaker.pojo.Message message = com.happysnaker.pojo.Message.createSystemMessage("通知，您的订单处理失败", "服务器发送了一些不好的事情，因此没能正确处理您的订单，十分抱歉，您可以前往 我的-客服 寻求退款，订单ID为唯一凭证。订单ID：" + om.getOrder().getId(), om.getOrder().getUserId());
        messageMapper.insertMessage(message);
        if (messageMapper.updateUnReadUserMsgCount(message.getUserId(), 1) == 0) {
            messageMapper.insertUnReadUserMsgCount(message.getUserId(), 1);
        }

        // 发送消息回滚 redis
        om.getDishNumMap().put(-1, om.getOrder().getStoreId());
        rabbit.convertAndSend(OrderRabbitMqConfig.ROLL_BACK_STOCK_ROUTEING_KEY, om.getDishNumMap());
        channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
    }


    /**
     * 死信队列，尝试取消订单
     *
     * @param m
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = {OrderRabbitMqConfig.ORDER_CANCEL_QUEUE})
    public void doCancelOrder(byte[] bytes, Message m, com.rabbitmq.client.Channel channel) throws IOException {
        channel.basicQos(1);
        System.out.println("开始删除订单!!!");
        OrderMessage om = null;
        try {
            om = (OrderMessage) JsonUtils.getObjectFromBytes(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
        }
        String orderId = om.getOrder().getId();
        Order nowOrder = orderMapper.queryOrder(orderId);
        // 如果订单状态与数据库中不相等，说明已经被消费过了，或者用户支付了订单，直接确认即可
        if (nowOrder.getOrderType() != om.getOrder().getOrderType()) {
            channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        // 发送消息回滚 redis
        Map<Integer, Integer> m1 = getDishNumMap(om.getOrder().getDishOrders());
        m1.put(-1, om.getOrder().getStoreId());
        rabbit.convertAndSend(OrderRabbitMqConfig.ROLL_BACK_STOCK_ROUTEING_KEY, m1);

        // 取消订单
        orderMapper.updateOrderType(om.getOrder().getId(), CANCELLED_STATUS);

        com.happysnaker.pojo.Message message = com.happysnaker.pojo.Message.createSystemMessage("订单取消通知", "您有一份订单由于超时未支付而取消，订单ID为 " + om.getOrder().getId(), om.getOrder().getUserId());
        messageMapper.insertMessage(message);
        if (messageMapper.updateUnReadUserMsgCount(message.getUserId(), 1) == 0) {
            messageMapper.insertUnReadUserMsgCount(message.getUserId(), 1);
        }
        channel.basicAck(m.getMessageProperties().getDeliveryTag(), false);
    }
}
