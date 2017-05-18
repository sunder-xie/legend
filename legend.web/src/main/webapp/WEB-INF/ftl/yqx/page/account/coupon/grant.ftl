<#include "yqx/layout/header.ftl">
<div class="yqx-wrapper clearfix">
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/grant.css?82011c5c746440cd27ae8b7d5c746fdc" type="text/css"/>
    <div class="left-menu">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="right-content">
        <h1 class="headline">客户查询<small>-发券</small></h1>
        <div class="content">
            <div class="account-info">
                <div class="fl">个人信息</div>
                <div class="show-grid inline">
                    <div>
                        <label>　　车主：</label>
                        <i>${customer.customerName}</i>
                    </div><div>
                    <label>车主电话：</label>
                    <i>${customer.mobile}</i>
                </div><div class="car-license-box">
                    <label>拥有车辆：</label>
                <#if customerCars>
                    <#list customerCars as customerCar>
                        <i class="license-icon">${customerCar.license}</i>
                    </#list>
                </#if>

                </div>
                </div>
            </div>
        </div>
        <div class="content">
            <div class="choose-package">
                <input type="hidden" value="${id}" name="accountId" />
                <div class="plan">
                    <div class="frame js-current">赠送</div>
                <#list couponSuites as item>
                    <div class="frame ellipsis-1" data-id="${item.id}" data-price="${item.salePrice}" name="suiteId">${item.suiteName}</div>
                </#list>
                </div>
            </div>
            <div class="charge-table">
                <table>
                    <tr class="table-head">
                        <th class="w200">优惠券</th>
                        <th class="w90">数量</th>
                        <th class="w50">操作</th>
                    </tr>
                    <tbody class="yqx-tbody" id="orderServiceTB">
                    <#list basicOrderService as orderService>
                    <tr class="service-datatr js-move" data-id="<%=orderService.id%>">
                        <td class="p10">
                            <div class="form-item w100">
                                <input type="text" name="serviceName" class="yqx-input yqx-input-small js-show-tips"
                                       value="${orderService.couponName}" disabled="" readonly/>
                            </div>
                        </td>
                        <td class="p10">
                            <div class="form-item w100">
                                <input type="text" name="couponNum" class="yqx-input yqx-input-icon"
                                       data-v-type="number|maxValue:999"
                                >
                                <span class="fa">张</span>
                            </div>
                        </td>
                        <td>
                            <span class="delete">删除</span>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                    <tbody id="tableFill" ></tbody>
                </table>
                <button class="yqx-btn yqx-btn-3 add js-get-service">添加</button>
            </div>
            <div class="input-info">
                <div class="amount">
                    <div class="form-label">
                        套餐售价
                    </div>
                    <div class="form-item">
                        <input type="text" name="price" class="yqx-input yqx-input-icon" disabled="disabled">
                        <span class="fa">元</span>
                    </div>
                </div>
                <div class="pay-way">
                    <div class="form-label">
                        支付方式
                    </div>
                    <div class="form-item">
                        <select name="paymentId" class="yqx-input yqx-input-icon h36">
                        <#list payment as item>
                            <option value="${item.id}">${item.name}
                            </option>
                        </#list>
                        </select>
                    </div>
                </div>
                <div class="collection">
                    <div class="form-label">
                        收款金额
                    </div>
                    <div class="form-item">
                        <input type="text" name="payAmount" class="yqx-input yqx-input-icon" data-v-type="required|number" placeholder="请输入金额">
                        <span class="fa">元</span>
                    </div>
                </div>
            </div>
            <div class="remark">
                <div class="form-label w60">
                    备注信息
                </div>
                <div class="form-item w700">
                    <textarea class="yqx-textarea" name="" id="" cols="100" rows="3"></textarea>
                </div>
            </div>
            <div class="button">
                <button class="yqx-btn yqx-btn-2 submit">提交</button>
                <a href="javascript:;" class="yqx-btn yqx-btn-1 fr return js-return">返回</a>
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
                <input type="text" name="couponNum" class="yqx-input yqx-input-icon"
                       data-v-type="number|maxValue:999"
                >
                <span class="fa">张</span>
            </div>
        </td>
        <td>
            <span class="delete">删除</span>
        </td>
    </tr>
    <% }} %>
</script>
<script type="text/template" id="tableTpl">
    <%if(json){%>
    <%for(i=0;i<json.data.length;i++){%>
    <tr class="service-datatr js-move">
        <%var a=json.data[i];%>
        <td class="p10">
            <div class="form-item w100">
                <input type="text" name="serviceName" class="yqx-input" readonly
                       value="<%=a.couponName%>" disabled=""/>
            </div>
        </td>
        <td class="p10">
            <div class="form-item w100">
                <input type="text" name="couponNum" class="yqx-input yqx-input-icon" readonly value="<%=a.couponCount%>" disabled="">
                <span class="fa">张</span>
            </div>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>
<#include "yqx/tpl/common/get-coupon-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/account/coupon/grant.js?05e6cf7157aed404d11f99a82b6d2e71"></script>
<#include "yqx/layout/footer.ftl">