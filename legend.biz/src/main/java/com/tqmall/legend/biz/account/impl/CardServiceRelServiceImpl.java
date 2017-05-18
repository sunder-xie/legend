package com.tqmall.legend.biz.account.impl;

import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.CardServiceRelService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.dao.account.CardServiceRelDao;
import com.tqmall.legend.entity.account.CardServiceRel;
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
public class CardServiceRelServiceImpl extends BaseServiceImpl implements CardServiceRelService {
    @Autowired
    private CardServiceRelDao cardServiceRelDao;

    public List<CardServiceRel> getAllCardServiceRel() {
        return super.getAll(cardServiceRelDao);
    }

    public Page<CardServiceRel> getPageCardServiceRel(Pageable pageable, Map<String, Object> searchParams) {
        return super.getPage(cardServiceRelDao, pageable, searchParams);
    }

    public CardServiceRel getCardServiceRelById(Long id) {
        return super.getById(cardServiceRelDao, id);
    }

    public boolean saveCardServiceRel(CardServiceRel cardServiceRel) {
        return super.save(cardServiceRelDao, cardServiceRel);
    }

    public boolean deleteCardServiceRelById(Long id) {
        return super.deleteById(cardServiceRelDao, id);
    }

    public int deleteCardServiceRelByIds(Long[] ids) {
        return super.deleteByIds(cardServiceRelDao, ids);
    }

    @Override
    public int batchSave(List<CardServiceRel> cardServiceRelList) {
        return cardServiceRelDao.batchInsert(cardServiceRelList);
    }

    @Override
    public List<CardServiceRel> listByCardInfoId(Long shopId, Long cardTypeId) {
        Assert.notNull(shopId);
        Assert.notNull(cardTypeId);

        /**
         * 组装查询参数
         */
        HashMap<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("shopId", shopId);
        paramMap.put("cardInfoId", cardTypeId);
        return this.cardServiceRelDao.select(paramMap);
    }

    @Override
    public void deleteByCardInfoId(Long shopId, Long cardInfoId) {
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("cardInfoId", cardInfoId);
        cardServiceRelDao.delete(param);
    }

    @Override
    public List<CardServiceRel> selectByCardInfoIds(Long shopId, Set<Long> cardInfoIds) {
        if(shopId==null || CollectionUtils.isEmpty(cardInfoIds)){
            return new ArrayList<>();
        }
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("cardInfoIds", cardInfoIds);
        return cardServiceRelDao.select(param);
    }

    @Override
    public List<CardServiceRel> selectByServiceCateId(Long serviceCateId) {
        if (serviceCateId == null) {
            return new ArrayList<>();
        }
        Map param = Maps.newHashMap();
        param.put("serviceCatId", serviceCateId);
        return cardServiceRelDao.select(param);
    }
}
