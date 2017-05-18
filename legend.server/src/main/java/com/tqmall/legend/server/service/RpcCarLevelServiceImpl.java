package com.tqmall.legend.server.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.shop.CarLevelService;
import com.tqmall.legend.entity.shop.CarLevel;
import com.tqmall.legend.object.result.service.CarLevelDTO;
import com.tqmall.legend.service.service.RpcCarLevelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/11/22.
 */
@Service("rpcCarLevelService")
public class RpcCarLevelServiceImpl implements RpcCarLevelService {
    @Autowired
    private CarLevelService carLevelService;

    /**
     * 根据门店id获取车辆级别
     *
     * @param shopId
     * @param nameLike 车辆级别名称模糊查询，可不传
     * @return
     */
    @Override
    public Result<List<CarLevelDTO>> getCarLevelList(final Long shopId, final String nameLike) {
        return new ApiTemplate<List<CarLevelDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId, "门店id不能为空");
            }

            @Override
            protected List<CarLevelDTO> process() throws BizException {
                List<CarLevelDTO> carLevelDTOList = Lists.newArrayList();
                Map<String, Object> searchMap = Maps.newHashMap();
                searchMap.put("shopId", shopId);
                if (StringUtils.isNotBlank(nameLike)) {
                    searchMap.put("nameLike", nameLike);
                }
                List<CarLevel> carLevelList = carLevelService.selectNoCache(searchMap);
                if (CollectionUtils.isEmpty(carLevelList)) {
                    return carLevelDTOList;
                }
                for (CarLevel carLevel : carLevelList) {
                    CarLevelDTO carLevelDTO = new CarLevelDTO();
                    BeanUtils.copyProperties(carLevel, carLevelDTO);
                    carLevelDTOList.add(carLevelDTO);
                }
                return carLevelDTOList;
            }
        }.execute();
    }
}
