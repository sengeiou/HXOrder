package com.happysnaker.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author happysnakers
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Discount implements Serializable {
  public static final int DISCOUNT = 1;
  public static final int INSTANT_REDUCTION = 2;

  private int dishId;
  private int type;
  private int val;
  private int count;
  private int unit;
}
