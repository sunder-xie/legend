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
import com.tqmall.cube.shop.RpcGatherPercentageService;
import com.tqmall.cube.shop.param.report.RepairPrefParam;
import com.tqmall.cube.shop.result.shop.*;
import com.tqmall.legend.biz.goods.GoodsService;
import com.tqmall.legend.biz.shop.ShopServiceInfoService;
import com.tqmall.legend.entity.goods.Goods;
import com.tqmall.legend.entity.shop.ShopServiceInfo;
import com.tqmall.legend.facade.report.GatherStaffPerfFacade;
import com.tqmall.legend.facade.report.bo.GatherConfigParam;
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
 * Created by tanghao on 16/12/16.
 */
@Service
@Slf4j
public class GatherStaffPerfFacadeImpl implements GatherStaffPerfFacade {

    final Integer PART_SUBMIT = 1;

    @Autowired
    private RpcGatherPercentageService rpcGatherPercentageService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ShopServiceInfoService shopServiceInfoService;
    @Autowired
    private RpcEmpPercentageService rpcEmpPercentageService;


    @Override
    public boolean checkConfig(Long shopId,String month) {
        Assert.notNull(shopId,"门店id不能为空.");
        com.tqmall.core.common.entity.Result<Boolean> result = rpcGatherPercentageService.checkConfig(shopId,month);
        BizAssert.notNull(result,"检测绩效配置信息接口返回对象为空");
        if(result.isSuccess()){
            return result.getData();
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public boolean submitPerformanceConfig(final Long shopId, final GatherConfigParam gatherConfigParam) {
        return new BizTemplate<Boolean>(){
            @Override
            protected void checkParams() throws IllegalArgumentException {
                BizAssert.notNull(gatherConfigParam,"提交配置对象不能为空");
                BizAssert.isTrue(!CollectionUtils.isEmpty(gatherConfigParam.getPerformanceConfigVOs()),"配置信息不能为空");
            }

            @Override
            protected Boolean process() throws BizException {
                com.tqmall.core.common.entity.Result<Boolean> result = rpcGatherPercentageService.submitPerformanceConfig(shopId,initSubmitConfigList(gatherConfigParam));
                BizAssert.notNull(result,"调用");
                BizAssert.isTrue(result.getData(),result.getMessage());
                return result.getData();
            }
        }.execute();
    }

    private List<EmployeePerformanceConfigVO> initSubmitConfigList(GatherConfigParam gatherConfigParam){
        if(gatherConfigParam.getType() == PART_SUBMIT){
            List<EmployeePerformanceConfigVO> resultList = gatherConfigParam.getPerformanceConfigVOs();
            EmployeePerformanceConfigVO service = new EmployeePerformanceConfigVO();
            service.setPercentageRate(BigDecimal.ZERO);
            service.setPercentageType(0);
            service.setConfigType(0);
            EmployeePerformanceConfigVO goods = new EmployeePerformanceConfigVO();
            goods.setPercentageRate(BigDecimal.ZERO);
            goods.setPercentageType(0);
            goods.setConfigType(1);
            EmployeePerformanceConfigVO sa = new EmployeePerformanceConfigVO();
            sa.setPercentageRate(BigDecimal.ZERO);
            sa.setPercentageType(0);
            sa.setConfigType(2);
            resultList.add(service);
            resultList.add(goods);
            resultList.add(sa);
            return resultList;
        }else {
            return gatherConfigParam.getPerformanceConfigVOs();
        }
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
                com.tqmall.core.common.entity.Result<List<EmployeePerformanceConfigVO>> result = rpcGatherPercentageService.getPerformanceConfig(shopId,monthStr);
                BizAssert.notNull(result,"查询配置信息接口调用失败.");
                BizAssert.notNull(result.getData(),"查询配置信息接口调用返回接口为空.");
                Set goodsIds = Sets.newHashSet();
                Set<Long> serviceIds = Sets.newHashSet();
                List<EmpPrefConfig> empPrefConfigs = Lists.newArrayList();
                for(EmployeePerformanceConfigVO resultVO : result.getData()){
                    EmpPrefConfig empPrefConfig = new EmpPrefConfigConverter().convert(resultVO);
                    if(EmployeePerformanceConfigVO.CONFIGTYPE_GOODS==resultVO.getConfigType()){
                        if(resultVO.getRelId()!= 0L){
                            goodsIds.add(resultVO.getRelId());
                        }
                    }
                    if(EmployeePerformanceConfigVO.CONFIGTYPE_SERVICE==resultVO.getConfigType()){
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

    @Override
    public List<EmpPrefConfig> getAddPointConfig(Long shopId) {
        Assert.notNull(shopId,"门店id不能为空");
        String month = DateUtil.convertDateToYMDHMS(DateUtil.getEndMonth(new Date()));
        com.tqmall.core.common.entity.Result<List<EmployeePerformanceConfigVO>> result = rpcGatherPercentageService.getPerformanceConfig(shopId,month);
        BizAssert.notNull(result,"调用获取绩效配置接口失败");
        if(result.isSuccess()){
            List<EmployeePerformanceConfigVO> list = result.getData();
            BizAssert.notNull(list,"绩效配置信息为空");
            List<EmpPrefConfig> empPrefConfigs = Lists.newArrayList();
            for(EmployeePerformanceConfigVO resultVO : list){
                if(EmployeePerformanceConfigVO.CONFIGTYPE_ADDPOINT==resultVO.getConfigType()){
                    EmpPrefConfig empPrefConfig = new EmpPrefConfigConverter().convert(resultVO);
                    empPrefConfigs.add(empPrefConfig);
                }
            }

            return empPrefConfigs;
        }else {
            throw new BizException(result.getMessage());
        }
    }

    @Override
    public List<GatherBossPerformanceInfoVO> queryBossCollect(final Long shopId, final String month) {
        return new BizTemplate<List<GatherBossPerformanceInfoVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                BizAssert.notNull(shopId,"门店id不能为空");
                BizAssert.hasText(month,"日期不能为空");
            }

            @Override
            protected List<GatherBossPerformanceInfoVO> process() throws BizException {
                com.tqmall.core.common.entity.Result<List<GatherBossPerformanceInfoVO>> result = null;
                try {
                    result = rpcEmpPercentageService.queryGatherBossPrefInfo(shopId,month);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                BizAssert.notNull(result,"cube获取老板绩效报表接口异常");
                BizAssert.notNull(result.getData(),"cube获取绩效报表接口数据异常");
                return result.getData();
            }
        }.execute();
    }

    @Override
    public GatherEmpPerformanceInfoVO queryUserCollect(final Long shopId, final String month, final Long userId) {
        return new BizTemplate<GatherEmpPerformanceInfoVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                BizAssert.notNull(shopId,"门店id不能为空.");
                BizAssert.hasText(month,"查询日期不能为空.");
                BizAssert.notNull(userId,"查询员工id不能为空");
            }

            @Override
            protected GatherEmpPerformanceInfoVO process() throws BizException {
                com.tqmall.core.common.entity.Result<GatherEmpPerformanceInfoVO> result = null;
                try {
                    result = rpcEmpPercentageService.queryGatherEmpPrefInfo(shopId,month,userId);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                BizAssert.notNull(result,"cube获取员工绩效报表接口异常");
                BizAssert.notNull(result.getData(),"cube获取员工绩效报表数据异常");
                return result.getData();
            }
        }.execute();
    }

    @Override
    public List<GatherAddPointPrefVO> queryAddPointInfo(final Long shopId, final String month) {
        return new BizTemplate<List<GatherAddPointPrefVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                BizAssert.notNull(shopId,"门店id不能为空");
                BizAssert.hasText(month,"月份不能为空");
            }

            @Override
            protected List<GatherAddPointPrefVO> process() throws BizException {
                com.tqmall.core.common.entity.Result<List<GatherAddPointPrefVO>> result = rpcEmpPercentageService.queryAddPointPrefStatistic(shopId,month);
                BizAssert.notNull(result,"cube获取加点绩效统计列表接口异常");
                BizAssert.notNull(result.getData(),"cube获取加点绩效统计列表数据异常");
                return result.getData();
            }
        }.execute();
    }

    @Override
    public GatherAddPointPrefVO queryAddPointDetail(final Long shopId, final String month, final Long userId) {
        return new BizTemplate<GatherAddPointPrefVO>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                BizAssert.notNull(shopId,"门店id不能为空");
                BizAssert.hasText(month,"月份不能为空");
                BizAssert.notNull(userId,"员工id不能为空");
            }

            @Override
            protected GatherAddPointPrefVO process() throws BizException {
                Result<GatherAddPointPrefVO> result = rpcEmpPercentageService.queryGatherAddPointDetail(shopId,month,userId);
                BizAssert.notNull(result,"cube获取加点提成详情接口异常");
                BizAssert.notNull(result.getData(),"cube获取加点提成详情数据异常");
                return result.getData();
            }
        }.execute();
    }

    @Override
    public List<GatherPrefSaleStarVO> querySaleStarList(final Long shopId, final String month, final Long userId) {
        return new BizTemplate<List<GatherPrefSaleStarVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                BizAssert.notNull(shopId,"门店id不能为空");
                BizAssert.hasText(month,"月份不能为空");
                BizAssert.notNull(userId,"员工id不能为空");
            }

            @Override
            protected List<GatherPrefSaleStarVO> process() throws BizException {
                Result<List<GatherPrefSaleStarVO>> result = rpcEmpPercentageService.querySaleStarList(shopId,month,userId);
                BizAssert.notNull(result,"cube获取销售之星列表接口异常");
                BizAssert.notNull(result.getData(),"cube获取销售之星列表数据异常");
                return result.getData();
            }
        }.execute();
    }

    @Override
    public SimplePage<GatherPrefNewCustomerPrefVo> queryNewCustomerList(final RepairPrefParam param) {
        return new BizTemplate<SimplePage<GatherPrefNewCustomerPrefVo>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                BizAssert.notNull(param.getShopId(),"门店id不能为空");
                BizAssert.hasText(param.getDateStr(),"月份不能为空");
                BizAssert.notNull(param.getEmpId(),"员工id不能为空");
                BizAssert.notNull(param.getPage(),"页码不能为空");
            }

            @Override
            protected SimplePage<GatherPrefNewCustomerPrefVo> process() throws BizException {
                Result<SimplePage<GatherPrefNewCustomerPrefVo>> result = rpcEmpPercentageService.queryNewCustomerPage(param);
                BizAssert.notNull(result,"cube获取拉新客户列表接口异常");
                BizAssert.notNull(result.getData(),"cube获取拉新客户列表数据异常");
                return result.getData();
            }
        }.execute();
    }

    @Override
    public SimplePage<GatherBusinessBeloneVO> queryBusinessBeloneList(final RepairPrefParam param) {
        return new BizTemplate<SimplePage<GatherBusinessBeloneVO>>() {
            @Override
            protected void checkParams() throws IllegalArgumentException {
                BizAssert.notNull(param.getShopId(),"门店id不能为空");
                BizAssert.hasText(param.getDateStr(),"月份不能为空");
                BizAssert.notNull(param.getEmpId(),"员工id不能为空");
                BizAssert.notNull(param.getPage(),"页码不能为空");
            }

            @Override
            protected SimplePage<GatherBusinessBeloneVO> process() throws BizException {
                Result<SimplePage<GatherBusinessBeloneVO>> result = rpcEmpPercentageService.queryBusinessBelonePage(param);
                BizAssert.notNull(result,"cube获取业绩归属列表接口异常");
                BizAssert.notNull(result.getData(),"cube获取业绩归属列表数据异常");
                return result.getData();
            }
        }.execute();
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

}
