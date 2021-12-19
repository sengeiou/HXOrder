package com.happysnaker.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.api.BaiduApi;
import com.happysnaker.config.RedisCacheManager;
import com.happysnaker.pojo.Store;
import com.happysnaker.service.BaseService;
import com.happysnaker.service.StoreService;
import com.happysnaker.utils.JsonUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Service
public class StoreServiceImpl extends BaseService implements StoreService {

    @Override
    public String getStoreInfo() {
        // 热数据可以使用缓存
        if (redisManager.hasKey(RedisCacheManager.STORE_CACHE_KEY)) {
            return (String) redisManager.getForValue(RedisCacheManager.STORE_CACHE_KEY);
        }
        // 同步店铺启用状态以及支持外卖状态
        List<Store> stores = storeMapper.getStoreInfo().stream().filter(store-> {
            return !redisManager.hasKey(RedisCacheManager.STORE_WORKING_STATUS_KEY)
                            || redisManager.getBit(RedisCacheManager.STORE_WORKING_STATUS_KEY, (long) store.getId());
        }).map(store-> {
            if (redisManager.hasKey(RedisCacheManager.STORE_SUP_TAKEOUT_STATUS_KEY)) {
                store.setSupportTakeout(redisManager.getBit(RedisCacheManager.STORE_SUP_TAKEOUT_STATUS_KEY, store.getId()));
            }
            return store;
        }).collect(Collectors.toList());
        for (Store store : stores) {
            String[] addrs = BaiduApi.getLatitudeAndLongitude(store.getAddress());
            store.setLongitude(addrs[0]);
            store.setLatitude(addrs[1]);
        }
        String ans = JsonUtils.listAddToJsonObject(new JSONObject(), stores).toJSONString();
        redisManager.addIfAbsentForValue(RedisCacheManager.STORE_CACHE_KEY, ans);
        return ans;
    }

    @Override
    public List<Integer> getCollectedStore(String userId) {
        //以缓存中的信息为主
        try {
            return storeMapper.queryAllStoreId().stream().filter((id)-> {
                return redisManager.getBit(RedisCacheManager.getUserCollectedStoreCacheKey(userId), (long)id);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            // 缓存可能不存在
            redisManager.initRedisUserMarkedCache(storeMapper.queryAllStoreId(), userMapper.queryCollectedStore(userId), RedisCacheManager.getUserCollectedStoreCacheKey(userId));
            return getCollectedStore(userId);
        }
    }

    @Override
    public Store getStoreById(int id) {
        return storeMapper.getStore(id);
    }


}
