package com.tqmall.legend.facade.wechat.vo;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.common.util.DateUtil;
import com.tqmall.dandelion.wechat.client.dto.wechat.ArticleDTO;

/**
 * Created by wushuai on 16/6/6.
 */
@Setter
@Getter
public class WechatArticleVo extends ArticleDTO {
    private String sendTimeStr;//文章发送时间

    public String getSendTimeStr() {
        if(getSendTime() == null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getSendTime());
    }

}
