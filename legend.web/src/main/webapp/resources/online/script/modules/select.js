/*-- 淘汽云修 2016-03-09 04:48:56 --*/
define(function(require,exports,module){var a=require("./ajax"),b=require("./../libs/artTemplate/artTemplate"),c=require("./chosenSelect");exports.init=function(){var d=$("select"),e=[];$.each(d,function(a,b){var c={},f=$(b).attr("service_url");return f?(c.obj=[b],c.url=f,$.each(d,function(a,d){var e=$(d).attr("service_url");return b===d||!e||$(d).data("flag")?!0:void(e==f&&($(d).data("flag",!0),c.obj.push(d)))}),void($(b).data("flag")||e.push(c))):!0});for(var f=0;f<e.length;f++)!function(b,c){a.get({url:c,loadShow:!1,success:function(a){for(var c=0;c<b.obj.length;c++)g(a,$(b.obj[c]))}})}(e[f],e[f].url);var g=function(a,d){d.find("option:gt(0)").remove();var e=d.siblings("script").html(),f=b.compile(e),g=f({json:a});d.append(g);var h=d.attr("val");h&&d.val(h),d.hasClass("chosen")&&c.handleChoosenSelect(d)}}});