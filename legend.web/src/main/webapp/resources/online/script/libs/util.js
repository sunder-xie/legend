/*-- 淘汽云修 2015-12-07 03:32:29 --*/
var util={submit:function(a){var b=$.extend({formid:"",extData:{},url:"",method:"",loadShow:!0,loadText:"正在保存信息中...",callback:null},a);seajs.use(["ajax","formData","dialog","check"],function(a,c,d,e){var f=$("#"+b.formid),g={};g=""!=b.method&&"get2"==b.method?c.get2("#"+b.formid):c.get(b.formid);var h=e.check("#"+b.formid);if(h){var i=f.attr("method")||"post",j=""==b.url?f.attr("action"):b.url,k={url:j,data:$.extend(g,b.extData),contentType:b.contentType||"application/x-www-form-urlencoded",loadShow:b.loadShow||!0,loadText:b.loadText,success:function(a){null!=b.callback&&"function"==typeof b.callback?b.callback(a,d):a.success?(d.info("操作成功",1),history.go(-1)):d.info(a.errorMsg,3)}};"post"==i?a.post(k):a.get(k)}})},ammountFormat:function(a){if(a+="",""==a||0==a.length)return a;if(a.length<=3)return-1!=a.indexOf(".")?a:a+".00";if(!/^(\+|-)?(\d+)(\.\d+)?$/.test(a))return a;var b=RegExp.$1,c=RegExp.$2,d=RegExp.$3,e=new RegExp;for(e.compile("(\\d)(\\d{3})(,|$)");e.test(c);)c=c.replace(e,"$1,$2$3");return""!=d?(d=parseFloat(d).toFixed(2)+"",d=d.substring(1)):d=".00",b+""+c+d},ammountReversion:function(a){return a+="",a.length<=3?a:a.replace(/,+/g,"")},ccid:function(a){for(var b=["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"],c="",d=0;a>d;d++){var e=Math.ceil(35*Math.random());c+=b[e]}return c},getQueryString:function(a){var b=new RegExp("(^|&)"+a+"=([^&]*)(&|$)","i"),c=window.location.search.substr(1).match(b);return null!=c?unescape(c[2]):null},uploadInit:function(fn){var uploadComplete=function(file,data){if("function"!=typeof fn){if(data=eval("("+data+")"),data.success){var imgInfo=data.data;for(fileName in imgInfo){var imgPathObj=imgInfo[fileName],thumb=imgPathObj.thumb;$("#goods_img_preview").attr("src",thumb),$(".goods_img_hidden").val(thumb)}}}else fn(file,data)};seajs.use("upload",function(a){a(),$(".qxy_file").uploadifive({uploadLimit:1,buttonText:"选择图片上传",uploadScript:BASE_PATH+"/index/oss/upload_image_name",onUploadComplete:function(a,b){uploadComplete(a,b)}})})},carTypeSelect:function(a){seajs.use(["ajax","dialog","artTemplate"],function(b,c,d){function e(a){g=[],h=-1;var b=$(".carBox dl").children(),c=new RegExp(a,"i");b.each(function(a){var b=$.trim($(this).text());c.test(b)&&g.push($(this))}),f()}function f(){var a=h+1;$(".cm_sp").text("第 "+a+" 条，共 "+g.length+" 条")}b.get({url:BASE_PATH+"/shop/car_category/hot",success:function(a){i(a)}}),d.helper("$toStr",function(a){return JSON.stringify(a)});var g=[],h=-1,i=function(i){var j=d("car_type_template",i),k=c.dialog({html:j});$(".cm_b").on("click",".cm_tit li",function(){var a=$(this);if(!a.hasClass("current")){var c=a.index(),f=a.siblings(".current"),g=$(".carBox"),h=$.trim($(".cm_ss").val());1==c&&""==$.trim(g.eq(c).html())?b.get({url:BASE_PATH+"/shop/car_category/brand_letter",success:function(b){var i=d("car_all_type_template",b);g.eq(c).html(i),f.removeClass("current"),g.eq(f.index()).hide(),a.addClass("current"),g.eq(c).show(),""!=h&&e(h)}}):(f.removeClass("current"),g.eq(f.index()).hide(),a.addClass("current"),g.eq(c).show(),1==c&&""!=h&&e(h))}}),$(".cm_ss").on("click",function(){var a=$(".cm_tit li:last"),b=$.trim(this.value);""!=b&&(a.hasClass("current")?e(b):a.click())}),$(".cm_xia").click(function(){var a=g.length;if(a>0){++h>=a&&(h=0);var b=g[h],c=b.parents(".carBox"),d=b.offset().top-c.offset().top;c.scrollTop(c.scrollTop()+d),f()}}),$(".cm_ss").on("keypress",function(a){if(13==a.keyCode){var b=g.length;if(b>0){++h>=b&&(h=0);var c=g[h],d=c.parents(".carBox"),e=c.offset().top-d.offset().top;d.scrollTop(d.scrollTop()+e),f()}}}),$(".cm_shang").click(function(){var a=g.length;if(a>0){--h<0&&(h=a-1);var b=g[h],c=b.parents(".carBox"),d=b.offset().top-c.offset().top;c.scrollTop(c.scrollTop()+d),f()}}),$(".cm_b").on("click",".cart1",function(){var a=$(this);if(a.hasClass("current"))a.removeClass("current"),a.next().slideUp();else{var c=a.parents(".carBox").find(".current");0!=c.length&&(c.removeClass("current"),c.next().hide());var e=a.parent(),f=e.parents(".carBox");0!=a.next().length?(a.next().slideDown(function(){var a=e.offset().top-f.offset().top;f.scrollTop(f.scrollTop()+a)}),a.addClass("current")):b.get({url:BASE_PATH+"/shop/car_category",data:{pid:a.data("id")},success:function(b){var c=d("car_type_child_template",b);a.after(c),a.addClass("current"),a.next().slideDown(function(){var a=e.offset().top-f.offset().top;f.scrollTop(f.scrollTop()+a)})}})}}),$(".cm_b").on("click",".cart2",function(){var b=$(this).data("obj");b.pid=$(this).parent().prev().data("id"),a&&a(b),c.close(k)})}})},isLowIE:function(){var a=navigator.userAgent,b={};return-1!=a.indexOf("MSIE 6.0")?(b.isLowIE=!0,b.version=6):-1!=a.indexOf("MSIE 7.0")?(b.isLowIE=!0,b.version=7):-1!=a.indexOf("MSIE 8.0")?(b.isLowIE=!0,b.version=8):-1!=a.indexOf("MSIE 9.0")?(b.isLowIE=!0,b.version=9):b.isLowIE=!1,b},formatDate:function(a,b){var c=new Date(a),d=c.getFullYear(),e=c.getMonth()+1,f=c.getDate(),g=b||"-";return"ch"==g?d+"年"+e+"月"+f+"日":d+g+e+g+f},Jia:function(a,b){var c,d,e;try{c=a.toString().split(".")[1].length}catch(f){c=0}try{d=b.toString().split(".")[1].length}catch(f){d=0}return e=Math.pow(10,Math.max(c,d)),(a*e+b*e)/e},Jian:function(a,b){var c,d,e,f;try{c=a.toString().split(".")[1].length}catch(g){c=0}try{d=b.toString().split(".")[1].length}catch(g){d=0}return e=Math.pow(10,Math.max(c,d)),f=c>=d?c:d,Number(((a*e-b*e)/e).toFixed(f))},Cheng:function(a,b){var c=0,d=a.toString(),e=b.toString();try{c+=d.split(".")[1].length}catch(f){}try{c+=e.split(".")[1].length}catch(f){}return Number(d.replace(".",""))*Number(e.replace(".",""))/Math.pow(10,c)},Chu:function(arg1,arg2){var r1,r2,t1=0,t2=0;try{t1=arg1.toString().split(".")[1].length}catch(e){}try{t2=arg2.toString().split(".")[1].length}catch(e){}with(Math)return r1=Number(arg1.toString().replace(".","")),r2=Number(arg2.toString().replace(".","")),r1/r2*pow(10,t2-t1)},isChrome:function(){var a=window.navigator.userAgent;return/chrome/gi.test(a)},print:function(a){util.isChrome()?$("body").append("<iframe style='display:none;' src='"+a+"'></iframe>"):window.open(a)}};!function(a){Number.prototype.Jia=function(a){return util.Jia(a,this)},Number.prototype.Jian=function(a){return util.Jian(this,a)},Number.prototype.Cheng=function(a){return util.Cheng(a,this)},Number.prototype.Chu=function(a){return util.Chu(this,a)},a.fn.v=function(){var b=this.val();return a.isNumeric(b)?Number(b):0},a.fn.vmenu=function(b){$this=a(this),$this.find("p").each(function(b,c){a(c).on("click",function(){a(this).next("ul").toggle(500),a(this).toggleClass("contract")})}),$this.find("a").each(function(b,c){a(c).attr("href")==window.location.pathname&&a(c).addClass("selected")})},a.remark=function(b){var c=a.extend({width:400},b);a("body").on("click",function(b){return a(b.target).data("remark")?!1:void a(".qxy_remark").remove()});var d="width: "+c.width+"px;min-height: 100px;padding:10px 20px;background:#fff;border: 1px solid #ccc;border-radius: 5px;display: none;position: absolute;box-shadow: 1px 1px 5px #ddd;z-index:4;",e="font-size: 16px;line-height: 20px;word-wrap: break-word;word-break: break-all;";a("body").on("click",c.selector,function(){var b=a(this).offset(),f=b.left,g=b.top;a(".qxy_remark").remove(),a("body").append('<div class="qxy_remark" style="'+d+'"><p style="'+e+'">'+a(this).val()+"</p></div>"),a(".qxy_remark").fadeIn("fast").offset({top:g,left:f-(c.width+45)}),a(this).on("keyup",function(){a(".qxy_remark p").html(a(this).val())}),a(this).data("remark",!0)})}}(jQuery);var placeHolder={check:function(){return"placeholder"in document.createElement("input")},init:function(a){this.check()||this.fix(a||"body")},fix:function(a){$("input[placeholder],areatext[placeholder]",a).each(function(a,b){var c=$(this),d=c.attr("placeholder");if(c.hasClass("qxy_picker"))return!0;c.parent().css("position","relative");var e=(c.offset(),c.outerHeight(!0)),f=c.css("padding-left"),g=c.css("top"),h=c.css("left"),i=c.css("right");g&&"auto"!=g||(g=0),h&&"auto"!=h||(h=0);var j={position:"absolute",left:h,top:g,height:e,lineHeight:e+"px",paddingLeft:f,color:"#aaa",zIndex:4};i&&"auto"!=i&&(delete j.left,j.right=i),"absolute"==c.css("position")&&(j.width=c.outerWidth(!0),j.textAlign="center");var k=$("<span></span>").text(d).css(j).appendTo(c.parent());c.focusin(function(a){k.hide()}).focusout(function(a){c.val()||k.show()}),c.val()&&""!=c.val&&k.hide(),k.click(function(a){k.hide(),c.focus()})})}};$(function(){var a=$.trim($("a",".topmenu li.current").text());a||(a=$.trim($(".cont_ul li a.current").text())),a&&$("title").prepend(a+" - ")});