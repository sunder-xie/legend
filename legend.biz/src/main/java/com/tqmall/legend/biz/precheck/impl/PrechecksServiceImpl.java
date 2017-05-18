package com.tqmall.legend.biz.precheck.impl;

import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.legend.api.entity.ApiCarVo;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.common.DefaultPage;
import com.tqmall.legend.biz.customer.CustomerCarFileService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.legend.biz.customer.CustomerService;
import com.tqmall.legend.biz.customer.ICustomerContactService;
import com.tqmall.legend.biz.precheck.PrechecksService;
import com.tqmall.legend.biz.shop.SerialNumberService;
import com.tqmall.legend.biz.util.StringUtil;
import com.tqmall.legend.common.CommonUtils;
import com.tqmall.legend.dao.prechecks.*;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarFile;
import com.tqmall.legend.entity.customer.CustomerContact;
import com.tqmall.legend.entity.order.SerialTypeEnum;
import com.tqmall.legend.entity.precheck.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by guangxue on 14/10/28.
 * updated by Sven.zhang on 16/04/08.
 */
@Service
@Slf4j
public class PrechecksServiceImpl extends BaseServiceImpl implements PrechecksService {
    @Autowired
    private PrechecksDao prechecksDao;

    @Autowired
    private PrecheckDetailsDao precheckDetailsDao;

    @Autowired
    private PrecheckValueDao precheckValueDao;

    @Autowired
    private PrecheckItemsDao precheckItemsDao;

    @Autowired
    private CustomerCarService customerCarService;

    @Autowired
    private PrecheckRequestDao precheckRequestDao;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ICustomerContactService customerContactService;
    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private CustomerCarFileService customerCarFileService;

