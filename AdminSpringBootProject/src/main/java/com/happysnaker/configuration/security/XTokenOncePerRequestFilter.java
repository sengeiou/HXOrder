package com.happysnaker.configuration.security;

import com.happysnaker.service.AuthService;
import com.happysnaker.utils.JSONUtils;
import com.happysnaker.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截器
 */
@Component
public class XTokenOncePerRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthService authService;

    private JwtTokenUtil jwtTokenUtil = JwtTokenUtil.getInstance();


    private String header = "Authorization";
    private String loginUri = "/login";
    private String getAdminInfo = "/admin/info";
    private String xhr = "OPTIONS";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        System.out.println("xon" + request.getRequestURI() + request.getMethod());
        //如果用户要登陆，此时无 token，直接放行，让 usernameFilter 处理
        if (uri.equals(loginUri) || request.getMethod().equals(xhr)) {
            chain.doFilter(request, response);
            return;
        }


        String token = request.getHeader(header) == null ? null : request.getHeader(header) .replace("Bearer", "").trim();
        String username = null;
        try {
            username = jwtTokenUtil.getUsernameFromToken(token);
            System.out.println("username = " + username);
        } catch (Exception e) {
            // 登录过期
            Map map = new HashMap(2);
            map.put("code", 401);
            map.put("msg", "当前登录状态已过期，请先登录！");
            JSONUtils.writeJSON(response, map);
            return;
        }

        boolean validata = true;

        //token不合法，无法获取 username
        if (username == null) {
            validata = false;
        }
        //token 过期，需要重新登陆
        if (validata && jwtTokenUtil.isTokenExpired(token)) {
            validata = false;
        }

        //否则，用户此时为已登录，查询用户登录状态是否一致，如果用户未登录则不合法，提醒用户重新登录
        if (!authService.getUserLoginStatus(username) && !request.getMethod().equals("options")) {
            System.out.println("用户未登录！");
            Map map = new HashMap(2);
            map.put("code", 401);
            map.put("msg", "当前未登录，请先登录！");
            JSONUtils.writeJSON(response, map);
            return;
        }

        // token 合法有效
        if (validata) {
            //如果是一条获取用户权限信息的请求，这个请求将请求用户权限，则不用进入到过滤链，直接处理返回，因为此时用户是带着 token 来的，没有携带用户名参数，属于权限管理阶段，因此放在 controller 处理是不太合适的
            if (uri.equals(getAdminInfo) && request.getMethod().equals("GET")) {
                JSONUtils.writeJSON(response, authService.getUserInfo(username));
                return;
            }
            //其他情况下，封装 userDetails 让 SpringSecurity 其他验证器进行验证
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
