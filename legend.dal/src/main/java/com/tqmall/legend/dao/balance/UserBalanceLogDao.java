package com.tqmall.legend.dao.balance;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.balance.UserBalanceLog;
import com.tqmall.legend.pojo.balance.UserBalanceLogVO;

import java.util.List;
import java.util.Map;

/**
 * Created by xiangDong.qu on 15/12/17.
 */
@MyBatisRepository
public interface UserBalanceLogDao extends BaseDao<UserBalanceLog> {

    /**
     * 获取红包记录和提现记录的数据
     *
     * @param param {
     *              limit:每页数量
     *              offset:起始值
     *              userId:用户id
     *              shopId:店铺id
     *              isDeleted:"N"
     *              sorts: 记录的添加时间倒序
     *              }
     */
    public List<UserBalanceLogVO> getUserBalanceLogVOList(Map<String, Object> param);

}
