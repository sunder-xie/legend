<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wifi-manage.css?25704101b3cdb27d6c51b5fcd04e2c01"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head">
            <h1 class="headline">设置WIFI
                <a class="yqx-btn yqx-btn-default help" href="${BASE_PATH}/shop/help?id=90">
                    <i class="question"></i><i>帮助中心</i>
                </a>
            </h1>
        </div>
        <div class="order-body">
            <div class="top">
                <div class="wifi-icon">
                    <img src="${BASE_PATH}/static/img/page/wechat/wifi.png">
                </div>
                <div class="wifi-text">
                    <p>填写WiFi账号及密码后，车主关注门店公众号即可知道密码联网啦~</p>

                    <p>让来蹭网的人们，都成为你的粉丝吧~</p>
                </div>
            </div>
            <div class="form">
                <fieldset>
                    <i class="fieldset-title">WIFI名称</i>
                    <input type="hidden" name="id" value="${wechatWifi.id}">
                    <div class="form-item">
                    <input class="yqx-input" name="wifiName"
                           data-v-type="required|maxLength:30"
                           data-label="WIFI名称"
                           value="${wechatWifi.wifiName}">
                    </div>
                    <label>总长度30个字符以内</label>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title">WIFI密码</i>
                    <div class="form-item">
                    <input class="yqx-input" name="wifiPwd"
                           data-v-type="required|maxLength:30|minLength:8"
                           data-label="WIFI密码"
                           value="${wechatWifi.wifiPwd}">
                        </div>
                    <label>请输入8-30个字符密码</label>
                </fieldset>
                <fieldset>
                    <i class="fieldset-title"></i>
                    <button class="yqx-btn yqx-btn-3 js-save">提交</button>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-default js-back">返回</button>
                </fieldset>
            </div>
        </div>

    </div>
</div>

<script src="${BASE_PATH}/static/js/page/wechat/wifi-manage.js?6ba09ef434455757bcee69e4cea63ca4"></script>
<#include "yqx/layout/footer.ftl">

