//此模块是对cookie操作的模块
define(function(require,exports,module){
	var expiryHours = 24;
	module.exports = {
		setCookie : function(name, value, hours) {
		    if (String(name) == "undefined" || name == null || name == "") return;
		    var expire = "";
		    if (hours != null) {
		        expire = new Date((new Date()).getTime() + hours * 600000);
		    } else {
		        expire = new Date((new Date()).getTime() + expiryHours * 600000);
		    }
		    expire = "; expires=" + expire.toGMTString();
		    document.cookie = name + "=" + escape(value) + expire;
		},
		removeCookie : function(name) {
		    this.setCookie(name, '', -1);
		},
		clearCookie : function() {
		    var name, a = document.cookie.split(";");
			var that = this;
		    for (var i = 0; i < a.length; i++) {
		        name = a[i].split("=");
		        if (name.length > 0) {
		            that.removeCookie(name[0]);
		        }
		    }
		},
		//在现有cookie值中追加值 
		combine : function() {
		    var args = this.combine.arguments;
		    var result = "";
		    for (var i = 0; i < args.length; i++) {
		        if (String(args[i]) != 'undefined' && args[i] != null && args[i] != "") {
		            if (result != "") result += ",";
		            result += args[i];
		        }
		    }
		    return result;
		},
		//判断cookie中是否存在name,value的值
		existsCookieValue : function(name, value) {
		    var v = this.getCookie(name);
		    if (String(v) == "undefined" || v == null || v == "") {
		        return false;
		    }
		    var a = v.split(',');
		    for (var i = 0; i < a.length; i++) {
		        if (a[i] == value) {
		            return true;
		        }
		    }
		    return false;
		},
		//只判断cookie中是否存在name
		existsCookieKey : function(key) {
		    if (String(key) == "undefined" || key == null || key == "") {
		        return false;
		    }
		    var name, a = document.cookie.split(';');
		    for (var i = 0; i < a.length; i++) {
		        name = a[i].split("=");
		        if (name.length > 0 && name[0] == key) {
		            return true;
		        }
		    }
		    return false;
		},
		//在现有cookie中某一个键对应的值中做追加
		appendCookie : function(name, value, hours) {
		    if (this.existsCookieValue(name, value)) {
		        return;
		    }
		    var v = this.getCookie(name);
		    this.setCookie(name, this.combine(v, value), hours);
		},
		//获取cookie
		getCookie : function(name) {
		    var cookieValue = "";
		    var search = name + "=";
		    if (document.cookie.length > 0) {
		        offset = document.cookie.indexOf(search);
		        if (offset != -1) {
		            offset += search.length;
		            end = document.cookie.indexOf(";", offset);
		            if (end == -1) {
		                end = document.cookie.length;
		            }
		            cookieValue = unescape(document.cookie.substring(offset, end))
		        }
		    }
		    return cookieValue;
		}
	}

});