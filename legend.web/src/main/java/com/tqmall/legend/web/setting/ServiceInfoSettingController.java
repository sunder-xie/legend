package com.tqmall.legend.web.setting;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.enums.shopServiceInfo.ShopServiceCateTagEnum;
import com.tqmall.itemcenter.object.param.carLevel.CarLevelQryParam;
import com.tqmall.itemcenter.object.param.shopServiceInfo.ShopServiceCateSearchParam;
import com.tqmall.itemcenter.object.param.shopServiceInfo.ShopServiceInfoSearchParam;
import com.tqmall.itemcenter.object.result.carLevel.CarLevelDTO;
import com.tqmall.itemcenter.object.result.shopServiceInfo.ServiceGoodsSuiteDTO;
import com.tqmall.itemcenter.object.result.shopServiceInfo.ShopServiceCateDTO;
import com.tqmall.itemcenter.object.result.shopServiceInfo.ShopServiceInfoDTO;
import com.tqmall.itemcenter.service.carLevel.RpcCarLevelService;
import com.tqmall.itemcenter.service.shopServiceInfo.RpcShopServiceCateService;
import com.tqmall.itemcenter.service.shopServiceInfo.RpcShopServiceInfoService;
import com.tqmall.legend.annotation.HttpRequestLog;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.ShopServiceInfoConvertor;
import com.tqmall.legend.cache.SnFactory;
import com.tqmall.legend.entity.goods.SuiteGoods;
import com.tqmall.legend.entity.shop.ServiceGoodsSuite;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.entity.shop.ShopServiceInfoBo;
import com.tqmall.legend.enums.serviceInfo.ServiceInfoTypeEnum;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BeanParam;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wushuai on 17/01/06.
 * 门店服务资料
 */
@Controller
@RequestMapping("shop/setting/serviceInfo")
@Slf4j
public class ServiceInfoSettingController extends BaseController {

    @Autowired
    private RpcShopServiceCateService rpcShopServiceCateService;
    @Autowired
    private RpcCarLevelService rpcCarLevelService;
    @Autowired
    private RpcShopServiceInfoService rpcShopServiceInfoService;
    @Autowired
    private ShopServiceInfoFacade shopServiceInfoFacade;
    @Autowired
    private SnFactory snFactory;


    /**
     * 门店服务列表
     * @return
     */
    @HttpRequestLog
    @RequestMapping("serviceInfo-list")
    public String list(Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        //.服务类别
        Result<List<ShopServiceCateDTO>> serviceCateListResult = rpcShopServiceCateService.getShopServiceCate(shopId, null);
        model.addAttribute("shopServiceCateList", serviceCateListResult.getData());
        //.车辆级别
        CarLevelQryParam carLevelQryParam = new CarLevelQryParam();
        carLevelQryParam.setShopId(shopId);
        carLevelQryParam.setSize(Integer.MAX_VALUE);
        Result<List<CarLevelDTO>> carLevelListResult = rpcCarLevelService.selectByName(carLevelQryParam);
        model.addAttribute("carLevelList", carLevelListResult.getData());
        return "yqx/page/setting/serviceInfo/serviceInfo-list";
    }

    /**
     * 门店服务列表
     * @return
     */
    @RequestMapping("serviceCate-list")
    public String cateList() {
        return "yqx/page/setting/serviceInfo/serviceCate-list";
    }

