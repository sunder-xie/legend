package com.tqmall.legend.facade.magic.vo;

import com.tqmall.legend.pojo.ShopManagerCom;
import lombok.Data;

import java.util.Date;

/**
 * Created by shulin on 16/7/1.
 */
@Data
public class ShopManagerExtVO {
    private Long id;             //*
    private Long modifier;
    private Date gmtModified; //修改时间
    private Long shopId;        //所属门店id *
    private Long managerId;     //员工id  *
    private String managerName;
    private Long teamId;        //班组id   *
    private String teamName;    //*
    private Long processId;     //工序id *
    private String processName; //*
    private String cardNum;     //工牌号
    private Integer workStatus; //0表示不在岗，1表示在岗*
}
