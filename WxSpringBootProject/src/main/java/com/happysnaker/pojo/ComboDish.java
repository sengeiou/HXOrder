package com.happysnaker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author happysnakers
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComboDish implements Serializable {
  private int comboId;
  private int dishId;
  private int dishNum;
  private int dishPrice;
  private boolean isAdd;
  private String dishName;
  private String dishImg;
}
