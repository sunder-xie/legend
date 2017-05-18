package com.tqmall.legend.entity.favourite;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

@EqualsAndHashCode(callSuper = false)
@Data
public class Favourite extends BaseEntity {

    private Long favouriteUserId;
    private Long bookId;
    private Integer favouriteType;

}


