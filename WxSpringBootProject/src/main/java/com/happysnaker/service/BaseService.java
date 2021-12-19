package com.happysnaker.service;

import com.happysnaker.config.RedisCacheManager;
import com.happysnaker.mapper.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/26
 * @email happysnaker@foxmail.com
 */
@Data
public class BaseService implements Serializable {
    protected final String ALL_DISH_ID = "allDishId";
    protected final String ALL_STORE_ID = "allStoreId";


    @Qualifier("myRedisTemplate")
    @Autowired
    protected RedisTemplate redis;

    @Autowired
    protected RedisCacheManager redisManager;

    @Autowired
    protected MessageMapper messageMapper;
    @Autowired
    protected AddressMapper addressMapper;
    @Autowired
    protected ComboMapper comboMapper;
    @Autowired
    protected DiscountMapper discountMapper;
    @Autowired
    protected DishMapper dishMapper;
    @Autowired
    protected OrderMapper orderMapper;
    @Autowired
    protected SearchMapper searchMapper;
    @Autowired
    protected StoreMapper storeMapper;
    @Autowired
    protected UserMapper userMapper;


}
