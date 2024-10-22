package com.happysnaker.mapper;

import com.happysnaker.pojo.Dish;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

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
@Repository
public interface DishMapper {
    @Select("SELECT id FROM dms_dish UNION SELECT id FROM dms_combo")
    List<Integer> queryAllDishId();

    List<Dish> queryDishes();

    /**
     * 查询指定菜品的信息，这个函数仅仅只会读取 dish 表中的信息
     * @param id 菜品ID
     * @return
     */
    Dish queryDishById(@Param("id") int id);

    /**
     * 获取数据库中 dish表 的全部菜品信息, 并连接 dish_tag, discount, dish_stock 表
     * @param storeId 店铺ID，查询库存需要指定店铺
     * @return 以链表的形式封装
     */
    List<Dish> queryDishInfo(@Param("storeId") int storeId);

    /**
     * 获取热销榜单中的菜品信息，通过这个方法返回的 Dish 类只包括 dish 表中的全部信息 和 关联的 tag，不包含 discount 与 dish_stock 信息
     * @param n 表示获取前几的菜品，例如当 n = 10 时，则返回销售量前十的菜品
     * @return 以链表的形式封装
     */
    List<Dish> queryHotSaleDish(@Param("n") int n);

    /**
     * 获取新品推荐榜单中的菜品信息，通过这个方法返回的 Dish 类只包括 dish 表中的全部信息 和 关联的 tag，不包含 discount 与 dish_stock 信息
     * @param n 表示获取前几的菜品，例如当 n = 10 时，则返回销售量前十的菜品
     * @return 以链表的形式封装
     */
    List<Dish> queryNewDish();

    /**
     * 通过dishID获取该菜品的标签信息
     * @param id 菜品的ID
     * @return 菜品的标签信息
     */
    List<String> queryDishTag(@Param("id") int id);

    /**
     * 获取指定店铺中所有菜品的库存
     * @param storeId 店铺的ID
     * @return 返回一个Map，key代表菜品id，val是一个哈希表，其中可以通过 get("stock") 得到对应菜品id的库存量
     */
    @MapKey("dish_id")
    Map<Integer, Map> queryDishInventory(@Param("storeId") int storeId);


    /**
     * 获取 queryClassification 表中的全部信息
     * @return key = 表列名, val = 值
     */
    List<Map<String, String>> queryClassification();


    /**
     * 获取指定店铺中指定菜品的库存量
     * @param dishId 菜品ID
     * @param storeId 店铺OD
     * @return 返回对应的库存量
     */
    Integer getTheDishInventory(@Param("storeId") int storeId, @Param("dishId") int dishId);

    /**
     * 查询店长推荐的菜品，通过这个方法返回的 Dish 类，除了 dish 表中的信息，还应该包括推荐指数和推荐理由、标签等信息
     * @return 菜品链表
     */
    List<Dish> queryRecommendedDish();


    /**
     * 增加或减少 dish 表中 指定菜品的销量
     * @param dishId 菜品ID
     * @param val 增加或者减少的值，当这个值为负时表示减少，所有的更改都是在原值上修改的
     * @return 影响的记录数，如果成功，则为1，否则为0
     */
    @Update("UPDATE dms_dish SET sale = sale + #{val}")
    int updateDishSale(@Param("dishId") int dishId, @Param("val") int val);

    /**
     * 向 dish_sale 表中插入一条日志记录
     * @param dishId 菜品ID
     * @param data 当前日期
     * @param storeId 店铺ID
     * @param val 单日销量
     * @return
     */
    @Insert("INSERT INTO dms_dish_sale SELECT #{data}, #{storeId}, #{val}")
    int insertSaleLog(@Param("data")Timestamp data, @Param("storeId") int storeId, @Param("val") int val);

    /**
     * 增加或减少指定店铺中指定菜品的喜欢数量
     * @param dishId 菜品ID
     * @param val 增加或者减少的值，当这个值为负时表示减少，所有的更改都是在原值上修改的
     * @return 影响的记录数，如果成功，则为1，否则为0
     */
    int updateDishFavoriteNum(@Param("dishId") int dishId, @Param("val") int val);

    /**
     * 增加或减少指定店铺中指定菜品的库存
     * @param storeId 店铺ID
     * @param dishId 菜品ID
     * @param val 增加或者减少的值，当这个值为负时表示减少，所有的更改都是在原值上修改的
     * @return 影响的记录数，如果成功，则为1，否则为0
     */
    int updateDishInventory(@Param("storeId") int storeId, @Param("dishId") int dishId, @Param("val") int val);

    /**
     * 当updateDishInventory方法调用失败时，此时数据库没有该记录，需要插入一条记录
     * @param storeId 店铺ID
     * @param dishId 菜品ID
     * @param val 初始值
     * @return 影响的记录数，如果成功，则为1，否则为0
     */
    int insertDishInventory(@Param("storeId") int storeId, @Param("dishId") int dishId, @Param("val") int val);
}
