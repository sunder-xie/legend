/**
 * Created by huage on 2016/12/23.
 */
$(function () {
    seajs.use(['date', 'dialog', 'art'], function (dp, dg, at) {
        /*======= 进页面加载 开始========*/
        var $doc = $(document),
            basicId = sessionStorage.getItem("basicId"),
            isHaveID = $(".isHaveID").val(),
            modeV = util.getPara("mode"),                  //拿到mode的值
            modeV2 = $(".modeV").val(),
            $bInfo = $(".bInfo"),
            vehicleSn;
        // 判断是否有  haveId 开始
        init_checkId(isHaveID);
        // 点击事件
        init_click_event();

        //各类插件 开始
        init_plugin(dp);

        //判断modeV 的方法
        init_mode();

        //初始化下来列表中的dataBPrice
        initdataBPrice();
        /*======= 进页面加载 结束========*/
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

        //检测是否有id
        function init_checkId(isHaveID) {
            if (isHaveID) {
                addHeadStyle(2);
                var timestamp3 = new Date().getTime();
                $(".firstTimestamp").val(timestamp3);
                $(".totalSeats").text($('[name="seatNumber"]').val() - 1);
                $(".chooseScheme").show();
            } else {
                if ($bInfo.length > 0) {
                    addHeadStyle(0);
                    $bInfo.show();
                } else {
                    addHeadStyle(1);
                    $('.car').text(vehicleSn);
                    $('.carInfo').show();
                }
            }
        }

        //时间插件引用 开始
        function init_plugin(dp) {
            $.each($(".datepicker2"),function(i,date_obj){
               var the_val = $(date_obj).data("time");
                if(the_val != ""){
                    $(date_obj).val(the_val);
                }
            });

            dp.datePicker(".datepicker2", {                      //保险生效时间
                dateFmt: 'yyyy-MM-dd 00:00:00',
                minDate: '%y-%M-#{%d+1} 00:00:00',            //生效时间设置
                isShowToday: false,
                onpicked: pickedF                             //设置起始时间后去做的事情
            });
            dp.datePicker(".datepicker3", {                      //
                dateFmt: 'yyyy-MM-dd',
                maxDate: '%y-%M-%d',            //生效时间设置
                isShowToday: false
            });
            dp.datePicker(".datepicker");                   //普通时间选择
        }

        function init_click_event() {
            // 点击接口成功后的数据，填充到input框
            $doc.on("click", ".ulStyle li", function () {
                var $this = $(this),
                    liV = $this.text(),
                    liData = $this.data("typecode"),
                    $ul2 = $this.parents(".ulStyle");
                $(".papers").val(liV).data("typecode", liData);
                $ul2.slideUp();
                $(".v-t-M").addClass("s_up").removeClass("s_down")
            });

            //点击过户车
            $doc.on("click", ".transferCar", function () {
                var check = $(this).prop("checked");
                if (check) {
                    $(".transferTime").removeClass("dis");
                } else {
                    $(".ownershipTime").val("");
                    $(".transferTime").addClass("dis");
                }
            });
            $doc.on("click", ".queryDiv", function (e) {
                Smart.stopPropagation(e);                 //阻止事件冒泡
                var $this = $(this);
                Smart.select_list($this, '.choose_btn');
            });
            var dialogId;
            $doc.on("click", ".choose-btn,.re_choose", function () {               //点击请选择,选择车型
                dialogId = dg.open({
                    content: $('#maskTpl').html()
                });
            });
            $doc.on("click", ".first-back", function () {       //点击填写车辆信息页面的返回修改
                if ($bInfo.length > 0) {            //看是否是智能投保
                    removeeadStyle(1);
                    $(".carInfo").hide();
                    $(".bInfo").show();
                } else {
                    var url = BASE_PATH + '/smart/bihu/flow/bihu-flow';
                    window.location.href = Smart.mode_url(url);
                }
            });
            $doc.on("click", ".second-back", function () {
                removeeadStyle(2);
                $(".chooseScheme").hide();
                $(".carInfo").show();
            });
            $doc.on("click", ".touB_ul li", function () {
                var $this = $(this),
                    li = $this.html(),
                    dataP = $this.data("price"),            //拿到对应li的data-price
                    dataId = $this.data("id");
                $this.parents(".touB_ul").siblings(".bPrice").html(li);
                $this.parents(".touB").data("id", dataId).data("bprice", dataP);
            });
            $doc.on("click", ".mainCheck,.additionalCheck", function () {                 //点击主险或者附加险,将不计免赔勾上
                var $this = $(this);
                var $thisCheck = $this.prop("checked");
                var $disregard = $this.parent("td").siblings("td.inputC").find("input");                       //当前险种的不计免赔
                $disregard.prop("checked", $thisCheck);
            });
            $doc.on("click", ".additionalCheckAnoth", function () {          //选择附险时
                var additionalCheckS = $(this).prop("checked");             //是否选中选中状态
                var additionalT = $(this).parents("td").siblings(".textB").text();
                var CarDamagS = $(".CarDamag").prop("checked");              //车损险的选中状态
                var $siblingsInp = $(this).parent("td").siblings("td.inputC").find("input");
                var dgId;
                if (additionalCheckS) {                                //选中时。
                    if (!CarDamagS) {
                        $siblingsInp.removeAttr("checked");
                        dgId = dg.confirm("需要先选择主险[车损险],才能选择附加险[" + additionalT + "]", function () {
                            dg.close(dgId);
                        }, ['知道了']);
                        return false;
                    }
                }
            });
            $doc.on("click", ".closeBtn", function () {
                dg.close(dialogId);
            });
            $doc.on("click", ".touB_jp", function () {                      //点击各个保险的价格
                if ($(this).children("ul").hasClass("dis")) {
                    $(this).children("ul").removeClass("dis");
                    $(this).children(".btn").removeClass("s_up2").addClass("s_down2");
                    $(this).parents("tr").siblings().find(".touB_ul").addClass("dis");
                    $(this).parents("tr").siblings().find(".btn").addClass("s_up2").removeClass("s_down2")
                } else {
                    $(this).children("ul").addClass("dis");
                    $(this).children(".btn").addClass("s_up2").removeClass("s_down2")
                }
            });
            $doc.on("click", ".mentalComforts", function () {
                var mainCheckMentalS = [],            //三责险,车上人险
                    mainCheckMentalText = [],
                    $this = $(this);
                $(".mainCheckMental").each(function () {
                    mainCheckMentalText.push($(this).parents("td").siblings(".textB").text());
                    mainCheckMentalS.push($(this).prop("checked"))
                });
                var mentalComfortsS = $this.prop("checked");           //精神抚慰险的点击状态
                var additionalT = $this.parents("td").siblings(".textB").text();
                var $siblingsInp = $this.parent("td").siblings("td.inputC").find("input");
                var dgId;
                if (mentalComfortsS) {
                    if (!mainCheckMentalS[0]) {
                        $siblingsInp.removeAttr("checked");
                        dgId = dg.confirm("需要先选择主险[" + mainCheckMentalText[0] + "],才能选择附加险[" + additionalT + "]", function () {
                            dg.close(dgId);
                        });
                        return false;
                    }
                    if (!mainCheckMentalS[1]) {
                        $siblingsInp.removeAttr("checked");
                        dgId = dg.confirm("需要先选择主险[" + mainCheckMentalText[1] + "],才能选择附加险[" + additionalT + "]", function () {
                            dg.close(dgId);
                        });
                        return false;
                    }
                    if (!mainCheckMentalS[2]) {
                        $siblingsInp.removeAttr("checked");
                        dgId = dg.confirm("需要先选择主险[" + mainCheckMentalText[2] + "],才能选择附加险[" + additionalT + "]", function () {
                            dg.close(dgId);
                        });
                        return false;
                    }
                }
            });
            $doc.on("click", ".papers-type-div", function (e) {          //点击证件类型
                Smart.stopPropagation(e);
                var $this = $(this);
                var $ul = $this.children("ul");
                $ul.empty();                               //点击调接口前先清空ul
                if (!$this.hasClass("cur")) {
                    var url = '/insurance/anxin/api/getInsuredCertType';
                    get_data(url, $ul, $this);
                } else {
                    Smart.select_list($this, ".v-t-M");
                }
            });
            $doc.on("click", ".CarDamag,.mainCheckMental", function () {
                var $this = $(this);
                if ($this.hasClass("CarDamag")) {
                    isOrNo($this, ".additionalCheckAnoth");
                } else {
                    isOrNo($this, ".mentalComforts");
                }
            });
            $doc.on("focus", "input", function () {     //聚焦input框时
                var $this = $(this);
                if ($this.hasClass("red_border")) {        //判断是否有红框这个属性
                    $this.removeClass("red_border").parent().removeClass("red_border");      //有就去掉
                }
            });
            $doc.on("click", ".nextStep", function () {     //点击第二步的下一步
                try {
                    //数据校验
                    nextStepCheck();
                } catch (msg) {
                    dg.warn(msg);
                    return;
                }
                //数据通过，正常业务流程
                //第二流程获取当前时间   第一次
                var timestamp = new Date().getTime();
                $(".firstTimestamp").val(timestamp);
                var car_numV = $(".car_num").val();                    //车牌号
                var S_cheK = $(".checkbox").prop("checked");                //是否勾选木有车牌
                if (S_cheK) {                                   //把车牌号填充到第三步流程上
                    $(".car").text("尚未取得车牌");
                } else {
                    $(".car").text(car_numV)
                }
                addHeadStyle(2);
                $(".totalSeats").text($('[name="seatNumber"]').val() - 1);
                $(".carInfo").hide();
                $(".chooseScheme").show();
            });


            $doc.on("click", ".next_btn, .next-Step", function () {
                //看页面是否有选择保险
                var Check = [];
                $(".forceCheck,.mainCheck,.additionalCheck").each(function () {
                    var state = $(this).prop("checked");
                    if (eval(state) == true) {
                        Check.push(state)
                    }
                });
                if (Check.length < 1) {
                    dg.warn("请选择你需要的险种");
                    return false;
                }
                var forceArray = [];
                //查看交强险有没有选
                var forceState = $(".forceCheck").prop("checked");
                if (forceState) {
                var forceStartT = $(".forceStartT").val();
                if (forceStartT) {
                    var forceobj = {};
                    var forceEnd = $(".forceEndT").text();
                    var forceEndT = Date.parse(new Date(forceEnd.replace(/-/g, "/")));       //格式化的交强险失效时间
                    var forceStartTN = Date.parse(new Date(forceStartT.replace(/-/g, "/")));               //格式化的交强险生效时间
                    var forceName = $(".forceName").text();                 //交强险名称
                    var forceCode = $(".forceName").data("code");           //交强险编码
                    var forceInsureCode = $(".forceTouB").data("id");       //投保字段的id
                    forceobj.insuranceName = forceName;
                    forceobj.insuranceItemCoverageValue = 0;
                    forceobj.insuranceCategoryCode = forceCode;
                    forceobj.insuranceItemCoverageId = forceInsureCode;
                    forceobj.insuranceType = 1;
                    forceArray.push(forceobj);
                }
                else {
                    dg.warn("请选择交强险生效时间");
                    return false;
                }
                }
                var businessArray = [];                                     // 选中保险的值
                var n = 0;
                var mark = true;
                $(".mainCheck,.additionalCheck").each(function () {             //所有选中的遍历
                    var $this = $(this),
                        businessState = $this.prop("checked"),
                        //判断车损险选择后,其他主险有没有选择
                        noCarArray = [];

                    //判断选中的险种,把值封装进对象
                    if (businessState) {
                        if ($this.hasClass("CarDamag")) {
                            $(".noCarDamag").each(function () {
                                var noCarCheck = $(this).prop("checked");                //非车损险是否选中状态
                                if (noCarCheck) {                 //非车损险是否选中状态若有选中放进数组中
                                    noCarArray.push(noCarCheck)
                                }
                            });
                            var noCarLen = noCarArray.length;
                            if (noCarLen <= 0) {                  //若数组长度为0,则未选择除车损险外的保险。
                                dg.warn("车损险及其附加险不能单独承保！");
                                mark = false;
                                return mark;
                            }
                        }
                        n++;
                        var obj = {};
                        var insuranceName = $this.parents("td").siblings(".textB").text();          //险种名称
                        var insuranceItemCoverageValue = $this.parents("td").siblings(".touB").data("bprice");   // 保额
                        if (insuranceItemCoverageValue == null) {
                            insuranceItemCoverageValue = 0
                        }
                        var insuranceCategoryCode = $this.parents("td").siblings(".textB").data("code");                     //险种编码
                        var insuranceItemCoverageId = $this.parents("td").siblings(".touB").data("id");                //保额id
                        var exemptCheck = $this.parents("td").siblings(".inputC").children("input");
                        var exemptState = exemptCheck.prop("checked");
                        var isDeductible;
                        if (exemptCheck && exemptCheck.length > 0) {
                            if (exemptState) {
                                isDeductible = 0;
                            } else {
                                isDeductible = 1;
                            }
                        } else {
                            isDeductible = 2;
                        }
                        obj.isDeductible = isDeductible;
                        obj.insuranceName = insuranceName;
                        obj.insuranceItemCoverageValue = insuranceItemCoverageValue;
                        obj.insuranceCategoryCode = insuranceCategoryCode;
                        obj.insuranceItemCoverageId = insuranceItemCoverageId;
                        obj.insuranceType = 2;
                        if ($this.hasClass("mainCheck")) {
                            obj.insuranceSubcategoryType = 1;            //主险
                        } else {
                            obj.insuranceSubcategoryType = 2;            //附险
                        }
                        businessArray.push(obj)
                    }
                });
                if (!mark) {
                    return false;
                }
                if (n > 0) {
                    var businessStartT = $(".businessStartT").val();
                    if (businessStartT == "") {
                        dg.warn("请选择商业险生效时间");
                        return false;
                    } else {
                        var businessEnd = $(".businessEndT").text();
                        var businessEndT = Date.parse(new Date(businessEnd.replace(/-/g, "/")));       //格式化的商业险失效时间
                        var businessStartN = Date.parse(new Date(businessStartT.replace(/-/g, "/")));               //格式化的商业险生效时间
                    }
                }
                var vehicleOwnerName = $(".COwnerName").val();              //车主名称
                var vehicleOwnerCertCode = $(".IDNumber").val();          //证件编码
                var vehicleOwnerPhone = $(".OwnerPhNum").val();          //车主手机号
                var insuredName = $(".insuredName").val();          //被保人名称
                var newCarBillSn = $(".newCarNum").val();          //新车购置发票
                var invoiceDate = $(".invoiceDate").val();
                var newCarBillTime = NewDate(invoiceDate);         //发票开具时间
                var newCarPurcharsePrice = $(".car_price").text();          //新车购置价
                var carEngineSn = $(".motorNum").val();          //发动机号
                var carFrameSn = $(".frame_num").val();          // 车架号
                var carEgisterDate = NewDate($(".registerDate").val());          // 车辆登记日期
                var hasTransfered = $(".transferCar").prop("checked");          // 是否过户
                var transferTime = NewDate($(".ownershipTime").val());          // 过户时间
                var insuredProvince,
                    insuredCity,
                    insuredProvinceCode,
                    insuredCityCode;
                if ($bInfo.length > 0) {
                    vehicleSn = $(".car_num").val();          //车牌号码
                    insuredProvince = $(".choose_prov").val();          // 投保人所在省
                    insuredCity = $(".choose_region").val();          // 投保人所在城市
                    insuredProvinceCode = $(".choose_prov").data("code");          // 投保人所在省code
                    insuredCityCode = $(".choose_region").data("code");          // 投保人所在城市code
                } else {
                    insuredProvince = sessionStorage.getItem('prov'),
                        insuredCity = sessionStorage.getItem('city'),
                        insuredProvinceCode = sessionStorage.getItem('provCode'),
                        insuredCityCode = sessionStorage.getItem('cityCode');
                    vehicleSn = sessionStorage.getItem('carNum');
                }
                hasTransfered = eval(hasTransfered) == true ? 1 : 0;
                var vehicleOwnerCertTypeN = $(".papers").val();           //证件类型
                var S_cheK = $(".checkbox").prop("checked");                //是否获得车牌
                var hasLicense = eval(S_cheK) == true ? 0 : 1;
                var seatNum = $.trim($('[name="seatNumber"]').val());
                var cProdPlace = $(".tdText").data("cprodplace");
                var vehicleOwnerCertType = $(".papers").data("typecode");
                var vehicleCode = $(".tdText").data("vehiclecode");
                //获取当前时间
                var timestamp2 = new Date().getTime();
                var timestamp = $(".firstTimestamp").val();
                var yearPattern = $(".tdText").data("yearpattern");
                var carConfigType = $('.js-car-type').text();
                sessionStorage.setItem("timestamp2", timestamp2);
                sessionStorage.removeItem("basicId");
                sessionStorage.removeItem("result_data");
                if ($(this).hasClass("next_btn")) {                  //点击计算保费
                    sessionStorage.setItem("modeV", modeV2);            //mode 的值
                    modeV2 = Number(modeV2);
                    if (modeV2 == 2 && businessArray.length == 0) {
                        dg.warn("您尚未选择商业险，无法进行服务包选择");
                        return false;
                    }
                    var modeV3 = modeV2 == 0 ? 1 : modeV2;
                    $.ajax({
                        url: BASE_PATH + '/insurance/anxin/flow/FeeSave',
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify({
                            id: basicId,
                            cooperationMode: modeV3,
                            vehicleOwnerName: vehicleOwnerName,
                            vehicleOwnerCertType: vehicleOwnerCertType,
                            vehicleOwnerCertName: vehicleOwnerCertTypeN,
                            vehicleOwnerCertCode: vehicleOwnerCertCode,
                            vehicleOwnerPhone: vehicleOwnerPhone,
                            insuredName: insuredName,
                            vehicleSn: vehicleSn,
                            newCarBillSn: newCarBillSn,
                            newCarBillTime: newCarBillTime,
                            newCarPurcharsePrice: newCarPurcharsePrice,
                            carEngineSn: carEngineSn,
                            carFrameSn: carFrameSn,
                            carEgisterDate: carEgisterDate,
                            hasTransfered: hasTransfered,
                            transferTime: transferTime,
                            insuredProvince: insuredProvince,
                            insuredCity: insuredCity,
                            insuredProvinceCode: insuredProvinceCode,
                            insuredCityCode: insuredCityCode,
                            hasLicense: hasLicense,
                            isProdPlace: cProdPlace,
                            seatNumber: seatNum,
                            hasvehicleCode: vehicleCode,
                            yearPattern: yearPattern,
                            insuranceToken: timestamp,
                            carConfigType: carConfigType,
                            insuranceFormDTOList: [
                                {
                                    insuranceType: 1,                        //交强险
                                    packageStartTime: forceStartTN,
                                    packageEndTime: forceEndT,
                                    itemDTOList: forceArray
                                },
                                {
                                    insuranceType: 2,                        //商业险
                                    packageStartTime: businessStartN,
                                    packageEndTime: businessEndT,
                                    itemDTOList: businessArray
                                }
                            ]
                        }),
                        success: function (result) {
                            if (result.success) {
                                var data = result.data;
                                    forceLength = forceArray.length;
                                        forceTime = forceStartTN;
                                var result = JSON.stringify(result);
                                sessionStorage.setItem("result_data", result);
                                if (businessArray.length > 0) {
                                    check_policy(data, businessStartN, 1);
                                } else {
                                    check_jq(data,forceStartTN,1)
                                }
                            } else {
                                dg.fail(result.errorMsg);
                            }
                        }
                    })
                } else {                                              //点击下一步
                    if (businessArray.length == 0) {
                        dg.warn("您尚未选择商业险，无法进行服务包选择");
                        return false;
                    }
                    sessionStorage.setItem("modeV", modeV);
                    $.ajax({
                        url: BASE_PATH + '/insurance/anxin/virtual/flow/save',
                        type: "POST",
                        contentType: "application/json",
                        data: JSON.stringify({
                            id: basicId,
                            cooperationMode: Number(modeV),
                            vehicleOwnerName: vehicleOwnerName,
                            vehicleOwnerCertType: vehicleOwnerCertType,
                            vehicleOwnerCertName: vehicleOwnerCertTypeN,
                            vehicleOwnerCertCode: vehicleOwnerCertCode,
                            vehicleOwnerPhone: vehicleOwnerPhone,
                            insuredName: insuredName,
                            vehicleSn: vehicleSn,
                            newCarBillSn: newCarBillSn,
                            newCarBillTime: newCarBillTime,
                            newCarPurcharsePrice: newCarPurcharsePrice,
                            carEngineSn: carEngineSn,
                            carFrameSn: carFrameSn,
                            carEgisterDate: carEgisterDate,
                            hasTransfered: hasTransfered,
                            transferTime: transferTime,
                            insuredProvince: insuredProvince,
                            insuredCity: insuredCity,
                            insuredProvinceCode: insuredProvinceCode,
                            insuredCityCode: insuredCityCode,
                            hasLicense: hasLicense,
                            isProdPlace: cProdPlace,
                            seatNumber: seatNum,
                            hasvehicleCode: vehicleCode,
                            insuranceToken: timestamp,
                            yearPattern: yearPattern,
                            carConfigType: carConfigType,
                            insuranceFormDTOList: [
                                {
                                    insuranceType: 1,                        //交强险
                                    packageStartTime: forceStartTN,
                                    packageEndTime: forceEndT,
                                    itemDTOList: forceArray
                                },
                                {
                                    insuranceType: 2,                        //商业险
                                    packageStartTime: businessStartN,
                                    packageEndTime: businessEndT,
                                    itemDTOList: businessArray
                                }
                            ]
                        }),
                        success: function (result) {
                            if (result.success) {
                                var url_smart;
                                if($bInfo.length > 0){
                                    url_smart = BASE_PATH + "/insurance/anxin/virtual/flow/virtual-plan";
                                }else {
                                    sessionStorage.setItem('virtual_smart',true);
                                    url_smart = BASE_PATH + "/smart/bihu/flow/virtual-plan";
                                }
                                window.location.href = Smart.mode_url(url_smart) + '&id=' + result.data;
                            } else {
                                dg.fail(result.errorMsg);
                            }
                        }
                    })
                }

            });

            $doc.on("click", ".vehicleList tr.modelResult", function () {
                var $this = $(this);
                var vehicleCode = $this.data("vehiclecode");          // 车型编码
                var pri = $this.data("price");                        //车价格
                var seatNum = $this.data("seatnum");                      //车座位
                var cProdPlace = $this.data("cprodplace");
                var td = $this.children(),
                    tdV = "";
                $(td).each(function () {
                    tdV += $(this).text() + " ";
                });
                var yearPattern = $this.data("yearpattern");
                $(".tdText").text(vehicleCode + " " + $.trim(tdV)).addClass("ma-l").data("vehiclecode", vehicleCode).data("seatnum", seatNum).data("cprodplace", cProdPlace).data("yearpattern", yearPattern)
                $(".car_price").text(pri);
                $(".choose-btn").text("重新选择");
                var name_seatNumber = $('[name="seatNumber"]');
                name_seatNumber.val(seatNum);
                dg.close(dialogId);
            });


            //选择车型
            var familyC = [];       //品牌对应code
            var brandC = [];        //车系对应code
            var engineDescC = [];   //发动机对应code
            var gearboxTypeC = [];  //驱动机对应code
            $doc.on("click", ".search-button", function () {            //点击搜索车型
                var searchV = $(this).siblings(".search").val();
                $(".ipt").data("code", "");
                $.ajax({
                    url: BASE_PATH + '/insurance/anxin/api/getCarModel',
                    data: "vehicleName=" + searchV,
                    success: function (result) {
                        if (result.success) {
                            $(".tiS").hide();
                            $(".familyL").siblings(".ipt").text("品牌");          //查询成功把输入框的值先置为初始
                            $(".brandL").siblings(".ipt").text("车系");
                            $(".engineDescL").siblings(".ipt").text("发动机描述");
                            $(".gearboxTypeL").siblings(".ipt").text("驱动形式");
                            fourType(result);
                            $(document).off("click", ".query ul li");
                            $(document).on("click", ".query ul li", function () {
                                var val = $(this).text();
                                $(this).parents("ul").siblings(".ipt").text(val);
                                var index = $(this).index();                 //对应ul里面的位置
                                var pClass = $(this).parents("ul");
                                if (pClass.hasClass("familyL")) {//选的品牌类
                                    var codeF = familyC[index];
                                    pClass.siblings(".ipt").data("code", codeF);
                                }
                                if (pClass.hasClass("brandL")) { //选的车系类
                                    var codeB = brandC[index];
                                    pClass.siblings(".ipt").data("code", codeB);
                                }
                                if (pClass.hasClass("engineDescL")) {//选的描述类
                                    var codeE = engineDescC[index];
                                    pClass.siblings(".ipt").data("code", codeE);
                                }
                                if (pClass.hasClass("gearboxTypeL")) {
                                    var codeG = gearboxTypeC[index];
                                    pClass.siblings(".ipt").data("code", codeG);
                                }
                                var s_codeG = $(".gearboxTypeL").siblings(".ipt").data("code");
                                var s_codeE = $(".engineDescL").siblings(".ipt").data("code");
                                var s_codeB = $(".brandL").siblings(".ipt").data("code");
                                var s_codeF = $(".familyL").siblings(".ipt").data("code");
                                $.ajax({
                                    url: BASE_PATH + '/insurance/anxin/api/getCarModel',
                                    data: {
                                        vehicleName: searchV,
                                        gearboxType: s_codeG,
                                        engineDesc: s_codeE,
                                        brandId: s_codeF,
                                        familyId: s_codeB
                                    },
                                    success: function (result) {
                                        if (result.success) {
                                            fourType(result);
                                        }
                                    }
                                })
                            })
                        }
                    }
                })
            });
            //四大类选择填充
            function fourType(result) {
                familyC = [];       //品牌对应code
                brandC = [];        //车系对应code
                engineDescC = [];   //发动机对应code
                gearboxTypeC = [];  //驱动机对应code
                var vehicleL = at("vehicleList", result);
                $(".vehicleList").html(vehicleL);
                $(".query ul").empty();
                var dT = result.data;
                if (dT.length == 0) {
                    $(".tiS").show();
                }
                for (var i = 0; i < dT.length; i++) {
                    var familyL = dT[i].brandList;
                    for (var f = 0; f < familyL.length; f++) {      //品牌
                        var brandNameTrim = $.trim(familyL[f].brandName);
                        $(".familyL").append("<li>" + brandNameTrim + "</li>");
                        familyC.push(familyL[f].brandId);
                    }
                    var brandL = dT[i].familyList;
                    for (var b = 0; b < brandL.length; b++) {       //车系
                        var familyNameTrim = $.trim(brandL[b].familyName);
                        $(".brandL").append("<li>" + familyNameTrim + "</li>");
                        brandC.push(brandL[b].familyId);
                    }
                    var engineDescL = dT[i].engineDescList;
                    for (var e = 0; e < engineDescL.length; e++) {     //发动机
                        var engineDescLTrim = $.trim(engineDescL[e]);
                        $(".engineDescL").append("<li>" + engineDescLTrim + "</li>");
                        engineDescC.push(engineDescL[e]);
                    }
                    var gearboxTypeL = dT[i].gearboxTypeList;
                    for (var g = 0; g < gearboxTypeL.length; g++) {     //驱动
                        var gearboxTypeLTrim = $.trim(gearboxTypeL[g]);
                        $(".gearboxTypeL").append("<li>" + gearboxTypeLTrim + "</li>");
                        gearboxTypeC.push(gearboxTypeL[g]);
                    }
                }
            }
        }


        /*======== */
        function check_policy(data, StartT, flag) {
            var timeForInsurance,
                isRepeatInsurance,
                insuranceName;
            if (flag == 1) {
                timeForInsurance = data.timeForSyInsurance;
                isRepeatInsurance = data.isSyRepeatInsurance;
                insuranceName = '商业险';
            } else if(flag == 0){
                timeForInsurance = data.timeForJqInsurance;
                isRepeatInsurance = data.isJqRepeatInsurance;
                insuranceName = '交强险';
            }
            if (isRepeatInsurance == 1) {
                dg.confirm('您还有一份保单未到期，当前页面选择的保单起止时间与未到期保单存在重叠，请确认是否继续投保', function () {
                    check_jq(data,forceTime,flag);
                }, function () {
                })
            } else if (isRepeatInsurance == 0) {
                var timeSy = formatDate(timeForInsurance, false);
                var failureTime = timeForInsurance - 1000;
                var failureSyTime = formatDate(failureTime, false);
                if (timeForInsurance) {
                    if (timeForInsurance == StartT) {
                        check_jq(data,forceTime,flag);
                    } else {
                        dg.confirm("温馨提示，您上份" + insuranceName + "保单的失效时间为" + failureSyTime + "，新的" + insuranceName + "保单最早生效时间为" + timeSy + "，您当前选择的生效时间晚于" + timeSy + "，存在脱保风险，请确认是否更改",
                            function () {
                            },
                            function () {
                                check_jq(data,forceTime,flag);
                            })
                    }
                } else {
                    check_jq(data,forceTime,flag);
                }
            }else{
                check_jq(data,forceTime,flag);
            }
        }

        /*======== */

        /*======校验交强逻辑  开始======*/
        function check_jq(data,StartT,flag) {
            if (flag==1) {
                if (forceLength > 0) {
                    check_policy(data, StartT, 0);
                } else {
                    discriminate();
                }
            } else {
                discriminate();
            }
        }

        /*======== */
        /* =======跳转页面 区分智能还是手工 开始===== */
              function discriminate() {
                  var url;
                  if($bInfo.length > 0){        //
                      url = BASE_PATH + "/insurance/anxin/flow/confirm-info";
                  }else {
                      sessionStorage.setItem('isSmart',true);
                      url = BASE_PATH + "/smart/bihu/flow/confirm-info";
                  }
                  window.location.href = Smart.mode_url(url);
              }


        /* =======跳转页面 区分智能还是手工 结束===== */

        /*======点击下拉框掉接口 开始======*/
        // 调接口
        function get_data(url, $ul, $this) {
            SmartAjax.get({
                url: BASE_PATH + url,
                success: function (result) {
                    if (result.success) {
                        var data = result.data;
                        ajax_success(data, $ul);
                        Smart.select_list($this, ".v-t-M");
                    } else {
                        dg.warn(result.message)
                    }
                }
            });
        }

        /*======点击下拉框掉接口 结束======*/

        /*======点击下拉框掉接口成功的函数 开始======*/
        // 身份信息掉接口成功的函数
        function ajax_success(data, $ul) {
            for (var i in data) {
                $ul.append("<li" + " data-typeCode=" + i + " >" + data[i] + "</li>")
            }
        }

        //选中时间后将时间拿出来,显示失效的时间到页面上
        function pickedF() {
            var year = parseInt($dp.cal.getP("y")) + 1;       // 选中的年+1年
            var mont = $dp.cal.getP("M");       //选中的月
            var day = $dp.cal.getP("d");    //选中的日
            var t = new Date(year + '-' + mont + '-' + day);
            t.setDate(day - 1);
            var day2 = t.getDate() < 10 ? "0" + t.getDate() : t.getDate();
            var mont2 = (t.getMonth() + 1) < 10 ? "0" + (t.getMonth() + 1) : (t.getMonth() + 1);
            $(this).siblings(".endT").text(t.getFullYear() + "-" + mont2 + "-" + day2 + " " + "23:59:59");
        }

        /*======点击下拉框掉接口成功的函数 结束======*/


        //点下一步检测输入框是否有值
        function nextStepCheck() {
            var condition,
                mark_false = 'red_border',
                $newCarNum = $(".newCarNum"),
                $invoiceDate = $(".invoiceDate"),
                invoiceDate_parent = $newCarNum.parents(".div");        //新车购置发票号
            if (!invoiceDate_parent.hasClass("dis")) {
                var newCNV = $newCarNum.val(),
                    invoiceDate = $invoiceDate.val();
                condition = newCNV.length > 50;
                Smart.judge_input($newCarNum, newCNV, "填写新车购置发票号", mark_false, "新车购置发票号字数超过最大限制", condition);
                Smart.judge_input($invoiceDate, invoiceDate, "选择发票开具日期", mark_false, null, null);
            }
            //品牌型号的值
            var $tdText = $(".tdText");
            var tdText = $tdText.text();
            Smart.judge_input($tdText, tdText, "选择品牌型号", mark_false, null, null);
            //座位数
            var $seat = $('[name="seatNumber"]'),
                seatNum = $seat.val();
            condition = !/^\d+$/.test($.trim(seatNum));
            Smart.judge_input($seat, seatNum, "填写座位号", mark_false, '座位数必须为数字', condition);
            //发动机号的值
            var $motor = $(".motorNum"),
                motorNum = $motor.val();
            condition = motorNum.length > 20;
            Smart.judge_input($motor, motorNum, "填写发动机号", mark_false, '发动机号格式不正确', condition);
            //车架号的值
            var $frame_num = $(".frame_num");
            var frame_num = $frame_num.val();
            condition = frame_num.length != 17;
            Smart.judge_input($frame_num, frame_num, "填写车架号", mark_false, '车架号格式不正确', condition);
            //车辆登记日期的值
            var $register = $(".registerDate"),
                registerDate = $register.val();
            Smart.judge_input($register, registerDate, "选择车辆登记日期", mark_false, null, null);
            //如果过户车那栏勾选了
            var transferState = $(".transferCar").prop("checked");
            var $transferTime = $(".ownershipTime");
            var transferTime = $transferTime.val();
            if (transferState) {
                Smart.judge_input($transferTime, transferTime, "填写过户日期", mark_false, null, null);
            }
            //车主姓名的值
            var $COwnerName = $(".COwnerName"),
                COwnerName = $COwnerName.val();
            condition = COwnerName.length > 30;
            Smart.judge_input($COwnerName, COwnerName, "填写车主姓名", mark_false, '车主姓名字数超过限制', condition);
            //证件号码的值
            var $IDNumber = $(".IDNumber"),
                IDNumber = $IDNumber.val(),
                code = $('.js-card-type').data('typecode');
            if (code == '120001') {
                condition = IDNumber.length != 18;
                Smart.judge_input($IDNumber, IDNumber, "填写证件号码", mark_false, '请正确输入18位身份证号码', condition);
            } else {
                condition = IDNumber.length > 20;
                Smart.judge_input($IDNumber, IDNumber, "填写证件号码", mark_false, '证件号码字数超过限制', condition);
            }
            //手机号的值
            var $OwnerPhNum = $(".OwnerPhNum"),
                OwnerPhNum = $OwnerPhNum.val();
            condition = !(/^1[34578]\d{9}$/.test(OwnerPhNum));
            Smart.judge_input($OwnerPhNum, OwnerPhNum, "填写手机号码", mark_false, '手机格式不正确', condition);
            //被保人的姓名
            if (!$(".recognizeeN").hasClass("dis")) {
                var $insuredName = $(".insuredName"),
                    insuredName = $insuredName.val();
                condition = IDNumber.length > 30;
                Smart.judge_input($insuredName, insuredName, "填写被保人姓名", mark_false, '被保人姓名字数超过限制', condition);
            }
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

        //转换时间
        function formatDate(Time, boolean) {         //boolean为true为推迟一年后的时间
            var time = new Date(Time);
            var year,
                date,
                day = time.getDate(),
                hour,
                hour2,
                min,
                min2,
                sec,
                sec2;
            if (boolean) {
                time.setDate(day - 1);
                date = time.getDate();          //日
                year = time.getFullYear() + 1;      //年
                hour2 = 23;
                min2 = 59;
                sec2 = 59;
            } else {
                year = time.getFullYear();      //年
                date = time.getDate();          //日
                hour = time.getHours();         //时
                hour2 = hour < 10 ? "0" + hour : hour;
                min = time.getMinutes();        //分
                min2 = min < 10 ? "0" + min : min;
                sec = time.getMinutes();        //秒
                sec2 = sec < 10 ? "0" + sec : sec;
            }
            var mon = time.getMonth() + 1;        //月
            var mon2 = mon < 10 ? "0" + mon : mon;
            var date2 = date < 10 ? "0" + date : date;
            return year + "-" + mon2 + "-" + date2 + " " + hour2 + ":" + min2 + ":" + sec2
        }
//主险勾选时附加险的勾选框的变化
        function isOrNo(mainCheck, additionalCheck) {
            var isState = mainCheck.prop("checked");
            if (!isState) {                  //如果主险未被勾选
                $(additionalCheck).attr("checked", false);
            }
        }
    })

    function initdataBPrice(){
        var chengke=$('#chengke').val();
        var siji=$('#siji').val();
        var sanze=$('#sanze').val();
        var boli=$('#boli').val();
        var huahen=$('#huahen').val();
        if(chengke!='0'){
            $('#chengke').parents('.touB').data('bprice',chengke);
        }
        if(siji!='0'){
            $('#siji').parents('.touB').data('bprice',siji);
        }
        if(sanze!='0'){
            $('#sanze').parents('.touB').data('bprice',sanze);
        }
        if(boli!=0){
            $('#boli').parents('.touB').data('bprice',boli);
        }
        if(huahen!=0){
            $('#huahen').parents('.touB').data('brpice',huahen);
        }
    }
});
