<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/paint/inventoryPaintDtl.css?24c81ec5937c766b9bf5ac2dff0a41ab"/>
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
            <h3 class="headline fl">油漆盘点<small>－油漆盘点单详情</small></h3>
            <input class="record_id" type="hidden" value="${(paintInventoryRecordDTO.id)!''}">
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 start -->
        <div class="content">
            <div class="wrap-one">
                <div class="title"><i></i>基本信息</div>
                <div class="form-box">
                    <div class="form-label">
                        盘点编号:
                    </div>
                    <div class="form-item">
                        ${(paintInventoryRecordDTO.recordSn)!''}
                    </div>

                    <div class="form-label">
                        盘点日期:
                    </div>
                    <div class="form-item">
                    ${(paintInventoryRecordDTO.inventoryTimeStr)!''}
                    </div>

                    <div class="form-label">
                        盘点人:
                    </div>
                    <div class="form-item">
                    ${(paintInventoryRecordDTO.paintCheckerName)!''}
                    </div>

                    <div class="form-label">
                        开单人:
                    </div>
                    <div class="form-item">
                    ${(paintInventoryRecordDTO.operatorName)!''}
                    </div>
                </div>
            </div>
            <div class="wrap-two">
            <div class="title"><i></i>油漆商品</div>
            <div class="table-con">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th class="text-l">油漆名称</th>
                        <th class="text-r">
                            <div class="p-right">成本</div>
                        </th>
                        <th class="text-r tc-3">整桶数量</th>
                        <th class="text-r">
                            非整桶总质量
                            <a href="javascript:;" class="show-tips">
                                <img src="${BASE_PATH}/static/img/page/magic/tips.png" class="tips"/>
                                <div class="tips-show">
                                    <div class="tips-box">含桶和搅拌头</div>
                                    <i class="arrow-down"></i>
                                </div>
                            </a>
                        </th>
                        <th class="text-r">非整桶数量</th>
                        <th class="text-r">搅拌头数量</th>
                        <th class="text-r">油漆消耗量</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#if paintInventoryRecordDTO??>
                    <#if paintInventoryRecordDTO.paintInventoryStockDTOList??>
                    <#list paintInventoryRecordDTO.paintInventoryStockDTOList as paintInventoryStock>
                    <tr class="no-border">
                        <td rowspan="2" class="text-l no-border">
                            <div class="max-name-width ellipsis-2 weight black">${paintInventoryStock.goodsName}</div>
                            <div class="max-num-width">零件号:${paintInventoryStock.goodsFormat}</div>
                        </td>
                        <td rowspan="2" class="no-border text-r">
                            <div class="p-right">&yen;${paintInventoryStock.inventoryPrice}/${paintInventoryStock.measureUnit}</div>
                        </td>
                        <td class="text-r">
                            <span>现库存:　</span>${paintInventoryStock.currentStock}
                        </td>
                        <td class="text-r">
                            ${paintInventoryStock.currentNoBucketWeight}g
                        </td>
                        <td class="no-border text-r">
                        ${paintInventoryStock.currentNoBucketNum}
                        </td>
                        <td class="no-border text-r">
                        ${paintInventoryStock.currentStirNum}
                        </td>
                        <td rowspan="2" class="text-r">
                        ${paintInventoryStock.diffStock}g
                        </td>
                    </tr>
                    <tr>
                        <td class="text-r">
                            <span>实盘库存:</span>
                        ${paintInventoryStock.realStock}
                        </td>
                        <td class="text-r">
                        ${paintInventoryStock.realNoBucketWeight}g
                        </td>
                        <td class="no-border text-r">
                        ${paintInventoryStock.realNoBucketNum}
                        </td>
                        <td class="no-border text-r">
                        ${paintInventoryStock.realStirNum}
                        </td>
                    </tr>
                    </#list>
                    </#if>
                    </#if>
                    </tbody>
                </table>
            </div>
            <div class="remarks">
                <div class="form-label">
                    备注:
                </div>
                <div class="form-item">
                ${paintInventoryRecordDTO.paintRemark}
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 js-export-inventoryPaint">导出</button>
                <button class="yqx-btn yqx-btn-1 js-print">打印</button>
                <button class="yqx-btn yqx-btn-1 fr js-return">返回</button>
            </div>
        </div>
        </div>
        <!-- 右侧内容区 end -->
    </div>
</div>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/paint/inventoryPaintDtl.js?28a956a05b6d060f18c14c98cadb5306"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">