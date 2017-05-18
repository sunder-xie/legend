package com.tqmall.legend.server.news;

import com.tqmall.common.exception.BizException;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.result.base.PageEntityDTO;
import com.tqmall.legend.object.result.news.LegendNewsDTO;
import com.tqmall.legend.server.BaseCaseTest;
import com.tqmall.legend.service.news.RpcLegendNewsService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 17/4/18.
 */
public class RpcLegendNewsServiceTest extends BaseCaseTest {

    @Autowired
    private RpcLegendNewsService rpcLegendNewsService;

    /**
     * 资讯列表原始返回数据
     */
    @Test
    public void getNewsListTest_01() {
        Long newsTypeId = 1L;
        Result<PageEntityDTO<LegendNewsDTO>> result = rpcLegendNewsService.getNewsList(newsTypeId);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isSuccess());
        PageEntityDTO<LegendNewsDTO> pageEntityDTO = result.getData();
        List<LegendNewsDTO> newsDTOList = pageEntityDTO.getContent();
        for (LegendNewsDTO newsDTO : newsDTOList) {
            Assert.assertNotNull(newsDTO.getId());
            Assert.assertNotNull(newsDTO.getGmtCreate());
            Assert.assertNotNull(newsDTO.getNewsTitle());

        }
    }

    /**
     * 校验资讯详情数据
     */
    @Test
    public void getNewsDetailTest_01(){
        Long id = 50L;
        Result<LegendNewsDTO> result = rpcLegendNewsService.getNewsDetail(id);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isSuccess());
        LegendNewsDTO newsDTO = result.getData();
        Assert.assertEquals(id, newsDTO.getId());
        Assert.assertNotNull(newsDTO.getNewsContent());
        if (newsDTO.getShowStyle() != null && newsDTO.getShowStyle().equals(1)) {
            Assert.assertNotNull(newsDTO.getImgThumbUrl());
        }
    }
    /**
     * 校验错误参数id
     */
    @Test
    public void getNewsDetailTest_02(){
        Long id = 0L;
        Result<LegendNewsDTO> result = rpcLegendNewsService.getNewsDetail(id);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isSuccess());
    }

    /**
     * 校验资讯app浏览量+1是否放入缓存成功
     */
    @Test
    public void updateNewsAppPvTest_01(){
        Long id = 50L;
        Result result = rpcLegendNewsService.updateNewsAppPv(id);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isSuccess());
    }

    /**
     * 校验资讯点赞数是否+1
     */
    @Test
    public void uptNewsPraiseTest_01() {
        Long id = 50L;
        Integer type = 1;
        String sql = "select * from legend_news where is_deleted= 'N' and id=" + id;
        List<Map<String, Object>> dataList = queryData(sql);
        Map<String, Object> data = dataList.get(0);
        Long praiseCount = (Long)data.get("app_upvote_count");
        Result<Long> result = rpcLegendNewsService.uptNewsPraise(id, type);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(++praiseCount, result.getData());

    }

    /**
     * 校验资讯点赞数是否-1
     */
    @Test
    public void uptNewsPraiseTest_02() {
        Long id = 50L;
        Integer type = 0;
        String sql = "select * from legend_news where is_deleted= 'N' and id=" + id;
        List<Map<String, Object>> dataList = queryData(sql);
        Map<String, Object> data = dataList.get(0);
        Long praiseCount = (Long)data.get("app_upvote_count");
        Result<Long> result = rpcLegendNewsService.uptNewsPraise(id, type);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(--praiseCount, result.getData());

    }
}
