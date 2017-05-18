<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/settle/consume-service.css?b22fbbb8dcc26646658c641a7a0814cd"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">核销服务券</h3>
        </div>
        <div class="content-box">
            <div class="consume-service">
                <div class="consume-title clearfix">
                    <h3 class="fl">${insuranceUserServicePackageVo.packageName}</h3>
                    <h3 class="m-left fl">${insuranceUserServicePackageVo.vehiclePlateNo}</h3>
                    <p class="fr">市场售价：<span class="money-font">${insuranceUserServicePackageVo.marketPrice}</span></p>
                </div>
                <div class="consume-table">
                    <table>
                        <thead>
                            <tr>
                                <th class="col1">项目名称</th>
                                <th class="col2">型号</th>
                                <th class="col3">单位</th>
                                <th class="col4">次数</th>
                                <th class="col5"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list insuranceUserServicePackageVo.itemVoList as itemVoList >
                            <tr class="js-volist">
                                <input type="hidden" value="${itemVoList.id}" class="service-item-id"/>
                                <input type="hidden" value="${insuranceUserServicePackageVo.id}" class="package-id"/>
                                <td class="item-name">${itemVoList.itemName}</td>
                                <td>
                                <#if itemVoList.materialModelInfo>
                                    ${itemVoList.materialModelInfo}
                                <#else>
                                    /
                                </#if>
                                </td>
                                <td>${itemVoList.itemUnit}</td>
                                <td class="times">${itemVoList.remainServiceTimes}</td>
                                <td>
                                    <#if itemVoList.remainServiceTimes !=0>
                                    <div class="regulator clearfix">
                                        <button  class="subtract js-sub fl"> - </button>
                                        <input type="text" value="0" class="regulator-input consume-times fl js-consume-times">
                                        <button  class="addition js-add fl"> + </button>
                                    </div>
                                    </#if>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
            </div>

            <#--<div class="contact-box clearfix">-->
                <#--<div class="phone-box">车主电话：<span class="phone-num">${insuranceUserServicePackageVo.clientPhone}</span></div>-->
                <#--<button class="yqx-btn yqx-btn-3 yqx-btn-small js-send">发送手机验证码</button>-->
                <#--<div class="form-label">-->
                    <#--手机验证码：-->
                <#--</div>-->
                <#--<div class="form-item">-->
                    <#--<input type="text" name="" class="yqx-input yqx-input-small captcha" value="" placeholder="请输入">-->
                <#--</div>-->
            <#--</div>-->
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-submit">提交核销</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-return">取消</button>
            </div>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 脚本引入区 start -->

<script src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script src="${BASE_PATH}/static/js/page/ax_insurance/settle/consume-service.js?0724cbd921d86a0418c6c2614ccc57a8"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">