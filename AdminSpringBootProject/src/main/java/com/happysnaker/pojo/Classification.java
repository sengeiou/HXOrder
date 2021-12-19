package com.happysnaker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Classification  implements Serializable {
  private int id;
  private String name;
  private String icon;
}
