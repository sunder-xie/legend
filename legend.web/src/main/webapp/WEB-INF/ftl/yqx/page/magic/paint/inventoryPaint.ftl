<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/paint/inventoryPaint.css?5161e3448ac769b164bf540e4853179a"/>
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
            <#if paintInventoryRecordDTO??>
                <h3 class="headline fl">油漆盘点<small>－编辑油漆盘点单</small></h3>
            <#else>
                <h3 class="headline fl">油漆盘点<small>－新建油漆盘点单</small></h3>
            </#if>

            <input  class="record_id" type="hidden" value="${(paintInventoryRecordDTO.id)!''}"/>
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 start -->
        <div class="content">
            <div class="title"><i></i>基本信息</div>
            <div class="form-box" id="formData">
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            盘点编号:
                        </div>
                        <div class="form-item">
                            <#if paintInventoryRecordDTO??>
                                <input type="text" name="recordSn" class="yqx-input yqx-input-small" value="${paintInventoryRecordDTO.recordSn}" placeholder="" readonly="" disabled>
                            <#else>
                                <input type="text" name="recordSn" class="yqx-input yqx-input-small" value="${inventorySN!''}" placeholder="" readonly="" disabled>
                            </#if>
                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            盘点日期:
                        </div>
                        <div class="form-item">
                            <#if paintInventoryRecordDTO??>
                                <input type="text" name="inventoryTimeStr" class="yqx-input yqx-input-small" value="${paintInventoryRecordDTO.inventoryTimeStr}" placeholder="" readonly="" disabled>
                            <#else>
                                <input type="text" name="inventoryTimeStr" class="yqx-input yqx-input-small" value="${inventoryDate!''}" placeholder="" readonly="" disabled>
                            </#if>

                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            盘点人:
                        </div>
                        <div class="form-item">
                            <#if paintInventoryRecordDTO??>
                                <input type="text" name="paintCheckerName" class="yqx-input yqx-input-small js-select" value="${paintInventoryRecordDTO.paintCheckerName}" placeholder="请选择" data-v-type="required">
                                <input type="hidden" name="paintCheckerId" class="paintCheckerId" value=""/>
                                <span class="fa icon-angle-down icon-small"></span>
                            <#else>
                                <input type="text" name="paintCheckerName" class="yqx-input yqx-input-small js-select" value="" placeholder="请选择" readonly="" data-v-type="required">
                                <span class="fa icon-angle-down icon-small"></span>
                                <input type="hidden" name="paintCheckerId" class="paintCheckerId" value=""/>
                            </#if>

                        </div>
                    </div>
                    <div class="col-6">
                        <div class="form-label form-label-must">
                            开单人:
                        </div>
                        <div class="form-item">
                            <#if paintInventoryRecordDTO??>
                                <input type="text" name="operatorName" class="yqx-input yqx-input-small" value="${paintInventoryRecordDTO.operatorName}" placeholder="" readonly="" disabled>
                            <#else>
                                <input type="text" name="operatorName" class="yqx-input yqx-input-small" value="${userInfo.name}" placeholder="" readonly="" disabled>
                            </#if>

                        </div>
                    </div>
                </div>
            </div>
            <div class="title"><i></i>油漆商品</div>
            <div class="table-con">
                <table class="yqx-table" id="listData">
                    <thead>
                    <tr>
                        <th class="text-l">油漆名称</th>
                        <th class="text-c">成本</th>
                        <th class="text-c">整桶数量</th>
                        <th class="text-c">非整桶总质量
                            <a href="javascript:;" class="show-tips">
                                <img src="${BASE_PATH}/static/img/page/magic/tips.png" class="tips"/>
                                <div class="tips-show">
                                    <div class="tips-box">含桶和搅拌头</div>
                                    <i class="arrow-down"></i>
                                </div>
                            </a>
                        </th>
                        <th class="text-c">非整桶数量</th>
                        <th class="text-c">搅拌头数量</th>
                        <th class="text-c">油漆消耗量</th>
                        <th class="text-c">操作</th>
                    </tr>
                    </thead>

                    <!--编辑页面-->
                    <#if paintInventoryRecordDTO?? && paintInventoryRecordDTO.paintInventoryStockDTOList??>
                    <tbody id="editTableCon" class="list-data">
                    <#list paintInventoryRecordDTO.paintInventoryStockDTOList as paintInventoryStock>
                        <tr>
                            <td colspan="8" class="no-border"></td>
                        </tr>
                        <tr class="has-border table-form total-list">
                            <input type="hidden" name="netWeight" value="${paintInventoryStock.netWeight}" class="netWeight"/>
                            <input type="hidden" name="bucketWeight" value="${paintInventoryStock.bucketWeight}" class="bucketWeight"/>
                            <input type="hidden" name="stirWeight"value="${paintInventoryStock.stirWeight}" class="stirWeight"/>
                            <input type="hidden" name="goodsId"value="${paintInventoryStock.goodsId}" class="goodsId"/>
                            <input type="hidden" name="id"value="${paintInventoryStock.id}" class="id"/>
                            <td rowspan="2" class="text-l">
                                <div class="max-name-width ellipsis-2 weight black goodsName">${paintInventoryStock.goodsName}</div>
                                <div class="max-num-width">零件号:<span class="goodsFormat">${paintInventoryStock.goodsFormat}</span></div>
                            </td>
                            <td rowspan="2" class="no-border text-c">
                                <div class="form-item w-100">
                                    <input type="text" name="inventoryPrice" class="yqx-input yqx-input-icon yqx-input-small" value="${paintInventoryStock.inventoryPrice}" placeholder="" readonly="" disabled>
                                    <span class="fa icon-small measureUnit">${paintInventoryStock.measureUnit}</span>
                                </div>
                            </td>
                            <td class="text-c">
                                　现库存:
                                <input type="text" name="currentStock" class="yqx-input yqx-input-small w-40 js-" value="${paintInventoryStock.currentStock}" placeholder="" readonly="" disabled>
                            </td>
                            <td class="text-c">
                                <div class="form-item w-100">
                                    <input type="text" name="currentNoBucketWeight" class="yqx-input yqx-input-icon yqx-input-small" value="${paintInventoryStock.currentNoBucketWeight}" placeholder="" readonly="" disabled>
                                    <span class="fa icon-small">克</span>
                                </div>
                            </td>
                            <td class="no-border text-c">
                                <input type="text" name="currentNoBucketNum" class="yqx-input yqx-input-small w-55" value="${paintInventoryStock.currentNoBucketNum}" placeholder="" readonly="" disabled>
                            </td>
                            <td class="no-border text-c">
                                <input type="text" name="currentStirNum" class="yqx-input yqx-input-small w-55" value="${paintInventoryStock.currentStirNum}" placeholder="" readonly="" disabled>
                            </td>
                            <td rowspan="2" class="text-c">

                                <input type="text" name="diffStock" class="yqx-input yqx-input-small w-55 aWeight" value="${paintInventoryStock.diffStock}" placeholder="" readonly="" disabled>
                                <input type="hidden" name="diffStockPrice" class="diffStockPrice"/>
                                <input type="hidden" name="totalStockAmount" class="totalStockAmount"/>
                            </td>
                            <td rowspan="2" class="no-border text-c">
                                <a href="javascript:;" class="delete js-delete">删除</a>
                            </td>
                        </tr>
                        <tr class="table-form">

                                    <td>
                                        实盘库存:
                                        <div class="form-item w-40">
                                            <input type="text" name="realStock" class="yqx-input yqx-input-small" value="${paintInventoryStock.realStock}" placeholder="" data-v-type="required | integer">
                                        </div>
                                    </td>
                                    <td class="text-c">
                                        <div class="form-item w-100">
                                            <input type="text" name="realNoBucketWeight" class="yqx-input yqx-input-small" value="${paintInventoryStock.realNoBucketWeight}" placeholder="" data-v-type="required | integer">
                                            <span class="fa icon-small">克</span>
                                        </div>
                                    </td>
                                    <td class="no-border">
                                        <div class="form-item w-55">
                                            <input type="text" name="realNoBucketNum" class="yqx-input yqx-input-small" value="${paintInventoryStock.realNoBucketNum}" placeholder="" data-v-type="required | integer">
                                        </div>
                                    </td>
                                    <td class="no-border">
                                        <div class="form-item w-55">
                                            <input type="text" name="realStirNum" class="yqx-input yqx-input-small" value="${paintInventoryStock.realStirNum}" placeholder="" data-v-type="required | integer">
                                        </div>
                                    </td>

                        </tr>

                    </#list>
                    </tbody>
                    <#else>
                    <tbody id="tableCon" class="list-data">

                    </tbody>
                    </#if>

                </table>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small" id="goodsAddBtn">添加油漆</button>
            <div class="remarks">
                <div class="form-label">
                    备注:
                </div>
                <div class="form-item">
                    <textarea class="yqx-textarea" name="paintRemark"  id="" cols="100" rows="3" placeholder="请填写备注信息" data-v-type="maxLength:200">${paintInventoryRecordDTO.paintRemark}</textarea>
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 js-saveDrafts">保存草稿</button>
                <button class="yqx-btn yqx-btn-2 js-save">生成盘点单</button>
                <#if paintInventoryRecordDTO??>
                <button class="yqx-btn yqx-btn-1 js-print">打印</button>
                </#if>
                <button class="yqx-btn yqx-btn-1 fr js-return">返回</button>
                <p>油漆盘点建议：保存草稿后，打印盘点单，手动填写实盘库存；再次编辑盘点单，录入系统，生成盘点单！</p>
            </div>
        </div>
        <!-- 右侧内容区 end -->
    </div>
