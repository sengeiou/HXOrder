package com.happysnaker.service;

import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Address;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/30
 * @email happysnaker@foxmail.com
 */
public interface AddressService {
    /**
     * 获取用户的收货地址
     * @param userId 用户ID
     * @return JSON-ADDRESS
     */
    public List<Address> getUserAddress(String userId);

    /**
     * 移除用户的收货地址
     * @param addrId 地址
     * @return 返回影响的 rows
     */
    public void removeUserAddress(int addrId) throws UpdateException;

    /**
     * 添加用户的收货地址
     * @param address 地址
     * @return 返回影响的 rows
     */
    public void addUserAddress(Address address) throws UpdateException;

    /**
     * 更新用户的收货地址
     * @param address 地址
     * @return 返回影响的 rows
     */
    public void updateUserAddress(Address address) throws UpdateException;
}
