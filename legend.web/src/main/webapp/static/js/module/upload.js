/**
 * ch 2016-04-15
 * 图片上传
 */

define(function(require,exports,module){

	var ax = require('ajax');
	var dg = require('dialog');
	//图片上传
	exports.init = function(opt){
		var args = $.extend({
			dom: "",
			url: BASE_PATH + '/shop/member/upload_image',
			callback: null,
			fileSizeLimit: 5242880,//1024*1024*5byte,即5M
			maxWidth: null,
			maxHeight: null
		},opt);
		if(args.dom == ""){
			console.error('请传入上传按钮的选择器');
			return;
		}
		$(document).off('change.file', args.dom).on('change.file', args.dom, function(){
			// 无数据时
			var self = this;
			if (!self.files.length) {
				return;
			}
			var reader = new FileReader();
			reader.readAsDataURL($(this).get(0).files[0]);
			reader.onload = function (theFile) {
				var image = new Image();
				image.src = theFile.target.result;
				image.onload = function () {
					if (args.maxWidth && args.maxWidth <= this.width) {
						dg.fail('图片宽度不能超过' + args.maxWidth + "px");
						return;
					}
					if (args.maxHeight && args.maxHeight <= this.height) {
						dg.fail('图片高度度不能超过' + args.maxWidth + "px");
						return;
					}
					var formData = new FormData();
					formData.append('xxx', $(self).get(0).files[0]);
					formData.append('_fileSizeLimit', args.fileSizeLimit);
					$.ajax({
						url: args.url,
						type: 'post',
						data: formData,
						cache: false,
						contentType: false,
						processData: false,
						dataType: 'json'
					}).done(function (json) {
						if (json.success) {
							dg.success('图片上传成功', function () {
								args.callback && args.callback.call(self, json);
							});
						} else {
							args.error && args.error.call(self, json);
							dg.fail(json.errorMsg || '图片上传失败');
						}
					}).fail(function (json) {
						args.error && args.error.call(self, json);
					});
				};
			};

		});
	}
});