<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/proxy/translateproxy.css?c0a69d98eb043c65d988a1316262aca0"/>
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
            <#if proxyDtlResult??>
                <h1 class="headline fl"> 更新委托单</h1>
                <#else >
                    <h1 class="headline fl"> 工单委托</h1>
            </#if>
        </div>

        <div class="wt-top-box">

            <input type="hidden" class="shopId" value="${shop.id}">
            <input type="hidden" class="orderId" value="${result.data.orderId}">
            <input type="hidden" class="receiverName" value="${result.data.receiverName}">
            <input type="hidden" class="customerId" value="${result.data.customerId}">
            <input type="hidden" class="customerCarId" value="${result.data.customerCarId}">
            <input type="hidden" class="shopName" value="${shop.name}">
            <input type="hidden" class="contactName" value="${shop.contact}">
            <input type="hidden" class="contactMobile" value="${shop.mobile}">
            <input type="hidden" class="proxyAddress" value="${shop.address}">
            <input type="hidden" class="serviceSaId" value="${result.data.receiver}">


            <div class="wt-select">
                <div class="form-label">
                    受托方:
                </div>
                <#if proxyDtlResult??>
                    <div class="form-item">
                        <input type="text" name="proxyShopName" class="yqx-input yqx-input-icon yqx-input-small proxyShopName" value="${proxyDtlResult.data.proxyShopName}" placeholder="" disabled="disabled">
                    </div>
                    <input class="proxyShopId" type="hidden" value="${proxyDtlResult.data.proxyShopId}" name="proxyShopId">
                    <input class="proxyId" type="hidden" value="${proxyDtlResult.data.id}">
                <#else >
                    <div class="form-item">
                        <input type="text" name="proxyShopName" class="yqx-input yqx-input-icon yqx-input-small js-trustee proxyShopName" value="" placeholder="请选择">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <input class="proxyShopId" type="hidden" value="" name="proxyShopId">
                </#if>
            </div>
        <#if proxyDtlResult??>
            <div class="wt-infor">
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label detail-title">车牌：</div>
                        <div class="form-item">
                            <div class="yqx-text carLicense">${proxyDtlResult.data.carLicense}</div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">车型：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <div class="car-text-width js-show-tips carInfo">${proxyDtlResult.data.carInfo}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">年款排量：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <div class="car-text-width js-show-tips carYearPower">${proxyDtlResult.data.carYearPower}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label detail-title">期望交车时间：</div>
                        <div class="form-item">
                            <div class="yqx-text expectTime">${(proxyDtlResult.data.expectTime?string("yyyy-MM-dd hh:mm"))!}</div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">颜色：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <div class="car-text-width js-show-tips carColor">${proxyDtlResult.data.carColor}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">vin码：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <div class="car-text-width js-show-tips vin">${proxyDtlResult.data.vin}</div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
            <!--用于编辑页面-->

                <div class="wt-info">
                    <div class="show-grid">
                        <div class="col-4">
                            <div class="form-label detail-title">受托方联系人:</div>
                            <div class="form-item">
                                <div class="yqx-text js-call shareName">
                                    ${proxyDtlResult.data.shareName}
                                </div>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="form-label detail-title">受托方联系电话:</div>
                            <div class="form-item">
                                <div class="yqx-text shareMobile">
                                ${proxyDtlResult.data.shareMobile}
                                </div>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="form-label detail-title">受托方联系地址:</div>
                            <div class="form-item">
                                <div class="yqx-text">
                                    <div class="car-text-width js-show-tips shareAddr">${proxyDtlResult.data.shareAddr}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="show-grid">
                        <div class="form-label detail-title">备注：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <div class="car-text-width js-show-tips remark">${proxyDtlResult.data.remark}</div>
                            </div>
                        </div>
                    </div>
                </div>
            <#else >
                <div class="wt-infor">
                    <div class="show-grid">
                        <div class="col-4">
                            <div class="form-label detail-title">车牌：</div>
                            <div class="form-item">
                                <div class="yqx-text carLicense">${result.data.carLicense}</div>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="form-label detail-title">车型：</div>
                            <div class="form-item">
                                <div class="yqx-text">
                                    <div class="car-text-width js-show-tips carInfo">${result.data.carInfo}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="form-label detail-title">年款排量：</div>
                            <div class="form-item">
                                <div class="yqx-text">
                                    <div class="car-text-width js-show-tips carYearPower">${result.data.yearPower}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="show-grid">
                        <div class="col-4">
                            <div class="form-label detail-title">期望交车时间：</div>
                            <div class="form-item">
                                <div class="yqx-text expectTime">${(result.data.expectTime?string("yyyy-MM-dd hh:mm"))!}</div>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="form-label detail-title">颜色：</div>
                            <div class="form-item">
                                <div class="yqx-text">
                                    <div class="car-text-width js-show-tips carColor">${result.data.carColor}</div>
                                </div>
                            </div>
                        </div>
                        <div class="col-4">
                            <div class="form-label detail-title">vin码：</div>
                            <div class="form-item">
                                <div class="yqx-text">
                                    <div class="car-text-width js-show-tips vin">${result.data.vin}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="wtCon">

                    </div>
                    <div class="show-grid">
                        <div class="col-4">
                            <div class="form-label detail-title">备注：</div>
                            <div class="form-item">
                                <div class="yqx-text">
                                    <div class="car-text-width js-show-tips remark">${result.data.postscript}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </#if>

        </div>
        <#include "yqx/tpl/order/static-precheck-tpl.ftl">

        <div class="wt-bt-box">
            <div class="no-wt-title">待委托项目列表</div>
            <!--待委托项目列表 start-->
            <div class="wt-project js-table">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th><input type="checkbox" class="js-selectall"></th>
                        <th>序号</th>
                        <th>服务类型</th>
                        <th>服务项目</th>
                        <th>工时</th>
                        <th>工时费</th>
                        <th>服务金额</th>
                        <th>委托金额</th>
                        <th>服务备注</th>
                    </tr>
                    </thead>
                    <tbody id="tableBox">


                            <#if proxyDtlResult??>
                                <input type="hidden" class="proxySn" value="${proxyDtlResult.data.proxySn}"/>
                                <!--编辑时用到-->
                                <#if orderServicesVos??>
                                <#list orderServicesVos as orderServices>
                                <tr>

                                    <input type="hidden" class="proxyServiceId" value="${orderServices.id}">
                                    <input type="hidden" class="js-service-id serviceId" value="${orderServices.serviceId}" name="serviceId">
                                    <input type="hidden" class="serviceSn" value="${orderServices.serviceSn}" name="serviceSn">
                                    <input type="hidden" class="surfaceNum" value="${orderServices.surfaceNum}" name="surfaceNum">
                                    <#if orderServices.chooseStatus = true>
                                        <td><input type="checkbox" checked="checked" class="select-list"></td>
                                    <#else >
                                        <td><input type="checkbox" class="select-list"></td>
                                    </#if>
                                    <td>${orderServices_index+1}</td>
                                    <td class="serviceType">${orderServices.serviceCatName}</td>
                                    <td class="serviceName">${orderServices.serviceName}</td>
                                    <td>
                                        <input type="text" name="serviceHour" class="yqx-input  yqx-input-small serviceHour" value="${orderServices.serviceHour}" placeholder="">
                                    </td>

                                    <td>
                                            <div class="form-item">
                                                <input type="text" name="sharePrice" class="yqx-input yqx-input-icon yqx-input-small js-hour sharePrice" data-options="" value="${orderServices.sharePrice}" placeholder="">
                                                <div class="yqx-select-options" style="width:169px;display: none;">
                                                    <dl>
                                                        <#list orderServices.shopServiceInfoList as list>
                                                            <dd class="yqx-select-option js-show-tips" title="${list.sharePrice}" data-key="${list.sharePrice}">${list.sharePrice}</dd>
                                                        </#list>
                                                    </dl>
                                                </div>
                                                <span class="fa icon-angle-down icon-small"></span>
                                            </div>
                                    </td>

                                    <td class="servicePrice">${orderServices.servicePrice}</td>
                                    <td><input type="text" name="proxyAmount" class="yqx-input  yqx-input-small js-wtprice proxyAmount" value="${orderServices.proxyAmount}" placeholder=""></td>
                                    <td><input type="text" name="serviceNote" class="yqx-input yqx-input-small serviceNote" value="${orderServices.serviceNote}" placeholder=""></td>
                                </tr>
                                </#list>
                                </#if>


                                <#else >
                                <!--添加时用到-->
                                    <#if result.data??>
                                    <#list result.data.orderServicesList as item>
                                        <tr>
                                            <input type="hidden" class="js-service-id serviceId" value="${item.serviceId}" name="serviceId">
                                            <input type="hidden" class="serviceSn" value="${item.serviceSn}" name="serviceSn">
                                            <input type="hidden" class="surfaceNum" value="${item.surfaceNum}" name="surfaceNum">

                                            <td><input type="checkbox" class="select-list"></td>

                                            <td>${item_index+1}</td>
                                            <td class="serviceType">${item.serviceCatName}</td>
                                            <td class="serviceName">${item.serviceName}</td>
                                            <td>
                                                <input type="text" name="serviceHour" class="yqx-input  yqx-input-small serviceHour" value="${item.serviceHour}">
                                            </td>

                                            <td>
                                                <div class="form-item">
                                                    <input type="text" name="sharePrice" class="yqx-input yqx-input-icon yqx-input-small js-hour sharePrice" value="${item.sharePrice}">
                                                    <div class="yqx-select-options" style="width:169px;display: none;">
                                                        <dl>
                                                            <#list item.shopServiceInfoList as list>
                                                            <dd class="yqx-select-option js-show-tips" title="${list.sharePrice}" data-key="${list.sharePrice}">${list.sharePrice}</dd>
                                                            </#list>
                                                        </dl>
                                                    </div>
                                                    <span class="fa icon-angle-down icon-small"></span>
                                                </div>
                                            </td>

                                            <td class="servicePrice">${item.servicePrice}</td>
                                            <td><input type="text" name="proxyAmount" class="yqx-input yqx-input-small js-wtprice proxyAmount" value="${item.proxyAmount}" placeholder=""></td>
                                            <td><input type="text" name="serviceNote" class="yqx-input yqx-input-small serviceNote" value="${item.serviceNote}" placeholder=""></td>
                                        </tr>
                                    </#list>
                                    </#if>
                            </#if>
                    </tbody>
                </table>
            </div>
            <div class="total-box">
                <#if proxyDtlResult??>
                    数量合计：<span class="js-numtotal serviceNum">${proxyDtlResult.data.serviceNum}</span>
                    服务金额合计：<span class="js-ordertotal amount">${proxyDtlResult.data.amount}</span>
                    委托金额合计：<span class="js-proxytotal totalProxyAmount">${proxyDtlResult.data.proxyAmount}</span>
                <#else >
                    数量合计：<span class="js-numtotal serviceNum">0</span>
                    服务金额合计：<span class="js-ordertotal amount">0.00</span>
                    委托金额合计：<span class="js-proxytotal totalProxyAmount">0.00</span>
                </#if>
            </div>

            <!--待委托项目列表 end-->
            <div class="btn-box">
                <#if proxyDtlResult??>
                    <button href="javascript:;" class="yqx-btn yqx-btn-2 js-bc-btn">保存</button>
                <#else >
                    <button href="javascript:;" class="yqx-btn yqx-btn-2 js-wt-btn">委托</button>
                </#if>
                <button class="yqx-btn yqx-btn-1 fr js-goback">返回</button>
            </div>
        </div>


    </div>
    <!-- 右侧内容区 end -->
</div>

<script type="text/html" id="wtTpl">
    <%if (json.success) {%>
    <%if (json.data){%>
    <%if (json.data.shop){%>
    <%var item = json.data.shop%>
    <div class="show-grid">
        <div class="col-4">
            <div class="form-label detail-title">受托方联系人:</div>
            <div class="form-item">
                <div class="yqx-text js-call shareName"><%=item.contact%></div>
            </div>
        </div>
        <div class="col-4">
            <div class="form-label detail-title">受托方联系电话:</div>
            <div class="form-item">
                <div class="yqx-text shareMobile"><%=item.tel%></div>
            </div>
        </div>
        <div class="col-4">
            <div class="form-label detail-title">受托方联系地址:</div>
            <div class="form-item">
                <div class="yqx-text shareAddr">
                    <div class="car-text-width js-show-tips"><%=item.address%></div>
                </div>
            </div>
        </div>
    </div>
    <%}}}%>
</script>


<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/proxy/translateproxy.js?add5a1b4a140ae2c317cc55124371084"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">