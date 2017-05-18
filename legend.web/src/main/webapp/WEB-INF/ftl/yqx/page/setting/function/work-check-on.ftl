<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/work-check-on.css?dd3ea85d59be5c249cf95055df427dab"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/style/page/member/bootstrap-clockpicker.min.css"/>
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
            <h3 class="headline">上下班设置</h3>
        </div>
        <div class="content">
            <h3>设置时间，让员工在App上打卡记录上班时间</h3>
            <div id="signInfoConfigDiv" class="form-data">
                <div class="show-grid">
                    <i class="on-time"></i>
                    <div class="form-label">
                        上班时间:
                    </div>
                    <div class="form-item field_box clockpicker"  data-placement="bottom" data-align="top" data-autoclose="true">
                        <input type="text" name="signInTime" class="yqx-input yqx-input-icon yqx-input-small" value="${signInTime}" placeholder="请选择上班时间" readonly>
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
                <div class="show-grid">
                    <i class="off-time"></i>
                    <div class="form-label">
                        下班时间:
                    </div>
                    <div class="form-item field_box clockpicker"  data-placement="bottom" data-align="top" data-autoclose="true">
                        <input type="text" name="signOffTime" class="yqx-input yqx-input-icon yqx-input-small" value="${signOffTime}" placeholder="请选择下班时间" readonly>
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
            <#if isBpShare>
                <div class="show-grid">
                    <i class="rest-time"></i>
                    <div class="form-label">
                        午休时间:
                    </div>
                    <div class="form-item field_box clockpicker" data-placement="bottom" data-align="top"
                         data-autoclose="true">
                        <input type="text" name="noonBreakStartTime" class="yqx-input yqx-input-icon yqx-input-small" value="${noonBreakStartTime}"
                               placeholder="午休开始时间" readonly>
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                    至
                    <div class="form-item field_box clockpicker" data-placement="bottom" data-align="top"
                         data-autoclose="true">
                        <input type="text" name="noonBreakEndTime" class="yqx-input yqx-input-icon yqx-input-small" value="${noonBreakEndTime}"
                               placeholder="午休结束时间" readonly>
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
            </#if>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small save-btn js-save">保存</button>
            </div>
        </div>
    </div>
</div>

<script src="${BASE_PATH}/static/js/page/setting/work-check-on.js?19f80e9fa4fb518280a5a8e501ecdf35"></script>
<script src="${BASE_PATH}/resources/script/page/member/bootstrap-clockpicker.min.js?d25d9e41f486972994261114f443e372"></script>

<#include "yqx/layout/footer.ftl">
