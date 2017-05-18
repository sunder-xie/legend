package com.tqmall.legend.log;

import java.util.List;
import java.util.Map;

/**
 * Created by xin on 2017/5/9.
 */
public class UserOperateParams {

    private List<String> condition;

    private Map<String, Object> paramMap;

    public UserOperateParams(List<String> condition, Map<String, Object> paramMap) {
        this.condition = condition;
        this.paramMap = paramMap;
    }

    public List<String> getCondition() {
        return condition;
    }

    public void setCondition(List<String> condition) {
        this.condition = condition;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }
}
