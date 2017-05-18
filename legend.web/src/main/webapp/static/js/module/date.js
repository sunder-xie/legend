/**
 * ch 2016-04-09
 * 日历，jquery ui 插件
 *
 * 已废弃 sky 2016-04-25
 */

define(function (require, exports, module) {
    /*
     * jQuery UI Datepicker 1.7.1
     *
     * Copyright (c) 2009 AUTHORS.txt (http://jqueryui.com/about)
     * Dual licensed under the MIT (MIT-LICENSE.txt)
     * and GPL (GPL-LICENSE.txt) licenses.
     *
     * http://docs.jquery.com/UI/Datepicker
     *
     * Depends:
     *	ui.core.js
     */
    (function ($) {
        $.extend($.ui, {
            datepicker: {
                version: "1.7.1"
            }
        });
        var PROP_NAME = "datepicker";

        function Datepicker() {
            this.debug = false;
            this._curInst = null;
            this._keyEvent = false;
            this._disabledInputs = [];
            this._datepickerShowing = false;
            this._inDialog = false;
            this._mainDivId = "ui-datepicker-div";
            this._inlineClass = "ui-datepicker-inline";
            this._appendClass = "ui-datepicker-append";
            this._triggerClass = "ui-datepicker-trigger";
            this._dialogClass = "ui-datepicker-dialog";
            this._disableClass = "ui-datepicker-disabled";
            this._unselectableClass = "ui-datepicker-unselectable";
            this._currentClass = "ui-datepicker-current-day";
            this._dayOverClass = "ui-datepicker-days-cell-over";
            this.regional = [];
            this.regional[""] = {
                clearText: '清除',
                clearStatus: '清除已选日期',
                closeText: '关闭',
                closeStatus: '不改变当前选择',
                prevText: '<上月',
                prevStatus: '显示上月',
                prevBigText: '<<',
                prevBigStatus: '显示上一年',
                nextText: '下月>',
                nextStatus: '显示下月',
                nextBigText: '>>',
                nextBigStatus: '显示下一年',
                currentText: '今天',
                currentStatus: '显示本月',
                monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
                monthNamesShort: ['一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '十一', '十二'],
                monthStatus: '选择月份',
                yearStatus: '选择年份',
                weekHeader: '周',
                weekStatus: '年内周次',
                dayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
                dayNamesShort: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
                dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
                dayStatus: '设置 DD 为一周起始',
                dateStatus: '选择 m月 d日, DD',
                dateFormat: 'yy-mm-dd',
                firstDay: 1,
                initStatus: '请选择日期',
                isRTL: false

            };
            this._defaults = {
                showOn: "focus",
                showAnim: "show",
                showOptions: {},
                defaultDate: null,
                appendText: "",
                buttonText: "...",
                buttonImage: "",
                buttonImageOnly: false,
                hideIfNoPrevNext: false,
                navigationAsDateFormat: false,
                gotoCurrent: false,
                changeMonth: true,
                changeYear: true,
                showMonthAfterYear: false,
                yearRange: "-10:+10",
                showOtherMonths: false,
                calculateWeek: this.iso8601Week,
                shortYearCutoff: "+10",
                minDate: null,
                maxDate: null,
                duration: "normal",
                beforeShowDay: null,
                beforeShow: null,
                onSelect: null,
                onChangeMonthYear: null,
                onClose: null,
                numberOfMonths: 1,
                showCurrentAtPos: 0,
                stepMonths: 1,
                stepBigMonths: 12,
                altField: "",
                altFormat: "",
                constrainInput: true,
                showTime: false,
                // wry 16-04-15，是否显示秒
                showSecond: true,
                showButtonPanel: false
            };
            $.extend(this._defaults, this.regional[""]);
            this.dpDiv = $('<div id="' + this._mainDivId + '" class="ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all ui-helper-hidden-accessible" style="display:none"></div>')
        }

        $.extend(Datepicker.prototype, {
            markerClassName: "hasDatepicker",
            log: function () {
                if (this.debug) {
                    console.log.apply("", arguments)
                }
            },
            setDefaults: function (settings) {
                extendRemove(this._defaults, settings || {});
                return this
            },
            _attachDatepicker: function (target, settings) {
                var inlineSettings = null;
                for (var attrName in this._defaults) {
                    var attrValue = target.getAttribute("date:" + attrName);
                    if (attrValue) {
                        inlineSettings = inlineSettings || {};
                        try {
                            inlineSettings[attrName] = eval(attrValue)
                        } catch (err) {
                            inlineSettings[attrName] = attrValue
                        }
                    }
                }
                var nodeName = target.nodeName.toLowerCase();
                var inline = (nodeName == "div" || nodeName == "span");
                if (!target.id) {
                    target.id = "dp" + (++this.uuid)
                }
                var inst = this._newInst($(target), inline);
                inst.settings = $.extend({}, settings || {}, inlineSettings || {});
                if (nodeName == "input") {
                    this._connectDatepicker(target, inst)
                } else {
                    if (inline) {
                        this._inlineDatepicker(target, inst)
                    }
                }
            },
            _newInst: function (target, inline) {
                var id = target[0].id.replace(/([:\[\]\.])/g, "\\\\$1");
                return {
                    id: id,
                    input: target,
                    selectedDay: 0,
                    selectedMonth: 0,
                    selectedYear: 0,
                    drawMonth: 0,
                    drawYear: 0,
                    inline: inline,
                    dpDiv: (!inline ? this.dpDiv : $('<div class="' + this._inlineClass + ' ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all"></div>'))
                }
            },
            _connectDatepicker: function (target, inst) {
                var input = $(target);
                inst.trigger = $([]);
                if (input.hasClass(this.markerClassName)) {
                    return
                }
                var appendText = this._get(inst, "appendText");
                var isRTL = this._get(inst, "isRTL");
                if (appendText) {
                    input[isRTL ? "before" : "after"]('<span class="' + this._appendClass + '">' + appendText + "</span>")
                }
                var showOn = this._get(inst, "showOn");
                if (showOn == "focus" || showOn == "both") {
                    input.focus(this._showDatepicker)
                }
                if (showOn == "button" || showOn == "both") {
                    var buttonText = this._get(inst, "buttonText");
                    var buttonImage = this._get(inst, "buttonImage");
                    inst.trigger = $(this._get(inst, "buttonImageOnly") ? $("<img/>").addClass(this._triggerClass).attr({
                        src: buttonImage,
                        alt: buttonText,
                        title: buttonText
                    }) : $('<button type="button"></button>').addClass(this._triggerClass).html(buttonImage == "" ? buttonText : $("<img/>").attr({
                        src: buttonImage,
                        alt: buttonText,
                        title: buttonText
                    })));
                    input[isRTL ? "before" : "after"](inst.trigger);
                    inst.trigger.click(function () {
                        if ($.datepicker._datepickerShowing && $.datepicker._lastInput == target) {
                            $.datepicker._hideDatepicker()
                        } else {
                            $.datepicker._showDatepicker(target)
                        }
                        return false
                    })
                }
                input.addClass(this.markerClassName).keydown(this._doKeyDown).keypress(this._doKeyPress).bind("setData.datepicker", function (event, key, value) {
                    inst.settings[key] = value
                }).bind("getData.datepicker", function (event, key) {
                    return this._get(inst, key)
                });
                $.data(target, PROP_NAME, inst)
            },
            _inlineDatepicker: function (target, inst) {
                var divSpan = $(target);
                if (divSpan.hasClass(this.markerClassName)) {
                    return
                }
                divSpan.addClass(this.markerClassName).append(inst.dpDiv).bind("setData.datepicker", function (event, key, value) {
                    inst.settings[key] = value
                }).bind("getData.datepicker", function (event, key) {
                    return this._get(inst, key)
                });
                $.data(target, PROP_NAME, inst);
                this._setDate(inst, this._getDefaultDate(inst));
                this._updateDatepicker(inst);
                this._updateAlternate(inst)
            },
            _dialogDatepicker: function (input, dateText, onSelect, settings, pos) {
                var inst = this._dialogInst;
                if (!inst) {
                    var id = "dp" + (++this.uuid);
                    this._dialogInput = $('<input type="text" id="' + id + '" size="1" style="position: absolute; top: -100px;"/>');
                    this._dialogInput.keydown(this._doKeyDown);
                    $("body").append(this._dialogInput);
                    inst = this._dialogInst = this._newInst(this._dialogInput, false);
                    inst.settings = {};
                    $.data(this._dialogInput[0], PROP_NAME, inst)
                }
                extendRemove(inst.settings, settings || {});
                this._dialogInput.val(dateText);
                this._pos = (pos ? (pos.length ? pos : [pos.pageX, pos.pageY]) : null);
                if (!this._pos) {
                    var browserWidth = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
                    var browserHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
                    var scrollX = document.documentElement.scrollLeft || document.body.scrollLeft;
                    var scrollY = document.documentElement.scrollTop || document.body.scrollTop;
                    this._pos = [(browserWidth / 2) - 100 + scrollX, (browserHeight / 2) - 150 + scrollY]
                }
                this._dialogInput.css("left", this._pos[0] + "px").css("top", this._pos[1] + "px");
                inst.settings.onSelect = onSelect;
                this._inDialog = true;
                this.dpDiv.addClass(this._dialogClass);
                this._showDatepicker(this._dialogInput[0]);
                if ($.blockUI) {
                    $.blockUI(this.dpDiv)
                }
                $.data(this._dialogInput[0], PROP_NAME, inst);
                return this
            },
            _destroyDatepicker: function (target) {
                var $target = $(target);
                var inst = $.data(target, PROP_NAME);
                if (!$target.hasClass(this.markerClassName)) {
                    return
                }
                var nodeName = target.nodeName.toLowerCase();
                $.removeData(target, PROP_NAME);
                if (nodeName == "input") {
                    inst.trigger.remove();
                    $target.siblings("." + this._appendClass).remove().end().removeClass(this.markerClassName).unbind("focus", this._showDatepicker).unbind("keydown", this._doKeyDown).unbind("keypress", this._doKeyPress)
                } else {
                    if (nodeName == "div" || nodeName == "span") {
                        $target.removeClass(this.markerClassName).empty()
                    }
                }
            },
            _enableDatepicker: function (target) {
                var $target = $(target);
                var inst = $.data(target, PROP_NAME);
                if (!$target.hasClass(this.markerClassName)) {
                    return
                }
                var nodeName = target.nodeName.toLowerCase();
                if (nodeName == "input") {
                    target.disabled = false;
                    inst.trigger.filter("button").each(function () {
                        this.disabled = false
                    }).end().filter("img").css({
                        opacity: "1.0",
                        cursor: ""
                    })
                } else {
                    if (nodeName == "div" || nodeName == "span") {
                        var inline = $target.children("." + this._inlineClass);
                        inline.children().removeClass("ui-state-disabled")
                    }
                }
                this._disabledInputs = $.map(this._disabledInputs, function (value) {
                    return (value == target ? null : value)
                })
            },
            _disableDatepicker: function (target) {
                var $target = $(target);
                var inst = $.data(target, PROP_NAME);
                if (!$target.hasClass(this.markerClassName)) {
                    return
                }
                var nodeName = target.nodeName.toLowerCase();
                if (nodeName == "input") {
                    target.disabled = true;
                    inst.trigger.filter("button").each(function () {
                        this.disabled = true
                    }).end().filter("img").css({
                        opacity: "0.5",
                        cursor: "default"
                    })
                } else {
                    if (nodeName == "div" || nodeName == "span") {
                        var inline = $target.children("." + this._inlineClass);
                        inline.children().addClass("ui-state-disabled")
                    }
                }
                this._disabledInputs = $.map(this._disabledInputs, function (value) {
                    return (value == target ? null : value)
                });
                this._disabledInputs[this._disabledInputs.length] = target
            },
            _isDisabledDatepicker: function (target) {
                if (!target) {
                    return false
                }
                for (var i = 0; i < this._disabledInputs.length; i++) {
                    if (this._disabledInputs[i] == target) {
                        return true
                    }
                }
                return false
            },
            _getInst: function (target) {
                try {
                    return $.data(target, PROP_NAME)
                } catch (err) {
                    throw "Missing instance data for this datepicker"
                }
            },
            _optionDatepicker: function (target, name, value) {
                var settings = name || {};
                if (typeof name == "string") {
                    settings = {};
                    settings[name] = value
                }
                var inst = this._getInst(target);
                if (inst) {
                    if (this._curInst == inst) {
                        this._hideDatepicker(null)
                    }
                    extendRemove(inst.settings, settings);
                    var date = new Date();
                    extendRemove(inst, {
                        rangeStart: null,
                        endDay: null,
                        endMonth: null,
                        endYear: null,
                        selectedDay: date.getDate(),
                        selectedMonth: date.getMonth(),
                        selectedYear: date.getFullYear(),
                        currentDay: date.getDate(),
                        currentMonth: date.getMonth(),
                        currentYear: date.getFullYear(),
                        drawMonth: date.getMonth(),
                        drawYear: date.getFullYear()
                    });
                    this._updateDatepicker(inst)
                }
            },
            _changeDatepicker: function (target, name, value) {
                this._optionDatepicker(target, name, value)
            },
            _refreshDatepicker: function (target) {
                var inst = this._getInst(target);
                if (inst) {
                    this._updateDatepicker(inst)
                }
            },
            _setDateDatepicker: function (target, date, endDate) {
                var inst = this._getInst(target);
                if (inst) {
                    this._setDate(inst, date, endDate);
                    this._updateDatepicker(inst);
                    this._updateAlternate(inst)
                }
            },
            _getDateDatepicker: function (target) {
                var inst = this._getInst(target);
                if (inst && !inst.inline) {
                    this._setDateFromField(inst)
                }
                return (inst ? this._getDate(inst) : null)
            },
            _doKeyDown: function (event) {
                var inst = $.datepicker._getInst(event.target);
                var handled = true;
                var isRTL = inst.dpDiv.is(".ui-datepicker-rtl");
                inst._keyEvent = true;
                if ($.datepicker._datepickerShowing) {
                    switch (event.keyCode) {
                        case 9:
                            $.datepicker._hideDatepicker(null, "");
                            break;
                        case 13:
                            var sel = $("td." + $.datepicker._dayOverClass + ", td." + $.datepicker._currentClass, inst.dpDiv);
                            if (sel[0]) {
                                $.datepicker._selectDay(event.target, inst.selectedMonth, inst.selectedYear, sel[0])
                            } else {
                                $.datepicker._hideDatepicker(null, $.datepicker._get(inst, "duration"))
                            }
                            return false;
                            break;
                        case 27:
                            $.datepicker._hideDatepicker(null, $.datepicker._get(inst, "duration"));
                            break;
                        case 33:
                            $.datepicker._adjustDate(event.target, (event.ctrlKey ? -$.datepicker._get(inst, "stepBigMonths") : -$.datepicker._get(inst, "stepMonths")), "M");
                            break;
                        case 34:
                            $.datepicker._adjustDate(event.target, (event.ctrlKey ? +$.datepicker._get(inst, "stepBigMonths") : +$.datepicker._get(inst, "stepMonths")), "M");
                            break;
                        case 35:
                            if (event.ctrlKey || event.metaKey) {
                                $.datepicker._clearDate(event.target)
                            }
                            handled = event.ctrlKey || event.metaKey;
                            break;
                        case 36:
                            if (event.ctrlKey || event.metaKey) {
                                $.datepicker._gotoToday(event.target)
                            }
                            handled = event.ctrlKey || event.metaKey;
                            break;
                        case 37:
                            if (event.ctrlKey || event.metaKey) {
                                $.datepicker._adjustDate(event.target, (isRTL ? +1 : -1), "D")
                            }
                            handled = event.ctrlKey || event.metaKey;
                            if (event.originalEvent.altKey) {
                                $.datepicker._adjustDate(event.target, (event.ctrlKey ? -$.datepicker._get(inst, "stepBigMonths") : -$.datepicker._get(inst, "stepMonths")), "M")
                            }
                            break;
                        case 38:
                            if (event.ctrlKey || event.metaKey) {
                                $.datepicker._adjustDate(event.target, -7, "D")
                            }
                            handled = event.ctrlKey || event.metaKey;
                            break;
                        case 39:
                            if (event.ctrlKey || event.metaKey) {
                                $.datepicker._adjustDate(event.target, (isRTL ? -1 : +1), "D")
                            }
                            handled = event.ctrlKey || event.metaKey;
                            if (event.originalEvent.altKey) {
                                $.datepicker._adjustDate(event.target, (event.ctrlKey ? +$.datepicker._get(inst, "stepBigMonths") : +$.datepicker._get(inst, "stepMonths")), "M")
                            }
                            break;
                        case 40:
                            if (event.ctrlKey || event.metaKey) {
                                $.datepicker._adjustDate(event.target, +7, "D")
                            }
                            handled = event.ctrlKey || event.metaKey;
                            break;
                        default:
                            handled = false
                    }
                } else {
                    if (event.keyCode == 36 && event.ctrlKey) {
                        $.datepicker._showDatepicker(this)
                    } else {
                        handled = false
                    }
                }
                if (handled) {
                    event.preventDefault();
                    event.stopPropagation()
                }
            },
            _doKeyPress: function (event) {
                var inst = $.datepicker._getInst(event.target);
                if ($.datepicker._get(inst, "constrainInput")) {
                    var chars = $.datepicker._possibleChars($.datepicker._get(inst, "dateFormat"));
                    var chr = String.fromCharCode(event.charCode == undefined ? event.keyCode : event.charCode);
                    return event.ctrlKey || (chr < " " || !chars || chars.indexOf(chr) > -1)
                }
            },
            _showDatepicker: function (input) {
                input = input.target || input;
                if (input.nodeName.toLowerCase() != "input") {
                    input = $("input", input.parentNode)[0]
                }
                if ($.datepicker._isDisabledDatepicker(input) || $.datepicker._lastInput == input) {
                    return
                }
                var inst = $.datepicker._getInst(input);
                var beforeShow = $.datepicker._get(inst, "beforeShow");
                extendRemove(inst.settings, (beforeShow ? beforeShow.apply(input, [input, inst]) : {}));
                $.datepicker._hideDatepicker(null, "");
                $.datepicker._lastInput = input;
                $.datepicker._setDateFromField(inst);
                if ($.datepicker._inDialog) {
                    input.value = ""
                }
                if (!$.datepicker._pos) {
                    $.datepicker._pos = $.datepicker._findPos(input);
                    $.datepicker._pos[1] += input.offsetHeight
                }
                var isFixed = false;
                $(input).parents().each(function () {
                    isFixed |= $(this).css("position") == "fixed";
                    return !isFixed
                });
                if (isFixed) {
                    $.datepicker._pos[0] -= document.documentElement.scrollLeft;
                    $.datepicker._pos[1] -= document.documentElement.scrollTop
                }
                var offset = {
                    left: $.datepicker._pos[0],
                    top: $.datepicker._pos[1]
                };
                $.datepicker._pos = null;
                inst.rangeStart = null;
                inst.dpDiv.css({
                    position: "absolute",
                    display: "block",
                    top: "-1000px"
                });
                $.datepicker._updateDatepicker(inst);
                offset = $.datepicker._checkOffset(inst, offset, isFixed);
                inst.dpDiv.css({
                    position: ($.datepicker._inDialog && $.blockUI ? "static" : (isFixed ? "fixed" : "absolute")),
                    display: "none",
                    left: offset.left + "px",
                    top: offset.top + "px"
                });
                if (!inst.inline) {
                    var showAnim = $.datepicker._get(inst, "showAnim") || "show";
                    var duration = $.datepicker._get(inst, "duration");
                    var postProcess = function () {
                        $.datepicker._datepickerShowing = true;

                    };
                    if ($.effects && $.effects[showAnim]) {
                        inst.dpDiv.show(showAnim, $.datepicker._get(inst, "showOptions"), duration, postProcess)
                    } else {
                        inst.dpDiv[showAnim](duration, postProcess);
                    }
                    if (duration == "") {
                        postProcess()
                    }
                    if (inst.input[0].type != "hidden") {
                        inst.input[0].focus()
                    }
                    $.datepicker._curInst = inst
                }
            },
            _updateDatepicker: function (inst) {
                var dims = {
                    width: inst.dpDiv.width() + 4,
                    height: inst.dpDiv.height() + 4
                };
                var self = this;
                inst.dpDiv.empty().append(this._generateHTML(inst)).find("iframe.ui-datepicker-cover").css({
                    width: dims.width,
                    height: dims.height
                }).end().find("button, .ui-datepicker-prev, .ui-datepicker-next, .ui-datepicker-calendar td a").bind("mouseout", function () {
                    $(this).removeClass("ui-state-hover");
                    if (this.className.indexOf("ui-datepicker-prev") != -1) {
                        $(this).removeClass("ui-datepicker-prev-hover")
                    }
                    if (this.className.indexOf("ui-datepicker-next") != -1) {
                        $(this).removeClass("ui-datepicker-next-hover")
                    }
                }).bind("mouseover", function () {
                    if (!self._isDisabledDatepicker(inst.inline ? inst.dpDiv.parent()[0] : inst.input[0])) {
                        $(this).parents(".ui-datepicker-calendar").find("a").removeClass("ui-state-hover");
                        $(this).addClass("ui-state-hover");
                        if (this.className.indexOf("ui-datepicker-prev") != -1) {
                            $(this).addClass("ui-datepicker-prev-hover")
                        }
                        if (this.className.indexOf("ui-datepicker-next") != -1) {
                            $(this).addClass("ui-datepicker-next-hover")
                        }
                    }
                }).end().find("." + this._dayOverClass + " a").trigger("mouseover").end();
                var numMonths = this._getNumberOfMonths(inst);
                var cols = numMonths[1];
                var width = 17;
                if (cols > 1) {
                    inst.dpDiv.addClass("ui-datepicker-multi-" + cols).css("width", (width * cols) + "em")
                } else {
                    inst.dpDiv.removeClass("ui-datepicker-multi-2 ui-datepicker-multi-3 ui-datepicker-multi-4").width("")
                }
                inst.dpDiv[(numMonths[0] != 1 || numMonths[1] != 1 ? "add" : "remove") + "Class"]("ui-datepicker-multi");
                inst.dpDiv[(this._get(inst, "isRTL") ? "add" : "remove") + "Class"]("ui-datepicker-rtl");
                if (inst.input && inst.input[0].type != "hidden" && inst == $.datepicker._curInst) {
                    $(inst.input[0]).focus()
                }
            },
            _checkOffset: function (inst, offset, isFixed) {
                var dpWidth = inst.dpDiv.outerWidth();
                var dpHeight = inst.dpDiv.outerHeight();
                var inputWidth = inst.input ? inst.input.outerWidth() : 0;
                var inputHeight = inst.input ? inst.input.outerHeight() : 0;
                var viewWidth = (window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth) + $(document).scrollLeft();
                var viewHeight = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight) + $(document).scrollTop();
                offset.left -= (this._get(inst, "isRTL") ? (dpWidth - inputWidth) : 0);
                offset.left -= (isFixed && offset.left == inst.input.offset().left) ? $(document).scrollLeft() : 0;
                offset.top -= (isFixed && offset.top == (inst.input.offset().top + inputHeight)) ? $(document).scrollTop() : 0;
                offset.left -= (offset.left + dpWidth > viewWidth && viewWidth > dpWidth) ? Math.abs(offset.left + dpWidth - viewWidth) : 0;
                offset.top -= (offset.top + dpHeight > viewHeight && viewHeight > dpHeight) ? Math.abs(offset.top + dpHeight + inputHeight * 2 - viewHeight) : 0;
                return offset
            },
            _findPos: function (obj) {
                while (obj && (obj.type == "hidden" || obj.nodeType != 1)) {
                    obj = obj.nextSibling
                }
                var position = $(obj).offset();
                return [position.left, position.top]
            },
            _hideDatepicker: function (input, duration) {
                var inst = this._curInst;
                if (!inst || (input && inst != $.data(input, PROP_NAME))) {
                    return
                }
                if (inst.stayOpen) {
                    this._selectDate("#" + inst.id, this._formatDate(inst, inst.currentDay, inst.currentMonth, inst.currentYear))
                }
                inst.stayOpen = false;
                if (this._datepickerShowing) {
                    duration = (duration != null ? duration : this._get(inst, "duration"));
                    var showAnim = this._get(inst, "showAnim");
                    var postProcess = function () {
                        $.datepicker._tidyDialog(inst)
                    };
                    if (duration != "" && $.effects && $.effects[showAnim]) {
                        inst.dpDiv.hide(showAnim, $.datepicker._get(inst, "showOptions"), duration, postProcess)
                    } else {
                        inst.dpDiv[(duration == "" ? "hide" : "hide")](duration, postProcess)
                    }
                    if (duration == "") {
                        this._tidyDialog(inst)
                    }
                    var onClose = this._get(inst, "onClose");
                    if (onClose) {
                        onClose.apply((inst.input ? inst.input[0] : null), [(inst.input ? inst.input.val() : ""), inst])
                    }
                    this._datepickerShowing = false;
                    this._lastInput = null;
                    if (this._inDialog) {
                        this._dialogInput.css({
                            position: "absolute",
                            left: "0",
                            top: "-100px"
                        });
                        if ($.blockUI) {
                            $.unblockUI();
                            $("body").append(this.dpDiv)
                        }
                    }
                    this._inDialog = false
                }
                this._curInst = null
            },
            _tidyDialog: function (inst) {
                inst.dpDiv.removeClass(this._dialogClass).unbind(".ui-datepicker-calendar")
            },
            _checkExternalClick: function (event) {
                if (!$.datepicker._curInst) {
                    return
                }
                var $target = $(event.target);
                if (($target.parents("#" + $.datepicker._mainDivId).length == 0) && !$target.hasClass($.datepicker.markerClassName) && !$target.hasClass($.datepicker._triggerClass) && $.datepicker._datepickerShowing && !($.datepicker._inDialog && $.blockUI)) {
                    $.datepicker._hideDatepicker(null, "")
                }
            },
            _adjustDate: function (id, offset, period) {
                var target = $(id);
                var inst = this._getInst(target[0]);
                if (this._isDisabledDatepicker(target[0])) {
                    return
                }
                this._adjustInstDate(inst, offset + (period == "M" ? this._get(inst, "showCurrentAtPos") : 0), period);
                this._updateDatepicker(inst)
            },
            _gotoToday: function (id) {
                var target = $(id);
                var inst = this._getInst(target[0]);
                if (this._get(inst, "gotoCurrent") && inst.currentDay) {
                    inst.selectedDay = inst.currentDay;
                    inst.drawMonth = inst.selectedMonth = inst.currentMonth;
                    inst.drawYear = inst.selectedYear = inst.currentYear
                } else {
                    var date = new Date();
                    inst.selectedDay = date.getDate();
                    inst.drawMonth = inst.selectedMonth = date.getMonth();
                    inst.drawYear = inst.selectedYear = date.getFullYear()
                }
                this._notifyChange(inst);
                this._adjustDate(target)
            },
            _selectMonthYear: function (id, select, period) {
                var target = $(id);
                var inst = this._getInst(target[0]);
                inst._selectingMonthYear = false;
                inst["selected" + (period == "M" ? "Month" : "Year")] = inst["draw" + (period == "M" ? "Month" : "Year")] = parseInt(select.options[select.selectedIndex].value, 10);
                this._notifyChange(inst);
                this._adjustDate(target)
            },
            _clickMonthYear: function (id) {
                var target = $(id);
                var inst = this._getInst(target[0]);
                if (inst.input && inst._selectingMonthYear) {
                    inst.input[0].focus()
                }
                inst._selectingMonthYear = !inst._selectingMonthYear
            },
            _selectDay: function (id, month, year, td) {
                var target = $(id);
                if ($(td).hasClass(this._unselectableClass) || this._isDisabledDatepicker(target[0])) {
                    return
                }
                var inst = this._getInst(target[0]);
                inst.selectedDay = inst.currentDay = $("a", td).html();
                inst.selectedMonth = inst.currentMonth = month;
                inst.selectedYear = inst.currentYear = year;
                if (inst.stayOpen) {
                    inst.endDay = inst.endMonth = inst.endYear = null
                }
                this._selectDate(id, this._formatDate(inst, inst.currentDay, inst.currentMonth, inst.currentYear));


                /**
                 * ch 2016-04-11修改 start
                 **/
                var year = inst.currentYear;
                var month = inst.currentMonth;
                month = parseInt(month) + 1;
                var day = inst.currentDay;
                var hour = $("#date_hour").val();
                hour = $.trim(hour);
                var minutes = $("#date_minutes").val();
                minutes = $.trim(minutes);
                var seconds = $("#date_seconds").val();
                seconds = $.trim(seconds);
                if (hour == "") {
                    hour = "00";
                }
                if (minutes == "") {
                    minutes = "00";
                }
                if (isNaN(hour) || isNaN(minutes)) {
                    alert("时间格式不正确！");
                    return;
                }

                if (hour >= 24) {
                    hour = 23;
                }


                if (minutes >= 60) {
                    minutes = 59;
                }


                if (hour.length == 1) {
                    hour = "0" + hour;
                }

                if (minutes.length == 1) {
                    minutes = "0" + minutes;
                }

                if (seconds.length == 1) {
                    seconds = "0" + seconds;
                }


                var dateStr;
                var showTime = this._get(inst, 'showTime');
                var showSecond = this._get(inst, 'showSecond');
                if (showTime) {
                    if (showSecond)
                        dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
                    else {
                        dateStr = year + "-" + month + "-" + day + " " + hour + ":" + minutes;
                    }
                } else {
                    dateStr = year + "-" + month + "-" + day;
                }
                this._selectDate(id, dateStr);
                /**
                 * ch 2016-04-11修改 end
                 **/
                if (inst.stayOpen) {
                    inst.rangeStart = this._daylightSavingAdjust(new Date(inst.currentYear, inst.currentMonth, inst.currentDay));
                    this._updateDatepicker(inst)
                }
            },
            _clearDate: function (id) {
                var target = $(id);
                var inst = this._getInst(target[0]);
                inst.stayOpen = false;
                inst.endDay = inst.endMonth = inst.endYear = inst.rangeStart = null;
                this._selectDate(target, "")
            },
            _selectDate: function (id, dateStr) {
                var target = $(id);
                var inst = this._getInst(target[0]);
                dateStr = (dateStr != null ? dateStr : this._formatDate(inst));
                if (inst.input) {
                    inst.input.val(dateStr).blur();
                }
                this._updateAlternate(inst);
                var onSelect = this._get(inst, "onSelect");
                if (onSelect) {
                    onSelect.apply((inst.input ? inst.input[0] : null), [dateStr, inst])
                } else {
                    if (inst.input) {
                        inst.input.trigger("change")
                    }
                }
                if (inst.inline) {
                    this._updateDatepicker(inst)
                } else {
                    if (!inst.stayOpen) {
                        this._hideDatepicker(null, this._get(inst, "duration"));
                        this._lastInput = inst.input[0];
                        if (typeof(inst.input[0]) != "object") {
                            inst.input[0].focus()
                        }
                        this._lastInput = null
                    }
                }
            },
            _updateAlternate: function (inst) {
                var altField = this._get(inst, "altField");
                if (altField) {
                    var altFormat = this._get(inst, "altFormat") || this._get(inst, "dateFormat");
                    var date = this._getDate(inst);
                    dateStr = this.formatDate(altFormat, date, this._getFormatConfig(inst));
                    $(altField).each(function () {
                        $(this).val(dateStr)
                    })
                }
            },
            noWeekends: function (date) {
                var day = date.getDay();
                return [(day > 0 && day < 6), ""]
            },
            iso8601Week: function (date) {
                var checkDate = new Date(date.getFullYear(), date.getMonth(), date.getDate());
                var firstMon = new Date(checkDate.getFullYear(), 1 - 1, 4);
                var firstDay = firstMon.getDay() || 7;
                firstMon.setDate(firstMon.getDate() + 1 - firstDay);
                if (firstDay < 4 && checkDate < firstMon) {
                    checkDate.setDate(checkDate.getDate() - 3);
                    return $.datepicker.iso8601Week(checkDate)
                } else {
                    if (checkDate > new Date(checkDate.getFullYear(), 12 - 1, 28)) {
                        firstDay = new Date(checkDate.getFullYear() + 1, 1 - 1, 4).getDay() || 7;
                        if (firstDay > 4 && (checkDate.getDay() || 7) < firstDay - 3) {
                            return 1
                        }
                    }
                }
                return Math.floor(((checkDate - firstMon) / 86400000) / 7) + 1
            },
            parseDate: function (format, value, settings) {
                if (format == null || value == null) {
                    throw "Invalid arguments"
                }
                value = (typeof value == "object" ? value.toString() : value + "");
                if (value == "") {
                    return null
                }
                var shortYearCutoff = (settings ? settings.shortYearCutoff : null) || this._defaults.shortYearCutoff;
                var dayNamesShort = (settings ? settings.dayNamesShort : null) || this._defaults.dayNamesShort;
                var dayNames = (settings ? settings.dayNames : null) || this._defaults.dayNames;
                var monthNamesShort = (settings ? settings.monthNamesShort : null) || this._defaults.monthNamesShort;
                var monthNames = (settings ? settings.monthNames : null) || this._defaults.monthNames;
                var year = -1;
                var month = -1;
                var day = -1;
                var doy = -1;
                var literal = false;
                var lookAhead = function (match) {
                    var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) == match);
                    if (matches) {
                        iFormat++
                    }
                    return matches
                };
                var getNumber = function (match) {
                    lookAhead(match);
                    var origSize = (match == "@" ? 14 : (match == "y" ? 4 : (match == "o" ? 3 : 2)));
                    var size = origSize;
                    var num = 0;
                    while (size > 0 && iValue < value.length && value.charAt(iValue) >= "0" && value.charAt(iValue) <= "9") {
                        num = num * 10 + parseInt(value.charAt(iValue++), 10);
                        size--
                    }
                    if (size == origSize) {
                        throw "Missing number at position " + iValue
                    }
                    return num
                };
                var getName = function (match, shortNames, longNames) {
                    var names = (lookAhead(match) ? longNames : shortNames);
                    var size = 0;
                    for (var j = 0; j < names.length; j++) {
                        size = Math.max(size, names[j].length)
                    }
                    var name = "";
                    var iInit = iValue;
                    while (size > 0 && iValue < value.length) {
                        name += value.charAt(iValue++);
                        for (var i = 0; i < names.length; i++) {
                            if (name == names[i]) {
                                return i + 1
                            }
                        }
                        size--
                    }
                    throw "Unknown name at position " + iInit
                };
                var checkLiteral = function () {
                    if (value.charAt(iValue) != format.charAt(iFormat)) {
                        throw "Unexpected literal at position " + iValue
                    }
                    iValue++
                };
                var iValue = 0;
                for (var iFormat = 0; iFormat < format.length; iFormat++) {
                    if (literal) {
                        if (format.charAt(iFormat) == "'" && !lookAhead("'")) {
                            literal = false
                        } else {
                            checkLiteral()
                        }
                    } else {
                        switch (format.charAt(iFormat)) {
                            case "d":
                                day = getNumber("d");
                                break;
                            case "D":
                                getName("D", dayNamesShort, dayNames);
                                break;
                            case "o":
                                doy = getNumber("o");
                                break;
                            case "m":
                                month = getNumber("m");
                                break;
                            case "M":
                                month = getName("M", monthNamesShort, monthNames);
                                break;
                            case "y":
                                year = getNumber("y");
                                break;
                            case "@":
                                var date = new Date(getNumber("@"));
                                year = date.getFullYear();
                                month = date.getMonth() + 1;
                                day = date.getDate();
                                break;
                            case "'":
                                if (lookAhead("'")) {
                                    checkLiteral()
                                } else {
                                    literal = true
                                }
                                break;
                            default:
                                checkLiteral()
                        }
                    }
                }
                if (year == -1) {
                    year = new Date().getFullYear()
                } else {
                    if (year < 100) {
                        year += new Date().getFullYear() - new Date().getFullYear() % 100 + (year <= shortYearCutoff ? 0 : -100)
                    }
                }
                if (doy > -1) {
                    month = 1;
                    day = doy;
                    do {
                        var dim = this._getDaysInMonth(year, month - 1);
                        if (day <= dim) {
                            break
                        }
                        month++;
                        day -= dim
                    } while (true)
                }
                var date = this._daylightSavingAdjust(new Date(year, month - 1, day));
                if (date.getFullYear() != year || date.getMonth() + 1 != month || date.getDate() != day) {
                    throw "Invalid date"
                }
                return date
            },
            ATOM: "yy-mm-dd",
            COOKIE: "D, dd M yy",
            ISO_8601: "yy-mm-dd",
            RFC_822: "D, d M y",
            RFC_850: "DD, dd-M-y",
            RFC_1036: "D, d M y",
            RFC_1123: "D, d M yy",
            RFC_2822: "D, d M yy",
            RSS: "D, d M y",
            TIMESTAMP: "@",
            W3C: "yy-mm-dd",
            formatDate: function (format, date, settings) {
                if (!date) {
                    return ""
                }
                var dayNamesShort = (settings ? settings.dayNamesShort : null) || this._defaults.dayNamesShort;
                var dayNames = (settings ? settings.dayNames : null) || this._defaults.dayNames;
                var monthNamesShort = (settings ? settings.monthNamesShort : null) || this._defaults.monthNamesShort;
                var monthNames = (settings ? settings.monthNames : null) || this._defaults.monthNames;
                var lookAhead = function (match) {
                    var matches = (iFormat + 1 < format.length && format.charAt(iFormat + 1) == match);
                    if (matches) {
                        iFormat++
                    }
                    return matches
                };
                var formatNumber = function (match, value, len) {
                    var num = "" + value;
                    if (lookAhead(match)) {
                        while (num.length < len) {
                            num = "0" + num
                        }
                    }
                    return num
                };
                var formatName = function (match, value, shortNames, longNames) {
                    return (lookAhead(match) ? longNames[value] : shortNames[value])
                };
                var output = "";
                var literal = false;
                if (date) {
                    for (var iFormat = 0; iFormat < format.length; iFormat++) {
                        if (literal) {
                            if (format.charAt(iFormat) == "'" && !lookAhead("'")) {
                                literal = false
                            } else {
                                output += format.charAt(iFormat)
                            }
                        } else {
                            switch (format.charAt(iFormat)) {
                                case "d":
                                    output += formatNumber("d", date.getDate(), 2);
                                    break;
                                case "D":
                                    output += formatName("D", date.getDay(), dayNamesShort, dayNames);
                                    break;
                                case "o":
                                    var doy = date.getDate();
                                    for (var m = date.getMonth() - 1; m >= 0; m--) {
                                        doy += this._getDaysInMonth(date.getFullYear(), m)
                                    }
                                    output += formatNumber("o", doy, 3);
                                    break;
                                case "m":
                                    output += formatNumber("m", date.getMonth() + 1, 2);
                                    break;
                                case "M":
                                    output += formatName("M", date.getMonth(), monthNamesShort, monthNames);
                                    break;
                                case "y":
                                    output += (lookAhead("y") ? date.getFullYear() : (date.getYear() % 100 < 10 ? "0" : "") + date.getYear() % 100);
                                    break;
                                case "@":
                                    output += date.getTime();
                                    break;
                                case "'":
                                    if (lookAhead("'")) {
                                        output += "'"
                                    } else {
                                        literal = true
                                    }
                                    break;
                                default:
                                    output += format.charAt(iFormat)
                            }
                        }
                    }
                }
                return output
            },
            _possibleChars: function (format) {
                var chars = "";
                var literal = false;
                for (var iFormat = 0; iFormat < format.length; iFormat++) {
                    if (literal) {
                        if (format.charAt(iFormat) == "'" && !lookAhead("'")) {
                            literal = false
                        } else {
                            chars += format.charAt(iFormat)
                        }
                    } else {
                        switch (format.charAt(iFormat)) {
                            case "d":
                            case "m":
                            case "y":
                            case "@":
                                chars += "0123456789";
                                break;
                            case "D":
                            case "M":
                                return null;
                            case "'":
                                if (lookAhead("'")) {
                                    chars += "'"
                                } else {
                                    literal = true
                                }
                                break;
                            default:
                                chars += format.charAt(iFormat)
                        }
                    }
                }
                return chars
            },
            _get: function (inst, name) {
                return inst.settings[name] !== undefined ? inst.settings[name] : this._defaults[name]
            },
            _setDateFromField: function (inst) {
                var dateFormat = this._get(inst, "dateFormat");
                var dates = inst.input ? inst.input.val() : null;
                inst.endDay = inst.endMonth = inst.endYear = null;
                var date = defaultDate = this._getDefaultDate(inst);
                var settings = this._getFormatConfig(inst);
                try {
                    date = this.parseDate(dateFormat, dates, settings) || defaultDate
                } catch (event) {
                    this.log(event);
                    date = defaultDate
                }
                inst.selectedDay = date.getDate();
                inst.drawMonth = inst.selectedMonth = date.getMonth();
                inst.drawYear = inst.selectedYear = date.getFullYear();
                inst.currentDay = (dates ? date.getDate() : 0);
                inst.currentMonth = (dates ? date.getMonth() : 0);
                inst.currentYear = (dates ? date.getFullYear() : 0);
                this._adjustInstDate(inst)
            },
            _getDefaultDate: function (inst) {
                var date = this._determineDate(this._get(inst, "defaultDate"), new Date());
                var minDate = this._getMinMaxDate(inst, "min", true);
                var maxDate = this._getMinMaxDate(inst, "max");
                date = (minDate && date < minDate ? minDate : date);
                date = (maxDate && date > maxDate ? maxDate : date);
                return date
            },
            _determineDate: function (date, defaultDate) {
                var offsetNumeric = function (offset) {
                    var date = new Date();
                    date.setDate(date.getDate() + offset);
                    return date
                };
                var offsetString = function (offset, getDaysInMonth) {
                    var date = new Date();
                    var year = date.getFullYear();
                    var month = date.getMonth();
                    var day = date.getDate();
                    var pattern = /([+-]?[0-9]+)\s*(d|D|w|W|m|M|y|Y)?/g;
                    var matches = pattern.exec(offset);
                    while (matches) {
                        switch (matches[2] || "d") {
                            case "d":
                            case "D":
                                day += parseInt(matches[1], 10);
                                break;
                            case "w":
                            case "W":
                                day += parseInt(matches[1], 10) * 7;
                                break;
                            case "m":
                            case "M":
                                month += parseInt(matches[1], 10);
                                day = Math.min(day, getDaysInMonth(year, month));
                                break;
                            case "y":
                            case "Y":
                                year += parseInt(matches[1], 10);
                                day = Math.min(day, getDaysInMonth(year, month));
                                break
                        }
                        matches = pattern.exec(offset)
                    }
                    return new Date(year, month, day)
                };
                date = (date == null ? defaultDate : (typeof date == "string" ? offsetString(date, this._getDaysInMonth) : (typeof date == "number" ? (isNaN(date) ? defaultDate : offsetNumeric(date)) : date)));
                date = (date && date.toString() == "Invalid Date" ? defaultDate : date);
                if (date) {
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    date.setMilliseconds(0)
                }
                return this._daylightSavingAdjust(date)
            },
            _daylightSavingAdjust: function (date) {
                if (!date) {
                    return null
                }
                date.setHours(date.getHours() > 12 ? date.getHours() + 2 : 0);
                return date
            },
            _setDate: function (inst, date, endDate) {
                var clear = !(date);
                var origMonth = inst.selectedMonth;
                var origYear = inst.selectedYear;
                date = this._determineDate(date, new Date());
                inst.selectedDay = inst.currentDay = date.getDate();
                inst.drawMonth = inst.selectedMonth = inst.currentMonth = date.getMonth();
                inst.drawYear = inst.selectedYear = inst.currentYear = date.getFullYear();
                if (origMonth != inst.selectedMonth || origYear != inst.selectedYear) {
                    this._notifyChange(inst)
                }
                this._adjustInstDate(inst);
                if (inst.input) {
                    inst.input.val(clear ? "" : this._formatDate(inst))
                }
            },
            _getDate: function (inst) {
                var startDate = (!inst.currentYear || (inst.input && inst.input.val() == "") ? null : this._daylightSavingAdjust(new Date(inst.currentYear, inst.currentMonth, inst.currentDay)));
                return startDate
            },
            _generateHTML: function (inst) {
                var today = new Date();
                today = this._daylightSavingAdjust(new Date(today.getFullYear(), today.getMonth(), today.getDate()));
                var isRTL = this._get(inst, "isRTL");
                var showButtonPanel = this._get(inst, "showButtonPanel");
                var hideIfNoPrevNext = this._get(inst, "hideIfNoPrevNext");
                var navigationAsDateFormat = this._get(inst, "navigationAsDateFormat");
                var numMonths = this._getNumberOfMonths(inst);
                var showCurrentAtPos = this._get(inst, "showCurrentAtPos");
                var stepMonths = this._get(inst, "stepMonths");
                var stepBigMonths = this._get(inst, "stepBigMonths");
                var isMultiMonth = (numMonths[0] != 1 || numMonths[1] != 1);
                var currentDate = this._daylightSavingAdjust((!inst.currentDay ? new Date(9999, 9, 9) : new Date(inst.currentYear, inst.currentMonth, inst.currentDay)));
                var minDate = this._getMinMaxDate(inst, "min", true);
                var maxDate = this._getMinMaxDate(inst, "max");
                var drawMonth = inst.drawMonth - showCurrentAtPos;
                var drawYear = inst.drawYear;
                if (drawMonth < 0) {
                    drawMonth += 12;
                    drawYear--
                }
                if (maxDate) {
                    var maxDraw = this._daylightSavingAdjust(new Date(maxDate.getFullYear(), maxDate.getMonth() - numMonths[1] + 1, maxDate.getDate()));
                    maxDraw = (minDate && maxDraw < minDate ? minDate : maxDraw);
                    while (this._daylightSavingAdjust(new Date(drawYear, drawMonth, 1)) > maxDraw) {
                        drawMonth--;
                        if (drawMonth < 0) {
                            drawMonth = 11;
                            drawYear--
                        }
                    }
                }
                inst.drawMonth = drawMonth;
                inst.drawYear = drawYear;
                var prevText = this._get(inst, "prevText");
                prevText = (!navigationAsDateFormat ? prevText : this.formatDate(prevText, this._daylightSavingAdjust(new Date(drawYear, drawMonth - stepMonths, 1)), this._getFormatConfig(inst)));
                var prev = (this._canAdjustMonth(inst, -1, drawYear, drawMonth) ? '<a class="ui-datepicker-prev ui-corner-all" onclick="DP_jQuery.datepicker._adjustDate(\'#' + inst.id + "', -" + stepMonths + ", 'M');\" title=\"" + prevText + '"><span class="ui-icon ui-icon-circle-triangle-' + (isRTL ? "e" : "w") + '">' + prevText + "</span></a>" : (hideIfNoPrevNext ? "" : '<a class="ui-datepicker-prev ui-corner-all ui-state-disabled" title="' + prevText + '"><span class="ui-icon ui-icon-circle-triangle-' + (isRTL ? "e" : "w") + '">' + prevText + "</span></a>"));
                var nextText = this._get(inst, "nextText");
                nextText = (!navigationAsDateFormat ? nextText : this.formatDate(nextText, this._daylightSavingAdjust(new Date(drawYear, drawMonth + stepMonths, 1)), this._getFormatConfig(inst)));
                var next = (this._canAdjustMonth(inst, +1, drawYear, drawMonth) ? '<a class="ui-datepicker-next ui-corner-all" onclick="DP_jQuery.datepicker._adjustDate(\'#' + inst.id + "', +" + stepMonths + ", 'M');\" title=\"" + nextText + '"><span class="ui-icon ui-icon-circle-triangle-' + (isRTL ? "w" : "e") + '">' + nextText + "</span></a>" : (hideIfNoPrevNext ? "" : '<a class="ui-datepicker-next ui-corner-all ui-state-disabled" title="' + nextText + '"><span class="ui-icon ui-icon-circle-triangle-' + (isRTL ? "w" : "e") + '">' + nextText + "</span></a>"));
                var currentText = this._get(inst, "currentText");
                var gotoDate = (this._get(inst, "gotoCurrent") && inst.currentDay ? currentDate : today);
                currentText = (!navigationAsDateFormat ? currentText : this.formatDate(currentText, gotoDate, this._getFormatConfig(inst)));
                var controls = (!inst.inline ? '<button type="button" class="ui-datepicker-close ui-state-default ui-priority-primary ui-corner-all" onclick="DP_jQuery.datepicker._hideDatepicker();">' + this._get(inst, "closeText") + "</button>" : "");
                var buttonPanel;
                var showTime = this._get(inst, 'showTime');
                var showSecond = this._get(inst, 'showSecond');
                // ch 2016-04-11 start
                if (showTime) {
                    var now = new Date();
                    var hour = now.getHours();
                    var minute = now.getMinutes();
                    var seconds = now.getSeconds();
                    // $("#date_hour").val(hour);
                    // $("#date_minutes").val(minute);
                    // $("#date_seconds").val(seconds);
                    // 
                    if (showButtonPanel) {
                        buttonPanel = '<div style="padding-top:4px;padding-bottom:4px;" class="ui-datepicker-buttonpane ui-widget-content ui-datepicker-time-box">' + (isRTL ? controls : '') +
                            (this._isInRange(inst, gotoDate) ? '<button style="display:none;" type="button" class="ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all" onclick="DP_jQuery.datepicker._gotoToday(\'#' + inst.id + '\');"' +
                            '>' + currentText + '</button><span class="time-label">选择时间:</span><input type="text" style="width:20px;" value="' + hour + '" id="date_hour" maxlength="2">:<input type="text" style="width:20px;" value="' + minute + '" id="date_minutes" maxlength="2">' + (showSecond ? ':<input type="text" style="width:20px;" value="' + seconds + '" id="date_seconds" maxlength="2">' : '') : '') + (isRTL ? '' : controls) + '</div>'
                    } else {
                        buttonPanel = '';
                    }
                } else {
                    buttonPanel = (showButtonPanel) ? '<div class="ui-datepicker-buttonpane ui-widget-content">' + (isRTL ? controls : "") + (this._isInRange(inst, gotoDate) ? '<button type="button" class="ui-datepicker-current ui-state-default ui-priority-secondary ui-corner-all" onclick="DP_jQuery.datepicker._gotoToday(\'#' + inst.id + "');\">" + currentText + "</button>" : "") + (isRTL ? "" : controls) + "</div>" : "";
                }
                // ch 2016-04-11 end
                var firstDay = parseInt(this._get(inst, "firstDay"), 10);
                firstDay = (isNaN(firstDay) ? 0 : firstDay);
                var dayNames = this._get(inst, "dayNames");
                var dayNamesShort = this._get(inst, "dayNamesShort");
                var dayNamesMin = this._get(inst, "dayNamesMin");
                var monthNames = this._get(inst, "monthNames");
                var monthNamesShort = this._get(inst, "monthNamesShort");
                var beforeShowDay = this._get(inst, "beforeShowDay");
                var showOtherMonths = this._get(inst, "showOtherMonths");
                var calculateWeek = this._get(inst, "calculateWeek") || this.iso8601Week;
                var endDate = inst.endDay ? this._daylightSavingAdjust(new Date(inst.endYear, inst.endMonth, inst.endDay)) : currentDate;
                var defaultDate = this._getDefaultDate(inst);
                var html = "";
                for (var row = 0; row < numMonths[0]; row++) {
                    var group = "";
                    for (var col = 0; col < numMonths[1]; col++) {
                        var selectedDate = this._daylightSavingAdjust(new Date(drawYear, drawMonth, inst.selectedDay));
                        var cornerClass = " ui-corner-all";
                        var calender = "";
                        if (isMultiMonth) {
                            calender += '<div class="ui-datepicker-group ui-datepicker-group-';
                            switch (col) {
                                case 0:
                                    calender += "first";
                                    cornerClass = " ui-corner-" + (isRTL ? "right" : "left");
                                    break;
                                case numMonths[1] - 1:
                                    calender += "last";
                                    cornerClass = " ui-corner-" + (isRTL ? "left" : "right");
                                    break;
                                default:
                                    calender += "middle";
                                    cornerClass = "";
                                    break
                            }
                            calender += '">'
                        }
                        calender += '<div class="ui-datepicker-header ui-widget-header ui-helper-clearfix' + cornerClass + '">' + (/all|left/.test(cornerClass) && row == 0 ? (isRTL ? next : prev) : "") + (/all|right/.test(cornerClass) && row == 0 ? (isRTL ? prev : next) : "") + this._generateMonthYearHeader(inst, drawMonth, drawYear, minDate, maxDate, selectedDate, row > 0 || col > 0, monthNames, monthNamesShort) + '</div><table class="ui-datepicker-calendar"><thead><tr>';
                        var thead = "";
                        for (var dow = 0; dow < 7; dow++) {
                            var day = (dow + firstDay) % 7;
                            thead += "<th" + ((dow + firstDay + 6) % 7 >= 5 ? ' class="ui-datepicker-week-end"' : "") + '><span title="' + dayNames[day] + '">' + dayNamesMin[day] + "</span></th>"
                        }
                        calender += thead + "</tr></thead><tbody>";
                        var daysInMonth = this._getDaysInMonth(drawYear, drawMonth);
                        if (drawYear == inst.selectedYear && drawMonth == inst.selectedMonth) {
                            inst.selectedDay = Math.min(inst.selectedDay, daysInMonth)
                        }
                        var leadDays = (this._getFirstDayOfMonth(drawYear, drawMonth) - firstDay + 7) % 7;
                        var numRows = (isMultiMonth ? 6 : Math.ceil((leadDays + daysInMonth) / 7));
                        var printDate = this._daylightSavingAdjust(new Date(drawYear, drawMonth, 1 - leadDays));
                        for (var dRow = 0; dRow < numRows; dRow++) {
                            calender += "<tr>";
                            var tbody = "";
                            for (var dow = 0; dow < 7; dow++) {
                                var daySettings = (beforeShowDay ? beforeShowDay.apply((inst.input ? inst.input[0] : null), [printDate]) : [true, ""]);
                                var otherMonth = (printDate.getMonth() != drawMonth);
                                var unselectable = otherMonth || !daySettings[0] || (minDate && printDate < minDate) || (maxDate && printDate > maxDate);
                                tbody += '<td class="' + ((dow + firstDay + 6) % 7 >= 5 ? " ui-datepicker-week-end" : "") + (otherMonth ? " ui-datepicker-other-month" : "") + ((printDate.getTime() == selectedDate.getTime() && drawMonth == inst.selectedMonth && inst._keyEvent) || (defaultDate.getTime() == printDate.getTime() && defaultDate.getTime() == selectedDate.getTime()) ? " " + this._dayOverClass : "") + (unselectable ? " " + this._unselectableClass + " ui-state-disabled" : "") + (otherMonth && !showOtherMonths ? "" : " " + daySettings[1] + (printDate.getTime() >= currentDate.getTime() && printDate.getTime() <= endDate.getTime() ? " " + this._currentClass : "") + (printDate.getTime() == today.getTime() ? " ui-datepicker-today" : "")) + '"' + ((!otherMonth || showOtherMonths) && daySettings[2] ? ' title="' + daySettings[2] + '"' : "") + (unselectable ? "" : " onclick=\"DP_jQuery.datepicker._selectDay('#" + inst.id + "'," + drawMonth + "," + drawYear + ', this);return false;"') + ">" + (otherMonth ? (showOtherMonths ? printDate.getDate() : "&#xa0;") : (unselectable ? '<span class="ui-state-default">' + printDate.getDate() + "</span>" : '<a class="ui-state-default' + (printDate.getTime() == today.getTime() ? " ui-state-highlight" : "") + (printDate.getTime() >= currentDate.getTime() && printDate.getTime() <= endDate.getTime() ? " ui-state-active" : "") + '" href="#">' + printDate.getDate() + "</a>")) + "</td>";
                                printDate.setDate(printDate.getDate() + 1);
                                printDate = this._daylightSavingAdjust(printDate)
                            }
                            calender += tbody + "</tr>"
                        }
                        drawMonth++;
                        if (drawMonth > 11) {
                            drawMonth = 0;
                            drawYear++
                        }
                        calender += "</tbody></table>" + (isMultiMonth ? "</div>" + ((numMonths[0] > 0 && col == numMonths[1] - 1) ? '<div class="ui-datepicker-row-break"></div>' : "") : "");
                        group += calender
                    }
                    html += group
                }
                html += buttonPanel + (!inst.inline ? '<iframe src="javascript:false;" class="ui-datepicker-cover" frameborder="0"></iframe>' : "");
                inst._keyEvent = false;
                return html
            },
            _generateMonthYearHeader: function (inst, drawMonth, drawYear, minDate, maxDate, selectedDate, secondary, monthNames, monthNamesShort) {
                minDate = (inst.rangeStart && minDate && selectedDate < minDate ? selectedDate : minDate);
                var changeMonth = this._get(inst, "changeMonth");
                var changeYear = this._get(inst, "changeYear");
                var showMonthAfterYear = this._get(inst, "showMonthAfterYear");
                var html = '<div class="ui-datepicker-title">';
                var monthHtml = "";
                if (secondary || !changeMonth) {
                    monthHtml += '<span class="ui-datepicker-month">' + monthNames[drawMonth] + "</span> "
                } else {
                    var inMinYear = (minDate && minDate.getFullYear() == drawYear);
                    var inMaxYear = (maxDate && maxDate.getFullYear() == drawYear);
                    monthHtml += '<select class="ui-datepicker-month" onchange="DP_jQuery.datepicker._selectMonthYear(\'#' + inst.id + "', this, 'M');\" onclick=\"DP_jQuery.datepicker._clickMonthYear('#" + inst.id + "');\">";
                    for (var month = 0; month < 12; month++) {
                        if ((!inMinYear || month >= minDate.getMonth()) && (!inMaxYear || month <= maxDate.getMonth())) {
                            monthHtml += '<option value="' + month + '"' + (month == drawMonth ? ' selected="selected"' : "") + ">" + monthNamesShort[month] + "</option>"
                        }
                    }
                    monthHtml += "</select>"
                }
                if (!showMonthAfterYear) {
                    html += monthHtml + ((secondary || changeMonth || changeYear) && (!(changeMonth && changeYear)) ? "&#xa0;" : "")
                }
                if (secondary || !changeYear) {
                    html += '<span class="ui-datepicker-year">' + drawYear + "</span>"
                } else {
                    var years = this._get(inst, "yearRange").split(":");
                    var year = 0;
                    var endYear = 0;
                    if (years.length != 2) {
                        year = drawYear - 10;
                        endYear = drawYear + 10
                    } else {
                        if (years[0].charAt(0) == "+" || years[0].charAt(0) == "-") {
                            year = drawYear + parseInt(years[0], 10);
                            endYear = drawYear + parseInt(years[1], 10)
                        } else {
                            year = parseInt(years[0], 10);
                            endYear = parseInt(years[1], 10)
                        }
                    }
                    year = (minDate ? Math.max(year, minDate.getFullYear()) : year);
                    endYear = (maxDate ? Math.min(endYear, maxDate.getFullYear()) : endYear);
                    html += '<select class="ui-datepicker-year" onchange="DP_jQuery.datepicker._selectMonthYear(\'#' + inst.id + "', this, 'Y');\" onclick=\"DP_jQuery.datepicker._clickMonthYear('#" + inst.id + "');\">";
                    for (; year <= endYear; year++) {
                        html += '<option value="' + year + '"' + (year == drawYear ? ' selected="selected"' : "") + ">" + year + "</option>"
                    }
                    html += "</select>"
                }
                if (showMonthAfterYear) {
                    html += (secondary || changeMonth || changeYear ? "&#xa0;" : "") + monthHtml
                }
                html += "</div>";
                return html
            },
            _adjustInstDate: function (inst, offset, period) {
                var year = inst.drawYear + (period == "Y" ? offset : 0);
                var month = inst.drawMonth + (period == "M" ? offset : 0);
                var day = Math.min(inst.selectedDay, this._getDaysInMonth(year, month)) + (period == "D" ? offset : 0);
                var date = this._daylightSavingAdjust(new Date(year, month, day));
                var minDate = this._getMinMaxDate(inst, "min", true);
                var maxDate = this._getMinMaxDate(inst, "max");
                date = (minDate && date < minDate ? minDate : date);
                date = (maxDate && date > maxDate ? maxDate : date);
                inst.selectedDay = date.getDate();
                inst.drawMonth = inst.selectedMonth = date.getMonth();
                inst.drawYear = inst.selectedYear = date.getFullYear();
                if (period == "M" || period == "Y") {
                    this._notifyChange(inst)
                }
            },
            _notifyChange: function (inst) {
                var onChange = this._get(inst, "onChangeMonthYear");
                if (onChange) {
                    onChange.apply((inst.input ? inst.input[0] : null), [inst.selectedYear, inst.selectedMonth + 1, inst])
                }
            },
            _getNumberOfMonths: function (inst) {
                var numMonths = this._get(inst, "numberOfMonths");
                return (numMonths == null ? [1, 1] : (typeof numMonths == "number" ? [1, numMonths] : numMonths))
            },
            _getMinMaxDate: function (inst, minMax, checkRange) {
                var date = this._determineDate(this._get(inst, minMax + "Date"), null);
                return (!checkRange || !inst.rangeStart ? date : (!date || inst.rangeStart > date ? inst.rangeStart : date))
            },
            _getDaysInMonth: function (year, month) {
                return 32 - new Date(year, month, 32).getDate()
            },
            _getFirstDayOfMonth: function (year, month) {
                return new Date(year, month, 1).getDay()
            },
            _canAdjustMonth: function (inst, offset, curYear, curMonth) {
                var numMonths = this._getNumberOfMonths(inst);
                var date = this._daylightSavingAdjust(new Date(curYear, curMonth + (offset < 0 ? offset : numMonths[1]), 1));
                if (offset < 0) {
                    date.setDate(this._getDaysInMonth(date.getFullYear(), date.getMonth()))
                }
                return this._isInRange(inst, date)
            },
            _isInRange: function (inst, date) {
                var newMinDate = (!inst.rangeStart ? null : this._daylightSavingAdjust(new Date(inst.selectedYear, inst.selectedMonth, inst.selectedDay)));
                newMinDate = (newMinDate && inst.rangeStart < newMinDate ? inst.rangeStart : newMinDate);
                var minDate = newMinDate || this._getMinMaxDate(inst, "min");
                var maxDate = this._getMinMaxDate(inst, "max");
                return ((!minDate || date >= minDate) && (!maxDate || date <= maxDate))
            },
            _getFormatConfig: function (inst) {
                var shortYearCutoff = this._get(inst, "shortYearCutoff");
                shortYearCutoff = (typeof shortYearCutoff != "string" ? shortYearCutoff : new Date().getFullYear() % 100 + parseInt(shortYearCutoff, 10));
                return {
                    shortYearCutoff: shortYearCutoff,
                    dayNamesShort: this._get(inst, "dayNamesShort"),
                    dayNames: this._get(inst, "dayNames"),
                    monthNamesShort: this._get(inst, "monthNamesShort"),
                    monthNames: this._get(inst, "monthNames")
                }
            },
            _formatDate: function (inst, day, month, year) {
                if (!day) {
                    inst.currentDay = inst.selectedDay;
                    inst.currentMonth = inst.selectedMonth;
                    inst.currentYear = inst.selectedYear
                }
                var date = (day ? (typeof day == "object" ? day : this._daylightSavingAdjust(new Date(year, month, day))) : this._daylightSavingAdjust(new Date(inst.currentYear, inst.currentMonth, inst.currentDay)));
                return this.formatDate(this._get(inst, "dateFormat"), date, this._getFormatConfig(inst))
            }
        });

        function extendRemove(target, props) {
            $.extend(target, props);
            for (var name in props) {
                if (props[name] == null || props[name] == undefined) {
                    target[name] = props[name]
                }
            }
            return target
        }

        function isArray(a) {
            return (a && ((typeof a == "object" && a.length) || (a.constructor && a.constructor.toString().match(/\Array\(\)/))))
        }

        $.fn.datepicker = function (options) {
            if (!$.datepicker.initialized) {
                $(document).mousedown($.datepicker._checkExternalClick).find("body").append($.datepicker.dpDiv);
                $.datepicker.initialized = true
            }
            var otherArgs = Array.prototype.slice.call(arguments, 1);
            if (typeof options == "string" && (options == "isDisabled" || options == "getDate")) {
                return $.datepicker["_" + options + "Datepicker"].apply($.datepicker, [this[0]].concat(otherArgs))
            }
            return this.each(function () {
                typeof options == "string" ? $.datepicker["_" + options + "Datepicker"].apply($.datepicker, [this].concat(otherArgs)) : $.datepicker._attachDatepicker(this, options)
            })
        };
        $.datepicker = new Datepicker();
        $.datepicker.initialized = false;
        $.datepicker.uuid = new Date().getTime();
        $.datepicker.version = "1.7.1";
        window.DP_jQuery = $
    })(jQuery);
    ;
    /***/
    /* --------------------------------------------------------------------
     * jQuery-Plugin "daterangepicker.jQuery.js"
     * by Scott Jehl, scott@filamentgroup.com
     * http://www.filamentgroup.com
     * reference article: http://www.filamentgroup.com/lab/update_date_range_picker_with_jquery_ui/
     * demo page: http://www.filamentgroup.com/examples/daterangepicker/
     * 
     * Copyright (c) 2008 Filament Group, Inc
     * Dual licensed under the MIT (filamentgroup.com/examples/mit-license.txt) and GPL (filamentgroup.com/examples/gpl-license.txt) licenses.
     *
     * Dependencies: jquery, jquery UI datepicker, date.js library (included at bottom), jQuery UI CSS Framework
     * Changelog:
     * 	10.23.2008 initial Version
     *  11.12.2008 changed dateFormat option to allow custom date formatting (credit: http://alexgoldstone.com/)
     *  01.04.09 updated markup to new jQuery UI CSS Framework
     *  01.19.2008 changed presets hash to support different text 
     * --------------------------------------------------------------------
     */
    jQuery.fn.daterangepicker = function (settings) {
        var rangeInput = jQuery(this);

        //defaults
        var options = jQuery.extend({
            presetRanges: [
                {text: 'Today', dateStart: 'today', dateEnd: 'today'},
                {text: 'Last 7 days', dateStart: 'today-7days', dateEnd: 'today'},
                {
                    text: 'Month to date', dateStart: function () {
                    return Date.parse('today').moveToFirstDayOfMonth();
                }, dateEnd: 'today'
                },
                {
                    text: 'Year to date', dateStart: function () {
                    var x = Date.parse('today');
                    x.setMonth(0);
                    x.setDate(1);
                    return x;
                }, dateEnd: 'today'
                },
                //extras:
                {
                    text: 'The previous Month', dateStart: function () {
                    return Date.parse('1 month ago').moveToFirstDayOfMonth();
                }, dateEnd: function () {
                    return Date.parse('1 month ago').moveToLastDayOfMonth();
                }
                }
                //{text: 'Tomorrow', dateStart: 'Tomorrow', dateEnd: 'Tomorrow' },
                //{text: 'Ad Campaign', dateStart: '03/07/08', dateEnd: 'Today' },
                //{text: 'Last 30 Days', dateStart: 'Today-30', dateEnd: 'Today' },
                //{text: 'Next 30 Days', dateStart: 'Today', dateEnd: 'Today+30' },
                //{text: 'Our Ad Campaign', dateStart: '03/07/08', dateEnd: '07/08/08' }
            ],
            //presetRanges: array of objects for each menu preset. 
            //Each obj must have text, dateStart, dateEnd. dateStart, dateEnd accept date.js string or a function which returns a date object
            presets: {
                specificDate: 'Specific Date',
                allDatesBefore: 'All Dates Before',
                allDatesAfter: 'All Dates After',
                dateRange: 'Date Range'
            },
            rangeStartTitle: 'Start date',
            rangeEndTitle: 'End date',
            nextLinkText: 'Next',
            prevLinkText: 'Prev',
            doneButtonText: 'Done',
            cancelButtonText: '取消',
            earliestDate: Date.parse('-15years'), //earliest date allowed 
            latestDate: Date.parse('+15years'), //latest date allowed 
            rangeSplitter: '-', //string to use between dates in single input
            dateFormat: 'm/d/yy', // date formatting. Available formats: http://docs.jquery.com/UI/Datepicker/%24.datepicker.formatDate
            closeOnSelect: false, //if a complete selection is made, close the menu
            arrows: false,
            /**
             * 2016-04-12 ch add start
             */
            startDateDom: '',
            endDateDom: '',
            /**
             * 2016-04-12 ch add end
             */
            posX: 0, // x position
            posY: rangeInput.outerHeight(), // y position
            appendTo: rangeInput.parent(),
            onClose: function () {
            },
            onOpen: function () {
            },
            onChange: function () {
            },
            datepickerOptions: null //object containing native UI datepicker API options
        }, settings);


        //custom datepicker options, extended by options
        var datepickerOptions = {
            onSelect: function () {
                if (rp.find('.ui-daterangepicker-specificDate').is('.ui-state-active')) {
                    rp.find('.range-end').datepicker('setDate', rp.find('.range-start').datepicker('getDate'));
                }
                var rangeA = fDate(rp.find('.range-start').datepicker('getDate'));
                var rangeB = fDate(rp.find('.range-end').datepicker('getDate'));

                // 2016-04-19 wry
                if(rangeA > rangeB) {
                    rangeB = '';
                }

                //send back to input or inputs
                if (rangeInput.length == 2) {
                    rangeInput.eq(0).val(rangeA);
                    rangeInput.eq(1).val(rangeB);
                } else {
                    rangeInput.val(rangeA + (rangeB ? ' ' + options.rangeSplitter + ' ' + rangeB : ''));
                }

                /**
                 * 2016-04-12 ch add start
                 */
                if (options.startDateDom) {
                    rangeInput.siblings(options.startDateDom).val(rangeA);
                }
                if (options.endDateDom) {
                    rangeInput.siblings(options.endDateDom).val(rangeB);
                }
                /**
                 * 2016-04-12 ch add end
                 */

                //if closeOnSelect is true
                if (options.closeOnSelect) {
                    if (!rp.find('li.ui-state-active').is('.ui-daterangepicker-dateRange') && !rp.is(':animated')) {
                        hideRP();
                    }
                }
                options.onChange();
            },
            defaultDate: +0
        };

        //change event fires both when a calendar is updated or a change event on the input is triggered
        rangeInput.change(options.onChange);


        //datepicker options from options
        options.datepickerOptions = (settings) ? jQuery.extend(datepickerOptions, settings.datepickerOptions) : datepickerOptions;

        //Capture Dates from input(s)
        var inputDateA, inputDateB = Date.parse('today');
        var inputDateAtemp, inputDateBtemp;
        if (rangeInput.size() == 2) {
            inputDateAtemp = Date.parse(rangeInput.eq(0).val());
            inputDateBtemp = Date.parse(rangeInput.eq(1).val());
            if (inputDateAtemp == null) {
                inputDateAtemp = inputDateBtemp;
            }
            if (inputDateBtemp == null) {
                inputDateBtemp = inputDateAtemp;
            }
        }
        else {
            inputDateAtemp = Date.parse(rangeInput.val().split(options.rangeSplitter)[0]);
            inputDateBtemp = Date.parse(rangeInput.val().split(options.rangeSplitter)[1]);
            if (inputDateBtemp == null) {
                inputDateBtemp = inputDateAtemp;
            } //if one date, set both
        }
        if (inputDateAtemp != null) {
            inputDateA = inputDateAtemp;
        }
        if (inputDateBtemp != null) {
            inputDateB = inputDateBtemp;
        }


        //build picker and 
        var rp = jQuery('<div class="ui-daterangepicker ui-widget ui-helper-clearfix ui-widget-content ui-corner-all"></div>');
        var rpPresets = (function () {
            var ul = jQuery('<ul class="ui-widget-content"></ul>').appendTo(rp);
            jQuery.each(options.presetRanges, function () {
                jQuery('<li class="ui-daterangepicker-' + this.text.replace(/ /g, '') + ' ui-corner-all"><a href="#">' + this.text + '</a></li>')
                    .data('dateStart', this.dateStart)
                    .data('dateEnd', this.dateEnd)
                    .appendTo(ul);
            });
            var x = 0;
            jQuery.each(options.presets, function (key, value) {
                jQuery('<li class="ui-daterangepicker-' + key + ' preset_' + x + ' ui-helper-clearfix ui-corner-all"><span class="ui-icon ui-icon-triangle-1-e"></span><a href="#">' + value + '</a></li>')
                    .appendTo(ul);
                x++;
            });

            ul.find('li').hover(
                function () {
                    jQuery(this).addClass('ui-state-hover');
                },
                function () {
                    jQuery(this).removeClass('ui-state-hover');
                })
                .click(function () {
                    rp.find('.ui-state-active').removeClass('ui-state-active');
                    jQuery(this).addClass('ui-state-active').clickActions(rp, rpPickers, doneBtn);
                    return false;
                });
            return ul;
        })();

        //function to format a date string        
        function fDate(date) {
            if (!date || !date.getDate()) {
                date = new Date();
            }
            var day = date.getDate();
            var month = date.getMonth();
            var year = date.getFullYear();
            month++; // adjust javascript month
            var dateFormat = options.dateFormat;
            return jQuery.datepicker.formatDate(dateFormat, date);
        }


        jQuery.fn.restoreDateFromData = function () {
            if (jQuery(this).data('saveDate')) {
                jQuery(this).datepicker('setDate', jQuery(this).data('saveDate')).removeData('saveDate');
            }
            return this;
        }
        jQuery.fn.saveDateToData = function () {
            if (!jQuery(this).data('saveDate')) {
                jQuery(this).data('saveDate', jQuery(this).datepicker('getDate'));
            }
            return this;
        }

        //show, hide, or toggle rangepicker
        function showRP() {
            if (rp.data('state') == 'closed') {
                rp.data('state', 'open');
                rp.show();
                options.onOpen();
            }
        }

        function hideRP() {
            if (rp.data('state') == 'open') {
                rp.data('state', 'closed');
                rp.hide();
                options.onClose();
            }
        }

        function toggleRP() {
            if (rp.data('state') == 'open') {
                hideRP();
            }
            else {
                showRP();
            }
        }

        rp.data('state', 'closed');

        //preset menu click events	
        jQuery.fn.clickActions = function (rp, rpPickers, doneBtn) {
            if (jQuery(this).is('.ui-daterangepicker-specificDate')) {
                doneBtn.hide();
                rpPickers.show();
                rp.find('.title-start').text(options.presets.specificDate);
                rp.find('.range-start').restoreDateFromData().show();
                rp.find('.range-end').restoreDateFromData().hide();
                doneBtn.show();
                // setTimeout(function(){doneBtn.fadeIn();}, 400);
            }
            else if (jQuery(this).is('.ui-daterangepicker-allDatesBefore')) {
                doneBtn.hide();
                rpPickers.show();
                rp.find('.title-end').text(options.presets.allDatesBefore);
                rp.find('.range-start').saveDateToData().datepicker('setDate', options.earliestDate).hide();
                rp.find('.range-end').restoreDateFromData().show();
                doneBtn.show();
                // setTimeout(function(){doneBtn.fadeIn();}, 400);
            }
            else if (jQuery(this).is('.ui-daterangepicker-allDatesAfter')) {
                doneBtn.hide();
                rpPickers.show();
                rp.find('.title-start').text(options.presets.allDatesAfter);
                rp.find('.range-start').restoreDateFromData().show();
                rp.find('.range-end').saveDateToData().datepicker('setDate', options.latestDate).hide();
                doneBtn.show();
                // setTimeout(function(){doneBtn.fadeIn();}, 400);
            }
            else if (jQuery(this).is('.ui-daterangepicker-dateRange')) {
                doneBtn.hide();
                rpPickers.show();
                rp.find('.title-start').text(options.rangeStartTitle);
                rp.find('.title-end').text(options.rangeEndTitle);
                rp.find('.range-start').restoreDateFromData().show();
                rp.find('.range-end').restoreDateFromData().show();
                doneBtn.show();
                // setTimeout(function(){doneBtn.fadeIn();}, 400);
            }
            else {
                //custom date range
                doneBtn.hide();
                rp.find('.range-start, .range-end').hide(0, function () {
                    rpPickers.hide();
                });
                var dateStart = (typeof jQuery(this).data('dateStart') == 'string') ? Date.parse(jQuery(this).data('dateStart')) : jQuery(this).data('dateStart')();
                var dateEnd = (typeof jQuery(this).data('dateEnd') == 'string') ? Date.parse(jQuery(this).data('dateEnd')) : jQuery(this).data('dateEnd')();
                rp.find('.range-start').datepicker('setDate', dateStart).find('.ui-datepicker-current-day').trigger('click');
                rp.find('.range-end').datepicker('setDate', dateEnd).find('.ui-datepicker-current-day').trigger('click');
            }

            return false;
        }


        //picker divs
        var rpPickers = jQuery('<div class="ranges ui-widget-header ui-corner-all ui-helper-clearfix"><div class="range-start"><span class="title-start">Start Date</span></div><div class="range-end"><span class="title-end">End Date</span></div></div>').appendTo(rp);
        rpPickers.find('.range-start, .range-end').datepicker(options.datepickerOptions);
        rpPickers.find('.range-start').datepicker('setDate', inputDateA);
        rpPickers.find('.range-end').datepicker('setDate', inputDateB);
        var doneBtn = jQuery('<button class="btnDone ui-state-default ui-corner-all">' + options.doneButtonText + '</button>')
            .click(function () {
                rp.find('.ui-datepicker-current-day').trigger('click');
                hideRP();
            })
            .hover(
            function () {
                jQuery(this).addClass('ui-state-hover');
            },
            function () {
                jQuery(this).removeClass('ui-state-hover');
            }
        )
            .appendTo(rpPickers);


        //inputs toggle rangepicker visibility
        jQuery(this).click(function () {
            toggleRP();
            return false;
        });
        //hide em all
        rpPickers.css('display', 'none').find('.range-start, .range-end, .btnDone').css('display', 'none');

        //inject rp
        jQuery(options.appendTo).append(rp);

        //wrap and position
        rp.wrap('<div class="ui-daterangepickercontain"></div>');
        if (options.posX) {
            rp.parent().css('left', options.posX);
        }
        if (options.posY) {
            rp.parent().css('top', options.posY);
        }

        //add arrows (only available on one input)
        if (options.arrows && rangeInput.size() == 1) {
            var prevLink = jQuery('<a href="#" class="ui-daterangepicker-prev ui-corner-all" title="' + options.prevLinkText + '"><span class="ui-icon ui-icon-circle-triangle-w">' + options.prevLinkText + '</span></a>');
            var nextLink = jQuery('<a href="#" class="ui-daterangepicker-next ui-corner-all" title="' + options.nextLinkText + '"><span class="ui-icon ui-icon-circle-triangle-e">' + options.nextLinkText + '</span></a>');
            jQuery(this)
                .addClass('ui-rangepicker-input ui-widget-content')
                .wrap('<div class="ui-daterangepicker-arrows ui-widget ui-widget-header ui-helper-clearfix ui-corner-all"></div>')
                .before(prevLink)
                .before(nextLink)
                .parent().find('a').click(function () {
                    var dateA = rpPickers.find('.range-start').datepicker('getDate');
                    var dateB = rpPickers.find('.range-end').datepicker('getDate');
                    var diff = Math.abs(new TimeSpan(dateA - dateB).getTotalMilliseconds()) + 86400000; //difference plus one day
                    if (jQuery(this).is('.ui-daterangepicker-prev')) {
                        diff = -diff;
                    }

                    rpPickers.find('.range-start, .range-end ').each(function () {
                        var thisDate = jQuery(this).datepicker("getDate");
                        if (thisDate == null) {
                            return false;
                        }
                        jQuery(this).datepicker("setDate", thisDate.add({milliseconds: diff})).find('.ui-datepicker-current-day').trigger('click');
                    });

                    return false;
                })
                .hover(
                function () {
                    jQuery(this).addClass('ui-state-hover');
                },
                function () {
                    jQuery(this).removeClass('ui-state-hover');
                })
            ;
        }


        jQuery(document).click(function () {
            if (rp.is(':visible')) {
                hideRP();
            }
        });


        rp.click(function () {
            return false;
        }).hide();
        return this;
    }


    /**
     * Version: 1.0 Alpha-1
     * Build Date: 13-Nov-2007
     * Copyright (c) 2006-2007, Coolite Inc. (http://www.coolite.com/). All rights reserved.
     * License: Licensed under The MIT License. See license.txt and http://www.datejs.com/license/.
     * Website: http://www.datejs.com/ or http://www.coolite.com/datejs/
     */
    Date.CultureInfo = {
        name: "en-US",
        englishName: "English (United States)",
        nativeName: "English (United States)",
        dayNames: ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
        abbreviatedDayNames: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
        shortestDayNames: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
        firstLetterDayNames: ["S", "M", "T", "W", "T", "F", "S"],
        monthNames: ["Ò»ÔÂ", "¶þÔÂ", "ÈýÔÂ", "ËÄÔÂ", "ÎåÔÂ", "ÁùÔÂ", "ÆßÔÂ", "°ËÔÂ", "¾ÅÔÂ", "Ê®ÔÂ", "Ê®Ò»ÔÂ", "Ê®¶þÔÂ"],
        abbreviatedMonthNames: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        amDesignator: "AM",
        pmDesignator: "PM",
        firstDayOfWeek: 0,
        twoDigitYearMax: 2029,
        dateElementOrder: "mdy",
        formatPatterns: {
            shortDate: "M/d/yyyy",
            longDate: "dddd, MMMM dd, yyyy",
            shortTime: "h:mm tt",
            longTime: "h:mm:ss tt",
            fullDateTime: "dddd, MMMM dd, yyyy h:mm:ss tt",
            sortableDateTime: "yyyy-MM-ddTHH:mm:ss",
            universalSortableDateTime: "yyyy-MM-dd HH:mm:ssZ",
            rfc1123: "ddd, dd MMM yyyy HH:mm:ss GMT",
            monthDay: "MMMM dd",
            yearMonth: "MMMM, yyyy"
        },
        regexPatterns: {
            jan: /^jan(uary)?/i,
            feb: /^feb(ruary)?/i,
            mar: /^mar(ch)?/i,
            apr: /^apr(il)?/i,
            may: /^may/i,
            jun: /^jun(e)?/i,
            jul: /^jul(y)?/i,
            aug: /^aug(ust)?/i,
            sep: /^sep(t(ember)?)?/i,
            oct: /^oct(ober)?/i,
            nov: /^nov(ember)?/i,
            dec: /^dec(ember)?/i,
            sun: /^su(n(day)?)?/i,
            mon: /^mo(n(day)?)?/i,
            tue: /^tu(e(s(day)?)?)?/i,
            wed: /^we(d(nesday)?)?/i,
            thu: /^th(u(r(s(day)?)?)?)?/i,
            fri: /^fr(i(day)?)?/i,
            sat: /^sa(t(urday)?)?/i,
            future: /^next/i,
            past: /^last|past|prev(ious)?/i,
            add: /^(\+|after|from)/i,
            subtract: /^(\-|before|ago)/i,
            yesterday: /^yesterday/i,
            today: /^t(oday)?/i,
            tomorrow: /^tomorrow/i,
            now: /^n(ow)?/i,
            millisecond: /^ms|milli(second)?s?/i,
            second: /^sec(ond)?s?/i,
            minute: /^min(ute)?s?/i,
            hour: /^h(ou)?rs?/i,
            week: /^w(ee)?k/i,
            month: /^m(o(nth)?s?)?/i,
            day: /^d(ays?)?/i,
            year: /^y((ea)?rs?)?/i,
            shortMeridian: /^(a|p)/i,
            longMeridian: /^(a\.?m?\.?|p\.?m?\.?)/i,
            timezone: /^((e(s|d)t|c(s|d)t|m(s|d)t|p(s|d)t)|((gmt)?\s*(\+|\-)\s*\d\d\d\d?)|gmt)/i,
            ordinalSuffix: /^\s*(st|nd|rd|th)/i,
            timeContext: /^\s*(\:|a|p)/i
        },
        abbreviatedTimeZoneStandard: {GMT: "-000", EST: "-0400", CST: "-0500", MST: "-0600", PST: "-0700"},
        abbreviatedTimeZoneDST: {GMT: "-000", EDT: "-0500", CDT: "-0600", MDT: "-0700", PDT: "-0800"}
    };
    Date.getMonthNumberFromName = function (name) {
        var n = Date.CultureInfo.monthNames, m = Date.CultureInfo.abbreviatedMonthNames, s = name.toLowerCase();
        for (var i = 0; i < n.length; i++) {
            if (n[i].toLowerCase() == s || m[i].toLowerCase() == s) {
                return i;
            }
        }
        return -1;
    };
    Date.getDayNumberFromName = function (name) {
        var n = Date.CultureInfo.dayNames, m = Date.CultureInfo.abbreviatedDayNames, o = Date.CultureInfo.shortestDayNames, s = name.toLowerCase();
        for (var i = 0; i < n.length; i++) {
            if (n[i].toLowerCase() == s || m[i].toLowerCase() == s) {
                return i;
            }
        }
        return -1;
    };
    Date.isLeapYear = function (year) {
        return (((year % 4 === 0) && (year % 100 !== 0)) || (year % 400 === 0));
    };
    Date.getDaysInMonth = function (year, month) {
        return [31, (Date.isLeapYear(year) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][month];
    };
    Date.getTimezoneOffset = function (s, dst) {
        return (dst || false) ? Date.CultureInfo.abbreviatedTimeZoneDST[s.toUpperCase()] : Date.CultureInfo.abbreviatedTimeZoneStandard[s.toUpperCase()];
    };
    Date.getTimezoneAbbreviation = function (offset, dst) {
        var n = (dst || false) ? Date.CultureInfo.abbreviatedTimeZoneDST : Date.CultureInfo.abbreviatedTimeZoneStandard, p;
        for (p in n) {
            if (n[p] === offset) {
                return p;
            }
        }
        return null;
    };
    Date.prototype.clone = function () {
        return new Date(this.getTime());
    };
    Date.prototype.compareTo = function (date) {
        if (isNaN(this)) {
            throw new Error(this);
        }
        if (date instanceof Date && !isNaN(date)) {
            return (this > date) ? 1 : (this < date) ? -1 : 0;
        } else {
            throw new TypeError(date);
        }
    };
    Date.prototype.equals = function (date) {
        return (this.compareTo(date) === 0);
    };
    Date.prototype.between = function (start, end) {
        var t = this.getTime();
        return t >= start.getTime() && t <= end.getTime();
    };
    Date.prototype.addMilliseconds = function (value) {
        this.setMilliseconds(this.getMilliseconds() + value);
        return this;
    };
    Date.prototype.addSeconds = function (value) {
        return this.addMilliseconds(value * 1000);
    };
    Date.prototype.addMinutes = function (value) {
        return this.addMilliseconds(value * 60000);
    };
    Date.prototype.addHours = function (value) {
        return this.addMilliseconds(value * 3600000);
    };
    Date.prototype.addDays = function (value) {
        return this.addMilliseconds(value * 86400000);
    };
    Date.prototype.addWeeks = function (value) {
        return this.addMilliseconds(value * 604800000);
    };
    Date.prototype.addMonths = function (value) {
        var n = this.getDate();
        this.setDate(1);
        this.setMonth(this.getMonth() + value);
        this.setDate(Math.min(n, this.getDaysInMonth()));
        return this;
    };
    Date.prototype.addYears = function (value) {
        return this.addMonths(value * 12);
    };
    Date.prototype.add = function (config) {
        if (typeof config == "number") {
            this._orient = config;
            return this;
        }
        var x = config;
        if (x.millisecond || x.milliseconds) {
            this.addMilliseconds(x.millisecond || x.milliseconds);
        }
        if (x.second || x.seconds) {
            this.addSeconds(x.second || x.seconds);
        }
        if (x.minute || x.minutes) {
            this.addMinutes(x.minute || x.minutes);
        }
        if (x.hour || x.hours) {
            this.addHours(x.hour || x.hours);
        }
        if (x.month || x.months) {
            this.addMonths(x.month || x.months);
        }
        if (x.year || x.years) {
            this.addYears(x.year || x.years);
        }
        if (x.day || x.days) {
            this.addDays(x.day || x.days);
        }
        return this;
    };
    Date._validate = function (value, min, max, name) {
        if (typeof value != "number") {
            throw new TypeError(value + " is not a Number.");
        } else if (value < min || value > max) {
            throw new RangeError(value + " is not a valid value for " + name + ".");
        }
        return true;
    };
    Date.validateMillisecond = function (n) {
        return Date._validate(n, 0, 999, "milliseconds");
    };
    Date.validateSecond = function (n) {
        return Date._validate(n, 0, 59, "seconds");
    };
    Date.validateMinute = function (n) {
        return Date._validate(n, 0, 59, "minutes");
    };
    Date.validateHour = function (n) {
        return Date._validate(n, 0, 23, "hours");
    };
    Date.validateDay = function (n, year, month) {
        return Date._validate(n, 1, Date.getDaysInMonth(year, month), "days");
    };
    Date.validateMonth = function (n) {
        return Date._validate(n, 0, 11, "months");
    };
    Date.validateYear = function (n) {
        return Date._validate(n, 1, 9999, "seconds");
    };
    Date.prototype.set = function (config) {
        var x = config;
        if (!x.millisecond && x.millisecond !== 0) {
            x.millisecond = -1;
        }
        if (!x.second && x.second !== 0) {
            x.second = -1;
        }
        if (!x.minute && x.minute !== 0) {
            x.minute = -1;
        }
        if (!x.hour && x.hour !== 0) {
            x.hour = -1;
        }
        if (!x.day && x.day !== 0) {
            x.day = -1;
        }
        if (!x.month && x.month !== 0) {
            x.month = -1;
        }
        if (!x.year && x.year !== 0) {
            x.year = -1;
        }
        if (x.millisecond != -1 && Date.validateMillisecond(x.millisecond)) {
            this.addMilliseconds(x.millisecond - this.getMilliseconds());
        }
        if (x.second != -1 && Date.validateSecond(x.second)) {
            this.addSeconds(x.second - this.getSeconds());
        }
        if (x.minute != -1 && Date.validateMinute(x.minute)) {
            this.addMinutes(x.minute - this.getMinutes());
        }
        if (x.hour != -1 && Date.validateHour(x.hour)) {
            this.addHours(x.hour - this.getHours());
        }
        if (x.month !== -1 && Date.validateMonth(x.month)) {
            this.addMonths(x.month - this.getMonth());
        }
        if (x.year != -1 && Date.validateYear(x.year)) {
            this.addYears(x.year - this.getFullYear());
        }
        if (x.day != -1 && Date.validateDay(x.day, this.getFullYear(), this.getMonth())) {
            this.addDays(x.day - this.getDate());
        }
        if (x.timezone) {
            this.setTimezone(x.timezone);
        }
        if (x.timezoneOffset) {
            this.setTimezoneOffset(x.timezoneOffset);
        }
        return this;
    };
    Date.prototype.clearTime = function () {
        this.setHours(0);
        this.setMinutes(0);
        this.setSeconds(0);
        this.setMilliseconds(0);
        return this;
    };
    Date.prototype.isLeapYear = function () {
        var y = this.getFullYear();
        return (((y % 4 === 0) && (y % 100 !== 0)) || (y % 400 === 0));
    };
    Date.prototype.isWeekday = function () {
        return !(this.is().sat() || this.is().sun());
    };
    Date.prototype.getDaysInMonth = function () {
        return Date.getDaysInMonth(this.getFullYear(), this.getMonth());
    };
    Date.prototype.moveToFirstDayOfMonth = function () {
        return this.set({day: 1});
    };
    Date.prototype.moveToLastDayOfMonth = function () {
        return this.set({day: this.getDaysInMonth()});
    };
    Date.prototype.moveToDayOfWeek = function (day, orient) {
        var diff = (day - this.getDay() + 7 * (orient || +1)) % 7;
        return this.addDays((diff === 0) ? diff += 7 * (orient || +1) : diff);
    };
    Date.prototype.moveToMonth = function (month, orient) {
        var diff = (month - this.getMonth() + 12 * (orient || +1)) % 12;
        return this.addMonths((diff === 0) ? diff += 12 * (orient || +1) : diff);
    };
    Date.prototype.getDayOfYear = function () {
        return Math.floor((this - new Date(this.getFullYear(), 0, 1)) / 86400000);
    };
    Date.prototype.getWeekOfYear = function (firstDayOfWeek) {
        var y = this.getFullYear(), m = this.getMonth(), d = this.getDate();
        var dow = firstDayOfWeek || Date.CultureInfo.firstDayOfWeek;
        var offset = 7 + 1 - new Date(y, 0, 1).getDay();
        if (offset == 8) {
            offset = 1;
        }
        var daynum = ((Date.UTC(y, m, d, 0, 0, 0) - Date.UTC(y, 0, 1, 0, 0, 0)) / 86400000) + 1;
        var w = Math.floor((daynum - offset + 7) / 7);
        if (w === dow) {
            y--;
            var prevOffset = 7 + 1 - new Date(y, 0, 1).getDay();
            if (prevOffset == 2 || prevOffset == 8) {
                w = 53;
            } else {
                w = 52;
            }
        }
        return w;
    };
    Date.prototype.isDST = function () {
        return this.toString().match(/(E|C|M|P)(S|D)T/)[2] == "D";
    };
    Date.prototype.getTimezone = function () {
        return Date.getTimezoneAbbreviation(this.getUTCOffset, this.isDST());
    };
    Date.prototype.setTimezoneOffset = function (s) {
        var here = this.getTimezoneOffset(), there = Number(s) * -6 / 10;
        this.addMinutes(there - here);
        return this;
    };
    Date.prototype.setTimezone = function (s) {
        return this.setTimezoneOffset(Date.getTimezoneOffset(s));
    };
    Date.prototype.getUTCOffset = function () {
        var n = this.getTimezoneOffset() * -10 / 6, r;
        if (n < 0) {
            r = (n - 10000).toString();
            return r[0] + r.substr(2);
        } else {
            r = (n + 10000).toString();
            return "+" + r.substr(1);
        }
    };
    Date.prototype.getDayName = function (abbrev) {
        return abbrev ? Date.CultureInfo.abbreviatedDayNames[this.getDay()] : Date.CultureInfo.dayNames[this.getDay()];
    };
    Date.prototype.getMonthName = function (abbrev) {
        return abbrev ? Date.CultureInfo.abbreviatedMonthNames[this.getMonth()] : Date.CultureInfo.monthNames[this.getMonth()];
    };
    Date.prototype._toString = Date.prototype.toString;
    Date.prototype.toString = function (format) {
        var self = this;
        var p = function p(s) {
            return (s.toString().length == 1) ? "0" + s : s;
        };
        return format ? format.replace(/dd?d?d?|MM?M?M?|yy?y?y?|hh?|HH?|mm?|ss?|tt?|zz?z?/g, function (format) {
            switch (format) {
                case"hh":
                    return p(self.getHours() < 13 ? self.getHours() : (self.getHours() - 12));
                case"h":
                    return self.getHours() < 13 ? self.getHours() : (self.getHours() - 12);
                case"HH":
                    return p(self.getHours());
                case"H":
                    return self.getHours();
                case"mm":
                    return p(self.getMinutes());
                case"m":
                    return self.getMinutes();
                case"ss":
                    return p(self.getSeconds());
                case"s":
                    return self.getSeconds();
                case"yyyy":
                    return self.getFullYear();
                case"yy":
                    return self.getFullYear().toString().substring(2, 4);
                case"dddd":
                    return self.getDayName();
                case"ddd":
                    return self.getDayName(true);
                case"dd":
                    return p(self.getDate());
                case"d":
                    return self.getDate().toString();
                case"MMMM":
                    return self.getMonthName();
                case"MMM":
                    return self.getMonthName(true);
                case"MM":
                    return p((self.getMonth() + 1));
                case"M":
                    return self.getMonth() + 1;
                case"t":
                    return self.getHours() < 12 ? Date.CultureInfo.amDesignator.substring(0, 1) : Date.CultureInfo.pmDesignator.substring(0, 1);
                case"tt":
                    return self.getHours() < 12 ? Date.CultureInfo.amDesignator : Date.CultureInfo.pmDesignator;
                case"zzz":
                case"zz":
                case"z":
                    return "";
            }
        }) : this._toString();
    };
    Date.now = function () {
        return new Date();
    };
    Date.today = function () {
        return Date.now().clearTime();
    };
    Date.prototype._orient = +1;
    Date.prototype.next = function () {
        this._orient = +1;
        return this;
    };
    Date.prototype.last = Date.prototype.prev = Date.prototype.previous = function () {
        this._orient = -1;
        return this;
    };
    Date.prototype._is = false;
    Date.prototype.is = function () {
        this._is = true;
        return this;
    };
    Number.prototype._dateElement = "day";
    Number.prototype.fromNow = function () {
        var c = {};
        c[this._dateElement] = this;
        return Date.now().add(c);
    };
    Number.prototype.ago = function () {
        var c = {};
        c[this._dateElement] = this * -1;
        return Date.now().add(c);
    };
    (function () {
        var $D = Date.prototype, $N = Number.prototype;
        var dx = ("sunday monday tuesday wednesday thursday friday saturday").split(/\s/), mx = ("january february march april may june july august september october november december").split(/\s/), px = ("Millisecond Second Minute Hour Day Week Month Year").split(/\s/), de;
        var df = function (n) {
            return function () {
                if (this._is) {
                    this._is = false;
                    return this.getDay() == n;
                }
                return this.moveToDayOfWeek(n, this._orient);
            };
        };
        for (var i = 0; i < dx.length; i++) {
            $D[dx[i]] = $D[dx[i].substring(0, 3)] = df(i);
        }
        var mf = function (n) {
            return function () {
                if (this._is) {
                    this._is = false;
                    return this.getMonth() === n;
                }
                return this.moveToMonth(n, this._orient);
            };
        };
        for (var j = 0; j < mx.length; j++) {
            $D[mx[j]] = $D[mx[j].substring(0, 3)] = mf(j);
        }
        var ef = function (j) {
            return function () {
                if (j.substring(j.length - 1) != "s") {
                    j += "s";
                }
                return this["add" + j](this._orient);
            };
        };
        var nf = function (n) {
            return function () {
                this._dateElement = n;
                return this;
            };
        };
        for (var k = 0; k < px.length; k++) {
            de = px[k].toLowerCase();
            $D[de] = $D[de + "s"] = ef(px[k]);
            $N[de] = $N[de + "s"] = nf(de);
        }
    }());
    Date.prototype.toJSONString = function () {
        return this.toString("yyyy-MM-ddThh:mm:ssZ");
    };
    Date.prototype.toShortDateString = function () {
        return this.toString(Date.CultureInfo.formatPatterns.shortDatePattern);
    };
    Date.prototype.toLongDateString = function () {
        return this.toString(Date.CultureInfo.formatPatterns.longDatePattern);
    };
    Date.prototype.toShortTimeString = function () {
        return this.toString(Date.CultureInfo.formatPatterns.shortTimePattern);
    };
    Date.prototype.toLongTimeString = function () {
        return this.toString(Date.CultureInfo.formatPatterns.longTimePattern);
    };
    Date.prototype.getOrdinal = function () {
        switch (this.getDate()) {
            case 1:
            case 21:
            case 31:
                return "st";
            case 2:
            case 22:
                return "nd";
            case 3:
            case 23:
                return "rd";
            default:
                return "th";
        }
    };
    (function () {
        Date.Parsing = {
            Exception: function (s) {
                this.message = "Parse error at '" + s.substring(0, 10) + " ...'";
            }
        };
        var $P = Date.Parsing;
        var _ = $P.Operators = {
            rtoken: function (r) {
                return function (s) {
                    var mx = s.match(r);
                    if (mx) {
                        return ([mx[0], s.substring(mx[0].length)]);
                    } else {
                        throw new $P.Exception(s);
                    }
                };
            }, token: function (s) {
                return function (s) {
                    return _.rtoken(new RegExp("^\s*" + s + "\s*"))(s);
                };
            }, stoken: function (s) {
                return _.rtoken(new RegExp("^" + s));
            }, until: function (p) {
                return function (s) {
                    var qx = [], rx = null;
                    while (s.length) {
                        try {
                            rx = p.call(this, s);
                        } catch (e) {
                            qx.push(rx[0]);
                            s = rx[1];
                            continue;
                        }
                        break;
                    }
                    return [qx, s];
                };
            }, many: function (p) {
                return function (s) {
                    var rx = [], r = null;
                    while (s.length) {
                        try {
                            r = p.call(this, s);
                        } catch (e) {
                            return [rx, s];
                        }
                        rx.push(r[0]);
                        s = r[1];
                    }
                    return [rx, s];
                };
            }, optional: function (p) {
                return function (s) {
                    var r = null;
                    try {
                        r = p.call(this, s);
                    } catch (e) {
                        return [null, s];
                    }
                    return [r[0], r[1]];
                };
            }, not: function (p) {
                return function (s) {
                    try {
                        p.call(this, s);
                    } catch (e) {
                        return [null, s];
                    }
                    throw new $P.Exception(s);
                };
            }, ignore: function (p) {
                return p ? function (s) {
                    var r = null;
                    r = p.call(this, s);
                    return [null, r[1]];
                } : null;
            }, product: function () {
                var px = arguments[0], qx = Array.prototype.slice.call(arguments, 1), rx = [];
                for (var i = 0; i < px.length; i++) {
                    rx.push(_.each(px[i], qx));
                }
                return rx;
            }, cache: function (rule) {
                var cache = {}, r = null;
                return function (s) {
                    try {
                        r = cache[s] = (cache[s] || rule.call(this, s));
                    } catch (e) {
                        r = cache[s] = e;
                    }
                    if (r instanceof $P.Exception) {
                        throw r;
                    } else {
                        return r;
                    }
                };
            }, any: function () {
                var px = arguments;
                return function (s) {
                    var r = null;
                    for (var i = 0; i < px.length; i++) {
                        if (px[i] == null) {
                            continue;
                        }
                        try {
                            r = (px[i].call(this, s));
                        } catch (e) {
                            r = null;
                        }
                        if (r) {
                            return r;
                        }
                    }
                    throw new $P.Exception(s);
                };
            }, each: function () {
                var px = arguments;
                return function (s) {
                    var rx = [], r = null;
                    for (var i = 0; i < px.length; i++) {
                        if (px[i] == null) {
                            continue;
                        }
                        try {
                            r = (px[i].call(this, s));
                        } catch (e) {
                            throw new $P.Exception(s);
                        }
                        rx.push(r[0]);
                        s = r[1];
                    }
                    return [rx, s];
                };
            }, all: function () {
                var px = arguments, _ = _;
                return _.each(_.optional(px));
            }, sequence: function (px, d, c) {
                d = d || _.rtoken(/^\s*/);
                c = c || null;
                if (px.length == 1) {
                    return px[0];
                }
                return function (s) {
                    var r = null, q = null;
                    var rx = [];
                    for (var i = 0; i < px.length; i++) {
                        try {
                            r = px[i].call(this, s);
                        } catch (e) {
                            break;
                        }
                        rx.push(r[0]);
                        try {
                            q = d.call(this, r[1]);
                        } catch (ex) {
                            q = null;
                            break;
                        }
                        s = q[1];
                    }
                    if (!r) {
                        throw new $P.Exception(s);
                    }
                    if (q) {
                        throw new $P.Exception(q[1]);
                    }
                    if (c) {
                        try {
                            r = c.call(this, r[1]);
                        } catch (ey) {
                            throw new $P.Exception(r[1]);
                        }
                    }
                    return [rx, (r ? r[1] : s)];
                };
            }, between: function (d1, p, d2) {
                d2 = d2 || d1;
                var _fn = _.each(_.ignore(d1), p, _.ignore(d2));
                return function (s) {
                    var rx = _fn.call(this, s);
                    return [[rx[0][0], r[0][2]], rx[1]];
                };
            }, list: function (p, d, c) {
                d = d || _.rtoken(/^\s*/);
                c = c || null;
                return (p instanceof Array ? _.each(_.product(p.slice(0, -1), _.ignore(d)), p.slice(-1), _.ignore(c)) : _.each(_.many(_.each(p, _.ignore(d))), px, _.ignore(c)));
            }, set: function (px, d, c) {
                d = d || _.rtoken(/^\s*/);
                c = c || null;
                return function (s) {
                    var r = null, p = null, q = null, rx = null, best = [[], s], last = false;
                    for (var i = 0; i < px.length; i++) {
                        q = null;
                        p = null;
                        r = null;
                        last = (px.length == 1);
                        try {
                            r = px[i].call(this, s);
                        } catch (e) {
                            continue;
                        }
                        rx = [[r[0]], r[1]];
                        if (r[1].length > 0 && !last) {
                            try {
                                q = d.call(this, r[1]);
                            } catch (ex) {
                                last = true;
                            }
                        } else {
                            last = true;
                        }
                        if (!last && q[1].length === 0) {
                            last = true;
                        }
                        if (!last) {
                            var qx = [];
                            for (var j = 0; j < px.length; j++) {
                                if (i != j) {
                                    qx.push(px[j]);
                                }
                            }
                            p = _.set(qx, d).call(this, q[1]);
                            if (p[0].length > 0) {
                                rx[0] = rx[0].concat(p[0]);
                                rx[1] = p[1];
                            }
                        }
                        if (rx[1].length < best[1].length) {
                            best = rx;
                        }
                        if (best[1].length === 0) {
                            break;
                        }
                    }
                    if (best[0].length === 0) {
                        return best;
                    }
                    if (c) {
                        try {
                            q = c.call(this, best[1]);
                        } catch (ey) {
                            throw new $P.Exception(best[1]);
                        }
                        best[1] = q[1];
                    }
                    return best;
                };
            }, forward: function (gr, fname) {
                return function (s) {
                    return gr[fname].call(this, s);
                };
            }, replace: function (rule, repl) {
                return function (s) {
                    var r = rule.call(this, s);
                    return [repl, r[1]];
                };
            }, process: function (rule, fn) {
                return function (s) {
                    var r = rule.call(this, s);
                    return [fn.call(this, r[0]), r[1]];
                };
            }, min: function (min, rule) {
                return function (s) {
                    var rx = rule.call(this, s);
                    if (rx[0].length < min) {
                        throw new $P.Exception(s);
                    }
                    return rx;
                };
            }
        };
        var _generator = function (op) {
            return function () {
                var args = null, rx = [];
                if (arguments.length > 1) {
                    args = Array.prototype.slice.call(arguments);
                } else if (arguments[0]instanceof Array) {
                    args = arguments[0];
                }
                if (args) {
                    for (var i = 0, px = args.shift(); i < px.length; i++) {
                        args.unshift(px[i]);
                        rx.push(op.apply(null, args));
                        args.shift();
                        return rx;
                    }
                } else {
                    return op.apply(null, arguments);
                }
            };
        };
        var gx = "optional not ignore cache".split(/\s/);
        for (var i = 0; i < gx.length; i++) {
            _[gx[i]] = _generator(_[gx[i]]);
        }
        var _vector = function (op) {
            return function () {
                if (arguments[0]instanceof Array) {
                    return op.apply(null, arguments[0]);
                } else {
                    return op.apply(null, arguments);
                }
            };
        };
        var vx = "each any all".split(/\s/);
        for (var j = 0; j < vx.length; j++) {
            _[vx[j]] = _vector(_[vx[j]]);
        }
    }());
    (function () {
        var flattenAndCompact = function (ax) {
            var rx = [];
            for (var i = 0; i < ax.length; i++) {
                if (ax[i]instanceof Array) {
                    rx = rx.concat(flattenAndCompact(ax[i]));
                } else {
                    if (ax[i]) {
                        rx.push(ax[i]);
                    }
                }
            }
            return rx;
        };
        Date.Grammar = {};
        Date.Translator = {
            hour: function (s) {
                return function () {
                    this.hour = Number(s);
                };
            }, minute: function (s) {
                return function () {
                    this.minute = Number(s);
                };
            }, second: function (s) {
                return function () {
                    this.second = Number(s);
                };
            }, meridian: function (s) {
                return function () {
                    this.meridian = s.slice(0, 1).toLowerCase();
                };
            }, timezone: function (s) {
                return function () {
                    var n = s.replace(/[^\d\+\-]/g, "");
                    if (n.length) {
                        this.timezoneOffset = Number(n);
                    } else {
                        this.timezone = s.toLowerCase();
                    }
                };
            }, day: function (x) {
                var s = x[0];
                return function () {
                    this.day = Number(s.match(/\d+/)[0]);
                };
            }, month: function (s) {
                return function () {
                    this.month = ((s.length == 3) ? Date.getMonthNumberFromName(s) : (Number(s) - 1));
                };
            }, year: function (s) {
                return function () {
                    var n = Number(s);
                    this.year = ((s.length > 2) ? n : (n + (((n + 2000) < Date.CultureInfo.twoDigitYearMax) ? 2000 : 1900)));
                };
            }, rday: function (s) {
                return function () {
                    switch (s) {
                        case"yesterday":
                            this.days = -1;
                            break;
                        case"tomorrow":
                            this.days = 1;
                            break;
                        case"today":
                            this.days = 0;
                            break;
                        case"now":
                            this.days = 0;
                            this.now = true;
                            break;
                    }
                };
            }, finishExact: function (x) {
                x = (x instanceof Array) ? x : [x];
                var now = new Date();
                this.year = now.getFullYear();
                this.month = now.getMonth();
                this.day = 1;
                this.hour = 0;
                this.minute = 0;
                this.second = 0;
                for (var i = 0; i < x.length; i++) {
                    if (x[i]) {
                        x[i].call(this);
                    }
                }
                this.hour = (this.meridian == "p" && this.hour < 13) ? this.hour + 12 : this.hour;
                if (this.day > Date.getDaysInMonth(this.year, this.month)) {
                    throw new RangeError(this.day + " is not a valid value for days.");
                }
                var r = new Date(this.year, this.month, this.day, this.hour, this.minute, this.second);
                if (this.timezone) {
                    r.set({timezone: this.timezone});
                } else if (this.timezoneOffset) {
                    r.set({timezoneOffset: this.timezoneOffset});
                }
                return r;
            }, finish: function (x) {
                x = (x instanceof Array) ? flattenAndCompact(x) : [x];
                if (x.length === 0) {
                    return null;
                }
                for (var i = 0; i < x.length; i++) {
                    if (typeof x[i] == "function") {
                        x[i].call(this);
                    }
                }
                if (this.now) {
                    return new Date();
                }
                var today = Date.today();
                var method = null;
                var expression = !!(this.days != null || this.orient || this.operator);
                if (expression) {
                    var gap, mod, orient;
                    orient = ((this.orient == "past" || this.operator == "subtract") ? -1 : 1);
                    if (this.weekday) {
                        this.unit = "day";
                        gap = (Date.getDayNumberFromName(this.weekday) - today.getDay());
                        mod = 7;
                        this.days = gap ? ((gap + (orient * mod)) % mod) : (orient * mod);
                    }
                    if (this.month) {
                        this.unit = "month";
                        gap = (this.month - today.getMonth());
                        mod = 12;
                        this.months = gap ? ((gap + (orient * mod)) % mod) : (orient * mod);
                        this.month = null;
                    }
                    if (!this.unit) {
                        this.unit = "day";
                    }
                    if (this[this.unit + "s"] == null || this.operator != null) {
                        if (!this.value) {
                            this.value = 1;
                        }
                        if (this.unit == "week") {
                            this.unit = "day";
                            this.value = this.value * 7;
                        }
                        this[this.unit + "s"] = this.value * orient;
                    }
                    return today.add(this);
                } else {
                    if (this.meridian && this.hour) {
                        this.hour = (this.hour < 13 && this.meridian == "p") ? this.hour + 12 : this.hour;
                    }
                    if (this.weekday && !this.day) {
                        this.day = (today.addDays((Date.getDayNumberFromName(this.weekday) - today.getDay()))).getDate();
                    }
                    if (this.month && !this.day) {
                        this.day = 1;
                    }
                    return today.set(this);
                }
            }
        };
        var _ = Date.Parsing.Operators, g = Date.Grammar, t = Date.Translator, _fn;
        g.datePartDelimiter = _.rtoken(/^([\s\-\.\,\/\x27]+)/);
        g.timePartDelimiter = _.stoken(":");
        g.whiteSpace = _.rtoken(/^\s*/);
        g.generalDelimiter = _.rtoken(/^(([\s\,]|at|on)+)/);
        var _C = {};
        g.ctoken = function (keys) {
            var fn = _C[keys];
            if (!fn) {
                var c = Date.CultureInfo.regexPatterns;
                var kx = keys.split(/\s+/), px = [];
                for (var i = 0; i < kx.length; i++) {
                    px.push(_.replace(_.rtoken(c[kx[i]]), kx[i]));
                }
                fn = _C[keys] = _.any.apply(null, px);
            }
            return fn;
        };
        g.ctoken2 = function (key) {
            return _.rtoken(Date.CultureInfo.regexPatterns[key]);
        };
        g.h = _.cache(_.process(_.rtoken(/^(0[0-9]|1[0-2]|[1-9])/), t.hour));
        g.hh = _.cache(_.process(_.rtoken(/^(0[0-9]|1[0-2])/), t.hour));
        g.H = _.cache(_.process(_.rtoken(/^([0-1][0-9]|2[0-3]|[0-9])/), t.hour));
        g.HH = _.cache(_.process(_.rtoken(/^([0-1][0-9]|2[0-3])/), t.hour));
        g.m = _.cache(_.process(_.rtoken(/^([0-5][0-9]|[0-9])/), t.minute));
        g.mm = _.cache(_.process(_.rtoken(/^[0-5][0-9]/), t.minute));
        g.s = _.cache(_.process(_.rtoken(/^([0-5][0-9]|[0-9])/), t.second));
        g.ss = _.cache(_.process(_.rtoken(/^[0-5][0-9]/), t.second));
        g.hms = _.cache(_.sequence([g.H, g.mm, g.ss], g.timePartDelimiter));
        g.t = _.cache(_.process(g.ctoken2("shortMeridian"), t.meridian));
        g.tt = _.cache(_.process(g.ctoken2("longMeridian"), t.meridian));
        g.z = _.cache(_.process(_.rtoken(/^(\+|\-)?\s*\d\d\d\d?/), t.timezone));
        g.zz = _.cache(_.process(_.rtoken(/^(\+|\-)\s*\d\d\d\d/), t.timezone));
        g.zzz = _.cache(_.process(g.ctoken2("timezone"), t.timezone));
        g.timeSuffix = _.each(_.ignore(g.whiteSpace), _.set([g.tt, g.zzz]));
        g.time = _.each(_.optional(_.ignore(_.stoken("T"))), g.hms, g.timeSuffix);
        g.d = _.cache(_.process(_.each(_.rtoken(/^([0-2]\d|3[0-1]|\d)/), _.optional(g.ctoken2("ordinalSuffix"))), t.day));
        g.dd = _.cache(_.process(_.each(_.rtoken(/^([0-2]\d|3[0-1])/), _.optional(g.ctoken2("ordinalSuffix"))), t.day));
        g.ddd = g.dddd = _.cache(_.process(g.ctoken("sun mon tue wed thu fri sat"), function (s) {
            return function () {
                this.weekday = s;
            };
        }));
        g.M = _.cache(_.process(_.rtoken(/^(1[0-2]|0\d|\d)/), t.month));
        g.MM = _.cache(_.process(_.rtoken(/^(1[0-2]|0\d)/), t.month));
        g.MMM = g.MMMM = _.cache(_.process(g.ctoken("jan feb mar apr may jun jul aug sep oct nov dec"), t.month));
        g.y = _.cache(_.process(_.rtoken(/^(\d\d?)/), t.year));
        g.yy = _.cache(_.process(_.rtoken(/^(\d\d)/), t.year));
        g.yyy = _.cache(_.process(_.rtoken(/^(\d\d?\d?\d?)/), t.year));
        g.yyyy = _.cache(_.process(_.rtoken(/^(\d\d\d\d)/), t.year));
        _fn = function () {
            return _.each(_.any.apply(null, arguments), _.not(g.ctoken2("timeContext")));
        };
        g.day = _fn(g.d, g.dd);
        g.month = _fn(g.M, g.MMM);
        g.year = _fn(g.yyyy, g.yy);
        g.orientation = _.process(g.ctoken("past future"), function (s) {
            return function () {
                this.orient = s;
            };
        });
        g.operator = _.process(g.ctoken("add subtract"), function (s) {
            return function () {
                this.operator = s;
            };
        });
        g.rday = _.process(g.ctoken("yesterday tomorrow today now"), t.rday);
        g.unit = _.process(g.ctoken("minute hour day week month year"), function (s) {
            return function () {
                this.unit = s;
            };
        });
        g.value = _.process(_.rtoken(/^\d\d?(st|nd|rd|th)?/), function (s) {
            return function () {
                this.value = s.replace(/\D/g, "");
            };
        });
        g.expression = _.set([g.rday, g.operator, g.value, g.unit, g.orientation, g.ddd, g.MMM]);
        _fn = function () {
            return _.set(arguments, g.datePartDelimiter);
        };
        g.mdy = _fn(g.ddd, g.month, g.day, g.year);
        g.ymd = _fn(g.ddd, g.year, g.month, g.day);
        g.dmy = _fn(g.ddd, g.day, g.month, g.year);
        g.date = function (s) {
            return ((g[Date.CultureInfo.dateElementOrder] || g.mdy).call(this, s));
        };
        g.format = _.process(_.many(_.any(_.process(_.rtoken(/^(dd?d?d?|MM?M?M?|yy?y?y?|hh?|HH?|mm?|ss?|tt?|zz?z?)/), function (fmt) {
            if (g[fmt]) {
                return g[fmt];
            } else {
                throw Date.Parsing.Exception(fmt);
            }
        }), _.process(_.rtoken(/^[^dMyhHmstz]+/), function (s) {
            return _.ignore(_.stoken(s));
        }))), function (rules) {
            return _.process(_.each.apply(null, rules), t.finishExact);
        });
        var _F = {};
        var _get = function (f) {
            return _F[f] = (_F[f] || g.format(f)[0]);
        };
        g.formats = function (fx) {
            if (fx instanceof Array) {
                var rx = [];
                for (var i = 0; i < fx.length; i++) {
                    rx.push(_get(fx[i]));
                }
                return _.any.apply(null, rx);
            } else {
                return _get(fx);
            }
        };
        g._formats = g.formats(["yyyy-MM-ddTHH:mm:ss", "ddd, MMM dd, yyyy H:mm:ss tt", "ddd MMM d yyyy HH:mm:ss zzz", "d"]);
        g._start = _.process(_.set([g.date, g.time, g.expression], g.generalDelimiter, g.whiteSpace), t.finish);
        g.start = function (s) {
            try {
                var r = g._formats.call({}, s);
                if (r[1].length === 0) {
                    return r;
                }
            } catch (e) {
            }
            return g._start.call({}, s);
        };
    }());
    Date._parse = Date.parse;
    Date.parse = function (s) {
        var r = null;
        if (!s) {
            return null;
        }
        try {
            r = Date.Grammar.start.call({}, s);
        } catch (e) {
            return null;
        }
        return ((r[1].length === 0) ? r[0] : null);
    };
    Date.getParseFunction = function (fx) {
        var fn = Date.Grammar.formats(fx);
        return function (s) {
            var r = null;
            try {
                r = fn.call({}, s);
            } catch (e) {
                return null;
            }
            return ((r[1].length === 0) ? r[0] : null);
        };
    };
    Date.parseExact = function (s, fx) {
        return Date.getParseFunction(fx)(s);
    };


    /**
     * @version: 1.0 Alpha-1
     * @author: Coolite Inc. http://www.coolite.com/
     * @date: 2008-04-13
     * @copyright: Copyright (c) 2006-2008, Coolite Inc. (http://www.coolite.com/). All rights reserved.
     * @license: Licensed under The MIT License. See license.txt and http://www.datejs.com/license/.
     * @website: http://www.datejs.com/
     */

    /* 
     * TimeSpan(milliseconds);
     * TimeSpan(days, hours, minutes, seconds);
     * TimeSpan(days, hours, minutes, seconds, milliseconds);
     */
    var TimeSpan = function (days, hours, minutes, seconds, milliseconds) {
        var attrs = "days hours minutes seconds milliseconds".split(/\s+/);

        var gFn = function (attr) {
            return function () {
                return this[attr];
            };
        };

        var sFn = function (attr) {
            return function (val) {
                this[attr] = val;
                return this;
            };
        };

        for (var i = 0; i < attrs.length; i++) {
            var $a = attrs[i], $b = $a.slice(0, 1).toUpperCase() + $a.slice(1);
            TimeSpan.prototype[$a] = 0;
            TimeSpan.prototype["get" + $b] = gFn($a);
            TimeSpan.prototype["set" + $b] = sFn($a);
        }

        if (arguments.length == 4) {
            this.setDays(days);
            this.setHours(hours);
            this.setMinutes(minutes);
            this.setSeconds(seconds);
        } else if (arguments.length == 5) {
            this.setDays(days);
            this.setHours(hours);
            this.setMinutes(minutes);
            this.setSeconds(seconds);
            this.setMilliseconds(milliseconds);
        } else if (arguments.length == 1 && typeof days == "number") {
            var orient = (days < 0) ? -1 : +1;
            this.setMilliseconds(Math.abs(days));

            this.setDays(Math.floor(this.getMilliseconds() / 86400000) * orient);
            this.setMilliseconds(this.getMilliseconds() % 86400000);

            this.setHours(Math.floor(this.getMilliseconds() / 3600000) * orient);
            this.setMilliseconds(this.getMilliseconds() % 3600000);

            this.setMinutes(Math.floor(this.getMilliseconds() / 60000) * orient);
            this.setMilliseconds(this.getMilliseconds() % 60000);

            this.setSeconds(Math.floor(this.getMilliseconds() / 1000) * orient);
            this.setMilliseconds(this.getMilliseconds() % 1000);

            this.setMilliseconds(this.getMilliseconds() * orient);
        }

        this.getTotalMilliseconds = function () {
            return (this.getDays() * 86400000) + (this.getHours() * 3600000) + (this.getMinutes() * 60000) + (this.getSeconds() * 1000);
        };

        this.compareTo = function (time) {
            var t1 = new Date(1970, 1, 1, this.getHours(), this.getMinutes(), this.getSeconds()), t2;
            if (time === null) {
                t2 = new Date(1970, 1, 1, 0, 0, 0);
            }
            else {
                t2 = new Date(1970, 1, 1, time.getHours(), time.getMinutes(), time.getSeconds());
            }
            return (t1 < t2) ? -1 : (t1 > t2) ? 1 : 0;
        };

        this.equals = function (time) {
            return (this.compareTo(time) === 0);
        };

        this.add = function (time) {
            return (time === null) ? this : this.addSeconds(time.getTotalMilliseconds() / 1000);
        };

        this.subtract = function (time) {
            return (time === null) ? this : this.addSeconds(-time.getTotalMilliseconds() / 1000);
        };

        this.addDays = function (n) {
            return new TimeSpan(this.getTotalMilliseconds() + (n * 86400000));
        };

        this.addHours = function (n) {
            return new TimeSpan(this.getTotalMilliseconds() + (n * 3600000));
        };

        this.addMinutes = function (n) {
            return new TimeSpan(this.getTotalMilliseconds() + (n * 60000));
        };

        this.addSeconds = function (n) {
            return new TimeSpan(this.getTotalMilliseconds() + (n * 1000));
        };

        this.addMilliseconds = function (n) {
            return new TimeSpan(this.getTotalMilliseconds() + n);
        };

        this.get12HourHour = function () {
            return (this.getHours() > 12) ? this.getHours() - 12 : (this.getHours() === 0) ? 12 : this.getHours();
        };

        this.getDesignator = function () {
            return (this.getHours() < 12) ? Date.CultureInfo.amDesignator : Date.CultureInfo.pmDesignator;
        };

        this.toString = function (format) {
            this._toString = function () {
                if (this.getDays() !== null && this.getDays() > 0) {
                    return this.getDays() + "." + this.getHours() + ":" + this.p(this.getMinutes()) + ":" + this.p(this.getSeconds());
                }
                else {
                    return this.getHours() + ":" + this.p(this.getMinutes()) + ":" + this.p(this.getSeconds());
                }
            };

            this.p = function (s) {
                return (s.toString().length < 2) ? "0" + s : s;
            };

            var me = this;

            return format ? format.replace(/dd?|HH?|hh?|mm?|ss?|tt?/g,
                function (format) {
                    switch (format) {
                        case "d":
                            return me.getDays();
                        case "dd":
                            return me.p(me.getDays());
                        case "H":
                            return me.getHours();
                        case "HH":
                            return me.p(me.getHours());
                        case "h":
                            return me.get12HourHour();
                        case "hh":
                            return me.p(me.get12HourHour());
                        case "m":
                            return me.getMinutes();
                        case "mm":
                            return me.p(me.getMinutes());
                        case "s":
                            return me.getSeconds();
                        case "ss":
                            return me.p(me.getSeconds());
                        case "t":
                            return ((me.getHours() < 12) ? Date.CultureInfo.amDesignator : Date.CultureInfo.pmDesignator).substring(0, 1);
                        case "tt":
                            return (me.getHours() < 12) ? Date.CultureInfo.amDesignator : Date.CultureInfo.pmDesignator;
                    }
                }
            ) : this._toString();
        };
        return this;
    };

    /**
     * Gets the time of day for this date instances.
     * @return {TimeSpan} TimeSpan
     */
    Date.prototype.getTimeOfDay = function () {
        return new TimeSpan(0, this.getHours(), this.getMinutes(), this.getSeconds(), this.getMilliseconds());
    };

    /* 
     * TimePeriod(startDate, endDate);
     * TimePeriod(years, months, days, hours, minutes, seconds, milliseconds);
     */
    var TimePeriod = function (years, months, days, hours, minutes, seconds, milliseconds) {
        var attrs = "years months days hours minutes seconds milliseconds".split(/\s+/);

        var gFn = function (attr) {
            return function () {
                return this[attr];
            };
        };

        var sFn = function (attr) {
            return function (val) {
                this[attr] = val;
                return this;
            };
        };

        for (var i = 0; i < attrs.length; i++) {
            var $a = attrs[i], $b = $a.slice(0, 1).toUpperCase() + $a.slice(1);
            TimePeriod.prototype[$a] = 0;
            TimePeriod.prototype["get" + $b] = gFn($a);
            TimePeriod.prototype["set" + $b] = sFn($a);
        }

        if (arguments.length == 7) {
            this.years = years;
            this.months = months;
            this.setDays(days);
            this.setHours(hours);
            this.setMinutes(minutes);
            this.setSeconds(seconds);
            this.setMilliseconds(milliseconds);
        } else if (arguments.length == 2 && arguments[0] instanceof Date && arguments[1] instanceof Date) {
            // startDate and endDate as arguments

            var d1 = years.clone();
            var d2 = months.clone();

            var temp = d1.clone();
            var orient = (d1 > d2) ? -1 : +1;

            this.years = d2.getFullYear() - d1.getFullYear();
            temp.addYears(this.years);

            if (orient == +1) {
                if (temp > d2) {
                    if (this.years !== 0) {
                        this.years--;
                    }
                }
            } else {
                if (temp < d2) {
                    if (this.years !== 0) {
                        this.years++;
                    }
                }
            }

            d1.addYears(this.years);

            if (orient == +1) {
                while (d1 < d2 && d1.clone().addDays(Date.getDaysInMonth(d1.getYear(), d1.getMonth())) < d2) {
                    d1.addMonths(1);
                    this.months++;
                }
            }
            else {
                while (d1 > d2 && d1.clone().addDays(-d1.getDaysInMonth()) > d2) {
                    d1.addMonths(-1);
                    this.months--;
                }
            }

            var diff = d2 - d1;

            if (diff !== 0) {
                var ts = new TimeSpan(diff);
                this.setDays(ts.getDays());
                this.setHours(ts.getHours());
                this.setMinutes(ts.getMinutes());
                this.setSeconds(ts.getSeconds());
                this.setMilliseconds(ts.getMilliseconds());
            }
        }
        return this;
    };

    exports.init = function (opt) {


        if (opt.double && opt.double == true) {
            $(opt.dom).daterangepicker({
                presetRanges: [
                    {text: '最近一周', dateStart: 'yesterday-7days', dateEnd: 'yesterday'},
                    {text: '最近一月', dateStart: 'yesterday-1month', dateEnd: 'yesterday'},
                    {text: '最近一年', dateStart: 'yesterday-1year', dateEnd: 'yesterday'}
                ],
                presets: {
                    dateRange: '自定义时间'
                },
                rangeStartTitle: '起始日期',
                rangeEndTitle: '结束日期',
                nextLinkText: '下月',
                prevLinkText: '上月',
                doneButtonText: '确定',
                cancelButtonText: '取消',
                earliestDate: '',
                latestDate: Date.parse('today'),
                constrainDates: true,
                rangeSplitter: '至',
                dateFormat: 'yy-mm-dd',
                closeOnSelect: false,
                /**
                 * 2016-04-12 ch add start
                 */
                startDateDom: opt.startDateDom,
                endDateDom: opt.endDateDom,
                /**
                 * 2016-04-12 ch add end
                 */
                onOpen: function () {
                    $('.ui-daterangepicker-dateRange').click().parent().hide();
                }
            });
            var optIcon = $(opt.dom).siblings('.icon-calendar');
            optIcon.on('click', function () {
                $(this).siblings(opt.dom).trigger('click').trigger('foucs');
                return false;
            });

        } else {
            if (opt.option && opt.option.showTime) {
                opt.option.showButtonPanel = true;
            }
            $(opt.dom).datepicker(opt.option || {});
            var optIcon = $(opt.dom).siblings('.icon-calendar');
            optIcon.on('click', function () {
                $(this).siblings(opt.dom).trigger('focus');
                return false;
            });
        }
    }

});