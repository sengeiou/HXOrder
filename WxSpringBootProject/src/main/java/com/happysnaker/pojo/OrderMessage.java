package com.happysnaker.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/13
 * @email happysnaker@foxmail.com
 */
@Data
public class OrderMessage implements Serializable {
    Map<Integer, Integer> dishNumMap;
    Order order;
    String messageId;

    public OrderMessage(Map<Integer, Integer> dishStockMap, Order order) {
        this.dishNumMap = dishStockMap;
        this.order = order;
        messageId = UUID.randomUUID().toString();
        System.out.println("mid是" + messageId);
    }
}
