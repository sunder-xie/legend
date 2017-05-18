package com.tqmall.legend.biz.shop.impl;

import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.shop.GiftService;
import com.tqmall.legend.common.Result;
import com.tqmall.legend.dao.customer.CustomerCarDao;
import com.tqmall.legend.dao.privilege.ShopManagerDao;
import com.tqmall.legend.dao.shop.GiftDao;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.shop.Gift;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GiftServiceImpl extends BaseServiceImpl implements GiftService{

    Logger logger = LoggerFactory.getLogger(GiftServiceImpl.class);

    @Autowired
    private GiftDao giftDao;
    @Autowired
    private ShopManagerDao shopManagerDao;
    @Autowired
    private CustomerCarDao customerCarDao;

    /**
     * 添加记录
     * @param gift
     * @return
     */
    public Result add(Gift gift) {

        try {
            Long customerCarId = gift.getCustomerCarId();
            CustomerCar customerCar = customerCarDao.selectById(customerCarId);
            if (null == customerCar) {
                return Result.wrapErrorResult("-1","该车辆信息不存在,请确认!");
            }
            Long registrantId = gift.getRegistrantId();
            if (StringUtils.isNumeric(registrantId + "")) {
                ShopManager shopManager = shopManagerDao.selectById(registrantId);
                if (null != shopManager) {
                    gift.setRegistrantName(shopManager.getName());
                    logger.info("礼品发放成功! 发放人:{}", shopManager.getName());
                }
            }
            Integer cnt = giftDao.insert(gift);
            if (cnt > 0) {
                return Result.wrapSuccessfulResult(true);
            } else {
                logger.info("保存礼品信息失败!{}", gift);
                return Result.wrapErrorResult("-1","保存礼品信息失败!");

            }
        } catch (Exception e) {
            logger.info("保存礼品信息异常!{}", e);
            return Result.wrapErrorResult("-1","保存礼品信息异常");
        }
    }

    /**
     * create by jason 2015-08-03
     * 获得所有礼品发放记录
     *
     */
    public Page<Gift> getPageGiftInfo(Pageable pageable, Map<String, Object> searchParams) {
        //总数
        Integer totalSize = giftDao.selectCount(searchParams);
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());
        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());

        List<String> sorts = new ArrayList<>();
        sorts.add("gmtCreate desc");
        searchParams.put("sorts", sorts);
        List<Gift> data = null;
        try {
            data = giftDao.select(searchParams);
        } catch (Exception e) {
            logger.error("获得礼品发放数据异常!{}", e);
        }

        DefaultPage<Gift> page = new DefaultPage<Gift>(data, pageRequest, totalSize);
        return page;
    }
}
