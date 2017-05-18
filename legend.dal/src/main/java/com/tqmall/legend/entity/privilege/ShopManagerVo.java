package com.tqmall.legend.entity.privilege;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by dingbao on 16/3/11.
 */
@EqualsAndHashCode(callSuper = false)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopManagerVo {

    private  Long id ;

    private String newPassword;



}
