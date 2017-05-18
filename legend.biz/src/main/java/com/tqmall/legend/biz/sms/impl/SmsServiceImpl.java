package com.tqmall.legend.biz.sms.impl;

import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.common.JsonUtils;
import com.tqmall.tqmallstall.domain.param.sms.SmsParam;
import com.tqmall.tqmallstall.domain.result.sms.SmsDTO;
import com.tqmall.tqmallstall.service.sms.AppSmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * Created by sven on 16/8/19.
 */
@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    @Resource
    private AppSmsService appSmsService;
    @Value("${sms.send}")
    private boolean smsSend;

    /**
     * 发送短信
     *
     * @param smsBase
     * @param msg
     * @return
     */

    @Override
    public boolean sendMsg(SmsBase smsBase, String msg) {
        if (smsBase == null || smsBase.getData() == null) {
            return false;
        }
        Object content = smsBase.getData().get("content");
        if (content != null) {
            String strContent = content.toString();
            smsBase.getData().put("content", strContent.replaceAll("【", "[").replaceAll("】", "]"));
        }

        String data = JsonUtils.mapToJsonStr(smsBase.getData());
        SmsParam smsParam = new SmsParam();
        smsParam.setMobile(smsBase.getMobile());
        smsParam.setSource(Constants.CUST_SOURCE);
        smsParam.setAction(smsBase.getAction());
        smsParam.setData(data);

        return send(smsParam, msg);
    }

    public String generateCode() {
        List<String> list = Lists.newArrayList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    private boolean send(SmsParam smsParam, String info) {
        try {
            log.info("{},入参:{}", info, LogUtils.objectToString(smsParam));
            if(smsSend){
                Result<SmsDTO> result = appSmsService.sendSms(smsParam);
                if (!result.isSuccess()) {
                    log.error("短信发送失败,失败原因:{}", LogUtils.objectToString(result));
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("短信发送失败,错误原因:{}", e);
            return false;
        }
        return true;
    }

}
