package com.tqmall.legend.facade.insurance.vo;

import com.tqmall.insurance.domain.result.InsuranceUserServiceItemDTO;
import com.tqmall.insurance.domain.result.InsuranceUserServiceItemMaterialDTO;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by zsy on 16/9/19.
 */
@Data
public class InsuranceUserServiceItemVo extends InsuranceUserServiceItemDTO {
    private String materialModelInfo;//车型

    public String getMaterialModelInfo() {
        if (CollectionUtils.isEmpty(getMaterialList())) {
            return "";
        }
        StringBuffer materialModelSb = new StringBuffer();
        for (InsuranceUserServiceItemMaterialDTO insuranceUserServiceItemMaterialDTO : getMaterialList()) {
            String goodsSn = insuranceUserServiceItemMaterialDTO.getGoodsSn();
            String materialModel = insuranceUserServiceItemMaterialDTO.getMaterialModel();
            if (StringUtils.isNotBlank(goodsSn) && StringUtils.isNotBlank(materialModel)) {
                materialModelSb.append(materialModel);
                materialModelSb.append(",");
            }
        }
        if (materialModelSb.length() > 0) {
            return materialModelSb.substring(0, materialModelSb.length() - 1);
        }
        return "";
    }
}
