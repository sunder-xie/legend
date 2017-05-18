package com.tqmall.legend.facade.magic;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.magic.vo.ShopManagerExtVO;
import com.tqmall.legend.facade.magic.vo.TeamVO;

import java.util.List;

/**
 * 钣喷中心 班组管理
 * Created by shulin on 16/7/4.
 */
public interface TeamFacade {

    /**
     * 添加班组
     *
     * @param teamVO
     * @return
     */
    Result<Long> addTeam(TeamVO teamVO);

    /**
     * 删除班组
     *
     * @param teamId
     * @return
     */
    Result<Integer> removeTeam(Long teamId);

    /**
     * 更新班组
     *
     * @param teamVO
     * @return
     */
    Result<Integer> modifyTeam(TeamVO teamVO);

    /**
     * 获取门店所有的班组列表
     *
     * @param shopId
     * @return
     */
    Result<List<TeamVO>> getTeamByShopId(Long shopId);

    /**
     * 获取班组详情
     *
     * @param teamId
     * @return
     */
    Result<TeamVO> getTeamDetail(Long shopId,Long teamId);

    /**
     * 获取班组下的人员列表
     *
     * @param teamId
     * @return
     */
    Result<List<ShopManagerExtVO>> getManagerExtListByTeamId(Long shopId,Long teamId);

}
