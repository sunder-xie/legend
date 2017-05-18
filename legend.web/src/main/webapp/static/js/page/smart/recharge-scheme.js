/**
 * Created by huage on 2016/12/20.
 */
seajs.use(['art', 'dialog'], function (at, dg) {
    var $doc = $(document),
        feeRecharge,
        NumRecharge,
        $choosePayWay = $('.choose-payWay'),            //选择支付方式页面
        $rechargeBox = $('.recharge-box'),              //选择充值方案页面
        chargeMode,                                     //1为快速充值，2为自定义充值
        $liChecked,
        $inputMoney,
        trimV,
        liCheckedL;
    /*============ 进页面执行的方法 开始=========*/
    //进页面展示选择充值方案页面
    $rechargeBox.removeClass('dis');
    init_load();
    click_event();
    blur_event();
    /* ===========进页面执行的方法 结束===========*/


    /*=== 进页面执行的方法 开始 ===*/
    function init_load() {
        //调快速充值的列表接口
        var url1 = BASE_PATH +'/smart/bihu/recharge/getRechargeRuleList',
            $class = $('.fast-recharge');
        SmartAjax.get({
            url: url1,
            success: function (result) {
                ajax_callback($class, result)
            }
        });

        //调门店信息接口
        var url2 = BASE_PATH +'/smart/bihu/recharge/getShopInfo';
        SmartAjax.get({
            url: url2,
            success: function (result) {
                if (result.success) {
                    var num = result.data.remainNum;
                    $('.remain-number').text(num);
                } else {
                    dg.warn(result.errorMsg)
                }
            }
        })
    }

    /*== 进页面执行的方法 结束 ===*/


    /*======= 事件 开始 =======*/
    //点击事件
    function click_event() {
        //点击快速充值
        $doc.on('click', '.fast-recharge-li', function () {
            var $this = $(this);
            if ($this.hasClass('li-checked')) {
                $this.removeClass('li-checked');
            } else {
                $('.input-money').val('');
                $this.addClass('li-checked').siblings().removeClass('li-checked');
            }
        });
        //点击提交订单
        $doc.on('click', '.js-search-button', function () {
            $liChecked = $('.fast-recharge-li.li-checked');
            liCheckedL = $liChecked.length;
            $inputMoney = $('.input-money').val();
            trimV = $.trim($inputMoney);
            if (liCheckedL <= 0 && !trimV) {
                dg.warn('请选择充值金额');
                return;
            } else {
                if (liCheckedL > 0) {
                    chargeMode = 1;
                    feeRecharge = $liChecked.data('fee');
                } else if (trimV) {
                    chargeMode = 2;
                    feeRecharge = trimV;
                }
                $('.must-pay-money').text(feeRecharge.toFixed(2));
            }
            change_page($choosePayWay, $rechargeBox);
            addHeadStyle(1);
        });

        $doc.on('click', '.back-recharge', function () {
            change_page($rechargeBox, $choosePayWay)
        });

        $doc.on('click', '.go-pay', function () {
            var url,
                data;
            if (chargeMode == 1) {
                url = BASE_PATH + '/smart/bihu/recharge/speedRecharge';
                var rechargeLiId = $liChecked.data('id');
                data = {
                    rechargeRuleId: rechargeLiId
                };
            } else if (chargeMode == 2) {
                url = BASE_PATH + '/smart/bihu/recharge/feeRecharge';
                data = {
                    rechargeFee: trimV
                };
            }
            before_ajax(url, data);
        });
        $doc.on('click','.back-smart-index',function () {
                var url = BASE_PATH + '/smart/bihu/flow/bihu-flow';
                window.location.href = Smart.mode_url(url);
        });
    }

    // 移开焦点事件
    function blur_event() {
        $doc.on('blur', '.input-money', function () {
            var $this = $(this),
                $thisVal = $this.val(),
                trimVal = $.trim($thisVal),
                checkedLi = $('.fast-recharge-li'),
                minVal = $('.minimumFee').text();            // 充值的最小临界值
            if (!trimVal) {       //不能输入空格纯空格
                $this.val(trimVal);
                return;
            }
            if (/^[1-9]\d*$/.test(trimVal)) {         //智能能输入正整数
                var parseVal = parseInt(trimVal, 10);
                if (parseVal >= minVal) {
                    checkedLi.removeClass('li-checked');
                } else {
                    dg.warn('抱歉，充值金额至少为'+minVal+'元，请调整充值金额');
                    $this.val(Math.abs(Math.round(trimVal)));
                    return;
                }
            } else {
                dg.warn('请输入整数金额进行充值');
                $this.val(Math.abs(Math.round(trimVal)));
                return;
            }
        })
    }

    /*=========事件  结束=======*/

    /*=====接口调通的回调函数 开始======*/
    function ajax_callback($class, result) {
        if (result.success) {
            var Tpl = at("fastRechargeList", result);
            $class.html(Tpl);
        } else {
            dg.warn(result.message)
        }
    }
    
    //去支付订单创建成功后的回调函数
    function submit_callback(result) {
        if (result.success) {
            feeRecharge = result.data.rechargeFee;
            NumRecharge = result.data.rechargeNumber;
            setTimeout(dg.confirm('是否完成支付', function () {
                isConfirm(NumRecharge);
            }, function () {
                isConfirm(NumRecharge);
            }, ['是', '否']),1000);
            $('#openPay').attr('href',BASE_PATH + '/smart/bihu/recharge/alipay?payFee='+ feeRecharge + '&rechargeNumber=' + NumRecharge);
        } else {
            dg.warn(result.message)
        }
    }


    /*===========是否支付完成的确认框 开始============*/
    function  isConfirm(NumRecharge) {
        window.location.href = BASE_PATH + '/smart/bihu/recharge/judge-go?rechargeNumber='+NumRecharge;
    }
    /*===========是否支付完成的确认框 结束============*/
    
    
    
    /*=========ajax请求 开始=======*/
    function before_ajax(url, data) {
        SmartAjax.post({
            url:url,
            data: data,
            async:false,
            success: function (result) {
                if (result.success) {
                    submit_callback(result);
                } else {
                    dg.confirm(
                        result.errorMsg
                        , function () {
                        }, function () {
                            var url = BASE_PATH + '/insurance/anxin/flow/insurance-flow';
                            window.location.href = Smart.mode_url(url);
                        }, ['取消', '前往手工开单']);
                    //dg.fail(result.errorMsg);
                    return false;
                }
            }
        })
    }

    /*==========ajax请求 结束=======*/

    /*====页面切换  开始====*/
    function change_page(showPage, hidePage) {
        showPage.removeClass('dis');
        hidePage.addClass('dis');
    }
    /*====页面切换  结束====*/
});





