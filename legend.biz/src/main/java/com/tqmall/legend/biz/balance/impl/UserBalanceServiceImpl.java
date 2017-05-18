package com.tqmall.legend.biz.balance.impl;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.biz.activity.MActivityService;
import com.tqmall.legend.biz.balance.UserBalanceService;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.balance.UserBalanceDao;
import com.tqmall.legend.entity.balance.UserBalance;
import com.tqmall.legend.entity.lottery.Activity;
import com.tqmall.legend.pojo.balance.UserBalanceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@Service
@Slf4j
public class UserBalanceServiceImpl extends BaseServiceImpl implements UserBalanceService {

    @Autowired
    private UserBalanceDao userBalanceDao;

    @Autowired
    private MActivityService activityService;


    @Override
    public Result getUserBalance(Long userId, Long shopId) {
        Map<String, Object> userBalanceParam = new HashMap<>();
        userBalanceParam.put("userId", userId);
        userBalanceParam.put("shopId", shopId);
        List<UserBalance> userBalanceList = new ArrayList<>();
        try {
            userBalanceList = userBalanceDao.select(userBalanceParam);
        } catch (Exception e) {
            log.error("[用户余额] 获取用户余额 DB error. e={}", e);
            return Result.wrapErrorResult("", "获取用户余额失败");
        }
        log.info("[用户余额] 数据库获取结果.userId={},shopId={},userBalanceList={}", userId, shopId, userBalanceList);

        UserBalanceVO userBalanceVO = new UserBalanceVO();
        if (!CollectionUtils.isEmpty(userBalanceList)) {
            UserBalance userBalance = userBalanceList.get(0);
            userBalanceVO.setBalance(userBalance.getBalance());
            userBalanceVO.setWithdrawStatus(false);
        } else {
            userBalanceVO.setBalance(BigDecimal.ZERO);
            userBalanceVO.setWithdrawStatus(false);
        }

        Activity activity = activityService.getActivityWithDrawTime();
        if (null != activity) {
            if (null != activity.getWithdrawEndTime()) {
                StringBuffer stringBuffer = new StringBuffer("");
                stringBuffer.append("提现开启(");
                stringBuffer.append(DateUtil.convertDateToStr(activity.getWithdrawStartTime(), "MM月dd日"));
                stringBuffer.append("到");
                stringBuffer.append(DateUtil.convertDateToStr(activity.getWithdrawEndTime(), "MM月dd日"));
                stringBuffer.append(")");
                userBalanceVO.setNoticeStr(stringBuffer.toString());
                Date currentDate = new Date();
                if (currentDate.after(activity.getWithdrawStartTime()) && currentDate.before(activity.getWithdrawEndTime())) {
                    userBalanceVO.setWithdrawStatus(true);
                }
            } else {
                userBalanceVO.setNoticeStr("提现尚未开启");
            }
        } else {
            userBalanceVO.setNoticeStr("提现尚未开启");
        }

        return Result.wrapSuccessfulResult(userBalanceVO);
    }

    @Override
    public List<UserBalance> getUserBalanceOnly(Long userId, Long shopId) {
        Map<String, Object> userBalanceParam = new HashMap<>();
        userBalanceParam.put("userId", userId);
        userBalanceParam.put("shopId", shopId);
        userBalanceParam.put("isDeleted","N");
        List<UserBalance> userBalanceList = new ArrayList<>();
        userBalanceList = userBalanceDao.select(userBalanceParam);
        log.info("[用户余额] 数据库获取结果.userId={},shopId={},userBalanceList={}", userId, shopId, userBalanceList);
        return userBalanceList;

    }
}
