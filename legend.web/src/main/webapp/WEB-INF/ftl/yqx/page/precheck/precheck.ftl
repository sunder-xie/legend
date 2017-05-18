<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/precheck/precheck.css?8b689f4aaec30009f013343636568271"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <#if (act == "edit")>
                <h1 class="headline fl"> 编辑预检单</h1>
                <input type="hidden" id="act" value="update"/>
            <#else>
                <h1 class="headline fl"> 新建预检单</h1>
                <input type="hidden" id="act" value="save"/>
            </#if>
        </div>


        <!--预检单信息 start-->
        <div class="pre-num">
        <#if precheckHead.id >预检单编号：${precheckHead.precheckSn} - 预检时间：${precheckHead.gmtCreateStr}</#if>
            <input type="hidden" name="id" id="id" value="${precheckHead.id}"/>
            <input type="hidden" name="precheckSn" id="precheckSn" value="${precheckHead.precheckSn}"/>

        </div>
        <div class="order-form" id="precheckHead">
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        车牌
                    </div><div class="form-item yqx-downlist-wrap form-data-width">
                        <input type="text" name="plateNumber" class="yqx-input" <#if (act == "edit")>disabled</#if><#if customerCar> value="${customerCar.license}" <#else >value="${license}" </#if> placeholder="请输入车牌" data-v-type="licence | required" data-label="车牌">
                        <input type="hidden" name="customerCarId" value="">
                        <input type="hidden" name="id" id="id" value="${precheckHead.id}"/>
                        <input type="hidden" name="precheckSn" id="precheckSn" value="${precheckHead.precheckSn}"/>
                        <input type="hidden" name="customerCarId" id="customerCarId" value="${precheckHead.customerCarId}"/>
                    </div>
                </div>

                <div class="col-3">
                    <div class="form-label">
                        联系人
                    </div><div class="form-item">
                        <input type="text" name="customerName" class="yqx-input" value="${customerCar.customerName}" placeholder="请输入联系人">
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        联系电话
                    </div><div class="form-item">
                        <input type="text" name="mobile" class="yqx-input" value="${customerCar.mobile}" placeholder="请输入联系电话" data-v-type="phone" data-label="">
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label">
                        车型
                    </div><div class="form-item form-data-width">
                        <input type="text" name="carModel" class="yqx-input" value="${customerCar.carInfo}" disabled>
                        <input type="hidden" name="carModelId" value="${customerCar.carModelId}">
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        行驶里程
                    </div><div class="form-item">
                        <input type="text" name="mileage" class="yqx-input js-number" value="${customerCar.mileage}" data-v-type="integer" maxlength="8">
                        <span class="fa">km</span>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        下次保养里程
                    </div><div class="form-item">
                        <input type="text" name="upkeepMileage" class="yqx-input js-number yqx-input-icon" value="${customerCar.upkeepMileage}" data-v-type="integer">
                        <span class="fa">km</span>
                    </div>
                </div>

            </div>
            <div class="show-grid">

                <div class="col-3">
                    <div class="form-label">
                        保险到期
                    </div><div class="form-item">
                        <input type="text" name="dueDate" class="yqx-input yqx-input-icon js-due-date" value="${precheckHead.dueDateStr}" placeholder="请选择时间">
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label">
                        下次保养时间
                    </div><div class="form-item">
                        <input type="text" name="nextTime" class="yqx-input yqx-input-icon js-date" value="${precheckHead.nextTime}" placeholder="请选择时间">
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label">
                        备注
                    </div><div class="form-item">
                    <input type="text" name="comments" class="yqx-input form-data-width" value="${precheckHead.comments}" placeholder="请输入备注" maxlength="50">
                    </div>
                </div>
            </div>
        </div>
        <!--预检单信息 end-->

        <!--车况登记 start-->
        <div class="register-title">车况登记</div>
        <div class="register">
            <div id="appearance">
            <!-- 外观检测group start -->
            <div class="yqx-group">
                <!--group标题 start -->
                <div class="yqx-group-head js-head-show">
                    外观检测
                    <i class="group-head-line"></i>
                    <i class="group-arrow arrow-up js-arrow-up"></i>
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content group-pre wg-bk">

                <#include "yqx/tpl/order/precheck-tpl.ftl">

                </div>
                <!-- group内容 end -->
                </div>
            </div>
            <!-- 外观检测group end -->

            <!-- 客户需求group start -->
            <div class="yqx-group">
                <div id="customerRequest">
                <!--group标题 start -->
                <div class="yqx-group-head js-head-show">
                    客户需求
                    <i class="group-head-line"></i>
                    <i class="group-arrow arrow-down js-arrow-down"></i>
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content group-initialization group-pre">
                    <div class="request-detail">
                        <table class="detail-box">
                            <thead>
                            <tr>
                                <th>内容</th>
                                <th>要求（预计所需配料)</th>
                                <th><button class="yqx-btn yqx-btn-3 yqx-btn-small addlist-btn js-addbtn">添加</button></th>
                            </tr>
                            </thead>
                            <tbody id="customerCon">
                                <#if (requestList)?? >
                                    <#list requestList as customerRequest>
                                    <tr class="js-customer-request-tr">
                                        <td>
                                            <div class="form-item">
                                                <input type="text" name="content" class="yqx-input yqx-input-small js-content" value="${customerRequest.content}" placeholder="" maxlength="50">
                                            </div>
                                        </td>
                                        <td>
                                            <div class="form-item">
                                                <input type="text" name="contentGoods" class="yqx-input yqx-input-small js-contentGoods" value="${customerRequest.contentGoods}" placeholder="" maxlength="100">
                                            </div>
                                        </td>
                                        <td><a href="javascript:;" class="dellist-btn js-delbtn">删除</a></td>
                                    </tr>
                                    </#list>
                                <#else >
                                <tr class="js-customer-request-tr">
                                    <td>
                                        <div class="form-item">
                                            <input type="text" name="content" class="yqx-input yqx-input-small js-content" value="" placeholder="" maxlength="50">
                                        </div>
                                    </td>
                                    <td>
                                        <div class="form-item">
                                            <input type="text" name="contentGoods" class="yqx-input yqx-input-small js-contentGoods" value="" placeholder="" maxlength="100">
                                        </div>
                                    </td>
                                    <td><a href="javascript:;" class="dellist-btn js-delbtn">删除</a></td>
                                </tr>
                                </#if>
                            </tbody>
                        </table>
                    </div>
                    <div id="precheckOther">
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            预计工时
                        </div><div class="form-item interior-width">
                            <input type="text" name="manHour" class="yqx-input js-float-1" value="${precheckHead.manHour}" placeholder="" data-v-type="number" data-label="" maxlength="10">
                        </div>
                    </div>
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            预计总费用
                        </div><div class="form-item interior-width">
                            <input type="text" name="expFee" class="yqx-input js-float-2" value="${precheckHead.expFee}" placeholder="" data-v-type="price" data-label="" maxlength="9">
                        </div>
                    </div>
                    </div>
                </div>
                </div>
                <!-- group内容 end -->
            </div>
            <!-- 客户需求group end -->
        <div id="precheckOtherDetail">
            <!-- 车内设施group start -->
            <div class="yqx-group">
                <!--group标题 start -->
                <div class="yqx-group-head js-head-show">
                    车内设施
                    <i class="group-head-line"></i>
                    <i class="group-arrow arrow-down js-arrow-down"></i>
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content group-initialization group-pre">
                    <!--内饰-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            内饰
                        </div><div class="form-item interior-width">
                            <input type="text" name="interior" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="interior")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>

                    <!--显示屏-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            显示屏
                        </div><div class="form-item interior-width">
                            <input type="text" name="screen" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="screen")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>
                    <!--音响-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            音响
                        </div><div class="form-item interior-width">
                            <input type="text" name="audio" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="audio")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>

                    <!--点烟器-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            点烟器
                        </div><div class="form-item interior-width">
                            <input type="text" name="cigar_lighter" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="cigar_lighter")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>
                    <!--升降器-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            升降器
                        </div><div class="form-item interior-width">
                            <input type="text" name="glass_lifter" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="glass_lifter")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>

                    <!--自动门锁-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            自动门锁
                        </div><div class="form-item interior-width">
                            <input type="text" name="auto_door" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="auto_door")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>
                </div>
                <!-- group内容 end -->
            </div>
            <!-- 车内设施group end -->

            <!-- 仪表group start -->
            <div class="yqx-group">
                <!--group标题 start -->
                <div class="yqx-group-head js-head-show">
                    仪 表
                    <i class="group-head-line"></i>
                    <i class="group-arrow arrow-down js-arrow-down"></i>
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content group-initialization group-pre">

                    <!--故障灯-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            故障灯
                        </div><div class="form-item interior-width">
                            <input type="text" name="trouble_light" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="trouble_light")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>
                    <!--仪表-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            仪表
                        </div><div class="form-item interior-width">
                            <input type="text" name="dashboard" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="dashboard")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>
                    <!--油表油量-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            油表油量
                        </div><div class="form-item item-select interior-width">
                            <select class="xxsel" id="oil_meter" name="oil_meter">
                                <option value="0">油表油量</option>
                            <#list oilMeterValues as oilValue>
                                <option value="${oilValue.id}"
                                    <#list precheckDetailsList as precheckDetail>
                                        <#if ((oilValue.id == precheckDetail.precheckValueId)&&(precheckDetail.ftlId == "oil_meter"))>
                                        selected
                                        </#if>
                                    </#list>
                                        >
                                ${oilValue.value}
                                </option>
                            </#list>
                            </select>
                        </div>
                    </div>
                </div>
                <!-- group内容 end -->
            </div>
            <!-- 仪表group end -->

            <!-- 发 动 机group start -->
            <div class="yqx-group">
                <!--group标题 start -->
                <div class="yqx-group-head js-head-show">
                    发 动 机
                    <i class="group-head-line"></i>
                    <i class="group-arrow arrow-down js-arrow-down"></i>
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content group-initialization group-pre">
                    <!--异响-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            异响
                        </div><div class="form-item interior-width">
                            <input type="text" name="noise" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="noise")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>
                    <!--使用异常-->
                    <div class="appearance-sel">
                        <div class="form-label apper-label-width">
                            使用异常
                        </div><div class="form-item interior-width">
                            <input type="text" name="func_error" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="func_error")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>
                </div>
                <!-- group内容 end -->
            </div>
            <!-- 发 动 机group end -->

            <!-- 轮      胎group start -->
            <div class="yqx-group">
                <!--group标题 start -->
                <div class="yqx-group-head js-head-show">
                    轮      胎
                    <i class="group-head-line"></i>
                    <i class="group-arrow arrow-down js-arrow-down"></i>
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content group-initialization group-pre">
                    <div class="appearance-sel ring-box">
                        <div class="form-label mileage-label-width">
                            钢圈
                        </div><div class="form-item ring-select">
                            <select id="left_front_hub" name="left_front_hub"
                                    class="xxsel steel_ring">
                                <option value="0">左前</option>
                            <#list outlineValues as outline>
                                <option value="${outline.id}"
                                    <#list precheckDetailsList as precheckDetail>
                                        <#if ((outline.id == precheckDetail.precheckValueId)&&(precheckDetail.ftlId == "left_front_hub"))>
                                        selected
                                        </#if>
                                    </#list>
                                        >
                                ${"左前${outline.value}"}
                                </option>
                            </#list>
                            </select>
                        </div><div class="form-item ring-select">
                            <select id="right_front_hub" name="right_front_hub"
                                    class="xxsel steel_ring">
                                <option value="0">右前</option>
                            <#list outlineValues as outline>
                                <option value="${outline.id}"
                                    <#list precheckDetailsList as precheckDetail>
                                        <#if ((outline.id == precheckDetail.precheckValueId)&&(precheckDetail.ftlId == "right_front_hub"))>
                                        selected
                                        </#if>
                                    </#list>
                                        >
                                ${"右前${outline.value}"}
                                </option>
                            </#list>
                            </select>
                        </div><div class="form-item ring-select">
                            <select id="left_rear_hub" name="left_rear_hub"
                                    class="xxsel steel_ring">
                                <option value="0">左后</option>
                            <#list outlineValues as outline>
                                <option value="${outline.id}"
                                    <#list precheckDetailsList as precheckDetail>
                                        <#if ((outline.id == precheckDetail.precheckValueId)&&(precheckDetail.ftlId == "left_rear_hub"))>
                                        selected
                                        </#if>
                                    </#list>
                                        >
                                ${"左后${outline.value}"}
                                </option>
                            </#list>
                            </select>
                        </div><div class="form-item ring-select">
                            <select id="right_rear_hub" name="right_rear_hub"
                                    class="xxsel steel_ring">
                                <option value="0">右后</option>
                            <#list outlineValues as outline>
                                <option value="${outline.id}"
                                    <#list precheckDetailsList as precheckDetail>
                                        <#if ((outline.id == precheckDetail.precheckValueId)&&(precheckDetail.ftlId == "right_rear_hub"))>
                                        selected
                                        </#if>
                                    </#list>
                                        >
                                ${"右后${outline.value}"}
                                </option>
                            </#list>
                            </select>
                        </div>
                    </div>
                    <!--建议更换里程-->
                    <div class="appearance-sel">
                        <div class="form-label mileage-label-width">
                            建议更换里程
                        </div><div class="form-item mileage-width">
                            <input type="text" name="tyre_change" class="yqx-input js-number"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="tyre_change")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" data-v-type="number" data-label="建议更换里程" maxlength="8">
                        </div>
                        公里
                    </div>
                    <!--异常记录-->
                    <div class="appearance-sel ">
                        <div class="form-label mileage-label-width">
                            异常记录
                        </div><div class="form-item mileage-width" >
                            <input type="text" name="tyre_error" class="yqx-input"
                            <#list precheckDetailsList as precheckDetail>
                                   <#if (precheckDetail.ftlId=="tyre_error")>value="${precheckDetail.suggestion}"</#if>
                            </#list> placeholder="" maxlength="50">
                        </div>
                    </div>
                </div>
                <!-- group内容 end -->
            </div>
            <!-- 轮      胎group end -->


            <!-- 其它group start -->
            <div class="yqx-group">
                <!--group标题 start -->
                <div class="yqx-group-head js-head-show">
                    其它
                    <i class="group-head-line"></i>
                    <i class="group-arrow arrow-down js-arrow-down"></i>
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content group-initialization group-pre">
                    <div class="tire-left fl">
                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                三滤
                            </div><div class="form-item tire-width">
                                <input type="text" name="three_filters" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="three_filters")>value="${precheckDetail.suggestion}"</#if>
                                </#list>  placeholder="建议更换里程" data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>

                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                机油
                            </div><div class="form-item tire-width">
                                <input type="text" name="engine_oil" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="engine_oil")>value="${precheckDetail.suggestion}"</#if>
                                </#list> placeholder="建议更换里程"  data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>
                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                火花塞
                            </div><div class="form-item tire-width">
                                <input type="text" name="spark_plug" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="spark_plug")>value="${precheckDetail.suggestion}"</#if>
                                </#list> placeholder="建议更换里程"  data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>

                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                刹车油
                            </div><div class="form-item tire-width">
                                <input type="text" name="brake_oil" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="brake_oil")>value="${precheckDetail.suggestion}"</#if>
                                </#list> placeholder="建议更换里程"  data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>
                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                雨刮
                            </div><div class="form-item tire-width">
                                <input type="text" name="rain_wiper" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="rain_wiper")> value="${precheckDetail.suggestion}"</#if>
                                </#list> placeholder="建议更换里程"  data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>

                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                变速箱油
                            </div><div class="form-item tire-width">
                                <input type="text" name="gear_oil" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="gear_oil")>value="${precheckDetail.suggestion}"</#if>
                                </#list> placeholder="建议更换里程"  data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>
                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                刹车
                            </div><div class="form-item tire-width">
                                <input type="text" name="brake_pad" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="brake_pad")>value="${precheckDetail.suggestion}"</#if>
                                </#list> placeholder="建议更换里程"  data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>
                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                防冻液
                            </div><div class="form-item tire-width">
                                <input type="text" name="antifreeze" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="antifreeze")>value="${precheckDetail.suggestion}"</#if>
                                </#list> placeholder="建议更换里程"  data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>
                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                电瓶
                            </div><div class="form-item tire-width">
                                <input type="text" name="battery" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="battery")>value="${precheckDetail.suggestion}"</#if>
                                </#list> placeholder="建议更换里程"  data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>
                        <div class="appearance-sel">
                            <div class="form-label apper-label-width">
                                助力液
                            </div><div class="form-item tire-width">
                                <input type="text" name="steering_fluid" class="yqx-input js-number"
                                <#list precheckDetailsList as precheckDetail>
                                       <#if (precheckDetail.ftlId=="steering_fluid")>value="${precheckDetail.suggestion}"</#if>
                                </#list> placeholder="建议更换里程"  data-v-type="number" data-label="" maxlength="8">
                            </div>
                            公里
                        </div>
                    </div>
                    <div class="tire-right fr">
                        <div class="form-item">
                            <textarea class="yqx-textarea other-area" name="other_suggestion"   placeholder="其它建议" maxlength="200">
                            <#list precheckDetailsList as precheckDetail><#if (precheckDetail.ftlId=="other_suggestion")>${precheckDetail.suggestion}</#if></#list></textarea>

                            </textarea>
                        </div>
                    </div>
                </div>
                <!-- group内容 end -->
            </div>
            <!-- 其 它 group end -->
            </div>
            <div class="personal" id="goodsInCar">
                随车物品
                <span><input type="checkbox" name="key" value="true" <#list precheckDetailsList as precheckDetail>
                    <#if precheckDetail.ftlId == "key">
                             checked
                    </#if>
                </#list>>车钥匙 </span>
                <span><input type="checkbox" name="extinguisher" value="true"
                <#list precheckDetailsList as precheckDetail>
                    <#if precheckDetail.ftlId == "extinguisher">
                             checked
                    </#if>
                </#list>>灭火器 </span>
                <span><input type="checkbox" name="jack" value="true"
                <#list precheckDetailsList as precheckDetail>
                    <#if precheckDetail.ftlId == "jack">
                             checked
                    </#if>
                </#list>>千斤顶 </span>
                <span><input type="checkbox" name="drive_license" value="true"
                <#list precheckDetailsList as precheckDetail>
                    <#if precheckDetail.ftlId == "drive_license">
                             checked
                    </#if>
                </#list>>驾驶证 </span>
                <span><input type="checkbox" name="car_license" value="true"
                <#list precheckDetailsList as precheckDetail>
                    <#if precheckDetail.ftlId == "car_license">
                             checked
                    </#if>
                </#list>>行驶证 </span>
                <span><input type="checkbox" name="tool_box" value="true"
                <#list precheckDetailsList as precheckDetail>
                    <#if precheckDetail.ftlId == "tool_box">
                             checked
                    </#if>
                </#list>>随车工具箱 </span>
                <span><input type="checkbox" name="back_tyre" value="true"
                <#list precheckDetailsList as precheckDetail>
                    <#if precheckDetail.ftlId == "back_tyre">
                        checked
                    </#if>
                </#list>>备胎 </span>
                <span><input type="checkbox" name="file_in_car" value="true"
                <#list precheckDetailsList as precheckDetail>
                    <#if precheckDetail.ftlId == "file_in_car">
                             checked
                    </#if>
                </#list>>随车资料 </span>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-2 save-height js-save-btn">保存</button>
                <#if BPSHARE != 'true'>
                <a href="javascript:void(0)" class="yqx-btn nav-btn btn-org js-add-addSpeedily" id="addSpeedily"><i class="fa icon-plus btn-plus"></i> <span>快修快保单</span></a>
                </#if>
                <!-- 档口店不显示综合维修单 -->
                <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                <a href="javascript:void(0)" class="yqx-btn nav-btn yqx-btn-3 js-add-addOrder" id="addOrder"><i class="fa icon-plus btn-plus"></i> <span>综合维修单</span></a>
                </#if>
                <a href="javascript:void(0)" class="yqx-btn yqx-btn-1 fr js-back" > <span>返回</span></a>
                </div>
        </div>
        <!--车况登记 end-->
    </div>
    <!-- 右侧内容区 end -->
</div>
<!--客户需求模板  start-->
<script type="text/html" id="customerTpl">
    <tr>
        <td>
            <div class="form-item">
                <input type="text" name="" class="yqx-input yqx-input-small js-content" maxlength="50">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="" class="yqx-input yqx-input-small js-contentGoods" maxlength="100">
            </div>
        </td>
        <td><a href="javascript:;" class="dellist-btn js-delbtn">删除</a></td>
    </tr>
</script>
<!--客户需求模板  end-->

<!-- 车牌模版 -->
<#include "yqx/tpl/common/car-licence-tpl.ftl">

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script  src="${BASE_PATH}/static/js/page/precheck/precheck.js?24d66ba40a9da4dc8694d173cc04f622"></script>

<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">