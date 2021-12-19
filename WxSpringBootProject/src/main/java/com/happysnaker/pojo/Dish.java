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
public class Dish implements Serializable {
  private int id;
  private String name;
  private double price;
  private int publishStatus;
  private int recommendStatus;
  private String reason;
  private int stars;
  private int newStatus;
  private int sale;
  private int likeNum;
  private String desc;
  private String dishImg;
  private String mainIngredient;
  private String ingredient;
  private String makeMethod;
  private double weight;
  private double makeTime;
  private List<Integer> classificationIds;
  private int stock;
  private List<String> tags;
  private Discount discount;
}
