package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.service.DishService;
import com.happysnaker.utils.JsonUtils;
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
public class DishController extends BaseController {
    private DishService service;

    @Autowired
    public DishController(DishService service) {
        this.service = service;
    }

    @GetMapping(value = "/get_index_dish_info")
    public String getIndexDishInfo(HttpServletRequest request, HttpServletResponse response) {
        return service.getIndexDishInfo();
    }

    @GetMapping(value = "/get_order_dish_info")
    public String getOrderDishInfo(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumber(request.getParameter(STORE_ID_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }
        return service.getOrderDishInfo(Integer.parseInt(request.getParameter(STORE_ID_PARAM)));
    }


    @GetMapping(value = "/get_dish_classification")
    public String getDishClassification(HttpServletRequest request, HttpServletResponse response) {
        return JsonUtils.listAddToJsonObject(new JSONObject(), service.getDishClassification()).toJSONString();
    }

    @GetMapping("/get_user_collected_dishes")
    public String getUserCollectedDishInfo(HttpServletRequest request, HttpServletResponse response) {
        return JsonUtils.listAddToJsonObject(new JSONObject(), service.getUserCollectedDishes(request.getParameter(USER_ID_PARAM))).toJSONString();
    }


}
