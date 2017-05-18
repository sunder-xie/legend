package com.tqmall.legend.facade.insurance;

import com.tqmall.mana.client.beans.settle.SettleServiceCheckDetailDTO;
import com.tqmall.mana.client.beans.settle.SettleShopDTO;
import com.tqmall.mana.client.beans.settle.SettleShopRuleIntroductionDTO;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.search.dubbo.client.PageableResponseExtend;
import com.tqmall.search.dubbo.client.mana.param.SettleCheckDetailRequest;
import com.tqmall.search.dubbo.client.mana.result.SettleCheckDetailResult;

import java.util.List;

/**
 * Created by zxg on 17/1/20.
 * 16:13
 * no bug,以后改代码的哥们，祝你好运~！！
 */
public interface AnxinManaSettleFacade {
    // 获得门店的基础数据
    SettleShopDTO getSettleShopDetailByShopId(Integer shopId);
    // 根据保险单号 获得服务包的详情
    SettleServiceCheckDetailDTO getServicePackageDetailByInsuranceSn(String insuranceOrderSn);

    // 获得搜索的 的 分页详情数据
    PageableResponseExtend<SettleCheckDetailResult> getSettleCheckDetailPage(SettleCheckDetailRequest settleCheckDetailRequest, PageableRequest pageableRequest);

    // 获得规则说明列表
    List<SettleShopRuleIntroductionDTO> getIntroductionList();
}
