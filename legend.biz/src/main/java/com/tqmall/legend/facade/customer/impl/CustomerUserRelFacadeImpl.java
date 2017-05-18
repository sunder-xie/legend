package com.tqmall.legend.facade.customer.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.Constants;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.BdUtil;
import com.tqmall.common.util.log.LogUtils;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.RpcCustomerInfoService;
import com.tqmall.cube.shop.RpcTagService;
import com.tqmall.cube.shop.param.customer.AllotCarParam;
import com.tqmall.cube.shop.result.CustomerInfoDTO;
import com.tqmall.cube.shop.result.tag.BriefCustomerDTO;
import com.tqmall.legend.biz.customer.CustomerUserRelService;
import com.tqmall.legend.biz.marketing.ng.MarketingCenterService;
import com.tqmall.legend.biz.order.OrderInfoService;
import com.tqmall.legend.biz.privilege.ShopManagerService;
import com.tqmall.legend.biz.pvg.IPvgUserOrgService;
import com.tqmall.legend.entity.customer.CustomerUserRel;
import com.tqmall.legend.entity.marketing.ng.CustomerInfo;
import com.tqmall.legend.entity.privilege.ShopManager;
import com.tqmall.legend.entity.pvg.PvgUserOrg;
import com.tqmall.legend.facade.customer.CustomerUserRelFacade;
import com.tqmall.legend.facade.customer.bo.AllotBo;
import com.tqmall.legend.facade.customer.bo.CustomAllotBo;
import com.tqmall.legend.facade.customer.bo.CustomerUserRelAllotBo;
import com.tqmall.legend.facade.customer.bo.UnbindingBo;
import com.tqmall.legend.facade.customer.vo.AllotResultVo;
import com.tqmall.legend.facade.customer.vo.AllotUserVo;
import com.tqmall.search.common.data.PageableRequest;
import com.tqmall.wheel.support.data.Page;
import com.tqmall.wheel.support.rpc.param.PagedParam;
import com.tqmall.wheel.support.rpc.result.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Set;

/**
 * Created by zsy on 16/12/15.
 */
@Slf4j
@Service
public class CustomerUserRelFacadeImpl implements CustomerUserRelFacade {
    @Autowired
    private CustomerUserRelService customerUserRelService;
    @Autowired
    private ShopManagerService shopManagerService;
    @Autowired
    private IPvgUserOrgService pvgUserOrgService;
    @Autowired
    private OrderInfoService orderInfoService;
    @Autowired
    private RpcTagService rpcTagService;
    @Autowired
    private MarketingCenterService marketingCenterService;
    @Autowired
    private RpcCustomerInfoService rpcCustomerInfoService;

