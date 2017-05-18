package com.tqmall.legend.biz.account.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.account.CardGoodsRelService;
import com.tqmall.legend.biz.account.CardServiceRelService;
import com.tqmall.legend.biz.account.MemberCardInfoService;
import com.tqmall.legend.biz.account.bo.CardCreateBo;
import com.tqmall.legend.biz.account.bo.GoodsCatBo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.dao.account.MemberCardDao;
import com.tqmall.legend.dao.account.MemberCardInfoDao;
import com.tqmall.legend.entity.account.CardGoodsRel;
import com.tqmall.legend.entity.account.CardServiceRel;
import com.tqmall.legend.entity.account.MemberCardInfo;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import com.tqmall.wheel.lang.Langs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wanghui on 6/2/16.
 */
@Service
@Slf4j
public class MemberCardInfoServiceImpl extends BaseServiceImpl implements MemberCardInfoService {


    @Autowired
    private MemberCardInfoDao memberCardInfoDao;
    @Autowired
    private CardServiceRelService cardServiceRelService;
    @Autowired
    private CardGoodsRelService cardGoodsRelService;
    @Autowired
    private MemberCardDao memberCardDao;
    @Autowired
    private ShopServiceCateService shopServiceCateService;

    public List<MemberCardInfo> findAllByShopId(Long shopId,Integer type) {
        Assert.notNull(shopId);

        HashMap<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("sorts",new String[]{"gmt_create desc", "gmt_modified desc"});
        paramMap.put("shopId", shopId);
        if(null!=type&&type==1){
            paramMap.put("cardInfoStatus",MemberCardInfo.ENABLED);
        }
        List<MemberCardInfo> memberCardInfoList = this.memberCardInfoDao.select(paramMap);

        /**
         * 内存中组装会员卡信息
         */
        List<CommonPair<Long, Integer>> usedCardTypeList = memberCardDao.findUsedCardType(shopId);
        Map<Long, CommonPair<Long, Integer>> usedCardTypeMap = Maps.uniqueIndex(usedCardTypeList, new Function<CommonPair<Long, Integer>, Long>() {
            @Override
            public Long apply(CommonPair<Long, Integer> input) {
                return input.getDataF();
            }
        });
        for (MemberCardInfo memberCardInfo : memberCardInfoList) {
            if (usedCardTypeMap.containsKey(memberCardInfo.getId())) {
                memberCardInfo.setGrantedCount(usedCardTypeMap.get(memberCardInfo.getId()).getDataS());
            } else {
                memberCardInfo.setGrantedCount(0);
            }
        }

        /**
         * 内存中组装关联服务、关联物料信息
         */
        for (MemberCardInfo memberCardInfo : memberCardInfoList) {
            attachDiscount(memberCardInfo);
        }

        if(!CollectionUtils.isEmpty(memberCardInfoList)){
            memberCardInfoList.get(0).setSize(memberCardInfoList.size());
        }

        return memberCardInfoList;
    }


    public MemberCardInfo findById(Long id) {
        return super.getById(memberCardInfoDao, id);
    }

    public boolean saveMemberCardInfo(MemberCardInfo memberCardInfo) {
        memberCardInfo.setCardInfoStatus(MemberCardInfo.ENABLED);
        return super.save(memberCardInfoDao, memberCardInfo);
    }

    public boolean deleteMemberCardInfoById(Long shopId, Long id) {
        HashMap param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("id", id);
        cardGoodsRelService.deleteByCardInfoId(shopId,id);
        cardServiceRelService.deleteByCardInfoId(shopId,id);
        return memberCardInfoDao.delete(param) > 0;

    }

    @Override
    public MemberCardInfo findByCardTypeName(Long shopId, String cardTypeName) {
        Assert.notNull(shopId);
        Assert.notNull(cardTypeName);
        HashMap<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("shopId", shopId);
        paramMap.put("typeName", cardTypeName);
        List<MemberCardInfo> memberCardInfos = this.memberCardInfoDao.select(paramMap);
        if (CollectionUtils.isNotEmpty(memberCardInfos)) {
            if (memberCardInfos.size() == 1) {
                return memberCardInfos.get(0);
            } else {
                log.error("门店[{}]存在{}张同名的会员卡.", shopId, cardTypeName);
            }
        }
        return null;
    }

    @Override
    public void enableCardInfoById(Long id,UserInfo userInfo) {
        MemberCardInfo memberCardInfo = new MemberCardInfo();
        memberCardInfo.setId(id);
        memberCardInfo.setCardInfoStatus(MemberCardInfo.ENABLED);
        memberCardInfo.setModifier(userInfo.getUserId());
        memberCardInfoDao.updateById(memberCardInfo);
    }

