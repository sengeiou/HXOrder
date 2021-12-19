package com.happysnaker.service.impl;

import com.happysnaker.pojo.Discount;
import com.happysnaker.pojo.Dish;
import com.happysnaker.service.MarketingService;
import com.happysnaker.service.base.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/8
 * @email happysnaker@foxmail.com
 */
@Service
public class MarketingServiceImpl extends BaseService implements MarketingService {
    @Override
    public int getSize() {
        return discountMapper.querySize();
    }

    @Override
    public List<Dish> getDiscountDishListByPagination(int pageNum, int pageSize, String keyword, Integer type) {
        int offset = (pageNum - 1) * pageSize;
        System.out.println("size = " + dishMapper.getDiscountDishListByPagination(offset, pageNum, keyword, type).size());
        if (keyword != null) {
            keyword = "%" + keyword + "%";
        }
        return dishMapper.getDiscountDishListByPagination(offset, pageSize, keyword, type);
    }

    @Override
    public void updateDiscount(Discount discount) {
        discountMapper.deleteDiscount(discount.getDishId());
        discountMapper.insertDiscount(discount);
    }

    @Override
    public void addDiscount(Discount discount) {
        discountMapper.insertDiscount(discount);
    }

    @Override
    public void deleteDiscount(int dishId) {
        discountMapper.deleteDiscount(dishId);
    }
}
