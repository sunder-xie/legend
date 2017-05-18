package com.tqmall.legend.pojo.balance;

import com.tqmall.common.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xiangDong.qu on 15/12/18.
 */
@Data
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBalanceLogVO {
    private Long id;
    private Date gmtCreate;
    private Long userId;//用户id
    private Long shopId;
    private BigDecimal amount;//流水金额
    private String actionType;//0是进账，1是提现
    private Integer handleStatus;//'操作状态 1:提现中 2:提现成功 3:提现失败 4:红包待审核 5:红包无效 6:红包有效'
    private Integer lotteryRecordId;//'红包id（标记红包

    //红包类型id
    private Long awardId;
}
