/**
 * Created by hhl on 16/3/28.
 */

App.changeSize = function () {
    var page = $(window);
    window.orientation;
    var phoneWidth = parseInt(page.width());
    var phoneScale = phoneWidth / 640;
    $('.wrapper')[0].style.cssText = '-webkit-transform-origin:0 0;transform-origin:0 0;-webkit-transform:scale(' + phoneScale + ');transform:scale(' + phoneScale + ');';
};

var isSameDay;
$(function () {
    App.changeSize();
    window.addEventListener('onorientationchange' in window ? 'orientationchange' : 'resize', App.changeSize);
    App.run(view);
});

    Model.home = function (success) {
        var url = '/pub/activity/get_shop';
        var params = {
            shopId: Dao.parseUrl()['shopId'] || ''
        };
        Dao.xhrRender({
            action: url,
            params: params,
            tag: 'shop',
            tpl: 'shopTpl',
            fill: '~'
        }, success);
    };

    Model.service = function (success) {
        var url = '/pub/activity/get_shop_services';
        var params = {
            shopId: Dao.parseUrl()['shopId'] || '',
            actTplId: Dao.parseUrl()['actTplId'] || ''
        };
        Dao.xhrRender({
            action: url,
            params: params,
            tag: 'service',
            tpl: 'serviceTpl',
            fill: '~'
        }, success);
    };

function view (){
    var UA = window.navigator.userAgent.toLowerCase();
    if(!UA.match(/Android|Mobile|iOS|iPhone/i)) {
        location.href = "http://www.yunqixiu.com/legend/portal/dandelion/download";
    }
    Model.home(function (json){
        if (json.success) {
            Model.service(function (data){
                if (data.success) {

                } else {
                    if (data.code == 20022013) { //服务过期
                        $(".overDate").show();
                        $(".noService").hide();
                        $(".hasService").hide();
                    } else {  //无服务
                        $(".overDate").hide();
                        $(".noService").show();
                        $(".hasService").hide();
                    }
                }
            });
        } else {
            App.alert(json.errorMsg);
        }
    });

    var host = "http://" + location.host,
        shopId = Dao.parseUrl()['shopId'],
        actTplId = Dao.parseUrl()['actTplId'];

    $(document).on("tap", ".btn_back", function (){  //返回首页
        location.href = host + '/legend/dandelion/html/push_page/summer/index.html?shopId={0}&actTplId={1}'.format(shopId,actTplId);
    }).on("tap", ".service_list li", function (){
        $(this).find(".choose").addClass("on");
        $(this).siblings().find(".choose").removeClass("on");
    }).on("tap", ".btn_apply", function (){
        var serviceId = '',
            time = $(".choose_time").text(),
            txt = $(".choose_time").data('time');
        if (txt == undefined) {
            App.alert("请选择抵店时间");
            return false;
        }
        $(".service_list li").each(function (){
            if ($(this).find(".choose").hasClass("on")) {
                serviceId = $(this).data('serviceid');
            }
        });
        location.href = host + "/legend/dandelion/html/push_page/summer/login.html?shopId={0}&serviceId={1}&time={2}".format(shopId, serviceId, time);
    });
}

/*
 * 时间插件
 */
var jQ = jQuery.noConflict();
jQuery(function () {
    // mobiscroll 模拟事件委托
    jQ("#JQWrap").on('click.delegate', '#scroller', function() {
        // mobiscroll 日期选择
        var now = new Date(),
            nowYear = now.getFullYear(),
            nowMonth = now.getMonth(),
            nowDate = now.getDate(),
            nowHour = now.getHours(),
            $hourRange = jQ("#treelist"),
            max = $hourRange.find("li").last().data("hh"),
            _invalid = nowHour >= max ? {daysOfMonth: [(nowMonth + 1) + "/" + nowDate]} : {},   // 判断今天是否还有预约时间
            _default, appointYear, appointMonth, appointDay, appointHour, valText;  // 设置用于显示的时间

        /* 通用属性设置 S */
        _default = {

            theme: "android-ics light",
            lang: "zh",
            headerText: "请选择抵店时间",
            closeOnOverlay: true, // 点击覆盖层关闭
            display: "bottom",
            context: "#wrapper"
        };

        jQ(this).mobiscroll().date(jQ.extend({}, _default, {
            /* 时间属性设置 S */
            minDate: new Date(nowYear, nowMonth, nowDate),
            maxDate: new Date(nowYear, nowMonth, nowDate + 10),
            separator: "/",
            dateFormat: "yy/mm/dd DD",
            dateOrder: "yymmdd",
            yearText: "年",
            monthText: "月",
            dayText: "日",
            invalid: _invalid,
            onSelect: function (valueText) {

                // 解析“年月日”数据
                var tmp = valueText.split(" ")[0].split("/");

                valText = valueText;
                appointYear = tmp[0];
                appointMonth = tmp[1];
                appointDay = tmp[2];

                // 检查当前时间是否大于等于预约时间
                $hourRange.find("li").each(function () {
                    var $this = jQ(this),
                        hh = $this.data("hh"),
                    // 检查当前时间区间是否无效
                        isValid = (nowYear >= appointYear && (nowMonth + 1) >= appointMonth && nowDate >= appointDay && nowHour >= hh);

                    // 设置无效时间区间(被设置为无效的选项不能选择)
                    $this.data("invalid", isValid);
                    //if (nowYear >= appointYear && (nowMonth + 1) >= appointMonth && nowDate >= appointDay && nowHour >= hh) {
                    //    $this.data("invalid", true);
                    //} else {
                    //    $this.data("invalid", false);
                    //}
                });


                // 选择预约的时间区间
                $hourRange.mobiscroll().treelist($.extend({}, _default, {
                    onSelect: function (index) {

                        var $selectedHour = jQ(this).find("li").eq(index),
                            $scroller = jQ("#scroller"),
                            time;

                        // 初始化参数
                        valText += "  " + $selectedHour.text();
                        appointHour = $selectedHour.data("hh");
                        $scroller.text( valText );
                        time = new Date(appointYear, appointMonth - 1, appointDay, appointHour).getTime();
                        // 为了兼容Zepto也能获取到time的数据，使用HTML5原生属性
                        $scroller[0].dataset["time"] = time;
                        isSameDay = (nowYear == appointYear && (nowMonth + 1) == appointMonth && nowDate == appointDay);

                        // 清除不必要内容
                        jQ("#treelist_dummy").remove();
                        valText = undefined;
                    }
                }));

                // 主动触发时间区间
                $hourRange.trigger("click.dw");
            }
        }));

        // 首次click事件触发mobiscroll的click.dw事件，调用“年月日”
        jQ(this).trigger("click.dw");
        // 解绑模拟的事件委托
        jQ(this).off("click.delegate");
    });
});
