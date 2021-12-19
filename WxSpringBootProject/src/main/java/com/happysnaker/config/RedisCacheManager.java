package com.happysnaker.config;

import com.happysnaker.mapper.DiscountMapper;
import com.happysnaker.mapper.DishMapper;
import com.happysnaker.mapper.StoreMapper;
import com.happysnaker.mapper.UserMapper;
import com.happysnaker.pojo.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 */
@Component
class ScheduledTask {
    @Autowired
    UserMapper userMapper;

    @Autowired
    DishMapper dishMapper;

    @Autowired
    StoreMapper storeMapper;

    @Autowired
    DiscountMapper discountMapper;


    @Qualifier("myRedisTemplate")
    @Autowired
    RedisTemplate redis;




    @Scheduled(cron = "0 0 3,15 * * ?")
    public void doTask1() {

        for (String userId : userMapper.queryAllUserIds()) {
            List<Integer> store = userMapper.queryCollectedStore(userId);
            List<Integer> collectedDish = userMapper.queryCollectedDish(userId);
            List<Integer> favoriteDish = userMapper.queryFavoriteDish(userId);
            List<Integer> willBuyDish = userMapper.queryWillBuyDish(userId);
            boolean b1 = redis.hasKey(RedisCacheManager.getUserLikeDishCacheKey(userId));
            boolean b2 = redis.hasKey(RedisCacheManager.getUserCollectedDishCacheKey(userId));
            boolean b3 = redis.hasKey(RedisCacheManager.getUserWillBuyDishCacheKey(userId));
            boolean b4 = redis.hasKey(RedisCacheManager.getUserCollectedStoreCacheKey(userId));
            if (b1 || b2 || b3) {
                for (Integer id : dishMapper.queryAllDishId()) {
                    if (b1) {
                        boolean val1 = redis.opsForValue().getBit(RedisCacheManager.getUserLikeDishCacheKey(userId), id);
                        if (collectedDish.indexOf(id) == -1 && val1) {
                            userMapper.addCollectedDish(userId, id);
                        } else if (collectedDish.indexOf(id) != -1 && !val1) {
                            userMapper.removeCollectedDish(userId, id);
                        }
                    }
                    if (b2) {
                        boolean val2 = redis.opsForValue().getBit(RedisCacheManager.getUserCollectedDishCacheKey(userId), id);
                        if (favoriteDish.indexOf(id) == -1 && val2) {
                            userMapper.addFavoriteDish(userId, id);
                        } else if (favoriteDish.indexOf(id) != -1 && !val2) {
                            userMapper.removeFavoriteDish(userId, id);
                        }
                    }
                    if (b3) {
                        boolean val3 = redis.opsForValue().getBit(RedisCacheManager.getUserWillBuyDishCacheKey(userId), id);
                        if (willBuyDish.indexOf(id) == -1 && val3) {
                            userMapper.addWillBuyDish(userId, id);
                        } else if (willBuyDish.indexOf(id) != -1 && !val3) {
                            userMapper.removeWillBuyDish(userId, id);
                        }
                    }
                }
            }
            if (b4) {
                for (Integer id : storeMapper.queryAllStoreId()) {
                    boolean val1 = redis.opsForValue().getBit(RedisCacheManager.getUserCollectedStoreCacheKey(userId), id);
                    if (store.indexOf(id) == -1 && val1) {
                        userMapper.addCollectedStore(userId, id);
                    } else if (store.indexOf(id) != -1 && !val1) {
                        userMapper.removeCollectedStore(userId, id);
                    }
                }
            }
            redis.delete(RedisCacheManager.getUserLikeDishCacheKey(userId));
            redis.delete(RedisCacheManager.getUserCollectedDishCacheKey(userId));
            redis.delete(RedisCacheManager.getUserWillBuyDishCacheKey(userId));
            redis.delete(RedisCacheManager.getUserCollectedStoreCacheKey(userId));
        }
    }

