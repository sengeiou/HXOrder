package com.happysnaker.mapper;

import com.happysnaker.pojo.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/1
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface MessageMapper {
    /**
     * 插入信息
     * @param message
     * @return
     */
    @Insert("INSERT INTO `ums_message`(`id`,`title`,`content`,`type`,`store_id`,`user_id`,`create_time`) SELECT #{id},#{title},#{content},#{type},#{storeId},#{userId},#{createTime}")
    int insertMessage(Message message);


    /**
     * 获取用户未读的消息数目
     * @param userId 用户ID
     * @param ts 当前时间戳
     * @return 消息数目
     */
    @Select("SELECT num FROM `ums_user_msg_num` WHERE user_id = #{userId}")
    int queryUnReadUserMsgCount(String userId);

    /**
     * 更新用户未读消息
     * @param userId
     * @param delta
     * @return
     */
    @Update("UPDATE `ums_user_msg_num` SET num = num + #{val} WHERE user_id = #{id}")
    int updateUnReadUserMsgCount(@Param("id") String userId, @Param("val") int delta);

    /**
     * 获取用户的消息
     * @param userId 用户ID
     * @return 消息
     */
    @Select("SELECT * FROM `ums_message` WHERE user_id = #{userId}")
    List<Message> queryMessage(String userId);


    /**
     * 插入用户未读消息
     * @param userId
     * @param val
     * @return
     */
    @Update("INSERT `ums_user_msg_num` SELECT #{id}, #{val}")
    int insertUnReadUserMsgCount(@Param("id") String userId, @Param("val") int val);
}
