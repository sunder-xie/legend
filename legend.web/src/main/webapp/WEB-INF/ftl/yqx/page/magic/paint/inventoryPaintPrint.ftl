<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>维修施工单</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/base.css?8bb0f9a84cfb070249dcc1186f9f6a25"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print_common.css?e174653fe93fff7de684a409310c97d7"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/paint/print.css?0b1e4a946a6448fd002390eb8ce29252"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/order/order_print_blank.css?f9cc77af03e07dee8e01b589d4a298f8"/>
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</head>
<body>
<div class="w980">
    <h2 class="text-center bold">${(shop.name)!''}</h2>
    <p class="text-center">油漆盘点表</p>
    <div class="order-info clearfix">
        <div class="fl">单号：${(paintInventoryRecordDTO.recordSn)!''}</div>
        <div class="fr">盘点日期：${(paintInventoryRecordDTO.inventoryTimeStr)!''}</div>
    </div>
    <div class="info">
        <table class="info-table">
            <tr>
                <th>油漆名称</th>
                <th>零件号</th>
                <th></th>
                <th>整桶数量</th>
                <th>非整桶总质量</th>
                <th>非整桶数量</th>
                <th>搅拌头数量</th>
                <th>油漆消耗量</th>
                <th>油漆消耗成本</th>
            </tr>
        <#if paintInventoryRecordDTO??>
            <#if paintInventoryRecordDTO.paintInventoryStockDTOList??>
                <#list paintInventoryRecordDTO.paintInventoryStockDTOList as paintInventoryStock>
                    <tr>
                        <td rowspan="2">
                            <div class="w-260">${paintInventoryStock.goodsName}</div>
                        </td>
                        <td rowspan="2">
                            <div class="w-120">${paintInventoryStock.goodsFormat}</div>
                        </td>
                        <td>现库存</td>
                        <td>${paintInventoryStock.currentStock}</td>
                        <td>${paintInventoryStock.currentNoBucketWeight}g</td>
                        <td>${paintInventoryStock.currentNoBucketNum}</td>
                        <td>${paintInventoryStock.currentStirNum}</td>
                        <td rowspan="2">${paintInventoryStock.diffStock}<#if paintInventoryStock.diffStock != ''>g</#if></td>
                        <td rowspan="2"><#if paintInventoryStock.diffStockPrice != ''>￥</#if>${paintInventoryStock.diffStockPrice}</td>
                    </tr>
                    <tr>
                        <td>实盘库存</td>
                        <td>${paintInventoryStock.realStock}</td>
                        <td>${paintInventoryStock.realNoBucketWeight}<#if paintInventoryStock.realNoBucketWeight != ''>g</#if></td>
                        <td>${paintInventoryStock.realNoBucketNum}</td>
                        <td>${paintInventoryStock.realStirNum}</td>
                    </tr>
                </#list>
            </#if>
        </#if>

        </table>
    </div>
    <div class="remarks">备注:${(paintInventoryRecordDTO.paintRemark)!''}</div>
    <div class="sign-box">
        <div class="col-3">
            <div class="sign-text">
                <span>盘点人签字：</span>
                <p></p>
            </div>
            <div class="sign-text">
                <span>签字时间：</span>
                <p>　　年　　月　　日</p>
            </div>
        </div>
        <div class="col-3">
            <div class="sign-text">
                <span>仓库主管签字：</span>
            </div>
            <div class="sign-text">
                <span>签字时间：</span>
                <p>　　年　　月　　日</p>
            </div>
        </div>
        <div class="col-3">
            <div class="sign-text">
                <span>财务主管签字：</span>
            </div>
            <div class="sign-text">
                <span>签字时间：</span>
                <p>　　年　　月　　日</p>
            </div>
        </div>
    </div>
</div>
</body>
</html>