<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>使用记录打印</title>
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
    <h2 class="text-center bold">${shopName}</h2>
    <p class="text-center">油漆使用记录</p>
    <div class="order-info clearfix">
        <div class="fl">单号：${(paintUseRecord.useRecordSn)!''}</div>
        <div class="fr">领料日期：${(paintUseRecord.warehouseOutTimeStr)!''}</div>
    </div>
    <div class="info">
        <table class="info-table">
            <tr>
                <th>序号</th>
                <th>油漆等级</th>
                <th>油漆类型</th>
                <th>出库质量</th>
                <th>销售价（元/100g）</th>
                <th>金额</th>
                <th>备注</th>
            </tr>
        <#if paintUseRecord??>
            <#if paintUseRecord.paintRecordDetailVoList??>
                <#list paintUseRecord.paintRecordDetailVoList as paintRecordDetail>
                        <tr>
                            <td>
                                <div class="w-260">${paintRecordDetail.seqno}</div>
                            </td>
                            <td>
                                <div class="w-120">${paintRecordDetail.paintLevel}</div>
                            </td>
                            <td> ${paintRecordDetail.paintType}</td>
                            <td>${paintRecordDetail.warehouseOutWeight}</td>
                            <td>￥${paintRecordDetail.salePrice}</td>
                            <td>￥${paintRecordDetail.warehouseOutAmount}</td>
                            <td>${paintRecordDetail.detailRemark}</td>
                        </tr>

                    </#list>
                <tr>
                    <td>合计</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td>￥${paintUseRecord.totalAmount}</td>
                    <td></td>
                </tr>
                </#if>
            </#if>

        </table>
    </div>
    <div class="remarks">备注:${(paintUseRecord.recordRemark)!''}</div>
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