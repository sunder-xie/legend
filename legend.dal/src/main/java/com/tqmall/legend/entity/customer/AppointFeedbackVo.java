package com.tqmall.legend.entity.customer;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jason on 15/7/29.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class AppointFeedbackVo extends BaseEntity {

    private String appointDateStr;
    private Date appointTime;
    private String appointContent;//服务备注
    private String appointAddress;//预约地点
    private BigDecimal appointAmount;//预约单金额

    public String getAppointDateStr() {
        if(appointTime!=null){
            appointDateStr = DateUtil.convertDateToStr(appointTime,"yyyy-MM-dd HH:mm:ss");
        }
        return appointDateStr;
    }

    public String getAppointContent(){
        if(StringUtils.isNotBlank(appointContent)){
            return appointContent.replace("\n","<br/>");
        }else{
            return "";
        }
    }
}
