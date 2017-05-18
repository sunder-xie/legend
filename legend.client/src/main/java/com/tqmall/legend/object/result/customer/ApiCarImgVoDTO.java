package com.tqmall.legend.object.result.customer;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by dingbao on 16/9/22.
 */
@Data
public class ApiCarImgVoDTO implements Serializable {
    /**
     * 车辆图片id
     */
    private Long id;
    /**
     * 图片路径
     */
    private String path;
}
