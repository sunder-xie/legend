<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/paint/paintUseRecordAdd.css?16916bbc7b530b87b5d3e6666ab4f023"/>
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
            <h3 class="headline fl">使用记录<small>－新增使用记录</small></h3>
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 start -->
        <div class="content">
            <div class="title"><i></i>基本信息</div>
            <div class="form-box" id="formData">
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label">
                            使用单号:
                        </div>
                        <div class="form-item">
                            <input type="text" name="useRecordSn" class="yqx-input yqx-input-small" value="${useRecordSn}" placeholder="" readonly="" disabled>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            出库日期:
                        </div>
                        <div class="form-item">
                            <input type="text" name="warehouseOutTime" class="yqx-input yqx-input-small datepicker" value="" placeholder="请选择"  data-v-type="required">
                            <span class="fa icon-calendar icon-small"></span>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            出库类型:
                        </div>
                        <div class="form-item">
                            <input type="text" name="warehouseOutType" class="yqx-input yqx-input-small" value="内部领用" placeholder="" data-v-type="required" disabled>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            领料人:
                        </div>
                        <div class="form-item">
                            <input type="hidden" value="" name="painterId"/>
                            <input type="text" name="painterName" class="yqx-input yqx-input-small js-packing" value="" placeholder="请选择" data-v-type="required">
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            出库人:
                        </div>
                        <div class="form-item">
                            <input type="hidden" name="operatorId" value="${operatorId}"/>
                            <input type="text" name="operatorName" class="yqx-input yqx-input-small" value="${operatorName}" placeholder="" disabled data-v-type="required">
                            <span class="fa icon-angle-down icon-small"></span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="title"><i></i>出库油漆</div>
            <div class="table-con">
                <table class="yqx-table">
                    <thead>
                    <tr>
                        <th class="text-l">油漆等级</th>
                        <th class="text-l">油漆类型</th>
                        <th class="text-c">出库质量</th>
                        <th class="text-l">销售价</th>
                        <th class="text-l">备注</th>
                        <th class="text-c">操作</th>
                    </tr>
                    </thead>
                    <tbody id="tableCon">
                    <tr class="table-list">
                        <td class="text-l">
                            <div class="form-item">
                                <input type="text" name="paintLevel" class="yqx-input yqx-input-small js-paint-level" value="" placeholder="请选择" data-v-type="required">
                                <span class="fa icon-angle-down icon-small"></span>
                            </div>
                        </td>
                        <td class="text-l">
                            <div class="form-item">
                                <input type="text" name="paintType" class="yqx-input yqx-input-small js-paint-type" value="" placeholder="请选择" data-v-type="required">
                                <span class="fa icon-angle-down icon-small"></span>
                            </div>
                        </td>
                        <td class="text-c">
                            <div class="form-item">
                                <input type="text" name="warehouseOutWeight" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="required | number">
                                <span class="fa icon-small">克</span>
                            </div>
                        </td>
                        <td class="text-c">
                            <div class="form-item">
                                <input type="text" name="salePrice" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="required | number">
                                <span class="fa icon-small sale-unit">元/100克</span>
                            </div>
                        </td>
                        <td class="text-l">
                            <div class="form-item">
                                <input type="text" name="detailRemark" class="yqx-input yqx-input-small" value="" placeholder="">
                            </div>
                        </td>
                        <td class="text-c"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-add-paint">添加</button>
            <div class="remarks">
                <div class="form-label">
                    备注:
                </div>
                <div class="form-item">
                    <textarea class="yqx-textarea" name="recordRemark" id="" cols="100" rows="3" placeholder="请填写备注信息" data-v-type="maxLength:200"></textarea>
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-2 js-confirm">提交</button>
                <button class="yqx-btn yqx-btn-1 fr js-return">返回</button>
            </div>
        </div>
        <!-- 右侧内容区 end -->
    </div>
</div>

<script type="text/html" id="tableTpl">
    <tr class="table-list">
        <td class="text-l">
            <div class="form-item">
                <input type="text" name="paintLevel" class="yqx-input yqx-input-small js-paint-level" value="" placeholder="请选择" data-v-type="required">
                <span class="fa icon-angle-down icon-small"></span>
            </div>
        </td>
        <td class="text-l">
            <div class="form-item">
                <input type="text" name="paintType" class="yqx-input yqx-input-small js-paint-type" value="" placeholder="请选择" data-v-type="required">
                <span class="fa icon-angle-down icon-small"></span>
            </div>
        </td>
        <td class="text-c">
            <div class="form-item">
                <input type="text" name="warehouseOutWeight" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="required | number">
                <span class="fa icon-small">克</span>
            </div>
        </td>
        <td class="text-c">
            <div class="form-item">
                <input type="text" name="salePrice" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="required | number">
                <span class="fa icon-small sale-unit">元/100克</span>
            </div>
        </td>
        <td class="text-l">
            <div class="form-item">
                <input type="text" name="detailRemark" class="yqx-input yqx-input-small" value="" placeholder="">
            </div>
        </td>

        <td class="text-c">
            <a href="javascript:;" class="delete js-delete">删除</a>
        </td>
    </tr>
</script>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/paint/paintUseRecordAdd.js?a4d6307d2ae2f505950a7c4b0ce9969a"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">