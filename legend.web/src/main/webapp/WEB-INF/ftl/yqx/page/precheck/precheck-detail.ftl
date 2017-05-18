<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/precheck/precheck-detail.css?9ffc82c7b2a91bbb3c2f8172181fd81a"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl ">
        <h3 class="title">车况登记详细页</h3>
        <!--车辆详情 start-->
        <div class="car-message">
            <div class="detail-row">
                <div class="form-label detail-title order-bold">预检单号:</div>
                <div class="form-item">
                    <div class="yqx-text detail-width order-bold">
                    ${precheck.precheckSn}
                    </div>
                </div>
                <div class="form-label detail-title order-bold">预检时间:</div>
                <div class="form-item">
                    <div class="yqx-text detail-width order-bold">
                     ${precheck.gmtCreateStr}
                    </div>
                </div>
            </div>
            <div class="detail-row">
                <div class="form-label detail-title">车牌:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${customerCar.license}
                    </div>
                </div>
                <div class="form-label detail-title">车型:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message js-show-tips" title="${customerCar.carInfo}">
                    ${customerCar.carInfo}
                    </div>
                </div>
                <div class="form-label detail-title">预检登记人:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                        ${shopManager.name}
                    </div>
                </div>
            </div>
            <div class="detail-row">
                <div class="form-label detail-title">联系人:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${precheck.customerName}
                    </div>
                </div>
                <div class="form-label detail-title">联系电话:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${precheck.mobile}
                    </div>
                </div>
                <div class="form-label detail-title">保险到期:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${precheck.dueDateStr}
                    </div>
                </div>

            </div>
            <div class="detail-row">
                <div class="form-label detail-title">行驶里程:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${customerCar.mileage}
                    </div>
                </div>
                <div class="form-label detail-title">下次保养里程:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${customerCar.upkeepMileage}
                    </div>
                </div>
                <div class="form-label detail-title">下次保养时间:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${precheck.nextTime}
                    </div>
                </div>

            </div>
            <div class="detail-row">
                <div class="form-label detail-title">备注:</div>
                <div class="form-item">
                    <div class="yqx-text detail-message js-show-tips">
                    ${precheck.comments}
                    </div>
                </div>
            </div>
        </div>
        <!--车辆详情 end-->

        <!--车况信息 start-->
        <div class="car-infor">
            <div class="car-infor-tab">车况信息</div>

            <div class="car-condition">
                <#if (appearanceCount > 0)>
                    <#include "yqx/tpl/order/static-precheck-tpl.ftl">
                    <div class="car-condition-infor">
                        <#list precheckDetailsList as precheckDetails>
                            <#if  (precheckDetails.ftlId == "out_lights") >
                                <span>灯  光：
                                    <#list precheckDetailsList as precheckDetail>
                                        <#if (precheckDetail.ftlId == "out_lights")>
                                        ${precheckDetail.suggestion}
                                        </#if>
                                    </#list>
                                        </span>
                            </#if>
                        </#list>
                      </div>
                  </#if>
<!--客户需求 -->
                    <#if (requestList?size >0 )|| ((precheck.manHour != null ) &&(precheck.manHour != "0") )|| ((precheck.expFee != null )&& (precheck.expFee != "0"))>

                    <div class="car-condition-title">客户需求</div>
                    <div class="car-condition-infor">
                        <#if (requestList?size == 1)>
                            <#list requestList as req>
                                <span>内容:${req.content}</span>
                                <span>要求(所需物料):${req.contentGoods}</span>
                            </#list>
                        </#if>
                        <#if (requestList?size >1)>
                            <#list requestList as req>
                                <span>内容${req_index+1}:${req.content}</span>
                                <span>要求(所需物料)${req_index+1}:${req.contentGoods}</span>
                            </#list>
                        </#if>
                        <#if (precheck.manHour != null && precheck.manHour != "0") >
                            <span>预计工时:${precheck.manHour}</span>
                        </#if>
                        <#if precheck.expFee != null && precheck.expFee != "0">
                            <span>预计费用:${precheck.expFee}</span>
                        </#if>
                    </div>
                    </#if>
