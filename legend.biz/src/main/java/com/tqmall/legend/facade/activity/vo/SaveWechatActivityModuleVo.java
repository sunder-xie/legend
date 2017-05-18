package com.tqmall.legend.facade.activity.vo;

import com.tqmall.legend.facade.service.vo.ServiceTemplateVo;
import com.tqmall.legend.facade.service.vo.WechatActServiceVo;
import lombok.Data;

import java.util.List;

/**
 * Created by wushuai on 16/8/6.
 */
@Data
public class SaveWechatActivityModuleVo {
    private String uniqueCode;//模块唯一标识，取自模板活动页面与模块的关联关系表中的unique_code
    private Integer moduleIndex;//模块在页面中的顺序，从0开始
    private String moduleType;//模块类型（图片:pic、文本框:text、门店信息模板:shopinfo、预约服务:service、按钮:btn）
    private List<WechatActServiceVo> serviceVoList;//module包含的服务列表(保存时用)
    private List<ServiceTemplateVo> detailServiceVoList;//module包含的服务列表(查询详情时用)
    private List<CouponTplVo> couponVoList;//module包含的优惠券列表
}
