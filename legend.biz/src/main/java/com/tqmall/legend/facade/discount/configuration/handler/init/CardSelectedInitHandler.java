package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.tqmall.common.exception.BizException;
import com.tqmall.legend.facade.discount.bo.AccountCardDiscountBo;
import com.tqmall.legend.facade.discount.bo.AccountDiscountBo;
import com.tqmall.legend.facade.discount.bo.DiscountContext;
import com.tqmall.legend.facade.discount.bo.SelectedCardBo;
import com.tqmall.legend.facade.discount.configuration.handler.InitHandler;
import com.tqmall.wheel.lang.Objects;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.tqmall.wheel.lang.Langs.isNotEmpty;
import static com.tqmall.wheel.lang.Langs.isNotNull;

/**
 * @Author 辉辉大侠
 * @Date:2:13 PM 06/03/2017
 */
@Slf4j
public class CardSelectedInitHandler implements InitHandler {

    @Override
    public void init(DiscountContext cxt) {
        SelectedCardBo selectedCard = cxt.getSelected().getSelectedCard();
        if (isNotNull(selectedCard)) {
            boolean checkedCard = false;
            if (isNotNull(cxt.getAccountDiscount())) {
                checkedCard = isSelected(cxt.getAccountDiscount().getCardDiscountList(), selectedCard.getCardId());
            }
            if (!checkedCard) {
                if (isNotEmpty(cxt.getBindAccountDiscountList())) {
                    for (AccountDiscountBo account : cxt.getBindAccountDiscountList()) {
                        checkedCard = isSelected(account.getCardDiscountList(), selectedCard.getCardId());
                        if (checkedCard) {
                            break;
                        }
                    }
                }
            }

            if (!checkedCard) {
                if (isNotNull(cxt.getGuestAccountDiscount())) {
                    checkedCard = isSelected(cxt.getGuestAccountDiscount().getCardDiscountList(), selectedCard.getCardId());
                }
            }

            if (!checkedCard) {
                log.error("从账户列表中查找不到被选中的会员卡id[{}],cxt:{}", selectedCard.getCardId(), Objects.toJSON(cxt));
                throw new BizException("选中的会员卡在账户列表中查找不到.");
            }
        }
    }

    private boolean isSelected(List<AccountCardDiscountBo> cardList, Long selectedCardId) {
        if (isNotEmpty(cardList)) {
            for (AccountCardDiscountBo card : cardList) {
                if (card.getCardId().equals(selectedCardId)) {
                    card.setSelected(true);
                    return true;
                }
            }
        }
        return false;
    }
}
