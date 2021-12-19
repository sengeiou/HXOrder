package com.happysnaker.service;

import com.happysnaker.pojo.Discount;
import com.happysnaker.pojo.Dish;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/8
 * @email happysnaker@foxmail.com
 */
public interface MarketingService {

    int getSize();

    /**
     * 查询折扣菜品
     * @param pageNum 页号
     * @param pageSize 页大小
     * @param keyword 菜品名称关键字
     * @param type 折扣类型
     * @return
     */
    List<Dish> getDiscountDishListByPagination(int pageNum, int pageSize, String keyword, Integer type);

    void updateDiscount(Discount discount);

    void addDiscount(Discount discount);

    void deleteDiscount(int dishId);

    default void deleteDiscount(List ids) {
        for (Object id : ids) {
            deleteDiscount((Integer) id);
        }
    };
}
