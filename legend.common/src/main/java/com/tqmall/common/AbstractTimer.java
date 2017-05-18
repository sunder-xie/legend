package com.tqmall.common;

import com.tqmall.concurrent.ManagedThreadPool;
import com.tqmall.concurrent.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by yuchengdu on 16/7/29.
 */
public abstract class AbstractTimer {
    /**
     * 日志的具体实现
     */
    protected Logger logger;

    protected RejectedExecutionHandler rejectedExecutionHandler;

    protected BlockingQueue<Runnable> taskQueue;

    protected ManagedThreadPool executor;

    /**
     * @param jobClazz 子类的Class对象
     * @param jobName 子类调度名称
     * @param blockingQueueSize  任务队列的数量
     * @param threadSize 线程池大小
     */
    public AbstractTimer(Class jobClazz, final String jobName, int blockingQueueSize, int threadSize) {
        logger = LoggerFactory.getLogger(jobClazz);

        taskQueue = new ArrayBlockingQueue<Runnable>(blockingQueueSize);

        rejectedExecutionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                logger.error("BlockingQueue is full in" + jobName);
            }
        };

        executor = new ManagedThreadPool(threadSize, threadSize, 60L, TimeUnit.SECONDS, taskQueue,
                new NamedThreadFactory(jobName + "ThreadPool"), rejectedExecutionHandler);
    }

    public abstract void execute();
}
