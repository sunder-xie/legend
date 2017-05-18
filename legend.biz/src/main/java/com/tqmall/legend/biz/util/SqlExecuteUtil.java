package com.tqmall.legend.biz.util;

import java.util.LinkedList;
import java.util.List;


/**
 *
 * Created by gaorongyu on 14-8-20.
 */

public class SqlExecuteUtil {

    /**
     *
     * sql批量执行时，参数分组后按组提交，避免游标、数据量超过限制
     *
     * @param originList 原始参数列表
     * @param capacity   分组后每组的大小
     *
     * @return List<List<T>> 分组后的参数列表
     *
     */
    public static <T> List<List<T>>divideList(List<T> originList, int capacity) {
        List<List<T>> list = new LinkedList<>();
        int originListSize = originList.size();

        int length = originListSize / capacity;
        if(length == 0) {
            list.add(originList);
            return list;
        }
        if(originListSize % capacity > 0) {
            length = length + 1;
        }
        for(int i = 0; i < length; i++) {
            int fromIndex = i * capacity;
            int toIndex = (i + 1) * capacity > originListSize ? originListSize : (i + 1) * capacity;

            list.add(originList.subList(fromIndex, toIndex));
        }
        return list;
    }

}