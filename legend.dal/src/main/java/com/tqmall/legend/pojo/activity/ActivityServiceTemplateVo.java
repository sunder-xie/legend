package com.tqmall.legend.pojo.activity;

import com.tqmall.legend.entity.shop.ServiceInfoList;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lixiao on 16/2/25.
 */
@Data
public class ActivityServiceTemplateVo {
    private Long actTplId;
    private Long serviceTplId;

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
    private String agreement;                          //服务模板协议
    private Integer editStatus;
    private ShopServiceInfo shopServiceInfo;           //报名的服务

    private String auditReason;                              //审核原因

    private Integer joinNum;    //参加人数，虚拟参加人数+实际参加人数
    private Integer joinFlag;   //参加标志，0表示未参加，1表示审核中，2表示审核成功，3表示审核失败

    private List<ServiceInfoList> serviceInfoList;      //详情的json串

    public String getServiceNote() {
        if (!StringUtils.isBlank(serviceNote)) {
            return serviceNote.replace("\n", "<br/>");
        }
        return "";
    }

    public String getShopServiceNote() {
        if (!StringUtils.isBlank(shopServiceNote)) {
            return shopServiceNote.replace("\n", "<br/>");
        }
        return "";
    }

    //获取最后一个审核失败的原因
    public String getAuditReason() {
        if (!StringUtils.isBlank(auditReason)) {
            String[] auditReasons = auditReason.split("<br>");
            int size = auditReasons.length;
            return auditReasons[size - 1];
        }
        return "";
    }
}
