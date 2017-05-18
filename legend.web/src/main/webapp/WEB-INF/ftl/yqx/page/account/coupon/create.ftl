<#include "yqx/layout/header.ftl">
<div class="yqx-wrapper clearfix">
    <div class="aside">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <#--样式引入区-->
        <link rel="stylesheet" href="${BASE_PATH}/static/css/common/account/create.css?a40d1d0bbfbce9cc5ece513dc36e9458">
        <link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/coupon/create.css?b56d6c037ff7b0694aa2ca8f69358bb5" type="text/css"/>
    <#--样式引入区-->
    </div>
    <div class="aside-main">
        <h3 class="Z-title"> 客户管理 > <a target="_self" href="${BASE_PATH}/account/setting"> 优惠设置 > </a>
            <#if couponInfo.id=null>
                <i>新增优惠券</i>
            <#else>
                <i>编辑优惠券</i>
            </#if>
        </h3>

        <input type="hidden" value="${couponInfo.id}" id="id" name="id">
        <input type="hidden" value="${couponInfo.couponType}" id="couponType" name="couponType">
        <input type="hidden" value="${couponInfo.useRange}" id="useRange" name="useRange">
        <div class="order-panel">
            <div class="order-panel-head clearfix">
                <div class="tabs-control fl">
                    <span class="tab-item <#if couponInfo.couponType == 1 || !couponInfo> current-item</#if> js-tab-item"
                    data-target=".cash-body"
                    data-type="1">现金券</span><span data-type="2"
                    data-target=".common-body"
                    class="tab-item js-tab-item<#if couponInfo.couponType == 2> current-item</#if>">通用券</span>
                </div>
            </div>
            <div class="order-panel-body info-body cash-body <#if couponInfo.couponType == 1 || !couponInfo>current-body<#else> hide</#if>" id="money">
                <div class="info info-base">
                    <h2 class="info-head">基本信息</h2>
                    <ul class="show-grid">
                        <li class="col-6">
                            <div class="form-label form-label-must">
                                优惠券名称
                            </div>
                            <div class="form-item">
                            <#if couponInfo.id=null>
                                <input type="text" name="couponName" class="yqx-input" value="${couponInfo.couponName}" data-v-type="required | maxLength:20">
                            <#else>
                                <input type="text" name="couponName" class="yqx-input" value="${couponInfo.couponName}" disabled="">
                            </#if>
                            </div>
                        </li>
                        <li class="col-6">
                            <div class="form-label form-label-must">
                                抵扣金额
                            </div>
                            <div class="form-item">
                                <input type="text" name="discountAmount" class="yqx-input yqx-input-icon" value="${couponInfo.discountAmount}" data-v-type="required | integer:1 | maxValue:99999">
                                <span class="fa">元</span>
                            </div>
                        </li>
                    </ul>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small <#if !couponInfo.amountLimit || couponInfo.amountLimit == 0>selected_btn</#if> cost no-cost" data-id="0">无消费金额限制</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small <#if couponInfo.amountLimit gt 0>selected_btn</#if> cost" id="cost" data-id="1">设定消费金额限制</button>
                    满
                    <div class="form-item">
                        <input type="text" name="limitAmount" class="yqx-input money_input"
                               data-v-type="minValue:0"
                               value="<#if couponInfo.amountLimit gt 0>${couponInfo.amountLimit}</#if>" disabled>
                    </div>
                    元使用
                </div>
                <div class="info info-region">
                    <h2 class="info-head">使用范围</h2>
                    <ul class="single-tag useRange">
                        <li class=" <#if couponInfo.useRange == 0>selected</#if> <#if !couponInfo.useRange>selected</#if>"
                            data-id="0">
                            <div class="tag-control">
                                全场通用</div><div class="tag-padding"></div>
                        </li>
                        <li data-id="1"  class="<#if couponInfo.useRange == 1>selected</#if>">
                            <div class="tag-control">全部服务</div><div class="tag-padding"></div>
                        </li>
                        <li class="service <#if couponInfo.useRange == 2>selected</#if>" data-id="2"  >
                            <div class="tag-control">指定服务</div><div class="tag-padding">
                            </div>
                            <div class="table-box <#if couponInfo.useRange != 2>hide</#if>">
                                <table class="yqx-table">
                                    <thead>
                                    <th>服务名称</th>
                                    <th>操作</th>
                                    </thead>
                                    <#list couponInfo.couponServiceList as item>
                                        <tr class="service-datatr">
                                            <!--服务类别-->
                                            <td>
                                                <div class="form-item width-full">
                                                    <input type="text"
                                                           value="${item.serviceName}"
                                                           readonly
                                                           class="yqx-input yqx-input-small">
                                                    <input type="hidden" name="serviceId" value="${item.serviceId}">
                                                </div>
                                            </td>
                                            <td>
                                                <i class="del js-del">删除</i>
                                            </td>
                                        </tr>
                                    </#list>
                                </table>
                                <button class="js-add-service yqx-btn-3 yqx-btn-small">添加服务</button>
                            </div>
                        </li>
                    </ul>

                </div>
                <div class="info info-expire">
                    <h2 class="info-head">有效期</h2>
                    <ul class="show-grid">
                        <li class="col-6">
                            <div class="form-label form-label-must">
                                生效时间
                            </div>
                            <div class="form-item effectiveTime">
                                <button class="yqx-btn yqx-btn-1 yqx-btn-small im-btn <#if !couponInfo.effectiveDateStr> selected_btn</#if>">发放后立即生效可用
                                </button>
                                <button class="yqx-btn yqx-btn-1 yqx-btn-small self-btn <#if couponInfo.effectiveDateStr> selected_btn</#if>">自定义生效及失效时间</button>
                            </div>
                        </li>
                        <li class="col-6 mt13">
                            <div class="form-label form-label-must">
                                有效期
                            </div>
                            <div class="form-item">
                                <input type="text" name="effectivePeriodDays"
                                       <#if couponInfo.effectiveDateStr>disabled</#if>
                                       class="yqx-input yqx-input-icon im-input" value="${couponInfo.effectivePeriodDays}" data-v-type=" required | integer:1 | notempty | maxLength:6">
                                <span class="fa">天</span>
                            </div>
                        </li>
                    </ul>
                    <ul class="show-grid self-ul" <#if couponInfo.effectiveDateStr>style="display: block"</#if>>
                        <li class="col-6">
                            <div class="form-label form-label-must">
                                生效时间
                            </div>
                            <div class="form-item">
                                <input type="text" name="effectiveDate" class="yqx-input Date"
                                data-v-type="required"
                                       <#if !couponInfo.effectiveDateStr>disabled</#if>
                                value="${couponInfo.effectiveDateStr}" placeholder="选择生效时间">
                            </div>
                        </li>
                        <li class="col-6">
                            <div class="form-label form-label-must">
                                失效时间
                            </div>
                            <div class="form-item">
                                <input type="text" name="expiredDate" class="yqx-input Date"
                                 data-v-type="required"
                                       <#if !couponInfo.effectiveDateStr>disabled</#if>
                                 value="${couponInfo.expireDateStr}" placeholder="选择失效时间">
                            </div>
                        </li>
                    </ul>

                </div>
                <div class="info info-regular">
                    <h2 class="info-head">使用规则</h2>
                    <ul class="show-grid">
                        <li class="col-5">
                            <input type="checkbox" id="compatibleWithCard" <#if couponInfo??&&couponInfo.compatibleWithCard==1>checked</#if>/><label for="compatibleWithCard">允许与会员卡共同使用</label>
                        </li>
                        <li class="col-5"
                            data-id="">
                            <input type="checkbox" id="singleUse"<#if couponInfo??&&couponInfo.singleUse==1>checked</#if>/><label for="singleUse">一张工单只允许使用一张该优惠券</label>
                        </li>
                    </ul>

                </div>
                <div class="info info-remark">
                    <h2 class="info-head">备注</h2>
                    <ul class="show-grid">
                        <li class="col-12">
                            <textarea rows="2" name="remark" data-v-type="maxLength:200" class="yqx-textarea text-area-width" ><#if couponInfo??&&couponInfo.remark??>${couponInfo.remark}</#if></textarea>
                        </li>
                    </ul>
                </div>
                <div class="order-panel-foot clearfix">
                    <button class="yqx-btn yqx-btn-2 submit">提交</button>
                    <button class="yqx-btn yqx-btn-1 return js-goback">返回</button>
                </div>
            </div>
            <div class="order-panel-body info-body common-body <#if couponInfo.couponType == 2>current-body<#else> hide</#if>" id="ticket">
                <div class="info info-base">
                    <h2 class="info-head">基本信息</h2>
                    <ul class="show-grid">
                        <li class="col-6">
                            <div class="form-label form-label-must">
                                优惠券名称
                            </div>
                            <div class="form-item">
                                <input type="text" name="couponName" class="yqx-input" value="${couponInfo.couponName}" data-v-type="required | maxLength:20">
                            </div>
                        </li>
                        <li class="col-6">
                            <div class="form-label form-label-must">
                                抵扣设定
                            </div>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-icon" value=""
                                       placeholder="在结算时手动输入金额，直接扣减" style="width:230px;" disabled>
                                <span class="fa">元</span>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="info info-region">
                    <h2 class="info-head">使用范围</h2>

                    <p>全场通用</p>
                </div>
                <div class="info info-expire">
                    <h2 class="info-head">有效期</h2>
                    <ul class="show-grid">
                        <li class="col-6">
                            <div class="form-label form-label-must">
                                生效时间
                            </div>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input" value="发放后立即生效可用" placeholder=""
                                       readonly>
                            </div>
                        </li>
                        <li class="col-6">
                            <div class="form-label form-label-must">
                                有效期
                            </div>
                            <div class="form-item">
                                <input type="text" name="effectivePeriodDays" class="yqx-input yqx-input-icon" value="${couponInfo.effectivePeriodDays}" data-v-type="required | integer:1 | notempty | maxLength:6">
                                <span class="fa">天</span>
                            </div>
                        </li>
                    </ul>

                </div>

                <div class="info info-regular">
                    <h2 class="info-head">使用规则</h2>
                    <ul class="show-grid">
                        <li class="col-5">
                            <input type="checkbox" id="compatibleWithCard" <#if couponInfo??&&couponInfo.compatibleWithCard==1>checked</#if>/><label for="compatibleWithCard">允许与会员卡共同使用</label>
                        </li>
                        <li class="col-5"
                            data-id="">
                        <input type="checkbox" id="singleUse"<#if couponInfo??&&couponInfo.singleUse==1>checked</#if>/><label for="singleUse">一张工单只允许使用一张该优惠券</label>
                        </li>
                    </ul>

                </div>
                <div class="info info-remark">
                    <h2 class="info-head">备注</h2>
                    <ul class="show-grid">
                        <li class="col-12">
                            <textarea rows="2" name="remark" data-v-type="maxLength:200" class="yqx-textarea text-area-width"><#if couponInfo??&&couponInfo.remark??>${couponInfo.remark}</#if></textarea>
                        </li>
                    </ul>
                </div>
                <div class="order-panel-foot clearfix">
                    <button class="yqx-btn yqx-btn-2 submit">提交</button>
                    <button class="yqx-btn yqx-btn-1 return js-goback">返回</button>
                </div>
            </div>

        </div>
    </div>
</div>
<#--弹框-->
<!--服务项目-->
<script type="text/html" id="serviceTpl">
    <tr class="service-datatr">
        <!--服务类别-->
        <td>
            <div class="form-item width-full">
                <input type="text"
                       value="<%=json.name%>"
                       readonly
                       class="yqx-input yqx-input-small">
                <input type="hidden" name="serviceId" value="<%=json.id%>">
            </div>
        </td>
        <td>
            <i class="del js-del">删除</i>
        </td>
    </tr>
</script>
<!-- 添加服务模版 -->
<#include "yqx/tpl/common/get-service-tpl.ftl">
<script type="text/javascript" src="${BASE_PATH}/static/js/page/account/coupon/create.js?d5f698626ee45ea456c671f24f13af24"></script>
<#include "yqx/layout/footer.ftl">