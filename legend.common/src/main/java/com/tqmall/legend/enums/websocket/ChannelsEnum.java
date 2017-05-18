package com.tqmall.legend.enums.websocket;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by zsy on 2016/06/21.
 */
public enum ChannelsEnum {
    BP_BOARD_PROCESS_SHARE_NOTICE(1, "工序看板redis订阅管道"),
    LOGIN_SHARE_NOTICE(2, "安全登录redis订阅管道");

    private Integer code;
    private String channel;

    public Integer getCode() {
        return this.code;
    }

    public String getChannel() {
        return this.channel;
    }

    private ChannelsEnum(Integer code, String channel) {
        this.code = code;
        this.channel = channel;
    }

    public static String getChannelByName(String name) {
        ChannelsEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            ChannelsEnum iEnum = arr[i];
            if (iEnum.name().equals(name)) {
                return iEnum.getChannel();
            }
        }
        return null;
    }

    public static Integer getCodeByName(String name) {
        ChannelsEnum[] arr = values();
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            ChannelsEnum iEnum = arr[i];
            if (iEnum.name().equals(name)) {
                return iEnum.getCode();
            }
        }
        return null;
    }

    public static String[] getChannelsStr() {
        ChannelsEnum[] arr = values();
        List<String> stringList = Lists.newArrayList();
        for (ChannelsEnum channelsEnum : arr) {
            stringList.add(channelsEnum.name());
        }
        String[] channels = stringList.toArray(new String[arr.length]);
        return channels;
    }
}
