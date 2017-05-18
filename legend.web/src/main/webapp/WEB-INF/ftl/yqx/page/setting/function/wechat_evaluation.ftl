<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/on-off-common.css?6fb5ea71934b5a899146d7872c8b9337"/>
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
            <h3 class="headline">微信评论</h3>
        </div>
        <div class="content clearfix">
            <div class="title">
                <i></i>管理微信的服务评论功能
            </div>
            <div class="left-box fl">
                <div class="btn-group clearfix">
                    <img src="${BASE_PATH}/static/img/page/setting/weixin.png" class="fl"/>
                    <p class="fl">评论开关</p>
                    <div class="on-off fl <#if !evaluationSwitch>red-color</#if>">
                        <span class="fl">已开启</span>
                        <span class="fl">未开启</span>
                        <a href="javascript:;" class="on-off-btn js-on-btn <#if !evaluationSwitch>off-btn</#if>"></a>
                    </div>
                </div>
            </div>
            <div class="right-box p-top fr">
                <p>开启评论后</p>
                <p>用户可针对您的服务给出评论，如果关闭则无法评论</p>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/setting/function/wechat_evaluation.js?94759002445460015c5e45b07c616652"></script>
<#include "yqx/layout/footer.ftl">