package com.tqmall.legend.object.param.appoint;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zsy on 16/1/12.
 */
@Data
public class AppointParam extends BaseRpcParam{
    private static final long serialVersionUID = -2612077039152520664L;
    /**
     * 数据中心消息type
     */
    String CUBE_APPOINT_CREATE = "9";//预约单创建
    String CUBE_APPOINT_CANCEL = "10";//预约单取消

    private Long id;//预约单id
    private Long shopId;//门店id
    private Date appointTime;//预约时间
    private String appointTimeFormat;//预约时间
    private String appointContent;//预约内容
    private String mobile;//联系人手机号

    public String getAppointTimeFormat() {
        if(appointTimeFormat != null){
            return appointTimeFormat ;
        }
        SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        if (appointTime != null) {
            return df.format(appointTime);
        } else {
            return null;
        }
    }
}
