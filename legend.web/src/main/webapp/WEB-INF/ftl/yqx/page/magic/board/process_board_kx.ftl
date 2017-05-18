<#include "yqx/layout/kb-header.ftl">
<base href="<%=basePath%>">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/process_board.css?16a139cba2dd2f4cebf76f2f49701c77"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/bxslider.css?50b6cbaa122486731d7c4f0f4433e31d"/>
<!-- 样式引入区 end-->
<div class="fluid-wrapper clearfix">
<#include "yqx/layout/kb-top.ftl">
<#include "yqx/layout/kb-aside.ftl">
    <div class="main">
        <div class="title clearfix">
            <p class="title_name fl">${lineName!''}工序看板</p>
            <ul class="sumary fr">
                <li>全部（<span id="allCount"></span>）</li>
                <li><span class="icon-normal icon-sumary fl"></span>正常（<span id="normalCount"></span>）</li>
                <li><span class="icon-edge icon-sumary fl"></span>临界（<span id="criticalCount"></span>）</li>
                <li><span class="icon-timeout icon-sumary fl"></span>超时（<span id="timeOutCount"></span>）</li>
            </ul>
        </div>
        <div class="loadplate">
            <ul id="lineBox" class="flow-col-4"></ul>
            <div class="flow-col-1">
                <div class="interrupt-space"></div>
                <div id="breakRepair" class="interrupt"></div>
                <div class="interrupt-space"></div>
            </div>
        </div>
    </div>
</div>




<script src="${BASE_PATH}/static/js/common/bxslider.js?f6adf0b58ffa49fbd9815cff3b78fd95" type="text/javascript"></script>
<script>

/**
 * @fileOverview easeAni
 * @author 张海勇
 * @version 1.0
 */


