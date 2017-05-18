/**
 * @fileOverview 通用浮出层
 * @author zhanghaiyong
 * @createTime 2014/11/13
 * @updateTime 2014/11/20
 * @version 1.1.1
 */

taoqi = (function () {
    var arr = navigator.userAgent.match(/MSIE (\d+)\.\d+;/);
    var INPUT_EVENT = arr && arr[1] < 9 ? 'propertychange' : 'input';

//    login
    var _loginId;
    function login() {
        if (_loginId != undefined)
            return false;
        _loginId = $.layer({
            type: 1,
            title: false,
            area: ['auto', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.1, '#000000'],
            shadeClose: true,
            closeBtn: [false],
            shift: 'top',
//            move: '.addtit',
            end: function () {
                _loginId = undefined;
            },
            page: {
                html: Dao.html('loginTpl', null)
            }
        });
        $('.ad_bc').click(function () {
        });
        $('.ad_qx').click(function () {
        });
    }

//    tijiao
    var _saId;
    function submitAudit() {
        if (_saId != undefined)
            return false;
        _saId = $.layer({
            type: 1,
            title: false,
            area: ['auto', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.6, '#000000'],
//            shadeClose: true,
            closeBtn: [false],
            shift: 'top',
//            move: '.addtit',
            end: function () {
                _saId = undefined;
            },
            page: {
                html: Dao.html('subadTpl', null)
            }
        });
        $('.bucl button').click(function () {
            window.location.href= BASE_PATH+"/portal/invest/audit_info_ext";
            close(_saId);
        });
        $('.bucl span').click(function () {
            close(_saId);
        });
    }

//    淘汽云修授权合作商意向书
    var _uamId;
    function userAgreement() {
        if (_uamId != undefined)
            return false;
        _uamId = $.layer({
            type: 1,
            title: false,
            area: ['auto', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.6, '#000000'],
//            shadeClose: true,
            closeBtn: [false],
            shift: 'top',
//            move: '.addtit',
            end: function () {
                _uamId = undefined;
            },
            page: {
                html: Dao.html('uagmTpl', null)
            }
        });
        $('.tongyi').on('click', '.tyi', function () {
            close(_uamId);
        });
        $('.tongyi').on('click', '.zbx', function () {
            close(_uamId);
            window.location.href=BASE_PATH+"/portal/invest";
        });
    }



    function success(msg,second) {
        if(second ==null){
            second = 5;
        }
        layer.msg(msg, second, {
            type: 1,
            shade: false,
            rate: 'left'
        });
    }

    function failure(msg,second) {
        if(second ==null){
            second = 5;
        }
        layer.msg(msg, second, {
            type: 0,
            shade: false,
            rate: 'left'
        });
    }

    function ask(msg, yes, no) {
        $.layer({
            area: ['300px', 'auto'],
            border: [0], //去掉默认边框
            shade: [0.5, '#000000'],
            closeBtn: [0, false], //去掉默认关闭按钮
            shift: 'top',
            dialog: {
                msg: msg,
                btns: 2,
                type: 4,
                btn: ['确定', '取消'],
                yes: function () {
                    yes();
                },
                no: function () {
                    no && no();
                }
            }
        });
    }

    function error(msg,second) {
        if(second ==null){
            second = 5;
        }
        layer.msg(msg, second, {
            type: 5,
            shade: false,
            rate: 'left'
        });
    }

    function close(id) {
        if (id == undefined) {
            layer.close(layer.index);
        } else {
            layer.close(id);
        }
    }

    function info(msg, fn) {
        if (msg != undefined) {
            layer.alert(msg, 0, fn);
        }
    }

    function loading(msg, second) {
        if(second != null){
            second = 15;
        }
        return layer.load(msg || '正在加载，请稍后...', second);
    }

    return {
        login: login,
        submitAudit: submitAudit,
        userAgreement: userAgreement,
        success: success,
        error: error,
        close: close,
        ask: ask,
        info: info,
        loading: loading,
        failure: failure
    };

})();

