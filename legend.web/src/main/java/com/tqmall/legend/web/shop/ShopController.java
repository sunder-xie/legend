package com.tqmall.legend.web.shop;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.Shop;

/**
 * Created by zwb on 14/10/29.
 */
@Controller
@RequestMapping("user/shop")
public class ShopController {
    @Autowired ShopService shopService;

    @RequestMapping(value = "updateCity")
    @ResponseBody
    public Result updateCity(@RequestParam("cityId") Long changeCityId,@RequestParam("cityName") String changeCityName,HttpServletRequest request) {
        Shop shop = new Shop();
        long shopId= UserUtils.getShopIdForSession(request);
        long modifier = UserUtils.getUserIdForSession(request);
        shop.setId(shopId);
        shop.setModifier(modifier);
        shop.setChangeCityId(changeCityId);
        shop.setChangeCityName(changeCityName);
        return shopService.updateCity(shop);
    }


}
