package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.exception.OrderAddException;
import com.happysnaker.exception.ReadWriterLockException;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Order;
import com.happysnaker.pojo.OrderApplyTable;
import com.happysnaker.service.OrderService;
import com.happysnaker.utils.VerifyUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@RestController
@Api(tags = {"测试接口"})
public class OrderController extends BaseController {
    private OrderService service;

    @Autowired
    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping(value = "/get_user_orders")
    String getUserOrders(HttpServletRequest request, HttpServletResponse response) {
        var ans = service.getUserOrders(request.getParameter(USER_ID_PARAM));
        return ans == null ? error(response) : ans;
    }

    @PostMapping(value = "/add_user_order")
    String addUserOrder(HttpServletRequest request, HttpServletResponse response) {
        if (VerifyUtils.isNullOrEmpty(request.getParameter(ORDER_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }

        Order order = JSONObject.parseObject(request.getParameter(ORDER_PARAM), Order.class);
        int[] t = new int[]{2, 0, 1, 1};
        order.setOrderType(t[order.getConsumeType()]); // 防止恶意用户伪造 orderType
        Map res = null;
        try {
            res = service.addUserOrder(request.getParameter(USER_ID_PARAM), order);
        } catch (OrderAddException | ReadWriterLockException e) {
            e.printStackTrace();
            Map map = new HashMap();
            map.put("code", 409);
            map.put("msg", "库存不足");
            response.setStatus(409);
            return JSONObject.toJSONString(map);
        }
        return JSONObject.toJSONString(res);
    }


    @GetMapping("/get_waiting_time")
    public String getWaitingTime(HttpServletRequest request, HttpServletResponse response) {
        if (!VerifyUtils.isNumber(request.getParameter(STORE_ID_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }
        double waitingTime =  service.getWaitingTime(Integer.parseInt(request.getParameter(STORE_ID_PARAM)));
        JSONObject object = new JSONObject();
        object.put("time", waitingTime);
        return object.toJSONString();
    }

    @PostMapping("/delete_order")
    public String deleteOrder(HttpServletRequest request, HttpServletResponse response) {
        if (VerifyUtils.isNullOrEmpty(request.getParameter(ORDER_ID_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return null;
        }
        try {
            service.deleteOrder(request.getParameter(ORDER_ID_PARAM));
            return OK;
        } catch (Exception e) {
            e.printStackTrace();
            return error(response);
        }
    }

    @PostMapping("/cancel_order")
    public String cancelOrder(HttpServletRequest request, HttpServletResponse response) {
        OrderApplyTable at = null;
        try {
            at = JSONObject.parseObject(request.getParameter("apply"), OrderApplyTable.class);
        } catch (ClassCastException e) {
            return error(response);
        }
        service.cancelOrder(at);
        return OK;
    }

    @PostMapping("/pay")
    public String pay(HttpServletRequest request, HttpServletResponse response) {
        if (VerifyUtils.isNullOrEmpty(request.getParameter(PAY_ID_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return PARAM_ERROR_MSG;
        }
        boolean b;
        try {
            b = service.pay(request.getParameter(PAY_ID_PARAM));
        } catch (UpdateException e) {
            e.printStackTrace();
            return error(response);
        }
        return b ? OK : error(response);
    }

    /**
     * 用户取消支付，订单状态设置为待支付，默认一段时间后取消
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/cancelpay")
    public String cancelPay(HttpServletRequest request, HttpServletResponse response) {
        if (VerifyUtils.isNullOrEmpty(request.getParameter(ORDER_ID_PARAM))) {
            response.setStatus(PARAM_ERROR_STATUS);
            return PARAM_ERROR_MSG;
        }
        try {
            service.cancelPay(request.getParameter(ORDER_ID_PARAM));
        } catch (UpdateException e) {
            e.printStackTrace();
            return error(response);
        }
        return OK;
    }
}
