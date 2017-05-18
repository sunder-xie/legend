/**
 * Created by hhl on 16/3/29.
 */
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

function view (){
    var UA = window.navigator.userAgent.toLowerCase();
    if(!UA.match(/Android|Mobile|iOS|iPhone/i)) {
        location.href = "http://www.yunqixiu.com/legend/portal/dandelion/download";
    }
    App.share("免费检测还送赠品，这等好事岂能错过", "春天万物复苏（霉菌滋生），百花飘香（车内异味,车内空气差到等于吸雾霾）。云修门店邀请你免费做空调检测，听说还有赠品哦~~", "http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/images/201603/source_img/original_p_145925561380914238.jpg", location.href);
    var id = Dao.parseUrl()['shopId'],
        actTplId = Dao.parseUrl()['actTplId'],
        host = "http://" + location.host;

    Model.home(function (json){
        if (!json.success) {
            App.alert(json.errorMsg);
        }
    });

    $(document).on("tap", ".btn_quan", function (){
        location.href = host + "/legend/dandelion/html/push_page/summer/confirm.html?shopId={0}&actTplId={1}".format(id, actTplId);
    });
}


App.changeSize = function () {
    var page = $(window);
    window.orientation;
    var phoneWidth = parseInt(page.width());
    var phoneScale = phoneWidth / 640;
    $('#wrapper')[0].style.cssText = '-webkit-transform-origin:0 0;transform-origin:0 0;-webkit-transform:scale(' + phoneScale + ');transform:scale(' + phoneScale + ');';
    $('.btn_quan')[0].style.cssText = '-webkit-transform-origin:0 100%;transform-origin:0 100%;-webkit-transform:scale(' + phoneScale + ');transform:scale(' + phoneScale + ');';
};

$(function () {
    App.changeSize();
    window.addEventListener('onorientationchange' in window ? 'orientationchange' : 'resize', App.changeSize);
    App.run(view);
});