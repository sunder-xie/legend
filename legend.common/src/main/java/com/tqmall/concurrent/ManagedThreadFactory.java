package com.tqmall.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * Created by yuchengdu on 16/7/29.
 */
public class ManagedThreadFactory {
    private static final ThreadFactory NON_FAC = new NamedThreadFactory("anon-pool");

    public static ThreadFactory defaultThreadFactory() {
        String name = null;
        Throwable t = new Throwable();
        for (StackTraceElement st : t.getStackTrace()) {
            String className = st.getClassName();
            if (!className.startsWith("com.tqmall.concurrent")) {
                name = "anon-" + st.getClassName() + "." + st.getMethodName() + ":" + st.getLineNumber();
                return new NamedThreadFactory(name);
            }
        }

        return NON_FAC;
    }
}
