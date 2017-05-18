package com.tqmall.legend.facade.magic;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.entity.shop.Shop;
import com.tqmall.legend.facade.magic.vo.ShopPartnerVO;
import org.springframework.data.domain.Pageable;

/**
 * Created by macx on 16/5/17.
 */
public interface PartnerFacade {

    public Result getPartnerPage(Pageable pageable, Long shopId,String name, Integer partnerStatus);

    public Result getAddPartnerList(Shop shop, String name);

    public Result updatePartnerInfo(ShopPartnerVO shopPartnerVO,Long shopId,Long userId);

    public Result quitShopPartner(Long id,Long shopId,Long userId,String reason);

    public Result addShopPartner(String[] partnerIdArray,Long shopId,Long userId);

    public Result joinShopPartner(Long id, Long shopId, Long userId);

    public Result getPartnerInfo(Long id,Long shopId);

    public Result getUnJoinPartnerPage(Pageable pageable,Shop shop,String name);
}
