package com.tqmall.legend.facade.magic.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by shulin on 16/7/14.
 */
@Setter
@Getter
public class AutoChooseParamVo {
    private Long lineId;  //快修线与快喷线必须传
    private List<ProcessWorkTimeVo> processWorkTimeVoList;   //必须传
    private List<ProcessManagerRelVo> processManagerRelVoList; //事故线必须传
}
