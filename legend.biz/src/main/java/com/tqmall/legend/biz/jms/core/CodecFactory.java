package com.tqmall.legend.biz.jms.core;

import java.io.IOException;

/**
 * Created by guozhiqiang on 14-7-4.
 */
public interface CodecFactory {

    byte[] serialize(Object obj) throws IOException;

    Object deSerialize(byte[] in) throws IOException;

}
