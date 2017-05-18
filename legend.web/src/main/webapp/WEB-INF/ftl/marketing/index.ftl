<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/marketing.css?7786655fd7d9c6c0f4363e56ceb8ab5d"/>
<script type="application/javascript" src="${BASE_PATH}/resources/script/page/marketing/marketing_common.js?2041a738ed7deab02ce7376d6a73bcde"></script>
<#include "marketing/nav.ftl" >
<div class="wrapper">
    <#include "marketing/nav.ftl"/>
    <div class="left_content">
        <div class="qxy_search">
            <div class="mk-sc-box">
                <div class="mk-sc-item">
                    <label>统计时间</label>
                    <input type="text" id="sTime" name="startTime" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'eTime\')}'})" class="xxtxt suggestion_cp w268" placeholder="选择开始时间">
                    <span>至</span>
                    <input type="text" id="eTime" name="endTime" onclick="WdatePicker({minDate:'#F{$dp.$D(\'sTime\')}',maxDate:'2020-10-01'})" placeholder="选择结束时间">
                </div>
                <div class="mk-sc-item">
                    <label>消费金额</label>
                    <input type="text" class="xxtxt suggestion_cp w268" placeholder="请输入金额">
                    <span>至</span>
                    <input type="text" placeholder="请输入金额">
                </div>
                <div class="mk-sc-item">
                    <button class="qxy_btn" id="btnST">统计</button>
                </div>
            </div>
            <div style="clear: both"></div>
            <div class="mk-sc-box">
                <div class="mk-sc-tab">
                    <ul>
                        <li class="tab-sel">全部</li>
                        <li>1000元以下</li>
                        <li>1000元~2000元</li>
                        <li>2000元~3000元</li>
                        <li>3000元~4000元</li>
                        <li>4000元~5000元</li>
                        <li>5000元~7000元</li>
                        <li>7000元~10000元</li>
                        <li>10000元以上</li>
                    </ul>
                </div>
            </div>
        </div>
        <div style="clear: both"></div>
        <div class="mk-sc-box">
            <button class="mk-btn-sms">发短信</button>
        </div>
        <div style="clear: both"></div>
        <div class="mk-dataTable">
            <table>
                <tr class="mk-dt-head">
                    <th data-opt="ccarLicense">车牌</th>
                    <th data-opt="ccarType">车辆型号</th>
                    <th data-opt="cname">车主</th>
                    <th data-opt="cmobile">车主电话</th>
                    <th data-opt="cyearAge">来店年份</th>
                    <th data-opt="ototalAmount">消费金额</th>
                    <th data-opt="olast30daysAmount">近30天消费</th>
                    <th data-opt="olast3monthsAmount">近3个月消费</th>
                    <th data-opt="olastHalfYearAmount">近半年消费</th>
                    <th data-opt="oyearAmount">近一年消费</th>
                    <th data-opt="olastYearAmount">去年消费</th>
                </tr>
                <tbody id="ddFill">

                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript" src="${BASE_PATH}/resources/js/marketing/marketing.js?62d96bcc64a9bc7c586e44cb893806e4"></script>
<#include "layout/footer.ftl" >