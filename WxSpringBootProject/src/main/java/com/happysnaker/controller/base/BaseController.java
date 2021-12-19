package com.happysnaker.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.Observer.Observer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/6
 * @email happysnaker@foxmail.com
 */
@Slf4j
public class BaseController {
    @Qualifier("myRedisTemplate")
    @Autowired
    protected RedisTemplate redis;
    /**请求体中的参数信息*/
    protected final String NUM_PARA = "num";
    protected final String USER_ID_PARAM = "userId";
    protected final String STORE_ID_PARAM = "storeId";
    protected final String CACHE_PARAM = "cache";
    protected final String DISH_ID_PARAM = "dishId";
    protected final String JS_CODE_PARAM = "jsCode";
    protected final String ORDER_PARAM = "order";
    protected final String ORDER_ID_PARAM = "orderId";
    protected final String PAY_ID_PARAM = "payId";
    protected final String ADDRESS_PARAM = "address";
    protected final String ADDRESS_ID_PARAM = "addressId";
    protected final String USER_REQUEST_MSG_TIMESTAMP = "timestamp";



    /** 状态码 */
    protected final String ENABLE_CACHE = "true";
    protected final String ORDER_FAIL_MESSAGE = "Dish Not Enough";
    protected final String OK = "{code: 200, msg: 'ok'}";
    protected final int PARAM_ERROR_STATUS = 400;
    protected final String PARAM_ERROR_MSG = "{code: 400, msg: '参数不对'}";
    protected final int STOCK_NOT_ENOUGH_STATUS = 409;

    /** 其他默认信息 */
    protected final long DEFAULT_EXPIRATION_TIME = 1500;
    protected  int DEFAULT_DISH_LIKE_FLUSH_NUM = 1;

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    protected Map<Integer, List<Observer>> observers;

    public void observerRegister(Observer observer, int type) {
        if (observers == null) {
            observers = new HashMap<>((1 << 2));
        }
        List o = observers.getOrDefault(type, new ArrayList<>());
        o.add(observer);
        observers.put(type, o);
    }

    protected void notifyObservers(int type, Object... args) {
        if (observers == null) {
            return;
        }
        for (Observer observer : observers.getOrDefault(type, new ArrayList<>())) {
            observer.action(this, args);
        }
    }

//    protected Object getCacheIfEnableCache(HttpServletRequest request, String key) {
//        if (!VerifyUtils.isNullOrEmpty(request.getParameter(CACHE_PARAM))) {
//            if (request.getParameter(CACHE_PARAM).equals(ENABLE_CACHE) && redis.hasKey(key)) {
//                return (String) redis.opsForValue().get(key);
//            }
//        }
//        return null;
//    }
//
//    protected Object getCacheOrDefaultIfEnableCache(HttpServletRequest request, String key, Object def) {
//        Object cache = getCacheIfEnableCache(request, key);
//        return cache == null ? def : cache;
//    }
//
//    protected Object addCacheIfAbsent(String key, Object val, long time) {
//        if (!redis.hasKey(key)) {
//            redis.opsForValue().set(key, val);
//            redis.expire(key, time, TimeUnit.SECONDS);
//        }
//        return val;
//    }
//
//    protected Object addCacheIfAbsent(String key, Object val) {
//        return addCacheIfAbsent(key, val, DEFAULT_EXPIRATION_TIME);
//    }

    protected String error(HttpServletResponse response) {
        response.setStatus(406);
        Map map = new HashMap(2);
        map.put("code", 406);
        map.put("msg", "服务器无法正确服务该请求");
        return JSONObject.toJSONString(map);
    }

    protected void logInfo(HttpServletRequest request, String method, String val) {
        log.info(BaseController.getIpAddress(request) + " 访问方法: " + method + " ===> 返回数据: " + val);
    }
}
