package com.tqmall.legend.web.service;


import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.exception.BusinessCheckedException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.HttpUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.enums.shopServiceInfo.ShopServiceFieldEnum;
import com.tqmall.legend.biz.bo.dandelion.TaoqiCouponParam;
import com.tqmall.legend.biz.bo.order.WashCarBo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.shop.*;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.goods.SuiteGoods;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.*;
import com.tqmall.legend.facade.service.ShopServiceInfoFacade;
import com.tqmall.legend.object.param.service.ServiceInfoParam;
import com.tqmall.legend.object.result.service.ShopServiceInfoDTO;
import com.tqmall.legend.service.service.RpcShopServiceInfoService;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.web.utils.ServletUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
import java.util.*;

/**
 * 门店服务控制器 Created by zwb on 14/10/29.
 */
@Controller
@RequestMapping("shop/shop_service_info")
public class ShopServiceInfoController extends BaseController {

	public static final Logger logger = LoggerFactory.getLogger(ShopServiceInfoController.class);
	@Autowired
	ShopServiceInfoService shopServiceInfoService;
	@Autowired
	ServiceGoodsSuiteService serviceGoodsSuiteService;
	@Autowired
	CarLevelService carLevelService;
	@Autowired
	GoodsService goodsService;
	@Autowired
	GiftService giftService;
	@Autowired
	ShopService shopService;
	@Autowired
	ShopManagerService shopManagerService;
	@Autowired
	ShopServiceCateService shopServiceCateService;
	@Autowired
	private ShopServiceInfoFacade shopServiceInfoFacade;
	@Autowired
	private RpcShopServiceInfoService rpcShopServiceInfoService;
	@Autowired
	private com.tqmall.itemcenter.service.shopServiceInfo.RpcShopServiceInfoService itemcenterShopServiceInfoService;

	//蒲公英项目URL
	@Value("${dandelion.url}")
	private String dandelionUrl;

	/**
	 * 获取门店服务
	 *
	 * @param serviceId 门店服务ID
	 * @return
	 */
	@RequestMapping(value = "shopservice/get", method = RequestMethod.GET)
	@ResponseBody
	public Result getShopService(@RequestParam(value = "serviceid", required = true) Long serviceId,
									 HttpServletRequest request) {

		Optional<ShopServiceInfo> serviceInfoOptional = shopServiceInfoService.get(serviceId);
		if (!serviceInfoOptional.isPresent()) {
			logger.error("门店服务不存在 服务编号ID:{}", serviceId);
			return Result.wrapErrorResult("", "门店服务不存在");
		}

		return Result.wrapSuccessfulResult(serviceInfoOptional.get());
	}

	@RequestMapping(value = "get_suit_goods")
	@ResponseBody
	public Result getSuitGoods(Model model, @RequestParam(value = "id", required = true) Long id) {

		Long shopId = UserUtils.getShopIdForSession(request);

		ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteService.selectByServiceId(id, shopId);
		Object goodslist = null;
		model.addAttribute("serviceGoodsSuite", serviceGoodsSuite);
		if (serviceGoodsSuite != null) {
			String goodsInfoListStr = serviceGoodsSuite.getGoodsInfo();
			goodslist = new Gson().fromJson(goodsInfoListStr, new TypeToken<List<SuiteGoods>>() {
			}.getType());

		} else {
			goodslist = ListUtils.EMPTY_LIST;
		}

		return Result.wrapSuccessfulResult(goodslist);
	}

	/**
	 * get shopService recommendAppService
	 * <p/>
	 * TODO encapsulation common elasticService
	 *
	 * @param request
	 * @param serviceSn   服务编号
	 * @param serviceName 服务名称
	 * @param type        服务类型 {1：基本服务；2：附加服务}
	 * @param suiteNumLT  {0:单个服务;1:为带配件服务;2:服务大套餐}
	 * @return json
	 */
	@RequestMapping(value = "/json", method = RequestMethod.GET)
	@ResponseBody
	public String json(HttpServletRequest request,
					   @RequestParam(value = "serviceSn", required = false) String serviceSn,
					   @RequestParam(value = "serviceName", required = false) String serviceName,
					   @RequestParam(value = "type", required = true) String type,
					   @RequestParam(value = "suiteNumLT", required = false) String suiteNumLT,
					   @RequestParam(value = "categoryId", required = false) Long categoryId) {

		Long shopId = UserUtils.getShopIdForSession(request);
		String json = shopServiceInfoFacade.getShopService(serviceSn, serviceName, type, suiteNumLT, categoryId, shopId);
		return json;
	}

