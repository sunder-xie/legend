<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/proxy/proxydetail.css?a79211ceaa7b90869c08576f0473f36b"/>
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
            <h1 class="headline fl">
            <#if currentRole=='authorize'>
                委托单详情
            </#if>
            <#if currentRole=='trustee'>
                受托单详情
            </#if>
            </h1>
        </div>
        <div class="wt-top-box">
            <input type="hidden" class="js-proxyid" data-proxy-id="${result.data.id}">
            <input type="hidden" class="js-orderid" data-order-id="${result.data.orderId}">
            <input type="hidden" class="js-proxy-order-id" data-proxy-order-id="${result.data.proxyId}">

            <div class="wt-infor">
                <!--委托信息 start-->
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label detail-title font-bold">委托单号：</div>
                        <div class="form-item">
                            <div class="yqx-text font-bold">
                            ${result.data.proxySn}(开单日期：${result.data.proxyTimeStr})
                            </div>
                        </div>
                    </div>
                    <!--
                    <div class="col-6">
                        <div class="form-label detail-title">期望交车日期：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                             ${result.data.completeTime}
                            </div>
                        </div>
                    </div>
                    -->
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label detail-title">车牌：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.carLicense}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">车型：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <div class="car-text-width js-show-tips">${result.data.carInfo}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label detail-title">年款排量：</div>
                    <div class="form-item">
                        <div class="yqx-text long-width js-show-tips">
                        ${result.data.carYearPower}
                        </div>
                    </div>
                </div>

                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label detail-title">委托方:</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.shopName}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">委托方联系人：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.contactName}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">委托方联系电话：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.contactMobile}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label detail-title">受托方：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.proxyShopName}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">受托方联系人：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.shareName}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">受托方联系电话：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.shareMobile}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                        <div class="form-label detail-title">受托方联系地址：</div>
                        <div class="form-item">
                            <div class="yqx-text long-width js-show-tips">
                            ${result.data.shareAddr}
                            </div>
                        </div>

                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label detail-title">期望交车时间：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${(result.data.expectTime?string("yyyy-MM-dd hh:mm"))!}
                            </div>
                        </div>
                    </div>

                    <div class="col-4">
                        <div class="form-label detail-title">颜色：</div>
                        <div class="form-item">
                            <div class="yqx-tex max-width js-show-tips">
                            ${result.data.carColor}
                            </div>
                        </div>
                    </div>

                    <div class="col-4">
                        <div class="form-label detail-title">VIN码：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.vin}
                            </div>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label detail-title">服务顾问：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.serviceSa}
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">备注：</div>
                        <div class="form-item">
                            <div class="yqx-text max-width js-show-tips">
                            ${result.data.remark}
                            </div>
                        </div>
                    </div>
                </div>

            <#if result.data.proxyStatus=='YQX'>
                <div class="seal-box  already-cancelled"/></div>
            </#if>
            <#if result.data.proxyStatus=='YJQ'>
                <div class="seal-box  settled"/></div>
            </#if>
            <#if result.data.proxyStatus=='YWT'>
                <div class="seal-box  already-commissioned"/></div>
            </#if>
            <#if result.data.proxyStatus=='YJD'>
                <div class="seal-box  connected-list"/></div>
            </#if>
            <#if result.data.proxyStatus=='YYC'>
                <div class="seal-box  inspection"/></div>
            </#if>
            <#if result.data.proxyStatus=='YJC'>
                <div class="seal-box  Delivered"/></div>
            </#if>
            <#if result.data.proxyStatus=='FPDD'>
                <div class="seal-box  already-workers"/></div>
            </#if>
            <#if result.data.proxyStatus=='DDWC'>
                <div class="seal-box  already-completed"/></div>
            </#if>

                <!--委托信息 end-->
            </div>
        </div>
        <#include "yqx/tpl/order/static-precheck-tpl.ftl">
        <div class="wt-bt-box">
            <div class="no-wt-title">已委托项目列表</div>
            <!--待委托项目列表 start-->
            <div class="wt-project" id="tableCon">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>服务类型</th>
                        <th>服务项目</th>
                        <th>工时</th>
                        <th>工时费</th>
                        <th>委托金额</th>
                        <th>服务备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list result.data.proxyServicesVoList as proxyServices>
                    <tr>
                        <td class="num">${proxyServices_index+1}</td>
                        <td>${proxyServices.serviceType}</td>
                        <td>
                            <div class="max-width js-show-tips">${proxyServices.serviceName}</div>
                        </td>
                        <td>${proxyServices.serviceHour}</td>
                        <td>${proxyServices.sharePrice}</td>
                        <td class="price">${proxyServices.proxyAmount}</td>
                        <td>
                            <div class="max-width js-show-tips">${proxyServices.serviceNote}</div>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <div class="total-box">
                <p>数量合计：<span class="js-numtotal"></span>个</p>

                <p>委托金额合计：<span class="js-moneytotal"></span>元</p>
            </div>
            <!--待委托项目列表 end-->
            <div class="btn-box">
            <#if currentRole == 'trustee'>
                <#if result.data.proxyStatus == 'YWT'>
                    <button href="javascript:;" class="yqx-btn yqx-btn-2 js-accept-order">接单</button>
                </#if>
                <#if result.data.proxyStatus == 'DDWC'>
                    <button href="javascript:;" class="yqx-btn yqx-btn-2 js-back-car">交车</button>
                </#if>
                <#if result.data.proxyStatus == 'YJD'>
                    <button href="javascript:;" class="yqx-btn yqx-btn-2 js-translate-order">转工单</button>
                </#if>
                <#if result.data.proxyId != 0>
                    <button href="javascript:;" class="yqx-btn yqx-btn-2 js-show-order-info">查看工单</button>
                </#if>
            </#if>
            <#if currentRole=='authorize'>
                <button href="javascript:;" class="yqx-btn yqx-btn-2 js-show-proxy-order-info">查看工单</button>
                <button class="yqx-btn yqx-btn-1 fr js-goback">返回</button>
                <#if result.data.proxyStatus=='YQX' || result.data.proxyStatus=='DDWC'>
                <button class="yqx-btn yqx-btn-1 fr js-invalid hide">无效</button>
                <#else>
                <button class="yqx-btn yqx-btn-1 fr js-invalid">无效</button>
                </#if>
                <#if result.data.proxyStatus=='YWT' || result.data.proxyStatus=='YJD' || result.data.proxyStatus=='YYC' || result.data.proxyStatus=='FPDD'>
                    <button class="yqx-btn yqx-btn-1 fr js-edit">编辑</button>
                </#if>
            </#if>
            </div>
        </div>
    </div>

    <!-- 右侧内容区 end -->
</div>

<script type="text/html" id="tableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>服务类型</th>
            <th>服务项目</th>
            <th>工时</th>
            <th>工时费</th>
            <th>委托金额</th>
            <th>服务备注</th>
        </tr>
        </thead>
        <tbody>


        <#list result.data.proxyServicesVoList as proxyServices>
        <tr>
            <td>${proxyServices_index+1}</td>
            <td>${proxyServices.serviceType}</td>
            <td>${proxyServices.serviceName}</td>
            <td>${proxyServices.serviceHour}</td>
            <td>${proxyServices.sharePrice}</td>
            <td>${proxyServices.proxyAmount}</td>
            <td>${proxyServices.serviceNote}</td>
        </tr>
        </#list>
        </tbody>
    </table>
</script>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/proxy/proxydetail.js?c81ebc34eb2e2f7c55e2e837aa2f8a31"></script>

<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">