(function(){
	var now = { row:1}, last = { row:0},
		towards = { up:1, right:2, down:3, left:4},
		isAnimating = false,
		clientHeight = $(window).height(),
		clientWidth = $(window).width();
	$(".wrapper").css({"height":clientHeight,"width":clientWidth});

	//屏蔽默认的划动手势
	document.addEventListener('touchmove',function(event){
		event.preventDefault(); 
	},false);
	//手势向上滑
	$(document).swipeUp(function(){
		if (isAnimating) return;
		last.row = now.row;
		if (last.row != 4) { now.row = last.row+1;  pageMove(towards.up);}
	});
	//手势向下滑
	$(document).swipeDown(function(){
		if (isAnimating) return;
		last.row = now.row;
		if (last.row!=1) { now.row = last.row-1;pageMove(towards.down);}
	});
	function pageMove(tw){
		var lastPage = ".page_"+last.row,
			nowPage = ".page_"+now.row;
		switch(tw) {
			case towards.up:
				//手势向上滑
				outClass = 'pt-page-moveToTop';
				inClass = 'pt-page-moveFromBottom';
				break;
			case towards.down:
				//手势向下滑
				outClass = 'pt-page-moveToBottom';
				inClass = 'pt-page-moveFromTop';
				break;
		}
		isAnimating = true;
		$(lastPage).addClass(outClass);
		$(nowPage).removeClass('hide')
			.addClass(inClass)
			.addClass('current');

		//一个页面执行完动画以后，初始一些属性，方便下次执行。
		setTimeout(function(){
			$(lastPage).removeClass('current')
				.removeClass(outClass)
				.addClass("hide")
				.children()
				.addClass("hide");

			$(nowPage).addClass('current')
				.removeClass(inClass)
				.children()
				.removeClass("hide");

			isAnimating = false;
		},600);
	}

	// 加盟要求slide切换
	$(".item").on("tap",function(){
		var $this =$(this);
		if($this.hasClass("item_current")){
			return;
		}
		$this.find("p > i").remove();
		$this.siblings(".item_current").find("p").append("<i></i>");
		$this.find('.item_inner').show();
		$this.addClass('item_current').siblings('.item').removeClass('item_current').find(".item_inner").hide();

	});
	// pageScale();
})();
function pageScale() {
    var page = $(window);
    window.orientation;
    var phoneWidth = parseInt(page.width());
    phoneScale = phoneWidth / 1080;
    s = phoneScale;
    $('.wrapper')[0].style.cssText += '-webkit-transform-origin:0 0;transform-origin:0 0;-webkit-transform:scale(' + phoneScale + ');transform:scale(' + phoneScale + ');';
    
}