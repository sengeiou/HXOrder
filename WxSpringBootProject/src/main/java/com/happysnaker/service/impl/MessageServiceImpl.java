package com.happysnaker.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.pojo.Message;
import com.happysnaker.service.BaseService;
import com.happysnaker.service.MessageService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/1
 * @email happysnaker@foxmail.com
 */
@Service
public class MessageServiceImpl extends BaseService implements MessageService {
    @Override
    public String queryUserMsgCount(String userId, Timestamp ts) {
        System.out.println(userId + "===" + messageMapper.queryUnReadUserMsgCount(userId));
        return new JSONObject()
                    .fluentPut("msgCount", messageMapper.queryUnReadUserMsgCount(userId))
                    .toJSONString();
    }

    @Override
    public List<Message> queryMessage(String userId) {
        var list = messageMapper.queryMessage(userId);
        // 按时间戳排序，近的在前
        Collections.sort(list, (m1, m2)-> {
            return m1.getCreateTime().getTime() > m2.getCreateTime().getTime() ? -1 : 1;
        });
        return list;
    }

    @Override
    public void clearUserNotReadMsg(String userId) {
        messageMapper.updateUnReadUserMsgCount(userId, -messageMapper.queryUnReadUserMsgCount(userId));
    }
}
