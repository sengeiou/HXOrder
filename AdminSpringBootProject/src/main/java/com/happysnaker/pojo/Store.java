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
@AllArgsConstructor
@NoArgsConstructor
public class Store implements Serializable {
  private int id;
  private String name;
  private String address;
  private String img;
  private String time;
  private int supportTakeout;
  private String contactPhone;
  private int working;
  private String longitude;
  private String latitude;
  List<StoreTable> tables;
}
