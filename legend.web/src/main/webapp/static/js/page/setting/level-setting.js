/*
 * create by zmx 2017/1/6
 * 安全登录设置
 */

$(function(){
    var doc = $(document);
    $('.level-current').find('.line').show();
    seajs.use([
        'dialog'
    ],function(dg){
        doc.on('click','.js-select',function(){
            var _this = $(this);
            var level = $('.level');
            level.removeClass('level-current');
            level.find('.js-select').text('选择').removeClass('select-btn-text').addClass('select-btn');
            level.find('.line').hide();

            _this.parents('.level').addClass('level-current');
            _this.text('已选择').removeClass('select-btn').addClass('select-btn-text');
            _this.parents('.level-current').find('.line').show();
            var value = _this.data("level");
            $.ajax({
                url: BASE_PATH + '/shop/security-login/level-chang/'+value,
                type:"POST",
                success: function (result) {
                    if (result.success) {
                        if(value == 1){
                            $('.level2-show').hide();
                            $('.level3-show').hide();
                        }else if(value == 2){
                            $('.level2-show').show();
                            $('.level3-show').hide();
                        }else if(value == 3){
                            $('.level2-show').show();
                            $('.level3-show').show();
                        }
                    } else {
                        dg.fail(result.message)
                    }
                }
            });
        })
    })
});