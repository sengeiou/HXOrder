package com.happysnaker.service;

import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
public interface LoginService {

    /**
     * 通过前端穿来的 jsCode 获取用户唯一 OpenId 与 access_token，该 OpenId 将用以以后登录的凭证, access_token 将用于网页授权验证(如果需要的话)
     * @param jsCode jsCode
     * @return 返回 openId 与 session_key(map 中的 key)
     */
    Map<String, String> getOpenId(String jsCode);
}
