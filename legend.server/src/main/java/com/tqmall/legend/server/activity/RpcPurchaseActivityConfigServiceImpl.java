package com.tqmall.legend.server.activity;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.activity.PurchaseActivityConfigService;
import com.tqmall.legend.entity.activity.PurchaseActivityConfig;
import com.tqmall.legend.object.param.activity.PurchaseActivityConfigParam;
import com.tqmall.legend.object.result.activity.PurchaseActivityConfigDTO;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.server.activity.converter.PurchaseActivityConfigConverter;
import com.tqmall.legend.server.activity.converter.PurchaseActivityConfigDTOConverter;
import com.tqmall.legend.service.activity.RpcPurchaseActivityConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by tanghao on 16/11/24.
 */
@Service("rpcPurchaseActivityConfigService")
@Slf4j
public class RpcPurchaseActivityConfigServiceImpl implements RpcPurchaseActivityConfigService{
    @Autowired
    private PurchaseActivityConfigService purchaseActivityConfigService;

    @Override
    public Result<Boolean> insert(final PurchaseActivityConfigParam param) {
       return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(param,"插入对象不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                PurchaseActivityConfig config = new PurchaseActivityConfigConverter().convert(param);
                return purchaseActivityConfigService.insert(config)>0;
            }
        }.execute();

    }

    @Override
    public Result<Boolean> update(final PurchaseActivityConfigParam param) {
       return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(param,"插入对象不能为空");
                Assert.notNull(param.getId(),"更新对象id不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                PurchaseActivityConfig config = new PurchaseActivityConfigConverter().convert(param);
                return purchaseActivityConfigService.update(config)>0;
            }
        }.execute();
    }

    @Override
    public Result<Boolean> delete(final Long id) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id,"要删除id不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                return purchaseActivityConfigService.deleteById(id)>0;
            }
        }.execute();
    }

    @Override
    public Result<PageEntityDTO<PurchaseActivityConfigDTO>> query(final PurchaseActivityConfigParam param) {
        return new ApiTemplate<PageEntityDTO<PurchaseActivityConfigDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected PageEntityDTO<PurchaseActivityConfigDTO> process() throws BizException {
                PurchaseActivityConfig config = new PurchaseActivityConfigConverter().convert(param);
                Integer totalSize = purchaseActivityConfigService.selectCount(config);
                List<PurchaseActivityConfig> purchaseActivityConfigs = purchaseActivityConfigService.query(config);
                List<PurchaseActivityConfigDTO> purchaseActivityConfigDTOs = new PurchaseActivityConfigDTOConverter().getList(purchaseActivityConfigs);
                PageEntityDTO<PurchaseActivityConfigDTO> result = new PageEntityDTO<PurchaseActivityConfigDTO>();
                result.setPageNum(param.getPage());
                result.setRecordList(purchaseActivityConfigDTOs);
                result.setTotalNum(totalSize);
                return result;
            }
        }.execute();
    }

    @Override
    public Result<PurchaseActivityConfigDTO> queryById(Long id) {
        Assert.notNull(id,"id不能为空.");
        PurchaseActivityConfig purchaseActivityConfig = purchaseActivityConfigService.queryById(id);
        PurchaseActivityConfigDTO dto = new PurchaseActivityConfigDTOConverter().convert(purchaseActivityConfig);
        return Result.wrapSuccessfulResult(dto);
    }

    @Override
    public Result<List<PurchaseActivityConfigDTO>> queryAllList() {
        List<PurchaseActivityConfig> list = purchaseActivityConfigService.getAllActivity();
        List<PurchaseActivityConfigDTO> purchaseActivityConfigDTOs = new PurchaseActivityConfigDTOConverter().getList(list);
        return Result.wrapSuccessfulResult(purchaseActivityConfigDTOs);
    }


}
