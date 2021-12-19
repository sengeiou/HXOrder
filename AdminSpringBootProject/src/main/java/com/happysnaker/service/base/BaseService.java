package com.happysnaker.service.base;

import com.happysnaker.configuration.RedisCacheManager;
import com.happysnaker.mapper.*;
import com.happysnaker.service.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
public class BaseService implements Serializable {
    @Qualifier("myRedis")
    @Autowired
    protected RedisTemplate redis;

    @Autowired
    protected RedisCacheManager redisUtils;

    @Autowired
    protected AuthMapper authMapper;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected DishMapper dishMapper;

    @Autowired
    protected OrderMapper orderMapper;

    @Autowired
    protected StoreMapper storeMapper;

    @Autowired
    protected DiscountMapper discountMapper;

    @Autowired
    protected ComboMapper comboMapper;

    @Autowired
    protected MessageMapper messageMapper;

    protected Map<Integer, List<Observer>> observers;

    protected void registerObserver(Observer observer, int type) {
        if (observers == null) {
            observers = new HashMap<>(16);
        }
        observers.putIfAbsent(type, new ArrayList<>());
        observers.get(type).add(observer);
    }

    protected void notify(int type, Object... args) {
        for (Observer observer : observers.getOrDefault(type, new ArrayList<>())) {
            observer.acton(this, args);
        }
    }
}
