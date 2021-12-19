package com.happysnaker.service;

import com.happysnaker.pojo.Dish;

import java.util.List;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
public interface DishService {

    /**
     * 获取菜品的主要信息，并以JSON字符串的形式返回 信息以对象形式组织成JSON，为小程序前端的首页页面提供服务，即热销榜单、店长推荐、新品推荐、优惠套餐
     * @return 返回JSON字符串
     */
    String getIndexDishInfo();

    /**
     * 获取菜品的主要信息，并以JSON字符串的形式返回
     * 信息以对象形式组织成JSON，为小程序前端的点菜页面提供服务
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @return 返回JSON字符串
     */
    String getOrderDishInfo(int storeId);

    /**
     * 获取菜品的主要信息，并以JSON字符串的形式返回
     * 信息以对象形式组织成JSON，为小程序前端的点菜页面提供服务（已废弃）
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @return 返回JSON字符串
     */
    List<Dish> getDishInfo(int storeId);

    /**
     * 获取订餐时菜品所属的分类信息
     * @return 返回JSON字符串
     */
    List<Map<String, String>> getDishClassification();

    /**
     * 获取用户收藏的菜品，并以 Dish 类的 JSON 形式返回，其与 UserService 中实现的功能不同，UserService仅返回由 dishId 封装的 JSONArray，而该函数还会将 Combo 抽象成 Dish 返回，用以小程序端收藏页面的展示
     * @param userId 用户ID
     * @return List&lt;Dish&gt; - JSON
     */
    List<Dish> getUserCollectedDishes(String userId);

}