    private Map<Long, Map<String, String>> getAllItems() {
        Map<Long, Map<String, String>> getPrecheckItemsMap = new HashMap<>();
        List<PrecheckItems> precheckItemsList = precheckItemsDao.select(null);
        if (CollectionUtils.isEmpty(precheckItemsList)) {
            return getPrecheckItemsMap;
        }
        for (PrecheckItems tmp : precheckItemsList) {
            Map<String, String> tmpValue = new HashMap<>();
            getPrecheckItemsMap.put(tmp.getId(), tmpValue);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String value = objectMapper.writeValueAsString(tmp);
                tmpValue = objectMapper.readValue(value,
                        new TypeReference<HashMap<String, String>>() {
                        });
                getPrecheckItemsMap.put(tmp.getId(), tmpValue);
            } catch (Exception e) {
                log.error("数据转换出错precheckItemsList:{}", precheckItemsList, e);
            }
        }
        return getPrecheckItemsMap;
    }

    private Map<Long, Map<String, String>> getAllItemValues() {
        Map<Long, Map<String, String>> precheckValuesMap = new HashMap<>();
        List<PrecheckValue> precheckValueList = precheckValueDao.select(null);
        if (CollectionUtils.isEmpty(precheckValueList)) {
            return precheckValuesMap;
        }
        for (PrecheckValue tmp : precheckValueList) {
            Map<String, String> tmpValue = new HashMap<>();
            precheckValuesMap.put(tmp.getId(), tmpValue);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String value = objectMapper.writeValueAsString(tmp);
                tmpValue = objectMapper.readValue(value,
                        new TypeReference<HashMap<String, String>>() {
                        });
                precheckValuesMap.put(tmp.getId(), tmpValue);
            } catch (Exception e) {
                log.error("数据转换出错precheckValueList:{}", precheckValueList, e);
                return precheckValuesMap;
            }
        }
        return precheckValuesMap;
    }

    @Override
    public Map<Long, Map<String, String>> getAllPrecheckItems() {
        return getAllItems();
    }

    @Override
    public Map<Long, Map<String, String>> getAllPrecheckValues() {
        return getAllItemValues();
    }


    @Override
    @Transactional
    public Boolean deletePrechecks(Long precheckId, UserInfo userInfo) {
        Long shopId = userInfo.getShopId();
        Map<String, Object> params = new HashMap<>();
        params.put("id", precheckId);
        params.put("shopId", shopId);
        prechecksDao.delete(params);
        Map<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("precheckId", precheckId);
        deleteMap.put("shopId", shopId);
        //删除预检单详情，用户检测需求
        precheckDetailsDao.delete(deleteMap);
        precheckRequestDao.delete(deleteMap);
        return Boolean.TRUE;
    }

    @Override
    public List<Prechecks> getAPrecheck(Map<String, Object> params) {
        return prechecksDao.getPrecheckOrder(params);
    }

    @Override
    public Long batchInsertDetails(List<PrecheckDetails> precheckDetailsList) throws Exception {
        return precheckDetailsDao.batchInsertDetail(precheckDetailsList);
    }

    private Long batchInsertCustomerRequest(List<PrecheckRequest> precheckRequestList)
            throws Exception {
        return precheckRequestDao.batchInsertRequest(precheckRequestList);
    }


    @Override
    public List<PrecheckValue> getItemValuesByType(Long valueType) {
        return precheckValueDao.getValuesByType(valueType);
    }

    /**
     * params map: shopId 店铺Id start 提醒开始日期, format yyyy-mm-dd days 提醒的天数
     *
     * @return
     */
    @Override
    public List<PrecheckDetailsVO> getPrecheckRemindList(Long shopId, String start, Integer days) {
        Map<String, Object> params = new HashMap<>();
        String dateFormatStr = "yyyy-MM-dd";
        Date startDate = DateUtil.convertStrToDate(start, dateFormatStr);
        // 保证传入的start不会是2014-08-33之类的日期
        String end = String.format("%s%d", start.substring(0, 8), (Integer.parseInt(start.substring(8, 10)) + days));
        Date endDate = DateUtil.convertStrToDate(end, dateFormatStr);
        params.put("shopId", shopId);
        params.put("remindStart", DateUtil.convertDateToYMD(startDate));
        params.put("remindEnd", DateUtil.convertDateToYMD(endDate));

        List<PrecheckDetailsVO> precheckDetailsVOList = new ArrayList<>();
        List<Prechecks> preHeaders = prechecksDao.getRemindPrechecks(params);
        for (Prechecks tmp : preHeaders) {
            PrecheckDetailsVO precheckDetailsVO = new PrecheckDetailsVO();
            precheckDetailsVO.setPrecheckId(tmp.getId());
            precheckDetailsVO.setPrecheckSn(tmp.getPrecheckSn());
            precheckDetailsVO.setGmtCreate(DateUtil.convertDate(tmp.getGmtCreate()));
            params.put("precheckId", tmp.getId());
            Map<String, List<PrecheckDetails>> mapDetails = getPrecheckDetails(params);
            precheckDetailsVO.setDetailsMap(mapDetails);
            precheckDetailsVOList.add(precheckDetailsVO);
        }

        return precheckDetailsVOList;

    }

    private void addItemsToList(String key, PrecheckDetails item,
                                Map<String, List<PrecheckDetails>> retMap) {
        List<PrecheckDetails> outlineList;
        outlineList = retMap.get(key);
        if (null == outlineList) {
            outlineList = new ArrayList<>();
            outlineList.add(item);
            retMap.put(key, outlineList);
        } else
            outlineList.add(item);
    }

    @Override
    public List<PrecheckRequest> getListedPrecheckRequests(Map<String, Object> params) {
        return precheckRequestDao.select(params);
    }

    @Override
    public List<PrecheckDetails> getListedPrecheckDetails(Map<String, Object> params) {
        if (params.containsKey("carId") && !params.containsKey("precheckId")) {
            Map<String, Object> precheckMap = Maps.newHashMap();
            List<String> sorts = new ArrayList<>();
            sorts.add(" id desc ");
            precheckMap.put("sorts", sorts);
            precheckMap.put("offset", 0);
            precheckMap.put("limit", 1);
            precheckMap.put("carId", params.get("carId"));
            List<Prechecks> precheck = prechecksDao.select(precheckMap);
            params.put("precheckId", precheck.get(0).getId());

        }
        return precheckDetailsDao.getPrecheckDetail(params);
    }

    @Override
    public Page<PrecheckDetailsVO> getPrecheckDetailsList(Pageable pageable,
                                                          Map<String, Object> params) {
        Integer totalSize = prechecksDao.selectCount(params);

        List<PrecheckDetailsVO> retList = new ArrayList<>();

        PageRequest pageRequest = new PageRequest((pageable.getPageNumber() < 1 ? 1
                : pageable.getPageNumber()) - 1, pageable.getPageSize() < 1 ? 1
                : pageable.getPageSize(), pageable.getSort());
        params.put("offset", pageRequest.getOffset());
        params.put("limit", pageRequest.getPageSize());

        List<Prechecks> preHeaders = prechecksDao.selectPrechecks(params);
        for (Prechecks tmp : preHeaders) {
            PrecheckDetailsVO precheckDetailsVO = new PrecheckDetailsVO();
            precheckDetailsVO.setPrecheckId(tmp.getId());
            precheckDetailsVO.setPrecheckSn(tmp.getPrecheckSn());
            precheckDetailsVO.setGmtCreate(DateUtil.convertDate(tmp.getGmtCreate()));
            params.put("precheckId", tmp.getId());
            Map<String, List<PrecheckDetails>> mapDetails = getPrecheckDetails(params);
            precheckDetailsVO.setDetailsMap(mapDetails);
            retList.add(precheckDetailsVO);
        }

        DefaultPage<PrecheckDetailsVO> page = new DefaultPage<>(retList, pageRequest, totalSize);
        return page;
    }

    /**
     * 返回按预检类型分组的预检单明细条目
     *
     * @param params
     * @return
     */
    private Map<String, List<PrecheckDetails>> getPrecheckDetails(Map<String, Object> params) {
        List<PrecheckDetails> tmpList = getListedPrecheckDetails(params);
        Map<String, List<PrecheckDetails>> retMap = new HashMap<>();
        for (PrecheckDetails tmp : tmpList) {
            if (tmp.getPrecheckItemType() != null && 0 != tmp.getPrecheckItemType()) {
                if (CommonUtils.isRed(tmp.getPrecheckValue())) {
                    tmp.setRedFlag(true);
                }
                addItemsToList(tmp.getPrecheckItemType().toString(), tmp, retMap);
            }
        }
        return retMap;
    }

    /**
     * 更新预检单
     * 1.先将预检单ID号为id的预检单详情逻辑删除, 再添加新的详情
     *
     * @param precheckHead
     * @param precheckDetailsList
     * @param precheckRequestList
     * @return
     */
    @Override
    @Transactional
    public Boolean updatePrecheckOrder(Prechecks precheckHead,
                                       List<PrecheckDetails> precheckDetailsList,
                                       List<PrecheckRequest> precheckRequestList)
            throws Exception {

        long shopId = precheckHead.getShopId();
        Long id = precheckHead.getId();
        Map<String, Object> params = new HashMap<>();
        params.put("precheckId", id);
        params.put("shopId", shopId);
        //删除预检单详情，用户检测需求
        precheckDetailsDao.delete(params);
        precheckRequestDao.delete(params);
        //新增客户需求
        if (!CollectionUtils.isEmpty(precheckRequestList)) {
            for (PrecheckRequest tmp : precheckRequestList) {
                tmp.setShopId(shopId);
                tmp.setPrecheckId(id);
            }
            batchInsertCustomerRequest(precheckRequestList);
        }
        //新增预检单详情
        if (!CollectionUtils.isEmpty(precheckDetailsList)) {
            for (PrecheckDetails tmp : precheckDetailsList)
                tmp.setPrecheckId(id);
            batchInsertDetails(precheckDetailsList);
        }
        params.clear();
        // 获得precheckHead的信息
        params.put("shopId", shopId);
        params.put("id", id);
        if (null != precheckHead) {
            String comments = precheckHead.getComments();
            params.put("comments", emptyToNull(comments));
            String nextTime = precheckHead.getNextTime();
            params.put("nextTime", emptyToNull(nextTime));
            String color = precheckHead.getColor();
            params.put("color", emptyToNull(color));
            String insurance = precheckHead.getInsurance();
            params.put("insurance", emptyToNull(insurance));
            String dueDate = DateUtil.convertDateToYMD(precheckHead.getDueDate());
            params.put("dueDate", emptyToNull(dueDate));
            String mileage = precheckHead.getMileage();
            params.put("mileage", emptyToNull(mileage));
            String upkeepMileage = precheckHead.getUpkeepMileage();
            params.put("upkeepMileage", emptyToNull(upkeepMileage));
            Long insuranceId = precheckHead.getInsuranceId();
            if (insuranceId == null || insuranceId.compareTo(0L) == 0) {
                params.put("insuranceId", null);
            } else {
                params.put("insuranceId", insuranceId);
            }
            if (null == precheckHead.getManHour()) {
                precheckHead.setManHour("0");
            }
            if (null == precheckHead.getExpFee()) {
                precheckHead.setExpFee(BigDecimal.valueOf(0.00));
            }
            params.put("manHour", precheckHead.getManHour());
            params.put("expFee", precheckHead.getExpFee());
            prechecksDao.updatePrecheckComment(params);
        }

        updateCustomerContact(precheckHead);
        CustomerCar customerCar = new CustomerCar();
        updateCustomerCarInfo(precheckHead, customerCar);
        customerCarService.update(customerCar);
        return Boolean.TRUE;
    }

    /**
     * legend_customer_car更新行驶里程,下次保养里程、时间，保险到期
     *
     * @param prechecks
     * @param customerCar
     */
    private void updateCustomerCarInfo(Prechecks prechecks, CustomerCar customerCar) {
        customerCar.setShopId(prechecks.getShopId());
        customerCar.setId(prechecks.getCustomerCarId());
        if (StringUtil.isNotStringEmpty(prechecks.getMileage())) {
            customerCar.setMileage(Long.valueOf(prechecks.getMileage()));
        }
        if (StringUtil.isNotStringEmpty(prechecks.getUpkeepMileage())) {
            customerCar.setUpkeepMileage(prechecks.getUpkeepMileage());
        }
        if (null != prechecks.getDueDate()) {
            customerCar.setInsuranceTime(prechecks.getDueDate());
        }
        if (null != prechecks.getNextTime()) {
            customerCar.setKeepupTime(DateUtil.convertStringToDateYMD(prechecks.getNextTime()));
        }
    }

    /**
     * 新增预检单
     */
    @Override
    @Transactional
    public Boolean addNewPrecheckOrder(Prechecks prechecks,
                                       List<PrecheckDetails> precheckDetailsList, List<PrecheckRequest> precheckRequestList,
                                       UserInfo userInfo) throws Exception {
        Map<String, Object> params = new HashMap<>();

        //预检单编号
        params.put("userId", userInfo.getUserId());
        params.put("shopId", prechecks.getShopId());
        params.put("serialType", SerialTypeEnum.PRECHECK.getCode());
        String serialNumber = serialNumberService.getSerialNumber(params);
        if (StringUtils.isEmpty(serialNumber)) {
            throw new RuntimeException("生成预检单编号为空");
        }
        prechecks.setPrecheckSn(serialNumber);
        if (StringUtils.isEmpty(prechecks.getNextTime())) {
            prechecks.setNextTime(null);
        }
        log.info(userInfo.getName() + "插入预检单记录:", prechecks);
        prechecksDao.insert(prechecks);
        log.info(userInfo.getName() + "插入预检单用户检测需求:", precheckRequestList);
        if (!CollectionUtils.isEmpty(precheckRequestList)) {
            for (PrecheckRequest tmp : precheckRequestList) {
                tmp.setShopId(prechecks.getShopId());
                tmp.setPrecheckId(prechecks.getId());
            }
            precheckRequestDao.batchInsertRequest(precheckRequestList);
        }
        Long precheckId = prechecks.getId();
        log.info(userInfo.getName() + "插入预检单预检详情:", precheckDetailsList);
        if (!CollectionUtils.isEmpty(precheckDetailsList)) {
            for (PrecheckDetails tmp : precheckDetailsList)
                tmp.setPrecheckId(precheckId);
            batchInsertDetails(precheckDetailsList);
        }

        updateCustomerContact(prechecks);
        // 更新车辆信息里的检测次数和最近一次登记信息
        // 获得车辆的信息
        try {
            CustomerCar customerCar = customerCarService.selectById(prechecks
                    .getCustomerCarId());
            if (null != customerCar) {
                Integer precheckCount = customerCar.getPrecheckCount();
                if (precheckCount == null || precheckCount == 0) {
                    precheckCount = 1;
                } else {
                    precheckCount += 1;
                }
                customerCar.setPrecheckCount(precheckCount);
                customerCar.setModifier(prechecks.getCreator());
                customerCar.setLatestPrecheck(new Date());
                updateCustomerCarInfo(prechecks, customerCar);
                customerCarService.update(customerCar);
            }
        } catch (Exception e) {
            throw new RuntimeException("更新车辆信息失败!");
        }
        return Boolean.TRUE;
    }

    @Override
    public List<Prechecks> getAllCarPreCheckHeads(Map<String, Object> params) {
        List<Prechecks> retList = prechecksDao.select(params);
        return retList;
    }

    @Override
    public Prechecks selectById(Long id) {
        Prechecks prechecks = prechecksDao.selectById(id);
        return prechecks;
    }

    @Override
    public PrecheckVO select(Map<String, Object> searchMap) {
        PrecheckVO ret = new PrecheckVO();
        List<PrecheckDetails> ret2 = precheckDetailsDao.getPrecheckDetail(searchMap);
        ret.setPrecheckDetailsList(ret2);
        return ret;
    }

    @Override
    @Transactional
    public void toAppCustomerCarUpdate(Long shopId, ApiCarVo car) {
        CustomerCar customerCar = customerCarService.selectById(car.getCarId());
        if (StringUtils.isNotBlank(car.getMileage())) {
            customerCar.setMileage(Long.valueOf(car.getMileage()));
        }
        if (StringUtils.isNotBlank(car.getNextUpkeepMileage())) {
            customerCar.setUpkeepMileage(car.getNextUpkeepMileage());
            if (StringUtils.isNotBlank(car.getMileage()) ) {
                Long mileage = Long.valueOf(car.getMileage());
                Long upkeepMileage = Long.valueOf(car.getNextUpkeepMileage());
                if (upkeepMileage.compareTo(mileage) <= 0 ) {
                    throw new BizException("下次保养里程不能小于或等于行驶里程");
                }
            }
        }
        String nextUpKeepTimeStr = car.getNextUpkeepTimeStr();
        if (StringUtils.isNotBlank(nextUpKeepTimeStr)) {
            customerCar.setKeepupTime(DateUtil.convertStringToDateYMD(nextUpKeepTimeStr));
        } else {
            customerCar.setKeepupTime(car.getNextUpkeepTime());
        }
        String insuranceTimeStr = car.getInsuranceTimeStr();
        if (StringUtils.isNotBlank(insuranceTimeStr)) {
            customerCar.setInsuranceTime(DateUtil.convertStringToDateYMD(insuranceTimeStr));
        }
        customerCar.setModifier(car.getUserId());
        customerCarService.update(customerCar);

        //add by twg 2015-11-2 保养
        Map params = Maps.newConcurrentMap();
        params.put("shopId", shopId);
        //add by twg 预检单编号
        params.put("userId", car.getUserId());
        params.put("serialType", SerialTypeEnum.PRECHECK.getCode());
        String serialNumber = serialNumberService.getSerialNumber(params);
        if (StringUtils.isEmpty(serialNumber)) {
            throw new RuntimeException("生成预检单编号为空");
        }
        //end
        //更新车辆图片
        if (org.apache.commons.lang3.StringUtils.isNotBlank(car.getImgUrl())) {
            CustomerCarFile customerCarFile = new CustomerCarFile();
            customerCarFile.setShopId(shopId);
            customerCarFile.setRelId(customerCar.getId());
            customerCarFile.setRelType(1L);
            customerCarFile.setFileType(5);
            customerCarFile.setFilePath(car.getImgUrl());
            customerCarFile.setCreator(car.getUserId());
            customerCarFile.setModifier(car.getUserId());
            customerCarFileService.add(customerCarFile);
        }
        Customer customer = customerService.selectById(customerCar.getCustomerId());
        Prechecks prechecks = new Prechecks();
        prechecks.setShopId(shopId);
        prechecks.setCustomerCarId(customerCar.getId());
        prechecks.setCustomerName(customer.getCustomerName());
        prechecks.setCustomerId(customerCar.getCustomerId());
        prechecks.setMobile(customer.getMobile());
        prechecks.setColor(customerCar.getColor());
        prechecks.setPrecheckSn(serialNumber);
        prechecks.setRefer(car.getRefer());
        prechecks.setVer(car.getVer());
        //数据来源
        if (!StringUtils.isBlank(car.getInsuranceTimeStr())) {
            prechecks.setDueDate(DateUtil.convertStringToDateYMD(car.getInsuranceTimeStr()));
        }
        if (!StringUtils.isBlank(car.getNextUpkeepTimeStr())) {
            prechecks.setNextTime(car.getNextUpkeepTimeStr());
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(car.getNextUpkeepMileage())) {
            prechecks.setUpkeepMileage(car.getNextUpkeepMileage());
        }
        if (org.apache.commons.lang3.StringUtils.isNotBlank(car.getMileage())) {
            prechecks.setMileage(car.getMileage());
        }
        prechecksDao.insert(prechecks);
        log.info("End toAppCustomerCarUpdate. ");
    }

    @Override
    public List<PrecheckDetailsVO> toAppGetPrecheckDetailsList(Map<String, Object> params) {
        log.info("[客户档案] toAppGetPrecheckDetailsList().调用原有接口获取预检单信息,传参:params={}", JSONUtil.object2Json(params));

        List<PrecheckDetailsVO> retList = new ArrayList<>();
        List<Prechecks> preHeaders = prechecksDao.select(params);
        for (Prechecks tmp : preHeaders) {
            PrecheckDetailsVO precheckDetailsVO = new PrecheckDetailsVO();
            precheckDetailsVO.setPrecheckId(tmp.getId());
            precheckDetailsVO.setPrecheckSn(tmp.getPrecheckSn());
            precheckDetailsVO.setGmtCreate(DateUtil.convertDateToYMDHHmm(tmp.getGmtCreate()));
            params.put("precheckId", tmp.getId());
            Map<String, List<PrecheckDetails>> mapDetails = getPrecheckDetails(params);
            precheckDetailsVO.setDetailsMap(mapDetails);
            retList.add(precheckDetailsVO);
        }
        log.info("End toAppCustomerCarUpdate. ");
        return retList;

    }
      //新增，编辑操作更新联系人
    private void updateCustomerContact(Prechecks prechecks) {
        HashMap<String, Object> params = new HashMap<>();
        Customer customer = new Customer();
        customer.setId(prechecks.getCustomerId());
        customer.setShopId(prechecks.getShopId());
        customer.setContact(prechecks.getCustomerName());
        customer.setContactMobile(prechecks.getMobile());
        // 更新联系人信息
        if (StringUtil.isNotStringEmpty(customer.getContact()) && StringUtil.isNotStringEmpty(customer.getContactMobile())) {
            customerService.update(customer);
            params.clear();
            params.put("shopId", prechecks.getShopId());
            params.put("customerId", customer.getId());
            params.put("customerCarId", prechecks.getCustomerCarId());
            params.put("contact", customer.getContact());
            params.put("isDeleted", "N");
            List<CustomerContact> customerContacts = customerContactService.select(params);

            CustomerContact customerContact = new CustomerContact();
            customerContact.setShopId(prechecks.getShopId());
            customerContact.setCustomerId(customer.getId());
            customerContact.setCustomerCarId(prechecks.getCustomerCarId());
            customerContact.setContact(customer.getContact());
            customerContact.setContactMobile(customer.getContactMobile());
            // 更新联系人列表信息
            if (CollectionUtils.isEmpty(customerContacts)) {
                customerContactService.save(customerContact);
            } else {
                customerContact.setId(customerContacts.get(0).getId());
                if (customerContacts.get(0).getContact().equals(customer.getContact().trim())) {
                    customerContactService.update(customerContact);
                } else {
                    customerContactService.save(customerContact);
                }
            }
        }

    }

    //保证空字符串不存入数据库
    private String emptyToNull(String str) {
        if (null == str || "".equals(str.trim())) {
            return null;
        }
        return str.trim();
    }

    @Override
    public Integer countPrecheckCar(Map<String, Object> params) {
        return prechecksDao.countPrecheckCar(params);
    }
}