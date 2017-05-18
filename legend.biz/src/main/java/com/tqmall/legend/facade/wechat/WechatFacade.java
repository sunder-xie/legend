package com.tqmall.legend.facade.wechat;

import com.tqmall.common.UserInfo;
import com.tqmall.dandelion.wechat.client.dto.wechat.*;
import com.tqmall.dandelion.wechat.client.dto.wechat.cardCoupon.CouponMapDTO;
import com.tqmall.dandelion.wechat.client.dto.wechat.cardCoupon.MembershipCardMapDTO;
import com.tqmall.dandelion.wechat.client.param.wechat.AssessmentParam;
import com.tqmall.dandelion.wechat.client.param.wechat.CouponStatisticsParam;
import com.tqmall.dandelion.wechat.client.param.wechat.RescueParam;
import com.tqmall.dandelion.wechat.client.param.wechat.GameCouponUserLogParam;
import com.tqmall.dandelion.wechat.client.param.wechat.TemplateReplyParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.QueryCardParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.QueryCouponParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.SaveCardParam;
import com.tqmall.dandelion.wechat.client.param.wechat.cardCoupon.SaveCouponParam;
import com.tqmall.holy.provider.entity.customer.CustomerJoinAuditDTO;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.facade.activity.vo.GameActivityDetailVo;
import com.tqmall.legend.facade.activity.vo.GameCouponStatisticsVo;
import com.tqmall.legend.facade.activity.vo.ShopActivityVo;
import com.tqmall.legend.facade.wechat.vo.*;
import com.tqmall.legend.object.param.onlinepay.CallbackParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by wushuai on 16/6/6.
 */
public interface WechatFacade {

    /**
     * 从CRM获取门店注册资料信息
     *
     * @param shopId
     * @return
     */
    public CustomerJoinAuditDTO getShopInfoFromCrm(Long shopId,Integer shopLevel);

    /**
     * 门店签约微信号服务
     *
     * @param shopId
     * @return
     */
    public Result<String> initShopWechatInfo(Long shopId);

    /**
     * 查询门店微信信息
     *
     * @param shopId
     * @return
     */
    public Result<ShopWechatVo> qryShopWechat(Long shopId);

    /**
     * 支付宝支付注册费用
     *
     * @param userInfo
     * @param backUrl
     * @return
     */
    public Result<String> regPayByZhifubao(Long payTplId, UserInfo userInfo, String backUrl);

    /**
     * 更新门店微信资料
     *
     * @param shopWechatVo
     * @return
     */
    public Result saveWechatInfo(ShopWechatVo shopWechatVo,Long shopId);

    /**
     * 线下支付
     *
     * @param payTplId
     * @param payVoucher
     * @param userInfo
     * @return
     */
    public Result regPayByOffline(Long payTplId, String payVoucher, UserInfo userInfo);

    /**
     * 添加支付流水
     *
     * @param orderSn
     * @param userGlobalId
     * @param payTplId
     * @param payWay       1.支付宝支付2线下支付
     * @param payVoucher   线下支付时的支付凭证
     * @return
     */
    public Result addPayFlow(String orderSn, Long userGlobalId, Long payTplId, String payWay, String payVoucher);

    /**
     * 查询费用模板
     *
     * @param payTplParams
     * @return
     */
    public Result<List<PayTplDTO>> qryPayTpl(PagingParams payTplParams);

    /**
     * 在线支付结果回写
     *
     * @return
     */
    public com.tqmall.core.common.entity.Result<String> callBackPayFlow(CallbackParam param);

    /**
     * 查询注册费用支付信息
     *
     * @param shopId
     * @return
     */
    public Result<PayFlowDTO> qryRegPayInfo(Long shopId);

    /**
     * 查询微信文章
     *
     * @param articleParams
     * @return
     */
    public Result<Page<WechatArticleVo>> queryArticlesPage(ArticleParams articleParams, Long shopId);

    /**
     * 当月剩余可发送文章数
     *
     * @return
     */
    public Result<Integer> canSendArticleCount(Long shopId);

    /**
     * 发送微信文章
     *
     * @return
     */
    public Result<String> sendArticle(Long shopAricleRelId, Long shopId);

    /**
     * 新增或者更新自动回复消息(根据有无Id区分)
     *
     * @param templateReplyDTO
     * @return
     */
    public Result<Integer> saveReplyMsg(TemplateReplyDTO templateReplyDTO, Long shopId);

    /**
     * 查询门店wifi
     *
     * @param shopId
     * @return
     */
    public Result<WechatWifiDTO> getWifiByShopId(Long shopId);

    /**
     * 保存或者更新门店WiFi信息(根据有无Id区分)
     *
     * @return
     */
    public Result<Integer> saveWifi(WechatWifiDTO wechatWifiDTO, Long shopId);

    /**
     * 分页查询自动回复消息列表
     *
     * @param shopId
     * @param offset
     * @param limit
     * @return
     */
    public Result<Page<TemplateReplyDTO>> qryReplyMsgPage(TemplateReplyParam templateReplyParam, Long shopId, Long offset, Long limit);

    /**
     * 查询二维码列表
     *
     * @param qrcodeParams
     * @param shopId
     * @return
     */
    public Result<Page<ShopQrcodeRelDTO>> qryQrcodePage(QrcodeParams qrcodeParams, Long shopId);

    /**
     * 查询微信菜单
     *
     * @param shopId
     * @return
     */
    public WechatMenuVo qryWechatMenu(Long shopId);

    /**
     * 保存微信菜单设置
     *
     * @param menuDTO
     * @return
     */
    public Result<MenuDTO> saveWechatMenu(MenuDTO menuDTO,Long shopId);