</div>

<script type="text/html" id="tableTpl">
    <%if(json.data){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <tr>
        <td colspan="8" class="no-border"></td>
    </tr>
    <tr class="has-border table-form total-list">
        <input type="hidden" name="netWeight" value="<%=item.netWeight%>" class="netWeight"/>
        <input type="hidden" name="bucketWeight" value="<%=item.bucketWeight%>" class="bucketWeight"/>
        <input type="hidden" name="stirWeight"value="<%=item.stirWeight%>" class="stirWeight"/>
        <input type="hidden" name="goodsId"value="<%=item.id%>"/>
        <input type="hidden" name="goodsSn"value="<%=item.goodsSn%>"/>
        <td rowspan="2" class="text-l">
            <div class="max-name-width ellipsis-2 weight black goodsName"><%=item.name%></div>
            <div class="max-num-width">零件号:<span class="goodsFormat"><%=item.format%></span></div>
        </td>
        <td rowspan="2" class="no-border text-c">
            <div class="form-item w-100">
                <input type="text" name="inventoryPrice" class="yqx-input  yqx-input-icon yqx-input-small" value="<%=item.inventoryPrice%>" placeholder="" readonly="" disabled>
                <span class="fa icon-small measureUnit"><%=item.measureUnit%></span>
            </div>
        </td>
        <td class="text-c">
            　现库存:
            <input type="text" name="currentStock" class="yqx-input yqx-input-small w-40 js-" value="<%=item.stock%>" placeholder="" readonly="" disabled>
        </td>
        <td class="text-c">
            <div class="form-item w-100">
                <input type="text" name="currentNoBucketWeight" class="yqx-input yqx-input-icon yqx-input-icon yqx-input-small" value="<%=item.noBucketWeight%>" placeholder="" readonly="" disabled>
                <span class="fa icon-small">克</span>
            </div>
        </td>
        <td class="no-border text-c">
            <input type="text" name="currentNoBucketNum" class="yqx-input yqx-input-small w-55" value="<%=item.noBucketNum%>" placeholder="" readonly="" disabled>
        </td>
        <td class="no-border text-c">
            <input type="text" name="currentStirNum" class="yqx-input yqx-input-small w-55" value="<%=item.stirNum%>" placeholder="" readonly="" disabled>
        </td>
        <td rowspan="2" class="text-c">
            <input type="text" name="diffStock" class="yqx-input yqx-input-small w-55 aWeight" value="" placeholder="" readonly="" disabled>
            <input type="hidden" name="diffStockPrice" value="" class="diffStockPrice"/>
            <input type="hidden" name="totalStockAmount" value="" class="totalStockAmount"/>
        </td>
        <td rowspan="2" class="no-border text-c">
            <a href="javascript:;" class="delete js-delete">删除</a>
        </td>
    </tr>
    <tr class="table-form">
        <td>
            实盘库存:
            <div class="form-item w-40">
                <input type="text" name="realStock" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="required | integer">
            </div>
        </td>
        <td class="text-c">
            <div class="form-item w-100">
                <input type="text" name="realNoBucketWeight" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="" data-v-type="required | integer">
                <span class="fa icon-small">克</span>
            </div>
        </td>
        <td class="no-border">
            <div class="form-item w-55">
                <input type="text" name="realNoBucketNum" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="required | integer">
            </div>
        </td>
        <td class="no-border">
            <div class="form-item w-55">
                <input type="text" name="realStirNum" class="yqx-input yqx-input-small" value="" placeholder="" data-v-type="required | integer">
            </div>
        </td>
    </tr>
    <%}}%>
</script>

<!-- 添加物料模版 -->
<#include "yqx/tpl/common/add-stock-accessories-tpl.ftl">

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/paint/inventoryPaint.js?5afb905342db9482e026b91aa6a929de"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">