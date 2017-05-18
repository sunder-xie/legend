package com.tqmall.legend.dao.warehouseshare;

/**
 * Created by tanghao on 16/11/10.
 */
import com.tqmall.legend.dao.base.BaseDao;
import com.tqmall.legend.dao.common.MyBatisRepository;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContact;
import com.tqmall.legend.entity.warehouseshare.WarehouseShareShopContactVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@MyBatisRepository
public interface WarehouseShareShopContactDao extends BaseDao<WarehouseShareShopContact> {

    /**
     * 根据门店id获取门店联系人列表
     * @param shopId
     * @return
     */
    List<WarehouseShareShopContactVO> queryContactWithShopInfoByShopId(@Param("shopId")Long shopId,@Param("userId")Long userId);

    /**
     * 根据门店id检测门店联系人
     * @param shopId
     * @return
     */
    List<WarehouseShareShopContact> checkContactWithShopInfoByShopId(@Param("shopId")Long shopId);
}
