package com.tqmall.legend.facade.sms.newsms.processor.presend;

import com.tqmall.legend.facade.sms.newsms.processor.TemplateData;
import com.tqmall.legend.facade.sms.newsms.util.Pager;

import java.util.List;

/**
 * Created by majian on 16/11/24.
 */
public class PreSendContext {
    private Long shopId;
    private String template;
    private int pageIndex;
    private int needNumber = 0;
    private String redisKey;
    private List<TemplateData> templateDataList;
    private Integer position;

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getShopId() {
        return shopId;
    }

    public String getTemplate() {
        return template;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getNeedNumber() {
        return needNumber;
    }

    public void setNeedNumber(int needNumber) {
        this.needNumber = needNumber;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public void addNeedNumber(int number) {
        needNumber += number;
    }

    public List<TemplateData> getTemplateDataList(int fromIndex, int endIndex) {
        return templateDataList.subList(fromIndex, endIndex);
    }

    public void setTemplateDataList(List<TemplateData> templateDataList) {
        this.templateDataList = templateDataList;
    }

    public List<TemplateData> getPagedTemplateDataList() {
        Pager pager = new Pager(1000, templateDataList.size());
        int startIndex = pager.getStartIndex(pageIndex);
        int endIndex = pager.getEndIndex(pageIndex);
        return getTemplateDataList(startIndex, endIndex);
    }

    public int getPageNumber() {
        return new Pager(1000, templateDataList.size()).getPages();
    }

    public Integer getMobileNumber() {
        return templateDataList.size();
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

}
