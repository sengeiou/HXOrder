package com.happysnaker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author happysnakers
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Address {
  private int id;
  private String userId;
  private String name;
  private String phone;
  private String address;
}
