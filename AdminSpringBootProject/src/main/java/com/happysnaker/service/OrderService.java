package com.happysnaker.service;

import com.happysnaker.exception.UpdateException;
import com.happysnaker.pojo.Message;
import com.happysnaker.pojo.Order;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/9
 * @email happysnaker@foxmail.com
 */
public interface OrderService {
    /**
     * 获取下一阶段的订单状态
     * <p>consume_type 0 对应扫码点餐  经历流程：待点餐 - 确认中 - 待支付 - 已完成<br/>
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
        String[] ss = new String[]{"0217", "#23417", "12357", "12367"};
        String s = ss[consumeType];
        int nowIndex = s.indexOf(String.valueOf(orderType));
        return s.charAt(nowIndex + 1) - '0';
    }
    public static String[] typeMap = new String[]{"待支付保证金", "待支付", "确认中", "备餐中", "待用餐", "待取餐", "配送中", "已完成", "取消中", "已取消"};
    /**待支付保证金*/
    int TO_BE_ORDER_STATUS = 0;
    /**待支付*/
    int TO_BE_PAID_STATUS = 1;
    /**确认中*/
    int CONFIRMING_STATUS = 2;
    /**
    备餐中
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
     * 查询订单
     * @param pageNum 页号
     * @param pageSize 页大小
     * @param keyword 菜品名称关键字
     * @param type 订单状态
     * @param creatTime 创建时间
     * @param storeId
     * @return
     */
    List<Order> getOrderListByPagination(int pageNum, int pageSize, String keyword, Integer type, Timestamp creatTime, Integer storeId);

    /**
     * 删除订单
     * @param id
     * @return
     */
    void deleteOrder(String id) throws UpdateException;

    /**
     * 更新订单状态
     * @param id
     * @param type
     * @return
     */
    void updateOrderType(String id, int type) throws UpdateException;


    /**
     * 批量删除
     * @param ids
     * @return
     */
    default void deleteOrder(String[] ids) throws UpdateException {
        for (String id : ids) {
            deleteOrder(id);
        }
    }

    /**
     * 获取订单
     * @param id
     * @return
     */
    Order getOrder(String id);

    /**
     * 确认订单
     * @param order
     */
    void confirmOrder(Order order) throws UpdateException;

    /**
     * 获取订单是否可支付状态
     * @param id
     * @return
     */
    boolean getPaymentStatus(String id);

    /**
     * 处理待支付订单，并设置订单是否可支付
     * @param val
     * @param order
     */
    void handingAndSetPaymentStatus(Order order,  boolean val);

    /**
     * 发送站内信
     * @param message
     */
    void sendMessage(Message message);

    /**
     * 获取 size
     * @return
     */
    int getSize();
}

