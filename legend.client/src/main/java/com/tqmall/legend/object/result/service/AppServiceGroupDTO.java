package com.tqmall.legend.object.result.service;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wushuai on 16/10/11.
 */
@Data
public class AppServiceGroupDTO implements Serializable {
    private static final long serialVersionUID = 6907914987326571076L;
    private Long cateId;//一级服务类型,表示置顶活动
    private String cateName;//一级服务类型名称
    private Integer cateSort;//类别排序,默认降序
    private String iconUrl;//类型图标url
    private List<AppServiceInfoDTO> serviceInfoDTOs;//改类型下的可以在车主段展示的服务(TQFW、MDFW、CZFW)列表
}
