package com.tqmall.legend.entity.shop;

import com.tqmall.legend.entity.base.BaseEntity;
import com.tqmall.wheel.component.excel.annotation.Excel;
import com.tqmall.wheel.component.excel.annotation.ExcelCol;
import com.tqmall.wheel.component.excel.annotation.ExcelRowNumber;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Excel
public class ShopServiceInfo extends BaseEntity {
    @ExcelRowNumber
    private Integer rowNumber;
    @ExcelCol(value = 0,title = "*服务名称")
    @NotBlank(message = "服务名称不能为空")
    @Length(min = 1,max = 255,message = "服务名称长度不能超过255")
    private String name;
    @ExcelCol(value = 5,title = "服务编号")
    @Length(max = 20,message = "服务编号长度必须在0和20之间")
    private String serviceSn;

    private Long shopId;

    private Long tqmallServiceItemId;

    private String expectTime;
    @ExcelCol(value = 1,title = "服务价格")
    @DecimalMin(value = "0.00",message = "服务价格必须大于或等于0.00")
    private BigDecimal servicePrice;

    private Integer tqmallStatus;

    private Integer type;

    private Long parentId;

    private Long categoryId;
    @ExcelCol(value = 2,defaultValue = "小修",title = "服务类型(默认为小修)")
    @Length(max = 60,message = "服务类型长度不能超过60")
    private String categoryName;

    private Long carLevelId;

    private Long suiteNum;

    private Long suiteGoodsNum;

    private String flags;
    @ExcelCol(value = 3,title = "车辆级别")
    @Length(max = 50,message = "车辆级别长度不能超过50")
    private String carLevelName;

    private BigDecimal suiteAmount;//套餐价
    @ExcelCol(value = 4,defaultValue = "工时",title = "服务单位(默认为工时)")
    @Length(max = 5,message = "服务单位长度不能超过5")
    private String serviceUnit;

    private Long suiteServiceNum;

    private Long status;// 0有效-1无效

    // 额外属性
    private String serviceCatName;

    private Integer isParticipated;// 是否参加淘汽活动 1为参加 0为未参加

    // 支持大套餐(也指是指工时)
    private Long serviceNum = 1l;

    private BigDecimal serviceAmount;//工时金额=工时费(servicePrice)*工时(serviceNum)

    // 车主服务
    private Integer isRecommend;// 是否推荐

    private String serviceNote;// 服务备注

    private Long firstCateId;// 一级类目ID

    private String firstCateName;// 一类类目名称

    private Integer appCateId;// 车主类别ID

    private String appCateName;// 车主类别名称

    private Integer isShow;// 车主服务checkbox

    private String content;// 淘汽服务描述内容

    private Integer pictureType;// 配合前端图片显示

    //蒲公英模板服务小图片地址
    private String imgUrl;

    //微信模板服务图片地址
    private String thirdImgUrl;

    //第三方（如橙牛）模板服务图片详细信息json字符串
    private String thirdServiceInfo;

    private String goodsIdStr;// 服务对应的goodsID集合,逗号隔开

    private List<ServiceInfoList> serviceInfoList;

    private Long priceType;//服务价格类型 0 正常价格数值显示 1 到店洽谈 2 免费
    //排序
    private Long sort;
    //服务详情json字符串
    private String serviceInfo;

    //营销标
    private List<String> marketingFlagList;
    //品质标
    private List<String> qualityFlagList;

    private boolean isCheck;//门店的标准化服务是否存在，存在则选中

    private Integer cateTag;//存放服务的类别类目标签：标准7大类：1保养2洗车3美容4检修5钣喷6救援7其他
    private Integer auditStatus;//审核状态:0待审核,1审核通过,2审核失败，3退出活动
    private String shopReason;//门店参加、退出服务的原因
    private Date auditPassTime;//审核服务通过时间
    private String auditReason;//后台人员审核服务通过，失败的原因
    private Integer editStatus;//编辑状态, 0可编辑 1可部分编辑,价格等 2不可编辑
    private BigDecimal sharePrice; //共享中心结算价格
    private BigDecimal surfaceNum; //面数，用于喷漆、补漆类别的共享服务
    private BigDecimal marketPrice;//市场价格，仅做车主展示
    private Integer appPublishStatus;//发布车主服务状态:0未发布,1已发布
    private BigDecimal downPayment;//预付定金
    private Integer deleteStatus;//编辑状态, 0可删除 1不可删除

    private Integer isBPFW;//是否钣喷服务, 1是
    // 临时字段
	private Long shopActivityId;
    private String flagsName;

    /**
     * 将服务模板转换为门店的服务   wjc
     * @param serviceTemplate
     */
    public void setFromTemplate(ServiceTemplate serviceTemplate) {
        setName(serviceTemplate.getName());
        setServiceSn(serviceTemplate.getServiceSn());
        setServicePrice(serviceTemplate.getServicePrice());
        setFlags(serviceTemplate.getFlags());
        setServiceNote(serviceTemplate.getServiceNote());
        setImgUrl(serviceTemplate.getImgUrl());
        setThirdImgUrl(serviceTemplate.getThirdImgUrl());
        setThirdServiceInfo(serviceTemplate.getThirdServiceInfo());
        setSort(serviceTemplate.getSort().longValue());
        setPriceType(serviceTemplate.getPriceType().longValue());
        setServiceInfo(serviceTemplate.getServiceInfo());
        setCategoryId(serviceTemplate.getCateId());
        setCateTag(serviceTemplate.getCateTag());
        setType(1);//常规服务
    }

    public String getFlagsName(){
        if(ServiceFlagsEnum.getMesByCode(flags) != null){
            return ServiceFlagsEnum.getMesByCode(flags);
        }else{
            return null;
        }
    }

    /**
     * marketPrice不能低于servicePrice,suiteAmount
     *
     * @return
     */
    public BigDecimal getMarketPrice() {
        suiteAmount = suiteAmount == null ? new BigDecimal("0.00") : suiteAmount;
        servicePrice = servicePrice == null ? new BigDecimal("0.00") : servicePrice;
        marketPrice = marketPrice == null ? new BigDecimal("0.00") : marketPrice;
        if (suiteAmount.compareTo(marketPrice) > 0) {
            return suiteAmount;
        }
        if (servicePrice.compareTo(marketPrice) > 0) {
            return servicePrice;
        }
        return marketPrice;
    }
}
