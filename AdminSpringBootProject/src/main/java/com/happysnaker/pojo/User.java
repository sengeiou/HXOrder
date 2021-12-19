package com.happysnaker.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 这里的 USER 指的是 admin，注意与小程序端区分
 * @author happysnakers
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

  private int id;
  private String username;
  private String password;
  private String nickName;
  private String mail;
  private String avatar;
  private java.sql.Timestamp createTime;
  private java.sql.Timestamp lastLoginTime;
  private int enable;
  private int roleId;
  private int loginStatus;

}
