package com.happysnaker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author happysnakers
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order  implements Serializable {
  private boolean isNew;

  // lombok 离奇失效
  public boolean getIsNew() {
    return isNew;
  }
  public void setIsNew(boolean b) {
    this.isNew = b;
  }


  private String id;
  private int orderType;
  private int storeId;
  private String userId;
  private String payType;
  private String payId;
  private int consumeType;
  private int table;
  private int deleteFlag;
  private int paymentStatus;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp finalTime;
  private double originalPrice;
  private double shopDiscount;
  private double otherFee;
  private double couponDiscount;
  private double margin;
  private int addressId;
  private Address address;
  private java.sql.Timestamp expectedTime;
  private int taste;
  private String fetchMealCode;
  private String remark;
  /**
   * Map -> dishId: v; dishName: v; dishPrice: v; dishNum: v
   */
  private List<Map<String, Object>> dishOrders;
  private OrderApplyTable applyTable;
}
