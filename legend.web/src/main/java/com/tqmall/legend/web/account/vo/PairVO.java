package com.tqmall.legend.web.account.vo;

/**
 * Created by majian on 17/1/3.
 */
public class PairVO {
    private Long id;
    private String name;

    public PairVO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PairVO pairVO = (PairVO) o;

        return id != null ? id.equals(pairVO.id) : pairVO.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
