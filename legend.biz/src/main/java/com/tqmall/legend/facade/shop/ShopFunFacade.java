package com.tqmall.legend.facade.shop;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.enums.shop.ShopTagCodeEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zsy on 16/7/7.
 */
public interface ShopFunFacade {
    /**
     * 根据门店id判断是否加入委托体系
     *
     * 状态：0：无，1：加入 默认为0
     * @param shopId
     * @return false 未加入，true 加入
     */
    boolean isJoin(Long shopId);

    /**
     * 根据门店id判断是否使用车间
     *
     * 使用车间状态 0：不使用车间 1：使用车间
     * @param shopId false 不使用车间，true 使用
     * @return
     */
    boolean isUseWorkshop(Long shopId);

    /**
     * 是否是钣喷共享中心
     *
     * @param shopId
     * @return false 不是，true 是
     */
    boolean isBpShare(HttpServletRequest request, Long shopId);

    /**
     * 是否使用新的预检单/综合维修单
     * 条件：如果可以委托、使用车间或者是钣喷中心
     * @param shopId
     * @return false 不是，true 是
     */
    boolean isUseNewPrecheck(HttpServletRequest request, Long shopId);

    /**
     * 安心保险：根据门店的userGlobalId,获取门店选择的模式
     *
     * @param request
     * @param userGlobalId
     * @return ""：未参加任何模式
     * 模式一："1"
     * 模式二："2"（包含买服务包送保险、买保险送服务包）
     */
    String getAnxinInsuranceModel(HttpServletRequest request, String userGlobalId);

    /**
     * 校验当前登陆的用户是否具有funcName列出的所有权限<br>
     *     管理员Admin拥有所有权限
     * @param userInfo
     * @param funcName
     * @return
     */
    boolean checkFuncAnd(UserInfo userInfo, String... funcName);

    /**
     * 校验当前登陆的用户是否具有funcName列出的所有权限中的任意一项<br>
     *     管理员Admin拥有所有权限
     * @param userInfo
     * @param funcName
     * @return
     */
    boolean checkFuncOr(UserInfo userInfo, String... funcName);

    /**
     * 设置门店标签信息，如是否是样板门店
     * @param request
     * @param shopId
     * @param shopTagCodeEnum
     * @return
     */
    boolean hasTagName(HttpServletRequest request, Long shopId, ShopTagCodeEnum shopTagCodeEnum);

    /**
     * 是否是样板店
     * @param shopId
     * @return
     */
    boolean isYBD(Long shopId);
}
