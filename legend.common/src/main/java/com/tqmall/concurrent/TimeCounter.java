package com.tqmall.concurrent;

/**
 * Created by yuchengdu on 16/7/29.
 * 主要用来统计线程池的总计执行时长
 */
public interface TimeCounter {
    long getFinishTime();
}
