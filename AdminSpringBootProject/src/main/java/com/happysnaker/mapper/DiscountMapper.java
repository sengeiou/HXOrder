package com.happysnaker.mapper;

import com.happysnaker.pojo.Discount;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author Happysnaker
 * @description
 * @date 2021/12/6
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface DiscountMapper {
    @Insert(" INSERT INTO `mms_discount`(`dish_id`,`type`,`val`,`count`,`unit`) " +
            "SELECT #{dishId},#{type},#{val},#{count},#{unit}")
    int insertDiscount(Discount discount);

    @Delete("DELETE FROM `mms_discount` WHERE dish_id = #{dishId}")
    int deleteDiscount(int dishId);

    @Select("SELECT count(*) FROM `mms_discount`")
    int querySize();
}
