package com.tqmall.magic.web.base;

import com.tqmall.common.UserInfo;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.magic.PartnerFacade;
import com.tqmall.legend.enums.magic.PartnerStatusEnum;
import com.tqmall.legend.facade.magic.vo.ShareStatusVO;
import com.tqmall.legend.facade.magic.vo.ShopPartnerVO;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macx on 16/5/11.
 */
@Controller
@Slf4j
@RequestMapping("share/partner")
public class PartnerController extends BaseController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private PartnerFacade partnerFacade;

    /**
     * 进入股东列表页
     * @return
     */
    @RequestMapping("index")
    public String index(Model model) {
        model.addAttribute("leftNav","partner");
        return "yqx/page/magic/partner/stockholder";
    }

    /**
     * 进入股东新增页
     * @return
     */
    @RequestMapping("add")
    public String add() {

        return "yqx/page/magic/partner/add";
    }

    /**
     * 股东状态
     * @return
     */
    @RequestMapping("getPartnerStatus")
    @ResponseBody
    public Result getPartnerStatus(){
        List<ShareStatusVO> shareStatusVOs = new ArrayList<>();
        PartnerStatusEnum[] partnerStatusEnums = PartnerStatusEnum.values();
        for (PartnerStatusEnum partnerStatusEnum : partnerStatusEnums) {
            ShareStatusVO shareStatusVO = new ShareStatusVO();
            shareStatusVO.setCode(partnerStatusEnum.getCode());
            shareStatusVO.setName(partnerStatusEnum.getName());
            shareStatusVOs.add(shareStatusVO);
        }
        return Result.wrapSuccessfulResult(shareStatusVOs);
    }

    /**
     * 获取股东分页列表
     * @param pageable
     * @param name
     * @param partnerStatus
     * @return
     */
    @RequestMapping("getPartnerPage")
    @ResponseBody
    public Result getPartnerPage(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable, String name, Integer partnerStatus) {
        log.info("[共享中心] 获取股东分页列表,参数:page={};pageSize={};name={};partnerStatus={}", pageable.getPageNumber(), pageable.getPageSize(), name, partnerStatus);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopId == null){
            log.error("[共享中心] 获取股东分页列表,登录信息错误 shopId={}",shopId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        if (partnerStatus == null){
            log.error("[共享中心] 获取股东分页列表,股东状态错误 partnerStatus={}",partnerStatus);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getCode(),"股东状态错误");
        }
        Shop shop = shopService.selectById(shopId);
        if (shop == null){
            log.error("[共享中心] 获取股东分页列表,门店不存在 shopId={}",shopId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_SHOP_NOT_EXIST_ERROR.getCode(), LegendErrorCode.SHARE_SHOP_NOT_EXIST_ERROR.getErrorMessage());
        }
        Result result;
        if (partnerStatus.compareTo(PartnerStatusEnum.UNJOIN.getCode()) == 0){
            result = partnerFacade.getUnJoinPartnerPage(pageable, shop, name);
        } else if (partnerStatus.compareTo(PartnerStatusEnum.JOIN.getCode()) == 0 || partnerStatus.compareTo(PartnerStatusEnum.QUIT.getCode()) == 0) {
            result = partnerFacade.getPartnerPage(pageable, shopId, name, partnerStatus);
        } else {
            log.error("[共享中心] 获取股东分页列表,股东状态错误 partnerStatus={}",partnerStatus);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getCode(),LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getErrorMessage());
        }
        return result;
    }

    /**
     * 获取股东信息
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "getPartnerInfo")
    @ResponseBody
    public Result getPartnerInfo(Long id) {
        log.info("[共享中心] 获取股东信息,参数:id={}",id);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopId == null){
            log.error("[共享中心] 获取股东信息,登录信息错误 shopId={}",shopId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        if (id == null) {
            log.error("[共享中心] 获取股东信息,股东id不能为空 id={}",id);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getCode(), "参数股东id不能为空");
        }
        Result result = partnerFacade.getPartnerInfo(id, shopId);
        return result;
    }


    /**
     * 获取门店已加入&待加入股东
     *
     * @return
     */
    @RequestMapping("getAllPartner")
    @ResponseBody
    public Result getAllPartner(String name) {
        log.info("[共享中心] 获取门店已加入&待加入股东列表,参数:name={}",name);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopId == null){
            log.error("[共享中心] 获取门店已加入&待加入股东列表,登录信息错误 shopId={}", shopId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        Shop shop = shopService.selectById(shopId);
        if (shop == null){
            log.error("[共享中心] 获取门店已加入&待加入股东列表,门店不存在 shopId={}",shopId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_SHOP_NOT_EXIST_ERROR.getCode(), LegendErrorCode.SHARE_SHOP_NOT_EXIST_ERROR.getErrorMessage());
        }
        Result result = partnerFacade.getAddPartnerList(shop,name);
        return result;
    }

    /**
     * 编辑股东保存
     *
     * @param shopPartnerVO
     * @return
     */
    @RequestMapping(value = "updatePartnerInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result updatePartnerInfo(@RequestBody ShopPartnerVO shopPartnerVO) {
        log.info("[共享中心] 保存股东信息,参数:shopPartnerVO={}", shopPartnerVO);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        if (shopId == null || userId == null || shopId <= 0 || userId <= 0){
            log.error("[共享中心] 保存股东信息,登录信息错误 shopId={};userId={}",shopId,userId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        if (shopPartnerVO.getId() == null){
            log.error("[共享中心] 保存股东信息,股东id不能为空");
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getCode(),"股东id不能为空");
        }
        if (shopPartnerVO.getRate() == null){
            log.error("[共享中心] 保存股东信息,结算比例为空");
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getCode(), "结算比例不能为空");
        }
        if (StringUtils.isNotBlank(shopPartnerVO.getMobile()) && !StringUtil.isMobileNO(shopPartnerVO.getMobile()) && !StringUtil.isPhone(shopPartnerVO.getMobile())) {
            log.error("[共享中心] 保存股东信息,手机号码格式错误 mobile={}", shopPartnerVO.getMobile());
            return Result.wrapErrorResult(LegendErrorCode.SHARE_MOBILE_STYLE_ERROR.getCode(), LegendErrorCode.SHARE_MOBILE_STYLE_ERROR.getErrorMessage());
        }
        Result result = partnerFacade.updatePartnerInfo(shopPartnerVO, shopId,userId);
        return result;
    }

    /**
     * 退出股东
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "quitPartner", method = RequestMethod.POST)
    @ResponseBody
    public Result quitPartner(Long id,String reason) {
        log.info("[共享中心] 退出股东,参数:id={};reason={}",id,reason);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        if (shopId == null || shopId <= 0 || userId == null || userId <= 0) {
            log.error("[共享中心] 退出股东,登录信息错误 shopId={},userId={}", shopId, userId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        if (id == null){
            log.error("[共享中心] 退出股东,股东id为空");
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getCode(), "股东id不能为空");
        }
        if (StringUtils.isBlank(reason)) {
            log.error("[共享中心] 退出股东,退出原因为空");
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getCode(),"退出原因不能为空");
        }
        Result result = partnerFacade.quitShopPartner(id, shopId, userId, reason);
        return result;
    }

    /**
     * 加入股东
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "joinPartner", method = RequestMethod.POST)
    @ResponseBody
    public Result joinPartner(Long id) {
        log.info("[共享中心] 加入股东,参数:id={}",id);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        if (shopId == null || shopId <= 0 || userId == null || userId <= 0) {
            log.error("[共享中心] 加入股东,登录信息错误 shopId={},userId={}", shopId, userId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        if (id == null){
            log.error("[共享中心] 加入股东,股东id为空");
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_PARAM_ERROR.getCode(),"股东id不能为空");
        }
        Result result = partnerFacade.joinShopPartner(id, shopId, userId);
        return result;
    }


    /**
     * 添加股东
     *
     * @param partnerIds
     * @return
     */
    @RequestMapping(value = "addPartner", method = RequestMethod.POST)
    @ResponseBody
    public Result addPartner(String partnerIds) {
        log.info("[共享中心] 添加股东,参数:partnerIds={}",partnerIds);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        Long userId = userInfo.getUserId();
        if (shopId == null || shopId <= 0 || userId == null || userId <= 0) {
            log.error("[共享中心] 添加股东,登录信息错误 shopId={},userId={}", shopId, userId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        if (StringUtils.isBlank(partnerIds)) {
            log.error("[共享中心] 添加股东,id为空");
            return Result.wrapErrorResult(LegendErrorCode.SHARE_PARTNER_ADD_PARAM_ERROR.getCode(), LegendErrorCode.SHARE_PARTNER_ADD_PARAM_ERROR.getErrorMessage());
        }
        String[] partnerIdArray;
        if (partnerIds.contains(",")) {
            partnerIdArray = partnerIds.split(",");
        }else{
            partnerIdArray = new String[]{partnerIds};
        }
        Result result = partnerFacade.addShopPartner(partnerIdArray, shopId, userId);
        return result;
    }

}
