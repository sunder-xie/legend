define(function (require, exports, module) {
    var dg = require('dialog'),
        at = require('art'),
        dp = require('date'),
        ck = require('check');
    var _args;

    var returnRecordTpl = '\
    <div class="yqx-dialog return-record-dialog">\
    <div class="dialog-title">电话回访记录 </div>\
    <div class="yqx-dialog-body">\
    <div class="person-name">\
    <input type="hidden" value="" class="js-account-id"/>\
    <span class="name js-name"></span>\
    <span class="mobile js-mobile"></span>\
    <span class="js-license"></span>\
    <span class="js-model"></span>\
    </div>\
    <div class="record-box clearfix">\
    <div class="left-record fl">\
    <div class="title">消费记录</div>\
    <div id="consumeRecordCon">\
\
    </div>\
    </div>\
    <div class="right-record fl">\
    <div class="title">回访记录</div>\
    <div class="record-content">\
    <div class="form-item">\
    <textarea class="yqx-textarea js-return-text" name="" id="" cols="100" rows="3"\
data-v-type="required"></textarea>\
    </div>\
    <div class="give-ticket">\
    赠送优惠券：\
<img src="' + BASE_PATH +'/static/img/page/marketing/gather/ticket-ico.png"/>\
    <span class="dialog-coupon-name js-dialog-coupon"></span>\
    <input type="hidden" value="" class="js-dialog-couponId"/>\
    </div>\
    <div class="grid">\
    <div class="form-item">\
    <input type="text" name=""\
class="yqx-input yqx-input-icon yqx-input-small datepicker js-visitTimeFormat"\
value="" placeholder="下次回访时间">\
    <span class="fa icon-calendar icon-small"></span>\
    </div>\
    </div>\
    <div class="grid">\
    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-dialog-save">保存</button>\
    </div>\
    <div id="feedbackListCon">\
    \
    </div>\
    </div>\
    </div>\
    </div>\
    </div>\
    </div>';
    var consumeRecordTpl = '\
        <%if(json.success && json.data.length){%>\
<ul class="consume-record">\
        <%for(var i=0; i< json.data.length; i++){%>\
    <%var item = json.data[i]%>\
            <li class="js-consume-list">\
            <div class="clearfix">\
            <div class="record-num fl"><%=item.orderSn%></div>\
            <div class="record-time fr"><%=item.orderTime%></div>\
            </div>\
            <div class="service-pro">\
            <div class="service-title">服务项目：</div>\
        <% for (var j = 0; j < item.orderServicesList.length; j++) { %>\
        <% var subItem = item.orderServicesList[j] %>\
            <p><%=subItem.serviceName%></p>\
                <%}%>\
    </div>\
        </li>\
        <%}%>\
</ul>\
    <%}%>';

    var feedbackListTpl = '\
    <%if(json.success && json.data.length){%>\
<ul class="mobile-record">\
        <%for(var i=0; i< json.data.length; i++){%>\
    <%var item = json.data[i]%>\
            <li>\
            <div class="mobile-record-name"><%=item.customerFeedback%></div>\
            <div class="clearfix">\
            <span class="mobile-record-time fl"><%=item.visitTimeFormat%>   <%=item.visitMethod%></span>\
            <span class="fr"><%=item.visitorName%></span>\
            </div>\
            </li>\
            <%}%>\
</ul>\
<%}%>';

    var thisTr = null;
    var _data = null;


    $(document).on('click', '.return-record-dialog .js-dialog-save', function () {
        if (!ck.check()) {
            return;
        }
        var data = {
            customerCarId: _data.customerCarId,
            accountId: $('.js-account-id').val(),
            noteInfoId: _data.noteInfoId,
            couponInfoId: $('.js-dialog-couponId').val(),
            content: $('.js-return-text').val(),
            nextVisitTime: $('.js-visitTimeFormat').val()
        };

        $.ajax({
            type: 'post',
            url: BASE_PATH + '/marketing/gather/plan/feedback/phone',
            data: data,
            success: function (result) {
                if (result.success) {
                    dg.close();
                    if(_args.callback && typeof _args.callback == 'function') {
                        _args.callback();
                    }
                } else {
                    dg.fail(result.message)
                }
            }
        })
    });
    $(document).on('click', '.return-record-dialog .js-consume-list', function () {
        $('.js-consume-list').each(function () {
            $(this).find('.service-pro').hide();
        });
        $(this).find('.service-pro').show();
    });

    function showDialog(customerCarId, customerId, name, mobile, license, carModel, couponName, couponId, noteInfoId) {
        var render = at.compile(returnRecordTpl);

        _data = {
            customerCarId: customerCarId,
            customerId: customerId,
            noteInfoId: noteInfoId
        };
        dg.open({
            area: ['660px', '474px'],
            content: render()
        });
        $('.js-name').text(name);
        $('.js-mobile').text(mobile);
        $('.js-license').text(license);
        $('.js-type').text('');
        $('.js-dialog-coupon').text(couponName);
        $('.js-dialog-couponId').val(couponId);
        $('.js-model').text(carModel);

        if(couponName == '' || couponName == null){
            $('.give-ticket').hide()
        }
        //消费记录
        $.ajax({
            url: BASE_PATH + '/marketing/ng/maintain/consumeRecord',
            data: {
                customerCarId: customerCarId
            },
            success: function (result) {
                if (result.success) {
                    var render = at.compile(consumeRecordTpl);

                    $('#consumeRecordCon').html(render({
                        json: result
                    }))
                } else {
                    dg.fail(result.message)
                }
            }
        });

        //历史记录
        $.ajax({
            url: BASE_PATH + '/marketing/ng/maintain/feedbackList',
            data: {
                customerCarId: customerCarId
            },
            success: function (result) {
                if (result.success) {
                    var render = at.compile(feedbackListTpl);

                    $('#feedbackListCon').html(render({
                        json: result
                    }))
                } else {
                    dg.fail(result.message)
                }
            }
        });

        $.ajax({
            url: BASE_PATH + '/marketing/gather/plan/account',
            data: {
                customerId: customerId
            },
            success: function (result) {
                if (result.success) {
                    $('.js-account-id').val(result.data.id)
                } else {
                    $('.give-ticket').hide()
                }
            }
        });
    }

    function revisitDialog (opt) {
        var args = _args = $.extend({
            dom: ''
        }, opt);

        ck.init();
        dp.datePicker('.return-record-dialog .js-visitTimeFormat');

        $(document).on('click', args.dom, function () {
            thisTr = $(this);

            var customerCarId = $(this).parents('tr').find('input[name="customerCarId"]').val();
            var customerId = $(this).parents('tr').find('input[name="customerId"]').val();
            var name = $(this).parents('tr').find('.car-name').text();
            var mobile = $(this).parents('tr').find('.car-mobile').text();
            var license = $(this).parents('tr').find('.car-license').text();
            var carModel = $(this).parents('tr').find('input[name="carModel"]').val();
            var couponName = $('.js-coupon-text').val();
            var couponId = $('.js-coupon-id').val();

            showDialog(customerCarId, customerId, name, mobile,
                license, carModel, couponName, couponId,
                thisTr.parents('tr').find('input[name="noteInfoId"]').val());
        });
    }

    revisitDialog.show = showDialog;

    module.exports = revisitDialog;
});
