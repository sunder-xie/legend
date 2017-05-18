package com.tqmall.legend.biz.account;

import com.tqmall.legend.biz.base.BaseService;
import com.tqmall.legend.entity.account.CardServiceRel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wanghui on 6/2/16.
 */
public interface CardServiceRelService extends BaseService{
    List<CardServiceRel> getAllCardServiceRel();

    Page<CardServiceRel> getPageCardServiceRel(Pageable pageable, Map<String, Object> searchParams);

    CardServiceRel getCardServiceRelById(Long id);

    boolean saveCardServiceRel(CardServiceRel cardServiceRel);

    boolean deleteCardServiceRelById(Long id);

    int deleteCardServiceRelByIds(Long[] ids);

    int batchSave(List<CardServiceRel> cardServiceRelList);

    /**
     * 根据门店id和会员类型获取会员卡关联的服务列表
     * @param shopId
     * @param cardTypeId
     * @return
     */
    List<CardServiceRel> listByCardInfoId(Long shopId, Long cardTypeId);

    void deleteByCardInfoId(Long shopId, Long cardInfoId);

    List<CardServiceRel> selectByCardInfoIds(Long shopId, Set<Long> cardInfoIds);

    List<CardServiceRel> selectByServiceCateId(Long serviceCateId);
}
