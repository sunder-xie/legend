/*-- 淘汽云修 2015-12-07 03:32:29 --*/
!function(a){"use strict";"function"==typeof define&&define.amd?define(["jquery"],a):a(window.jQuery)}(function(a){"use strict";var b=0;a.ajaxTransport("iframe",function(c){if(c.async){var d,e,f,g=c.initialIframeSrc||"javascript:false;";return{send:function(h,i){d=a('<form style="display:none;"></form>'),d.attr("accept-charset",c.formAcceptCharset),f=/\?/.test(c.url)?"&":"?","DELETE"===c.type?(c.url=c.url+f+"_method=DELETE",c.type="POST"):"PUT"===c.type?(c.url=c.url+f+"_method=PUT",c.type="POST"):"PATCH"===c.type&&(c.url=c.url+f+"_method=PATCH",c.type="POST"),b+=1,e=a('<iframe src="'+g+'" name="iframe-transport-'+b+'"></iframe>').bind("load",function(){var b,f=a.isArray(c.paramName)?c.paramName:[c.paramName];e.unbind("load").bind("load",function(){var b;try{if(b=e.contents(),!b.length||!b[0].firstChild)throw new Error}catch(c){b=void 0}i(200,"success",{iframe:b}),a('<iframe src="'+g+'"></iframe>').appendTo(d),window.setTimeout(function(){d.remove()},0)}),d.prop("target",e.prop("name")).prop("action",c.url).prop("method",c.type),c.formData&&a.each(c.formData,function(b,c){a('<input type="hidden"/>').prop("name",c.name).val(c.value).appendTo(d)}),c.fileInput&&c.fileInput.length&&"POST"===c.type&&(b=c.fileInput.clone(),c.fileInput.after(function(a){return b[a]}),c.paramName&&c.fileInput.each(function(b){a(this).prop("name",f[b]||c.paramName)}),d.append(c.fileInput).prop("enctype","multipart/form-data").prop("encoding","multipart/form-data"),c.fileInput.removeAttr("form")),d.submit(),b&&b.length&&c.fileInput.each(function(c,d){var e=a(b[c]);a(d).prop("name",e.prop("name")).attr("form",e.attr("form")),e.replaceWith(d)})}),d.append(e).appendTo(document.body)},abort:function(){e&&e.unbind("load").prop("src",g),d&&d.remove()}}}}),a.ajaxSetup({converters:{"iframe text":function(b){return b&&a(b[0].body).text()},"iframe json":function(b){return b&&a.parseJSON(a(b[0].body).text())},"iframe html":function(b){return b&&a(b[0].body).html()},"iframe xml":function(b){var c=b&&b[0];return c&&a.isXMLDoc(c)?c:a.parseXML(c.XMLDocument&&c.XMLDocument.xml||a(c.body).html())},"iframe script":function(b){return b&&a.globalEval(a(b[0].body).text())}}})});