package com.happysnaker.Observer.impl;

import com.happysnaker.Observer.Observer;
import com.happysnaker.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Happysnaker
 * @description
 * @date 2021/11/9
 * @email happysnaker@foxmail.com
 */
public class DishFavoriteNumObserver implements Observer {
    @Autowired
    private DishMapper mapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void action(Object target, Object... args) {
        int dishId = 0, val = 0;
        try {
            dishId = (int) args[0];
            val = (int) args[1];
        } catch (ClassCastException cce) {
            return;
        }
        mapper.updateDishFavoriteNum(dishId, val);
    }
}
