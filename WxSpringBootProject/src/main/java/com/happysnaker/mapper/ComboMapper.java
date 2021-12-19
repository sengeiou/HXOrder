package com.happysnaker.mapper;

import com.happysnaker.pojo.Combo;
import com.happysnaker.pojo.ComboDish;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 由于数据库的设计，其中诸如查询冻结数量等方法可共用 DishMapper 中的方法
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface ComboMapper {
    /**
     * 这个函数将查询所有的 combo 表中的信息，封装成链表返回。
     * 请注意这个函数包含了 queryComboDish
     * @return 封装Combo类形成的链表
     */
    List<Combo> queryComboInfo();

    /**
     * map中存放着 combo_dish 表中的信息，key1是字段名，我们将其映射为dishId、dishNum、comboId, val1是对应的值
     * @return Map
     */
    @MapKey("combo_id")
    List<Map<String, Object>> queryComboDish();

    /**
     * 获取套餐菜品
     * @param comboId 套餐Id
     * @return key是字段名，我们将其映射为dishId、dishNum、comboId, val是对应的值
     */
    List<ComboDish> queryComboDishById(@Param("comboId") int comboId);


    /**
     * 通过comboID获取该菜品的标签信息
     * @param id 菜品的ID
     * @return 菜品的标签信息
     */
    List<String> queryDishTag(@Param("id") int id);

}
