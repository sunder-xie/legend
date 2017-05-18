package com.tqmall.legend.biz.manager.thread;

import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by YangFalcom on 14-9-22.
 *
 */
@Component
public class ThreadManagerImpl implements ThreadManager {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    @Override
    public void execute(Runnable task) {
        EXECUTOR_SERVICE.execute(task);
    }


}
