package com.tqmall.legend.biz.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 实体对象工具类
 *
 * Created by gry on 2014/8/14.
 *
 */
public class EntityUtil {

    private static Logger logger = LoggerFactory.getLogger(EntityUtil.class);
    /**
     *
     * 将list中的元素放到Map<K, V>以建立 key - value 索引<p>
     *
     * @param list  List<V> 元素列表
     * @param keyFieldName String 元素的属性名称, 该属性的值作为索引key
     * @param <K> key类型
     * @param <V> value类型
     * @return Map<K, V> key - value 索引
     *
     * Created by gry on 2014/8/14.
     *
     */
  @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> makeEntityMap(List<V> list, String keyFieldName) {
        Map<K, V> map = new HashMap<>();
        if(list == null || list.size() == 0) {
            return map;
        }
        try {
            Method getter = getMethod(list.get(0).getClass(), keyFieldName, "get");
            for (V item : list) {
                map.put((K) getter.invoke(item), item);
            }
        } catch (Exception e) {
            logger.error("makeEntityMap error list is " + list, e);
            return map;
        }
        return map;
    }

    /**
     *
     * 将list中的元素放到Map<K, List<V>> 以建立 key - List<value> 索引<p>
     *
     * @param list  List<V> 元素列表
     * @param keyFieldName String 元素的属性名称, 该属性的值作为索引key
     * @param <K> key类型
     * @param <V> value类型
     * @return Map<K, V> key - value 索引
     *
     * Created by gry on 2014/8/14.
     *
     */
    public static <K, V> Map<K, List<V>> makeEntityListMap(List<V> list, String keyFieldName) {
        Map<K, List<V>> map = new HashMap<>();
        if(list == null || list.size() == 0) {
            return map;
        }
        try {
            Method getter = getMethod(list.get(0).getClass(), keyFieldName, "get");
            for (V item : list) {
        @SuppressWarnings("unchecked")
                K key = (K) getter.invoke(item);
                List<V> groupList = map.get(key);
                if (groupList == null) {
                    groupList = new ArrayList<>();
                    map.put(key, groupList);
                }
                groupList.add(item);
            }
        } catch (Exception e) {
            logger.error("makeEntityListMap error list is " + list, e);
            return map;
        }
        return map;
    }

    /**
     * 获取getter或setter
     */
  @SuppressWarnings("unchecked")
  private static Method getMethod(@SuppressWarnings("rawtypes") Class clazz, String fieldName,
      String methodPrefix) throws NoSuchMethodException {
        String first = fieldName.substring(0, 1);
        String getterName = methodPrefix + fieldName.replaceFirst(first, first.toUpperCase());
        return clazz.getMethod(getterName);
    }

}