    // 11.55 定时写入今日销量，清除等待队列，更新用户每日折扣数目
    @Scheduled(cron = "0 55 23 * * ?")
    public void doTask2() {
        for (Integer id : storeMapper.queryAllStoreId()) {
            int num = 0;
            if (redis.hasKey(RedisCacheManager.getTodayDateKey())) {
                num = (int) redis.opsForHash().get(RedisCacheManager.getTodayDateKey(), id);
            }
            dishMapper.insertSaleLog(new Timestamp(RedisCacheManager.getTodayDate().getTime()), id, num);
        }

        redis.delete(RedisCacheManager.DISH_WAITING_QUEUE_KEY);

        for (String userId : userMapper.queryAllUserIds()) {
            Map<Integer, Map> map = userMapper.queryUserUsedDiscountCountInAllDish(userId);
            for (Map.Entry<Integer, Map> it : map.entrySet()) {
                int dishId = it.getKey();
                userMapper.updateUsedDiscountCountByANewVal(userId, dishId, 0);
            }
        }

    }
}

/**
 * REDIS缓存管理器，该类维护着一系列 redis 参数以及初始化任务和定时任务
 *
 * @author Happysnaker
 * @description
 * @date 2021/12/11
 * @email happysnaker@foxmail.com
 */
@Component
public class RedisCacheManager {
    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redis;

//    @Qualifier("myRabbitTemplate")
//    @Autowired
//    RabbitTemplate rabbit;

    /**
     * 前缀 A 表示这属于小程序端的 key
     */
    public static final String INDEX_DISH_INFO_CACHE_KEY = "A-redis-kv:indexDishInfo-key";
    public static final String STORE_CACHE_KEY = "A-redis-kv:stores-key";
    public static final String DISH_LIKE_NUM_CACHE_KEY = "A-redis-kv:dish-like-num-key";
    public static final String DISH_MAKE_TIME_CACHE_KEY = "A-redis-hash:dish-make-time-key";
    public static final String DISH_WAITING_QUEUE_KEY = "A-redis-list:dish-queue-key";

    /**
     * 前缀 B 表示这属于后台的 key
     */
    public static final String COMBO_PUBLISH_STATUS_KEY = "B-redis-bitmap:combo-publish-key";
    public static final String DISH_PUBLISH_STATUS_KEY = "B-redis-bitmap:dish-publish-key";
    public static final String DISH_RECOMMEND_STATUS_KEY = "B-redis-bitmap:dish-recom-key";
    public static final String DISH_NEW_STATUS_KEY = "B-redis-bitmap:dish-new-key";
    public static final String STORE_WORKING_STATUS_KEY = "B-redis-bitmap:store-enable-key";
    public static final String STORE_SUP_TAKEOUT_STATUS_KEY = "B-redis-bitmap:store-tot-key";

    public static String getOrderMessageCacheKey(String mid) {
        return "A-redis-kv:order-msg-key?msgId=" + mid;
    }


    public static String getDishStockCacheKey(int storeId) {
        return "A-redis-hash:dish-stock-key?storeId=" + storeId;
    }

    public static String getIpCacheKey(String ip) {
        return "A-redis-kv:ip-cache?ip=" + ip;
    }

    public static String getTokenCacheKey(String token) {
        return "A-redis-kv:token-cache?tk=" + token;
    }

    public static String getUserLikeDishCacheKey(String userId) {
        return "A-redis-bitmap:user-like-dish-cache?uid=" + userId;
    }

    public static String getUserCollectedDishCacheKey(String userId) {
        return "A-redis-bitmap:user-collected-dish-cache?uid=" + userId;
    }

    public static String getUserWillBuyDishCacheKey(String userId) {
        return "A-redis-bitmap:user-will-buy-dish-cache?uid=" + userId;
    }

    public static String getUserCollectedStoreCacheKey(String userId) {
        return "A-redis-bitmap:user-collected-store-cache?uid=" + userId;
    }

