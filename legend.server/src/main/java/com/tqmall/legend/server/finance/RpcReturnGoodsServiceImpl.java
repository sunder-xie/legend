package com.tqmall.legend.server.finance;

import com.google.common.collect.Lists;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.error.LegendThirdPartyErrorCode;
import com.tqmall.legend.biz.subsidy.SubsidyActivityService;
import com.tqmall.legend.dao.shop.ShopDao;
import com.tqmall.legend.dao.subsidy.ShopOrderSubsidyDao;
import com.tqmall.legend.dao.subsidy.SubsidyGoodsDao;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.subsidy.ShopOrderSubsidy;
import com.tqmall.legend.entity.subsidy.SubsidyGoods;
import com.tqmall.legend.object.param.finance.VerifyParam;
import com.tqmall.legend.service.finance.RpcReturnGoodsService;
import com.tqmall.venus.service.common.OrderGoodsService;
import com.tqmall.venus.service.common.dto.OrderGoodsDTO;
import com.tqmall.zenith.errorcode.support.SourceKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jason on 16/4/5.
 */
@Slf4j
@Service("rpcReturnGoodsService")
public class RpcReturnGoodsServiceImpl implements RpcReturnGoodsService {
    //订单中心的
    @Autowired
    private OrderGoodsService rpcOrderGoodsService;
    @Autowired
    private ShopOrderSubsidyDao shopOrderSubsidyDao;



    @Override
    public Result<Boolean> newVerifySubsidyGoods(List<VerifyParam> paramList) {
        log.info("调用退换货校验返利接口传参：{}", JSONUtil.object2Json(paramList));
        if (CollectionUtils.isEmpty(paramList)) {
            return Result.wrapErrorResult(LegendErrorCode.RETURNS_NO_VERIFYPARAM.getCode(), LegendErrorCode.RETURNS_NO_VERIFYPARAM.getErrorMessage());
        }
        //去除赠品参数
        this._removeGiftParam(paramList);
        //审核商品退货数量是否超量
        return this._goodsCheck(paramList);
    }


    //审核商品退货数量是否超量
    private Result<Boolean> _goodsCheck(List<VerifyParam> paramList) {
        //key:订单号，value：商品退货信息集合
        Map<String, List<VerifyParam>> verifyParamMap = new HashMap<>();
        for (VerifyParam verifyParam : paramList) {
            String orderSn = verifyParam.getOrderSn();//订单号
            if (verifyParamMap.containsKey(orderSn)) {
                verifyParamMap.get(orderSn).add(verifyParam);
            } else {
                verifyParamMap.put(orderSn, Lists.newArrayList(verifyParam));
            }
        }

        for (Map.Entry<String, List<VerifyParam>> entry : verifyParamMap.entrySet()) {
            String orderSn = entry.getKey();//订单号
            List<VerifyParam> verifyParams = entry.getValue();
            //key:商品id，value:商品退货数量
            Map<Integer, Integer> numMap = new HashMap<>();
            for (VerifyParam verifyParam : verifyParams) {
                Integer goodsId = verifyParam.getGoodsId();
                if (numMap.containsKey(goodsId)) {
                    numMap.put(goodsId, verifyParam.getGoodsNum() + numMap.get(goodsId));
                } else {
                    numMap.put(goodsId, verifyParam.getGoodsNum());
                }
            }

            //门店userGlobalId
            String userGlobalId = verifyParams.get(0).getUserGlobalId().toString();
            for (Map.Entry<Integer, Integer> entry1 : numMap.entrySet()) {
                Integer goodsId = entry1.getKey();//商品id
                Integer num = entry1.getValue();//商品退货数量
                Map paramMap = new HashMap();
                paramMap.put("goodsId", goodsId);
                paramMap.put("orderSn", orderSn);
                paramMap.put("userGlobalId", userGlobalId);
                List<ShopOrderSubsidy> shopOrderSubsidyList = shopOrderSubsidyDao.select(paramMap);

                if (!CollectionUtils.isEmpty(shopOrderSubsidyList)) {
                    ShopOrderSubsidy shopOrderSubsidy = shopOrderSubsidyList.get(0);
                    if (shopOrderSubsidy != null) {
                        int confirmNum = 0;//可退货的数量
                        int freezeStatus = shopOrderSubsidy.getFreezeStatus();//冻结状态
                        if (freezeStatus == 0) {
                            confirmNum = shopOrderSubsidy.getHasActiveCount().intValue();
                        } else if (freezeStatus == 1) {
                            confirmNum = shopOrderSubsidy.getWaitActiveCount().intValue();
                        }
                        log.info("订单号:{},商品ID:{},冻结状态:{},可退货数量:{},退货数量:{}", orderSn, goodsId, freezeStatus, confirmNum, num);
                        if (confirmNum < num) {
                            log.info("退货商品数量过多，退货信息：{}", JSONUtil.object2Json(verifyParams));
                            StringBuffer m = new StringBuffer();
                            m.append("订单").append(orderSn).append("中，").append("商品").append(shopOrderSubsidy.getGoodsName()).append("的最大可退货数量为").append(confirmNum).append("，退货数量超过最大可退货数");
                            return Result.wrapErrorResult(LegendThirdPartyErrorCode.RETURNS_EXCEED_LIMIT.getCode(), m.toString());
                        }
                    }
                }
            }
        }
        return Result.wrapSuccessfulResult(true);
    }


