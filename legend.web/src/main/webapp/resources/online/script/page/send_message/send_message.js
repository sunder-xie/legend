/**
 * Created by twg on 15/5/27.
 */
var tab = {
    change : function(){    //账户信息和历史内容tab切换
        var aArrs = $(".qxy_tab li");
        var contents = $(".tab_content");
        aArrs.on("click",function(){
            var index = $(this).index();
            aArrs.find("a").removeClass("current");
            $(this).find("a").addClass("current");
            contents.hide();
            contents.eq(index).show();
        })
    },
    checked : function(){   //选中某条历史内容记录
        var lis = $(".history li");
        lis.on("click",function(){
            lis.removeClass("checked");
            $(this).addClass("checked");
            lis.find(".checked_img").hide();
            $(this).find(".checked_img").show();
        })
    }
}
/*短信设定内容js start*/
$(document).ready(function(){
    //tab.change();
    tab.checked();
});