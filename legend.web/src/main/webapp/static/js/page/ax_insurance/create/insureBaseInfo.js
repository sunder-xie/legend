/**
 * Created by huage on 16/9/18.
 * 虚拟和真实的页面的前三步
 */
$(function () {
    seajs.use(["date", "art", "dialog"], function (dp, at, dg) {
        /*===进页面加载的方法 开始====*/
        var modeV = util.getPara("mode"),                  //拿到mode的值
            modeV2 = $(".modeV").val(),
            $doc = $(document);

        //判断modeV 的方法
        init_mode();

        //点击事件
        init_clickEvent();

        /*===进页面加载的方法 结束====*/

        //判断modeV 的方法
        function init_mode() {
            var process_nav_true = $(".process-nav-true"),
                process_nav_virtual = $(".process-nav-virtual"),
                $next_step = $(".next-Step"),
                $next_btn = $(".next_btn");
            if (modeV == "3") {
                process_nav_true.remove();
                process_nav_virtual.show();
                $next_step.show();
                $next_btn.hide();
            } else {
                process_nav_virtual.remove();
                process_nav_true.show();
                $next_step.hide();
                $next_btn.show();
            }
        }
        //点击事件
        function init_clickEvent() {
            //对document绑定点击事件
            $doc.on("click", function () {
                $(".enter ul,.papers-type-div ul,.queryDiv ul").slideUp();
                $(".province,.address,.papers-type-div,.queryDiv").removeClass("cur");
                $(".choose_btn,.v-t-M").addClass("s_up").removeClass("s_down");
            });
            $doc.on("click", ".n-button", function () {     //第一步的下一步点击
                var $prov = $(".choose_prov"),
                    $region = $(".choose_region"),
                    $carNum = $(".car_num"),
                    $CarName = $('.CarName'),
                    prev = $prov.val(),
                    region = $region.val(),
                    carNum = $.trim($carNum.val()),
                    CarName = $.trim($CarName.val()),
                    add_class = 'red_border',
                    cheK = $(".checkbox").prop("checked");
                if (!prev) {
                    $(".province").addClass("red_border");
                    dg.warn("请选择投保地区");            //浮窗方法
                    return false;
                }
                if (!region) {
                    $(".address").addClass("red_border");
                    dg.warn("请选择投保地区");
                    return false;
                }
                var fl = 1;
                if (!cheK) {                 //若获得车牌
                    try {
                        //数据校验
                        Smart.judge_input($carNum, carNum, "填写车牌号", add_class, '车牌号字数超过最大限制', carNum.length > 10);
                        Smart.judge_input($CarName, CarName, "填写车主姓名", add_class, '车主姓名字数超过限制', CarName.length > 30);
                    } catch (msg) {
                        dg.warn(msg);
                        return;
                    }
                    var insuredProvinceCode = $(".choose_prov").data("code");          // 投保人所在省code
                    var insuredCityCode = $(".choose_region").data("code");          // 投保人所在城市code
                    var vehicleSn = $(".car_num").val();          //车牌号码
                    $.ajax({
                        url: BASE_PATH + '/insurance/anxin/api/check-license',
                        type: "GET",
                        contentType: "application/json",
                        data: {
                            provincesCode: insuredProvinceCode,
                            cityCode: insuredCityCode,
                            licenseNo: vehicleSn
                        },
                        success: function (result) {
                            if (!result.success) {
                                dg.warn(result.errorMsg);
                                fl = 0;
                                return fl;
                            }else{
                                getDataForInsurance(carNum,CarName,fl)
                            }
                        }
                    });
                } else if (prev == "北京市") {
                    var mask = 1;
                    getDataForInsurance(carNum,CarName,fl,mask);
                } else {
                    var mask = 2;
                    getDataForInsurance(carNum,CarName,fl,mask);
                }
            });
            $doc.on("click", "input", function () {     //聚焦input框时
                var $this = $(this);
                if ($this.hasClass("red_border") || $this.parent().hasClass("red_border")) {        //判断是否有红框这个属性
                    $this.removeClass("red_border").parent().removeClass("red_border");      //有就去掉
                }
            });
        }

        //由于ajax异步请求的问题处理
        function asynchronousProcessing(flag,mask) {
            if (!flag) {
                return false
            } else {
                var $newCarNumP = $(".newCarNum").parents(".newCar-receipt"),
                    invoiceDateP = $(".invoiceDate").parents(".receipt-time"),
                    prev =  $(".choose_prov").val();
                addHeadStyle(1);
                if (prev == "北京市") {
                    $(".recognizeeN").removeClass("dis")
                } else {
                    $(".recognizeeN").addClass("dis")
                }
                if(mask){
                    if(mask == 1){
                        $newCarNumP.removeClass("dis");
                        invoiceDateP.removeClass("dis");
                    }else if(mask == 2){
                        $newCarNumP.addClass("dis");
                        invoiceDateP.addClass("dis");
                    }
                }else{
                    $newCarNumP.addClass("dis");
                    invoiceDateP.addClass("dis");
                }
                $(".bInfo").hide();
                $(".carInfo").show();
            }
        }
        //向安心请求查询数据
        function getDataForInsurance(carNo,ownerN,fl,mask) {
            $.ajax({
                url: BASE_PATH + '/insurance/anxin/flow/search-vehicleInfo',
                type: "GET",
                contentType: "application/json",
                dataType:'text',
                data: {
                    carNo: carNo,
                    ownerName: ownerN
                },
                success: function (result) {
                    $('.second-third-mix').html(result);
                    init_mode();
                    asynchronousProcessing(fl,mask);
                }
            });
        }
    });
});