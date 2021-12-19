package com.happysnaker.configuration;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/13
 * @email happysnaker@foxmail.com
 */
@Slf4j
@Aspect
@Configuration
public class LogAspect {

    /** 定义切点Pointcut */
    @Pointcut("execution(* com.happysnaker.controller.*Controller.*(..))")
    public void excudeService() {}



    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String method = request.getMethod();
        String uri = request.getRequestURI();
        String paraString = JSON.toJSONString(request.getParameterMap());
        log.info("***************************************************");
        log.info("请求开始 URI: {}, method: {}, params: {}", uri, method, paraString);

        // result的值就是被拦截方法的返回值
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("请求结束，返回数据： " + JSON.toJSONString(result));
        return result;
    }

}