	/**
	 * by twg
	 *
	 * @param request
	 * @param serviceName 服务名称
	 * @param type        服务类型 {1：基本服务；2：附加服务}
	 * @return json
	 */
	@RequestMapping(value = "/serviceInfo", method = RequestMethod.GET)
	@ResponseBody
	public Result serviceInfo(HttpServletRequest request, @RequestParam(value = "serviceSn", required = false) String serviceSn, @RequestParam(value = "serviceName", required = false) String serviceName, @RequestParam(value = "type") String type, @RequestParam(value = "suiteNumLT", required = false) String suiteNumLT) {
		long shopId = UserUtils.getShopIdForSession(request);
		// TODO 抽取方法
		Map<String, Object> reqParamMap = Maps.newHashMap();
		reqParamMap.put("shopId", shopId);
		reqParamMap.put("type", type);
		reqParamMap.put("offset", 0);
		reqParamMap.put("limit", 5);
		if (StringUtils.isNotEmpty(serviceName)) {
			reqParamMap.put("serviceNameLike", serviceName);
		}
		List<ShopServiceInfo> list = shopServiceInfoService.select(reqParamMap);

		return Result.wrapSuccessfulResult(list);

	}


	/**
	 * 服务套餐
	 *
	 * @param serviceId
	 * @return
	 */
	@RequestMapping("getPackageByServiceId")
	@ResponseBody
	public Result getPackageByServiceId(Long serviceId) {
		Long shopId = UserUtils.getShopIdForSession(request);
		ShopServiceInfo shopServiceInfo = shopServiceInfoService.selectById(serviceId, shopId);

		PackageVo packageVo = new PackageVo();

		if (shopServiceInfo != null) {
			ServiceGoodsSuite serviceGoodsSuite = serviceGoodsSuiteService.selectByServiceId(serviceId, shopId);
			packageVo.setShopServiceInfo(shopServiceInfo);
			String goodsInfo = serviceGoodsSuite.getGoodsInfo();
			String serviceInfo = serviceGoodsSuite.getServiceInfo();

			List<Goods> goodsMapList = new Gson().fromJson(goodsInfo, new TypeToken<List<Goods>>() {
			}.getType());
			List<Goods> goodsList = new ArrayList<>();
			// 存放goodsId
			StringBuffer goodsIdSb = new StringBuffer();
			if (!CollectionUtils.isEmpty(goodsMapList)) {
				for (int i = 0; i < goodsMapList.size(); i++) {
					Long goodsId = goodsMapList.get(i).getId();
					goodsIdSb.append(goodsId).append(",");
					Optional<Goods> goodsOptional = goodsService.selectById(goodsId, shopId);
					if (goodsOptional.isPresent()) {
						Goods goods = goodsOptional.get();
						goods.setGoodsNum(goodsMapList.get(i).getGoodsNum());
						goodsList.add(goods);
					}

				}
				packageVo.setGoodsList(goodsList);
			}

			List<ShopServiceInfo> shopServiceInfoList = new ArrayList<>();

			if (shopServiceInfo.getType() == 1 && shopServiceInfo.getSuiteNum() == 1) {

				Map cateMap = new HashMap();
				cateMap.put("shopId", shopId);
				cateMap.put("id", shopServiceInfo.getCategoryId());
				ShopServiceCate shopServiceCate = shopServiceCateService.selectById(shopServiceInfo.getCategoryId());
				if (shopServiceCate != null) {
					shopServiceInfo.setServiceCatName(shopServiceCate.getName());
				}
				shopServiceInfo.setServiceNum(1L);
				// 获得服务对应goodsId
				if (StringUtil.isNotStringEmpty(goodsIdSb.toString())) {
					shopServiceInfo.setGoodsIdStr((goodsIdSb.deleteCharAt(goodsIdSb.length() - 1)).toString());// 截掉最后一位逗号
				}
				shopServiceInfoList.add(shopServiceInfo);
				packageVo.setShopServiceInfoList(shopServiceInfoList);
				return Result.wrapSuccessfulResult(packageVo);

			} else if (shopServiceInfo.getType() == 1 && shopServiceInfo.getSuiteNum() == 2) {
				List<ShopServiceInfo> shopServiceMapList = new Gson().fromJson(serviceInfo, new TypeToken<List<ShopServiceInfo>>() {
				}.getType());
				if (!CollectionUtils.isEmpty(shopServiceMapList)) {
					for (int j = 0; j < shopServiceMapList.size(); j++) {
						Long shopServiceId = shopServiceMapList.get(j).getId();
						ShopServiceInfo shopServiceInfo1 = shopServiceInfoService.selectById(shopServiceId, shopId);
						if (shopServiceInfo1 != null) {

							// 获得服务对应的goodsId 为了前端删除服务的时候联动删除配件
							ServiceGoodsSuite serviceGoodsSuite1 = serviceGoodsSuiteService.selectByServiceId(shopServiceId, shopId);
							if (null != serviceGoodsSuite1) {
								String goodsInfo1 = serviceGoodsSuite1.getGoodsInfo();
								List<Goods> goodsMapList1 = new Gson().fromJson(goodsInfo1, new TypeToken<List<Goods>>() {
								}.getType());

								if (!CollectionUtils.isEmpty(goodsMapList1)) {
									StringBuffer goodsIdSb1 = new StringBuffer();
									for (int i = 0; i < goodsMapList1.size(); i++) {
										Long goodsId1 = goodsMapList1.get(i).getId();
										goodsIdSb1.append(goodsId1).append(",");
									}
									if (StringUtil.isNotStringEmpty(goodsIdSb1.toString())) {
										shopServiceInfo1.setGoodsIdStr((goodsIdSb1.deleteCharAt(goodsIdSb1.length() - 1)).toString());
									}
								}
							}
							if (shopServiceInfo1.getCategoryId() != null) {
								ShopServiceCate shopServiceCate1 = shopServiceCateService.selectById(shopServiceInfo1.getCategoryId());
								if (shopServiceCate1 != null) {
									shopServiceInfo1.setServiceCatName(shopServiceCate1.getName());
								}
							}
							shopServiceInfo1.setServiceNum(shopServiceMapList.get(j).getServiceNum());
							shopServiceInfoList.add(shopServiceInfo1);
						}
					}
				}
				packageVo.setShopServiceInfoList(shopServiceInfoList);
			}
			return Result.wrapSuccessfulResult(packageVo);
		}
		return Result.wrapErrorResult("", "服务不存在");
	}

