package com.tqmall.legend.server.tqcheck;

import com.google.common.collect.Maps;
import com.tqmall.common.UserInfo;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.error.LegendErrorCode;
import com.tqmall.legend.biz.base.BaseServiceImpl;
import com.tqmall.legend.biz.customer.CustomerCarFileService;
import com.tqmall.legend.biz.customer.CustomerCarService;
import com.tqmall.common.exception.BizException;
import com.tqmall.legend.biz.shop.SerialNumberService;
import com.tqmall.legend.dao.customer.CustomerCarDao;
import com.tqmall.legend.dao.customer.CustomerDao;
import com.tqmall.legend.dao.tqcheck.TqCheckCategoryDao;
import com.tqmall.legend.dao.tqcheck.TqCheckLogDao;
import com.tqmall.legend.dao.tqcheck.TqCheckDetailDao;
import com.tqmall.legend.entity.customer.Customer;
import com.tqmall.legend.entity.customer.CustomerCar;
import com.tqmall.legend.entity.customer.CustomerCarFile;
import com.tqmall.legend.entity.tqcheck.*;
import com.tqmall.legend.object.param.tqcheck.TqCheckAddParam;
import com.tqmall.legend.object.param.tqcheck.TqCheckDetailParam;
import com.tqmall.legend.object.result.tqcheck.TqCheckDetailDTO;
import com.tqmall.legend.object.result.tqcheck.TqCheckDetailListDTO;
import com.tqmall.legend.object.result.tqcheck.TqCheckItemDTO;
import com.tqmall.legend.service.tqcheck.RpcTqCheckService;
import com.tqmall.zenith.errorcode.support.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lifeilong on 2016/4/12.
 */
@Slf4j
@Service ("rpcTqCheckService")
public class RpcTqCheckServiceImpl extends BaseServiceImpl implements RpcTqCheckService {

    @Autowired
    private TqCheckLogDao tqCheckLogDao;

    @Autowired
    private CustomerCarDao customerCarDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerCarService customerCarService;

    @Autowired
    private TqCheckCategoryDao tqCheckCategoryDao;

    @Autowired
    private TqCheckDetailDao tqCheckDetailDao;

    @Autowired
    private SerialNumberService serialNumberService;

    @Autowired
    private CustomerCarFileService customerCarFileService;

    private static Map<Long, TqCheckCategory> tqCheckCategoryMap;

