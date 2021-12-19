package com.happysnaker.configuration;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 同步小程序端缓存
 * @author Happysnaker
 * @description
 * @date 2021/12/14
 * @email happysnaker@foxmail.com
 */
@Component
@Aspect
public class SyncWxAop {
    @Qualifier("myRedis")
    @Autowired
    RedisTemplate redis;

    /** 定义切点Pointcut */
    @Pointcut("execution(* com.happysnaker.controller.DishStatusController.*(..))")
    public void pointCut1() {}

    @Pointcut("execution(* com.happysnaker.controller.StoreController.*(..))")
    public void pointCut2() {}


    @After("pointCut1()")
    public void doAfter1() throws Throwable {
        System.out.println("删除永不");
        redis.delete(RedisCacheManager.INDEX_DISH_INFO_CACHE_KEY);
    }

    @After("pointCut2()")
    public void doAfter2() throws Throwable {
        System.out.println("删除永不");
        redis.delete(RedisCacheManager.STORE_CACHE_KEY);
    }
}
