package com.tqmall.legend.entity.shop;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 导出excel密码设置 实体
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class ExportPasswordSetConfig {


    // 密码
    private String password;
    // 短信验证码
    private String SMSCode;
}