    @Override
    public Result addNewCustomerTqCheckMsg(TqCheckAddParam tqCheckAddParam) {
        log.info("[dubbo][淘汽检测-新建]新增车辆检测信息,关键参数:shopId={},license={},customerCarId={}",
                tqCheckAddParam.getShopId(),tqCheckAddParam.getCarLicense(), tqCheckAddParam.getCustomerCarId());
        CustomerCar customerCar = new CustomerCar();
        try {
            BeanUtils.copyProperties(tqCheckAddParam, customerCar);
        } catch (BeansException e) {
            log.error("[dubbo][淘汽检测-新建]. 属性复制失败,e={}", e.toString());
            return LegendErrorCode.BEAN_COPYPROPERTIES_ERROR.newResult();
        }
        customerCar.setLicense(tqCheckAddParam.getCarLicense());
        customerCar.setContact(tqCheckAddParam.getContactName());
        customerCar.setCarModelId(tqCheckAddParam.getCarModelsId());
        customerCar.setCarModel(tqCheckAddParam.getCarModels());
        customerCar.setRefer(tqCheckAddParam.getSource());
        customerCar.setCarPictureUrl(tqCheckAddParam.getImgUrl());
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(tqCheckAddParam.getUserId());
        userInfo.setShopId(tqCheckAddParam.getShopId());
        //如果有cutomerCarId ，但又修改了车牌号，校验修改车牌号与原带入的车辆车牌号是否还一致
        if (null != tqCheckAddParam.getCustomerCarId() && tqCheckAddParam.getCustomerCarId() > 0) {
            CustomerCar car = customerCarDao.selectById(tqCheckAddParam.getCustomerCarId());
            if (null != car) {
                if (car.getLicense().equalsIgnoreCase(tqCheckAddParam.getCarLicense())) {
                    customerCar.setId(tqCheckAddParam.getCustomerCarId());
                }
            }
        }

        // 添加或更新车辆信息
        try {
            customerCar = customerCarService.addOrUpdate(userInfo, customerCar);
        } catch (BizException e) {
            log.error("[dubbo][淘汽检测], 新建或更新客户车辆信息失败，异常信息:{}", e.toString());
            return LegendErrorCode.ADD_OR_UPDATE_CUSTOMER_CAR_ERROR.newResult();
        }
        if (null == customerCar) {
            log.error("[dubbo][淘汽检测], 新建或更新客户车辆信息失败,shopId={}, license={}, customerCarId={}", tqCheckAddParam.getShopId(),tqCheckAddParam.getCarLicense(), tqCheckAddParam.getCustomerCarId());
            return LegendErrorCode.ADD_OR_UPDATE_CUSTOMER_CAR_ERROR.newResult();
        }
        log.info("[dubbo][淘汽检测]新增或更新车辆信息, customerCarId = {}, license={}", customerCar.getId(), customerCar.getLicense());
        TqCheckLog tqCheckLog = new TqCheckLog();
        try {
            BeanUtils.copyProperties(tqCheckAddParam, tqCheckLog);
        } catch (BeansException e) {
            log.error("[dubbo][淘汽检测-新建]. 属性复制失败,e={}", e);
            return LegendErrorCode.BEAN_COPYPROPERTIES_ERROR.newResult();
        }

        tqCheckLog.setCustomerCarId(customerCar.getId());
        tqCheckLog.setCustomerId(customerCar.getCustomerId());
        log.info("[dubbo][淘汽检测] 获取淘汽检测编号,入参:userId={},shopId={},serialType='TQCHECK' ", tqCheckAddParam.getUserId(), tqCheckAddParam.getShopId());
        Map params = Maps.newConcurrentMap();
        params.put("userId", tqCheckAddParam.getUserId());
        params.put("shopId", tqCheckAddParam.getShopId());
        params.put("serialType", "TQCHECK");
        String serialNumber = serialNumberService.getSerialNumber(params);
        log.info("[dubbo][淘汽检测] 检测编号:serialNumber={}", serialNumber);
        if (StringUtils.isBlank(serialNumber)) {
            log.error("[dubbo][淘汽检测] 新建检测记录失败,获取检测编号失败,serialNumber=null");
            return LegendErrorCode.TQ_CHECK_ADD_RECORD_ERROR.newResult();
        }
        tqCheckLog.setCheckSn(serialNumber);
        // 新增检测记录
        if (tqCheckLogDao.insert(tqCheckLog) > 0) {
            log.info("[dubbo][淘汽检测]插入检测记录成功 tqCheckLog={}", JSONUtil.object2Json(tqCheckLog));
            List<TqCheckDetailParam> tqCheckDetailList = tqCheckAddParam.getTqCheckDetailList();
            if (!CollectionUtils.isEmpty(tqCheckDetailList)) {
                for (TqCheckDetailParam tqCheckDetailParam : tqCheckDetailList) {
                    if (!tqCheckDetailParamCheck(tqCheckDetailParam)) {
                        log.error("[淘汽检测] 要新建的检测记录具体详情错误，缺少参数 category={},categoryItem={},itemValueType={}", tqCheckDetailParam.getCategory(), tqCheckDetailParam.getCategoryItem(), tqCheckDetailParam.getItemValueType());
                        continue;
                    }
                    //插入检测详情信息
                    TqCheckDetail tqCheckDetail = new TqCheckDetail();
                    BeanUtils.copyProperties(tqCheckDetailParam, tqCheckDetail);
                    tqCheckDetail.setCheckLogId(tqCheckLog.getId());
                    tqCheckDetail.setShopId(tqCheckAddParam.getShopId());
                    tqCheckDetail.setCategoryId(Long.parseLong(getIdAndItemName(tqCheckDetailParam.getCategory(), tqCheckDetailParam.getCategoryItem(), 1)));
                    tqCheckDetail.setCategoryItemName(getIdAndItemName(tqCheckDetailParam.getCategory(), tqCheckDetailParam.getCategoryItem(), 2));
                    tqCheckDetail.setItemValueType(tqCheckDetailParam.getItemValueType());
                    tqCheckDetailDao.insert(tqCheckDetail);
                }
                return Result.wrapSuccessfulResult("新建成功");
            }
        }
        return LegendErrorCode.TQ_CHECK_ADD_RECORD_ERROR.newResult();
    }