<!-- 车内设施-->
     <#if (carFacilityCount > 0) >
        <div class="car-condition-title">车内设施</div>
        <div class="car-condition-infor">
                 <#list precheckDetailsList as precheckDetails>
                     <#if  (precheckDetails.ftlId == "interior") >
                         <span>内饰:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="interior")>
                                 ${precheckDetail.suggestion}
                                </#if>
                            </#list>
                         </span>
                     </#if>
                 </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "screen") >
                        <span>显示屏:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="screen")>
                                ${precheckDetail.suggestion}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "audio") >
                        <span>音响:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="audio")>
                                ${precheckDetail.suggestion}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "cigar_lighter") >
                        <span>点烟器:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="cigar_lighter")>
                                ${precheckDetail.suggestion}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "glass_lifter") >
                        <span>升降器:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="glass_lifter")>
                                ${precheckDetail.suggestion}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "auto_door") >
                        <span>自动门锁:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="auto_door")>
                                ${precheckDetail.suggestion}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
        </div>
     </#if>
      <!-- 仪表-->
      <#if (meterCount >0) || (oilCount > 0) >
            <div class="car-condition-title">仪表</div>
            <div class="car-condition-infor">
                    <#list precheckDetailsList as precheckDetails>
                        <#if  (precheckDetails.ftlId == "trouble_light") >
                            <span>故障灯:
                                <#list precheckDetailsList as precheckDetail>
                                    <#if (precheckDetail.ftlId=="trouble_light")>
                                    ${precheckDetail.suggestion}
                                    </#if>
                                </#list>
                            </span>
                        </#if>
                    </#list>
                    <#list precheckDetailsList as precheckDetails>
                        <#if  (precheckDetails.ftlId == "dashboard") >
                            <span>仪表:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="dashboard")>
                                ${precheckDetail.suggestion}
                                </#if>
                            </#list>
                        </span>
                        </#if>
                    </#list>
                    <#list precheckDetailsList as precheckDetails>
                        <#if  (precheckDetails.ftlId == "oil_meter") >
                            <span>油表油量:
                                <#list precheckDetailsList as precheckDetail>
                                    <#if (precheckDetail.ftlId=="oil_meter")>
                                    ${precheckDetail.precheckValue}
                                    </#if>
                                </#list>
                            </span>
                        </#if>
                    </#list>
            </div>
      </#if>
<!--发动机 -->
      <#if (engineCount > 0) >
         <div class="car-condition-title">发动机</div>
         <div class="car-condition-infor">
                 <#list precheckDetailsList as precheckDetails>
                     <#if  (precheckDetails.ftlId == "noise") >
                         <span>异响 :
                             <#list precheckDetailsList as precheckDetail>
                                 <#if (precheckDetail.ftlId=="noise")>
                                    ${precheckDetail.suggestion}
                                </#if>
                             </#list>
                         </span>
                     </#if>
                 </#list>
                 <#list precheckDetailsList as precheckDetails>
                     <#if  (precheckDetails.ftlId == "func_error") >
                         <span>使用异常 :
                             <#list precheckDetailsList as precheckDetail>
                                 <#if (precheckDetail.ftlId=="func_error")>
                                 ${precheckDetail.suggestion}
                                 </#if>
                             </#list>
                         </span>
                     </#if>
                 </#list>
        </div>
      </#if>
      <#if (tyreCount > 0 )>
        <div class="car-condition-title">轮胎</div>
        <div class="car-condition-infor">

              <#list precheckDetailsList as precheckDetails>
                  <#if  (precheckDetails.ftlId == "left_front_hub") ||(precheckDetails.ftlId == "rigth_front_hub") || (precheckDetails.ftlId == "left_rear_hub") ||(precheckDetails.ftlId == "rigth_rear_hub")>
                      <span>钢圈：
                          <#list precheckDetailsList as precheckDetail>
                              <#if (precheckDetail.ftlId == "left_front_hub")>
                              ${"左前${precheckDetail.precheckValue}"}
                              </#if>
                              <#if (precheckDetail.ftlId == "right_front_hub")>
                              ${"右前${precheckDetail.precheckValue}"}
                              </#if>
                              <#if (precheckDetail.ftlId == "left_rear_hub")>
                              ${"左后${precheckDetail.precheckValue}"}
                              </#if>
                              <#if (precheckDetail.ftlId == "right_rear_hub")>
                              ${"右后${precheckDetail.precheckValue}"}
                              </#if>
                          </#list>
                      </span>
                      <#break>
                  </#if>
              </#list>

              <#list precheckDetailsList as precheckDetails>
                  <#if   (precheckDetails.ftlId == "tyre_change") >
                      <span>建议更换里程 :
                      <#list precheckDetailsList as precheckDetail>
                          <#if (precheckDetail.ftlId=="tyre_change")>
                          ${"${precheckDetail.suggestion} 公里"}
                          </#if>
                      </#list>
                  </span>
                  <#break>
                  </#if>
              </#list>

                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "tyre_error") >
                        <span>异常记录 :
                        <#list precheckDetailsList as precheckDetail>
                            <#if (precheckDetail.ftlId=="tyre_error")>
                            ${precheckDetail.suggestion}
                            </#if>
                        </#list>
                    </span>
                    </#if>
                </#list>
          </div>
      </#if>
