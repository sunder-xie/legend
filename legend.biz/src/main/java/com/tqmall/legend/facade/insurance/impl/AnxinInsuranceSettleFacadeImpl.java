package com.tqmall.legend.facade.insurance.impl;

import com.google.common.collect.Lists;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.PagingResult;
import com.tqmall.core.common.entity.Result;
import com.tqmall.insurance.domain.param.insurance.ConsumeServiceParam;
import com.tqmall.insurance.domain.param.insurance.InsuranceUserServicePackageParam;
import com.tqmall.insurance.domain.result.InsuranceUserServicePackageDTO;
import com.tqmall.insurance.service.insurance.RpcInsuranceUserService;
import com.tqmall.insurance.service.insurance.RpcServiceItemService;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.sms.SmsService;
import com.tqmall.legend.biz.sms.vo.SmsBase;
import com.tqmall.legend.facade.insurance.AnxinInsuranceSettleFacade;
import com.tqmall.legend.facade.insurance.bo.ConsumeServiceBo;
import com.tqmall.legend.facade.insurance.bo.ConsumeServiceParamBo;
import com.tqmall.legend.facade.insurance.vo.InsuranceUserServicePackageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/9/19.
 */
@Slf4j
@Service
public class AnxinInsuranceSettleFacadeImpl implements AnxinInsuranceSettleFacade {
    @Autowired
    private RpcInsuranceUserService rpcInsuranceUserService;
    @Autowired
    private RpcServiceItemService rpcServiceItemService;
    @Autowired
    private SmsService smsService;

    @Override
    public DefaultPage<InsuranceUserServicePackageVo> getPage(Pageable pageable, InsuranceUserServicePackageParam servicePackageParam) {
        DefaultPage<InsuranceUserServicePackageVo> page = null;
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
        servicePackageParam.setLimit(pageRequest.getPageSize());
        servicePackageParam.setStart(pageRequest.getOffset());
        servicePackageParam.setOrderStr("gmt_create");//创建时间排序
        servicePackageParam.setOrderStyle("desc");
        log.info("【dubbo:调用insurance】获取服务券列表,入参:{}", LogUtils.objectToString(servicePackageParam));
        PagingResult<InsuranceUserServicePackageDTO> pagingResult = rpcInsuranceUserService.getUserServicePackageList(servicePackageParam);
        log.info("【dubbo:调用insurance】获取服务券列表,返回success:{}", pagingResult.isSuccess());
        if (pagingResult.isSuccess()) {
            List<InsuranceUserServicePackageDTO> insuranceUserServicePackageDTOList = pagingResult.getList();
            List<InsuranceUserServicePackageVo> insuranceUserServicePackageVoList = Lists.newArrayList();
            for (InsuranceUserServicePackageDTO insuranceUserServicePackageDTO : insuranceUserServicePackageDTOList) {
                InsuranceUserServicePackageVo insuranceUserServicePackageVo = new InsuranceUserServicePackageVo();
                BeanUtils.copyProperties(insuranceUserServicePackageDTO, insuranceUserServicePackageVo);
                insuranceUserServicePackageVoList.add(insuranceUserServicePackageVo);
            }
            int total = pagingResult.getTotal();
            page = new DefaultPage<InsuranceUserServicePackageVo>(insuranceUserServicePackageVoList, pageRequest, total);
        }
        return page;
    }

    @Override
    public InsuranceUserServicePackageVo getDetail(Integer id) {
        log.info("【dubbo:调用insurance】服务券核销，获取服务券详情,入参:{}", id);
        com.tqmall.core.common.entity.Result<InsuranceUserServicePackageDTO> packageDTOResult = rpcInsuranceUserService.getUserServicePackageInfo(id);
        log.info("【dubbo:调用insurance】服务券核销，获取服务券详情,返回success:{}", packageDTOResult.isSuccess());
        if (packageDTOResult.isSuccess()) {
            InsuranceUserServicePackageDTO insuranceUserServicePackageDTO = packageDTOResult.getData();
            InsuranceUserServicePackageVo insuranceUserServicePackageVo = new InsuranceUserServicePackageVo();
            BeanUtils.copyProperties(insuranceUserServicePackageDTO, insuranceUserServicePackageVo);
            return insuranceUserServicePackageVo;
        }
        return null;
    }

    @Override
    public void consumeServiceItem(ConsumeServiceBo consumeServiceBo) {
        List<ConsumeServiceParamBo> consumeServiceParamBoList = consumeServiceBo.getConsumeServiceParamBoList();
        List<ConsumeServiceParam> consumeServiceParamList = Lists.newArrayList();
        for (ConsumeServiceParamBo consumeServiceParamBo : consumeServiceParamBoList) {
            ConsumeServiceParam consumeServiceParam = new ConsumeServiceParam();
            BeanUtils.copyProperties(consumeServiceParamBo, consumeServiceParam);
            consumeServiceParamList.add(consumeServiceParam);
        }
        //核销
        log.info("【dubbo:调用insurance】服务券核销，入参：{}",LogUtils.objectToString(consumeServiceBo));
        Result<Boolean> consumeServiceItemResult = rpcServiceItemService.consumeServiceItem(consumeServiceParamList);
        log.info("【dubbo:调用insurance】服务券核销，返回结果:{}", consumeServiceItemResult.isSuccess());
        if(consumeServiceItemResult.isSuccess()){
            //发短信
            sendMobile(consumeServiceBo, consumeServiceParamBoList);
        }else{
            throw new BizException("核销失败");
        }
    }

    /**
     * 发送短信给用户
     * 例：【淘汽云修】您的服务包于2016年9月8号核销【打蜡】1次，【洗车】1次，若有疑问请致电4009937288
     *
     * @param consumeServiceBo
     * @param consumeServiceParamBoList
     */
    private void sendMobile(ConsumeServiceBo consumeServiceBo, List<ConsumeServiceParamBo> consumeServiceParamBoList) {
        String mobile = consumeServiceBo.getMobile();
        SmsBase smsBase = new SmsBase();
        smsBase.setAction(Constants.LEGEND_MARKETING_SMS_TPL);
        smsBase.setMobile(mobile);
        Map<String, Object> smsMap = new HashMap<>();
        StringBuffer content = new StringBuffer("您的服务包于");
        content.append(DateUtil.convertDate(new Date()));
        content.append("核销");
        for (ConsumeServiceParamBo consumeServiceParamBo : consumeServiceParamBoList) {
            String itemName = consumeServiceParamBo.getItemName();
            Integer consumeTimes = consumeServiceParamBo.getConsumeTimes();
            content.append("【");
            content.append(itemName);
            content.append("】");
            content.append(consumeTimes);
            content.append("次,");
        }
        content.append("若有疑问请致电4009937288");
        smsMap.put("content", content);
        smsBase.setData(smsMap);
        smsService.sendMsg(smsBase, "安心保险服务券核销");
    }
}
