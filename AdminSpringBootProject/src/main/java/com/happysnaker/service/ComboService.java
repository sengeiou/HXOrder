package com.happysnaker.service;

import com.happysnaker.pojo.Combo;
import com.happysnaker.pojo.ComboDish;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/7
 * @email happysnaker@foxmail.com
 */
public interface ComboService {

    /**
     * 查询套餐
     * @param pageNum 页号
     * @param pageSize 页大小
     * @param keyword 菜品名称关键字
     * @param publishStatus 上架状态
     * @return
     */
    List<Combo> getComboListByPagination(int pageNum, int pageSize, String keyword, Integer publishStatus);

    /**
     * 获取所有套餐数量
     * @return
     */
    int getComboSize();

    /**
     * 添加
     * @param combo
     * @return
     */
    int addCombo(Combo combo);

    void deleteCombo(int comboId);

    void updateCombo(Combo combo);

    /**
     * 更改套餐的上架状态
     * @param dishIds
     * @param val 1 OR 0
     */
    void updateComboStatus(int id, int val);

    default void updateComboStatus(List<Integer> ids, int val) {
        for (Integer id : ids) {
            updateComboStatus(id, val);
        }
    };

    default void deleteCombo(List<Integer> ids) {
        for (Integer id : ids) {
            deleteCombo(id);
        }
    };

    /**
     * 获取套餐对应的菜品
     * @param id
     * @return
     */
    List<ComboDish> getComboDishList(int id);

    void updateComboDish(List<ComboDish> comboDishes);

    Combo getCombo(int id);

    void deleteComboDish(ComboDish c);
}
