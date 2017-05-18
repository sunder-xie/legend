/**
 * zmx 2016-04-08
 * 新建预检单页面
 */

$(function () {
    var $document = $(document);

    seajs.use([
        "group",
        "art",
        "downlist",
        'formData',
        "select",
        'dialog',
        "date",
        "ajax",
        "check"
    ], function (gp, at, dl, fd, st, dg, date, ax, ck) {
        ck.init();
        gp.init();

        $document.on('input', '.js-number', function () {
            var val = $(this).val();

            $(this).val(checkFloat(val, 0));
        });

        $document.on('input', '.js-float-1', function () {
            var val = $(this).val();

            $(this).val(checkFloat(val, 1));
        });

        $document.on('input', '.js-float-2', function () {
            var val = $(this).val();

            $(this).val(checkFloat(val, 2));
        });

        //客户需求添加行
        $document.on('click', ".js-addbtn", function () {
            var html,
                fillTarget,
                $this;
            $this = $(this);
            html = at("customerTpl", {});
            fillTarget = $('#customerCon', $this.parents('table'));
            fillTarget.append(html);
        });

        //客户需求删除行
        $document.on('click', '.js-delbtn', function () {
            $(this).parents('tr').remove();
        });

        //初始化车牌下拉框
        dl.init({
            url: BASE_PATH + '/shop/customer/search/mobile',
            searchKey: 'com_license',
            tplCols: {'license': '车牌'},
            tplId: 'carLicenceTpl',
            showKey: 'license',
            dom: 'input[name="plateNumber"]',
            hasInput: false,
            callbackFn: function (obj, item) {
                $('input[name="customerName"]').val(item["customerName"]);
                $('input[name="mobile"]').val(item["mobile"]);
                $('input[name="carModel"]').val(item["carInfo"]);
                $('input[name="carModelId"]').val(item["carModelId"]);
                $('input[name="customerCarId"]').val(item["id"]);
                $('input[name="mileage"]').val(item["mileage"]);
                $('input[name="dueDate"]').val(item["insuranceTimeYMDStr"]);
                $('input[name="nextTime"]').val(item["keepupTimeYMDStr"]);
                getUpkeepMileage();
            }

        });

        $document.on('blur','input[name="plateNumber"]',function(){
            var nolicense = $('input[name="plateNumber"]').val();
            if(nolicense == ''){
                $('input[name="customerName"]').val('');
                $('input[name="mobile"]').val('');
                $('input[name="carModel"]').val('');
                $('input[name="carModelId"]').val('');
                $('input[name="customerCarId"]').val('');
                $('input[name="mileage"]').val('');
                $('input[name="upkeepMileage"]').val('');
            }
        })

        //日历绑定
        date.datePicker('.js-date', {
            minDate: '%y-%M-%d',
            doubleCalendar: true
        });
        //保险到期日历绑定
        date.datePicker('.js-due-date', {
            doubleCalendar: true
        });


        //计算下次保养里程
        var getUpkeepMileage = function () {
            var carId = $("input[name='customerCarId']").val();
            var carModelId = $("input[name='carModelId']").val();
            var mileage = $("input[name='mileage']").val();

            if (!$.isNumeric(carId) || !$.isNumeric(mileage) || mileage < 0) {
                return;
            }

            $.ajax({
                type: 'get',
                url: BASE_PATH + "/shop/order/get_upkeep_mileage",
                data: {
                    carId: carId,
                    carModelId: carModelId,
                    mileage: mileage
                },
                success: function (json) {
                    if (json != null && json != undefined && json.success) {
                        oldUpkeepMileage = json.data;
                        $("input[name='upkeepMileage']").val(json.data);
                    } else {
                        $("input[name='upkeepMileage']").val(null);
                    }
                }
            });

        };
        $("input[name='mileage']").change(getUpkeepMileage);

        // 下次保养里程校验
        $document.on("blur", "input[name='upkeepMileage']", function () {
            var mileage = $("input[name='mileage']").val();
            var upkeepMileage = $("input[name='upkeepMileage']").val();
            if (!$.isNumeric(mileage) || mileage < 0 || !$.isNumeric(upkeepMileage) || upkeepMileage < 0 || parseInt(mileage) < parseInt(upkeepMileage)) {
                return;
            }

            dg.warn("下次保养里程不能小于或等于行驶里程");
            $("input[name='upkeepMileage']").val(oldUpkeepMileage);

        });

        function saveInfo(refer) {
            var carId = $('#customerCarId').val();
            var details = [];
            if (!ck.check()) {//作用域为整个页面的验证
                return;
            }
            ;
            $('#customerRequest').find('tr').each(function () {
                var content = $.trim($(this).find('.js-content').val());
                var contentGoods = $.trim($(this).find('.js-contentGoods').val());
                if (content != "") {
                    details.push({
                        content: content,
                        contentGoods: contentGoods
                    });
                }
            });
            var act = $("#act").val();
            var path = BASE_PATH + "/shop/precheck/precheck-" + act;
            var precheckHead = JSON.stringify(fd.get("#precheckHead")),
                appearance = JSON.stringify(getCheckCarFormdata()),
                customerRequest = JSON.stringify(details),
                precheckOther = JSON.stringify(fd.get("#precheckOther")),
                precheckOtherDetail = JSON.stringify(fd.get("#precheckOtherDetail"));
            var goodsInCars = {};

            $('input[type="checkbox"]').each(function() {
                if($(this).is(":checked")){
                    goodsInCars[$(this).attr('name')] = $(this).val();
                }
            });
            var goodsInCar = JSON.stringify(goodsInCars);
            $.ajax({
                type: 'post',
                url: path,
                data: {
                    precheckHead: precheckHead,
                    appearance: appearance,
                    customerRequest: customerRequest,
                    precheckOther: precheckOther,
                    precheckOtherDetail: precheckOtherDetail,
                    goodsInCar: goodsInCar
                },
                success: function (result) {

                    if (!result.success) {
                        dg.fail("保存失败");
                    } else if (refer == "save") {
                        dg.success("保存成功", function () {
                            window.location.href = BASE_PATH + "/shop/precheck/precheck-detail?id=" + result.data;
                        });
                    } else if (refer == "addSpeedily") {
                        dg.success("保存成功", function () {
                            window.location.href = BASE_PATH + "/shop/order/speedily?customerCarId=" + carId + "&refer=precheck";
                        });
                    } else if (refer == "addOrder") {
                        dg.success("保存成功", function () {
                            var id = result.data;
                            window.location.href = BASE_PATH + "/shop/order/common-add?customerCarId=" + carId + "&refer=precheck&precheckId=" + id;
                        });
                    }

                }
            });
        }

        //保存按钮
        $document.on('click', '.js-save-btn', function () {
            saveInfo("save");
        });
        //保存按钮
        $document.on('click', '.js-add-addSpeedily', function () {
            saveInfo("addSpeedily");
        });
        //保存按钮
        $document.on('click', '.js-add-addOrder', function () {
            saveInfo("addOrder");
        });
        $document.on('click', '.js-back', function () {
            util.goBack();
        });

    });


    $('.yqx-group').on('click', '.js-head-show', function () {
        var $this = $(this);

        $('.register').find('.yqx-group-content').each(function () {
            if ($(this).css('display') === 'block') {
                $(this).hide();
            }
        });

        $this.next().toggle();

        if ($this.next().is(":hidden")) {
            $this.find('.group-arrow').removeClass('arrow-up').addClass('arrow-down');
        } else {
            $this.find('.group-arrow').removeClass('arrow-down').addClass('arrow-up');
        }
        return false;
    });

})