<!--其他 -->
      <#if (otherCount > 0 )>
        <div class="car-condition-title">其他</div>
        <div class="car-condition-infor">
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "three_filters") >
                        <span>三滤:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="three_filters")>
                                ${"${precheckDetail.suggestion}公里"}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "engine_oil") >
                        <span>机油:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="engine_oil")>
                                ${"${precheckDetail.suggestion}公里"}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "spark_plug") >
                        <span>火花塞:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="spark_plug")>
                                ${"${precheckDetail.suggestion}公里"}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "brake_oil") >
                        <span>刹车油:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="brake_oil")>
                                ${"${precheckDetail.suggestion}公里"}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "rain_wiper") >
                        <span>雨刮:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId=="rain_wiper")>
                                ${"${precheckDetail.suggestion}公里"}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "gear_oil") >
                        <span>变速箱油:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId == "gear_oil")>
                                ${"${precheckDetail.suggestion}公里"}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "brake_pad") >
                        <span>刹车:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId == "brake_pad")>
                                ${"${precheckDetail.suggestion}公里"}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "antifreeze") >
                        <span>防冻液:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId == "antifreeze")>
                                ${"${precheckDetail.suggestion}公里"}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "battery") >
                        <span>电瓶:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId == "battery")>
                                ${"${precheckDetail.suggestion}公里"}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "steering_fluid") >
                        <span>助力液:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId == "steering_fluid")>
                                ${"${precheckDetail.suggestion}公里"}

                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>

                <#list precheckDetailsList as precheckDetails>
                    <#if  (precheckDetails.ftlId == "other_suggestion") >
                        <span>其他建议:
                            <#list precheckDetailsList as precheckDetail>
                                <#if (precheckDetail.ftlId == "other_suggestion")>
                                ${precheckDetail.suggestion}
                                </#if>
                            </#list>
                        </span>
                    </#if>
                </#list>
        </div>
      </#if>
<!--随车物品-->
                    <div class="car-condition-infor">

                        <#list precheckDetailsList as precheckDetails>
                            <#if   ((precheckDetails.ftlId == "key")|| (precheckDetails.ftlId == "file_in_car")
                            || (precheckDetails.ftlId == "jack") || (precheckDetails.ftlId == "drive_license") || (precheckDetails.ftlId == "extinguisher")
                            || (precheckDetails.ftlId == "car_license") || (precheckDetails.ftlId == "tool_box") || (precheckDetails.ftlId == "back_tyre"))  >

                                <span>随车物品:
                                <#list precheckDetailsList as precheckDetail>
                                    <#if (precheckDetail.ftlId == "key")>
                                         ${precheckDetail.precheckItemName}
                                    </#if>
                                    <#if (precheckDetail.ftlId == "jack")>
                                         ${precheckDetail.precheckItemName}
                                    </#if>
                                    <#if (precheckDetail.ftlId=="extinguisher")>
                                         ${precheckDetail.precheckItemName}
                                    </#if>
                                    <#if (precheckDetail.ftlId == "drive_license")>
                                         ${precheckDetail.precheckItemName}
                                    </#if>
                                    <#if (precheckDetail.ftlId=="car_license")>
                                        ${precheckDetail.precheckItemName}
                                    </#if>
                                    <#if (precheckDetail.ftlId == "tool_box")>
                                        ${precheckDetail.precheckItemName}
                                    </#if>
                                    <#if (precheckDetail.ftlId == "back_tyre")>
                                    ${precheckDetail.precheckItemName}
                                    </#if>
                                    <#if (precheckDetail.ftlId == "file_in_car")>
                                    ${precheckDetail.precheckItemName}
                                    </#if>

                                </#list>
                            </span>
                                <#break>
                            </#if>
                        </#list>




                    </div>

            </div>

            <div class="condition-btn">
                <input type="hidden" id="precheck_id" value="${precheck.id}">
                <#if BPSHARE != 'true'>
                <a href="${BASE_PATH}/shop/order/speedily?customerCarId=${precheck.customerCarId}&refer=precheck-detail" class="yqx-btn nav-btn btn-org" id="addSpeedily"><i class="fa icon-plus btn-plus"></i> <span>快修快保单</span></a>
                </#if>
                <!-- 档口店不显示综合维修单 -->
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                <a href="${BASE_PATH}/shop/order/common-add?customerCarId=${precheck.customerCarId}&refer=precheck-detail&precheckId=${precheck.id}" class="yqx-btn nav-btn yqx-btn-3" id="addOrder"><i class="fa icon-plus btn-plus"></i> <span>综合维修单</span></a>
                </#if>
                <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 yqx-btn-small save-height js-print">打印</a>
                <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-return fr">返回</a>
                <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 yqx-btn-small js-precheck-delete fr">删除</a>
                <a href="${BASE_PATH}/shop/precheck/before-edit?id=${precheck.id}&refer=precheck-detail" class="yqx-btn yqx-btn-1 yqx-btn-small fr">编辑</a>

            </div>
        </div>
            <!--车况信息 end-->

    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/precheck/precheck-detail.js?8b1f116269bc285d162d1a0d625c30ec"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">