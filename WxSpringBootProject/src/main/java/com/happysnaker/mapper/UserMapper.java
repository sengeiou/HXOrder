package com.happysnaker.mapper;

import com.happysnaker.pojo.User;
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
public interface UserMapper {
    /**
     * 更新积分
     * @param userId
     * @param delta
     * @return
     */
    @Update("UPDATE `ums_user` SET integral = integral + #{val}")
    int updateUserPoints(@Param("id") String userId,@Param("val") int delta);

    @Select("SELECT id FROM `ums_user`")
    List<String> queryAllUserIds();

    /**
     * 记录用户上一次登录
     */
    @Insert("INSERT INTO ums_user_login_log SELECT userId, time, ip")
    void userLog(@Param("userId") String userId, @Param("ip") String ip, @Param("time") Timestamp time);


    /**
     * 根据 id 查询 User 表中的所有信息
     * @param userId 用户ID
     * @return User
     */
    User queryUser(@Param("userId") String userId);

    /**
     * 是否存在用户
     * @param userId 用户id
     * @return 是-true，否-false
     */
    default boolean hasUser(String userId) {
        return queryUser(userId) != null;
    }

    /**
     * 根据 userID 查询用户收藏的菜品
     * @param userId 用户ID
     * @return 由菜品ID封装成的链表
     */
    List<Integer> queryCollectedDish(@Param("userId") String userId);

    /**
     * 根据 userID 查询用户收藏的店铺
     * @param userId 用户ID
     * @return 由店铺ID封装成的链表
     */
    List<Integer> queryCollectedStore(@Param("userId") String userId);

    /**
     * 根据 userID 查询用户喜欢的菜品
     * @param userId 用户ID
     * @return 由菜品ID封装成的链表
     */
    List<Integer> queryFavoriteDish(@Param("userId") String userId);

    /**
     * 根据 userID 查询用户待选的菜品
     * @param userId 用户ID
     * @return 由菜品ID封装成的链表
     */
    List<Integer> queryWillBuyDish(@Param("userId") String userId);


    /**
     * 返回用户在指定店铺中已经享受过菜品的折扣的次数
     * @param userId 用户ID
     * @return key 是对应的 dishId，val是用户对于该菜品已享受折扣的次数，由于mybatis的缘故，此处val为一个map，通过调用 map.get("count")以得到次数
     */
    @MapKey("dish_id")
    Map<Integer, Map> queryUserUsedDiscountCountInAllDish(@Param("userId") String userId);

    /**
     * 更新用户已经享受过菜品的折扣的次数
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @param newVal 新的值
     * @return row
     */
    int updateUsedDiscountCountByANewVal(@Param("userId") String userId,  @Param("dishId") int dishId, @Param("val") int newVal);

    /**
     * 更新用户在指定店铺中已经享受过菜品的折扣的次数
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @param val 要增加的值，值为负表示减少
     * @return row
     */
    int updateUsedDiscountCount(@Param("userId") String userId, @Param("dishId") int dishId, @Param("val") int val);

    /**
     * 插入用户在指定店铺中已经享受过菜品的折扣的次数
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @param dishId 菜品ID
     * @param newVal 新的值
     * @return row
     */
    int insertUsedDiscountCount(@Param("userId") String userId,  @Param("dishId") int dishId, @Param("val") int newVal);

    /**
     * 添加一个用户
     * @param user user
     * @return row
     */
    int addUser(User user);

    /**
     * 修改一个用户，以ID唯一表示，尝试更新除ID字段外的所有列
     * @param user user
     * @return row
     */
    int updateUser(User user);

    /**
     * 向用户收藏列表中添加一个菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return 成功返回 1，失败返回 0
     */
    int addCollectedDish(@Param("userId") String userId, @Param("dishId") int dishId);

    /**
     * 向用户收藏列表中添加一个店铺
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @return 成功返回 1，失败返回 0
     */
    int addCollectedStore(@Param("userId") String userId, @Param("storeId") int storeId);

    /**
     * 向用户喜欢列表中添加一个菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return 成功返回 1，失败返回 0
     */
    int addFavoriteDish(@Param("userId") String userId, @Param("dishId") int dishId);

    /**
     * 向用户待选列表中添加一个菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return 成功返回 1，失败返回 0
     */
    int addWillBuyDish(@Param("userId") String userId, @Param("dishId") int dishId);


    /**
     * 向用户收藏列表中删除一个菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return 成功返回 1，失败返回 0
     */
    int removeCollectedDish(@Param("userId") String userId, @Param("dishId") int dishId);

    /**
     * 向用户收藏列表中删除一个店铺
     * @param userId 用户ID
     * @param storeId 店铺ID
     * @return 成功返回 1，失败返回 0
     */
    int removeCollectedStore(@Param("userId") String userId, @Param("storeId") int storeId);

    /**
     * 向用户喜欢列表中删除一个菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return 成功返回 1，失败返回 0
     */
    int removeFavoriteDish(@Param("userId") String userId, @Param("dishId") int dishId);

    /**
     * 向用户待选列表中删除一个菜品
     * @param userId 用户ID
     * @param dishId 菜品ID
     * @return 成功返回 1，失败返回 0
     */
    int removeWillBuyDish(@Param("userId") String userId, @Param("dishId") int dishId);
}
