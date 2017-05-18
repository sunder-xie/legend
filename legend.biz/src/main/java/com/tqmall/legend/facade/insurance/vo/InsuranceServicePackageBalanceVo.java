package com.tqmall.legend.facade.insurance.vo;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by twg on 16/9/19.
 */
@Data
public class InsuranceServicePackageBalanceVo {
    private BigDecimal commercialInsureFee = BigDecimal.ZERO;//商业险保费
    private BigDecimal compulsoryInsureFee = BigDecimal.ZERO;//交强险保费
    private Date insureCreateTime;//保单创建日期
    private Date insureStartTime;//保单生效日期
    private String packageName;//服务包名字
    private Integer packageStatus;//服务包状态 0:待生效 1:未发货  2:配送中 3:已签收
    private String materialOrderSn;//物料订单
    private BigDecimal rewardAmount = BigDecimal.ZERO;//现金收入
    private Integer confirmAgentMoney;//结算状态；0：未结算，1：已结算
    private BigDecimal packageMarketPrice = BigDecimal.ZERO;//服务包总价
    private Date packageGmtValid;//服务包购买日期
    private String outerInsuranceFormNo;//商业险保单号

    public String getInsureCreateTimeStr(){
        if(insureCreateTime != null){
            return DateUtil.convertDate1(insureCreateTime);
        }
        return "";
    }

    public String getInsureStartTimeStr(){
        if(insureStartTime != null){
            return DateUtil.convertDate1(insureStartTime);
        }
        return "";
    }

    public String getPackageGmtValidStr(){
        if(packageGmtValid != null){
            return DateUtil.convertDate1(packageGmtValid);
        }
        return "";
    }

    public String getMaterialOrderSn(){
        if(StringUtils.isBlank(materialOrderSn)){
            return "暂无";
        }
        return materialOrderSn;
    }

    public String getOuterInsuranceFormNo(){
        if(StringUtils.isBlank(outerInsuranceFormNo)){
            return "暂无";
        }
        return outerInsuranceFormNo;
    }

    public String getConfirmAgentMoneyStr(){
        if(confirmAgentMoney != null && confirmAgentMoney.intValue() == 0){
            return "未结算";
        }else if(confirmAgentMoney != null && confirmAgentMoney.intValue() == 1){
            return "已结算";
        }
        return "";
    }

    public String getPackageStatusStr(){
        if(packageStatus != null && packageStatus.intValue() == 0){
            return "待生效";
        }else if(packageStatus != null && packageStatus.intValue() == 1){
            return "待发货";
        }else if(packageStatus != null && packageStatus.intValue() == 2){
            return "配送中";
        }else if(packageStatus != null && packageStatus.intValue() == 3){
            return "已签收";
        }
        return "";
    }
}