    private Boolean tqCheckDetailParamCheck(TqCheckDetailParam tqCheckDetailParam) {
        if (null == tqCheckDetailParam.getCategory() || null == tqCheckDetailParam.getCategoryItem() || null == tqCheckDetailParam.getItemValueType()) {
            return false;
        }
        return true;
    }


    @Override
    public Result<TqCheckDetailListDTO> getTqCheckDetailList(Long shopId, String carLicense, Long customerCarId, Long checkLogId) {
        log.info("[dubbo][淘汽检测]获取客户淘汽检测列表,shopId={},carLicense={},customerCarId={},checkLogId={}", shopId, carLicense, customerCarId, checkLogId);
        if (null == shopId || shopId < 1) {
            log.error("[dubbo][淘汽检测] 门店信息错误 shopId={}", shopId);
            return LegendErrorCode.APP_SHOP_ID_ERROR.newResult();
        }
        TqCheckDetailListDTO tqCheckDetailListDTO = new TqCheckDetailListDTO();
        if (StringUtils.isBlank(carLicense) && null == customerCarId && null == checkLogId) {
            log.info("[dubbo][淘汽检测] 新建 - 展示新建列表信息 carLicense=null,customerCarId=null,checkLogId=null");
            tqCheckDetailListDTO.setIsHasCheckRecord(false);
            tqCheckDetailListDTO = getTqCheckList(tqCheckDetailListDTO);
            return Result.wrapSuccessfulResult(tqCheckDetailListDTO);
        }
        if (null != checkLogId && checkLogId > 0) {
            //客户档案-上次车况入口 回显检测记录信息(提交按钮置灰)
            Map<String, Object> tqCheckParams = new HashMap<>();
            tqCheckParams.put("shopId", shopId);
            tqCheckParams.put("id", checkLogId);
            List<TqCheckLog> tqCheckLogList = tqCheckLogDao.select(tqCheckParams);
            if (!CollectionUtils.isEmpty(tqCheckLogList)) {
                //已有检测记录，获取各检测项目详情列表
                tqCheckDetailListDTO.setIsHasCheckRecord(true);
                TqCheckLog tqCheckLog = tqCheckLogList.get(0);
                tqCheckDetailListDTO.setCarLicense(tqCheckLog.getCarLicense());
                tqCheckDetailListDTO.setCustomerCarId(tqCheckLog.getCustomerCarId());
                tqCheckDetailListDTO.setCarInfo(tqCheckLog.getCarInfo());
                tqCheckDetailListDTO.setContactName(tqCheckLog.getContactName());
                tqCheckDetailListDTO.setContactMobile(tqCheckLog.getContactMobile());
                //tqCheckDetailListDTO.setCarGearBoxId(tqCheckLog.getCarGearBoxId());
                log.info("[淘汽检测] 找到车辆检测记录，获取各检测项目详情列表,shopId={},checkLogId={}", shopId, tqCheckLog.getId());
                tqCheckDetailListDTO.setImgUrl(tqCheckLog.getImgUrl());
                tqCheckDetailListDTO.setCheckId(tqCheckLog.getId());
                tqCheckDetailListDTO = setCheckDetailList(tqCheckDetailListDTO, shopId, tqCheckLog.getId());
                return Result.wrapSuccessfulResult(tqCheckDetailListDTO);
            } else {
                log.error("[dubbo][淘汽检测]没有找到该检测记录,checkLogId={}", checkLogId);
                tqCheckDetailListDTO.setIsHasCheckRecord(false);
                return Result.wrapSuccessfulResult(tqCheckDetailListDTO);
            }
        } else if ((null == customerCarId || customerCarId < 1) && !StringUtils.isBlank(carLicense)) {
            //新建检测- 根据输入车牌获取车辆信息
            CustomerCar customerCar = customerCarDao.selectByLicenseAndShopId(carLicense, shopId);
            if (null != customerCar) {
                customerCarId = customerCar.getId();
            }
        }
        if (null != customerCarId && customerCarId > 0) {
            //根据车辆id回显车辆信息
            CustomerCar customerCar = customerCarDao.selectById(customerCarId);
            BeanUtils.copyProperties(customerCar, tqCheckDetailListDTO);
            tqCheckDetailListDTO.setCarLicense(customerCar.getLicense());
            tqCheckDetailListDTO.setCustomerCarId(customerCar.getId());
            tqCheckDetailListDTO.setCarInfo(customerCar.getCarInfo());
            Customer customer = customerDao.selectById(customerCar.getCustomerId());
            if (null != customer) {
                if (StringUtils.isBlank(customer.getContact())) {
                    tqCheckDetailListDTO.setContactName(customer.getCustomerName());
                    tqCheckDetailListDTO.setContactMobile(customer.getMobile());
                } else {
                    tqCheckDetailListDTO.setContactName(customer.getContact());
                    tqCheckDetailListDTO.setContactMobile(customer.getContactMobile());
                }
            }
            /*车辆文件信息 -图片*/
            Map<String, Object> carFileParam = new HashMap<>();
            carFileParam.put("shopId", shopId);
            carFileParam.put("relId", customerCarId);
            carFileParam.put("relType", 1);
            carFileParam.put("sorts", new String[] { "gmt_create desc" });
            List<CustomerCarFile> carFiles = customerCarFileService.select(carFileParam);
            if (!CollectionUtils.isEmpty(carFiles)) {
                tqCheckDetailListDTO.setImgUrl(carFiles.get(0).getFilePath());
            }
        }
        return Result.wrapSuccessfulResult(tqCheckDetailListDTO);
    }

