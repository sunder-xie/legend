package com.tqmall.legend.facade.wechat.vo;

import com.tqmall.dandelion.wechat.client.dto.wechat.ButtonDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by wushuai on 16/6/6.
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class WechatMenuVo{
    private List<ButtonDTO> customMenu;//当前有效菜单(实例菜单)
    private List<ButtonDTO> defaultMenu;//管理后台默认菜单
    private List<ButtonDTO> customNoUseMenu;//用户自定义未使用菜单
}
