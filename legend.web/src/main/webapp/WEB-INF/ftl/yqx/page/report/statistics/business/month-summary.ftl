<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/common/report/report-common.css?c58c9a3ee3acf5c0dbc310373fe3c349">
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/report/statistics/business/summary.css?10ff6052307756faab3b2ab01a02f6f3">
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/report/left-nav.ftl"/>
    <div class="content fl">
        <h1 class="headline">经营分析报告</h1>

        <div class="container summary-main">
            <div class="summary-description">
                <h2 class="font-yahei">一份报告</h2>
                <h3>助您全面<strong class="color-y">掌握门店经营状况</strong></h3>
                <ul class="description-list font-yahei">
                    <li>
                        <i>1</i>配件销量分析
                    </li>
                    <li>
                        <i>2</i>服务销量分析
                    </li>
                    <li>
                        <i>3</i>进店客户分析
                    </li>
                </ul>
                <ul class="description-list font-yahei">
                    <li>
                        <i>4</i>员工绩效分析
                    </li>
                    <li>
                        <i>5</i>其他绩效分析
                    </li>
                </ul>
                <div class="description-info">
                <#if hasReport>
                    <p>
                        <i class="js-show-history show-history">
                            历史报告
                        </i><i class="separate-line"></i><i>
                            由阿里云数据安全专家提供安全加密
                        </i>
                    </p>
                </#if>
                </div>
            </div>
            <div class="summary-op">
                <div class="op-img-box">
                    <img src="${BASE_PATH}/static/img/page/report/statistics/business/summary/tqmall-logo.png">
                </div><h2 class="summary-report-title font-yahei">
                    <#if hasReport>${shopName} ${reportFile.reportDate?substring(5)?number}月经营分析报告<#else>经营分析报告还未生成</#if>
                </h2><#if hasReport><div class="summary-btn-box">
                <input type="hidden" id="reportDate" value="${reportFile.reportDate}"/>
                    <a class="js-preview yqx-btn font-yahei"
                       target="_blank"
                       data-blank="true">预览</a>
                    <a class="js-download yqx-btn font-yahei">下载</a>
                </div></#if>
                <p class="summary-ps font-yahei">注：每月8日凌晨生成上月报告，为保证正常查看报表，<a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/software/Baidu_FoxitReader_CHS_8.0.4.815.exe">请点击下载阅读器</a>
                </p>
            </div>
        </div>
    </div>
</div>

<div class="yqx-dialog history-dialog hide">
    <div class="dialog-title">
        历史报告
    </div>
    <div class="dialog-content">
        <ul class="history-list">
        <#list dateList as date>
            <li>
                <i class="font-yahei <#if date_index==0>has-new-icon</#if>">${date?substring(5)?number}月经营分析报告</i>
                <div class="btn-box fr">
                    <a data-report-date="${date}"
                       target="_blank"
                       data-blank="true" class="js-preview yqx-btn yqx-btn-3">预览</a>
                    <a download=""
                       data-report-date="${date}"
                       class="js-download yqx-btn yqx-btn-3">下载</a>
                </div>
            </li>
        </#list>
        </ul>
    </div>
</div>
<!--信息导出控制模板-->
<#include "yqx/tpl/common/export-confirm-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/report/statistics/business/summary.js?4d5db05519ee4a7d75b2353abc40486d"></script>

<#include "yqx/layout/footer.ftl">