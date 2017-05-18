window.location.href = '/legend/dandelion/html/push_page/app_h5_relay.html';
/*
 * 智能机浏览器版本信息:
 *
 */
// var browser = {
//     versions: function () {
//         var u = navigator.userAgent, app = navigator.appVersion;
//         return {//移动终端浏览器版本信息
//             trident: u.indexOf('Trident') > -1, //IE内核
//             presto: u.indexOf('Presto') > -1, //opera内核
//             webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
//             gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
//             mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
//             ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
//             android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
//             iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
//             iPad: u.indexOf('iPad') > -1, //是否iPad
//             weixin: u.toLowerCase().indexOf('micromessenger') > -1,
//             webApp: u.indexOf('Safari') == -1//是否web应该程序，没有头部与底部
//         };
//     }(),
//     language: (navigator.browserLanguage || navigator.language).toLowerCase()
// }
// if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
//     $('.app_download').attr('href', 'https://itunes.apple.com/us/app/tao-qi-yun-xiu/id990531550?mt=8');
// }
// //ios9版本下bug处理。
// $("head").append('<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />');

// if(browser.versions.weixin){
//     // $('.comment').show();
//     // var winHeight = typeof window.innerHeight != 'undefined' ? window.innerHeight : document.documentElement.clientHeight;

//     //蒙层提示，跳出微信去下载。
//     $("body").append('<div id="weixin-tip"><p><img src="/legend/resources/img/common/live_weixin.png" alt="微信打开" width="100%"/></p></div>');
// }