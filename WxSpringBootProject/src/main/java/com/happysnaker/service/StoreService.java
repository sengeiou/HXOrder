package com.happysnaker.service;

import com.happysnaker.pojo.Store;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
public interface StoreService {
    /**
     * 获取所有的门店信息，并以JSON形式返回
     * @return JSON字符串
     */
    String getStoreInfo();

    /**
     * 获取用户收藏的店铺
     * @param userId 用户ID
     * @return List To JSON
     */
    List<Integer> getCollectedStore(String userId);

    /**
     * 通过ID获取店铺
     * @param id
     * @return
     */
    Store getStoreById(int id);
}
