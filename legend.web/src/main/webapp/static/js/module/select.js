/**
 * ch 2016-03-28
 * 自定义select模块，支持异步加载
 */

define(function(require,exports,module){
    var ajax = require("ajax"),
        art = require("art"),
        doc = $(document),
        isBound = false,
        defaultOpt = {
            // 下拉列表作用域
            scope: null,
            // 下拉框挂载的对象，必填
            dom: "",
            // 异步对象
            url: "",
            // url参数配置 function/object
            params: null,
            // key值，和后台返回json里需要显示的字段一致
            showKey: "id",
            // value值，和后台返回json里需要显示的字段一致
            showValue: "name",
            // 下拉框宽度
            _width: '',
            // 模板id
            // 带 # 号
            tplId: '',
            // 是否可清空
            isClear: false,
            // 是否有'请选择'（Boolean/String）
            pleaseSelect: false,
            // 是否有'全部'
            allSelect: false,
            // 储存获取的数据
            // 如：多级选择，但是接口全部返回了
            retData: {},
            // 静态下拉框数据
            data: [{
                id: -1,
                name: "url或data参数必填"
            }],
            // 设置点击选中值时的回调函数
            callback: null,
            // 点击选项时, 该函数返回 false 时阻止选中
            select: null,
            // 在出现下拉框之前
            beforeSelectClick: null,
            // 阻止冒泡
            stopPropagation: true,
            // 设置选中值
            selectedKey: null,
            // 设置选中值时的回调函数
            selectedCallback: null,
            // 能否输入
            canInput: false,
            // 是否缓存
            cache: false,
            // 没有数据时的回调
            noDataCallback:null
        };
    var cache = new Cache();

    var Select = function(option){
        this.args = $.extend({}, defaultOpt, option);
    };

    Select.prototype = {
        //渲染select的option
        renderHtml: function(data){
            var tpl;
            if ( this.args.tplId && $(this.args.tplId).length ) {
                tpl = $(this.args.tplId).html();
            } else {
                tpl = [
                    '<div class="yqx-select-options" style="width:' + this.args._width + 'px;display:none;">',
                    '<dl>',
                    (this.args.pleaseSelect ? '<dd class="yqx-select-option" data-key="">' + (typeof this.args.pleaseSelect == 'string' ? this.args.pleaseSelect : '请选择') + '</dd>' : ''),
                    (this.args.allSelect ? '<dd class="yqx-select-option" data-key="">全部</dd>' : ''),
                    (this.args.isClear ? '<dd class="yqx-select-clean">请选择</dd>' : ''),
                    '<%for(var i=0;i<data.length;i++){%>',
                    '<dd class="yqx-select-option js-show-tips" title="<%=data[i][\"'
                    + this.args.showValue+'\"]%>" data-key="<%=data[i]["'
                    + this.args.showKey+'"]%>"'
                    + ' data-index="<%=i%>"'
                    + '><%=data[i]["'+this.args.showValue+'"]%></dd>',
                    '<%}%>',
                    '</dl>',
                    '</div>'
                ].join('');
            }

            return art.compile(tpl)({data:data});
        },
        getData: function(fn){
            var self = this,
                type = typeof self.args.params,
                params, data;

            if ('function' === type) {
                params = self.args.params();
            } else if ('object' === type) {
                params = self.args.params;
            }

            if(data = cache.get(self.args.url)) {
                fn && fn(data);
                if (!data.data || !data.data.length) {
                    self.args.noDataCallback && self.args.noDataCallback(data);
                }
                return;
            }
            $.ajax({
                url: self.args.url,
                data: params,
                success: function(json){
                    if (json.success) {
                        self.args.cache && cache.set(self.args.url, json);

                        fn && fn(json);
                        if (!json.data || !json.data.length) {
                            self.args.noDataCallback && self.args.noDataCallback(json);
                        }
                    }
                }
            });
        },
        _optionSelect: function(self) {
            var $this = $(this),
                key = $this.data('key'),
                value = (key != null ? $this.text() : ''),
                option = $this.parents('.yqx-select-options'),
                target = option.siblings(self.args.dom),
                index = $this.data('index');
            var retData = self.args.retData;

            if(self.args.select && typeof self.args.select == 'function') {
                if(self.args.select.call(this) === false) {
                    return;
                }
            }
            target.siblings('input[type="hidden"]').val(key);
            target.val(value).blur();
            self.close();
            typeof  self.args.callback == "function" && self.args.callback.call($this[0], key, value, retData && retData.data || null, index)
        },
        init: function(){
            var self = this,
                $scope = $(self.args.scope);

            $scope = $scope.length ? $scope : $('body');
            if(!isBound) {
                doc.off('click.sel')
                    .on('click.sel', function () {
                        self.close();
                    });

                doc.on('click.sel', '.icon-angle-up', function (event) {
                    self.close();

                    event.stopImmediatePropagation();
                });

                doc.on('click.sel', '.icon-angle-down', function (event) {
                    var $e = $(this).siblings('input');

                    $e.each(function () {
                        var type = $(this).attr('type');
                        if(!type || type == 'text') {
                            $(this).click();
                            $(this).focus();
                        }
                    });

                    event.stopImmediatePropagation();
                });
                isBound = true;
            }

            if(self.args.canInput) {
                // 模糊搜索
                $(document).
                off('input.select', self.args.dom)
                .on('input.select', self.args.dom, function () {
                    filter(this);

                    $(this).siblings('input[type="hidden"]').val('');
                });

                if( $(self.args.dom).val() ) {
                    filter($(self.args.dom)[0]);
                }
            }

            //绑定下拉列表单击事件。
            $scope.off('click.sel', self.args.dom)
                .on('click.sel', self.args.dom, function(){
                    var $this = $(this),
                        opt = $this.siblings('.yqx-select-options');
                    var before = self.args.beforeSelectClick;

                    // 下拉出现之前执行
                    if(before && typeof before == 'function') {
                        // 返回的是 false 则不出现下拉，并返回 false
                        if(before() === false) {
                            return false;
                        }
                    }

                    self.close(opt);

                    self.showOptions.call(this, opt, $this, self);
                    bindClickEvent.call(this, self, $scope);

                    if(self.args.stopPropagation === true) {
                        return false;
                    }
                });

            self.args.selectedKey != null ? $(self.args.dom).click() : selectedCallbackFn(self);

            return self;
        },
        update: function($ele) {
            var self = this,
                dom = $ele;

            self.args._width = dom.outerWidth();

            if(self.args.tplId != '') {
                dom.siblings('.yqx-select-options')
                    .css('width', self.args._width);
            }
            //对象只支持选择
            if(!self.args.canInput)
            dom.prop("readonly",true);
        },
        close: function($e){
            var options = $('.yqx-select-options:visible');
            var fa = options.siblings('.fa');
            $e = $e || [];

            options.each(function () {
                if($e[0] === this) {
                    return;
                }
                $(this).hide();
            });

            if(fa.hasClass('icon-angle-up')) {
                fa.removeClass('icon-angle-up').addClass('icon-angle-down');
            }
        },
        showOptions: function(opt, $this, self, data, show) {
            var html, $options;
            // 下面都是 select.js 外的调用
            // 为了重新设置选项
            // 多级的时候
            if(!self) {
                self = this;
                self.args.data = data;
            }
            if(!$this) {
                $this = $(self.args.dom);
            }
            self.update($this);
            if(opt && opt.size() > 0){
                if(opt.is(':visible')) {
                    self.close();
                } else {
                    opt.show();
                    if($this.siblings('.fa').hasClass('icon-angle-down')) {
                        $this.siblings('.fa').removeClass('icon-angle-down').addClass('icon-angle-up');
                    }
                }
            }else if(self.args.url){
                self.getData(function(json){
                    var data = json.data;

                    if(json.data && json.data.length) {

                        if (json.data[0] != null && (typeof json.data[0] == 'string' || typeof json.data[0] == 'number')) {
                            data = $.map(json.data, function (v) {
                                return {
                                    id: v,
                                    name: v
                                };
                            });
                        }
                        
                        html = self.renderHtml(data);
                    }

                    // 保存获取的数据
                    // {}.data 是为了异步，表示引用而不是拷贝
                    if(self.args.retData) {
                        self.args.retData.data = data;
                    }

                    $options = $this.after(html).parent()
                        .find('.yqx-select-options');

                    // tplId 时后更新
                    self.update($this);

                    if(self.args.selectedKey == null){
                        $options.show();
                        if($this.siblings('.fa').hasClass('icon-angle-down')) {
                            $this.siblings('.fa').removeClass('icon-angle-down').addClass('icon-angle-up');
                        }
                    } else {
                        setSelectedKey(html, self);
                        selectedCallbackFn(self);
                    }
                });
            }else if(self.args.data){
                // 重新设置 options 的时候删除旧的
                $this.parent()
                    .find('.yqx-select-options').remove();

                if(self.args.data.length) {
                    html = self.renderHtml(self.args.data);
                } else {
                    return;
                }

                $options = $this.after(html).parent()
                    .find('.yqx-select-options');

                if(self.args.selectedKey == null && show !== false){
                    $options.show();
                    if($this.siblings('.fa').hasClass('icon-angle-down')) {
                        $this.siblings('.fa').removeClass('icon-angle-down').addClass('icon-angle-up');
                    }
                }
                else{
                    setSelectedKey(html, self);
                    selectedCallbackFn(self);
                }
            }

        }
    };

    function setSelectedKey(html, self) {
        $(html).find('.yqx-select-option').each(function () {
            var key = $(this).data('key');
            var value;
            if(key == self.args.selectedKey) {
                self.args.selectedKey = null;
                value = $(this).text();

                $(self.args.dom).val(value)
                    .siblings('input[type="hidden"]').val(key);
            }
        });
    }

    function selectedCallbackFn(self){
        self.args.selectedCallback && self.args.selectedCallback();
    }

    function bindClickEvent(self, scope) {
        /* 每次触发时再进行事件的绑定，防止绑定事件多次触发，影响性能 start */
        scope.off('click.sel', '.yqx-select-option')
            .on('click.sel', '.yqx-select-option', function () {
                self._optionSelect.call(this, self);
                return false;
            });

        if(self.args.isClear) {
            scope.off('click.sel', '.yqx-select-clean')
                .on('click.sel', '.yqx-select-clean', function () {
                    self._optionSelect.call(this, self);
                    return false;
                });
        }
    }


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

    function filter(thisArg) {
        var arr = $(thisArg).parent().find('.yqx-select-options dd');
        var val = $.trim($(thisArg).val()).toLowerCase();

        arr.each(function () {
            var text = $(this).text().toLowerCase();

            if (text.indexOf(val) > -1) {
                $(this).removeClass('hide');
            } else {
                $(this).addClass('hide');
            }
        });
    }

    exports.init = function(option){
        var select = new Select(option);
        return select.init();
    }
});