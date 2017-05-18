package com.tqmall.legend.object.param.subsidy;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by xiangDong.qu on 16/2/28.
 */
@Data
@ToString
public class RpcSubsidyApplySaveParam implements Serializable{
    private static final long serialVersionUID = -537983542734913269L;

    private Long userId;      //用户id
    private Long shopId;      //店铺id
    private Long subsidyActId;//补贴活动id
    private Long subsidyNum;  //补贴申领个数
}
