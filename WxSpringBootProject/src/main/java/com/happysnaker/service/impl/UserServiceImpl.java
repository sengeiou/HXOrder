package com.happysnaker.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.config.RedisCacheManager;
import com.happysnaker.mapper.DishMapper;
import com.happysnaker.mapper.UserMapper;
import com.happysnaker.service.BaseService;
import com.happysnaker.service.UserService;
import com.happysnaker.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {



    @Autowired
    public UserServiceImpl(UserMapper userMapper, DishMapper dishMapper) {
        this.userMapper = userMapper;
    }

    private void initializedRedisCache(String userId) {
        List<Integer> dishIds = dishMapper.queryAllDishId();
        List<Integer> storeIds = storeMapper.queryAllStoreId();
        //尝试更新收藏店铺
        if (!redisManager.hasKey(RedisCacheManager.getUserCollectedStoreCacheKey(userId))) {
            redisManager.initRedisUserMarkedCache(storeIds, userMapper.queryCollectedStore(userId), RedisCacheManager.getUserCollectedStoreCacheKey(userId));
        }
        //尝试更新收藏菜品
        if (!redisManager.hasKey(RedisCacheManager.getUserCollectedDishCacheKey(userId))) {
            redisManager.initRedisUserMarkedCache(dishIds, userMapper.queryCollectedDish(userId), RedisCacheManager.getUserCollectedDishCacheKey(userId));
        }
        //尝试更新喜欢菜品
        if (!redisManager.hasKey(RedisCacheManager.getUserLikeDishCacheKey(userId))) {
            redisManager.initRedisUserMarkedCache(dishIds, userMapper.queryFavoriteDish(userId), RedisCacheManager.getUserLikeDishCacheKey(userId));
        }
        //尝试更新用户待选菜品
        if (!redisManager.hasKey(RedisCacheManager.getUserWillBuyDishCacheKey(userId))) {
            redisManager.initRedisUserMarkedCache(dishIds, userMapper.queryWillBuyDish(userId), RedisCacheManager.getUserWillBuyDishCacheKey(userId));
        }
    }

    private List<Integer> getMarkedFromCache(String cacheKey, String idKey) {
        List<Integer> ret = new ArrayList<>();
        List<Integer> ids = idKey == ALL_DISH_ID ? dishMapper.queryAllDishId() : storeMapper.queryAllStoreId();
        for (int id : ids) {
            if (redis.opsForValue().getBit(cacheKey, id)) {
                ret.add( id);
            }
        }
        return ret;
    }

    @Override
    public String getUserInfo(String userId) {
        return new JSONObject().fluentPut("user", userMapper.queryUser(userId)).toJSONString();
    }

    @Override
    public String getUserMarkedDish(String userId) {
        try {
            List<Integer> list1 = getMarkedFromCache(RedisCacheManager.getUserLikeDishCacheKey(userId), ALL_DISH_ID);
            List<Integer> list2 = getMarkedFromCache(RedisCacheManager.getUserCollectedDishCacheKey(userId), ALL_DISH_ID);
            List<Integer> list3 = getMarkedFromCache(RedisCacheManager.getUserWillBuyDishCacheKey(userId), ALL_DISH_ID);

            JSONObject object = new JSONObject();
            JsonUtils.listAddToJsonObject(object, list1, "like");
            JsonUtils.listAddToJsonObject(object, list2, "collection");
            JsonUtils.listAddToJsonObject(object, list3, "willBuy");
            return object.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            // 缓存中可能无数据
            initializedRedisCache(userId);
            return getUserMarkedDish(userId);
        }
    }

    @Override
    public String getCollectedStores(String userId) {
        //如果 REDIS 位图中没有用户喜欢、收藏、待选等数据，则初始化 REDIS 缓存
        if (!redis.hasKey(RedisCacheManager.getUserLikeDishCacheKey(userId))) {
            initializedRedisCache(userId);
        }
        return JsonUtils.listAddToJsonObject(new JSONObject(), getMarkedFromCache(RedisCacheManager.getUserCollectedStoreCacheKey(userId), ALL_STORE_ID)).toJSONString();
    }

    @Override
    public String getUsedDiscountCount(String userId) {
        Map<Integer, Map> map = userMapper.queryUserUsedDiscountCountInAllDish(userId);
        JSONObject jsonObject = new JSONObject();
        for (var it : map.entrySet()) {
            jsonObject.put(String.valueOf(it.getKey()), it.getValue());
        }
        return jsonObject.toJSONString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addUserLikeDish(String userId, int dishId) {
        System.out.println("dishId = " + dishId);
        redis.opsForValue().setBit(RedisCacheManager.getUserLikeDishCacheKey(userId), dishId, true);
        return "OK";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addUserCollectedDish(String userId, int dishId) {
        redis.opsForValue().setBit(RedisCacheManager.getUserCollectedDishCacheKey(userId), dishId, true);
        return "OK";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addUserWillBuyDish(String userId, int dishId) {
        System.out.println(RedisCacheManager.getUserWillBuyDishCacheKey(userId) + "--" + dishId);
        redis.opsForValue().setBit(RedisCacheManager.getUserWillBuyDishCacheKey(userId), dishId, true);
        return "OK";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addUserCollectedStore(String userId, int storeId) {
        System.out.println(RedisCacheManager.getUserWillBuyDishCacheKey(userId) + "--" + storeId);
        redis.opsForValue().setBit(RedisCacheManager.getUserCollectedStoreCacheKey(userId), storeId, true);
        return "OK";
    }

    @Override
    public String removeUserLikeDish(String userId, int dishId) {
        redis.opsForValue().setBit(RedisCacheManager.getUserLikeDishCacheKey(userId), dishId, false);
        return "OK";
    }

    @Override
    public String removeUserCollectedDish(String userId, int dishId) {
        redis.opsForValue().setBit(RedisCacheManager.getUserCollectedDishCacheKey(userId), dishId, false);
        return "OK";
    }

    @Override
    public String removeUserWillBuyDish(String userId, int dishId) {
        redis.opsForValue().setBit(RedisCacheManager.getUserWillBuyDishCacheKey(userId), dishId, false);
        return "OK";
    }

    @Override
    public String removeUserCollectedStore(String userId, int storeId) {
        redis.opsForValue().setBit(RedisCacheManager.getUserCollectedStoreCacheKey(userId), storeId, false);
        return "OK";
    }
}
