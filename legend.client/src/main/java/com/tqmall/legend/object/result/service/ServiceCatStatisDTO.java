package com.tqmall.legend.object.result.service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by majian on 16/10/20.
 */
public class ServiceCatStatisDTO implements Serializable {
    private Integer total;
    private List<ServiceCatStatisVO> voList;

    public ServiceCatStatisDTO(List<ServiceCatStatisVO> voList, Integer total) {
        this.voList = voList;
        this.total = total;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<ServiceCatStatisVO> getVoList() {
        return voList;
    }

    public void setVoList(List<ServiceCatStatisVO> voList) {
        this.voList = voList;
    }
}
