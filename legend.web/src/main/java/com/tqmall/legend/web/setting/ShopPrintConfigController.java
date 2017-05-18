package com.tqmall.legend.web.setting;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.BdUtil;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.setting.ShopPrintConfigService;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.setting.PrintConfigField;
import com.tqmall.legend.entity.setting.ShopPrintConfig;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.facade.print.PrintFacade;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.legend.biz.setting.vo.ShopPrintConfigVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lilige on 16/11/3.
 */
@Controller
@Slf4j
@RequestMapping("shop/print-config")
public class ShopPrintConfigController extends BaseController {
    @Autowired
    ShopPrintConfigService shopPrintConfigService;
    @Autowired
    ShopConfigureService shopConfigureService;
    @Autowired
    ShopService shopService;
    @Autowired
    PrintFacade printFacade;

    /**
     * 打印设置跳页面
     * @return
     */
    @RequestMapping("")
    public String printConfigSetting(Model model, @RequestParam(value = "refer",required = false)String refer){
        Long shopId = UserUtils.getShopIdForSession(request);
        //获取门店对应的配置列表
        List<ShopPrintConfig> shopPrintConfigs = shopPrintConfigService.getShopPrintConfigs(shopId,null);
        //转换成VO对象
        List<ShopPrintConfigVO> voList = new ArrayList<>();
        try {
            for (ShopPrintConfig shopPrintConfig : shopPrintConfigs) {
                ShopPrintConfigVO vo = new ShopPrintConfigVO();
                BeanUtils.copyProperties(vo, shopPrintConfig);
                voList.add(vo);
            }
        } catch (Exception e) {
            log.error("[打印设置跳转到设置主页错误:配置对象转换成VO错误]shopId:{} ",shopId,e);
        }
        model.addAttribute("shopPrintConfigs",voList);
        model.addAttribute("moduleUrl","printConfig");
        if(StringUtils.isNotBlank(refer)){
            model.addAttribute("refer",refer);
        }
        return "yqx/page/setting/print-setting";
    }

    // 小票试打印
    @RequestMapping("try-receipt")
    public String tryPrintReceipt() {
        return "yqx/page/settlement/print/receipt-print-tpl";
    }

    /**
     * 修改开启状态
     * @param printTemplate
     * @return
     */
    @RequestMapping("chang-open-status")
    @ResponseBody
    public Result changeOpenStatue(Integer printTemplate){
        Long shopId = UserUtils.getShopIdForSession(request);
        try {
            shopPrintConfigService.changeOpenStatus(shopId, printTemplate);
            //刷新缓存
            shopPrintConfigService.cacheReload(shopId,request);
            return Result.wrapSuccessfulResult(true);
        } catch (BizException e) {
            log.error("[修改单据打印开启状态失败]:",e);
            return Result.wrapErrorResult("",e.getMessage());
        }
    }

    /**
     * 跳转到相应的设置页面
     * @param printTemplate 单据模版
     * @return
     */
    @RequestMapping("print-setting-detail")
    public String changePrintTemplate(Integer printTemplate , Model model){
        Long shopId = UserUtils.getShopIdForSession(request);

        Shop shop =  shopService.selectById(shopId);
        //获取门店的该单据
        ShopPrintConfig shopPrintConfig = shopPrintConfigService.getConfigByPrintTemplate(shopId,printTemplate);
        ShopPrintConfigVO shopPrintConfigVO = new ShopPrintConfigVO();
        try {
            BeanUtils.copyProperties(shopPrintConfigVO,shopPrintConfig);
        } catch (Exception e) {
            log.error("[打印设置,跳转到对应设置页面错误:配置对象转换成VO错误]shopId{},printTemplate{} ",shopId,printTemplate,e);
        }

        //打印自定义设置
        printFacade.printSelfConfig(model, shopId);

        model.addAttribute("moduleUrl", printTemplate);
        model.addAttribute("shop", shop);
        model.addAttribute("shopPrintConfig", shopPrintConfigVO);
        return "yqx/page/setting/print-setting-detail";
    }

    /**
     * 打印设置保存接口
     * @param shopPrintConfig
     * @return
     */
    @RequestMapping(value = "save",method = RequestMethod.POST)
    @ResponseBody
    public Result save( ShopPrintConfig shopPrintConfig){
        Long shopId = UserUtils.getShopIdForSession(request);
        try{
            shopPrintConfigService.addOrUpdate(shopPrintConfig,shopId);
            shopPrintConfigService.cacheReload(shopId,request);
            return Result.wrapSuccessfulResult(true);
        }catch (Exception e){
            log.error("[打印设置保存失败]:",e);
            return Result.wrapErrorResult("","保存失败");
        }
    }

