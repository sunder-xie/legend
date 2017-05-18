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
            <h3 class="headline">委托设置</h3>
        </div>
        <div class="content clearfix">
            <div class="title">
                <i></i>门店委托管理
            </div>
            <div class="left-box fl">
                <div class="btn-group clearfix">
                    <img src="${BASE_PATH}/static/img/page/setting/spurt.png" class="fl"/>
                    <p class="fl">加入钣喷委托</p>
                    <div class="on-off fl <#if !isJoin>red-color</#if>">
                        <span class="fl">已开启</span>
                        <span class="fl">未开启</span>
                        <a href="javascript:;" class="on-off-btn js-on-btn <#if !isJoin>off-btn</#if>"></a>
                    </div>
                </div>
            </div>
            <div class="right-box p-top fr">
                <p>加入委托之后</p>
                <p>工单中的钣金、喷漆服务即可委托给淘汽云修钣喷中心完成</p>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/setting/function/share-join.js?90c057f75738da92a2726640b7c8267d"></script>
<#include "yqx/layout/footer.ftl">