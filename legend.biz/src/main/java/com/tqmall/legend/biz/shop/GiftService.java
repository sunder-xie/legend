package com.tqmall.legend.biz.shop;

import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.shop.Gift;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by jason on 2015/08/03.
 */
public interface GiftService {

    /**
     * 添加记录
     * @param gift
     * @return
     */
    public Result add(Gift gift);

    /**
     * 根据 searchParams 条件获得礼品发放信息
     * @param pageable
     * @return
     */
    public Page<Gift> getPageGiftInfo(Pageable pageable, Map<String, Object> searchParams);

}
