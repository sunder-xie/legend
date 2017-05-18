package com.tqmall.legend.object.result.appsign;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 123 on 2016/3/22 0022.
 */
@Data
public class SignTimeDTO implements Serializable{

    private Date signInTime;
    private Date signOffTime;
}
