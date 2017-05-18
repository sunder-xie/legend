//级联下拉框组件模块
define(function(require, exports, module) {
	var ajaxModule = require("./ajax");
	var art = require("./../libs/artTemplate/artTemplate");
	var chosen = require("./chosenSelect");
	exports.init = function(scope) {
		if($(scope).size() > 0){
			$(scope).each(function(i,cascadeSelect){
				$cascadeSelect  = $(cascadeSelect);
				/**
				 * 清空数据和美化框
				 */
				$cascadeSelect.find('select').each(function(i,e){
					$e = $(e);
					$e.find("option:gt(0)").remove();
					chosen.handleChoosenSelect($e);
				})
				
				$1select = $cascadeSelect.find("select:eq(0)");
				/**
				 * 级联数据初始化
				 */
				if($1select){
					asyncInitLoad(null, $1select);
				}
				
				/**
				 * 级联数据绑定
				 */
				var $selects = $(scope).find('select');
			})
		}
		$('body').on('change','.cascadeSelect select',function() {
			$select = $(this);
			var val = $select.val();// 获取选中值
			// 如果上一级下拉框数据改变,则联动其余数据全部清空
			$select.parent('.field_box').nextAll('.field_box').find('select').each(function(i,e){
				$e = $(e);
				$e.find("option:gt(0)").remove();
				chosen.handleChoosenSelect($e);
			})
				
			var $fb = $select.parent('.field_box').next('.field_box');
			if($fb.size()>0){
				var $nextSelect = $fb.find('select');
				if($nextSelect){
					if (val) {
						var dataObj = {};
						dataObj[$nextSelect.attr('paramName')] = val;
						ajaxModule.post({
							url : $nextSelect.attr('service_url2'),
							data : dataObj,
							success : function(json) {
								var tpl = $nextSelect.siblings("script");
								var temp = art.compile(tpl.html());
								var html = temp({
									json : json
								});
								$nextSelect.append(html);
								chosen.handleChoosenSelect($nextSelect);
							}
						})
					}
				}
			}
		})
	}
	
	function asyncInitLoad(pid, $elem){
		if($elem){
			var dataObj = {};
			if(pid && $elem.attr('paramName')){
				dataObj[$elem.attr('paramName')]=pid;
			}
			ajaxModule.post({
				url : $elem.attr('service_url2'),
				data:dataObj,
				success : function(json) {
					var tpl = $elem.siblings("script");
					//如果存在模板,则渲染它
					if(tpl){
						var temp = art.compile(tpl.html());
						var html = temp({
							json : json
						});
						$elem.append(html);
						/**
						 * 数据反显
						 */
						var val = $elem.attr('val');
						if (val) {
							$elem.val(val);
							/**
							 * 如果存在数值,则存在下一级的情况需要反显下一集
							 */
							$fb = $elem.parent('.field_box').next('.field_box');
							if($fb.size()>0){
								$select = $fb.find('select');
								if($select){
									asyncInitLoad(val, $select);
								}
							}
						}
						/**
						 * 再次美化
						 */
						chosen.handleChoosenSelect($elem);
					}
				}
			});
		}
	}
});