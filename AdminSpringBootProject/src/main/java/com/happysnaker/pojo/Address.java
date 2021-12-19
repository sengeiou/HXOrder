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
public class Address  implements Serializable {
  private int id;
  private String name;
  private String phone;
  private String address;
  private String userId;
}
