package com.tqmall.legend.facade.insurance.vo;

import com.google.common.collect.Lists;
import com.tqmall.common.util.DateUtil;
import com.tqmall.insurance.domain.result.InsuranceUserServiceItemDTO;
import com.tqmall.insurance.domain.result.InsuranceUserServicePackageDTO;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by zsy on 16/9/19.
 */
@Data
public class InsuranceUserServicePackageVo extends InsuranceUserServicePackageDTO {

    private String gmtCreateStr;//创建时间
    private String gmtValidStr;//服务包生效时间
    private String gmtExpireStr;//服务包失效时间
    private boolean available;//是否可用

    private List<InsuranceUserServiceItemVo> itemVoList;

    public String getGmtCreateStr() {
        if (getGmtCreate() != null) {
            return DateUtil.convertDateToYMDHMS(getGmtCreate());
        }
        return "";
    }

    public String getGmtValidStr() {
        if (getGmtValid() != null) {
            return DateUtil.convertDateToYMDHMS(getGmtValid());
        }
        return "";
    }

    public String getGmtExpireStr() {
        if (getGmtExpire() != null) {
            return DateUtil.convertDateToYMDHMS(getGmtExpire());
        }
        return "";
    }

    public boolean getAvailable() {
        if (getGmtValid() == null || getGmtExpire() == null) {
            return false;
        }
        long time = new Date().getTime();
        long gmtValidTime = getGmtValid().getTime();
        long expireDateTime = getGmtExpire().getTime();
        if (gmtValidTime <= time && time <= expireDateTime) {
            return true;
        }
        return false;
    }

    public List<InsuranceUserServiceItemVo> getItemVoList() {
        if (CollectionUtils.isEmpty(getItemList())) {
            return null;
        }
        List<InsuranceUserServiceItemVo> itemVoList = Lists.newArrayList();
        for (InsuranceUserServiceItemDTO insuranceUserServiceItemDTO : getItemList()) {
            InsuranceUserServiceItemVo insuranceUserServiceItemVo = new InsuranceUserServiceItemVo();
            BeanUtils.copyProperties(insuranceUserServiceItemDTO, insuranceUserServiceItemVo);
            itemVoList.add(insuranceUserServiceItemVo);
        }
        return itemVoList;
    }
}
