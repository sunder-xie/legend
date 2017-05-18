package com.tqmall.legend.biz.common;

import java.util.HashMap;
import java.util.Map;

/**
 *  访问上下文：用户相关及常用信息
 *
 * Created by gry on 2014/7/18.
 */
public class Context {

    private Map<String, Object> attributes = new HashMap<>();

    private Integer Id = null;
    private Integer currentUserId = null;
    private String currentUserName = null;


    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public void put(String key, Object value) {
        attributes.put(key, value);
    }

    public Object get(String key) {
        return attributes.get(key);
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

}
