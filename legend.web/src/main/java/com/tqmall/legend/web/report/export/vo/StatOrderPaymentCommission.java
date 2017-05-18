package com.tqmall.legend.web.report.export.vo;

import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tanghao on 17/2/8.
 */
@Setter
@Getter
@com.tqmall.wheel.component.excel.annotation.Excel
public class StatOrderPaymentCommission {
    @ExcelCol(value = 0, title = "收款时间")
    private String statisDate; //结算时间
    @ExcelCol(value = 1, title = "工单编号")
    private String orderSn; //工单编号
    @ExcelCol(value = 3, title = "车牌")
    private String license; //车牌
    @ExcelCol(value = 4, title = "车主")
    private String customerName; //车主
    @ExcelCol(value = 25, title = "服务顾问")
    private String serverName; //服务顾问
    @ExcelCol(value = 5, title = "总计")
    private BigDecimal totalAmount; //总计
    @ExcelCol(value = 6, title = "应收金额")
    private BigDecimal totalPayAmount; //应付金额
    @ExcelCol(value = 7, title = "物料成本")
    private BigDecimal costAmount; //物料成本
    @ExcelCol(value = 8, title = "毛利")
    private BigDecimal grossProfit; //毛利
    @ExcelCol(value = 9, title = "实收金额")
    private BigDecimal payAmount; //营业实收
    @ExcelCol(value = 10, title = "现金")
    private BigDecimal cash; //1现金
    @ExcelCol(value = 15, title = "储蓄卡")
    private BigDecimal card; //7118储蓄卡
    @ExcelCol(value = 14, title = "信用卡")
    private BigDecimal credit; //7117信用卡
    @ExcelCol(value = 12, title = "银行转账")
    private BigDecimal bank; //5银行转账
    @ExcelCol(value = 13, title = "支付宝支付")
    private BigDecimal thirdPay; //6支付宝支付
    @ExcelCol(value = 17, title = "转账支票")
    private BigDecimal transfer; //7120转账支票
    @ExcelCol(value = 18, title = "现金支票")
    private BigDecimal check; //7121现金支票
    @ExcelCol(value = 19, title = "其他")
    private BigDecimal other; //7122其他支付方式
    @ExcelCol(value = 16, title = "微信支付")
    private BigDecimal wei; //7119微信支付
    @ExcelCol(value = 11, title = "会员卡")
    private BigDecimal member; //0会员卡支付
    @ExcelCol(value = 21, title = "挂账")
    private BigDecimal signAmount; //挂账金额
    @ExcelCol(value = 20, title = "自定义")
    private BigDecimal defined; //自定义
    @ExcelCol(value = 23, title = "坏账金额")
    private BigDecimal bad; //坏账
    @ExcelCol(value = 22, title = "结算状态")
    private String status; //结算状态
    @ExcelCol(value = 24, title = "结算人")
    private String operatorName; //结算人名称
    private Long shopId;
    private Date sPayTime;
    private Date ePayTime;
    private String flag;
    private Integer limit;
    private Integer page;
    private Integer total;
    private Long serverId;
    private Integer orderTag;
    private String orderTagStr;

    public void addPayAmount(BigDecimal temp){
        if(payAmount == null){
            payAmount = BigDecimal.ZERO;
        }
        if(temp != null){
            payAmount = payAmount.add(temp);
        }
    }
    public void addCash(BigDecimal temp){
        if(cash == null){
            cash = BigDecimal.ZERO;
        }
        if(temp != null){
            cash = cash.add(temp);
        }
    }
    public void addCard(BigDecimal temp){
        if(card == null){
            card = BigDecimal.ZERO;
        }
        if(temp != null){
            card = card.add(temp);
        }
    }
    public void addCredit(BigDecimal temp){
        if(credit == null){
            credit = BigDecimal.ZERO;
        }
        if(temp != null){
            credit = credit.add(temp);
        }
    }
    public void addBank(BigDecimal temp){
        if(bank == null){
            bank = BigDecimal.ZERO;
        }
        if(temp != null){
            bank = bank.add(temp);
        }
    }
    public void addThirdPay(BigDecimal temp){
        if(thirdPay == null){
            thirdPay = BigDecimal.ZERO;
        }
        if(temp != null){
            thirdPay = thirdPay.add(temp);
        }
    }
    public void addTransfer(BigDecimal temp){
        if(transfer == null){
            transfer = BigDecimal.ZERO;
        }
        if(temp != null){
            transfer = transfer.add(temp);
        }
    }
    public void addCheck(BigDecimal temp){
        if(check == null){
            check = BigDecimal.ZERO;
        }
        if(temp != null){
            check = check.add(temp);
        }
    }
    public void addOther(BigDecimal temp){
        if(other == null){
            other = BigDecimal.ZERO;
        }
        if(temp != null){
            other = other.add(temp);
        }
    }
    public void addWei(BigDecimal temp){
        if(wei == null){
            wei = BigDecimal.ZERO;
        }
        if(temp != null){
            wei = wei.add(temp);
        }
    }
    public void addDefined(BigDecimal temp){
        if(defined == null){
            defined = BigDecimal.ZERO;
        }
        if(temp != null){
            defined = defined.add(temp);
        }
    }
    public void addMember(BigDecimal temp){
        if(member == null){
            member = BigDecimal.ZERO;
        }
        if(temp != null){
            member = member.add(temp);
        }
    }
    public void addBad(BigDecimal temp){
        if(bad == null){
            bad = BigDecimal.ZERO;
        }
        if(temp != null){
            bad = bad.add(temp);
        }
    }

    @ExcelCol(value = 2, title = "工单类型")
    public String getOrderTagStr(){
        switch (orderTag) {
            case 1:
                return "综合维修";
            case 2:
                return "洗车单";
            case 3:
                return "快修快保";
            case 4:
                return "引流活动";
            case 5:
                return "销售";
            default:
                return "";
        }
    }
}
