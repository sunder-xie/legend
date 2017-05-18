package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.facade.discount.bo.AccountComboDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.DiscountServiceBo;
import com.tqmall.legend.facade.discount.bo.SelectedComboBo;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import com.tqmall.wheel.lang.Objects;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;

/**
 * @Author 辉辉大侠
 * @Date:2:46 PM 06/03/2017
 */
@Slf4j
public class ComboSelectedInitHandler implements InitHandler {

    @Override
    public void init(DiscountContext cxt) {
        List<SelectedComboBo> selectedCombos = cxt.getSelected().getSelectedComboList();
        if (isNotEmpty(selectedCombos)) {
            /**
             * key --> 计次卡服务id
             * value --> 服务使用次数
             */
            Map<Long, Integer> comboServiceIdMap = Maps.newHashMap();
            for (SelectedComboBo combo : selectedCombos) {
                comboServiceIdMap.put(combo.getComboServiceId(), combo.getCount());
            }

            List<AccountComboDiscountBo> comboServiceList = cxt.getAllComboServiceList();
            if (isNotEmpty(comboServiceList)) {
                for (AccountComboDiscountBo accountCombo : comboServiceList) {
                    if (comboServiceIdMap.containsKey(accountCombo.getComboServiceId())) {
                        Integer useCount = comboServiceIdMap.get(accountCombo.getComboServiceId());
                        if (accountCombo.isAvailable() && accountCombo.getCount() >= useCount) {
                            accountCombo.setSelected(true);
                            accountCombo.setUseCount(useCount);
                            comboServiceIdMap.put(accountCombo.getComboServiceId(), 0);
                        } else {
                            log.error("计次卡[{}]被错误选中了{}次", accountCombo.getComboServiceId(), useCount);
                            throw new BizException("计次卡不可用或选中次数超过可用次数.");
                        }
                    }
                }
            }

            for (Map.Entry<Long, Integer> entrySet : comboServiceIdMap.entrySet()) {
                if (!Integer.valueOf(0).equals(entrySet.getValue())) {
                    log.error("已选中计次卡[{}]在账户列表中未能找到.cxt:{}", entrySet.getKey()
                            , Objects.toJSON(cxt));
                    throw new BizException("已选中的计次卡在账户列表中未找到.");
                }
            }

            ImmutableMap<Long, AccountComboDiscountBo> comboServiceMap = Maps.uniqueIndex(cxt.getAllComboServiceList(), new Function<AccountComboDiscountBo, Long>() {
                @Override
                public Long apply(AccountComboDiscountBo input) {
                    return input.getComboServiceId();
                }
            });
            Map<Long, Integer> orderServiceCntMap = Maps.newHashMap();
            for (DiscountServiceBo discountServiceBo : cxt.getDiscountServiceList()) {
                if (!orderServiceCntMap.containsKey(discountServiceBo.getServiceId())) {
                    orderServiceCntMap.put(discountServiceBo.getServiceId(), 1);
                } else {
                    orderServiceCntMap.put(discountServiceBo.getServiceId(), orderServiceCntMap.get(discountServiceBo.getServiceId()) + 1);
                }
            }

            for (SelectedComboBo combo : selectedCombos) {
                AccountComboDiscountBo accountCombo = comboServiceMap.get(combo.getComboServiceId());
                for (int k = 0; k < combo.getCount(); k++) {
                    if (orderServiceCntMap.containsKey(accountCombo.getServiceId())) {
                        int cnt = orderServiceCntMap.get(accountCombo.getServiceId()) - 1;
                        if (cnt < 0) {
                            log.error("计次卡[comboServiceId:{}]设置次数[{}]大于工单服务数", accountCombo.getComboServiceId(), combo.getCount());
                            throw new BizException("计次卡选中次数超过工单服务数.");
                        } else {
                            orderServiceCntMap.put(accountCombo.getServiceId(), cnt);
                        }
                    } else {
                        log.error("已选中计次卡无找到对应工单服务,comboServiceId:[]", accountCombo.getComboServiceId());
                        throw new BizException("被选中计次卡无对应服务.");
                    }
                }
            }
            for (AccountComboDiscountBo accountComboDiscountBo : comboServiceList) {
                if (!accountComboDiscountBo.isSelected() && accountComboDiscountBo.isAvailable()) {
                    if (orderServiceCntMap.containsKey(accountComboDiscountBo.getServiceId())
                            && orderServiceCntMap.get(accountComboDiscountBo.getServiceId()) == 0) {
                        accountComboDiscountBo.setAvailable(false);
                        accountComboDiscountBo.setMessage("工单中服务已被抵扣，该计次卡不可用。");
                    }
                }
            }

        }
    }
}
