<script>
	var UA = window.navigator.userAgent.toLowerCase();
    if(UA.match(/Android|Mobile|iOS|iPhone/i)) {
        //移动访问pc端下载页，跳到移动下载页
        location.href = "${BASE_PATH}/dandelion/html/push_page/app_h5_relay.html?2016";

    }
</script>
<#include "layout/portal-header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/merchant/merchant_download.css?2816550caa4e4c621b2fed18e3d29890"/>
<div class="wraper">
    <div class="box_wrap">
        <div class="box1">
        	<div class="box">
        		<a class="download" href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/mace/android/tqmall-1.2-tqmall-release.apk">立即下载</a>
        	</div>
        </div>
        <div class="box2"></div>
        <div class="box3"></div>
        <div class="box4"></div>
        <div class="box5"></div>
        <div class="box6"></div>
    </div>
</div>
<#include "layout/portal-footer.ftl">
<script type="text/javascript">
	$(function(){
		var browser = {
		    versions: function () {
		        var u = navigator.userAgent, app = navigator.appVersion;
		        return {//移动终端浏览器版本信息
		            trident: u.indexOf('Trident') > -1, //IE内核
		            presto: u.indexOf('Presto') > -1, //opera内核
		            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
		            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
		            mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
		            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
		            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
		            iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
		            iPad: u.indexOf('iPad') > -1, //是否iPad
		            weixin: u.toLowerCase().indexOf('micromessenger') > -1,
		            webApp: u.indexOf('Safari') == -1//是否web应该程序，没有头部与底部
		        };
		    }(),
		    language: (navigator.browserLanguage || navigator.language).toLowerCase()
		}
		
		if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
		    $('.download').attr('href', 'https://itunes.apple.com/us/app/tao-qi-yun-xiu/id990531550?mt=8');
		}
	});
</script>