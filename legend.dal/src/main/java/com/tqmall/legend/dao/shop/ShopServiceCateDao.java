package com.tqmall.legend.dao.shop;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.pub.service.ServiceCateVo;
import com.tqmall.legend.entity.shop.ShopServiceCate;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@MyBatisRepository
public interface ShopServiceCateDao extends BaseDao<ShopServiceCate> {

    List<ServiceCateVo> selectFirstCate();

    List<ShopServiceCate> selectByIds2(@Param("shopId") Long shopId, @Param("ids") Collection<Long> serviceCatIds);

    /**
     * 根据门店id、服务类别名，获取服务类别信息
     *
     * @param shopId   门店id
     * @param catNames 服务类别名
     * @return
     */
    List<ShopServiceCate> findServiceCatesByCatNames(@Param("shopId")Long shopId,@Param("cateType")Integer cateType, @Param("catNames")String... catNames);

}
