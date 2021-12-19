package com.happysnaker.configuration;

import com.happysnaker.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 这个类负责初始化缓存，以及维护一系列 key 以及持久化定时任务
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
@Component
public class RedisCacheManager {
    @Qualifier("myRedis")
    @Autowired
    private RedisTemplate redis;
    /**前缀 A 表示这属于小程序端的 key*/
    public static final String INDEX_DISH_INFO_CACHE_KEY = "A-redis-kv:indexDishInfo-key";
    public static final String STORE_CACHE_KEY = "A-redis-kv:stores-key";

    public static final String COMBO_PUBLISH_STATUS_KEY = "B-redis-bitmap:combo-publish-key";
    public static final String DISH_PUBLISH_STATUS_KEY = "B-redis-bitmap:dish-publish-key";
    public static final String DISH_RECOMMEND_STATUS_KEY = "B-redis-bitmap:dish-recom-key";
    public static final String DISH_NEW_STATUS_KEY = "B-redis-bitmap:dish-new-key";
    public static final String STORE_WORKING_STATUS_KEY = "B-redis-bitmap:store-enable-key";
    public static final String STORE_SUP_TAKEOUT_STATUS_KEY = "B-redis-bitmap:store-tot-key";
    public static final String USER_ENABLE_STATUS_KEY = "B-redis-bitmap:user-status-key";
    public static final String USER_LOGIN_STATUS_KEY = "B-redis-bitmap:user-login-status-key";

    /**默认的超时时间将保证在任意两次定时任务间隔内，缓存不会丢失，这样可以确保数据能够即时地更新到数据库中而不会丢失，使用者自定义过期时间时应考虑到缓存失效状态，过期时间应长于定时任务的间隔，或者可以自定义函数缓存，设置无过期时间也是可以的，设置缓存时间是担心服务器停止服务而忘记清除缓存，此时 Redis 服务器上将存在大量冗余信息，抑或是防止 Redis 崩溃而丢失大量信息，这里偷个懒，设置过期时间 3 年*/
    public static final long AUTH_DEFAULT_EXPIRATION_SECONDS = 60 * 60 * 24 * 1024;
    public static final long DISH_STATUS_DEFAULT_EXPIRATION_SECONDS = 60 * 60 * 24 * 1024;
    public static final long STORE_STATUS_DEFAULT_EXPIRATION_SECONDS = 60 * 60 * 24 * 1024;
    public static final long USER_STATUS_DEFAULT_EXPIRATION_SECONDS = 60 * 60 * 24 * 1024;
    public static final long ALL_DISH_COMBO_ID_EXPIRATION_SECONDS = 60 * 60 * 24 * 1024;



    public boolean isKeyExpired(String key) {
        return redis.opsForValue().getOperations().getExpire(key) <= 0;
    }

    public boolean hasKey(String key) {
        return redis.hasKey(key) && !isKeyExpired(key);
    }

    public boolean getBit(String key, long offset) {
        if (!hasKey(key)) {
            throw new RuntimeException("不存在 key " + key);
        }
        return redis.opsForValue().getBit(key, offset);
    }

    public int getBit(String key, int offset) {
        if (!hasKey(key)) {
            throw new RuntimeException("不存在 key " + key);
        }
        return redis.opsForValue().getBit(key, offset) ? 1 : 0;
    }

    public void setBit(String key, long offset, boolean val) {
        redis.opsForValue().setBit(key, offset, val);
    }

    public void setBit(String key, long offset, int val) {
        redis.opsForValue().setBit(key, offset, val != 0);
    }

    public static String getRoleApiListCacheKey(String role) {
        return "B-redis-list:role-api-key?role=" + role;
    }

    public static String getRoleStoreListCacheKey(String role) {
        return "B-redis-list:role-store-key?role=" + role;
    }

    public void initRedisApiCache(String role, List<ApiTable> roleApis, long timeout, TimeUnit unit) {
        for (ApiTable roleApi : roleApis) {
            redis.opsForList().rightPush(getRoleApiListCacheKey(role), roleApi.getUri());
        }
        redis.expire(getRoleApiListCacheKey(role), timeout, unit);
    }

    public void initRedisStoreCache(String role, List<Integer> roleStores, long timeout, TimeUnit unit) {
        for (Integer id : roleStores) {
            redis.opsForList().rightPush(getRoleStoreListCacheKey(role), id);
        }
        redis.expire(getRoleStoreListCacheKey(role), timeout, unit);
    }

