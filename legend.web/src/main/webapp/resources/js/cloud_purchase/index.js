/**
 * Created by Sky on 2015/7/22.
 */


$(function () {

    var extData = {};
    var Model = {
        carModel: function (pid, success) {

            var url = BASE_PATH + "/shop/car_category";
            $.getJSON(url, {pid: pid}, success);
        },
        cartModel: function(data, callback) {
        	$.ajax({
                url: BASE_PATH + "/shop/buy/tqmall_goods/inc_shopcart",
                data: data,
                contentType: "application/json",
                dataType: "json",
                success: function (json) {
                	
                	callback&&callback(json);
                }
            });
        }
    };

    var addDOMEvent = function () {
        $(document).on("click", ".selector li", function () { //当前选项切换

            var $this = $(this),
                $pare = $this.parent();
            $pare.find(".cur").removeClass("cur");
            if ($pare.hasClass("filter")) {
                $pare.siblings(".no-filter").removeClass("cur");
            }
            $this.addClass("cur");
        }).on("click", ".no-filter", function () { //针对“不限”条件做当前选项切换

            var $this = $(this);
            $this.siblings(".filter").find(".cur").removeClass("cur");
            $this.addClass("cur");
        }).on("click", ".display", function () { //展现全部或者部分

            var $this = $(this),
                $li = $this.siblings(".filter").find("li");
            $li.each(function (index) {
                if (index >= 5) {
                    $(this).toggle();
                }
            });
            if ($this.hasClass("show")) {
                $this.removeClass("show");
                $this.text("收起");
            } else {
                $this.addClass("show");
                $this.text("全部");
            }
        }).on("click", ".filter-btn", function () {

            $(".filter-detail").toggle();
        }).on("click", ".toDetail", function () {

            var url = $(this).data("url");
            var catId = $(".parts-type").find(".cur").data("catid");

            url = catId ? (url + "&catId=" + catId) : url;
            location.href = url;
        }).on("keyup", ".num", function () {
            var $this = $(this);
            if ($this.val() > 9999) {
                $this.val(9999);
            }
        }).on("click", ".J-cart", function () {
            var $this = $(this),
                $input = $this.closest("tr").find("input[type=text]"),
                $purt = $(".P-total-count"),
                data = {
                    goodsId: $this.data("goodsid"),
                    incCount: $input.val(),
                    step:$(this).parents('tr').find('.js-step-num').data('step'),
                    minBuyNum:$(this).parents('tr').find('.js-step-num').data('minBuy')
                };
            var stockNum = $(this).parents('tr').find('.stock-num').text();
            var procurementNum = $(this).parents('tr').find('.js-step-num').val();
            if(Number(procurementNum) > Number(stockNum)){
                taoqi.error('商品库存不足');
                return;
            }

            if ($input.val() == "" || isNaN($input.val()) || parseInt($input.val(), 10) <= 0) {
                taoqi.error("请输入正确的信息！");
                return false;
            }
            Model.cartModel(data, function (json) {

                if (json.success) {
                    var cartPosition = $(".P-tol").offset();
                    var addCarPosistion = $this.offset();
                    var img = $this.parents("tr").find('.parts-img').attr('src');
                    var fly = $('<img class="fly_img" src="' + img + '" width="88" height="88">');
                    fly.fly({
                        start: {
                            left: addCarPosistion.left,
                            top: addCarPosistion.top - 100
                        },
                        end: {
                            left: cartPosition.left + 50,
                            top: cartPosition.top - 50,
                            width: 0,
                            height: 0
                        },
                        speed: 1.2,
                        onEnd: function () {
                            fly.remove();
                        }
                    });

                    $purt.text(json.data);
                    $input.val("");
                } else {
                    taoqi.error(json.message);
                }

            });
        }).on("change", ".brand", function () { //适用车型

            searchCarModels($(this), ".type", [".type", ".displacement", ".models"]);
        }).on("change", ".type", function () {  //适用车型

            searchCarModels($(this), ".displacement", [".displacement", ".models"]);
        }).on("change", ".displacement", function () {  //适用车型

            searchCarModels($(this), ".models", [".models"]);
        }).on("click", ".topo-srch-btn", function () {  //局部搜索

            extData = topoSearchParms();
            $("#topoSearch").submit();
            $(".sort-box li").removeClass("cur");
        }).on("click", ".gbl-srch-btn", function () {  //全局搜索
            var q = "";
            if ($(".q1").val()) {
                $(".hq1").val($(".q1").val());
                q = q + " " + $(".q1").val();
            } else {
                $(".hq1").val("");
            }
            if ($(".q2").val()) {
                $(".hq2").val($(".q2").val());
                q = q + " " + $(".q2").val();
            } else {
                $(".hq2").val("");
            }
            if ($(".q3").val()) {
                $(".hq3").val($(".q3").val());
                q = q + " " + $(".q3").val();
            } else {
                $(".hq3").val("");
            }
            $(".left.cur").removeClass("cur");
            extData = {
                q: q
            };
            $("#glbSearch").submit();
            $(".sort-box li").removeClass("cur");
        }).on("click", ".sort-box li", function () {

            var $this = $(this);
            $this.siblings("li").removeClass("desc");
            $this.toggleClass("desc");
            if ($this.hasClass("goods-num")) {
                if ($this.hasClass("desc")) {
                    extData.sort = -2;
                } else {
                    extData.sort = 2;
                }
            } else {
                if ($this.hasClass("desc")) {
                    extData.sort = -3;
                } else {
                    extData.sort = 3;
                }
            }
            $("#topoSearch").submit();
        });
    };

    $("#topoSearch").submit(function () {
        var pageNum = 1;
        moreData($(this), pageNum, extData, "listTpl");
        return false;
    });

    $("#glbSearch").submit(function () {
        var pageNum = 1;
        moreData($(this), pageNum, extData, "listTpl");
        return false;
    });

    var initCityChange = function() {
        $(document).on("click", ".search-item .city-name, .search-item .city-chose", function(){
            $(".city-list").show();
        }).on("mouseleave", ".city-st", function(){
            $(".city-list").hide();
        }).on("click", ".city-list span[rel_id]", function(e) {
            var $e = $(e.target);
           data = {cityId:$e.attr("rel_id"), cityName:$e.text()};
            $.ajax({
                url: BASE_PATH + "/user/shop/updateCity",
                data: data,
                contentType: "application/json",
                dataType: "json",
                success: function (json) {
                    if(json.success) {
                        $(".d-city-name").text(data.cityName);
                        $("input[name='changeCityId']").val(data.cityId);
                        $(".city-list").hide();
                        location.reload();
                    } else {
                        taoqi.error("切换城市站失败.");
                    }
                }
            });
        });

    };

    (function init() {

        addDOMEvent();
        initCityChange();
        Model.carModel(0, function (json) {

            var tmp = opts(json.data);
            $(".brand").html(tmp);
            $(".chosen").chosen();
        });
        $(".topo-srch-btn").click();
    })();

    function topoSearchParms() {  //局部搜索参数
        var attr = "",
            $brand = $(".brand"),
            $type = $(".type"),
            $displacement = $(".displacement"),
            $models = $(".models"),
            brandId, catId, carType, q, hq1Val, hq2Val, hq3Val;
        $(".filter-name").each(function () {
            var $this = $(this),
                cur = $this.siblings(".filter").find(".cur").data("type");
            if (cur != null) {
                attr += $this.data('type') + ":" + cur + ",";
            }
        });

        brandId = $(".brand-id.cur").data("id");
        catId = $(".cate-id.cur").data("id");
        if(catId == null){
        	catId = $(".left.cur").data("catid");
        }

        carType = "";
        if ( $models.val() && !isNaN($models.val()) ) {
            carType = $models.val();
        } else if ( $displacement.val() && !isNaN($displacement.val()) ) {
            carType = $displacement.val();
        } else if ( $type.val() && !isNaN($type.val()) ) {
            carType = $type.val();
        } else if ( $brand.val() && !isNaN($brand.val()) ) {
            carType = $brand.val();
        }
        q = "";
        hq1Val = $(".hq1").val();
        hq2Val = $(".hq2").val();
        hq3Val = $(".hq3").val();
        hq1Val && (q += " " + hq1Val);
        hq2Val && (q += " " + hq2Val);
        hq3Val && (q += " " + hq3Val);
        return {
            attr: attr,
            carType: carType,
            brandId: brandId,
            catId: catId,
            q: encodeURI(q)
        };
    }
    
    function searchCarModels(self, selector, selectorArr) {  //搜索车型
    	
    	var pid = self.find("option:selected").val();
    
    	for(var i = 0; i < selectorArr.length; i++) {
    		$(selectorArr[i]).empty().append("<option value=''>请选择</option>");
    	}


    	if(!pid) { return false; }
    	var load = taoqi.loading();
    	Model.carModel(pid, function (json) {

    		taoqi.close(load);
    		var tmp = "";
            if(selector == '.type'){
                tmp = opts(json.data,2);
            }else{
                tmp = opts(json.data);
            }
    		$(selector).html(tmp);
            //更新select内容
            $(".chosen").trigger("chosen:updated");
    	});
    }

    function opts(data,index) {  //填充车型列表

        var tmp = "<option value=''>请选择</option>";
        if(data) {
        	for (var i = 0; i < data.length; i++) {
                if(index == 2){
                    tmp += "<option value=" + data[i].id + ">" + data[i].name+"("+data[i].importInfo+ ")</option>";
                }else{
                    tmp += "<option value=" + data[i].id + ">" + data[i].name + "</option>";
                }
        	}
        }
        return tmp;
    }


    //新需求：商品起售数量部分
    $(document).on('blur','.js-step-num',function(){
        var step = Number($(this).data('step'));
        var num = Number($(this).val());
        var minBuy = Number($(this).data('minBuy'));
        var stockNum = Number($(this).parents('tr').find('.stock-num').text());
        if(num > stockNum){
            taoqi.error('采购数量不能大于库存数量');
            return;
        }
        if(step !=1 || num !=1 ){
            if((num-minBuy)%step != 0 ){
                $(this).parents('td').find('.step-num').show();
                num = minBuy + step*(Math.floor(num/step) -1);
                $(this).val(num);
            }else{
                $(this).parents('td').find('.step-num').hide();
            }
        }
    })

});