package com.tqmall.legend.biz.pvg;

import com.tqmall.common.UserInfo;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.entity.privilege.Roles;

/**
 * human resource management service
 * <p/>
 * Created by dongc on 15/9/22.
 */
public interface IHRMService {

    /**
     * 从模板表插入岗位树
     *
     * @param type
     * @param userInfo
     * @return
     */
    Result insertOrgTreeFromTemplate(Integer type, UserInfo userInfo);

}
