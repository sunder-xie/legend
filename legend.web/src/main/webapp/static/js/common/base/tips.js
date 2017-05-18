/**
 * Created by Sky on 2015/7/29.
 */

$(function() {


    //非chrome浏览器，提示使用chrome
    (function(window,$){
        //浏览器下载地址
        var downloadUrl = 'http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/software/chrome_47.0.2526.106.exe';
        var htmlStr = [
            '<div class="not_chrome_tips">',
                '<div class="tips_inner clearfix">',
                    '<div class="tips_left">',
                        '云修系统推荐您使用谷歌（Google Chrome）浏览器',
                    '</div>',
                    '<div class="tips_right clearfix">',
                        '<a href="'+downloadUrl+'" class="tips_btn1">下载</a>',
                        '<a href="javascript:;" class="tips_btn2" id="after_tip">稍后提示</a>',
                        '<a href="javascript:;" class="tips_btn2" id="never_tip">永不提示</a>',
                    '</div>',
                '</div>',
            '</div>'
        ].join('');
        var userAgentStr = window.navigator.userAgent;
        var cookie = {
            setCookie : function(name, value, hours) {
                hours = hours || 24*3650;
                if (String(name) == "undefined" || name == null || name == "") return;
                var expire = "";
                expire = new Date((new Date()).getTime() + hours * 60 * 60 * 1000);
                expire = "; expires=" + expire.toGMTString() + "; path=/";
                document.cookie = name + "=" + escape(value) + expire;
            },
            getCookie : function(name) {
                var cookieValue = "";
                var search = name + "=";
                if (document.cookie.length > 0) {
                    offset = document.cookie.indexOf(search);
                    if (offset != -1) {
                        offset += search.length;
                        end = document.cookie.indexOf(";", offset);
                        if (end == -1) {
                            end = document.cookie.length;
                        }
                        cookieValue = unescape(document.cookie.substring(offset, end))
                    }else{
                        cookieValue = null;
                    }
                }
                return cookieValue;
            }
        }

        var removeTips = function(){
            $('.not_chrome_tips').remove();
            $('.header').removeClass('not_chrome_tips_header');
            $('.login_header').removeClass('not_chrome_tips_login_header');
            $('.wrapper,.qxy_wrapper').removeClass('not_chrome_tips_wrapper');
            $('.sidebar').removeClass('not_chrome_tips_sidebar');
        }
        
        if(!(/chrome/gi.test(userAgentStr)) && cookie.getCookie('not_chrome_tips_flag') == null){
            $('body').prepend(htmlStr);
            $('.header').addClass('not_chrome_tips_header');
            $('.login_header').addClass('not_chrome_tips_login_header');
            $('.wrapper,.qxy_wrapper').addClass('not_chrome_tips_wrapper');
            $('.sidebar').addClass('not_chrome_tips_sidebar');

            //稍后提示
            $('#after_tip').click(function(){
                cookie.setCookie('not_chrome_tips_flag',true,72);
                removeTips();
            });

            //永不提示
            $('#never_tip').click(function(){
                cookie.setCookie('not_chrome_tips_flag',true);
                removeTips();
            });
        }
    })(window,$)
    
});
