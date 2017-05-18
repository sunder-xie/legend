package com.tqmall.legend.facade.activity.vo;

import com.tqmall.legend.entity.activity.ActivityTemplate;
import com.tqmall.legend.facade.service.vo.ServiceTemplateVo;
import lombok.Data;

import java.util.List;

/**
 * 活动详细,包含服务
 * Created by wushuai on 16/8/2.
 */
@Data
public class ActivityDetailVo extends ActivityTemplate {
    private Integer shopActivityStatus;//0关闭,1草稿,2发布, -1未发布(未参加)
    private Integer selectedServiceCount;//已选择的服务数
    private Long actId;//活动id
    private String shopStartTimeStr;//门店活动开始时间格式化字符串
    private String shopEndTimeStr;//门店活动结束时间字符串
    private String tplStartTimeStr;//门店活动开始时间格式化字符串
    private String tplEndTimeStr;//门店活动结束时间字符串
    private List<ServiceTemplateVo> serviceTemplateVoList;//活动模版包含的服务
    private List<SaveWechatActivityPageVo> pageVoList;
}
