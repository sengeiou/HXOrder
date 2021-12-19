package com.happysnaker.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author happysnakers
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Combo implements Serializable {
  private int id;
  private String name;
  private double price;
  private int sale;
  private int likeNum;
  private List<Integer> classificationIds;
  private int publishStatus;
  private List<ComboDish> comboDish;
  private List<String> tags;
  private List<String> imgs;
  private Discount discount;
}
