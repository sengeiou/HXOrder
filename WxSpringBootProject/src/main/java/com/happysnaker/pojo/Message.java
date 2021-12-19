package com.happysnaker.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author happysnakers
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

  private int id;
  private String title;
  private String content;
  /**
   * type = 0时，表示为系统消息；type = 1时，表示为店铺管理员发送给用户的站内信
   */
  private int type;
  private int storeId;
  private String userId;
  private java.sql.Timestamp createTime;

  public static Message createSystemMessage(String content, String userId) {
    return new Message(0, "系统通知", content, 0, 0, userId, new Timestamp(System.currentTimeMillis()));
  }

  public static Message createSystemMessage(String title, String content, String userId) {
    return new Message(0, title, content, 0, 0, userId, new Timestamp(System.currentTimeMillis()));
  }

}
