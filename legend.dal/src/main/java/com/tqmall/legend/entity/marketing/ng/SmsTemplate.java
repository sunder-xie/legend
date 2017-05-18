package com.tqmall.legend.entity.marketing.ng;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class SmsTemplate extends BaseEntity implements Comparable<SmsTemplate>{
    public static final int DEFAULT_TYPE = 10;
    private Long shopId;
    private String content;
    private Integer templateType;

    @Override
    public int compareTo(SmsTemplate o) {
        if(templateType == null || o==null|| o.getTemplateType()==null){
            return 0;
        }
        if(o.getTemplateType()<this.getTemplateType()){
            return 1;
        }else{
            return -1;
        }
    }
}

