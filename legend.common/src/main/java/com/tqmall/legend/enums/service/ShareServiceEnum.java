package com.tqmall.legend.enums.service;

import java.math.BigDecimal;

/**
 * Created by zsy on 2015/7/16.
 * 共享体系的服务初始化
 */
public enum ShareServiceEnum {
    /**
     * 补漆服务13个，共6个价格 共78个服务
     */
    BQ_ZQM("左前门", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_ZHM("左后门", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_YHM("右后门", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_YQM("右前门", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_ZQYZB("左前叶子板", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_YHYZB("右后叶子板", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_YQYZB("右前叶子板", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_ZHYZB("左后叶子板", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_QG("前杠", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_HG("后杠", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_QJG("前机盖", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_HCG("后仓盖", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    BQ_CD("车顶", "补漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    /**
     * 喷漆服务26个，共6个价格 共156个服务
     */
    PQ_ZQM("左前门", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_ZHM("左后门", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_YHM("右后门", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_YQM("右前门", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_ZQYZB("左前叶子板", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_YHYZB("右后叶子板", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_YQYZB("右前叶子板", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_ZHYZB("左后叶子板", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_QG("前杠", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_HG("后杠", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_QJG("前机盖", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("1.5")),
    PQ_HCG("后仓盖", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, BigDecimal.ONE),
    PQ_CD("车顶", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("1.5")),
    PQ_AZ("A柱", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    PQ_BZ("B柱", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    PQ_CZ("C柱", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    PQ_ZHSJ("左后视镜", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.3")),
    PQ_YHSJ("右后视镜", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.3")),
    PQ_ZDB("左大边", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    PQ_YDB("右大边", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    PQ_SMFCT("四门防擦条/每根", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.2")),
    PQ_FDJCN("发动机舱内", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    PQ_HBXCN("后备箱舱内", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    PQ_LW("轮辋/每个", "喷漆", new String[]{"240", "200", "180", "160", "150", "140"}, new BigDecimal("0.5")),
    PQ_QCGS("全车改色喷漆", "喷漆", new String[]{"150"}, new BigDecimal("13")),
    PQ_QCWG("全车外观喷漆", "喷漆", new String[]{"150"}, new BigDecimal("13")),
    /**
     * 钣金服务20个，一个价格，共20个服务
     */
    BJ_ZQM("左前门", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_ZHM("左后门", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_YHM("右后门", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_YQM("右前门", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_ZQYZB("左前叶子板", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_YHYZB("右后叶子板", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_YQYZB("右前叶子板", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_ZHYZB("左后叶子板", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_QG("前杠", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_HG("后杠", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_QJQ("前机盖", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_HCG("后仓盖", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_CD("车顶", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_AZ("A柱", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_BZ("B柱", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_CZ("C柱", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_ZDB("左大边", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_YHB("右大边", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_FDZCN("发动机舱内", "钣金", new String[]{"60"}, BigDecimal.ZERO),
    BJ_HBXCN("后备箱舱内", "钣金", new String[]{"60"}, BigDecimal.ZERO);

    private String serviceName;//服务名称
    private String cateName;//服务类别
    private String[] sharePrices;//服务价格
    private BigDecimal surfaceNum;//服务面数（补漆、喷漆类别，钣金为0）

    ShareServiceEnum(String serviceName, String cateName, String[] sharePrices, BigDecimal surfaceNum) {
        this.serviceName = serviceName;
        this.cateName = cateName;
        this.sharePrices = sharePrices;
        this.surfaceNum = surfaceNum;
    }

    public static ShareServiceEnum[] getMessages() {
        ShareServiceEnum[] arr = values();
        return arr;
    }

    public static BigDecimal getSurfaceNumByServiceNameAndCateName(String serviceName,String cateName) {
        for (ShareServiceEnum shareServiceEnum : ShareServiceEnum.values()) {
            if (shareServiceEnum.getServiceName().equals(serviceName) && shareServiceEnum.getCateName().equals(cateName) && shareServiceEnum.getSurfaceNum().compareTo(BigDecimal.ZERO) == 1) {
                return shareServiceEnum.getSurfaceNum();
            }
        }
        return null;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public String getCateName() {
        return this.cateName;
    }

    public String[] getSharePrices() {
        return this.sharePrices;
    }

    public BigDecimal getSurfaceNum() {
        return this.surfaceNum;
    }
}