    /**
     * 获取门店开启的单据
     * @return
     */
    @RequestMapping("shop-open-print")
    @ResponseBody
    public Result getShopOpenPrint(){
        List<ShopPrintConfig> configs = shopPrintConfigService.getShopOpenConfig(request);
        return Result.wrapSuccessfulResult(configs);
    }

    /**
     * 根据模版和版本获取打印配置
     * @param printTemplate 模版
     * @param printType 版本
     * @return
     */
    @RequestMapping("get-print-config")
    @ResponseBody
    public Result<ShopPrintConfigVO> getConfigByPrintTemplate(Integer printTemplate,Integer printType){
        Long shopId = UserUtils.getShopIdForSession(request);
        try {
            ShopPrintConfig shopPrintConfig = shopPrintConfigService.getConfigByPrintTemplate(shopId,printTemplate,printType);
            ShopPrintConfigVO vo = new ShopPrintConfigVO();
            if (printType != null &&  !printType.equals(shopPrintConfig.getPrintType())){
                shopPrintConfig.setPrintType(printType);
            }
            BdUtil.bo2do(shopPrintConfig, vo);
            return Result.wrapSuccessfulResult(vo);
        } catch (BizException e1) {
            log.error("[根据模版和版本获取模版打印配置失败]:",e1);
            return Result.wrapErrorResult("",e1.getMessage());
        } catch (Exception e2){
            log.error("[根据模版和版本获取模版打印配置失败]:shopId:{},printTemplate:{},printType:{}",shopId,printTemplate,printType,e2);
            return Result.wrapErrorResult("","获取打印设置失败");
        }
    }

    /**
     * 数据修复
     * @return
     */
    @RequestMapping("data-fix")
    @ResponseBody
    public Result dataFix(){
        Long shopId = UserUtils.getShopIdForSession(request);
        // 查询出最新的此类型的配置
        Map<String, Object> searchMap = new HashMap<>();
        //工单打印
        searchMap.put("confType", 0);
        List<ShopConfigure> orderList = shopConfigureService.select(searchMap);
        fix(orderList,0);
        //结算单打印
        searchMap.put("confType", 1);
        List<ShopConfigure> settleList = shopConfigureService.select(searchMap);
        fix(settleList,1);
        return Result.wrapSuccessfulResult(true);
    }

    private void fix(List<ShopConfigure> confList , Integer confType){
        if (CollectionUtils.isEmpty(confList)){
            return;
        }
        Map<Long,String> map = new HashMap<>();
        for (ShopConfigure configure : confList){
            if (null != configure.getShopId() && configure.getShopId() > 0){
                map.put(configure.getShopId(),configure.getConfValue());
            }
        }
        //模版
        Map<String, Object> param = new HashMap<>();
        param.put("shopId",0l);
        param.put("printTemplate",++confType);
        param.put("printType",2);
        ShopPrintConfig printTemplate = shopPrintConfigService.select(param).get(0);
        ArrayList<ShopPrintConfig> printConfigList = new ArrayList<>();
        try {
            ShopPrintConfigVO vo = new ShopPrintConfigVO();
            BeanUtils.copyProperties(vo, printTemplate);
            List<PrintConfigField> fieldVO = vo.getConfigFieldVO().getAfterFixVO();
            Iterator<Long> it = map.keySet().iterator();
            //循环门店设置提示语,并加入新增列表
            while (it.hasNext()){
                Long shopId = it.next();
                for (PrintConfigField configField : fieldVO) {
                    if ("reminder".equals(configField.getField())) {
                        configField.setExtValue(map.get(shopId));
                        break;
                    }
                }
                String configField = new Gson().toJson(vo.getConfigFieldVO());
                printTemplate.setConfigField(configField);
                printTemplate.setShopId(shopId);
                ShopPrintConfig temp = new ShopPrintConfig();
                BeanUtils.copyProperties(temp, printTemplate);
                printConfigList.add(temp);
            }
            shopPrintConfigService.batchInsert(printConfigList);
        } catch (Exception e) {
            log.error("门店修复数据错误",e);
        }
    }


}
