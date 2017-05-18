/**
 * Created by sky on 16/3/28.
 * 下拉菜单模块(dropList)
 */

define(function(require, exports, module) {


    /**** ?import modules ****/
    var tpl = require('art');
    require('./ajax');

    var $doc = $(document),
        $body = $('body'),
        // ajax请求数据回填清空按钮
        clearBtn = 'js-downlist-clear-btn',
        domClearBtn = '.' + clearBtn,
        // ajax请求输入框
        searchInput = 'js-downlist-input',
        domSearchInput = '.' + searchInput,
        // 下拉列表的class
        list = 'yqx-downlist-panel',
        domList = '.' + list,
        // 模板工具（输入框和清空）
        tplTool = 'js-downlist-tools',
        domTplTool = '.' + tplTool,
        // 下拉列表的选项部分
        listContent = 'js-downlist-content',
        domListContent = '.' + listContent,
        // 下拉列表子项的class
        listItem = 'js-downlist-item',
        domListItem = '.' + listItem,

        currAjax,
        timer,
        // 当前选中的li
        current,
        // 判断鼠标是否操作
        mouseOpa,
        // 最近一次输入信息
        lastInput,
        // 缓存
        cache = new Cache();


    /**** ?是否为第一次显示下拉列表 ****/
    function isFirstRenderFn() {
        return $(domList).size() ? false : true;
    }

    /**** ?重置下拉列表选中的li ****/
    function resetCurrentLi() { current = -1; }

    /**** ?ajax请求 ****/
    function ajaxReq(url, data, success) {

        var cacheData,key = url + JSON.stringify(data);

        if(cacheData = cache.get(key)) {
            if (typeof success === 'function') {
                success(cacheData);
                return;
            }
        }


        currAjax && currAjax.abort();
        currAjax = $.ajax({
            url: url,
            data: data,
            global: false,
            success: function (json) {
                cache.set(key, json);
                if (typeof success === 'function') {
                    success(json);
                }
            }
        });
    }

    /*
     * 匹配元素，将由 -、_、数字、字母 组成的字符串转化为 [name=*]
     * params: string/array
     * return: array
     */
    function getDOMSelector(selectors) {
        var i, selector;

        if (typeof selectors === 'string') {
            selectors = selectors.split(',');
        }

        if (selectors instanceof Array) {
            for (i = 0; i < selectors.length; i++) {
                selector = selectors[i];
                // 排除id、class，且selector符合简单name命名规则的
                if( !/^[\.#][1-9a-zA-Z]+([-_][1-9a-zA-Z]+)?$/.test(selector)
                    && /^[1-9a-zA-Z]+([-_][1-9a-zA-Z]+)?$/.test(selector) ) {
                    selectors[i] = '[name="' + selector + '"]';
                }
            }
        }

        return selectors;
    }


    function DownList(options) {
        this.setting = $.extend({}, DownList._default, options);
    }

    /**** ?options参数以这里为准 ****/
    DownList._default = {
        /* DOM 相关 start */
        // 作用域
        scope: 'yqx-downlist-wrap',
        // 绑定元素
        dom: '.js-downlist',
        /* DOM 相关 end */

        /* 必填项 start */
        //* ajax请求路径（类型：字符串）
        url: "",
        //* 回调函数显示参数的key（类型：以“,”隔开的字符串）
        showKey: "",
        /* 必填项 end */

        // 回调函数显示参数的选择器（与 showKey 配合使用，类型：以“,”隔开的字符串/数组）
        showSelector: "",
        // 回调函数隐藏参数key（类型：以“,”隔开的字符串）
        hiddenKey: "",
        // 回调函数隐藏参数的选择器（与 hiddenKey 配合使用，若为空，hiddenSelector = hiddenKey，类型：以“,”隔开的字符串/数组）
        hiddenSelector: "",
        // scope作用局内的局部检索ajax参数（以[name=*]获取值，类型：以“,”隔开的字符串）
        searchKey: "",
        // 全局检索ajax参数（类型：对象）
        globalData: null,
        // 请求结束的回调函数
        reqCallbackFn: null,
        // 选中一项列表后的回调函数（类型：方法）
        callbackFn: null,
        // 清空选中项的回调函数（类型：方法）
        clearCallbackFn: null,
        // 模板填充（类型：字符串）
        tplId: null,
        // 模板列（设置模板的每一列的显示内容与 tplId 互斥，类型：对象）
        tplCols: null,
        // 模板列宽（设置模板每一列的宽度，与 tplCols 配合使用，类型：数组）
        tplColsWidth: null,
        // 模板是否有title
        hasTitle: true,
        // 模板是否有输入框（类型：布尔）
        hasInput: true,
        // 是否清空当前输入框，可选
        notClearInput: false,
        // ajax延迟时间（类型：数字）
        delay: 300,
        // 当数据只有一条时，自动选中第一条（类型：布尔）
        autoFill: false
    };

    DownList.prototype = {
        constructor: DownList,
        lastEvent: '',
        init: function() {
            // 解析选择器
            var selectors = getDOMSelector(this.setting.dom).join(',');
            if (this.setting.hasInput) {
                $(selectors).prop('readonly', true).css({'background': '#fff', 'cursor': 'pointer'});
            }
            this.bindEvent(selectors);
            return this;
        },
        /**** ?模板渲染(使用call传入this, this是指输入框) ****/
        renderTemplate: function (selector, json) {
            var self = this,
                templateTool = [
                    '<div class="xqy-downlist-tools ' + tplTool + '">',
                        '<input class="xqy-downlist-input yqx-input ' + searchInput + '" type="text" />',
                    '</div>'
                ].join(''),
                showKey = self.setting.showKey && self.setting.showKey.split(','),
                defaultWidth = selector.outerWidth(),
                templateTitle, template, templateBody,
                i, name,
                dataColsJson, colsWidthList, dataColsLen = 0, widthCount = 0;

            // 获取指定templateId的模板
            if ( self.setting.tplId ) {
                templateBody = $('#' + self.setting.tplId).html();
                template = isFirstRenderFn() ? [
                    '<div class="' + list + '">',
                        (self.setting.hasInput ? templateTool : ''),
                        '<div class="js-downlist-body">',
                        templateBody,
                    '</div></div>'
                ].join('') : templateBody;
                return tpl.compile(template)({templateData: json.data});

                // 使用默认公用模板
            } else {
                if ( !(dataColsJson = self.setting.tplCols) ) {
                    if (showKey.length) {
                        dataColsJson = {};
                        for(i = 0; i < showKey.length; i++) {
                            dataColsJson[showKey[i]] = '';
                        }
                    } else {
                        console.error(selector, '默认downlist模板必须初始化设置showKey或者tplCols属性!');
                        return "";
                    }
                }

                if ( (colsWidthList = self.setting.tplColsWidth) ) {
                    for (i = 0; i < colsWidthList.length; i++) {
                        widthCount += colsWidthList[i];
                    }
                }

                for (name in dataColsJson) {
                    dataColsLen++;
                }

                if (colsWidthList && colsWidthList.length && colsWidthList.length != dataColsLen) {
                    console.warn('列宽数组长度与列个数不一致，请检查tplCols属性和tplColsWidth属性(downlist.js)');
                }

                // 下拉列表头
                templateTitle = [
                    '<ul class="yqx-downlist-title">',
                    '<li' + (colsWidthList?' style="width:' + widthCount + 'px"' : '') + '>',
                    '<%for(name in dataColsJson){%>',
                    '<b style="width:',
                    '<%=(colsWidthList ? colsWidthList[index++] +"px" : 100/dataColsLen + "%")%>"><%=dataColsJson[name];%></b>',
                    '<%}%></li>',
                    '</ul>'
                ].join('');

                // 下拉列表体
                templateBody = [
                    '<%if(templateData&&templateData.length){%>',
                    // 列表头的参数
                    '<%var index = 0, name;%>',
                    (self.setting.hasTitle ? templateTitle : ''),
                    '<ul class="yqx-downlist-content ' + listContent + '">',
                    // 下拉列表内容体
                    '<%for(var i = 0; i < templateData.length; i++){' +
                    'var item = templateData[i]; ' +
                    'index = 0;%>',
                    '<li class="' + listItem + '"' + (colsWidthList?' style="width:' + widthCount + 'px"' : '') + '>' +
                    '<%for(name in dataColsJson){%>',
                    '<span class="js-show-tips" title="<%= item[name] %>" style="width:' +
                        // 此处百分比减-1是为了兼容IE7
                    '<%=(colsWidthList&&colsWidthList[index++] + "px") || ~~(100/dataColsLen) + "%"%>"><%=item[name]%></span>',
                    '<%}%></li><%}%>',
                    '</ul>',
                    '<%}else{%>',
                    '<%}%>'
                ].join('');
                template = isFirstRenderFn() ? [
                    '<div class="' + list + '"' + (colsWidthList?'': 'style="width:' + (dataColsLen*defaultWidth) + 'px"') + '>',
                    (self.setting.hasInput ? templateTool : ''),
                    '<div class="js-downlist-body">',
                    templateBody,
                    '</div></div>'
                ].join('') : templateBody;

                return tpl.compile(template)({
                    templateData: json.data,
                    dataColsJson: dataColsJson,
                    dataColsLen: dataColsLen,
                    colsWidthList: colsWidthList
                });
            }
        },
        /**** ?移除下拉框 ****/
        closeDownList: function () {
            $doc.off('keyup.dl');
            $doc.off('mousemove.dl');
            $(domList).remove();
        },
        /**** ?清空下拉数据 ****/
        clearDownListContent: function () {
            $('.js-downlist-body').empty();
        },
        /**** ?对应DOM的绑定 ****/
        bindEvent: function (selectors) {
            var self = this,
                $scope, url, showKey, showSelector, hiddenKey,
                hiddenSelector, searchKey, globalData;

            $doc.on('click.dl', self.closeDownList);
            /* 将事件绑定在body上，防止冒泡到document触发关闭下拉菜单事件 */
            $doc.on('change.dl', selectors, function(e) {
                var $input = $(this);
                if (lastInput != null && lastInput != $input.val() ) {
                    self._clear($input, $scope, showSelector, hiddenSelector);
                }
            });
            $body
                .on('click.dl focus.dl', selectors, function (e) {
                    if ((self.lastEvent === 'focusin' || self.lastEvent === 'focus') && e.type === 'click') {
                        self.lastEvent = e.type;
                        return false;
                    }

                    self.lastEvent = e.type;

                    var $input = $(this);
                    // 作用域
                    $scope = $input.parents('.' + self.setting.scope);
                    url = self.setting.url;
                    showKey = self.setting.showKey;
                    showSelector = self.setting.showSelector;
                    hiddenKey = self.setting.hiddenKey;
                    hiddenSelector = self.setting.hiddenSelector;
                    searchKey = self.setting.searchKey;
                    globalData = self.setting.globalData;

                    if(showSelector && !self.setting.scope) {
                        console.warn('请设置 scope, scope 为类选择器');
                    }
                    // 输入关键字时请求数据
                    var keyupQuery = function (data) {
                        // 延迟进行ajax请求
                        if (!timer) {
                            timer = setTimeout(function () {
                                ajaxReq(url, data, self._ajaxSuccess.bind(self, $input, $scope));
                                clearTimeout(timer);
                                timer = null;
                            }, self.setting.delay);
                        }
                    };

                    // 获得焦点时请求数据
                    var clickQuery = function (data) {
                        ajaxReq(url, data, self._ajaxSuccess.bind(self, $input, $scope));
                    };

                    // INITIALIZE START
                    if (!url) {
                        console.error($input, 'url不能为空！');
                        return;
                    }
                    if (!showKey) {
                        console.error($input, 'showKey不能为空！');
                        return;
                    }
                    // 设置作用域
                    if ( $scope.size() ) {
                        $scope.css({'position': 'relative'});
                    } else {
                        $input.wrap('<div class=' + self.setting.scope + '></div>');
                        $scope = $input.parent();
                        $scope.css({'display': 'inline-block', 'position': 'relative'});
                        // modify by sky 2016-04-25 修复包裹父元素时失焦的问题
                        $input.trigger('focus.dl');
                        return;
                    }
                    // 关闭其他下拉框
                    self.closeDownList();
                    e.stopPropagation();
                    resetCurrentLi();
                    // 参数初始化
                    lastInput = $input.val();
                    searchKey = searchKey ? searchKey.split(',') : [];
                    showKey = showKey ? showKey.split(',') : [];
                    /* modify by sky 2016-04-14 删除将showKey赋值给showSelector */
                    showSelector = showSelector ? getDOMSelector(showSelector) : [];
                    hiddenKey = hiddenKey ? hiddenKey.split(',') : [];
                    hiddenSelector || (hiddenSelector = hiddenKey);
                    hiddenSelector = hiddenSelector ? getDOMSelector(hiddenSelector) : [];
                    // 请求数据
                    clickQuery( self._getData($input, $scope, searchKey, globalData) );
                    // INITIALIZE END

                    /**** ?下拉列表事件 ****/
                    $doc
                        .off('keyup.dl')
                        .on('keyup.dl', function (e) {
                            var $li = $(domListItem, $scope),
                                liSize = $li.size(),
                                liHeight = $li.eq(0).outerHeight(),
                                $cur;

                            // 设置选中的li
                            function currentLiChange() {
                                mouseOpa = false;
                                if (current < 0) {
                                    current = liSize - 1;
                                } else if (current >= liSize) {
                                    current = 0;
                                }

                                $li.siblings('.current').removeClass('current')
                                    .end().eq(current).addClass('current');
                                $(domListContent, $scope).scrollTop(current * liHeight);
                            }

                            if ( !(e.ctrlKey || e.altKey || e.shiftKey || e.metaKey) ) {
                                switch (e.keyCode) {
                                    case 37:
                                    case 38:    // up
                                        current--;
                                        currentLiChange();
                                        break;
                                    case 39:
                                    case 40:    // down
                                        current++;
                                        currentLiChange();
                                        break;
                                    case 13:    // enter
                                        // 模拟当前选中项的click事件
                                        if ($(domList, $scope).size()) {
                                            $cur = $(domListItem + '.current', domList);
                                            $cur.size() && $cur.trigger('click.dl');
                                        } else {
                                            keyupQuery( self._getData($input, $scope, searchKey, globalData) );
                                        }
                                        break;
                                    default :
                                        keyupQuery( self._getData($input, $scope, searchKey, globalData) );
                                }
                            }

                        })
                        // 鼠标移动（避免与键盘事件的冲突）
                        .off('mousemove.dl')
                        .on('mousemove.dl', function() {
                            mouseOpa = true;
                        })
                        // 鼠标移入选中li
                        .off('mouseenter.dl', domListItem)
                        .on('mouseenter.dl', domListItem, function () {
                            if (!mouseOpa) { return; }
                            var $this = $(this);
                            current = $this.index();
                            $this.addClass('current')
                                .siblings('.current').removeClass('current');
                        })
                        // 点击选中的li
                        .off('click.dl', domListItem)
                        .on('click.dl', domListItem, function() {
                            self._clear($input, $scope, showSelector, hiddenSelector);
                            self._enterOpt($input, $scope, $(this), showKey, showSelector, hiddenKey, hiddenSelector);
                        });

                });
            /* $body end */
        },
        _clear: function($input, $scope, showSelector, hiddenSelector) {
            var self = this, i, selector;
            /* modify by sky 2016-04-21 start */
            // 清空所有的显示内容和隐藏域内容
            if (showSelector.length) {
                for (i = 0; i < showSelector.length; i++) {
                    selector = $(showSelector[i], $scope);
                    if ( !self.setting.notClearInput || (self.setting.notClearInput&&selector[0] != $input[0]) ) {
                        selector.val('');
                    }
                }
            } else {
                if(!self.setting.notClearInput) {
                    $input.val('');
                }
            }
            /* modify by sky 2016-04-21 end */

            if (hiddenSelector.length) {
                for (i = 0; i < hiddenSelector.length; i++) {
                    $(hiddenSelector[i], $scope).val('');
                }
            }

            if (typeof self.setting.clearCallbackFn === 'function') {
                self.setting.clearCallbackFn($input, $scope);
            }
        },
        _getData: function($input, $scope, keys, globalData) {
            // 配置ajax请求的data(keys为searchKey)
            var self = this;
            var ajaxData = {},
                value = self.setting.hasInput ? $.trim($(domSearchInput).val()) : $.trim($input.val()),
                i, k;

            value = (value === undefined) ? '' : value;

            if (!keys.length) {
                ajaxData['q'] = value;
            } else if (keys.length == 1) {
                k = keys[0];
                ajaxData[k] = value;
            } else {
                for(i = 0; i < keys.length; i++) {
                    k = keys[i];
                    ajaxData[k] = $('[name="' + k + '"]', $scope).val();
                }
            }

            if (typeof globalData === 'object') {
                ajaxData = $.extend({}, ajaxData, globalData);
            }

            return ajaxData;
        },
        _ajaxSuccess: function($input, $scope, json) {
            var self = this;
            self.clearDownListContent();
            // 重置当前选中的li
            resetCurrentLi();

            self.setting.reqCallbackFn && self.setting.reqCallbackFn(json);
            if (!json.success) { return; }

            $input.data('listData', json.data);

            // 插入下拉菜单
            if ( isFirstRenderFn() ) {
                $input.after( self.renderTemplate($input, json) );
            } else {
                $('.js-downlist-body', $scope).html( self.renderTemplate($input, json) );
            }


            // 设置下拉菜单宽度
            $input.next(domList).css({'min-width': $input.outerWidth()});

            // 缓存每条数据到li上(点击li时需要读取这个数据)
            $(domListItem, domList).each(function(i) {
                $(this).data('itemData', json.data[i]);
            });

            /* 事件绑定 */
            $(domTplTool).off('click.dl').on('click.dl', function(e) {
                e.stopPropagation();
            });

            // 选中第一条数据
            if (self.setting.autoFill) {
                $(domListItem, domList).eq(0).addClass('current');
                current++;
            }

            // 聚焦到下拉列表输入框
            self.setting.hasInput && $(domSearchInput, $scope).focus();
        },
        _enterOpt: function($input, $scope, $li, showKey, showSelector, hiddenKey, hiddenSelector) {
            var self = this,
                itemData = $li.data('itemData'),
                i, value, tmp;

            // tmp 处理value = null 问题 modify by sky 2016-04-14
            if (itemData) {
                /*
                 * modify by sky 2016-04-14
                 * 增强showKey和showSelector功能
                 */
                if (showKey.length && !showSelector.length) {
                    value = '';
                    for(i = 0; i < showKey.length; i++) {
                        tmp = itemData[showKey[i]] != null ? itemData[showKey[i]] + ' ' : '';
                        value += tmp;
                    }
                    $input.val( $.trim(value) );
                } else {
                    if (showSelector.length == showKey.length) {
                        for (i = 0; i < showKey.length; i++) {
                            value = itemData[showKey[i]] != null ? itemData[showKey[i]] + ' ' : '';
                            $(showSelector[i], $scope).val( $.trim(value) );
                        }
                    } else if (showSelector.length == 1) {
                        value = '';
                        for (i = 0; i < showKey.length; i++) {
                            tmp = itemData[showKey[i]] != null ? itemData[showKey[i]] + ' ' : '';
                            value += tmp + ' ';
                        }
                        $(showSelector[0], $scope).val( $.trim(value) );
                    } else {
                        console.warn($input, 'showSelector与showKey字段不对等，请检查！');
                    }
                }
                /* 增强showKey和showSelector功能 end */
                lastInput = $input.val();
            }

            if (hiddenSelector.length == hiddenKey.length) {
                for (i = 0; i < hiddenKey.length; i++) {
                    $(hiddenSelector[i], $scope).val(itemData[hiddenKey[i]]);
                }
            } else {
                console.warn($input, 'hiddenSelector与hiddenKey字段不对等，请检查！');
            }

            if (typeof self.setting.callbackFn == 'function') {
                self.setting.callbackFn($input, itemData, $scope);
            }

            self.closeDownList();
        }
    };


    // 初始化Downlist
    exports.init = function (options) {
        var downlist = new DownList(options);
        return downlist.init();
    };
    // 修改Downlist.setting
    exports.update = function (dl, options) {
        this.reset(dl);
        dl['_setting'] = $.extend(true, {}, dl.setting);
        // 注：不要试图去更新选择器
        $.extend(dl.setting, options);
    };
    // 重置Downlist.setting
    exports.reset = function (dl) {
        if(dl['_setting']) {
            $.extend(dl.setting, dl._setting);
            delete dl._setting;
        }
    };

    //缓存
    function Cache() {
        this.data = {};

        return this;
    }

    Cache.prototype.MAX_COUNT = 10;

    Cache.prototype.get = function (key) {
        return this.data[key];
    };

    Cache.prototype.set = function (key, data) {
        var i, count = 0;

        for(i in this.data) {
            count ++;
        }
        if(count >= this.MAX_COUNT) {
            count = 0;
            for(i in this.data) {
                if(count == 0) {
                    this.data[i] = null;
                    break;
                }
            }
        }

        this.data[key] = data;
    };
});
