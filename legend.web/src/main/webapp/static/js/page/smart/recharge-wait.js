/**
 * Created by huage on 2016/12/21.
 */
$(function () {
    seajs.use('dialog', function (dg) {
        var recordOne = 1,              //状态判断常量
            recordTwo = 2,
            recordThree = 3,
            recordFour = 4;
        var intVal_obj;//定时器
        /*=====初进页面 开始=====*/
        addHeadStyle(2);
        var recordResult = $('#state').val(),
            rechargeNumber = $('#recharge-number').val();
        judge_state();
        /*=====初进页面 开始=====*/

        /*===判断状态展示页面的流程图 开始===*/
        function judge_state() {
            if (intVal_obj) {
                // 关闭定时器
                if (recordResult != recordOne) {
                    clearInterval(intVal_obj);
                }
            } else {
                // 定时器开启
                if (recordResult == recordOne) {
                    interval_func();
                }
            }
            changeState(recordResult)
        }

        /*===判断状态展示页面的流程图 结束===*/

        /*===页面的流程图切换方法 开始===*/
        // i = 1 支付成功，i= 3,4 全勾选
        function changeState(step) {
            var liThis = $('.process-icon'),
                lineThis = $('.line ');
            var have_index = 0;
            if (step == recordOne) {
                have_index = 2;
                // 下一步的转圈圈
                liThis.eq(2).removeClass("aliPay-receipt-gray").addClass('aliPay-receipt');
            } else {
                if (step == recordThree || step == recordFour) {
                    have_index = 4;
                }
                setTimeout(function () {
                    window.location.href = BASE_PATH + '/smart/bihu/recharge/judge-go?rechargeNumber=' + rechargeNumber;
                }, 3000);
            }

            $.each(liThis, function (i, li_obj) {
                if (i < have_index) {
                    if ($(li_obj).hasClass("aliPay-receipt") || $(li_obj).hasClass("payState-unExecute")) {
                        $(li_obj).removeClass('payState-unExecute').removeClass('aliPay-receipt').addClass('process-paying');
                        $(li_obj).siblings("div").html("");
                    }
                }
            });
            $.each(lineThis, function (i, line_obj) {
                if (i < have_index) {
                    if ($(line_obj).hasClass("line-bgColor-gray")) {
                        $(line_obj).removeClass('line-bgColor-gray').addClass('line-bgColor-green');
                    }
                }
            });


            // 下一步的转圈圈
            //liThis.eq(step+1).removeClass("aliPay-receipt-gray").addClass('aliPay-receipt');
            //lineThis.eq(step).addClass('line-bgColor-green').removeClass('line-bgColor-gray');
            //liThis.eq(step).removeClass('aliPay-receipt').addClass('process-paying');
            //if(step == 3){
            //    liThis.eq(step).removeClass('payState-unExecute').addClass('process-paying');
            //}
        }

        /*===页面的流程图切换方法 结束===*/
        function interval_func() {
            var every_time = 2000,          //setInterval执行的时间
                maxSet = 10,                //执行的最多的次数
                intVal = 0;                //初始化
                intVal_obj = setInterval(function () {
                    intVal++;
                    if (intVal > maxSet) {
                        clearInterval(intVal_obj);
                    }
                    SmartAjax.post({
                        url: BASE_PATH + '/smart/bihu/recharge/confirmPay',
                        data: {
                            rechargeNumber: rechargeNumber
                        },
                        success: function (result) {
                            if (result.success) {
                                recordResult = result.data.billStatus;
                                judge_state();
                            } else {
                                dg.warn(result.message);
                            }
                        }
                    });
                }, every_time);
        }
    })
});
