package com.happysnaker.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu implements Serializable {
  private int id;
  private int parentId;
  private String name;
  private String title;
  private String icon;
  private boolean hidden;

  List<Menu> children;
}
