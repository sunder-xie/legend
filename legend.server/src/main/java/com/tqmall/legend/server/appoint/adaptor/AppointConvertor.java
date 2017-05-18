package com.tqmall.legend.server.appoint.adaptor;

import com.tqmall.legend.entity.customer.Appoint;
import com.tqmall.legend.entity.customer.AppointServiceVo;
import com.tqmall.legend.entity.customer.AppointVo;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.facade.appoint.vo.AppointDetailFacVo;
import com.tqmall.legend.object.result.appoint.AppointDTO;
import com.tqmall.legend.object.result.appoint.AppointDetailDTO;
import com.tqmall.legend.object.result.appoint.AppointServiceInfoDTO;
import com.tqmall.legend.object.result.goods.GoodsDTO;
import com.tqmall.legend.object.result.service.ShopServiceInfoDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushuai on 17/2/13.
 */
public class AppointConvertor {

    public static List<AppointDTO> convert(List<Appoint> appointList) {
        List<AppointDTO> appointDTOList = new ArrayList<>();
        if (CollectionUtils.isEmpty(appointList)) {
            return appointDTOList;
        }
        for (Appoint appoint : appointList) {
            AppointDTO appointDTO = new AppointDTO();
            BeanUtils.copyProperties(appointDTO, appoint);
            appointDTOList.add(appointDTO);
        }
        return appointDTOList;
    }

    public static AppointDetailDTO convert(AppointDetailFacVo appointDetailFacVo) {
        if (appointDetailFacVo==null) {
            return null;
        }
        AppointDetailDTO appointDetailDTO = new AppointDetailDTO();
        AppointVo appointVo = appointDetailFacVo.getAppointVo();
        if (appointVo != null) {
            BeanUtils.copyProperties(appointVo, appointDetailDTO);
        }
        //.服务列表
        List<AppointServiceVo> appointServiceVoList = appointDetailFacVo.getAppointServiceVoList();
        if (!CollectionUtils.isEmpty(appointServiceVoList)) {
            List<AppointServiceInfoDTO> appointServiceInfoDTOList = new ArrayList<>();
            for (AppointServiceVo appointServiceVo : appointServiceVoList) {
                AppointServiceInfoDTO appointServiceInfoDTO = new AppointServiceInfoDTO();
                BeanUtils.copyProperties(appointServiceVo,appointServiceInfoDTO);
                appointServiceInfoDTOList.add(appointServiceInfoDTO);
            }
            appointDetailDTO.setAppointServiceInfoDTOList(appointServiceInfoDTOList);
        }

        //.物料列表
        List<Goods> goodsList = appointDetailFacVo.getGoodsList();
        if (!CollectionUtils.isEmpty(goodsList)) {
            List<GoodsDTO> goodsDTOList = new ArrayList<>();
            for (Goods goods : goodsList) {
                GoodsDTO goodsDTO = new GoodsDTO();
                BeanUtils.copyProperties(goods, goodsDTO);
                goodsDTOList.add(goodsDTO);
            }
            appointDetailDTO.setGoodsDTOList(goodsDTOList);
        }
        return appointDetailDTO;
    }
}