	/**
	 * 获取门店服务基本信息
	 *
	 * @param serviceId
	 * @return
	 */
	@RequestMapping("getShopService")
	@ResponseBody
	public Result getShopService(Long serviceId) {
		Long shopId = UserUtils.getShopIdForSession(request);
		ShopServiceInfo shopServiceInfo = shopServiceInfoService.selectById(serviceId, shopId);

		if (shopServiceInfo == null) {
			return Result.wrapErrorResult("", "服务不存在");
		}
		ShopServiceCate shopServiceCate = shopServiceCateService.selectById(shopServiceInfo.getCategoryId());
		if (shopServiceCate != null) {
			shopServiceInfo.setServiceCatName(shopServiceCate.getName());
		}
		return Result.wrapSuccessfulResult(shopServiceInfo);
	}

	@RequestMapping("checkServiceName")
	@ResponseBody
	public com.tqmall.core.common.entity.Result<Boolean> checkServiceName(
			@RequestParam(value = "serviceName", required = true) final String serviceName) {
		return new ApiTemplate<Boolean>(){
			@Override
			protected void checkParams() throws IllegalArgumentException {
				Assert.notNull(serviceName,"服务名称不能为空");
			}

			@Override
			protected Boolean process() throws BizException {
				Long shopId = UserUtils.getShopIdForSession(request);
				com.tqmall.core.common.entity.Result<com.tqmall.itemcenter.object.result.shopServiceInfo.ShopServiceInfoDTO> shopServiceInfoDTOResult
						= itemcenterShopServiceInfoService.checkShopService(shopId, ShopServiceFieldEnum.NAME,serviceName);
				logger.info("[dubbo-itemcenter]查询服务是否已存在,shopId:{},fieldName:{},serviceName:{},success:{},message:{}",
						shopId,ShopServiceFieldEnum.NAME.getFieldName(),shopServiceInfoDTOResult.isSuccess(),shopServiceInfoDTOResult.getMessage());
				if (!shopServiceInfoDTOResult.isSuccess()) {
					throw new BizException(shopServiceInfoDTOResult.getMessage());
				}
				if (shopServiceInfoDTOResult.getData() == null) {
					return true;//true表示可以添加
				}
				return false;//false表示不可以添加
			}
		}.execute();
	}