    /**
     * 门店服务编辑页
     * @return
     */
    @RequestMapping("serviceInfo-edit")
    public String edit(@RequestParam(required = false) Long id,
                       @RequestParam(required = false, defaultValue = "0") Integer suiteNum,
                       Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        ShopServiceInfoDTO shopServiceInfoDTO = null;
        if (id != null) {
            Result<ShopServiceInfoDTO> shopServiceInfoDTOResult = rpcShopServiceInfoService.selectById(shopId, id);
            shopServiceInfoDTO = shopServiceInfoDTOResult.getData();
            if (shopServiceInfoDTO != null) {
                ServiceGoodsSuiteDTO serviceGoodsSuiteDTO = shopServiceInfoDTO.getServiceGoodsSuiteDTO();
                if (serviceGoodsSuiteDTO != null) {
                    String serviceInfo = serviceGoodsSuiteDTO.getServiceInfo();
                    if (StringUtils.isNotBlank(serviceInfo)) {
                        List<ShopServiceInfo> shopServiceInfoList = JSONUtil.jsonStr2List(serviceInfo, ShopServiceInfo.class);
                        model.addAttribute("shopServiceInfoList", shopServiceInfoList);
                    }
                    String goodsInfoListStr = serviceGoodsSuiteDTO.getGoodsInfo();
                    if (StringUtils.isNotBlank(goodsInfoListStr)) {
                        List<SuiteGoods> goodsList = JSONUtil.jsonStr2List(goodsInfoListStr, SuiteGoods.class);
                        model.addAttribute("goodsList", goodsList);
                    }
                }

                String thirdServiceInfo = shopServiceInfoDTO.getThirdServiceInfo();
                if (StringUtils.isNotBlank(thirdServiceInfo)) {
                    List<HashMap> thirdServiceInfoList = new Gson().fromJson(thirdServiceInfo, new TypeToken<List<HashMap>>() {
                    }.getType());
                    model.addAttribute("thirdServiceInfoList", thirdServiceInfoList);
                }
            }
        } else {
            shopServiceInfoDTO = new ShopServiceInfoDTO();
            shopServiceInfoDTO.setServiceSn(snFactory.generateSn(SnFactory.SERVICE_SN_INCREMENT, shopId, SnFactory.SERVICE));
            shopServiceInfoDTO.setSuiteNum(suiteNum.longValue());
        }
        model.addAttribute("shopServiceInfoDTO", shopServiceInfoDTO);
        return "yqx/page/setting/serviceInfo/serviceInfo-edit";
    }

    /**
     * 门店服务费用编辑页
     * @return
     */
    @RequestMapping("serviceInfo-fee-edit")
    public String feeEdit(@RequestParam(required = false) Long id,Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        ShopServiceInfoDTO shopServiceInfoDTO = null;
        if (id == null) {
            shopServiceInfoDTO = new ShopServiceInfoDTO();
            shopServiceInfoDTO.setServiceSn(snFactory.generateSn(SnFactory.FEE_SN_INCREMENT, shopId, SnFactory.FEE));
        } else {
            shopServiceInfoDTO = rpcShopServiceInfoService.selectById(shopId, id).getData();
        }
        model.addAttribute("shopServiceInfo", shopServiceInfoDTO);
        return "yqx/page/setting/serviceInfo/serviceInfo-fee-edit";
    }

