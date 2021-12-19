package com.happysnaker.configuration.security;


import com.happysnaker.utils.JSONUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录失败操作
 * @author happysnakers
 */
@Component
public class XAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {
        System.out.println("登陆失败");
        response.setStatus(401);
        Map map = new HashMap(2);
        map.put("code", "401");
        map.put("msg", "密码或用户名错误");
        JSONUtils.writeJSON(response, map);
    }
}