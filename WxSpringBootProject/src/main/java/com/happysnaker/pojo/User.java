package com.happysnaker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Happysnaker
 * @description
 * @date 2021/10/22
 * @email happysnaker@foxmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private String id;
    private int rank;
    private int integral;
    private int wallet;
    private String name;
    private String phone;
    private List<Integer> collectedShops;
    private List<Integer> collectedDishes;
    private List<Integer> favoriteDishes;
    private List<Integer> willBuyDishes;

    public User(String id) {
        this.id = id;
    }
}