    @Override
    public Result getTqCheckLogList(Map<String, Object> params) {
        List<TqCheckLog> logList = null;
        Page<TqCheckLog> page = null;
        Pageable pageable = (Pageable) params.get("pageable");
        if (null == pageable) {
            logList = tqCheckLogDao.select(params);
        } else {
            page = super.getPage(tqCheckLogDao, pageable, params);
            logList = page.getContent();
        }
        if (CollectionUtils.isEmpty(logList)) {
            return null;
        }
        if (null == pageable) {
            return Result.wrapSuccessfulResult(logList);
        }
        return Result.wrapSuccessfulResult(page);
    }

    /**
     * 获取 所有类目下的各检测项目详情列表
     */
    private TqCheckDetailListDTO setCheckDetailList(TqCheckDetailListDTO tqCheckDetailListDTO, Long shopId, Long checkLogId) {
        List<TqCheckDetailDTO> detailDTOList = new ArrayList<>();
        Map<String, Object> detailParam = new HashMap<>();
        detailParam.put("shopId", shopId);
        detailParam.put("checkLogId", checkLogId);
        List<TqCheckDetail> tqCheckDetails = tqCheckDetailDao.select(detailParam);
        if (!CollectionUtils.isEmpty(tqCheckDetails)) {
            for (TqCheckDetail tqCheckDetail : tqCheckDetails) {
                TqCheckDetailDTO tqCheckDetailDTO = new TqCheckDetailDTO();
                tqCheckDetailDTO.setCategory(tqCheckDetail.getCategory());
                tqCheckDetailDTO.setCategoryName(getCheckName(tqCheckDetail.getCategoryId(), 1));
                tqCheckDetailDTO.setCategoryDetailName(getCheckName(tqCheckDetail.getCategoryId(), 2));
                tqCheckDetailDTO.setSort(Long.parseLong(getCheckName(tqCheckDetail.getCategoryId(), 4)));
                detailDTOList = setTqCheckItemList(detailDTOList, tqCheckDetail, tqCheckDetailDTO);
            }
        }
        tqCheckDetailListDTO.setDetailDTOList(detailDTOList);
        return tqCheckDetailListDTO;
    }

