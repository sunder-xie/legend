;(function(win, undefined) {
    win.util = {
        //加法函数
        Jia : function(arg1, arg2) {
            var r1, r2, m;
            try {
                r1 = arg1.toString().split(".")[1].length;
            }
            catch (e) {
                r1 = 0;
            }
            try {
                r2 = arg2.toString().split(".")[1].length;
            }
            catch (e) {
                r2 = 0;
            }
            m = Math.pow(10, Math.max(r1, r2) + 1);
            return (arg1 * m + arg2 * m) / m;
        },
        //减法函数
        Jian : function (arg1, arg2) {
            var r1, r2, m, n;
            try {
                r1 = arg1.toString().split(".")[1].length;
            }
            catch (e) {
                r1 = 0;
            }
            try {
                r2 = arg2.toString().split(".")[1].length;
            }
            catch (e) {
                r2 = 0;
            }
            m = Math.pow(10, Math.max(r1, r2));
            //last modify by deeka
            //动态控制精度长度
            n = (r1 >= r2) ? r1 : r2;
            return Number(((arg1 * m - arg2 * m) / m).toFixed(n));
        },
        //乘法函数
        Cheng : function(arg1, arg2) {
            var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
            try {
                m += s1.split(".")[1].length;
            }
            catch (e) {
            }
            try {
                m += s2.split(".")[1].length;
            }
            catch (e) {
            }
            return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
        },
        //除法函数
        Chu : function(arg1, arg2) {
            var t1 = 0, t2 = 0, r1, r2;
            try {
                t1 = arg1.toString().split(".")[1].length;
            }
            catch (e) {}
            try {
                t2 = arg2.toString().split(".")[1].length;
            }
            catch (e) {}
            r1 = Number(arg1.toString().replace(".", ""));
            r2 = Number(arg2.toString().replace(".", ""));
            return (r1 / r2) * Math.pow(10, t2 - t1);
        },
        //是否是谷歌浏览器
        isChrome: function () {
            var str = window.navigator.userAgent;
            return /chrome/gi.test(str);
        },
        // fn { beforePrint: function, afterPrint: function }
        print: function (url, fn) {
            if (util.isChrome()) {
                var iframe = $('<iframe style="height: 0;"></iframe>').attr('src', url);

                // 打印的回调
                if(fn) {
                    iframe.on('load', function () {
                        // 获取 iframe 的 window
                        if (iframe[0].contentWindow.matchMedia) {
                            var mediaQueryList = iframe[0].contentWindow.matchMedia('print');
                            mediaQueryList.addListener(function (mql) {
                                if (mql.matches) {
                                    // 注意该函数执行的环境
                                    fn.beforePrint && fn.beforePrint();
                                } else {
                                    fn.afterPrint && fn.afterPrint();
                                }
                            });
                        }
                    });
                }

                $("body").append(iframe);
            } else {
                window.open(url);
            }
        },
        /** wry 2016-04-26
         *
         * @param opt [url, -1]
         */
        goBack: function (opt) {
            var location = window.location,
                history = window.history,
                opener = window.opener,
                referrer = document.referrer,
                state = null,
                index = opt || -1;
            if (typeof opt === 'string' && /^\//.test(opt)) {
                location.href = opt;
            } else {
                // reload之后referrer地址变为当前地址fix
                state = history.go(-1);
                if (!state) {
                    if (referrer && referrer != location.href) {
                        location.href = referrer
                    } else if (opener) {
                        location.href = opener.location.href
                    }
                }
            }
        },
        /**
         * 判断按钮是否有权限，display-none需要特殊加样式
         * @param funcName 权限名称，如工单删除
         * @param opt 某个对象，如".js-common-delete"
         */
        checkFunc:function(funcName,opt){
            $.ajax({
                type: 'GET',
                url: BASE_PATH + '/shop/func/check_func',
                data: {
                    funcName: funcName
                },
                success: function (result) {
                    if (result.success) {
                        var data = result.data;
                        if (data) {
                            //有权删除
                            $(opt).removeClass("display-none");
                        }
                    }
                }
            });
        },
        /**
         * 判断按钮是否有权限,没有则提示错误信息
         * @param funcNameList 权限列表，以，隔开的字符串
         */
        checkFuncList:function(funcNameList, async, fn){
            var flag = false;
            async = async || false;
            $.ajax({
                type: 'GET',
                url: BASE_PATH + '/shop/func/check_func_list',
                data: {
                    funcNameList: funcNameList
                },
                async: async,
                success: function (result) {
                    fn && fn(result);
                    if (result.success) {
                        flag = true;
                    }
                }
            });
            if(!flag && async == false){
                //无权限
                seajs.use('dialog', function (dg) {
                    dg.warn("您的账号没有此操作权限，请联系管理员进行开通");
                });
            }
            return flag;
        },
        getPara: function(name) {
            var reg = new RegExp('(\\?|&)' + name + '=([^&?]*)', 'i');
            var arr = location.search.match(reg);
            if (arr) {
                return arr[2];
            }
            return '';
        },
        /**
         * 获得cookie值，如果不传参，返回cookie集合
         * @param name
         * @returns {*}
         */
        getCookie:function (name) {
            var i, entries, cookies = {}, reg, cookie, match;
            if (name == null) {
                entries = document.cookie.split(';');
                $.each(entries, function (idx, entry) {
                    var tmp;
                    if (entry) {
                        tmp = $.trim(entry).split('=');
                        cookies[tmp[0]] = encodeURIComponent(tmp[1]);
                    }
                });

                return cookies;
            } else {
                reg = new RegExp('(?:^|;\\s*)' + name + '=(\\S+?)(?:$|;\\s*)');
                match = document.cookie.match(reg);
                return match ? encodeURIComponent(match[1]) : '';
            }
        },
        // 图片按比例放大，原图是大图的时候
        // height\width 其中一个固定
        imgZoomBigger: function (opt) {
            opt = opt || {};
            var height, width, src;
            var _width = 712, MAX_WIDTH = opt.maxWidth || 1000,
                MAX_HEIGHT = opt.maxHeight || 700;

            if(!opt || typeof opt !== 'object')
                opt = {};
            if(!opt.height) {
                width = opt.width || _width;
            }
            if( this.tagName === 'IMG' && (src = $(this).attr('src')) ) {
                // 取整
                if(width) {
                    height = (width / this.naturalWidth * this.naturalHeight) >> 0;
                } else {
                    height = opt.height;
                    width = (height / this.naturalHeight * this.naturalWidth) >> 0;
                }
                height = height > MAX_HEIGHT ? MAX_HEIGHT : height;
                width  = width  > MAX_WIDTH ? MAX_WIDTH : width;

                height += 'px';
                width  += 'px';

                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: 1,
                    area: [width, height],
                    skin: 'layui-layer-nobg', //没有背景色
                    shadeClose: true,
                    content: '<img src=' + src + '  style="width:' + width
                    + ';height:' + height + ';"' + '>'
                });
            }
        }
    };

    /**** ?Number对象扩展 start ****/
