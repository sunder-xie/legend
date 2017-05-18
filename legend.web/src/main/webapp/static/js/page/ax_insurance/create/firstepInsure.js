/**
 * Created by huage on 2016/12/22.
 */
$(function () {
    seajs.use(['dialog'], function (dg) {
        /*=== 点击事件 开始===*/
        first_page_click(dg);
        /*=== 点击事件 开始===*/
    });


    /*=== 点击事件 ===*/
    function first_page_click(dg) {
        var $doc = $(document),
            car_num = $(".car_num");
        $doc.on("click", ".checkbox", function () {            //点击是否拿到车牌
            var cheK = $(this).prop("checked");
            if (cheK) {
                car_num.attr("readonly", true).removeClass("bc").val("").removeClass("redC");
            } else {
                car_num.attr("readonly", false).addClass("bc");
            }
        });
        $doc.on("click", ".province,.address", function (e) {
            stopPropagation(e);                 //阻止事件冒泡
            var $this = $(this);
            /*==点击调接口==*/
            if (!$this.hasClass("cur")) {
                before_ajax(dg, $this);
            }else{
                Smart.select_list($this,'.choose_btn');
            }

        });
        $doc.on("click", "ul li", function () {
            var $this = $(this),
                liV = $this.text(),       //获取li的值
                liCode = $this.data('code');
            $this.parents("ul").siblings(".cho_add").val(liV).data('code', liCode);
        });
    }
    /*=== 点击事件 ===*/

    /*==点击事件二级方法开始==*/
    function before_ajax(dg, $this) {
        var data = {},
            url;
        if ($this.hasClass('province')) {
            data = {
                regionParentCode: "000000"
            };
            url = "/insurance/anxin/api/getAddressProvinceCity";
            //调省份的接口
            getCityData(dg,$this, data, url, $(".prov"));
            var choose_region = $('.choose_region');
            choose_region.val('').data('code','');
        } else {
            var provCode = $('.choose_prov').data("code");
            $this.parents("ul").siblings(".choose_prov").data("code", provCode);
            url = "/insurance/anxin/api/getAddressProvinceCity";
            data = {
                regionParentCode: provCode
            };
            if(provCode){
                getCityData(dg,$this, data, url, $(".region"))
            }else{
                dg.warn('请先选择投保省份');
                $('.province').addClass('red_border');
                return;
            }
        }

    }
    /*==点击事件二级方法结束==*/

    /*==点击调省市接口方法开始==*/
    function getCityData(dg,$this, data, url, emptyDoM) {
        $.ajax({
            url: BASE_PATH + url,
            data: data,
            success: function (result) {         // 先去获取到省
                if (result.success) {
                    emptyDoM.empty();
                    var data = result.data;
                    var code = [];
                    for (var i = 0; i < data.length; i++) {
                        var str = "<li" + " data-code=" + data[i].regionCode + ">" + data[i].regionName + "</li>";
                        emptyDoM.append(str);
                        code.push(data[i].regionCode);               //获取省对应的code
                    }
                    Smart.select_list($this,'.choose_btn');
                } else {
                    dg.warn(result.errorMsg)
                }
            }
        })
    }

    /*==点击事件二级方法结束==*/

    function stopPropagation(e) {
        if (e.stopPropagation) {
            e.stopPropagation();
        } else {
            e.cancelBubble = true;
        }
    }
});