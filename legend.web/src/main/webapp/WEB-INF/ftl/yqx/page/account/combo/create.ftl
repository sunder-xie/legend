<#include "yqx/layout/header.ftl">
<div class="yqx-wrapper clearfix">
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/combo/create.css?d267619a80129dcaab9552b214a2dcd7"/>
    <div class="left-menu">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="right-content">
        <input type="hidden" value="${comboInfo.customizeTime}" id="customizeTime" name="customizeTime">
        <h3 class="Z-title">客户管理 > <a target="_self" href="${BASE_PATH}/account/setting"> 优惠设置 > </a>
            <#if comboInfo == null>
                <i>新增计次卡</i>
            <#else>
                <i>编辑计次卡</i>
                <input type="hidden" id="editCombo" value="true">
            </#if>
        </h3>

        <div class="content">
            <div class="basic-info">
                <div class="form-label title">
                    基本信息
                </div>
                <div>
                    <input type="hidden" name="comboInfoId" value="${comboInfo.id}">
                    <div class="coupon-name">
                        <div class="form-label form-label-must">
                            计次卡名称
                        </div>
                        <div class="form-item">
                                <input type="text" name="comboName" class="yqx-input" value="${comboInfo.comboName}" data-v-type="required | maxLength:20">
                        </div>
                    </div>
                </div>
            </div>
            <div class="info info-expire">
                <div class="form-lable title">有效期</div>
                <ul class="show-grid">
                    <li class="col-6">
                        <div class="form-label form-label-must">
                            生效时间
                        </div>
                        <div class="form-item effectiveTime">
                            <button class="yqx-btn yqx-btn-1 yqx-btn-small im-btn selected_btn">发放后立即生效可用
                            </button>
                            <button class="yqx-btn yqx-btn-1 yqx-btn-small self-btn">自定义生效及失效时间</button>
                        </div>
                    </li>
                    <li class="col-6 mt13">
                        <div class="form-label form-label-must">
                            有效期
                        </div>
                        <div class="form-item">
                            <input type="text" name="effectivePeriodDays" class="yqx-input yqx-input-icon im-input" value="${comboInfo.effectivePeriodDays}" data-v-type=" required | integer:1 | notempty | maxLength:6">
                            <span class="fa">天</span>
                        </div>
                    </li>
                </ul>
                <ul class="show-grid self-ul hide">
                    <li class="col-6">
                        <div class="form-label form-label-must">
                            生效时间
                        </div>
                        <div class="form-item">
                            <input type="text" name="effectiveDateStr" class="yqx-input Date"
                                   data-v-type="required" disabled
                                   value="${comboInfo.effectiveDateStr}" placeholder="选择生效时间">
                        </div>
                    </li>
                    <li class="col-6">
                        <div class="form-label form-label-must">
                            失效时间
                        </div>
                        <div class="form-item">
                            <input type="text" name="expireDateStr" class="yqx-input Date"
                                   data-v-type="required" disabled
                                   value="${comboInfo.expireDateStr}" placeholder="选择失效时间">
                        </div>
                    </li>
                </ul>

            </div>
            <div class="charge-table">
                <div class="form-label title">
                    添加服务
                </div>
                <div class="ml77">
                    <table>
                        <tr class="table-head">
                            <th class="w200">服务名称</th>
                            <th class="w90">总次数</th>
                            <th class="w50">操作</th>
                        </tr>
                        <tbody class="yqx-tbody" id="orderServiceTB">
                        <#list comboInfo.content as item>
                        <tr class="service-datatr js-move" data-id="${item.serviceId}">
                            <td class="p10">
                                <div class="form-item w100">
                                    <input type="text" name="serviceName" class="yqx-input yqx-input-small js-show-tips"
                                           value="${item.serviceName}" disabled/>
                                </div>
                            </td>
                            <td class="p10">
                                <div class="form-item">
                                    <input type="text" name="serviceCount" class="yqx-input yqx-input-icon" value="${item.serviceCount}">
                                    <span class="fa">次</span>
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
                        <input type="text" name="salePrice" data-v-type="required | number" class="yqx-input yqx-input-icon" value="${comboInfo.salePrice}">
                        <span class="fa">元</span>
                    </div>
                </div>
            </div>
            <div class="info info-remark">
                <div class="form-label title">备注</div>
                <ul class="show-grid">
                    <li class="col-12">
                        <textarea name="remark" rows="2" id="remark"data-v-type="maxLength:200" class="yqx-textarea">${comboInfo.remark}</textarea>
                    </li>
                </ul>
            </div>
            <div class="button">
                <button class="yqx-btn yqx-btn-2 js-submit">提交</button>
                <button class="yqx-btn yqx-btn-1 return js-goback fr">返回</button>
            </div>
        </div>
    </div>　
</div>
<script type="text/html" id="serviceTpl">
    <%if (json){%>
    <%for(var i in json) { %>
    <%var orderService = json[i]%>
    <tr class="service-datatr js-move" data-id="<%=orderService.id%>">
        <!--服务名称-->
        <td>
            <div class="form-item">
                <input type="text" name="serviceName" class="yqx-input yqx-input-small js-show-tips"
                       value="<%=orderService.name%>" disabled/>
            </div>
        </td>
        <td class="p10">
            <div class="form-item">
                <input type="text" name="serviceCount" class="yqx-input yqx-input-icon">
                <span class="fa">次</span>
            </div>
        </td>
        <td>
            <span class="delete">删除</span>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>
<!-- 添加服务模版 -->
<#include "yqx/tpl/common/get-service-tpl.ftl">

<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/account/combo/create.js?c1cef8b7a8fa0b6e0170f39b293c4ca7"></script>
<#include "yqx/layout/footer.ftl">