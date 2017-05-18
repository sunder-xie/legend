<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/workshop/workshop-common.css?81df30b71cef68b65bf9855b2f6721d3"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/goods/paintStock.css?18177a6a56b9c52c5b7380d13ec1da5b"/>
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
            <h3 class="headline fl">油漆库存</h3>
            <div class="total-title fr"><img src="${BASE_PATH}/static/img/page/magic/money-ico.png"/>库存总金额:&yen;<span class="js-stock-money"></span></div>
        </div>
        <!-- 标题 end -->
        <!-- 右侧内容区 start -->
        <!-- form start -->
        <div class="form-box" id="formData">
            <div class="form-item">
                <input type="hidden" value="" name="search_brandId" class="brand-id"/>
                <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-brand" value="" placeholder="选择配件品牌">
                <span class="fa icon-angle-down icon-small"></span>
            </div>
            <div class="form-item">
                <input type="text" name="search_goodsNameLike" class="yqx-input yqx-input-small" value="" placeholder="油漆名称">
            </div>
            <div class="form-item">
                <input type="text" name="search_goodsFormatLike" class="yqx-input yqx-input-small" value="" placeholder="零件号">
            </div>
            <div class="form-item">
                <input type="text" name="search_depotLike" class="yqx-input yqx-input-small" value="" placeholder="仓库货位">
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
        </div>
        <!-- form end -->
        <!-- list start -->
        <div class="table-btn">
            <div class="fl select-all-box">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-stock-select"/> 盘点全部库存(共<span class="js-all-stock">0</span>项)</button>
                <input type="hidden" value="" class="goods-ids"/>
            </div>
            <button class="yqx-btn yqx-btn-2 yqx-btn-small fl grey-color js-inventory" disabled>库存预盘点</button>
            <span class="stock-num">已选择 <i class="select-num js-select-num">0</i> 项</span>
            <a href="javascript:;" class="fr export js-export-paintStock"><i class="icon-signout"></i>导出Excel</a>
        </div>
        <!-- 表格容器 start -->
        <div class="table-list"  id="tableCon"></div>
        <!-- 表格容器 end -->

        <!-- 分页容器 start -->
        <div class="yqx-page" id="paging"></div>
        <!-- 分页容器 end -->
        <!-- list end -->
        <!-- 右侧内容区 end -->
        </div>
    </div>

<script type="text/html" id="tableTpl">
    <%if(json.success && json.data != ''){%>
    <table class="yqx-table yqx-table-hover yqx-table-link" id="tableTest">
        <thead>
        <tr>
            <th class="text-c">
                <div class="p-left"><input type="checkbox" class="js-select-all"/>
            </th>
            <th class="text-l">序号</th>
            <th class="text-l">油漆名称</th>
            <th class="text-l">适配车型</th>
            <th class="text-r">整桶数量</th>
            <th class="text-r">非整桶总质量</th>
            <th class="text-r">非整桶数量</th>
            <th class="text-r">搅拌头数量</th>
            <th class="text-r">库存总成本</th>
            <th class="text-r">
                <div class="p-right">仓库货位</div>
            </th>
        </tr>
        </thead>
        <tbody>
        <%for(var i=0;i< json.data.content.length;i++){%>
        <%var item = json.data.content[i]%>
        <tr data-goods-id = '<%=item.id%>'>
            <td class="text-c">
                <div class="p-left"><input type="checkbox" class="js-select"/></div>
            </td>
            <td class="text-l"><%=json.data.size*(json.data.number)+i+1%></td>
            <td class="text-l">
                <div class="max-name-width ellipsis-2 js-show-tips weight black">
                    <%if(item.onsaleStatus == 0){%>
                    <span>(下架)</span>
                    <%}%>
                    <%=item.name%>
                </div>
                <div class="max-width js-show-tips">配件编号:<%=item.goodsSn%></div>
                <div class="max-width js-show-tips">零件号:<%=item.format%></div>
            </td>
            <td class="text-l">
                <div class="max-width-other js-show-tips"><%=item.carInfoStr%></div>
            </td>
            <td class="text-r"><%=item.stock%></td>
            <td class="text-r"><%=item.noBucketWeight%></td>
            <td class="text-r"><%=item.noBucketNum%></td>
            <td class="text-r"><%=item.stirNum%></td>
            <td class="text-r money-font">&yen;<%=item.totalStockAmount || 0%></td>
            <td class="text-r">
                <div class="p-right"><%=item.depot%></div>
            </td>
        </tr>
        <%}%>
        <%}%>
        </tbody>
    </table>
</script>

<script type="text/html" id="stepDialog">
    <div class="dialog">
        <div class="dialog-title">油漆盘点流程指导</div>
        <div class="dialog-con">
            <div class="step-box">
                <p class="step-title">第一步</p>
                <p class="step-text">勾选要盘点的油漆商品，点击<span>“库存盘点”</span></p>
            </div>
            <div class="step-box">
                <p class="step-title">第二步</p>
                <p class="step-text"><span>添加</span>油漆商品，<span>保存</span>盘点单草稿，并<span>打印</span>草稿</p>
            </div>
            <div class="step-box">
                <p class="step-title">第三步</p>
                <p class="step-text">持纸质盘点单到油漆库存盘点，<span>手写记录</span>实盘库存</p>
            </div>
            <div class="step-box">
                <p class="step-title">第四步</p>
                <p class="step-text">登录系统，打开草稿状态的盘点单，将实盘库存录入系统</p>
            </div>
            <div class="step-box step-5">
                <p class="step-title">第五步</p>
                <p class="step-text"><span>生成</span>盘点单，盘点完成</p>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small step-btn js-know">我知道了</button>
                <input type="checkbox" name="noTips"/> 不再提示
            </div>
        </div>
    </div>
</script>

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/magic/goods/paintStock.js?243d7c79c2f3b8a7fb269c562dadcc2c"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">