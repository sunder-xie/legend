package com.tqmall.legend.biz.api.redis;

/**
 * redis key
 * <p/>
 * Created by dongc on 15/10/30.
 */
public final class RedisKey {

    // 当前在线活动(ACTIVITY:ONLINE)
    public static final String ACTIVITY_ONLINE = "ACTIVITY:ONLINE";
    // 活动实体（ACTIVITY:活动ID)
    public static final String ACTIVITY = "ACTIVITY:";
    // 活动总抽奖次数(ACTIVITY:TOTALLOTTERYNUM:活动ID)
    public static final String ACTIVITY_TOTALLOTTERYNUM = "ACTIVITY:TOTALLOTTERYNUM:";
    // 活动TOP抽奖记录(ACTIVITY:TOPRECORD:活动ID)
    public static final String ACTIVITY_TOPRECORD = "ACTIVITY:TOPRECORD:";
    // 活动配置信息(ACTIVITY:CONFIG:活动ID)
    public static final String ACTIVITY_CONFIG = "ACTIVITY:CONFIG:";
    // ISERVER 活动总抽奖次数(ACTIVITY:RPC:TOTALLOTTERYNUM:activityId:userGlobalId)
    public static final String ACTIVITY_RPC_TOTALLOTTERYNUM = "ACTIVITY:RPC:TOTALNUM:";

}