    /**
     * 查询微信首页数据
     *
     * @param shopId
     * @return
     */
    public DataDTO qryHomeData(Long shopId);

    /**
     * 删除自动回复消息
     * @param msgId
     * @return
     */
    public Result<Integer> deleteReplyMsg(Long msgId);

    /**
     * 查询账户优惠券列表
     * @param searchParams
     * @param pageable
     * @return
     */
    public Result<Page<WechatAccountCouponVo>> qryAccountCouponList(Map<String, Object> searchParams, Pageable pageable);

    /**
     *  查询门店微信公众号优惠券配置
     * @param shopId
     * @return
     */
    public Result<WechatCfgCouponVo> qryWechatCouponCfg(Long shopId);

    /**
     *  查询门店微信公众号优惠券配置历史
     * @param shopId
     * @return
     */
    public Result<List<WechatCfgCouponVo>> qryWechatCouponHisCfg(Long shopId);

    /**
     * 保存门店微信公众号配置
     * @param wechatCouponVo
     * @return
     */
    public Result<String> saveWechatCouponCfg(WechatCfgCouponVo wechatCouponVo,Long shopId);

    /**
     * 查询门店端微信公众号优惠券领取使用情况统计数据
     * @param couponStatisticsParam
     * @param shopId
     * @return
     */
    public Result<CouponStatisticsDTO> qryCouponStatis(CouponStatisticsParam couponStatisticsParam, Long shopId);

    /**
     * 获取到微信官方授权的相关信息
     * @param shopId
     * @return
     */
    Result getGrantUrl(Long shopId,String redirectUrl);

    /**
     * 将授权同步通知授权码传给ddl-wechat进行授权校验
     * @param shopId
     * @param authCode
     * @return
     */
    Result checkAuthor(Long shopId, String authCode);

    /**
     * 删除自定义菜单
     * @param btnId
     * @return
     */
    Result<String> delCustomMenu(Long btnId);


    /**
     * 保存自定义菜单
     * @return
     */
    Result<ButtonDTO> saveCustomMenu(ButtonDTO buttonDTO,Long shopId);

    /**
     * 查询游戏活动列表
     * @param param
     * @return
     */
    public Page<ShopActivityVo> getActivityPage(Map<String,Object> param) throws BizException;

    /**
     * 查询微信游戏活动详情
     * @param gameId
     * @return
     * @throws BizException
     */
    public GameActivityDetailVo gameDetailsByGameId(Long gameId,Long shopId) throws BizException;

    /**
     * 保存微信游戏活动
     * @param gameActivityDetailVo
     * @param userInfo
     * @return
     */
    public Result<GameActivityDetailVo> saveWechatGameActivity(GameActivityDetailVo gameActivityDetailVo,UserInfo userInfo) throws BizException;

    /**
     * 微信游戏活动的领券信息
     * @param gameId
     * @param shopId
     * @return
     */
    public GameCouponStatisticsVo gameCouponStatistic(Long gameId, Long shopId) throws BizException;

    /**
     * 微信游戏活动优惠券领取用户列表信息
     * @param shopId
     * @param gameCouponUserLogParam
     * @return
     */
    public Page<GameCouponLogDTO> getGameCouponUserList(Long shopId, GameCouponUserLogParam gameCouponUserLogParam);

    /**
     * 获取微信活动的url
     * @param shopId
     * @param actId
     * @param isFormal
     * @return
     */
    public Result<String> getGameActUrl(Long shopId, Long actId, Integer isFormal);



    /**
     * 查询定损列表
     *
     * @param assessmentParam
     * @return
     */
    public Result<Page<AssessmentVo>> queryAssessmentsPage(AssessmentParam assessmentParam, Long shopId);

    /**
     * 查询救援列表
     *
     * @param rescueParam
     * @return
     */
    public Result<Page<RescueVo>> queryRescuesPage(RescueParam rescueParam, Long shopId);

    /**
     * 修改定损状态
     * @param id
     * @param processStatus
     * @return
     */
    public Result<String> updateAssessmentStatus(Long id,Integer processStatus);

    /**
     * 修改救援状态
     * @param id
     * @param processStatus
     * @return
     */
    public Result<String> updateRescuesStatus(Long id,Integer processStatus);

    /**
     * 查询微信端卡券商城中会员卡信息
     * @param shopId
     * @param queryCardParam
     * @return
     */
    Page<WechatFavormallCardVo> qryFavormallCardList(Long shopId, QueryCardParam queryCardParam)  throws IllegalArgumentException,BizException;

    /**
     * 保存微信端卡券商城中会员卡设置
     * @param shopId
     * @param saveCardParam
     * @return
     */
    boolean saveFavormallCard(Long shopId, SaveCardParam saveCardParam) throws IllegalArgumentException,BizException;

    /**
     * 查询微信端卡券商城中优惠券信息
     * @param shopId
     * @param queryCouponParam
     * @return
     */
    Page<WechatFavormallCouponVo> qryFavormallCouponList(Long shopId, QueryCouponParam queryCouponParam);

    /**
     * 保存微信端卡券商城中优惠券设置
     * @param shopId
     * @param saveCouponParam
     * @return
     */
    boolean saveFavormallCoupon(Long shopId, SaveCouponParam saveCouponParam);

    /**
     * 检查卡券商城中是否已经配置过此会员卡,优惠券
     * @param configType 1.会员卡,2.优惠券
     * @param configId
     * @return
     */
    boolean isExistFavormallConfig(Integer configType, Long configId);
}
