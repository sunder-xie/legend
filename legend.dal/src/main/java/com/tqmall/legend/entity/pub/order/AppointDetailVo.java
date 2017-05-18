

package com.tqmall.legend.entity.pub.order;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.marketing.activity.CzCouponNotice;
import com.tqmall.legend.entity.pub.service.ServiceInfoVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class AppointDetailVo {

    private Long appointId;//预约单ID
    private String appointSn;//预约单编码
    private String shopName;//店铺名称
    private String userGlobalId;
    private String shopAddress;//店铺地点
    private Long appointStatus;//订单状态 0 待确认, 1 预约成功, 2 工单生成  3 车主取消, 4 门店取消
    private String tel;
    private String mobile;
    private Long orderId;//工单ID

    private BigDecimal appointAmount;//预约单价格
    private BigDecimal downPayment;//预付定金
    private Integer payStatus;//支付状态，0支付失败，1支付成功

    private Date appointTime;//预约时间
    private String appointTimeStr;
    public String getAppointTimeStr() {
        return DateUtil.convertsDate(appointTime);
    }
    private String carLicense;//车牌号
    private String carModel;//车型
    private String contactName;//联系人
    private String contactMobile;//联系人电话

    private String appointContent;//预约内容

    private String cancelReason;//取消原因
    private Long channel;//来源

    private List<ServiceInfoVo> appointServiceList;

    private List<CzCouponNotice> couponNoticeList;//门店优惠公告

    private Date gmtCreate;//创建时间
    private String gmtCreateStr;
    public String getGmtCreateStr() {
        return DateUtil.convertDateYMDHMS(gmtCreate);
    }

    private String serviceSuitName;//预约费服务套餐的名称,有多个时","隔开
    private String serviceSuitImgUrl;//预约费服务套餐的服务小图,默认取第一个
    private BigDecimal totalDiscount;//优惠总额
}