    public void initRedisApiCache(String role, List<ApiTable> roleApis) {
        this.initRedisApiCache(role, roleApis, AUTH_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public void initRedisStoreCache(String role, List<Integer> roleStores) {
        this.initRedisStoreCache(role, roleStores, AUTH_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public void initRedisDishPublishStatusCache(List<Dish> dishes, long timeout, TimeUnit unit) {
        for (Dish dish : dishes) {
            System.out.println(dish);
            redis.opsForValue().setBit(DISH_PUBLISH_STATUS_KEY, dish.getId(), dish.getPublishStatus() != 0);
        }
        redis.expire(DISH_PUBLISH_STATUS_KEY, timeout, unit);
    }

    public void initRedisDishPublishStatusCache(List<Dish> dishes) {
        this.initRedisDishPublishStatusCache(dishes, DISH_STATUS_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public void initRedisDishRecommendStatusCache(List<Dish> dishes, long timeout, TimeUnit unit) {
        for (Dish dish : dishes) {
            redis.opsForValue().setBit(DISH_RECOMMEND_STATUS_KEY, dish.getId(), dish.getRecommendStatus() != 0);
        }
        redis.expire(DISH_RECOMMEND_STATUS_KEY, timeout, unit);
    }

    public void initRedisDishRecommendStatusCache(List<Dish> dishes) {
        this.initRedisDishRecommendStatusCache( dishes, DISH_STATUS_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public void initRedisDishNewStatusCache(List<Dish> dishes, long timeout, TimeUnit unit) {
        for (Dish dish : dishes) {
            redis.opsForValue().setBit(DISH_NEW_STATUS_KEY, dish.getId(), dish.getNewStatus()!= 0);
        }
        redis.expire(DISH_NEW_STATUS_KEY, timeout, unit);
    }

    public void initRedisDishNewStatusCache(List<Dish> dishes) {
        this.initRedisDishNewStatusCache(dishes, DISH_STATUS_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public void initRedisComboPublishStatusCache( List<Combo> combos, long timeout, TimeUnit unit) {
        for (Combo combo : combos) {
            redis.opsForValue().setBit(COMBO_PUBLISH_STATUS_KEY, combo.getId(), combo.getPublishStatus() != 0);
        }
        redis.expire(COMBO_PUBLISH_STATUS_KEY, timeout, unit);
    }

    public void initRedisComboPublishStatusCache(List<Combo> combos) {
        this.initRedisComboPublishStatusCache(combos, DISH_STATUS_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public void initRedisStoreWorkingStatusCache(List<Store> stores, long timeout, TimeUnit unit) {
        for (Store store : stores) {
            redis.opsForValue().set(STORE_WORKING_STATUS_KEY, store.getId(), store.getWorking());
        }
        redis.expire(STORE_WORKING_STATUS_KEY, timeout, unit);
    }

    public void initRedisStoreWorkingStatusCache(List<Store> stores) {
        this.initRedisStoreWorkingStatusCache(stores, STORE_STATUS_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public void initRedisStoreSupportTakeoutStatusCache(List<Store> stores, long timeout, TimeUnit unit) {
        for (Store store : stores) {
            this.setBit(STORE_SUP_TAKEOUT_STATUS_KEY, store.getId(), store.getSupportTakeout());
        }
        redis.expire(STORE_SUP_TAKEOUT_STATUS_KEY, timeout, unit);
    }

    public void initRedisStoreSupportTakeoutStatusCache(List<Store> stores) {
        this.initRedisStoreSupportTakeoutStatusCache(stores, STORE_STATUS_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public void initRedisUserEnableStatusCache(List<User> users, long timeout, TimeUnit unit) {
        for (User user : users) {
            this.setBit(USER_ENABLE_STATUS_KEY, user.getId(), user.getEnable());
        }
        redis.expire(USER_ENABLE_STATUS_KEY, timeout, unit);
    }

    public void initRedisUserEnableStatusCache(List<User> users) {
        this.initRedisUserEnableStatusCache(users, USER_STATUS_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public void initRedisUserLoginStatusCache(List<User> users, long timeout, TimeUnit unit) {
        for (User user : users) {
            this.setBit(USER_LOGIN_STATUS_KEY, user.getId(), user.getLoginStatus());
        }
        redis.expire(USER_LOGIN_STATUS_KEY, timeout, unit);
    }

    public void initRedisUserLoginStatusCache(List<User> users) {
        this.initRedisUserLoginStatusCache(users, USER_STATUS_DEFAULT_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }
}
