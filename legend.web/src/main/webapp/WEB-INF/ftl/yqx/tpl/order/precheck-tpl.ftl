<#--
    外观检查
    zhy 2016-07-13

    用到的页面：
    预检单：precheck.ftl
    预检单打印：precheck-print.ftl
    综合维修单：magic-common-add.ftl
    综合维修单打印：common-order-print.ftl
 -->


<link rel="stylesheet" href="${BASE_PATH}/static/css/page/precheck/precheck-tpl.css?1a87cccfb69f866165de36af51182d6b"/>

<div class="carBox" data-tpl-ref="precheck-tpl">
    <ul class="actLayer">
        <li class="act1"></li>
        <li class="act2"></li>
        <li class="act3"></li>
        <li class="act4"></li>
        <li class="act5"></li>
        <li class="act6"></li>
        <li class="act7"></li>
        <li class="act8"></li>
        <li class="act9"></li>
        <li class="act10"></li>
        <li class="act11"></li>
        <li class="act12"></li>
        <li class="act13"></li>
        <li class="act14"></li>
        <li class="act15"></li>
        <li class="act16"></li>
        <li class="act17"></li>
        <li class="act18"></li>
        <li class="act19"></li>
        <li class="act20"></li>
        <li class="act21"></li>
        <li class="act22"></li>
    </ul>
    <p class="dataList"><#list outlineValues as outline>${outline.id}|${outline.value}#</#list></p>
    <p class="dataList2"><#list precheckDetailsList as precheckDetail>${precheckDetail.precheckValueId}|${precheckDetail.precheckValue}|${precheckDetail.ftlId}#</#list></p>

    <ul class="lineTxt"></ul>
</div>

<script>
    $(function () {
        var _d = $.trim($('.dataList').text()),
                _d2 = $.trim($('.dataList2').text()),
                keysMap = [
                    'left_front_bumper',
                    'right_front_bumper',
                    'left_front_wing',
                    'right_front_wing',
                    'front_hood',
                    'left_wing_mirror',
                    'right_wing_mirror',
                    'left_front_door',
                    'right_front_door',
                    'left_rear_door',
                    'right_rear_door',
                    'left_rear_wing',
                    'right_rear_wing',
                    'left_rear_bumper',
                    'right_rear_bumper',
                    'front_wind_shield',
                    'front_cover',
                    'rear_cover',
                    'rear_wind_shield',
                    'rear_hood',
                    'left_beam',
                    'right_beam'
                ],
                layerId,
                dataList = [],
                selects = [];

        _d = _d.slice(0, -1).split('#');
        _d2 = _d2.slice(0, -1).split('#');

        for (var i = 0; i < _d.length; i++) {
            var _a = $.trim(_d[i]).split('|');
            dataList.push({
                id: _a[0],
                value: _a[1]
            });
        }

        for (var j = 0; j < _d2.length; j++) {
            var _b = $.trim(_d2[j]).split('|');
            selects.push({
                id: _b[0],
                value: _b[1],
                key: _b[2]
            });
        }


        String.prototype.format = function() {
            for (var i = 0, val = this, len = arguments.length; i < len; i++)
                val = val.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
            return val;
        };

        function getKeyByNum(n) {
            return keysMap[n - 1];
        }

        function getNumByKey(k) {
            for (var i = 0; i < keysMap.length; i++) {
                if (k == keysMap[i]) {
                    return i + 1;
                }
            }
        }

        function getSelectedByKey(k) {
            var res = null, i = 0;
            for (; i < selects.length; i++) {
                if (k == selects[i].key) {
                    res = selects[i];
                    break;
                }
            }
            return res;
        }

        function choiceBox(n) {
            seajs.use('dialog', function (dg) {
                var key = getKeyByNum(n),
                        obj = getSelectedByKey(key),
//                        $box = $('.wgNav'),
                        $c = $('.carBox'),
                        bx = $c.offset(),
                        $html = '';
                for (var i = 0; i < dataList.length; i++) {
                    var d = dataList[i],
                            cur = '';
                    if (obj && (d.id == obj.id)) {
                        cur = ' current';
                    }
                    $html += '<li data-id="{0}" data-num="{3}" class="item{1}">{2}</li>'.format(d.id, cur, d.value, n);
                }
//                $html += '<li class="close">关闭</li>';
                $html = '<ul class="wgNav">' + $html + '</ul>';
//                $box.html($html).show();
                layerId = dg.open({
                    content: $html,
                    closeBtn: 1,
                    scrollbar: true,
                    fix: true,
                    offset: ['120px', bx.left + 510 + 'px']
                });
            });
        }

        function closeBox() {
//            var $box = $('.wgNav');
//            $box.hide().empty();
            seajs.use('dialog', function (dg) {
                if (layerId !== undefined) {
                    dg.close(layerId);
                    layerId = undefined;
                }
            });
        }

        function addLine(n, id, val) {
            var key = getKeyByNum(n),
                    $lb = $('.lineTxt');
            selects.push({
                id: id,
                value: val,
                key: key
            });
            $lb.append('<li class="line{0}"><span>{1}<em data-num="{0}" class="lixx"></em></span></li>'.format(n, val));
        }

        function removeLine(n) {
            var key = getKeyByNum(n);
            $('.lineTxt .line' + n).remove();
            for (var i = 0; i < selects.length; i++) {
                if (key == selects[i].key) {
                    selects.splice(i, 1);
                    break;
                }
            }
        }

        window.getCheckCarFormdata = function () {
            var res = {}, i = 0;
            for (; i < keysMap.length; i++) {
                var key = keysMap[i],
                        obj = getSelectedByKey(key);
                res[key] = obj ? obj.id : 0;
            }
            return res;
        };

        function initLines() {
            var $lb = $('.lineTxt'),
                    $html = '';
            for (var i = 0; i < selects.length; i++) {
                var n = getNumByKey(selects[i].key);
                if (n === undefined)
                    continue;
                $html += '<li class="line{0}"><span>{1}<em data-num="{0}" class="lixx"></em></span></li>'.format(n, selects[i].value);
            }
            $lb.html($html);
        }

        initLines();

        $(document)
                .on('click', '.actLayer li', function () {
                    var n = parseInt(this.className.substr(3));
                    choiceBox(n);
                })
                .on('click', '.lineTxt li', function () {
                    var n = parseInt(this.className.substr(4));
                    choiceBox(n);
                })
                .on('click', '.wgNav .item', function () {
                    var n = parseInt($(this).data('num')),
                            id = $(this).data('id'),
                            val = this.innerText;
                    if ($(this).hasClass('current')) {
                        removeLine(n);
                    } else {
                        if ($(this).siblings().is('.current'))
                            removeLine(n);
                        addLine(n, id, val);
                    }
                    closeBox();
                })
                .on('click', '.lixx', function (e) {
                    e.stopPropagation();
                    var n = parseInt($(this).data('num'));
                    removeLine(n);
                });

    });
</script>
