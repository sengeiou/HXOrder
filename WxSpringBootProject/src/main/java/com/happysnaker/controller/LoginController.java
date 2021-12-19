package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.config.RedisCacheManager;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.exception.SignException;
import com.happysnaker.service.LoginService;
import com.happysnaker.utils.TokenBuilder;
import com.happysnaker.utils.TokenUtils;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@RestController
public class LoginController extends BaseController {
    private LoginService service;
    private TokenUtils tokenUtils;

    @Autowired
    public LoginController(LoginService service, TokenUtils tokenUtils) {
        this.service = service;
        this.tokenUtils = tokenUtils;
    }

    @PostMapping(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, SignException {
        if (VerifyUtils.isNullOrEmpty(request.getParameter(JS_CODE_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }
        Map<String, String> map = service.getOpenId(request.getParameter(JS_CODE_PARAM));
        System.out.println(map);
        String openid = map.get("openid");
        String sessionKey = map.get("session_key");
        String token = new TokenBuilder(tokenUtils)
                .setIssuedAt(System.currentTimeMillis())
                .setIssuer("happysnaker")
                .setSubject("auth_token")
                .setKV("random", Math.random() * 20211212)
                .build();
        map.put("token", token);
        tokenUtils.storeToken(token, openid, redis);
        String Key = RedisCacheManager.getTokenCacheKey(token);
        return JSONObject.toJSONString(map);
    }

}
