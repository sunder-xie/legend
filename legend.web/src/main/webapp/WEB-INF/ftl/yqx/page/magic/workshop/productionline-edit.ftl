<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/prorductionLineprocess-edit.css?35f8cc8a2596d1fd74bcc3be038eecda"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->
    <div class="order-right fl">
        <div class="title">
            生产线管理<span>-编辑生产线</span>
        </div>
        <div class="content">
            <div class="show-grid">
                <input type="hidden" name="lineId" value="${productionLineProcessVO.id}" class="lineId"/>

                <div class="form-label">
                    名称：
                </div>
                <div class="form-item name-width">
                    <input type="text" name="lineName" class="yqx-input yqx-input-small"
                           value="${productionLineProcessVO.lineName}" placeholder="">
                </div>
                <div class="form-label">
                    类型：
                </div>
                <div class="form-item type-width">
                    <input type="text" name="" class="yqx-input yqx-input-small js-type-select"
                           value="" data-type="${productionLineProcessVO.type}" placeholder="请选择" disabled>
                    <span class="fa icon-angle-down icon-small"></span>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-label">
                    备注：
                </div>
                <div class="form-item remarks-width">
                    <input type="text" name="remark" class="yqx-input yqx-input-small"
                           value="${productionLineProcessVO.remark}" placeholder="">
                </div>
            </div>
            <!--表格数据-->
            <div id="tbaleCon">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th></th>
                        <th>工序号</th>
                        <th>工序名称</th>
                        <th>工时（分钟）</th>
                        <th>工序牌样式</th>
                    </tr>
                    </thead>
                <#if productionLineProcessVO??>
                    <#if productionLineProcessVO.processVOList??>
                        <#list productionLineProcessVO.processVOList as processRel>
                            <tr class="table-list">
                                <input type="hidden" value="${processRel.id}" class="id"/>
                                <input type="hidden" value="" class="isDeleted"/>
                                <#if processRel.isDeleted=='N'>
                                    <#if processRel.processName=='钣金' && productionLineProcessVO.type==2>
                                        <td><input type="checkbox" name="" class="js-check" checked disabled></td>
                                    <#else>
                                        <td><input type="checkbox" name="" class="js-check" checked></td>
                                    </#if>
                                </#if>
                                <#if processRel.isDeleted=='Y'>
                                    <#if processRel.processName=='钣金' && productionLineProcessVO.type==2>
                                        <td><input type="checkbox" name="" class="js-check" disabled></td>
                                    <#else >
                                        <td><input type="checkbox" name="" class="js-check" ></td>
                                    </#if>
                                </#if>
                                <td class="barCode">${processRel.barCode}</td>
                                <td class="processName">${processRel.processName}</td>
                                <#if processRel.processName=='钣金' && productionLineProcessVO.type==2>
                                    <td><input type="text" value="${processRel.workTime}" class="time-hour" disabled/>
                                    </td>
                                <#else >
                                    <td><input type="text" value="${processRel.workTime}" class="time-hour"/></td>
                                </#if>

                                <td>
                                    <a href="javascript:;" class="color-green js-card-pic">查看图片</a>
                                </td>
                            </tr>
                        </#list>
                    </#if>
                </#if>
                </table>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-return">返回</button>
            </div>
        </div>

    </div>
</div>

<!--查看图片模板-->
<script type="text/html" id="cardPic">
    <img src="<%=cardUrl%>" class="card-photo"/>
    <div class="download-box">
        <a href="<%=cardUrl%>" class="yqx-btn yqx-btn-3 yqx-btn-small js-download" download="工牌.jpg">下载</a>
    </div>
</script>

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/workshop/productionLineprocess-edit.js?76f18e827b3dbc833813c300b06250d0"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">