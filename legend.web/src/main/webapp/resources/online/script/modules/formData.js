/*-- 淘汽云修 2016-03-09 04:48:56 --*/
define(function(require,exports,module){exports.get=function(a){var b={};return $(".form_item,.form_item_hidden","#"+a).each(function(){var a=$(this),c=a.attr("dynamic_id"),d=a.attr("dynamic_name"),e=null;return a.parent().hasClass("form_item")?!0:($("input,select,textarea",a).each(function(){var a=$(this),d=a.attr("type"),f=$.trim(a.val()),g=a.attr("no_submit"),h=a.attr("name");if("button"==d||"reset"==d||"submit"==d||""==f||void 0!=g||"file"==d||null==f)return!0;if(c)null!=e?e[h]=f:(e={},e[h]=f);else if("radio"==d){if(!a.is(":checked"))return!0;b[h]=f}else if("checkbox"==d){if(!a.is(":checked"))return!0;b[h]?b[h].push(f):b[h]=[f]}else b[h]=f}),void(c&&(b[d]?null!=e&&b[d].push(e):(b[d]=[],null!=e&&b[d].push(e)))))}),$.each(b,function(a,c){"object"==typeof c&&(b[a]=JSON.stringify(c))}),b},exports.get2=function(a){var b={};return $("input,select,textarea",a).each(function(){var a=$(this),c=a.attr("type"),d=$.trim(a.val()),e=a.attr("no_submit"),f=a.attr("name");if("button"==c||"reset"==c||"submit"==c||""==d||void 0!=e||"file"==c)return!0;if("radio"==c){if(!a.is(":checked"))return!0;b[f]=d}else if("checkbox"==c){if(!a.is(":checked"))return!0;b[f]?b[f].push(d):b[f]=[d]}else b[f]=d}),b},exports.get3=function(a){var b={};return $("input,select,textarea",a).each(function(){var a=$(this),c=a.attr("type"),d=$.trim(a.val()),e=a.attr("no_submit"),f=a.attr("name"),g=["button","reset","submit","file"];if(-1!=g.indexOf(c)||void 0!=e)return!0;if("radio"==c){if(!a.is(":checked"))return!0;b[f]=d}else if("checkbox"==c){if(!a.is(":checked"))return!0;b[f]?b[f].push(d):b[f]=[d]}else b[f]=d}),b}});