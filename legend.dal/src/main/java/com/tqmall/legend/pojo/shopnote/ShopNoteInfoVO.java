package com.tqmall.legend.pojo.shopnote;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.shop.NoteInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by xin on 16/8/8.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ShopNoteInfoVO extends NoteInfo {

    // 预约单下单时间
    private Date appointCreateTime;
    // 预约单内容
    private String appointContent;

    // 上次保养时间
    private Date lastNoteKeepupTime;

    // 行驶里程
    private Long mileage;

    // 车型
    private String carModel;

    // 最近消费时间
    private Date lastPayTime;

    // 消费次数
    private Integer totalNumber;

    public String getAppointCreateTimeStr() {
        if (appointCreateTime != null) {
            return DateUtil.convertDateToYMD(appointCreateTime);
        }
        return null;
    }

    public String getLastPayTimeStr() {
        if (lastPayTime != null) {
            return DateUtil.convertDateToYMD(lastPayTime);
        }
        return null;
    }
}
