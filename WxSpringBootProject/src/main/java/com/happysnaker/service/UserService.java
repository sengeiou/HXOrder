package com.happysnaker.service;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
public interface UserService {

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return User - JSON
     */
    String getUserInfo(String userId);

    /**
     * 获取用户标记的菜品，这包括喜欢、收藏、待选
     * @param userId 用户id
     * @return JSON字符串，其中菜品仅返回 ID
     */
    String getUserMarkedDish(String userId);

    /**
     * 获取用户收藏的店铺ID集合
     * @param userId 用户ID
     * @return List - JSON
     */
    String getCollectedStores(String userId);

    /**
     * 获取用户在某个店对于菜品已享受折扣的次数
     * @param userId 用户ID
     * @param storeId 菜品ID
     * @return Map - JSON
     */
    String getUsedDiscountCount(String userId);

    /**
     * 添加用户喜欢菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return "OK" if OK, null if fail
     */
    String addUserLikeDish(String userId, int dishId);

    /**
     * 添加用户收藏菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return "OK" if OK, null if fail
     */
    String addUserCollectedDish(String userId, int dishId);


    /**
     * 添加用户待选菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return "OK" if OK, null if fail
     */
    String addUserWillBuyDish(String userId, int dishId);


    /**
     * 添加用户收藏店铺
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @return "OK" if OK, null if fail
     */
    String addUserCollectedStore(String userId, int storeId);



    /**
     * 移除一个用户喜欢菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return "OK" if OK, null if fail
     */
    String removeUserLikeDish(String userId, int dishId);

    /**
     * 移除一个用户收藏菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return "OK" if OK, null if fail
     */
    String removeUserCollectedDish(String userId, int dishId);


    /**
     * 移除一个用户待选菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return "OK" if OK, null if fail
     */
    String removeUserWillBuyDish(String userId, int dishId);


    /**
     * 移除一个用户收藏店铺
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @return "OK" if OK, null if fail
     */
    String removeUserCollectedStore(String userId, int storeId);


}
