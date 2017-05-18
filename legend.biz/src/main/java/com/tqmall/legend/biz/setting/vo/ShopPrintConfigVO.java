package com.tqmall.legend.biz.setting.vo;

import com.google.gson.Gson;
import com.tqmall.legend.entity.setting.ConfigFieldVO;
import com.tqmall.legend.entity.setting.ShopPrintConfig;
import com.tqmall.legend.enums.setting.PrintTemplateEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

/**
 * Created by lilige on 16/11/9.
 */

@Setter
@Getter
public class ShopPrintConfigVO extends ShopPrintConfig{

    private String printConfigName;

    private ConfigFieldVO configFieldVO;

    public ConfigFieldVO getConfigFieldVO(){
        String configField = getConfigField();
        if (null == configFieldVO && StringUtils.isNotBlank(configField)){
            configFieldVO = new Gson().fromJson(configField,ConfigFieldVO.class);
            return configFieldVO;
        }
        return configFieldVO;
    }

    public String getPrintConfigName(){
        printConfigName = PrintTemplateEnum.getNameByCode(getPrintTemplate());
        return printConfigName;
    }
}
