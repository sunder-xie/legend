package com.tqmall.legend.web.smart;

import com.google.common.base.Preconditions;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.insurance.domain.result.smart.SmartConsumeRecordDTO;
import com.tqmall.insurance.domain.result.smart.SmartRechargeRecordDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.facade.smart.BihuSmartViewFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by zwb on 16/12/21.
 */
@Slf4j
@Controller
@RequestMapping("smart/bihu/usedView")
public class BihuSmartUsedViewController extends BaseController {
    @Autowired
    private BihuSmartViewFacade bihuSmartViewFacade;


    /**
     * 跳转到充值列表页
     *
     * @return
     */
    @RequestMapping(value = "recharge-list", method = RequestMethod.GET)
    public String rechargeList() {

        return "yqx/page/smart/smart-used-view";
    }


    /**
     * 分页获取充值记录
     *
     * @return
     */
    @RequestMapping(value = "getRechargeList")
    @ResponseBody
    public com.tqmall.core.common.entity.Result getRechargeList(@PageableDefault(page = 1, size = 15, sort = "id", direction = Sort.Direction.DESC) final Pageable pageable) {

        return new ApiTemplate<DefaultPage<SmartRechargeRecordDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);
                Preconditions.checkArgument(StringUtils.isNotBlank(ucShopId), "门店userGlobalId为空");
            }

            @Override
            protected DefaultPage<SmartRechargeRecordDTO> process() throws BizException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);

                Integer start = (pageable.getPageNumber() - 1) * pageable.getPageSize();
                Integer limit = pageable.getPageSize();
                PagingResult<SmartRechargeRecordDTO> rechargeRecordPage = bihuSmartViewFacade.getRechargeRecordPage(Integer.valueOf(ucShopId), start, limit);
                Integer totalSize = rechargeRecordPage.getTotal();
                PageRequest pageRequest =
                        new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                                pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<SmartRechargeRecordDTO> page = new DefaultPage<>(rechargeRecordPage.getList(), pageRequest, totalSize);
                return page;
            }
        }.execute();
    }

    @RequestMapping("consume/listPage")
    public String listPage(Model model) {
        return "";
    }

    @RequestMapping("consume/list")
    @ResponseBody
    public com.tqmall.core.common.entity.Result getListFromBiHu(@PageableDefault(page = 1, size = 15, sort = "id", direction = Sort.Direction.DESC) final Pageable pageable) {
        return new ApiTemplate<DefaultPage<SmartConsumeRecordDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                String ucShopId = UserUtils.getUserGlobalIdForSession(request);
                Preconditions.checkArgument(StringUtils.isNotBlank(ucShopId), "门店userGlobalId为空");
            }

            @Override
            protected DefaultPage<SmartConsumeRecordDTO> process() throws BizException {
                String shopId = UserUtils.getUserGlobalIdForSession(request);
                PagingResult<SmartConsumeRecordDTO> pagingResult = bihuSmartViewFacade.getConsumeRecordPageList(pageable, Integer.parseInt(shopId));

                Integer totalSize = pagingResult.getTotal();
                PageRequest pageRequest =
                        new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                                pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
                DefaultPage<SmartConsumeRecordDTO> page = new DefaultPage<>(pagingResult.getList(), pageRequest, totalSize);
                return page;
            }
        }.execute();

    }
}
