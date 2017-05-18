/**
 * Created by zz on 2016/7/11.
 */
$(function () {
    var doc = $(document);
    var time;
    $('input[name="order"]').focus();

    //判断时间重置计时器
    function timer() {
        clearTimeout(time);
        time = window.setTimeout(function () {
            location.reload();
        }, 20000);
    }

    $('.keydown').keydown(function () {
        timer();
    });

    //施工单条码
    doc.on('click', '.js-scan-order', function () {
        var order = $('input[name="order"]').val(), length = $(this).val().length, orderReg = /^SG/, workReg = /^YG/;
        if (orderReg.test(order) && length == 17) {
            $.ajax({
                url: BASE_PATH + '/workshop/process/workorder/scan?workOrderSn=' + order,
                success: function (result) {
                    if (result.success) {
                        $(".scan-result").show();
                        $('input[name="order"]').attr('disabled', true);
                        if (result.data == 0) {
                            $('.order-result').text(result.message);
                        } else {
                            $('.order-result').text("施工单扫描成功，请扫描工牌条码");
                            $(".step2-scan").show();
                            $('input[name="workcard"]').focus();
                            $('button.js-scan-order').remove();
                        }
                    } else {
                        $(".scan-result").show();
                        $('.order-result').text(result.message);
                        $('input[name="order"]').val("").focus();
                    }
                    timer();
                }
            });
        }
        else if (workReg.test(order) && length == 11) {//工牌扫描
            $.ajax({
                url: BASE_PATH + '/workshop/process/card/scan?cardNum=' + order,
                success: function (result) {
                    $(".scan-result3").show();
                    if (result.success) {
                        $(".workerInfo").show();
                        $('input[name="order"]').attr('disabled', true);
                        $('.worker-result').text(result.message);
                        $(".workerName").text(result.data.name)
                        $(".cardName").text(result.data.carNum);
                        $('button.js-scan-order').remove();
                        seajs.use("art",function(art){
                          var html =  art('workerResultTpl',{json:result});
                            $("#workerResult").html(html);
                        });
                    } else {
                        $('.worker-result').text(result.message);
                        $('input[name="order"]').val("").focus();
                    }
                    timer();
                }
            });
        }
    });
    //施工单条码扫描
    var save = true;
    $('input[name="order"]').on('input', function () {
        var order = $(this).val(), length = $(this).val().length, orderReg = /^SG/, workReg = /^YG/;
        //施工单扫描，走以前的老逻辑
        if (orderReg.test(order) && length == 17 && save) {
            save = false;
            $.ajax({
                url: BASE_PATH + '/workshop/process/workorder/scan?workOrderSn=' + order,
                success: function (result) {
                    if (result.success) {
                        $(".scan-result").show();
                        $('input[name="order"]').attr('disabled', true);
                        if (result.data == 0) {
                            $('.order-result').text(result.message);
                        } else {
                            $('.order-result').text("施工单扫描成功，请扫描工牌条码");
                            $(".step2-scan").show();
                            $('input[name="workcard"]').focus();
                            $('button.js-scan-order').remove();
                        }
                    } else {
                        $(".scan-result").show();
                        $('.order-result').text(result.message);
                        $('input[name="order"]').val("").focus();
                    }
                    save = true;
                    timer();
                }
            });
        }
        else if (workReg.test(order) && length == 11 && save) {//工牌扫描
            save = false;
            $.ajax({
                url: BASE_PATH + '/workshop/process/card/scan?cardNum=' + order,
                success: function (result) {
                    $(".scan-result3").show();
                    if (result.success) {
                        $(".workerInfo").show();
                        $('input[name="order"]').attr('disabled', true);
                        $('.worker-result').text(result.message);
                        $(".workerName").text(result.data.name)
                        $(".cardName").text(result.data.carNum);
                        $('button.js-scan-order').remove();
                        seajs.use("art",function(art){
                            var html =  art('workerResultTpl',{json:result});
                            $("#workerResult").html(html);
                        });
                    } else {
                        $('.worker-result').text(result.message);
                        $('input[name="order"]').val("").focus();
                    }
                    timer();
                    save = true;
                }
            });
        }
    });

    //员工条码
    doc.on('click', '.js-scan-card', function () {
        var card = $('input[name="workcard"]').val();
        $.ajax({
            url: BASE_PATH + '/workshop/process/manager/scan?cardNum=' + card,
            success: function (result) {
                if (result.success) {
                    window.clearTimeout(time);
                    $(".scan-result1").show();
                    $('input[name="workcard"]').attr('disabled', true);
                    $('.card-result').text(result.message);
                    $(".step3-scan").show();
                    $('input[name="process"]').focus();
                    $('button.js-scan-card').remove();
                } else {
                    window.clearTimeout(time);
                    $(".scan-result1").show();
                    $('.card-result').text(result.message);
                    $('input[name="workcard"]').val("").focus();
                }
                timer();
            }
        });
    });

    //员工条码扫描
    $('input[name="workcard"]').on('input', function () {
        var length1 = $(this).val().length;
        if (length1 == 11 && save) {
            save = false;
            var card = $('input[name="workcard"]').val();
            $.ajax({
                url: BASE_PATH + '/workshop/process/manager/scan?cardNum=' + card,
                success: function (result) {
                    if (result.success) {
                        window.clearTimeout(time);
                        $(".scan-result1").show();
                        $('input[name="workcard"]').attr('disabled', true);
                        $('.card-result').text(result.message);
                        $(".step3-scan").show();
                        $('input[name="process"]').focus();
                        $('button.js-scan-card').remove();
                    } else {
                        window.clearTimeout(time);
                        $(".scan-result1").show();
                        $('.card-result').text(result.message);
                        $('input[name="workcard"]').val("").focus();
                    }
                    save = true;
                    timer();
                }
            });
        }
    });

    //工序条码
    doc.on('click', '.js-scan-process', function () {
        var order = $('input[name="order"]').val();
        var card = $('input[name="workcard"]').val();
        var process = $('input[name="process"]').val();
        $.ajax({
            url: BASE_PATH + '/workshop/process/process/scan?barCode=' + process + '&workOrderSn=' + order + '&cardNum=' + card,
            success: function (result) {
                if (result.success) {
                    window.clearTimeout(time);
                    $(".scan-result2").show();
                    $('input[name="process"]').attr('disabled', true);
                    $('.process-result').text(result.message);
                    $('button.js-scan-process').remove();
                } else {
                    window.clearTimeout(time);
                    $(".scan-result2").show();
                    $('.process-result').text(result.message);
                    $('input[name="process"]').val("").focus();
                }
                timer();
            }
        });
    });

    //工序条码扫描
    $('input[name="process"]').on('input', function () {
        var length2 = $(this).val().length;
        if (length2 == 13 && save) {
            save = false;
            var order = $('input[name="order"]').val();
            var card = $('input[name="workcard"]').val();
            var process = $('input[name="process"]').val();
            $.ajax({
                url: BASE_PATH + '/workshop/process/process/scan?barCode=' + process + '&workOrderSn=' + order + '&cardNum=' + card,
                success: function (result) {
                    if (result.success) {
                        window.clearTimeout(time);
                        $(".scan-result2").show();
                        $('input[name="process"]').attr('disabled', true);
                        $('.process-result').html(result.message);
                        $('button.js-scan-process').remove();
                    } else {
                        window.clearTimeout(time);
                        $(".scan-result2").show();
                        $('.process-result').text(result.message);
                        $('input[name="process"]').val("").focus();
                    }
                    save = true;
                    timer();
                }
            });
        }
    });
})