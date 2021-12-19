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
public class ApiTable implements Serializable {

  private int id;
  private String uri;
  private String desc;

}