    @Override
    public boolean isAllot(Long shopId, Long userId) {
        Integer selectCount = customerUserRelService.selectCount(shopId, userId);
        if (selectCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Integer userAllotCount(Long shopId, Long userId) {
        return customerUserRelService.selectCount(shopId, userId);
    }

    /**
     * 单个、多个客户调整接口
     *
     * @param shopId            门店id
     * @param newUserId         员工id
     * @param customerCarIdList 车辆id集合   包含未分配、已分配（原userId和新userId相同情况）
     * @param operatorId        操作人
     * @return 分配成功的车辆数
     */
    @Override
    @Transactional
    public Integer allotCustomerCars(Long shopId, Long newUserId, List<Long> customerCarIdList, Long operatorId) {
        log.info("客户调整，shopId：{}，操作人：{}，新员工id：{}，车辆ids：{}", shopId, operatorId, newUserId, LogUtils.objectToString(customerCarIdList));
        List<CustomerUserRel> customerUserRelList = customerUserRelService.selectByShopIdAndCustomerCarIds(shopId, customerCarIdList);
        List<Long> newCustomerCarIdList = Lists.newArrayList();//需要调整的车辆ids
        Map<Long,Object> existCustomerCarIdMap = Maps.newHashMap();//存在的车辆idMap
        if (CollectionUtils.isNotEmpty(customerUserRelList)) {
            Iterator<CustomerUserRel> iterator = customerUserRelList.iterator();
            //去除新员工与原员工相同情况
            while (iterator.hasNext()) {
                CustomerUserRel customerUserRel = iterator.next();
                Long oldUserId = customerUserRel.getUserId();
                Long customerCarId = customerUserRel.getCustomerCarId();
                if (oldUserId.equals(newUserId)) {
                    iterator.remove();
                } else {
                    newCustomerCarIdList.add(customerCarId);
                }
                existCustomerCarIdMap.put(customerCarId, null);
            }
            if (CollectionUtils.isNotEmpty(newCustomerCarIdList)) {
                //删除原绑定关系
                bachDeleteCustomerUserRel(shopId, newCustomerCarIdList, operatorId);
            }
        }
        //设置未分配的客户的归属关系
        List<CustomerUserRel> needInsertList = Lists.newArrayList();
        Set<Long> catIds = Sets.newHashSet();
        for (Long carId : customerCarIdList) {
            if(!existCustomerCarIdMap.containsKey(carId)){
                setInsertCustomerUserRel(shopId, newUserId, needInsertList, carId);
                newCustomerCarIdList.add(carId);
                catIds.add(carId);
            }
        }
        customerUserRelList.addAll(needInsertList);
        if (CollectionUtils.isEmpty(customerUserRelList)) {
            throw new BizException("员工相同，无需调整");
        }
        if (CollectionUtils.isNotEmpty(catIds)) {
            Result<Map<Long, CustomerInfoDTO>> customerInfoByCarIdsResult = rpcCustomerInfoService.findCustomerInfoByCarIds(shopId, catIds);
            if (!customerInfoByCarIdsResult.isSuccess()) {
                log.error("客户归属调整，获取客户信息异常异常，门店id：{}，车辆ids：{}", shopId, LogUtils.objectToString(catIds));
                throw new BizException("获取客户信息异常，请稍后再试");
            }
            Map<Long, CustomerInfoDTO> customerInfoDTOMap = customerInfoByCarIdsResult.getData();
            for(CustomerUserRel customerUserRel : needInsertList){
                Long carId = customerUserRel.getCustomerCarId();
                if(customerInfoDTOMap.containsKey(carId)){
                    CustomerInfoDTO customerInfoDTO = customerInfoDTOMap.get(carId);
                    //设置客户id
                    customerUserRel.setCustomerId(customerInfoDTO.getCustomerId());
                }
            }
        }
        //插入新绑定关系
        allotCustomerUserRel(newUserId, operatorId, customerUserRelList);
        Result<Integer> allotCustomerCarsResult = rpcCustomerInfoService.allotCustomerCars(shopId, newUserId, newCustomerCarIdList);
        if (!allotCustomerCarsResult.isSuccess()) {
            throw new BizException("客户调整失败，请稍后再试");
        }
        return allotCustomerCarsResult.getData();
    }

    /**
     * 设置需要添加的归属信息
     *
     * @param shopId
     * @param newUserId
     * @param needInsertList
     * @param carId
     */
    private void setInsertCustomerUserRel(Long shopId, Long newUserId, List<CustomerUserRel> needInsertList, Long carId) {
        CustomerUserRel customerUserRel = new CustomerUserRel();
        customerUserRel.setShopId(shopId);
        customerUserRel.setCustomerCarId(carId);
        customerUserRel.setUserId(newUserId);
        needInsertList.add(customerUserRel);
    }

    /**
     * 分配新客户给新员工
     * 先删除原员工绑定关系，后调整成新的员工
     *
     * @param newUserId
     * @param operatorId
     * @param customerUserRelList
     */
    private void allotCustomerUserRel(Long newUserId, Long operatorId, List<CustomerUserRel> customerUserRelList) {
        String uuid = UUID.randomUUID().toString();//批次号
        for (CustomerUserRel customerUserRel : customerUserRelList) {
            customerUserRel.setId(null);
            customerUserRel.setGmtCreate(null);
            customerUserRel.setGmtModified(null);
            customerUserRel.setCreator(operatorId);
            customerUserRel.setUserId(newUserId);
            customerUserRel.setAllotSn(uuid);
        }
        customerUserRelService.batchInsert(customerUserRelList);
    }

    /**
     * 解绑车辆
     *
     * @param shopId 门店id
     * @param userId 预解绑的员工id
     * @return 解绑成功的车辆数
     */
    @Override
    @Transactional
    public Integer unAllotByUserId(Long shopId, Long userId, Long operatorId) {
        log.info("客户全部解绑，shopId：{}，操作人：{}，解绑员工id：{}", shopId, operatorId, userId);
        bachDeleteCustomerUserRel(shopId, userId, operatorId);
        Result<Integer> unallotByUserIdResult = rpcCustomerInfoService.unallotByUserId(shopId, userId);
        if (!unallotByUserIdResult.isSuccess()) {
            throw new BizException("解绑失败，请稍后再试");
        }
        return unallotByUserIdResult.getData();
    }

    /**
     * 全部删除客户绑定信息
     * @param shopId
     * @param userId
     * @param operatorId
     */
    private void bachDeleteCustomerUserRel(Long shopId, Long userId, Long operatorId) {
        bachDeleteCustomerUserRel(shopId, userId, null, operatorId);
    }

    /**
     * 删除指定车辆绑定信息
     * @param shopId
     * @param customerCarIds
     * @param operatorId
     */
    private void bachDeleteCustomerUserRel(Long shopId, List<Long> customerCarIds, Long operatorId) {
        bachDeleteCustomerUserRel(shopId, null, customerCarIds, operatorId);
    }

    private void bachDeleteCustomerUserRel(Long shopId, Long userId, List<Long> customerCarIds, Long operatorId) {
        UnbindingBo unbindingBo = new UnbindingBo();
        unbindingBo.setShopId(shopId);
        unbindingBo.setUserId(userId);
        unbindingBo.setCustomerCarIds(customerCarIds);
        unbindingBo.setOperatorId(operatorId);
        customerUserRelService.batchDelete(unbindingBo);
    }

    /**
     * 解绑车辆
     *
     * @param shopId        门店id
     * @param customerCarId 车辆id集合
     * @return 成功解绑的车辆数
     */
    @Override
    @Transactional
    public Integer unAllotCustomerCarIds(Long shopId, Long operatorId, Long... customerCarId) {
        //删除原客户，同步cube
        log.info("客户解绑，shopId：{}，操作人：{}，解绑车辆：{}", shopId, operatorId, customerCarId);
        List<Long> customerCarIds = Lists.newArrayList();
        for (Long carId : customerCarId) {
            customerCarIds.add(carId);
        }
        List<CustomerUserRel> customerUserRelList = customerUserRelService.selectByShopIdAndCustomerCarIds(shopId,customerCarIds);
        if(CollectionUtils.isEmpty(customerUserRelList)){
            throw new BizException("没有绑定的数据，无需解绑");
        }
        bachDeleteCustomerUserRel(shopId, customerCarIds, operatorId);
        Result<Integer> unallotCustomerCarIdsResult = rpcCustomerInfoService.unallotCustomerCarIds(shopId, customerCarId);
        if (!unallotCustomerCarIdsResult.isSuccess()) {
            throw new BizException("解绑失败，请稍后再试");
        }
        return unallotCustomerCarIdsResult.getData();
    }

    /**
     * 车辆归属迁移
     *
     * @param shopId       门店id
     * @param oldUserId 原员工id
     * @param newUserId 目标员工id
     * @return 成功迁移的车辆数
     */
    @Override
    @Transactional
    public Integer transformAllotCustomerCars(Long shopId, Long oldUserId, Long newUserId, Long operatorId) {
        log.info("【客户归属调整】操作人id：{}，：shopId：{}，原员工id：{}，目标员工id：{}", operatorId, shopId, oldUserId, newUserId);
        List<CustomerUserRel> customerUserRelList = customerUserRelService.selectByUserId(oldUserId);
        if (CollectionUtils.isEmpty(customerUserRelList)) {
            return 0;
        }
        //删除原绑定关系
        bachDeleteCustomerUserRel(shopId,oldUserId,operatorId);
        //添加新绑定关系
        allotCustomerUserRel(newUserId, operatorId, customerUserRelList);
        Result<Integer> transformAllotCustomerCarsResult = rpcCustomerInfoService.transformAllotCustomerCars(shopId, oldUserId, newUserId);
        if (!transformAllotCustomerCarsResult.isSuccess()) {
            throw new BizException("客户调整失败，请稍后再试");
        }
        return transformAllotCustomerCarsResult.getData();
    }

    /**
     * 获取归属员工列表 （店长+老板+服务顾问）
     *
     * @param shopId 门店id
     * @return
     */
    @Override
    public List<AllotUserVo> getAllotUserList(Long shopId) {
        List<AllotUserVo> allotUserVoList = Lists.newArrayList();
        //获取门店所有员工信息
        List<ShopManager> shopManagerList = shopManagerService.selectByShopId(shopId);
        if (CollectionUtils.isEmpty(shopManagerList)) {
            return allotUserVoList;
        }
        List<Long> userIds = Lists.newArrayList();
        for (ShopManager shopManager : shopManagerList) {
            Long id = shopManager.getId();
            userIds.add(id);
        }
        //查询所有员工的角色
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("userIds", userIds);
        List<PvgUserOrg> pvgUserOrgList = pvgUserOrgService.select(searchMap);
        Map<Long, PvgUserOrg> pvgUserOrgMap = Maps.newHashMap();
        for (PvgUserOrg pvgUserOrg : pvgUserOrgList) {
            Long pvgRoleId = pvgUserOrg.getPvgRoleId();
            //店长（2）+老板（管理员1)
            if (Long.compare(pvgRoleId, 2l) != 1) {
                pvgUserOrgMap.put(pvgUserOrg.getUserId(), null);
            }
        }
        //查询所有开过工单的服务顾问ids
        List<Long> receiverList = orderInfoService.selectReceiverByShopId(shopId);
        for (Long receiver : receiverList) {
            pvgUserOrgMap.put(receiver, null);
        }
        for (ShopManager shopManager : shopManagerList) {
            Integer isAdmin = shopManager.getIsAdmin();
            Long userId = shopManager.getId();
            if (isAdmin == 1 || pvgUserOrgMap.containsKey(userId)) {
                AllotUserVo allotUserVo = buildAllotUserVo(shopManager);
                allotUserVoList.add(allotUserVo);
            }
        }
        return allotUserVoList;
    }

    @Override
    public List<AllotUserVo> getAllotUserList(Long shopId, Boolean isAllot) {
        return getAllotUserList(shopId, isAllot, null);
    }

    /**
     * 获取归属员工列表
     *
     * @param shopId       门店id
     * @param isAllot      是否只查询分配过的员工，false:否 true:是，默认false
     * @param choseUserIds 已选中的员工 , 有值，则过滤此员工
     * @return
     */
    @Override
    public List<AllotUserVo> getAllotUserList(Long shopId, Boolean isAllot, List<Long> choseUserIds) {
        List<AllotUserVo> allotUserVoList = Lists.newArrayList();
        Map<String, Object> searchMap = Maps.newHashMap();
        if (isAllot != null && isAllot) {
            //查询已分配的员工ids
            List<Long> allotUserIds = customerUserRelService.selectAllotUserIds(shopId);
            if (CollectionUtils.isEmpty(allotUserIds)) {
                return allotUserVoList;
            }
            searchMap.put("ids", allotUserIds);
        }
        Map<Long, Object> choseUserIdsMap = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(choseUserIds)) {
            for (Long userId : choseUserIds) {
                choseUserIdsMap.put(userId, null);
            }
        }
        //获取门店所有员工信息
        searchMap.put("shopId", shopId);
        List<ShopManager> shopManagerList = shopManagerService.select(searchMap);
        for (ShopManager shopManager : shopManagerList) {
            Long userId = shopManager.getId();
            if (!choseUserIdsMap.containsKey(userId)) {
                AllotUserVo allotUserVo = buildAllotUserVo(shopManager);
                allotUserVoList.add(allotUserVo);
            }
        }
        return allotUserVoList;
    }

    @Override
    public AllotUserVo getAllotUserByCarId(Long carId) {
        CustomerUserRel customerUserRel = customerUserRelService.selectByCustomerCarId(carId);
        if (customerUserRel == null) {
            return null;
        }
        Long userId = customerUserRel.getUserId();
        ShopManager shopManager = shopManagerService.selectById(userId);
        AllotUserVo allotUserVo = buildAllotUserVo(shopManager);
        return allotUserVo;
    }

    /**
     * 根据门店id,车辆ids获取分配的员工信息
     *
     * @param shopId
     * @param carIds
     * @return key为carId，value为服务顾问对象
     */
    @Override
    public Map<Long, AllotUserVo> getAllotUserMapByShopIdAndCarIds(Long shopId, List<Long> carIds) {
        Map<Long, AllotUserVo> allotUserVoMap = Maps.newHashMap();
        if (CollectionUtils.isEmpty(carIds)) {
            return allotUserVoMap;
        }
        List<CustomerUserRel> customerUserRelList = customerUserRelService.selectByShopIdAndCustomerCarIds(shopId, carIds);
        if (CollectionUtils.isEmpty(customerUserRelList)) {
            return allotUserVoMap;
        }
        List<Long> userIds = Lists.newArrayList();
        Map<Long, CustomerUserRel> customerUserRelMap = Maps.newHashMap();
        for (CustomerUserRel customerUserRel : customerUserRelList) {
            userIds.add(customerUserRel.getUserId());
            customerUserRelMap.put(customerUserRel.getUserId(), customerUserRel);
        }
        //获取门店所有员工信息
        Map<String, Object> searchMap = Maps.newHashMap();
        searchMap.put("shopId", shopId);
        searchMap.put("ids", userIds);
        List<ShopManager> shopManagerList = shopManagerService.select(searchMap);
        for (ShopManager shopManager : shopManagerList) {
            AllotUserVo allotUserVo = buildAllotUserVo(shopManager);
            Long userId = shopManager.getId();
            if (customerUserRelMap.containsKey(userId)) {
                CustomerUserRel customerUserRel = customerUserRelMap.get(userId);
                Long customerCarId = customerUserRel.getCustomerCarId();
                allotUserVoMap.put(customerCarId, allotUserVo);
            }
        }
        return allotUserVoMap;
    }

    @Override
    public List<AllotResultVo> allot(AllotBo allotBo) {
        //查询需要分配的结果
        String allotSn = UUID.randomUUID().toString();//批次号
        Map<Long, AllotResultVo> allotResultVoMap = Maps.newHashMap();
        //设置标记位
        CustomerUserRelAllotBo customerUserRelAllotBo = new CustomerUserRelAllotBo();
        //分配操作
        while (true) {
            PagedParam pagedParam = new PagedParam();
            pagedParam.setShopId(allotBo.getShopId());
            pagedParam.setPageNum(1);//100条数据查询
            pagedParam.setPageSize(Constants.MED_PAGE_SIZE);//100条数据查询
            RpcResult<Page<BriefCustomerDTO>> listBriefCustomerResult = rpcTagService.listBriefCustomer(pagedParam);
            if (listBriefCustomerResult == null || !listBriefCustomerResult.isSuccess()) {
                break;
            }
            Page<BriefCustomerDTO> briefCustomerDTOPage = listBriefCustomerResult.getData();
            if (briefCustomerDTOPage == null || CollectionUtils.isEmpty(briefCustomerDTOPage.getRecords())) {
                break;
            }
            List<BriefCustomerDTO> briefCustomerDTOList = briefCustomerDTOPage.getRecords();
            Map<Long, AllotResultVo> thisAllotResultVoMap = Maps.newHashMap();//本次分配结果
            try {
                allot(briefCustomerDTOList, allotBo, allotSn, allotResultVoMap, thisAllotResultVoMap, customerUserRelAllotBo);
            } catch (BizException e) {
                //减去本次分配的数
                for (Long userId : allotResultVoMap.keySet()) {
                    AllotResultVo allotResultVo = allotResultVoMap.get(userId);
                    Integer active = allotResultVo.getActive();
                    Integer lazy = allotResultVo.getLazy();
                    Integer lost = allotResultVo.getLost();
                    Integer allot = allotResultVo.getAllot();
                    if (thisAllotResultVoMap.containsKey(userId)) {
                        AllotResultVo thisAllotResultVo = thisAllotResultVoMap.get(userId);
                        active -= thisAllotResultVo.getActive();
                        lazy -= thisAllotResultVo.getLazy();
                        lost -= thisAllotResultVo.getLost();
                        allot -= thisAllotResultVo.getAllot();
                    }
                    allotResultVo.setActive(active);
                    allotResultVo.setLazy(lazy);
                    allotResultVo.setLost(lost);
                    allotResultVo.setAllot(allot);
                }
                break;
            }
        }
        //返回数据
        List<Long> choseUserIds = allotBo.getChoseUserIds();
        List<AllotResultVo> allotResultVoList = Lists.newArrayList();
        //查询客户
        Long[] userIds = choseUserIds.toArray(new Long[choseUserIds.size()]);
        Map<Long, ShopManager> shopManagerMap = shopManagerService.getMapByByIds(userIds);
        //查询客户归属
        Map<Long, Integer> userNumMap = customerUserRelService.selectAllotUserNumMapByUserIds(allotBo.getShopId(), userIds);
        for (Long userId : choseUserIds) {
            AllotResultVo allotResultVo;
            if (allotResultVoMap.containsKey(userId)) {
                allotResultVo = allotResultVoMap.get(userId);
            } else {
                allotResultVo = new AllotResultVo();
                allotResultVo.setLost(0);
                allotResultVo.setLazy(0);
                allotResultVo.setActive(0);
                allotResultVo.setAllot(0);
            }
            if (shopManagerMap.containsKey(userId)) {
                ShopManager shopManager = shopManagerMap.get(userId);
                allotResultVo.setUserName(shopManager.getName());
            }
            Integer totalAllot = 0;
            if(userNumMap.containsKey(userId)){
                totalAllot = userNumMap.get(userId);
            }
            allotResultVo.setTotalAllot(totalAllot);
            allotResultVoList.add(allotResultVo);
        }
        return allotResultVoList;
    }

    @Override
    public List<AllotResultVo> customAllot(CustomAllotBo customAllotBo) {
        //查询搜索
        Map<String, Object> searchParams = getSearchMap(customAllotBo);
        String sort = "customer_id";
        String allotSn = UUID.randomUUID().toString();//批次号
        Map<Long, Integer> allotResultVoMap = Maps.newHashMap();
        //设置标记位
        CustomerUserRelAllotBo customerUserRelAllotBo = new CustomerUserRelAllotBo();
        while (true) {
            Pageable pageable = new PageableRequest(1, Constants.MED_PAGE_SIZE, org.springframework.data.domain.Sort.Direction.ASC, sort);
            org.springframework.data.domain.Page<CustomerInfo> customerInfoPage = marketingCenterService.selectAccurate(searchParams, pageable);
            if (customerInfoPage == null || CollectionUtils.isEmpty(customerInfoPage.getContent())) {
                break;
            }
            List<CustomerInfo> customerInfoList = customerInfoPage.getContent();
            Map<Long, Integer> thisAllotResultVoMap = Maps.newHashMap();//本次分配结果
            try {
                customAllot(customerInfoList, customAllotBo, allotSn, allotResultVoMap, thisAllotResultVoMap, customerUserRelAllotBo);
            } catch (BizException e) {
                for (Long userId : allotResultVoMap.keySet()) {
                    if (thisAllotResultVoMap.containsKey(userId)) {
                        Integer count = allotResultVoMap.get(userId);
                        count -= thisAllotResultVoMap.get(userId);
                        allotResultVoMap.put(userId, count);
                    }
                }
                break;
            }
        }
        //返回数据
        List<Long> choseUserIds = customAllotBo.getChoseUserIds();
        List<AllotResultVo> allotResultVoList = Lists.newArrayList();
        //查询客户
        Long[] userIds = choseUserIds.toArray(new Long[choseUserIds.size()]);
        Map<Long, ShopManager> shopManagerMap = shopManagerService.getMapByByIds(userIds);
        for (Long userId : choseUserIds) {
            AllotResultVo allotResultVo = new AllotResultVo();
            Integer totalAllot = 0;
            if (allotResultVoMap.containsKey(userId)) {
                totalAllot = allotResultVoMap.get(userId);
            }
            if (shopManagerMap.containsKey(userId)) {
                ShopManager shopManager = shopManagerMap.get(userId);
                allotResultVo.setUserName(shopManager.getName());
            }
            allotResultVo.setTotalAllot(totalAllot);
            allotResultVoList.add(allotResultVo);
        }
        return allotResultVoList;
    }

    private Map<String, Object> getSearchMap(CustomAllotBo customAllotBo) {
        Map<String, Object> searchParams = BdUtil.beanToMap(customAllotBo);
        //去除无用查询防止留坑
        if (searchParams.containsKey("size")) {
            searchParams.remove("size");
        }
        if (searchParams.containsKey("choseUserIds")) {
            searchParams.remove("choseUserIds");
        }
        if (searchParams.containsKey("daySign")) {
            searchParams.put("daySign", HtmlUtils.htmlUnescape(searchParams.get("daySign").toString()));
        }
        if (searchParams.containsKey("numberSign")) {
            searchParams.put("numberSign", HtmlUtils.htmlUnescape(searchParams.get("numberSign").toString()));
        }
        return searchParams;
    }

    private void customAllot(List<CustomerInfo> customerInfoList, CustomAllotBo customAllotBo, String allotSn, Map<Long, Integer> allotResultVoMap, Map<Long, Integer> thisAllotResultVoMap, CustomerUserRelAllotBo customerUserRelAllotBo) throws BizException {
        if (CollectionUtils.isEmpty(customerInfoList)) {
            return;
        }
        List<Long> choseUserIds = customAllotBo.getChoseUserIds();
        Long shopId = customAllotBo.getShopId();
        Long operatorId = customAllotBo.getOperatorId();
        int size = choseUserIds.size();//长度
        List<CustomerUserRel> insertList = Lists.newArrayList();
        Integer index = customerUserRelAllotBo.getIndex();
        Long preCustomerId = customerUserRelAllotBo.getPreCustomerId();
        Map<Long, List<Long>> cubeCustomerInfoMap = Maps.newHashMap();//批量更新用户归属map
        for (CustomerInfo customerInfo : customerInfoList) {
            Long customerCarId = customerInfo.getCustomerCarId();
            Long customerId = customerInfo.getCustomerId();
            CustomerUserRel customerUserRel = buildCustomerUserRel(allotSn, shopId, customerCarId, customerId, operatorId);
            //解绑客户，或者客户id和上一个客户id不相同时，分配给另一个员工
            if (!customerUserRelAllotBo.isUseIndex()) {
                customerUserRelAllotBo.setUseIndex(true);
            } else if (customerId == null || Long.compare(customerId, 0l) == 0 || !customerId.equals(preCustomerId)) {
                //customerId为空，0 或不等于上一条数据的customerId时，分配给下一个员工
                index++;
            }
            if (index > size - 1) {
                //从第一个员工开始分配
                index = 0;
            }
            //客户相等绑定同一个员工
            Long userId = choseUserIds.get(index);
            customerUserRel.setUserId(userId);
            insertList.add(customerUserRel);
            preCustomerId = customerId;
            setCustomAllotResultVoMap(allotResultVoMap, userId);
            setCustomAllotResultVoMap(thisAllotResultVoMap, userId);
            List<Long> customerCarIds;
            if (cubeCustomerInfoMap.containsKey(userId)) {
                customerCarIds = cubeCustomerInfoMap.get(userId);
            } else {
                customerCarIds = Lists.newArrayList();
            }
            customerCarIds.add(customerCarId);
            cubeCustomerInfoMap.put(userId, customerCarIds);
        }
        customerUserRelAllotBo.setIndex(index);
        customerUserRelAllotBo.setPreCustomerId(preCustomerId);
        //调用cube更新数据
        synCubeCustomerInfo(insertList, shopId, cubeCustomerInfoMap);
    }

    private void setCustomAllotResultVoMap(Map<Long, Integer> allotResultVoMap, Long userId) {
        Integer count = 1;
        if (allotResultVoMap.containsKey(userId)) {
            Integer oldCount = allotResultVoMap.get(userId);
            count += oldCount;
        }
        allotResultVoMap.put(userId, count);
    }

    private void allot(List<BriefCustomerDTO> briefCustomerDTOList, AllotBo allotBo, String allotSn, Map<Long, AllotResultVo> allotResultVoMap, Map<Long, AllotResultVo> thisAllotResultVoMap, CustomerUserRelAllotBo customerUserRelAllotBo) throws BizException {
        List<Long> choseUserIds = allotBo.getChoseUserIds();
        Map<Long, Long> choseUserIdsMap = Maps.newHashMap();
        for (Long userId : choseUserIds) {
            choseUserIdsMap.put(userId, userId);
        }
        Long shopId = allotBo.getShopId();
        Long operatorId = allotBo.getOperatorId();
        int size = choseUserIds.size();//长度
        List<CustomerUserRel> insertList = Lists.newArrayList();
        Integer index = customerUserRelAllotBo.getIndex();
        Long preCustomerId = customerUserRelAllotBo.getPreCustomerId();
        Long preUserId = customerUserRelAllotBo.getPreUserId();
        Map<Long, List<Long>> cubeCustomerInfoMap = Maps.newHashMap();//批量更新用户归属map
        for (BriefCustomerDTO briefCustomerDTO : briefCustomerDTOList) {
            Long customerCarId = briefCustomerDTO.getCarId();
            Long customerId = briefCustomerDTO.getCustomerId();
            //客户相等绑定同一个员工
            CustomerUserRel customerUserRel = buildCustomerUserRel(allotSn, shopId, customerCarId, customerId, operatorId);
            Long userId = 0l;
            BriefCustomerDTO.TypeEnum typeEnum = briefCustomerDTO.getType();
            if (typeEnum.name().equals(BriefCustomerDTO.TypeEnum.ACTIVE.toString())) {
                List<Long> userIds = briefCustomerDTO.getUserIds();
                for (Long temp : userIds) {
                    if (choseUserIdsMap.containsKey(temp)) {
                        userId = temp;
                        break;
                    }
                }
            }
            //customerId不是null,且>0时，校验是否上一个customerId是否与本此的customerId相同
            if (customerId != null && Long.compare(customerId, 0l) == 1 && customerId.equals(preCustomerId)) {
                userId = preUserId;
            }
            if (Long.compare(userId, 0l) == 0) {
                if (!customerUserRelAllotBo.isUseIndex()) {
                    customerUserRelAllotBo.setUseIndex(true);
                } else if (customerId == null || Long.compare(customerId, 0l) == 0 || !customerId.equals(preCustomerId)) {
                    //customerId为空，0 或不等于上一条数据的customerId时，分配给下一个员工
                    index++;
                }
                if (index > size - 1) {
                    //从第一个员工开始分配
                    index = 0;
                }
                userId = choseUserIds.get(index);
            }
            customerUserRel.setUserId(userId);
            insertList.add(customerUserRel);
            preCustomerId = customerId;
            preUserId = userId;
            //设置总分配数
            setAllotResultVoMap(allotResultVoMap, userId, typeEnum);
            //设置本次分配数
            setAllotResultVoMap(thisAllotResultVoMap, userId, typeEnum);
            List<Long> customerCarIds;
            if (cubeCustomerInfoMap.containsKey(userId)) {
                customerCarIds = cubeCustomerInfoMap.get(userId);
            } else {
                customerCarIds = Lists.newArrayList();
            }
            customerCarIds.add(customerCarId);
            cubeCustomerInfoMap.put(userId, customerCarIds);
        }
        customerUserRelAllotBo.setIndex(index);
        customerUserRelAllotBo.setPreCustomerId(preCustomerId);
        customerUserRelAllotBo.setPreUserId(preUserId);
        //调用cube更新数据
        synCubeCustomerInfo(insertList, shopId, cubeCustomerInfoMap);
    }

    private void setAllotResultVoMap(Map<Long, AllotResultVo> allotResultVoMap, Long userId, BriefCustomerDTO.TypeEnum typeEnum) {
        AllotResultVo allotResultVo;
        Integer active = 0;
        Integer lazy = 0;
        Integer lost = 0;
        Integer allot = 0;
        if (allotResultVoMap.containsKey(userId)) {
            allotResultVo = allotResultVoMap.get(userId);
            active = allotResultVo.getActive();
            lazy = allotResultVo.getLazy();
            lost = allotResultVo.getLost();
            allot = allotResultVo.getAllot();
        } else {
            allotResultVo = new AllotResultVo();
        }
        if (typeEnum.name().equals(BriefCustomerDTO.TypeEnum.ACTIVE.toString())) {
            active++;
        } else if (typeEnum.name().equals(BriefCustomerDTO.TypeEnum.LAZY.toString())) {
            lazy++;
        } else if (typeEnum.name().equals(BriefCustomerDTO.TypeEnum.LOST.toString())) {
            lost++;
        }
        allot++;
        allotResultVo.setActive(active);
        allotResultVo.setLazy(lazy);
        allotResultVo.setLost(lost);
        allotResultVo.setAllot(allot);
        allotResultVoMap.put(userId, allotResultVo);
    }

    @Transactional
    private void synCubeCustomerInfo(List<CustomerUserRel> insertList, Long shopId, Map<Long, List<Long>> cubeCustomerInfoMap) {
        if (CollectionUtils.isEmpty(insertList)) {
            return;
        }
        //插入数据
        customerUserRelService.batchInsert(insertList);
        List<AllotCarParam> allotCarParamList = Lists.newArrayList();
        for (Long userId : cubeCustomerInfoMap.keySet()) {
            List<Long> customerCarIds = cubeCustomerInfoMap.get(userId);
            AllotCarParam allotCarParam = new AllotCarParam();
            allotCarParam.setUserId(userId);
            allotCarParam.setCustomerCarIdList(customerCarIds);
            allotCarParamList.add(allotCarParam);
        }
        Result<Integer> allotCustomerCarsResult = rpcCustomerInfoService.allotCustomerCars(shopId, allotCarParamList);
        if (!allotCustomerCarsResult.isSuccess()) {
            log.error("同步客户车辆异常,入参：shopId：{},{},返回信息：{}", shopId, LogUtils.objectToString(allotCarParamList), allotCustomerCarsResult.getMessage());
            throw new BizException("同步客户车辆异常");
        }
    }

    private CustomerUserRel buildCustomerUserRel(String allotSn, Long shopId, Long customerCarId, Long customerId, Long operatorId) {
        CustomerUserRel customerUserRel = new CustomerUserRel();
        customerUserRel.setShopId(shopId);
        customerUserRel.setAllotSn(allotSn);
        customerUserRel.setCustomerCarId(customerCarId);
        customerUserRel.setCustomerId(customerId);
        customerUserRel.setCreator(operatorId);
        return customerUserRel;
    }

    private AllotUserVo buildAllotUserVo(ShopManager shopManager) {
        if (shopManager == null) {
            return null;
        }
        AllotUserVo allotUserVo = new AllotUserVo();
        allotUserVo.setUserId(shopManager.getId());
        allotUserVo.setUserName(shopManager.getName());
        return allotUserVo;
    }
}
