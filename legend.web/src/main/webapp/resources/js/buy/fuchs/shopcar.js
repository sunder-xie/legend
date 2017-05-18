var org = {
    layerId : "",
    num : 0
}
$("body").on("click",".cancel_invoice",function(){     //关闭收货地址弹框
    layer.close(org.layerId);
    org.num = 0;
})

$("#userNewAddress,.edit-Addr").click(function(){     //新增收获地址
    org.layerId = $.layer({
        type: 1,
        title: ["新增收货地址","text-align:center;background:#f5f2e6"],
        area: ['auto', 'auto'],
        border: [0], //去掉默认边框
        shade: [0.5, '#000000'],
        closeBtn: [0, false], //去掉默认关闭按钮
        shift: 'top',
        move: '.us_t span',
        page: {
            html: Dao.html('choiceAddress', null)
        }
    });
    var $this = $(this);
    var addressId = $this.parent(".J-Addr").find("input[type=radio]").val();     //收货地址ID
    var objJson = {};
    if($(this).hasClass("edit-Addr")){       //编辑收货地址
        var provinceNum = $this.parents(".J-Addr").find("input[name=province]").val();
        var cityNum = $this.parents(".J-Addr").find("input[name=city]").val();
        var districtNum = $this.parents(".J-Addr").find("input[name=district]").val();
        var streetNum = $this.parents(".J-Addr").find("input[name=street]").val();

        objJson = {
            "provinceNum" : provinceNum,
            "cityNum" : cityNum,
            "districtNum" : districtNum,
            "streetNum" : streetNum
        }

        $("input[name=contactsName]").val($this.parents(".J-Addr").find(".name").text());
        $("input[name=mobilePhone]").val($this.parents(".J-Addr").find(".mobile").text());
        $("textarea[name=address]").val($this.parents(".J-Addr").find(".address").text());
        fn.ajax("get",BASE_PATH+"/index/region/sub",{"pid":1},fn.getprivence,$("#privence"),objJson);
    }
    else{
        fn.ajax("get",BASE_PATH+"/index/region/sub",{"pid":1},fn.getprivence,$("#privence"));
    }

    $("body").off("change","#privence").on("change","#privence",function(){      //省
        var parendId = $(this).find("option:selected").attr("value");
        fn.ajax("get",BASE_PATH+"/index/region/sub",{"pid":parendId},fn.getprivence,$("#city"),objJson);
    })
    $("body").off("change","#city").on("change","#city",function(){           //市
        var parendId = $(this).find("option:selected").attr("value");
        fn.ajax("get",BASE_PATH+"/index/region/sub",{"pid":parendId},fn.getprivence,$("#town"),objJson);
    })
    $("body").off("change","#town").on("change","#town",function(){          //区
        var parendId = $(this).find("option:selected").attr("value");
        fn.ajax("get",BASE_PATH+"/index/region/sub",{"pid":parendId},fn.getprivence,$("#street"),objJson);
    })
    $(".save_invoice").click(function(){     //保存收货地址弹框
        //var shopName = $("input[name=shopName]"),
        var contactsName = $("input[name=contactsName]"),
            mobilePhone = $("input[name=mobilePhone]"),
            province = $("#privence").find("option:selected").attr("value"),
            city = $("#city").find("option:selected").attr("value"),
            district = $("#town").find("option:selected").attr("value"),
            street = $("#street").find("option:selected").attr("value"),
            address = $("textarea[name=address]");
            //telephone = $("input[name=telephone]")
        var data = {
            contactsName : contactsName.val(),
            mobilePhone : mobilePhone.val(),
            province : province,
            city : city,
            district : district,
            street : street,
            address : address.val()
        }
        if(!fn.isNaull(contactsName,"联系人不能为空")){
            return false;
        }
        if(!fn.isNaull(mobilePhone,"联系电话不能为空")){
            return false;
        }
        var tel = mobilePhone.val();
        var reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
        if (!reg.test(tel)) {
            taoqi.error("联系电话格式有误");
            return false;
        }
        if(!province || !city || !district || !street || !address.val()){
            taoqi.error("请输入完整的收货地址");
            return false;
        }
        if($this.hasClass("edit-Addr")){
            data["addressId"] = addressId;
            fn.ajax("post",BASE_PATH+"/shop/buy/tqmall_goods/update_address",data,fn.getAdress,$this);
        }
        else{
            fn.ajax("post",BASE_PATH+"/shop/buy/tqmall_goods/add_address",data,fn.getAdress,$this);
        }

    })
})
$("#agree").click(function(){     //协议弹框
    $.layer({
        type: 1,
        title: ["服务条款","text-align:center;background:#f5f2e6"],
        area: ['auto', 'auto'],
        border: [0], //去掉默认边框
        shade: [0.5, '#000000'],
        closeBtn: [0, true], //去掉默认关闭按钮
        shift: 'top',
        move: '.us_t span',
        page: {
            html: Dao.html('showAgree', null)
        }
    });
})
var fn = {
    ajax : function(type,url,data,callBack,obj,objJson){    //ajax请求
        taoqi.loading();
        $.ajax({
            type : type,
            url : url,
            data : data,
            dataType: "json",
            success: function(data){
                taoqi.close();
                if(data.success){
                    callBack && callBack(data,obj,objJson);
                }
                else{
                    taoqi.error(data.message);
                }
            },
            error: function(error){
                taoqi.error("服务器错误");
                taoqi.close();
            }
        })
    },
    getprivence : function(data,obj,objJson){     //地址回调函数
        var data = data.data;
        var html = "<option value=''>请选择</option>";
        obj.html("");
        for(var i = 0 ; i <data.length; i++){
            html += "<option value="+data[i].id+">"+data[i].regionName+"</option>";
        }
        obj.append(html);
        if(objJson){
            org.num += 1;
            if(org.num == 1){
                $("#privence").find("option[value ="+objJson.provinceNum+"]").attr("selected","true");
                $("#privence").trigger("change");
            }
            else if(org.num == 2){
                $("#city").find("option[value ="+objJson.cityNum+"]").attr("selected","true");
                $("#city").trigger("change");
            }
            else if(org.num == 3){
                $("#town").find("option[value ="+objJson.districtNum+"]").attr("selected","true");
                $("#town").trigger("change");
            }
            else{
                $("#street").find("option[value ="+objJson.streetNum+"]").attr("selected","true");
            }
        }
    },
    isNaull : function(obj,msg){    //验证是否为空
        if(obj.val() == ""){
            taoqi.error(msg);
            return false;
        }
        else{
            return true;
        }
    },
    getAdress : function(){
        taoqi.close(org.layerId);
        window.location.reload();
    },
    calculation : function(postage){
        var purAmount = $("#purAmount");        //合计金额
        var tCostArr = $(".T-cost");
        var totalAmount = $("#totalAmount");      //应付金额
        var total = 0;

        tCostArr.each(function(index){
            total += ($(this).text()) * 100;
        })
        purAmount.text(total/100);
        totalAmount.text((total + postage * 100)/100 );

    },
    getPostage : function(){              //计算邮费
        var cityId = $(".cur").eq(0).find("input[name=city]").val();           //城市ID
        if (!cityId) {
            return
            //cityId = $(".city_box").val();
        }
        fn.ajax("post",BASE_PATH+"/shop/buy/tqmall_goods/get_fee",{"cityId" : cityId},function(data){
            $(".postage").text(data.data);
            fn.calculation(data.data);
            seajs.use(['dialog','artTemplate'],function(dg,at){
                var city = $("#changeCityName").val();
                if(city != "") {
                    var html = at('err_tpl', {});
                    dg.dialog({
                        html: html
                    });
                }
            });
        });
    }

}

