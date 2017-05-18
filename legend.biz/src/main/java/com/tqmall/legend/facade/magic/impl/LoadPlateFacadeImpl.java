package com.tqmall.legend.facade.magic.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tqmall.common.util.DateUtil;
import com.tqmall.common.util.JSONUtil;
import com.tqmall.core.common.entity.Result;
import com.tqmall.legend.biz.bo.attendance.SignTime;
import com.tqmall.legend.biz.component.converter.DataSignTimeConverter;
import com.tqmall.legend.biz.enums.ProductionLineTypeEnum;
import com.tqmall.legend.biz.shop.ShopConfigureService;
import com.tqmall.legend.entity.shop.ShopConfigureTypeEnum;
import com.tqmall.legend.facade.magic.LaodPlateFacade;
import com.tqmall.legend.facade.magic.WorkTimeFacade;
import com.tqmall.legend.facade.magic.vo.*;
import com.tqmall.magic.object.param.shopmanager.TeamParam;
import com.tqmall.magic.object.param.workshop.ProductionLineParam;
import com.tqmall.magic.object.result.shopmanager.TeamDTO;
import com.tqmall.magic.object.result.workshop.*;
import com.tqmall.magic.service.shopmanger.RpcShopManagerExtService;
import com.tqmall.magic.service.workshop.RpcLineProcessManagerService;
import com.tqmall.magic.service.workshop.RpcPLineProcessRelService;
import com.tqmall.magic.service.workshop.RpcProductionLineService;
import com.tqmall.magic.service.workshop.RpcWorkOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yanxinyin on 16/7/22.
 */
@Service
@Slf4j
public class LoadPlateFacadeImpl implements LaodPlateFacade {

    @Autowired
    private RpcWorkOrderService rpcWorkOrderService;

    @Autowired
    private RpcProductionLineService rpcProductionLineService;

    @Autowired
    private RpcLineProcessManagerService rpcLineProcessManagerService;

    @Autowired
    private WorkTimeFacade workTimeFacade;
    @Autowired
    RpcShopManagerExtService rpcShopManagerExtService;
    @Autowired
    private ShopConfigureService shopConfigureService;
    @Autowired
    private RpcPLineProcessRelService rpcPLineProcessRelService;
    private Integer carNumberQuick = 0;//快修或者快喷
    private Integer carNumberSlow = 0; //事故
    private Integer carNumberInterrupt = 0;//中断

    /**
     * 通过shopId获取负载信息
     *
     * @param shopId
     * @return
     */
    @Override
    public Result<LoadVO> getMessage(Long shopId) {
        try {
            LoadPlateVO mapListkuaixiu = new LoadPlateVO();
            mapListkuaixiu.setType(1);
            mapListkuaixiu.setShopId(shopId);
            mapListkuaixiu = getLoadPlate(shopId, 1L, 0L);
            LoadPlateVO mapListsshigu = new LoadPlateVO();
            mapListsshigu.setType(2);
            mapListsshigu.setShopId(shopId);
            mapListsshigu = getLoadPlate(shopId, 2L, 0L);
            LoadPlateVO mapListkuaipen = new LoadPlateVO();
            mapListkuaipen.setType(3);
            mapListkuaipen.setShopId(shopId);
            mapListkuaipen = getLoadPlate(shopId, 3L, 0L);
            LoadVO loadVO = new LoadVO();
            List<LoadPlateVO> loadPlateVO = Lists.newArrayList();

            loadPlateVO.add(mapListkuaixiu);
            loadPlateVO.add(mapListsshigu);
            loadPlateVO.add(mapListkuaipen);
            loadVO.setLoadPlateVO(loadPlateVO);
            int waitNumber = 0;
            if (null != mapListkuaixiu.getPlateVOs()) {
                waitNumber = waitNumber + mapListkuaixiu.getPlateVOs().getWaitNumber();
            }
            if (null != mapListkuaipen.getPlateVOs()) {
                waitNumber = waitNumber + mapListkuaipen.getPlateVOs().getWaitNumber();
            }
            loadVO.setCarNumberQuick(waitNumber);
            //中断车辆
            carNumberInterrupt = waitCarNumber(shopId, null, "SGZD");
            loadVO.setCarNumberInterrupt(carNumberInterrupt);
            loadVO.setCarNumberSlow(carNumberSlow);
            carNumberQuick = 0;
            carNumberSlow = 0;
            carNumberInterrupt = 0;
            return Result.wrapSuccessfulResult(loadVO);
        } catch (Exception e) {
            log.error("[负载看板] 负载信息读取失败！e={}", e);
            return Result.wrapErrorResult("", "负载信息读取失败！");
        }
    }

