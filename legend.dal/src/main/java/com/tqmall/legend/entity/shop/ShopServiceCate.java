package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.tqmall.legend.entity.base.BaseEntity;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
public class ShopServiceCate extends BaseEntity {

    private String name;
    private Long shopId;
    private Long parentId;
    private Integer cateType;                               //类别类型，0门店服务，1淘汽、车主服务，2，标准服务类别
    private Integer cateTag;                                //标准服务标签，默认0，标准7大类：1保养2洗车3美容4检修5钣喷6救援7其他
    private String iconUrl; //类型图标url
    private String defaultImgUrl;//该类目下服务的默认图片;
    private Integer cateSort;//类别排序,默认降序
    private String firstCateName;                           //一类类目名称

    private boolean isCheck  = false;                       //临时存放门店是否添加了此服务类别
    private List<ServiceTemplate> serviceTemplateList;      //临时存放此类目下的标准化服务

    //是否为顶级类目
    public boolean isTop() {
        if(parentId == null){
            return false;
        }
        return parentId.equals(0l);
    }
}
