package com.happysnaker.configuration.security;

import com.happysnaker.utils.JSONUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限校验处理器
 */
@Component
public class XAccessDeniedHandler  implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        //装入token
        System.out.println("权限检验失败");
        Map map = new HashMap();
        map.put("code", 403);
        map.put("msg", "鉴权失败，您无权访问该资源");
        JSONUtils.writeJSON(response, map);
    }
}
