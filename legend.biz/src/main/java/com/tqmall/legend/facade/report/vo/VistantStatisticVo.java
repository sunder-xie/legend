package com.tqmall.legend.facade.report.vo;

import lombok.Data;

/**
 * Created by majian on 16/9/1.
 */
@Data
public class VistantStatisticVo {
    private Integer receivedCarNumber;//接车维修车辆数
    private Integer receptionNumber;//接车维修次数
    private Integer newCarNumber;//新建车辆数
    private Integer newCustomerNumber;//新客户数
    private Integer oldCustomerNumber;//老客户数
}
