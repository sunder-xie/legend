<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wechat-index.css?0d6a9bc326298268ba213d23cd637dce">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head">
            <div class="data-box total green">
                <div class="left">
                    <h1>总用户数</h1>

                    <input type="hidden" name="all" value="${dataDTO.new_user}">
                    <input type="hidden" name="new" value="${dataDTO.cumulate_user}">
                    <h2>${dataDTO.cumulate_user}<small>位</small>
                    </h2>
                </div>
                <div class="right clearfix">
                    <h2 class="back-text clearfix">TOTAL</h2>

                    <div class="circle">
                        <i class="index-icon people middle"></i>
                    </div>
                </div>
            </div><div class="data-box increase blue">
                <div class="left">
                    <h1>新增用户数</h1>

                    <h2>${dataDTO.new_user}<small>位</small>
                    </h2>
                </div>
                <div class="right">
                    <h2 class="back-text clearfix">INCREASE</h2>

                    <div class="circle">
                        <i class="index-icon person middle"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="order-body">
            <h2>文章</h2>

            <div>
                <div id="echart-1" class="echart">

                </div>
                <div class="split-line"></div>
                <div id="echart-2" class="echart">

                </div>
            </div>
        </div>
        <div class="menus">
            <input type="hidden" value="${dataDTO.target_user}" name="target_user">
            <input type="hidden" value="${dataDTO.ori_page_read_user}" name="ori_page_read_user">
            <input value="${dataDTO.share_count}" type="hidden" name="share_count">
            <input value="${dataDTO.add_to_fav_count}" type="hidden" name="add_to_fav_count">

            <a href="${BASE_PATH}/shop/wechat/wechat-info" class="menu-box">
                <div class="index-icon editor"></div>

                <h3>资料维护</h3>
            </a><a href="${BASE_PATH}/shop/wechat/article-list" class="menu-box">
                <div class="index-icon pages"></div>

                <h3>文章管理</h3>
            </a><a href="${BASE_PATH}/shop/wechat/msg-list" class="menu-box">
                <div class="index-icon comment"></div>

                <h3>自动回复</h3>
            </a><a href="${BASE_PATH}/shop/wechat/wifi-manage" class="menu-box">
                <div class="index-icon wifi"></div>

                <h3>设置WIFI</h3>
            </a><a href="${BASE_PATH}/shop/wechat/qrcode-list" class="menu-box">
                <div class="index-icon qrcode"></div>

                <h3>二维码</h3>
            </a><a href="${BASE_PATH}/shop/wechat/wechat-menu" class="menu-box">
                <div class="index-icon menu"></div>

                <h3>菜单配置</h3>
            </a>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/third-plugin/echart/echarts.min.js"></script>
<script src="${BASE_PATH}/static/js/page/wechat/wechat-index.js?3aeda06b744228fcf2a4ce8c7c1f096d"></script>
<#include "yqx/layout/footer.ftl" >