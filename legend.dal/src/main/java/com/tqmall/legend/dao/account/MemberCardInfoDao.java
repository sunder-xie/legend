package com.tqmall.legend.dao.account;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.account.MemberCardInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface MemberCardInfoDao extends BaseDao<MemberCardInfo> {

    /**
     * 根据名字批量查询
     * @param names
     * @param shopId
     * @return
     */
    List<MemberCardInfo> selectByNames(@Param("names") List<String> names, @Param("shopId") Long shopId);

    /**
     * 根据id批量查询
     * @param shopId
     * @param ids
     * @return
     */
    List<MemberCardInfo> selectByIds(@Param("shopId") Long shopId, @Param("ids") List<Long> ids);

    void update(MemberCardInfo cardInfo);

    String getTypeNameById(@Param("shopId") Long shopId, @Param("id") Long id);

    MemberCardInfo getInfoById(Long id);
}