//加
    Number.prototype.Jia = function(arg) {
        return util.Jia(arg, this);
    };
//减
    Number.prototype.Jian = function(arg) {
        return util.Jian(this, arg);
    };
//乘
    Number.prototype.Cheng = function(arg) {
        return util.Cheng(arg, this);
    };
//除
    Number.prototype.Chu = function(arg) {
        return util.Chu(this, arg);
    };

    Number.prototype.priceFormat = function () {
        var reg = /\B(?=(\d{3})+(?!\d))/g;
        var t = this.toString();

        if(t.indexOf('.') > -1) {
            t = this.toFixed(2);
        }
        t = t.split('.');
        t[0] = t[0].replace(reg, ',');

        if(t[1]) {
            t = t[0] + '.' + t[1];
        } else {
            t = t[0];
        }
        return t;
    };
    /**** ?Number对象扩展 end ****/

    /**** Date start ***/
    // 时间格式化
    Date.prototype.getFormat = function (format) {
        var date = {
            year: this.getFullYear(),
            month: this.getMonth() + 1,
            day: this.getDate(),
            hour: this.getHours(),
            min: this.getMinutes(),
            ss: this.getSeconds()
        };

        if (!format || typeof format !== 'string') {
            return this.getFormatCN('yyyy-MM-dd');
        } else {
            if (format == 'all') {
                return this.getFormatCN('yyyy-MM-dd hh:mm:ss');
            } else {
                format = format
                    .replace(/(y*)?.?(M*)?.?(d*)?.?(h*)?.?(m*)?.?(s*)?/,
                        function (match, year, mon, day, hour, min, ss) {
                            var t = '';

                            if (year) {
                                t = dateReplacer(date.year, 4, year);
                            }
                            if (mon) {
                                t += '-' + dateReplacer(date.month, 2, mon);
                            }
                            if (day) {
                                t += '-' + dateReplacer(date.day, 2, day);
                            }

                            if (hour) {
                                t += ' ' + dateReplacer(date.hour, 2, hour);
                            }

                            if (min) {
                                t += ':' + dateReplacer(date.min, 2, min);
                            }

                            if (ss) {
                                t += ':' + dateReplacer(date.ss, 2, ss);
                            }

                            return t;
                        });
            }
        }

        return format;
    };

    function dateReplacer(str, len, match) {
        var t = match.length > len ? len : match.length;

        return str ? addPaddingZero(str).slice(-t) : '0'.repeat(t);
    }

    function addPaddingZero(num) {
        if (num < 10 && num.toString().length < 2) {
            num = '0' + num;
        }

        return num.toString();
    }
    /**** Date end ***/

    /****?String对象扩展 start ****/
    String.prototype.format = function() {
        var v = this.toString(),
            args = arguments;
        return v.replace(/\{(\d+)\}/g, function ($0, $1) {
            return args[$1];
        });
    };
    /****?String对象扩展 end ****/

})(window);