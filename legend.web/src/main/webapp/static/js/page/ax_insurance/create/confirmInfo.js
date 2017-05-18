/**
 * Created by huage on 16/8/30.
 * 真实投保
 */
$(function () {
    var colorDialog = null,
        thirdPartyDialog = null,
        thirdPartyTpl = $('.modeV').val() == 2 ? '#servicePackageThirdPartyProtocolTpl' : '#servicePackageThirdPartyProtocolWithRewardTpl';
    addHeadStyle(3);
    var isSmart = sessionStorage.getItem('isSmart');
    seajs.use('art', function (at) {
        at.helper('toFixed', function (num, limit) {
            limit = limit || 2;
            return (+num).toFixed(limit);
        });
        at.helper("JsonStringify", function (Json) {
            return JSON.stringify(Json);
        });
        //为模版增加转换时间戳的方法
        at.helper("toTime", function (Time, state) {
            var time = new Date(Time);
            var year = time.getFullYear();      //年
            var mon = time.getMonth() + 1;        //月
            var mon2 = mon < 10 ? "0" + mon : mon;
            var date = time.getDate();          //日
            var date2 = date < 10 ? "0" + date : date;
            var hour = time.getHours();         //时
            var hour2 = hour < 10 ? "0" + hour : hour;
            var min = time.getMinutes();        //分
            var min2 = min < 10 ? "0" + min : min;
            var sec = time.getMinutes();        //秒
            var sec2 = sec < 10 ? "0" + sec : sec;
            if (state == 1) {
                return year + "." + mon2 + "." + date2;
            } else if (state == 2) {
                return year + "-" + mon2 + "-" + date2;
            } else {
                return year + "-" + mon2 + "-" + date2 + " " + hour2 + ":" + min2 + ":" + sec2;
            }
        });

        // 服务包提示信息价格加高亮
        at.helper('setMark', function (data) {
            if (data == null) {
                return data;
            }
            return data.replace(/(\d+)元/, function ($0, $1) {
                return '<span class="mark">' + $1 + '</span>元';
            });
        });
    });

    seajs.use(["art", "dialog"], function (at, dg) {
        dg.titleInit();
        var winScrollTop = 0;
        var result = JSON.parse(sessionStorage.getItem("result_data"));
        var mode = $(".modeV").val();
        var allData = at("allDataTpl", result);
        var insuranceFee,
            jqInsuranceFee;
        $(".allData").html(allData);
        var premiumData = at("premiumTpl", result);
        $(".premium").html(premiumData);
        var resultData = result.data;
        var province_length = $('#province').length;
        var FormDTOList = resultData.insuranceFormDTOList;
        for (var i = 0; i < FormDTOList.length; i++) {
            if (FormDTOList[i].insuranceType == 2) {
                insuranceFee = FormDTOList[i].insuredFee;
            } else if (FormDTOList[i].insuranceType == 1) {
                jqInsuranceFee = FormDTOList[i].insuredFee;
            }
        }
        //进页面，若配送信息与被保人一致。
        isCheck_same_insured(true);
        if (jqInsuranceFee) {
            $('.jq-insurance').text(jqInsuranceFee.toFixed(2))
        }
        if (insuranceFee) {
            $('.sy-insurance').text(insuranceFee.toFixed(2))
        }
        if (mode == 2 || mode == 0) {
            if (insuranceFee && insuranceFee < 1400) {              //保费小于1400，展示不一样
                $(".backG .sorry").removeClass("dis");
                thirdPartyTpl = '#servicePackageThirdPartyProtocolWithRewardTpl';
                mode = 1;
            } else {
                $(".backG .selected").removeClass("dis");
                if (mode == 2) {
                    $(".triangle").addClass("dis");
                }
            }
        }
        var ApplyNo1 = $(".table1").data("outerinsuranceapplyno"), //投保单号
            ApplyNo2 = $(".table2").data("outerinsuranceapplyno"),//投保单号
            id = resultData.id,
            agentId = resultData.agentId,
            agentName = resultData.agentName,
            InsuredProvince = resultData.insuredProvince,
            InsuredCity = resultData.insuredCity,
            InsuredProvinceCode = resultData.insuredProvinceCode,
            InsuredCityCode = resultData.insuredCityCode,
            formId1 = $(".table1").data("formid"),
            formId2 = $(".table2").data("formid"),
            vehiclePhone = resultData.vehicleOwnerPhone;



        // 获取优惠券列表
        if (insuranceFee) {
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/coupon/coupon-list",
                data: {
                    amount: insuranceFee,
                    mobile: vehiclePhone
                },
                success: function (result) {
                    if (result.success) {
                        var allData = at("couponTpl", result);
                        $(".coupon-list").html(allData);
                        $('.question-mark').hover(function () {
                            $(this).parents('.coupon-list-box').siblings('.coupon-use-describe').show();
                        }, function () {
                            $(this).parents('.coupon-list-box').siblings('.coupon-use-describe').hide();
                        });
                        init_click_coupon();
                    }
                }
            });
        }
        function init_click_coupon() {
            //点击时校验优惠券能否用
            $(document).on('click', '.coupon-list-li:not(.disabled)', function () {
                var $this = $(this),
                    $thisState = $this.data('state'),
                    freezeTime = $this.data('freezetime'),
                    couponId = $this.data('id'),
                    $deductibleAmount = $('.coupon-amount'),
                    deductibleAmount = $this.data('deductibleamount');
                if ($this.hasClass('coupon-checked')) {
                    $this.removeClass('coupon-checked');
                    $deductibleAmount.text("0.00");
                } else {
                    $this.addClass('coupon-checked').siblings().removeClass('coupon-checked');
                    $deductibleAmount.text(deductibleAmount.toFixed(2));
                }
                if ($thisState == 2) {
                    dg.confirm('由于您于' + toTime(freezeTime) + '使用过此优惠劵 优惠劵已被投保单占用 请确认是否需要释放优惠劵', function () {
                        // 确认往后台掉接口
                        $.ajax({
                            url: BASE_PATH + "/insurance/anxin/coupon/coupon-thaw",
                            data: {
                                couponId: couponId
                            },
                            success: function (result) {
                                if (result.success) {
                                    $this.data('state', 1);
                                    dg.success('优惠券释放成功');
                                } else {
                                    dg.fail(result.errorMsg);
                                }
                            }
                        });
                    }, function () {
                        $this.removeClass('coupon-checked');
                        $deductibleAmount.text('0.00');
                    })
                }
            });
        }

        $(document).on("click", function () {
            $(".prevUl,.cityUl,.areaUl").addClass("dis");
            $(".warranty ul").addClass("dis");
            $(".choose_btn").addClass("s_up").removeClass("s_down");
            $(".zj_btn").addClass("s_up").removeClass("s_down");

        })
            .on("click", ".all_inf_w_btn", function () {
                var $ul = $(this).children("ul");
                $ul.empty();
                if ($ul.hasClass("dis")) {
                    $(this).children(".zj_btn").removeClass("s_up").addClass("s_down");
                    $ul.removeClass("dis");
                    $.ajax({
                        url: BASE_PATH + "/insurance/anxin/api/getInsuredCertType",
                        success: function (result) {
                            if (result.success) {
                                var data = result.data;
                                for (var i in data) {
                                    $ul.append("<li" + " data-typeCode=" + i + " > " + data[i] + "</li>");
                                }
                            }
                        }
                    })
                } else {
                    $(this).children(".zj_btn").addClass("s_up").removeClass("s_down");
                    $ul.addClass("dis");
                }
            })
            .on("click", ".pack-check", function () {           //点击服务包的选择框
                var $sibling = $(this).parent("div").siblings("div").find(".pack-check");
                $sibling.attr("checked", false);
                var colorType = $(this).next('.pack-content').find('.color-type').val();
                $('.colour-type').val('');
                //防冻液颜色确认
                if (colorType && $(this).is(":checked")) {
                    var html = $('#dialogTpl').html();
                    colorDialog = dg.open({
                        area: ['400px', '255px'],
                        content: html,
                        closeBtn: 0
                    })
                }
            })
            .on("click", ".back-insure", function () {
                $(".pack-check").each(function () {
                    var $check = $(this).prop("checked");
                    if ($check) {
                        var Json = $(this).siblings("div.pack-content").data("json");
                        var selectedPack = at("selectedPackTpl", {data: Json});
                        $(".SelectedPack").html(selectedPack);
                        $(".backG").addClass("dis");
                        $(".re_selected").removeClass("dis");
                        $(window).scrollTop(winScrollTop);
                    }
                });
                $(".sendPack-all").hide();
                $(".confirmInfo").show();
            })
            .on("click", ".all_inf_w_btn li,.warranty li", function () {
                var $this = $(this),
                    liV = $this.text(),
                    liCode = $this.data("typecode"),
                    $thisParent = $this.parent('ul');
                $this.parents("ul").siblings(".zj_l").data("code", liCode).text(liV);
                if($thisParent.hasClass('js-traverseUl')){
                    isCheck_same_insured();
                }
            })
            .on("click", ".warranty", function (e) {
                stopPropagation(e);
                var $ul = $(this).children("ul");
                if ($ul.hasClass("dis")) {
                    $ul.removeClass("dis");
                    $(this).children(".zj_btn").addClass("s_down").removeClass("s_up")
                } else {
                    $ul.addClass("dis");
                    $(this).children(".zj_btn").removeClass("s_down").addClass("s_up")
                }
            })
            .on("click", ".prevK,.cityK,.areaK", function (e) {
                stopPropagation(e);
                $(this).siblings("div").children("ul").addClass("dis");
                $(this).siblings("div").children(".choose_btn").addClass("s_up").removeClass("s_down");
                var $ul = $(this).children("ul");
                if ($ul.hasClass("dis")) {
                    $ul.removeClass("dis");
                    $(this).children(".zj_btn").removeClass("s_up").addClass("s_down")
                } else {
                    $ul.addClass("dis");
                    $(this).children(".zj_btn").addClass("s_up").removeClass("s_down")
                }
            })
            .on("click", ".prevK", function () {
                var $ul = $(this).children(".prevUl");
                $ul.empty();
                var code = [];
                if (!$ul.hasClass("dis")) {
                    $.ajax({
                        url: BASE_PATH + "/insurance/anxin/api/getInsureProvinceCity",
                        data: {
                            regionParentCode: "000000"
                        },
                        success: function (result) {         // 先去获取到省
                            if (result.success) {
                                var data = result.data;
                                for (var i = 0; i < data.length; i++) {
                                    var str = "<li" + " data-code=" + data[i].regionCode + ">" + data[i].regionName + "</li>";
                                    $ul.append(str);
                                    code.push(data[i].regionCode);               //获取省对应的code
                                }
                            }else{
                                dg.fail(result.message)
                            }
                        }
                    })
                }
            })
            .on("click", ".cityK", function () {
                var $ul = $(this).children(".cityUl");
                var code = [],prevCode=$('.receiverProvince').data('code'),choosePrevCode=$(this).prev().find(".prev").data('code');
                if (!$ul.hasClass("dis")) {
                    $ul.empty();
                    if(choosePrevCode && choosePrevCode != prevCode){
                        prevCode=choosePrevCode;
                    }
                    $.ajax({
                        url: BASE_PATH + "/insurance/anxin/api/getInsureProvinceCity",
                        data: {
                            regionParentCode: prevCode
                        },
                        success: function (result) {         // 先去获取到省
                            if (result.success) {
                                var data = result.data;
                                for (var i = 0; i < data.length; i++) {
                                    var str = "<li" + " data-code2=" + data[i].regionCode + ">" + data[i].regionName + "</li>";
                                    $ul.append(str);
                                    code.push(data[i].regionCode);               //获取省对应的code
                                }
                            }else{
                                dg.fail(result.message)
                            }
                        }
                    })
                }
            })
            .on("click", ".areaK", function () {
                var $ul = $(this).children(".areaUl");
                var code = [],cityCode=$('.receiverCity').data('code'),chooseCityCode =$(this).prev().find(".city").data('code2');
                if (!$ul.hasClass("dis")) {
                    $ul.empty();
                    if(chooseCityCode && chooseCityCode != cityCode){
                        cityCode=chooseCityCode;
                    }
                    $.ajax({
                        url: BASE_PATH + "/insurance/anxin/api/getInsureProvinceCity",
                        data: {
                            regionParentCode: cityCode
                        },
                        success: function (result) {         // 先去获取到省
                            if (result.success) {
                                var data = result.data;
                                for (var i = 0; i < data.length; i++) {
                                    var str = "<li" + " data-code3=" + data[i].regionCode + ">" + data[i].regionName + "</li>";
                                    $ul.append(str);
                                    code.push(data[i].regionCode);               //获取省对应的code
                                }
                            }else{
                                dg.fail(result.message)
                            }
                        }
                    })
                }
            })
            .on("click", ".prevUl li", function () {
                var $UL2 = $(this).parents(".prevK").siblings(".cityK").children(".cityUl");
                $UL2.empty();
                var liV = $(this).text();
                $(this).parents("ul").siblings(".prev").text(liV);
                var prevCode = $(this).data("code");
                $(this).parents("ul").siblings(".prev").data("code", prevCode);
                $(this).parents(".prevK").siblings(".cityK").children(".city").text("请选择市");
                $(this).parents(".prevK").siblings(".areaK").children(".area").text("请选择区");
                $.ajax({
                    url: BASE_PATH + "/insurance/anxin/api/getInsureProvinceCity",
                    data: {
                        regionParentCode: prevCode
                    },
                    success: function (json) {           //用省对应的code去获取市
                        if(json.success){
                            var data2 = json.data;
                            for (var j = 0; j < data2.length; j++) {
                                var str2 = "<li" + " data-code2=" + data2[j].regionCode + ">" + data2[j].regionName + "</li>";
                                $UL2.append(str2);
                            }
                        }else{
                            dg.fail(result.message)
                        }
                    }
                })
            })
            .on("click", ".cityUl li", function () {
                var $UL3 = $(this).parents(".cityK").siblings(".areaK").children(".areaUl");
                $UL3.empty();
                var liV = $(this).text();
                $(this).parents("ul").siblings(".city").text(liV);
                var cityCode = $(this).data("code2");
                $(this).parents("ul").siblings(".city").data("code2", cityCode);
                $(this).parents(".cityK").siblings(".areaK").children(".area").text("请选择区");
                $.ajax({
                    url: BASE_PATH + "/insurance/anxin/api/getInsureProvinceCity",
                    data: {
                        regionParentCode: cityCode
                    },
                    success: function (jsonD) {           //用省对应的code去获取市
                        if(jsonD.success){
                            var data3 = jsonD.data;
                            for (var j = 0; j < data3.length; j++) {
                                var str3 = "<li" + " data-code3=" + data3[j].regionCode + ">" + data3[j].regionName + "</li>";
                                $UL3.append(str3);
                            }
                        }else {
                            dg.fail(result.message)
                        }
                    }
                })
            })
            .on("click", ".areaUl li", function () {
            var liV = $(this).text();
            $(this).parents("ul").siblings(".area").text(liV);
            var areaCode = $(this).data("code3");
            $(this).parents("ul").siblings(".area").data("code3", areaCode);
        }).on("change", ".js-agree-protocol", function () {            //点击我同意
            var $thisState = $(this).prop("checked");
            var $third = $('.js-third-party-link');
            if ($thisState && $third.length) {
                $third.click();
            }
        }).on("click", ".triangle", function () {
            var $incentive = $(".incentive");
            if (mode == "0") {
                var isSelectedPackage = $("#isSelectedPackage").val();              //为1的话,说明选择了服务包
                if (isSelectedPackage) {
                    $incentive.addClass("dis")
                } else {
                    if ($incentive.hasClass("dis")) {
                        $incentive.removeClass("dis")
                    } else {
                        $incentive.addClass("dis")
                    }
                }
            } else if (mode == "1") {
                if ($incentive.hasClass("dis")) {
                    $incentive.removeClass("dis")
                } else {
                    $incentive.addClass("dis")
                }
            }
        }).on('click', '.sendCode', function () {
            var $insureInput = $('#insureInfo input[type="text"]'),
                $this = $(this),
                params = check_getVal($insureInput,'被保人信息'),
                is_inf1 = $this.parents().hasClass('all_inf1'),
                parent_div = $this.parents('.margin').siblings('div');
            if (params) {
                if (is_inf1) {
                    var all_input = parent_div.find('input[type="text"]');
                    var params_child = check_getVal(all_input,'投保人信息');
                    if(params_child){
                        $.extend(params,params_child);
                        send_code(params);
                    }
                } else {
                    var $child_div = parent_div.find('div[dynamic_name]');
                    var obj = {};
                    $child_div.each(function () {
                        var $this = $(this);
                        var $name = $this.attr('dynamic_name');
                        obj[$name] = $this.text();
                    });
                    $.extend(params,obj);
                    send_code(params);
                }
            }
        }).on("click", ".selectServicePack", function () {            //点击选择服务包,掉服务包列表的接口
            if (mode == "0" && !formId2) {
                dg.warn("您尚未选择商业险，无法进行服务包选择");
                return false;
            }
            winScrollTop = $(window).scrollTop();
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/api/getPackageList",
                data: {
                    formId: formId2,
                    isVirtualForm: false
                },
                success: function (result) {
                    if (result.success) {
                        var data = result.data;
                        if (data.length) {
                            var servicePack = at("servicePackTpl", result);
                            $(".pack-list").html(servicePack);
                            $(".sendPack-all").show();
                            $(".confirmInfo").hide();
                            $(window).scrollTop(0);
                        } else {
                            dg.warn("由于当前商业险保费低于1400元，无法匹配到服务包，请增加商业险保费！");
                            return false;
                        }
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            })
        }).on("click", ".submit", function () {       //点击提交
            var sameDriC = $(".sameDri").prop("checked"),
                sameInsureC = $(".sameInsure").prop("checked"),
                insureC2 = $(".insureC2").prop("checked"),
                vehicleOwnerName = $(".carN").text(),
                carParpers = $(".carPapers"),
                certType = carParpers.data("certtype"),
                certName = carParpers.text(),
                carPhone = $(".carPhone").text(),
                packId = $(".SelectedPack").children(".pack-content").data("id"),
                colourType = $('.colour-type').val(),
                vehicleOwnerCertCode = $(".papersNum").text(),
                inf2 = $(".all_inf2"),
                inf1 = $(".all_inf1"),
                insuredName,
                insuredPhone,
                insuredCertType,
                insuredCertTN,
                insuredCertCode,
                applicantName,
                applicantType,
                applicantTypeN,
                applicantCertCode,
                applicantPhone,
                VCode_num,
                VCode_num2,
                receiverName,
                receiverPhone,
                receiverType,
                forceReceiverType,
                receiptType;
            sessionStorage.removeItem("dimensionCodeD");
            if (sameDriC) {                          //勾了与车主一致
                insuredName = inf2.find(".recognizeeName").text();
                insuredPhone = inf2.find(".recognizeePhone").text();
                insuredCertType = inf2.find(".recognizeeType").data("code");
                insuredCertTN = inf2.find(".recognizeeType").text();                  //证件类型名称
                insuredCertCode = inf2.find(".recognizeeNum").text();
            } else {                                 //没勾
                insuredName = inf1.find(".recognizeeName").val();
                var insuredName2 = $.trim(insuredName);
                layout(insuredName2, "被保人姓名", ".recognizeeName", 30);
                if (flag) {
                    return false;
                }
                insuredPhone = inf1.find(".recognizeePhone").val();
                var insuredPhone2 = $.trim(insuredPhone);
                if (!insuredPhone2) {
                    dg.warn("请填写被保人手机号");
                    inf1.find(".recognizeePhone").addClass("red_c");
                    return false;
                } else if (!(/^1[3|4|5|7|8]\d{9}$/.test(insuredPhone2))) {
                    dg.warn("手机格式不正确");
                    inf1.find(".recognizeePhone").addClass("red_c");
                    return false;
                }
                insuredCertType = inf1.find(".recognizeeType").data("code");
                insuredCertTN = $.trim(inf1.find(".recognizeeType").text());
                if (!insuredCertTN) {
                    dg.warn("请选择被保人证件类型");
                    return false;
                }
                insuredCertCode = inf1.find(".recognizeeNum").val();
                var insuredCertCode2 = $.trim(insuredCertCode);
                layout(insuredCertCode2, "被保人证件编码", ".recognizeeNum", 20);
                if (flag) {
                    return false;
                }
            }
            if (sameInsureC) {                          //勾了与被保人一致
                applicantName = inf2.find(".applicantName").text();
                applicantType = inf2.find(".applicantType").data("code");         //投保人证件类型的code
                applicantTypeN = inf2.find(".applicantType").text();
                applicantCertCode = inf2.find(".applicantNum").text();
                applicantPhone = inf2.find(".applicantPhone").text();
                VCode_num = inf2.find(".VCode_num").val();
                VCode_num2 = $.trim(VCode_num);
                if (!VCode_num2 && province_length!=0) {
                    dg.warn("请填写手机号验证码");
                    return false;
                }
            } else {                                 //没勾
                applicantName = inf1.find(".applicantName").val();
                var applicantName2 = $.trim(applicantName);
                layout(applicantName2, "投保人姓名", ".applicantName", 30);
                if (flag) {
                    return false;
                }
                applicantType = inf1.find(".applicantType").data("code");
                applicantTypeN = $.trim(inf1.find(".applicantType").text());
                if (!applicantTypeN) {
                    dg.warn("请选择投保人证件类型");
                    return false;
                }
                applicantCertCode = inf1.find(".applicantNum").val();
                var applicantCertCode2 = $.trim(applicantCertCode);
                layout(applicantCertCode2, "投保人证件编码", ".applicantNum", 20);
                if (flag) {
                    return false;
                }
                applicantPhone = inf1.find(".applicantPhone").val();
                var applicantPhone2 = $.trim(applicantPhone);
                if (!applicantPhone2) {
                    dg.warn("请填写投保人手机号");
                    inf1.find(".applicantPhone").addClass("red_c");
                    return false;
                } else if (!(/^1[3|4|5|7|8]\d{9}$/.test(applicantPhone2))) {
                    dg.warn("手机格式不正确");
                    inf1.find(".applicantPhone").addClass("red_c");
                    return false
                }
                VCode_num = inf1.find(".VCode_num").val();
                VCode_num2 = $.trim(VCode_num);
                if (!VCode_num2 && province_length!=0) {
                    dg.warn("请填写手机号验证码");
                    return false;
                }
            }
            var shipping_address,
                receiverEmail;
            if (insureC2) {                          //勾了与。。一致
                receiverName = inf2.find(".receiverName").text();
                receiverPhone = inf2.find(".receiverPhone").text();
                receiverType = inf2.find(".warrantyType").data("code");   //商业保单类型
                forceReceiverType = inf2.find(".forceWarrantyType").data("code");   //交强险保单类型
                receiptType = inf2.find(".receipt-type").data("code");   //电子保单类型
                //地址框为class为dis时；
                if(!inf2.find('.js-post-address').eq(0).hasClass('dis')){
                    shipping_address = Insurance.shipping_Address(dg,inf2);
                    if(!shipping_address){
                        return false;
                    }
                }
                if(!inf2.find('.js-mail').hasClass('dis')){
                    receiverEmail = email_verify(inf2);
                    if(!receiverEmail){
                        return false;
                    }
                }
            } else {                                 //没勾
                receiverName = inf1.find(".receiverName").val();
                var receiverName2 = $.trim(receiverName);
                layout(receiverName2, "收件人姓名", ".receiverName", 30);
                if (flag) {
                    return false;
                }
                receiverPhone = inf1.find(".receiverPhone").val();
                var receiverPhone2 = $.trim(receiverPhone);
                if (!receiverPhone2) {
                    dg.warn("请填写收件人手机号码");
                    inf1.find(".receiverPhone").addClass("red_c");
                    return false;
                } else if (!(/^1[3|4|5|7|8]\d{9}$/.test(receiverPhone2))) {
                    dg.warn("手机格式不正确");
                    inf1.find(".receiverPhone").addClass("red_c");
                    return false
                }
                receiverType = inf1.find(".warrantyType").data("code");
                forceReceiverType = inf1.find(".forceWarrantyType").data("code");   //交强险保单类型
                receiptType = inf1.find(".receipt-type").data("code");   //电子保单类型
                //地址框为class为dis时；
                if(!inf1.find('.js-post-address').eq(0).hasClass('dis')){
                    shipping_address = Insurance.shipping_Address(dg,inf1);
                    if(!shipping_address){
                        return false;
                    }
                }
                if(!inf1.find('.js-mail').hasClass('dis')){
                    receiverEmail = email_verify(inf1);
                    if(!receiverEmail){
                        return false;
                    }
                }
            }
            var $jsAgreeProtocol = $(".js-agree-protocol").prop("checked");      //我同意是否勾选
            if (!$jsAgreeProtocol) {                  //若未勾选
                dg.warn('请勾选“我已阅读并同意以下声明”选项!');
                return false
            }
            var timestamp2 = sessionStorage.getItem("timestamp2");
            var Url;
            var isSelectedPackage = $("#isSelectedPackage").val();
            var CMode = mode;
            if (mode == "0") {
                CMode = mode == isSelectedPackage ? 2 : 1;   //没选服务包
            }
            if (mode == "2" && isSelectedPackage == null) {
                dg.warn("请选择服务包后再提交");
                return false;
            }
            if (CMode == 2) {
                Url = "PackageFeeSubmit";
            } else {
                Url = "FeeSubmit";
            }
            var $couponChecked = $('li.coupon-checked'),
                couponId = $couponChecked.data('id'),
                $couponCheckedL = $couponChecked.length,
                canUseCoupon = $('li.coupon-list-li:not(.disabled)').length,     //可使用的优惠券
                receiverProvince,
                receiverCity,
                receiverArea,
                receiverAddr;
                if(shipping_address){
                    receiverProvince=shipping_address['receiverProvince'];
                    receiverCity= shipping_address['receiverCity'];
                    receiverArea=shipping_address['receiverArea'];
                    receiverAddr=shipping_address['receiverAddr'];
                }
            var data = {
                id: id,
                cooperationMode: CMode,
                agentId: agentId,
                agentName: agentName,
                insuredName: insuredName,
                insuredPhone: insuredPhone,
                insuredCertType: insuredCertType,            //证件类型   传code
                insuredCertCode: insuredCertCode,
                applicantName: applicantName,
                tapplicantCertType: applicantType,
                vehicleOwnerCertName: certName,
                applicantCertName: applicantTypeN,
                insuredCertName: insuredCertTN,
                applicantCertCode: applicantCertCode,
                applicantPhone: applicantPhone,
                receiverName: receiverName,
                receiverPhone: receiverPhone,
                receiverProvince: receiverProvince,
                receiverCity: receiverCity,
                receiverArea: receiverArea,
                receiverAddr: receiverAddr,
                vehicleOwnerCertType: certType,
                vehicleOwnerName: vehicleOwnerName,
                vehicleOwnerPhone: carPhone,
                vehicleOwnerCertCode: vehicleOwnerCertCode,
                insuranceCompanyId: 1,               //保险公司id
                insuredProvince: InsuredProvince,        //投保人省
                insuredCity: InsuredCity,                //投保人市
                insuredProvinceCode: InsuredProvinceCode,    //投保人省code
                insuredCityCode: InsuredCityCode, //投保人市code
                insuranceFormToken: timestamp2,
                colourType: colourType,
                receiverEmail: receiverEmail,
                appValidateNo:VCode_num2,
                insuranceServicePackageParam: {
                    id: packId
                },
                insuranceFormDTOList: [
                    {
                        receiverType: forceReceiverType,
                        insuranceType: 1,
                        id: formId1,
                        outerInsuranceApplyNo: ApplyNo1,
                        receiptType:receiptType    //发票类型
                    },
                    {
                        receiverType: receiverType,
                        insuranceType: 2,
                        id: formId2,
                        outerInsuranceApplyNo: ApplyNo2,
                        receiptType:receiptType    //发票类型
                    }
                ]
            };
            //提示用户有未使用的优惠券
            if (canUseCoupon > 0) {               //有可使用的优惠券时
                if ($couponCheckedL <= 0) {
                    dg.confirm('当前车主有可用优惠劵未使用，是否确定立即提交订单', function () {
                        if (mode == "0" && mode != isSelectedPackage) {
                            dg.confirm("您尚未选择服务包，是否确定提交?", function () {
                                submit(Url, data, false);
                            })
                        } else {
                            submit(Url, data, false);
                        }
                    }, function () {

                    })
                } else {
                    couponCheck(mode, isSelectedPackage, couponId, CMode, Url, data)
                }
            } else {                  //无可使用的优惠券时
                if (mode == "0" && mode != isSelectedPackage) {
                    dg.confirm("您尚未选择服务包，是否确定提交?", function () {
                        submit(Url, data, false);
                    })
                } else {
                    submit(Url, data, false);
                }
            }
        }).on("click", ".three-back", function () {
            sessionStorage.setItem("basicId", id);
            var urlT;
            if (isSmart) {
                urlT = BASE_PATH + "/smart/bihu/flow/back-update";
            } else {
                urlT = BASE_PATH + "/insurance/anxin/flow/back-update";
            }
            window.location.href = urlT + "?id=" + id;
        }).on("focus", "input", function () {
            $(this).removeClass("red_c")
        });
        // 勾选与投保人一致
        $(document).on("click", ".sameInsure,.insureC2", function () {
            var $this = $(this);
            var sameDriC = $(".sameDri").prop("checked");
            var $all_inf1 = $(".all_inf1");
            var insuredName = $.trim($all_inf1.find(".recognizeeName").val());
            var insuredPhone = $.trim($all_inf1.find(".recognizeePhone").val());
            var insuredCertCode = $.trim($all_inf1.find(".recognizeeNum").val());
            if (!sameDriC) {            //如果 与车主一致 为未勾选状态
                if (!insuredName || !insuredPhone || !insuredCertCode) {
                    dg.warn("请将被保人信息填写完整");
                    return false;
                } else {
                    stateChange($this)
                }
            } else {
                stateChange($this)
            }
            if ($this.hasClass("insureC2")) {
                // inputEmpty($this, ".receiverName", ".receiverPhone", ".warrantyType", ".receiverProvince", ".receiverCity", ".receiverArea", ".receiverAddr");
                displaySameInfo(sameDriC, $this, ".receiverName", ".receiverPhone", ".recognizeeName", ".recognizeePhone")
            } else {
                inputEmpty($this, ".applicantName", ".applicantPhone", ".applicantNum", ".applicantType");
                displaySameInfo(sameDriC, $this, ".applicantName", ".applicantPhone", ".recognizeeName", ".recognizeePhone", ".recognizeeType", ".recognizeeNum", ".applicantType", ".applicantNum");
            }
        })
            .on("click", ".sameDC", function () {             //勾选与车主一致
                var $this = $(this);
                var $thisS = $this.prop("checked");
                var $sameInsure = $(".sameInsure");
                var $insureC2 = $(".insureC2");
                var $sameInsureSibling = $sameInsure.parents("p.detail_w").siblings(".car_detail");
                var $insureC2Sibling = $insureC2.parents("p.detail_w").siblings(".car_detail");
                if (!$thisS) {                    //未勾选与车主一致
                    $sameInsure.attr("checked", false);
                    $sameInsureSibling.children(".all_inf1").removeClass("dis");
                    $sameInsureSibling.children(".all_inf2").addClass("dis");
                    $insureC2.attr("checked", false);
                    $insureC2Sibling.children(".all_inf1").removeClass("dis");
                    $insureC2Sibling.children(".all_inf2").addClass("dis");
                } else {
                    checkCarAgain($sameInsure, ".applicantName", ".applicantPhone", ".applicantType", ".applicantNum");
                    checkCarAgain($insureC2, ".receiverName", ".receiverPhone");
                    inputEmpty($this, ".recognizeeName", ".recognizeePhone", ".recognizeeNum", ".recognizeeType");
                }
                stateChange($this);
            }).on("blur", "#insureInfo input", function () {
            var sameDriC = $(".sameDC").prop("checked");
            displaySameInfo(sameDriC, $(".sameInsure"), ".applicantName", ".applicantPhone", ".recognizeeName", ".recognizeePhone", ".recognizeeType", ".recognizeeNum", ".applicantType", ".applicantNum");
            displaySameInfo(sameDriC, $(".insureC2"), ".receiverName", ".receiverPhone", ".recognizeeName", ".recognizeePhone");
        })
            .on("click", "#insureInfo ul li", function () {
                var $thisText = $(this).text();
                var $thisCode = $(this).data("typecode");
                $(".sameInsure").parents("p.detail_w").siblings(".car_detail").children(".all_inf2").find(".applicantType").text($thisText).data("code", $thisCode)

            });
        $(document)
            .on('click', '.js-protocol-link', function () {
                var tplId = $(this).attr('href');
                protocolDialog(tplId);
                return false;
            })
            .on('click', '.js-third-party-link', function () {
                var $tpl = $(thirdPartyTpl),
                    protocolTpl = $tpl.html();
                thirdPartyDialog = dg.open({
                    shadeClose: false,
                    closeBtn: false,
                    area: ['700px', '500px'],
                    content: protocolTpl
                });
            })
            .on('click', '.js-agree-third-party-btn', function () {

                printLog(thirdPartyTpl);
                $('.js-agree-protocol').prop('checked', true);
                dg.close(thirdPartyDialog);
            });
        //勾选与。。一样的状态改变
        function stateChange($this) {
            var check = $this.prop("checked");
            var all_inf1 = $this.parents("p.detail_w").siblings(".car_detail").children(".all_inf1");
            var all_inf2 = $this.parents("p.detail_w").siblings(".car_detail").children(".all_inf2");
            if (check) {
                all_inf1.addClass("dis");
                all_inf2.removeClass("dis");
            } else {
                all_inf1.removeClass("dis");
                all_inf2.addClass("dis");
            }
        }

        //未点击勾中状态时,将输入框置空
        function inputEmpty() {
            var checkBoxClass = arguments[0];               //arguments[0]复选框class名称
            var nameClass = arguments[1];                        //arguments[1]姓名输入框的class
            var phoneClass = arguments[2];                   //arguments[2]手机号输入框的class
            var checkBoxClassChildren = checkBoxClass.parents("p.detail_w").siblings(".car_detail").children(".all_inf1");
            checkBoxClassChildren.find(nameClass).val("");              //姓名清空
            checkBoxClassChildren.find(phoneClass).val("");             //手机号清空
            if (checkBoxClass.hasClass("insureC2")) {
                var $receiverProvince = arguments[4];           //收件人省class
                var $receiverCity = arguments[5];           //收件人市class
                var $receiverArea = arguments[6];           //收件人区class
                var $receiverAddr = arguments[7];           //收件人详细class
                checkBoxClassChildren.find($receiverProvince).text("请选择省").data("code", "");
                checkBoxClassChildren.find($receiverCity).text("请选择市").data("code", "");
                checkBoxClassChildren.find($receiverArea).text("请选择区").data("code", "");
                checkBoxClassChildren.find($receiverAddr).val("")
            } else {
                var $Num = arguments[3];                        //  证件号码
                var $certType = arguments[4];                   //证件类型
                checkBoxClassChildren.find($Num).val("");
                checkBoxClassChildren.find($certType).data("code", "120001").text("居民身份证");
            }
        }

        //与被保人一致 的勾选后信息展示一致
        function displaySameInfo() {
            var sameCarC = arguments[0];                   //arguments[0]为 C与车主一致 的选中状态
            var checkBoxClass = arguments[1];                    //当前点击的checkbox
            var $nameClass = arguments[2];                       //姓名的class
            var $phoneClass = arguments[3];                   //手机号的class
            var $sameCarNameClass = arguments[4];
            var $sameCarPhoneClass = arguments[5];
            var checkBoxClassChildren = checkBoxClass.parents("p.detail_w").siblings(".car_detail").children(".all_inf2");
            var sameCarClassChildren = $(".sameDC").parents("p.detail_w").siblings(".car_detail");
            var sameCarNameClassChildrenV;
            var sameCarPhoneChildrenV;
            var sameCarTypeClassChildrenV;
            var sameCarTypeCodeClassChildrenV;
            var sameCarCodeClassChildrenV;
            if (checkBoxClass.hasClass("sameInsure")) {
                var $sameCerTypeClass = arguments[6];
                var $sameCarCodeClass = arguments[7];
                var $certTypeClass = arguments[8];                   //证件类型class
                var $certCodeClass = arguments[9];                   //证件号码class
            }
            if (sameCarC) {                           //如果为勾选
                sameCarNameClassChildrenV = sameCarClassChildren.children(".all_inf2").find($sameCarNameClass).text();
                sameCarPhoneChildrenV = sameCarClassChildren.children(".all_inf2").find($sameCarPhoneClass).text();
                sameCarTypeClassChildrenV = sameCarClassChildren.children(".all_inf2").find($sameCerTypeClass).text();
                sameCarTypeCodeClassChildrenV = sameCarClassChildren.children(".all_inf2").find($sameCerTypeClass).data("code");
                sameCarCodeClassChildrenV = sameCarClassChildren.children(".all_inf2").find($sameCarCodeClass).text();

            } else {
                sameCarNameClassChildrenV = sameCarClassChildren.children(".all_inf1").find($sameCarNameClass).val();
                sameCarPhoneChildrenV = sameCarClassChildren.children(".all_inf1").find($sameCarPhoneClass).val();
                sameCarTypeClassChildrenV = sameCarClassChildren.children(".all_inf1").find($sameCerTypeClass).text();
                sameCarTypeCodeClassChildrenV = sameCarClassChildren.children(".all_inf1").find($sameCerTypeClass).data("code");
                sameCarCodeClassChildrenV = sameCarClassChildren.children(".all_inf1").find($sameCarCodeClass).val();
            }
            checkBoxClassChildren.find($nameClass).text(sameCarNameClassChildrenV);
            checkBoxClassChildren.find($phoneClass).text(sameCarPhoneChildrenV);
            checkBoxClassChildren.find($certTypeClass).text(sameCarTypeClassChildrenV);
            checkBoxClassChildren.find($certTypeClass).data("code", sameCarTypeCodeClassChildrenV);
            checkBoxClassChildren.find($certCodeClass).text(sameCarCodeClassChildrenV);
        }

        //当勾选 与车主一致时 ,判断  底下有没有与被保人一致被勾选
        function checkCarAgain() {
            var $sameInsureClass = arguments[0];
            var $nameClass = arguments[1];
            var $phoneClass = arguments[2];
            var sameInsureC = $sameInsureClass.prop("checked");             //  sameInsureC  与被保人一致的状态
            var sameInsureCChildren = $sameInsureClass.parents("p.detail_w").siblings(".car_detail").children(".all_inf2");
            var sameCarClassChildren = $(".sameDC").parents("p.detail_w").siblings(".car_detail").children(".all_inf2");
            var sameCarNameClassChildrenV = sameCarClassChildren.find(".recognizeeName").text();
            var sameCarPhoneChildrenV = sameCarClassChildren.find(".recognizeePhone").text();
            var sameCarTypeClassChildrenV = sameCarClassChildren.find(".recognizeeType").text();
            var sameCarTypeCodeClassChildrenV = sameCarClassChildren.find(".recognizeeType").data("code");
            var sameCarCodeClassChildrenV = sameCarClassChildren.find(".recognizeeNum").text();
            if (sameInsureC) {
                if ($sameInsureClass.hasClass("sameInsure")) {             //如果勾选的是投保人信息那栏
                    var $certTypeClass = arguments[3];                   //证件类型class
                    var $certCodeClass = arguments[4];                   //证件号码class
                    sameInsureCChildren.find($certTypeClass).text(sameCarTypeClassChildrenV);
                    sameInsureCChildren.find($certTypeClass).data("code", sameCarTypeCodeClassChildrenV);
                    sameInsureCChildren.find($certCodeClass).text(sameCarCodeClassChildrenV);
                }
                sameInsureCChildren.find($nameClass).text(sameCarNameClassChildrenV);
                sameInsureCChildren.find($phoneClass).text(sameCarPhoneChildrenV);
            }
        }

        //判断是否填写和格式是否正确
        var flag = 0;

        function layout(val, notFilled, ele, len) {              //val为输入框的值   notFilled未填写的提示  ele当前选择框  len为长度
            var informationOne = $(".all_inf1");
            if (!val) {           //新车购置发票号未填写
                dg.warn("请填写" + notFilled);
                informationOne.find(ele).addClass("red_c");
                flag = 1;
            } else if (val.length > len) {
                dg.warn(notFilled + "字数超过最大限制");
                informationOne.find(ele).addClass("red_c");
                flag = 1;
            } else {
                flag = 0;
            }
            return flag;
        }

        function protocolDialog(tplId) {
            $tpl = $(tplId + "Tpl");
            var title = $tpl.data('tplTitle'),
                commonTpl = $('#commonDialogTpl').html(),
                protocolTpl = $tpl.html(),
                html = commonTpl.format(title, protocolTpl);
            dg.open({
                area: ['700px', '500px'],
                content: html
            });
        }

        //检测优惠券成功后，判断模式后提交订单
        function couponCheck(mode, isSelectedPackage, couponId, CMode, Url, data) {
            var useMode = $('li.coupon-checked').data('couponusemode');
            if (useMode == '2') {
                if (CMode != useMode) {
                    dg.warn("抱歉，您选择的优惠劵只能用于买保险送服务包/买服务包送保险");
                    return;
                }
            }
            if (useMode == '1') {
                if (CMode != useMode) {
                    dg.warn("抱歉，您选择的优惠劵只能用于买保险返奖励金");
                    return;
                }
            }
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/coupon/coupon-check",
                data: {
                    couponId: couponId,
                    mode: CMode,
                    amount: insuranceFee
                },
                success: function (result) {
                    if (result.success) {
                        if (mode == "0" && mode != isSelectedPackage) {
                            dg.confirm("您尚未选择服务包，是否确定提交?", function () {
                                submit(Url, data, couponId, CMode, true);
                            })
                        } else {
                            submit(Url, data, couponId, CMode, true)
                        }
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        }

        //阻止事件冒泡
        function stopPropagation(e) {
            if (e.stopPropagation) {
                e.stopPropagation();
            } else {
                e.cancelBubble = true;
            }
        }

        //点击提交订单后
        function submit(Url, data, couponId, flag) {
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/flow/" + Url,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(data),
                success: function (result) {
                    if (result.success) {
                        var orderSn = result.data.insuranceOrderSn,
                            data = JSON.stringify(result.data);
                        if (flag) {               //flag 为true为有可使用的优惠券
                            $.ajax({
                                url: BASE_PATH + "/insurance/anxin/coupon/coupon-use",
                                data: {
                                    couponId: couponId,
                                    mode: 1,
                                    orderSn: orderSn
                                },
                                success: function (result) {
                                    if (result.success) {
                                        sessionStorage.setItem("dimensionCodeD", data);
                                        jump_success(orderSn);
                                    } else {
                                        dg.fail(result.errorMsg);
                                    }
                                }
                            });
                        } else {              // false为无可使用的优惠券
                            sessionStorage.setItem("dimensionCodeD", data);
                            jump_success(orderSn);
                        }
                    } else {
                        dg.fail(result.errorMsg)
                    }
                }
            });
        }

        //判断是否是智能，为左侧菜单栏
        function jump_success(orderSn) {
            var url;
            var syUploadImg = $('#syUploadImg').val();
            var jqUploadImg = $('#jqUploadImg').val();

            if (isSmart) {
                url = BASE_PATH + "/smart/bihu/flow/insurance-result?orderSn="+orderSn;
            } else {
                url = BASE_PATH + "/insurance/anxin/flow/insurance-result?orderSn="+orderSn;
            }
            if(syUploadImg == "0" || jqUploadImg =="0"){
                url = BASE_PATH + "/insurance/anxin/pay/choose?sn="+orderSn;
            }
            window.location.href = url;
        }

        //将时间戳转换成时间格式
        function toTime(Time) {
            var time = new Date(Time);
            var year = time.getFullYear();      //年
            var mon = time.getMonth() + 1;        //月
            var mon2 = mon < 10 ? "0" + mon : mon;
            var date = time.getDate();          //日
            var date2 = date < 10 ? "0" + date : date;
            var hour = time.getHours();         //时
            var hour2 = hour < 10 ? "0" + hour : hour;
            var min = time.getMinutes();        //分
            var min2 = min < 10 ? "0" + min : min;
            var sec = time.getMinutes();        //秒
            var sec2 = sec < 10 ? "0" + sec : sec;
            return year + "-" + mon2 + "-" + date2;
        }

        //判断输入框是否输入并且拿值
        function check_getVal($insureInput,tips) {
            var obj = {};
            var flag = true;
            $insureInput.each(function () {
                var $this = $(this),
                    this_val =$.trim($this.val()),
                    reg = /^1[3|4|5|7|8][0-9]\d{8}$/;
                if (!this_val) {
                    dg.warn('请将'+tips+'填写完整');
                    $this.addClass('red_c');
                    flag = false;
                    return false;
                } else {
                    if($this.hasClass('Mobile')){
                        if(!reg.test(this_val)){
                            dg.warn('手机号格式不正确');
                            $this.addClass('red_c');
                            flag = false;
                            return false;
                        }
                    }
                    if($this.hasClass('papersCode')){
                        var $paperType = $this.siblings().find('.paperType'),
                            paperType_code = '120001',
                            this_valNum = 18;
                        if($paperType.data('code') == paperType_code && this_val.length !=this_valNum){
                            dg.warn('身份证号格式不正确');
                            $this.addClass('red_c');
                            flag = false;
                            return false;
                        }
                    }
                    var $name = $this.attr('dynamic_name');
                    obj[$name] = this_val;
                }
            });
            return flag && obj;
        }

        //发送手机验证码
        function send_code(params) {
            var time = 60;
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/flow/upload-info",
                data:params,
                success: function (result) {
                    if (result.success) {
                        var $sendCode = $('.sendCode');
                        var time_run = setInterval(function () {
                            time--;
                            if(time == 0){
                                $sendCode.removeClass('sendCode_bc').prop('disabled',false).text('重新发送');
                                clearInterval(time_run);
                            }else{
                                $sendCode.addClass('sendCode_bc').prop('disabled',true).text('重新发送'+time+'s');
                            }
                        },1000)
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            })
        }

        //邮箱的信息验证
        function email_verify(inf) {
            var flag = true;
            var receiverEmail = $.trim(inf.find(".email-receive").val());
            if (!receiverEmail) {
                dg.warn("请填写收件人电子邮箱");
                inf.find(".email-receive").addClass("red_c");
                flag = false;
                return false;
            } else if (!(/^([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\-|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(receiverEmail))) {
                dg.warn("请填写正确的电子邮箱");
                inf.find(".email-receive").addClass("red_c");
                flag = false;
                return false
            }
            return flag && receiverEmail
        }
        //配送信息是否勾选与被保人一致
        function isCheck_same_insured(first) {
            if(first){
                traverse_type('.all_inf2');
                traverse_type('.all_inf1');
                return false;
            }
            if($(".insureC2").prop("checked")){
                traverse_type('.all_inf2')
            }else{
                traverse_type('.all_inf1')
            }
        }
        //遍历发票类型、保单类型，来判断是否展示收件人地址，邮箱
        function traverse_type(info) {
            //mail电子，send_by_post纸质
            var traverse_type =  $(info).find('.js-traverse-type'),
                mail = [],
                send_by_post = [];
            traverse_type.each(function () {
                var data_code = $(this).data('code');
                if(data_code == 0){
                    send_by_post.push(data_code);
                    return;
                }
                mail.push(data_code);
            });
            var post_address = $(info).find('.js-post-address'),
                js_mail = $(info).find('.js-mail');
            if(mail.length>0){
                js_mail.removeClass('dis');
            }else {
                js_mail.addClass('dis');
            }
            if(send_by_post.length>0){
                post_address.removeClass('dis');
            }else{
                post_address.addClass('dis');
            }
        }

        $(document).on('click', '.btn-box a', function () {
            $(this).addClass('current').siblings().removeClass('current');
        });

        $(document).on('click', '.js-color-confim', function () {
            var colorType = $('.btn-box').find('.current').data('color');
            $('.colour-type').val(colorType);
            if (!colorType) {
                dg.fail('请选择颜色');
                return;
            }
            dg.close(colorDialog);
        });
        //同意协议打印日志
        function printLog(pro) {
            var protocol = true;
            if (pro == "#servicePackageThirdPartyProtocolTpl") {
                protocol = false;
            }
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/api/log",
                type: "POST",
                data: {
                    protocol: JSON.stringify(protocol)
                },
                success: function () {

                }
            });
        }




    });

});
