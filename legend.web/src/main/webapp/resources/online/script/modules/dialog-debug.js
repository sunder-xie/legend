//弹框模块
define("legend/resources/online/script/dialog-debug", [ "./../libs/layer/layer-debug" ], function(require, exports, module) {
    require("./../libs/layer/layer-debug");
    module.exports = {
        //普通弹框
        dialog: function(option) {
            var args = $.extend({
                type: 1,
                //弹框宽度
                width: "auto",
                //弹框高度
                height: "auto",
                //
                closeBtn: [ 1, true ],
                //dom选择器
                dom: "",
                shift: "top",
                //html代码
                html: "",
                //弹框出来后的回调方法
                init: function() {},
                //弹框关闭后的回调方法
                close: function() {}
            }, option);
            var page;
            //异步加载的url
            args.url = $(args.dom).attr("service_url") || "";
            //弹框的宽高
            //			args.area = [$(args.dom).outerWidth(),$(args.dom).outerHeight()];
            //弹框标题
            args.title = $(args.dom).attr("dialog_title") || false;
            if (args.url != "") {
                args.type = 2;
                args.iframe = {
                    src: args.url
                };
                page = {};
            } else if (args.dom != "") {
                page = {
                    dom: args.dom
                };
            } else {
                page = {
                    html: args.html
                };
            }
            var did = $.layer({
                type: args.type,
                title: args.title,
                area: [ args.width, args.height ],
                iframe: args.iframe,
                //iframe
                border: [ 0 ],
                //去掉默认边框
                shade: [ .5, "#000000" ],
                closeBtn: args.closeBtn,
                //去掉默认关闭按钮
                shift: args.shift,
                move: ".xubox_title",
                page: page,
                success: function() {
                    //布局计算
                    formLayoutCalc(args.dom);
                    //清除值。
                    if (page.dom) {
                        $("input[type='text'],select,textarea,hidden", page.dom).each(function() {
                            if ($(this)[0].tagName == "select") {
                                $(this).find("option:eq(0)").attr("selected", "selected");
                            } else {
                                $(this).val("");
                            }
                        });
                    }
                    //隐藏所有验证提示
                    if (args.dom != "") {
                        $(".qxy_err_msg", args.dom).hide();
                    }
                    args.init();
                },
                end: function() {
                    //layer.closeTips();
                    args.close && args.close();
                    did = undefined;
                    args = undefined;
                }
            });
            if (page.dom) {
                $(page.dom).data("dialog-id", did);
            }
            var layerid = "#xubox_layer" + did;
            //第一个form元素自动聚焦
            //引起页面跳屏，暂时注释
            //$("input,select",layerid).eq(0).focus();
            //绑定ESC事件
            $("body").on("keyup", layerid, function(event) {
                if (event.keyCode == "27") {
                    layer.close(did);
                }
            });
            //绑定Enter事件
            //			$(layerid).on("keyup",function(event){
            //				if(event.keyCode=="13"){
            //					var result = check.check("#"+$(layerid+" .qxy_btn[ref_form_id]").attr("ref_form_id"));
            //					if(!result){
            //						return;
            //					}
            //
            //					$(layerid+" .qxy_btn[ref_form_id]").trigger("click");
            //					layer.close(did);
            //				}
            //			})
            return did;
        },
        //确认弹框
        //yes是点确定后的回调方法
        //no是点取消后的回调方法
        confirm: function(msg, yes, no) {
            $.layer({
                area: [ "300px", "auto" ],
                border: [ 0 ],
                //去掉默认边框
                shade: [ .5, "#000000" ],
                closeBtn: [ 0, false ],
                //去掉默认关闭按钮
                shift: "top",
                dialog: {
                    msg: msg,
                    btns: 2,
                    type: 4,
                    btn: [ "确定", "取消" ],
                    yes: function() {
                        yes();
                    },
                    no: function() {
                        no && no();
                    }
                }
            });
        },
        //加载弹框
        //msg 信息
        //timer是加载框默认时间，单位为s
        load: function(msg, timer) {
            var loadi = layer.load(msg || "正在加载，请稍候...", timer || 10);
            return loadi;
        },
        //信息提示弹框
        //type:图标类型
        //0是警告图标，1是成功图标，3是失败图标，5是错误图标
        //timer信息显示的时间
        //fn信息时间停留完成后的回调
        info: function(msg, type, timer, fn) {
            timer = timer || 3;
            layer.msg(msg || "信息内容未设置", timer, {
                type: type || 0,
                shade: false,
                rate: "left"
            });
            if (fn) {
                setTimeout(function() {
                    fn && fn();
                }, timer * 1e3);
            }
        },
        //关闭弹框,参数是dialog方法返回弹窗的索引
        close: function(id) {
            layer.close(id);
        },
        //关闭弹框,参数是弹框的id
        closeDialog: function(id) {
            var did = $(id).data("dialog-id");
            did && layer.close(did);
        },
        //tips
        tips: function(obj, msg) {
            var tip = layer.tips(msg, obj, {
                guide: 0,
                isGuide: true,
                more: false,
                style: [ "background:#f77c7e; color:#fff; border:1px solid #f77c7e;top:15px;left:10px;", "#f77c7e" ]
            });
            return tip;
        }
    };
});