package com.happysnaker.mapper;

import com.happysnaker.pojo.Combo;
import com.happysnaker.pojo.ComboDish;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
    List<ComboDish> queryComboDish();

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

    int insertComboDish(ComboDish comboDish);

    int insertCombo(Combo combo);

    @Delete("DELETE FROM `dms_combo_dish` WHERE combo_id = #{id}")
    int deleteComboDish(int id);

    @Delete("DELETE FROM `dms_combo` WHERE id = #{id}")
    int deleteCombo(int id);

    @Select("SELECT id FROM `dms_combo` WHERE name = #{name}")
    int getIdByName(String name);

    Combo getComboById(int id);

    @Delete("DELETE FROM `dms_combo_dish` WHERE combo_id = #{comboId} AND " +
            "dish_id = #{dishId}")
    int deleteComboDishItem(ComboDish c);
    /**
     * 查询 套餐数目
     * @return
     */
    @Select("SELECT count(*) FROM `dms_combo`")
    int queryComboTotalSize();

    /**
     * 查询套餐的菜品信息
     * @param pageNum 页数
     * @param pageSize 页大小
     * @return
     */
    List<Combo> queryComboByPagination(@Param("offset") int offset, @Param("num") int num, @Param("keyword") String keyword, @Param("status") Integer status);

    /**
     * 更新套餐菜品信息
     * @param comboDish
     * @return
     */
    @Update("UPDATE `dms_combo_dish` SET " +
            "dish_num = #{dishNum} " +
            "WHERE combo_id = #{comboId} ")
    int updateComboDish(ComboDish comboDish);
}
