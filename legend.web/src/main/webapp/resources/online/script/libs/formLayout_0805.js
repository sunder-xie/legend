/*-- 淘汽云修 2015-12-07 03:32:29 --*/
function formLayoutCalc(a){if(a=a||"body",!$(a).data("calced")){$(a).data("calced",!0);for(var b=function(a,b){a=$(a);var c=a.parent().children(),d=a.parent().width(),e=c.eq(1).outerWidth();c.eq(0).width(b?d-e-b:d-e)},c=function(a){for(var b=a.parentNode.children,c=[],d=0;d<b.length;d++)b[d]!=a&&c.push(b[d]);return c},d=document.getElementsByClassName("search_left"),e=0;e<d.length;e++){var f=d[e];b(f,c(f).length<1?0:10)}$(".form_row",a).each(function(){var a=$(this),b=$(".form_item",a),c=$(".form_item[fixed]",a),d=b.size(),e=~~a.width(),f=10,g=0,h=0,i=(d-1)*f,j=$(".form_item[dynamic_name]",a).size(),k=d-j,l=0;c.each(function(a){d-=1,h+=$(this).width()}),g=(e-i-h)/d,l=k>=1?(e-i-h)/(k+1):e,b.each(function(a){var b=$(this),c=b.attr("dynamic_name"),e=b.attr("fixed");if(e||b.width(~~g),c){b.width(~~l);var h=(a+1)%d;0==h||isNaN(h)?b.css({"margin-right":0}):b.css({"margin-right":f})}else a+1==d?b.css({"margin-right":0}):b.css({"margin-right":f})}),b.each(function(a){var c=b.eq(a),d=b.eq(a-1),e=c.attr("dynamic_name"),f=d.attr("dynamic_name"),g=d&&d.data("calcedWidth"),h=0,i=5;if(g&&e&&e==f)h=g;else{var j=$(".label_box",c),k=c.width(),l=j.width()||0,m=j.size(),n=$(".field_box",c),o=n.size(),p=(o-1)*i,q=0;$(".field_box[fixed]",c).each(function(){q+=$(this).width(),o-=1}),h=(k-m*l-q-p)/o}$(".field_box:not([fixed])",c).width(~~h),$(".field_box:gt(0)",c).css({"margin-left":i}),c.data("calcedWidth",~~h)})}),$(".form_row").css({opacity:1}),$(".form_item[dynamic_name]").parent().each(function(a){var b=$(this);return b.parent().attr("dynamic_group")?!0:(b.find(".form_item[dynamic_name]").each(function(a){var b=$(this);b.find("[num]").text(a+1)}),void b.parent().attr("dynamic_group","true"))})}}$(function(){formLayoutCalc()});