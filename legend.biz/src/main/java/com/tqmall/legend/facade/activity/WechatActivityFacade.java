package com.tqmall.legend.facade.activity;

import com.tqmall.common.UserInfo;
import com.tqmall.dandelion.wechat.client.dto.JoinActivityLimitDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActAppointmentDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.ActivityGroupBuyUserDetailDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.DiscountServiceDTO;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.activity.vo.ActivityAppointDataVo;
import com.tqmall.legend.facade.activity.vo.ActivityDetailVo;
import com.tqmall.legend.facade.activity.vo.ActivityGroupBuyShareVo;
import com.tqmall.legend.facade.activity.vo.BarginAppointDataVo;
import com.tqmall.legend.facade.activity.vo.BarginCouponDataVo;
import com.tqmall.legend.facade.activity.vo.SaveWechatActivityVo;
import com.tqmall.legend.facade.activity.vo.ShopActivityVo;
import org.springframework.data.domain.Page;

import java.util.Map;

/**
 * Created by wushuai on 16/8/2.
 */
public interface WechatActivityFacade {
    /**
     * 查询门店微信活动(推送到门店的活动模版,若门店参加了还需要查出门店活动实体)<br>
     * 非联表实现
     */
    public Page<ShopActivityVo> getActivityPage(Map<String,Object> param);

    /**
     * 查询微信活动的预约统计信息
     * @param shopId
     * @param actId
     * @return
     */
    public Result<ActivityAppointDataVo> qryActivityAppointData(Long shopId, Long actId);

    /**
     * 查询微信活动内服务预约的用户列表
     * @param shopId
     * @param actId
     * @param serviceId
     * @return
     */
    public Result<Page<ActAppointmentDTO>> qryActAppointList(Long shopId, Long actId, Long serviceId, int offset, int limit);

    /**
     * 查询活动详细信息
     * @param shopId
     * @param actTplId
     * @return
     */
    public ActivityDetailVo getActivityDetail(Long shopId, Long actTplId);

    /**
     * 根据活动模版保存门店活动实体
     * @param saveWechatActivityVo
     * @param userInfo
     * @return
     */
    public Result<ShopActivityVo> save(SaveWechatActivityVo saveWechatActivityVo, UserInfo userInfo);

    /**
     * 获取活动预览url
     * @param shopId
     * @param actId
     * @return
     */
    public Result<String> getActivityPreviewUrl(Long shopId, Long actId,Integer isFormal);

    JoinActivityLimitDTO getJoinActivityLimit(Long actTplId, Long shopId);

    /**
     * 查询微信砍价活动的预约统计信息
     * @param shopId
     * @param actId
     * @return
     */
    public Result<BarginAppointDataVo> qryBarginAppointData(Long shopId, Long actId);

    /**
     * 查询微信砍价活动内服务预约的用户列表
     * @param shopId
     * @param actId
     * @param serviceId
     * @return
     */
    public Result<Page<DiscountServiceDTO>> qryBarginAppointList(Long shopId, Long actId, Long serviceId, int offset, int limit);

    /**
     * 判断服务是否配置在活动中
     * @param shopId
     * @param serviceId
     * @return
     */
    boolean whetherServiceInActivity(Long shopId, Long serviceId);

    /**
     * 微信拼团活动的拼团用户信息
     * @param shopId
     * @param actId
     * @return
     */
    ActivityGroupBuyShareVo qryActivityGroupbugShareData(Long shopId, Long actId);

    /**
     * 微信拼团活动单个服务详细用户列表信息
     * @param shopId
     * @param actId
     * @param serviceId
     * @param offset
     * @param limit
     * @return
     */
    Page<ActivityGroupBuyUserDetailDTO> qryQroupBuyUserDetail(Long shopId, Long actId, Long serviceId, int offset, int limit);


    /**
     * 查询微信砍券活动的参与统计信息
     * @param shopId
     * @param actId
     * @return
     */
    public BarginCouponDataVo qryBarginCouponData(Long shopId, Long actId);

    /**
     * 查询微信砍价活动内服务预约的用户列表
     * @param shopId
     * @param actId
     * @param serviceId
     * @return
     */
    public Page<DiscountServiceDTO> qryBarginCouponUsers(Long shopId, Long actId, Long couponTplId, int offset, int limit);

}
