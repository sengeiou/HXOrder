package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.pojo.Discount;
import com.happysnaker.pojo.Dish;
import com.happysnaker.service.MarketingService;
import com.happysnaker.utils.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/8
 * @email happysnaker@foxmail.com
 */
@RestController
public class MarketingController extends BaseController {
    @Autowired
    private MarketingService service;

    @GetMapping("/marketing/discount_list")
    public String getDiscountDishList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = super.getPaginationInfo(request);
        int pageNum = (int) map.get(PAGE_NUM_PARAM);
        int pageSize = (int) map.get(PAGE_SIZE_PARAM);
        String keyword = (String) map.get(KEY_WORD_PARAM);

        Integer type = null;
        if (VerifyUtils.isNumber(request.getParameter(DISCOUNT_TYPE_PARAM))) {
            type = Integer.parseInt(request.getParameter(DISCOUNT_TYPE_PARAM));
        }

        List<Dish> dishes = service.getDiscountDishListByPagination(pageNum, pageSize, keyword, type);
        JSONObject object = new JSONObject();
        object.put("arrays", dishes);
        object.put("total", service.getSize());
        return object.toJSONString();
    }

    @PostMapping("/marketing/update_discount")
    public String updateDiscount(@RequestBody Discount discount, HttpServletResponse response) {
        try {
            service.updateDiscount(discount);
        } catch (Exception e) {
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, NOT_ACCEPTABLE_MSG);
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/marketing/add_discount")
    public String addDiscount(@RequestBody Discount discount, HttpServletResponse response) {
        try {
            service.addDiscount(discount);
        } catch (Exception e) {
            e.printStackTrace();
            Map map = new HashMap();
            map.put("code", NOT_ACCEPTABLE_CODE);
            map.put("msg", "请勿重复添加");
            return JSONObject.toJSONString(map);
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/marketing/delete_discount")
    public String deleteDiscount(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumberList(request.getParameter(DISH_ID_LIST_PARAM))) {
            response.setStatus(PARAM_ERR_CODE);
            return PARAM_ERR_MSG;
        }
        List<Integer> ids = VerifyUtils.string2NumberList(request.getParameter(DISH_ID_LIST_PARAM));
        service.deleteDiscount(ids);
        return SUCCESS_MSG;
    }
}
