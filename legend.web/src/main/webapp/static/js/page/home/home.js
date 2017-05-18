$(function () {
    // banner
    var BANNER_MAX_WIDTH = 1004;
    var $viewer = $('.banner-view'), _index = 0;
    var width = 1004, _t = 0, transitionProperty = 'all';
    var time = 3000, len = $('.banner-view a').length;
    var timer, hoverDelay = 1000, hoverDelayTimer;

    width = formatBanner();

    if(len) {
        if( !$('.main .entry').hasClass('tq-entry') ) {
            timer = setInterval(function () {
                var a = move(_index, _t);

                _index = a.index;
                _t = a.t;
            }, time);
        }
    }

    $('.js-pointers .pointer').on('click', function () {
        var index = +$(this).data('index');

        move(index, index);
    });

    seajs.use(['dialog'], function (dg) {
        dg.titleInit();
    });

    $('.js-hover').hover(function () {
        var target = $(this).data('target');

        $(target).removeClass('hide');
        if(hoverDelayTimer) {
            clearTimeout(hoverDelayTimer);
        }
    }, function () {
        var target = $(this).data('target');

        hoverDelayTimer = setTimeout(function () {
            $(target).addClass('hide')
        }, hoverDelay)
    });

    $('.see-more-wrapper').hover(function () {
        clearTimeout(hoverDelayTimer);
    }, function () {
        $(this).addClass('hide');
    });

    // 保险判断在home-header.ftl

    function move(index, t) {
        // 小圆点
        if(index > len-1) {
            index = 0
        }

        if(t > len-1) {
            t = 0;
        }
        // 移动
        $viewer.css(
            {
                transform: 'translate3d(' + (-width * t) + 'px, 0, 0)',
                transitionProperty: 'all'
            }
        );

        // 小圆点
        $('.pointer.pointer-current').removeClass('pointer-current');
        $('.pointer[data-index=' + index + ']').addClass('pointer-current');

        index++;
        t++;

        return {
            index: index,
            t: t
        };
    }

    // 根据 图片长度 调整,最大为 1004
    function formatBanner() {
        var img = $('.banner-view').find('img');
        var width = img.length && img.eq(0)[0].naturalWidth;

        if(!width) {
            return;
        }
        if (width < BANNER_MAX_WIDTH && width > 400) {
            $('.banner').css('width', width);
        } else {
            width = BANNER_MAX_WIDTH;

            img.each(function () {
                $(this).css({
                    width: width,
                    height: 150
                });
            });
        }

        $('.banner-view').css('width', width * img.length);

        return width;
    }
});