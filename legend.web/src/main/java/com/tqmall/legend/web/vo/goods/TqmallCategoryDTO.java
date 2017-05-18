package com.tqmall.legend.web.vo.goods;

import lombok.Getter;
import lombok.Setter;

import com.tqmall.tqmallstall.domain.result.category.CategoryDTO;

/**
 * Created by wanghui on 12/7/15.
 */
@Setter
@Getter
public class TqmallCategoryDTO extends CategoryDTO {
    private boolean isCustomCat;
    public TqmallCategoryDTO(){
        this.isCustomCat = false;
    }

}
