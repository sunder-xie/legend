package com.tqmall.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * do 和 bo 的互相转化类
 * Created by shulin on 16/6/30.
 */
public class BdUtil {
    public static <DO, BO> BO do2bo(DO request, Class<BO> cls) {
        if (null == request) return null;
        BO result;
        try {
            result = cls.newInstance();
            BeanUtils.copyProperties(request, result);
        } catch (Exception e) {
            throw new IllegalArgumentException("对象copy失败，请检查相关module", e);
        }
        return result;
    }

    public static <DO, BO> void do2bo(DO request, BO bo) {
        try {
            BeanUtils.copyProperties(request, bo);
        } catch (Exception e) {
            throw new IllegalArgumentException("对象copy失败，请检查相关module", e);
        }

    }

    public static <DO, BO> List<BO> do2bo4List(List<DO> request, Class<BO> cls) {
        List<BO> result = Lists.newArrayList();
        if (request == null) return result;
        for (DO obj : request) {
            result.add(do2bo(obj, cls));
        }
        return result;
    }

    public static <DO, BO> DO bo2do(BO request, Class<DO> cls) {
        if (null == request) return null;
        DO result;
        try {
            result = cls.newInstance();
            BeanUtils.copyProperties(request, result);
        } catch (Exception e) {
            throw new IllegalArgumentException("对象copy失败，请检查相关module", e);
        }
        return result;
    }

    public static <DO, BO> void bo2do(BO request, DO do1) {

        try {
            BeanUtils.copyProperties(request, do1);
        } catch (Exception e) {
            throw new IllegalArgumentException("对象copy失败，请检查相关module", e);
        }

    }

    public static <DO, BO> List<DO> bo2do4List(Collection<BO> request, Class<DO> cls) {
        List<DO> result = Lists.newArrayList();
        for (BO obj : request) {
            result.add(bo2do(obj, cls));
        }
        return result;
    }

    public static <DO, BO, K> Map<K, DO> bo2do4Map(Map<K, BO> request, Class<DO> cls) {
        Map<K, DO> result = Maps.newHashMap();
        for (Map.Entry<K, BO> item : request.entrySet()) {
            K key = item.getKey();
            BO val = item.getValue();
            result.put(key, bo2do(val, cls));
        }
        return result;
    }

    public static <DO, BO, K> Map<K, BO> do2bo4Map(Map<K, DO> request, Class<BO> cls) {
        Map<K, BO> result = Maps.newHashMap();
        for (Map.Entry<K, DO> item : request.entrySet()) {
            K key = item.getKey();
            DO val = item.getValue();
            result.put(key, bo2do(val, cls));
        }
        return result;
    }

    //将javabean实体类转为map类型，然后返回一个map类型的值
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = Maps.newHashMap();
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                Object object = propertyUtilsBean.getNestedProperty(obj, name);
                if (!"class".equals(name) && object != null) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("javabean实体类转为map类型，请检查", e);
        }
        return params;
    }


    /**
     * 使用反射把 map 转换成对象
     * @param map
     * @param beanClass
     * @return
     */
    public static Object mapToBean(Map<String, Object> map, Class<?> beanClass){
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = beanClass.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                field.set(obj, map.get(field.getName()));
            }
        }catch (Exception e) {
            throw new IllegalArgumentException("map转换javabean实体类异常，请检查", e);
        }
        return obj;
    }

}
