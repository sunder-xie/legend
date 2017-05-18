package com.tqmall.legend.facade.activity.vo;

import com.tqmall.dandelion.wechat.client.dto.wechat.ActivityGroupBuyShareDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by wushuai on 16/11/16.
 */
@Getter
@Setter
public class ActivityGroupBuyShareVo extends ActivityGroupBuyShareDTO {
    private Integer durationDay;//活动进行天数
}
