package com.happysnaker.service.impl;

import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Address;
import com.happysnaker.service.AddressService;
import com.happysnaker.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/30
 * @email happysnaker@foxmail.com
 */
@Service
public class AddressServiceImpl extends BaseService implements AddressService {
    @Override
    public List<Address> getUserAddress(String userId) {
        return addressMapper.getUserAddress(userId);
    }

    @Override
    public void removeUserAddress(int addrId) throws UpdateException {
       int row = addressMapper.removeUserAddress(addrId);
       if (row == 0) {
           throw  new UpdateException("移除地址ID为 " + addrId + " 的数据失败");
       }
    }

    @Override
    public void addUserAddress(Address address) throws UpdateException {
        int row = addressMapper.addUserAddress(address);
        if (row == 0) {
            throw  new UpdateException("增加地址失败，数据库中可能存在该地址: "+ address);
        }
    }

    @Override
    public void updateUserAddress(Address address) throws UpdateException {
        int row = addressMapper.updateUserAddress(address);
        if (row == 0) {
            throw  new UpdateException("更新地址失败，数据库中可能不存在该地址: " +address);
        }
    }
}
