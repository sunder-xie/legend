/**
 * Created by ZhuangQianQian on 16/8/22.
 */
$(function(){
    var $doc = $(document);
    seajs.use([
       'dialog'
    ],function(dg){
        //防止window.open()被浏览器拦截，所以一进页面就先把href加上，之后让用户自己点击跳转
        var packageId = $(".js-business").data("packageId");
        var insuranceOrderSn = $(".js-business").data("insuranceordersn");
        if(packageId == ''){
            return;
        }
        $.ajax({
            url:BASE_PATH+'/insurance/anxin/virtual/flow/virtual-pay-fee?packageId='+packageId,
            type:"GET",
            success:function(result){
                if(result.success){
                    $(".Z-reCal").attr('href',BASE_PATH+"/insurance/anxin/virtual/flow/card-select?totalFee="+result.data+"&orderSn="+insuranceOrderSn);
                    //window.open(BASE_PATH+"/insurance/anxin/virtual/flow/card-select?totalFee="+result.data+"&orderSn="+insuranceOrderSn);
                }else{
                    dg.fail(result.errorMsg);
                }
            }
        });
        $doc.on("mouseover",".insurance-money",function(){
            $(this).next().show();
        }).on("mouseout",".insurance-money",function(){
            $(this).next().hide();
        }).on("click",".Z-modify",function(){
            var id = $(".Z-modify").data("id");
            window.location.href  = BASE_PATH+"/insurance/anxin/virtual/flow/virtual-plan?id=" + id;
        }).on("click",".Z-back",function(){
            window.history.go(-1);
        })
    });
});
