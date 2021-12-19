package com.happysnaker.service.observer;

import com.happysnaker.mapper.MessageMapper;
import com.happysnaker.mapper.OrderMapper;
import com.happysnaker.pojo.Message;
import com.happysnaker.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/13
 * @email happysnaker@foxmail.com
 */
@Slf4j
@Component
public class OrderUpdateObserver implements Observer {

    OrderMapper orderMapper;
    MessageMapper messageMapper;

    @Autowired
    public OrderUpdateObserver(OrderMapper mapper, MessageMapper messageMapper) {
        this.orderMapper = mapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public void acton(Object obj, Object... args)  {
        String orderId = (String) args[0];
        int type = (int) args[1];
        OrderService service = (OrderService) obj;
        // 这里可以异步处理
        try {
            String userId = orderMapper.queryOrder(orderId).getUserId();
            Message m = Message.createSystemMessage("您的订单ID为 " + orderId + " 的订单状态已更新至" + OrderService.typeMap[type], userId);
            service.sendMessage(m);
            if (messageMapper.updateUnReadUserMsgCount(m.getUserId(), 1) == 0) {
                messageMapper.insertUnReadUserMsgCount(m.getUserId(), 1);
            }
        } catch (Exception e) {
            log.info("更新订单: " + orderId + " 状态出错：" + " cause：" + e.getMessage());
            throw e;
        }
        return;
    }
}
