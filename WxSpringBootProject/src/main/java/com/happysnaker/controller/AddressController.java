package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Address;
import com.happysnaker.service.AddressService;
import com.happysnaker.utils.JsonUtils;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/30
 * @email happysnaker@foxmail.com
 */
@RestController
public class AddressController extends BaseController {
    private AddressService service;

    @Autowired
    public AddressController(AddressService service) {
        this.service = service;
    }

    @GetMapping("/get_user_address")
    public String getUserAddress(HttpServletRequest request, HttpServletResponse response) {
        return JsonUtils.listAddToJsonObject(new JSONObject(), service.getUserAddress(request.getParameter(USER_ID_PARAM))).toJSONString();
    }

    @PostMapping("/remove_user_address")
    public String removeUserAddress(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumber(request.getParameter(ADDRESS_ID_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }
        try {
            service.removeUserAddress(Integer.parseInt(request.getParameter(ADDRESS_ID_PARAM)));
        } catch (UpdateException e) {
            e.printStackTrace();
            return error(response);
        }
        return OK;
    }

    @PostMapping("/add_user_address")
    public String addUserAddress(HttpServletRequest request, HttpServletResponse response) {
        if (VerifyUtils.isNullOrEmpty(request.getParameter(ADDRESS_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }
        System.out.println(request.getParameter(ADDRESS_PARAM));
        Address addr = JSONObject.parseObject(request.getParameter(ADDRESS_PARAM), Address.class);
        try {
            service.addUserAddress(addr);
        } catch (UpdateException e) {
            e.printStackTrace();
            return error(response);
        }
        return OK;
    }

    @PostMapping("/update_user_address")
    public String updateUserAddress(HttpServletRequest request, HttpServletResponse response) {
        if (VerifyUtils.isNullOrEmpty(request.getParameter(ADDRESS_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }
        Address addr = JSONObject.parseObject(request.getParameter(ADDRESS_PARAM), Address.class);
        try {
            service.updateUserAddress(addr);
        } catch (UpdateException e) {
            e.printStackTrace();
            return error(response);
        }
        return OK;
    }

}
