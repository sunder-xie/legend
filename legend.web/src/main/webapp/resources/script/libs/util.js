//全局的util工具类
var util = {
	//form表单手动提交数据的方法
	submit : function(option){
		var args = $.extend({
			formid : "", //表单id
			extData : {}, //扩展数据字段
			url : "",
			method : "",
			loadShow : true, //是否显示菊花转
			loadText : '正在保存信息中...', //菊花后面的文字提示
			callback : null //提交数据响应后的回调方法
		},option);
		//获取依赖模块
		seajs.use(["ajax","formData","dialog","check"],function(ajax,formData,dialog,check){
			var form = $("#"+args.formid);
			//获取表单数据
			var datas = {};
			if(args.method != "" && args.method == "get2"){
				//获取form表单中的扁平化数据（没有嵌套关系）
				datas = formData.get2("#"+args.formid);
			}else{
				//获取form_item下的input的值,有动态生成行的数据，做特殊处理
				datas = formData.get(args.formid);
			}
			//表单全局验证
			var result = check.check("#"+args.formid);
			if(!result){
				return;
			}
			var type = form.attr("method") || "post";

			var url = args.url == "" ? form.attr("action") : args.url;

			var ajaxArgs = {
				url:url,
				data:$.extend(datas,args.extData),
				contentType : args.contentType || "application/x-www-form-urlencoded",
				loadShow : args.loadShow || true,
				loadText:args.loadText,
				success:function(json){
					if(args.callback != null && typeof(args.callback) == "function"){
						//如果有自定义回调，则优先执行自定义回调
						//回调中参数包括json返回数据和弹框对象
						args.callback(json,dialog);
					}else{
						//默认的回调方法
						if(json.success){
							dialog.info("操作成功",1);
							history.go(-1);
						}else{
							dialog.info(json.errorMsg,3);
						}
					}
				}
			}
			type=="post" ? ajax.post(ajaxArgs) : ajax.get(ajaxArgs);
		});
	},
	//金额转化成三位逗号分隔
	ammountFormat:function(strNum){
		strNum += "";
		if(strNum=="" || strNum.length == 0){
			return strNum;
		}
		if (strNum.length <= 3){
			if(strNum.indexOf(".")!=-1){
				return strNum;
			}
			return strNum + ".00";
		}
		if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)){
			return strNum;
		}
		var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
		var re = new RegExp();
		re.compile("(\\d)(\\d{3})(,|$)");
		while (re.test(b)) {
			b = b.replace(re, "$1,$2$3");
		}
		if(c!=""){
			c = parseFloat(c).toFixed(2) + "";
			c = c.substring(1);
		}else{
			c = ".00";
		}
		return a + "" + b + "" + c;
	},
	//金额格式化后还原成数字
	ammountReversion:function(strNum){
		strNum += "";
		if (strNum.length <= 3){
			return strNum;
		}
		return strNum.replace(/,+/g, "");
	},
	//生成字母加数字的随机字符串
	ccid : function(n) {
		var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
	    var res = "";
	    for(var i = 0; i < n ; i ++) {
	        var id = Math.ceil(Math.random()*35);
	        res += chars[id];
	    }
	    return res;
	},
	//获取URL中参数的值
	getQueryString : function (name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		var r = window.location.search.substr(1).match(reg);
		if (r != null) return unescape(r[2]); return null;
	},
	//图片上传的参数配置
	uploadInit : function(fn){
		var uploadComplete =  function(file,data){
			if(typeof(fn) != "function"){
        		data = eval('(' + data + ')');
	            if (data.success) {
	                var imgInfo = data.data;
	                for (fileName in imgInfo) {
	                    var imgPathObj = imgInfo[fileName];
	                    var thumb = imgPathObj['thumb'];
	                    $("#goods_img_preview").attr("src", thumb);
	                    $(".goods_img_hidden").val(thumb);
	                }
	            }
        	}else{
        		fn(file,data);
        	}
		}
		seajs.use("upload",function(a){
			a();
			$(".qxy_file").uploadifive({
				'uploadLimit' : 1,
		        'buttonText' : '选择图片上传',
		        'uploadScript' : BASE_PATH + '/index/oss/upload_image_name',
		        'onUploadComplete' : function(file, data) {
		        	uploadComplete(file,data);
		        }
			});
		});
	},
	//车型选择
	carTypeSelect : function(callback){
		seajs.use(["ajax","dialog","artTemplate"],function(ajax,dialog,art){
	 		ajax.get({
	 			url : BASE_PATH + "/shop/car_category/hot",
	 			success : function(json){
	 				success(json);
	 			}
	 		});
	 		art.helper('$toStr', function (obj) {
		        return JSON.stringify(obj);
		    });
		    var inRes = [],
		        inCur = -1;

		    function inputFixed(v) {
		        inRes = [];
		        inCur = -1;
		        var arr = $('.carBox dl').children();
		        var reg = new RegExp(v, 'i');
		        arr.each(function (i) {
		            var str = $.trim($(this).text());
		            reg.test(str) && inRes.push($(this));
		        });
		        updateNum();
		    }

		    function updateNum() {
		        var n = inCur + 1;
		        $('.cm_sp').text('第 ' + n + ' 条，共 ' + inRes.length + ' 条');
		    }
	 		var success = function(json){
	 			var html = art("car_type_template",json);
	 			var dialogId = dialog.dialog({
		 			"html":html
		 		});
		 		$('.cm_b').on('click', '.cm_tit li', function () {
			        var $t = $(this);
			        if ($t.hasClass('current'))
			            return;
			        var n = $t.index();
			        var od = $t.siblings('.current');
			        var wp = $('.carBox');
			        var val = $.trim($('.cm_ss').val());
			        if (n == 1 && $.trim(wp.eq(n).html()) == '') {
			            ajax.get({
			            	url:BASE_PATH + '/shop/car_category/brand_letter',
			            	success:function(json){
			            		var html = art("car_all_type_template",json);
			            		wp.eq(n).html(html);
				                od.removeClass('current');
				                wp.eq(od.index()).hide();
				                $t.addClass('current');
				                wp.eq(n).show();
				                val != '' && inputFixed(val);
			            	}
			            });
			        } else {
			            od.removeClass('current');
			            wp.eq(od.index()).hide();
			            $t.addClass('current');
			            wp.eq(n).show();
			            n == 1 && val != '' && inputFixed(val);
			        }
			    });
			    $('.cm_ss').on("click", function () {
			        var $cur = $('.cm_tit li:last');
			        var val = $.trim(this.value);
			        if (val == '')
			            return;
			        if ($cur.hasClass('current')) {
			            inputFixed(val);
			        } else {
			            $cur.click();
			        }
			    });
			    $('.cm_xia').click(function () {
			        var l = inRes.length;
			        if (l > 0) {
			            if (++inCur >= l)
			                inCur = 0;
			            var dom = inRes[inCur];
			            var par = dom.parents('.carBox');
			            var ph = dom.offset().top - par.offset().top;
			            par.scrollTop(par.scrollTop() + ph);
			            updateNum();
			        }
			    });
			    $('.cm_ss').on('keypress', function (e) {
			        if (e.keyCode == 13) {
			            var l = inRes.length;
			            if (l > 0) {
			                if (++inCur >= l)
			                    inCur = 0;
			                var dom = inRes[inCur];
			                var par = dom.parents('.carBox');
			                var ph = dom.offset().top - par.offset().top;
			                par.scrollTop(par.scrollTop() + ph);
			                updateNum();
			            }
			        }
			    });
			    $('.cm_shang').click(function () {
			        var l = inRes.length;
			        if (l > 0) {
			            if (--inCur < 0)
			                inCur = l - 1;
			            var dom = inRes[inCur];
			            var par = dom.parents('.carBox');
			            var ph = dom.offset().top - par.offset().top;
			            par.scrollTop(par.scrollTop() + ph);
			            updateNum();
			        }
			    });
			    $('.cm_b').on('click', '.cart1', function () {
			        var $t = $(this);
			        if ($t.hasClass('current')) {
			            $t.removeClass('current');
			            $t.next().slideUp();
			        } else {
			            var cur = $t.parents('.carBox').find('.current');
			            if (cur.length != 0) {
			                cur.removeClass('current');
			                cur.next().hide();
			            }
			            var dom = $t.parent();
			            var par = dom.parents('.carBox');
			            if ($t.next().length != 0) {
			                $t.next().slideDown(function () {
			                    var ph = dom.offset().top - par.offset().top;
			                    par.scrollTop(par.scrollTop() + ph);
			                });
			                $t.addClass('current');
			            } else {
			            	ajax.get({
			            		url:BASE_PATH+"/shop/car_category",
			            		data:{pid:$t.data('id')},
			            		success:function(json){
			            			var html = art("car_type_child_template",json);
			            			$t.after(html);
				                    $t.addClass('current');
				                    $t.next().slideDown(function () {
				                        var ph = dom.offset().top - par.offset().top;
				                        par.scrollTop(par.scrollTop() + ph);
				                    });
			            		}
			            	});
			            }
			        }
			    });
			    $('.cm_b').on('click', '.cart2', function () {
			        var obj = $(this).data('obj');
			        obj.pid = $(this).parent().prev().data('id');
			        callback && callback(obj);
			        dialog.close(dialogId);
			    });
	 		}
	 	});
    },
    //是否是低版本ie(6,7,8),并返回版本号
    isLowIE: function () {
        //判断ie版本
        var user_agent = navigator.userAgent;
        var result = {};
        if (user_agent.indexOf("MSIE 6.0") != -1) {
            //ie6
            result.isLowIE = true;
            result.version = 6;
        } else if (user_agent.indexOf("MSIE 7.0") != -1) {
            //ie7
            result.isLowIE = true;
            result.version = 7;
        } else if (user_agent.indexOf("MSIE 8.0") != -1) {
            //ie8
            result.isLowIE = true;
            result.version = 8;
        }else if (user_agent.indexOf("MSIE 9.0") != -1) {
			//ie8
			result.isLowIE = true;
			result.version = 9;
		} else {
            result.isLowIE = false;
        }
        return result;
    },
    //根据时间戳返回格式化的时间
    //timer是时间戳
    //type是连接年月日的符号，例如 2015-14-14中的“-”，如果不指定默认是"-"
    formatDate : function(timer,type){
    	var _date = new Date(timer),
    		_year = _date.getFullYear(),
    		_month = _date.getMonth()+1,
    		_day = _date.getDate(),
    		_type = type || "-";
    	if(_type == "ch"){
    		return _year + "年" + _month + "月" + _day + "日";
    	}
    	return _year + _type + _month + _type + _day;
    },
    //加法函数
	Jia : function(arg1, arg2) {
		var r1, r2, m;
		try {
			r1 = arg1.toString().split(".")[1].length;
		}
		catch (e) {
			r1 = 0;
		}
		try {
			r2 = arg2.toString().split(".")[1].length;
		}
		catch (e) {
			r2 = 0;
		}
		m = Math.pow(10, Math.max(r1, r2));
		return (arg1 * m + arg2 * m) / m;
	},
	//减法函数
	Jian : function (arg1, arg2) {
		var r1, r2, m, n;
		try {
			r1 = arg1.toString().split(".")[1].length;
		}
		catch (e) {
			r1 = 0;
		}
		try {
			r2 = arg2.toString().split(".")[1].length;
		}
		catch (e) {
			r2 = 0;
		}
		m = Math.pow(10, Math.max(r1, r2));
	     //last modify by deeka
	     //动态控制精度长度
		n = (r1 >= r2) ? r1 : r2;
		return Number(((arg1 * m - arg2 * m) / m).toFixed(n));
	},
	//乘法函数
	Cheng : function(arg1, arg2) {
		var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
		try {
			m += s1.split(".")[1].length;
		}
		catch (e) {
		}
		try {
			m += s2.split(".")[1].length;
		}
		catch (e) {
		}
		return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
	},
	//除法函数
	Chu : function(arg1, arg2) {
		var t1 = 0, t2 = 0, r1, r2;
		try {
			t1 = arg1.toString().split(".")[1].length;
		}
		catch (e) {
		}
		try {
			t2 = arg2.toString().split(".")[1].length;
		}
		catch (e) {
		}
		with (Math) {
			r1 = Number(arg1.toString().replace(".", ""));
			r2 = Number(arg2.toString().replace(".", ""));
			return (r1 / r2) * pow(10, t2 - t1);
		}
	},
	//是否是谷歌浏览器
	isChrome : function(){
		var str = window.navigator.userAgent;
		return /chrome/gi.test(str);
	},
	print : function(url){
		if(util.isChrome()){
			$("body").append("<iframe style='display:none;' src='"+url+"'></iframe>");
		}else{
			window.open(url);
		}
	}
}


