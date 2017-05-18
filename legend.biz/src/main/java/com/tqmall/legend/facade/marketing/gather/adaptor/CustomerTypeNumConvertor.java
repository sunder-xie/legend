package com.tqmall.legend.facade.marketing.gather.adaptor;

import com.tqmall.cube.shop.result.tag.TagSummaryDTO;
import com.tqmall.legend.facade.marketing.gather.vo.CustomerTypeNum;

/**
 * Created by xin on 2016/12/16.
 */
public class CustomerTypeNumConvertor {

    public static CustomerTypeNum convert(TagSummaryDTO tagSummaryDTO) {
        CustomerTypeNum customerTypeNum = new CustomerTypeNum();
        if (tagSummaryDTO != null) {
            customerTypeNum.setHasMobileNum(tagSummaryDTO.getMobileCount());
            customerTypeNum.setHasMemberNum(tagSummaryDTO.getMemberCount());
            customerTypeNum.setNoneMemberNum(tagSummaryDTO.getNonMemberCount());
            customerTypeNum.setSleepNum(tagSummaryDTO.getLazyCount());
            customerTypeNum.setLostNum(tagSummaryDTO.getLostCount());
            customerTypeNum.setActiveNum(tagSummaryDTO.getActiveCount());

            customerTypeNum.setAuditingNoteNum(tagSummaryDTO.getCheckupCount());
            customerTypeNum.setMaintainNoteNum(tagSummaryDTO.getKeepupCount() + tagSummaryDTO.getKeepupSecondCount());
            customerTypeNum.setInsuranceNoteNum(tagSummaryDTO.getInsuranceCount());
            customerTypeNum.setBirthdayNoteNum(tagSummaryDTO.getBirthDayCount());
            customerTypeNum.setLostNoteNum(tagSummaryDTO.getCustomerLostCount());
            customerTypeNum.setVisitNoteNum(tagSummaryDTO.getVisitBackCount() + tagSummaryDTO.getVisitBackSecondCount());
        }
        return customerTypeNum;
    }
}
