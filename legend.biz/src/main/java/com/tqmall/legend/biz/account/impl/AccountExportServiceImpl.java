package com.tqmall.legend.biz.account.impl;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tqmall.legend.biz.account.AccountExportService;
import com.tqmall.legend.biz.account.param.ComboExportArgs;
import com.tqmall.legend.biz.account.param.MemberExportArgs;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.dao.account.AccountExportDao;
import com.tqmall.legend.dao.account.param.ComboExportPagedParam;
import com.tqmall.legend.dao.account.param.ComboExportParam;
import com.tqmall.legend.dao.account.param.MemberExportPagedParam;
import com.tqmall.legend.dao.account.param.MemberExportParam;
import com.tqmall.legend.entity.account.ComboExportEntity;
import com.tqmall.legend.entity.account.ComboExportSummary;
import com.tqmall.legend.entity.account.MemberExportEntity;
import com.tqmall.legend.entity.account.MemberExportSummary;

/**
 * Created by majian on 17/1/5.
 */
@Service
@Slf4j
public class AccountExportServiceImpl implements AccountExportService {
    @Autowired
    private AccountExportDao dao;
    @Autowired
    private CustomerCarService carService;

    @Override
    public Page<MemberExportEntity> pageMemeber(Pageable pageRequest, MemberExportArgs args) {
        MemberExportParam param = new MemberExportParam();
        BeanUtils.copyProperties(args, param);
        MemberExportPagedParam pagedParam = new MemberExportPagedParam(pageRequest, param);
        List<MemberExportEntity> content = dao.pageMember(pagedParam);
        List<Long> customerIds = Lists.transform(content, new Function<MemberExportEntity, Long>() {
            @Override
            public Long apply(MemberExportEntity input) {
                return input.getCustomerId();
            }
        });
        Map<Long, String> customerId2LicensesMap = carService.mapCustomerId2Licenses(args.getShopId(), customerIds);
        for (MemberExportEntity item : content) {
            String licenses = customerId2LicensesMap.get(item.getCustomerId());
            item.setLicenses(licenses);
        }
        Integer totalElements = dao.countMember(param);
        return new PageImpl<>(content, pageRequest, totalElements);
    }


    @Override
    public Page<ComboExportEntity> pageCombo(Pageable pageRequest, ComboExportArgs args) {
        ComboExportParam param = new ComboExportParam();
        BeanUtils.copyProperties(args, param);
        ComboExportPagedParam pagedParam = new ComboExportPagedParam(pageRequest, param);
        List<ComboExportEntity> content = dao.pageCombo(pagedParam);
        List<Long> customerIds = Lists.transform(content, new Function<ComboExportEntity, Long>() {
            @Override
            public Long apply(ComboExportEntity input) {
                return input.getCustomerId();
            }
        });
        Map<Long, String> customerId2LicensesMap = carService.mapCustomerId2Licenses(args.getShopId(), customerIds);
        for (ComboExportEntity item : content) {
            String licenses = customerId2LicensesMap.get(item.getCustomerId());
            item.setLicenses(licenses);
        }
        Integer totalElements = dao.countCombo(param);
        return new PageImpl<>(content, pageRequest, totalElements);
    }

    @Override
    public MemberExportSummary summarizeMember(MemberExportArgs args) {
        MemberExportParam param = new MemberExportParam();
        BeanUtils.copyProperties(args, param);
        return dao.summarizeMember(param);
    }

    @Override
    public ComboExportSummary summarizeCombo(ComboExportArgs args) {
        ComboExportParam param = new ComboExportParam();
        BeanUtils.copyProperties(args, param);
        return dao.summarizeCombo(param);
    }


}
