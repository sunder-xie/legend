<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/paymentType/pay-type.css?c23965f01cfa00ba23b9e7ba14de41a3"/>
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
            <h3 class="headline">业务类型设置</h3>
        </div>
        <div class="content clearfix">
            <div class="title">
                <i></i>管理门店中的业务类型
            </div>
            <div class="form-box fl">
                <div class="pay-type-box">
                    <#list orderTypeList as orderType>
                    <div class="col-6">
                        <div class="form-label js-show-tips">
                        ${orderType.name}
                        </div>
                        <div class="on-off<#if orderType.showStatus==1> background-green<#else> background-red</#if>">
                            <span class="fl">已开启</span>
                            <span class="fl">未开启</span>
                            <a href="javascript:;" class="on-off-btn js-on-btn<#if orderType.showStatus==1><#else> off-btn</#if>" data-name="${orderType.name}" data-id="${orderType.id}"></a>
                        </div>
                    </div>
                    </#list>
                </div>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small m-top js-pay-btn">新增业务</button>
                <div class="add-pay-type">
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small pay-type-name" value="" placeholder="请输入业务名称" data-v-type="required">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-btn-save">保存</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-pay-cancel">取消</button>
                </div>
            </div>
            <div class="pay-picture fr">
                <p>将用于新建工单时选择您开单时业务类型</p>
                <div class="pay-type-picture">
                    <img src="${BASE_PATH}/static/img/page/setting/order-type.png"/>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/js/page/setting/order-type.js?6496ed4bb3bb27bffe7f9a88a8f717ed"></script>
<#include "yqx/layout/footer.ftl">
