package com.tqmall.legend.dao.warehouseshare;

import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.warehouseshare.WarehouseShare;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareCountVO;
import com.tqmall.legend.pojo.warehouseshare.WarehouseShareGoodsDetail;
import com.tqmall.legend.pojo.warehouseshare.WarehouseShareVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by tanghao on 16/11/10.
 */

@MyBatisRepository
public interface WarehouseShareDao extends BaseDao<WarehouseShare> {

    int countByCondition(@Param("goodsCate") String goodsCate, @Param("goodsName") String goodsName, @Param("provinceId") Long provinceId, @Param("cityId") Long cityId);

    List<WarehouseShareVO> getListByCondition(@Param("goodsCate") String goodsCate, @Param("goodsName") String goodsName, @Param("provinceId") Long provinceId, @Param("cityId") Long cityId, @Param("offset") int offset, @Param("limit") int limit);

    WarehouseShareGoodsDetail getWarehouseShareGoodsDetail(Long id);

    /**
     * 获取各种状态的销售列表数量
     * @param shopId
     * @return
     */
    WarehouseShareCountVO querySaleCount(@Param("shopId") Long shopId);

    /**
     * 根据门店id查询已存在goodsId
     * @param shopId
     * @return
     */
    List<Long> queryExistGoodsId(@Param("shopId") Long shopId);


    int countByParam(@Param("goodsCate") String goodsCate, @Param("goodsName") String goodsName, @Param("shopId") Long shopId, @Param("status") Integer status);

    List<WarehouseShare> searchListByParam(@Param("goodsCate") String goodsCate, @Param("goodsName") String goodsName, @Param("shopId") Long shopId, @Param("status") Integer status, @Param("offset") int offset, @Param("limit") int limit);

    int checkPass(@Param("id") Long id);

    int checkNotPass(@Param("id") Long id, @Param("remark") String remark);
}
