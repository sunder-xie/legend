<!DOCTYPE HTML>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>天安服务单打印</title>
    <link rel="stylesheet" href="${BASE_PATH}/resources/style/page/print/print_ng.css?25d0777a64ef04f70f5eb3efea87b3c1"/>
</head>
<body>
<div class="print_ng">
    <div class="print_header">
        <div class="print_logo"><img src="${BASE_PATH}/resources/img/common/print_logo.png" alt=""></div>
        <span class="font32 bold">${shop.companyName}</span>
        <span class="date">日期：${bill.gmtCreateStr}</span>
    </div>
    <table>
        <tr>
            <td class="bold">车牌：</td>
            <td>${bill.carLicense}</td>
            <td class="bold">车主：</td>
            <td>${bill.customerName}</td>
            <td class="bold">车主电话：</td>
            <td colspan="3">${bill.customerMobile}</td>
        </tr>
        <tr>
            <td class="bold">受损部位：</td>
            <td colspan="3">${bill.woundPart}</td>
            <td class="bold">核销码：</td>
            <td colspan="3">${bill.carLicense}</td>
        </tr>
        <tr>
            <td class="bold">服务名称：</td>
            <td>${service.name}</td>
            <td class="bold">服务类别：</td>
            <td>${service.categoryName}</td>
            <td class="bold">工时费：</td>
            <td>${service.servicePrice}</td>
            <td class="bold">工时：</td>
            <td>1</td>
        </tr>
        <tr>
            <td class="bold">金额：</td>
            <td  colspan="7">${service.servicePrice}</td>
        </tr>
        <tr>
            <td class="bold" colspan="8">
                照片：
            </td>
        </tr>
    </table>
    <table class="border_top_none">
        <tr>
            <td class="w20 border_top_none"><img src="${bill.imgUrl}" width="200" height="200"></td>
            <td class="w20 border_top_none"><img src="${bill.woundSnapshoot}" width="200" height="200"></td>
            <td class="w20 border_top_none"><img src="${bill.acceptanceSnapshoot}" width="200" height="200"></td>
        </tr>
    </table>

    <table class="margin_top_10">
        <tr>
            <td colspan="6" class="bold">
                <br />
                请客户代表（取车人）签字：
                <br />
                <br />
            </td>
        </tr>
    </table>
    <div class="margin_top_40">
        门店名称：${shop.name}
        <br />
        <br />
        地址：${shop.provinceName}${shop.cityName}${shop.districtName}${shop.streetName}${shop.address}
        <br />
        <br />
        座机电话：${shop.tel}
        <br />
        <br />
    </div>
</div>

<script>
    window.onload = function(){
        window.print();
    }
</script>
</body>
</html>
