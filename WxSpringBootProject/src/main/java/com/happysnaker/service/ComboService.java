package com.happysnaker.service;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
public interface ComboService {
    /**
     * 获取所有的套餐信息，该信息展现在小程序首页
     * @return 将所有的Combo类封装成JSON字符串
     */
    String getIndexComboInfo();

    /**
     * 获取所有的套餐信息，该信息展现在小程序订餐页面
     * @param storeId 店铺ID
     * @return 将所有的Combo类封装成JSON字符串
     */
    String getOrderingComboInfo(int storeId);


}
