<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <title>会员卡充值回执单</title>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/print_common.css">
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/member_print.css?cf37d48dfbcfc7a03ac30e03c0329c94">
    <script type="text/javascript">
        window.onload = function () {
            window.print();
        }
    </script>
</head>
<body>
<p class="tq_print_logo"><img src="${BASE_PATH}/resources/images/print_logo.png"/></p>

<h1 class="print_head">${shopName}</h1>
<h2 class="print_headline">会员卡充值回执单</h2>

<p class="base_info">
    <span>单号：</span><strong>${flowSn}</strong>
    <span>日期：</span><strong>${date}</strong>
    <span>会员卡号：</span><strong>${cardSn}</strong>
    <span>会员姓名：</span><strong>${memberName}</strong>
    <span>车牌：</span><strong>${license}</strong>
    <span>联系电话：</span><strong>${mobile}</strong>
</p>
<table class="business" border="0" cellpadding="0" cellspacing="0">
    <caption>办理内容</caption>
    <tr>
        <th width="50">序号</th>
        <th width="630">套餐及优惠</th>
        <th width="100">数量</th>
        <th width="100">单价（元）</th>
        <th width="100">总价（元）</th>
    </tr>
    <#list suiteList as suiteItem>
    <tr>
        <td>${suiteItem_index + 1}</td>
        <td>${suiteItem.suitName}</td>
        <td>${suiteItem.count}</td>
        <td>${suiteItem.price}</td>
        <td>${suiteItem.amount}</td>
    </tr>
    </#list>
    <tr>
        <td class="note" colspan="5">注：${remark}</td>
    </tr>
</table>
<div class="print_foot">
    <div class="print_text">
        <p class="amount">${totalAmount}</p>
    </div>
    <div class="print_label">
        <p>本单收款（合计）: </p>
        <p>客户签字：</p>
    </div>
</div>
<table class="store_info" border="0" cellpadding="0" cellspacing="0">
    <caption>门店信息</caption>
    <tr>
        <th width="80">门店名称：</th>
        <td width="900">${shopName}</td>
    </tr>
    <tr>
        <th>座机电话：</th>
        <td>${shopPhone}</td>
    </tr>
    <tr>
        <th>门店地址：</th>
        <td>${shopAddress}</td>
    </tr>
    <tr>
        <th>提示：</th>
        <td class="tips">根据汽车维修部门规定：小修或日常维护保养期：三天或者500公里</td>
    </tr>
</table>
</body>
</html>
