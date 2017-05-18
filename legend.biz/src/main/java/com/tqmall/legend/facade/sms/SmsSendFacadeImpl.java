package com.tqmall.legend.facade.sms;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.legend.biz.marketing.MarketingSmsService;
import com.tqmall.legend.biz.shop.ShopNoteInfoService;
import com.tqmall.legend.biz.sms.MobileVerifyRecordService;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.entity.marketing.MarketingSms;
import com.tqmall.legend.entity.shop.NoteInfo;
import com.tqmall.legend.entity.sms.MobileVerifyRecord;
import com.tqmall.legend.facade.sms.newsms.CallbackAdaptor;
import com.tqmall.legend.facade.sms.newsms.SmsCenter;
import com.tqmall.legend.facade.sms.newsms.param.SendParam;
import com.tqmall.legend.facade.sms.newsms.processor.send.SendBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by majian on 16/12/21.
 */
@Slf4j
@Service
public class SmsSendFacadeImpl implements SmsSendFacade {
    @Autowired
    private SmsCenter smsCenter;
    @Autowired
    private ShopNoteInfoService shopNoteInfoService;
    @Autowired
    private MarketingSmsService marketingSmsService;
    @Autowired
    private MobileVerifyRecordService mobileVerifyRecordService;
    @Autowired
    private SmsService smsService;
    @Override
    public Map<String, Long> sendForGather(final SendParam sendParam) {
        final Map<String, Long> license2logId = Maps.newHashMap();
        final Long shopId = sendParam.getShopId();

        smsCenter.send(sendParam, new CallbackAdaptor(){
            @Override
            public void onFinish(Long logId) {
                List<MarketingSms> smsList = marketingSmsService.listSucessByLogId(shopId, logId);
                for (MarketingSms sms : smsList) {
                    String licensesStr = sms.getLicenses();
                    if (!Strings.isNullOrEmpty(licensesStr)) {
                        String[] carLicenses = licensesStr.split(",");
                        for (String carLicense : carLicenses) {
                            license2logId.put(carLicense, sms.getId());
                        }
                    }
                }
            }
        });
        return license2logId;
    }

    @Override
    public Integer sendForNote(SendParam sendParam, int noteType) {
        final Set<String> carLicenseSet = Sets.newHashSet();

        Integer number = smsCenter.send(sendParam, new CallbackAdaptor() {
            @Override
            public void onEachSuccess(SendBO sendBO) {
                String licensesStr = sendBO.getLicenses();
                if (!Strings.isNullOrEmpty(licensesStr)) {
                    String[] carLicenses = licensesStr.split(",");
                    for (String carLicense : carLicenses) {
                        carLicenseSet.add(carLicense);
                    }
                }
            }
        });


        Long shopId = sendParam.getShopId();
        List<NoteInfo> noteInfos = shopNoteInfoService.listUnhandled(shopId, noteType, carLicenseSet);
        List<Long> noteInfoIds = Lists.transform(noteInfos, new Function<NoteInfo, Long>() {
            @Override
            public Long apply(NoteInfo input) {
                return input.getId();
            }
        });
        shopNoteInfoService.batchHandleNoteInfo(shopId,noteInfoIds ,0, sendParam.getOperator());
        log.info("[提醒中心发送营销短信]shopId={},noteType={},num={}", shopId, noteType, number);
        return number;
    }

    /**
     * 发送手机验证码
     * @param mobile 手机号
     * @param sendCode 验证码
     * @param smsBase 短信模板信息
     * @param validTime 多长时间才能再发送
     * @return
     */
    @Override
    public Boolean sendMobileCore(final String mobile, final String sendCode, final SmsBase smsBase, final int validTime) {
        return new BizTemplate<Boolean>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {

            }

            @Override
            protected Boolean process() throws BizException {
                MobileVerifyRecord mobileVerifyRecord = mobileVerifyRecordService.getMobileVerifyRecord(mobile);
                //在一定时间只可发送一次 ms
                if (mobileVerifyRecord != null) {
                    Long diffTime = new Date().getTime() - mobileVerifyRecord.getGmtModified().getTime();
                    if (diffTime < validTime) {
                        throw new BizException("您发送验证码操作太频繁了，请稍后再试");
                    }
                }

                //保存发送的验证记录legend_mobile_verify_record
                boolean success = smsService.sendMsg(smsBase, "手机发送短信");
                logger.info("[发送手机短信] smsBase:{}, 发送结果success:{}", smsBase, success);
                if (success) {
                    MobileVerifyRecord mobileVerifyRecordUpt = new MobileVerifyRecord();
                    // 将随机数存入数据库
                    if (mobileVerifyRecord != null) {
                        mobileVerifyRecordUpt.setId(mobileVerifyRecord.getId());
                        mobileVerifyRecordUpt.setCode(sendCode);
                        mobileVerifyRecordUpt.setGmtModified(new Date());
                        mobileVerifyRecordService.update(mobileVerifyRecordUpt);
                    } else {
                        mobileVerifyRecordUpt.setCode(sendCode);
                        mobileVerifyRecordUpt.setMobile(mobile);
                        mobileVerifyRecordService.insert(mobileVerifyRecordUpt);
                    }
                    return success;
                }
                throw new BizException("发送短信失败");
            }
        }.execute();
    }


}
