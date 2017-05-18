package com.tqmall.legend.facade.report.vo;

import java.util.List;

/**
 * Created by majian on 16/9/23.
 */
public class GoodsCatRankPage {
    private Integer totalCount;
    private List<GoodsCatSaleRankVo> content;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<GoodsCatSaleRankVo> getContent() {
        return content;
    }

    public void setContent(List<GoodsCatSaleRankVo> content) {
        this.content = content;
    }

    public GoodsCatRankPage(Integer totalCount, List<GoodsCatSaleRankVo> content) {
        this.totalCount = totalCount;
        this.content = content;
    }

    public GoodsCatRankPage() {
    }
}
