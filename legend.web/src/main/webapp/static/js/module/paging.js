/**
 * ch 2016-03-23
 * 分页插件
 */
define(function (require, exports, module) {
    var art = require('art');
    var ms = {
        callback: function (args, pageNum) {
            args.dom.get(0).pageNum = pageNum;
            ms.fillHtml($.extend({}, args, {
                "current": pageNum,
                "pageCount": args.pageCount,
                dom: args.dom,
                itemSize: args.itemSize
            }));
            typeof args.backFn == "function" && args.backFn(pageNum);
        },
        //填充html
        fillHtml: function (args) {
            var str = '';
            var obj = $(".yqx-page-inner:eq(0)", args.dom);
            var obj_right = $(".opt-right").eq(0);   //页面右上角仅仅存在上一页、下一页

            // 将分页保存到当前父级DOM上
            args.dom.get(0).pageNum = args.current;

            if (args.pageTplId) {
                args.dom.html(art(args.pageTplId, {args: args}));
                return;
            }

            str += '<div class="yqx-page-inner">';

            //上一页之前
            str += '<span class="disabled yqx-page-count">共' + args.itemSize + '条记录</span>';
            if (args.current < 10000) {
                if (args.current > 1 && args.current <= args.pageCount) {
                    str += '<a class="yqx-page-first" href="javascript:;">首页</a>';
                    str += '<a href="javascript:;" class="yqx-page-prev">上一页</a>';
                    obj_right.find("span:not(.disabled)").prev().remove();
                    obj_right.find("span:not(.disabled)").before('<a href="javascript:;" class="yqx-link yqx-page-prev">上一页</a>');
                } else {
                    str += '<span class="disabled">首页</span>';
                    str += '<span class="disabled">上一页</span>';
                    obj_right.find("span:not(.disabled)").prev().remove();
                    obj_right.find("span:not(.disabled)").before('<span class="disabled">上一页</span>');
                }
            }
            //中间页码
            if (args.current == 1) {
                str += '<span class="current">1</span>';
            } else {
                str += '<a href="javascript:;" class="yqx-page-num">1</a>';
            }
            if (args.current >= 4 && args.pageCount > 5) {
                str += '<span>...</span>';
            }
            var start = args.current - 1, end = args.current + 1;

            if (args.current > args.pageCount - 3) {
                start = args.pageCount - 3;
            }
            if (start < 1) {
                start = 1;
            }
            if (args.current < 4 && args.pageCount >= 4) {
                if (args.pageCount > 4) {
                    end = 4;
                } else if (args.pageCount == 4) {
                    end = 3;
                } else {
                    end = args.pageCount - 1;
                }
            }
            if (args.pageCount == 1) {
                end = 1;
            }

            for (; start <= end; start++) {
                // console.log(start +","+ end);
                if (start < args.pageCount && start > 1) {
                    if (start != args.current) {
                        str += '<a href="javascript:;" class="yqx-page-num">' + start + '</a>';
                    } else {
                        str += '<span class="current">' + start + '</span>';
                    }
                }
            }
            if (args.current + 1 < args.pageCount - 1 && args.pageCount > 5) {
                str += '<span>...</span>';
            }

            if (args.pageCount > 1) {
                if (args.current == args.pageCount) {
                    str += '<span class="current">' + args.pageCount + '</span>';
                } else {
                    str += '<a href="javascript:;" class="yqx-page-num">' + args.pageCount + '</a>';
                }
            }


            //下一页之后
            if (args.current < 10000) {
                if (args.current < args.pageCount) {
                    str += '<a href="javascript:;" class="yqx-page-next">下一页</a>';
                    str += '<a class="yqx-page-last" href="javascript:;">末页</a>';
                    obj_right.find("span:not(.disabled)").next().remove();
                    obj_right.find("span:not(.disabled)").after('<a class="yqx-link yqx-page-next" href="javascript:;">下一页</a>');
                } else {
                    obj.remove('.nextPage');
                    str += '<span class="disabled">下一页</span>';
                    str += '<span class="disabled yqx-page-last">末页</span>';
                    obj_right.find("span:not(.disabled)").next().remove();
                    obj_right.find("span:not(.disabled)").after('<span class="disabled">下一页</span>');
                }
            }
            if (args.pageCount <= 1) {
                str += '<span class="yqx-go-num disabled">共' + args.pageCount + '页,去第 <input type="text" disabled="disabled" value=""> 页</span>';
                str += '<span class="yqx-go-btn disabled">跳转</span>';
            } else {
                str += '<span class="yqx-go-num">共' + args.pageCount + '页,去第 <input type="text" value=""> 页</span>';
                str += '<a class="yqx-go-btn" href="javascript:;">跳转</a>';
            }

            str += '</div>';

            args.dom.html(str);
        },
        //绑定事件
        bindEvent: function (args) {
            //页码事件绑定
            args.dom.off("click", "a.yqx-page-num").on("click", "a.yqx-page-num", function () {
                var current = parseInt($(this).text()) || +location.hash.replace('#', '');
                ms.callback(args, current);
            });
            //上一页
            args.dom.off("click", "a.yqx-page-prev").on("click", "a.yqx-page-prev", function () {
                var current = parseInt(args.dom.get(0).pageNum) || +location.hash.replace('#', '');
                if (current == 1) return;
                ms.callback(args, current - 1);
            });
            //下一页
            args.dom.off("click", "a.yqx-page-next").on("click", "a.yqx-page-next", function () {
                var current = parseInt(args.dom.get(0).pageNum) || +location.hash.replace('#', '');
                if (current == args.pageCount || args.pageCount == 0) return;
                ms.callback(args, current + 1);
            });
            //首页事件
            args.dom.off("click", "a.yqx-page-first").on("click", "a.yqx-page-first", function () {
                ms.callback(args, 1);
            });
            //末页事件
            args.dom.off("click", "a.yqx-page-last").on("click", "a.yqx-page-last", function () {
                ms.callback(args, args.pageCount);
            });
            //跳转事件
            args.dom.off("click", "a.yqx-go-btn").on("click", "a.yqx-go-btn", function () {
                var current = parseInt(args.dom.get(0).pageNum) || +location.hash.replace('#', '');
                var goNumInput = $(this).prev().find("input");
                var goNum = parseInt(goNumInput.val());
                if (goNum > args.pageCount || !$.isNumeric(goNum) || goNum == current) {
                    goNumInput.val("").focus();
                    return;
                }
                ms.callback(args, goNum || current);
            });
        }
    };

    exports.init = function (options) {
        var args = $.extend({
            dom: $(".yqx-page"),
            itemSize: 0,
            pageCount: 0,
            current: 0,
            pageTplId: null,
            backFn: null
        }, options);
        if (!args.dom.length) {
            return;
        }
        args.current = +args.current;
        ms.fillHtml(args);
        ms.bindEvent(args);
    }
});