    //去除赠品
    private void _removeGiftParam(List<VerifyParam> paramList) {
        VerifyParam vp = paramList.get(0);
        Long userGlobalId = vp.getUserGlobalId();
        if (!SourceKey.TQMALLSTALL.getKey().equals(vp.getSource())) {
            //合并重复的商品信息
            List<Integer> orderGoodsIdList = new ArrayList<>();//商品orderGoodsId集合
            //key：商品orderGoodsId，value：VerifyParam
            Map<Integer, VerifyParam> goodsMap = new HashMap<>();
            for (VerifyParam verifyParam : paramList) {
                Integer orderGoodsId = verifyParam.getOrderGoodsId();
                orderGoodsIdList.add(orderGoodsId);
                if (goodsMap.containsKey(orderGoodsId)) {
                    VerifyParam verifyParam1 = goodsMap.get(orderGoodsId);
                    //设置商品数量
                    verifyParam1.setGoodsNum(verifyParam.getGoodsNum() + verifyParam1.getGoodsNum());
                } else {
                    goodsMap.put(orderGoodsId, verifyParam);
                }
            }
            paramList.clear();
            for (VerifyParam verifyParam : goodsMap.values()) {
                paramList.add(verifyParam);
            }

            //根据orderGoodsId集合查询对应商品信息
            log.info("userGlobalId为:{}的门店调用订单中心用合并后的商品信息的orderGoodsId的集合查询商品信息接口,退货信息:{}", userGlobalId, JSONUtil.object2Json(paramList));
            Result<List<OrderGoodsDTO>> orderGoodsDTOsResult = rpcOrderGoodsService.queryOrderGoodsByIdList(orderGoodsIdList);
            log.info("userGlobalId为:{}的门店调用订单中心用orderGoodsId集合查询商品信息接口,返回结果:{}", userGlobalId, JSONUtil.object2Json(orderGoodsDTOsResult));
            if (orderGoodsDTOsResult != null && orderGoodsDTOsResult.isSuccess()) {
                List<OrderGoodsDTO> orderGoodsDTOList = orderGoodsDTOsResult.getData();
                if (!CollectionUtils.isEmpty(orderGoodsDTOList)) {
                    for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
                        if (orderGoodsDTO.getIsGift() == 1) {  //是赠品
                            paramList.remove(goodsMap.get(orderGoodsDTO.getId()));
                        }
                    }
                    log.info("userGlobalId为:{}的门店去除赠品后的退货信息:{}", userGlobalId, JSONUtil.object2Json(paramList));
                }
            }
        }
    }
}
