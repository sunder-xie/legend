package com.tqmall.legend.object.result.account;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wushuai on 2016/07/12.
 */
public class AccountComboPageDTO implements Serializable{
    private static final long serialVersionUID = 7626174847195636521L;
    private List<AccountComboDTO> content = null;
    private Integer count =0;//总数

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<AccountComboDTO> getContent() {
        return content;
    }

    public void setContent(List<AccountComboDTO> content) {
        this.content = content;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
