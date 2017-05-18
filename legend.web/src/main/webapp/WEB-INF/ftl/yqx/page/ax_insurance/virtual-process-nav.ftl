
<#--虚拟投保-->
<link rel="stylesheet" type="text/css"  href="${BASE_PATH}/static/css/page/ax_insurance/process-nav.css?da074116385977b8b42060d47c3ba6d0">
<style>
    .nav_process li.nav_pLi{
        width: 122px;
    }
</style>
<ul class="nav_process">
    <li class="nav_pLi greenW navLi"><span>1 输入基本信息</span></li>
    <li class="nav_pLi width"><span>2 填写车辆信息</span></li>
    <li class="nav_pLi width"><span>3 选择投保方案</span></li>
    <li class="nav_pLi width"><span>4 确认投保信息</span></li>
    <li class="nav_pLi width"><span>5 选择服务包</span></li>
    <li class="width"><span>6 支付服务费</span></li>
</ul>
<script>
    //给头部添加样式
    function addHeadStyle(i) {              //i 为要添加背景的第几个
        for(var j = 0;j<i;j++){
            if(j>0){
                $(".nav_pLi").eq(j-1).addClass("step-befor-change");
            }
            $(".nav_pLi").eq(j).addClass("step-background").removeClass("greenW");
        }
        $(".nav_pLi").eq(i).addClass("greenW");
        if (i==5){
            $(".width").addClass("greenW");
        }
    }
    //返回时去掉头部样式
    function removeeadStyle(i) {
        var navLi =  $(".nav_pLi");
        navLi.eq(i-1).removeClass("step-background").addClass("greenW");
        navLi.eq(i-2).removeClass("step-befor-change");
        navLi.eq(i).removeClass("greenW")
    }
</script>

