package com.tqmall.legend.web.fileImport.process;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.legend.biz.account.AccountComboService;
import com.tqmall.legend.biz.account.AccountFlowService;
import com.tqmall.legend.biz.account.ComboInfoService;
import com.tqmall.legend.common.fileImport.CommonFileImportContext;
import com.tqmall.legend.common.fileImport.FileImportProcess;
import com.tqmall.legend.entity.account.AccountCombo;
import com.tqmall.legend.entity.account.AccountComboServiceRel;
import com.tqmall.legend.entity.account.ComboInfoServiceRel;
import com.tqmall.legend.web.fileImport.vo.AccountComboImportContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by twg on 16/12/7.
 */
@Slf4j
@Component
public class AccountComboImportProcess implements FileImportProcess {
    @Autowired
    private AccountComboService accountComboService;
    @Autowired
    private ComboInfoService comboInfoService;
    @Autowired
    private AccountFlowService accountFlowService;

    @Override
    @Transactional
    public void process(CommonFileImportContext fileImportContext) {
        if (log.isDebugEnabled()) {
            log.debug("This is AccountComboImportProcess");
        }
        AccountComboImportContext accountComboImportContext = (AccountComboImportContext) fileImportContext;
        Long shopId = accountComboImportContext.getShopId();
        Long userId = accountComboImportContext.getUserId();
        List<AccountCombo> accountCombos = accountComboImportContext.getExcelContents();

        /*计次卡对应的服务项目信息*/
        Map<String, Map<String, ComboInfoServiceRel>> comboInfoAndServiceRelMap = comboInfoService.getComboInfoMap(shopId);
        /*手机号对应的计次卡名称、服务项目实例信息*/
        Map<String, Map<String, Map<String, AccountCombo>>> mobileToComboServiceRelMap = Maps.newHashMap();

        List<AccountCombo> result = Lists.newArrayList();
        for (int i = 0; i < accountCombos.size(); i++) {
            AccountCombo accountCombo = accountCombos.get(i);
            String serviceName = accountCombo.getServiceName();
            String mobile = accountCombo.getMobile();
            String comboName = accountCombo.getComboName();
            String expireDateStr = accountCombo.getExpireDateStr();
            String key = comboName + expireDateStr;
            accountCombo.setEffectiveDate(new Date());
            if (mobileToComboServiceRelMap.containsKey(mobile)) {
                Map<String, Map<String, AccountCombo>> comboNameMap = mobileToComboServiceRelMap.get(mobile);
                if (comboNameMap.containsKey(key)) {
                    Map<String, AccountCombo> serviceNameMap = comboNameMap.get(key);
                    if (serviceNameMap.containsKey(serviceName)) {
                        AccountCombo combo = serviceNameMap.get(serviceName);
                        transform(result, comboInfoAndServiceRelMap, combo, serviceNameMap);
                        serviceNameMap.clear();
                    }
                    serviceNameMap.put(serviceName, accountCombo);
                } else {
                    Map<String, AccountCombo> serviceMap = Maps.newHashMap();
                    serviceMap.put(serviceName, accountCombo);
                    comboNameMap.put(key, serviceMap);
                }
            } else {
                Map<String, AccountCombo> serviceNameMap = Maps.newHashMap();
                serviceNameMap.put(serviceName, accountCombo);
                Map<String, Map<String, AccountCombo>> comboNameMap = Maps.newHashMap();
                comboNameMap.put(key, serviceNameMap);
                mobileToComboServiceRelMap.put(mobile, comboNameMap);
            }
        }

        //2. 将instanceMap转为accountComboList
        Collection<Map<String, Map<String, AccountCombo>>> comboMaps = mobileToComboServiceRelMap.values();
        if (CollectionUtils.isEmpty(comboMaps)) {
            return;
        }
        for (Map<String, Map<String, AccountCombo>> comboMap : comboMaps) {
            Collection<Map<String, AccountCombo>> serviceMaps = comboMap.values();
            if (CollectionUtils.isEmpty(serviceMaps)) {
                continue;
            }
            for (Map<String, AccountCombo> serviceMap : serviceMaps) {
                Collection<AccountCombo> importBOs = serviceMap.values();
                AccountCombo accountCombo = importBOs.iterator().next();
                transform(result, comboInfoAndServiceRelMap, accountCombo, serviceMap);
            }
        }

        for (AccountCombo combo : result) {
            AccountCombo resultCombo = accountComboService.importCombo(combo, shopId, userId);
            accountFlowService.recordFlowForComboImport(resultCombo, shopId, userId);
        }

        accountComboImportContext.setSuccessNum(accountCombos.size());
    }

    private void transform(List<AccountCombo> result, Map<String, Map<String, ComboInfoServiceRel>> comboInfoMap, AccountCombo accountCombo, Map<String, AccountCombo> serviceNameMap) {
        AccountCombo combo = new AccountCombo();
        combo.setExpireDate(accountCombo.getExpireDate());
        combo.setEffectiveDate(accountCombo.getEffectiveDate());
        combo.setComboName(accountCombo.getComboName());
        combo.setComboStatus(AccountCombo.NOT_EXHAUSTED);
        combo.setComboInfoId(accountCombo.getComboInfoId());
        combo.setAccountId(accountCombo.getAccountId());
        combo.setServiceList(new ArrayList<AccountComboServiceRel>());

        //遍历计次卡类型中的所有服务
        Map<String, ComboInfoServiceRel> infoServiceMap = comboInfoMap.get(accountCombo.getComboName());
        Set<Map.Entry<String, ComboInfoServiceRel>> infoEntries = infoServiceMap.entrySet();
        for (Map.Entry<String, ComboInfoServiceRel> entry : infoEntries) {
            ComboInfoServiceRel infoService = entry.getValue();
            AccountComboServiceRel comboService = new AccountComboServiceRel();
            comboService.setServiceId(infoService.getServiceId());
            comboService.setServiceName(infoService.getServiceName());
            comboService.setTotalServiceCount(infoService.getServiceCount());
            int usedCount = infoService.getServiceCount();
            if (serviceNameMap.containsKey(infoService.getServiceName())) {
                AccountCombo account_combo = serviceNameMap.get(infoService.getServiceName());
                Integer serviceNubmer = account_combo.getServiceCount();
                usedCount = infoService.getServiceCount() - serviceNubmer;
            }
            comboService.setUsedServiceCount(usedCount);
            combo.getServiceList().add(comboService);
        }
        result.add(combo);
    }

}