    @Override
    public void disableCardInfoById(Long id, UserInfo userInfo) {
        MemberCardInfo memberCardInfo = new MemberCardInfo();
        memberCardInfo.setId(id);
        memberCardInfo.setCardInfoStatus(MemberCardInfo.DISABLED);
        memberCardInfo.setModifier(userInfo.getUserId());
        memberCardInfoDao.updateById(memberCardInfo);
    }
    @Override
    public List<MemberCardInfo> selectInfoByIds(Long shopId, List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return memberCardInfoDao.selectByIds(shopId,ids);
    }

    @Override
    public Integer selectCount(Long shopId) {
        Map<String,Object> paramMap = Maps.newHashMap();
        paramMap.put("shopId", shopId);
        return this.memberCardInfoDao.selectCount(paramMap);
    }

    @Override
    public List<MemberCardInfo> selectByNames(List<String> names, Long shopId) {
        return memberCardInfoDao.selectByNames(names, shopId);
    }


    @Override
    public void create(CardCreateBo createBo) {
        if (isCardInfoNameRepeated(createBo.getShopId(), null,createBo.getName())) {
            throw new BizException("会员卡名称重复");
        }

        MemberCardInfo cardInfo = convertBo2CardInfo(createBo);
        cardInfo.setCardInfoStatus(MemberCardInfo.ENABLED);
        memberCardInfoDao.insert(cardInfo);

        saveServiceRel(createBo, cardInfo);

        saveGoodsRel(createBo, cardInfo);

    }

    private void saveServiceRel(CardCreateBo createBo, MemberCardInfo cardInfo) {
        if (isPartServiceDiscount(cardInfo)) {
            Map<Long, BigDecimal> serviceCatRelMap = createBo.getServiceCatRels();
            Set<Long> serviceCatIds = serviceCatRelMap.keySet();
            if (CollectionUtils.isNotEmpty(serviceCatIds)) {
                List<ShopServiceCate> shopServiceCates = shopServiceCateService.list(createBo.getShopId(),serviceCatIds);
                List<CardServiceRel> cardServiceRels = composeCardServiceRels(cardInfo, serviceCatRelMap, shopServiceCates);
                cardServiceRelService.batchSave(cardServiceRels);
            }
        }
    }

    private boolean isPartGoodsDiscount(MemberCardInfo cardInfo) {return (cardInfo.getDiscountType().equals(3)||cardInfo.getDiscountType().equals(4)) && Integer.valueOf(2).equals(cardInfo.getGoodDiscountType());}

    private boolean isPartServiceDiscount(MemberCardInfo cardInfo) {return (cardInfo.getDiscountType().equals(2)||cardInfo.getDiscountType().equals(4)) && Integer.valueOf(2).equals(cardInfo.getServiceDiscountType());}

    private boolean isCardInfoNameRepeated(Long shopId, Long cardInfoId, String cardInfoName) {
        MemberCardInfo cardInfo = findByCardTypeName(shopId, cardInfoName);
        if (cardInfo == null) {
            return false;
        }
        if (cardInfo.getId().equals(cardInfoId)) {
            return false;
        }
        return true;
    }


    @Override
    public void update(CardCreateBo updateBo) {
        Long shopId = updateBo.getShopId();
        Long cardInfoId = updateBo.getId();
        if (isCardInfoNameRepeated(shopId, cardInfoId,updateBo.getName())) {
            throw new BizException("会员卡名称重复");
        }
        if(isGranted(shopId,cardInfoId)) {
            throw new BizException("会员卡已使用");
        }
        MemberCardInfo cardInfo = convertBo2CardInfo(updateBo);
        cardInfo.setId(updateBo.getId());
        memberCardInfoDao.update(cardInfo);

        cardServiceRelService.deleteByCardInfoId(updateBo.getShopId(),cardInfo.getId());
        saveServiceRel(updateBo, cardInfo);

        cardGoodsRelService.deleteByCardInfoId(updateBo.getShopId(), cardInfo.getId());
        saveGoodsRel(updateBo, cardInfo);

    }

    private void saveGoodsRel(CardCreateBo updateBo, MemberCardInfo cardInfo) {
        if (isPartGoodsDiscount(cardInfo)) {
            List<GoodsCatBo> goodsCats = updateBo.getGoodsCats();
            if (CollectionUtils.isNotEmpty(goodsCats)) {
                List<CardGoodsRel> cardGoodsRels = composeCardGoodsRels(cardInfo, goodsCats);
                cardGoodsRelService.batchSave(cardGoodsRels);
            }
        }
    }

    private boolean isGranted(Long shopId, Long cardInfoId) {
        return getGrantNumber(shopId, cardInfoId) > 0;
    }

    private int getGrantNumber(Long shopId, Long cardInfoId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("cardTypeId", cardInfoId);
        return memberCardDao.selectCount(param);
    }

