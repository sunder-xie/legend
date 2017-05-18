package com.tqmall.legend.object.result.appoint;

import com.tqmall.legend.object.result.customer.CustomerCarDTO;
import com.tqmall.legend.object.result.goods.GoodsDTO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by wushuai on 17/2/13.
 */
@Getter
@Setter
public class AppointDetailDTO implements Serializable{
    private static final long serialVersionUID = -4046058183866119806L;

    /**
     * 基本字段(预约单基本信息)
     */
    private Long id;
    private Long customerCarId;
    private String appointSn;
    private String license;
    private Long carBrandId;
    private String carBrandName;
    private Long carSeriesId;
    private String carSeriesName;
    private String carAlias;
    private String customerName;
    private String mobile;
    private Date appointTime;
    private String appointContent;
    private Long registrantId;
    private String registrantName;
    private Long shopId;
    private Long orderId;
    private String refer;//来源
    private Long previewType;//是否预览状态
    private Long pushStatus;//下推状态 0 滴滴过来的预约单要有下推操作 1 另外途径过来
    private String comment;//备注
    private BigDecimal appointAmount;//预约单金额
    private Long channel;//0 门店web, 1 商家app, 2 车主app, 3 车主微信, 4 橙牛
    private Long status;//0 待确认, 1 预约成功, 2 工单生成  3 车主取消, 4 门店取消
    private String cancelReason;//取消原因
    private String echelianid;//易车联下预约单带过来的
    private BigDecimal downPayment;//预付定金
    private Integer payStatus;//支付状态，0支付失败，1支付成功

    /**
     * 扩展字段
     */
    private BigDecimal totalServiceAmount;//服务总金额
    private BigDecimal totalGoodsAmount;//物料总金额
    private List<AppointServiceInfoDTO> appointServiceInfoDTOList;//预约单包含的服务列表
    private List<GoodsDTO> goodsDTOList;//预约单包含的物料列表
    private CustomerCarDTO customerCarDTO;//预约单对应的车辆信息

}
