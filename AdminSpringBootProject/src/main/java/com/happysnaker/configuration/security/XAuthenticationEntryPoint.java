package com.happysnaker.configuration.security;

import com.happysnaker.utils.JSONUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份校验出现异常处理器，AccessDeniedHandler 处理器处理的是已认证的用户在鉴权时失败，处理权限异常；而此处理器处理未认证用户在鉴权时异常，如 token 异常，未设置 userdetail，则认证不通过，为匿名用户，本系统不支持匿名访问，当认证失败时，回送 401 错误码，让前端重新登录校验<br/>
 * <br/>
 * 这与 AuthenticationFailureHandler 的区别在于， AuthenticationFailureHandler 处理的是有名用户(携带 username，被 Security 封装成 userdetails 设置在上下文中)但认证失败，AuthenticationFailureHandler 的对象是匿名的，即不存在 username
 */
@Component
public class XAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        System.out.println("身份校验失败: " + authException.getMessage());
        //用户已经登陆了，
//        response.setStatus(403);
        Map map = new HashMap(2);
        map.put("code", 401);
        map.put("msg", "用户信息验证未通过，请重新登陆");
        JSONUtils.writeJSON(response, map);
    }
}
