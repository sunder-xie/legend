package com.tqmall.legend.object.result.book;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by macx on 16/2/23.
 */
@Data
public class BookPageDTO<T> implements Serializable{
    private static final long serialVersionUID = 2932317688500435694L;
    private Integer totalSize;//总页数
    private List<T> dataList;//分页数据
}
