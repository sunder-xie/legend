package com.tqmall.legend.server.shop;

import com.tqmall.common.Constants;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.shop.ShopApplyRecordService;
import com.tqmall.legend.biz.shop.ShopService;
import com.tqmall.legend.biz.shop.ShopWxpayConfigService;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.entity.shop.ShopApplyRecord;
import com.tqmall.legend.entity.shop.ShopApplyStatusEnum;
import com.tqmall.legend.entity.shop.ShopWxpayConfig;
import com.tqmall.legend.object.param.shop.ApplyRecordSearchParam;
import com.tqmall.legend.object.param.shop.ShopWxpayConfigSaveParam;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.object.result.shop.ShopApplyRecordDTO;
import com.tqmall.legend.object.result.shop.ShopWxpayConfigDTO;
import com.tqmall.legend.service.shop.RpcShopApplyRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feilong.li on 16/10/17.
 */
@Service("rpcShopApplyRecordService")
@Slf4j
public class RpcShopApplyRecordServiceImpl implements RpcShopApplyRecordService {

    @Autowired
    private ShopApplyRecordService shopApplyRecordService;

    @Autowired
    private ShopWxpayConfigService shopWxpayConfigService;

    @Autowired
    private ShopService shopService;

    /**
     * 获取门店支付申请数量
     *
     * @return
     */
    @Override
    public Result<Integer> getShopApplyRecordCount() {
//        if (null == type) {
//            log.info("[门店支付申请记录数] 参数错误, type:{}", type);
//            return Result.wrapErrorResult("", "参数错误");
//        }
        Integer count = shopApplyRecordService.selectCount(null);
        if (null == count) {
            count = 0;
        }
        return Result.wrapSuccessfulResult(count);
    }

