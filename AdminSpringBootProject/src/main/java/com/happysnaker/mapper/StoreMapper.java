package com.happysnaker.mapper;

import com.happysnaker.pojo.Store;
import com.happysnaker.pojo.StoreTable;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface StoreMapper {
    @Select("SELECT count(*) FROM `sms_store`;")
    int getStoreSize();

    @Select("SELECT * FROM `sms_store_table` WHERE store_id = #{id}")
    List<StoreTable> getStoreTables(int id);

    /**
     * 无聊的冲服造轮子
     * @param pageNum
     * @param pageSize
     * @param keyword
     * @param publishStatus
     * @return
     */
    List<Store> queryStoreByPagination(@Param("offset") int offset, @Param("num") int num, @Param("keyword") String keyword, @Param("status") Integer status);

    @Select("SELECT id FROM `sms_store` WHERE name = #{name}")
    int getIdByName(String name);


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

    /**
     * 插入店铺
     * @param store
     * @return
     */
    int insertStore(Store store);

    /**
     * 插入店铺桌位
     * @param st
     * @return
     */
    @Insert("INSERT INTO `sms_store_table` SELECT " +
            "#{storeId}, #{table}, #{occupationStatus}, #{capacity}")
    int insertStoreTable(StoreTable st);

    @Delete("DELETE FROM `sms_store` WHERE id = #{id}")
    int deleteStore(int id);

    @Delete("DELETE FROM `sms_store_table` WHERE store_id = #{id}")
    int deleteStoreTable(int id);


    @Update("UPDATE `sms_store_table` SET capacity = #{capacity} " +
            "WHERE store_id = #{storeId} AND table = #{table}")
    int updateStoreTable(StoreTable st);
}
