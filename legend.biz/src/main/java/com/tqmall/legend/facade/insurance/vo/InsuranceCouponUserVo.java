package com.tqmall.legend.facade.insurance.vo;


import com.tqmall.common.util.DateUtil;
import com.tqmall.insurance.constants.CouponUserStatusEnum;
import com.tqmall.insurance.domain.result.coupon.InsuranceCouponUserDTO;

/**
 * Created by sven on 2016/12/9.
 */
public class InsuranceCouponUserVo extends InsuranceCouponUserDTO {

    public String getValidateTimeStr() {
        return DateUtil.convertDate1(super.getValidateTime());
    }

    public String getGmtCouponExpiredStr() {
        return DateUtil.convertDate1(super.getGmtCouponExpired());
    }

    public String getGmtCouponUsedStr() {
        return DateUtil.convertDateToYMD(super.getGmtCouponUsed());
    }

    /**
     * 核销状态
     *
     * @return
     */
    public String getCouponStatusName() {
        boolean flag = CouponUserStatusEnum.isUsed(super.getCouponStatus());
        if (flag) {
            return "已核销";
        }
        return "未核销";
    }

    /**
     * 结算状态
     *
     * @return
     */
    public String getCouponSettleStatusName() {
        boolean flag = CouponUserStatusEnum.isSettled(super.getCouponStatus());
        if (flag) {
            return "已结算";
        }
        return "未结算";
    }
}
