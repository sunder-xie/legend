/**
 * Created by ZhuangQianQian on 16/8/22.
 */
$(function(){
    var $doc = $(document);
    $doc.on("mouseover",".insurance-money",function(){
        $(this).next().show();
    }).on("mouseout",".insurance-money",function(){
        $(this).next().hide();
    }).on("click",".Z-back",function(){
        window.history.go(-1);
    })
});
