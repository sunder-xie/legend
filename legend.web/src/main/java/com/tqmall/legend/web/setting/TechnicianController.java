package com.tqmall.legend.web.setting;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.biz.component.CacheComponent;
import com.tqmall.legend.biz.component.CacheKeyConstant;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.privilege.TechnicianService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.*;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.enums.setting.EducationEnum;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 *  设置改版后的新 技师认证
 * Created by lilige on 17/1/5.
 */
@Controller
@RequestMapping("shop/setting/technician")
public class TechnicianController extends BaseController {
    @Autowired
    private TechnicianService technicianService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private CacheComponent cacheComponent;

    @RequestMapping("/technician-detail")
    public String getTechnician(Model model){
        UserInfo user = UserUtils.getUserInfo(request);
        Long shopId = user.getShopId();
        Long managerId = user.getUserId();
        Result<Technician> technician = technicianService.getTechnician(shopId, managerId);
        ShopManager shopManager = shopManagerService.selectByShopIdAndManagerIdDB(shopId, managerId);
        if (null != technician){
            model.addAttribute("technician",technician.getData());
        }
        if (null != shopManager){
            model.addAttribute("shopManager",shopManager);
        }
        TechnicianSeniorityEnum[] seniorityEnums = TechnicianSeniorityEnum.getMessages();
        model.addAttribute("seniorityEnum", seniorityEnums);
        TechnicianLevelEnum[] levelEnums = TechnicianLevelEnum.getMessages();
        model.addAttribute("levelEnum", levelEnums);
        model.addAttribute("educationEnum", EducationEnum.values());
        return "yqx/page/setting/technician/technician-detail";
    }

    @RequestMapping(value = "save",method = RequestMethod.POST)
    @ResponseBody
    public Result saveTechnician(@RequestBody TechnicianBO technicianBO ,HttpServletRequest request){
        Long shopId = UserUtils.getShopIdForSession(request);
        Long managerId = UserUtils.getUserIdForSession(request);
        ShopManager shopManager = technicianBO.getShopManager();
        Technician technician = technicianBO.getTechnician();
        technician.setManagerId(managerId);
        technician.setShopId(shopId);
        technician.setModifier(managerId);
        Result result = technicianService.saveTechnician(technician,shopManager);
        if (result.isSuccess()){
            cacheComponent.reload(CacheKeyConstant.PICKING);
            return Result.wrapSuccessfulResult(true);
        }
        return Result.wrapErrorResult(result.getCode(),result.getErrorMsg());
    }
}
