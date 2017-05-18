package com.tqmall.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by yuchengdu on 16/7/29.
 */
public class ManagedThreadPool extends ThreadPoolExecutor implements TimeCounter {


    public ManagedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                             BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,ManagedThreadFactory.defaultThreadFactory());
    }

    public ManagedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                             BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, ManagedThreadFactory.defaultThreadFactory(), handler);
    }

    public ManagedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                             BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ManagedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                             BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    private static final Logger log = LoggerFactory.getLogger(ManagedThreadPool.class);

    private final AtomicLong finishTime = new AtomicLong();

    private final static ThreadLocal<Long> local = new ThreadLocal<Long>();

    /**
     * 获取线程池的总执行时长z
     * @return
     */
    public long getFinishTime() {
        return finishTime.get();
    }

    @Override
    public void beforeExecute(Thread t, Runnable r) {
        local.set(System.currentTimeMillis());
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable ex) {
        try {
            long time = local.get();
            local.remove();
            finishTime.addAndGet(System.currentTimeMillis() - time);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (ex != null)
            log.warn("在线程池中捕获到未知异常:" + ex, ex);
    }

    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new NamedTask<T>(runnable, value);
    }

    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new NamedTask<T>(callable);
    }

    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor tpe = new ManagedThreadPool(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>());

        tpe.execute(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread.sleep(1000);
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            System.out.println(t);
        }

        tpe.shutdown();
    }
}
