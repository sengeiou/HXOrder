package com.happysnaker.service.impl;

import com.happysnaker.configuration.RedisCacheManager;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Store;
import com.happysnaker.pojo.StoreTable;
import com.happysnaker.service.StoreService;
import com.happysnaker.service.base.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StoreServiceImpl extends BaseService implements StoreService {
    private List<Store> getStatusFromRedis(List<Store> stores) {
        if (!redisUtils.hasKey(RedisCacheManager.STORE_WORKING_STATUS_KEY)) {
            System.out.println("缓存过期！！！");
            redisUtils.initRedisStoreWorkingStatusCache(storeMapper.getStoreInfo());
        }
        if (!redisUtils.hasKey(RedisCacheManager.STORE_SUP_TAKEOUT_STATUS_KEY)) {
            redisUtils.initRedisStoreSupportTakeoutStatusCache(storeMapper.getStoreInfo());
        }
        try {
            for (Store store : stores) {
                store.setWorking(redisUtils.getBit(RedisCacheManager.STORE_WORKING_STATUS_KEY, store.getId()));
                store.setSupportTakeout(redisUtils.getBit(RedisCacheManager.STORE_SUP_TAKEOUT_STATUS_KEY, store.getId()));
            }
            return stores;
        } catch (Exception e) {
            System.out.println("重新尝试");
            e.printStackTrace();
            //重新尝试
            return getStatusFromRedis(stores);
        }
    }

    @Override
    public Store getStore(int id) {
        Store store = storeMapper.getStore(id);
        store.setTables(storeMapper.getStoreTables(id));
        return store;
    }

    @Override
    public int getStoreSize() {
        return storeMapper.getStoreSize();
    }

    @Override
    public List<Store> getStoreList() {
        return getStatusFromRedis(storeMapper.getStoreInfo());
    }

    @Override
    public void addStore(Store store) {
        storeMapper.insertStore(store);
        int id = storeMapper.getIdByName(store.getName());
        for (StoreTable table : store.getTables()) {
            table.setStoreId(id);
            storeMapper.insertStoreTable(table);
        }
        redis.delete(RedisCacheManager.STORE_WORKING_STATUS_KEY);
        redis.delete(RedisCacheManager.STORE_SUP_TAKEOUT_STATUS_KEY);
    }

    @Override
    public void updateStore(Store store) {
        deleteStore(store.getId());
        addStore(store);
    }

    @Override
    public void deleteStore(int id) {
        storeMapper.deleteStore(id);
        storeMapper.deleteStoreTable(id);
    }

    @Override
    public void updateStoreStatus(int id, int val, int options) {
        if (options == 1) {
            redisUtils.setBit(RedisCacheManager.STORE_WORKING_STATUS_KEY, id, val);
        } else if (options == 2) {
            redisUtils.setBit(RedisCacheManager.STORE_SUP_TAKEOUT_STATUS_KEY, id, val);
        }
    }

    @Override
    public void updateStoreTable(StoreTable st) throws UpdateException {
        int row = storeMapper.updateStoreTable(st);
        if (row == 0) {
            throw new UpdateException("数据库中不存在该 storeTable: " + st);
        }
    }

    @Override
    public List<Store> getStoreListByPagination(int pageNum, int pageSize, String keyword, Integer status) {
        int offset = (pageNum - 1)* pageSize;
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }

        //菜品状态以缓存中为主，因此我们不从数据库中读 status
        List<Store> stores = getStatusFromRedis(storeMapper.queryStoreByPagination(offset, pageSize, keyword, null));

        //如果用户确实查询了营业状态，则根据缓存中营业状态进行过滤
        if (status != null) {
            stores = stores.stream().filter((store)-> {
                return redisUtils.getBit(
                        RedisCacheManager.STORE_WORKING_STATUS_KEY,
                        store.getId()
                ) == status;
            }).collect(Collectors.toList());
        }

        //更新店铺的其他状态
        System.out.println("stores 1" + stores.get(0).getWorking());
        return getStatusFromRedis(stores);
    }
}