    /**
     * 服务列表查询
     * @return
     */
    @RequestMapping(value = "getServiceInfoList", method = RequestMethod.GET)
    @ResponseBody
    public Result<Page<ShopServiceInfoDTO>> getList(final @BeanParam ShopServiceInfoSearchParam shopServiceInfoSearchParam) {
        return new ApiTemplate<Page<ShopServiceInfoDTO>>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected Page<ShopServiceInfoDTO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                shopServiceInfoSearchParam.setShopId(shopId);
                PagingResult<ShopServiceInfoDTO> result = rpcShopServiceInfoService.getShopServiceInfosFromSearch(shopServiceInfoSearchParam);
                logger.info("[dubbo][门店服务资料列表查询],查询条件shopServiceInfoSearchParam:{},success:{},message:{}", LogUtils.objectToString(shopServiceInfoSearchParam), result.isSuccess(), result.getMessage());
                if (!result.isSuccess()) {
                    throw new BizException(result.getMessage());
                }
                PageRequest pageRequest = new PageRequest(shopServiceInfoSearchParam.getPage(), shopServiceInfoSearchParam.getSize());
                DefaultPage<ShopServiceInfoDTO> page = new DefaultPage<>(result.getList(), pageRequest, result.getTotal());
                return page;
            }
        }.execute();
    }

    /**
     * 保存费用资料
     * @return
     */
    @RequestMapping(value = "saveShopFee", method = RequestMethod.POST)
    @ResponseBody
    public Result<ShopServiceInfoDTO> saveShopFee(final @RequestBody ShopServiceInfoBo shopServiceInfoBo) {
        return new ApiTemplate<ShopServiceInfoDTO>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopServiceInfoBo.getShopServiceInfo(),"服务资料不能为空");
            }

            @Override
            protected ShopServiceInfoDTO process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Long shopId = userInfo.getShopId();
                Long userId = userInfo.getUserId();
                ShopServiceInfo shopServiceInfo = shopServiceInfoBo.getShopServiceInfo();
                if(shopServiceInfo.getShopId()==null){
                    shopServiceInfo.setShopId(shopId);
                    shopServiceInfo.setType(ServiceInfoTypeEnum.OTHER_SERVICE.getCode());
                }
                if (shopServiceInfo.getModifier()==null) {
                    shopServiceInfo.setModifier(userId);
                }
                if (shopServiceInfo.getCreator()==null) {
                    shopServiceInfo.setCreator(userId);
                }
                ShopServiceInfoDTO shopServiceInfoDTO = ShopServiceInfoConvertor.convert(shopServiceInfoBo);
                Result<ShopServiceInfoDTO> result = rpcShopServiceInfoService.saveShopFee(shopServiceInfoDTO);
                logger.info("[dubbo][保存门店费用资料]shopServiceInfoDTO:{},success:{},message:{}", LogUtils.objectToString(shopServiceInfoDTO), result.isSuccess(), result.getMessage());
                if (!result.isSuccess()) {
                    throw new BizException(result.getMessage());
                }
                return result.getData();
            }
        }.execute();
    }

    /**
     * 服务服务资料(包括套餐)
     * @return
     */
    @RequestMapping(value = "saveShopServiceInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result<ShopServiceInfoDTO> saveShopServiceInfo(final @RequestBody ShopServiceInfoBo shopServiceInfoBo) {
        return new ApiTemplate<ShopServiceInfoDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopServiceInfoBo.getShopServiceInfo(),"服务资料不能为空");
            }

            @Override
            protected ShopServiceInfoDTO process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Long shopId = userInfo.getShopId();
                Long userId = userInfo.getUserId();
                ShopServiceInfo shopServiceInfo = shopServiceInfoBo.getShopServiceInfo();
                if(shopServiceInfo.getShopId()==null){
                    shopServiceInfo.setShopId(shopId);
                }
                if (shopServiceInfo.getModifier()==null) {
                    shopServiceInfo.setModifier(userId);
                }
                if (shopServiceInfo.getCreator()==null) {
                    shopServiceInfo.setCreator(userId);
                }
                ServiceGoodsSuite serviceGoodsSuite = shopServiceInfoBo.getServiceGoodsSuite();
                if (serviceGoodsSuite != null) {
                    if(serviceGoodsSuite.getShopId()==null){
                        serviceGoodsSuite.setShopId(shopId);
                    }
                    if (serviceGoodsSuite.getModifier() == null) {
                        serviceGoodsSuite.setModifier(userId);
                    }
                    if (serviceGoodsSuite.getCreator() == null) {
                        serviceGoodsSuite.setCreator(userId);
                    }
                }
                ShopServiceInfoDTO shopServiceInfoDTO = ShopServiceInfoConvertor.convert(shopServiceInfoBo);
                Result<ShopServiceInfoDTO> result = rpcShopServiceInfoService.saveShopServiceInfo(shopServiceInfoDTO);
                logger.info("[dubbo][保存门店服务资料]shopServiceInfoDTO:{},success:{},message:{}", LogUtils.objectToString(shopServiceInfoDTO), result.isSuccess(), result.getMessage());
                if (!result.isSuccess()) {
                    throw new BizException(result.getMessage());
                }
                return result.getData();
            }
        }.execute();
    }

    /**
     * 删除服务资料(包括套餐)
     * @return
     */
    @RequestMapping(value = "deleteShopServiceInfo", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> deleteShopServiceInfo(final @RequestParam Long id) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "服务ID不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                logger.info("[门店服务资料]删除服务,shopId:{},serviceId:{}", shopId, id);
                shopServiceInfoFacade.deleteByIdAndShopId(id, shopId);
                return true;
            }
        }.execute();
    }

    /**
     * 服务类别列表查询
     * @return
     */
    @RequestMapping(value = "getServiceCateList", method = RequestMethod.GET)
    @ResponseBody
    public Result<Page<ShopServiceCateDTO>> getServiceCateList(final @BeanParam ShopServiceCateSearchParam shopServiceCateSearchParam) {
        return new ApiTemplate<Page<ShopServiceCateDTO>>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected Page<ShopServiceCateDTO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                shopServiceCateSearchParam.setShopId(shopId);
                PagingResult<ShopServiceCateDTO> result = rpcShopServiceCateService.getZDYServiceCatePage(shopServiceCateSearchParam);
                logger.info("[dubbo][门店服务列表列表查询],查询条件shopServiceInfoSearchParam:{},success:{},message:{}", LogUtils.objectToString(shopServiceCateSearchParam), result.isSuccess(), result.getMessage());
                if (!result.isSuccess()) {
                    throw new BizException(result.getMessage());
                }
                PageRequest pageRequest = new PageRequest(shopServiceCateSearchParam.getPage(), shopServiceCateSearchParam.getSize());
                Page<ShopServiceCateDTO> page = new DefaultPage<>(result.getList(), pageRequest, result.getTotal());
                return page;
            }
        }.execute();
    }

    /**
     * 保存服务类别
     * @return
     */
    @RequestMapping(value = "saveShopServiceCate", method = RequestMethod.POST)
    @ResponseBody
    public Result<ShopServiceCateDTO> saveShopServiceCate(final @RequestBody ShopServiceCateDTO shopServiceCateDTO) {
        return new ApiTemplate<ShopServiceCateDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopServiceCateDTO, "服务类别信息不能为空");
            }

            @Override
            protected ShopServiceCateDTO process() throws BizException {
                UserInfo userInfo = UserUtils.getUserInfo(request);
                Long shopId = userInfo.getShopId();
                Long userId = userInfo.getUserId();
                if (shopServiceCateDTO.getShopId() == null) {
                    shopServiceCateDTO.setShopId(shopId);
                }
                if (shopServiceCateDTO.getModifier() == null) {
                    shopServiceCateDTO.setModifier(userId);
                }
                if (shopServiceCateDTO.getCreator() == null) {
                    shopServiceCateDTO.setCreator(userId);
                }
                if(shopServiceCateDTO.getCateTag()==null){
                    shopServiceCateDTO.setCateTag(ShopServiceCateTagEnum.QT.getValue());//自定义的类别标其他类目
                }
                Result<ShopServiceCateDTO> result = rpcShopServiceCateService.save(shopServiceCateDTO);
                logger.info("[dubbo][保存门店服务类别]shopServiceInfoDTO:{},success:{},message:{}", LogUtils.objectToString(shopServiceCateDTO), result.isSuccess(), result.getMessage());
                if (!result.isSuccess()) {
                    throw new BizException(result.getMessage());
                }
                return result.getData();
            }
        }.execute();
    }

    /**
     * 删除服务资料(包括套餐)
     * @return
     */
    @RequestMapping(value = "deleteShopServiceCate", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> deleteShopServiceCate(final @RequestParam Long id) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "服务类别ID不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                logger.info("[门店服务资料]删除服务,shopId:{},serviceId:{}", shopId, id);
                shopServiceInfoFacade.deleteShopServiceCate(id, shopId);
                return true;
            }
        }.execute();
    }

}
