/**
 * Created by zqq on 16/1/7.
 */
var checkNum = function(){
    if(parseInt($('.buyNum').val()) > 0){
        $('.minus').show();
        $('.minus_no').hide();
    }else{
        $('.minus').hide();
        $('.minus_no').show();
    }
}
$(document).ready(function(){
    checkNum();
});
var calTotal = function(){
    var totalPrice =0;
    var obj = $(".buyNum");
    var buyNum = parseInt($(obj).val());
    var price = parseInt($(obj).parent().parent().find(".hongbao-price").text());
    totalPrice += buyNum * price;
    $(".totalPrice").text((Math.round(totalPrice * 100) / 100).toFixed(2));
}
$(document).on("blur",".buyNum",function(){
    if(parseInt($(".buyNum").val())<0||isNaN(parseInt($(this).val()))){
        alert("购买数量错误!");
        $(this).val(0);
    }
    var maxNumber = parseInt($(this).data("max"));
    if ($(this).val() > 9999 || $(this).val() > maxNumber) {
        alert("超过最大购买数量！");
        return false;
    }
    calTotal();
});
$(".plus").on("click",function(){
    var buyNum = parseInt($(this).parent().find(".buyNum").val());
    var maxNumber = parseInt($(this).parent().find(".buyNum").data("max"));
    if (buyNum >=9999 || buyNum >=maxNumber) {
        alert("超过最大购买数量！");
        return false;
    }
    $(this).parent().find(".buyNum").val(buyNum+1);
    checkNum();
    calTotal();
});
$(".minus").on("click",function(){
    var buyNum = parseInt($(this).parent().find(".buyNum").val());
    if(buyNum == 0){
        return false;
    }
    $(this).parent().find(".buyNum").val(buyNum-1);
    checkNum();
    calTotal();
});
$(document).on('click', '.buy-now', function() {

    var count = $("#buy-num").val();
    if (count == 0) {
        alert("请选择商品！");
    } else {
        window.location.href = BASE_PATH+"/shop/yunxiu/fuchs/order?count="+count;
    }
});
