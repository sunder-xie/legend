package com.tqmall.legend.biz.goods;

import com.tqmall.common.util.log.LogUtils;
import com.tqmall.legend.biz.base.BizJunitBase;
import junit.runner.BaseTestRunner;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by sven on 2017/2/23.
 */
@Slf4j
public class GoodsServiceTest extends BizJunitBase {
    @Resource
    private GoodsService goodsService;

    @Test
    public void getGoodsLocationTest() {
        long start = System.currentTimeMillis();
        List<String> list = goodsService.getGoodsLocation(1L);
        log.info("耗时1" + (System.currentTimeMillis() - start));
        log.info(LogUtils.objectToString(list));
    }

}
