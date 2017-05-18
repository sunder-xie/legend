package com.tqmall.legend.biz.sms.impl;

import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.sms.MobileVerifyRecordService;
import com.tqmall.legend.dao.sms.MobileVerifyRecordDao;
import com.tqmall.legend.entity.sms.MobileVerifyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Created by lixiao on 15-3-13.
 */
@Service
public class MobileVerifyRecordServiceImpl extends BaseServiceImpl implements MobileVerifyRecordService {

    @Autowired
    MobileVerifyRecordDao mobileVerifyRecordDao;

    @Override
    public List<MobileVerifyRecord> select(Map map) {
        return mobileVerifyRecordDao.select(map);
    }

    @Override
    public int insert(MobileVerifyRecord mobileVerifyRecord) {
        return mobileVerifyRecordDao.insert(mobileVerifyRecord);
    }

    @Override
    public int update(MobileVerifyRecord mobileVerifyRecord) {
        return mobileVerifyRecordDao.updateById(mobileVerifyRecord);
    }

    @Override
    public void saveVerifyRecord(@NotNull String mobile, @NotNull String sendCode) {

        // IF 存在记录 THEN 更新验证码
        Map<String, Object> queryParam = new HashMap<String, Object>(4);
        queryParam.put("mobile", mobile);
        List<String> sorts = new ArrayList<String>(1);
        sorts.add(" id desc ");
        queryParam.put("sorts", sorts);
        queryParam.put("offset", 0);
        queryParam.put("limit", 1);
        List<MobileVerifyRecord> mobileVerifyRecords = this.select(queryParam);
        if (!CollectionUtils.isEmpty(mobileVerifyRecords)) {
            MobileVerifyRecord mobileVerifyRecord = mobileVerifyRecords.get(0);
            mobileVerifyRecord.setCode(sendCode);
            mobileVerifyRecord.setGmtModified(new Date());
            this.update(mobileVerifyRecord);
            return;
        }

        // 插入一条新的验证码
        MobileVerifyRecord mobileVerifyRecord = new MobileVerifyRecord();
        mobileVerifyRecord.setCode(sendCode);
        mobileVerifyRecord.setMobile(mobile);
        mobileVerifyRecord.setGmtModified(new Date());
        mobileVerifyRecord.setGmtCreate(new Date());
        this.insert(mobileVerifyRecord);
    }

    @Override
    public boolean checkSMSCode(@NotNull String mobile, @NotNull String smsCode, int validTime) {

        // 是否通过
        boolean isPass = Boolean.FALSE;

        Map<String, Object> queryParam = new HashMap<String, Object>(4);
        queryParam.put("mobile", mobile);
        List<String> sorts = new ArrayList<String>(1);
        sorts.add(" id desc ");
        queryParam.put("sorts", sorts);
        queryParam.put("offset", 0);
        queryParam.put("limit", 1);
        List<MobileVerifyRecord> mobileVerifyRecords = this.select(queryParam);
        if (CollectionUtils.isEmpty(mobileVerifyRecords)) {
            return isPass;
        }
        MobileVerifyRecord mobileVerifyRecord = mobileVerifyRecords.get(0);
        Date gmtModified = mobileVerifyRecord.getGmtModified();
        if (gmtModified == null) {
            return isPass;
        }

        // 是否超过有效时间
        Long diffTime = new Date().getTime() - gmtModified.getTime();
        if (diffTime > validTime * 1000) {
            return isPass;
        }
        String code = mobileVerifyRecord.getCode();
        if (code != null && code.equals(smsCode)) {
            return Boolean.TRUE;
        }

        return isPass;
    }

    @Override
    public String checkSMSCodeThrowException(final String mobile, final String smsCode, final int validTime) {
        return new BizTemplate<String>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected String process() throws BizException {
                Map<String, Object> queryParam = new HashMap<String, Object>(4);
                queryParam.put("mobile", mobile);
                List<String> sorts = new ArrayList<String>(1);
                sorts.add(" id desc ");
                queryParam.put("sorts", sorts);
                queryParam.put("offset", 0);
                queryParam.put("limit", 1);
                List<MobileVerifyRecord> mobileVerifyRecords = select(queryParam);
                if (CollectionUtils.isEmpty(mobileVerifyRecords)) {
                    throw new BizException("未发送验证码");
                }
                MobileVerifyRecord mobileVerifyRecord = mobileVerifyRecords.get(0);
                Date gmtModified = mobileVerifyRecord.getGmtModified();
                if (gmtModified == null) {
                    throw new BizException("手机验证码错误");
                }

                // 是否超过有效时间ms
                Long diffTime = new Date().getTime() - gmtModified.getTime();
                if (diffTime > validTime) {
                    throw new BizException("手机验证码已失效,请重新发送");
                }
                String code = mobileVerifyRecord.getCode();
                if (code != null && code.equals(smsCode)) {
                    return "验证通过";
                }
                throw new BizException("手机验证码错误");
            }
        }.execute();

    }

    @Override
    public MobileVerifyRecord getMobileVerifyRecord(final String mobile) {
        return new BizTemplate<MobileVerifyRecord>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected MobileVerifyRecord process() throws BizException {
                MobileVerifyRecord mobileVerifyRecord = null;
                Map<String, Object> queryParam = new HashMap<String, Object>(4);
                queryParam.put("mobile", mobile);
                List<String> sorts = new ArrayList<String>(1);
                sorts.add(" id desc ");
                queryParam.put("sorts", sorts);
                queryParam.put("offset", 0);
                queryParam.put("limit", 1);
                List<MobileVerifyRecord> mobileVerifyRecords = select(queryParam);
                if (!CollectionUtils.isEmpty(mobileVerifyRecords)) {
                    mobileVerifyRecord = mobileVerifyRecords.get(0);
                }
                return mobileVerifyRecord;
            }
        }.execute();


    }

}
