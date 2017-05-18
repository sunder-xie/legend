package com.tqmall.legend.facade.marketing.gather.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.provider.marketing.gather.RpcGatherStatService;
import com.tqmall.cube.shop.result.marketing.gather.LaXinStatDTO;
import com.tqmall.cube.shop.result.marketing.gather.PanHuoStatDTO;
import com.tqmall.legend.facade.marketing.gather.GatherEffectFacade;
import com.tqmall.legend.facade.marketing.gather.adaptor.LaXinStatConvertor;
import com.tqmall.legend.facade.marketing.gather.adaptor.PanHuoStatConvertor;
import com.tqmall.legend.facade.marketing.gather.vo.LaXinStatVO;
import com.tqmall.legend.facade.marketing.gather.vo.PanHuoStatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xin on 2016/12/21.
 */
@Service
public class GatherEffectFacadeImpl implements GatherEffectFacade {
    @Autowired
    private RpcGatherStatService rpcGatherStatService;


    /**
     * 统计盘活效果
     *
     * @param shopId
     * @param userId
     * @return
     */
    @Override
    public List<PanHuoStatVO> getPanHuoStat(final Long shopId, final Long userId, final String dateStr) {

        return new BizTemplate<List<PanHuoStatVO>>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected List<PanHuoStatVO> process() throws BizException {
                Result<List<PanHuoStatDTO>> result = rpcGatherStatService.getPanHuoStat(shopId, userId, dateStr);
                if (result == null || !result.isSuccess()) {
                    throw new BizException("统计盘活效果错误");
                }
                return PanHuoStatConvertor.convertList(result.getData());
            }
        }.execute();
    }

    /**
     * 统计拉新效果
     *
     * @param shopId
     * @param userId
     * @return
     */
    @Override
    public LaXinStatVO getLaXinStat(final Long shopId, final Long userId, final String dateStr) {
        return new BizTemplate<LaXinStatVO>() {
            /**
             * 参数合法性检查 IllegalArgumentException
             */
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            /**
             * 主逻辑入口 抛出BizException类型的异常 在execute方法中进行处理
             *
             * @return
             * @throws BizException
             */
            @Override
            protected LaXinStatVO process() throws BizException {
                Result<LaXinStatDTO> result = rpcGatherStatService.getLaXinStat(shopId, userId, dateStr);
                if (result == null || !result.isSuccess()) {
                    throw new BizException("统计拉新效果错误");
                }
                return LaXinStatConvertor.convert(result.getData());
            }
        }.execute();
    }
}
