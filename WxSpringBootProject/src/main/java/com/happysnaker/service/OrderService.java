package com.happysnaker.service;

import com.happysnaker.exception.OrderAddException;
import com.happysnaker.exception.ReadWriterLockException;
import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Order;
import com.happysnaker.pojo.OrderApplyTable;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
public interface OrderService {
    /**
     * 获取下一阶段的订单状态
     * <p>consume_type 0 对应扫码点餐  经历流程：确认中 - 待支付 - 已完成<br/>
     * <p>
     * consume_type 1 对应到店消费 经历流程：支付保证金 - 确认中 - 备餐中 - 待用餐 - 待支付 - 已完成<br/>
     * <p>
     * consume_type 2 对应到店自取 经历流程：待支付 - 确认中 - 备餐中 - 待取餐 - 已完成<br/>
     * <p>
     * consume_type 3 对应外卖 经历流程：待支付 - 确认中 - 备餐中 - 配送中 - 已完成<br/>
     * <p>
     * orderType: 0待点餐, 1待支付, 2确认中，3备餐中，4待用餐，5待取餐，6配送中，7已完成，8取消确认中，9已取消<br/>
     * </p>
     *
     * @param consumeType 消费类型
     * @param orderType 订单状态
     * @return
     */
    default int getNextStatus(int consumeType, int orderType) {
        //如果消费类型是 -1，说明前端不想让我们正常走到下个状态，那么以前端传递过来的状态为准
        if (consumeType == -1) {
            return orderType;
        }
        if (orderType == CANCELING_STATUS) {
            return CANCELLED_STATUS;
        }
        String[] ss = new String[]{"217", "023417", "12357", "12367"};
        String s = ss[consumeType];
        int nowIndex = s.indexOf(String.valueOf(orderType));
        return s.charAt(nowIndex + 1) - '0';
    }

    public static String[] typeMap = new String[]{"待点餐", "待支付", "确认中", "备餐中", "待用餐", "待取餐", "配送中", "已完成", "取消中", "已取消"};
    /**
     * 待支付保证金
     */
    int TO_BE_PAID_MARGIN_STATUS = 0;
    /**
     * 待支付
     */
    int TO_BE_PAID_STATUS = 1;
    /**
     * 确认中
     */
    int CONFIRMING_STATUS = 2;
    /**
     * 备餐中
     */
    int PREPARING_MEAL_STATUS = 3;
    /**
     * 待用餐
     */
    int TO_HAVE_A_MEAL_STATUS = 4;
    /**
     * 待取餐
     */
    int MEAL_WAITING_STATUS = 5;
    /**
     * 配送中
     */
    int IN_DELIVERY_STATUS = 6;
    /**
     * 已完成
     */
    int COMPLETED_STATUS = 7;
    /**
     * 取消中
     */
    int CANCELING_STATUS = 8;
    /**
     * 已取消
     */
    int CANCELLED_STATUS = 9;


    /**
     * 更改订单的状态
     *
     * @param orderId   订单ID
     * @param newStatus 新的状态
     * @return "ok" if ok, null else
     */
    void updateOrderType(String orderId, int newStatus) throws UpdateException;

    void updateFinalTime(String orderId, Timestamp ts) throws UpdateException;

    /**
     * 获取用户的订单信息
     *
     * @param userId 用户ID
     * @return List Form TO JSON
     */
    String getUserOrders(String userId);

    boolean pay(String payId) throws UpdateException;

    void cancelPay(String orderId) throws UpdateException;

    /**
     * 添加一个订单
     * @param userId 用户ID
     * @param form   待接受的订单
     * @return
     */
    Map addUserOrder(String userId, Order form) throws OrderAddException, ReadWriterLockException;


    /**
     * 获取用户需要等待的时间
     *
     * @param storeId 店铺ID
     * @return 返回一个JSON: {’time‘: val}，val 为需要等待的时间(分钟)
     */
    double getWaitingTime(int storeId);


    void deleteOrder(String id);

    void cancelOrder(OrderApplyTable at);
}
