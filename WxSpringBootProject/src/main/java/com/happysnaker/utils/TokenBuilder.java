package com.happysnaker.utils;

import com.happysnaker.exception.SignException;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/12
 * @email happysnaker@foxmail.com
 */
public class TokenBuilder {
    public static final String ALG = "sha256";
    public static final String TYP = "jwt";

    private Map<String, Object> playLoadMap;
    private Map<String, Object> headMap;

    private TokenUtils tokenUtils;

    public TokenBuilder(TokenUtils tokenUtils) {
        this();
        this.tokenUtils = tokenUtils;
    }

    public String build() throws NoSuchAlgorithmException, SignException {
        return tokenUtils.generateToken(headMap, playLoadMap, TokenUtils.secretKey);
    }

    public String build(String secretKey) throws NoSuchAlgorithmException, SignException {
        return tokenUtils.generateToken(headMap, playLoadMap, secretKey);
    }

    private TokenBuilder() {
        this.headMap = new HashMap<>(10);
        this.playLoadMap = new HashMap<>(10);
        headMap.put("alg", ALG);
        headMap.put("typ", TYP);
    }


    public TokenBuilder buildTypeAtHeader(String type) {
        headMap.put("typ", type);
        return this;
    }

    public TokenBuilder buildAtHeader(Map<String, Object> args) {
        headMap.putAll(args);
        return this;
    }


    /**
     * 发布时间
     * @param issuedAt
     * @return
     */
    public TokenBuilder setIssuedAt(long issuedAt) {
        playLoadMap.put("iat", issuedAt);
        return this;
    }

    /**
     * 过期时间
     * @param expiration
     * @return
     */
    public TokenBuilder setExpiration(long expiration) {
        playLoadMap.put("exp", expiration);
        return this;
    }

    /**
     * 发行人
     * @param issuer
     * @return
     */
    public TokenBuilder setIssuer(String issuer) {
        playLoadMap.put("iss", issuer);
        return this;
    }

    /**
     * 主题
     * @param subject
     * @return
     */
    public TokenBuilder setSubject(String subject) {
        playLoadMap.put("sub", subject);
        return this;
    }

    /**
     * 对象
     * @param audience
     * @return
     */
    public TokenBuilder setAudience(String audience) {
        playLoadMap.put("aud", audience);
        return this;
    }

    public TokenBuilder setAll(Map<String, Object> args) {
        playLoadMap.putAll(args);
        return this;
    }

    public TokenBuilder setKV(String key, Object val) {
        playLoadMap.put(key, val);
        return this;
    }
}