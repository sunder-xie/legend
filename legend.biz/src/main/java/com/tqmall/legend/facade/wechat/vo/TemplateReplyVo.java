package com.tqmall.legend.facade.wechat.vo;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.dandelion.wechat.client.dto.wechat.TemplateReplyDTO;

/**
 * Created by wushuai on 16/6/6.
 */
@Setter
@Getter
public class TemplateReplyVo extends TemplateReplyDTO {
    private String[] replyKeywords;

    public String[] getReplyKeywords() {
        if(replyKeywords!=null){
            return replyKeywords;
        }
        String oriReplyKeyword = getReplyKeyword();
        if(oriReplyKeyword!=null){
            return getReplyKeyword().split(",");
        }
        return null;
    }
}
