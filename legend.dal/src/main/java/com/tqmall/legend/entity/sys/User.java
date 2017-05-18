package com.tqmall.legend.entity.sys;

import lombok.*;

@EqualsAndHashCode(callSuper = false)
@Data
public class User {
  
  private String username;
  private String password;
  private Integer enabled;
}