package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 导出excel密码 检查实体
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class ExportPasswordCheckVO {

    // 是否是管理员
    private String isAdmin;
    // 是否设置过导出密码
    private String isSetedExportPassword;
}
