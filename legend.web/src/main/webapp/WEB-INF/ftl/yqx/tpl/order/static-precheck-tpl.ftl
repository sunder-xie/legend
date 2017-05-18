


<link rel="stylesheet" href="${BASE_PATH}/static/css/page/precheck/precheck-tpl.css?1a87cccfb69f866165de36af51182d6b"/>
<style>
    .wgbig {
        width: 540px;
        height: 540px;
        position: relative;
        margin: 0 auto 10px;
    }
    .actLayer {
        display: none;
    }
    .carBox {
        z-index: 3;
        background: none!important;
    }
    .lineTxt li {
        height: 0!important;
        background: transparent!important;
        border-top: 1px solid #000;
    }
    .lineTxt li span {
        color: #000!important;
    }
    .checkcarpic {
        position: absolute;
        display: block;
        border: none;
        left: 37px;
        top: 24px;
    }
    .wgtitle {
        width: 958px;
        padding: 10px;
        margin: 15px auto 0;
        font-size: 20px;
        font-weight: bold;
    }
    .wg-precheck{
        background: #fff;
        margin-bottom: 15px;
    }
</style>
<div class="wg-precheck">
    <div class="wgtitle">外观</div>
    <div class="wgbig">
    <img class="checkcarpic" src="${BASE_PATH}/static/img/page/magic/wg_1.jpg"/>
    <div class="carBox" data-tpl-ref="static-precheck-tpl">
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
</div>
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

        function getNumByKey(k) {
            for (var i = 0; i < keysMap.length; i++) {
                if (k == keysMap[i]) {
                    return i + 1;
                }
            }
        }

        function initLines() {
            var $lb = $('.lineTxt'),
                    $html = '';
            for (var i = 0; i < selects.length; i++) {
                var n = getNumByKey(selects[i].key);
                if (n === undefined)
                    continue;
                $html += '<li class="line{0}"><span>{1}</span></li>'.format(n, selects[i].value);
            }
            $lb.html($html);
        }

        initLines();

    });
</script>
