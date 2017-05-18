package com.tqmall.legend.object.param.appoint;

import com.tqmall.legend.object.param.BaseRpcParam;
import lombok.Data;

/**
 * Created by zsy on 16/3/30.
 */
@Data
public class AppointSearchParams extends BaseRpcParam {

    private static final long serialVersionUID = 596072168563305517L;

    Integer serviceTplId;//服务模板id
    Integer city;//城市id

    Integer priceSort = 1;//价格排序，1升序，2降序，默认升序
}
