package com.happysnaker.service;

import com.happysnaker.pojo.Message;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/1
 * @email happysnaker@foxmail.com
 */
public interface MessageService {
    /**获取用户消息数目，
     * 这个函数将直接返回 JSON 字符串，key 是 msgCount，val是消息数目*/
    String queryUserMsgCount(String userId, Timestamp ts);

    /**获取用户具体消息*/
    List<Message> queryMessage(String userId);

    /**
     * 清空用户未读消息
     * @param userId
     */
    void clearUserNotReadMsg(String userId);
}
