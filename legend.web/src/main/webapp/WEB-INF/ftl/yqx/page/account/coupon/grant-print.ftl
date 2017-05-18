<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>优惠券套餐办理回执单</title>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/base.css?8bb0f9a84cfb070249dcc1186f9f6a25"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/common/print/print_common.css?e174653fe93fff7de684a409310c97d7"/>
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/member/recharge-print.css?d9dc9847db48f19b8461d70fdf54e6f0"/>
    <script src="${BASE_PATH}/static/third-plugin/jquery.min.js"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</head>
<body>
<div>
    <#if SESSION_SHOP_LEVEL != 10 && SESSION_SHOP_LEVEL != 11 && SESSION_SHOP_LEVEL != 12>
    <p class="tq_print_logo"><img src="${BASE_PATH}/static/img/common/print/print_logo.png"/></p>
    </#if>
    <h1 class="text-center margin-5">${printObj.shopName}</h1>

    <h2 class="text-center text-red">优惠券套餐办理回执单</h2>

    <div class="car-info">
        <table class="no-border">
            <tr>
                <td class="col-5 no-float">
                    <i>单号：</i>
                    <i class="ellipsis">${printObj.flowSn}</i>
                </td>
                <td class="col-4 no-float">
                    <i>日期：</i>
                    <i class="text-left">${printObj.dateStr}</i>
                </td>
                <td class="col-3 text-right no-float">
                    <i class="text-left">车主：</i>
                    <i class="ellipsis">${printObj.customerName}</i>
                </td>
            </tr>
            <tr>
                <td class="col-5 no-float">
                    <i>车牌：</i>
                    <i class="ellipsis">${printObj.carLicenses}</i>
                </td>
                <td class="col-4 no-float">
                    <i>联系电话：</i>
                    <i class="text-left">${printObj.phone}</i>
                </td>
            </tr>
        </table>
    </div>

    <table class="content">
        <caption>办理内容</caption>
        <thead>
        <tr>
            <th class="col-1 no-float">序号</th>
            <th>套餐</th>
            <th>数量</th>
            <th>单价</th>
            <th>总价</th>
        </tr>
        </thead>
        <tr class="text-center">
            <td>1</td>
            <td>${printObj.name}</td>
            <td>${printObj.number}</td>
            <td class="money-font">&yen;${printObj.amount}</td>
            <td class="money-font">&yen;${printObj.payableAmount}</td>
        </tr>
        <tr>
            <td colspan="5 padding-5"><p>注：${printObj.remark}</p></td>
        </tr>
    </table>


    <div class="footer">
        <div class="text-right signature">
            <div class="inline-block fr">
                <div class="strong marin-10 money-font">&yen;${printObj.payAmount}</div>
            </div>
            <div class="inline-block text-left fr">
                <div class="strong margin-10">本单收款（合计）：</div>
                <div class="strong">客户签字</div>
            </div>
        </div>

        <table>
            <caption class="text-left">门店信息</caption>
            <tr>
                <td class="col-8 no-float">门店名称：${printObj.shopName}</td>
                <td class="col-4 no-float">座机电话：${printObj.shopTele}</td>
            </tr>
            <tr>
                <td colspan="2">门店地址：${printObj.shopAddress}</td>
            </tr>
            <tr>
                <td colspan="2">
                    提示：
                    <div>
                    ${printObj.tips}
                    </div>
                </td>
            </tr>
        </table>
    </div>
</div>
</body>
</html>