/*-- 淘汽云修 2016-03-09 04:48:56 --*/
define(function(require,exports,module){require("./../libs/layer/layer"),module.exports={dialog:function(a){var b,c=$.extend({type:1,width:"auto",height:"auto",closeBtn:[1,!0],dom:"",shift:"top",fix:!0,html:"",move:".xubox_title",init:function(){},close:function(){}},a);c.url=$(c.dom).attr("service_url")||"",c.title=$(c.dom).attr("dialog_title")||!1,""!=c.url?(c.type=2,c.iframe={src:c.url},b={}):b=""!=c.dom?{dom:c.dom}:{html:c.html};var d=$.layer({type:c.type,title:c.title,area:[c.width,c.height],iframe:c.iframe,border:[0],shade:[.5,"#000000"],closeBtn:c.closeBtn,shift:c.shift,fix:c.fix,move:c.move,page:b,success:function(){try{formLayoutCalc(c.dom)}catch(a){}b.dom&&$("input[type='text'],select,textarea,hidden",b.dom).each(function(){if("select"==$(this)[0].tagName)$(this).find("option:eq(0)").attr("selected","selected");else{if($(this).hasClass("qxy_input_unit"))return!0;$(this).val("")}}),""!=c.dom&&$(".qxy_err_msg",c.dom).hide(),c.init()},end:function(){c.close&&c.close(),d=void 0,c=void 0}});b.dom&&$(b.dom).data("dialog-id",d);var e="#xubox_layer"+d;return $("body").on("keyup",e,function(a){"27"==a.keyCode&&layer.close(d)}),d},confirm:function(a,b,c){$.layer({area:["300px","auto"],border:[0],shade:[.5,"#000000"],closeBtn:[0,!1],shift:"top",dialog:{msg:a,btns:2,type:4,btn:["确定","取消"],yes:function(a){b(),layer.close(a)},no:function(){c&&c()}}})},inform:function(a){$.layer({area:["300px","auto"],border:[0],shade:[.5,"#000000"],closeBtn:[0,!1],shift:"top",dialog:{msg:a,btns:1,type:1}})},load:function(a,b){var c=layer.load(a||"正在加载，请稍候...",b||0);return c},info:function(a,b,c,d,e){c=c||3;var f={type:b||0,shade:!1,rate:"left"};e===!0&&delete f.shade;var g=layer.msg(a||"信息内容未设置",c,f);return d&&setTimeout(function(){d&&d()},1e3*c),g},close:function(a){layer.close(a)},closeDialog:function(a){var b=$(a).data("dialog-id");b&&layer.close(b)},tips:function(a,b){var c=layer.tips(b,a,{guide:0,isGuide:!0,more:!1,style:["background:#f77c7e; color:#fff; border:1px solid #f77c7e;top:15px;left:10px;","#f77c7e"]});return c}}});