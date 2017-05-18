package com.tqmall.legend.biz.account.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.CardGoodsRelService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.account.CardGoodsRelDao;
import com.tqmall.legend.entity.account.CardGoodsRel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Created by wanghui on 6/2/16.
 */
@Service
public class CardGoodsRelServiceImpl extends BaseServiceImpl implements CardGoodsRelService {
    @Autowired
    private CardGoodsRelDao cardGoodsRelDao;

    public List<CardGoodsRel> getAllCardGoodsRel() {
        return super.getAll(cardGoodsRelDao);
    }

    public Page<CardGoodsRel> getPageCardGoodsRel(Pageable pageable, Map<String, Object> searchParams) {
        return super.getPage(cardGoodsRelDao, pageable, searchParams);
    }

    public CardGoodsRel getCardGoodsRelById(Long id) {
        return super.getById(cardGoodsRelDao, id);
    }

    public boolean saveCardGoodsRel(CardGoodsRel cardGoodsRel) {
        return super.save(cardGoodsRelDao, cardGoodsRel);
    }

    public boolean deleteCardGoodsRelById(Long id) {
        return super.deleteById(cardGoodsRelDao, id);
    }

    public int deleteCardGoodsRelByIds(Long[] ids) {
        return super.deleteByIds(cardGoodsRelDao, ids);
    }

    @Override
    public int batchSave(List<CardGoodsRel> cardGoodsRels) {
        return cardGoodsRelDao.batchInsert(cardGoodsRels);
    }

    @Override
    public List<CardGoodsRel> selectCardGoodsByCardInfoId(Long shopId, Long cardInfoId) {
        Assert.notNull(shopId);
        Assert.notNull(cardInfoId);
        /**
         * 组装查询参数
         */
        HashMap<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("shopId", shopId);
        paramMap.put("cardInfoId", cardInfoId);
        return this.cardGoodsRelDao.select(paramMap);
    }

    @Override
    public void deleteByCardInfoId(Long shopId, Long cardInfoId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("cardInfoId", cardInfoId);
        cardGoodsRelDao.delete(param);
    }

    @Override
    public List<CardGoodsRel> selectByCardInfoIds(Long shopId, Set<Long> cardInfoIds) {
        if(shopId==null || CollectionUtils.isEmpty(cardInfoIds)){
            return new ArrayList<>();
        }
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("cardInfoIds", cardInfoIds);
        return cardGoodsRelDao.select(param);
    }

    @Override
    public List<CardGoodsRel> selectCardGoodsByCatId(Long shopId, Long catId) {
        Map param = Maps.newHashMap();
        param.put("shopId", shopId);
        param.put("goodsCatId", catId);
        return cardGoodsRelDao.select(param);
    }
}
