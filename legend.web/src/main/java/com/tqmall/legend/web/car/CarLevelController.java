package com.tqmall.legend.web.car;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.ApiTemplate;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.core.utils.UserUtils;
import com.tqmall.itemcenter.object.param.carLevel.CarLevelQryParam;
import com.tqmall.itemcenter.object.result.carLevel.CarLevelDTO;
import com.tqmall.itemcenter.service.carLevel.RpcCarLevelService;
import com.tqmall.legend.web.common.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author : 祝文博<wenbo.zhu@tqmall.com>
 * @Create : 2014-12-10 22:17
 */
@Controller
@RequestMapping("shop/car_level")
public class CarLevelController extends BaseController {

    @Autowired
    private RpcCarLevelService rpcCarLevelService;

    /**
     * 车辆级别接口
     * 用到地方：
     * 设置-服务资料：费用添加编辑、服务添加编辑、套餐添加编辑
     * 接车维修：工单新建附加资料、工单新建服务、服务编辑页(新版)
     * @param name
     * @return
     */
    @RequestMapping("get_car_level_by_name")
    @ResponseBody
    public Result<List<CarLevelDTO>> getCarLevelByName(@RequestParam(value = "name", required = false) final String name) {
        return new ApiTemplate<List<CarLevelDTO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected List<CarLevelDTO> process() throws BizException {
                Long shopId = UserUtils.getShopIdForSession(request);
                CarLevelQryParam carLevelQryParam = new CarLevelQryParam();
                carLevelQryParam.setShopId(shopId);
                carLevelQryParam.setNameLike(name);
                carLevelQryParam.setPage(1);
                carLevelQryParam.setSize(Integer.MAX_VALUE);
                Result<List<CarLevelDTO>> carLevelDTOListResult = rpcCarLevelService.selectByName(carLevelQryParam);
                logger.info("[dubbo-itemcenter]查询车辆级别,carLevelQryParam:{},success:{},message:{}", LogUtils.objectToString(carLevelQryParam), carLevelDTOListResult.isSuccess(), carLevelDTOListResult.getMessage());
                if (!carLevelDTOListResult.isSuccess()) {
                    throw new BizException(carLevelDTOListResult.getMessage());
                }
                return carLevelDTOListResult.getData();
            }
        }.execute();
    }
}
