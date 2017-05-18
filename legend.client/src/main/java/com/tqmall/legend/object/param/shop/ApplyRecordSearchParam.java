package com.tqmall.legend.object.param.shop;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by feilong.li on 16/10/17.
 */
@Data
public class ApplyRecordSearchParam extends BaseRpcParam implements Serializable{

    private static final long serialVersionUID = -6968204890430145615L;

    private String shopName;    //门店名称
    private Integer applyWxMode;  //门店微信支付模式
    private Integer applyStatus;   //支付状态
    private Integer pageNum;    //页码
    private Integer pageSize;   //分页大小
}
