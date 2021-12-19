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
public class OrderApplyTable implements Serializable {

  private String orderId;
  private String userId;
  private String phone;
  private String reason;
  private int oldOrderType;
}
