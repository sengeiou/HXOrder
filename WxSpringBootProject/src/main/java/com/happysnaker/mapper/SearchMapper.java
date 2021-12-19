package com.happysnaker.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Mapper
public interface SearchMapper {
    /**
     * 通过名字搜索菜品，这个名字是相似的
     * @param name 一个相似的名字
     * @return 菜品ID 封装成 List
     */
    List<Integer> queryDishByName(@Param("name") String name);

    /**
     * 通过标签搜索菜品，这个标签是相似的
     * @param name 一个相似的标签
     * @return 菜品ID 封装成 List
     */
    List<Integer> queryDishByTag(@Param("name") String name);

    /**
     * 通过地名搜索店铺，这个地名是相似的
     * @param addressName 一个相似的地名
     * @return 店铺ID 封装成 List
     */
    List<Integer> queryStoreByName(@Param("name") String addressName);
}
