/**
 * Created by huage on 2016/12/19.
 */
$(function () {
    seajs.use(['date', 'art', 'dialog'], function (dp, at, dg) {
        $('.carInfo').hide();
        // 悬浮框
        dg.titleInit();

        var modeV = util.getPara("mode");   //拿到mode的值
        // 异步加载页面数据
        init_data(dg);
        /*=== 初进页面加载的方法 开始 ==*/
        init_start(modeV);
        /*=== 初进页面加载的方法 结束 ==*/

        /*=== 初进页面点击方法 开始 ==*/
        init_click(dg, modeV);
        /*=== 初进页面点击方法 结束 ==*/

        //进页面判断是否是返回修改的
        init_fillData();
    })
});
//初进页面加载
function init_start(modeV) {
    var process_nav_true = $(".process-nav-true"),
        process_nav_virtual = $(".process-nav-virtual");
    if (modeV == '3') {
        process_nav_true.remove();
        process_nav_virtual.show();
    } else {
        process_nav_true.show();
        process_nav_virtual.remove();
    }
}


// 初始化基本数据
function init_data(dg) {
    SmartAjax.get({
        url: "/legend/smart/bihu/recharge/getShopInfo",
        success: function (result) {
            if (result.success) {
                var num = result.data.remainNum;
                $('.remain-time').text(num);
            } else {
                dg.warn(result.errorMsg)
            }
        }
    });
}

//进页面遍历省份的code
function init_fillData() {
    var choose_prov = $('.choose_prov'),
        session_prov = sessionStorage.getItem('prov'),
        session_city = sessionStorage.getItem('city'),
        session_cityCode = sessionStorage.getItem('cityCode'),
        session_provCode = sessionStorage.getItem('provCode'),
        session_carNum = sessionStorage.getItem('carNum');
    if (session_prov) {
        choose_prov.val(session_prov).data('code', session_provCode);
        $('.choose_region').val(session_city).data('code', session_cityCode);
        $('.car_num').val(session_carNum);
    }
}

/*=== 点击事件开始  ==*/
function init_click(dg, modeV) {
    var $doc = $(document);
    // 点击我要充值
    //$doc.on('click', '.want-recharge', function () {
    //    var url = '/legend/smart/bihu/recharge/index';
    //    window.location.href = Smart.mode_url(url);
    //});
    // 点击 使用记录
    $doc.on('click', '.want-used-list', function () {
        window.location.href = '/legend/smart/bihu/usedView/recharge-list';
    });
    $doc.on("click", "input", function () {     //聚焦input框时
        var $this = $(this);
        if ($this.hasClass("red_border") || $this.parent().hasClass("red_border")) {        //判断是否有红框这个属性
            $this.removeClass("red_border").parent().removeClass("red_border");      //有就去掉
        }
    });
    // 点击智能查询
    $doc.on('click', '.js-search-button', function () {
        var $prov = $(".choose_prov"),
            prev = $prov.val(),
            $choose_region = $(".choose_region"),
            region = $choose_region.val(),
            $carNum = $('.car_num'),
            carNum = $.trim($carNum.val()),
            insuredProvinceCode = $prov.data("code"),          // 投保人所在省code
            cityCode = $choose_region.data('code');  //投保人所在城市code
        data = {
            cityCode: cityCode,
            licenseNo: carNum
        };
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

        if (!$carNum.hasClass('bc')) {        //若车牌号为空，弹出框提示
            dg.confirm(
                '抱歉，由于您尚未取得车牌，无法使用智能开单功能 只能委屈您使用手工开单功能咯～   '
                , function () {

                }, function () {
                    var url = BASE_PATH + '/insurance/anxin/flow/insurance-flow';
                    window.location.href = Smart.mode_url(url);
                }, ['取消', '前往手工开单'])
        } else if(!carNum) {
            try {
                //数据校验
                Smart.judge_input($carNum, carNum, "填写车牌号", 'red_border', '车牌号字数超过最大限制', carNum.length > 10);
            } catch (msg) {
                dg.warn(msg);
                return;
            }
        }else{
            $.ajax({
                url: BASE_PATH + '/insurance/anxin/api/check-license',
                type: "GET",
                contentType: "application/json",
                data: {
                    provincesCode: insuredProvinceCode,
                    cityCode: cityCode,
                    licenseNo: carNum
                },
                success: function (result) {
                    if (!result.success) {
                        dg.warn(result.errorMsg);
                        return false;
                    }else{
                        ajax_bihuSearch(dg,region,cityCode,insuredProvinceCode,carNum,prev);
                    }
                }
            });
        }
    })
}
//车牌匹配成功调接口
function ajax_bihuSearch(dg,region,cityCode,insuredProvinceCode,carNum,prev) {
    SmartAjax.post({
        url: '/legend/smart/bihu/flow/bihuSearch',
        data: JSON.stringify(data),
        contentType: 'application/json',
        success: function (result) {
            var next_url = '/legend/smart/bihu/flow/bihu-flwo-two';
            var return_url =  Smart.mode_url(next_url);

            sessionStorage.setItem('prov', prev);
            sessionStorage.setItem('city', region);
            sessionStorage.setItem('cityCode', cityCode);
            sessionStorage.setItem('provCode', insuredProvinceCode);
            sessionStorage.setItem('carNum', carNum);
            if (result.success) {
                var post_data = result.data;
                var data = JSON.stringify(post_data);
                if (result.message) {
                    dg.success(result.message,function () {
                        post(return_url, {data: data});
                    });
                }else{
                    dg.success('智能开单功能已成功匹配出'+carNum+'去年的投保信息，信息供参考，感谢您的使用！',function () {
                        post(return_url, {data: data});
                    });
                }
            } else {
                if (result.code == '110100024') {
                    dg.confirm(result.message,function () {
                        window.location.href = BASE_PATH + '/smart/bihu/recharge/index'
                    },function () {
                        var url = BASE_PATH + '/insurance/anxin/flow/insurance-flow';
                        window.location.href = Smart.mode_url(url);
                    },['去充值','前往手工开单']);
                } else {
                    dg.confirm(result.message, function () {

                    },function () {
                        var url = BASE_PATH + '/insurance/anxin/flow/insurance-flow';
                        window.location.href = Smart.mode_url(url);
                    },['我知道了','前往手工开单']);
                }
            }

        }
    });
}

/*=== 点击事件结束  ==*/
function post(action, data) {
    var temp = document.createElement("form");
    temp.action = action;
    temp.method = "post";
    temp.style.display = "none";
    for (var x in data) {
        var opt = document.createElement("textarea");
        opt.name = x;
        opt.value = data[x];
        temp.appendChild(opt);
    }
    document.body.appendChild(temp);
    temp.submit();
}
