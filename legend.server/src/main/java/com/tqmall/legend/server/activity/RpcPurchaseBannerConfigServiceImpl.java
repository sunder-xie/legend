package com.tqmall.legend.server.activity;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.activity.PurchaseBannerConfigService;
import com.tqmall.legend.entity.activity.PurchaseBannerConfig;
import com.tqmall.legend.object.param.activity.PurchaseActivityConfigParam;
import com.tqmall.legend.object.param.activity.PurchaseBannerConfigParam;
import com.tqmall.legend.object.result.activity.PurchaseActivityConfigDTO;
import com.tqmall.legend.object.result.activity.PurchaseBannerConfigDTO;
import com.tqmall.legend.object.result.common.PageEntityDTO;
import com.tqmall.legend.server.activity.converter.PurchaseActivityConfigConverter;
import com.tqmall.legend.server.activity.converter.PurchaseActivityConfigDTOConverter;
import com.tqmall.legend.server.activity.converter.PurchaseBannerConfigConverter;
import com.tqmall.legend.server.activity.converter.PurchaseBannerConfigDTOConverter;
import com.tqmall.legend.service.activity.RpcPurchaseBannerConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by tanghao on 16/11/24.
 */
@Service("rpcPurchaseBannerConfigService")
public class RpcPurchaseBannerConfigServiceImpl implements RpcPurchaseBannerConfigService{

    @Autowired
    private PurchaseBannerConfigService purchaseBannerConfigService;

    @Override
    public Result<Boolean> insert(final PurchaseBannerConfigParam param) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(param,"插入对象不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                PurchaseBannerConfig config = new PurchaseBannerConfigConverter().convert(param);
                return purchaseBannerConfigService.insert(config)>0;
            }
        }.execute();

    }

    @Override
    public Result<Boolean> update(final PurchaseBannerConfigParam param) {
        return new ApiTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(param,"插入对象不能为空");
                Assert.notNull(param.getId(),"更新对象id不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                PurchaseBannerConfig config = new PurchaseBannerConfigConverter().convert(param);
                return purchaseBannerConfigService.update(config)>0;
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
                return purchaseBannerConfigService.deleteById(id)>0;
            }
        }.execute();
    }

    @Override
    public Result<PageEntityDTO<PurchaseBannerConfigDTO>> query(final PurchaseBannerConfigParam param) {
        return new ApiTemplate<PageEntityDTO<PurchaseBannerConfigDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected PageEntityDTO<PurchaseBannerConfigDTO> process() throws BizException {
                PurchaseBannerConfig config = new PurchaseBannerConfigConverter().convert(param);
                Integer totalSize = purchaseBannerConfigService.selectCount(config);
                List<PurchaseBannerConfig> purchaseBannerConfigs = purchaseBannerConfigService.query(config);
                List<PurchaseBannerConfigDTO> purchaseBannerConfigDTOs = new PurchaseBannerConfigDTOConverter().getList(purchaseBannerConfigs);
                PageEntityDTO<PurchaseBannerConfigDTO> resultPage = new PageEntityDTO<>();
                resultPage.setTotalNum(totalSize);
                resultPage.setRecordList(purchaseBannerConfigDTOs);
                resultPage.setPageNum(param.getPage());
                return resultPage;
            }
        }.execute();
    }

    @Override
    public Result<PurchaseBannerConfigDTO> queryById(final Long id) {
        return new ApiTemplate<PurchaseBannerConfigDTO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(id, "id不能为空.");
            }

            @Override
            protected PurchaseBannerConfigDTO process() throws BizException {
                PurchaseBannerConfig config = purchaseBannerConfigService.queryById(id);
                PurchaseBannerConfigDTO dto = new PurchaseBannerConfigDTOConverter().convert(config);
                return dto;
            }
        }.execute();
    }
}
