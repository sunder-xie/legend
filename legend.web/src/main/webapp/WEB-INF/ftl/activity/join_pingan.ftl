<#include "layout/ng-header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/resources/style/page/ng-common.css?a78008352fe2612dfd8e6b56d05fdf90"/>
<link rel="stylesheet"
      href="${BASE_PATH}/resources/style/page/common_tpl_style/addCustomer_tpl.css?f8d335e345e8e8bcbebc63ba9bca3fe8"/>
<link rel="stylesheet"
      href="${BASE_PATH}/resources/style/page/common_tpl_style/goods_type.css?57ad2cbaa229c6abdfb1ddb83e78ebfe">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/resources/css/drainage_activities/drainage_common.css?a6d55e6868e027a7ad7c9b6fc45786bc"/>
<div class="qxy_wrapper clearfix">
<#include "activity/activity_left.ftl" >
    <div class="main">
        <div class="head clearfix">
            <h1 class="headline">平安保养服务<strong><#if orderInfo>---<span class="color${orderInfo.auditStatus}">${orderInfo.auditStatusName}</span></#if></strong></h1>
            <div class="order_info"><#if orderInfo><#if order><label>工单编号：<strong><span style="color:#8836b2;cursor: pointer" onclick="window.location.href='${BASE_PATH}/shop/order/detail?orderId=${order.id}'">${order.orderSn}</span></strong></label></#if><label>服务时间：<strong>${orderInfo.gmtCreateStr}</strong></#if></label>
            </div>
        </div>
        <ul class="flow">
            <li content="1">创建平安保养服务单</li>
            <li content="2">系统自动生成工单</li>
            <li content="3">平安服务单审核</li>
            <li content="4">平安保养单对账</li>
        </ul>
        <div class="content must_scope">
            <div class="group_row clearfix">
                <div class="col">
                    <span class="label label_required">车牌</span><input class="input w_220" type="text"
                                                                       name="orderInfo.carLicense"
                                                                       v_type='{"required":true}'
                                                                       label="车牌"
                                                                       service_url="/legend/shop/customer/search/mobile"
                                                                       callback_fn="callback.getCustomerId"
                                                                       show_key="license"
                                                                       search_key="com_license"
                                                                       autocomplete="off"
                                                                       data_cols='{"license":"车牌"}'
                                                                       remote="true"
                                                                       value="${orderInfo.carLicense}"
                                                                       placeholder="请输入车牌"
                                                                       maxlength="10"/>
                    <input type="hidden" name="orderInfo.customerCarId" value="${orderInfo.customerCarId}"/>
                    <input type="hidden" name="orderInfo.id" value="${orderInfo.id}"/>

                    <input type="hidden" name="orderInfo.serviceId" value="${orderInfo.serviceId}"/>
                    <input type="hidden" name="orderInfo.billType" value="PINGAN.BAOYANG"/>
                    <input type="hidden" name="orderInfo.shopActId" value="${shopActId}"/>
                    <input type="hidden" name="orderInfo.actTplId" value="${actTplId}"/>
                    <input type="hidden" name="orderInfo.orderId" value="${orderInfo.orderId}"/>
                </div>
                <div class="col">
                    <span class="label label_required">车主</span><input class="input w_156"
                                                                       type="text"
                                                                       name="orderInfo.customerName"
                                                                       v_type='{"required":true}'
                                                                       label="车主姓名"
                                                                       autocomplete="off"
                                                                       value="${orderInfo.customerName}"
                                                                       placeholder="请输入车主姓名">
                </div>
                <div class="col">
                    <span class="label label_required">车主电话</span><input class="input w_126"
                                                                         type="text"
                                                                         name="orderInfo.customerMobile"
                                                                         v_type='{"required":true,"type":"phone"}'
                                                                         label="车主电话"
                                                                         autocomplete="off"
                                                                         value="${orderInfo.customerMobile}"
                                                                         placeholder="请输入车主电话">
                </div>
            </div>
            <div class="group_row clearfix">
                <div class="col">
                    <span class="label">车辆型号</span><input class="input w_220"
                                                          type="text"
                                                          name="carModeBak"
                                                          v_type='{"required":false}'
                                                          label="车辆型号"
                                                          service_url="/legend/shop/car_category/car_model"
                                                          callback_fn="carModeFn"
                                                          search_key="q"
                                                          autocomplete="off"
                                                          remote="true"
                                                          value="${orderInfo.carInfo}"
                                                          id="carMode"
                                                          no_submit="true"
                                                          placeholder="请选择车辆型号"/>
                    <script type="text/html">
                        <div class="qxy_input_downlist" style="width:auto;">
                            <ul class="list_content">
                                <%for(var i=0;i<templateData.length;i++){var item=templateData[i];%>
                                    <li>
                                        <%if(item.importInfo!=null&&item.importInfo!=""){%>
                                            <span style="width:260px;"
                                                  title="<%= item.brand%>&nbsp;&nbsp;(<%= item.importInfo%>)<%= item.model%>">
                                                <%= item.brand%>&nbsp;&nbsp;
                                                (<%= item.importInfo%>)
                                                <%= item.model%>
                                            </span>
                                        <%}else{%>
                                            <span style="width:260px;"
                                                  title="<%= item.brand%>&nbsp;&nbsp;<%= item.model%>">
                                                <%= item.brand%>&nbsp;&nbsp;
                                                <%= item.model%>
                                            </span>
                                        <%}%>
                                    </li>
                                    <%}%>
                            </ul>
                        </div>
                    </script>
                    <div class="input_btn_box">
                        <button class="btn btn_primary input_btn carType_btn" type="button">选择车型</button>
                    </div>
                    <!-- hidden [[  -->
                    <!-- 品牌 -->
                    <input type="hidden" name="orderInfo.carBrandId" value="${orderInfo.carBrandId}"/>
                    <input type="hidden" name="orderInfo.carBrand" value="${orderInfo.carBrand}"/>
                    <!-- 车系 -->
                    <input type="hidden" name="orderInfo.carSeriesId" value="${orderInfo.carSeriesId}"/>
                    <input type="hidden" name="orderInfo.carSeries" value="${orderInfo.carSeries}"/>
                    <!-- 车辆型号 -->
                    <input type="hidden" name="orderInfo.carModelsId" value="${orderInfo.carModelsId}"/>
                    <input type="hidden" name="orderInfo.carModels" value="${orderInfo.carModels}"/>
                    <!-- 进口与国产 -->
                    <input type="hidden" name="orderInfo.importInfo" value="${orderInfo.importInfo}"/>
                    <!-- ]] hidden -->
                </div>
                <div class="col form_item">
                    <input type="hidden" name="orderInfo.carModelsId" value="${orderInfo.carModelsId}"/>
                    <!-- 年款 -->
                    <input type="hidden" name="orderInfo.carYear" value="${orderInfo.carYear}"/>
                    <input type="hidden" name="orderInfo.carYearId" value="${orderInfo.carYearId}"/>
                    <!-- 排量 -->
                    <input type="hidden" name="orderInfo.carPower" value="${orderInfo.carPower}"/>
                    <input type="hidden" name="orderInfo.carPowerId" value="${orderInfo.carPowerId}"/>
                    <!-- 变速箱 -->
                    <input type="hidden" name="orderInfo.carGearBox" value="${orderInfo.carGearBox}"/>
                    <input type="hidden" name="orderInfo.carGearBoxId"
                           value="${orderInfo.carGearBoxId}"/>
                    <span class="label">年款排量</span><input class="input w_156"
                                                          disabled="disabled"
                                                          name="yearPowerBak"
                                                          service_url="/legend/shop/car_category/car_model"
                                                          search_key="q,model_id"
                                                          callback_fn="yearPowerFn"
                                                          ext_data="yearPowerBak,orderInfo.carModelsId"
                                                          type="text"
                                                          value="${orderInfo.carYear} <#if orderInfo.carGearBox>${orderInfo.carGearBox}<#else>${orderInfo.carPower}</#if>"/>
                    <script type="text/html">
                        <div class="qxy_input_downlist" style="width:auto;">
                            <ul class="list_content">
                                <%for(var i=0;i<templateData.length ;i++){
                                        var item=templateData[i];%>
                                    <li>
                                        <span style="width:260px;"
                                              title="<%= item.year%>&nbsp;&nbsp;<%= item.gearbox%>">
                                                <%= item.year%>&nbsp;&nbsp;
                                                <%= item.gearbox%>
                                        </span>
                                    </li>
                                <%}%>
                            </ul>
                        </div>
                    </script>
                </div>
                <div class="col">
                    <span class="label">行驶里程</span><input class="input w_126"
                                                          type="text"
                                                          name="orderInfo.mileage"
                                                          v_type='{"required":false}'
                                                          label="行驶里程"
                                                          autocomplete="off"
                                                          value="${orderInfo.mileage}"
                                                          placeholder="请输入里程数"/><input class="input input_unit"
                                                                                        value="km" disabled/>
                </div>
            </div>
            <div class="group_row clearfix">
                <div class="col">
                    <span class="label">客户单位</span><input class="input w_220"
                                                          type="text"
                                                          name="orderInfo.company"
                                                          v_type='{"required":false}'
                                                          label="客户单位"
                                                          autocomplete="off"
                                                          value="${orderInfo.company}"
                                                          maxlength="17"
                                                          placeholder="请输入客户单位"/>
                </div>
                <div class="col">
                    <span class="label">VIN码</span><input class="input w_156"
                                                          type="text"
                                                          name="orderInfo.vin"
                                                          v_type='{"required":false}'
                                                          label="VIN码"
                                                          autocomplete="off"
                                                          value="${orderInfo.vin}"
                                                          maxlength="17"
                                                          placeholder="请输入VIN码"/>
                </div>
                <div class="col">
                    <span class="label label_required">保单人</span><input class="input w_126"
                                                                        type="text"
                                                                        name="orderInfo.insured"
                                                                        v_type='{"required":true}'
                                                                        label="保险人"
                                                                        autocomplete="off"
                                                                        value="${orderInfo.insured}"
                                                                        placeholder="请输入保险人姓名"/>
                </div>
            </div>
            <div class="group_row clearfix">
                <div class="col">
                <#if orderInfo.auditStatus ==0 || orderInfo.auditStatus =="">
                    <span class="label label_required">保养套餐级别</span><select
                        class="chosen w_220" v_type='{"required":true}' id="BYChosen"
                        label="保养套餐级别" data-placeholder="请选择保养套餐级别">
                    <option value=""></option>
                    <#list servicelist as s>
                    <#if orderInfo.serviceId  == s.id>
                        <option value="${s.id}" selected>${s.name}</option>
                    <#else>
                        <option value="${s.id}">${s.name}</option>
                    </#if>
                    </#list>
                </select>
                <#else>
                    <span class="label label_required">保养套餐级别</span><select class="chosen w_220" v_type='{"required":true}'
                                                                            id="BYChosen" data-placeholder="请选择保养套餐级别" disabled="disabled">
                    <option value="${orderInfo.serviceId}">${orderInfo.serviceName} </option>
                </select>

                </select>
                </#if>

                </div>
                <div class="col">
                    <span class="label label_required">保险号</span><input class="input w_156"
                                                                        type="text"
                                                                        name="orderInfo.insuredCode"
                                                                        v_type='{"required":true}'
                                                                        label="保单号"
                                                                        autocomplete="off"
                                                                        value="${orderInfo.insuredCode}"
                                                                        maxlength="25"
                                                                        placeholder="请输入保单号"/>
                </div>
                <div class="col">
                    <span class="label label_required">核销码</span><input class="input w_126"
                                                                        type="text"
                                                                        name="orderInfo.verificationCode"
                                                                        v_type='{"required":true}'
                                                                        data-limit_type="zh"
                                                                        label="核销码"
                                                                        autocomplete="off"
                                                                        value="${orderInfo.verificationCode}"
                                                                        maxlength="25"
                                                                        placeholder="请输入核销码"/>
                </div>
            </div>
            <div class="form_row address_link">
                <a href="http://chexian.pingan.com/sjdh/" target="_blank">平安核销地址 http://chexian.pingan.com/sjdh/</a>
            </div>
            <div class="group_row">
                <span class="label">服务项目</span>
                <div class="container">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>服务名称</th>
                            <th>服务类别</th>
                            <th>工时费</th>
                            <th>工时</th>
                            <th>金额</th>
                        </tr>
                        </thead>
                        <tbody id="tbody">
                        <#if service?exists>
                        <tr>
                            <td>${service.name}</td>
                            <td>${service.categoryName}</td>
                            <td>${service.servicePrice}</td>
                            <td>1</td>
                            <td class="highlight">${service.servicePrice}</td>
                        </tr>
                        </#if>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="group_row">
                <span class="label">服务备注</span><textarea
                    class="mul_text" type="text" name="orderInfo.billNote" v_type='{"required":false}' maxlength="1000"
                    label="服务备注" autocomplete="off">${orderInfo.billNote}</textarea>
            </div>


            <div class="group_row">
                <span class="label">照片</span>

                <div class="group_photo clearfix">
                    <div class="photo_box">
                        <!-- 车牌图片 -->

                        <div class="img_item">
                            <div class="img_item_inner">
                            <#if orderInfo.imgUrl !=null && orderInfo.imgUrl !="">
                                <img src="${orderInfo.imgUrl}" width="120" height="120"/>
                            <#else>
                            <#-- 默认图片 -->
                                <img src="${BASE_PATH}/resources/style/img/page/order/img_u1.jpg" alt="" width="120" height="120">
                            </#if>
                            </div>
                            <div class="img_txt" id="u1"></div>
                            <input type="hidden" id="imgUrl" name="orderInfo.imgUrl" value="${orderInfo.imgUrl}"  label="图片"/>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 工单申请优惠 兼容普通工单-->
            <input type="hidden" name="orderInfo.preDiscountRate" value="1"/>
            <input type="hidden" name="orderInfo.preTaxAmount" value="0"/>
            <input type="hidden" name="orderInfo.prePreferentiaAmount" value="0"/>
            <input type="hidden" name="orderInfo.preCouponAmount" value="0"/>
            <input type="hidden" name="orderInfo.preTotalAmount"/>

            <div class="bottom">
            <#if orderInfo.id gt 0>
                <#if orderInfo.auditStatus == 0>
                    <button class="btn btn_danger btn_mw_106 update" type="button">编辑保存</button>
                </#if>
            <#else>
                <button class="btn btn_danger btn_mw_106 save" type="button">保存</button>
            </#if>
            <#if orderInfo.id gt 0>
                <#if orderInfo.auditStatus == 0>
                    <button class="btn btn_danger btn_mw_106 submit" type="button">提交审核</button>

                </#if>
                <#if orderInfo.auditStatus == 3>
                    <button class="btn btn_danger btn_mw_106 resubmit" type="button">提交审核</button>
                </#if>
                <button class="btn btn_danger btn_mw_106" type="button" onclick="util.print('${BASE_PATH}/shop/activity/join/pingan/print?billid=${orderInfo.id}')">打印</button>
            </#if>

                <button class="btn btn_default btn_mw_106 mr_10"
                        type="button" onclick="history.go(-1);">返回</button>
                <div class="note mt_10"></div>
            </div>

            <!-- 失败才显示失败原因-->
            <input id="auditStatus" type="hidden" data-result="${orderInfo.postscript}" value="${orderInfo.auditStatus}"/>
        <#--<#if orderInfo.auditStatus == 3>-->
            <#--<div class="qxy_row red content_center">-->
                <#--失败原因：${orderInfo.postscript}-->
            <#--</div>-->
        <#--</#if>-->
        </div>
    </div>
