package com.tqmall.legend.facade.customer.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.util.DateUtil;
import com.tqmall.legend.bi.dao.cube.CustomerInfoDao;
import com.tqmall.legend.bi.entity.cube.CustomerInfo;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.facade.customer.CubeCustomerInfoFacade;
import com.tqmall.legend.facade.customer.vo.CubeCustomerInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by zsy on 16/12/19.
 */
@Slf4j
@Service
public class CubeCustomerInfoFacadeImpl implements CubeCustomerInfoFacade {
    @Autowired
    private CustomerInfoDao customerInfoDao;
    @Autowired
    private ShopManagerService shopManagerService;

    /*
     * 查询cube_customer_info分页
     *
     * @param pageable
     * @param searchParams：
     * 门店id：shopId
     * 车牌,模糊检索：carLicense
     * 车主电话,模糊检索：mobile
     * 车主,模糊检索：customerName
     * 用户id,精确查询：userIds
     * 联系电话,模糊检索,car_license,mobile,customer_name,contact_mobile：searchKey
     * 是否只查询分配的车辆：isAllot
     * @return
     */
    public DefaultPage<CubeCustomerInfoVo> getCubeCustomerInfoFromSearch(Pageable pageable, Map<String, Object> searchParams) {
        List<CubeCustomerInfoVo> cubeCustomerInfoVoList = Lists.newArrayList();
        if (!searchParams.containsKey("shopId")) {
            return new DefaultPage(cubeCustomerInfoVoList);
        }
        Long shopId = (Long) searchParams.get("shopId");
        List<String> sorts = Lists.newArrayList();
        sorts.add("id desc");
        searchParams.put("sorts", sorts);
        if (searchParams.containsKey("userIds")) {
            String userIdsStr = (String) searchParams.get("userIds");
            if (StringUtils.isNotBlank(userIdsStr)) {
                String[] userIdArr = userIdsStr.split(",");
                List<Long> userIds = Lists.newArrayList();
                for (String userId : userIdArr) {
                    userIds.add(Long.parseLong(userId));
                }
                searchParams.put("userIds", userIds);
            } else {
                searchParams.remove("userIds");
            }
        }

        Integer totalSize = customerInfoDao.selectCount(searchParams);
        PageRequest pageRequest =
                new PageRequest((pageable.getPageNumber() < 1 ? 1 : pageable.getPageNumber()) - 1,
                        pageable.getPageSize() < 1 ? 1 : pageable.getPageSize(), pageable.getSort());

        searchParams.put("limit", pageRequest.getPageSize());
        searchParams.put("offset", pageRequest.getOffset());

        List<CustomerInfo> customerInfoList = customerInfoDao.select(searchParams);
        if (CollectionUtils.isNotEmpty(customerInfoList)) {
            //设置员工姓名
            Map<Long, ShopManager> shopManagerMap = Maps.newHashMap();
            List<ShopManager> shopManagerList = shopManagerService.selectByShopId(shopId);
            for (ShopManager shopManager : shopManagerList) {
                shopManagerMap.put(shopManager.getId(), shopManager);
            }
            for (CustomerInfo customerInfo : customerInfoList) {
                CubeCustomerInfoVo cubeCustomerInfoVo = new CubeCustomerInfoVo();
                Long userId = customerInfo.getUserId();
                if (shopManagerMap.containsKey(userId)) {
                    ShopManager shopManager = shopManagerMap.get(userId);
                    cubeCustomerInfoVo.setUserName(shopManager.getName());
                }
                cubeCustomerInfoVo.setId(customerInfo.getId());
                cubeCustomerInfoVo.setCustomerCarId(customerInfo.getCustomerCarId());
                cubeCustomerInfoVo.setCustomerId(customerInfo.getCustomerId());
                cubeCustomerInfoVo.setGmtCreate(DateUtil.convertDateToYMDHMS(customerInfo.getGmtCreate()));
                cubeCustomerInfoVo.setGmtModified(DateUtil.convertDateToYMDHMS(customerInfo.getGmtModified()));
                cubeCustomerInfoVo.setLastPayTime(DateUtil.convertDateToYMDHMS(customerInfo.getLastPayTime()));
                cubeCustomerInfoVo.setTotalAmount(customerInfo.getTotalAmount());
                Long totalNumber = customerInfo.getTotalNumber();
                if (totalNumber != null) {
                    cubeCustomerInfoVo.setTotalNumber(totalNumber.intValue());
                }
                cubeCustomerInfoVo.setCarLicense(customerInfo.getCarLicense());
                cubeCustomerInfoVo.setMobile(customerInfo.getMobile());
                cubeCustomerInfoVo.setCustomerName(customerInfo.getCustomerName());
                cubeCustomerInfoVo.setUserId(customerInfo.getUserId());
                cubeCustomerInfoVo.setShopId(customerInfo.getShopId());
                cubeCustomerInfoVoList.add(cubeCustomerInfoVo);
            }
        }
        return new DefaultPage<CubeCustomerInfoVo>(cubeCustomerInfoVoList, pageRequest, totalSize);
    }
}
