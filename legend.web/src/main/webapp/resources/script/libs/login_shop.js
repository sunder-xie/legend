$(function(){
    //默认模板
    defaultTpl();
    function defaultTpl(){
        var html = $('#accountsTpl').html();
        $('#loginCon').html(html)
    }
    //跳转到二维码登录模板、二维码过期刷新二维码
    $(document).on('click','.js-barcode , .js-refresh',function(){
        var html = $('#barcodeTpl').html();
        $('#loginCon').html(html);
        webSocket();
    });
    //跳转到帐号登录模板
    $(document).on('click','.js-login',function(){
        defaultTpl();
    });
    var socketUrl = $(".js-socket-url").val();

    function webSocket(){
        $.ajax({
            type: 'GET',
            url: BASE_PATH + '/index/getQRCode',
            success: function (result) {
                if (result.success) {
                    var data = result.data;
                    var uuid = data[0];
                    var qrCode = data[1];
                    $(".js-qr-code").attr('src',qrCode);
                    new Socket(uuid);
                } else {
                    taoqi.error(result.errorMsg);
                    return false;
                }
            }
        });
    }

    function Socket(uuid) {
        this.url = socketUrl + 'websocket/login/'+uuid;
        this.init();
    }

    Socket.prototype = {
        constructor: Socket,
        init: function () {
            if (window.WebSocket) {
                this.mySocket = new WebSocket(this.url);
                this.bindEvent();
            } else {
                console.log('此浏览器不支持socket通信！');
            }
        },
        bindEvent: function () {
            var ws = this.mySocket;
            ws.addEventListener('open', this.onopen.bind(this));
            ws.addEventListener('message', this.onmessage.bind(this));
            ws.addEventListener('error', this.onerror.bind(this));
            ws.addEventListener('close', this.onclose.bind(this));
        },
        send: function (uuid) {
            return this.mySocket.send(uuid);
        },
        close: function () {
            return this.mySocket.close();
        },
        onopen: function (evt) {
            console.log('open: yes ok')
        },
        onmessage: function (evt) {
            var json = JSON.parse(evt.data);
            if(json.success){
                //跳首页（/home）chookie
                var uuid = json.data[0];
                var session_user_name= json.data[1];
                //cookie7天有效
                SetCookie("UUID",uuid,24);
                SetCookie("SESSION_USER_NAME",session_user_name,24);
                window.location.href = BASE_PATH + '/home';
            }else{
                //跳二维码失效页面
                var html = $('#overdueTpl').html();
                $('#loginCon').html(html)
            }
        },
        onerror: function (evt) {
            console.log('error:' + evt);
        },

        onclose: function (evt) {
            console.log('close: bye bye');
        }

    };
});