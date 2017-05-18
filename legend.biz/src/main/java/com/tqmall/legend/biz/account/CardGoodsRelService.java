package com.tqmall.legend.biz.account;

import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.entity.account.CardGoodsRel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanghui on 6/2/16.
 */
public interface CardGoodsRelService extends BaseService {
    List<CardGoodsRel> getAllCardGoodsRel();

    Page<CardGoodsRel> getPageCardGoodsRel(Pageable pageable, Map<String, Object> searchParams);

    CardGoodsRel getCardGoodsRelById(Long id);

    boolean saveCardGoodsRel(CardGoodsRel cardGoodsRel);

    boolean deleteCardGoodsRelById(Long id);

    int deleteCardGoodsRelByIds(Long[] ids);

    int batchSave(List<CardGoodsRel> cardGoodsRels);

    /**
     * 根据会员卡id获取与之关联的物料信息
     * @param shopId
     * @param cardInfoId
     * @return
     */
    List<CardGoodsRel> selectCardGoodsByCardInfoId(Long shopId, Long cardInfoId);

    void deleteByCardInfoId(Long shopId, Long cardInfoId);

    List<CardGoodsRel> selectByCardInfoIds(Long shopId, Set<Long> cardInfoIds);

    List<CardGoodsRel> selectCardGoodsByCatId(Long shopId, Long catId);
}
