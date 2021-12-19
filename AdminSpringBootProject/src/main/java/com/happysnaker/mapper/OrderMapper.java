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

    @Select("SELECT * FROM `oms_order`")
    List<Order> queryAllOrder();


    /**
     * 更新原价
     * @param id
     * @param originalPrice
     * @return
     */
    @Update("UPDATE `oms_order` SET original_price = #{val} WHERE id = #{id}")
    int updateShopOriginalPrice(@Param("id") String id, @Param("val") double originalPrice);

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
     * 获取订单是否可支付状态
     * @param id
     * @return
     */
    @Select("SELECT payment_status FROM `oms_order_payment_status` WHERE order_id = #{id}")
    int getPaymentStatus(String id);

    /**
     * 设置订单是否可支付
     * @param id
     * @param val
     */
    @Update("UPDATE `oms_order_payment_status` SET payment_status = #{val} " +
            " WHERE order_id = #{id}")
    int setPaymentStatus(@Param("id") String id, @Param("val") int val);


    /**
     * 设置订单是否可支付
     * @param id
     * @param val
     */
    @Insert("INSERT INTO `oms_order_payment_status` SELECT #{id}, #{val}")
    void insertPaymentStatus(@Param("id") String id, @Param("val") int val);

    /**
     * 逻辑删除某一订单
     * @param id 订单ID
     * @param flag 标志位
     * @return
     */
    @Update("UPDATE `oms_order` SET delete_flag = delete_flag | 2 WHERE id = #{id}")
    int logicalDeleteOrder(@Param("id") String id);

    @Delete("DELETE FROM `oms_order_dish` WHERE order_id = #{oid}")
    int deleteOrderDish(@Param("oid") String oid);

    @Select("SELECT * FROM `oms_cancel_order_apply` WHERE order_id = #{id}")
    OrderApplyTable queryOrderApplyTable(String id);

    @Select("SELECT COUNT(*) FROM oms_order;")
    int getSize();

    List<Order> queryOrderByPagination(@Param("offset") int offset, @Param("num") int num, @Param("keyword") String keyword, @Param("status") Integer type, @Param("bts")Timestamp beginTime, @Param("ets") Timestamp endTime, @Param("storeId") Integer storeId);

    /**
     * 更改订单状态
     * @param orderId ID
     * @param val 新状态
     * @return rows
     */
    @Update("UPDATE `oms_order` SET order_type = #{val} WHERE id = #{orderId}")
    int updateOrderType(@Param("orderId")String orderId, @Param("val") int val);

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

}
