$(function(){
    var isAdmin = $("input[name='isAdmin']").val();
    var dialogIndex = null;
    if(isAdmin == 1){
        $(".setting_btn").show();
    }else{
        $(".setting_btn").hide();
    }
    var readyCheck = $("#readyCheck").val();
    var showTechnician = $("#showTechnician").val();
    var managerId = $("#managerId").val();
    $(document).on("click",".technician_go",function(){
        location.href=BASE_PATH+"/shop/technician";
    });
    if(showTechnician){
        var technician = localStorage.getItem("technician"+managerId);
        if(!technician&&!readyCheck){
            seajs.use(["dialog","artTemplate"], function (dg,at) {
                var html = $(".technician_dialog").html();
                dialogIndex = dg.dialog({
                    width: 440,
                    height: 300,
                    html: html
                });
            });
            localStorage.setItem("technician"+managerId,true);
        }
    }
    //关闭技师认证提醒框
    $(document).on('click', '.technician_cancle', function () {
        seajs.use("dialog", function (dg) {
            dialogIndex && dg.close(dialogIndex);
        });
    });
});