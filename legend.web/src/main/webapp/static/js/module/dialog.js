//弹框模块
define(function (require, exports, module) {
    var layer = require("layer");

    layer.config({
        path: BASE_PATH + '/static/third-plugin/layer/',
        shift: 5,
        shadeClose: true
    });

    module.exports = {
        //普通弹框
        open: function (option) {
            return layer.open($.extend({
                type: 1,
                title: false,
                scrollbar: false,
                area: ['auto', 'auto'],
                content: 'html内容'
            }, option));
        },
        // 通用内容弹窗
        dialog: function (option) {
            var title = '', head = '', main = '', foot = '',
                header = '', body = '', footer = '';

            function getContent(tag, content) {
                var reg = new RegExp('<' + tag + '>([\\S\\s]*)</' + tag + '>'),
                    result = content.match(reg);
                return result && result[1];
            }

            if (option && option.content) {
                title = getContent('dg-title', option.content);
                head = getContent('dg-head', option.content);
                main = getContent('dg-body', option.content);
                foot = getContent('dg-foot', option.content);

                if (title != null) {
                    header = '<div class="yqx-dialog-header"><div class="yqx-dialog-headline">{0}</div></div>'.format(title);
                } else if (head) {
                    header = '<div class="yqx-dialog-header">{0}</div>'.format(head);
                }

                if (main) {
                    body = '<div class="yqx-dialog-body">{0}</div>'.format(main);
                }

                if (foot) {
                    footer = '<div class="yqx-dialog-footer">{0}</div>'.format(foot);
                }

                option.content = body ?
                    '<section class="yqx-dialog {0}">{1}{2}{3}</section>'
                        .format(option.wrapClass ? option.wrapClass : '', header, body, footer) :
                    option.content;
            }

            return this.open(option);
        },
        /**
         *
         * @param msg
         * @param yes   确认的回调函数
         * @param no    取消的回调函数
         * @param btn
         * @returns {*}
         */
        confirm: function (msg, yes, no, btn) {
            if (yes instanceof Array || typeof yes === "string") {
                btn = yes;
                yes = no = null;
            } else if (no instanceof Array || typeof yes === "string") {
                btn = no;
                no = null;
            }
            return layer.confirm(
                msg, {
                    icon: 3,
                    title: '提示',
                    shadeClose: false,
                    move: false,
                    btn: btn || ['确定', '取消']
                }, function (index) {
                    yes && yes();
                    layer.close(index);
                }, function (index) {
                    no && no();
                    layer.close(index);
                }
            );
        },

        //加载弹框
        load: function (option) {
            return layer.load(1, $.extend({
                shade: [0.1, '#000'],
                shadeClose: false
            }, option));
        },

        //信息提示弹框（icon -- 0: 警告; 1: 成功; 2: 失败;）
        msg: function (msg, option, fn) {
            var t, str, id, time;

            if (typeof option === 'function') {
                fn = option;
                option = {};
            }

            if (msg instanceof Array && msg.length === 3) {
                str = msg.join('');
                time = +msg[1] || 3;
                t = true;
                option["time"] = time * 1000;
            } else {
                str = msg;
            }

            id = layer.msg(str,
                $.extend({
                    shade: [0.3, '#000']
                }, option), fn
            );
            id = '#layui-layer' + id;

            if (t) {
                this.setReciprocal(id, msg, time);
            }

            return id;
        },
        // 成功提示弹框
        success: function (msg, fn) {
            this.msg(msg, {icon: 1}, fn);
        },
        // 警告提示弹框
        warn: function (msg, fn) {
            this.msg(msg, {icon: 0}, fn);
        },
        // 失败提示弹框
        fail: function (msg, fn) {
            this.msg(msg, {icon: 2}, fn);
        },
        //关闭弹框,参数是dialog方法返回弹窗的索引
        close: function (id) {
            if (id != null) {
                layer.close(id);
            } else {
                layer.closeAll();
            }
        },
        //tips modify by sky 2016-04-25 移除1.8.5版本的属性设置(已失效)
        tips: function (obj, msg, opt) {
            // 下方
            return layer.tips(msg, obj, {
                maxWidth: 500,
                tips: [3, '#607b0a'],
                time: opt && opt.time >= 0 && typeof opt.time === 'number' ? opt.time : 3000
            });

        },
        setReciprocal: function (id, msg, time) {
            try {
                // 获取文本节点，避免直接使用 $(e).text()
                // 通过id 获取，避免多个 layer
                var a = $(id).find('.layui-layer-content').contents().filter(function () {
                    return this.nodeType === 3;
                })[0];

                id = setInterval(function () {
                    time -= 1;
                    msg[1] = time;

                    if (a.textContent)
                        a.textContent = msg.join('');
                    else
                        a.innerText = msg.join('');

                    if (time <= 0) {
                        clearInterval(id);
                    }

                }, 1000);

            } catch (e) {
                console.error(e);
            }
        },
        // title提示
        titleInit: function () {
            // 遍历 .js--tips
            var that = this;
            var tips;
            $(document).on('mouseenter', '.js-show-tips', function (event) {
                var a, clientWidth = this.clientWidth,
                    scrollWidth = this.scrollWidth,
                    text = $(this).attr('data-tips');

                // 判断是否超出
                if (clientWidth < scrollWidth || $(this).hasClass('ellipsis-2')) {
                    // 两行隐藏的时候判断高度
                    if ($(this).hasClass('ellipsis-2')
                        && this.clientHeight < $(this).css('max-height').replace('px', '') ) {
                        return false;
                    }
                    if (this.tagName === 'INPUT') {
                        tips = that.tips(this, $.trim(this.value), {
                            time: 0
                        });
                    } else {
                        a = $(this);

                        tips = that.tips(this, $.trim(a.text()), {
                            time: 0
                        });
                        // 置空
                        a.attr('title', '');
                    }
                } else if (text) {
                    tips = that.tips(this, $.trim(text), {
                        time: 0
                    });
                }

            });

            // 移除时立即清除 dialog
            $(document).on('mouseleave', '.js-show-tips', function (event) {
                    if (tips) {
                        that.close(tips);
                        tips = null;
                    }
                })
                // 目标元素消失的情况
                .on('mouseenter', function (event) {
                    if (event.target.className.indexOf('js-show-tips') == -1) {
                        if (tips) {
                            that.close(tips);
                            tips = null;
                        }
                    }
                });
        }
    }
});