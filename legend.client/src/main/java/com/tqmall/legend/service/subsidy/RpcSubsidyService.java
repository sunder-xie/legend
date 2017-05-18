package com.tqmall.legend.service.subsidy;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.object.param.subsidy.RpcUserDefaultACSetParam;
import com.tqmall.legend.object.result.subsidy.UserDefaultBankDTO;

/**
 * TODO 后续迁移mace
 * Created by xiangDong.qu on 16/2/24.
 */
@Deprecated
public interface RpcSubsidyService {
    /**
     * 获取用户的默认提现账号信息
     *
     * @param userId 用户id
     * @param shopId 店铺id
     */
    public Result<UserDefaultBankDTO> getUserDefaultBank(Long userId, Long shopId);

    /**
     * 用户设置默认的提现银行卡
     */
    public Result<UserDefaultBankDTO> userDefaultACSet(RpcUserDefaultACSetParam rpcUserDefaultACSetParam);

 }