;(function($){
	//加
	Number.prototype.Jia = function (arg) {  
	    return util.Jia(arg, this);  
	};
	//减
	Number.prototype.Jian = function (arg) {  
	    return util.Jian(this, arg);  
	};
	//乘
	Number.prototype.Cheng = function (arg) {  
	    return util.Cheng(arg, this);  
	};
	//除
	Number.prototype.Chu = function (arg) {  
	    return util.Chu(this, arg);  
	};     
	//如果不是数字返回0，如果是数字则返回本身值
	$.fn.v = function(){
		var val = this.val();
		if(!$.isNumeric(val)){
			return 0;
		}
		return Number(val);
	}
	/*垂直菜单组件*/
	$.fn.vmenu = function(options){
		$this = $(this);
		$this.find("p").each(function(i,e){
			$(e).on("click",function(){
				$(this).next("ul").toggle(500);
				$(this).toggleClass("contract");
			});
		})
		$this.find("a").each(function(i,e){
			if($(e).attr('href') == window.location.pathname){
				$(e).addClass('selected');
			}
		})

	}
	//备注放大浮窗
	$.remark = function (opt) {
        var args = $.extend({
            width: 400
        }, opt);
        //单击空白关闭备注prop
        $("body").on("click", function (e) {
            if ($(e.target).data("remark")) {
                return false;
            }
            $(".qxy_remark").remove();
        });
        var qxyRemarkClass = "width: " + args.width + "px;min-height: 100px;padding:10px 20px;background:#fff;border: 1px solid #ccc;border-radius: 5px;display: none;position: absolute;box-shadow: 1px 1px 5px #ddd;z-index:4;"
        var pClass = "font-size: 16px;line-height: 20px;word-wrap: break-word;word-break: break-all;";
        //点击输入框打开备注prop
        $("body").on("click",args.selector,function () {
            var offset = $(this).offset(),
                left = offset.left,
                top = offset.top;
            $(".qxy_remark").remove();
            $("body").append('<div class="qxy_remark" style="' + qxyRemarkClass + '"><p style="' + pClass + '">' + $(this).val() + '</p></div>');
            $(".qxy_remark").fadeIn("fast").offset({top: top, left: left - (args.width + 45)});
            $(this).on("keyup", function(){
                $(".qxy_remark p").html($(this).val());
            });
            $(this).data("remark", true);
        });

    }
})(jQuery);

