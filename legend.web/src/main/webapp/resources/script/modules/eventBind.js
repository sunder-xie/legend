define(function(require,exports,module){

	var check = require("./check");
	var cascadeSelect = require("./cascadeSelect");
	var chosen = require("./chosenSelect");
	var dialog = require("./dialog");
	var ajax = require("./ajax");
	var artTemplate = require("../libs/artTemplate/artTemplate");

	$(function(){
		//绑定动态生成表单项，表单行事件
		$(document).on("click",".qxy_add_icon",function(){
			var dynamic = $(this).parents(".form_item")
			var formRow = $(this).parents(".form_row");
			var maxCol = $("input[name=maxCol]", dynamic).val();
			//如果动态行，有最大个数限制。
			if(maxCol){
				if(Number(maxCol)>0){
					var dSize = dynamic.siblings('.form_item').size();
					if((dSize+1)>=maxCol){
						dialog.info("最多只能加"+maxCol+"条",3);
						return;
					}
				}
			}
			if(dynamic[0]){
				var html = dynamic[0].outerHTML;
				$(this).removeClass("qxy_add_icon").addClass("qxy_del_icon");
				dynamic.after(html);

				/*--------隐藏域无法清空,手工清空--------------*/
				dynamic.next('.form_item').find("input[type='hidden'][clearable]").val('')
				/*---------级联下拉框需要重新渲染-------start-------*/
				dynamic.next('.form_item').find('.cascadeSelect').each(function(i,e){
					cascadeSelect.init(e);
				});
				dynamic.next('.form_item').find('.chosen').each(function(i,e){
					chosen.handleChoosenSelect(e);
				});
				/*---------级联下拉框需要重新渲染--------end------*/
				/*--------自增长序列号--------------*/
				var num = dynamic.find("[num]").text();
				dynamic.next('.form_item').find('[num]').text(++num);

				if(dynamic.width() < formRow.width()){
					//动态生成行认后在该form_row中的索引
					var index = $(".form_item",formRow).index(dynamic.next()) + 1;
					//一行能放几个动态元素,结果向下取整
					var dynamicNum = Math.floor(formRow.width()/dynamic.width());
					//css margin-right
					var marginRight = $(".form_item:eq(0)",formRow).css("margin-right");
					//console.log(index +" % "+dynamicNum + "=" +index % dynamicNum);
					if(index % dynamicNum == 0){
						dynamic.next().css("margin-right",0);
					}else{
						dynamic.next().css("margin-right",marginRight);
					}
				}
				//移除多余元素
				dynamic.next().find(".part-info").remove();
				dynamic.next().find(".qxy_err_msg").hide();
			}
		});
		//表单行事件删除
		$(document).on("click",".qxy_del_icon",function(){
			var $this = $(this),
				formItem = $this.parents(".form_item"),
				formRow = $this.parents(".form_row"),
				delFn = formItem.attr("dynamic_fn") || formItem.attr("del_fn"),
				delBeforeFn = formItem.attr("del_before_fn"),
				next = formItem.next(),
				flag = true;
			if(delBeforeFn){
				delBeforeFn = eval(delBeforeFn);
				flag = delBeforeFn(formItem);
			}
			if(!flag){
				return;
			}
			formItem.nextAll().each(function(){
				var numObj = $(this).find("[num]");
				var num = numObj.text();
				numObj.text(num-1);
			});
			formItem.remove();
			if(delFn){
				//删除formitem后的回调方法
				delFn = eval(delFn);
				delFn(next);
			}

			if(formItem.width() < formRow.width()){
				//删除后重新排列formItem
				$(".form_item",formRow).each(function(){
					var index = $(this).index() + 1;
					//一行能放几个动态元素,结果向下取整
					var formItemNum = Math.floor(formRow.width()/formItem.width());
					//css margin-right
					var marginRight = $(".form_item:eq(0)",formRow).css("margin-right");
					if(index % formItemNum == 0){
						$(this).css("margin-right",0);
					}else{
						$(this).css("margin-right",marginRight);
					}
				});
			}
		});
		//在动态行中点击Enter热键自动增加行
		$(document).on("keypress","div[dynamic_id] input:not([service_url])",function(event){
			if(event.keyCode=="13"){
				$(event.target).parents(".form_row").find(".qxy_add_icon").trigger("click");
				/*yangf3需求,对于动态添加的行,如果是Enter键入,则不可编辑的内容将变为可编辑
				 _nextItem = $(event.target).parents(".form_item").find('input');
				 _nextItem.removeAttr('readonly');
				 _nextItem.removeAttr('disabled');*/
			}
		})
		//绑定表单保存事件
		$(document).on("click",".qxy_btn[ref_form_op]",function(){
			var $this = $(this);
			var action = $this.attr("ref_form_op");
			if(action && actionList[action]){
				if(typeof(actionList[action]) == "function"){
					actionList[action]($this);
				}
			}
		});
		//日历事件初始化绑定
		$(document).on("focus",".qxy_picker",function(){
			var wp = require("./../libs/My97DatePicker/WdatePicker");
			var dateFmt = $(this).attr("format") || "yyyy-MM-dd";
			var minDate = $(this).attr("min") || "";
			var maxDate = $(this).attr("max") || "";
			var doubleCalendar = $(this).attr("pairs") || false;
			var args = {
				dateFmt : dateFmt,
				minDate : minDate,
				maxDate : maxDate,
				doubleCalendar : doubleCalendar
			}
			wp.wp(args);
		});
		//group内容显示隐藏事件绑定
		$(document).on("click",".qxy_group_slider >.group_head",function(){
			var icon = $("i",$(this));
			var iconClass = icon.attr("class");
			var next = $(this).next(".group_content");
			if(!iconClass){
				return;
			}
			if(iconClass=="arrow_down"){
				icon.attr("class","arrow_up");
				next.show();
				formLayoutCalc(next);
			}else{
				icon.attr("class","arrow_down");
				next.hide();
				//收起group时，关闭该group里所有的tips
				$(".xubox_layer[type='tips']",next).remove();
			}
		});
		//group显示更多
		$(document).on("click",".downward",function(){
			var $this = $(this);
			var parent = $this.parent();
			$(".hide_part",parent).show();
			formLayoutCalc($(".hide_part",parent));
			chosen.handleChoosenSelect($(".chosen",parent));
			if($(this).hasClass("search_form_slide_btn")){
				$this.html("高级搜索<i></i>");
			}else{
				$this.html("隐藏更多<i></i>");
			}
			$this.addClass('upward').removeClass('downward');
		});
		//group隐藏更多
		$(document).on("click",".upward",function(){
			var $this = $(this);
			var parent = $this.parent();
			$(".hide_part",parent).hide();
			if($(this).hasClass("search_form_slide_btn")){
				$this.html("高级搜索<i></i>");
			}else{
				$this.html("显示更多<i></i>");
			}
			$this.addClass('downward').removeClass('upward');
		});
		//页面更新时不做存在判断
		$("input[v_type]").each(function(i){
			var $this = $(this);
			var vType = JSON.parse($this.attr("v_type"));
			if(vType.vurl){
				$this.data("data",$this.val());
				$this.data("vurl",vType.vurl);
				delete vType.vurl;
				$this.attr("v_type",JSON.stringify(vType));
			}else{
				return true;
			}
			$this.on("keyup",function(){
				var val = $(this).val();
				var data = $(this).data("data");
				//console.log("框中的值："+val);
				//console.log("缓存的值："+data);
				if(data == val){
					var temp = JSON.parse($this.attr("v_type"));
					$this.data("vurl",temp.vurl);
					delete temp.vurl;
					$this.attr("v_type",JSON.stringify(temp));
					//console.log("跟缓存值一一一一一样，加入vurl："+JSON.stringify(temp));
				}else{
					var temp = JSON.parse($this.attr("v_type"));
					var vurl = $(this).data("vurl");
					temp = $.extend(temp,{vurl:vurl});
					//console.log("跟缓存值不不不不一样，加入vurl："+JSON.stringify(temp));
					$this.attr("v_type",JSON.stringify(temp));
				}
			});
		});
		//输入限制
		$(document).on("keyup", ".J_input_limit", function(){
			var self = $(this);
			var limit_type = self.data('limit_type');
			var value = self.val();
			var t_value = '';
			if(limit_type === 'number'){
				t_value = value.replace(/[^0-9]/g,'');//剔除非数字字符
				//  t_value = t_value.replace(/^0+/g,'');//剔除首位为.的数字
				if(t_value !== value){
					self.val(t_value);
				}
			}
			if(limit_type === 'price'){
				t_value = value.replace(/[^0-9.]/g,'');//剔除非数字字符，除去.
				t_value = t_value.replace(/^\.+/g,'');//剔除首位为.的数字
				if(t_value !== value){
					self.val(t_value);
				}
			}
			if(limit_type === 'digits_letters'){
				t_value = value.replace(/[^0-9a-z]/gi,'');//剔除非数字和英文字符
				if(t_value !== value){
					self.val(t_value);
				}
			}
			if (limit_type === 'phone') {
				t_value = value.replace(/[^0-9-]/gi,'');
				if(t_value !== value){
					self.val(t_value);
				}
			}
			if (limit_type === 'account') {
				t_value = value.replace(/[^0-9\-]/gi,'');
				if(t_value !== value){
					self.val(t_value);
				}
			}
			if (limit_type === 'zh'){
				t_value = value.replace(/[\u4E00-\u9FA5]/gi,'');
				console.log(t_value);
				if(t_value !== value){
					self.val(t_value);
				}
			}
		});
		//icon 提示鼠标经过
		$(document).on("mouseover",".icon_tips",function(e){
			var $this = $(this);
			var selector = ".tips_content";
			var selectorInner = ".tips_content_inner";
			var parent = $this.parent();
			var content = $(selector,parent);
			var contentInner = $(selectorInner,parent);
			var arrow = $("i",contentInner);
				if(content.is(":visible")){
					return false;
				}
				if(arrow.size()<=0){
					contentInner.append("<i></i>");
				}
			var position = $this.offset();
				content.toggle();
			var height = content.height();
			var width = content.width();
			var winWidth = $(window).width();
				if((position.left+width+30) > winWidth){
					position.left -= width + 40;
					$("i",contentInner).removeClass('before').addClass('after');
				}else{
					$("i",contentInner).removeClass('after').addClass('before');
				}
				content.css({
					"left" : position.left + 30,
					"top" : position.top,
					"margin-top": -(height/2)+14
				}).show();
		});
		//icon 提示鼠标划出
		$(document).on("mouseout",".icon_tips",function(){
			var $this = $(this);
			var selector = ".tips_content";
			var parent = $this.parent();
				$(selector,parent).hide();
		});
		//表单验证事件初始化绑定
		check.init();
		check.bind();
	});

	//按钮事件列表
	var actionList = {
		//保存提交事件
		submit : function(obj){
			var ajax = require("./ajax");
			var formData = require("./formData");
			var dialog = require("./dialog");
			var formid = obj.attr("ref_form_id");
			var form = $("#"+formid);
			var datas = formData.get(formid);
			//表单全局验证
			var result = check.check("#"+formid);
			if(!result){
				return;
			}

			var type = form.attr("method") || "post";
			var args = {
				url:form.attr("action"),
				data:datas,
				loadText:'正在保存信息中...',
				success:function(json){
					if(json.success){
						dialog.info("操作成功",1);
						window.location.href = document.referrer;
					}else{
						dialog.info(json.errorMsg,3);
					}
				}
			}
			type=="post" ? ajax.post(args) : ajax.get(args);
		},
		back : function(){
			window.location.href = document.referrer;
		}
	}
});
