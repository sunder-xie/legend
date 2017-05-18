<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/paint/paintUseRecordDetail.css?c49400ea6a6a0c45c8b581ce033310fc"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fr">
        <!-- 标题 start -->
        <div class="order-head clearfix">
            <h3 class="headline fl">使用记录<small>－使用记录详情</small></h3>
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 start -->
        <div class="content">
            <div class="wrap-one">
                <div class="title"><i></i>基本信息</div>
                <div class="form-box">
                    <div class="show-grid">
                        <div class="form-label">
                            盘点单号:
                        </div>
                        <div class="form-item">
                            ${paintUseRecord.useRecordSn}
                        </div>

                        <div class="form-label">
                            出库日期:
                        </div>
                        <div class="form-item">
                            ${paintUseRecord.warehouseOutTimeStr}
                        </div>

                        <div class="form-label">
                            出库类型:
                        </div>
                        <div class="form-item">
                        ${paintUseRecord.warehouseOutType}
                        </div>
                    </div>
                    <div class="show-grid">
                        <div class="form-label">
                            领料人:
                        </div>
                        <div class="form-item">
                          ${paintUseRecord.painterName}
                        </div>

                        <div class="form-label">
                            出库人:
                        </div>
                        <div class="form-item">
                         ${paintUseRecord.operatorName}
                        </div>
                    </div>
                </div>
            </div>
            <div class="wrap-two">
                <div class="title"><i></i>出库油漆</div>
                <div class="table-con">
                    <table class="yqx-table">
                        <thead>
                        <tr>
                            <th class="text-l">序号</th>
                            <th class="text-l">油漆等级</th>
                            <th class="text-l">油漆类型</th>
                            <th class="text-r">出库质量</th>
                            <th class="text-r">销售价（元/100g)</th>
                            <th class="text-r">金额</th>
                            <th class="text-l">
                                <div class="remarks-list">备注</div>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
<#if paintUseRecord??>
    <#if paintUseRecord.paintRecordDetailVoList??>
        <#list paintUseRecord.paintRecordDetailVoList as paintRecordDetail>
                        <tr class="has-border">
                            <td class="text-l">
                                ${paintRecordDetail.seqno}
                            </td>
                            <td class="text-l">
                            ${paintRecordDetail.paintLevel}
                            </td>
                            <td class="text-l">
                            ${paintRecordDetail.paintType}
                            </td>
                            <td class="text-r">
                            ${paintRecordDetail.warehouseOutWeight}g
                            </td>
                            <td class="text-r">
                            &yen;${paintRecordDetail.salePrice}
                            </td>
                            <td class="text-r money-font">
                            &yen;${paintRecordDetail.warehouseOutAmount}
                            </td>
                            <td class="text-l">
                                <div class="remarks-list">${paintRecordDetail.detailRemark}</div>
                            </td>
                        </tr>
        </#list>
    </#if>
</#if>
                        </tbody>
                    </table>
                </div>
                <div class="money-box">
                    油漆使用金额合计:<span class="money-highlight">¥</span> <span class="money-font">${paintUseRecord.totalAmount}</span>
                </div>
                <div class="remarks">
                    <div class="form-label">
                        备注:
                    </div>
                    <div class="form-item">
                        ${paintUseRecord.recordRemark}
                    </div>
                </div>
                <div class="btn-box">
                    <button class="yqx-btn yqx-btn-3 js-export">导出</button>
                    <button class="yqx-btn yqx-btn-1 js-print">打印</button>
                    <button class="yqx-btn yqx-btn-1 fr js-return">返回</button>
                </div>
            </div>
        </div>
        <!-- 右侧内容区 end -->
    </div>
</div>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/paint/paintUseRecordDetail.js?5a69b491c0d1c6197fe63957ca0e302f"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">