    /**
     * 设置每个检测项目下 子检测类别 详情列表
     * e.g: PM2.5 - PM2.5测量 - 单位:微克/立 - '75' - 1(正常运行)
     */
    private List<TqCheckDetailDTO> setTqCheckItemList(List<TqCheckDetailDTO> detailDTOList, TqCheckDetail tqCheckDetail, TqCheckDetailDTO tqCheckDetailDTO) {
        if (!CollectionUtils.isEmpty(detailDTOList)) {
            //检测项目下具有 多个子检测类别
            for (TqCheckDetailDTO dto : detailDTOList) {
                if (dto.getCategory().equals(tqCheckDetail.getCategory())) {
                    TqCheckItemDTO tqCheckItemDTO = new TqCheckItemDTO();
                    tqCheckItemDTO.setCategoryItem(tqCheckDetail.getCategoryItem());
                    tqCheckItemDTO.setCategoryItemName(tqCheckDetail.getCategoryItemName());
                    tqCheckItemDTO.setItemValueType(tqCheckDetail.getItemValueType());
                    setTqCheckFlag(tqCheckItemDTO, tqCheckDetail.getCategory(), tqCheckDetail.getCategoryItem());
                    tqCheckItemDTO.setItemValueTypeName(TqChecksTypeEnum.getNameByKey(tqCheckDetail.getItemValueType()));
                    dto.getTqCheckItemDTOList().add(tqCheckItemDTO);
                    return detailDTOList;
                }
            }
        }
        //检测项目下只有 1 个子检测 类别
        TqCheckItemDTO tqCheckItemDTO = new TqCheckItemDTO();
        tqCheckItemDTO.setCategoryItem(tqCheckDetail.getCategoryItem());
        tqCheckItemDTO.setCategoryItemName(tqCheckDetail.getCategoryItemName());
        tqCheckItemDTO.setItemValueType(tqCheckDetail.getItemValueType());
        tqCheckItemDTO.setItemValueTypeName(TqChecksTypeEnum.getNameByKey(tqCheckDetail.getItemValueType()));
        setTqCheckFlag(tqCheckItemDTO, tqCheckDetail.getCategory(), tqCheckDetail.getCategoryItem());
        List<TqCheckItemDTO> tqCheckItemDTOs = new ArrayList<>();
        tqCheckItemDTOs.add(tqCheckItemDTO);
        tqCheckDetailDTO.setTqCheckItemDTOList(tqCheckItemDTOs);
        detailDTOList.add(tqCheckDetailDTO);
        return detailDTOList;
    }

    //检测项目名称 和 检测结果 范围数值 标志
    private void setTqCheckFlag(TqCheckItemDTO tqCheckItemDTO, Integer category, Integer itemType) {
        tqCheckItemDTO.setItemValueTypeOne(TqChecksTypeEnum.ZCYX.getName());
        tqCheckItemDTO.setItemValueTypeTwo(TqChecksTypeEnum.LYGC.getName());
        tqCheckItemDTO.setItemValueTypeThree(TqChecksTypeEnum.JYGH.getName());
        tqCheckItemDTO.setFlagOne(TqCheckFlagEnum.getFlagByCateAndItem(category, itemType, 1));
        tqCheckItemDTO.setFlagTwo(TqCheckFlagEnum.getFlagByCateAndItem(category, itemType, 2));
        tqCheckItemDTO.setFlagThree(TqCheckFlagEnum.getFlagByCateAndItem(category, itemType, 3));
    }

