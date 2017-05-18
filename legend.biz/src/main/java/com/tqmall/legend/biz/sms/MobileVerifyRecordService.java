package com.tqmall.legend.biz.sms;

import com.tqmall.legend.entity.sms.MobileVerifyRecord;

import java.util.List;
import java.util.Map;

/**
 * Created by lixiao on 15-3-13.
 */
public interface MobileVerifyRecordService {

    List<MobileVerifyRecord> select(Map map);

    int insert(MobileVerifyRecord mobileVerifyRecord);

    int update(MobileVerifyRecord mobileVerifyRecord);

    /**
     * 记录 短信验证记录
     *
     * @param mobile   手机号
     * @param sendCode 验证码
     */
    void saveVerifyRecord(String mobile, String sendCode);

    /**
     * 校验 短信验证码 与 手机号是否匹配
     *
     * @param mobile    手机号
     * @param smsCode   验证码
     * @param validTime 有效时间(单位秒 如:60s)
     * @return
     */
    boolean checkSMSCode(String mobile, String smsCode, int validTime);

    /**
     * 校验 短信验证码 与 手机号是否匹配 模板返回
     *
     * @param mobile    手机号
     * @param smsCode   验证码
     * @param validTime 有效时间(单位秒 如:60s)
     * @return
     */
    String checkSMSCodeThrowException(String mobile, String smsCode, int validTime);

    /**
     * 获取手机发送短信记录
     * @param mobile 手机号
     * @return
     */
    MobileVerifyRecord getMobileVerifyRecord(String mobile);
}
