package com.tqmall.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by yuchengdu on 16/7/29.
 */
public class  NamedTask<T> extends FutureTask<T> implements Nameable{
    final String cName;
    public NamedTask(Runnable runnable, T result) {
        super(runnable, result);
        if(runnable instanceof Nameable)
            cName = ((Nameable)runnable).getName();
        else
            cName = runnable.getClass().getName();
    }
    public NamedTask(Callable<T> callable) {
        super(callable);
        if(callable instanceof Nameable)
            cName = ((Nameable)callable).getName();
        else
            cName = callable.getClass().getName();
    }

    public String getName() {
        return cName;
    }
}