    /**
     * 获取淘汽检测所有检测分类
     */
    private Map<Long, TqCheckCategory> getAllTqCheckCategoryMap() {
        if (null != tqCheckCategoryMap) {
            return tqCheckCategoryMap;
        }
        List<TqCheckCategory> tqCheckCategoryList = tqCheckCategoryDao.select(null);
        tqCheckCategoryMap = new HashMap<>();
        for (TqCheckCategory tqCheckCategory : tqCheckCategoryList) {
            tqCheckCategoryMap.put(tqCheckCategory.getId(), tqCheckCategory);
        }
        return tqCheckCategoryMap;
    }

    /**
     * 获取所有检测项目 - 用户没有检测记录情况下（即新建检测记录）
     */
    private TqCheckDetailListDTO getTqCheckList(TqCheckDetailListDTO tqCheckDetailListDTO) {
        //所有检测项
        tqCheckCategoryMap = getAllTqCheckCategoryMap();
        List<TqCheckDetailDTO> detailDTOList = new ArrayList<>();
        for (TqCheckCategory tqCheckCategory : tqCheckCategoryMap.values()) {
            //设置每个检测的项目信息
            TqCheckDetailDTO tqCheckDetailDTO = new TqCheckDetailDTO();
            tqCheckDetailDTO.setCategory(tqCheckCategory.getCategory());
            tqCheckDetailDTO.setCategoryName(tqCheckCategory.getCategoryName());
            tqCheckDetailDTO.setCategoryDetailName(tqCheckCategory.getCategoryDetailName());
            TqCheckDetail tqCheckDetail = new TqCheckDetail();
            tqCheckDetail.setCategory(tqCheckCategory.getCategory());
            tqCheckDetail.setCategoryItem(tqCheckCategory.getCategoryItem());
            tqCheckDetail.setCategoryItemName(tqCheckCategory.getCategoryItemName());
            tqCheckDetailDTO.setSort(tqCheckCategory.getSort());
            //获取每个检测项下的子检测类别 和 检测结果标志
            detailDTOList = setTqCheckItemList(detailDTOList, tqCheckDetail, tqCheckDetailDTO);
        }
        tqCheckDetailListDTO.setDetailDTOList(detailDTOList);
        return tqCheckDetailListDTO;
    }

    /**
     * 根据 类目id ,获取类目名称或子检测类型名称和排序...
     */
    private String getCheckName(Long categoryId, Integer flag) {
        tqCheckCategoryMap = getAllTqCheckCategoryMap();
        if (tqCheckCategoryMap.containsKey(categoryId)) {
            TqCheckCategory tqCheckCategory = tqCheckCategoryMap.get(categoryId);
            //获取 类目名称
            if (flag.equals(1)) {
                return tqCheckCategory.getCategoryName();
            } else if (flag.equals(2)) {
                //获取具体检测项目范围名称
                return tqCheckCategory.getCategoryDetailName();
            } else if (flag.equals(3)) {
                //获取子类别名称
                return tqCheckCategory.getCategoryItemName();
            } else if (flag.equals(4)) {
                //排序
                return tqCheckCategory.getSort() + "";
            } else if (flag.equals(5)) {
                //分类id
                return tqCheckCategory.getId() + "";
            }
        }
        return null;
    }

    /**
     * 根据 类目 和 子检测类型 获取类目id或子检测类型名称
     */
    private String getIdAndItemName(Integer category, Integer categoryItem, Integer flag) {
        tqCheckCategoryMap = getAllTqCheckCategoryMap();
        for (TqCheckCategory tqCheckCategory : tqCheckCategoryMap.values()) {
            if (tqCheckCategory.getCategory().equals(category) && tqCheckCategory.getCategoryItem().equals(categoryItem)) {
                //获取 类目id
                if (flag.equals(1)) {
                    return tqCheckCategory.getId() + "";
                } else if (flag.equals(2)) {
                    //获取子检测项名称
                    return tqCheckCategory.getCategoryItemName();
                }
            } else {
                continue;
            }
        }
        return null;
    }

}
