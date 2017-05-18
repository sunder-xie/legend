<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>小票打印</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print.css?0c3994b5134160a03fdfc1ddc44657ff">
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/print/print-receipt.css?9a97d3c1be50f24441c0a27e36dbc34c">

    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script>
        $(document).ready(function () {
            window.print();
        });
    </script>
</head>
<#--小票试打印-->
<section class="print-box receipt-box">
    <header>
        <h2>杭州淘汽云修</h2>
        <h3>结算单</h3>
    </header>
    <ul class="info-box box">
        <li>
            <i style="width: 15mm;">车牌号：</i><i>
            <p>粤D12356</p>
        </i>
        </li>
        <li><i style="width: 15mm;">会员卡：</i><i>123***89</i></li>
        <li><i style="width: 19mm;">工单编号：</i><i style="font-size: 9pt;">C000011612300008</i></li>
        <li>服务顾问：测试</li>
    </ul>
    <table class="box">
        <thead>
        <tr>
            <th class="name">名称</th>
            <th style="width: 9mm">数量</th>
            <th style="width: 16mm">总计(¥)</th>
        </tr>
        </thead>
        <tbody><tr class="padding-top padding-bottom">
            <td>精品洗车</td>
            <td>1 </td>
            <td>0</td>
        </tr>

        <tr class="padding-top box border-top-dashed" style="border-bottom: 0;">
            <td>总优惠</td>
            <td></td>
            <td>
                0
            </td>
        </tr>
        <tr class="padding-bottom">
            <td>应收金额</td>
            <td></td>
            <td>0</td>
        </tr>

        </tbody></table>
    <ul class="border-top box">
        <li><i>打印时间：</i><i>01-05 09:56</i></li>
        <li>联系电话：87612665</li>
        <li id="address"><i style="width: 11mm;">地址：</i><i>浙江杭州余杭区五常街道联胜路7号675607</i></li>
    </ul>
    <div class="code-box">
        <img src="http://ddlwechat.img-cn-hangzhou.aliyuncs.com/qrcode/thumb_p_148169861813431528.jpg">
        <p>扫一扫，查看更多卡券、活动信息</p>
    </div>

</section>
</html>