package com.happysnaker.service;

import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Store;
import com.happysnaker.pojo.StoreTable;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
public interface StoreService {

    Store getStore(int id);

    int getStoreSize();
    /**
     * 获取店铺列表
     * @return
     */
    List<Store> getStoreList();

    /**
     * 添加店铺
     * @param stroe
     */
    void addStore(Store store);

    /**
     * 更新店铺
     * @param store
     */
    void updateStore(Store store);

    /**
     * 删除店铺
     * @param id
     */
    void deleteStore(int id);

    default void deleteStore(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteStore(id);
        }
    };

    /**
     * 更新店铺状态
     * @param id 店铺iD
     * @param val 状态值，0 代表关闭，其他代表开启
     * @param options 1 代表是否启用，2 代表是否支持外卖
     */
    void updateStoreStatus(int id, int val, int options);

    /**
     * 更新店铺状态
     * @param id 店铺iD
     * @param val 状态值，0 代表关闭，其他代表开启
     * @param options 1 代表是否启用，2 代表是否支持外卖
     */
    default  void updateStoreStatus(List<Integer> ids, int val, int options) {
        for (Integer id : ids) {
            this.updateStoreStatus(id, val, options);
        }
    };

    void updateStoreTable(StoreTable st) throws UpdateException;

    List<Store> getStoreListByPagination(int pageNum, int pageSize, String keyword, Integer publishStatus);
}
