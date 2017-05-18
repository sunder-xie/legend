package com.tqmall.legend.facade.sms.newsms.util;

/**
 * Created by majian on 16/11/24.
 * 分页器
 */
public class Pager {
    private int pageSize;//每页大小
    private int totalSize;//数据大小

    public Pager(int pageSize, int totalSize) {
        this.pageSize = pageSize;
        this.totalSize = totalSize;
    }

    /**
     * @param pageNum form 1
     * @return
     */
    public int getStartIndex(int pageNum){
        return (pageNum -1) * pageSize;
    }

    public int getEndIndex(int pageNum) {
        int toIndex = getStartIndex(pageNum) + pageSize;
        if (toIndex > totalSize) {
            toIndex = totalSize;
        }
        return toIndex;
    }

    public int getPages() {
        return totalSize/pageSize + 1;
    }
}