    /**
     * 设置负载整个负载看板信息
     *
     * @param shopId
     * @param type
     * @param lineId
     * @return
     */
    public LoadPlateVO getLoadPlate(Long shopId, Long type, Long lineId) {
        LoadPlateVO loadPlateVO = new LoadPlateVO();
        List<BalanceBoardVO> balanceBoardVOs = Lists.newArrayList();
        //通过生产线类型查生产线一条线
        Map<Long, ProductionLineDTO> productionLineDTOMap = getProductionLine(shopId, type);

        Iterator<Map.Entry<Long, ProductionLineDTO>> it = productionLineDTOMap.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Long, ProductionLineDTO> entry = it.next();
            if (lineId == 0L) {
                lineId = entry.getKey();
            }
            if (type != 2) {
                //当前待修车辆数
                carNumberQuick = carNumberQuick + waitCarNumber(shopId, lineId, "DSG");
            } else {
                //滞留车辆
                carNumberSlow = carNumberSlow + waitCarNumber(shopId, lineId, "DSG");
            }

            //流水线负载信息
            PlateVO plateVO = new PlateVO();
            plateVO.setType(type);
            plateVO = getPlateVO(plateVO, shopId, lineId, type);
            loadPlateVO.setPlateVOs(plateVO);
            getBalenceBoardVO(shopId, type, lineId, balanceBoardVOs);
        }

