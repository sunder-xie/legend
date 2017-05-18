package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.ComboInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@MyBatisRepository
public interface ComboInfoDao extends BaseDao<ComboInfo> {

    List<ComboInfo> selectByIdss(@Param("shopId") Long shopId, @Param("ids") Collection<Long> ids);

    /**
     * 通过计次卡名，获取计次卡类型信息
     * @param shopId
     * @param comboNames
     * @return
     */
    List<ComboInfo> findComboInfoByNames(@Param("shopId")Long shopId,@Param("comboNames")List<String> comboNames);
}
