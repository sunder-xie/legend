/**
 * @fileOverview 通用浮出层
 * @author zhanghaiyong
 * @createTime 2014/11/13
 * @updateTime 2014/11/20
 * @version 1.1.1
 */

taoqi = (function () {
    var arr = navigator.userAgent.match(/MSIE (\d+)\.\d+;/);
    var INPUT_EVENT = arr && arr[1] < 9 ? 'propertychange' : 'input';
//    添加新客户
    var _userId;
    Model.addUser = function (params, success) {
        var url = BASE_PATH + '/shop/customer/insert';
        Dao.postData(url, params, success);
    };

    Model.getCustomerSource = function () {
        var url = BASE_PATH + '/shop/customer_source/list';
        Dao.xhrRender({
            action: url,
            mark: true,
            tag: 'customerSourcel',
            tpl: 'customerSourceTpl',
            fill: '~'
        });
    };
    Model.getUserFushu = function (success) {
        var queue = [
            { action: BASE_PATH + '/shop/customer/get_license_prefix', params: {} },
            { action: BASE_PATH + '/shop/customer/insurance_company_list', params: {} }
        ];
        Dao.queueHtml({
            queue: queue,
            tpl: 'userBoxTpl'
        }, success);
    };
    function addUser(data, needRefresh,callback) {
        if (_userId != undefined)
            return false;
        Model.getUserFushu(function (html, data) {
            _userId = $.layer({
                type: 1,
                title: false,
                area: ['auto', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [0, false], //去掉默认关闭按钮
                shift: 'top',
                move: '.us_t span',
                page: {
                    html: html
                }
            });
            $('.lbut').click(function () {
                layer.close(_userId);
                _userId = undefined;
            });

            var curDom;
            $('#com_source').focus(function () {
                $(this).val('');
                curDom = $(this);
                Model.getCustomerSource();
                var l = $(this).offset().left - $('.xubox_main').offset().left;
                var h = $(this).offset().top - $('.xubox_main').offset().top + 28;
                $('#customerSourcel').css("left", l);
                $('#customerSourcel').css("top", h);
                $('#customerSourcel').show();
            });

            $('#customerSourcel').on('mousedown', 'a', function () {
                if (curDom) {
                    $(curDom).val($(this).text());
                    $(curDom).data('id', $(this).data('id'));
                    curDom = null;
                }
            });

            $('#com_source').blur(function () {
                $('#customerSourcel').hide().empty();
            });

            //
            var carCompany = '';
            var importInfo = '';
            var carBrandId = 0;
            var carSeriesId = 0;
            var carBrand = '';
            var carSeries = '';
            $('#com_carModelId').focus(function () {
                taoqi.modelSelection(function (data) {
                    carCompany = data.company;
                    importInfo = data.importInfo;
                    carBrandId = data.pid;
                    carBrand = data.brand;
                    carSeriesId = data.id;
                    carSeries = data.name;
                    var temp = '';
                    if (carBrand) {
                        temp = temp + carBrand;
                    }
                    if (carSeries) {
                        temp = temp + carSeries;
                    }
                    if (importInfo) {
                        temp = temp + "(" + importInfo + ")"
                    }
                    $('#com_carModelId').val(temp);
                });
            });
            $('.rbut').click(function () {
                var params = {
                    license: $.trim($('#com_license').val()),
                    carModelId: $.trim($('#com_carModelId').val()),
                    customerName: $.trim($('#com_customerName').val()),
                    mobile: $.trim($('#com_mobile').val()),
                    vin: $.trim($('#com_vin').val()),
                    carCompany: carCompany,
                    importInfo: importInfo,
                    carBrandId: carBrandId,
                    carBrand: carBrand,
                    carSeriesId: carSeriesId,
                    carSeries: carSeries,
                    byName: $.trim($('#com_byName').val()),
                    buyTimeStr: $.trim($('#com_buyTimeStr').val()),
                    carNumber: $.trim($('#com_carNumber').val()),
                    engineNo: $.trim($('#com_engineNo').val()),
                    auditingTimeStr: $.trim($('#com_auditingTimeStr').val()),
                    drivingLicense: $.trim($('#com_drivingLicense').val()),
                    customerAddr: $.trim($('#com_customerAddr').val()),
                    birthdayStr: $.trim($('#com_birthdayStr').val()),
                    company: $.trim($('#com_company').val()),
                    licenseType: $.trim($("#com_licenseType").val()),
                    mileage: $.trim($("#com_mileage").val()),
                    color: $.trim($("#com_color").val()),
                    receiveLicenseTimeStr: $.trim($("#com_receiveLicenseTimeStr").val()),
                    insuranceTimeStr: $.trim($("#com_insuranceTimeStr").val()),
                    insuranceId: $.trim($("#com_insuranceId").val()),
                    insuranceCompany: $.trim($("#com_insuranceId option:selected").html()),
                    contact: $.trim($("#com_contact").val()),
                    identityCard: $.trim($("#com_identityCard").val()),
                    contactMobile: $.trim($("#com_contactMobile").val()),
                    customerSource: $.trim($("#com_source").val()),
                    productionDateStr: $.trim($("#com_productionDateStr").val())
                };

                Model.addUser(params, function (data) {
                    if (data.success) {
                        success('操作成功！');
                        layer.close(_userId);
                        _userId = undefined;

                        if (needRefresh == true) {
                            window.location.reload();
                        } else {
                            $("#car_id").val(data.data.id);
                            $("#plate_nubmer").val(data.data.license);
                            $("#vin").val(data.data.vin);
                            $("#car_model").val(data.data.carBrand + data.data.carSeries + data.data.importInfo);
                            if ($.trim(data.data.contact) == "")
                                $("#customer_name").val(data.data.customerName);
                            else
                                $("#customer_name").val(data.data.contact);
                            if ($.trim(data.data.contactMobile) == "")
                                $("#userMobile").val(data.data.mobile);
                            else
                                $("#userMobile").val(data.data.contactMobile);

                            $("#byName").val(data.data.byName);

                            //2015-03-03 added by lgx
                            $("#color").val(data.data.color);
                            $("#mileage").val(data.data.mileage);
                            $("#due_date").val(data.data.insuranceTimeStr);
                            $("#insurance").children().eq(data.data.insuranceId).prop('selected', true);
                            $("#insurance").trigger("chosen:updated");

                            //2015-01-16 16:41:34 add by zwb
                            $("#car_brand_id").val(data.data.carBrandId);
                            $("#car_brand_name").val(data.data.carBrand);
                            $("#car_series_id").val(data.data.carSeriesId);
                            $("#car_series_name").val(data.data.carSeries);
                            if ($("#plate_nubmer").prop("tagName").toUpperCase() == "SELECT") {
                                $("#plate_nubmer option:eq(0)").after(
                                        '<option value="' + data.data.license + '" selected="selected">' + data.data.license + '</option>'
                                )
                                $("#plate_nubmer").trigger("chosen:updated");
                            }

                            if(callback && typeof callback == 'function'){
                                callback(data.data.license);
                            }
                        }
                    } else {
                        error(data.errorMsg);
                    }
                });
            });
        });
    }

//    添加员工账号
    var _staffId;
    Model.addStaff = function (params, success) {
        var url = BASE_PATH + '/shop/roles_func/add_user';
        Dao.postData(url, params, success);
    };

    Model.checkMobile = function (params, success) {
        var url = BASE_PATH + '/shop/roles_func/check-mobile';
        Dao.getData(url, params, success);
    };

    //时间
    $(document).on('click', '.clockpicker', function (e) {
        var clock = $(this).find('.clockpicker-popover');

        if (!clock.length) {
            clock = $('body > .clockpicker-popover');
            $(this).append(clock);
        }
        clock.css({
            top: 'initial',
            left: '0',
            position: 'absolute'
        });
        e.stopImmediatePropagation();
    });

    //时间校验
    $(document).on('blur','.js-time-ico',function(){
        var str = $.trim($(this).val());
        if(str.length!=0){
            var reg=/^((20|21|22|23|[0-1]\d)\:[0-5][0-9])(\:[0-5][0-9])?$/;
            if(!reg.test(str)){
                taoqi.failure("您输入的时间不正确");
            }
        }
    });

    function addStaff() {
        var width;
        if (_staffId != undefined)
            return false;
        _staffId = $.layer({
            type: 1,
            title: false,
            area: ['auto', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.5, '#000000'],
            closeBtn: [0, false], //去掉默认关闭按钮
            shift: 'top',
            move: '.us_t span',
            page: {
                html: Dao.html('staffBoxTpl', null)
            }
        });
        $(".clockpicker").clockpicker();

        // 控制账户下输入框的长度
        //width = $('.xubox_page').find('.tz_yg')[0].clientWidth;
        //$('#userAccount').css('width', 258 - width);

        $(":checkbox[name='pvgRoleId']").each(function () {
            if ($(this).val() ==  $('#currentPvgRoleId').val()) {
                $(this).attr("checked", "checked");
            }
        });
        $('.lbut').click(function () {
            layer.close(_staffId);
            _staffId = undefined;
        });

        //添加用户
        function addUser(params) {
            Model.addStaff(params, function (result) {
                if (result.success) {
                    taoqi.success("操作成功");
                    var temp = result.data;
                    $('.employee-accout-div').html(template.render('employeeAccoutDivTpl', {'templateData': temp}));
                    layer.close(_staffId);
                    _staffId = undefined;
                } else {
                    taoqi.error(result.errorMsg);
                }
            });
        }

        $('.rbut').click(function () {
            var preUserAccount = $.trim($('#shopName').val());
            var userAccount = $.trim($('#userAccount').val());
            var userPassword = $.trim($('#userPassword').val());
            var userName = $.trim($('#userName').val());
            var userMobile = $.trim($('#userMobile').val());
            var teamId = $.trim($('#teamId').val());
            var teamName = $.trim($('#teamId option:selected').text());
            var processId = $.trim($('#processId').val());
            var processName = $.trim($('#processId option:selected').text());
            var startTime = $.trim($("input[name='startTime']").val());
            var endTime = $.trim($("input[name='endTime']").val());

            var pvgRoleIds = [];
            //时间校验
            var flag=true;
            $('.js-time-ico').each(function(){
                var str = $.trim($(this).val());
                var reg=/^((20|21|22|23|[0-1]\d)\:[0-5][0-9])(\:[0-5][0-9])?$/;
                if(!reg.test(str)){
                    flag = false;
                }

            });
            if(!flag){
                taoqi.failure("您输入的时间不正确");
                return;
            }


            $(":checkbox[name='pvgRoleId']").each(function () {
                if ($(this).prop('checked')==true) {
                    pvgRoleIds.push($(this).val());
                }
            });
            if (preUserAccount == null || preUserAccount == "") {
                taoqi.failure("请输入账号前缀");
                return;
            }
            if (userAccount == null || userAccount == "") {
                taoqi.failure("请输入账号");
                return;
            }
            if (userPassword == null || userPassword == "") {
                taoqi.failure("请输入密码");
                return;
            }
            if (userName == null || userName == "") {
                taoqi.failure("请输入姓名");
                return;
            }
            if (userMobile == null || userMobile == "") {
                taoqi.failure("请输入手机号码");
                return;
            }
            if(pvgRoleIds == null || pvgRoleIds ==''){
                taoqi.failure("请选择APP 角色");
                return;
            }
            if(startTime == null || startTime == ''){
                taoqi.failure("登陆开始时间不能为空");
                return;
            }
            if(endTime == null || endTime == ''){
                taoqi.failure("登陆结束时间不能为空");
                return;
            }


            var online = $('#job').is(':checked');
            var workStatus = 1;
            if(online){
                workStatus = 1;
            }else{
                workStatus = 0;
            }


            var params = {
                preUserAccount: preUserAccount,
                userAccount: userAccount,
                userPassword: userPassword,
                userName: userName,
                userMobile: userMobile,
                userRoleId: $('#currentSelectRolesId').val(),
                pvgRoleIds: pvgRoleIds.join(','),
                teamId: teamId,
                teamName: teamName,
                processId: processId,
                processName: processName,
                workStatus: workStatus,
                startTime : startTime,
                endTime : endTime
            };
            var mobileparams = {
                mobile:userMobile
            };
            Model.checkMobile(mobileparams, function (result) {
                if (result.success) {
                    var boolean = result.data;
                    if(boolean){
                        taoqi.ask("此手机号存在于其他门店，是否继续添加？",function (){
                            addUser(params);
                        },function(){
                            return false;
                        });
                    } else {
                        addUser(params);
                    }
                } else {
                    taoqi.error(result.errorMsg);
                }
            });
        });
    }

    //非工作时间登录验证码
    var loginIdentifyId;
    Model.sendLoginIdentify = function (params, success) {
        var url = BASE_PATH + '/index/login_validate_code';
        Dao.postData(url, params, function (data) {
            if (data.success == true) {
                $("#loginForm").submit();
                taoqi.close(loginIdentifyId);
            }
            else {
                taoqi.error(data.errorMsg);
            }
        }, success);
    }
    function setLoginIdentify(callback) {
        if (loginIdentifyId != undefined)
            return false;
        loginIdentifyId = $.layer({
            type: 1,
            title: false,
            area: ['auto', 'auto'],
            border: [0],
            shadow: ['0.5', '#000'],
            closeBtn: [0, false],
            shift: "top",
            // bgcolor: "#fff",
            page: {
                html: Dao.html('loginIdentifyTpl', null)
            }
        });

        //提交验证码
        $(document).on('click', '.submitCode', function () {
            var identifyCode = $('.identifyTxt').val();
            var userId = $('.userId').val();
            var params = {
                code: identifyCode,
                id: userId
            };
            Model.sendLoginIdentify(params, null);

        });

        //关闭窗口
        $(document).on('click', '.cancelIdentify', function () {
            layer.close(loginIdentifyId);
            loginIdentifyId = undefined;
        });

        //获取验证码
        $(document).on('click', '.getCodeBtn', function () {
            if ($(this).hasClass("locks")) {
                return;
            }
            $(this).addClass("locks");
            var t = 60;
            var _this = this;
            $(this).val('重获验证码({0})'.format(t));
            var tid = setInterval(function () {
                if (--t) {
                    $(_this).val('重获验证码({0})'.format(t));
                } else {
                    clearInterval(tid);
                    $(_this).val('点击获取验证码');
                    $(_this).removeClass('locks');
                }
            }, 1000);
            var url = BASE_PATH + '/index/send_validate_code';
            var params = {
                shopId: $('.loginShop').val(),
                account: $('#username').val()
            };
            Dao.getData(url, params, function (data) {
                if (data.success == true) {
                    $('.userId').val(data.data);
                    taoqi.success("验证码已发送至管理员！");
                }
                else {
                    taoqi.error(data.errorMsg);
                }

            }, true);


        });
    }

//    提醒设置
    var _remindId;
    Model.getRemind = function (success) {
        var url = BASE_PATH + '/shop/remind_menu/get_short_remind_menu_choose';
        Dao.xhrHtml({
            action: url,
            tpl: 'remindTpl'
        }, success);
    };
    function setRemind(callback) {
        if (_remindId != undefined)
            return false;
        Model.getRemind(function (html) {
            _remindId = $.layer({
                type: 1,
                title: false,
                area: ['328px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                bgcolor: '#ffffff',
                move: '.rmtit',
                end: function () {
                    _remindId = undefined;
                },
                page: {
                    html: html
                }
            });

            $('.me_c').on('click', '.me_tit', function () {
                $(this).toggleClass('current');
                $(this).next().slideToggle();
            });

            $('.baocun').click(function () {
                var arr = [];
                $('.rmcheck:checked').each(function () {
                    arr.push($(this).data('obj'));
                });
                arr.length >= 0 && callback(arr);
                close(_remindId);
            });
        });
    }

//   预付款确认
    var _preSettlePayment;
    Model.getPreSettleInfo = function (id, success) {
        var url = BASE_PATH + '/shop/pre_payment_log/get';
        Dao.xhrHtml({
            action: url,
            params: {
                id: id
            },
            tpl: 'preSettleTpl'
        }, success);
    };

    function checkMemberAmount(obj)
    {
        var $this = obj;
        var $member_balance = $this.parent().find("#member_balance");
        if (Number($.trim($member_balance.text())) < Number($.trim($this.val()))) {
            taoqi.error("消费金额大于会员卡余额，请检查！");
            return false;
        } else {
            var signAmount = payCalculate();
            $('.guazhangJe').text(signAmount.toFixed(2));
            return true;
        }
    }
    function goPreSettlePayment(id, callback) {
        if (_preSettlePayment != undefined)
            return false;
        Model.getPreSettleInfo(id, function (html) {
            _preSettlePayment = $.layer({
                type: 1,
                title: false,
                fix: false,
                area: ['395px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                bgcolor: '#ffffff',
                move: '.js_tit',
                end: function () {
                    _preSettlePayment = undefined;
                },
                page: {
                    html: html
                }
            });

            var curDom;
            $('.fangsi').focus(function () {
                $(this).val('');
                curDom = $(this);
                Model.getPayment();
                var l = $(this).offset().left - $('.xubox_main').offset().left;
                var h = $(this).offset().top + $(this).parents('.jc_xm_for_fangshi').height() - $('.xubox_main').offset().top;
                $('#settle1').css("left", l);
                $('#settle1').css("top", h);
                $('#settle1').show();
            });

            $('#settle1').on('mousedown', 'a', function () {
                if (curDom) {
                    $(curDom).val($(this).text());
                    $(curDom).data('id', $(this).data('id'));
                    curDom = null;
                }
            });

            $('.fangsi').blur(function () {
                //算出结算方式金额
                var paymentValue = 0.00;
                //标记是否有结算方式
                var flag = false;
                $('.fangsi').each(function () {
                    if ($.trim($(this).val()) == '') {
                        flag = true;
                    }
                    if (!flag) {
                        paymentValue += Number($.trim($(this).next().val()));
                    }
                });
                $('.yufu_Je').text(paymentValue.toFixed(2));
                $('#settle1').hide().empty();
            });
            $('.js_jiner').blur(function () {
                //算出结算方式金额
                var paymentValue = 0.00;
                //标记是否有结算方式
                var flag = false;
                $('.fangsi').each(function () {
                    if ($.trim($(this).val()) == '') {
                        flag = true;
                    }
                    if (!flag) {
                        paymentValue += Number($.trim($(this).next().val()));
                    }
                });
                $('.yufu_Je').text(paymentValue.toFixed(2));
            });

            $('.btnjforJs').click(function () {
                var li = $(this).parent(),
                    c = 'jian';
                if (!$(this).hasClass(c)) {
                    var d = li.clone(true);
                    d.find('input').val('');
                    $(this).addClass(c);
                    d.insertAfter(li);
                } else {
                    li.remove();
                    var paymentValue = 0.00;

                    //标记是否有结算方式
                    var flag = false;
                    $('.fangsi').each(function () {
                        if ($.trim($(this).val()) == '') {
                            flag = true;
                        }
                        if (!flag) {
                            paymentValue += Number($.trim($(this).next().val()));
                        }
                    });
                    $('.yufu_Je').text(paymentValue.toFixed(2));
                }
            });


            $('.js_ok').click(function () {

                if(!checkMemberAmount($("#member_pay_amount"))){
                    return;
                }

                var paymentValue = 0.00;
                var flag = false;
                $('.fangsi').each(function () {
                    if ($.trim($(this).val()) == '') {
                        flag = true;
                    }
                    paymentValue += Number($.trim($(this).next().val()));
                });
                if (flag) {
                    taoqi.failure("请选择或输入结算方式");
                    return;
                }

                if (paymentValue != Number($.trim($('.yufu_Je').text()))) {
                    taoqi.failure("结算金额错误");
                    return;
                }
                callback && callback();
                close(_preSettlePayment);
            });

            $('.js_no').click(function () {
                window.location.reload();
                close(_preSettlePayment);
            });

        });
    }


//    结算信息确认
    var _settleId;
    Model.getSettleInfo = function (id, success) {
        var url = BASE_PATH + '/shop/settlement/settlement_order';
        Dao.xhrHtml({
            action: url,
            params: {
                id: id
            },
            tpl: 'settleTpl'
        }, success);
    };

    Model.getPayment = function () {
        var url = BASE_PATH + '/shop/payment/get_payment';
        Dao.xhrRender({
            action: url,
            mark: true,
            tag: 'settle1',
            tpl: 'settle1Tpl',
            fill: '~'
        });
    };

    Model.getCoupon = function (txt, success) {
        var url = BASE_PATH + '/shop/coupon/get_coupon';
        Dao.getData(url, {
            CouponSn: txt
        }, success);
    };

    function settleMoney() {
        var yf = Number($.trim($('.yingfu').text())),
            dz = Number($.trim($('.dazhe').val())),
            fy = Number($.trim($('.feiyong').val())),
            yh = Number($.trim($('.youhui').val())),
            hyyh = Number($.trim($('.huiyuanyouhui').val())),
            djq = 0;
        if (dz <= 0) {
            taoqi.failure("打折信息错误 大于0小于100");
            return false;
        }
        if (dz > 100) {
            $('.dazhe').val("100");
            dz = 100;
        }
        dz = dz / 100;
        $('.daijin').each(function () {
            if ($.trim($(this).next().val()) != null && $.trim($(this).next().val()) != '') {
                djq += Number($.trim($(this).val()));
            }
        });
        var total = yf * dz + fy - yh - djq - hyyh;
        $('.shifu').text(total.toFixed(2));

        var signAmount = payCalculate();
        $('.guazhangJe').text(signAmount.toFixed(2));
    }

    function payCalculate() {
        //算出结算方式金额
        var paymentValue = 0.00;
        var total = Number($('.shifu').text());
        var payedAmount = Number($('.yifuJe').val());
        var mpayAmount = Number($.trim($('#member_pay_amount').val()));
        if(isNaN(mpayAmount))
        {
            mpayAmount = 0.00;
        }
        var mpServiceAmount = 0;
        //会员服务项目F
        if($('#member_flag').val() == "0")
        {
            $('.member_entry_count').each(function () {
                mpServiceAmount += Number(($(this).data("total_count") - $(this).val()) * $(this).siblings(".member_service_avg").val());
            });
        }

        //标记是否有结算方式
        $('.fangsi').each(function () {
            if ($.trim($(this).val()) != '') {
                paymentValue += Number($.trim($(this).siblings(".js_jiner").val()));
            }
        });
        var signAmount = total - paymentValue - payedAmount - mpayAmount - mpServiceAmount;
        return signAmount;
    }


    function goSettle(id, callback) {
        if (_settleId != undefined)
            return false;
        Model.getSettleInfo(id, function (html) {
            _settleId = $.layer({
                type: 1,
                title: false,
                fix: false,
                area: ['auto', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                bgcolor: '#ffffff',
                move: '.js_tit',
                end: function () {
                    _settleId = undefined;
                },
                page: {
                    html: html
                }
            });
            var payStatus = $.trim($("#payStatus").val());

            if (payStatus != "" && payStatus == 0) {
                settleMoney();
                $('.btnj').click(function () {
                    var li = $(this).parent(),
                        c = 'jian';
                    if (!$(this).hasClass(c)) {
                        var d = li.clone(true);
                        d.find('input').val('');
                        $(this).addClass(c);

                        d.find('.daijin').attr("readonly", false)
                        d.insertAfter(li);
                        settleMoney();
                    } else {
                        li.remove();
                        settleMoney();
                    }
                });
            } else {
                $('.jc_set').find("input[type=text]").attr("readonly", "readonly");
                $('.jc_set').find(".mp_unit").addClass("readonly");
            }
            $('.btnjforJs').click(function () {
                var li = $(this).parent(),
                    c = 'jian';
                if (!$(this).hasClass(c)) {
                    var d = li.clone(true);
                    d.find('input').val('');
                    $(this).addClass(c);
                    d.insertAfter(li);
                } else {
                    li.remove();
                    var signAmount = payCalculate();
                    $('.guazhangJe').text(signAmount.toFixed(2));
                }
            });

            if (payStatus != "" && payStatus == 0) {
                $('.daijin').blur(function () {
                    settleMoney();
                });
                $('.dazhe').blur(function () {
                    settleMoney();
                });
                $('.feiyong').blur(function () {
                    settleMoney();
                });
                $('.youhui').blur(function () {
                    settleMoney();
                });
                $('.huiyuanyouhui').blur(function () {
                    settleMoney();
                });
                $('.quanhao').blur(function () {
                    var $q = $(this);
                    $q.parent().siblings().each(function () {
                        if ($q.val() == $(this).find('.quanhao').val()) {
                            taoqi.failure("优惠券重复");
                            $q.parent().prev().find('.btnj').removeClass('jian');
                            $q.parent().remove();
                            return;
                        }
                    });
                    var v = $.trim($(this).val());
                    if (v == '')
                        return;
                    var $t = $(this).prev();
                    Model.getCoupon(v, function (data) {
                        if (data.success) {
                            if (data.data != null) {
                                if (data.data.isUsed == 'N') {
                                    $t.val(data.data.couponValue);
                                    $t.attr('readonly', 'readonly');
                                } else {
                                    failure('对不起！该代金券已经使用');
                                    $t.attr('readonly', 'readonly');
                                    $t.val(0.00);
                                }
                            } else {
                                $t.removeAttr('readonly');
                            }
                            settleMoney();
                        } else {
                            $t.removeAttr('readonly');
                        }
                    });
                });
            }

            var curDom;
            $('.fangsi').focus(function () {
                $(this).val('');
                curDom = $(this);
                Model.getPayment();
                var l = $(this).offset().left - $('.xubox_main').offset().left;
                var h = $(this).offset.top + $(this).parents('.jc_xm_for_fangshi').height() - $('.xubox_main').offset().top;
                $('#settle1').css("left", l);
                $('#settle1').css("top", h);
                $('#settle1').show();
            });


            $('.fangsi').blur(function () {
                var signAmount = payCalculate();
                $('.guazhangJe').text(signAmount.toFixed(2));
                $('#settle1').hide().empty();
            });
            $('.js_jiner').blur(function () {
                var signAmount = payCalculate();
                $('.guazhangJe').text(signAmount.toFixed(2));
            });

            $('#settle1').on('mousedown', 'a', function () {
                if (curDom) {
                    $(curDom).val($(this).text());
                    $(curDom).data('id', $(this).data('id'));
                    curDom = null;
                }
            });

            var mp_card_btn_fn = function(){
                var cardSn = $("#mp_card_number").val();
                var carId = $(".set_car_id").val();
                var $this = $(this);
                $.ajax({
                    type: "POST",
                    url: BASE_PATH + "/shop/card/member/info",
                    data: {
                        cardSn: cardSn,
                        carId: carId
                    },
                    dataType: "json",
                    success: function (data) {
                        $(".mp_service").empty();
                        if (data.success == true) {
                            $this.prop("disabled", "disabled");
                            $('.mp_wrong').hide();
                            $('.mp_right').text("该会员卡折扣为：" + data.data.discount + "%");
                            $('.mp_right').show();
                            clickFlag = 1;
                            if(data.data.cardSn != null)
                            {
                                var html = Dao.html('memberServiceTpl', data);
                                $('.mp_service').append(html);
                                $('.mp_service').on("blur", "#member_pay_amount", function () {
                                    var $member_balance = $(this).parent().find("#member_balance"),
                                        $this = $(this);
                                    if (Number($.trim($member_balance.text())) < Number($.trim($this.val()))) {
                                        taoqi.error("消费金额大于会员卡余额，请检查！");
                                    } else {
                                        var signAmount = payCalculate();
                                        $('.guazhangJe').text(signAmount.toFixed(2));
                                    }
                                });
                                $('.mp_service').on("click", ".mp_service_sub", function () {
                                    var $member_entry_count = $(this).siblings(".member_entry_count"),
                                        $member_entry_value = $(this).parent().find(".member_entry_value"),
                                        member_service_avg = $(this).siblings(".member_service_avg").val(),
                                        $guazhangJe = $(".guazhangJe");
                                    if ($member_entry_count.val() > 0) {
                                        $member_entry_count.val((parseInt($member_entry_count.val() * 100) / 100 - 1).toFixed(0));
                                        $member_entry_value.text((parseInt($member_entry_value.text() * 100) / 100 - member_service_avg).toFixed(2));
                                        $guazhangJe.text((parseInt($guazhangJe.text() * 100) / 100 - member_service_avg).toFixed(2));
                                    }
                                    if ($member_entry_count.val() <= 0) {
                                        $(this).prop("disabled", "disabled").addClass("readonly");
                                    }
                                });
                                $('.mp_service').on("click", ".mp_service_sub", function () {
                                    var signAmount = payCalculate();
                                    $('.guazhangJe').text(signAmount.toFixed(2));
                                });
                            }
                            else
                            {
                                var html = Dao.html('chainMemberServiceTpl', data);
                                $('.mp_service').append(html);
                                $('.mp_service').on("blur", "#member_pay_amount", function () {
                                    checkMemberAmount($(this));
                                });
                                $('.mp_service').on("click", ".mp_service_sub", function () {
                                    var $member_entry_count = $(this).siblings(".member_entry_count"),
                                        $member_entry_value = $(this).parent().find(".member_entry_value");
                                    if ($member_entry_count.val() > 0) {
                                        $member_entry_count.val((parseInt($member_entry_count.val() * 100) / 100 - 1).toFixed(0));
                                    }
                                    if ($member_entry_count.val() <= 0) {
                                        $(this).prop("disabled", "disabled").addClass("readonly");
                                    }
                                });
                                $('.mp_service').on("click", ".mp_service_sub", function () {
                                    var signAmount = payCalculate();
                                    $('.guazhangJe').text(signAmount.toFixed(2));
                                });
                                $('.mp_right').hide();
                                var cut = $(".choice-number .cut"),
                                    add = $(".choice-number .add");
                                cut.on('click', function () {
                                    if($(".choice-number .reduce").hasClass("set-green")){
                                        var numberObj = $(this).next(),
                                            number = numberObj.val();
                                        if(number < 1){
                                            return;
                                        }
                                        else {
                                            numberObj.val(--number);
                                        }
                                    }
                                });
                                //递增
                                add.click(function () {
                                    if($(".choice-number .reduce").hasClass("set-green")) {
                                        var numberObj = $(this).prev(),
                                            number = numberObj.val(),
                                            numberN = $(this).parents("td").find(".number-n").text();
                                        if(number == numberN){
                                            return;
                                        }
                                        else{
                                            numberObj.val(++number);
                                        }
                                    }
                                });
                            }


                        } else {
                            if(data.code == "0")
                            {
                                $('.mp_wrong').html('会员卡无效或未与车辆绑定').show();
                            }
                            else{
                                $('.mp_wrong').html('会员卡无效或未与车辆绑定，<a href="/legend/shop/chain/member/addMemeberPage" class="js_fc">立即绑定</a>').show();
                            }
                            $('.mp_right').hide();
                        }
                    }
                });
            }

            mp_card_btn_fn();
            $(".js_k").on("click", ".mp_card_btn", function (e) {
                mp_card_btn_fn();
            });

            $('.js_ok').click(function () {
                if(!checkMemberAmount($("#member_pay_amount"))){
                    return;
                }
                var signAmount = Number($.trim($('.guazhangJe').text()));
                if (signAmount < 0) {
                    taoqi.failure("挂账金额小于0");
                    return;
                }
                var paymentValue = 0.00;
                var flag = false;
                $('.fangsi').each(function () {
                    if ($.trim($(this).val()) == '') {
                        flag = true;
                    }
                    paymentValue += Number($.trim($(this).next().val()));
                });
                if (flag && !$("#member_id").val()) {
                    taoqi.failure("请选择或输入结算方式");
                    return;
                }

//                if (paymentValue + Number($.trim($('.guazhangJe').text())) + Number($.trim($('#member_pay_amount').val())) + Number($.trim($('.yifuJe').val())) != Number($.trim($('.shifu').text()))) {
//                    taoqi.failure("结算金额错误");
//                    return;
//                }
                callback && callback();
                close(_settleId);
            });

            $('.js_no').click(function () {
                window.location.reload();
                close(_settleId);
            });


        });
    }

    //会员金额充值
    var _chargeAmountId;
    function addMemberAmount(memberInfo, callback) {
        if (_chargeAmountId) {
            return false;
        }
        var data = {
            serial: memberInfo.serial,
            balance: $("#balance").val()
        };
        _chargeAmountId = $.layer({
            type: 1,
            title: false,
            area: ['auto', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.5, '#000000'],
            closeBtn: [1, true],
            shift: 'top',
            bgcolor: '#ffffff',
            end: function () {
                _chargeAmountId = undefined;
            },
            page: {
                html: Dao.html("chargeAmountTpl", data)
            }
        });
        $('.chg_ok').on('click', function() {
            var cur_balance = Number($.trim($('.rechg_balance').val()));
            if(cur_balance <= 0) {
                return false;
            }
            memberInfo.balance = cur_balance;
            var save = taoqi.loading("余额充值, 请稍后...");
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: BASE_PATH + "/shop/card/member/charge",
                data: {
                    memberInfo: $.toJSON(memberInfo),
                    serviceId: 0
                },
                cache: false,
                success: function (data) {
                    taoqi.close(save);
                    if (!data.success) {
                        taoqi.error(data.errorMsg);
                    } else {
                        taoqi.success("余额充值成功!");
                        window.location.href = "/legend/shop/card/member/detail?Id=" + data.data + "&act=edit"
                    }
                },
                error: function (a, b, c) {

                }
            });
        });
        $('.chg_cancel').on("click", function() {
            close(_chargeAmountId);
        });
    }

    //服务项目充值
    var _serviceId;
    function addServiceCount(memberInfo, self, callback) {
        if (_serviceId) {
            return false;
        }
        var $wash_count = self.siblings('#wash_count'),
            serviceId = $(this).siblings(".service_id").val(),
            service, service_count, balance;
        if($wash_count.length > 0) {
            service = "洗车";
            service_count = $.trim($wash_count.val());
            balance = $.trim($('#wash_value').val());
        } else {
            service = self.siblings('.service_name').val();
            service_count = $.trim(self.siblings('.service_count').val());
            balance = $.trim(self.siblings('.service_val').val());
        }
        if(!service&&!service_count&&!balance) {
            return false;
        }
        var data = {
            serial: memberInfo.serial,
            service: service,
            service_count: service_count,
            balance: balance
        };
        _serviceId = $.layer({
            type: 1,
            title: false,
            area: ['auto', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.5, '#000000'],
            closeBtn: [1, true],
            shift: 'top',
            bgcolor: '#ffffff',
            end: function () {
                _serviceId = undefined;
            },
            page: {
                html: Dao.html("chargeServiceTpl", data)
            }
        });
        $('.chg_ok').on('click', function() {
            var cur_balance = Number($.trim($('.rechg_balance').val())),
                cur_service_count = Number($.trim($('.rechg_service_count').val()));
            if(cur_balance <= 0 || cur_service_count <= 0) {
                return false;
            }
            memberInfo.serviceCount = cur_service_count;
            memberInfo.serviceValue = cur_balance;
            var save = taoqi.loading(service + "服务充值, 请稍后...");
            $.ajax({
                type: 'post',
                dataType: 'json',
                url: BASE_PATH + "/shop/card/member/charge",
                data: {
                    memberInfo: $.toJSON(memberInfo),
                    serviceId: memberInfo.id
                },
                cache: false,
                success: function (data) {
                    taoqi.close(save);
                    if (!data.success) {
                        taoqi.error(data.errorMsg);
                    } else {
                        taoqi.success(service + "服务充值成功!");
                        window.location.href = "/legend/shop/card/member/detail?Id=" + data.data + "&act=edit"
                    }
                },
                error: function (a, b, c) {

                }
            });
        });
        $('.chg_cancel').on("click", function() {
            close(_serviceId);
        });
    }

    //开票
    var _invoiceId;
    Model.getInvoice = function (id, success) {
        var url = BASE_PATH + '/shop/settlement/settlement_order';
        Dao.xhrHtml({
            action: url,
            params: {
                id: id
            },
            tpl: 'invoiceTpl'
        }, success);
    };
    function setInvoice(id, callback) {
        if (_invoiceId != undefined)
            return false;
        Model.getInvoice(id, function (html) {
            _invoiceId = $.layer({
                type: 1,
                title: false,
                area: ['328px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                bgcolor: '#ffffff',
                move: '.rmtit',
                end: function () {
                    _invoiceId = undefined;
                },
                page: {
                    html: html
                }
            });
            $('.save_invoice').click(function () {
                callback && callback();
                close(_invoiceId);
            });

            $('.cancel_invoice').click(function () {
                window.location.reload();
                close(_settleId);
            });
        });
    };

    //结算付款   START
    var _pay;
    Model.getPay = function (id, success) {
        var url = BASE_PATH + '/shop/settlement/out/paid/list';
        Dao.xhrHtml({
            action: url,
            params: {
                warehouseInId: id
            },
            tpl: 'payTpl'
        }, success);
    };
    function setPay(id, callback) {
        if (_pay != undefined)
            return false;
        Model.getPay(id, function (html) {
            _pay = $.layer({
                type: 1,
                title: false,
                area: ['410px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                bgcolor: '#ffffff',
//                move: '.rmtit',
                end: function () {
                    _pay = undefined;
                },
                page: {
                    html: html
//                    html:Dao.html("payTpl", null)
                }
            });
            //结算方式
            var _this = undefined;
            $('.pay_cont').on("focus", '.pay_method', function () {
                var l = $(this).offset().left - $('.xubox_page').offset().left,
                    t = $(this).offset().top + $(this).parent().height() - $('.xubox_page').offset().top;
                _this = this;
                Model.getPayment();  //将结算方式加载到settle1中
                $('#settle1').css({left: l, top: t}).show();
            });
            $('.pay_cont').on("mousedown", '#settle1 a', function () {
                if (_this) {
                    $(_this).val($(this).text());
                    $(_this).data("id", $(this).data("id"));
                    _this = undefined;
                }
            });
            $('.pay_cont').on("blur", '.pay_method', function () {
                $('#settle1').hide().empty();
            });
            //添加结算方式
            $('.pay_cont').on("click", '.addRowBtn', function (e) {
                var li = $(this).parent("li"),
                    c = "jian";
                if (!$(this).hasClass(c)) {
                    var flag = 1;
                    $('.pay').each(function () {
                        var payMethod = $.trim($(this).siblings('.pay_method').val()),
                            pay = $.trim($(this).val());
                        if ((payMethod == "" && pay != "") || (payMethod != "" && pay == "")) {
                            taoqi.error("结算方式或者结算金额填写完整！");
                            flag = 0;
                            return false;
                        }
                    });
                    if (flag) {
                        var newRow = li.clone(true);
                        newRow.find("input").val("");
                        newRow.insertAfter(li);
                        $(this).addClass(c);
                    }
                } else {
                    li.remove();
                }
            });

            var paidVal = parseInt(($('.sum_payable').text() - $('.payable').text()) * 100, 10);
            $('.pay_cont').on("blur", '.pay', function () {
                var payAmount = 0;
                var sumPayable = parseInt($('.sum_payable').text() * 100, 10);
                for (var i = 0; i < $('.pay').length; i++) {
                    var payItem = parseInt($.trim($('.pay').eq(i).val()) * 100, 10);
                    if (isNaN(payItem)) {
                        alert("请输入合法数字");
                        return;
                    }
                    payAmount += payItem;
                }
                if (sumPayable < 0)
                    payAmount = 0 - Math.abs(payAmount);
                $('.payable').text(( sumPayable - payAmount - paidVal ) / 100);
            });

            $('.pay_cont').on("click", ".pay_ok", function () {
                callback && callback();
            });
        });

    };
    //结算付款 END

    //开票信息展示
    var _invoiceIdShow;
    Model.getInvoiceShow = function (id, success) {
        var url = BASE_PATH + '/shop/order_invoice/invoice_get';
        Dao.xhrHtml({
            action: url,
            params: {
                id: id
            },
            tpl: 'invoiceTplShow'
        }, success);
    };
    function getInvoiceInfo(id, callback) {
        if (_invoiceIdShow != undefined)
            return false;
        Model.getInvoiceShow(id, function (html) {
            _invoiceIdShow = $.layer({
                type: 1,
                title: false,
                area: ['328px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                bgcolor: '#ffffff',
                move: '.rmtit',
                end: function () {
                    _invoiceIdShow = undefined;
                },
                page: {
                    html: html
                }
            });
        });
    };

//    快捷菜单设置
    var _menuId;
    Model.getMenu = function (success) {
        var url = BASE_PATH + '/shop/short_cut_menu/get_func_for_choose';
        Dao.xhrHtml({
            action: url,
            tpl: 'setMenuTpl'
        }, success);
    };
    function setMenu(callback) {
        if (_menuId != undefined)
            return false;
        Model.getMenu(function (html) {
            _menuId = $.layer({
                type: 1,
                title: false,
                area: ['328px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                bgcolor: '#ffffff',
                move: '.rmtit',
                end: function () {
                    _menuId = undefined;
                },
                page: {
                    html: html
                }
            });
            $('.me_c').on('click', '.me_tit', function () {
                $(this).toggleClass('current');
                $(this).next().slideToggle();
            });
            $('.baocun').click(function () {
                var arr = [];
                $('.rmcheck:checked').each(function () {
                    arr.push($(this).data('obj'));
                });
                arr.length >= 0 && callback(arr);
                close(_menuId);
            });
        });
    }

//    添加X
    var _xId;

    function addX(tpl, data, callback) {
        if (_xId != undefined)
            return false;
        _xId = $.layer({
            type: 1,
            title: false,
            area: ['331px', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.5, '#000000'],
            closeBtn: [1, true],
            shift: 'top',
            bgcolor: '#ffffff',
            move: '.addtit',
            end: function () {
                _xId = undefined;
            },
            page: {
                html: Dao.html(tpl, data)
            }
        });
        $('.ad_bc').click(function () {
            callback() != false && close(_xId);
        });
        $('.ad_qx').click(function () {
            close(_xId);
        });
    }

//    更多仓库信息
    var _mwhid;

    Model.getMoreWarehouse = function (id, success) {
        var url = BASE_PATH + '/shop/order/getRealOrderGoodsListByOrderId';
        Dao.xhrHtml({
            action: url,
            params: {
                orderId: id
            },
            tpl: 'moreWarehouseTpl'
        }, success);
    };

    function moreWarehouse(id) {
        if (_mwhid != undefined)
            return false;
        Model.getMoreWarehouse(id, function (html) {
            _mwhid = $.layer({
                type: 1,
                title: false,
                area: ['auto', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                fix: false,
                bgcolor: '#ffffff',
                move: '.mwtab th',
                end: function () {
                    _mwhid = undefined;
                },
                page: {
                    html: html
                }
            });
        });
    }


//    添加 浮层 850px宽
    var _yId;

    function addY(tpl, data, callback) {
        if (_yId != undefined)
            return false;
        _yId = $.layer({
            type: 1,
            title: false,
            area: ['850px', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.5, '#000000'],
            closeBtn: [1, true],
            shift: 'top',
            bgcolor: '#ffffff',
            move: '.addtit',
            end: function () {
                _yId = undefined;
            },
            page: {
                html: Dao.html(tpl, data)
            }
        });
        $('.ad_bc').click(function () {
            callback() != false && close(_yId);
        });
        $('.ad_qx').click(function () {
            close(_yId);
        });
    }

//    请选择商品类别
    var _cateGoodsId;
    Model.goodsCategory = function (success) {
        var url = BASE_PATH + '/shop/goods_category/shop_list_all';
        Dao.xhrHtml({
            action: url,
            tpl: 'goodsCategoryTpl'
        }, success);
    };
    function goodsCategory(callback) {
        if (_cateGoodsId != undefined)
            return false;
        Model.goodsCategory(function (html) {
            _cateGoodsId = $.layer({
                type: 1,
                title: false,
                area: ['328px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                bgcolor: '#ffffff',
                move: '.rmtit',
                end: function () {
                    _cateGoodsId = undefined;
                },
                page: {
                    html: html
                }
            });
            $('.me_c').on('click', '.me_tit', function () {
                $(this).toggleClass('current');
                $(this).parent().nextAll('.rm_c').slideToggle();
            });
            $('.me_c').on('click', '.quanx', function () {
                if (this.checked) {
                    $(this).parent().parent().nextAll('.rm_c').find('.rmcheck').attr('checked', 'checked');
                } else {
                    $(this).parent().parent().nextAll('.rm_c').find('.rmcheck').removeAttr('checked');
                }
            });
            $('.baocun').click(function () {
                var arr = [];
                $('.rmcheck:checked').each(function () {
                    if ($(this).data('id') > 0) {
                        arr.push($(this).data('id'));
                    }
                });
                arr.length > 0 && callback(arr);
                close(_cateGoodsId);
            });
        });
    }
    //    技术专题报名弹窗
    var _attendPage;
    Model.getAttend = function (success) {
        //var url = BASE_PATH + '/shop/remind_menu/get_short_remind_menu_choose';
        //Dao.xhrHtml({
        //    action: url,
        //    tpl: 'remindTpl'
        //}, success);
    };
    function setAttend(callback) {
        if (_attendPage != undefined)
            return false;
        //Model.getAttend(function (html) {
            _attendPage = $.layer({
                type: 1,
                title: false,
                area: ['250px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                closeBtn: [1, true],
                shift: 'top',
                bgcolor: '#ffffff',
                move: false,
                end: function () {
                    $(".attend").each(function(){
                        $(".attend").removeClass("choose-detail");
                    });
                    _attendPage = undefined;
                },
                page: {
                    html: Dao.html("attend-popup", null)
                }
            });
            // 递减
            var numberObj = $(".attend-popup .number");
            $('.reduce').on('click', function () {
                var number = numberObj.val();
                if(number < 2){
                    return;
                }
                else{
                    numberObj.val(--number);
                }
            });
            //递增
            $('.add').click(function () {
                var number = numberObj.val();
                if(number > 4){
                    return;
                }
                else{
                    numberObj.val(++number);
                }
            });
        //});
    }

//车型选择
    Model.brandLetter = function (success) {
        var url = BASE_PATH + '/shop/car_category/brand_letter';
        Dao.xhrHtml({
            action: url,
            tpl: 'allModelTpl'
        }, success);
    };

    Model.carHot = function (success) {
        var url = BASE_PATH + '/shop/car_category/hot';
        Dao.xhrHtml({
            action: url,
            tpl: 'carModelTpl'
        }, success);
    };

    Model.carCategory = function (id, success) {
        var url = BASE_PATH + '/shop/car_category';
        var params = {
            pid: id
        };
        Dao.xhrHtml({
            action: url,
            params: params,
            tpl: 'sModelTpl'
        }, success);
    };

    template.helper('$toStr', function (obj) {
        return $.toJSON(obj);
    });

    var inRes = [],
        inCur = -1;

    function inputFixed(v) {
        inRes = [];
        inCur = -1;
        var arr = $('.carBox dl').children();
        var reg = new RegExp(v, 'i');
        arr.each(function (i) {
            var str = $.trim($(this).text());
            reg.test(str) && inRes.push($(this));
        });
        updateNum();
    }

    function updateNum() {
        var n = inCur + 1;
        $('.cm_sp').text('第 ' + n + ' 条，共 ' + inRes.length + ' 条');
    }

    var modelId;

    function modelSelection(callback) {
        if (modelId != undefined)
            return false;
        Model.carHot(function (html) {
            modelId = $.layer({
                type: 1,
                title: false,
                area: ['360px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000000'],
                shadeClose: true,
                bgcolor: '#efefef',
                closeBtn: [1, true], //去掉默认关闭按钮
                shift: 'top',
                end: function () {
                    modelId = undefined;
                },
                move: '.cm_t',
                page: {
                    html: html
                }
            });
            $('.cm_b').on('click', '.cm_tit li', function () {
                var $t = $(this);
                if ($t.hasClass('current'))
                    return;
                var n = $t.index();
                var od = $t.siblings('.current');
                var wp = $('.carBox');
                var val = $.trim($('.cm_ss').val());
                if (n == 1 && $.trim(wp.eq(n).html()) == '') {
                    Model.brandLetter(function (html) {
                        wp.eq(n).html(html);
                        od.removeClass('current');
                        wp.eq(od.index()).hide();
                        $t.addClass('current');
                        wp.eq(n).show();
                        val != '' && inputFixed(val);
                    });
                } else {
                    od.removeClass('current');
                    wp.eq(od.index()).hide();
                    $t.addClass('current');
                    wp.eq(n).show();
                    n == 1 && val != '' && inputFixed(val);
                }
            });
            $('.cm_ss').on(INPUT_EVENT, function () {
                var $cur = $('.cm_tit li:last');
                var val = $.trim(this.value);
                if (val == '')
                    return;
                if ($cur.hasClass('current')) {
                    inputFixed(val);
                } else {
                    $cur.click();
                }
            });
            $('.cm_xia').click(function () {
                var l = inRes.length;
                if (l > 0) {
                    if (++inCur >= l)
                        inCur = 0;
                    var dom = inRes[inCur];
                    var par = dom.parents('.carBox');
                    var ph = dom.offset().top - par.offset().top;
                    par.scrollTop(par.scrollTop() + ph);
                    updateNum();
                }
            });
            $('.cm_ss').on('keypress', function (e) {
                if (e.keyCode == 13) {
                    var l = inRes.length;
                    if (l > 0) {
                        if (++inCur >= l)
                            inCur = 0;
                        var dom = inRes[inCur];
                        var par = dom.parents('.carBox');
                        var ph = dom.offset().top - par.offset().top;
                        par.scrollTop(par.scrollTop() + ph);
                        updateNum();
                    }
                }
            });
            $('.cm_shang').click(function () {
                var l = inRes.length;
                if (l > 0) {
                    if (--inCur < 0)
                        inCur = l - 1;
                    var dom = inRes[inCur];
                    var par = dom.parents('.carBox');
                    var ph = dom.offset().top - par.offset().top;
                    par.scrollTop(par.scrollTop() + ph);
                    updateNum();
                }
            });
            $('.cm_b').on('click', '.cart1', function () {
                var $t = $(this);
                if ($t.hasClass('current')) {
                    $t.removeClass('current');
                    $t.next().slideUp();
                } else {
                    var cur = $t.parents('.carBox').find('.current');
                    if (cur.length != 0) {
                        cur.removeClass('current');
                        cur.next().hide();
                    }
                    var dom = $t.parent();
                    var par = dom.parents('.carBox');
                    if ($t.next().length != 0) {
                        $t.next().slideDown(function () {
                            var ph = dom.offset().top - par.offset().top;
                            par.scrollTop(par.scrollTop() + ph);
                        });
                        $t.addClass('current');
                    } else {
                        Model.carCategory($t.data('id'), function (html) {
                            $t.after(html);
                            $t.addClass('current');
                            $t.next().slideDown(function () {
                                var ph = dom.offset().top - par.offset().top;
                                par.scrollTop(par.scrollTop() + ph);
                            });
                        });
                    }
                }
            });
            $('.cm_b').on('click', '.cart2', function () {
                var obj = $(this).data('obj');
                obj.pid = $(this).parent().prev().data('id');
                callback(obj);
                close(modelId);
            });
        });
    }

    var partsTypeId;
    // 数据组装
    template.helper("group", function( data ) {

        var arr = [],
            len = data.length;

        // 将每四条数据并为一条
        for(var i = 0; i < len;) {
            var _data = {};
            _data.ids = "";
            _data.catNameMappings = "";
            // 将数据合并，
            // id用","隔开；
            // catNameMapping用"　"隔开；
            for(var j = 0; i < len && j < 4; j++) {
                _data.ids += data[i].id + ",";
                _data.catNameMappings += data[i].catNameMapping + "　";
                i++;
            }
            // 去掉数据多余标识，并存入数组中
            _data.ids = _data.ids.replace(/,$/, "");
            _data.catNameMappings = _data.catNameMappings.trim();
            arr.push( _data );
        }
        return arr;
    });
    // 获得一级类目
    Model.partsType = function(success) {

        var url = BASE_PATH + "/shop/goods_category/std/top_list";
        Dao.getData(url, success);
    };
    // 获得对应一级类目下的内容
    Model.partsTypeContent = function(ids, success) {

        var url = BASE_PATH + "/shop/goods_category/std/get_by_pid",
            queue = [];
        if( ids !== undefined ) {
            for (var i = 0; i < ids.length; i++) {
                var data = {
                    action: url,
                    params: {
                        pid: ids[i]
                    }
                };
                queue.push( data );
            }
            Dao.queueRender({
                queue: queue,
                tag: "navContentInner",
                tpl: "partsTypeContentTpl",
                fill: "~"
            }, success);
        }
    };
    // 配件分类弹窗
    function partsType (callback) {

        if(partsTypeId !== undefined) {
            return;
        }
        Model.partsType(function( result ) {
            partsTypeId = $.layer({
                type: 1,
                title: false,
                area: ['auto', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000'],
                shadeClose: true,
                bgcolor: '#f5f5f5',
                closeBtn: [1, true], //去掉默认关闭按钮
                shift: 'top',
                end: function () {
                    partsTypeId = undefined;
                },
                page: {
                    html: template("partsTypeTpl", result)
                }
            });

            $(document)
                .on('click', '.nav_side li', function() {   // 侧边栏的一级类目

                    var $this = $(this),
                        ids = $this.data("ids"),
                        map = $this.data("map");
                    $this.siblings(".cur").removeClass("cur")
                        .end().addClass("cur");
                    ids = ids && ids.split(",");
                    map = map && map.split("　");
                    // 获取二级类目
                    Model.partsTypeContent(ids, function( result) {

                        // 设置二级类目标题名称
                        for(var i = 0; i < result.length; i++) {
                            $(".category_name").eq(i).text(map[i]);
                        }
                    });
                })
                .on('click', '.item', function() {  // 内容的二级类目
                    // TODO 二级类目操作
                    var $this = $(this),
                        data = {
                            id: $this.data("id"),
                            catName: $this.data("name")
                        };
                    callback && callback(data);
                    layer.close(partsTypeId);
                })
                .on('click', '.more_btn', function() {  // 展现更多或者收起

                    var $this = $(this),
                        _c = "packup";
                    if( $this.hasClass(_c) ) {
                        $this.removeClass(_c)
                            .parent().siblings(".item_box").removeAttr("style");

                    } else {
                        $this.addClass(_c)
                            .parent().siblings(".item_box").css("height", "auto");
                    }
                })
                .on('click', '.search_btn', function() {    // 模糊搜索指定三级类目
                    // TODO 搜索
                });

            $(".nav_side li").eq(0).trigger("click");
        });
    }

    // 在线引流－办单店活动 活动预览弹窗
    var actPreviewId;
    Model.activityPreview = function(id, success) {
        var url = BASE_PATH + "/shop/cz_app/activity/preview/ng",
            params = {
                id: id
            };
        Dao.getData(url, params, success);
    };

    function activityPreview(id) {

        if(id === undefined) {
            taoqi.error("请先保存活动信息！");
            return;
        }
        Model.activityPreview(id, function(result) {

            if (actPreviewId != undefined || !result.success) {
                return;
            }

            actPreviewId = $.layer({
                type: 1,
                title: false,
                area: ['auto', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000'],
                shadeClose: true,
                bgcolor: '#fff',
                closeBtn: [1, true], //去掉默认关闭按钮
                shift: 'top',
                end: function () {
                    actPreviewId = undefined;
                },
                page: {
                    html: Dao.html("activityPreviewTpl", result)
                }
            });
        });
    }

    var activivtyNoticeId;
    Model.activityNotice = function(success) {
        var url = BASE_PATH + "/shop/cz_app/notice/preview/ng";
        Dao.getData(url, null, success);
    };

    function activityNotice() {

        Model.activityNotice(function(result) {

            if(activivtyNoticeId != undefined || !result.success) {
                return;
            }

            activivtyNoticeId = $.layer({
                type: 1,
                title: false,
                area: ['auto', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000'],
                shadeClose: true,
                bgcolor: '#fff',
                closeBtn: [1, true], //去掉默认关闭按钮
                shift: 'top',
                end: function () {
                    activivtyNoticeId = undefined;
                },
                page: {
                    html: Dao.html("activityNoticeTpl", result)
                }
            });
        });
    }

    Model.getGoodsListByIds = function(goodsIds, success) {
        var url = BASE_PATH + "/shop/goods/getGoodsListByIds";
        params = {
            goodsIds: goodsIds
        };
        Dao.getData(url, params, success);
    };

    function mergePart(goodsIds){
        Model.getGoodsListByIds(goodsIds, function(result) {
            if( !result.success) {
                return;
            }
            $.layer({
                type: 1,
                title: false,
                area: ['800px', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000'],
                //shadeClose: true,
                bgcolor: '#fff',
                closeBtn: [1, true], //去掉默认关闭按钮
                shift: 'top',
                end: function () {
                    goodsIds = undefined;
                },
                page: {
                    html: Dao.html("merge_part_tpl", result)
                }
            });
            $("#oldGoodsIds").val(goodsIds);
        });

    }

    function success(msg, second,callback,isShade) {
        if (second == null) {
            second = 5;
        }
        var param = {
            type: 1,
            shade: false,
            rate: 'left'
        }
        if(isShade === true){
            delete param.shade;
        }
        layer.msg(msg, second, param);
        if(callback){
            setTimeout(function(){
                callback && callback();
            },second*1000);
        }
    }

    function failure(msg, second) {
        if (second == null) {
            second = 5;
        }
        layer.msg(msg, second, {
            type: 0,
            shade: false,
            rate: 'left'
        });
    }

    function ask(msg, yes, no) {
        $.layer({
            area: ['300px', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.5, '#000000'],
            closeBtn: [0, false], //去掉默认关闭按钮
            shift: 'top',
            dialog: {
                msg: msg,
                btns: 2,
                type: 4,
                btn: ['确定', '取消'],
                yes: function (index) {
                    layer.close(index);
                    yes();
                },
                no: function () {
                    no && no();
                }
            }
        });
    }

    function error(msg, second) {
        if (second == null) {
            second = 5;
        }
        layer.msg(msg, second, {
            type: 5,
            shade: false,
            rate: 'left'
        });
    }

    function close(id) {
        if (id == undefined) {
            layer.close(layer.index);
        } else {
            layer.close(id);
        }
    }

    function info(msg, fn) {
        if (msg != undefined) {
            layer.alert(msg, 0, fn);
        }
    }

    function loading(msg, second) {
        if (second != null) {
            second = 15;
        }
        return layer.load(msg || '正在加载，请稍后...', second);
    }

    return {
        addUser: addUser,
        addStaff: addStaff,
        addX: addX,
        addY: addY,
        moreWarehouse: moreWarehouse,
        setRemind: setRemind,
        goSettle: goSettle,
//        goSettleForSign: goSettleForSign,
        setMenu: setMenu,
        goodsCategory: goodsCategory,
        success: success,
        error: error,
        close: close,
        ask: ask,
        info: info,
        loading: loading,
        modelSelection: modelSelection,
        failure: failure,
        setInvoice: setInvoice,
        getInvoiceInfo: getInvoiceInfo,
        goPreSettlePayment: goPreSettlePayment,
        setPay: setPay,
        setLoginIdentify: setLoginIdentify,
        addMemberAmount: addMemberAmount,
        addServiceCount: addServiceCount,
        setAttend: setAttend,
        activityPreview: activityPreview,
        activityNotice: activityNotice,
        mergePart : mergePart
    };

})();

$(function () {
    /*
     调用方式=============================渠项栋  注意
     taoqi.goSettle(111, function () {
     alert(777)
     });
     */
    /*  $("#reSetRemind").click(function(){
     taoqi.setRemind(function (arr) {
     setRemindMenu(arr);
     });
     });

     $("#setRemind").click(function(){
     taoqi.setRemind(function (arr) {
     setRemindMenu(arr);
     });
     });*/

    $("#fchange").click(function () {
        taoqi.setMenu(function (arr) {
            setShortCutMenu(arr);
        });
    });

    $("#noquick").click(function () {
        taoqi.setMenu(function (arr) {
            setShortCutMenu(arr);
        });
    });

});


/**
 * 快捷菜单设置
 * */
function setShortCutMenu(arr) {
    var funcId = [];

    if (arr.length > 0) {
        for (var item in arr) {
            //console.log(arr[item].id);
            funcId.push(arr[item].id);
        }
    } else {
        funcId.push(0);
    }

    funcId = funcId.join(",");
    //console.log(funcId);
    var save = taoqi.loading("保存信息中");
    $.ajax({
        type: 'POST',
        dataType: 'json',
        data: {
            funcId: funcId
        },
        url: BASE_PATH + '/shop/short_cut_menu/add_shortcut_menu',
        success: function (data) {
            layer.close(save);
            if (data.success != true) {
                taoqi.error(data.errorMsg);
                window.location.reload();
            } else {
                taoqi.success("操作成功");
                window.location.reload();
            }
        },
        error: function (a, b, c) {
            //console.log(a, b, c);
        }
    });
}


/**
 * 提醒菜单设置
 * */
function setRemindMenu(arr) {

    var funcId = [];
    if (arr.length > 9) {
        taoqi.failure("最多添加9个");
        return;
    } else if (arr.length > 0) {
        for (var item in arr) {
            //console.log(arr[item].id);
            funcId.push(arr[item].id);
        }
    } else {
        funcId.push(0);
    }
    funcId = funcId.join(",");

    var save = taoqi.loading("保存信息中");
    $.ajax({
        type: 'POST',
        dataType: 'json',
        data: {
            funcId: funcId
        },
        url: BASE_PATH + '/shop/remind_menu/add_remind_menu',
        success: function (data) {
            layer.close(save);
            if (data.success != true) {
                taoqi.error(data.errorMsg);
                window.location.reload();
            } else {
                taoqi.success("操作成功");
                window.location.reload();
            }
        },
        error: function (a, b, c) {
            //console.log(a, b, c);
        }
    });
}
$(document).ready(function () {

    $("#com_license").live('blur', function () {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            data: {
                license: $('#com_license').val()
            },
            url: BASE_PATH + '/shop/customer/checkLicense',
            success: function (data) {
                if (data.success != true) {
                    taoqi.error(data.errorMsg);
                }
            },
            error: function (a, b, c) {
                //console.log(a, b, c);
            }
        });
    });

    $("#commonServiceName1").live('blur', function () {
        $.ajax({
            type: 'GET',
            dataType: 'json',
            data: {
                serviceName: $(this).val()
            },
            url: BASE_PATH + '/shop/shop_service_info/checkServiceName',
            success: function (result) {
                if (result.success != true) {
                    taoqi.error(result.message);
                    return false;
                } else {
                    if (result.data == false) {
                        taoqi.error("服务名称已存在");
                        return false;
                    }
                }
            },
            error: function (a, b, c) {
                //console.log(a, b, c);
            }
        });
    });

    $(document).on("blur", "#commonGoodsFormat", function(){
        $.ajax({
            type: 'GET',
            dataType: 'json',
            data: {
                goodsFormat: $(this).val()
            },
            url: BASE_PATH + '/shop/goods/formatisexist',
            success: function (result) {
                if (result.success != true) {
                    taoqi.error(result.errorMsg);
                    return false;
                }else{
                    if(result.data ==false){
                        taoqi.error("零件号已存在");
                        return false;
                    }
                }
            },
            error: function (a, b, c) {
                //console.log(a, b, c);
            }
        });
    });

});



