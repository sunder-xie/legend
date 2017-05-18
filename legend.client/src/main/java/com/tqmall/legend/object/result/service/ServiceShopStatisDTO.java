package com.tqmall.legend.object.result.service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by majian on 16/10/20.
 */
public class ServiceShopStatisDTO implements Serializable{
    private Integer total;
    private List<ServiceShopStatisVO> voList;

    public ServiceShopStatisDTO(Integer total, List<ServiceShopStatisVO> voList) {
        this.total = total;
        this.voList = voList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<ServiceShopStatisVO> getVoList() {
        return voList;
    }

    public void setVoList(List<ServiceShopStatisVO> voList) {
        this.voList = voList;
    }
}
