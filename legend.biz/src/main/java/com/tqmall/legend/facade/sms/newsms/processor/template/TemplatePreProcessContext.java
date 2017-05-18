package com.tqmall.legend.facade.sms.newsms.processor.template;

/**
 * Created by majian on 16/11/30.
 */
public class TemplatePreProcessContext {
    private Long shopId;
    private String template;

    public TemplatePreProcessContext(Long shopId, String template) {
        this.shopId = shopId;
        this.template = template;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void inflateTemplate(String placeHolderName, String value) {
        String regex = "\\$\\{" + placeHolderName + "}";
        template = template.replaceAll(regex, value);
    }
}
