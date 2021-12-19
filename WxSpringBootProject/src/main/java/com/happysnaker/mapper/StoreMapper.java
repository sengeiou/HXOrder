package com.happysnaker.mapper;



import com.happysnaker.pojo.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface StoreMapper {
    @Select("SELECT id FROM sms_store")
    List<Integer> queryAllStoreId();

    /**
     * 查询 store 表中的所有信息
     * @return List&lt;Store&gt;
     */
    List<Store> getStoreInfo();

    /**
     * 获取店铺信息
     * @param storeId 店铺ID
     * @return STORE
     */
    Store getStore(@Param("storeId") int storeId);
}
