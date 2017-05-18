package com.tqmall.legend.facade.order.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.wheel.lang.Langs;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zsy on 16/5/10.
 */
@Data
public class OrderToProxyVo {
    /**
     * 工单信息
     */
    private Long orderId;//工单id
    private String orderSn;//工单编号
    private Long receiver;//服务顾问id
    private String receiverName;//服务顾问
    private Date expectTime;//期望交车时间
    private String expectTimeStr;//期望交车时间
    private String postscript;//备注
    /**
     * 车辆信息
     */
    private Long customerId;
    private Long customerCarId;
    private String carLicense;
    private String carColor;
    private String vin;
    //车型数据
    private Long carBrandId;
    private Long carSeriesId;
    private Long carModelsId;
    private String carBrand;
    private String carSeries;
    private String carModels;
    private String importInfo;
    private String carInfo;
    //年款排量
    private Long carPowerId;//排量
    private String carPower;
    private Long carYearId;//年款
    private String carYear;
    private Long carGearBoxId;//变速箱
    private String carGearBox;
    private String yearPower;//年款排量

    /**
     * 服务项目
     */
    private List<OrderServicesVo> orderServicesList;

    /**
     * 获取车型数据
     * @return
     */
    public String getCarInfo() {
        StringBuffer sb = new StringBuffer();
        if (getCarBrand() != null) {
            sb.append(getCarBrand());
        }
        if (StringUtils.isNotBlank(getImportInfo())) {
            sb.append('(').append(getImportInfo()).append(')');
        }
        if (StringUtils.isNotBlank(getCarModels())) {
            sb.append(' ').append(getCarModels());
        } else if (StringUtils.isNotBlank(getCarSeries())) {
            sb.append(' ').append(getCarSeries());
        }

        return sb.toString();
    }

    /**
     * 获取期望交车时间
     * @return
     */
    public String getExpectTimeStr(){
        if (expectTime == null) {
            return "";
        }
        return DateUtil.convertDateToYMDHHmm(expectTime);
    }

    /**
     * 获取年款排量
     * @return
     */
    public String getYearPower(){
        if(StringUtils.isNotBlank(carYear) && (StringUtils.isNotBlank(carGearBox) || StringUtils.isNotBlank(carPower))){
            StringBuffer yerPowerSb = new StringBuffer();
            yerPowerSb.append(carYear);
            yerPowerSb.append(" ");
            if(StringUtils.isNotBlank(carGearBox)) {
                yerPowerSb.append(carGearBox);
            }else{
                yerPowerSb.append(carPower);
            }
            return yerPowerSb.toString();
        }
        return "";
    }
}
