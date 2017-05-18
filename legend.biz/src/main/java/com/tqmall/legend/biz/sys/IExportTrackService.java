package com.tqmall.legend.biz.sys;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.entity.sys.ExportTrack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * 导出记录 interface
 */
public interface IExportTrackService {

    /**
     * 插入导出记录
     *
     * @param exportTrack 记录实体
     * @param userInfo    操作人
     */
    void insert(ExportTrack exportTrack, UserInfo userInfo);


    /**
     * 分页记录
     *
     * @param pageable
     * @param searchParams
     * @return
     */
    Page<ExportTrack> getPage(Pageable pageable,
                              Map<String, Object> searchParams);

}
