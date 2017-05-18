package com.tqmall.legend.dao.account;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tqmall.legend.dao.account.param.MemberExportPagedParam;
import com.tqmall.legend.dao.account.param.MemberExportParam;
import com.tqmall.legend.entity.account.MemberExportEntity;

/**
 * Created by majian on 17/1/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-dal-context.xml")
public class AccountExportDaoTest {
    @Autowired
    private AccountExportDao dao;

    @Test
    public void countMember() throws Exception {

    }

    @Test
    public void pageMember() throws Exception {
        Sort balanceOrder = new Sort(Sort.Direction.DESC, "balance");
        PageRequest pageRequest = new PageRequest(1, 10, balanceOrder);
        MemberExportParam param = new MemberExportParam();
        param.setShopId(1l);
        param.setMobile("13258899988");
        param.setCardInfoId(20246l);
        param.setCardNum("666");
        param.setCustomerName("maj");
        MemberExportPagedParam pagedParam = new MemberExportPagedParam(pageRequest,param);
        List<MemberExportEntity> resultSet = dao.pageMember(pagedParam);
        System.out.println("query success");
    }

    @Test
    public void countCombo() throws Exception {

    }

    @Test
    public void pageCombo() throws Exception {

    }
}