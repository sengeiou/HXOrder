package com.happysnaker.filter;

import com.happysnaker.config.RedisCacheManager;
import com.happysnaker.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截验证 token，如果 token 合法，则取出对应的 userId 放入参数
 * @author Happysnaker
 * @description
 * @date 2021/12/12
 * @email happysnaker@foxmail.com
 */
@Component("secondFilter")
public class TokenFilterChain extends AbstractFilterChain{
    private String header = "Authorization";
    private String loginUri = "/login";
    private String USER_ID = "userId";


    @Autowired
    private TokenUtils tokenUtils;

    @Qualifier("myRedisTemplate")
    @Autowired
    protected RedisTemplate redis;

    @Autowired
    public TokenFilterChain(@Qualifier("thirdFilter") AbstractFilterChain filterChain) {
        this.myFilterChain = filterChain;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 将 userId 注入参数
        HttpServletRequestWrapper requestProxy = new HttpServletRequestWrapper((HttpServletRequest) servletRequest) {
            @Override
            public String getParameter(String name) {
                if (name.equals("userId")) {
                    return (String) servletRequest.getAttribute(USER_ID);
                }
                return super.getParameter(name);
            }
        };
        super.doFilter(requestProxy, servletResponse, filterChain);
    }

    @Override
    public boolean isRequired(HttpServletRequest request, HttpServletResponse response) {
        return !request.getRequestURI().equals(loginUri);
    }

    @Override
    public boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(header);

        if (tokenUtils.isTokenExpired(RedisCacheManager.getTokenCacheKey(token)) || !tokenUtils.verifyToken(token)) {
            response.setStatus(401);
            return false;
        }
//        System.out.println("tkn = " + token);
        //根据 token 取出 userId
        Object userId = redis.opsForValue().get(RedisCacheManager.getTokenCacheKey(token));
        if (userId == null) {
            response.setStatus(401);
            return false;
        }
        request.setAttribute(USER_ID, userId);
        return true;
    }
}
