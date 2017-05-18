package com.tqmall.legend.log;

import com.tqmall.common.UserInfo;

/**
 * Created by xin on 16/10/9.
 */
public class ExportLog {

    /**
     * 导出日志
     * 例：【工单导出】门店id:1,操作人id:1,操作人姓名：test，导出耗时10000毫秒
     *
     * @param header     前缀 如：工单导出
     * @param userInfo   用户信息
     * @param totalSize  导出总数量
     * @param exportTime 导出时间
     * @return
     */
    public static String getExportLog(String header, UserInfo userInfo, Integer totalSize, long exportTime) {
        StringBuffer exportLog = new StringBuffer();
        exportLog.append("【");
        exportLog.append(header);
        exportLog.append("】");
        exportLog.append("门店id:");
        exportLog.append(userInfo.getShopId());
        exportLog.append(",操作人id:");
        exportLog.append(userInfo.getUserId());
        exportLog.append(",操作人姓名:");
        exportLog.append(userInfo.getName());
        exportLog.append(",导出数量:");
        exportLog.append(totalSize);
        exportLog.append(",导出耗时:");
        exportLog.append(exportTime);
        exportLog.append("毫秒");
        return exportLog.toString();
    }
}
