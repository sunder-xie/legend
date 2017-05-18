<#--真实投保的头部-->
<link rel="stylesheet" type="text/css"  href="${BASE_PATH}/static/css/page/ax_insurance/process-nav.css?da074116385977b8b42060d47c3ba6d0">
<ul class="nav_process">
    <li class="nav_pLi greenW navLi nav_li"><span>1 输入基本信息</span></li>
    <li class="nav_pLi width nav_li"><span>2 填写车辆信息</span></li>
    <li class="nav_pLi width nav_li"><span>3 选择投保方案</span></li>
    <li class="nav_pLi width nav_li"><span>4 确认投保信息</span></li>
    <li class="nav_pLi width nav_li"><span>5 车身拍照</span></li>
    <li class="nav_pLi width nav_li"><span>6 支付保费</span></li>
    <li class="width nav_li"><span>7 投保成功</span></li>
</ul>
<script>
    //给头部添加样式
    function addHeadStyle(i) {              //i 为要添加背景的第几个
        var navLi =  $(".nav_li");
        for(var j = 0;j<i;j++){
            if(j>0){
                navLi.eq(j-1).addClass("step-befor-change");
            }
            navLi.eq(j).addClass("step-background").removeClass("greenW");
        }
        navLi.eq(i).addClass("greenW");
    }

    //返回时去掉头部样式
     function removeeadStyle(i) {
         var navLi =  $(".nav_li");
         navLi.eq(i-1).removeClass("step-background").addClass("greenW");
         navLi.eq(i-2).removeClass("step-befor-change");
         navLi.eq(i).removeClass("greenW")
     }
</script>

