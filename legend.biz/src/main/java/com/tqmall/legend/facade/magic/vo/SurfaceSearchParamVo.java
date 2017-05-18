package com.tqmall.legend.facade.magic.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by shulin on 16/10/18.
 */
@Getter
@Setter
public class SurfaceSearchParamVo {
    private Long shopId;
    private Date startTime;
    private Date endTime;
}
