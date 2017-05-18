package com.tqmall.legend.facade.discount.bo;

import com.tqmall.wheel.utils.DateFormatUtils;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 计次卡折扣信息
 *
 * @Author 辉辉大侠
 * @Date:10:11 AM 02/03/2017
 */
@Data
public class AccountComboDiscountBo {
    public AccountComboDiscountBo() {
        this.selected = false;
        this.available = true;
        this.discount = BigDecimal.ZERO;
    }

    private Long comboId;
    private String comboName;
    private Long comboTypeId;
    /**
     * 计次卡识别id
     */
    private Long comboServiceId;
    private Long serviceId;
    private String serviceName;
    private boolean selected;
    private Integer count;
    private Integer useCount;
    /**
     * 是否可用
     */
    private boolean available;
    /**
     * 计次卡生效时间
     */
    private Date effectiveDate;
    /**
     * 计次卡失效时间
     */
    private Date expireDate;
    /**
     * 当计次卡不可用时具体的提示信息
     */
    private String message;

    private BigDecimal discount;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 客户手机号码
     */
    private String mobile;
    private Long accountId;

    /**
     * 过期时间
     * @return
     */
    public String getExpireDateStr(){
        return DateFormatUtils.toYMD(expireDate);
    }
}
