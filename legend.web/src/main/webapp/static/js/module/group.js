
/**
 * ch 2016-03-23
 * group组上的事件
 */

define(function(require,exports,module){
	exports.init = function(){
		var $group = $('.yqx-group');
		//group组收起展开
		$group.on('click','.js-arrow-up',function(){
			var $this = $(this);
			var parent = $this.parent();
			parent.next().slideUp();
			$this.removeClass('js-arrow-up').addClass('js-arrow-down');
			return false;
		});
		//group组收起展开
		$group.on('click','.js-arrow-down',function(){
			var $this = $(this);
			var parent = $this.parent();
			parent.next().slideDown();
			$this.removeClass('js-arrow-down').addClass('js-arrow-up');
			return false;
		});

	}
});