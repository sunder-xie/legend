package com.tqmall.legend.facade.report.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.tqmall.common.exception.BizException;
import com.tqmall.common.template.BizTemplate;
import com.tqmall.common.util.BizAssert;
import com.tqmall.common.util.DateUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.cube.shop.RpcEmpPercentageService;
import com.tqmall.cube.shop.RpcPerformanceConfigService;
import com.tqmall.cube.shop.param.report.RepairPrefParam;
import com.tqmall.cube.shop.result.shop.*;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.report.StaffPerfFacade;
import com.tqmall.legend.facade.report.convert.EmpPrefConfigConverter;
import com.tqmall.legend.facade.report.vo.EmpPrefConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by tanghao on 16/10/12.
 */
@Service
@Slf4j
public class StaffPerfFacadeImpl implements StaffPerfFacade {
    @Autowired
    private RpcEmpPercentageService rpcEmpPercentageService;
    @Autowired
    private RpcPerformanceConfigService rpcPerformanceConfigService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;

    /**
     * 检测配置信息
     * @param shopId
     * @return
     */
    @Override
    public boolean checkConfig(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空.");
        boolean flag = rpcPerformanceConfigService.checkConfig(shopId);
        return flag;
    }

    /**
     * 获取门店维修提成配置信息
     * @param shopId
     * @return
     */
    @Override
    public List<EmpPrefConfig> getRepairPerformanceConfig(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空");
        String month = DateUtil.convertDateToYMDHMS(DateUtil.getEndMonth(new Date()));
        Result<List<EmployeePerformanceConfigVO>> result = rpcPerformanceConfigService.getPerformanceConfig(shopId,month);
        BizAssert.notNull(result,"调用获取绩效配置接口失败");
        if(result.isSuccess()){
            List<EmployeePerformanceConfigVO> list = result.getData();

            Set serviceIds = Sets.newHashSet();
            List<EmpPrefConfig> empPrefConfigs = Lists.newArrayList();
            for(EmployeePerformanceConfigVO resultVO : list){
                if(EmployeePerformanceConfigVO.CONFIGTYPE_SERVICE==resultVO.getConfigType()){
                    if(resultVO.getRelId()!= 0L){
                        serviceIds.add(resultVO.getRelId());
                    }
                    EmpPrefConfig empPrefConfig = new EmpPrefConfigConverter().convert(resultVO);
                    empPrefConfigs.add(empPrefConfig);
                }
            }
            /**
             * 设置服务信息
             */
            setServiceConfigInfo(serviceIds, empPrefConfigs);

            return empPrefConfigs;
        }else {
            throw new BizException(result.getMessage());
        }
    }

    /**
     * 设置服务信息
     * @param serviceIds
     * @param empPrefConfigs
     */
    private void setServiceConfigInfo(Set serviceIds, List<EmpPrefConfig> empPrefConfigs) {
        List<ShopServiceInfo> shopServiceInfos = Lists.newArrayList();
        if(serviceIds.size()>0){
            shopServiceInfos = shopServiceInfoService.selectAllByIds(Lists.<Long>newArrayList(serviceIds));
        }
        Map<Long,ShopServiceInfo> shopServiceInfoMap = Maps.uniqueIndex(shopServiceInfos, new Function<ShopServiceInfo, Long>() {
            @Override
            public Long apply(ShopServiceInfo shopServiceInfo) {
                return shopServiceInfo.getId();
            }
        });

        for(EmpPrefConfig ec : empPrefConfigs){
            ShopServiceInfo shopServiceInfo = shopServiceInfoMap.get(ec.getRelId());
            if(null != shopServiceInfo){
                ec.setPrice(shopServiceInfo.getServicePrice());
                ec.setRelName(shopServiceInfo.getName());
            }
        }
    }

