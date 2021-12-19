package com.happysnaker.configuration.security;

import com.happysnaker.service.AuthService;
import com.happysnaker.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
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
public class XLogOutOncePerRequestFilter extends OncePerRequestFilter {
    @Autowired
    private AuthService service;

    @Autowired
    private UserDetailsService userDetailsService;

    private JwtTokenUtil jwtTokenUtil = JwtTokenUtil.getInstance();

    private String uri = "/logout";
    private String xhr = "OPTIONS";
    private String header = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals(xhr) || !request.getRequestURI().equals(uri)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(header) == null ? null : request.getHeader(header) .replace("Bearer", "").trim();
        String username = jwtTokenUtil.getUsernameFromToken(token);
        System.out.println(username);
        //如果是登录状态，则登出
        if (service.getUserLoginStatus(username)) {
            service.setUserLoginStatus(username, false);
        }
        //可能 username 不存在，此时让他报错，返回 500 服务器错误
        filterChain.doFilter(request, response);
    }
}
