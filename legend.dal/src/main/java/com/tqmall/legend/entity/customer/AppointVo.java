package com.tqmall.legend.entity.customer;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = false)
@Data
public class AppointVo extends Appoint {

    private Integer precheckCount;
    private Date latestPrecheck;
    private Integer repairCount;
    private Date latestRepair;
    private Integer appointCout;
    private Date latestAppoint;
    private String serviceName;
    private String carModel;
    private String importInfo;

    private String carYear;//车辆年款
    private String carPower;//车辆排量
    private String carInfo;//车辆型:品牌(进出口)系列名

    private String contact;//联系人
    private String contactMobile;//联系人电话

    private BigDecimal totalServiceAmount;//服务费用总金额
    private BigDecimal totalGoodsAmount;//物料费用总金额  appointAmount= serviceMoneyAmount+goodsMoneyAmount

    private String appointRecord;//预约记录: 次数 上次预约时间;
    private String precheckRecord;//预检记录: 次数 上次预检时间;
    private String repairRecord;//维修记录: 次数 上次预约时间;

    private BigDecimal downPayment;//预付定金
    private Integer payStatus;//支付状态，0支付失败，1支付成功


    private String latestPrecheckStr;

    public String getLatestPrecheckStr()
    {
        return DateUtil.convertDate(latestPrecheck);
    }

    private String latestRepairStr;

    public String getLatestRepairStr()
    {
        return DateUtil.convertDate(latestRepair);
    }

    private String latestAppointStr;

    public String getLatestAppointStr()
    {
        return DateUtil.convertDate(latestAppoint);
    }

    public String getCarInfo(){
        StringBuffer sb = new StringBuffer();
        if(getCarBrandName() != null){
            sb.append(getCarBrandName());
        }
        if(StringUtils.isNotBlank(getImportInfo())){
            sb.append('(').append(getImportInfo()).append(')');
        }
        if(StringUtils.isNotBlank(getCarModel())){
            sb.append(' ').append(getCarModel());
        } else if (StringUtils.isNotBlank(getCarSeriesName())){
            sb.append(' ').append(getCarSeriesName());
        }

        return sb.toString();
    }

    public String getAppointRecord() {
        if(appointCout==null ||appointCout.intValue()<1){
            return "暂无";
        }
        String s = appointCout+"次";
        if(StringUtils.isNotEmpty(getLatestAppointStr())){
            s += ",上次"+getLatestAppointStr();
        }
        return s;
    }

    public String getPrecheckRecord() {
        if(precheckCount==null ||precheckCount.intValue()<1){
            return "暂无";
        }
        String s = precheckCount+"次";
        if(StringUtils.isNotEmpty(getLatestPrecheckStr())){
            s += ",上次"+getLatestPrecheckStr();
        }
        return s;
    }

    public String getRepairRecord() {
        if(repairCount==null ||repairCount.intValue()<1){
            return "暂无";
        }
        String s = repairCount+"次";
        if(StringUtils.isNotEmpty(getLatestRepairStr())){
            s += ",上次"+getLatestRepairStr();
        }
        return s;
    }
}

