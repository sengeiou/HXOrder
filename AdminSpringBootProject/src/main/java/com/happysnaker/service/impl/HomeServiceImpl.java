package com.happysnaker.service.impl;

import com.happysnaker.pojo.Order;
import com.happysnaker.service.HomeService;
import com.happysnaker.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
@Service
public class HomeServiceImpl extends BaseService implements HomeService {


    @Override
    public Map<String, Object> getData(int storeId) {
        List<Order> orders = orderMapper.queryAllOrder();
        if (storeId != -1) {
            orders = orders.stream().filter(order -> order.getStoreId() == storeId).collect(Collectors.toList());
        }
        Map<String, Integer> orderNum = new HashMap<>(orders.size());
        Date today = new Date(System.currentTimeMillis());
        today.setHours(0);
        today.setHours(0);
        today.setMinutes(0);
        int todayOrderCount = 0;
        double todayAmount = 0;
        double lastWeekAmount = 0;
        for (Order order : orders) {
            orderNum.putIfAbsent(String.valueOf(order.getOrderType()), 0);
            System.out.println("o = " + order.getOrderType());
            orderNum.put(String.valueOf(order.getOrderType()), orderNum.get(String.valueOf(order.getOrderType())) + 1);
            if (order.getFinalTime() != null && order.getFinalTime().after(new Timestamp(today.getTime()))) {
                todayOrderCount += 1;
                todayAmount += order.getActuallyAmount();
            }
            if (order.getFinalTime() != null && order.getFinalTime().after(new Timestamp(today.getTime() - 86400000L * 7))) {
                lastWeekAmount += order.getActuallyAmount();
            }
        }


        Map<String, Object> ans = new HashMap<>(5);
        ans.put("todayOrderCount", todayOrderCount);
        ans.put("todayAmount", todayAmount);
        ans.put("orderNum", orderNum);
        ans.put("lastWeekAmount", lastWeekAmount);
        return ans;
    }
}
