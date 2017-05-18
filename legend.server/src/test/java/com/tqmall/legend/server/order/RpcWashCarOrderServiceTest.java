package com.tqmall.legend.server.order;

import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.order.WashCarOrderStatsDTO;
import com.tqmall.legend.server.BaseCaseTest;
import com.tqmall.legend.service.order.RpcWashCarOrderService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 17/4/28.
 */
public class RpcWashCarOrderServiceTest extends BaseCaseTest {
    @Autowired
    private RpcWashCarOrderService rpcWashCarOrderService;

    @Test
    public void getShopWashCarTodayStatsNoShopIdTest() throws Exception {
        Result<WashCarOrderStatsDTO> statsDTOResult = rpcWashCarOrderService.getShopWashCarTodayStats(null);
        Assert.assertFalse(statsDTOResult.isSuccess());
        Assert.assertEquals(statsDTOResult.getMessage(), "店铺信息错误");
    }

    @Test
    public void getShopWashCarTodayStatsTest() throws Exception {
        long shopId = 1;
        Result<WashCarOrderStatsDTO> statsDTOResult = rpcWashCarOrderService.getShopWashCarTodayStats(shopId);
        Assert.assertTrue(statsDTOResult.isSuccess());
        Date now = new Date();
        String startTime = DateUtil.convertDateToYMD(now) + " 00:00:00";
        String endTime = DateUtil.convertDateToYMD(now) + " 23:59:59";

        String querySql = "SELECT count(1) as statsCount FROM legend_order_info WHERE order_tag = 2 AND shop_id = " + shopId + " AND is_deleted = 'N' AND create_time >= '" + startTime + "' AND create_time <= '" + endTime + "'";
        List<Map<String, Object>> statsCountList = queryData(querySql);
        Map<String, Object> statsCountMap = statsCountList.get(0);

        String statsAmountSql = "SELECT SUM(a.pay_amount) as statsAmount\n" +
                "FROM legend_debit_bill_flow a\n" +
                "inner join legend_debit_bill b\n" +
                "on a.bill_id = b.id\n" +
                "inner join legend_order_info c\n" +
                "on c.id = b.rel_id\n" +
                "and a.is_deleted='N'\n" +
                "and b.is_deleted='N'\n" +
                "and c.is_deleted='N'\n" +
                "and c.order_tag = 2\n" +
                "and c.shop_id = " + shopId + "\n" +
                "and c.create_time >= '" + startTime + "' AND c.create_time <= '" + endTime + "'\n" +
                "and b.valid_status = 1\n" +
                "and a.valid_status = 1\n" +
                "and a.payment_id != 0\n" +
                "and a.payment_name != '会员卡'";
        List<Map<String, Object>> statsAmountList = queryData(statsAmountSql);
        Map<String, Object> statsAmountMap = statsAmountList.get(0);
        Assert.assertEquals(statsDTOResult.getData().getStatsCount().toString(), statsCountMap.get("statsCount").toString());
        Assert.assertEquals(statsDTOResult.getData().getStatsAmount(), statsAmountMap.get("statsAmount").toString());
    }
}
