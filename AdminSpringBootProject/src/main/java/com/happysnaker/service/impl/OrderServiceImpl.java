package com.happysnaker.service.impl;

import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Message;
import com.happysnaker.pojo.Order;
import com.happysnaker.service.OrderService;
import com.happysnaker.service.base.BaseService;
import com.happysnaker.service.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/9
 * @email happysnaker@foxmail.com
 */
@Transactional(rollbackFor = Exception.class)
@Service("orderServiceImpl")
public class OrderServiceImpl extends BaseService implements OrderService {


    @Autowired
    public OrderServiceImpl(Observer observer) {
        // 注册观察者
        System.out.println("注册观察者");
        super.registerObserver(observer, 1);
    }

    @Override
    public List<Order> getOrderListByPagination(int pageNum, int pageSize, String keyword, Integer type, Timestamp creatTime, Integer storeId) {
        int offset = (pageNum - 1) * pageSize;
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        Timestamp endTime = null;
        if (creatTime != null) {
            Date date = new Date(creatTime.getTime());
            date.setHours(23);
            date.setMinutes(59);
            endTime = new Timestamp(date.getTime());
        }

        List<Order> ans = orderMapper.queryOrderByPagination(offset, pageSize, keyword, type, creatTime, endTime, storeId);
        Collections.sort(ans, (o1, o2)-> {
            return o1.getCreateTime().getTime() - o2.getCreateTime().getTime() > 0 ? -1 : 1;
        });
        return ans;
    }

    @Override
    public void deleteOrder(String id) throws UpdateException {
        orderMapper.logicalDeleteOrder(id);
    }

    @Override
    public void updateOrderType(String id, int type) throws UpdateException {
        if (type == TO_BE_PAID_STATUS) {
            Order order = getOrder(id);
            for (Map<String, Object> it : order.getDishOrders()) {
                if ((int)it.getOrDefault("is_add", 0) == 1) {
                    confirmOrder(order);
                    return;
                }
            }
        }

        int row = orderMapper.updateOrderType(id, type);
        if (row == 0) {
            throw new UpdateException("数据库中不存在该订单记录，订单编号：" + id);
        }
        super.notify(1, id, type);
    }

    @Override
    public Order getOrder(String id) {
        return orderMapper.queryOrder(id);
    }

    @Override
    public void confirmOrder(Order order) throws UpdateException {
        List<Map<String, Object>> dishOrders = order.getDishOrders();
        // key 是 dishId, val 是 待插入的一项纪录，通过 dishId 合并可能相同的菜品
        Map<Integer, Map<String, Object>> map = new HashMap<>(dishOrders.size());
        //订单 dishOrders 可能有用户新增的菜品，这类菜品被标记为 isAdd，我们需要合并菜品(如果相同的话)
        for (Map<String, Object> dishOrder : dishOrders) {
            int dishId = (int) dishOrder.get("dishId");
            int dishNum = (int)map.getOrDefault(dishId, new HashMap<>(1)).getOrDefault("dishNum", 0) + (int)dishOrder.get("dishNum");
            map.putIfAbsent(dishId, dishOrder);
            map.get(dishId).put("dishNum", dishNum);
        }
        //确实合并了，则更新订单菜品记录，其他情况下，仅仅只需更新订单状态
        if (map.size() < dishOrders.size()) {
            orderMapper.deleteOrderDish(order.getId());
            for (Map.Entry<Integer, Map<String, Object>> it : map.entrySet()) {
                orderMapper.insertOrderDish(order.getId(), it.getValue());
            }
        }
        //更新至下一状态
        this.updateOrderType(order.getId(), getNextStatus(order.getConsumeType(), order.getOrderType()));
    }

    @Override
    public boolean getPaymentStatus(String id) {
        return orderMapper.getPaymentStatus(id) != 0;
    }

    @Override
    public void handingAndSetPaymentStatus(Order order,  boolean val) {
        String id = order.getId();
        // 管理员在处理待支付订单时通常可能会数额上的更改，直接删除在插入会更好些
        orderMapper.deleteOrderDish(id);
        for (Map<String, Object> dishOrder : order.getDishOrders()) {
            orderMapper.insertOrderDish(id, dishOrder);
        }
        //设置是否可支付
        int row = orderMapper.setPaymentStatus(id, val ? 1 : 0);
        if (row == 0) {
            orderMapper.insertPaymentStatus(id, val ? 1: 0);
        }
        //管理员可更新其他费用
        orderMapper.updateOtherFee(id, order.getOtherFee());
        //管理员可更新折扣费用
        orderMapper.updateShopDiscount(id, order.getShopDiscount());
        orderMapper.updateShopOriginalPrice(id, order.getOriginalPrice());
    }

    @Override
    public void sendMessage(Message message) {
        if (message.getCreateTime() == null) {
            message.setCreateTime(new Timestamp(System.currentTimeMillis()));
        }
        messageMapper.insertMessage(message);
    }

    @Override
    public int getSize() {
        return orderMapper.getSize();
    }
}
