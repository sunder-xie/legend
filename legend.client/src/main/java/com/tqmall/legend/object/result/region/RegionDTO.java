package com.tqmall.legend.object.result.region;

import com.tqmall.legend.object.result.base.BaseEntityDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsy on 15/12/15.
 */
@Data
public class RegionDTO extends BaseEntityDTO implements Serializable {
    private static final long serialVersionUID = 7073156673422938263L;
    private Integer id;             //城市id

    private Integer parentId;       //父id

    private String regionName;      //名称

    private Integer regionType;

    private Short agencyId;

    private String firstLetter;

    private Integer shopNum = 0;        //此城市门店总数

    private Integer shopJoinNum = 0;    //此城市参加活动的门店数量

    private List<RegionDTO> cityList;
}