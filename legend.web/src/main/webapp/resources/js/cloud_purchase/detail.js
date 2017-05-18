/**
 * Created by Sky on 2015/7/22.
 */


$(function() {

    var pageParams = {};

    var Model = {
        detailSearch: function(catId, success) {

            var url = BASE_PATH + "/shop/buy/tqmall_goods/getCategoryAttrs";
            var params = {
                catId: catId
            };
            Dao.xhrRender({
                action: url,
                params: params,
                tag: 'filter-box',
                tpl: 'filterTpl',
                fill: '~'
            }, success);
        }
    };

    var addDOMEvent = function() {
        $(document).on("click", ".selector li", function() { //当前选项切换

            var $this = $(this),
                $pare = $this.parent();
            $pare.find(".cur").removeClass("cur");
            $this.addClass("cur");

        }).on("click", ".img-preview li", function() {

            var $this = $(this),
                src = $this.data("bsrc");
            $(".img-view").prop("src", src);
        }).on("click", ".detail-tab li", function() {

            var $this = $(this),
                tabCls = "." + $this.data("tab");
            $(".product-detail .tab").hide();
            $(tabCls).show();
        }).on("click", "#add-cart", function() {
            var $this = $(this),
                $input = $("#J-number"),
                $purt = $(".Pur-total"),
                data = {
                    goodsId: $this.data("goodsid"),
                    incCount: $input.val(),
                    step:$input.data('step'),
                    minBuyNum:$input.data('minNum')
                };
            var val = $input.val();
            var stock = $('.free-store').text();
            var num = $('#J-number').val();
            
            stock = isNaN(stock) ? 0 : Number(stock);
            num = isNaN(stock) ? 0 : Number(stock);
            
            if(isNaN(val) || parseInt(val, 10) <= 0) {
                alert("请输入正确的商品数量！");
                return false;
            }
            if(num > stock){
                taoqi.error('库存不足！');
                return false;1
            }
            $.ajax({
                url: BASE_PATH + "/shop/buy/tqmall_goods/inc_shopcart",
                data: data,
                contentType: "application/json",
                dataType: "json",
                success: function(json) {

                    if(json.success) {
                        var cartPosition = $(".P-tol").offset();
                        var addCarPosistion = $this.offset();
                        var img = $(".img-view").prop("src");
                        var fly = $('<img class="fly_img" src="'+img+'" width="88" height="88">');
                        fly.fly({
                            start: {
                                left: addCarPosistion.left,
                                top: addCarPosistion.top - 50
                            },
                            end: {
                                left: cartPosition.left+50,
                                top: cartPosition.top+10,
                                width: 0,
                                height: 0
                            },
                            speed: 1.2,
                            onEnd: function(){
                                fly.remove();
                            }
                        });

                        $purt.text(json.data);
                    }
                }
            });
        });
    };

    (function init() {

        search();
        addDOMEvent();
        numSet();
        $(".product-detail .tab").hide();
        $(".tab-1").show();
    })();

    function search() {

        var params = location.search.substr(1).split("&");
        for(var i in params) {
            var args = params[i].split("="),
                key = args[0],
                value = args[1];
            pageParams[key] = value;
        }
    }

    function numSet() {//购买数量加减
        var $n = $(".number-buy");
        var valueChangedCallback = function(value){

        };
        //$n.each(function () {
            //var min = $(this).data('min') || 0;
            //var max = $(this).data('max') || 9999;
            //var remain = $(this).data('remain');
            //if( isNaN(remain) || remain < 0 ){ remain = max;}
            //var alreadybuynum = $(this).data('alreadybuynum') || 0;
            //var minMaxObj = getNumPadScope(min,max,remain,alreadybuynum);
            //$(this).addminusinputer({
            //    initValue:0,
            //    maxValue: minMaxObj.maxValue,
            //    minValue: minMaxObj.minValue,
            //    startValue: minMaxObj.startNum,
            //    valueChangedCallback:valueChangedCallback
            //});
        //});
        var step = Number($('#J-number').data('step'));
        var stock = $('.free-store').text();
        $('#reduce').prop('disabled',true);
        var n = 0;
        var minNum = Number($('#J-number').val());
        var num = Number($('#J-number').data('minNum'));

        $(document).on('blur','#J-number',function(){
            minNum = Number($(this).val());
            n=0;
            if(minNum > stock){
                taoqi.error('库存不足');
                minNum = num + step*(Math.floor((minNum - (minNum-stock))/step) -1);
                $(this).val(minNum);
                $('#add').prop('disabled',true);
                $('#reduce').prop('disabled',false);
                return;
            }else if(minNum < 1){
                $('#add').prop('disabled',false);
                $('#reduce').prop('disabled',true);
            }else{
                $('#add').prop('disabled',false);
                $('#reduce').prop('disabled',false);
                if(step !=1 || minNum !=1 ){
                    if((minNum-num)%step != 0 ){
                        $(this).parents('.number-buy').find('.step-num').show();
                        minNum = num + step*(Math.floor(minNum/step) -1);
                        $(this).val(minNum);
                    }else{
                        $(this).parents('.number-buy').find('.step-num').hide();
                        minNum = Number($(this).val());
                    }
                }
            }

            if(minNum > num){
                $('#reduce').prop('disabled',false);
            }
        });
        $(document).on('click','#add',function(){
            n++;
            var val = step*n;
            if( minNum + val >= stock){
                $('#add').prop('disabled',true);
                n--;
            }else{
                $('#J-number').val(minNum + val);
                $('#add').prop('disabled',false);
                $('#reduce').prop('disabled',false);
            }
        });

        $(document).on('click','#reduce',function(){
            n--;
            var val = step*n;
            if(minNum + val <= num ){
                $('#reduce').prop('disabled',true);
                $('#J-number').val(num);
                n = 0;
            }else{
                $('#J-number').val(minNum + val);
                $('#reduce').prop('disabled',false);
                $('#add').prop('disabled',false);
            }
        });

    }

    function getNumPadScope(min, /*限购数量*/max, /*库存量*/remain, /*已购买数量*/alreadyBuyNum){
        var minNum = min;
        var maxNum = Math.min( (max-alreadyBuyNum),remain );

        if( maxNum < minNum ){
            minNum = maxNum;
        }
        return {
            minValue:minNum,
            maxValue:maxNum,
            startNum:min
        };
    }
});
