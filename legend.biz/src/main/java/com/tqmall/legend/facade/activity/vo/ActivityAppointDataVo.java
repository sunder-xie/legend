package com.tqmall.legend.facade.activity.vo;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.dandelion.wechat.client.dto.wechat.ActServiceListDTO;

/**
 * Created by wushuai on 16/8/5.
 */
@Setter
@Getter
public class ActivityAppointDataVo extends ActServiceListDTO {
    private Integer durationDay;//活动进行天数
}
