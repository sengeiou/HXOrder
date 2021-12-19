package com.happysnaker.mapper;

import com.happysnaker.pojo.Order;
import com.happysnaker.pojo.OrderApplyTable;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface OrderMapper {

    /**
     * 根据支付订单号查询 orderId
     * @param payId 支付单号
     * @return
     */
    @Select("SELECT order_id FROM `oms_order_pay` WHERE pay_id = #{payId}")
    String queryOrderId(String payId);

    /**
     * 插入保证金
     * @param oid
     * @param val
     * @return
     */
    @Insert("INSERT INTO `oms_order_margin` SELECT #{id}, #{val}")
    int insertMargin(@Param("id") String oid, @Param("val") double val);

    /**
     * 插入订单支付表
     * @param orderId 订单ID
     * @param payId 支付单号
     * @return
     */
    @Insert("INSERT INTO `oms_order_pay` SELECT #{oid}, #{pid}")
    int insertOrderPay(@Param("oid") String orderId,@Param("pid") String payId);

    /**
     * 删除
     * @param id
     * @return
     */
    @Delete("DELETE FROM `oms_order` WHERE id = #{id}")
    int deleteOrder(String id);

    /**
     * 更新其他费用
     * @param id
     * @param fee
     * @return
     */
    @Update("UPDATE `oms_order` SET other_fee = #{fee} WHERE id = #{id}")
    int updateOtherFee(@Param("id") String id, @Param("fee") double fee);

    @Update("UPDATE `oms_order` SET shop_discount = #{val} WHERE id = #{id}")
    int updateShopDiscount(@Param("id") String id, @Param("val") double shopDiscount);

    /**
     * 更新原价
     * @param id
     * @param originalPrice
     * @return
     */
    @Update("UPDATE `oms_order` SET original_price = #{val} WHERE id = #{id}")
    int updateShopOriginalPrice(@Param("id") String id, @Param("val") double originalPrice);


    /**
     * 更改订单状态
     * @param orderId ID
     * @param t 结束时间
     * @return rows
     */
    @Update("UPDATE `oms_order` SET final_time = #{ts} WHERE id = #{orderId}")
    int updateOrderFinalTime(@Param("orderId")String orderId, @Param("ts")Timestamp t);

    /**
     * 更改订单状态
     * @param orderId ID
     * @param val 新状态
     * @return rows
     */
    @Update("UPDATE `oms_order` SET order_type = #{val} WHERE id = #{orderId}")
    int updateOrderType(@Param("orderId")String orderId, @Param("val") int val);


    /**
     * 插入退货申请
     * @param at
     * @return
     */
    @Insert("INSERT INTO `oms_cancel_order_apply` " +
            "SELECT #{orderId}, #{userId}, #{phone}, #{reason}, #{oldOrderType}")
    int insertCancelApply(OrderApplyTable at);


    /**
     * 删除退货申请
     * @param orderId
     * @return
     */
    @Insert("DELETE FROM `oms_cancel_order_apply` WHERE order_id = #{orderId}")
    int deleteCancelApply(String orderId);

    /**
     * 逻辑删除订单
     * @param orderId ID
     * @return
     */
    @Update("UPDATE `oms_order` SET delete_flag = delete_flag | 1 WHERE id = #{id}")
    int logicalDelete(@Param("id") String orderId);

    /**
     * 通过订单ID查询订单信息，该函数仅查询 order 表中的信息
     * @param orderId 订单ID
     * @return Order类
     */
    Order queryOrder(@Param("orderId") String orderId);

    /**
     * 通过订单ID查询订单菜品信息，该函数仅查询 order_detail 表中的信息，并将 dish_name, num, price 三个属性封装至 map 中，封装后 map 的 key 为 dishName, dishNum, dishPrice，而 val 则为对应的值
     * @param orderId 订单ID
     * @return Map
     */
    List<Map<String, Object>> queryOrderDish(@Param("id") String orderId);


    /**
     * 查询用户的所有订单
     * @param userId 用户ID
     * @return 返回Order类，但不包括具体Dishes
     */
    List<Order> queryOrdersByUserId(@Param("userId") String userId);

    /**
     * 向 order 表中插入数据
     * @param order 实体类
     * @return 影响的行数
     */
    int insertOrderInfo(Order order);


    /**
     * 向 order_dish 表中插入数据
     * @param orderId 订单ID
     * @param map 其中的一个菜品，key = {dishName, dishNum, doshPrice}, val为对应的值
     * @return 影响的行数
     */
    int insertOrderDish(@Param("orderId") String orderId, @Param("map") Map<String, Object> map);

    /**
     * 插入取餐凭证
     * @param orderId
     * @param code
     * @return
     */
    @Insert("INSERT INTO `oms_fetch_meal_code` SELECT #{id}, #{code}")
    int insertFetchMealCode(@Param("id") String orderId, @Param("code") String code);

}
