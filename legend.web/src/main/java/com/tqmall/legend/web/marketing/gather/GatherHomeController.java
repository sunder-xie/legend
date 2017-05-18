package com.tqmall.legend.web.marketing.gather;

import com.tqmall.common.UserInfo;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.legend.cache.JedisClient;
import com.tqmall.legend.web.common.BaseController;
import com.tqmall.wheel.lang.Langs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wanghui on 14/12/2016.
 */
@Controller
@RequestMapping("/marketing/gather")
public class GatherHomeController extends BaseController {

    @Autowired
    private JedisClient jedisClient;

    @RequestMapping
    public String index(Model model) {
        String key = "GATHER _USERID";
        UserInfo userInfo = UserUtils.getUserInfo(request);
        Long userId = userInfo.getUserId();
        Integer isAdmin = userInfo.getUserIsAdmin();
        String value = (String) jedisClient.hget(key, "userId_" + userId);
        if (value == null) {
            jedisClient.hset(key, "userId_" + userId, "1");
            return "yqx/page/marketing/gather/index";
        } else {
            if (isAdmin == 1) {
                model.addAttribute("subModule", "gather-import");
                return "redirect:/marketing/gather/import-page";
            } else {
                return "redirect:/marketing/gather/rule";
            }
        }

    }

    @ModelAttribute("moduleUrl")
    public String menu() {
        return "marketing";
    }

    @RequestMapping(value = "import-page", method = RequestMethod.GET)
    public String importPage(Model model) {
        model.addAttribute("subModule", "gather-import");
        return "yqx/page/marketing/gather/import-page";
    }

}
