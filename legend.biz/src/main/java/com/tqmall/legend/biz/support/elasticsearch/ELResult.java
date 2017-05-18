package com.tqmall.legend.biz.support.elasticsearch;

import com.tqmall.legend.biz.common.DefaultPage;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mokala on 8/27/15.
 * 搜索引擎返回结果
 */
@Data
public class ELResult<T> {
    private String environment;
    private Map<String, String> params;
    private ELResponse<T> response;

    public <T> Page<T> getPage() {
        PageRequest pageRequest = new PageRequest(getResponse().getPage() < 1 ? 1 : getResponse().getPage() - 1, getResponse().getSize());
        List results = getResponse().getList();
        results = (results == null) ? new ArrayList() : results;
        Long total = getResponse().getTotal();
        total = (total == null) ? Long.valueOf(0) : total;
        Page<T> page = new DefaultPage<T>((List<T>) results, pageRequest, total);

        return page;
    }
}