	@RequestMapping("checkServiceSn")
	@ResponseBody
	public com.tqmall.core.common.entity.Result<Boolean> checkServiceSn(@RequestParam(value = "serviceSn", required = true) final String serviceSn) {
		return new ApiTemplate<Boolean>(){
			@Override
			protected void checkParams() throws IllegalArgumentException {
				Assert.notNull(serviceSn,"serviceSn不能为空");
			}

			@Override
			protected Boolean process() throws BizException {
				Long shopId = UserUtils.getShopIdForSession(request);
				com.tqmall.core.common.entity.Result<com.tqmall.itemcenter.object.result.shopServiceInfo.ShopServiceInfoDTO> shopServiceInfoDTOResult
						= itemcenterShopServiceInfoService.checkShopService(shopId, ShopServiceFieldEnum.SERVICE_SN,serviceSn);
				logger.info("[dubbo-itemcenter]查询服务是否已存在,shopId:{},fieldName:{},serviceName:{},success:{},message:{}",
						shopId,ShopServiceFieldEnum.NAME.getFieldName(),shopServiceInfoDTOResult.isSuccess(),shopServiceInfoDTOResult.getMessage());
				if (!shopServiceInfoDTOResult.isSuccess()) {
					throw new BizException(shopServiceInfoDTOResult.getMessage());
				}
				if (shopServiceInfoDTOResult.getData() == null) {
					return true;//true表示可以添加
				}
				return false;//false表示不可以添加
			}
		}.execute();
	}

