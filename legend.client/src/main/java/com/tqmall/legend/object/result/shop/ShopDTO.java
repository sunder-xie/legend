package com.tqmall.legend.object.result.shop;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by zsy on 15/12/15.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShopDTO extends BaseEntityDTO implements Serializable{
    private static final long serialVersionUID = 6954913466022382691L;
    private Long id;//门店id
    private String name;//门店名称
    private String provinceName;//省
    private String cityName;//市
    private String districtName;//街
    private String streetName;//区
    private String address;//详细地址
    private String mobile;//门店电话
    private String contact;//联系人
    private Integer status;//0-参加 1-不参加
    private String statusStr;

    public String getStatusStr(){
        statusStr = status==0?"参加":"未参加";
        return statusStr;
    }

}