    private List<CardGoodsRel> composeCardGoodsRels(MemberCardInfo cardInfo, List<GoodsCatBo> goodsCats) {
        List<CardGoodsRel> cardGoodsRels = Lists.newArrayList();
        for (GoodsCatBo goodsCatBo : goodsCats) {
            CardGoodsRel cardGoodsRel = new CardGoodsRel();
            cardGoodsRel.setShopId(cardInfo.getShopId());
            cardGoodsRel.setCreator(cardInfo.getCreator());
            cardGoodsRel.setModifier(cardInfo.getModifier());

            cardGoodsRel.setCardInfoId(cardInfo.getId());
            cardGoodsRel.setGoodsCatId(goodsCatBo.getCatId());
            cardGoodsRel.setGoodsCatName(goodsCatBo.getCatName());
            cardGoodsRel.setDiscount(goodsCatBo.getDiscount());
            cardGoodsRel.setGoodsCatSource(goodsCatBo.getCatSource());
            cardGoodsRels.add(cardGoodsRel);
        }
        return cardGoodsRels;
    }

    private List<CardServiceRel> composeCardServiceRels(MemberCardInfo cardInfo, Map<Long, BigDecimal> serviceCatRelMap, List<ShopServiceCate> shopServiceCates) {
        List<CardServiceRel> cardServiceRels = Lists.newArrayList();
        for (ShopServiceCate shopServiceCate : shopServiceCates) {
            CardServiceRel cardServiceRel = new CardServiceRel();
            cardServiceRel.setShopId(cardInfo.getShopId());
            cardServiceRel.setModifier(cardInfo.getCreator());
            cardServiceRel.setCreator(cardInfo.getModifier());
            Long serviceCatId = shopServiceCate.getId();
            cardServiceRel.setServiceCatId(serviceCatId);
            cardServiceRel.setServiceCatName(shopServiceCate.getName());
            cardServiceRel.setDiscount(serviceCatRelMap.get(serviceCatId));
            cardServiceRel.setCardInfoId(cardInfo.getId());
            cardServiceRels.add(cardServiceRel);
        }
        return cardServiceRels;
    }

    private MemberCardInfo convertBo2CardInfo(CardCreateBo cardCreateBo) {
        MemberCardInfo cardInfo = new MemberCardInfo();
        cardInfo.setShopId(cardCreateBo.getShopId());
        cardInfo.setModifier(cardCreateBo.getOperatorId());
        cardInfo.setCreator(cardCreateBo.getOperatorId());

        cardInfo.setTypeName(cardCreateBo.getName());
        cardInfo.setCardInfoExplain(cardCreateBo.getDescript());
        cardInfo.setOrderDiscount(cardCreateBo.getOrderDiscount());
        cardInfo.setServiceDiscountType(cardCreateBo.getServiceDiscountType());
        cardInfo.setServiceDiscount(cardCreateBo.getServiceDiscount());
        cardInfo.setGoodDiscountType(cardCreateBo.getGoodsDiscountType());
        cardInfo.setGoodDiscount(cardCreateBo.getGoodsDiscount());
        cardInfo.setDiscountType(cardCreateBo.getDiscountType());
        cardInfo.setEffectivePeriodDays(cardCreateBo.getEffectivePeriodDays().longValue());
        cardInfo.setCompatibleWithCoupon(cardCreateBo.isCompatibleWithCoupon() ? 1 : 0);
        cardInfo.setGeneralUse(cardCreateBo.isGeneralUse() ? 1 : 0);
        cardInfo.setInitBalance(cardCreateBo.getInitBalance());
        cardInfo.setSalePrice(cardCreateBo.getSalePrice());
        return cardInfo;
    }

    @Override
    public MemberCardInfo findById(Long shopId, Long cardTypeId) {
        Assert.isTrue(cardTypeId != null && cardTypeId > 0);
        MemberCardInfo cardInfo = memberCardInfoDao.getInfoById(cardTypeId);
        if (cardInfo == null) {
            return null;
        }
        attachDiscount(cardInfo);
        return cardInfo;
    }

    @Override
    public void attachDiscount(MemberCardInfo cardInfo) {
        boolean isPartService = cardInfo.getServiceDiscountType().equals(2);
        boolean isPartGoods = cardInfo.getGoodDiscountType().equals(2);
        if(isPartService) {
            List<CardServiceRel> cardServiceRelList = cardServiceRelService.listByCardInfoId(cardInfo.getShopId(), cardInfo.getId());
            if (CollectionUtils.isNotEmpty(cardServiceRelList)) {
                cardInfo.setCardServiceRels(cardServiceRelList);
            }
        }
        if (isPartGoods) {
            List<CardGoodsRel> cardGoodsRelList = cardGoodsRelService.selectCardGoodsByCardInfoId(cardInfo.getShopId(), cardInfo.getId());
            if (CollectionUtils.isNotEmpty(cardGoodsRelList)) {
                cardInfo.setCardGoodsRels(cardGoodsRelList);
            }
        }
    }

