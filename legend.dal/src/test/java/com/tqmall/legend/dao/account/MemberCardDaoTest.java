package com.tqmall.legend.dao.account;

import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.entity.account.MemberCard;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by tanghao on 17/2/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-dal-context.xml")
public class MemberCardDaoTest {
    @Autowired
    MemberCardDao memberCardDao;

    @Test
    public void testFindMaxMemberCardId(){
        MemberCard memberCard = memberCardDao.findMaxMemberCardId(1L);
        System.out.println("-------testFindMaxMemberCardId------------:"+LogUtils.objectToString(memberCard));
    }

    @Test
    public void testGetMemberCardListByAccountId(){
        List<MemberCard> memberCards = memberCardDao.getMemberCardListByAccountId(255782L);
        System.out.println("---------testGetMemberCardListByAccountId-----------:"+LogUtils.objectToString(memberCards));
    }
}
