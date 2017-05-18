//公共底部动画模块
define(function(require,exports,module){
	var timer = 200; //动画执行时间
	exports.init = function(){
		$(".f_nav").hover(function(){
            $(this).stop().animate({
                top: '-62px'
            }, timer);
		},function(){
            $(this).stop().animate({
                top: '0px'
            }, timer);
		});
		
		$(".f_nav a").hover(function(){
            $(this).stop().animate({
                top: '-10px'
            }, timer);
		},function(){
            $(this).stop().animate({
                top: '0px'
            }, timer);
		});
	}
});