/**
 * Created by huage on 16/9/21.
 * 买服务包送保险
 */
$(function () {
    var virtualData = JSON.parse(sessionStorage.getItem("virtualData")),
        thirdPartyDialog;
    sessionStorage.removeItem("virtualData");
    if (virtualData) {
        backInsureceCheck(".sameDC");
        backInsureceCheck(".sameInsure");
        backInsureceCheck(".insureC2");
    }
    addHeadStyle(3);
    seajs.use(["art", "dialog", "date"], function (at, dg, dp) {
        dp.datePicker(".datepicker");
        //进页面，若配送信息与被保人一致。
        isCheck_same_insured(true);
        $(document)
            .on('click', '.js-protocol-link', function () {
                var tplId = $(this).attr('href');
                protocolDialog(tplId);
                return false;
            })
            .on('click', '.js-third-party-link', function () {
                var tplId = $(this).attr('href');
                var $tpl = $(tplId + "Tpl"),
                    protocolTpl = $tpl.html();
                thirdPartyDialog = dg.open({
                    shadeClose: false,
                    closeBtn: false,
                    area: ['700px', '500px'],
                    content: protocolTpl
                });
            })
            .on('click', '.js-agree-third-party-btn', function () {
                printLog();
                $('.js-agree-protocol').prop('checked', true);
                dg.close(thirdPartyDialog);
            });
        $(document).on("click", function () {
            $(".prevUl,.cityUl,.areaUl").addClass("dis");
            $(".warranty ul").addClass("dis");
            $(".choose_btn").addClass("s_up").removeClass("s_down");
            $(".zj_btn").addClass("s_up").removeClass("s_down");
        }).on("click", ".all_inf_w_btn", function () {
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
                                $ul.append("<li" + " data-typeCode=" + i + " > " + data[i] + "</li>")
                            }
                        }
                    }
                })
            } else {
                $(this).children(".zj_btn").addClass("s_up").removeClass("s_down");
                $ul.addClass("dis");
            }
        }).on("change", ".js-agree-protocol", function () {            //点击我同意
            var $thisState = $(this).prop("checked");
            var $third = $('.js-third-party-link');
            if ($thisState && $third.length) {
                $third.click();
            }
        }).on("click", ".all_inf_w_btn li,.warranty li", function () {
            var $this = $(this),
                liV = $this.text(),
                liCode = $this.data("typecode"),
                $thisParent = $this.parent('ul');
            $this.parents("ul").siblings(".zj_l").data("code", liCode).text(liV);
            if($thisParent.hasClass('js-traverseUl')){
                isCheck_same_insured();
            }
        }).on("click", ".warranty", function (e) {
            stopPropagation(e);                 //阻止事件冒泡
            var $ul = $(this).children("ul");
            if ($ul.hasClass("dis")) {
                $ul.removeClass("dis");
                $(this).children(".zj_btn").addClass("s_down").removeClass("s_up")
            } else {
                $ul.addClass("dis");
                $(this).children(".zj_btn").removeClass("s_down").addClass("s_up")
            }
        }).on("click", ".prevK,.cityK,.areaK", function (e) {
            stopPropagation(e);                 //阻止事件冒泡
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
        }).on("click", ".prevK", function () {
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
                        }
                    }
                })
            }
        }).on("click", ".prevUl li", function () {
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
                    var data2 = json.data;
                    for (var j = 0; j < data2.length; j++) {
                        var str2 = "<li" + " data-code2=" + data2[j].regionCode + ">" + data2[j].regionName + "</li>";
                        $UL2.append(str2);
                    }
                }
            })
        }).on("click", ".cityUl li", function () {
            var $UL3 = $(this).parents(".cityK").siblings(".areaK").children(".areaUl");
            $UL3.empty();
            var liV = $(this).text();
            $(this).parents("ul").siblings(".city").text(liV);
            var cityCode = $(this).data("code2");
            $(this).parents("ul").siblings(".city").data("code2", cityCode);
            $.ajax({
                url: BASE_PATH + "/insurance/anxin/api/getInsureProvinceCity",
                data: {
                    regionParentCode: cityCode
                },
                success: function (jsonD) {           //用省对应的code去获取市
                    var data3 = jsonD.data;
                    for (var j = 0; j < data3.length; j++) {
                        var str3 = "<li" + " data-code3=" + data3[j].regionCode + ">" + data3[j].regionName + "</li>";
                        $UL3.append(str3);
                    }
                }
            })
        }).on("click", ".areaUl li", function () {
            var liV = $(this).text();
            $(this).parents("ul").siblings(".area").text(liV);
            var areaCode = $(this).data("code3");
            $(this).parents("ul").siblings(".area").data("code3",areaCode);
        }).on("mouseover",".triangle",function () {
            $(".incentive").removeClass("dis");
        }).on("mouseout", ".triangle", function () {
            $(".incentive").addClass("dis");
        }).on("click",".submit",function () {       //点击提交              //vehicle车主信息    applicat是投保   inseured是被保   receiver是配送
            var sameDriC = $(".sameDri").prop("checked"),
                sameInsureC = $(".sameInsure").prop("checked"),
                insureC2 =  $(".insureC2").prop("checked"),
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
                receiptType;
            if (sameDriC){                          //勾了与车主一致
                insuredName = inf2.find(".recognizeeName").text();
                insuredPhone = inf2.find(".recognizeePhone").text();
                insuredCertCode = inf2.find(".recognizeeNum").text();
                insuredCertType = inf2.find(".recognizeeType").data("code");
                insuredCertTN = $.trim(inf2.find(".recognizeeType").text());
            }else {                                 //没勾
                insuredName =inf1.find(".recognizeeName").val();
                var insuredName2 = $.trim(insuredName);
                layout(insuredName2, "被保人姓名", ".recognizeeName", 10);
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
                if (!insuredCertTN){
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
            if (sameInsureC){                          //勾了与被保人一致
                applicantName = inf2.find(".applicantName").text();
                applicantType = inf2.find(".applicantType").data("code");         //投保人证件类型的code
                applicantCertCode = inf2.find(".applicantNum").text();
                applicantPhone = inf2.find(".applicantPhone").text();
                applicantTypeN = inf2.find(".applicantType").text();
                VCode_num = inf2.find(".VCode_num").val();
                VCode_num2 = $.trim(VCode_num);
                if (!VCode_num2){
                    dg.warn("请填写手机号验证码");
                    return false;
                }
            }else {                                 //没勾
                applicantName = inf1.find(".applicantName").val();
                var applicantName2 = $.trim(applicantName);
                layout(applicantName2, "投保人姓名", ".applicantName", 10);
                if (flag) {
                    return false;
                }
                applicantType = inf1.find(".applicantType").data("code");
                applicantTypeN = $.trim(inf1.find(".applicantType").text());
                if (!applicantTypeN){
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
                if (!VCode_num2){
                    dg.warn("请填写手机号验证码");
                    return false;
                }
            }
            var shipping_address,
                receiverEmail;
            if (insureC2){                          //勾了与。。一致
                receiverName = inf2.find(".receiverName").text();
                receiverPhone = inf2.find(".receiverPhone").text();
                receiverType = inf2.find(".warrantyType").data("code");   //商业保单类型
                receiptType = inf2.find(".receipt-type").data("code");   //电子保单类型
                //地址框为class为dis时；
                if(!inf2.find('.js-post-address').eq(0).hasClass('dis')){
                    shipping_address = shipping_Address(inf2);
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
            }else {                                 //没勾
                receiverName = inf1.find(".receiverName").val();
                receiverType = inf1.find(".warrantyType").data("code");   //商业保单类型
                var receiverName2 = $.trim(receiverName);
                layout(receiverName2, "收件人姓名", ".receiverName", 10);
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
                receiptType = inf1.find(".receipt-type").data("code");   //电子保单类型
                //地址框为class为dis时；
                if(!inf1.find('.js-post-address').eq(0).hasClass('dis')){
                    shipping_address = shipping_Address(inf1);
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
            var frontMoney = $(".frontMoney").val();            //上份商业保险金额
            if (!frontMoney) {
                dg.warn("请填写上份商业险保费金额");
                return false;
            }
            if (isNum(frontMoney)) {
                dg.warn("保费金额格式输入有误");
                return false
            }
            var timestamp2 = sessionStorage.getItem("timestamp2");
            var $jsAgreeProtocol = $(".js-agree-protocol").prop("checked");      //我同意是否勾选
            if (!$jsAgreeProtocol) {                  //若未勾选
                dg.warn('请勾选“我已阅读并同意以下声明”选项!');
                return false;
            }
            var id = util.getPara("id");                            //url上带过来的id
            var formId2 = $(".formId2").data("formid"),
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
                id:id,
                cooperationMode:3,
                applicantName:applicantName,
                tapplicantCertType:applicantType,
                applicantCertCode:applicantCertCode,
                applicantCertName:applicantTypeN,
                insuredCertName:insuredCertTN,
                applicantPhone:applicantPhone,
                insuredName:insuredName,
                insuredPhone:insuredPhone,
                insuredCertType:insuredCertType,            //证件类型   传code
                insuredCertCode:insuredCertCode,
                receiverName:receiverName,
                receiverPhone:receiverPhone,
                receiverProvince: receiverProvince,
                receiverCity:receiverCity,
                receiverArea: receiverArea,
                receiverAddr: receiverAddr,
                receiverEmail: receiverEmail,
                insuranceFormDTOList:[
                    {
                        insuredFee: frontMoney,                 //上份商业保险金额
                        receiverType: receiverType,
                        insuranceType: 2,
                        id: formId2,
                        receiptType:receiptType    //发票类型
                    }
                ]
            };

            var dialogId = dg.confirm("请再次确认车牌号填写是否正确？<br/>车牌号：" + $('.js-car-license').text(), function () {
                dg.close(dialogId);
                $.ajax({
                    url: BASE_PATH + "/insurance/anxin/virtual/flow/update",
                    type: "POST",
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function (result) {
                        if (result.success) {
                            sessionStorage.removeItem("virtualData");
                            var url = BASE_PATH + "/insurance/anxin/virtual/flow/virtual-service";
                            window.location.href = Smart.mode_url(url) + '&id=' + id;
                        } else {
                            dg.fail(result.errorMsg)
                        }
                    }
                });
            });
        }).on("click", ".back", function () {
            sessionStorage.removeItem("virtualData");
            var id = util.getPara("id"),
                virtual_smart = sessionStorage.getItem('virtual_smart');
            if(virtual_smart){
                url = BASE_PATH + "/smart/bihu/flow/virtual-modify";
            }else{
                url = BASE_PATH + "/insurance/anxin/virtual/flow/virtual-modify";
            }

            window.location.href = Smart.mode_url(url) + '&id=' + id;
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
                displaySameInfo(sameDriC, $this, ".receiverName", ".receiverPhone", ".recognizeeName", ".recognizeePhone");
                inputEmpty($this, ".receiverName", ".receiverPhone", ".warrantyType", ".receiverProvince", ".receiverCity", ".receiverArea", ".receiverAddr")
            } else {
                inputEmpty($this, ".applicantName", ".applicantPhone", ".applicantNum", ".applicantType");
                displaySameInfo(sameDriC, $this, ".applicantName", ".applicantPhone", ".recognizeeName", ".recognizeePhone", ".recognizeeType", ".recognizeeNum", ".applicantType", ".applicantNum");
            }
        }).on("click", ".sameDC", function () {             //勾选与车主一致
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
        }).on("click", "#insureInfo ul li", function () {
            var $thisText = $(this).text();
            var $thisCode = $(this).data("typecode");
            $(".sameInsure").parents("p.detail_w").siblings(".car_detail").children(".all_inf2").find(".applicantType").text($thisText).data("code", $thisCode)

        });


        //判断是否填写和格式是否正确
        var flag = 0;

        function layout(val, notFilled, ele, len) {              //val为输入框的值   notFilled未填写的提示  ele当前选择框  len为长度
            if (!val) {           //新车购置发票号未填写
                dg.warn("请填写" + notFilled);
                $(".all_inf1").find(ele).addClass("red_c");
                flag = 1;
            } else if (val.length > len) {
                dg.warn(notFilled + "字数超过最大限制");
                $(".all_inf1").find(ele).addClass("red_c");
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
            })
        }

        //配送地址的信息

        function shipping_Address(inf) {
            var flag = true,
                obj = {};
            var receiverProvince = $.trim(inf.find(".receiverProvince").text());
            if (!receiverProvince || receiverProvince == "请选择省") {
                dg.warn("请选择收件人省份");
                flag = false;
                return false;
            }
            obj['receiverProvince'] = receiverProvince;
            var receiverCity = $.trim(inf.find(".receiverCity").text());
            if (!receiverCity || receiverCity == "请选择市") {
                dg.warn("请选择收件人城市");
                flag = false;
                return false;
            }
            obj['receiverCity'] = receiverCity;
            var receiverArea = $.trim(inf.find(".receiverArea").text());
            if (!receiverArea || receiverArea == "请选择区") {
                dg.warn("请选择收件人地区");
                flag = false;
                return false;
            }
            obj['receiverArea'] = receiverArea;
            var receiverAddr = $.trim(inf.find(".receiverAddr").val());
            if (!receiverAddr) {
                dg.warn("请填写收件人详细地址");
                flag = false;
                return false;
            }
            obj['receiverAddr'] = receiverAddr;
            return flag && obj;

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
    });
    //小于10的加0
    function add0(parameter) {
        return parameter < 10 ? "0" + parameter : parameter;
    }

    //时间戳转换成日期格式
    function timeToTime(Time) {
        var time = new Date(Time);
        var year = time.getFullYear();      //年
        var mon = time.getMonth() + 1;        //月
        var date = time.getDate();          //日
        var hour = time.getHours();         //时
        var min = time.getMinutes();        //分
        var sec = time.getMinutes();        //秒
        return year + "-" + add0(mon) + "-" + add0(date) + " " + add0(hour) + ":" + add0(min) + ":" + add0(sec)
    }

    //无时分秒转化时间格式的方法
    function NewDate(date) {
        var time = new Date(date);
        var year = time.getFullYear();
        var month = time.getMonth();
        var day = time.getDate();
        var timestamp = Date.parse(new Date(year, month, day, 00, 00, 00));
        return timestamp;
    }

    //验证是否为数字
    function isNum(num) {
        var reNum = /[^\d.]/g;
        return (reNum.test(num))
    }

    //阻止事件冒泡
    function stopPropagation(e) {
        if (e.stopPropagation) {
            e.stopPropagation();
        } else {
            e.cancelBubble = true;
        }
    }

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
        if(checkBoxClass.hasClass("insureC2")){
            var $receiverProvince = arguments[4];           //收件人省class
            var $receiverCity = arguments[5];           //收件人市class
            var $receiverArea = arguments[6];           //收件人区class
            var $receiverAddr = arguments[7];           //收件人详细class
            checkBoxClassChildren.find($receiverProvince).text("请选择省").data("code","");
            checkBoxClassChildren.find($receiverCity).text("请选择市").data("code","");
            checkBoxClassChildren.find($receiverArea).text("请选择区").data("code","");
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

    //点返回投保进页面判断三个输入框的选中状态
    function backInsureceCheck(checkClass) {
        var checkState = $(checkClass).prop("checked");
        var all_inf1 = $(checkClass).parents("p.detail_w").siblings(".car_detail").children(".all_inf1");
        var all_inf2 = $(checkClass).parents("p.detail_w").siblings(".car_detail").children(".all_inf2");
        var sameDriC = $(".sameDC").prop("checked");
        if (checkState) {
            all_inf1.addClass("dis");
            all_inf2.removeClass("dis");
            fillSelect(checkClass);

            displaySameInfo(sameDriC, $(".sameInsure"), ".applicantName", ".applicantPhone", ".recognizeeName", ".recognizeePhone", ".recognizeeType", ".recognizeeNum", ".applicantType", ".applicantNum");
            displaySameInfo(sameDriC, $(".insureC2"), ".receiverName", ".receiverPhone", ".recognizeeName", ".recognizeePhone");
        } else {
            fillSelect(checkClass);
            all_inf1.removeClass("dis");
            all_inf2.addClass("dis");
        }
    }

    //点返回投保填充选择框的值
    function fillSelect(checkClass) {
        var all_inf1 = $(checkClass).parents("p.detail_w").siblings(".car_detail").children(".all_inf1");
        var all_inf2 = $(checkClass).parents("p.detail_w").siblings(".car_detail").children(".all_inf2");
        var insuredTypeName = virtualData.insuredCertTypeName;
        var applicantCertTypeName = virtualData.applicantCertTypeName;
        var insuredCert = virtualData.insuredCertType;
        var applicantCert = virtualData.applicantCertType;
        var receiverProvince = virtualData.receiverProvince;
        var receiverCity = virtualData.receiverCity;
        var receiverArea = virtualData.receiverArea;
        if ($(checkClass).hasClass("sameDC")) {
            all_inf1.find(".recognizeeType").text(insuredTypeName).data("code", insuredCert);
        } else if ($(checkClass).hasClass("sameInsure")) {
            all_inf1.find(".applicantType").text(applicantCertTypeName).data("code", applicantCert);
        } else {
            all_inf1.find(".receiverProvince").text(receiverProvince);
            all_inf2.find(".receiverProvince").text(receiverProvince);
            all_inf1.find(".receiverCity").text(receiverCity);
            all_inf2.find(".receiverCity").text(receiverCity);
            all_inf1.find(".receiverArea").text(receiverArea);
            all_inf2.find(".receiverArea").text(receiverArea);
            var receiverCode;
            if (virtualData.insuranceVirtualFormDTOList) {
                var DTOList = virtualData.insuranceVirtualFormDTOList;
                for (var i = 0; i < DTOList.length; i++) {
                    if (DTOList[i].insuranceType == 2) {
                        receiverCode = DTOList[i].receiverType;
                    }
                }
            }
            if (receiverCode == 0) {
                all_inf1.find(".warrantyType").text("纸质保单").data("code", receiverCode);
                all_inf2.find(".warrantyType").text("纸质保单").data("code", receiverCode);
            } else {
                all_inf1.find(".warrantyType").text("电子保单").data("code", receiverCode);
                all_inf2.find(".warrantyType").text("电子保单").data("code", receiverCode);
            }

        }
    }

    //同意协议打印日志
    function printLog() {
        var protocol = false;
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