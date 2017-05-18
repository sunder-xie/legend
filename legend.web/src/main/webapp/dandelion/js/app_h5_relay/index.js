(function(){

	var now = { row:1}, last = { row:0};
	const towards = { up:1, right:2, down:3, left:4};
	var isAnimating = false;
	s=window.innerHeight/500;
	ss=250*(1-s);
	// $('.wrap').css('-webkit-transform','scale('+s+','+s+') translate(0px,-'+ss+'px)');
	document.addEventListener('touchmove',function(event){
		event.preventDefault(); },false);
	$(document).swipeUp(function(){
		if (isAnimating) return;
		last.row = now.row;
		if (last.row != 6) { now.row = last.row+1;  pageMove(towards.up);}
	});
	$(document).swipeDown(function(){
		if (isAnimating) return;
		last.row = now.row;
		if (last.row!=1) { now.row = last.row-1;pageMove(towards.down);}
	});
	function pageMove(tw){
		var lastPage = ".page-"+last.row,
			nowPage = ".page-"+now.row;
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

		$(nowPage).removeClass("hide").removeClass("pt-page-moveToTop");
		$(lastPage).addClass(outClass);
		$(nowPage).addClass(inClass);

		setTimeout(function(){

			$(lastPage).removeClass('current');
			$(lastPage).removeClass(outClass);
			$(lastPage).addClass("hide");
			$(lastPage).children().addClass("hide");

			$(nowPage).addClass('current');
			$(nowPage).removeClass(inClass);
			$(nowPage).children().removeClass("hide");

			isAnimating = false;
		},600);
	}
})();