package com.tqmall.legend.web.warehouse.convert;

import com.tqmall.legend.enums.warehouse.WarehouseInStatusEnum;
import com.tqmall.legend.facade.warehouse.vo.LegendWarehouseInDTOVo;
import com.tqmall.legend.facade.warehouse.vo.WarehouseInDetailVo;
import com.tqmall.legend.web.warehouse.vo.WarehouseInExportVO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by lilige on 17/2/8.
 */
public class WarehouseInExportConvert {

    public static List<WarehouseInExportVO> convert(LegendWarehouseInDTOVo warehouseInVo){
        List<WarehouseInDetailVo> detailVoList = warehouseInVo.getDetailVoList();
        if (CollectionUtils.isEmpty(detailVoList)){
            return Collections.emptyList();
        }
        List<WarehouseInExportVO> exportVOList = new ArrayList<>(detailVoList.size());
        for (WarehouseInDetailVo detail : detailVoList){
            WarehouseInExportVO exportVO = new WarehouseInExportVO();
            exportVO.setWarehouseInSn(warehouseInVo.getWarehouseInSn());
            String status = WarehouseInStatusEnum.getMessageByName(warehouseInVo.getStatus());
            exportVO.setStatus(status);
            exportVO.setInTime(warehouseInVo.getInTime());
            exportVO.setGoodsFormat(detail.getGoodsFormat());
            exportVO.setGoodsName(detail.getGoodsName());
            exportVO.setCatName(detail.getCatName());
            exportVO.setPurchasePrice(detail.getPurchasePrice());
            exportVO.setGoodsCount(detail.getGoodsCount());
            exportVO.setMeasureUnit(detail.getMeasureUnit());
            exportVO.setPurchaseAmount(detail.getPurchaseAmount());
            exportVO.setDepot(detail.getDepot());
            exportVO.setCarInfoStr(detail.getCarInfoStr());
            exportVO.setSupplierName(warehouseInVo.getSupplierName());
            exportVO.setPurchaseAgentName(warehouseInVo.getPurchaseAgentName());
            exportVO.setOperatorName(warehouseInVo.getOperatorName());
            exportVO.setComment(warehouseInVo.getComment());
            exportVOList.add(exportVO);
        }
        return exportVOList;
    }

}
