package com.tqmall.legend.web.service;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.result.shopServiceInfo.AppServiceCateDTO;
import com.tqmall.itemcenter.object.result.shopServiceInfo.ShopServiceCateDTO;
import com.tqmall.itemcenter.service.shopServiceInfo.RpcShopServiceCateService;
import com.tqmall.legend.biz.shop.ShopServiceCateService;
import com.tqmall.legend.web.common.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 门店服务控制器
 * Created by zwb on 14/10/29.
 */
@Controller
@RequestMapping("shop/shop_service_cate")
public class ShopServiceCateController extends BaseController {

    Logger logger = LoggerFactory.getLogger(ShopServiceCateController.class);

    @Autowired
    ShopServiceCateService shopServiceCateService;
    @Autowired
    private RpcShopServiceCateService rpcShopServiceCateService;

    @RequestMapping("get_by_name")
    @ResponseBody
    public Result<List<ShopServiceCateDTO>> getByName(@RequestParam(value = "name", required = false) final String name) {
        return new ApiTemplate<List<ShopServiceCateDTO>>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<ShopServiceCateDTO> process() throws BizException {
                long shopId = UserUtils.getShopIdForSession(request);
                com.tqmall.core.common.entity.Result<List<ShopServiceCateDTO>> serviceCateListResult = rpcShopServiceCateService.getShopServiceCate(shopId, name);
                logger.info("[dubbo-itemcenter][根据名称查询服务类别]shopId:{},name:{},success:{},message:{}", shopId, name, serviceCateListResult.isSuccess(), serviceCateListResult.getMessage());
                if(!serviceCateListResult.isSuccess()){
                    throw new BizException(serviceCateListResult.getMessage());
                }
                return serviceCateListResult.getData();
            }
        }.execute();
    }

    /**
     * 获得车主service的类别 create by jason 2015-07-10
     *
     * @param name
     * @return result
     */
    @Deprecated
    @RequestMapping("appCateName")
    @ResponseBody
    public Result<List<AppServiceCateDTO>> getAppServiceCateByName(@RequestParam(value = "name", required = false) final String name,
                                                                   @RequestParam(value = "appCateId", required = false) final Long appCateId) {
        return new ApiTemplate<List<AppServiceCateDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<AppServiceCateDTO> process() throws BizException {
                Result<List<AppServiceCateDTO>> appServiceCateDTOListResult = rpcShopServiceCateService.getAppServiceCate(appCateId, name);
                logger.info("[dubbo-itemcenter][查询车主服务类别]appCateId:{},name:{},success:{},message:{}",appCateId,name,appServiceCateDTOListResult.isSuccess(),appServiceCateDTOListResult.getMessage());
                if(!appServiceCateDTOListResult.isSuccess()){
                    throw new BizException(appServiceCateDTOListResult.getMessage());
                }
                List<AppServiceCateDTO> appServiceCateDTOList = appServiceCateDTOListResult.getData();
                if (!CollectionUtils.isEmpty(appServiceCateDTOList)) {
                    for (AppServiceCateDTO appServiceCateDTO : appServiceCateDTOList) {
                        String firstCateName = appServiceCateDTO.getFirstCateName();//一级服务类目名称
                        String secondCateName = appServiceCateDTO.getName();//二级服务类目名称
                        appServiceCateDTO.setName(firstCateName+'-'+secondCateName);
                    }
                }
                return appServiceCateDTOListResult.getData();
            }
        }.execute();
    }
}