    /**
     * 获取门店支付申请列表
     *
     * @param applyRecordSearchParam
     * @return
     */
    @Override
    public Result<PageEntityDTO<ShopApplyRecordDTO>> getShopApplyRecordList(ApplyRecordSearchParam applyRecordSearchParam) {
        if (null == applyRecordSearchParam) {
            log.info("[门店支付申请记录] 获取申请记录列表失败,查询参数为空 applyRecordSearchParam==null");
            return Result.wrapErrorResult("", "参数错误");
        }
        log.info("[门店支付申请记录] 获取门店在线支付申请记录列表,[参数]applyRecordSearchParam:{}", applyRecordSearchParam);
        Map<String, Object> searchParams = new HashMap<>();
        //参数组装
        buildApplyRecordSearchParam(false, searchParams, applyRecordSearchParam);

        Integer totalCount = shopApplyRecordService.selectCount(searchParams);
        buildApplyRecordSearchParam(true, searchParams, applyRecordSearchParam);
        //查询列表
        List<ShopApplyRecord> shopApplyRecordList = shopApplyRecordService.select(searchParams);
        List<ShopApplyRecordDTO> shopApplyRecordDTOList = new ArrayList<>();
        //返回DTO组装
        if (!CollectionUtils.isEmpty(shopApplyRecordList)) {
            for (ShopApplyRecord shopApplyRecord : shopApplyRecordList) {
                ShopApplyRecordDTO shopApplyRecordDTO = new ShopApplyRecordDTO();
                BeanUtils.copyProperties(shopApplyRecord, shopApplyRecordDTO);
                shopApplyRecordDTOList.add(shopApplyRecordDTO);
            }
        }
        PageEntityDTO<ShopApplyRecordDTO> page = new PageEntityDTO<>();
        Integer pageNum = applyRecordSearchParam.getPageNum();
        totalCount = totalCount == null ? 0 : totalCount;
        pageNum = pageNum == null ? 1 : pageNum < 1 ? 1 : pageNum;
        page.setPageNum(pageNum);
        page.setTotalNum(totalCount);
        page.setRecordList(shopApplyRecordDTOList);
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 组装DB查询参数
     *
     * @param isSetPage              true: 设置分页参数; false:设置查询参数
     * @param searchParams           DB查询Map
     * @param applyRecordSearchParam sam查询入参
     */
    private void buildApplyRecordSearchParam(boolean isSetPage, Map<String, Object> searchParams, ApplyRecordSearchParam applyRecordSearchParam) {
        if (isSetPage) {
            //分页
            Integer pageNum = applyRecordSearchParam.getPageNum();
            Integer pageSize = applyRecordSearchParam.getPageSize();
            pageNum = pageNum == null ? 1 : pageNum < 1 ? 1 : pageNum;
            pageSize = pageSize == null ? 1 : pageSize < 1 ? 1 : pageSize > Constants.MAX_PAGE_SIZE ? Constants.MAX_PAGE_SIZE : pageSize;
            Integer offset = (pageNum - 1) * pageSize;
            Integer limit = pageSize;
            searchParams.put("offset", offset);
            searchParams.put("limit", limit);
        } else {
            //门店名称 - 模糊查询
            if (StringUtils.isNotBlank(applyRecordSearchParam.getShopName())) {
                searchParams.put("shopNameLike", applyRecordSearchParam.getShopName());
            }
            //申请状态
            if (null != applyRecordSearchParam.getApplyStatus()) {
                searchParams.put("applyStatus", applyRecordSearchParam.getApplyStatus());
            }
            //申请微信支付模式
            if (null != applyRecordSearchParam.getApplyWxMode()) {
                searchParams.put("applyWxMode", applyRecordSearchParam.getApplyWxMode());
            }
            //排序
            searchParams.put("sorts", new String[]{"id asc"});
        }
    }

    /**
     * 获取门店支付申请配置详情
     *
     * @param ucShopId      UC用户id
     * @param applyRecordId 申请记录id
     * @return
     */
    @Override
    public Result<ShopWxpayConfigDTO> getShopWxpayConfigDetail(Long ucShopId, Long applyRecordId) {
        log.info("[门店支付申请记录详情], 根据记录id获取申请记录详情,【参数】ucShopId:{}, applyRecordId:{}", ucShopId, applyRecordId);
        if (ucShopId == null || ucShopId < 1) {
            log.info("[门店支付申请记录详情], 根据记录id获取申请记录详情失败,参数错误【参数】ucShopId:{}", ucShopId);
            return Result.wrapErrorResult("", "参数错误");
        }
        ShopWxpayConfigDTO shopWxpayConfigDTO = null;

        Map<String, Object> shopParams = new HashMap<>();
        shopParams.put("userGlobalId", ucShopId + "");
        List<Shop> shopList = shopService.select(shopParams);
        if (!CollectionUtils.isEmpty(shopList)) {
            Shop shop = shopList.get(0);
            Long shopId = shop.getId();

            Map<String, Object> applyParams = new HashMap<>();
            Map<String, Object> configParams = new HashMap<>();

            applyParams.put("shopId", shopId);
            configParams.put("shopId", shopId);

            if (null != applyRecordId && applyRecordId > 0) {
                applyParams.put("id", applyRecordId);
                configParams.put("applyRecordId", applyRecordId);
            }
            //获取支付申请记录
            List<ShopApplyRecord> shopApplyRecordList = shopApplyRecordService.select(applyParams);
            if (!CollectionUtils.isEmpty(shopApplyRecordList)) {
                shopWxpayConfigDTO = new ShopWxpayConfigDTO();
                ShopApplyRecord shopApplyRecord = shopApplyRecordList.get(0);
                shopWxpayConfigDTO.setApplyStatus(shopApplyRecord.getApplyStatus());
            }
            //获取支付配置详情记录
            List<ShopWxpayConfig> shopWxpayConfigList = shopWxpayConfigService.select(configParams);
            if (!CollectionUtils.isEmpty(shopWxpayConfigList)) {
                ShopWxpayConfig shopWxpayConfig = shopWxpayConfigList.get(0);
                if (null == shopWxpayConfigDTO) {
                    shopWxpayConfigDTO = new ShopWxpayConfigDTO();
                }
                BeanUtils.copyProperties(shopWxpayConfig, shopWxpayConfigDTO);
            }
        }

        return Result.wrapSuccessfulResult(shopWxpayConfigDTO);
    }

    /**
     * 保存门店支付配置
     *
     * @param shopWxpayConfigSaveParam
     * @return
     */
    @Override
    public Result<String> saveShopWxpayConfig(ShopWxpayConfigSaveParam shopWxpayConfigSaveParam) {
        Result<String> checkResult = checkParams(shopWxpayConfigSaveParam);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        Map<String, Object> shopParams = new HashMap<>();
        Long userGlobalId = shopWxpayConfigSaveParam.getUcShopId();
        shopParams.put("userGlobalId", userGlobalId);

        List<Shop> shopList = shopService.select(shopParams);
        if (CollectionUtils.isEmpty(shopList)) {
            log.info("[门店支付配置保存] 保存失败, 该门店未开通云修系统,userGlobalId:{}", userGlobalId);
            return Result.wrapErrorResult("", "保存失败,门店未开通云修系统");
        } else if (shopList.size() > 1) {
            log.info("[门店支付配置保存] 保存失败, 该门店对应多个云修系统门店,userGlobalId:{}, shopList:{}", userGlobalId, shopList);
            return Result.wrapErrorResult("", "保存失败,该门店对应多个云修系统门店");
        }
        Long shopId = shopList.get(0).getId();

        Map<String, Object> applyParams = new HashMap<>();
        applyParams.put("shopId", shopId);
        List<ShopApplyRecord> shopApplyRecordList = shopApplyRecordService.select(applyParams);
        if (CollectionUtils.isEmpty(shopApplyRecordList)) {
            log.info("[门店支付配置保存] 保存失败, 该门店未申请开通在线支付,userGlobalId:{}", userGlobalId);
            return Result.wrapErrorResult("", "保存失败,该门店未申请开通在线支付");
        }
        Long applyRecordId = shopApplyRecordList.get(0).getId();
        Result result = null;
        try {
            result = shopWxpayConfigService.saveWxpayConfig(shopWxpayConfigSaveParam, shopId, applyRecordId);
        } catch (Exception e) {
            log.error("[门店支付配置保存] 保存异常,e:", e);
            return Result.wrapErrorResult("", "保存支付配置异常");
        }
        return result;
    }

    private Result<String> checkParams(ShopWxpayConfigSaveParam shopWxpayConfigSaveParam) {
        if (null == shopWxpayConfigSaveParam) {
            log.info("[门店支付配置保存] 保存失败,参数为null, shopWxpayConfigSaveParam==null");
            return Result.wrapErrorResult("", "保存参数为空");
        }
        log.info("[门店支付配置保存] 参数shopWxpayConfigSaveParam:{}", shopWxpayConfigSaveParam);
        if (null == shopWxpayConfigSaveParam.getUcShopId() || shopWxpayConfigSaveParam.getUcShopId() < 1) {
            log.info("[门店支付配置保存] 保存失败,参数错误, ucShopId:{}", shopWxpayConfigSaveParam.getUcShopId());
            return Result.wrapErrorResult("", "门店信息错误");
        }
        if (null == shopWxpayConfigSaveParam.getApplyStatus()
                || shopWxpayConfigSaveParam.getApplyStatus() < ShopApplyStatusEnum.SQZ.getCode()
                || shopWxpayConfigSaveParam.getApplyStatus() > ShopApplyStatusEnum.CSTG.getCode()) {
            log.info("[门店支付配置保存] 保存失败,申请状态错误, applyStatus:{}", shopWxpayConfigSaveParam.getApplyStatus());
            return Result.wrapErrorResult("", "门店申请状态错误");
        }
        if (null == shopWxpayConfigSaveParam.getPayMode() || (!shopWxpayConfigSaveParam.getPayMode().equals(0) && !shopWxpayConfigSaveParam.getPayMode().equals(1))) {
            log.info("[门店支付配置保存] 保存失败,受理模式错误, payMode:{}", shopWxpayConfigSaveParam.getPayMode());
            return Result.wrapErrorResult("", "配置受理模式错误");
        }
        if (StringUtils.isBlank(shopWxpayConfigSaveParam.getAppId())) {
            log.info("[门店支付配置保存] 保存失败,appId为空, appId:{}", shopWxpayConfigSaveParam.getAppId());
            return Result.wrapErrorResult("", "appId不能为空");
        }
        if (StringUtils.isBlank(shopWxpayConfigSaveParam.getMchId())) {
            log.info("[门店支付配置保存] 保存失败,appId为空, mchId:{}", shopWxpayConfigSaveParam.getMchId());
            return Result.wrapErrorResult("", "mchId不能为空");
        }
        //非受理模式 api密钥必填
        if (shopWxpayConfigSaveParam.getPayMode().equals(0) && StringUtils.isBlank(shopWxpayConfigSaveParam.getApiKey())) {
            log.info("[门店支付配置保存] 保存失败,非受理模式-apiKey不能为空, apiKey:{}", shopWxpayConfigSaveParam.getApiKey());
            return Result.wrapErrorResult("", "非受理模式apiKey不能为空");
        }
        return Result.wrapSuccessfulResult("");
    }

    /**
     * 查询门店支付申请状态是否已"测试通过"
     *
     * @param ucShopId
     * @return
     */
    @Override
    public Result<Boolean> getShopApplyStatusIsSuccess(Long ucShopId) {
        if (null == ucShopId || ucShopId < 1) {
            log.info("[门店支付申请是否已'测试通过'] 门店ucShopId参数错误, ucShopId:{}", ucShopId);
            return Result.wrapSuccessfulResult(false);
        }
        Map<String, Object> shopParams = new HashMap<>();
        shopParams.put("userGlobalId", ucShopId + "");
        List<Shop> shopList = shopService.select(shopParams);
        if (!CollectionUtils.isEmpty(shopList)) {
            if (shopList.size() > 1) {
                log.info("[门店支付申请是否已'测试通过'] 门店信息错误, 该ucShopId对应多个云修门店, ucShopId:{}; [DB结果]shopList:{}", ucShopId, shopList);
                return Result.wrapSuccessfulResult(false);
            }
            Shop shop = shopList.get(0);
            Map<String, Object> recordParams = new HashMap<>();
            recordParams.put("shopId", shop.getId());
            List<ShopApplyRecord> shopApplyRecordList = shopApplyRecordService.select(recordParams);
            if (!CollectionUtils.isEmpty(shopApplyRecordList)) {
                ShopApplyRecord shopApplyRecord = shopApplyRecordList.get(0);
                if (ShopApplyStatusEnum.CSTG.getCode().equals(shopApplyRecord.getApplyStatus())) {
                    log.info("[门店支付申请是否已'测试通过'] 门店支付申请已测试通过, id:{}, applyStatus:{}", shopApplyRecord.getId(), shopApplyRecord.getApplyStatus());
                    return Result.wrapSuccessfulResult(true);
                }
            }
        }

        return Result.wrapSuccessfulResult(false);
    }
}
