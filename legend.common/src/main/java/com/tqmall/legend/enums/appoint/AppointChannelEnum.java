package com.tqmall.legend.enums.appoint;

/**
 * Created by wushuai on 16/4/6.
 */
public enum AppointChannelEnum {

    WEB(0, "门店WEB", false,
            1, true, false,
            true,false),
    MACE(1, "商家APP", false,
            1, true, false,
            false,true),
    DANDELION(2, "车主APP", false,
            1, true, true,
            false,true),
    LOAM(3, "车主微信", false,
            1, true, true,
            false,true),
    CHENGNIU(4, "橙牛", false,
            0, false, false,
            false,true),
    DIDI(5, "滴滴", false,
            1, true, true,
            false,true),
    YXCL(6, "易迅车联", false,
            1, false, false,
            false,true),//此渠道已废弃
    H5(7, "H5商家详情分享页面", false,
            1, true, true,
            false,true),
    BMXC(8, "斑马行车", false,
            0, false, false,
            false,true),
    LEGENDM(9, "云修系统客服", false,
            1, true, true,
            false,false),
    SUMMER(10, "夏日活动H5页面", true,
            1, true, true,
            false,true),
    SHOP_WECHAT(11, "门店微信公众号", false,
            1, true, true,
            false,true);

    private final Integer channelId;//预约单来源渠道编号
    private final String channelName;//预约单来源渠道名称
    private final boolean needLimit;//是否限制同日预约次数

    private final Integer pushStatus;//下推状态 0 需下推, 1不需下推
    private final boolean pushMsg;//是否推消息到消息队列
    private final boolean smsSA;//是否发短信给SA(门店接待员)

    private final boolean checkAppointTime;//是否检查预约时间要大雨当前时间
    private final boolean needUpdateCustomer;//是否需要更新客户,车辆信息

    private AppointChannelEnum(int channelId, String channelName, boolean needLimit, int pushStatus, boolean pushMsg, boolean smsSA,boolean checkAppointTime,boolean needUpdateCustomer) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.needLimit = needLimit;
        this.pushStatus = pushStatus;
        this.pushMsg = pushMsg;
        this.smsSA = smsSA;
        this.checkAppointTime = checkAppointTime;
        this.needUpdateCustomer = needUpdateCustomer;
    }

    /**
     * get enum by channelId
     * @param channelId
     * @return OrderStatusEnum
     */
    public static AppointChannelEnum getAppointChannelEnum(int channelId) {
        for (AppointChannelEnum e : AppointChannelEnum.values()) {
            if (e.channelId==channelId) {
                return e;
            }
        }
        return null;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public boolean isNeedLimit() {
        return needLimit;
    }

    public Integer getPushStatus() {
        return pushStatus;
    }

    public boolean isPushMsg() {
        return pushMsg;
    }

    public boolean isSmsSA() {
        return smsSA;
    }

    public boolean isCheckAppointTime() {
        return checkAppointTime;
    }

    public boolean isNeedUpdateCustomer() {
        return needUpdateCustomer;
    }
}
