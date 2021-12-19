package com.happysnaker.configuration.security;

import com.happysnaker.utils.JSONUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/9
 * @email happysnaker@foxmail.com
 */
@Component
public class XLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setStatus(200);
        JSONUtils.writeJSON(response, "成功退出！");
    }
}
