/**
 *
 * Created by ende on 16/6/16.
 */
$(function() {
    var $viewer = $('.view-box'), _index = 0;
    var width = 826, _t = 0, transitionProperty = 'all';
    var time = 6000;
    var timer = setInterval(function() {
        var a = move(_index, _t);

        _index = a.index;
        _t     = a.t;
    }, time);

    $('.js-pointers .pointer').on('click', function () {
        var index = +$(this).data('index');

        move(index, index);
    });

    function move(index, t) {

        // 移动
        $viewer.css(
            {
                transform: 'translate3d(' + (-width * t) + 'px, 0, 0)',
                transitionProperty: 'all'
            }
        );

        if(t >= 4) {
            setTimeout(function() {
                $viewer.css({
                    transform: 'translate3d(' + (-width * 0) + 'px, 0, 0)',
                    transitionProperty: 'none'
                });
            }, 300);
        }

        // 小圆点
        $('.pointer.pointer-current').removeClass('pointer-current');
        $('.pointer[data-index=' + index + ']').addClass('pointer-current');

         // 小圆点
        if(index >= 3) {
            index = 0
        } else {
            index += 1;
        }

        if(t >= 4) {
            t = 1;
        } else {
            t += 1;
        }

        return {
            index: index,
            t: t
        };
    }

    $('.js-next').on('click', function () {
        var index = +$('.pointer-current').data('index');
        var t = index + 1,
            a;

        if(index >= 3) {
            index = -1;
        }

        a = move(index + 1, t);

        _index = a.index;
        _t     = a.t;
    });

    $('.js-prev').on('click', function () {
        var index = +$('.pointer-current').data('index');
        var t = index,
            a;

        if(index <= 0) {
            index = 3;
        } else {
            index -= 1;
        }
        if(t <= 0) {
            t = 3;
        } else {
            t -= 1;
        }

        a = move(index, t);

        _index = a.index;
        _t     = a.t;
    });
});
