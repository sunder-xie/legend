var _stopPropagation = true;
$(function () {
    seajs.use(['select', 'ajax', 'dialog'] , function (select, ajax, dialog) {
        var url = {
            service: BASE_PATH + '/shop/shop_service_cate/get_by_name'
        };

        dialog.titleInit();
        ['service'].forEach(function (e) {
            select.init({
                url: url[e],
                dom: '.js-' + e + '-type',
                showValue: 'name',
                showKey: 'id',
                canInput: true,
                cache: true,
                callback: function (key) {
                    var now = $(this).parents('tr');

                    $('.service .service-datatr').each(function () {
                        var id = $(this).find('input[name=catId]').val();

                        if(now[0] != this && id == key) {
                            _stopPropagation = false;

                            dialog.warn('已添加该服务项目，请不要重复添加');

                            setTimeout(function () {
                                now.find('.service-item input').val('');
                                _stopPropagation = true;
                            }, 400);
                            return false;
                        }
                    });
                }
            })
        });

    });
    var infoId  = $("#memberCardId").val();

    if(infoId){
        var useWith = $("input[name='useWithCoupon']").val();
        if(useWith==1){
            $("#compatibleWithCoupon").attr("checked", true);
        }
        $('.order-panel-foot:eq(0)').addClass("hidden");
        $('.order-panel-foot:eq(1)').removeClass("hidden");

    }

    $("input[name=orderDiscount]").attr("data-v-type", "required | number");

    $('.yqx-table').on('blur.check', 'input[data-v-type]', function () {
        return _stopPropagation;
    });

    // 选择折扣类型
    $('.info-region .card-discount').click(function(e){
        var selected = $('.info-region .selected');
        var flag = $(this).data('flag'),
            isPart = !!$(this).find('.js-part input:checked').length;

        if($(this).hasClass("service") ||
            $(this).hasClass("goods")) {

                selected.each(function () {
                if(!$(this).hasClass("service") &&
                    !$(this).hasClass("goods")) {

                    $(this).removeClass('selected')
                        .find('.yqx-input').prop('disabled', true).val('');
                }
            });
        } else {
            // 清除数据
            selected.removeClass('selected')
                .find('.yqx-input').prop('disabled', true).val('')
                .end().next('.table-box').eq(0).addClass('hide')
                .find('tbody').remove();
        }

        if($(this).hasClass("selected") && selected.length > 1){
            $(this).removeClass('selected')
                .find('.yqx-input').prop('disabled', true).val('')
                .end().next('.table-box').eq(0).addClass('hide')
                .find('tbody').remove();
            $(this).find('.js-part input:checked').prop("checked" , false);
            $(this).find('.js-all input').prop("checked" , true);
        }else{
            $(this).addClass('selected');
        }

        // 显示
        if($(this).hasClass('selected')) {
            if(isPart) {
                $(this).next('.table-box.hide').eq(0).removeClass('hide');
                $(this).find('.yqx-input').prop('disabled', true);
            }else{
                $(this).find('.service-all-discount').prop("disabled",false);
            }

            if($(this).find('.radio-discount:checked').val() == 1) {
                $(this).find('.yqx-input').prop('disabled', false);
            }
        }

    });

    $('.js-submit').click(function(){
        seajs.use(['formData', 'dialog'],function(fd, dg){
            var initBalance = $("input[name=initBalance]").val();
            var salePrice = $("input[name=salePrice]").val();
            if(initBalance != salePrice){
                dg.confirm("会员卡<span class='font-red'>售价</span>与会员卡<span class='font-red'>余额</span>不一致,是否确认提交?", submit);
            } else {
                submit();
            }

        })
    });

    $('.card-discount .yqx-input').click(function (e) {
        var box = $(this).parents('.card-discount');

        if(box.hasClass('selected')) {
            e.stopPropagation();
        }
    });

    $('input[name="goodsDiscountType"], input[name="serviceDiscountType"]').on('click', function (e) {
        var box = $(this).parents('.card-discount');
        if(this.checked) {
            var isPart = this.value == 2;

            box.next('.table-box').eq(0)
                .toggleClass('hide', !isPart);

            box.find('.form-item').find('.yqx-input')
                .prop('disabled', isPart)
                .val('');
        }

        if(box.hasClass('selected')) {
            e.stopPropagation()
        }
    });

    $(document).on('click', '.js-del', function () {
        $(this).parents('tr').remove();
    });

    //返回按钮
    $(document).on('click','.js-goback',function(){
        util.goBack();
    });
    seajs.use('art', function (art) {
        $('.js-add-service').click(function () {
            var html = art("serviceTpl", {});

            $('.service-table .yqx-table').append($(html));
        });

        $('.js-add-goods').click(function () {
            var html = art("goodsTpl", {});

            $('.goods-table .yqx-table').append($(html));
        });
    });

    seajs.use('check',function(ck){
      ck.init();
    })

    function submit() {
        seajs.use(['formData', 'dialog', 'check'],function(fd, dg, ck) {
            var data = $.extend(fd.get('.info-base'), fd.get('.info-expire'), fd.get('.info-set')),
                t, selected = $('.single-tag .selected'), hasList = false;
            var selectedType = {
                service: $('.card-discount.service.selected'),
                goods: $('.card-discount.goods.selected')
            };

            data.compatibleWithCoupon = $('#compatibleWithCoupon').prop('checked');
            data.discountType = $('.info-region .card-discount.selected').data('flag');

            if($('#service').hasClass('selected') && $('#goods').hasClass('selected')){
                data.discountType = 4;
            }
            data.generalUse = true;

            seajs.use(['check'], function (ck) {
                ck.init();
                if (!ck.check()) {//作用域为整个页面的验证
                    return false;
                } else {
                    $(".service-all-discount").each(function () {
                        var $t = $(this);
                        if (!$t.attr("disabled")) {
                            if ($t.val() > 10 || $t.val() < 0) {
                                dg.msg('请输入0~10之间的折扣');
                                return false;
                            }
                        }
                    });
                    var memberCardId = $("#memberCardId").val();
                    var url = BASE_PATH;

                    if(data.discountType == 1) {
                        data.orderDiscount = $('input[name=orderDiscount]').val();
                    }
                    var isTrue = true;
                    if(data.discountType == 2 || data.discountType == 3 || data.discountType == 4) {
                         selected.each(function () {
                            var $e  = $(this);
                            t = $e.find('.radio-discount:checked').val();
                            if (t == 2) {
                                if (selectedType.service && $e.hasClass('service')) {
                                    hasList = getServiceDiscountList(data, ck);

                                    if (!hasList) {
                                        dg.warn('请至少添加一个服务类型');
                                        isTrue = false;
                                        return false;
                                    }
                                    delete data.serviceDiscount;
                                    data.serviceDiscountType = t;
                                }
                                if (selectedType.goods && $e.hasClass('goods')) {
                                    hasList = getGoodsDiscountList(data, ck, fd);

                                    if (!hasList) {
                                        dg.warn('请至少添加一个配件类型');
                                        isTrue = false;
                                        return false;
                                    }

                                    data.goodsDiscountType = t;
                                    delete data.goodsDiscount;
                                }
                            } else {
                                if (selectedType.service && $e.hasClass('service')) {
                                    data.serviceDiscount = $('input[name=serviceDiscount]').val();
                                    data.serviceDiscountType = t;
                                }
                                if (selectedType.goods && $e.hasClass('goods')) {
                                    data.goodsDiscount = $('input[name=goodsDiscount]').val();
                                    data.goodsDiscountType = t;
                                }
                            }
                        });
                    }
                    if(isTrue){
                        if (memberCardId && memberCardId != "") {
                            data.id = memberCardId;
                            url += "/account/member/edit";
                        } else {
                            url += '/account/member/create';
                        }
                        $.ajax({
                            url: url,
                            dataType: 'json',
                            contentType: 'application/json',
                            data: JSON.stringify(data),
                            type: "POST",
                            success: function (result) {
                                if (result.success) {
                                    dg.success('提交成功');
                                    window.location = BASE_PATH + "/account/setting?flag=3";
                                } else {
                                    dg.fail(result.message);
                                }
                            }
                        });
                    }

                }
            });
        }); // seajs

    }
    function getServiceDiscountList(target, ck) {
        var count = 0;
        target.serviceRels = {};

        if( !ck.check('.service-table') ) {
            return false;
        }
        $('.service-datatr').each(function () {
            var id = $(this).find('input[name=catId]').val();
            var discount = $(this).find('input[name=discount]').val();

            if(id !== '') {
                target.serviceRels[id] = discount;
                count++;
            }
        });

        return !!count;
    }
    function getGoodsDiscountList(target, ck) {
        var count = 0;
        target.goodsCats = [];

        if( !ck.check('.goods-table') ) {
            return false;
        }
        $('.goods-datatr').each(function () {
            var id =  $(this).find('input[name=stdCatId]').val() || $(this).find('input[name=catId]').val();

            if(id != '') {
                target.goodsCats.push(
                    {
                        catName: $(this).find('input[name=catName]').val(),
                        discount: $(this).find('input[name=discount]').val(),
                        catId: id,
                        customCat: $(this).find('input[name=customCat]').val() == 'true' ? true : false
                    }
                );
                count++;
            }
        });

        return !!count;
    }
});

function goodsTypeSelectCallback (id) {
    var that = this;
    seajs.use('dialog', function (dialog) {

        $('.goods-datatr').each(function () {
            var t = $(this).find('input[name=catId]').val() || $(this).find('input[name=stdCatId]').val();
            var e = $(this).find('.js-goods-type');

            if (t == id && that != e[0]) {
                _stopPropagation = true;
                dialog.warn('已添加该配件，请不要重复添加');

                setTimeout(function () {
                    $(that).parents('tr')
                        .find('.goods-item input').val('');

                    _stopPropagation = true;
                }, 400);
                return false;
            }
        });
    });


}