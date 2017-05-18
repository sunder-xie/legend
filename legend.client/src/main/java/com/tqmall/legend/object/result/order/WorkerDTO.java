package com.tqmall.legend.object.result.order;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zsy on 16/12/2.
 * 洗车工、维修工对象
 */
@Getter
@Setter
public class WorkerDTO implements Serializable {
    private static final long serialVersionUID = 6027409918863506984L;
    private Long id;//主键id
    private String name;//姓名
}