var easeAni = (function () {

    Function.prototype.delay = function () {
        var _this = this;
        var arg = Array.prototype.slice.call(arguments);
        return setTimeout(function () {
            _this.apply(arg.shift(), arg);
        }, arg.shift());
    };

    String.prototype.format = function() {
        for (var i = 0, val = this, len = arguments.length; i < len; i++)
            val = val.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
        return val;
    };

    function easeAni(dom, x, y, z) {
        this.e_ = dom;
        this.prefix = easeAni.prefix;
        this.status = {
            //public:

            /**
             x轴座标,可读属性
             * @type {Number}
             */
            x: 0,

            /**
             y轴座标,可读属性
             * @type {Number}
             */
            y: 0,

            /**
             z轴座标,可读属性
             * @type {Number}
             */
            z: 0,

            /**
             x轴缩放比例尺,可读属性
             * @type {Number}
             */
            scaleX: 1,

            /**
             y轴缩放比例尺,可读属性
             * @type {Number}
             */
            scaleY: 1,

            /**
             z轴缩放比例尺,可读属性
             * @type {Number}
             */
            scaleZ: 1,

            /**
             x轴旋转角度,360为旋转一周,可读属性
             * @type {Number}
             */
            rotateX: 0,

            /**
             y轴旋转角度,360为旋转一周,可读属性
             * @type {Number}
             */
            rotateY: 0,

            /**
             z轴旋转角度,360为旋转一周,可读属性
             * @type {Number}
             */
            rotateZ: 0,

            /**
             x轴倾斜度,可读属性
             * @type {Number}
             */
            skewX: 0,

            /**
             y轴倾斜度,可读属性
             * @type {Number}
             */
            skewY: 0,

            opacity: 1

            //public:
        };
        this.cssbox = '';
        this.callback = null;
        this.incss = this.prefix +
                'transform-style:preserve-3d;' +
                this.prefix +
                'transform-origin:' +
                (x != undefined ? x + 'px ' : '50% ') +
                (y != undefined ? y + 'px ' : '50% ') +
                (z != undefined ? z + 'px;' : '0;');
        this.set();
        var _this = this;
        this.bang(easeAni.transitionEndName, function () {
            _this.set();
            if (_this.callback) {
                _this.callback();
                _this.callback = null;
            }
        });
    }
    easeAni.prototype = {

        constructor: easeAni,

        set: function (params) {
            if (params != undefined) {
                if (params.x != undefined) {
                    this.status.x = params.x;
                }
                if (params.y != undefined) {
                    this.status.y = params.y;
                }
                if (params.z != undefined) {
                    this.status.z = params.z;
                }
                if (params.scaleX != undefined) {
                    this.status.scaleX = params.scaleX;
                }
                if (params.scaleY != undefined) {
                    this.status.scaleY = params.scaleY;
                }
                if (params.scaleZ != undefined) {
                    this.status.scaleZ = params.scaleZ;
                }
                if (params.rotateX != undefined) {
                    this.status.rotateX = params.rotateX;
                }
                if (params.rotateY != undefined) {
                    this.status.rotateY = params.rotateY;
                }
                if (params.rotateZ != undefined) {
                    this.status.rotateZ = params.rotateZ;
                }
                if (params.skewX != undefined) {
                    this.status.skewX = params.skewX;
                }
                if (params.skewY != undefined) {
                    this.status.skewY = params.skewY;
                }
                if (params.opacity != undefined) {
                    this.status.opacity = params.opacity;
                }
            }
            this.cssbox = this.cssbox +
                    'opacity:' +
                    this.status.opacity +
                    ';' +
                    this.prefix +
                    'transform:' +
                    'rotateX(' + this.status.rotateX + 'deg) ' +
                    'rotateY(' + this.status.rotateY + 'deg) ' +
                    'rotateZ(' + this.status.rotateZ + 'deg) ' +
                    'translateX(' + this.status.x + 'px) ' +
                    'translateY(' + this.status.y + 'px) ' +
                    'translateZ(' + this.status.z + 'px) ' +
                    'scaleX(' + this.status.scaleX + ') ' +
                    'scaleY(' + this.status.scaleY + ') ' +
                    'scaleZ(' + this.status.scaleZ + ') ' +
                    'skewX(' + this.status.skewX + 'deg) ' +
                    'skewY(' + this.status.skewY + 'deg);';
            this.e_.style.cssText = this.incss + this.cssbox;
            this.cssbox = '';
        },

        animate: function (trans, end, callback) {
            this.cssbox = this.prefix +
                    'transition:all ' +
                    trans.time +
                    's ' +
                    (trans.cub ? this.cubic(trans.cub) : 'ease') +
                    ' ' +
                    (trans.delay || '0') +
                    's;';
            this.callback = callback;
            this.set(end);
        },

        cubic: function (val) {
            switch (val) {
                case 1:
                    return 'ease';
                    break;
                case 2:
                    return 'linear';
                    break;
                case 3:
                    return 'ease-in';
                    break;
                case 4:
                    return 'ease-out';
                    break;
                case 5:
                    return 'ease-in-out';
                    break;
                default:
                    return val;
            }
        },

        bang: function (event, fun, type) {
            this.e_.addEventListener(event, fun, type || false);
        },

        unbang: function (event, fun, type) {
            this.e_.removeEventListener(event, fun, type || false);
        },

        getCss: function (attr) {
            return window.getComputedStyle(this.e_, null).getPropertyValue(attr);
        },

        current: function () {
            var obj = eval('this.' + this.getCss(this.prefix + 'transform'));
            obj.opacity = this.getCss('opacity');
            return obj;
        },

        stop: function () {
            this.set(this.current());
            this.callback = null;
        },

        matrix3d: function (a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p) {
            return {
                x: m,
                y: n,
                z: o
            };
        },

        matrix: function (a, b, c, d, e, f) {
            return {
                x: e,
                y: f
            };
        }

    };

    easeAni.prefix = (function () {
        var ua = navigator.userAgent;
        if (ua.indexOf('AppleWebKit/') > -1)
            return '-webkit-';
        if (ua.indexOf('Trident/') > -1)
            return '-ms-';
        if (ua.indexOf('Gecko/') > -1)
            return '-moz-';
        if (ua.indexOf('Presto/') > -1)
            return '-o-';
        return '';
    })();
    easeAni.transitionEndName = (function () {
        switch (easeAni.prefix) {
            case '-webkit-':
                return 'webkitTransitionEnd';
            case '-ms-':
                return 'MSTransitionEnd';
            case '-moz-':
                return 'transitionend';
            case '-o-':
                return 'oTransitionEnd';
            default:
                return 'transitionend';
        }
    })();

    return easeAni;

})();


    $(function () {

        var $allCount = $('#allCount'),
            $normalCount = $('#normalCount'),
            $criticalCount = $('#criticalCount'),
            $timeOutCount = $('#timeOutCount'),
            $breakRepair = $('#breakRepair'),
            itemList = [],
            columnsMap = [
                {
                    id: 0,
                    cluClass: 'wait-plate',
                    bNum: 1,
                    listClass: 'wait-list',
                    waiter: false
                },
                {
                    id: 1,
                    cluClass: 'banjin-plate',
                    bNum: 2,
                    listClass: 'banjin-list',
                    waiter: true
                },
                {
                    id: 2,
                    cluClass: 'zuodi-plate',
                    bNum: 3,
                    listClass: 'zuodi-list',
                    waiter: true
                },
                {
                    id: 3,
                    cluClass: 'zhongtu-plate',
                    bNum: 4,
                    listClass: 'zhongtu-list',
                    waiter: true
                },
                {
                    id: 4,
                    cluClass: 'mianqi-plate',
                    bNum: 5,
                    listClass: 'mianqi-list',
                    waiter: true
                },
                {
                    id: 5,
                    cluClass: 'mianqi-plate',
                    bNum: 5,
                    listClass: 'mianqi-list',
                    waiter: true
                },
                {
                    id: 6,
                    cluClass: 'paoguang-plate',
                    bNum: 6,
                    listClass: 'paoguang-list',
                    waiter: true
                },
                {
                    id: 7,
                    cluClass: 'zhongtu-plate',
                    bNum: 4,
                    listClass: 'zhongtu-list',
                    waiter: true
                },
                {
                    id: 8,
                    cluClass: 'paoguang-plate',
                    bNum: 6,
                    listClass: 'paoguang-list',
                    waiter: true
                },
                {
                    id: -1,
                    cluClass: 'complete-plate',
                    bNum: 7,
                    listClass: 'complete-list',
                    waiter: false
                },
                {
                    id: -2
                }
            ],
            itemStatus = [
                    '',
                    'icon-normal',
                    'icon-edge',
                    'icon-timeout',
                    'icon-wait'
            ],
            MyWS = null,
            $lineBox = $('#lineBox');

        /*
        *
        *
        *                 aniControl: ani,
                isHandle: !!flag,
                $item: $li,
                processId: processId,
                workOrderId: data.workOrderId,
                subTime: data.subTime,
                data: data

        *
        *
        * */

        function getItemByWorkOrderId(id) {
            var i = 0, l = itemList.length, res = null;
            for (; i < l; i++) {
                if (id === itemList[i].workOrderId) {
                    res = itemList[i];
                    break;
                }
            }
            return res;
        }

        function parseUrl() {
            var searchStr = window.location.search.substr(1),
                    searchArr = searchStr.split("&"),
                    searchObj = {},
                    i = 0,
                    midArr;
            for (; i < searchArr.length; i++) {
                midArr = searchArr[i].split("=");
                searchObj[midArr[0]] = midArr[1];
            }
            return searchObj;
        }

        var pageP = parseUrl();

        var lineType;

        initData(pageP.lineId, function (json) {
            if (json.success) {
                lineType = json.data.lineType;
                initUI(json.data);
                setTimer();
                MyWS = new Socket(pageP.lineId);
            } else {
                alert('面板初始化失败！');
            }
        });

        function Socket(id) {
            this.url = '${socketUrl}websocket/'+id;
            this.init();
        }

        Socket.prototype = {

            constructor: Socket,

            init: function () {
                if (window.WebSocket) {
                    this.mySocket = new WebSocket(this.url);
                    this.bindEvent();
                } else {
                    console.log('此浏览器不支持socket通信！');
                }
            },

            bindEvent: function () {
                var ws = this.mySocket;
                ws.addEventListener('open', this.onopen.bind(this));
                ws.addEventListener('message', this.onmessage.bind(this));
                ws.addEventListener('error', this.onerror.bind(this));
                ws.addEventListener('close', this.onclose.bind(this));
            },

            send: function (msg) {
                msg = JSON.stringify(msg);
                return this.mySocket.send(msg);
            },

            close: function () {
                return this.mySocket.close();
            },

            onopen: function (evt) {
                console.log('open: yes ok')
            },

            onmessage: function (evt) {
                var json = JSON.parse(evt.data);
                console.log(json);
                switch (json.actionType) {
                    case 1:
                        this.addAction(json);
                        break;
                    case 2:
                        this.moveAction(json);
                        break;
                    case 3:
                        this.leaveAction(json);
                        break;
                }
            },

            addAction: function (data) {
                updateTwinkle(data);
                var pid = data.toProcess;
                var $ul = getColumnByProcessId(pid).$box.find('.being');
                var c = createAnimationControl(data, pid);
                itemList.push(c);
                $ul.append(c.$item);
                cloumnTitlePlus(pid);
            },

            leaveAction: function (data) {
                var ctrl = getItemByWorkOrderId(data.workOrderId);
                removeItem(ctrl);
                ctrl.$item.fadeOut('normal', function () {
                    $(this).remove();
                });
                cloumnTitleReduce(-1);
            },

            /*
            *
            *                 aniControl: ani,
                isHandle: !!flag,
                $item: $li,
                processId: processId,
                workOrderId: data.workOrderId,
                subTime: data.subTime,
                data: data

            * */


            moveAction: function (data) {
                updateTwinkle(data);
                var ctrl = getItemByWorkOrderId(data.workOrderId);
                var toclo = getColumnByProcessId(data.toProcess).$box;
                var $ul = getProcessPartDom(toclo, data.toProcessPart);
                var of1 = ctrl.$item.offset();
                var of2 = $ul.offset();
                var h2 = $ul.outerHeight();
                var w1 = ctrl.$item.outerWidth();
                var h1 = ctrl.$item.outerHeight();
                var $fp = $('<ul class="plate-list" style="position: absolute;left: {0}px;top: {1}px;width: {2}px;z-index: 8888;height: {3}px;"></ul>'.format(of1.left, of1.top, w1, h1));
                ctrl.$item.detach();
                $fp.append(ctrl.$item);
                $('body').append($fp);
                var x = of2.left - of1.left + 7, y = of2.top - of1.top + h2 - h1 - 2;
                var _this = this;
                var pid = data.toProcess;
                if (pid == -1) {
                    y -= h2;
                }
                ctrl.aniControl.animate.delay(10, ctrl.aniControl, {
                    time: 2,
                    cub: 2
                }, { x: x, y: y }, function () {
                    ctrl.$item.detach();
                    _this.updateContent(ctrl, data);
                    if (pid == -1) {
                        $ul.prepend(ctrl.$item);
                    } else {
                        $ul.append(ctrl.$item);
                    }
                    $fp.remove();
                    this.set({ x: 0, y: 0 });
                });
                cloumnTitlePlus(data.toProcess);
                cloumnTitleReduce(data.fromProcess);
            },

            updateContent: function (ctrl, data) {
                var state = itemStatus[data.workOrderStatus];
                var ts = timeFormat(data.subTime);
                ctrl.subTime = data.subTime;
                var processId = ctrl.processId = data.toProcess;
                $.extend(ctrl.data, data);
                data = ctrl.data;
                var s = '';
                if (processId == 0) {
                    s = '<p>接待员：{0}</p><p>{1} {2}</p><p>等待：<span class="sub-time">{3}</span></p>'.format(data.saName, data.processName, data.operatorName, ts);
                } else if (processId == -1) {
                    s = '<p>接待员：{0}</p><p>超时：<span class="sub-time">{1}</span></p><p>交车：{2}</p>'.format(data.saName, ts, data.expectTimeStr);
                } else if (processId == -2) {
                    s = '<p>中断人：{0}</p><p>{1}</p><p>等待：<span class="sub-time">{2}</span></p>'.format(data.operatorName, data.processName, ts);
                } else if (data.toProcessPart == 'SGZ') {
                    ctrl.isHandle = true;
                    s = '<p>{0}</p><p>剩余：<span class="sub-time">{1}</span></p>'.format(data.operatorName, ts);
                } else {
                    ctrl.isHandle = false;
                    var tg = '';
                    if (processId == 4 && data.letdownStatus) {
                        tg = '<p>{0}</p>'.format(data.letdownStatus);
                    }
                    s = '<p>{0} {1}</p>{3}<p>等待：<span class="sub-time">{2}</span></p>'.format(data.processName, data.operatorName, ts, tg);
                }
                var str = '<span class="{0} icon-state"></span><p class="f14 c3">{1}</p>'.format(state, data.carLicense) + s;
                ctrl.$item.html(str);
            },

            onerror: function (evt) {
                console.log('error:' + evt);
            },

            onclose: function (evt) {
                console.log('close: bye bye');
            }

        };

        function removeItem(item) {
            for (var i = 0, l = itemList.length; i < l; i++) {
                if (item === itemList[i]) {
                    itemList.splice(i, 1);
                    return true;
                }
            }
            return false;
        }

        function getProcessPartDom($box, p) {
            if (p == 'DSG') {
                return $box.find('.waiting');
            } else {
                return $box.find('.being');
            }
        }

        function setTimer() {
            var t = ts = 2000;
            var timeId = setTimeout(function () {
                var t1 = new Date().getTime();
                clearTimeout(timeId);
                action(ts);
                ts = new Date() - t1;
                if (t > ts) {
                    t1 = t - ts;
                    ts = t;
                } else {
                    t1 = 0;
                }
                timeId = setTimeout(arguments.callee, t1);
            }, t);
        }

        function action(ts) {
            ts /= 1000;
            var l = itemList.length;
            var nums = {
                allCount: l,
                criticalCount: 0,
                normalCount: 0,
                timeOutCount: 0
            };
            for (var i = 0; i < l; i++) {
                var ctrl = itemList[i];
                var sub = ctrl.subTime;
                var pid = ctrl.processId;
                if (typeof sub == 'number' && pid != -1) {
                    if (ctrl.isHandle) {
                        ctrl.subTime = sub -= ts;
                    } else {
                        ctrl.subTime = sub += ts;
                    }
                }
                var $li = ctrl.$item;
                $li.find('.sub-time').text(timeFormat(sub));
                updateStatus(ctrl);
                switch (ctrl.data.workOrderStatus) {
                    case 1:
                        nums.normalCount++;
                        break;
                    case 2:
                        nums.criticalCount++;
                        break;
                    case 3:
                        nums.timeOutCount++;
                        break;
                }
            }
            updateNavUI(nums);
        }

        function cloumnTitlePlus(id) {
            var c = getColumnByProcessId(id);
            var $t = c.$box.find('span.number');
            var d = c.data;
            d.carHandleCount++;
            $t.text(d.carHandleCount);
        }

        function cloumnTitleReduce(id) {
            var c = getColumnByProcessId(id);
            var $t = c.$box.find('span.number');
            var d = c.data;
            d.carHandleCount--;
            $t.text(d.carHandleCount);
        }

        function updateTwinkle(data) {
            var arr = data.glintChangeDTOs;
            if (!arr)
                return;
            for (var i = 0; i < arr.length; i++) {
                var obj = arr[i];
                var ctrl = getItemByWorkOrderId(obj.workOrderId);
                if (ctrl) {
                    ctrl.data.glint = obj.glint;
                }
            }
        }

        function iconTwinkle(ctrl) {
            var data = ctrl.data;
            var $li = ctrl.$item;
            var $sp = $li.children('.icon-state');
            var gl = data.glint;
            if (gl == 1 && !$sp.hasClass('iTwinkle')) {
                $sp.addClass('iTwinkle');
            } else if (gl == 2 && $sp.hasClass('iTwinkle')) {
                $sp.removeClass('iTwinkle');
            }
        }

        function updateStatus(ctrl) {
            var data = ctrl.data;
            if (data.workOrderStatus == 3 || data.workOrderStatus == 4 || data.overTime == null || data.criticalTime == null) {
                if (ctrl.isHandle && data.workOrderStatus == 3) {
                    iconTwinkle(ctrl);
                }
                return;
            }
            var $li = ctrl.$item;
            var $sp = $li.children('.icon-state');
            var sub = ctrl.subTime;
            var ot = data.overTime;
            var ct = data.criticalTime;
            if (ctrl.isHandle) {
                if (sub <= ot) {
                    data.workOrderStatus = 3;
                    $sp.attr('class', 'icon-state ' + itemStatus[3]);
                    iconTwinkle(ctrl);
                } else if (sub <= ct) {
                    data.workOrderStatus = 2;
                    $sp.attr('class', 'icon-state ' + itemStatus[2]);
                }
            } else {
                if (sub >= ot) {
                    data.workOrderStatus = 3;
                    $sp.attr('class', 'icon-state ' + itemStatus[3]);
                } else if (sub >= ct) {
                    data.workOrderStatus = 2;
                    $sp.attr('class', 'icon-state ' + itemStatus[2]);
                }
            }
        }

        function initSlider() {
            $('.interruptbox').bxSlider({

                slideWidth: 120,

                infiniteLoop: false,

                minSlides: 4,

                maxSlides: 4,

                pager: false,

                moveSlides: 1,

                slideMargin: 10

            });
        }

        function initUI(data) {
            initWaitRepairUI(data.waitRepair);
            initProcessRepairUI(data.processRepair);
            initFinishRepairUI(data.finishRepair);
            initBreakRepairUI(data.breakRepair);
            updateNavUI(data);
            initSlider();
        }

        function updateNavUI(data) {
            $('#allCount').text(data.allCount);
            $('#normalCount').text(data.normalCount);
            $('#criticalCount').text(data.criticalCount);
            $('#timeOutCount').text(data.timeOutCount);
        }

        function initWaitRepairUI(data) {
            var $box = createColumnDom(data);
            $box.appendTo($lineBox);
            var list = data.handleBoardCar;
            var $ul = $box.find('ul.being');
            for (var i = 0; i < list.length; i++) {
                var ctrl = createAnimationControl(list[i], data.processId);
                itemList.push(ctrl);
                $ul.append(ctrl.$item);
            }
        }

        function initFinishRepairUI(data) {
            var $box = createColumnDom(data);
            $box.appendTo($lineBox);
            var list = data.handleBoardCar;
            var $ul = $box.find('ul.being');
            for (var i = 0; i < list.length; i++) {
                var ctrl = createAnimationControl(list[i], data.processId);
                itemList.push(ctrl);
                $ul.append(ctrl.$item);
            }
        }

        function initBreakRepairUI(data) {
            var str = ('<p class="plate-top c3 interrupt-top">{0}（<span class="number">{1}</span>）</p>'
                + '<div class="interrupt-center"><div class="prev"><span class="icon-prev"></span></div>'
                + '<ul class="plate-list interruptbox being interrupt-list"></ul><div class="next"><span class="icon-next"></span></div></div>')
                .format(data.processName, data.carHandleCount);
            var $box = $(str);
            var obj = getColumnByProcessId(data.processId);
            obj.$box = $box;
            obj.data = data;
            $box.appendTo($breakRepair);
            var list = data.handleBoardCar;
            var $ul = $breakRepair.find('ul.being');
            for (var i = 0; i < list.length; i++) {
                var ctrl = createAnimationControl(list[i], data.processId);
                itemList.push(ctrl);
                $ul.append(ctrl.$item);
            }
        }

        function initProcessRepairUI(data) {
            for (var i = 0; i < data.length; i++) {
                var $box = createColumnDom(data[i]);
                $box.appendTo($lineBox);
                var list = data[i].handleBoardCar;
                var list2 = data[i].waitBoardCar;
                var $ul = $box.find('ul.being');
                var $ul2 = $box.find('ul.waiting');
                for (var j = 0; j < list.length; j++) {
                    var ctrl = createAnimationControl(list[j], data[i].processId, true);
                    itemList.push(ctrl);
                    $ul.append(ctrl.$item);
                }
                for (var j = 0; j < list2.length; j++) {
                    var ctrl = createAnimationControl(list2[j], data[i].processId);
                    itemList.push(ctrl);
                    $ul2.append(ctrl.$item);
                }
            }
        }

        function getColumnByProcessId(id) {
            var res = null;
            for (var i = 0; i < columnsMap.length; i++) {
                if (id === columnsMap[i].id) {
                    res = columnsMap[i];
                    break;
                }
            }
            return res;
        }

        function timeFormat(time) {
            if (typeof time != 'number') {
                return '无';
            }
            var h = 0, m = 0, x = '';
            if (time < 0) {
                x = '-';
                time = -time;
            }
            time = Math.ceil(time / 60);
            m = time % 60;
            h = (time - m) / 60;
            return '{2}{0}小时{1}分'.format(h, m, x);
        }

        function createAnimationControl(data, processId, flag) {
            var state = itemStatus[data.workOrderStatus];
            var ts = timeFormat(data.subTime);
            var s = '';
            if (processId == 0) {
                var sdd = '{0} {1}'.format(data.processName, data.operatorName);
                if (lineType == 2) {
                    sdd = '等待派工';
                }
                s = '<p>接待员：{0}</p><p>{1}</p><p>等待：<span class="sub-time">{2}</span></p>'.format(data.saName, sdd, ts);
            } else if (processId == -1) {
                s = '<p>接待员：{0}</p><p>超时：<span class="sub-time">{1}</span></p><p>交车：{2}</p>'.format(data.saName, ts, data.expectTimeStr);
            } else if (processId == -2) {
                s = '<p>中断人：{0}</p><p>{1}</p><p>等待：<span class="sub-time">{2}</span></p>'.format(data.operatorName, data.processName, ts);
            } else if (flag) {
                s = '<p>{0}</p><p>剩余：<span class="sub-time">{1}</span></p>'.format(data.operatorName, ts);
            } else {
                var tg = '';
                if (processId == 4 && data.letdownStatus) {
                    tg = '<p>{0}</p>'.format(data.letdownStatus);
                }
                s = '<p>{0} {1}</p>{3}<p>等待：<span class="sub-time">{2}</span></p>'.format(data.processName, data.operatorName, ts, tg);
            }
            var str = '<li><span class="{0} icon-state"></span><p class="f14 c3">{1}</p>'.format(state, data.carLicense) + s + '</li>';
            var $li = $(str);
            var ani = new easeAni($li[0]);
            return {
                aniControl: ani,
                isHandle: !!flag,
                $item: $li,
                processId: processId,
                workOrderId: data.workOrderId,
                subTime: data.subTime,
                data: data
            };
        }

        function createColumnDom(data) {
            var obj = getColumnByProcessId(data.processId);
            if (!obj) {
                return null;
            }
            var miqigao = '';
            if (lineType == 3 && data.processId == 4) {
                obj.waiter = false;
                miqigao = ' mianqigao';
            }
            var closeHtml = '</li>';
            var html = '<li class="flow-plate {0}"><p class="plate-top b{1}">{2}（<span class="number">{3}</span>）</p><ul class="plate-list{5} being {4}"></ul>'
                    .format(obj.cluClass, obj.bNum, data.processName, data.carHandleCount, obj.listClass, miqigao);
            if (obj.waiter) {
                html += '<div class="direction"><span class="icon-go"></span><span class="icon-up"></span></div>'
                        + '<ul class="plate-list waiting {0}"></ul><div class="direction"><span class="icon-down"></span><span class="icon-up"></span></div>'
                                .format(obj.listClass);
            }
            var $b = $(html + closeHtml);
            obj.$box = $b;
            obj.data = data;
            return $b;
        }

        function initData(line, cb) {
            var url = '{0}/board/queryBoardProcess/{1}'.format(BASE_PATH, line);
            return $.getJSON(url, cb);
        }

    });
</script>
<#include "yqx/layout/kb-footer.ftl">

