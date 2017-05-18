package com.tqmall.legend.facade.magic;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.facade.magic.vo.ChannelVO;
import org.springframework.data.domain.Pageable;

/**
 * Created by macx on 16/5/13.
 */
public interface ChannelFacade {
    public Result getChannelPage(Pageable pageable, Long shopId, String channelName);

    public Result getChannelInfo(Long channelId, Long shopId);

    public Result saveChannelInfo(ChannelVO channelVO,Long shopId);

    public Result deleteChannelInfo(Long channelId, Long shopId);
}
