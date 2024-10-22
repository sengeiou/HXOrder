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
  private final int DISCOUNT = 1;
  private final int INSTANT_REDUCTION = 2;

  private int dishId;
  private int type;
  private int val;
  private int count;
  private int unit;

  public double getDiscount(double price) {
    if (type == DISCOUNT) {
      return price * (double) (10 - val) / 10f;
    } else if (type == INSTANT_REDUCTION) {
      return val;
    }
    return 0;
  }

}
