package com.tqmall.legend.facade.magic.vo;

import com.tqmall.common.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 委托单详情VO对象
 * Created by shulin on 16/5/11.
 */
@Data
public class ProxyOrderDetailVo {
    private Long id;
    private Date gmtCreate;
    private Long shopId;//门店id
    private Long orderId;//委托方工单id
    private Long proxyId;//受托方工单id
    private String serviceSa;//服务顾问
    private String shopName;//委托方门店名称
    private Long proxyShopId;//受托方门店id
    private String proxyShopName;//受托方门店名称
    private Date accountTime;//对账时间
    private String proxySn;//委托单编号
    private Long customerId;//客户id
    private Long customerCarId;//客户车辆id
    private String proxyStatus;//'委托单状态
    private String orderStatus;//'委托单对应的工单状态：cjdd
    private String contactName;//联系人名称
    private String contactMobile;//联系人手机电话
    private BigDecimal costAmount;//成本金额
    private BigDecimal amount;//订单金额
    private BigDecimal proxyAmount;//委托金额
    private Long receiver;//接单人id
    private String receiverName;//接单人
    private Date receiveTime;//接单时间
    private String shareName;//共享中心联系人
    private String shareMobile;//共享中心联系人手机电话
    private String shareAddr;//门店地址
    private Long serviceNum;//服务数量
    private Date completeTime;//完工时间
    private String carInfo;//车型信息
    private String carLicense;//车牌号
    private List<ProxyServicesVo> proxyServicesVoList;//服务列表
    private String proxyTimeStr;//委托时间
    private String proxyStatusStr;
    private String remark;//备注
    private String carColor;//车身颜色
    private String vin;//vin码
    private Long serviceSaId;//服务顾问id
    private Date expectTime;//期望交车时间
    private String carYearPower;

    public String getProxyTimeStr() {
        if (gmtCreate != null) {
            return DateUtil.convertDateToYMDHHmm(gmtCreate);
        }
        return null;
    }

    public String getCompleteTime(){
        if(completeTime!=null){
            return DateUtil.convertDateToYMDHHmm(completeTime);
        }
        return null;
    }

    public String getProxyStatusStr() {
        if (StringUtils.isNotEmpty(proxyStatus)) {
            if (proxyStatus.equals("YWT")) {
                return "已委托";
            }
            if (proxyStatus.equals("YJD")) {
                return "已接单";
            }
            if (proxyStatus.equals("YYC")) {
                return "已验车";
            }
            if (proxyStatus.equals("FPDD")) {
                return "已派工";
            }
            if (proxyStatus.equals("DDWC")) {
                return "已完工";
            }
            if (proxyStatus.equals("YJC")) {
                return "已交车";
            }
            if (proxyStatus.equals("YJQ")) {
                return "已结清";
            }
            if (proxyStatus.equals("YQX")) {
                return "已取消";
            }
        }
        return "";
    }
}
