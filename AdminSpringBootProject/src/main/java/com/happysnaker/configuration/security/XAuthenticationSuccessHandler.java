package com.happysnaker.configuration.security;

import com.happysnaker.service.AuthService;
import com.happysnaker.utils.JSONUtils;
import com.happysnaker.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/2
 * @email happysnaker@foxmail.com
 */
@Component
public class XAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private AuthService service;

    @Qualifier("myRedis")
    @Autowired
    private RedisTemplate redis;

    private JwtTokenUtil jwtTokenUtil = JwtTokenUtil.getInstance();

    private final String USER_TOKEN_HASH_CACHE_KEY = "redis-hash:username-token-key";


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String username = userDetails.getUsername();

            Map map = new HashMap(5);
            String token = null;
            if (redis.opsForHash().hasKey(USER_TOKEN_HASH_CACHE_KEY, username)) {
                token = (String) redis.opsForHash().get(USER_TOKEN_HASH_CACHE_KEY, username);
            }
            if (token == null || jwtTokenUtil.isTokenExpired(token)) {
                token = jwtTokenUtil.generateToken(userDetails);
                redis.opsForHash().put(USER_TOKEN_HASH_CACHE_KEY, username, token);
            }

            System.out.println("给用户 " + userDetails.getUsername() + " 的token是" + token);
            map.put("token", token);
            service.log(username, new Timestamp(System.currentTimeMillis()));
            service.setUserLoginStatus(username, true);
            JSONUtils.writeJSON(response, map);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}
