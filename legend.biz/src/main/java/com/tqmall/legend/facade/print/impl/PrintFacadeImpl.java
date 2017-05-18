package com.tqmall.legend.facade.print.impl;

import com.google.common.base.Optional;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.entity.shop.ShopConfigure;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.facade.print.PrintFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**
 * Created by lixiao on 17/3/22.
 */
@Slf4j
@Service
public class PrintFacadeImpl implements PrintFacade {

    @Autowired
    private ShopConfigureService shopConfigureService;

    @Override
    public void printSelfConfig(Model model, Long shopId) {
        //printLogo和下面的方式不并存
        Optional<ShopConfigure> printLogoOpt =  shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.PRINT_LOGO, "print_logo", shopId);
        if(printLogoOpt.isPresent()){
            String printLogo = printLogoOpt.get().getConfValue();
            if(!StringUtils.isBlank(printLogo)){
                model.addAttribute("printLogo", printLogo);
            }
        }else{
            Optional<ShopConfigure> printTitleOpt =  shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.PRINT_TITLE, "print_title", shopId);
            if(printTitleOpt.isPresent()){
                String printTitle = printTitleOpt.get().getConfValue();
                if(!StringUtils.isBlank(printTitle)){
                    model.addAttribute("printTitle", printTitle);
                }
            }
            Optional<ShopConfigure> printPostionOpt =  shopConfigureService.getShopConfigure(ShopConfigureTypeEnum.PRINT_POSTION, "print_postion", shopId);
            if(printPostionOpt.isPresent()){
                String printPostion = printPostionOpt.get().getConfValue();
                if(!StringUtils.isBlank(printPostion)){
                    model.addAttribute("printPostion", printPostion);
                }
            }
        }


    }
}
