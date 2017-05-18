<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>员工考勤报表</title>
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/print_common.css">
    <link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/print_statistics_repair.css?201504201">
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery.js"></script>
    <script>
        $(document).ready(function ($) {
            window.print();
        });
    </script>
</head>
<body>
<div>
    <div class="head">
        <p class="tq_print_logo"><img src="${BASE_PATH}/resources/images/print_logo.png"/></p>

        <h1>${shop.companyName}</h1>

        <h3>员工考勤报表表</h3>

        <div class="headInfo">
            <span class="salesDate">
            <label>
            ${startTime}
            </label>
                至<label>
            </label>
            ${endTime}
            </span>
        </div>
    </div>
    <table cellspacing="0" cellpadding="0">
    <#list AttendData as item>
        <#list item as itemchild>
            <#if itemchild_index == 0>
                <tr>
                    <td>${itemchild.signDateStr}</td>
                    <td>上班时间</td>
                    <td>下班时间</td>
                    <td>状态</td>
                </tr>
            </#if>
            <tr>
                <td>${itemchild.userName}</td>
                <td>${itemchild.signInTimeStr}</td>
                <td>${itemchild.signOutTimeStr}</td>
                <td>${itemchild.signstatus}</td>
            </tr>

        </#list>
    </#list>
    </table>
    <div class="signature">
        <span class="sign_date"><label>打印日期：</label>${currentDate}</span>
        <span class="sign"><label>打印人：</label>${userName}</span>
    </div>
</div>
</body>
</html>

