package com.tqmall.legend.entity.precheck;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

/**
 * Created by guangxue on 14/11/11.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class PrecheckDetailsVO {
    Long   precheckId;
    String precheckSn;
    String gmtCreate;
    Map<String, List<PrecheckDetails>> detailsMap;

}
