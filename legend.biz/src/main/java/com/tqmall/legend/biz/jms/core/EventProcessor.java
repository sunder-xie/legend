package com.tqmall.legend.biz.jms.core;

/**
 * Created by guozhiqiang on 14-7-4.
 */
public interface EventProcessor {
  public void process(Object e);
}
