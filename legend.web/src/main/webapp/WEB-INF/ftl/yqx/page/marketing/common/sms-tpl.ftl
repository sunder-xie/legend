<#--
    发送短信公共模板，用到此模板页面，请在下面登记一下
     zmx 2016-12-16

    用到的页面：

 -->
<style>
    .sms-dialog {
        height: 443px;
        overflow: hidden;
        padding-bottom: 20px;
    }
    .sms-dialog.yqx-dialog .yqx-dialog-body {
        padding: 10px 25px 0 25px;
        max-height: 400px;
        overflow-y: auto;
    }

    .sms-dialog .dialog-width {
        width: 505px;
    }

    .sms-dialog .note-info, .send-content {
        line-height: 16px;
    }

    .sms-dialog .send-name {
        width: 295px;
    }

    .sms-dialog .send-note {
        width: 100px;
        margin-right: 20px;
        text-align: right;
    }

    .sms-dialog .send-note span {
        color: #fd461e;
    }

    .sms-dialog .pay-btn {
        width: 90px;
    }

    .sms-dialog .send-content textarea {
        width: 505px;
        height: 70px;
    }

    .sms-dialog .count {
        height: 42px;
        overflow: hidden;
    }

    .sms-dialog .count p {
        display: inline-block;
        line-height: 42px;
    }

    .count p span {
        font-weight: bold;
        padding: 0 2px;
    }

    .count button {
        margin-top: 10px;
    }

    .m-right {
        margin-right: 10px;
    }

    .template-box h3 {
        font-size: 12px;
        line-height: 30px;
    }
    .template-box li.hide {
        display: none;
    }

    .template-box li {
        display: block;
        position: relative;
        width: 480px;
        height: 37px;
        line-height: 37px;
        padding: 0 10px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        border: 1px solid #cacaca;
        border-radius: 2px;
        margin-bottom: 5px;
        cursor: pointer;
    }

    .template-box li.note-current {
        background: #e2e7d1;
        border: 2px solid #607b0a;
    }

    .template-box li.note-current:after {
        content: "";
        display: inline;
        width: 18px;
        height: 16px;
        background: url("../../static/img/page/marketing/gather/card-hover.png") no-repeat;
        position: absolute;
        bottom: 0;
        right: 0;
    }

    .template-box li.more {
        text-align: center;
    }
</style>
<script type="text/html" id="smsDialogTpl">
    <div class="yqx-dialog sms-dialog">
        <div class="dialog-title">
            <%=title || '发送短信'%>
        </div>
        <div class="yqx-dialog-body">
            <div class="dialog-width">
                <div class="note-info clearfix">
                    <div class="send-name fl">
                        <p>发送客户：<%=customer%></p>

                        <p>发送客户数：<span class="send-to"><%= len || 0 %></span>人</p>
                    </div>
                    <div class="send-note fl">
                        <p>剩余短信额度</p>

                        <p><span class="message-num"></span>条 <span class="sms-note"></span></p>
                    </div>
                    <div class="pay-btn fl">
                        <a href="${BASE_PATH}/marketing/ng/center/sms" target="_blank"
                           class="yqx-btn yqx-btn-2 yqx-btn-small">立即充值</a>
                    </div>
                </div>
                <div class="send-content">
                    <div>发送内容：</div>
                    <textarea class="yqx-textarea sms-content"><%=sms ? sms.content : ''%></textarea>

                    <div class="count">
                        <p>总计<span class="content-num"></span>字（1条），预计消费短信<span class="sms-num"><%= len || 0 %></span>条
                        </p>
                        <button class="yqx-btn yqx-btn-3 yqx-btn-micro fr js-send">发送</button>
                        <button class="yqx-btn yqx-btn-1 yqx-btn-micro fr m-right js-cancel">取消</button>
                    </div>
                </div>
                <div class="template-box">
                    <h3>选择其他模板</h3>
                    <ul>
                        <% if(sms) {%>
                        <li class="js-note-text js-show-tips note-current"
                            data-id="<%=sms.id%>">
                            <%= sms.content %>
                        </li>
                        <%}%>
                        <% for(var i=0;i < result.length;i++) { %>
                        <%if(sms == null || (sms && sms.content !== result[i].content) ) {%>
                            <%if(i <= 2 && result.length > 3) { %>
                        <li class="js-note-text js-show-tips <%if(i == 0 && sms == null) {%><%='note-current'%><%}%>"
                            data-id="<%=result[i].id%>">
                            <%= result[i].content %>
                        </li>
                        <%} else if(i > 2 ) {%>
                        <li class="js-note-text hide js-show-tips"
                            data-id="<%=result[i].id%>">
                            <%= result[i].content %>
                        </li>
                        <%}%>
                        <% } %>
                        <% } %>
                        <% if(result.length > 3) { %>
                        <li class="more js-more">更多<i class="icon-angle-down"></i></li>
                        <% } %>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</script>

