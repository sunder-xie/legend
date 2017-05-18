package com.tqmall.legend.object.result.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiangDong.qu on 16/2/26.
 */
@Data
public class PageEntityDTO<T> implements Serializable {
    private static final long serialVersionUID = -5206764258341141114L;

    // 总记录数
    int totalNum;
    // 当前页号
    int pageNum;
    // 记录列表
    List<T> recordList;
}
