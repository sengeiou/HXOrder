package com.happysnaker.utils;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.config.RedisCacheManager;
import com.happysnaker.exception.SignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/12
 * @email happysnaker@foxmail.com
 */
@Component
public class TokenUtils {

    @Qualifier("myRedisTemplate")
    @Autowired
    private RedisTemplate redis;

    @Autowired
    private RedisCacheManager redisManager;

    public final static long DEFAULT_TOKEN_EXPIRATION_TIME_HOUR = 10;
    public final static String secretKey = "qlkmdki2nfbvbzuuiq";


    public String generateToken(Map head, Map playLoad, String secretKey) throws NoSuchAlgorithmException, SignException {
        StringBuilder sb = new StringBuilder();
        String h = encodeBase64(head);
        sb.append(h);
        sb.append('.');
        String p = encodeBase64(playLoad);
        sb.append(p);
        sb.append('.');
        sb.append(sign(h + p, secretKey));
        return sb.toString();
    }

    public String encodeBase64(Map<String, Object> map) {
        return Base64.getEncoder().encodeToString(map.toString().getBytes());
    }

    public String decodeBase64(String src) {
        return new String(Base64.getDecoder().decode(src));
    }

    public String sign(String str, String secretKey) throws NoSuchAlgorithmException, SignException {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(str.getBytes());
            hash = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new SignException("签名错误: " + e.getMessage());
        }
        return hash;
    }

    public void storeToken(String token, String val, Object r) {
        String key = RedisCacheManager.getTokenCacheKey(token);
        redis.opsForValue().set(key, val);
        redis.expire(RedisCacheManager.getTokenCacheKey(token), DEFAULT_TOKEN_EXPIRATION_TIME_HOUR, TimeUnit.HOURS);
        System.out.println("ys = " + isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return redisManager.hasKey(RedisCacheManager.getTokenCacheKey(token));
    }

    public boolean verifyToken(String token, String secretKey)   {
        if (token == null) {
            return false;
        }
        String[] jwt = token.split("\\.");
        if (jwt.length != 3) {
            return false;
        }
        try {
            return sign(jwt[0] + jwt[1], secretKey).equals(jwt[2]);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyToken(String token) {
        return verifyToken(token, secretKey);
    }

    public Map getPlayLoad(String token) {
        String[] jwt = token.split(".");
        return JSONObject.parseObject(jwt[1], Map.class);
    }
}