</div>


<!-- save dialog -->
<script type="text/html" id="saveDialog">
    <div class="submit_dialog">
        <p class="txt1">您的服务单成功保存</p>
        <p class="txt2">系统自动生成已完工工单，请前往工单模块查看详情<a class="qxy_link"
                                                href="${BASE_PATH}/shop/order/detail?orderId=<%=orderid%>">立即前往</a></p>
    </div>
</script>

<script type="text/html" id="tbodyTpl">
    <tr>
        <td><%= item.name %></td>
        <td><%= item.categoryName %></td>
        <td><%= item.servicePrice %></td>
        <td>1</td>
        <td class="highlight"><%= item.servicePrice %></td>
    </tr>
</script>
<!-- submit dialog -->
<script type="text/html" id="submitDialog">
    <div class="submit_dialog">
        <p class="txt1">你的服务单成功结算，请等待云修审核</p>
        <p class="txt2">工单状态为已结算，请前往工单模块查看详情,<a class="qxy_link"
                                                href="${BASE_PATH}/shop/order/detail?orderId=<%=orderid%>">立即前往</a></p>
    </div>
</script>

<#include "common_template/car_type_dialog.ftl" >
<!-- 车辆型号选择交互文件 start-->
<script type="text/javascript"
        src="${BASE_PATH}/resources/script/page/common_template/customer_create_dialog.js?a1d77dad2d7eaae9c6c2d7e4f233ef42"></script>
<!-- 车辆型号选择交互文件 end-->
<script type="text/javascript"
        src="${BASE_PATH}/resources/script/page/order/insurance_pingan.js?035a5e36b99b2dcc306bdd756ec1a120"></script>
<#include "layout/ng-footer.ftl" >
