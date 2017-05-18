$(document).ready(function ($) {
    $(".attend").click(function(){
        taoqi.setAttend();
        //点击变换颜色效果
        $(".attend").each(function(){
            $(".attend").removeClass("choose-detail");
        });
        $(this).addClass("choose-detail");
        //获取课次与课程id
        var detailId = $(this).parent().parent().find(".detailId").val();
        var id = $(this).parent().parent().find(".id").val();
        //报名
        $(".sure-btn").click(function(){
            var joinCount = $(this).parent().find(".number").val();
            if(joinCount==0){
                taoqi.error("报名人数不能为0");
                return false;
            }
            taoqi.ask("您确定要报名吗？",function(){
                var loading = taoqi.loading("报名登记中...");
                $.ajax({
                    type: 'POST',
                    url: BASE_PATH + '/shop/tech/course/join',
                    data: {
                        id:detailId,
                        courseId:id,
                        joinCount:joinCount
                    },
                    success: function (result) {
                        taoqi.close(loading);

                        if (result.success != true) {
                            taoqi.failure(result.errorMsg);
                            return false;
                        }
                        else {
                            taoqi.success("登记完成");
                            window.location.href = BASE_PATH + '/shop/tech/course/detail?id='+id+'&detailId='+detailId;
                        }
                    },
                    error: function (a, b, c) {
                        //console.log(a,b,c);
                    }
                });
            },function(){
                return false;
            })
        });
    });
    $(".search_tech_keyword").keyup(function(e){
        if(e.keyCode == 13){
            var keywords = $(".search_tech_keyword").val();
            window.location.href = BASE_PATH+"/shop/tech/search?keywords="+keywords;
        }
    });
    $("#search_book_btn").click(function () {
        var search_keywords = $("#search_keywords").val();
        window.location.href = BASE_PATH + "/shop/tech/search?keywords=" + search_keywords;
    });
});