function getNumPadScope(min, max, remain, alreadyBuyNum){
    var minNum = min;
    var maxNum = Math.min( (max-alreadyBuyNum),remain);

    if( maxNum < minNum ){
        minNum = maxNum;
    }
    return {
        minNum:minNum,
        maxNum:maxNum
    };

}

$(function() {

    var addDOMEvent = function() {
        $(document).on("click", ".selector li", function() { //当前选项切换

            var $this = $(this),
                $pare = $this.parent();
            $pare.find(".cur").removeClass("cur");
            $this.addClass("cur");
            fn.getPostage();
        }).on("click", ".J-Addr", function() {

            var $this = $(this);
            $(".J-Addr").find("input[type=radio]").removeAttr("checked");
            $this.find("input[type=radio]").prop("checked", "true");
        }).on("click", ".delete-btn", function() {

            var $this = $(this);
            $this.closest("tr").remove();
        });
    };

    var btnCheck = function(){
        $(".J_input_limit").each(function() {
            var parent = $(this).parents("tr");
            var price = $(this).parents("tr").find(".price").text();    //单价
            var kucun = $(this).parents("tr").find(".kucun").text();      //库存
            var tCost = $(this).parents("tr").find(".T-cost");         //采购金额
            var goodsId = $(this).parents("tr").find("input[name=goodsId]").val();       //商品id

            if (parent.hasClass("goods_yx")) {
                var remain = parent.find(".kucun").text();      //库存
                if (isNaN(remain)) {
                    remain = 9999;
                }
                var alreadyBuyNum = parent.find("input[name=alredyBuyNumber]").val() || 0;  //已购买数量
                var max = parent.find("input[name=groupMaxNumber]").val();  //限购数量
                var min = parent.find("input[name=groupMinNumber]").val();  //最小起售数量
                var canbuy = getNumPadScope(min, max, remain, alreadyBuyNum).maxNum;
                if (parseInt($(this).val()) > canbuy) {
                    var tip_txt = '<div class="tip_txt">亲，您本月限购数量还剩' + canbuy + '件</div>';
                    $(this).parents("td").append(tip_txt);
                } else {
                    var tip = '';
                    $(this).parents("td").append(tip);
                }

            } else {
                if(parseInt($(this).val()) > parseInt(kucun)){
                    taoqi.error("库存量不足");
                    $(".submit-btn").attr("disabled","true");
                    return;
                } else {
                    $(".submit-btn").removeAttr("disabled");
                }
            }
            tCost.text(price * 10000 * $(this).val() / 10000);
        });
        var tip = $(".tip_txt");
        if(tip.length > 0){
            $(".submit-btn").attr("disabled","true");
        } else {
            $(".submit-btn").removeAttr("disabled");
        }

    };

    (function init() {
        var productList = $(".product-list tr:gt(0)");
        if(productList.length == 0){
            $(".submit-btn").attr("disabled","true");
        }
        $(".J-Addr").find("input[type=radio]").removeAttr("checked");
        $(".J-Addr").eq(0).find("input[type=radio]").prop("checked", "true");
        $("#protocol").click(function(){
            if($(this).attr("checked")){
                $(".submit-btn").removeAttr("disabled");
            }
            else{
                $(".submit-btn").attr("disabled","true");
            }
        })

        if(!$(".address-list").find("li").length){     //收货地址为空
            $(".submit-btn").attr("disabled","true");
        }
        addDOMEvent();
        //fn.calculation();
        fn.getPostage();

        btnCheck();
        if(!$("#protocol").attr("checked")) {
            $(".submit-btn").attr("disabled","true");
        }
    })();

    $("body").on("blur",".J_input_limit",function(){     //更改商品采购数量
        var can1 = false, can2 = false;
        if($(".goods").length <= 0){
            can2 = true;
        }
        if($(".goods_yx").length <= 0){
            can1 = true;
        }
        var parent = $(this).parents("tr");
        var price = $(this).parents("tr").find(".price").text();    //单价
        var kucun = $(this).parents("tr").find(".kucun").text();      //库存
        var tCost = $(this).parents("tr").find(".T-cost");         //采购金额
        var goodsId = $(this).parents("tr").find("input[name=goodsId]").val();       //商品id
        var groupId = $(this).parents("tr").find("input[name=groupId]").val();       //商品id

        if($(this).parents("tr").hasClass("goods_yx")){
            var remain = parent.find(".kucun").text();      //库存
            if (isNaN(remain)){
                remain = 9999;
            }
            var alreadyBuyNum = parent.find("input[name=alredyBuyNumber]").val() || 0;  //已购买数量
            var max = parent.find("input[name=groupMaxNumber]").val();  //限购数量
            var min = parent.find("input[name=groupMinNumber]").val();  //最小起售数量
            var canbuy = getNumPadScope(min, max, remain, alreadyBuyNum).maxNum;
            if (parseInt($(this).val()) > canbuy) {
                var tip_txt = '<div class="tip_txt">亲，您本月限购数量还剩' + canbuy + '件</div>';
                $(this).parents("td").append(tip_txt);
            } else {
                $(this).parents("td").find(".tip_txt").remove();
            }
            var tip = $(".tip_txt");
            if(tip.length > 0){
                can1 = false;
            } else {
                can1 = true;
            }
        }else if($(this).parents("tr").hasClass("goods_cgj")){
            var remain = parent.find(".kucun").text();      //库存
            if (isNaN(remain)){
                remain = 9999;
            }
            var alreadyBuyNum = parent.find("input[name=alredyBuyNumber]").val() || 0;  //已购买数量
            var max = parent.find("input[name=groupMaxNumber]").val();  //限购数量
            var min = parent.find("input[name=groupMinNumber]").val();  //最小起售数量
            var canbuy = getNumPadScope(min, max, remain, alreadyBuyNum).maxNum;
            if (parseInt($(this).val()) > canbuy) {
                var tip_txt = '<div class="tip_txt">亲，您本月限购数量还剩' + canbuy + '件</div>';
                $(this).parents("td").append(tip_txt);
            } else {
                $(this).parents("td").find(".tip_txt").remove();
            }
            var tip = $(".tip_txt");
            if(tip.length > 0){
                can1 = false;
            } else {
                can1 = true;
            }
            var val = Number($(this).val());
            var purchaseAmount = parent.find('input[name="purchaseAmount"]').val();
            parent.find('.purchaseAmount').text(val*purchaseAmount);
        } else {
            var $this = $(this);
            if($this.val() == "" || $this.val() == 0){
                taoqi.error("采购数量必须大于0");
                can2 = false;
                return;
            }
            if(parseInt($this.val()) > parseInt(kucun)){
                taoqi.error("库存量不足");
                can2 = false;
                //return;
            }
            else{
                can2 = true
            }
        }

        if (can1 == false || can2 == false) {
            $(".submit-btn").attr("disabled","true");
        } else {
            $(".submit-btn").removeAttr("disabled");
        }

        tCost.text(price * 10000 * $(this).val() / 10000);

        fn.getPostage();

        if(!$("#protocol").attr("checked")) {
            $(".submit-btn").attr("disabled","true");
        }

    });

    $(".submit-btn").click(function(){         //提交订单
        taoqi.loading("正在提交，请稍后...");
        var $this = $(this);
        var addressId = $(".cur").eq(0).find("input[type=radio]").val();     //收货地址ID
        var postScript = $(".remark-text").val();             //订单备注
        var data = {remark:postScript, addressId:addressId};
        var url = BASE_PATH+"/shop/yunxiu/fuchs/apply/fuchs_apply"; //有云修商品的请求地址
        $.ajax({
            type : "post",
            url : url,
            data : data,
            dataType: "json",
            success: function(newData){
                taoqi.close();
                if(newData.success){
                    taoqi.success("提交申请成功！");
                    window.location.href = BASE_PATH+"/shop/yunxiu/fuchs/apply";
                } else {
                    taoqi.error(newData.message || "提交失败，请稍后再试！");
                }
            },
            error: function(error){
                taoqi.error("服务器错误");
                taoqi.close();
            }
        });


    })



});