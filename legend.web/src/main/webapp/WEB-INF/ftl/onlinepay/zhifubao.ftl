<!DOCTYPE html>
<html>
<head>
    <title>支付中...</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

<body>
<div>
<#if payHtml == "">
    该订单已支付或者已完成
<#else>
${payHtml}
</#if>
</div>
</body>
</html>