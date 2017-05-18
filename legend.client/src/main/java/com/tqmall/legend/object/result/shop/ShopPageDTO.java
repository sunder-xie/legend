package com.tqmall.legend.object.result.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsy on 15/12/15.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopPageDTO implements Serializable{
    private static final long serialVersionUID = 1617018301589409243L;
    private Integer shopJoinNum = 0;//已参加门店
    private Integer shopNotJoinNum = 0;//未参加门店
    private List<ShopDTO> shopList;//展示门店数据
}
