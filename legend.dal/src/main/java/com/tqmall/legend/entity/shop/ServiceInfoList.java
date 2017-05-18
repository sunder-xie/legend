package com.tqmall.legend.entity.shop;

import java.util.List;

import lombok.Data;

/**
 * 服务图片列表实体
 * 
 * @author wjc
 *
 *         2015年8月25日下午3:46:39
 */
@Data
public class ServiceInfoList {
    private String name;

    private Integer sort;

    private List<ImgUrl> list;
}
