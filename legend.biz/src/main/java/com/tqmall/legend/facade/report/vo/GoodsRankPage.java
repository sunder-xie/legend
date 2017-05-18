package com.tqmall.legend.facade.report.vo;

import java.util.List;

/**
 * Created by majian on 16/9/25.
 */
public class GoodsRankPage {

    private Integer totalCount;
    private List<GoodsSaleRankVo> content;

    public GoodsRankPage() {
    }

    public GoodsRankPage(Integer totalCount, List<GoodsSaleRankVo> content) {
        this.totalCount = totalCount;
        this.content = content;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<GoodsSaleRankVo> getContent() {
        return content;
    }

    public void setContent(List<GoodsSaleRankVo> content) {
        this.content = content;
    }
}
