package com.tqmall.legend.bi.entity;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.wheel.lang.Langs;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 *
 * @author : jian.ma <jian.ma@tqmall.com>
 * @create : 16/3/22
 *
 */


@EqualsAndHashCode(callSuper = false)
@Data
public class AttendanceStatis extends BaseEntity {

    private Long userId;//用户id
    private String userName;//用户姓名
    private Long shopId;//店铺id
    private Date signInTime;//签入时间
    private Integer signInLocationIsValid;//签入地点是否有效
    private Date signOutTime;//签出时间
    private Integer signOutLocationIsValid;//签出地点是否有效
    private Integer signInStatus;//'打卡状态
    private Integer signOutStatus;//'打卡状态
    private Date signDate;//'打卡日期'
    private String signDateStr;//str类型打卡日期
    private String signInTimeStr;//str类型签入时间
    private String signOutTimeStr;//str类型签出时间
    private String signstatus;//中文签到状态
    private String signinStatusstr;//签入地点是否有效
    private String signoutStatusstr;//签出地点是否有效

    public String getSignDateStr(){
        if (Langs.isNotBlank(signDateStr)) {
            return signDateStr;
        }
        if(signDate != null){
            return DateUtil.convertDateToYMD(signDate);
        }
        return null;
    }
    public String getSignInTimeStr(){
        if (Langs.isNotBlank(signInTimeStr)) {
            return signInTimeStr;
        }
        if(signInTime != null){
            return DateUtil.convertDateToStr(signInTime,"HH:mm:ss");
        }
        return null;
    }
    public String getSignOutTimeStr(){
        if (Langs.isNotBlank(signOutTimeStr)) {
            return signOutTimeStr;
        }
        if(signOutTime != null){
            return DateUtil.convertDateToStr(signOutTime,"HH:mm:ss");
        }
        return null;
    }

    public String getSignstatus(){
        if (Langs.isNotBlank(signstatus)) {
            return signstatus;
        }
        StringBuffer sb = new StringBuffer("");
        if(signInStatus==4||signOutStatus==4){
            sb.append("未打卡");
            return sb.toString();
        }else if(signInStatus==1){
            if(signOutStatus==3){
                sb.append("早退");
            }else{
                sb.append("正常");
            }
            return sb.toString();
        }else if(signInStatus==2){
            sb.append("迟到");
            if(signOutStatus==3){
                sb.append("、早退");
            }
            return sb.toString();
        }
        return null;
    }

}
