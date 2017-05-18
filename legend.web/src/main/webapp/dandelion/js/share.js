/**
 * @fileOverview 微信分享接口封装
 * @author       haiyong.zhang
 * @createTime   2014/9/29
 * @updateTime   2014/9/29
 * @version      0.1.0
 */



var weixin = (function () {

    var isShare = false;
    var imgUrl = '';
    var lineLink = '';
    var descContent = '';
    var shareTitle = '';

    function _report(event, msg) {
        console.log(event + ':' + msg);
    }

    function shareFriend() {
        isShare && WeixinJSBridge.invoke('sendAppMessage',{
            "img_url": imgUrl,
            "link": lineLink,
            "desc": descContent,
            "title": shareTitle
        }, function(res) {
            _report('send_msg', res.err_msg);
        });
    }
    function shareTimeline() {
        isShare && WeixinJSBridge.invoke('shareTimeline',{
            "img_url": imgUrl,
            "link": lineLink,
            "desc": descContent,
            "title": shareTitle
        }, function(res) {
            _report('timeline', res.err_msg);
        });
    }
    function shareWeibo() {
        isShare && WeixinJSBridge.invoke('shareWeibo',{
            "content": descContent,
            "url": lineLink
        }, function(res) {
            _report('weibo', res.err_msg);
        });
    }
// 当微信内置浏览器完成内部初始化后会触发WeixinJSBridgeReady事件。
    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {

// 发送给好友
        WeixinJSBridge.on('menu:share:appmessage', function(argv){
            shareFriend();
        });

// 分享到朋友圈
        WeixinJSBridge.on('menu:share:timeline', function(argv){
            shareTimeline();
        });

// 分享到微博
        WeixinJSBridge.on('menu:share:weibo', function(argv){
            shareWeibo();
        });

    }, false);


    function share(data) {
        isShare = true;
        imgUrl = data.imgUrl;
        lineLink = data.link;
        descContent = data.desc;
        shareTitle = data.title;
    }

    function changeShareData(data) {
        imgUrl = data.imgUrl;
        lineLink = data.link;
        descContent = data.desc;
        shareTitle = data.title;
    }

    return {
        share: share,
        changeShareData: changeShareData
    };

})();

function weiboShare(data) {
    var params = {
        url: data.url,
        appkey: data.appkey || '583395093',
        title: data.title,
        pic: data.pic,
        ralateUid: data.ralateUid,
        language: 'zh_cn'
    };
    var arr = [];
    for (var k in params) {
        params[k] && arr.push(k + '=' + encodeURIComponent(params[k]));
    }
    window.open('http://service.weibo.com/share/share.php?' + arr.join('&'));
}

function shareToYixin(data) {
    var _s = [],
        _d = {
            appkey: data.appkey || 'yx7778e35cfd70457297798d229b032f84',
            type: data.type || 'webpage',
            title: data.title,
            desc: data.desc,
            userdesc: data.userdesc,
            pic: data.pic,
            url: data.url
        };
    for (var k in _d) {
        _d[k] && _s.push(k.toString() + '=' + encodeURIComponent(_d[k].toString()));
    }
    window.open('http://open.yixin.im/share?' + _s.join('&'));
}