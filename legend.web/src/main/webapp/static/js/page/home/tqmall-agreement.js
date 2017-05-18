$(function () {
    var t = 10, $e = $('.time');

    var timer = setInterval(function () {
        $e.text(--t + 'S');
        if(t == 0) {
            $('.agreement-btn').addClass('able')
                .text('同意协议并进入系统');
            clearInterval(timer);
        }
    }, 1000);

    $('.js-tqmall-agreement').on('click', function () {
        if(!$(this).hasClass('able')) {
            return;
        }
        $.ajax({
            url: BASE_PATH + '/shop/tqmall-agreement/agree'
        }).done(function (json) {
            if(json.data && json.success) {
                window.location.href = BASE_PATH + '/home';
            }
        });
    });
});