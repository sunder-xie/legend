// 页面初始化模块
define(function(require,exports,module){
	require("./eventBind");
	var chosen = require("./chosenSelect");
	// var footer = require("./footer");
	var select = require("./select");
	var cascadeSelect = require("./cascadeSelect");
	
	exports.init = function(){
		//下拉框数据获取
		select.init();
		//美化select下拉框
		chosen.handleChoosenSelect('.chosen');
		//底部动画初始化
		// footer.init();
		//级联下拉框初始化
		cascadeSelect.init(".cascadeSelect");
		//垂直菜单初始化
		$('.qxy_vmenu').vmenu();
	}
});