package com.tqmall.legend.facade.discount.configuration.handler.init;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Sets;
import com.tqmall.legend.facade.discount.bo.AccountComboDiscountBo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by 辉辉大侠 on 06/03/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ComboServiceLimitHandlerTest {

    @Test
    public void testDoLimit() throws Exception {
        List<AccountComboDiscountBo> comboDiscountList = Lists.newArrayList();
        AccountComboDiscountBo c = new AccountComboDiscountBo();
        c.setServiceId(1L);
        c.setAvailable(true);
        comboDiscountList.add(c);

        c = new AccountComboDiscountBo();
        c.setServiceId(2L);
        c.setAvailable(true);
        comboDiscountList.add(c);

        c = new AccountComboDiscountBo();
        c.setServiceId(3L);
        comboDiscountList.add(c);

        ComboServiceLimitHandler handler = new ComboServiceLimitHandler();
        Set<Long> serviceIds = Sets.newHashSet();
        serviceIds.add(1L);
        handler.doLimit(serviceIds, comboDiscountList);
        assertTrue(comboDiscountList.get(0).isAvailable());
        assertFalse(comboDiscountList.get(1).isAvailable());
        assertFalse(comboDiscountList.get(2).isAvailable());

    }

}