$(function(){

    $(".bottom input").eq(0).on("click",function(){

        var id = $("#sid").val();
        window.location.href = BASE_PATH+"/shop/marketing/sms/new/ng?id="+id;
    })

    $(".bottom input").eq(1).on("click",function(){
        window.location.href = BASE_PATH+"/shop/marketing/sms/ng";

    })

});