package com.tqmall.legend.object.param.order;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xiangdong.qu on 17/2/10 17:22.
 */
@Data
public class SaveAndUpdateServiceWarnParam implements Serializable {
    private String license;  //车牌号
    private Long shopId;     //店铺Id
    private Long id;         //customerCarId
    private Date keepupTime; //下次提醒时间
}