        loadPlateVO.setType(Integer.valueOf(type.toString()));
        loadPlateVO.setTypeName(ProductionLineTypeEnum.getNameByType(Integer.valueOf(type.toString())));
        loadPlateVO.setBalanceBoardVOs(balanceBoardVOs);
        return loadPlateVO;
    }

    /**
     * 获取负载信息
     *
     * @param shopId
     * @param type
     * @param lineId
     * @param balanceBoardVOs
     */
    private void getBalenceBoardVO(Long shopId, Long type, Long lineId, List<BalanceBoardVO> balanceBoardVOs) {
        BalanceBoardVO balanceBoardVO = new BalanceBoardVO();
        List<LineBalanceVO> lineBalanceVOs = Lists.newArrayList();
        //通过生产线查找关联的工序
        Map<Long, List<PLineProcessRelDTO>> mapList = getProductionLineRel(shopId, lineId);
        List<PLineProcessRelDTO> pLineProcessRelDTOs = mapList.get(lineId);

        //一个工序有多个人在做
        for (PLineProcessRelDTO pLineProcessRelDTO : pLineProcessRelDTOs) {
            balanceBoardVO.setLineName(pLineProcessRelDTO.getLineName());

            Map<Long, List<LineProcessManagerDTO>> longListMap = getLineProcessManager(shopId, pLineProcessRelDTO.getId(), type);
            List<LineProcessManagerDTO> lineProcessManagerDTOs = longListMap.get(pLineProcessRelDTO.getId());
            //一个人有多个施工工序
            for (LineProcessManagerDTO managerDTO : lineProcessManagerDTOs) {

                LineBalanceVO lineBalanceVO = new LineBalanceVO();
                lineBalanceVO.setProcessId(pLineProcessRelDTO.getProcessId());
                lineBalanceVO.setProcessName(pLineProcessRelDTO.getProcessName());
                lineBalanceVO.setLineName(pLineProcessRelDTO.getLineName());
                lineBalanceVO.setLineType(type);
                lineBalanceVO.setManagerName(managerDTO.getManagerName());
                lineBalanceVO.setManagerId(managerDTO.getManagerId());
                lineBalanceVO.setTeamName(managerDTO.getTeamName());

                List<WorkOrderProcessRelDTO> workOrderRel = getWorkOrderRel(pLineProcessRelDTO.getProcessId(), managerDTO.getTeamId(), managerDTO.getManagerId());
                List<Long> ids = Lists.transform(workOrderRel, new Function<WorkOrderProcessRelDTO, Long>() {
                    @Override
                    public Long apply(WorkOrderProcessRelDTO workOrderProcessRelDTO) {
                        return workOrderProcessRelDTO.getWorkOrderId();
                    }
                });

                //没有车信息但是显示人的信息
                if (CollectionUtils.isEmpty(ids)) {
                    lineBalanceVOs.add(lineBalanceVO);
                    continue;
                }
                Map<Long, WorkOrderDTO> mapWorkOrder = getWorkOrder(ids.toArray(new Long[ids.size()]));
                lineBalanceVO = getLineBalanceVO(lineBalanceVO, workOrderRel, mapWorkOrder, lineId, managerDTO.getManagerId(), pLineProcessRelDTO.getProcessId(), type);
                lineBalanceVOs.add(lineBalanceVO);
            }
        }
        Collections.sort(lineBalanceVOs, new Comparator<LineBalanceVO>() {
            @Override
            public int compare(LineBalanceVO o1, LineBalanceVO o2) {
                //按照工序进行升序排列
                if (o1.getProcessId() > o2.getProcessId()) {
                    return 1;
                }
                if (o1.getProcessId() == o2.getProcessId()) {
                    return 0;
                }
                return 0;
            }
        });
        balanceBoardVO.setLineBalanceVOs(lineBalanceVOs);
        balanceBoardVOs.add(balanceBoardVO);
    }

    /**
     * 设置负载看板的接车台次,负载值,交车时间信息
     *
     * @param plateVO
     * @param shopId
     * @param lineId
     * @param type
     * @return
     */
    public PlateVO getPlateVO(PlateVO plateVO, Long shopId, Long lineId, Long type) {
        List<String> status = Lists.newArrayList();
        status.add("DSG");
        Date startTime = DateUtil.getStartTime();
        Date endTime = DateUtil.getEndTime(new Date());
        //等待的数量
        Integer waitNumber = waitNumber(shopId, lineId, status, null, null);
        plateVO.setWaitNumber(waitNumber);
        status.remove("DSG");
        status.add("SGZ");
        status.add("SGZD");
        //工作的数量
        Integer workNumber = waitNumber(shopId, lineId, status, null, null);
        plateVO.setWorkNumber(workNumber);

        //今天接车台次
        Integer rNumberToday = getCarNumberByTime(shopId, lineId, startTime, endTime);
        plateVO.setRNumberToday(rNumberToday);

        //昨天接车台次
        Date startTime1 = DateUtil.getYstdStartTime();
        Date endTime1 = DateUtil.getYstdEndTime();
        Integer rNumberYesterday = getCarNumberByTime(shopId, lineId, startTime1, endTime1);
        plateVO.setRNumberYesterday(rNumberYesterday);

        //前天接车台次
        Date startTime2 = DateUtil.getBYstdStartTime();
        Date endTime2 = DateUtil.getBYstdEndTime();
        Integer rNumberBYesterday = getCarNumberByTime(shopId, lineId, startTime2, endTime2);
        plateVO.setRNumberByesterday(rNumberBYesterday);

        //当月接车台次
        Date startTime3 = DateUtil.getStartMonth(new Date());
        Date endTime3 = DateUtil.getEndMonth(new Date());
        Integer rNumberMonth = getCarNumberByTime(shopId, lineId, startTime3, endTime3);
        plateVO.setRNumberMonth(rNumberMonth);

        //今天负载
        Double bNumberToday = getCarBanace(shopId, lineId, startTime, endTime, type);
        plateVO.setBNumberToday(bNumberToday);
        //昨天负载
        Double bNumberYesterday = getCarBanace(shopId, lineId, startTime1, endTime1, type);
        plateVO.setBNumberYesterday(bNumberYesterday);
        //前天负载
        Double bNumberBYesterday = getCarBanace(shopId, lineId, startTime2, endTime2, type);
        plateVO.setBNumberByesterday(bNumberBYesterday);
        //当月负载
        Double bNumberMonth = getCarBanace(shopId, lineId, startTime3, endTime3, type);
        plateVO.setBNumberMonth(bNumberMonth);

        SignTime time = shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.COMMUTETIME.getCode(), new DataSignTimeConverter<SignTime>());
        Integer count = time.getSignInTime().getHours() * 60 + time.getSignInTime().getMinutes();
        WorkTimeVo workTimeVo = workTimeFacade.initWorkTime(shopId);

        //最后一辆车交车时间
        Date lastTime = getLastTime(shopId, lineId, type, workTimeVo, count);
        plateVO.setLastTime(lastTime);

        //预计交车时间
        Date planTime = plianTimeNew(lastTime, workTimeVo, count, shopId, lineId, type);
        plateVO.setPlanTime(planTime);
        return plateVO;
    }

    @Override
    public List<ProductionLineVO> getProductionLine(Long shopId) {
        try {
            ProductionLineParam productionLineParam = new ProductionLineParam();
            productionLineParam.setShopId(shopId);
            Result<List<ProductionLineDTO>> page = rpcProductionLineService.selectProductionLineList(productionLineParam);
            List<ProductionLineVO> productionLineVOList = Lists.newArrayList();
            if (page.isSuccess() && page != null) {
                for (ProductionLineDTO productionLineDTO : page.getData()) {
                    ProductionLineVO productionLineVO = new ProductionLineVO();
                    BeanUtils.copyProperties(productionLineDTO, productionLineVO);
                    productionLineVOList.add(productionLineVO);
                }
            }
            return productionLineVOList;
        } catch (Exception e) {
            log.error("[查询生产线信息] 异常！e={}", e);
        }
        return Lists.newArrayList();
    }

    /**
     * 设置车辆信息
     *
     * @param lineBalanceVO
     * @param workOrderProcessRelDTOs
     * @param mapWorkOrder
     * @param lineId
     * @return
     */
    private LineBalanceVO getLineBalanceVO(LineBalanceVO lineBalanceVO, List<WorkOrderProcessRelDTO> workOrderProcessRelDTOs, Map<Long, WorkOrderDTO> mapWorkOrder,
                                           Long lineId, Long managerId, Long processId, Long type) {
        List<CarVO> list = Lists.newArrayList();
        for (WorkOrderProcessRelDTO workOrderProcessRelDTO : workOrderProcessRelDTOs) {
            if (processId != null && processId == workOrderProcessRelDTO.getProcessId()) {
                CarVO carVO = new CarVO();
                if (type != 2) {
                    if (managerId != null && managerId.equals(workOrderProcessRelDTO.getOperatorId())) {
                        WorkOrderDTO workOrderDTO = mapWorkOrder.get(workOrderProcessRelDTO.getWorkOrderId());
                        if (null != workOrderDTO && lineId == workOrderDTO.getLineId()) {
                            carVO.setPlanEndTime(workOrderProcessRelDTO.getPlanEndTime());
                            carVO.setPlanStartTime(workOrderProcessRelDTO.getPlanStartTime());
                            carVO.setLicense(workOrderDTO.getCarLicense());
                            carVO.setCustomerCarId(workOrderDTO.getCustomerCarId());
                            carVO.setProcessId(workOrderProcessRelDTO.getProcessId());
                            if (null != managerId) {
                                carVO.setManagerId(managerId);
                            }
                        }
                    }
                } else {
                    WorkOrderDTO workOrderDTO = mapWorkOrder.get(workOrderProcessRelDTO.getWorkOrderId());
                    if (null != workOrderDTO && lineId == workOrderDTO.getLineId()) {
                        carVO.setPlanEndTime(workOrderProcessRelDTO.getPlanEndTime());
                        carVO.setPlanStartTime(workOrderProcessRelDTO.getPlanStartTime());
                        carVO.setLicense(workOrderDTO.getCarLicense());
                        carVO.setCustomerCarId(workOrderDTO.getCustomerCarId());
                        carVO.setProcessId(workOrderProcessRelDTO.getProcessId());
                        if (null != managerId) {
                            carVO.setManagerId(managerId);
                        }
                    }
                }
                if (null != carVO && null != carVO.getLicense()) {
                    list.add(carVO);
                }
            }

        }

        Collections.sort(list, new Comparator<CarVO>() {
            @Override
            public int compare(CarVO o1, CarVO o2) {
                //按照工序进行升序排列
                return o1.getPlanStartTime().compareTo(o2.getPlanStartTime());
            }
        });
        lineBalanceVO.setCarVOList(list);
        return lineBalanceVO;
    }

    /**
     * 通过生产线类型查找生产线(一个生产线类型对应一个工序,以后会对应多个)
     *
     * @param shopId
     * @param type
     * @return
     */
    public Map<Long, ProductionLineDTO> getProductionLine(Long shopId, Long type) {
        ProductionLineParam p = new ProductionLineParam();
        p.setShopId(shopId);
        p.setType(Integer.valueOf(type.toString()));
        Result<List<ProductionLineDTO>> production = rpcProductionLineService.selectProductionLineList(p);
        Map<Long, ProductionLineDTO> map = Maps.newHashMap();
        if (production.isSuccess() && null != production.getData()) {
            List<ProductionLineDTO> list = production.getData();
            for (ProductionLineDTO productionLineDTO : list) {
                map.put(productionLineDTO.getId(), productionLineDTO);
            }
        }
        return map;
    }

    /**
     * 通过生产线ID查找生产线关系(一条线对应对个工序)
     *
     * @param shopId
     * @param lineId
     * @return
     */
    public Map<Long, List<PLineProcessRelDTO>> getProductionLineRel(Long shopId, Long lineId) {
        Result<List<PLineProcessRelDTO>> pLineProcessRelList = rpcPLineProcessRelService.getPLineProcessRelList(shopId, lineId);
        Map<Long, List<PLineProcessRelDTO>> map = Maps.newHashMap();
        if (pLineProcessRelList.isSuccess() && null != pLineProcessRelList.getData()) {
            List<PLineProcessRelDTO> lineProcessRelDTOs = pLineProcessRelList.getData();
            map.put(lineId, lineProcessRelDTOs);
        }
        return map;
    }


    /**
     * 生产线工序关联表id查找人员班组(一个工序对应的一个人或多人,或者班组)
     *
     * @param shopId
     * @param lineProcessId
     * @return
     */
    public Map<Long, List<LineProcessManagerDTO>> getLineProcessManager(Long shopId, Long lineProcessId, Long type) {
        Map<Long, List<LineProcessManagerDTO>> map = Maps.newHashMap();
        if (type != 2) {
            List<Long> lineProcessIds = Lists.newArrayList(lineProcessId);
            Result<List<LineProcessManagerDTO>> listResult = rpcLineProcessManagerService.
                    selectListByLineProcessIdLs(lineProcessIds, shopId);
            if (listResult.isSuccess() && null != listResult.getData()) {
                List<LineProcessManagerDTO> lineProcessManagerDTOs = listResult.getData();
                map.put(lineProcessId, lineProcessManagerDTOs);
            }
        } else {
            List<LineProcessManagerDTO> lineProcessManagerDTOs = Lists.newArrayList();
            LineProcessManagerDTO lineProcessManagerDTO = new LineProcessManagerDTO();
            TeamParam teamParam = new TeamParam();
            teamParam.setShopId(shopId);
            Result<List<TeamDTO>> listResult = rpcShopManagerExtService.getTeamList(teamParam);
            TeamDTO teamDTO = new TeamDTO();
            for (TeamDTO teamDTO1 : listResult.getData()) {
                if (teamDTO1.getName().contains("事故")) {
                    teamDTO.setId(teamDTO1.getId());
                    teamDTO.setName(teamDTO1.getName());
                    break;
                }
            }
            if (teamDTO != null) {
                lineProcessManagerDTO.setTeamId(teamDTO.getId());
                lineProcessManagerDTO.setTeamName(teamDTO.getName());
            }
            lineProcessManagerDTO.setShopId(shopId);
            lineProcessManagerDTO.setLineProcessId(lineProcessId);
            lineProcessManagerDTOs.add(lineProcessManagerDTO);
            map.put(lineProcessId, lineProcessManagerDTOs);
        }

        return map;
    }


    /**
     * 根据teamId ,processId,opreatorId,status ,time查施工单工序关联表 (找出某个人对应的工作)
     *
     * @param processId
     * @param teamId
     * @param opreatorId
     * @return
     */
    public List<WorkOrderProcessRelDTO> getWorkOrderRel(Long processId, Long teamId, Long opreatorId) {
        Date startTime = DateUtil.getStartTime();
        Date endTime = DateUtil.getEndTime(new Date());
        //显示工序在两天之内的数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endTime);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        endTime = calendar.getTime();
        List<WorkOrderProcessRelDTO> workOrderProcessRelDTOs = Lists.newArrayList();
        Result<List<WorkOrderProcessRelDTO>> workOrderList = rpcWorkOrderService.
                getWorkOrderListByTeamIdAndProcessIdAndOperatorId(processId, teamId, opreatorId, startTime, endTime);
        if (workOrderList.isSuccess() && null != workOrderList.getData()) {
            workOrderProcessRelDTOs = workOrderList.getData();

        }
        return workOrderProcessRelDTOs;
    }


    /**
     * 一个人有多个施工单
     *
     * @param ids
     * @return
     */
    public Map<Long, WorkOrderDTO> getWorkOrder(Long[] ids) {
        String status = "YWG";
        Result<List<WorkOrderDTO>> workOrderList = rpcWorkOrderService.
                getWorkOrderInfoByOrderIds(ids, status);
        Map<Long, WorkOrderDTO> map = Maps.newHashMap();
        if (workOrderList.isSuccess() && null != workOrderList.getData()) {
            List<WorkOrderDTO> workOrderDTOs = workOrderList.getData();
            for (WorkOrderDTO workOrderDTO : workOrderDTOs) {
                map.put(workOrderDTO.getId(), workOrderDTO);
            }
        }
        return map;
    }

    /**
     * 等待中车辆的数量 ,快修,快喷,事故,施工单状态: 施工单状态：DSG:待施工，SGZ:施工中，SGZD:施工中断，YWG:已完工,YWX:已无效
     *
     * @param shopId
     * @param lineId
     * @param status
     * @return
     */
    public Integer waitNumber(Long shopId, Long lineId, List<String> status, Date startTime, Date endTime) {
        Result<List<WorkOrderDTO>> selectWaitNumber = rpcWorkOrderService.
                selectNumberByStatus(shopId, lineId, status, startTime, endTime);
        Integer waitNumber = 0;
        if (selectWaitNumber.isSuccess() && !CollectionUtils.isEmpty(selectWaitNumber.getData())) {
            waitNumber = selectWaitNumber.getData().size();
        }
        return waitNumber;
    }


    /**
     * 接车次数 :前天,昨天,今天,当月
     *
     * @param shopId
     * @param lineId
     * @return
     */
    public Integer getCarNumberByTime(Long shopId, Long lineId, Date startTime, Date endTime) {
        Result<List<WorkOrderDTO>> orderList = rpcWorkOrderService.selectNumberByTime(shopId, lineId, startTime, endTime);
        Integer waitNumber = 0;
        if (orderList.isSuccess() && !CollectionUtils.isEmpty(orderList.getData())) {
            waitNumber = orderList.getData().size();
        }
        return waitNumber;
    }

    /**
     * 最后一辆车结束时间
     *
     * @param shopId
     * @param lineId
     * @return
     */
    public Date getLastTime(Long shopId, Long lineId, Long type, WorkTimeVo workTimeVo, Integer count) {
        Date planTime = null;
        Result<List<WorkOrderDTO>> orderList = rpcWorkOrderService.
                selectNumberByTime(shopId, lineId, null, null);
        if (orderList.isSuccess() && !CollectionUtils.isEmpty(orderList.getData())) {
            List<WorkOrderDTO> list = orderList.getData();
            if (!CollectionUtils.isEmpty(list)) {
                planTime = list.get(list.size() - 1).getPlanEndTime();
                if (planTime != null) {
                    planTime = DateUtil.timePlus(planTime, 60);
                    planTime = getPlantDate(planTime, shopId, 1, workTimeVo, count);
                }
            }
        }
        return planTime;
    }

    /**
     * 预计交车时间
     *
     * @param shopId
     * @param lineId
     * @return
     */
    public Date plianTime(Long shopId, Long lineId, Long type, WorkTimeVo workTimeVo, Integer count) {
        Date planTime = null;
        Result<List<WorkOrderDTO>> orderList = rpcWorkOrderService.selectNumberByTime(shopId, lineId, null, null);
        if (orderList.isSuccess() && null != orderList.getData()) {
            List<WorkOrderDTO> list = orderList.getData();
            if (!CollectionUtils.isEmpty(list)) {
                planTime = list.get(list.size() - 1).getPlanEndTime();
                if (planTime != null) {
                    planTime = DateUtil.timePlus(planTime, 60);
                    planTime = getPlantDate(planTime, shopId, 2, workTimeVo, count);
                }
                if (planTime != null) {
                    planTime = getDate(shopId, lineId, type, planTime);

                }
            }
        }
        return planTime;
    }


    /**
     * 预计交车时间
     *
     * @param lastTime
     * @return
     */
    public Date plianTimeNew(Date lastTime, WorkTimeVo workTimeVo, Integer count, Long shopId, Long lineId, Long type) {

        if (null == lastTime) {
            log.error("计算交车时间时间异常,原因为最后交车时间为null");
            return null;
        }
        lastTime = DateUtil.timePlus(lastTime, 60);

        // 如果是在午休开始和午休结束之间 , 则增加1小时.
        Date date1 = DateUtil.convertStringToTodayYMDHMS(lastTime.getHours() + ":" + lastTime.getMinutes());
        if (date1.after(workTimeVo.getNoonBreakStartDate()) && date1.before(workTimeVo.getNoonBreakEndDate()) || date1.equals(workTimeVo.getNoonBreakEndDate())) {
            lastTime = DateUtil.timePlus(lastTime, 60);
            return lastTime;
        }


        // 预计交车时间
        Date A = DateUtil.convertStringToTodayYMDHMS(lastTime.getHours() + ":" + lastTime.getMinutes());

        // 下班时间
        Date B = workTimeVo.getCloseDate();

        Integer date3 = lastTime.getMinutes();

        lastTime = getDate(shopId, lineId, type, lastTime);

        // 如果是次日
        if (A.after(B)) {
            lastTime = DateUtil.getStartTime(lastTime);
            lastTime = DateUtil.addDate(lastTime, 1);
            if ((A.getTime() - B.getTime()) / 3600000 >= 1) {
                lastTime = DateUtil.timePlus(lastTime, (count + 60) + date3);
            } else {
                Long temp = (A.getTime() - B.getTime()) / 60000;
                lastTime = DateUtil.timePlus(lastTime, count + temp.intValue());
            }
        }

        return lastTime;
    }

    /**
     * 添加在12点和1点之间顺延1小时,如果在下班时间之后的时间推迟到第二日开工后一小时.
     *
     * @param planTime
     * @return
     */
    private Date getPlantDate(Date planTime, Long shopId, Integer flag, WorkTimeVo workTimeVo, Integer count) {
        Date date = DateUtil.convertStringToTodayYMDHMS(planTime.getHours() + ":" + planTime.getMinutes());

        if (date.after(workTimeVo.getNoonBreakStartDate()) && date.before(workTimeVo.getNoonBreakEndDate()) || date.equals(workTimeVo.getNoonBreakEndDate())) {
            planTime = DateUtil.timePlus(planTime, 60);
        }

        if (flag == 2) {
            planTime = DateUtil.timePlus(planTime, 60);
            Date date1 = DateUtil.convertStringToTodayYMDHMS(planTime.getHours() + ":" + planTime.getMinutes());
            if (date1.after(workTimeVo.getNoonBreakStartDate()) && date1.before(workTimeVo.getNoonBreakEndDate()) || date1.equals(workTimeVo.getNoonBreakEndDate())) {
                planTime = DateUtil.timePlus(planTime, 60);
            }
        }
        planTime = outWorkTime(planTime, count, workTimeVo);

        return planTime;
    }

    private Date outWorkTime(Date planTime, Integer count, WorkTimeVo workTimeVo) {
        Date date2 = DateUtil.convertStringToTodayYMDHMS(planTime.getHours() + ":" + planTime.getMinutes());


        // 预计交车时间
        Date A = DateUtil.convertStringToTodayYMDHMS(planTime.getHours() + ":" + planTime.getMinutes());
        Integer date3 = planTime.getMinutes();
        // 下班时间
        Date B = workTimeVo.getCloseDate();
        // 如果是次日
        if (A.after(B)) {
            planTime = DateUtil.getStartTime(planTime);
            planTime = DateUtil.addDate(planTime, 1);
            if ((A.getTime() - B.getTime()) / 3600000 >= 1) {
                planTime = DateUtil.timePlus(planTime, count + 60 + date3);
            } else {
                Long temp = (A.getTime() - B.getTime()) / 60000;
                planTime = DateUtil.timePlus(planTime, count + temp.intValue());
            }
        }

//        if (date2.after(workTimeVo.getCloseDate())) {
//            Integer date3 = planTime.getMinutes();
//            planTime = DateUtil.getStartTime(planTime);
//            planTime = DateUtil.addDate(planTime, 1);
//            planTime = DateUtil.timePlus(planTime, 60 * count + date3);
//        }
        return planTime;
    }

    /**
     * 事故线的计划生产周期
     *
     * @param shopId
     * @param lineId
     * @param type
     * @param planTime
     * @return
     */
    private Date getDate(Long shopId, Long lineId, Long type, Date planTime) {
        Integer workTime = 0;
        if (type == 2) {
            Result<List<PLineProcessRelDTO>> pLineProcessRelDTOResult = rpcPLineProcessRelService.getPLineProcessRelList(shopId, lineId);
            if (pLineProcessRelDTOResult.isSuccess() && null != pLineProcessRelDTOResult.getData()) {
                List<PLineProcessRelDTO> list = pLineProcessRelDTOResult.getData();
                for (PLineProcessRelDTO p : list) {
                    if (null != p && p.getProcessName().equals("钣金")) {
                        workTime = workTime + Integer.valueOf(p.getWorkTime());
                    }

                }
            }
            planTime = DateUtil.timePlus(planTime, workTime / 7);

        }
        return planTime;
    }

    /**
     * 当前等待车辆数
     *
     * @param shopId
     * @param LineId
     * @param status
     * @return
     */

    public Integer waitCarNumber(Long shopId, Long LineId, String status) {
        List<String> list = Lists.newArrayList();
        list.add(status);
        Result<List<WorkOrderDTO>> workOrderList = rpcWorkOrderService.
                selectNumberByStatus(shopId, LineId, list, null, null);
        Integer result = 0;
        if (workOrderList.isSuccess() && !CollectionUtils.isEmpty(workOrderList.getData())) {
            List<WorkOrderDTO> lists = workOrderList.getData();
            result = lists.size();
        }
        return result;
    }

    /**
     * 计算负载看板负载
     *
     * @param shopId
     * @param lineId
     * @param startTime
     * @param endTime
     * @return
     */
    public Double getCarBanace(Long shopId, Long lineId, Date startTime, Date endTime, Long type) {
        //通过生产线查找关联的工序
        Double banace = 0.00;
        //分子
        Integer minute1 = getPlanWorkTime(shopId, lineId, startTime, endTime);
        //分母
        Integer minute2 = getManagerNumber(shopId, lineId, startTime, endTime, type);
        if (minute2 != 0) {
            banace = Double.valueOf(minute1) / minute2 * 100;
        }
        BigDecimal b = new BigDecimal(banace);
        banace = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        return banace;
    }

    /**
     * 取负载看板的分母
     *
     * @param shopId
     * @param lineId
     * @return
     */
    public Integer getManagerNumber(Long shopId, Long lineId, Date startTime, Date endTime, Long type) {
        Map<Long, List<PLineProcessRelDTO>> mapList = getProductionLineRel(shopId, lineId);
        List<PLineProcessRelDTO> pLineProcessRelDTOs = mapList.get(lineId);
        List<Long> lineProcessId = Lists.transform(pLineProcessRelDTOs, new Function<PLineProcessRelDTO, Long>() {
            @Override
            public Long apply(PLineProcessRelDTO pLineProcessRelDTO) {
                return pLineProcessRelDTO.getId();
            }
        });
        Integer managerNumber = 0;
        if (type == 1) {
            for (Long l : lineProcessId) {
                Map<Long, List<LineProcessManagerDTO>> longListMap = getLineProcessManager(shopId, l, type);
                List<LineProcessManagerDTO> lineProcessManagerDTOs = longListMap.get(l);
                managerNumber = managerNumber + lineProcessManagerDTOs.size();
            }
        }
        if (type == 2) {
            managerNumber = 7;
        }
        if (type == 3) {
            managerNumber = 1;
        }

        SignTime time = shopConfigureService.getShopConfigure(shopId, ShopConfigureTypeEnum.COMMUTETIME.getCode(), new DataSignTimeConverter<SignTime>());
        //人数 * 时间 * 60
        managerNumber = managerNumber * (time.getSignOffTime().getHours() - time.getSignInTime().getHours() - 1) * 60;
        Date date = new Date();
        date = DateUtil.getStartMonth(date);
        //一个月的时间计算.一个月按照30天
        if (startTime.equals(date)) {
            managerNumber = managerNumber * 30;
        }
        return managerNumber;
    }

    /**
     * 取负载看板的分子
     *
     * @param shopId
     * @param lineId
     * @param startTime
     * @param endTime
     * @return
     */
    public Integer getPlanWorkTime(Long shopId, Long lineId, Date startTime, Date endTime) {
        Integer minute = 0;
        //两天内派工的订单
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date startTime1 = calendar.getTime();
        Result<List<WorkOrderDTO>> orderList = rpcWorkOrderService.
                selectNumberByTime(shopId, lineId, startTime1, endTime);
        if (orderList.isSuccess()) {
            List<WorkOrderDTO> list = orderList.getData();
            List<Long> ids = Lists.transform(list, new Function<WorkOrderDTO, Long>() {
                @Override
                public Long apply(WorkOrderDTO workOrderDTO) {
                    return workOrderDTO.getId();
                }
            });
            if (!CollectionUtils.isEmpty(ids)) {
                log.info("调用magic获取负载率开始，参数为：{}, Ids为：{}，开始时间为：{}, 结束时间为：{}", shopId, ids, startTime, endTime);
                Result<List<WorkOrderProcessRelDTO>> workOrderProcessRelList = rpcWorkOrderService.selectByShopIdAndWorkOrderIds(shopId, ids, null, null);
                log.info("调用magic获取负载率结束，结果为：{}", JSONUtil.object2Json(workOrderProcessRelList));
                if (workOrderProcessRelList.isSuccess()) {
                    List<WorkOrderProcessRelDTO> workOrderProcessRels = workOrderProcessRelList.getData();
                    for (WorkOrderProcessRelDTO workOrderProcessRelDTO : workOrderProcessRels) {
                        if (null != workOrderProcessRelDTO.getPlanStartTime() && null != workOrderProcessRelDTO.getPlanEndTime()) {
                            if (((workOrderProcessRelDTO.getPlanStartTime().after(startTime) && workOrderProcessRelDTO.getPlanStartTime().before(endTime))
                                    || (workOrderProcessRelDTO.getPlanEndTime().after(startTime) && workOrderProcessRelDTO.getPlanEndTime().before(endTime)))) {
                                if (null != workOrderProcessRelDTO.getWorkTime() && 0 < Integer.valueOf(workOrderProcessRelDTO.getWorkTime())) {
                                    minute = minute + Integer.valueOf(workOrderProcessRelDTO.getWorkTime());
                                }

                            }
                        }

                    }
                }
            }
        }
        return minute;
    }
}
