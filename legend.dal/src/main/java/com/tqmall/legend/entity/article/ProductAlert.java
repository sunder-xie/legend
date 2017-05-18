package com.tqmall.legend.entity.article;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class ProductAlert extends BaseEntity {


    private Integer shopId;//门店id
    private String itemTitle;//标题
    private String imgUrl;//图片地址
    private String itemContent;//资讯主体内容
    private String targetUrl;//跳转地址
    private Integer isPublished;//是否发布(0：未发布;1：发布)
    private Integer isTop;//是否置顶
    private String topImgUrl;//置顶图片地址
    private Integer itemSort;//序号


    private String gmtCreateStr;

    public String getGmtCreateStr()
    {
        return DateUtil.convertDateToYMDHMS(super.getGmtCreate());
    }


}