<script type="application/javascript">
    seajs.use([
        'dialog',
        'art'
    ], function (dialog, art) {
        var baseUrl = BASE_PATH + '/shop/marketing/sms/new';
        /*-------------- 营销：短信 start ------------------*/
        var $doc = $(document);
        var _loading;

        dialog.titleInit();

        function MarketingSMS(selectedCustomer, data, opt, _url, defaultType) {
            // 选择的用户
            this.selectedCustomer = selectedCustomer || {};
            // 打开弹窗获取到的数据
            this.smsTplData = null;
            // 弹窗 id
            this.dialogId = undefined;
            // 选择的短信模板 string
            this.template = undefined;
            this.position = opt.position;
            // 弹窗之前的动作，比如改变弹窗标题
            this.beforeDialog = opt.beforeDialog;
            // 发送短信后的
            this.callback = opt.callback;

            // 模板附加的内容
            this.appendText = opt.append;

            delete opt.position;
            // 位置 enum
            this.data = data.base;
            this.allData = data.all;
            // 保存的短信信息：如条数等等,
            // smsUID 为最终发送时需要的数据
            this.smsData = {};

            // 默认的短信模板的 type 参数
            this.defaultType = defaultType;

            this.type = opt.type;
            this.paramType = opt.paramType;

            this.url = _url.url;
            this.allUrl = _url.allUrl;

            // 是发送全部么
            this.isAllSend = false;
            // 初始化, dom event
            this.init();

            return this;
        }

        MarketingSMS.prototype = {
            showDialog: function (type) {
                var url = BASE_PATH + '/marketing/ng/sms/template/list';//这里是短信列表接口地址
                var sendTo = getCustomerNum(this.selectedCustomer);
                var self = this;
                var noteType = typeof self.data == 'function'
                                    ? self.data().noteType
                                    : self.data.noteType;
                var data = (this.beforeDialog && this.beforeDialog()) || {};

                if (sendTo == 0 && type !== 'all') {
                    dialog.warn('请选择有手机号的用户！');
                    return false;
                }

                if(typeof this.data == 'function' && !this.data()) {
                    return false;
                }

                self.isAllSend = type == 'all';

                openLoading();
                getTemplate(this.defaultType, function (sms) {
                    $.get(url, function (result) {
                        closeLoading();
                        if (self.dialogId != undefined || !result.success) {
                            return;
                        }

                        self.smsTplData = result;

                        self.dialogId = dialog.open({
                            end: function () {
                                self.dialogId = undefined;
                            },
                            content: art('smsDialogTpl', {
                                'result': result.data,
                                len: sendTo,
                                type: noteType,
                                sms: sms,
                                title: data.title,
                                customer: data.customer
                            })
                        });

                        preProcessTemplate.call(self,
                                getCurrentTemplate.call(self),
                                $('.sms-dialog .sms-content')
                        );
                        messageNum.call(self);
                    })
                });
            },
            closeDialog: function () {
                dialog.close(this.dialogId);
            },
            getCarId: function () {
                var data = typeof this.selectedCustomer == 'function'
                        ? this.selectedCustomer()
                        : this.selectedCustomer;
                var ret = [];

                for (var key in data) {
                    ret.push(data[key].customerCarId);
                }

                return ret.join(',');
            },
            init: function () {
                var self = this;

                //点击发短信按钮出现弹窗
                $doc.on('click', '.js-message', function () {
                    self.showDialog();
                })
                        .on('click', '.js-message-all', function () {
                            self.showDialog('all');
                        })
                        .on('click', '.return', function () {
                            self.closeDialog();
                        });

                //点击短信模版效果
                $doc.on('click', '.js-note-text', function () {
                    var box = $('.sms-dialog .sms-content');
                    var text = $.trim($(this).text());

                    $(this).addClass('note-current')
                            .siblings().removeClass('note-current');
                    box.val(text);

                    preProcessTemplate.call(self, getCurrentTemplate.call(self), box);
                });


                $doc.on('blur', '.sms-dialog .sms-content', function () {
                    preProcessTemplate.call(self, $.trim($(this).text()), $(this));
                });

                $doc.on('click', '.sms-dialog .js-send', function () {
                    var remindNum = $('.message-num').data('messageNum');

                    if (self.smsData.number > remindNum) {
                        dialog.warn('短信余额不足，请充值！');
                        return false;
                    } else if(self.smsData.mobileNumber == 0) {
                        dialog.warn('当前可无发送的客户');
                        return;
                    }
                    sendMessage.call(self);
                });
                //点击取消
                $doc.on('click', '.js-cancel', function () {
                    self.closeDialog();
                });
                // 更多
                $doc.on('click', '.sms-dialog .js-more', function () {
                    var li = $('.sms-dialog .js-note-text.hide');
                    li.slice(0, 3)
                            .show().removeClass('hide');

                    if ($('.sms-dialog .js-note-text.hide').length === 0) {
                        $(this).remove();
                    }
                });
            }
        };

        function getCustomerNum(selectedCustomer) {
            var num = 0;
            var t = typeof selectedCustomer == 'function'
                    ? selectedCustomer()
                    : selectedCustomer;

            for (var i in t) {
                if (t[i] && t[i].mobile) {
                    num++;
                }
            }

            return num;
        }

        //剩余短信数量
        function messageNum() {
            openLoading();

            $.getJSON(BASE_PATH + '/marketing/ng/sms/template/remain', function (json) {
                closeLoading();
                if (json.success && json.data != null) {
                    $('.sms-dialog .message-num').text(json.data)
                            .attr('data-message-num', json.data);

                    if (self.smsData && self.smsData.number > json.data) {
                        $('.sms-dialog .sms-note').text('余额不足！');
                    } else {
                        $('.sms-dialog .sms-note').text('');
                    }
                }
            });
        }

        function getCurrentTemplate() {
            var appendText = this.appendText && typeof this.appendText == 'function'
                    ? this.appendText()
                    : this.appendText;
            var content = $.trim($('.sms-dialog .sms-content').val());

            return content + (appendText || '');
        }

        function preProcessTemplate(template, $dom) {
            var self = this;


            var setting = {
                url: baseUrl + '/template_process',
                data: {
                    template: template
                },
                type: this.type || 'get',
                success: function (json) {
                    if (json.success && json.data) {
                        self.template = json.data;
                        $dom.val(json.data);
                        $('.sms-dialog .content-num').text(json.data.length);

                        if (json.data) {
                            if (!self.isAllSend) {
                                getSendSmsNum.call(self, json.data);
                            } else {
                                getAllSendSmsNum.call(self, json.data);
                            }
                        }
                    } else {
                        closeLoading();
                        dialog.fail(json.message || json.errorMsg || '获取预处理模板失败');
                    }
                }
            };


            openLoading();
            $.ajax(setting);
        }

        // 获取预计的发送短信数目
        function getSendSmsNum(template) {
            var url = baseUrl + '/calculate_number';
            var data = {
                template: template,
                carIds: this.getCarId(),
                position: this.position
            };

            getSmsNum.call(this, url, data);
        }

        function getSmsNum(url, data) {
            var self = this;
            var remindNum = $('.message-num').data('messageNum'),
                    num = 0;
            var box = $('.sms-dialog');

            if(typeof url == 'function') {
                url = url();
            }
            openLoading();
            $.get(url, data, function (json) {
                closeLoading();
                if (json.success && json.data) {
                    $.extend(true, self.smsData, json.data);

                    num = json.data.number;
                    if (num > remindNum) {
                        box.find('.sms-note').text('余额不足！');
                    } else {
                        box.find('.sms-note').text('');
                    }
                    box.find('.sms-num').text(num)
                            .end().find('.send-to').text(json.data.mobileNumber);
                } else {
                    dialog.fail(json.message);
                }
            });
        }

        // 全部发送
        function getAllSendSmsNum(template) {
            var t;
            var data;

            if (this.allData && typeof this.allData == 'function') {
                t = this.allData();
            } else {
                t = this.allData;
            }

            data = $.extend(true, {
                template: template
            }, t || {});

            delete data.page;
            delete data.size;

            getSmsNum.call(this, this.allUrl, data);
        }

        function getTemplate(type, fn) {
            if(typeof type === 'function') {
                type = type();

                $.get(BASE_PATH + '/marketing/ng/sms/template/get', {
                    type: type || 10
                }, function (result) {
                    if (!result.success || !result.data) {
                        dialog.fail(result.message || '获取默认模板失败');
                    }
                    fn(result.data);
                });
            } else if(type == null) {
                fn(result.data);
            }

        }

        //点击发送
        function sendMessage() {
            var self = this;
            var smsContent = $('.sms-dialog .sms-content').val();
            var data = {
                key: self.smsData.smsUID
            };
            var _data = typeof this.data == 'function' ? this.data() : this.data;
            var setting = {
                url: this.url,
                data: data,
                type: this.type || 'get',
                beforeSend: function () {
                    $('.send').prop('disabled', true);
                },
                success: function (result) {
                    closeLoading();

                    if (result.success) {
                        dialog.success('短信发送成功', self.callback);

                        dialog.close(self.dialogId);
                    } else {
                        dialog.fail(result.message)
                    }
                },
                complete: function () {
                    closeLoading();
                    $('.send').prop('disabled', false);
                }
            };

            if ($.trim(smsContent) == '') {
                dialog.warn('短信内容不能为空！');
                return false;
            }

            if (!self.smsData || self.smsData.smsUID == undefined) {
                dialog.warn('获取数据失败, smsUID 不能为空');
                return;
            }

            if(_data === false) {
                return;
            }

            $.extend(data, _data);

            if(this.type == 'post' && this.paramType == 'json') {
                setting.data = JSON.stringify(setting.data);
                setting.contentType = 'application/json';
            }
            openLoading();
            $.ajax(setting);
        }

        function openLoading() {
            if (_loading) {
                dialog.close(_loading);
                _loading = null;
            }
            _loading = dialog.load();
        }

        function closeLoading() {
            if (_loading) {
                dialog.close(_loading);
                _loading = null;
            }
        }

        window.MarketingSMS = MarketingSMS;
        /*-------------- 营销：短信 end ------------------*/
    });
</script>