    @Override
    public void attachDiscount(List<MemberCardInfo> cardInfoList) {
        if (CollectionUtils.isEmpty(cardInfoList)) {
            return;
        }
        Long shopId = cardInfoList.get(0).getShopId();
        Set<Long> serviceDiscountCardIds = new HashSet<>();
        Set<Long> goodsDiscountCardIds = new HashSet<>();
        for (MemberCardInfo memberCardInfo : cardInfoList) {
            int serviceDiscountType = memberCardInfo.getServiceDiscountType() == null ? 0 : memberCardInfo.getServiceDiscountType();
            int goodsDiscountType = memberCardInfo.getGoodDiscountType() == null ? 0 : memberCardInfo.getGoodDiscountType();
            if (serviceDiscountType == 2) {
                serviceDiscountCardIds.add(memberCardInfo.getId());
            }
            if (goodsDiscountType == 2) {
                goodsDiscountCardIds.add(memberCardInfo.getId());
            }
        }
        List<CardServiceRel> cardServiceRel = cardServiceRelService.selectByCardInfoIds(shopId, serviceDiscountCardIds);
        Map<Long, List<CardServiceRel>> serviceDiscountListMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(cardServiceRel)) {
            for (CardServiceRel serviceRel : cardServiceRel) {
                Long cardInfoId = serviceRel.getCardInfoId();
                List<CardServiceRel> serviceDiscountList = serviceDiscountListMap.get(cardInfoId);
                if (serviceDiscountList == null) {
                    serviceDiscountList = new ArrayList<>();
                }
                serviceDiscountList.add(serviceRel);
                serviceDiscountListMap.put(cardInfoId, serviceDiscountList);
            }
        }
        List<CardGoodsRel> cardGoodsRelList = cardGoodsRelService.selectByCardInfoIds(shopId, goodsDiscountCardIds);
        Map<Long, List<CardGoodsRel>> goodsDiscountListMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(cardGoodsRelList)) {
            for (CardGoodsRel cardGoodsRel : cardGoodsRelList) {
                Long cardInfoId = cardGoodsRel.getCardInfoId();
                List<CardGoodsRel> goodsDiscountList = goodsDiscountListMap.get(cardInfoId);
                if (goodsDiscountList == null) {
                    goodsDiscountList = new ArrayList<>();
                }
                goodsDiscountList.add(cardGoodsRel);
                goodsDiscountListMap.put(cardInfoId, goodsDiscountList);
            }
        }
        for (MemberCardInfo memberCardInfo : cardInfoList) {
            int serviceDiscountType = memberCardInfo.getServiceDiscountType() == null ? 0 : memberCardInfo.getServiceDiscountType();
            int goodsDiscountType = memberCardInfo.getGoodDiscountType() == null ? 0 : memberCardInfo.getGoodDiscountType();
            if (serviceDiscountType == 2) {
                List<CardServiceRel> serviceDiscountList = serviceDiscountListMap.get(memberCardInfo.getId());
                if (!CollectionUtils.isEmpty(serviceDiscountList)) {
                    memberCardInfo.setCardServiceRels(serviceDiscountList);
                }
            }
            if (goodsDiscountType == 2) {
                List<CardGoodsRel> goodsDiscountList = goodsDiscountListMap.get(memberCardInfo.getId());
                if (!CollectionUtils.isEmpty(goodsDiscountList)) {
                    memberCardInfo.setCardGoodsRels(goodsDiscountList);
                }
                goodsDiscountCardIds.add(memberCardInfo.getId());
            }
        }
    }

    @Override
    public String getTypeNameById(Long shopId, Long id) {
        Assert.notNull(shopId, "店铺id不能为空.");
        Assert.notNull(id, "会员卡类型id不能为空.");
        return memberCardInfoDao.getTypeNameById(shopId, id);
    }

    @Override
    public List<MemberCardInfo> list(Long shopId) {
        Assert.notNull(shopId);
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        return memberCardInfoDao.select(param);
    }

    @Override
    public Map<Long, String> getIdNameMap(Long shopId, List<Long> cardTypeIds) {
        if (Langs.isEmpty(cardTypeIds)) {
            return Collections.emptyMap();
        }
        List<MemberCardInfo> memberCardInfos = memberCardInfoDao.selectByIds(shopId, cardTypeIds);
        if (Langs.isEmpty(memberCardInfos)) {
            return Collections.emptyMap();
        }
        Map<Long, String> idNameMap = new HashMap<>(memberCardInfos.size());
        for (MemberCardInfo memberCardInfo : memberCardInfos) {
            idNameMap.put(memberCardInfo.getId(), memberCardInfo.getTypeName());
        }
        return idNameMap;
    }
}
