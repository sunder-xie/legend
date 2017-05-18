package com.tqmall.legend.object.result.service;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zsy on 16/5/30.
 * 模板服务
 */
@Data
public class ServiceTemplateDTO extends BaseEntityDTO{
    private static final long serialVersionUID = 1897545059039166917L;

    private Long id;
    private String name;
    private String serviceSn;
    private BigDecimal servicePrice;                    //售卖价
    private String flags;                               //“BZFW”：标准服务,“TQFW”：淘汽服务
    private String serviceNote;                         //车主服务备注
    private String shopServiceNote;                     //门店服务备注
    private Integer status;
    private String imgUrl;                              //蒲公英模板服务小图片地址
    private String thirdImgUrl;                         //微信模板服务图片地址
    private Integer sort;
    private Integer priceType;
    private String serviceInfo;
    private String thirdServiceInfo;                    //第三方（如橙牛）模板服务图片详细信息json字符串
    private Long cateId;
    private Integer cateTag;                            //标准服务标签，默认0，标准7大类：1保养2洗车3美容4检修5钣喷6救援7其他
    private Integer virtualJoinNum;                     //虚拟参加人数
    private BigDecimal settlePrice;                    //服务模板结算价（门店与云修对账价）
    private BigDecimal profitPrice;                    //服务模板立赚价（最高赢利价）
    private Integer editStatus;                          //编辑状态, 0可编辑 1可部分编辑,价格等 2不可编辑
}
