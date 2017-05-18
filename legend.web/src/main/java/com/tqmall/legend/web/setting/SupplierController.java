package com.tqmall.legend.web.setting;

import com.tqmall.common.UserInfo;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.enums.supplier.InvoiceTypeEnum;
import com.tqmall.itemcenter.object.enums.supplier.PayMethodEnum;
import com.tqmall.itemcenter.object.enums.supplier.SupplierCategoryEnum;
import com.tqmall.itemcenter.object.param.supplier.SupplierParam;
import com.tqmall.itemcenter.object.param.supplier.SupplierQuery;
import com.tqmall.itemcenter.object.result.supplier.SupplierDTO;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.entity.base.BaseEnumBo;
import com.tqmall.legend.enums.base.ModuleUrlEnum;
import com.tqmall.legend.facade.supplier.SupplierFacade;
import com.tqmall.legend.web.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sven on 16/11/28.
 */
@Controller
@Slf4j
@RequestMapping("shop/setting/supplier")
public class SupplierController extends BaseController {
    @Resource
    private SupplierFacade supplierFacade;

    /**
     * 公用--获取供应商分类
     *
     * @return
     */
    @RequestMapping(value = "get_supplier_category", method = RequestMethod.GET)
    @ResponseBody
    public Result getSupplierCategory() {
        List<BaseEnumBo> baseEnumBoList = new ArrayList<>();
        for (SupplierCategoryEnum categoryEnum : SupplierCategoryEnum.values()) {
            BaseEnumBo baseEnumBo = new BaseEnumBo();
            baseEnumBo.setCode(categoryEnum.getCode());
            baseEnumBo.setName(categoryEnum.getMessage());
            baseEnumBoList.add(baseEnumBo);
        }
        return Result.wrapSuccessfulResult(baseEnumBoList);
    }

    /**
     * 公用--获取付款方式
     *
     * @return
     */
    @RequestMapping(value = "get_pay_method", method = RequestMethod.GET)
    @ResponseBody
    public Result getPayMethod() {
        List<BaseEnumBo> baseEnumBoList = new ArrayList<>();
        for (PayMethodEnum payMethodEnum : PayMethodEnum.values()) {
            BaseEnumBo baseEnumBo = new BaseEnumBo();
            baseEnumBo.setCode(payMethodEnum.getCode());
            baseEnumBo.setName(payMethodEnum.getMessage());
            baseEnumBoList.add(baseEnumBo);
        }
        return Result.wrapSuccessfulResult(baseEnumBoList);
    }

    /**
     * 公用-- 获取发票类型
     *
     * @return
     */
    @RequestMapping(value = "get_invoice_type", method = RequestMethod.GET)
    @ResponseBody
    public Result getInvoiceType() {
        List<BaseEnumBo> baseEnumBoList = new ArrayList<>();
        for (InvoiceTypeEnum invoiceTypeEnum : InvoiceTypeEnum.values()) {
            BaseEnumBo baseEnumBo = new BaseEnumBo();
            baseEnumBo.setCode(invoiceTypeEnum.getCode());
            baseEnumBo.setName(invoiceTypeEnum.getMessage());
            baseEnumBoList.add(baseEnumBo);
        }
        return Result.wrapSuccessfulResult(baseEnumBoList);
    }

    @ModelAttribute("moduleUrl")
    public String menu() {
        return ModuleUrlEnum.SETTINGS.getModuleUrl();
    }

    /**
     * 公用---获取供应商
     *
     * @return
     */
    @RequestMapping("get_supplier_list")
    @ResponseBody
    public Result<List<SupplierDTO>> getSupplierList() {
        Long shopId = UserUtils.getShopIdForSession(request);
        List<SupplierDTO> supplierList = supplierFacade.selectByShopId(shopId);
        return Result.wrapSuccessfulResult(supplierList);
    }

    /**
     * 获取供应商编号
     *
     * @return
     */
    @RequestMapping(value = "get_supplier_sn", method = RequestMethod.GET)
    @ResponseBody
    public Result supplierSn() {
        Long shopId = UserUtils.getShopIdForSession(request);
        String supplierSn = supplierFacade.getSupplierSn(shopId);
        return Result.wrapSuccessfulResult(supplierSn);
    }

    /**
     * 获取供应商详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "supplier-detail", method = RequestMethod.GET)
    @ResponseBody
    public Result supplierDetail(@RequestParam(value = "id") Long id) {
        Long shopId = UserUtils.getShopIdForSession(request);
        SupplierDTO supplier = supplierFacade.selectDetail(shopId, id);
        return Result.wrapSuccessfulResult(supplier);
    }

    /**
     * 供应商编辑页跳转
     *
     * @return
     */
    @RequestMapping(value = "supplier-edit", method = RequestMethod.GET)
    public String supplierEdit(@RequestParam(value = "id", required = false) Long id, Model model) {
        Long shopId = UserUtils.getShopIdForSession(request);
        if (id != null) {
            SupplierDTO supplier = supplierFacade.selectDetail(shopId, id);
            model.addAttribute("supplier", supplier);
        } else {
            String supplierSn = supplierFacade.getSupplierSn(shopId);
            model.addAttribute("supplierSn", supplierSn);
        }
        return "yqx/page/setting/supplier/supplier-edit";
    }

    /**
     * 保存供应商
     *
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Result<Long> save(@RequestBody SupplierParam supplierParam) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        supplierParam.setShopId(userInfo.getShopId());
        supplierParam.setCreator(userInfo.getUserId());
        supplierParam.setModifier(userInfo.getUserId());
        Long id = supplierFacade.saveOrUpdate(supplierParam);
        return Result.wrapSuccessfulResult(id);
    }

    /**
     * 供应商列表
     *
     * @return
     */

    @RequestMapping(value = "supplier-list", method = RequestMethod.GET)
    public String supplierList() {
        return "yqx/page/setting/supplier/supplier-list";
    }

    /**
     * 供应商列表数据初始化
     *
     * @param supplierQuery
     * @return
     */
    @RequestMapping(value = "supplier-list/data", method = RequestMethod.GET)
    @ResponseBody
    public Result<DefaultPage<SupplierDTO>> supplierListData(SupplierQuery supplierQuery) {
        Long shopId = UserUtils.getShopIdForSession(request);
        supplierQuery.setShopId(shopId);
        DefaultPage<SupplierDTO> defaultPage = supplierFacade.select(supplierQuery);
        return Result.wrapSuccessfulResult(defaultPage);
    }

    /**
     * 删除供应商
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> supplierDelete(@RequestParam("id") Long id) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        supplierFacade.delete(userInfo, id);
        return Result.wrapSuccessfulResult(true);
    }

    /**
     * 合并供应商
     *
     * @param requestId
     * @param resultId
     * @return
     */
    @RequestMapping(value = "merge", method = RequestMethod.POST)
    @ResponseBody
    public Result<Boolean> supplierMerge(@RequestParam("requestId") Long requestId,
                                         @RequestParam("resultId") Long resultId) {
        UserInfo userInfo = UserUtils.getUserInfo(request);
        supplierFacade.mergeSupplier(requestId, resultId, userInfo);
        return Result.wrapSuccessfulResult(true);
    }
}
