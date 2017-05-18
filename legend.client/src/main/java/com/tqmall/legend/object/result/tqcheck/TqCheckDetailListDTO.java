package com.tqmall.legend.object.result.tqcheck;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by lifeilong on 2016/4/13.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TqCheckDetailListDTO implements Serializable{
    private static final long serialVersionUID = -5737410686546442665L;
    protected Date gmtCreate;
    protected Long creator;
    protected Date gmtModified;
    protected String gmtCreateStr;
    protected Long modifier;

    private Long customerCarId; //车辆id
    private String carLicense;  //车牌号
    private String imgUrl;  //车牌图片URL
    private String contactMobile;   //联系人电话
    private String contactName;     //联系人名称
    private Long carBrandId;    //车品牌id
    private String carBrand;    //车品牌名称
    private Long carSeriesId;   //车系列id
    private String carSeries;   //车系列名称
    private Long carModelId;    //车型ID
    private String carModel;    //车型
    private String importInfo;  //进出口
    private Long carGearBoxId;  // 变速箱id (可获取车型信息)

    private String carInfo;     //车辆型号信息(包装过后的)
    private Long checkId;       //检测记录id 对应legend_tq_check_log表
    private List<TqCheckDetailDTO> detailDTOList; //每个检测项目信息列表

    private Boolean isHasCheckRecord; // 是否已有检测记录

    public String getGmtCreateStr(){
        if (gmtCreateStr != null) {
            return gmtCreateStr;
        } else {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            return null == gmtCreate ? gmtCreateStr : f.format(gmtCreate);
        }
    }

}
