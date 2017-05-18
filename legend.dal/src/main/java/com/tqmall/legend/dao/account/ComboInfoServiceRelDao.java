package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.ComboInfoServiceRel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface ComboInfoServiceRelDao extends BaseDao<ComboInfoServiceRel> {

    List<ComboInfoServiceRel> selectByComboInfoId(@Param("comboInfoId") Long comboInfoId);
    List<ComboInfoServiceRel> selectByComboInfoIds(@Param("comboInfoIds") Long[] comboInfoIds);

    /**
     * 通过服务名，获取计次卡类型服务项目信息
     * @param shopId
     * @param serviceNames
     * @return
     */
    List<ComboInfoServiceRel> findByServiceNames(@Param("shopId")Long shopId,@Param("serviceNames")List<String> serviceNames);

    List<ComboInfoServiceRel> findByComboInfoIds(@Param("shopId")Long shopId, @Param("comboInfoIds")List<Long> comboInfoIds);

}
