package com.happysnaker.service;

import com.happysnaker.pojo.Classification;
import com.happysnaker.pojo.Dish;

import java.util.List;
import java.util.Map;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/5
 * @email happysnaker@foxmail.com
 */
public interface DishService {

    /**
     * 查询菜品
     * @param pageNum 页号
     * @param pageSize 页大小
     * @param keyword 菜品名称关键字
     * @param publishStatus 上架状态
     * @return
     */
    List<Dish> getDishListByPagination(int pageNum, int pageSize, String keyword, Integer publishStatus, Integer recommendStatus, Integer newStatus);

    int getDishSize();

    Dish getDish(int id);

    void updateDish(Dish dish);

    /**
     * 更新菜品状态
     * @param dishIds
     * @param options 1-上架状态, 2-推荐状态，3-新品状态
     * @param val 1 或 0
     */
    default void updateDishStatus(List<Integer> dishIds,  int val, int options) {
        for (Integer dishId : dishIds) {
            updateDishStatus(dishId, val, options);
        }
    }

    /**
     * 更新菜品状态
     * @param dishId
     * @param options 1-上架状态, 2-推荐状态，3-新品状态
     * @param val 1 或 0
     */
    void updateDishStatus(int dishId,  int val, int options);

    /**
     * 获取菜品在所有店铺中的库存信息
     * @param dishId
     * @return map -> key1:storeId, key2:storeName, key3: stock
     */
    List<Map> getDishStockList(int dishId);


    /**
     * 获取菜品在所有店铺中的库存信息
     * @param maps
     * @return 同上返回值
     */
    void updateDishStockList(List<Map> maps);

    List<Classification> getClassificationList();

    /**
     * 添加菜品
     * @param dish
     */
    int addDish(Dish dish);

    /**
     * 删除菜品
     * @param dishId
     */
    void deleteDish(int dishId);

    /**
     * 删除菜品
     * @param dishId
     */
    default void deleteDish(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteDish(id);
        }
    };
}
