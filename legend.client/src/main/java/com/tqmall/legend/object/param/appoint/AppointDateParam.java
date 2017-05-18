package com.tqmall.legend.object.param.appoint;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by feilong.li on 16/10/9.
 */
@Data
public class AppointDateParam extends BaseRpcParam implements Serializable {

    private static final long serialVersionUID = 8628659062780846190L;

    private Long shopId;    //门店id
    private String startTime;   //创建时间
    private String endTime; //结束时间
    private Integer pageNum;    //页数
    private Integer pageSize;   //大小
    private List<String> sorts; //排序
    private Integer[] statusArr;    //状态数组
    private Integer pushStatus;    //下推状态


}
