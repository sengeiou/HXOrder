package com.happysnaker.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/3
 * @email happysnaker@foxmail.com
 */
public class JwtTokenUtil {


    /**
     * 用以数据完整性签名的私钥
     */
    private static final String SECRET_KEY = "hjuhiuo?=912";

    /**
     * 过期时间 毫秒,设置默认1周的时间过期
     */
    private static final long EXPIRATION_TIME = 3600000L * 24 * 7;

    private volatile static JwtTokenUtil jwtTokenUtil = null;

    public static JwtTokenUtil getInstance() {
        if (jwtTokenUtil == null) {
            synchronized (JwtTokenUtil.class) {
                if (jwtTokenUtil == null) {
                    jwtTokenUtil = new JwtTokenUtil();
                }
            }
        }
        return jwtTokenUtil;
    }

    private JwtTokenUtil() {
        // 单例类，隐藏构造函数
    }

    /**
     * token是否过期？
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 生成令牌
     *
     * @param userDetails 用户
     * @return 令牌
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put(Claims.SUBJECT, userDetails.getUsername().trim());
        claims.put(Claims.ISSUED_AT, new Date());
        return generateToken(claims);
    }


    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis()+ EXPIRATION_TIME);
        return Jwts.builder().
                setClaims(claims).
                setExpiration(expirationDate).
                signWith(SignatureAlgorithm.HS512, SECRET_KEY).
                compact();
    }


    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        if (token == null) {
            return null;
        }
        String username = null;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            return null;
        }
        return username;
    }


    /**
     * 从令牌中获取数据声明，如果这里抛出异常，则代表 token 无法通过私匙验证
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("用户 TOKEN 不可信");
        }
        return claims;
    }
}
