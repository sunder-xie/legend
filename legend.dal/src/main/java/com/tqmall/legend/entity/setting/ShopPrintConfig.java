package com.tqmall.legend.entity.setting;

import com.google.gson.Gson;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by lilige on 16/11/2.
 */
@EqualsAndHashCode(callSuper = false)
@Setter
@Getter
public class ShopPrintConfig extends BaseEntity {

    private Long shopId;//门店id
    private String configField;//字段配置
    private Integer printType;//打印版本：1－简化版 2-标准版 3-专业版 4-自定义
    private Integer printTemplate;//打印模版：1-派工单 2-结算单 3-报价单 4-试车单 5-小票打印
    private Integer openStatus;//开启状态：0－未开启 1－开启
    private Integer fontStyle;//字体大小: 0-小字体 1-大字体

    public void changeOpenStatus(){
        if (null == openStatus){
            return;
        }
        openStatus = openStatus == 0 ? 1 : 0;
    }
}

