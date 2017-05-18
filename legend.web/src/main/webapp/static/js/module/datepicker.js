/**
 * Created by sky on 16/4/22.
 * My97时间插件封装
 */

define(function(require, exports, module) {
    var WdatePicker = require('dp');

    var $document = $(document);

    function StartEnd(options) {
        this.settings = $.extend({}, StartEnd._default, options);
        this.init();
    }

    StartEnd._default = {
        // 开始日期框的ID
        start: 'startDate',
        // 结束日期框的ID
        end: 'endDate',
        // 开始日期配置
        startSettings: '',
        // 结束日期配置
        endSettings: '',
        // 是否联动
        linkage: true
    };

    StartEnd.prototype = {
        init: function() {
            var self = this,
                startMaxDate = '#F{$dp.$D(\'' + self.settings.end + '\')||\'%y-%M-%d\'}',
                endMinDate = '#F{$dp.$D(\'' + self.settings.start + '\')}',
                // 选择后触发
                picked = function (dp, dom) {
                    if (this.value != '' && dom.value == '') {
                        dom.click();
                    }
                };
            // 设置maxDate和minDated
            self.settings.startSettings || (self.settings.startSettings = {maxDate: startMaxDate});
            self.settings.endSettings || (self.settings.endSettings = {minDate: endMinDate, maxDate: '%y-%M-%d'});
            if (self.settings.linkage) {
                $.extend(self.settings.startSettings, {onpicked: function(dp) {
                    picked.call(this, dp, dp.$(self.settings.end));
                }});
                $.extend(self.settings.endSettings, {onpicked: function(dp) {
                    picked.call(this, dp, dp.$(self.settings.start));
                }});
            }
            self.bindEvent();
        },
        bindEvent: function() {
            var self = this,
                startDom = '#' + self.settings.start,
                endDom = '#' + self.settings.end;

            $document
                .off('click.dp', startDom)
                .on('click.dp', startDom, function () {
                    WdatePicker.wp(self.settings.startSettings);
                })
                .off('click.dp', endDom)
                .on('click.dp', endDom, function () {
                    WdatePicker.wp(self.settings.endSettings);
                })
        }
    };

    // 开始结束时间绑定
    module.exports.dpStartEnd = function(options) {
         return new StartEnd(options);
    };

    // 普通时间绑定
    module.exports.datePicker = function(selectors, options) {
        var settings;
        if (typeof selectors == 'object') {
            options = selectors;
            selectors = null;
        }
        if (selectors == null) {
            selectors = '.datepicker';
        }
        settings = options || {};
        $document
            .off('click.dp', selectors)
            .on('click.dp', selectors, function() {
                WdatePicker.wp(settings);
            })
    }
});
