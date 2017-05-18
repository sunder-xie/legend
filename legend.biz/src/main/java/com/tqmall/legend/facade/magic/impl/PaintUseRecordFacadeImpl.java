package com.tqmall.legend.facade.magic.impl;

import com.tqmall.common.util.BdUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.magic.PaintUseRecordFacade;
import com.tqmall.legend.facade.magic.vo.PaintRecordDetailVo;
import com.tqmall.legend.facade.magic.vo.PaintUseRecordVo;
import com.tqmall.magic.object.param.paint.PaintUseRecordParam;
import com.tqmall.magic.object.result.common.PageEntityDTO;
import com.tqmall.magic.object.result.paint.PaintRecordDetailDTO;
import com.tqmall.magic.object.result.paint.PaintUseRecordDTO;
import com.tqmall.magic.service.paint.RpcPaintRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务场景：油漆使用记录相关业务
 * Created by shulin on 16/11/15.
 */
@Service
@Slf4j
public class PaintUseRecordFacadeImpl implements PaintUseRecordFacade {

    @Autowired
    private RpcPaintRecordService rpcPaintRecordService;

    @Override
    public PaintUseRecordVo getPaintUseRecord(Long shopId, Long useRecordId) {
        Result<PaintUseRecordDTO> result = rpcPaintRecordService.getPaintUseRecord(useRecordId, shopId);
        if (!result.isSuccess()) {
            log.info("[油漆使用记录] 调用Magic DUBBO接口获取记录失败，shopId={},useRecordId={},Msg={}", shopId, useRecordId, result.getMessage());
            return null;
        }
        PaintUseRecordDTO paintUseRecordDTO = result.getData();
        if (paintUseRecordDTO == null) {
            log.info("[油漆使用记录] 调用Magic DUBBO接口返回数据为空，shopId={},useRecordId={},Msg={}", shopId, useRecordId, result.getMessage());
            return null;
        }
        PaintUseRecordVo paintUseRecordVo = BdUtil.bo2do(paintUseRecordDTO, PaintUseRecordVo.class);
        List<PaintRecordDetailDTO> paintRecordDetailDTOList = paintUseRecordDTO.getPaintRecordDetailDTOList();
        List<PaintRecordDetailVo> paintRecordDetailVoList = null;
        if (!CollectionUtils.isEmpty(paintRecordDetailDTOList)) {
            int i = 1;
            paintRecordDetailVoList = new ArrayList<PaintRecordDetailVo>();
            for (PaintRecordDetailDTO tmp : paintRecordDetailDTOList) {
                PaintRecordDetailVo paintRecordDetailVo = BdUtil.bo2do(tmp, PaintRecordDetailVo.class);
                paintRecordDetailVo.setSeqno(i++);
                paintRecordDetailVoList.add(paintRecordDetailVo);
            }
        }
        paintUseRecordVo.setPaintRecordDetailVoList(paintRecordDetailVoList);
        return paintUseRecordVo;
    }

    @Override
    public DefaultPage<PaintUseRecordVo> getPaintUseRecordListByPage(PaintUseRecordParam param, Pageable pageable) {
        Result<PageEntityDTO<PaintUseRecordDTO>> result = rpcPaintRecordService.getPaintUseRecordsByPage(param);
        if (!result.isSuccess()) {
            log.info("[油漆使用记录] 获取油漆使用列表失败！Msg={}", result.getMessage());
            return null;
        }
        PageEntityDTO<PaintUseRecordDTO> resultData = result.getData();
        if (resultData == null) {
            log.info("[油漆使用记录] 获取油漆使用列表成功！返回数据为空！");
            return null;
        }
        List<PaintUseRecordDTO> paintUseRecordDTOList = resultData.getRecordList();
        if (CollectionUtils.isEmpty(paintUseRecordDTOList)) {
            log.info("[油漆使用记录] 获取油漆使用列表为空！");
            return null;
        }
        List<PaintUseRecordVo> paintUseRecordVoList = BdUtil.do2bo4List(paintUseRecordDTOList, PaintUseRecordVo.class);
        return new DefaultPage<PaintUseRecordVo>(paintUseRecordVoList, pageable, result.getData().getTotalNum());
    }

    @Override
    public Long addPaintUseRecord(PaintUseRecordVo paintUseRecordVo, Long shopId) {
        PaintUseRecordDTO paintUseRecordDTO = BdUtil.bo2do(paintUseRecordVo, PaintUseRecordDTO.class);
        paintUseRecordDTO.setShopId(shopId);
        List<PaintRecordDetailVo> paintRecordDetailVoList = paintUseRecordVo.getPaintRecordDetailVoList();
        List<PaintRecordDetailDTO> paintRecordDetailDTOList = null;
        if (!CollectionUtils.isEmpty(paintRecordDetailVoList)) {
            paintRecordDetailDTOList = new ArrayList<>();
            for (PaintRecordDetailVo tmp : paintRecordDetailVoList) {
                PaintRecordDetailDTO paintRecordDetailDTO = BdUtil.bo2do(tmp, PaintRecordDetailDTO.class);
                paintRecordDetailDTO.setShopId(shopId);
                paintRecordDetailDTOList.add(paintRecordDetailDTO);
            }
        }
        paintUseRecordDTO.setPaintRecordDetailDTOList(paintRecordDetailDTOList);
        Result<Long> result = rpcPaintRecordService.addPaintUserRecord(paintUseRecordDTO);

        if (!result.isSuccess()) {
            return 0l;
        }
        return result.getData();
    }
}
