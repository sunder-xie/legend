package com.tqmall.legend.entity.customer;

import com.tqmall.legend.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class CustomerCarFile extends BaseEntity {

    private Long shopId = Long.valueOf("0");
    private Long relId = Long.valueOf("0");
    private Long relType = Long.valueOf("0");
    private Integer fileType = Integer.valueOf("0");
    private Long fileSize = Long.valueOf("0");
    private String filePath;
    private String fileName;
    private String fileSuffix;

}

