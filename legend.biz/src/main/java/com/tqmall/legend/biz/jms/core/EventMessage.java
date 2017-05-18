package com.tqmall.legend.biz.jms.core;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by guozhiqiang on 14-7-4.
 */
public class EventMessage implements Serializable {

    private static final long serialVersionUID = -5923449142358760059L;

    private byte[] eventData;

    public EventMessage(byte[] eventData) {
        this.eventData = eventData;
    }

    public EventMessage() {
    }


    public byte[] getEventData() {
        return eventData;
    }

    @Override
    public String toString() {
        return "EopEventMessage [eventData=" + Arrays.toString(eventData) + "]";
    }
}
