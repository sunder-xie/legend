/**
 * Created by sky on 16/4/22.
 * 本地存储插件
 */

define(function(require, exports, module) {

    var _isSupported = (function () {
            var key = '__' + Math.round(Math.random() * 1e7);
            if (window.localStorage) {
                try {
                    localStorage.setItem(key, key);
                    localStorage.removeItem(key);
                } catch (ex) {
                    console.error('浏览器不支持Storage存储！');
                    return false;
                }
            } else {
                console.error('浏览器不支持Storage存储！');
                return false;
            }
            return true;
        }()),
        // 类型检查
        _isType = function (data) {
            return Object.prototype.toString.call(data).slice(8, -1).toLowerCase();
        },
        // 获得localStorage的值
        _getStorage = function (key) {
            var value, result = {data: ''};

            if (_isSupported) {
                value = localStorage.getItem(prefix + key);
                if (value) {
                    try {
                        result = JSON.parse(value);
                        return result;
                    } catch (ex) {
                        return result;
                    }
                }
                return result;
            }
            return result;
        },
        // 设置localStorage的值（expires的值为0时，将永久保存）
        _setStorage = function (key, value, expires) {
            var _value = {
                create: Date.now(),
                expires: (expires == null ? 7*24*60*60*1000 : expires),
                data: value
            };

            if(_isSupported) {
                localStorage.setItem(prefix + key, JSON.stringify(_value));
            }
        },
        // 存储长度
        _sizeof = function(str, charset) {
            //TODO 计算localStorage的容量
        },
        // 检查生命周期是否已过期
        _checkExpires = function () {
            var i = 0, key, value, expires;
            for (; i < localStorage.length; i++) {
                key = localStorage.key(i);
                if ( !prefixReg.test(key) ) continue;
                value = _getStorage(key);
                if (value) {
                    try {
                        value = JSON.parse(value);
                        expires = Date.now() - Number(value.create);
                        // value.expires为0或者false时，为永久储存
                        if (value.expires == null || expires > Number(value.expires)) {
                            localStorage.removeItem(key);
                        }
                    } catch (ex) {
                        console.error('localStorage中，' + key + '的值转换失败，请检查！');
                    }
                } else {
                    localStorage.removeItem(key);
                }
            }
        },
        prefix = 'yqx_',
        prefixReg = /^yqx_/;


    // 获取本地存储（支持传入多个参数）
    module.exports.getItem = function() {
        var result = '',
            len = arguments.length,
            key, i;

        if ( !_isSupported ) {
            return (len > 1 ? {} : '');
        }

        if (len == 1) {
            key = arguments[0];
            if ( _isType(key) === 'string' ) {
                _checkExpires();
                return _getStorage(key).data;
            }
        } else {
            result = {};
            for(i = 0; i < len; i++) {
                key = arguments[i];
                if ( _isType(key) === 'string' ) {
                    _checkExpires();
                    result[key] = _getStorage(key).data;
                }
            }
            return result;
        }
    };

    // 设置本地存储
    module.exports.setItem = function(key, value, expires) {
        if( _isType(key) === 'string' ) {
            if ( isNaN(expires) ) expires = '';
            _setStorage(key, value, expires);
        } else {
            console.error('key 必须为一个字符串！');
        }
    }
});