/*----------  解决ie9下不支持placeholder问题  ---------- */
var placeHolder = {
    //检测
    check : function(){
        return 'placeholder' in document.createElement('input');
    },
    //初始化
    init : function(scope){
        if(!this.check()){
            this.fix(scope || "body");
        }
    },
    //修复
    fix : function(scope){
        $('input[placeholder],areatext[placeholder]',scope).each(function(index, element) {
            var self = $(this), txt = self.attr('placeholder');
            if(self.hasClass("qxy_picker")){
            	return true;
            }
            // self.wrap($('<div></div>').css({position:'relative', zoom:'1', border:'none', background:'none', padding:'none', margin:'none'}));
            self.parent().css("position","relative");
            var pos = self.offset(), h = self.outerHeight(true), paddingleft = self.css('padding-left'),top = self.css("top"),left = self.css("left"),right=self.css("right");
            if(!top || top == "auto"){
            	top = 0;
            }
            if(!left || left == "auto"){
            	left = 0;
            }
            var cssStyle = {position:'absolute', left:left, top:top, height:h, "lineHeight":h+"px", paddingLeft:paddingleft, color:'#aaa',"zIndex":4};
            if(right && right!="auto"){
            	delete cssStyle.left;
            	cssStyle.right = right;
            }
            if(self.css("position") == "absolute"){
            	cssStyle.width = self.outerWidth(true);
            	cssStyle.textAlign = "center";
            }
            var holder = $('<span></span>').text(txt).css(cssStyle).appendTo(self.parent());
            self.focusin(function(e) {
                holder.hide();
            }).focusout(function(e) {
                if(!self.val()){
                    holder.show();
                }
            });
			if(self.val() && self.val != ""){
				holder.hide();
			}
            holder.click(function(e) {
                holder.hide();
                self.focus();
            });
        });
    }
};
//执行
$(function(){
	//ie8以下将不能使用此功能。
    // if(util.isLowIE().isLowIE && util.isLowIE().version > 7){
    // 	placeHolder.init();
    // }

    //追加title标题,覆盖重构后，重构前，初始化导入的所有页面。
    var smallTitle = $.trim($("a",".topmenu li.current").text());
    if(!smallTitle){
    	smallTitle = $.trim($(".cont_ul li a.current").text());
    }
    smallTitle && $("title").prepend(smallTitle + " - ");
});

//控制台打印图案
// $(function(){
// 	try{
// 		if(window.console&&window.console.log){
// 			console.log("%c和我们一起 改变未来","width:100%;line-height:60px;padding:15px 30px 15px 160px;background:#232e49 url(http://www.yunqixiu.com/legend/resources/images/join/logo.png) no-repeat 20px -6px;;font-size:2.5em;text-shadow:1px 1px 4px #fff;color:#fff;border-radius:100px;");
// 		}
// 	}catch(e){}
// });
