/**
 * Created by zsy on 2015/3/23
 */
$(function () {

    $('.gj_sr').on('click', function () {
        var $t = $(this);
        if ($t.hasClass('bihe')) {
            $t.removeClass('bihe');
            $('.wrbox').animate({
                height: '140px'
            });
        } else {
            $t.addClass('bihe');
            $('.wrbox').animate({
                height: '92px'
            });
        }
    });
    $('.mlist dt').on('click', function () {
        var $t = $(this);
        if ($t.hasClass('jia')) {
            $t.removeClass('jia');
            $t.next().slideDown();
        } else {
            $t.addClass('jia');
            $t.next().slideUp();
        }
    });
});