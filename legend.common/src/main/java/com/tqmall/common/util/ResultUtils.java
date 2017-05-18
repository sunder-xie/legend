package com.tqmall.common.util;

import com.tqmall.legend.common.Result;

/**
 * Created by wanghui on 12/1/15.
 */
public class ResultUtils {
    /**
     * 将Tqmall传过来的result转换为legend的result的
     * @param result
     * @return
     */
    public final static <T> Result convertToResult(com.tqmall.core.common.entity.Result result){
        if (result == null) {
            return null;
        }

        Result<T> r = new Result<T>();
        r.setCode(result.getCode());
        r.setData((T)result.getData());
        r.setErrorMsg(result.getMessage());
        r.setSuccess(result.isSuccess());
        return r;
    }

    /**
     * 将legend的result转换为对外的result的
     * @param result
     * @return
     */
    public final static <T> com.tqmall.core.common.entity.Result convertToOutResult(Result result){
        if (result == null) {
            return null;
        }

        com.tqmall.core.common.entity.Result<T> r = new com.tqmall.core.common.entity.Result<T>();
        r.setCode(result.getCode());
        r.setData((T)result.getData());
        r.setMessage(result.getErrorMsg());
        r.setSuccess(result.isSuccess());
        return r;
    }
}
