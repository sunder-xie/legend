<#include "yqx/layout/header.ftl">
<div class="yqx-wrapper clearfix">
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/suite_create.css?9dd76d8bac77447a94044ca5072fb510" type="text/css"/>
    <div class="left-menu">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="right-content">
        <h3 class="Z-title">客户管理 > <a target="_self" href="${BASE_PATH}/account/setting"> 优惠设置 > </a> <i>新增优惠券套餐</i></h3>
        <div class="content">
            <div class="basic-info">
                <input type="hidden" value="${couponSuite.id}" id="id" />
                <div class="form-label title">
                    基本信息
                </div>
                <div class="coupon-name">
                    <div class="form-label form-label-must">
                        套餐名称
                    </div>
                    <div class="form-item">
                        <input type="text" value="${couponSuite.suiteName}" name="suiteName" class="yqx-input" data-v-type="required | maxLength:20">
                    </div>
                </div>
            </div>
            <div class="charge-table">
                <div class="form-label title">
                    添加优惠券
                </div>
                <div class="ml77">
                    <table>
                        <tr class="table-head">
                            <th class="w200">优惠券</th>
                            <th class="w90">数量</th>
                            <th class="w50">操作</th>
                        </tr>
                        <tbody class="yqx-tbody" id="orderServiceTB">
                        <#list basicOrderService as orderService>
                        <tr class="service-datatr js-move" data-id="${orderService.couponInfoId}">
                            <td class="p10">
                                <div class="form-item w100">
                                    <input type="text" name="serviceName" class="yqx-input yqx-input-small js-show-tips"
                                           value="${orderService.couponName}" disabled/>
                                    <input type="hidden" name="couponId" value="${orderService.couponInfoId}" />
                                </div>
                            </td>
                            <td class="p10">
                                <div class="form-item w100">
                                    <input type="text" name="couponNum" class="yqx-input yqx-input-icon" value="${orderService.couponCount}">
                                    <span class="fa">张</span>
                                </div>
                            </td>
                            <td>
                                <span class="delete">删除</span>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                    <button class="yqx-btn yqx-btn-3 add js-get-service">添加</button>
                </div>
            </div>
            <div class="value">
                <div class="form-label title">
                    价值
                </div>
                <div class="price">
                    <div class="form-label">
                        售价
                    </div>
                    <div class="form-item">
                        <input type="text" name="salePrice" class="yqx-input yqx-input-icon" value="${couponSuite.salePrice}" data-v-type="required | number | maxValue:99999">
                        <span class="fa">元</span>
                    </div>
                </div>
            </div>
            <div class="button">
                <button class="yqx-btn yqx-btn-2 submit">提交</button>
                <button class="yqx-btn yqx-btn-1 return js-goback fr">返回</button>
            </div>
        </div>
    </div>　
</div>
<script type="text/html" id="serviceTpl">
    <% if (json && json.length) { %>
    <% for (var i = 0; i < json.length; i++) { %>
    <% var orderService = json[i]; %>
    <tr class="service-datatr js-move" data-id="<%=orderService.id%>">
        <td class="p10">
            <div class="form-item w100">
                <input type="text" name="" class="yqx-input" value="<%= orderService.couponName %>">
                <input type="hidden" name="couponId" value="<%= orderService.id %>" />
            </div>
        </td>
        <td class="p10">
            <div class="form-item w100">
                <input type="text" name="couponNum" class="yqx-input yqx-input-icon" value="" data-v-type="number|maxValue:999">
                <span class="fa">张</span>
            </div>
        </td>
        <td>
            <span class="delete">删除</span>
        </td>
    </tr>
    <% }} %>
</script>
<#include "yqx/tpl/common/get-coupon-tpl.ftl">
<script type="text/javascript" src="${BASE_PATH}/static/js/page/account/coupon/suite_create.js?95dbb85d340fc1fb02de660ebb79a32c"></script>
<#include "yqx/layout/footer.ftl">