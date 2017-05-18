package com.tqmall.legend.entity.precheck;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by guangxue on 14/10/31.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class PrecheckVO {
    private Prechecks prechecks;
    private List<PrecheckDetails> precheckDetailsList;
}
