package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
@RestController
public class HomeController {
    @Autowired
    private HomeService service;


    @GetMapping("/home")
    public String home(int storeId, String start, String end) {
        return JSONObject.toJSONString(service.getData(storeId));
    }
}
