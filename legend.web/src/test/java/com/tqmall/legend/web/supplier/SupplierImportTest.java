package com.tqmall.legend.web.supplier;

import com.tqmall.search.common.data.FieldsSort;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.legend.orderhistory.param.LegendOrderHistoryParam;
import com.tqmall.search.dubbo.client.legend.orderhistory.result.LegendOrderHistoryVO;
import com.tqmall.search.dubbo.client.legend.orderhistory.service.LegendOrderHistoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by twg on 16/12/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationCxt-dubbo.xml")
public class SupplierImportTest {

    @Autowired
    private LegendOrderHistoryService legendOrderHistoryService;
    @Test
    public void test_gongdan(){
        LegendOrderHistoryParam param = new LegendOrderHistoryParam();
        param.setShopId(31);
        param.setStartTime("2016-11-01 00:00:00");
        param.setEndTime("2017-03-02 00:00:00");
        //封装page数据
        int pageNum = 1;
        int pageSize = 20;
        Sort sort = new Sort("id DESC");
        FieldsSort fieldsSort = new FieldsSort(sort);
        PageableRequest pageRequest = new PageableRequest(pageNum, pageSize, fieldsSort);
        com.tqmall.search.common.result.Result<Page<LegendOrderHistoryVO>> searchResult = legendOrderHistoryService.getOrderHistories(param,pageRequest);
    }
}
