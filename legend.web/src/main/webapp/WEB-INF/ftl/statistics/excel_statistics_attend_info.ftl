<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>员工考勤情况表</title>
</head>

<body>
<table id="tableExcel" width="100%" border="1" cellspacing="0" cellpadding="0">
    <#--<tr>-->
        <#--<th>11111</th>-->
    <#--</tr>-->
    <tr>
        <th colspan="4">${shop.companyName}</th>
    </tr>
    <tr>
        <th colspan="4">员工考勤信息表</th>
    </tr>
    <tr>
        <th>${startTime}</th>
        <th >至</th>
        <th>${endTime}</th>
    </tr>
    <tbody>
    <#list attendData as item>
        <#list item as childitem>
        <#if childitem_index == 0>
            <tr>
                <th bgcolor="#EFEFEF">${childitem.signDateStr}</th>
                <th bgcolor="#EFEFEF">上班时间</th>
                <th bgcolor="#EFEFEF">下班时间</th>
                <th bgcolor="#EFEFEF">状态</th>
            </tr>
        </#if>

            <tr>
                <th>${childitem.userName}</th>
                <th>${childitem.signInTimeStr}</th>
                <th>${childitem.signOutTimeStr}</th>
                <th>${childitem.signstatus}</th>
            </tr>
        </#list>
    </#list>
    </tbody>
    <tr>
        <th>导出人:${userName}</th>
        <th></th>
        <th></th>
        <th>导出时间:${operateTime}</th>
    </tr>
</table>
</body>
</html>