    public static Date getTodayDate() {
        Date date = new Date(System.currentTimeMillis());
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    /**
     * 获取今日 0 点的 redisKey，存储每日的销量
     */
    public static String getTodayDateKey() {

        return "redis-kv:dish-sale-log?data=" + getTodayDate().toString();
    }

    private static final long DEFAULT_EXPIRATION_TIME_SECONDS = 60 * 60 * 24;

    public boolean isKeyExpired(String key) {
        return redis.opsForValue().getOperations().getExpire(key) <= 0;
    }

    public boolean hasKey(String key) {
        return key != null && redis.hasKey(key) && !isKeyExpired(key);
    }


    public boolean getBit(String key, long offset) {
        return redis.opsForValue().getBit(key, offset);
    }

    public int getBit(String key, int offset) {
        return redis.opsForValue().getBit(key, offset) ? 1 : 0;
    }

    public void setBit(String key, long offset, boolean val) {
        redis.opsForValue().setBit(key, offset, val);
    }

    public void setBit(String key, long offset, int val) {
        redis.opsForValue().setBit(key, offset, val != 0);
    }

    public void addIfAbsentForValue(String key, Object val, long timeout, TimeUnit unit) {
        if (!hasKey(key)) {
            redis.opsForValue().set(key, val);
            redis.expire(key, timeout, unit);
        }
    }

    public void addIfAbsentForValue(String key, Object val) {
        this.addIfAbsentForValue(key, val, DEFAULT_EXPIRATION_TIME_SECONDS, TimeUnit.SECONDS);
    }

    public Object getForValue(String key) {
        return redis.opsForValue().get(key);
    }

    public void setForHash(String key1, Object key2, Object val) {
        redis.opsForHash().put(key1, key2, val);
    }

    public Object getForHash(String key1, Object key2) {
        return redis.opsForHash().get(key1, key2);
    }

    /**
     * 初始化 Redis bitmap 缓存，过期时间将依赖于默认的过期时间
     *
     * @param allIds 所有的 ID
     * @param ids    将要设置为 true 的 id
     * @param key    键
     */
    public void initRedisUserMarkedCache(List<Integer> allIds, List<Integer> ids, String key) {
        redis.delete(key);
        for (Integer allId : allIds) {
            if (ids.contains(allId)) {
                setBit(key, allId, true);
            } else {
                setBit(key, allId, false);
            }
        }
        redis.expire(key, DEFAULT_EXPIRATION_TIME_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 初始化菜品制作时间缓存，将使用默认的过期时间
     *
     * @param dishes 菜品
     */
    public void initRedisDishMakeTimeCache(List<Dish> dishes) {
        redis.delete(DISH_MAKE_TIME_CACHE_KEY);
        for (Dish dish : dishes) {
            setForHash(DISH_MAKE_TIME_CACHE_KEY, dish.getId(), dish.getMakeTime());
        }
        redis.expire(DISH_MAKE_TIME_CACHE_KEY, DEFAULT_EXPIRATION_TIME_SECONDS, TimeUnit.SECONDS);
    }


    /**
     * 初始化菜品库存
     *
     * @param dishes
     */
    public synchronized void initRedisDishStockCache(List<Dish> dishes, int storeId) {
        if (hasKey(getDishStockCacheKey(storeId))) {
            return;
        }
//        Channel channel = rabbit.getConnectionFactory().createConnection().createChannel(false);
//        try {
//            channel.exchangeDeclare(OrderRabbitMqConfig.ORDER_EXCHANGE, "direct", true, false, null);
//            AMQP.Queue.DeclareOk declareOk = channel.queueDeclarePassive(OrderRabbitMqConfig.ORDER_ADD_DEAD_QUEUE);
//            while (declareOk.getMessageCount() != 0) {
//                System.out.println("消息队列非空，不安全，等待消费者为 0");
//                Thread.sleep(500);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        for (Dish dish : dishes) {
            System.out.println("gid" + dish.getId() + "gst" + dish.getStock());
            setForHash(getDishStockCacheKey(storeId), dish.getId(), dish.getStock());
        }
        redis.expire(getDishStockCacheKey(storeId), 60 * 30, TimeUnit.SECONDS);
    }


}
