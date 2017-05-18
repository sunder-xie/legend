package com.tqmall.legend.biz.account.datafix;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.tqmall.legend.dao.account.AccountCardFlowDetailDao;
import com.tqmall.legend.dao.account.AccountTradeFlowDao;
import com.tqmall.legend.dao.account.MemberCardDao;
import com.tqmall.legend.entity.account.AccountCardFlowDetail;
import com.tqmall.legend.entity.account.AccountConsumeTypeEnum;
import com.tqmall.legend.entity.account.AccountTradTypeEnum;
import com.tqmall.legend.entity.account.AccountTradeFlow;
import com.tqmall.legend.entity.account.MemberCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by majian on 16/7/20.
 */
@Service
@Slf4j
public class MemberDetailFixImpl implements MemberDetailFix {

    @Autowired
    private AccountTradeFlowDao accountTradeFlowDao;
    @Autowired
    private AccountCardFlowDetailDao accountCardFlowDetailDao;
    @Autowired
    private MemberCardDao memberCardDao;

    @Override
    public void fix() {


        Integer fixingSize = getFixingSize();
        log.info("数据修复开始,共需修复{}条",fixingSize);
        //1. <accountID, flowID>
        Multimap<Long, Long> accountIdFlowIdMap = getAccountIdFlowIdMap();
        int accountIdFlowIdMapSize = accountIdFlowIdMap.size();
        log.info("accountIdFlowIdMap.size()={}",accountIdFlowIdMapSize);
        if (accountIdFlowIdMapSize == 0 ) {
            return;
        }
        //2. <accountID, cardID>
        Multimap<Long, Long> accountIdCardIdMap = getAccountIdCardIdMap();
        int accountIdCardIdMapSize = accountIdCardIdMap.size();
        log.info("accountIdCardIdMap.size()={}",accountIdCardIdMapSize);
        if (accountIdCardIdMapSize == 0) {
            return;
        }
        //3. <flowID,cardID>
        Map<Long, Long> flowIdCardIdMap = getFlowIdCardIdMap(accountIdFlowIdMap, accountIdCardIdMap);
        //4. update according to <flowID,cardID>
        int flowIdCardIdMapSize = flowIdCardIdMap.size();
        log.info("flowIdCardIdMap.size()={}",flowIdCardIdMapSize);
        if (flowIdCardIdMapSize == 0) {
            return;
        }
        Set<Map.Entry<Long, Long>> entries = flowIdCardIdMap.entrySet();
        int fixedSize = 0;
        for (Map.Entry<Long, Long> entry : entries) {
            Long flowId = entry.getKey();
            Long cardId = entry.getValue();
            int i = accountCardFlowDetailDao.updateCardNumberByFlowId(flowId, cardId);
            if (i > 0) {
                fixedSize++;
            }
        }
        log.info("数据修复结束,共修复{}条",fixedSize);


    }

    private Integer getFixingSize() {Map param = Maps.newHashMap();
//        param.put("card_id", 0);
        param.put("consumeType", AccountCardFlowDetail.ConsumeTypeEnum.HANDLE_CARD.getCode());
        return accountCardFlowDetailDao.selectCount(param);
    }

    private Map<Long, Long> getFlowIdCardIdMap(Multimap<Long, Long> accountIdFlowIdMap, Multimap<Long, Long> accountIdCardIdMap) {

        Map<Long, Long> flowIdCardIdMap = Maps.newHashMap();
        Set<Long> accountIds = accountIdFlowIdMap.keySet();
        for (Long accountId : accountIds) {
            List<Long> flowIds = (List<Long>) accountIdFlowIdMap.get(accountId);
            List<Long> cardIds = (List<Long>) accountIdCardIdMap.get(accountId);

            try {
                for (int i = 0; i < flowIds.size(); i++) {
                    flowIdCardIdMap.put(flowIds.get(i), cardIds.get(i));
                }

            } catch (IndexOutOfBoundsException e) {
                log.error("accountId={},flowIds={},cardIds=", accountId, flowIds, cardIds);
                e.printStackTrace();
            }

        }
        return flowIdCardIdMap;
    }

    private Multimap<Long, Long> getAccountIdCardIdMap() {
        Multimap<Long, Long> multiMap = ArrayListMultimap.create();
        int offset = 0;
        final int LIMIT = 2000;
        while (true) {
            List<MemberCard> memberCardList = memberCardDao.findAllSortedCard(offset,LIMIT);
            if (memberCardList != null) {
                for (MemberCard card : memberCardList) {
                    multiMap.put(card.getAccountId(), card.getId());
                }

            }
            offset += LIMIT;

            if (memberCardList == null || memberCardList.size() < LIMIT) {
                break;
            }

        }
        return multiMap;
    }

    private Multimap<Long, Long> getAccountIdFlowIdMap() {

        Multimap<Long, Long> multiMap = ArrayListMultimap.create();
        int offset = 0;
        final int LIMIT = 2000;

        Map param = Maps.newHashMap();
        param.put("tradeType", AccountTradTypeEnum.MEMBER_CARD.getCode());
        param.put("consumeType", AccountConsumeTypeEnum.HANDLE_CARD.getCode());
        param.put("limit", LIMIT);
        ArrayList<String> sorts = new ArrayList<>();
        sorts.add("account_id");
        sorts.add("gmt_create DESC");
        param.put("sorts", sorts);
        while (true) {
            param.put("offset", offset);

            List<AccountTradeFlow> tradeFlows = accountTradeFlowDao.select(param);
            if (tradeFlows != null) {
                for (AccountTradeFlow tradeFlow :tradeFlows) {
                    multiMap.put(tradeFlow.getAccountId(),tradeFlow.getId());
                }
            }

            offset += LIMIT;

            if (tradeFlows == null || tradeFlows.size() < LIMIT) {
                break;
            }

        }
        return multiMap;

    }


}
