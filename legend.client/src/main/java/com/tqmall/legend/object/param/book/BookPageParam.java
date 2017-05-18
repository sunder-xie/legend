package com.tqmall.legend.object.param.book;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by macx on 16/2/23.
 */
@Data
public class BookPageParam implements Serializable {

    private static final long serialVersionUID = 1193337129168019827L;

    private Long bookId; //资料id
    private Integer pageSize = 1; //分页：一次返回的数据数
    private Integer page = 1;   //分页：搜索页数
}
