package com.tqmall.common.template;


import java.io.Serializable;

/**
 * Created by yuchengdu on 16/9/9.
 * @param <T> 返回数据类型
 * @param <F> 入参
 */
public class PageVO<T,F> implements Serializable {
    public T getResultList() {
        return resultList;
    }

    public void setResultList(T resultList) {
        this.resultList = resultList;
    }

    public F getParam() {
        return param;
    }

    public void setParam(F param) {
        this.param = param;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    private T resultList;
    private F param;
    private int totalCount;
    public PageVO(T result,F param,int totalCount){
        this.resultList=result;
        this.param=param;
        this.totalCount=totalCount;
    }
}
