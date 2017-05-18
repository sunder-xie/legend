package com.tqmall.legend.biz.sys.impl;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.sys.IExportTrackService;
import com.tqmall.legend.dao.sys.ExportTrackDao;
import com.tqmall.legend.entity.sys.ExportTrack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 导出记录 implement
 */
@Slf4j
@Service
public class ExportTrackServiceImpl extends BaseServiceImpl implements IExportTrackService {

    @Autowired
    ExportTrackDao exportTrackDao;

    @Override
    public void insert(ExportTrack exportTrack, UserInfo userInfo) {
        exportTrackDao.insert(exportTrack);
    }

    @Override
    public Page<ExportTrack> getPage(Pageable pageable,
                                     Map<String, Object> searchParams) {
        return super.getPage(exportTrackDao, pageable, searchParams);
    }
}
