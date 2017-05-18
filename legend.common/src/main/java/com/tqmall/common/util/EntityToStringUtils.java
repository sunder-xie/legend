package com.tqmall.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 实体打印工具类
 * Created by Minutch on 14-8-8.
 */
public class EntityToStringUtils {

  private static Logger logger = LoggerFactory.getLogger(EntityToStringUtils.class);

  /**
   * 将实体转化为String
   *
   * @param o 要转化的实体对象
   * @param c 实体类的class类型
   * @return
   */
  public static String toString(Object o, Class<?> c) {
    StringBuilder sb = new StringBuilder();
    sb.append(c.getSimpleName());
    sb.append(":");

    //获取类中所有字段
    Field[] fields = c.getDeclaredFields();
    //遍历字段，获取对应属性值
    for (Field field : fields) {
      if (field != null) {
        field.setAccessible(true);
        try {
          sb.append(field.getName());
          sb.append("=");
          sb.append(field.get(o));
          sb.append(",");
        } catch (Exception e) {
          logger.error("获取字段" + field.getName() + "的值出现异常.", e);
        }
      }
    }
    String result = sb.toString();
    if (result.indexOf(",") >= 0) {
      result = result.substring(0, result.length() - 2);
    }
    return result;
  }
}
