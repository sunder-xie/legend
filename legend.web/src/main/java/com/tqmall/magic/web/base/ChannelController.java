package com.tqmall.magic.web.base;

import com.tqmall.common.UserInfo;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.facade.magic.ChannelFacade;
import com.tqmall.legend.enums.magic.ChannelTypeEnum;
import com.tqmall.legend.facade.magic.vo.ChannelVO;
import com.tqmall.legend.facade.magic.vo.ShareStatusVO;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by macx on 16/5/11.
 */
@Controller
@Slf4j
@RequestMapping("share/channel")
public class ChannelController extends BaseController{

    @Autowired
    private ChannelFacade channelFacade;

    /**
     * 进入渠道管理页面
     * @return
     */
    @RequestMapping("index")
    public String index(Model model){
        model.addAttribute("leftNav","channel");
        return "yqx/page/magic/partner/channel";
    }

    /**
     * 获取渠道商类型
     * @return
     */
    @RequestMapping("getChannelTypes")
    @ResponseBody
    public Result getChannelTypes(){
        List<ShareStatusVO> shareStatusVOs = new ArrayList<>();
        ChannelTypeEnum[] channelTypeEna = ChannelTypeEnum.values();
        for (ChannelTypeEnum channelTypeEnum : channelTypeEna) {
            ShareStatusVO shareStatusVO = new ShareStatusVO();
            shareStatusVO.setCode(channelTypeEnum.getCode());
            shareStatusVO.setName(channelTypeEnum.getName());
            shareStatusVOs.add(shareStatusVO);
        }
        return Result.wrapSuccessfulResult(shareStatusVOs);
    }

    /**
     * 获取渠道商列表
     * @param pageable
     * @param channelName
     * @return
     */
    @RequestMapping("getChannelPage")
    @ResponseBody
    public Result getChannelPage(@PageableDefault(page = 1, value = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,@RequestParam(value = "channelName",required = false) String channelName){
        log.info("[共享中心] 获取渠道商分页列表,参数:pageNum={};pageSize={};channelName={}", pageable.getPageNumber(), pageable.getPageSize(), channelName);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopId == null){
            log.error("[共享中心] 获取渠道商分页列表,登录信息错误 shopId={}",shopId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(),LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        Result result = channelFacade.getChannelPage(pageable, shopId, channelName);
        return result;
    }

    /**
     * 获取渠道商信息
     * @param channelId
     * @return
     */
    @RequestMapping("getChannelInfo")
    @ResponseBody
    public Result getChannelInfo(Long channelId){
        log.info("[共享中心] 获取渠道商信息,参数: channelId={}",channelId);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopId == null){
            log.error("[共享中心] 获取渠道商信息,登录信息错误 shopId={}",shopId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        if (channelId == null) {
            log.error("[共享中心] 获取渠道商信息,channelId为空");
            return Result.wrapErrorResult(LegendErrorCode.SHARE_CHANNEL_ID_NULL_ERROR.getCode(), LegendErrorCode.SHARE_CHANNEL_ID_NULL_ERROR.getErrorMessage());
        }
        Result result = channelFacade.getChannelInfo(channelId, shopId);
        return result;
    }

    /**
     * 保存渠道商信息
     * @param channelVO
     * @return
     */
    @RequestMapping(value = "saveChannelInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result saveChannelInfo(@RequestBody ChannelVO channelVO) {
        log.info("[共享中心] 保存渠道商信息,参数 channelVO={}",channelVO);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopId == null){
            log.error("[共享中心] 保存渠道商信息,登录信息错误 shopId={}",shopId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        if (StringUtils.isBlank(channelVO.getChannelType()) || (!ChannelTypeEnum.INSURANCE.getName().equals(channelVO.getChannelType()) &&
            !ChannelTypeEnum.PERSONAL.getName().equals(channelVO.getChannelType()) && !ChannelTypeEnum.UNTQMALL.getName().equals(channelVO.getChannelType()) &&
                !ChannelTypeEnum.TQMALL.getName().equals(channelVO.getChannelType()) && !ChannelTypeEnum.PARTNER.getName().equals(channelVO.getChannelType()))){
            log.error("[共享中心] 保存渠道商信息,渠道商类型错误 channelType = {}",channelVO.getChannelType());
            return Result.wrapErrorResult(LegendErrorCode.SHARE_CHANNEL_PARAM_ERROR.getCode(), "渠道商类型错误");
        }
        if (StringUtils.isBlank(channelVO.getChannelName())) {
            log.error("[共享中心] 保存渠道商信息,渠道商名称不能为空");
            return Result.wrapErrorResult(LegendErrorCode.SHARE_CHANNEL_PARAM_ERROR.getCode(), "渠道商名称不能为空");
        }
        if (StringUtils.isNotBlank(channelVO.getMobile()) && !StringUtil.isMobileNO(channelVO.getMobile()) && !StringUtil.isPhone(channelVO.getMobile())) {
            log.error("[共享中心] 保存渠道商信息,手机号码格式错误 mobile={}", channelVO.getMobile());
            return Result.wrapErrorResult(LegendErrorCode.SHARE_MOBILE_STYLE_ERROR.getCode(), LegendErrorCode.SHARE_MOBILE_STYLE_ERROR.getErrorMessage());
        }
        Result result = channelFacade.saveChannelInfo(channelVO, shopId);
        return result;
    }

    /**
     * 删除渠道商信息
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteChannelInfo",method = RequestMethod.POST)
    @ResponseBody
    public Result deleteChannelInfo(Long id){
        log.error("[共享中心] 删除渠道商信息,登录信息错误 id={}",id);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long shopId = userInfo.getShopId();
        if (shopId == null){
            log.error("[共享中心] 删除渠道商信息,登录信息错误 shopId={}",shopId);
            return Result.wrapErrorResult(LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getCode(), LegendErrorCode.SHARE_LOGIN_INFO_ERROR.getErrorMessage());
        }
        if (id == null) {
            log.error("[共享中心] 删除渠道商,id为空");
            return Result.wrapErrorResult(LegendErrorCode.SHARE_CHANNEL_ID_NULL_ERROR.getCode(), LegendErrorCode.SHARE_CHANNEL_ID_NULL_ERROR.getErrorMessage());
        }
        Result result = channelFacade.deleteChannelInfo(id, shopId);
        return result;
    }

}
