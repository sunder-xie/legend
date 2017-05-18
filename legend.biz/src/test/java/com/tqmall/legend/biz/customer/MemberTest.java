package com.tqmall.legend.biz.customer;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.tqmall.legend.biz.account.MemberCardInfoService;
import com.tqmall.legend.biz.account.impl.MemberCardServiceImpl;
import com.tqmall.legend.dao.account.MemberCardDao;
import com.tqmall.legend.entity.account.MemberCard;
import com.tqmall.legend.entity.account.MemberCardInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * Created by tanghao on 17/2/28.
 */
@RunWith(MockitoJUnitRunner.class)
public class MemberTest {
    Long shopId = 1L;
    Long accountId = 1L;
    Long cardTypeId = 1L;

    @Mock
    MemberCardDao memberCardDao;
    @Mock
    MemberCardInfoService memberCardInfoService;
    @InjectMocks
    MemberCardServiceImpl memberCardService;
    @Test
    public void testGrantMemberCardAble(){
        /**
         * 当已办理未过期的会员卡为空时
         */
        testDotHaveMemberCard();
        /**
         * 当办理3张已过期会员卡时
         */
        testHaveThreeMemberCard(3,2009,true,cardTypeId);
        /**
         * 当办理三张未过期会员卡时
         */
        testHaveThreeMemberCard(3,2017,false,cardTypeId);
        /**
         * 办理已拥有的未过期会员卡时
         */
        testHaveThreeMemberCard(2,2017,false,cardTypeId);

    }

    private void testDotHaveMemberCard(){
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountId",accountId);
        when(memberCardDao.select(param)).thenReturn(Lists.<MemberCard>newArrayList());
        List<Long> ids = Lists.newArrayList();
        when(memberCardInfoService.selectInfoByIds(shopId,ids)).thenReturn(Lists.<MemberCardInfo>newArrayList());
        memberCardService = new MemberCardServiceImpl();
        ReflectionTestUtils.setField(memberCardService, "memberCardDao", memberCardDao);
        ReflectionTestUtils.setField(memberCardService, "memberCardInfoService", memberCardInfoService);
        boolean flag = memberCardService.grantMemberCardAble(shopId,accountId,cardTypeId);
        /**
         * 可以办理会员卡
         */
        Assert.assertTrue(flag);
    }

    /**
     * 测试不同条件下是否可以办理会员卡
     * @param n 会员卡张数
     * @param year 过期年份
     * @param isTrue 期待返回结果
     * @param cardTypeId 会员卡类型id
     */
    private void testHaveThreeMemberCard(int n,int year, boolean isTrue,Long cardTypeId){
        Map param = Maps.newHashMap();
        param.put("shopId",shopId);
        param.put("accountId",accountId);
        /**
         * 构建三张过期会员卡
         */
        List<MemberCard> memberCards = Lists.newArrayList();
        for(int i = 1; i <= n; i++) {
            MemberCard card = new MemberCard();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date  date = sdf.parse(year + "-11-0"+i);
                card.setCardTypeId(Long.valueOf(i));
                card.setExpireDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            memberCards.add(card);
        }

        when(memberCardDao.select(param)).thenReturn(memberCards);
        List<Long> ids = Lists.newArrayList();
        for (int i = 1; i<=n; i++) {
            ids.add(Long.valueOf(i));
        }
        /**
         * 构建会员卡类型列表
         */
        List<MemberCardInfo> memberCardInfoList = Lists.newArrayList();
        for (int i = 1; i <= n; i++) {
            MemberCardInfo memberCardInfo = new MemberCardInfo();
            memberCardInfo.setId(Long.valueOf(i));
            memberCardInfo.setTypeName("会员卡"+i);
            memberCardInfoList.add(memberCardInfo);
        }
        when(memberCardInfoService.selectInfoByIds(shopId,ids)).thenReturn(memberCardInfoList);
        memberCardService = new MemberCardServiceImpl();
        ReflectionTestUtils.setField(memberCardService, "memberCardDao", memberCardDao);
        ReflectionTestUtils.setField(memberCardService, "memberCardInfoService", memberCardInfoService);
        boolean flag = memberCardService.grantMemberCardAble(shopId,accountId,cardTypeId);
        /**
         * 可以办理会员卡
         */
        Assert.assertTrue(getFlag(flag,isTrue));
    }

    private boolean getFlag(boolean flag1,boolean flag2){
        if(flag2 && flag1){
            return true;
        }else if(!flag1 && !flag2){
            return true;
        }else {
            return false;
        }
    }
}
