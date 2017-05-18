package com.tqmall.legend.entity.balance;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.math.BigDecimal;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBalanceLog extends BaseEntity {

    private Long shopId;//店铺id
    private Long userId;//用户id
    private BigDecimal balance;//账户余额
    private String userName;//用户姓名
    private String mobile;//用户电话
    private String shopName;//店铺名称
    private String shopProvince;//店铺地址省份
    private String shopCity;//店铺地址城市
    private Long withdrawAccountId;//提现账号id
    private String withdrawAccount;//提现账号
    private BigDecimal amount;//流水金额
    private String actionType;//0是进账，1是提现
    private Integer handleStatus;//'操作状态 3:提现中 4:提现成功 5:提现失败 0:红包待审核 2:红包无效 1:红包有效'
    private Long lotteryRecordId;//补贴活动id(或红包id 活动1,2对应的是红包id)
    private String withdrawComment;//提现备注
    private Long actId;//活动id
    private BigDecimal singleAmount;//单个补贴金额
    private Long subsidyNum;//补贴申领个数
    private String subsidyName;//补贴名称
    private Integer subsidyType;//补贴(红包)类型
    private Integer nextNode;  //提现中下一个审核节点 提现中不同的审核流程节点标记(1:小秘书2:市场部3:业管中心4:财务)
}




