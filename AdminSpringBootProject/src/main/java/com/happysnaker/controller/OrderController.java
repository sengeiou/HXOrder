package com.happysnaker.controller;

import com.alibaba.fastjson.JSONObject;
import com.happysnaker.controller.base.BaseController;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Message;
import com.happysnaker.pojo.Order;
import com.happysnaker.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/9
 * @email happysnaker@foxmail.com
 */
@RestController
public class OrderController extends BaseController {
@Qualifier("orderServiceImpl")
    @Autowired
    private OrderService service;

    @GetMapping("/order/list")
    public String getOrderList(int pageNum, int pageSize, int storeId, String keyword, Integer orderType, String createTime) throws ParseException {
        Timestamp ts = null;
        if (createTime != null) {
            ts = new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(createTime).getTime());
        }
        System.out.println("ts == " + createTime + ts);
        JSONObject object = new JSONObject();
        object.put("total", service.getSize());
        object.put("arrays", service.getOrderListByPagination(pageNum, pageSize, keyword, orderType, ts, storeId == -1 ? null : storeId));
        return object.toJSONString();
    }


    @PostMapping("/order/delete")
    public String deleteOrder(String ids, HttpServletResponse response) {
        try {
            service.deleteOrder(ids.split(","));
        } catch (UpdateException e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, e.getMessage());
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/message/send")
    public String sendMessage(@RequestBody Message message) {
        service.sendMessage(message);
        return SUCCESS_MSG;
    }


    @PostMapping("/order/handle_to_be_paid_order")
    public String handleToBePaidOrder(@RequestBody Order order) {
        System.out.println("order == " + "service == " + service);
        service.handingAndSetPaymentStatus(order, order.getPaymentStatus() != 0);
        return SUCCESS_MSG;
    }

    @PostMapping("/order/update_type")
    public String updateOrderType(String orderId, int nowConsumeType, int nowOrderType, HttpServletResponse response) {
        int nextStatus = service.getNextStatus(nowConsumeType, nowOrderType);

        try {
            service.updateOrderType(orderId, nextStatus);
        } catch (UpdateException e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, e.getMessage());
        }
        return SUCCESS_MSG;
    }

    @PostMapping("/order/reject_confirm")
    public String rejectConfirm(String orderId, String userId, String rejectReason, int storeId, HttpServletResponse response) {
        try {
            service.updateOrderType(orderId, OrderService.CANCELLED_STATUS);
            service.sendMessage(new Message(0, "管理员拒绝了您的订单", rejectReason, 0, storeId, userId, new Timestamp(System.currentTimeMillis())));
        } catch (UpdateException e) {
            e.printStackTrace();
            return setCodeAndReturnMsg(response, NOT_ACCEPTABLE_CODE, e.getMessage());
        }
        return SUCCESS_MSG;
    }

    @GetMapping("/order/get")
    public String getOrder(String orderId) {
        return JSONObject.toJSONString(service.getOrder(orderId));
    }


}
