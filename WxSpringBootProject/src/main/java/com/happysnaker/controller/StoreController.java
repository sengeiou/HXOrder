package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.service.StoreService;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@RestController
public class StoreController extends BaseController {
    private StoreService service;

    @Autowired
    public StoreController(StoreService service) {
        this.service = service;
    }

    @GetMapping(value = "/get_store_list")
    public String getStoreInfo(HttpServletRequest request) {
        return service.getStoreInfo();
    }

    @GetMapping(value = "/get_store")
    public String getStoreById(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumber(request.getParameter(STORE_ID_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return PARAM_ERROR_MSG;
        }
        return JSONObject.toJSONString(service.getStoreById(Integer.parseInt(request.getParameter(STORE_ID_PARAM))));
    }

}
