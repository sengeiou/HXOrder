package com.happysnaker.mapper;

import com.happysnaker.pojo.Address;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/30
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface AddressMapper {

    /**
     * 获取用户的收货地址
     * @param userId 用户ID
     * @return JSON-ADDRESS
     */
    @Select("SELECT * FROM ums_address WHERE user_id = #{userId}")
    public List<Address> getUserAddress(String userId);

    /**
     * 移除用户的收货地址
     * @param addrId 地址编号
     * @return 返回影响的 rows
     */
    @Delete("DELETE FROM ums_address WHERE id = #{addrId}")
    public int removeUserAddress(@Param("addrId") int addrId);

    /**
     * 添加用户的收货地址
     * @param address 地址
     * @return 返回影响的 rows
     */
    @Insert("INSERT INTO ums_address(name, phone, address, user_id)" +
            " SELECT #{name}, #{phone}, #{address}, #{userId}")
    public int addUserAddress(Address address);

    /**
     * 更新用户的收货地址
     * @param address 地址
     * @return 返回影响的 rows
     */
    @Update("UPDATE ums_address SET name = #{name}, phone = #{phone}, " +
            "address =  #{address}, user_id = #{userId}")
    public int updateUserAddress(Address address);
}