    /**
     * TODO 待重构
     * <p/>
     * 添加或编辑服务 重载
     *
     * @param shopServiceInfo
     * @return ShopServiceInfo
     */
    @RequestMapping(value = "updateInOrder", method = RequestMethod.POST)
    @ResponseBody
    public com.tqmall.legend.common.Result updateInOrder(@BeanParam ShopServiceInfo shopServiceInfo) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        if (shopServiceInfo != null) {
            if (shopServiceInfo.getId() != null && shopServiceInfo.getId() > 0) {
                ShopServiceInfo shopServiceInfo1 = shopServiceInfoService.selectById(shopServiceInfo.getId());
                if (shopServiceInfo1 != null) {
                    shopServiceInfo.setModifier(userInfo.getUserId());
                    com.tqmall.legend.common.Result result = shopServiceInfoService.update(shopServiceInfo, userInfo);
                    return com.tqmall.legend.common.Result.wrapSuccessfulResult(result.getData());
                } else {
                    return com.tqmall.legend.common.Result.wrapErrorResult("", "操作失败");
                }
            } else {
				//服务名称校验
				shopServiceInfo.setShopId(userInfo.getShopId());
				shopServiceInfo.setCreator(userInfo.getUserId());
				ServiceInfoParam serviceInfoParam = new ServiceInfoParam();
				BeanUtils.copyProperties(shopServiceInfo, serviceInfoParam);
				com.tqmall.core.common.entity.Result<ShopServiceInfoDTO> insertResult = rpcShopServiceInfoService.insertCustomShopServiceInfo(serviceInfoParam);
				if (insertResult.isSuccess()) {
					return Result.wrapSuccessfulResult(insertResult.getData());
				} else {
					return Result.wrapErrorResult(insertResult.getCode(), insertResult.getMessage());
				}
			}
        } else {
            logger.error("操作失败");
            return com.tqmall.legend.common.Result.wrapErrorResult("", "操作失败");
        }
    }

	/**
	 * create by jason 2015-07-17 添加礼品发放功能页面
	 *
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "addGift")
	public String addGiftPage(Model model, @RequestParam(value = "id", required = false) Long id) {
		model.addAttribute("moduleUrl", "marketing");
		model.addAttribute("subModule","giftInfo");
		return "yqx/page/gift/gift-add";
	}

	/**
	 * create by jason 2015-07-17 新增添加礼品发放功能
	 *
	 * @param gift
	 * @return
	 */
	@RequestMapping(value = "add_gift_info")
	@ResponseBody
	public Result addGiftInfo(Gift gift, HttpServletRequest request) {
		UserInfo userInfo = UserUtils.getUserInfo(request);
		Long shopId = userInfo.getShopId();
		gift.setShopId(shopId);
		Long customerCarId = gift.getCustomerCarId();
		logger.info("添加礼品发放功能,礼品信息是:{}", gift);
		if (StringUtil.isStringEmpty(customerCarId + "")) {
			return Result.wrapErrorResult("-1", "请输入客户车辆信息!");
		} else if (StringUtil.isStringEmpty(gift.getGiftSn())) {
			return Result.wrapErrorResult("-1", "请输入礼品券码信息!");
		} else if (StringUtil.isStringEmpty(gift.getRegistrantId() + "")) {
			return Result.wrapErrorResult("-1", "请输入礼品发放人信息!");
		}

		// 设置发放时间
		gift.setGmtCreate(new Date());
		gift.setGmtCreateStr(gift.getGmtCreateStr());

		// 调用接口确认礼品券是否存在
		Result result = validateGiftSn(gift);
		if (result.isSuccess()) {

			String giftContent = (String) result.getData();
			gift.setGiftContent(giftContent);// 礼品内容
			Result giftResult = giftService.add(gift);
			if (giftResult.isSuccess()) {
				// 通知2C-APP端 礼品信息保存成功,已经消费了该礼品券
				verifyGiftInfo(gift);
				return Result.wrapSuccessfulResult(gift.getGiftContent());
			} else {
				logger.info("保存礼品信息失败! 当前信息是:{}", giftResult.getErrorMsg());
				return Result.wrapErrorResult("-1", "保存礼品信息失败!");
			}
		} else {
			return Result.wrapErrorResult("-1", "礼品券核销不成功，请确认礼品券代码");
		}

	}

	/**
	 * 调用APP接口,验证礼品券码是否有效 create by jason 2015-07-30
	 *
	 * @param gift
	 */
	private Result validateGiftSn(Gift gift) {
		String giftSn = gift.getGiftSn();
		String mobile = gift.getMobile();
		String license = gift.getLicense();
		Map<String, Object> resultMap = null;
		String requestUrl = dandelionUrl + "coupon/checkGiftCoupon";

		try {
			Map map = new HashMap();
			map.put("couponCode", giftSn);
			map.put("mobile", mobile);
			map.put("license", license);
			String mapStr = new Gson().toJson(map);
			String resultStr = HttpUtil.sendPost(requestUrl, mapStr);
			// 根据返回值判断结果
			if (StringUtils.isBlank(resultStr)) {
				logger.warn("调用2cApp超时，当前数据信息giftSn为：" + giftSn);
				return Result.wrapErrorResult("-1", "调用2cApp超时");
			} else {
				resultMap = JSONUtil.getMapper().readValue(resultStr, Map.class);
				Boolean success = (Boolean) resultMap.get("success");
				if (success != null && success) {
					logger.info("调用2cApp接口成功，当前数据信息giftSn为：" + giftSn);
					String data = (String) resultMap.get("data");
					if (!StringUtils.isEmpty(data)) {
						return Result.wrapSuccessfulResult(data);
					} else {
						return Result.wrapErrorResult("-1", "礼品内容为空!");
					}
				} else {
					logger.error("调用2cApp接口失败，{}", LogUtils.funToString(mapStr, resultMap));
					return Result.wrapErrorResult("-1", "调用2cApp接口失败");

				}
			}
		} catch (Exception ioe) {
			logger.error("JSON转化出错！" + ioe);
			return Result.wrapErrorResult("-1", "JSON转化出错");
		}

	}

	/**
	 * 调用APP接口,核销礼品券 create by jason 2015-08-04
	 *
	 * @param gift
	 */
	private void verifyGiftInfo(Gift gift) {
		String giftSn = gift.getGiftSn();
		String mobile = gift.getMobile();
		String license = gift.getLicense();
		Long shopId = gift.getShopId();
		Long registrantId = gift.getRegistrantId();

		Shop shop = shopService.selectById(shopId);
		if (null == shop) {
			logger.error("店铺信息为空! 门店ID:{}", shopId);
		}
		ShopManager shopManager = shopManagerService.selectById(registrantId);
		if (null == shopManager) {
			logger.error("礼品发放人信息为空! 礼品发放人ID:{}", registrantId);
		}

		Map<String, Object> resultMap = null;
		String requestUrl = dandelionUrl + "coupon/settleGiftCoupon";
		try {
			TaoqiCouponParam couponParam = new TaoqiCouponParam();
			couponParam.setLicense(license);// 客户车牌
			couponParam.setCouponCode(giftSn);// 礼品券
			couponParam.setMobile(mobile);// 客户手机号
			couponParam.setShopId(shopId);// 门店ID
			couponParam.setShopPhone(shop.getTel());// 门店固话
			couponParam.setSaId(registrantId.toString());// 礼品发放人ID
			couponParam.setSaName(gift.getRegistrantName());// 礼品发放人
			couponParam.setSaPhone(shopManager.getMobile());// 礼品发放人手机号
			couponParam.setSettleTime(gift.getGmtCreateStr());// 礼品发放时间

			String paramStr = new Gson().toJson(couponParam);

			String resultStr = HttpUtil.sendPost(requestUrl, paramStr);
			// 根据返回值判断结果
			if (StringUtils.isBlank(resultStr)) {
				logger.warn("调用2cApp超时，当前数据信息为：{}", paramStr);
			} else {
				resultMap = JSONUtil.getMapper().readValue(resultStr, Map.class);
				Boolean success = (Boolean) resultMap.get("success");
				if (success != null && success) {
					logger.info("调用2cApp接口成功，当前数据信息为：{}", paramStr);
				} else {
					logger.error("调用2cApp接口失败，{}", LogUtils.funToString(paramStr, resultMap));
				}
			}
		} catch (Exception ioe) {
			logger.error("JSON转化出错！{}", ioe);
		}
	}

	/**
	 * create by jason 2015-08-03 获得礼品发放记录信息
	 *
	 * @return
	 */
	@RequestMapping(value = "get_gift_list")
	@ResponseBody
	public Result getGiftList(@PageableDefault(page = 1, value = 12) Pageable pageable, HttpServletRequest request) {

		Map<String, Object> searchParams = ServletUtils.getParametersMapStartWith(request);
		Long shopId = UserUtils.getShopIdForSession(request);
		searchParams.put("shopId", shopId);

		// 统一接口
		DefaultPage<Gift> page = (DefaultPage<Gift>) giftService.getPageGiftInfo(pageable, searchParams);

		page.setPageUri(request.getRequestURI());
		page.setSearchParam(ServletUtils.getParametersStringStartWith(request));

		return Result.wrapSuccessfulResult(page);

	}

	/**
	 * create by jason 2015-07-17 展示礼品发放信息页面
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "giftInfo")
	public String giftInfo(Model model) {
		model.addAttribute("moduleUrl", "marketing");
		model.addAttribute("subModule", "giftInfo");
		return "yqx/page/gift/gift-info";
	}

	/**
	 * 保存标准洗车服务列表
	 *
	 * @param washCarBo
	 * @return
	 */
	@RequestMapping(value = "washcar/save_service_list", method = RequestMethod.POST)
	@ResponseBody
	public Result saveServiceList(@RequestBody WashCarBo washCarBo) {
		try {
			if (washCarBo == null) {
				return Result.wrapErrorResult("10000", "提交数据不能为空");
			}
			long shopId = UserUtils.getShopIdForSession(request);

			if (shopId < 1) {
				return Result.wrapErrorResult("10000", "店铺信息错误");
			}
			List<ShopServiceInfo> shopServiceInfoList = washCarBo.getShopServiceInfoList();
			if (CollectionUtils.isEmpty(shopServiceInfoList)) {
				return Result.wrapErrorResult("10001", "服务列表不能为空");
			}
			return shopServiceInfoService.saveWashCarServiceList(shopServiceInfoList, shopId);
		} catch (Exception e) {
			logger.error("保存标准洗车服务价格列表异常", e);
			Result errorResult = Result.wrapErrorResult("10002", "保存标准洗车服务价格列表异常");
			return errorResult;
		}
	}
}
