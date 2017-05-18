package com.tqmall.legend.facade.activity.vo;

import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.entity.activity.ShopActivity;
import com.tqmall.legend.enums.activity.ShopActivityStatusEnum;
import com.tqmall.legend.enums.wechat.WechatActModuleTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * Created by wushuai on 16/8/2.
 */
@Data
public class ShopActivityVo extends ShopActivity {
    private Integer isJoin;//0未参加,1已参加
    private Integer visitCount;//活动页面访问量
    private Integer partCount;//参与活动用户数
    private String timeLimitStr;//活动时间范围
    private String gmtCreateStr;//参加活动时间
    private String previewUrl;//活动短链接

    private String shopActivityStatus;//门店活动状态:未参加,已参加,已结束
    private Integer autoMenuSuccess;//自动生成菜单状态:0失败,1成功
    private Integer wechatActivityType;//1.预约活动,2.游戏活动,3.砍价活动,3.拼团活动

    private Integer actGameStatus;//0未参加  1关闭 2 草稿 3 发布

    private Integer canJoin=1;//能否参加:0不能参加,未开通微信公众号 1能参加, 2不能参加,未开通奖励金模式


    public enum WechatShopActGameStatusEnum {

        NOT_PART(0,"未参加"),
        CLOSED(1,"关闭"),
        OUTLINE(2,"草稿"),
        PUBLISH(3,"发布");

        private final Integer value;
        private final String name;

        private WechatShopActGameStatusEnum(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

    }

    public String getTimeLimitStr() {
        if(getStartTime()!=null &&getEndTime()!=null) {
            return DateUtil.convertDateToYMDHMS(getStartTime()) +"\n至\n" +DateUtil.convertDateToYMDHMS(getEndTime());
        }
        return "";
    }

    public String getGmtCreateStr() {
        if(getIsJoin()==null||getIsJoin()==0){
            return "";
        }
        if(getGmtCreate()==null){
            return "";
        }
        return DateUtil.convertDateToYMDHMS(getGmtCreate());
    }

    public String getShopActivityStatus() {
        if(wechatActivityType!=null && wechatActivityType==WechatActModuleTypeEnum.GAME.getValue()){
            //游戏活动
            if(actGameStatus==null
                    ||actGameStatus==ShopActivityVo.WechatShopActGameStatusEnum.NOT_PART.getValue()
                    ||actGameStatus.intValue()==ShopActivityVo.WechatShopActGameStatusEnum.OUTLINE.getValue()){
                return "未参加";
            } else if(actGameStatus.intValue()== WechatShopActGameStatusEnum.CLOSED.getValue()){
                return "已结束";
            } else if(actGameStatus.intValue()== WechatShopActGameStatusEnum.PUBLISH.getValue()){
                if (getEndTime().after(new Date())){
                    return "已参加";
                }
                return "已结束";
            }
            return "未知状态";
        } else{
            //普通活动
            if(getIsJoin()==null ||getIsJoin()==0){
                return "未参加";
            }
            if (getEndTime() != null && getEndTime().after(new Date()) && getActStatus() != null && getActStatus().intValue() == ShopActivityStatusEnum.PUBLISH.getValue()) {
                return "已参加";
            }
            return "已结束";
        }
    }
}
