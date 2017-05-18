package com.tqmall.legend.web.insurance.address;

import com.tqmall.common.UserInfo;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.insurance.InsuranceFormReceiveAddressFacade;
import com.tqmall.legend.facade.insurance.vo.InsuranceFormReceiveAddressVo;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * Created by zhouheng on 17/4/26.
 */
@Controller
@RequestMapping("insurance/anxin/address")
public class FormReceiveAddressController  extends BaseController{

    @Autowired
    private InsuranceFormReceiveAddressFacade formReceiveAddressFacade;

    @Autowired
    private ShopService shopService;

    /**
     * 新增地址保单接收地址信息
     *
     * @param addressVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "create",method = RequestMethod.POST)
    public Result<Void> createFormReceiveAddress(InsuranceFormReceiveAddressVo addressVo){

        Long shopId = UserUtils.getShopIdForSession(request);
        String ucShopId = UserUtils.getUserGlobalIdForSession(request);
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Shop shop = shopService.selectById(shopId);
        addressVo.setAgentId(Integer.valueOf(ucShopId));
        addressVo.setCreator(Integer.valueOf(userInfo.getUserGlobalId()));
        addressVo.setAgentName(shop.getName());
        addressVo.setGmtCreate(new Date());
        formReceiveAddressFacade.createFormReceiveAddress(addressVo);

        return Result.wrapSuccessfulResult(null);
    }

    /**
     * 删除地址保单接收地址信息
     *
     * @param addressId
     * @return
     */
    @ResponseBody
    @RequestMapping("delete")
    public Result<Void> deleteFormReceiveAddress(Integer addressId){

        UserInfo userInfo = UserUtils.getUserInfo(request);

        formReceiveAddressFacade.deleteFormReceiveAddress(addressId, Integer.valueOf(userInfo.getUserGlobalId()));

        return Result.wrapSuccessfulResult(null);
    }

    /**
     * 更新地址保单接收地址信息
     *
     * @param addressVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "update",method = RequestMethod.POST)
    public Result<Void> updateFormReceiveAddress(InsuranceFormReceiveAddressVo addressVo){

        UserInfo userInfo = UserUtils.getUserInfo(request);

        addressVo.setModifier(Integer.valueOf(userInfo.getUserGlobalId()));

        addressVo.setGmtModified(new Date());
        formReceiveAddressFacade.updateFormReceiveAddress(addressVo);
        return Result.wrapSuccessfulResult(null);

    }

    /**
     * 获取地址保单接收地址信息列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("getFormReceiveAddressList")
    public Result<List<InsuranceFormReceiveAddressVo>> getFormReceiveAddressList(){

        String ucShopId = UserUtils.getUserGlobalIdForSession(request);

        List<InsuranceFormReceiveAddressVo> list = formReceiveAddressFacade.getFormReceiveAddressList(Integer.valueOf(ucShopId));

        return Result.wrapSuccessfulResult(list);
    }

    /**
     * 通过id获取收保单地址信息
     *
     * @param addressId
     * @return
     */
    @ResponseBody
    @RequestMapping("getFormReceiveAddressById")
    public Result<InsuranceFormReceiveAddressVo> getFormReceiveAddressById(Integer addressId){

        InsuranceFormReceiveAddressVo  addressVo = formReceiveAddressFacade.getFormReceiveAddressById(addressId);

        return Result.wrapSuccessfulResult(addressVo);

    }


}
