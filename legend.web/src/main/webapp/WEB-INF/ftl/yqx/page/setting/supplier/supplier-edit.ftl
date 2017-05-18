<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/supplier/supplier-edit.css?ec35df01dffd567e672a2a85cfe86f4a"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">供应商资料<small>－<#if supplier> 编辑供应商<#else >新增供应商</#if></small></h3>
        </div>
        <div class="content js-supplier">
            <div class="title">
                <i></i>基本信息
                <div class="accessories-num fr">
                    供应商编号：<span class="goodsSn"><#if supplier> ${supplier.supplierSn} <#else >${supplierSn}</#if></span>
                    <input type="hidden" name="supplierSn" value="<#if supplier> ${supplier.supplierSn} <#else >${supplierSn}</#if>"/>
                </div>
            </div>
            <input type="hidden" name="id" value="${supplier.id}"/>
            <div class="form-box">
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            供应商名称:
                        </div>
                        <div class="form-item">
                            <input type="text" name="supplierName" class="yqx-input yqx-input-small" value="${supplier.supplierName}" placeholder="" data-v-type="required" maxlength="50">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            供应商类别:
                        </div>
                        <div class="form-item form-width">
                            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-category" value="<#if supplier.categoryName>${supplier.categoryName}<#else>一级</#if>" placeholder="请选择" data-v-type=" required">
                            <input type="hidden" name="category" value="<#if supplier.category>${supplier.category}<#else>1</#if>"/>
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            结算方式:
                        </div>
                        <div class="form-item form-width">
                            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-pay-method" value="<#if supplier.paymentMode>${supplier.paymentMode}<#else>日结</#if>" placeholder="请选择" data-v-type=" required">
                            <input type="hidden" name="payMethod" value="<#if supplier.payMethod>${supplier.payMethod}<#else>0</#if>"/>
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>

                    <div class="col-6">
                        <div class="form-label">
                            联系人:
                        </div>
                        <div class="form-item">
                            <input type="text" name="contact" class="yqx-input yqx-input-small" value="${supplier.contact}" placeholder="" maxlength="45">
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label">
                            供应商电话:
                        </div>
                        <div class="form-item">
                            <input type="text" name="mobile" class="yqx-input yqx-input-small" value="${supplier.mobile}" placeholder="" maxlength="45">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label">
                            主营业务:
                        </div>
                        <div class="form-item">
                            <input type="text" name="content" class="yqx-input yqx-input-small" value="${supplier.content}" placeholder="" maxlength="200">
                        </div>
                    </div>
                </div>
                <div class="show-grid">

                </div>
            </div>
            <div class="title"><i></i>开票信息</div>
            <div class="form-bottom-box">
                <div class="show-grid">
                    <div class="form-label ">
                        开票类型:
                    </div>
                    <ul class="receipt-box js-receipt">
                        <li class="current" data-invoice-type="0"><span>不开发票</span></li>
                        <li data-invoice-type="1"><span>普通发票</span></li>
                        <li data-invoice-type="2"><span>增值税发票</span></li>
                        <li data-invoice-type="3"><span>其他发票</span></li>
                    </ul>
                    <input type="hidden" name="invoiceType" value="<#if supplier> ${supplier.invoiceType}<#else >0</#if>"/>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            单位名称:
                        </div>
                        <div class="form-item">
                            <input type="text" name="companyName" class="yqx-input yqx-input-small" value="${supplier.companyName}" placeholder="" data-v-type="required" maxlength="100">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label">
                            单位地址:
                        </div>
                        <div class="form-item">
                            <input type="text" name="address" class="yqx-input yqx-input-small" value="${supplier.address}" placeholder="" maxlength="255">
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label">
                            单位电话:
                        </div>
                        <div class="form-item">
                            <input type="text" name="tel" class="yqx-input yqx-input-small" value="${supplier.tel}" placeholder="" maxlength="45">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label">
                            单位税号:
                        </div>
                        <div class="form-item">
                            <input type="text" name="invoiceNo" class="yqx-input yqx-input-small" value="${supplier.invoiceNo}" placeholder="" maxlength="100">
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label">
                            开户行:
                        </div>
                        <div class="form-item">
                            <input type="text" name="openingBank" class="yqx-input yqx-input-small" value="${supplier.openingBank}" placeholder="" maxlength="100">
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label">
                            开户账号:
                        </div>
                        <div class="form-item">
                            <input type="text" name="bankAccount" class="yqx-input yqx-input-small" value="${supplier.bankAccount}" placeholder="" minlength="16" maxlength="50">
                        </div>
                    </div>
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-return fr">返回</button>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/setting/supplier/supplier-edit.js?cbd1f8c9959d3df41a2b8dea0ff8dca1"></script>
<#include "yqx/layout/footer.ftl">