package com.tqmall.legend.biz.marketing.ng.impl;

import com.tqmall.legend.bi.entity.CommonPair;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.marketing.ng.NoteEffectService;
import com.tqmall.legend.dao.marketing.ng.NoteEffectDao;
import com.tqmall.legend.dao.shop.NoteInfoDao;
import com.tqmall.legend.entity.marketing.ng.NoteEffect;
import com.tqmall.legend.entity.marketing.ng.NoteEffectVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 提醒信息service
 * created by wjc on 3/14/2016
 */
@Service
@Slf4j
public class NoteEffectServiceImpl implements NoteEffectService {
    @Autowired
    private NoteEffectDao noteEffectDao;
    @Autowired
    private NoteInfoDao noteInfoDao;

    @Override
    public Integer count(Map<String,Object> params) {
        return noteEffectDao.selectCount(params);
    }

    @Override
    public BigDecimal effectAmount(Map<String, Object> params) {
        BigDecimal amount = noteEffectDao.effectAmount(params);
        if(amount!= null){
            return amount;
        }
        return BigDecimal.ZERO;
    }

    @Override
    public Page<NoteEffect> selectList(Pageable pageable,Map<String, Object> params) {
        if(pageable.getSort() != null){
            Iterator<Sort.Order> iterator = pageable.getSort().iterator();
            ArrayList<String> sorts = new ArrayList<String>();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                sorts.add(order.getProperty() + " " + order.getDirection().name());
            }
            params.put("sorts", sorts);
        }
        Integer totalSize = noteEffectDao.selectCount(params);
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        params.put("limit", pageRequest.getPageSize());
        params.put("offset", pageRequest.getOffset());

        List<NoteEffect> data = noteEffectDao.select(params);
        DefaultPage<NoteEffect> page = new DefaultPage<>(data, pageRequest, totalSize);
        return page;
    }

    @Override
    public List<NoteEffectVo> selectCountWithType(Map<String, Object> params) {
        List<NoteEffectVo> noteEffectVos = noteEffectDao.selectCountWithType(params);
        params.put("noteFlag",1);
        List<CommonPair<Integer, Integer>> commonPairs = noteInfoDao.selectCountWithType(params);
        Map<Integer,Integer> infos = new HashMap<>();
        for(CommonPair<Integer, Integer> cp : commonPairs){
            infos.put(cp.getDataF(),cp.getDataS());
        }
        for(NoteEffectVo noteEffectVo :noteEffectVos){
            Integer count = infos.remove(noteEffectVo.getNoteType());
            noteEffectVo.setNoteCount(count);
        }
        for(Integer i : infos.keySet()){
            NoteEffectVo noteEffectVo = new NoteEffectVo();
            noteEffectVo.setNoteType(i);
            noteEffectVo.setNoteCount(infos.get(i));
            noteEffectVo.setCustomerCount(0);
            noteEffectVo.setEffectAmount(BigDecimal.ZERO);
            noteEffectVos.add(noteEffectVo);
        }
        return noteEffectVos;
    }

    @Override
    public List<NoteEffect> selectActiveCustomer(Map param) {
        return noteEffectDao.selectActiveCustomer(param);
    }

    @Override
    public Integer selectCount(Map param) {
        return noteEffectDao.selectCount(param);
    }

    @Override
    public List<NoteEffectVo> selectCountWithWay(Map<String, Object> params) {
        List<NoteEffectVo> noteEffectVos = noteEffectDao.selectCountWithWay(params);
        params.put("noteFlag",1);
        List<CommonPair<Integer, Integer>> commonPairs = noteInfoDao.selectCountWithWay(params);
        Map<Integer,Integer> infos = new HashMap<>();
        for(CommonPair<Integer, Integer> cp : commonPairs){
            infos.put(cp.getDataF(),cp.getDataS());
        }
        for(NoteEffectVo noteEffectVo :noteEffectVos){
            Integer count = infos.remove(noteEffectVo.getNoteWay());
            noteEffectVo.setNoteCount(count);
        }
        for(Integer i : infos.keySet()){
            NoteEffectVo noteEffectVo = new NoteEffectVo();
            noteEffectVo.setNoteWay(i);
            noteEffectVo.setNoteCount(infos.get(i));
            noteEffectVo.setCustomerCount(0);
            noteEffectVo.setEffectAmount(BigDecimal.ZERO);
            noteEffectVos.add(noteEffectVo);
        }
        return noteEffectVos;
    }
}
