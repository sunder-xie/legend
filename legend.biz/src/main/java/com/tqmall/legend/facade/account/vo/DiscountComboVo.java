package com.tqmall.legend.facade.account.vo;

import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.ComboInfo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wanghui on 6/6/16.
 */
@Data
public class DiscountComboVo implements Comparable<DiscountComboVo>{
    /**
     * 计次卡实例id
     */
    private Long accountComboId;
    /**
     * 计次卡实例服务id
     */
    private Long comboServiceId;
    /**
     * 计次卡计次服务id
     */
    private Long comboServiceTypeId;
    /**
     * 计次卡服务名称
     */
    private String comboServiceName;
    /**
     * 计次卡服务剩余次数
     */
    private Integer comboServiceCount;
    /**
     * 计次卡信息
     */
    private AccountCombo combo;
    /**
     * 计次卡类型信息
     */
    private ComboInfo comboInfo;
    /**
     * 是否可用
     */
    private boolean available;
    /**
     * 是否使用
     */
    private boolean used;
    /**
     * 本次使用的张数
     */
    private Integer useCount;
    /**
     * 计次卡抵扣金额
     */
    private BigDecimal discountAmount;
    /**
     * 备注信息
     */
    private String message;
    @Override
    public int compareTo(DiscountComboVo o) {
        if (o == null) {
            return 1;
        } else {
            if (getCombo().getExpireDate() == null && o.getCombo().getExpireDate() == null) {
                return 0;
            } else if(getCombo().getExpireDate() == null) {
                return 1;
            } else if(o.getCombo().getExpireDate() == null) {
                return -1;
            } else {
                return getCombo().getExpireDate().compareTo(o.getCombo().getExpireDate());
            }
        }
    }
}
