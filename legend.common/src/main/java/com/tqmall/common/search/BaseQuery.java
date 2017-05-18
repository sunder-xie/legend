package com.tqmall.common.search;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;


import java.util.List;

/**
 * Created by zsy on 17/1/5.
 */
@Getter
@Setter
public class BaseQuery {
    private int page = 1;
    private int size = 10;
    private List<String> sorts;

    /**
     * 默认根据id倒序排序
     * 排序，如：
     * id desc
     * gmt_create asc
     * @return
     */
    public List<String> getSorts() {
        if (CollectionUtils.isEmpty(sorts)) {
            sorts = Lists.newArrayList();
            sorts.add("id desc");
        }
        return sorts;
    }

    public int getOffset() {
        int page = this.page < 1 ? 0 : this.page - 1;
        return this.size * page;
    }

    public int getLimit() {
        return this.size;
    }
}
