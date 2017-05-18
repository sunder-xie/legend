<#--充值支付-->
<link rel="stylesheet" type="text/css"  href="${BASE_PATH}/static/css/page/ax_insurance/process-nav.css?da074116385977b8b42060d47c3ba6d0">
<style>
    .process-nav-true .nav_process li{
        height: 39px;
        width: 185px;
    }
</style>
<ul class="nav_process">
    <li class="nav_pLi greenW navLi"><span>1 选择充值方案</span></li>
    <li class="nav_pLi width"><span>2 选择支付方式</span></li>
    <li class="nav_pLi width"><span>3 支付</span></li>
    <li class="width"><span>4 充值成功</span></li>
</ul>
<script>
    //给头部添加样式
    function addHeadStyle(i) {              //i 为要添加背景的第几个
        var navLi =  $(".nav_pLi");
        for(var j = 0;j<i;j++){
            if(j>0){
                navLi.eq(j-1).addClass("step-befor-change");
            }
            navLi.eq(j).addClass("step-background").removeClass("greenW");
        }
        navLi.eq(i).addClass("greenW");
        if (i==3){
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