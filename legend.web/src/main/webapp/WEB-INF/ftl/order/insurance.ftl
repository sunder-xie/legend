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
            <h1 class="headline">平安补漆服务<strong><#if orderInfo>---<span class="color${orderInfo.auditStatus}">${orderInfo.auditStatusName}</span></#if></strong></h1>
            <div class="order_info"><#if orderInfo><#if order><label>工单编号：<strong><span style="color:#8836b2;cursor: pointer" onclick="window.location.href='${BASE_PATH}/shop/order/detail?orderId=${order.id}'">${order.orderSn}</span></strong></label></#if><label>服务时间：<strong>${orderInfo.gmtCreateStr}</strong></#if></label>
            </div>
        </div>
        <ul class="flow">
            <li content="1">创建平安补漆服务单</li>
            <li content="2">系统自动生成工单</li>
            <li content="3">平安补漆服务单审核</li>
            <li content="4">平安补漆单对账</li>
        </ul>
        <div class="content must_scope">
            <div class="group_row clearfix">
                <div class="col">
                    <span class="label label_required">车牌</span><input type="text" name="orderInfo.carLicense"
                                                                       v_type='{"required":true}'
                                                                       class="input w_220" label="车牌"
                                                                       service_url="/legend/shop/customer/search/mobile"
                                                                       callback_fn="callback.getCustomerId"
                                                                       show_key="license" search_key="com_license"
                                                                       autocomplete="off" data_cols='{"license":"车牌"}'
                                                                       remote="true"
                                                                       value="${orderInfo.carLicense}"
                                                                       placeholder="请输入车牌" maxlength="10"/>
                    <input type="hidden" name="orderInfo.customerCarId" value="${orderInfo.customerCarId}"/>
                    <input type="hidden" name="orderInfo.id" value="${orderInfo.id}"/>
                    <input type="hidden" name="orderInfo.billType" value="PINGAN.BUQI"/>
                    <input type="hidden" name="orderInfo.orderSn" value="${orderInfo.orderSn}"/>
                    <input type="hidden" name="orderInfo.serviceId" value="${orderInfo.serviceId}"/>
                    <input type="hidden" name="orderInfo.orderId" value="${orderInfo.orderId}"/>
                    <input type="hidden" name="orderInfo.shopActId" value="${shopActId}"/>
                    <input type="hidden" name="orderInfo.actTplId" value="${actTplId}"/>
                </div>
                <div class="col">
                    <span class="label label_required">车主</span><input type="text"
                                                                       name="orderInfo.customerName"
                                                                       v_type='{"required":true}'
                                                                       class="input w_156"
                                                                       label="车主姓名"
                                                                       autocomplete="off"
                                                                       value="${orderInfo.customerName}"
                                                                       placeholder="请输入车主姓名"/>
                </div>
                <div class="col">
                    <span class="label label_required">车主电话</span><input type="text"
                                                                         name="orderInfo.customerMobile"
                                                                         v_type='{"required":true,"type":"phone"}'
                                                                         class="input w_126"
                                                                         label="车主电话"
                                                                         autocomplete="off"
                                                                         value="${orderInfo.customerMobile}"
                                                                         placeholder="请输入车主电话"/>
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
                                                          placeholder="请选择车辆型号">
                    <script type="text/html">
                        <div class="qxy_input_downlist" style="width:auto;">
                            <ul class="list_content">
                                <%for(var i=0;i <templateData.length;i++){var item=templateData[i];%>
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
                                <%for(var i=0;i<templateData.length;i++){
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
                    <span class="label label_required">保险人</span><input class="input w_126"
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
                    <span class="label label_required">保单号</span><input class="input w_220"
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
                    <span class="label label_required">受损部位</span><input class="input w_156"
                                                          type="text"
                                                          name="orderInfo.woundPart"
                                                          v_type='{"required":true}'
                                                          label="受损部位"
                                                          autocomplete="off"
                                                          value="${orderInfo.woundPart}"
                                                          placeholder="请输入受损部位名称"/>
                </div>
                <div class="col">
                    <span class="label label_required">核销码</span><input class="input w_126 J_input_limit"
                                                                        data-limit_type="zh"
                                                                        type="text"
                                                                        name="orderInfo.verificationCode"
                                                                        v_type='{"required":true}'
                                                                        label="核销码"
                                                                        autocomplete="off"
                                                                        value="${orderInfo.verificationCode}"
                                                                        maxlength="25"
                                                                        placeholder="请输入手机号/兑换码"/>
                </div>
            </div>
            <div class="form_row address_link">
                <a href="http://chexian.pingan.com/sjdh/" target="_blank">平安核销地址 http://chexian.pingan.com/sjdh/</a>
            </div>
            <div class="group_row cleafix img_row">
                <span class="label">照片</span>
                <div class="img_box clearfix">
                    <!-- 车牌图片 -->
                    <div class="col">
                        <div class="img_item">
                            <div class="img_item_inner">
                            <#if orderInfo.imgUrl !=null && orderInfo.imgUrl !="">
                                <img src="${orderInfo.imgUrl}" width="120" height="120"/>
                            <#else>
                            <#-- 默认图片 -->
                                <img src="${BASE_PATH}/resources/style/img/page/order/img_u1.jpg" alt="" width="120" height="120">
                            </#if>
                            </div>
                            <div class="img_txt"><input type="file" class="file_btn" id="u1"/></div>
                            <input type="hidden" id="imgUrl" name="orderInfo.imgUrl" value="${orderInfo.imgUrl}"/>
                        </div>
                    </div>

                    <!-- 车辆受损图片 -->
                    <div class="col">
                        <div class="img_item">
                            <div class="img_item_inner">
                            <#if orderInfo.woundSnapshoot !=null && orderInfo.woundSnapshoot !="">
                                <img src="${orderInfo.woundSnapshoot}" width="120" height="120"/>
                            <#else>
                            <#-- 默认图片 -->
                                <img src="${BASE_PATH}/resources/style/img/page/order/img_u2.jpg" alt="" width="120" height="120">
                            </#if>
                            </div>
                            <div class="img_txt"><input type="file" class="file_btn" id="u2"/></div>
                            <input type="hidden" id="woundSnapshoot" name="orderInfo.woundSnapshoot"
                                   value="${orderInfo.woundSnapshoot}"/>
                        </div>
                    </div>

                    <!-- 修复后图片 -->
                    <div class="col">
                        <div class="img_item">
                            <div class="img_item_inner">
                            <#if orderInfo.acceptanceSnapshoot !=null && orderInfo.acceptanceSnapshoot !="">
                                <img src="${orderInfo.acceptanceSnapshoot}" width="120" height="120"/>
                            <#else>
                            <#-- 默认图片 -->
                                <img src="${BASE_PATH}/resources/style/img/page/order/img_u3.jpg" alt="" width="120" height="120">
                            </#if>
                            </div>
                            <div class="img_txt"><input type="file" class="file_btn" id="u3"/></div>
                            <input type="hidden" id="acceptanceSnapshoot" name="orderInfo.acceptanceSnapshoot"
                                   value="${orderInfo.acceptanceSnapshoot}"/>
                        </div>
                    </div>
                </div>
            </div>
            <div class="group_row clearfix">
                <span class="label">备注</span><textarea
                    class="mul_text" type="text" name="orderInfo.billNote" v_type='{"required":false}' maxlength="1000"
                    label="备注" autocomplete="off">${orderInfo.billNote}</textarea>

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
                    <button class="btn btn_danger btn_mw_106 submit" type="button">确认核销</button>

                </#if>
                <#if orderInfo.auditStatus == 3>
                    <button class="btn btn_danger btn_mw_106 resubmit" type="button">确认核销</button>
                </#if>
                <button class="btn btn_danger btn_mw_106" type="button" onclick="util.print('${BASE_PATH}/insurance/bill/print?billid=${orderInfo.id}')">打印</button>
            </#if>

                <button class="btn btn_default btn_mw_106 mr_10"
                        type="button" onclick="history.go(-1);">返回</button>
                <div class="note mt_10"></div>
            </div>

            <!-- 失败才显示失败原因-->
            <input id="auditStatus" type="hidden" data-result="${orderInfo.postscript}" value="${orderInfo.auditStatus}"/>
        </div>
    </div>
</div>

<!-- submit dialog -->
<script type="text/html" id="submitDialog">
    <div class="submit_dialog">
        <p class="txt1">您的服务单成功提交审核</p>
        <p class="txt2">系统自动生成工单，请前往工单模块查看详情，<a class="qxy_link" href="${BASE_PATH}/shop/order/detail?orderId=<%=orderid%>">立即前往</a></p>
    </div>
</script>

<#include "common_template/car_type_dialog.ftl" >
<!-- 车辆型号选择交互文件 start-->
<script type="text/javascript"
        src="${BASE_PATH}/resources/script/page/common_template/customer_create_dialog.js?a1d77dad2d7eaae9c6c2d7e4f233ef42"></script>
<!-- 车辆型号选择交互文件 end-->
<script type="text/javascript"
        src="${BASE_PATH}/resources/script/page/order/insurance.js?49b1bb0010cd70c53c8f89cd6bfd0aab"></script>
<#include "layout/ng-footer.ftl" >


