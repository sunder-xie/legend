//替换下拉框的模块
define(function(require,exports,module){
	require("./../libs/chosen/chosen")($);
	exports.handleChoosenSelect = function(selectorName, width) {
	    $(selectorName).each(function () {
	    	var $this = $(this);
	        if ($this.find('option').length >= 1) {
	        	$this.show();
	        	$this.removeData("chosen");
	        	$this.next(".chosen-container").remove();
	        	$this.show();
	        	$this.chosen({
	                search_contains: true,
	                display_width: width || $this.width(),
	                allow_single_deselect: $(this).attr("data-with-diselect") === "1" ? true : false,
	                no_results_text: '没有搜索到结果'
	            });
	        }
	    });
	}
});