package com.tqmall.legend.biz.agreement.impl;

import com.tqmall.legend.biz.agreement.ShopSignAgreementService;
import com.tqmall.legend.dao.agreement.ShopSignAgreementDao;
import com.tqmall.legend.entity.agreement.ShopSignAgreement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by litan on 17/2/9.
 */
@Service
public class ShopSignAgreementServiceImpl implements ShopSignAgreementService{

    @Autowired
    private ShopSignAgreementDao shopSignAgreementDao;
    /**
     * 新增
     *
     * @param shopSignAgreement
     * @return
     */
    @Override
    public Integer add(ShopSignAgreement shopSignAgreement) {
        return shopSignAgreementDao.insert(shopSignAgreement);
    }
}
