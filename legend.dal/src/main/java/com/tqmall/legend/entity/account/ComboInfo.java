package com.tqmall.legend.entity.account;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class ComboInfo extends BaseEntity {
    public static final int ENABLED = 1;
    public static final int DISABLED = 2;
    public static final int NON_CUSTOM_TIME = 0;//非自定义时间
    public static final int CUSTOMIZED_TIME = 1;//自定义时间

    private Long shopId;//门店id
    private String comboName;//套餐名称
    private BigDecimal salePrice;//售价
    private Integer comboStatus;//状态1启用2停用
    private Long effectivePeriodDays;//有效期(天)
    private List<ComboInfoServiceRel> content;
    private Integer grantedCount;//已发放套数;
    private String remark;//备注
    private Date effectiveDate;//生效时间
    private Date expireDate;//失效时间
    private Integer customizeTime;//是否自定义时间.0:非自定义时间;1.自定义时间
    private String effectiveDateStr;//生效时间
    private String expireDateStr;//失效时间

    public String getGmtCreateStr() {
        if (gmtCreate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(gmtCreate);
        }
        return null;
    }

    public String getEffectiveDateStr() {
        if (effectiveDateStr != null) {
            return effectiveDateStr;
        }
        if (effectiveDate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(effectiveDate);
        }
        return null;
    }

    public String getExpireDateStr() {
        if (expireDateStr != null) {
            return expireDateStr;
        }
        if (expireDate != null) {
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            return df.format(expireDate);
        }
        return null;
    }

}

