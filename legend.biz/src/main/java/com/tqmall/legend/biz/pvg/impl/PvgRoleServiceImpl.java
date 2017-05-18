package com.tqmall.legend.biz.pvg.impl;

import com.tqmall.legend.biz.pvg.IPvgRoleService;
import com.tqmall.legend.dao.pvg.PvgRoleDao;
import com.tqmall.legend.entity.pvg.PvgRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色Service Impl
 * <p/>
 * Created by dongc on 15/9/21.
 */
@Service
public class PvgRoleServiceImpl implements IPvgRoleService {

    @Autowired
    private PvgRoleDao pvgRoleDao;

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public PvgRole findPvgRoleById(Long id) {
        return pvgRoleDao.selectById(id);
    }

    @Override
    public List<PvgRole> selectPvgRoleList() {
        return pvgRoleDao.select(null);
    }
}
