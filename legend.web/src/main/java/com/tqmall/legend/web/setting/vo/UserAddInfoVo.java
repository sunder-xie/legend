package com.tqmall.legend.web.setting.vo;

import com.tqmall.legend.facade.magic.vo.TeamVO;
import com.tqmall.magic.object.result.workshop.ProcessDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by zsy on 17/1/9.
 */
@Getter
@Setter
public class UserAddInfoVo {
    private String shopAbbr;
    private List<ProcessDTO> processDTOList;
    private List<TeamVO> teamVOList;
    private List<PvgRoleVo> pvgRoleVoList;
}
