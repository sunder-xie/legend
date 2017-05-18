package com.tqmall.legend.server;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

/**
 * Created by xin on 2017/3/15.
 */
public class BaseDubboTest {

    protected static <T> T getService(Class<T> clazz) {
        ApplicationConfig application = new ApplicationConfig();
        application.setName("legend");

        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("zookeeper://115.29.220.170:2182");

        ReferenceConfig<T> reference = new ReferenceConfig<>();
        reference.setApplication(application);
        reference.setRegistry(registry);
        reference.setInterface(clazz);
        reference.setVersion("1.0.0.local.twg");

        return reference.get();
    }
}