    @Override
    public List<EmpPrefConfig> getSalePerformanceConfig(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空");
        String month = DateUtil.convertDateToYMDHMS(DateUtil.getEndMonth(new Date()));
        Result<List<EmployeePerformanceConfigVO>> result = rpcPerformanceConfigService.getPerformanceConfig(shopId,month);
        if(result.isSuccess()){
            List<EmployeePerformanceConfigVO> list = result.getData();

            Set goodsIds = Sets.newHashSet();
            List<EmpPrefConfig> empPrefConfigs = Lists.newArrayList();
            for(EmployeePerformanceConfigVO resultVO : list){
                if(EmployeePerformanceConfigVO.CONFIGTYPE_GOODS==resultVO.getConfigType()){
                    if(resultVO.getRelId()!= 0L){
                        goodsIds.add(resultVO.getRelId());
                    }
                    EmpPrefConfig empPrefConfig = new EmpPrefConfigConverter().convert(resultVO);
                    empPrefConfigs.add(empPrefConfig);
                }
            }
            /**
             * 设置物料信息
             */
            setGoodsConfigInfo(goodsIds, empPrefConfigs);

            return empPrefConfigs;
        }else {
            throw new BizException(result.getMessage());
        }
    }

    /**
     * 设置物料信息
     * @param goodsIds
     * @param empPrefConfigs
     */
    private void setGoodsConfigInfo(Set<Long> goodsIds, List<EmpPrefConfig> empPrefConfigs) {
        List<Goods> goodses = Lists.newArrayList();
        if(goodsIds.size()>0){
            goodses = goodsService.selectByIds(goodsIds.toArray(new Long[goodsIds.size()]));
        }
        Map<Long,Goods> goodsMap = Maps.uniqueIndex(goodses, new Function<Goods, Long>() {
            @Override
            public Long apply(Goods goods) {
                return goods.getId();
            }
        });

        for(EmpPrefConfig ec : empPrefConfigs){
            Goods goods = goodsMap.get(ec.getRelId());
            if(null != goods){
                ec.setPrice(goods.getPrice());
                ec.setRelName(goods.getName());
                ec.setMeasureUnit(goods.getMeasureUnit());
            }
        }
    }

    @Override
    public BigDecimal getSaPerformanceConfig(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空.");
        String month = DateUtil.convertDateToYMDHMS(DateUtil.getEndMonth(new Date()));
        Result<List<EmployeePerformanceConfigVO>> result = rpcPerformanceConfigService.getPerformanceConfig(shopId,month);
        if(null == result){
            log.error("获取门店绩效配置信息接口异常,shopId:"+shopId);
            throw new BizException("门店绩效配置接口异常.");
        }
        if(result.isSuccess()){
            List<EmployeePerformanceConfigVO> resultList = result.getData();
            for(EmployeePerformanceConfigVO e : resultList){
                if(EmployeePerformanceConfigVO.CONFIGTYPE_SERVICEADVISOR.equals(e.getConfigType())&&
                        EmployeePerformanceConfigVO.PERCENTAGETYPE_DEFAULT.equals(e.getPercentageType())){
                    return e.getPercentageRate();
                }
            }
        }else {
            throw new BizException(result.getMessage());
        }
        return null;
    }

    @Override
    public List<EmpPrefConfig> getAllPerformanceConfigByMonth(final Long shopId, final String month) {
        return new BizTemplate<List<EmpPrefConfig>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                Assert.notNull(shopId,"门店id不能为空.");
                Assert.hasText(month,"查询月份不能为空.");
            }

