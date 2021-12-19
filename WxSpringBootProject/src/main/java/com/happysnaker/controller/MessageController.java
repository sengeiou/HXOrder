package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.service.MessageService;
import com.happysnaker.utils.JsonUtils;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/1
 * @email happysnaker@foxmail.com
 */
@RestController
public class MessageController extends BaseController {
    private MessageService service;

    @Autowired
    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/get_user_message_count")
    public String queryUserMsgCount(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumber(request.getParameter(USER_REQUEST_MSG_TIMESTAMP))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }

        return service.queryUserMsgCount(request.getParameter(USER_ID_PARAM), new Timestamp(Long.parseLong(request.getParameter(USER_REQUEST_MSG_TIMESTAMP))));
    }

    @GetMapping("/get_user_message")
    public String queryMessage(HttpServletRequest request) {
        service.clearUserNotReadMsg(request.getParameter(USER_ID_PARAM));
        return JsonUtils.listAddToJsonObject(new JSONObject(), service.queryMessage(request.getParameter(USER_ID_PARAM)), "messages").toJSONString();
    }
}
