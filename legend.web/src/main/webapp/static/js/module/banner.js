define(function (require, exports, module) {
    var defaults = {
        //图片盒子
        bannerBox: null,
        //图片切换按钮盒子
        btnBox: null,
        //是否有宽度（如果没有宽度代表全屏显示,默认为全屏显示）
        banWidth: null,
        //显示方式（1、从右向左滚动显示 2、逐个显示其它隐藏）
        displayModes: 1,
        //时间
        time: 3000

    };

    exports.init = function (option) {
        var args = $.extend({}, defaults, option);
        var banPic = args.bannerBox.find('li'),
            len = banPic.length,
            index = 0,
            timer = null,
            screenWidth = args.banWidth,
            $window = $(window);

        if(args.banWidth){
            screenWidth = args.banWidth;
        }else{
            screenWidth = $window.width();
            $window.resize(function() {
                screenWidth = $window.width();
                show();
            });
        }

        show();

        function show(){
            //判断显示方式
            if(args.displayModes == 1){
                args.btnBox.css('width', screenWidth * len + 'px');
                banPic.css({
                    'width':screenWidth + 'px',
                    'float':'left'
                });
            }else if(args.displayModes == 2){
                banPic.css({
                    'width':screenWidth + 'px',
                    'opacity':'0',
                    'position': 'absolute',
                    'top':'0',
                    'left':'0',
                    'z-index':'100'
                });
                banPic.eq(0).css({'opacity':'1','z-index':'300'});
            }
        }

        //生成按钮
        for (var i = 0; i < len; i++) {
            var dot = $('<li><a href="javascript:;"></a></li>');
            args.btnBox.append(dot)
        }
        var banBtn = args.btnBox.find('li');
        var banBtnWidth = banBtn.outerWidth(true) * len;
        args.btnBox.css({
            'width':banBtnWidth,
            'margin-left':-(banBtnWidth/2)

        });
        banBtn.eq(0).find('a').addClass("btn-hover");


        play();
        //轮播
        function play() {
            timer = setInterval(function () {
                if(args.displayModes == 1){
                    args.bannerBox.animate({
                        'left': -(screenWidth * index) + 'px'
                    });
                }else if(args.displayModes == 2){
                    banPic.eq(index).animate({
                        'opacity': '1',
                        'z-index':'300'
                    }).siblings().animate({
                        'opacity': '0',
                        'z-index':'100'
                    });
                }
                banBtn.find('a').removeClass("btn-hover");
                banBtn.eq(index).find('a').addClass("btn-hover");
                index++;
                if (index > len - 1) {
                    index = 0;
                }
            }, args.time);
        }

        //鼠标悬停时停时播放
        banPic.hover(
            function () {
                clearInterval(timer);
            },
            function () {
                play();
            });
        banBtn.hover(
            function () {
                clearInterval(timer);
            },
            function () {
                play()
            }
        );
        //单击底部按钮切换相应图片
        banBtn.click(function(){
            index = $(this).index();
            if(args.displayModes == 1){
                args.bannerBox.css('left', -(screenWidth * index));
            }else if(args.displayModes == 2){
                banPic.eq(index).css({'opacity':'1','z-index':'300'}).siblings().css({'opacity':'0','z-index':'100'});
            }
           banBtn.find('a').removeClass("btn-hover");
            $(this).find('a').addClass("btn-hover");
        })
    };
});