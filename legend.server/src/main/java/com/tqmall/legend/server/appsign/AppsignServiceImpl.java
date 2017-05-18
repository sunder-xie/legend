package com.tqmall.legend.server.appsign;

import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.attendance.AppAttendanceService;
import com.tqmall.legend.biz.bo.attendance.SignTime;
import com.tqmall.legend.object.result.appsign.SignTimeDTO;
import com.tqmall.legend.service.appsign.RpcAppsignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 123 on 2016/3/22 0022.
 */
@Service("rpcAppsignService")
public class AppsignServiceImpl implements RpcAppsignService{

    @Autowired
    private AppAttendanceService appAttendanceService;


    public static final Logger log = LoggerFactory.getLogger(AppsignServiceImpl.class);
    @Override
    public Result<SignTimeDTO> getSigTime(Long shopId) {
        Result returnResult;
        String errorCode = "1";
        if (shopId==0){
            errorCode="-1";
            returnResult =Result.wrapErrorResult(errorCode,"店铺id为空");
            log.error("【dubbo发送消息】：AppsignServiceImpl.getSigTime,店铺id为空");
        }
        SignTimeDTO dto = new SignTimeDTO();
        SignTime sigTime = appAttendanceService.getSigTime(shopId);
        dto.setSignInTime(sigTime.getSignInTime());
        dto.setSignOffTime(sigTime.getSignOffTime());
        log.info("【dubbo发送消息】：AppsignServiceImpl.getSigTime,入参{}", shopId);
        return Result.wrapSuccessfulResult(dto);
    }

}
