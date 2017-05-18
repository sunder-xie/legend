var org = {
    layerId: "",
    num: 0
};
var _totalAmount = 0;
$("body").on("click", ".cancel_invoice", function () {     //关闭收货地址弹框
    layer.close(org.layerId);
    org.num = 0;
})

var firstEnter = true;
function closeLayer() {
    if (firstEnter) {
        $('.xubox_shade').remove();
        $('.xubox_layer').remove();
        firstEnter = false;
    }
}


$("#userNewAddress,.edit-Addr").click(function () {     //新增收获地址
    org.layerId = $.layer({
        type: 1,
        title: ["新增收货地址", "text-align:center;background:#f5f2e6"],
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
    if ($(this).hasClass("edit-Addr")) {       //编辑收货地址
        var provinceNum = $this.parents(".J-Addr").find("input[name=province]").val();
        var cityNum = $this.parents(".J-Addr").find("input[name=city]").val();
        var districtNum = $this.parents(".J-Addr").find("input[name=district]").val();
        var streetNum = $this.parents(".J-Addr").find("input[name=street]").val();

        objJson = {
            "provinceNum": provinceNum,
            "cityNum": cityNum,
            "districtNum": districtNum,
            "streetNum": streetNum
        }

        $("input[name=contactsName]").val($this.parents(".J-Addr").find(".name").text());
        $("input[name=mobilePhone]").val($this.parents(".J-Addr").find(".mobile").text());
        $("textarea[name=address]").val($this.parents(".J-Addr").find(".address").text());
        fn.ajax("get", BASE_PATH + "/index/region/sub", {"pid": 1}, fn.getprivence, $("#privence"), objJson);
    }
    else {
        fn.ajax("get", BASE_PATH + "/index/region/sub", {"pid": 1}, fn.getprivence, $("#privence"));
    }

    $("body").off("change", "#privence").on("change", "#privence", function () {      //省
        var parendId = $(this).find("option:selected").attr("value");
        fn.ajax("get", BASE_PATH + "/index/region/sub", {"pid": parendId}, fn.getprivence, $("#city"), objJson);
    })
    $("body").off("change", "#city").on("change", "#city", function () {           //市
        var parendId = $(this).find("option:selected").attr("value");
        fn.ajax("get", BASE_PATH + "/index/region/sub", {"pid": parendId}, fn.getprivence, $("#town"), objJson);
    })
    $("body").off("change", "#town").on("change", "#town", function () {          //区
        var parendId = $(this).find("option:selected").attr("value");
        fn.ajax("get", BASE_PATH + "/index/region/sub", {"pid": parendId}, fn.getprivence, $("#street"), objJson);
    })
    $(".save_invoice").click(function () {     //保存收货地址弹框
        var $that = $(this);
        var disabled = $(this).prop('disabled');
        if (disabled == true || disabled == 'disabled') {
            return;
        } else {
            $(this).prop('disabled', true);
        }
        var contactsName = $("input[name=contactsName]"),
            mobilePhone = $("input[name=mobilePhone]"),
            province = $("#privence").find("option:selected").attr("value"),
            city = $("#city").find("option:selected").attr("value"),
            district = $("#town").find("option:selected").attr("value"),
            street = $("#street").find("option:selected").attr("value"),
            address = $("textarea[name=address]");
        //telephone = $("input[name=telephone]")
        var data = {
            contactsName: contactsName.val(),
            mobilePhone: mobilePhone.val(),
            province: province,
            city: city,
            district: district,
            street: street,
            address: address.val()
        }
        if (!fn.isNaull(contactsName, "联系人不能为空")) {
            $that.prop('disabled', false);
            return false;
        }
        if (!fn.isNaull(mobilePhone, "联系电话不能为空")) {
            $that.prop('disabled', false);
            return false;
        }
        var tel = mobilePhone.val();
        var reg = /^1[34578][0-9]\d{8}$/;
        if (!reg.test(tel)) {
            taoqi.error("联系电话格式有误");
            $that.prop('disabled', false);
            return false;
        }
        if (!province || !city || !district || !street || !address.val()) {
            taoqi.error("请输入完整的收货地址");
            $that.prop('disabled', false);
            return false;
        }
        if ($this.hasClass("edit-Addr")) {
            data["addressId"] = addressId;
            fn.ajax("post", BASE_PATH + "/shop/buy/tqmall_goods/update_address", data, fn.getAdress, function () {
                $that.prop('disabled', false);
            }, $this);
        } else {
            fn.ajax("post", BASE_PATH + "/shop/buy/tqmall_goods/add_address", data, fn.getAdress, function () {
                $that.prop('disabled', false);
            }, $this);
        }

    })
});
$("#agree").click(function () {     //协议弹框
    $.layer({
        type: 1,
        title: ["服务条款", "text-align:center;background:#f5f2e6"],
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
});
var fn = {
    ajax: function (type, url, data, successCallback, errorCallback, obj, objJson) {    //ajax请求
        taoqi.loading();
        if (typeof errorCallback !== 'function') {
            objJson = obj;
            obj = errorCallback;
        }

            $.ajax({
                type : type,
                url : url,
                data : data,
                dataType: "json",
                success: function(data){
                    taoqi.close();
                    if(data.success){
                        successCallback && successCallback(data,obj,objJson);
                    }
                    else{
                        errorCallback && errorCallback(data, obj, objJson);
                        taoqi.error(data.message);
                    }
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
            //fn.ajax("post",BASE_PATH+"/shop/buy/tqmall_goods/get_address_list","",fn.upDateAdress);
        },
        calculation : function(postage){
            var purAmount = $("#purAmount");        //合计金额
            var tCostArr = $(".T-cost");
            var totalAmount = $("#totalAmount");      //应付金额
            var rewardEle = $('.js-use-reward')[0];
            var reward = rewardEle && rewardEle.checked ? rewardEle.dataset.reward || 0 : 0;
            var total = 0, t;

        tCostArr.each(function (index) {
            total += ($(this).text()) * 100;
        })
        t = total;

        purAmount.text((total / 100).toFixed(2));

        t = (t + postage * 100) / 100;
        _totalAmount = t;

        if (t - reward > 0) {
            t = (t * 1000 - reward * 1000) / 1000;
        } else {
            t = 0;
        }
        totalAmount.text(t.toFixed(2));
    },
    getPostage: function (callback) {              //计算邮费
        var cityId = $(".cur").eq(0).find("input[name=city]").val();           //城市ID
        if (!cityId) {
            return
            //cityId = $(".city_box").val();
        }
        fn.ajax("post", BASE_PATH + "/shop/buy/tqmall_goods/get_fee", {"cityId": cityId}, function (data) {
            $(".postage").text(data.data);
            fn.calculation(data.data);
            seajs.use(['dialog', 'artTemplate'], function (dg, at) {
                var city = $("#changeCityName").val();
                if (city != "") {
                    var html = at('err_tpl', {});
                    dg.dialog({
                        html: html
                    });
                }
            });

            callback && callback();
        });
    }
};

function getNumPadScope(min, max, remain, alreadyBuyNum) {
    var minNum = min;
    var maxNum = Math.min((max - alreadyBuyNum), remain);

    if (maxNum < minNum) {
        minNum = maxNum;
    }
    return {
        minNum: minNum,
        maxNum: maxNum
    };

}

$(function () {
    var addDOMEvent = function () {
        $(document).on("click", ".selector li", function () { //当前选项切换

            var $this = $(this),
                $pare = $this.parent();
            $pare.find(".cur").removeClass("cur");
            $this.addClass("cur");
            fn.getPostage();
        }).on("click", ".J-Addr", function () {

            var $this = $(this);
            $(".J-Addr").find("input[type=radio]").removeAttr("checked");
            $this.find("input[type=radio]").prop("checked", "true");
        }).on("click", ".delete-btn", function () {

            var $this = $(this);
            $this.closest("tr").remove();
        });
    };

    var btnCheck = function () {
        $(".J_input_limit").each(function () {
            var parent = $(this).parents("tr");
            var price = $(this).parents("tr").find(".price").text();    //单价
            var kucun = $(this).parents("tr").find(".kucun").text();      //库存
            var tCost = $(this).parents("tr").find(".T-cost");         //采购金额
            var goodsId = $(this).parents("tr").find("input[name=goodsId]").val();       //商品id
            var step = Number($(this).data('step'));

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
                if (parseInt($(this).val()) > parseInt(kucun)) {
                    taoqi.error("库存量不足");
                    $(".submit-btn").attr("disabled", true);
                    return;
                } else {
                    $(".submit-btn").removeAttr("disabled");
                }
            }
            tCost.text(price * 10000 * $(this).val() / 10000);
        });
        var tip = $(".tip_txt");
        if (tip.length > 0) {
            $(".submit-btn").attr("disabled", true);
        } else {
            $(".submit-btn").removeAttr("disabled");
        }

    };

    (function init() {
        var productList = $(".product-list tr:gt(0)");
        if (productList.length == 0) {
            $(".submit-btn").attr("disabled", "true");
        }
        $(".J-Addr").find("input[type=radio]").removeAttr("checked");
        $(".J-Addr").eq(0).find("input[type=radio]").prop("checked", "true");
        $("#protocol").click(function () {
            if ($(this).attr("checked")) {
                $(".submit-btn").removeAttr("disabled");
            }
            else {
                $(".submit-btn").attr("disabled", "true");
            }
        })
        if (!$(".address-list").find("li").length) {     //收货地址为空
            $(".submit-btn").attr("disabled", "true");
        }
        addDOMEvent();
        fn.getPostage(function () {
            var isMaterialGoods = $('#isMaterialGoods').val();
            if (isMaterialGoods == "true") {
                taoqi.info('尊敬的云修客户您好！因本区域【物料商品】均为预售，同时由厂商直发，故需在您下单后15个自然日内【先行付款】，付款方式请联系当地销售，如逾期未付，订单则会自动取消。给您造成不便，尽情谅解！感谢您的支持！');
            }
            
            closeLayer();
        });

        btnCheck();
        getStep();
    })();

    $("body").on("blur", ".J_input_limit", function () {     //更改商品采购数量
        var can1 = false, can2 = false;
        if ($(".goods").length <= 0) {
            can2 = true;
        }

        if ($(".goods_yx").length <= 0) {
            can1 = true;
        }

        var parent = $(this).parents("tr");
        var price = $(this).parents("tr").find(".price").text();    //单价
        var kucun = $(this).parents("tr").find(".kucun").text();      //库存
        var tCost = $(this).parents("tr").find(".T-cost");         //采购金额
        var goodsId = $(this).parents("tr").find("input[name=goodsId]").val();       //商品id
        var groupId = $(this).parents("tr").find("input[name=groupId]").val();       //商品id

        if (!($(this).parents("tr").hasClass("goods_cgj"))) {
            var step = Number($(this).data('step'));
            var num = Number($(this).val());
            var minBuy = Number($(this).data('minNum'));
            if (num > kucun) {
                taoqi.error('采购数量不能大于库存数量');
                return;
            }
            if (step != 1 || num != 1) {
                if ((num - minBuy) % step != 0) {
                    $(this).parents('td').find('.step-num').show();
                    num = minBuy + step * (Math.floor(num / step) - 1);
                    $(this).val(num);
                } else {
                    $(this).parents('td').find('.step-num').hide();
                }
            }
        }

        if ($(this).parents("tr").hasClass("goods_yx")) {
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
                $(this).parents("td").find(".tip_txt").remove();
            }
            var tip = $(".tip_txt");
            if (tip.length > 0) {
                can1 = false;
            } else {
                can1 = true;
            }
        } else if ($(this).parents("tr").hasClass("goods_cgj")) {
            can2 = true;
            var val = Number($(this).val());
            var purchaseAmount = parent.find('input[name="purchaseAmount"]').val();
            parent.find('.purchaseAmount').text(val * purchaseAmount);


            //已购买过的数量
            var alreadyBuyNum = $(this).parents('tr').find('input[name="alreadyBuyNum"]').val();
            //最大购买的数量
            var groupMaxNumber = $(this).parents('tr').find('input[name="groupMaxNumber"]').val();
            //单位
            var actUnit = $(this).parents('tr').find('input[name="actUnit"]').val();
            //限购
            var buyLimitTag = $(this).parents('tr').find('input[name="buyLimitTag"]').val();

            //库存
            var remainNum = $(this).parents('tr').find('input[name="stockCount"]').val();

            if (remainNum == null) {
                //库存不限量
                remainNum = 9999;
            }
            //当前用户输入购买数量
            var number = Number($(this).val());

            var canBuyNumber = (groupMaxNumber - alreadyBuyNum);

            if (number > canBuyNumber) {
                $(this).val(canBuyNumber);
                taoqi.error('此商品' + buyLimitTag + '为' + groupMaxNumber + actUnit + "，您已经购买过" + alreadyBuyNum + actUnit + "，所以您只能购买" + canBuyNumber + actUnit);
                return;
            }

            if (number > remainNum) {
                $(this).val(remainNum);
                taoqi.error('此商品库存为' + remainNum + actUnit + '所以您只能购买' + remainNum + actUnit);
                return;
            }

            var list = $('.goods_cgj');
            var data = {};
            var purchaseRemainAmount = $('input[name="purchaseRemainAmount"]').val();
            var flag = true;
            list.each(function () {
                var $this = $(this);
                var goodsNum = Number($(this).val());
                //活动价
                var price1 = Number($this.parents('tr').find('input[name="price"]').val());
                //采购金
                var price2 = Number($this.parents('tr').find('input[name="purchaseAmount"]').val());

                if (data.goodsNum) {
                    data.goodsNum = data.goodsNum.Jia(goodsNum)
                } else {
                    data.goodsNum = goodsNum;
                }
                if (data.price1) {
                    data.price1 = data.price1.Jia(price1.Cheng(goodsNum))
                } else {
                    data.price1 = price1.Cheng(goodsNum);
                }
                if (data.price2) {
                    data.price2 = data.price2.Jia(price2.Cheng(goodsNum))
                } else {
                    data.price2 = price2.Cheng(goodsNum);
                }

                if (data.price2 > purchaseRemainAmount) {
                    flag = false;
                }
            });

            if (!flag) {
                taoqi.error('采购金余额不足');
                return;
            }
        } else {
            var $this = $(this);
            if ($this.val() == "" || $this.val() == 0) {
                taoqi.error("采购数量必须大于0");
                can2 = false;
                return;
            } else if (parseInt($this.val()) > parseInt(kucun)) {
                taoqi.error("库存量不足");
                can2 = false;
                return;
            } else {
                can2 = true;
            }
        }

        if (!can1 || !can2) {
            $(".submit-btn").prop("disabled", true);
        } else {
            $(".submit-btn").removeAttr("disabled");
        }

        tCost.text(price * 10000 * $(this).val() / 10000);
        
        if ($(this).parents("tr").hasClass("goods_yx")) {
            //云修专享
            fn.ajax("get", BASE_PATH + "/shop/yunxiu/sp/shopcart/update", {
                "groupId": groupId,
                "count": $(this).val()
            }, function (result) {
                if (result.success) {
                    fn.getPostage();
                }
            });
        } else if ($(this).parents("tr").hasClass("goods_cgj")) {
            //采购金
            fn.ajax("get", BASE_PATH + "/shop/taoqi/shopcart/update_count", {
                "groupId": groupId,
                "count": $(this).val(),
                "goodsId": goodsId
            }, function (result) {
                if (result.success) {
                    fn.getPostage();
                }
            });
        } else {
            //普通商品
            fn.ajax("get", BASE_PATH + "/shop/buy/tqmall_goods/add_or_update_shopcart", {
                "goodsId": goodsId,
                "count": $this.val()
            }, function (result) {
                if (result.success) {
                    fn.getPostage();
                }
            });
        }
    });

    $("body").on("click", ".delete-btn", function () {              //删除商品
        var goodsId = $(this).parents("tr").find("input[name=goodsId]").val();       //商品id
        var groupId = $(this).parents("tr").find("input[name=groupId]").val();       //商品id
        var that = $(this);
        var parent = $(this).parents("tr");
        var url;
        var data = {};
        if (parent.hasClass("goods_yx")) {
            //云修专享
            url = BASE_PATH + "/shop/yunxiu/sp/shopcart/delete";
            data = {"groupId": groupId};
        } else if (parent.hasClass("goods_cgj")) {
            //采购金
            url = BASE_PATH + "/shop/taoqi/shopcart/del";
            data = {
                "groupId": groupId,
                "goodsId": goodsId
            };
        } else {
            //普通商品
            url = BASE_PATH + "/shop/buy/tqmall_goods/del_shopcart";
            data = {"goodsId": goodsId};
        }
        fn.ajax("get", url, data, function () {
            fn.getPostage();
            var productList = $(".product-list tr:gt(0)");
            if (productList.length == 0) {
                $(".submit-btn").attr("disabled", "true");
            }
        });
    })

    var onOff = true;
    $(".submit-btn").click(function () {         //提交订单
        taoqi.loading("正在提交，请稍后...");
        $('.goods').each(function () {
            var procure = Number($(this).find(".J_input_limit").val());      //库存
            var stock = Number($(this).find(".kucun").text());      //库存
            if (procure > stock) {
                taoqi.error('采购数量不能大于库存数量');
                onOff = false;
                return;
            }
        });
        if (onOff) {
            var $this = $(this);
            var disabled = $this.prop('disabled');
            if (disabled == true || disabled == 'disabled') {
                return;
            } else {
                $this.prop('disabled', true);
            }
            var addressId = $(".cur").eq(0).find("input[type=radio]").val();     //收货地址ID
            var postScript = $(".remark-text").val();             //订单备注
            var cityId = $(".cur").eq(0).find("input[name=city]").val();           //城市ID
            var data = {
                "addressId": addressId,
                "postScript": postScript,
                "cityId": cityId
            };
            var productList = $(".product-list tr:gt(0)");
            productList.each(function (i) {
                if ($(this).hasClass("goods_yx") || $(this).hasClass("goods_cgj")) {
                    var json = {
                        "goodsId": "",
                        "goodsNumber": "",
                        "price": "",
                        "actId": "",
                        "groupId": ""
                    };
                    data["goodsParams[" + i + "].actId"] = $(this).find("input[name=actId]").val();
                    data["goodsParams[" + i + "].groupId"] = $(this).find("input[name=groupId]").val();
                } else {
                    var json = {
                        "goodsId": "",
                        "goodsNumber": "",
                        "price": ""
                    };
                }
                data["goodsParams[" + i + "].goodsId"] = $(this).find("input[name=goodsId]").val();
                data["goodsParams[" + i + "].goodsNumber"] = $(this).find(".J_input_limit").val();
                data["goodsParams[" + i + "].price"] = $(this).find(".price").text();
                data["goodsParams[" + i + "].step"] = $(this).find(".J_input_limit").data('step');
                data["goodsParams[" + i + "].minBuyNum"] = $(this).find(".J_input_limit").data('minNum');

            });

            var url = BASE_PATH + "/shop/buy/tqmall_goods/create_order"; //有云修商品的请求地址
            $.ajax({
                type: "post",
                url: url,
                data: data,
                dataType: "json",
                success: function (newData) {
                    taoqi.close();
                    $this.prop('disabled', false);
                    if (newData.success) {
                        taoqi.success("提交成功！");
                        window.location.href = BASE_PATH + "/shop/buy";
                    } else {
                        taoqi.error(newData.message || "提交失败，请稍后再试！");
                    }
                },
                error: function (error) {
                    taoqi.error("服务器错误");
                    taoqi.close();
                    $this.prop('disabled', false);
                }
            });

        }
    })


    // 选择采购金
    $('.js-use-reward').on('click', function () {
        var e = $('#totalAmount');
        var amount = _totalAmount;
        var val = +$(this).attr('data-reward');

        if ($(this).prop('checked')) {
            if (amount > 0) {
                if (val > amount) {
                    e.text(0);
                } else {
                    e.text((amount * 1000 - val * 1000) / 1000);
                }
            }
        } else {
            e.text(_totalAmount);
        }
    });
    function getStep() {
        $('.goods').each(function () {
            var step = Number($(this).find('.J_input_limit').data('step'));
            var num = Number($(this).find('.J_input_limit').val());
            var minBuy = Number($(this).find('.J_input_limit').data('minNum'));
            if (step != 1 || num != 1) {
                if ((num - minBuy) % step != 0) {
                    $(this).find('.step-num').show();
                } else {
                    $(this).find('.step-num').hide();

                }
            }
        });

    }
});

