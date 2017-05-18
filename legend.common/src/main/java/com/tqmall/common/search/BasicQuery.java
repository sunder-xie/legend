package com.tqmall.common.search;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yuchengdu on 16/9/9.
 */
public class BasicQuery extends LinkedHashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = -8055337885875435924L;

    protected Class<? extends Enum> paramType;

    public BasicQuery() {
    }

    public BasicQuery(Class<? extends Enum<?>> paramType) {
        this.paramType = paramType;
    }

    public Object put(String key, Object value) {

        // 忽略无效参数
        if (key == null || value == null) {
            return null;
        }

        // 检查参数有效性
        if (paramType != null) {
            Enum.valueOf(paramType, (String) key);
        }

        return super.put(key, value);
    }

    protected Object putNoCheck(String key, Object value) {
        return super.put(key, value);
    }

    public Map<String, Object> getParamMap() {
        return Collections.unmodifiableMap(this);
    }

    public boolean hasParam(String name) {
        return containsKey(name);
    }

    public Object getParam(String name) {
        return get(name);
    }

    protected <T, P> T fillSet(String param, Set<P> value) {
        put(param, value);
        return (T) this;
    }

    protected <T, P> T fillSingle(String param, P singleValue) {
        put(param, singleValue);
        return (T) this;
    }
}
