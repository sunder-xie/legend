<#--
    选择优惠券公共模板，用到此模板页面，请在下面登记一下
     wry 2016-12-16

    用到的页面：

// 使用方式：
<#include "yqx/page/marketing/common/coupon-tpl.ftl">
<script>
    getCouponList({
        dom: '.js-reset',
        // 默认多选
        // 单选须设置为 true
        single: Boolean,
        callback: function (data) {
            // 弹窗关闭后才会调用
            // data 选中的优惠券数据, array
            // this 对应的优惠券 dom
        }
    })
    </script>
 -->

<style>
    .get-coupon-dialog {
        overflow: hidden;
    }
    .get-coupon-dialog .yqx-dialog-body{
        width: 544px;
        height: 241px;
        padding: 10px 0;
    }
    .get-coupon-dialog .form-box{
        margin: 10px 0;
        padding: 0 36px;
        border: none;
    }
    .get-coupon-dialog .coupon-box {
        padding: 0 36px;
        height: 200px;
        overflow-y: auto;
    }
    .get-coupon-dialog .coupon-box li{
        height: 32px;
        border: 1px solid #cacaca;
        border-radius: 2px;
        padding: 10px;
        margin-bottom: 10px;
    }
    .get-coupon-dialog .coupon-box .coupon-left{
        width: 350px;
        height: 32px;
        float: left;
    }
    .get-coupon-dialog .coupon-box .coupon-right{
        height: 32px;
        float: right;
        text-align: right;
    }
    .get-coupon-dialog .coupon-box p{
        line-height: 16px;
    }
</style>

<div class="yqx-dialog hide get-coupon-dialog">
    <div class="dialog-title">
       选择优惠券
    </div>
    <div class="yqx-dialog-body">
        <div class="form-box">
            <a href="${BASE_PATH}/account/coupon/create" target="_blank"
               class="yqx-btn yqx-btn-1 yqx-btn-small"> + 新建优惠券</a>
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-small js-coupon-name" value="" placeholder="">
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-coupon-search">搜索</button>
        </div>
        <ul class="coupon-box" id="couponListContent">
        </ul>
    </div>
</div>

<script id="getCouponListTpl" type="text/template">
    <%if(json.data != null){%>
    <% for(var i in json.data) { %>
    <%var t = json.data[i];%>
        <li data-service-id="<%=t.id%>" class="js-get-coupon tag-control-n <%if(selectedCoupon[t.id]) {%> selected <%}%>">
            <div class="coupon-left">
                <p><%=t.couponName%></p>
                <p>有效期：<%=t.validityPeriodStr%></p>
            </div>
            <div class="coupon-right">
                <p class="coupon-type"><%=t.couponTypeName%></p>
                <p class="coupon-money">¥ <%=t.discountAmount%></p>
            </div>
        </li>
    <%}%>
    <%}%>
</script>

<script>
    function getCouponList(opt) {
        var options;
        var currentAjax;
        var _dialogId,
            _data,
            _selectedCoupon = {};
        var defaultOpt = {
            ajaxData: {
                url: '/account/coupon/search',
                data: {
                    serviceName: '',
                    type: 1
                }
            }
        };

        defaultOpt.ajaxData.data = $.extend(true, defaultOpt.ajaxData.data, opt.data);
        if (opt.url) {
            defaultOpt.ajaxData.url = opt.url;
        }

        // 默认多选
        opt.single = opt.single || false;
        // 单选即关闭
        opt.singleClose = opt.singleClose === true;

        options = $.extend(true, defaultOpt, opt);

        var $box = $('.get-coupon-dialog');

        /********** 函数部分 开始 *************/

        function init(options) {
            // 显示 选择配件
            setDialog(options.dom, options.ajaxData);

            setSearchEvent(options.ajaxData);

            setDataSelectEvent();

        }

        /** 显示主体
         * dom: 点击事件绑定的选择器
         * $table: 主体的table jquery 对象
         * ajaxData: 传入的相关数据
         */
        function setDialog(dom, ajaxData) {
            $('body').on('click', dom, function () {
                $('.js-coupon-name').val('')
                    .trigger('input');

                if (ajaxData && ajaxData.url) {
                    send(ajaxData);
                }
            });

        }

        function setSearchEvent(ajaxData) {
            $(document).on('click', '.get-coupon-dialog .js-coupon-search', function () {
                var val = $('.get-coupon-dialog .js-coupon-name').val();

                if (ajaxData && ajaxData.data)
                    ajaxData.data.serviceName = val;

                setTimeout(function() {
                    currentAjax && currentAjax.abort();

                    currentAjax = send(ajaxData, true);
                }, 500);
            });
        }

        function dialogEnd() {
            var ele = $('.get-coupon-dialog .js-get-coupon.selected');
            var data = [];

            _selectedCoupon = {};
            ele.each(function () {
                var id = $(this).data('serviceId');

                data.push(_data[id]);
                _selectedCoupon[id] = true;
            });

            options.ajaxData.data.serviceName = '';
            options.callback && typeof options.callback == 'function'
            && options.callback.call(this, data);

            _dialogId = null;
        }

        // 数据选择
        function setDataSelectEvent() {
            $('.get-coupon-dialog').on('click', '.js-get-coupon', function () {
                var id = $(this).data('id');

                if($(this).hasClass('selected')) {
                    $(this).removeClass('selected');
                    _selectedCoupon[id] = false;

                    return;
                } else {
                    if (options.single) {
                        $('.get-coupon-dialog .js-get-coupon.selected').removeClass('selected');
                        _selectedCoupon = {};
                    }
                    _selectedCoupon[id] = true;

                    $(this).addClass('selected');
                }

                if(options.singleClose && options.single) {
                    seajs.use('dialog', function (dg) {
                        dg.close(_dialogId);
                    });
                }
            });
        }

        function send(ajaxData) {
            seajs.use(['art', 'dialog'], function (art, dialog) {
                art.helper('selectedCoupon', _selectedCoupon);
                $.ajax({
                    url: BASE_PATH + ajaxData.url,
                    data: ajaxData.data
                }).done(function (json) {
                    if (json && json.success) {
                        var html = art("getCouponListTpl", {json: json});

                        _data = convertData(json.data);

                        $box.find('.coupon-box').html(html);

                        if(!_dialogId) {
                            _dialogId = dialog.open({
                                content: $box,
                                // 关闭时清除数据
                                end: dialogEnd
                            });
                        }
                    } else {
                        dialog.fail(json.errorMsg);
                    }
                });
            });
        }

        function convertData(data) {
            var ret = {};

            if(data && data.length) {
                for (var i in data) {
                    ret[data[i].id] = data[i];
                }
            }
            return ret;
        }

        /********** 函数部分 结束 *************/

        /********** 模块初始化    *************/
        return init(options);
    }
</script>