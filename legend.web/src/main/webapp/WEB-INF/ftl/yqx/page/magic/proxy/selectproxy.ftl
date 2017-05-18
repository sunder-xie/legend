<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/proxy/selectproxy.css?703a06180703d045d9ec442ed4eaf3ad"/>
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
            <h1 class="headline fl">接单</h1>
        </div>

        <div class="wt-top-box">
            <div class="wt-select">
                <div class="form-label">
                    接车人:
                </div>
                <div class="form-item">
                    <input type="text" name="proxyShopName" class="yqx-input yqx-input-icon yqx-input-small js-trustee proxyShopName" value="" placeholder="请选择">
                    <span class="fa icon-angle-down icon-small"></span>
                    <input type="hidden" value="" class="receiver">
                    <input type="hidden" value="" class="receiveName">

                </div>
            </div>

            <input type="hidden" value="${proxyOrderDetailVo.id}" class="proxyId">

            <div class="wt-infor">
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label detail-title">车牌：</div>
                        <div class="form-item">
                            <div class="yqx-text carLicense">${proxyOrderDetailVo.carLicense}</div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">车型：</div>
                        <div class="form-item">
                            <div class="yqx-text">
                                <div class="car-text-width js-show-tips carInfo">${proxyOrderDetailVo.carInfo}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">受托方:</div>
                        <div class="form-item">
                            <div class="yqx-text js-call shareNamen max-width js-show-tips">${proxyOrderDetailVo.proxyShopName}</div>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-4">
                        <div class="form-label detail-title">委托方联系人:</div>
                        <div class="form-item">
                            <div class="yqx-text shareMobile">${proxyOrderDetailVo.contactName}</div>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="form-label detail-title">委托方联系电话:</div>
                        <div class="form-item">
                            <div class="yqx-text shareAddr">
                                <div class="car-text-width js-show-tips">${proxyOrderDetailVo.contactMobile}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="wt-bt-box">
            <div class="no-wt-title">委托项目列表</div>
            <!--待委托项目列表 start-->
            <div class="wt-project js-table">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>服务类型</th>
                        <th>服务项目</th>
                        <th>工时</th>
                        <th>工时费</th>
                        <th>应收金额</th>
                        <th>服务备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if proxyOrderDetailVo.proxyServicesVoList??>
                        <#list proxyOrderDetailVo.proxyServicesVoList as list>
                        <tr>
                            <td>${list_index+1}</td>
                            <td class="serviceType">${list.serviceType}</td>
                            <td class="serviceName">${list.serviceName}</td>
                            <td>${list.serviceHour}</td>
                            <td>${list.sharePrice}</td>
                            <td>${list.proxyAmount}</td>
                            <td class="servicePrice"><div class="max-width js-show-tips">${list.serviceNote}</div></td>
                        </tr>
                        </#list>
                    </#if>
                    </tbody>
                </table>
            </div>
            <div class="total-box">
                数量合计：<span class=" serviceNum">${proxyOrderDetailVo.serviceNum}</span>
                委托金额合计：<span class=" totalProxyAmount">${proxyOrderDetailVo.proxyAmount}</span>
            </div>
            <div class="form-bz">
                <div class="form-label">
                    备注：
                </div>
                <div class="form-item">
                    <textarea class="yqx-textarea remark" name="" id="" cols="120" rows="1"></textarea>
                </div>
            </div>

            <!--待委托项目列表 end-->
            <div class="btn-box">
                <button href="javascript:;" class="yqx-btn yqx-btn-2 js-accept-order">接单</button>
                <button class="yqx-btn yqx-btn-1 fr js-goback">返回</button>
            </div>
        </div>


    </div>
    <!-- 右侧内容区 end -->
</div>



<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/proxy/selectproxy.js?809899bb415f5a11d09c2b7254e8a1d1"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">