            @Override
            protected List<EmpPrefConfig> process() throws BizException {
                String monthStr = DateUtil.convertDateToYMDHMS(DateUtil.getEndMonth(DateUtil.convertStringToDate(month,"yyyy-MM")));
                Result<List<EmployeePerformanceConfigVO>> result = rpcPerformanceConfigService.getPerformanceConfig(shopId,monthStr);
                BizAssert.notNull(result,"查询配置信息接口调用失败.");
                BizAssert.notNull(result.getData(),"查询配置信息接口调用返回接口为空.");
                Set<Long> goodsIds = Sets.newHashSet();
                Set serviceIds = Sets.newHashSet();
                List<EmpPrefConfig> empPrefConfigs = Lists.newArrayList();
                for(EmployeePerformanceConfigVO resultVO : result.getData()){
                    if(EmployeePerformanceConfigVO.CONFIGTYPE_ADDPOINT.equals(resultVO.getConfigType())){
                        continue;
                    }
                    EmpPrefConfig empPrefConfig = new EmpPrefConfigConverter().convert(resultVO);
                    if(EmployeePerformanceConfigVO.CONFIGTYPE_GOODS.equals(resultVO.getConfigType())){
                        if(resultVO.getRelId()!= 0L){
                            goodsIds.add(resultVO.getRelId());
                        }
                    }
                    if(EmployeePerformanceConfigVO.CONFIGTYPE_SERVICE.equals(resultVO.getConfigType())){
                        if(resultVO.getRelId()!= 0L){
                            serviceIds.add(resultVO.getRelId());
                        }

                    }
                    empPrefConfigs.add(empPrefConfig);
                }
                //设置服务信息
                setServiceConfigInfo(serviceIds,empPrefConfigs);
                //设置物料信息
                setGoodsConfigInfo(goodsIds,empPrefConfigs);
                return empPrefConfigs;
            }
        }.execute();
    }


    /**
     * 提交配置信息
     * @param shopId
     * @param employeePerformanceConfigVOs
     * @return
     */
    @Override
    public boolean submitPerformanceConfig(final Long shopId, final List<EmployeePerformanceConfigVO> employeePerformanceConfigVOs) {
        return new BizTemplate<Boolean>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                BizAssert.isTrue(!CollectionUtils.isEmpty(employeePerformanceConfigVOs),"配置信息不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                Result<Boolean> result = rpcPerformanceConfigService.submitPerformanceConfig(shopId,employeePerformanceConfigVOs);
                BizAssert.notNull(result,"调用");
                BizAssert.isTrue(result.getData(),result.getMessage());
                return result.getData();
            }
        }.execute();
    }

    @Override
    public List<EmpPerformanceInfoVO> queryTotalPercentageInfo(Long shopId, String dateStr) {
        Assert.notNull(shopId,"门店id不能为空.");
        Assert.hasText(dateStr,"查询日期不能为空.");
        Result<List<EmpPerformanceInfoVO>> result = null;
        try {
            result = rpcEmpPercentageService.queryTotalPercentageInfo(shopId,dateStr);
        } catch (ParseException e) {
            throw new BizException("日期格式异常");
        }
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public List<EmpRepairInfoVO> queryTotalRepairPref(Long shopId, String dateStr) {
        Assert.notNull(shopId,"门店id不能为空.");
        Assert.hasText(dateStr,"查询日期不能为空.");
        Result<List<EmpRepairInfoVO>> result = null;
        try {
            result = rpcEmpPercentageService.queryTotalRepairInfo(shopId,dateStr);
        } catch (ParseException e) {
            throw new BizException("日期格式异常");
        }
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public SimplePage<EmpRepairInfoVO> queryRepairInfoGroupByService(RepairPrefParam repairPrefParam) {
        Assert.notNull(repairPrefParam,"参数对象不能为空");
        Assert.notNull(repairPrefParam.getShopId(),"门店id不能为空.");
        Assert.hasText(repairPrefParam.getDateStr(),"查询日期不能为空.");
        Assert.notNull(repairPrefParam.getEmpId(),"员工id不能为空.");
        Assert.notNull(repairPrefParam.getPage(),"当前页码不能为空");
        Result<SimplePage<EmpRepairInfoVO>> result = null;
        try {
            result = rpcEmpPercentageService.queryRepairInfoGroupByService(repairPrefParam);
        } catch (ParseException e) {
            throw new BizException("日期格式异常");
        }
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public SimplePage<EmpRepairInfoVO> queryRepairInfoGroupByOrder(RepairPrefParam repairPrefParam) {
        Assert.notNull(repairPrefParam,"参数对象不能为空");
        Assert.notNull(repairPrefParam.getShopId(),"门店id不能为空.");
        Assert.hasText(repairPrefParam.getDateStr(),"查询日期不能为空.");
        Assert.notNull(repairPrefParam.getEmpId(),"员工id不能为空.");
        Assert.notNull(repairPrefParam.getPage(),"当前页码不能为空");
        Result<SimplePage<EmpRepairInfoVO>> result = null;
        try {
            result = rpcEmpPercentageService.queryRepairInfoGroupByOrder(repairPrefParam);
        } catch (ParseException e) {
            throw new BizException("日期格式异常");
        }
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public List<EmpSaleInfoVO> queryTotalSaleInfo(Long shopId, String dateStr) {
        Assert.notNull(shopId,"门店id不能为空.");
        Assert.hasText(dateStr,"查询日期不能为空.");
        Result<List<EmpSaleInfoVO>> result = null;
        try {
            result = rpcEmpPercentageService.queryTotalSaleInfo(shopId,dateStr);
        } catch (ParseException e) {
            throw new BizException("日期格式异常");
        }
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public List<EmpSaleInfoVO> querySaleInfoGroupByService(Long shopId, String dateStr, Long empId) {
        Assert.notNull(shopId,"门店id不能为空.");
        Assert.hasText(dateStr,"查询日期不能为空.");
        Assert.notNull(empId,"员工id不能为空.");
        Result<List<EmpSaleInfoVO>> result = null;
        try {
            result = rpcEmpPercentageService.querySaleInfoGroupByService(shopId,empId,dateStr);
        } catch (ParseException e) {
            throw new BizException("日期格式异常");
        }
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public SimplePage<EmpSaleInfoVO> querySaleInfoGroupByOrder(RepairPrefParam repairPrefParam) {
        Assert.notNull(repairPrefParam,"参数对象不能为空");
        Assert.notNull(repairPrefParam.getShopId(),"门店id不能为空.");
        Assert.hasText(repairPrefParam.getDateStr(),"查询日期不能为空.");
        Assert.notNull(repairPrefParam.getEmpId(),"员工id不能为空.");
        Assert.notNull(repairPrefParam.getPage(),"当前页码不能为空");
        Result<SimplePage<EmpSaleInfoVO>> result = null;
        try {
            result = rpcEmpPercentageService.querySaleInfoGroupByOrder(repairPrefParam);
        } catch (ParseException e) {
            throw new BizException("日期格式异常");
        }
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public SimplePage<EmpSAInfoVO> querySAInfoGroupByOrder(RepairPrefParam repairPrefParam) {
        Assert.notNull(repairPrefParam,"参数对象不能为空");
        Assert.notNull(repairPrefParam.getShopId(),"门店id不能为空.");
        Assert.hasText(repairPrefParam.getDateStr(),"查询日期不能为空.");
        Assert.notNull(repairPrefParam.getEmpId(),"员工id不能为空.");
        Assert.notNull(repairPrefParam.getPage(),"当前页码不能为空");
        Result<SimplePage<EmpSAInfoVO>> result = null;
        try {
            result = rpcEmpPercentageService.getEmpSAPrefOrderStatistic(repairPrefParam);
        } catch (ParseException e) {
            throw new BizException("日期格式异常");
        }
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public List<EmpSAInfoVO> querySATotalInfo(Long shopId, String dateStr) {
        Assert.notNull(shopId,"门店id不能为空.");
        Assert.hasText(dateStr,"查询日期不能为空.");
        Result<List<EmpSAInfoVO>> result = null;
        try {
            result = rpcEmpPercentageService.getEmpSAPrefWithGrossProfitStatistic(shopId,dateStr);
        } catch (ParseException e) {
            throw new BizException("日期格式异常");
        }
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

}
