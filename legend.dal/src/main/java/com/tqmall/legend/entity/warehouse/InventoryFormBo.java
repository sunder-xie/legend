package com.tqmall.legend.entity.warehouse;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 新建盘点表单
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class InventoryFormBo implements Serializable {

    private static final long serialVersionUID = -4758364763173232078L;


    // 盘点记录ID
    private Long recordId;
    // 盘点编号
    private String recordSn;
    // 盘点人ID
    private Long inventoryCheckerId;
    // 盘点人姓名
    private String inventoryCheckerName;
    // 1:草稿;2:正式的
    private int status;
    // 盘点配件列表
    private List<InventoryGoodsBo> inventoryGoodsBos;
    // 盘点配件JSON
    private String inventoryGoodsJSON;
    // 备注
    private String inventoryRemark;


}
