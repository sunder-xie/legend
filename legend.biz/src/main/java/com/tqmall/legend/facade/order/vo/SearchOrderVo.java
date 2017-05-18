package com.tqmall.legend.facade.order.vo;

import com.tqmall.legend.entity.order.OrderStatusEnum;
import com.tqmall.legend.enums.order.OrderNewStatusEnum;
import com.tqmall.search.dubbo.client.legend.order.result.LegendOrderInfoDTO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dongc on 16/11/1.
 */
@Data
public class SearchOrderVo extends LegendOrderInfoDTO {


    // 创建时间
    private String createTimeStr;
    //车型
    private String carInfo;
    // 工单类型对应名称
    private String orderTypeName;
    //新规则的工单状态
    private String OrderStatusName;
    //档口店工单状态
    private String tqmallOrderStatusName;
    //物料出库标志
    private Integer goodsOutFlag = 0;

    public String getCreateTimeStr() {
        if (createTimeStr != null) {
            return createTimeStr;
        }
        if (this.getCreateTime() != null) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date createTimeDate = f.parse(this.getCreateTime());
                return f.format(createTimeDate);
            } catch (ParseException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public String getCarInfo() {
        StringBuffer sb = new StringBuffer();
        if (getCarBrand() != null) {
            sb.append(getCarBrand());
        }
        if (StringUtils.isNotBlank(getImportInfo())) {
            sb.append('(').append(getImportInfo()).append(')');
        }
        if (StringUtils.isNotBlank(getCarModels())) {
            sb.append(' ').append(getCarModels());
        } else if (StringUtils.isNotBlank(getCarSeries())) {
            sb.append(' ').append(getCarSeries());
        }

        return sb.toString();
    }

    public String getOrderStatusClientName() {
        if (this.getOrderStatus() != null) {
            return OrderStatusEnum.getorderStatusClientNameByKey(this.getOrderStatus());
        } else {
            return null;
        }

    }

    public String getOrderStatusName() {
        if (this.getOrderStatus() != null && this.getPayStatus() != null) {
            return OrderNewStatusEnum.getOrderStatusName(this.getOrderStatus(), this.getPayStatus());
        } else {
            return null;
        }

    }

}
