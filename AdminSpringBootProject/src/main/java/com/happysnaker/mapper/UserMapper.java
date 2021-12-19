package com.happysnaker.mapper;

import com.happysnaker.pojo.User;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/3
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM `ums_admin` WHERE username = #{username}")
    User getUser(@Param("username") String username);


    @Select("SELECT * FROM `ums_admin`")
    List<User> getUserList();

    List<User> queryUserByPagination(@Param("offset") int offset, @Param("num") int num, @Param("keyword") String keyword);

    @Delete("DELETE FROM `ums_admin` WHERE id = #{id}")
    int deleteUser(int id);

    int insertUser(User user);

    @Update("UPDATE `ums_admin` SET last_login_time = #{ts} WHERE username = #{username}")
    int log(@Param("username") String username,@Param("ts") Timestamp ts);

    @Select("SELECT count(*) FROM `ums_admin`")
    int getSize();
}
