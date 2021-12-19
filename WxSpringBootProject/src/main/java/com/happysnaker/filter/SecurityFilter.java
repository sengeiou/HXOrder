package com.happysnaker.filter;

import com.happysnaker.config.RedisCacheManager;
import com.happysnaker.controller.base.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 流量监控
 * @author Happysnaker
 * @description
 * @date 2021/12/12
 * @email happysnaker@foxmail.com
 */
@Component("firstChain")
@Order(1)
@WebFilter(filterName = "AbstractFilterChain",urlPatterns = {"/*"})
@Slf4j
public class SecurityFilter extends AbstractFilterChain{
    /** 10s 内最多允许 ip 访问次数 */
    private final int MAX_COUNT_IN_TEN_SECOND = 10 * 30;

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redis;

    @Autowired
    private RedisCacheManager redisManager;

    @Autowired
    public SecurityFilter(@Qualifier("secondFilter") AbstractFilterChain filterChain) {
        this.myFilterChain = filterChain;
    }

    @Override
    public boolean isRequired(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public boolean doFilter(HttpServletRequest request, HttpServletResponse response) {
        String ip = BaseController.getIpAddress(request);
        if (!redisManager.hasKey(RedisCacheManager.getIpCacheKey(ip))) {
            redis.opsForValue().set(RedisCacheManager.getIpCacheKey(ip), 1);
            redis.expire(RedisCacheManager.getIpCacheKey(ip), 10, TimeUnit.SECONDS);
            return true;
        }
        if ((int)redis.opsForValue().get(RedisCacheManager.getIpCacheKey(ip)) >= MAX_COUNT_IN_TEN_SECOND) {
            log.info("ip 为 " + ip + " 的用户访问次数过多，限制访问");
            response.setStatus(503);
            return false;
        }
        redis.opsForValue().increment(RedisCacheManager.getIpCacheKey(ip), 1);
        return true;
    }
}
