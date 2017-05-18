<style>
    .yqx-dialog .yqx-dialog-body {
        padding: 0 30px;
    }

    .code-send-info {
        line-height: 52px;
        padding-top: 10px;
        font-size: 12px;
        color: #666;
    }

    .code-send-info .name {
        font-size: 14px;
        margin-right: 20px;
    }

    .code-send-info .mobile {
        font-weight: bold;
        font-size: 14px;
        color: #333;
    }

    .code .form-label {
        color: #666;
    }

    .code-send-btn {
        display: inline-block;
        height: 30px;
        padding: 0 12px;
        line-height: 30px;
        border: 1px solid #607a0c;
        border-radius: 2px;
        margin-left: 10px;
        color: #607a0c;
    }

    .code-send-btn:disabled {
        border: 1px solid #999;
        color: #999;
    }

    .code-btn-box {
        margin-top: 40px;
        text-align: center;
    }

    .code-btn-box a {
        margin: 0 5px;
    }
    .code-input {
        width: 130px;
    }

</style>
<script type="text/html" id="codeDialog">
    <div class="yqx-dialog">
        <div class="yqx-dialog-header">
            <div class="yqx-dialog-headline">验证码</div>
        </div>
        <div class="yqx-dialog-body">
            <h3 class="code-send-info">发送短信验证码至：
                <span class="name"><%= data.name%></span>
                <span class="send-mobile"><%= data.mobile%></span>
                <input type="hidden" name="sendLicense" value="<%= data.license%>"/>
            </h3>

            <div class="code">
                <div class="form-label">请输入</div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small code-input js-code-num" maxlength="10" data-v-type="required" data-label="验证码" readonly>
                </div>
                <button class="code-send-btn js-code">立即发送</button>
            </div>
            <div class="code-btn-box">
                <a href="javascript:;" class="yqx-btn yqx-btn-1 js-cancel">取消</a>
                <a href="javascript:;" class="yqx-btn yqx-btn-3 js-send-confirm">确定</a>
            </div>
        </div>
    </div>
</script>

<script type="text/javascript">
    $(function () {
        seajs.use(['dialog', 'art', 'check', 'ajax'], function (dg, at, ck) {
            // 验证
            ck.init();

            var defaults = {
                data: null,
                callback: null
            };
            var timer = null, dialogNum = 1;//弹框次数

            Components.codeDialog = function (option) {
                var args = $.extend({}, defaults, option);
                var html = at('codeDialog', {data: args.data}),
                    codeDgId;
                codeDgId = dg.open({
                    area: ['400px', '254px'],
                    content: html,
                    shadeClose:false,
                    closeBtn:0
                });

                //如果弹框次数大于1，则验证码可输入
                if(dialogNum > 1) {
                    $('.js-code-num')[0].readOnly = false;
                }

                $(document).off('click', '.js-code')
                        .on('click', '.js-code', function () {
                    sendCode();
                });

                $(document).off('click', '.js-cancel')
                        .on('click', '.js-cancel', function () {
                    dialogNum++;
                    dg.close(codeDgId);
                    timer && clearInterval(timer);
                });

                $(document).off('click', '.js-send-confirm')
                        .on('click', '.js-send-confirm', function () {
                    if(!ck.check()){
                        return false;
                    }
                    var guestMobile = $.trim($('.send-mobile').text());
                    var code = $.trim($('.js-code-num').val());
                    var GuestMobileCheckBo = {
                        guestMobile: guestMobile,
                        code: code
                    };
                    $.ajax({
                        type: 'post',
                        url: BASE_PATH + '/shop/settlement/debit/discount/check-code',
                        data: JSON.stringify(GuestMobileCheckBo),
                        contentType: "application/json"
                    }).done(function (result) {
                        if (result.success) {
                            dg.success("验证通过", function () {
                                dg.close(codeDgId);
                                args.callback();
                            });
                        } else {
                            dg.fail(result.message);
                            return false;
                        }
                    })
                });

                function sendCode() {
                    var $codeNum = $('.js-code-num');
                    if($codeNum[0].readOnly) {
                        $codeNum[0].readOnly = false;
                    }
                    var num = 120;
                    var settlementSmsBO = {
                        license: $('input[name="sendLicense"]').val(),
                        mobile: $('.send-mobile').text()
                    };
                    $.ajax({
                        type: 'post',
                        url: BASE_PATH + '/shop/settlement/debit/discount/send-code',
                        data: JSON.stringify(settlementSmsBO),
                        contentType: "application/json"
                    }).done(function (result) {
                        if (result.success) {
                            timer && clearInterval(timer);
                            timer = setInterval(function () {
                                num--;
                                if (num <= 0) {
                                    clearInterval(timer);
                                    timer = null;
                                    $('.js-code').text('重新发送').prop('disabled', false);
                                } else {
                                    $('.js-code').text('重新发送（' + num + 's）').prop('disabled', true);
                                }
                            }, 1000);
                        } else {
                            dg.fail(result.message)
                        }
                    });
                }

            }
        })
    })
</script>
