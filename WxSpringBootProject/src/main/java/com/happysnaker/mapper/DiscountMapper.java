package com.happysnaker.mapper;

import com.happysnaker.pojo.Discount;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface DiscountMapper {
    /**
     * 查询 discout 表中的所有信息
     * @return 将 Discount 类封装成链表
     */
    List<Discount> queryDiscountInfo();
}
