<#-- 选择短信弹框 start -->
<script id="Z-messageTpl" type="text/html">
    <% if(result) {%>
    <div class="messageTpl">
        <div class="messageTpl-title">短信内容</div>
        <div class="messageTpl-body">
            <div class="message-amount">
                <span class="message-amount-balance">
                    剩余短信额度&nbsp;<em class="message-num">0</em>条&nbsp;<em
                        class="message-balance-notice"></em>
                </span>
                <a class="message-amount-charge"
                   href="${BASE_PATH}/marketing/ng/center/sms">立即充值</a>
            </div>
            <div class="message-content">
                <textarea id="smsContent" class="sms-content"><%=sms||''%></textarea>
                <div class="message-action">
                    <span class="sendTo">
                        <em class="label">本次发送对象：</em><strong><%= len || 0 %></strong>
                    </span>
                    <span class="smsNum">
                        <em class="label">预估发送短信：</em><strong><%= len|| 0 %></strong>条
                    </span>
                    <span class="cancel">取消</span>
                    <span class="send">发送</span>
                </div>
                <ul class="messageTpl-list" id="messageTpl">
                    <li class="messageTpl-list-title">选择其他模版</li>
                    <% var len = min(3, result.length); %>
                    <% for(var i=0;i < len;i++) { %>
                    <li class="messageTpl-detail" data-id="<%=result[i].id%>" title="<%= result[i].content %>">
                        <%= result[i].content %>
                    </li>
                    <% } %>
                    <% if(result.length > 3) { %>
                    <li class="more">更多</li>
                    <% } %>
                </ul>
            </div>
        </div>
    </div>
    <%}%>
</script>
<script id="listTpl" type="text/html">
    <li class="messageTpl-list-title">选择其他模版</li>
    <% for(var i=0;i < result.length;i++) { %>
    <li class="messageTpl-detail" data-id="<%=result[i].id%>">
        <%= result[i].content %>
    </li>
    <% } %>
</script>

<#-- 选择短信 end -->
<script>
$(function () {
    var baseUrl = BASE_PATH + '/shop/marketing/sms/new';
    /*-------------- 营销：短信 start ------------------*/
    (function (window) {
        var $doc = $(document);
        var _loading;

        function MarketingSMS(selectedCustomer, position, allSendData, change, defaultSMSType) {
            // 选择的用户
            this.selectedCustomer = selectedCustomer || {};
            // 打开弹窗获取到的数据
            this.smsTplData = null;
            // 弹窗 id
            this.dialogId = undefined;
            // 选择的短信模板 string
            this.template = undefined;
            // 位置 enum
            this.position = position;
            // 保存的短信信息：如条数等等,
            // smsUID 为最终发送时需要的数据
            this.smsData = {};
            // 全部发送需要的数据
            /* 形如: {
                // 基础 url
                url: @param string,
                // 部分 url data
                partUrl: @param object
                // 对应的参数
                data: @param object,
                // data 中对应 url 的 key
                key: @param string
            }
                url = url + partUrl[key]
            */
            this.allSendData = allSendData;
            // 内容是否根据 type 变化
            this.change = change;
            this.defaultSMSType = defaultSMSType;

            // 是发送全部么
            this.isAllSend = false;
            // 初始化, dom event
            this.init();

            return this;
        }

        MarketingSMS.prototype = {
            showDialog: function (type) {
                var url = BASE_PATH + "/marketing/ng/sms/template/list";//这里是短信列表接口地址
                var sendTo = getCustomerNum(this.selectedCustomer);
                var self = this;

                if (sendTo == 0 && type !== 'all') {
                    layer.msg("请选择有手机号的用户！", 2, 3);
                    return false;
                }

                self.isAllSend = type == 'all' ? true : false;

                openLoading();
                getTemplate(self.defaultSMSType, function (sms) {
                    $.get(url, function (result) {
                        closeLoading();
                        if (self.dialogId != undefined || !result.success) {
                            return;
                        }

                        self.smsTplData = result;

                        if(sms) {
                            preProcessTemplate.call(self, sms.content, $('#smsContent'));
                        }

                        self.dialogId = $.layer({
                            type: 1,
                            title: false,
                            area: ['auto', 'auto'],
                            offset: ['20px', ''],
                            border: [0], //去掉默认边框
                            shade: [0.5, '#000'],
                            shadeClose: true,
                            bgcolor: '#fff',
                            closeBtn: [1, true], //去掉默认关闭按钮
                            shift: 'top',
                            end: function () {
                                self.dialogId = undefined;
                            },
                            page: {
                                html: Dao.html("Z-messageTpl", {
                                    'result': result.data,
                                    len: sendTo,
                                    sms: sms && sms.content
                                })
                            }
                        });
                        messageNum.call(self);
                    });
                });
            },
            closeDialog: function () {
                layer.close(this.dialogId);
            },
            getCarId: function () {
                var data = this.selectedCustomer;
                var ret = [];

                for (var key in data) {
                    ret.push(data[key].customerCarId);
                }

                return ret.join(',');
            },
            init: function () {
                var self = this;

                //点击发短信按钮出现弹窗
                $doc.on("click", ".js-message", function () {
                    self.showDialog();
                })
                        .on('click', '.js-message-all', function () {
                            self.showDialog('all');
                        })
                        .on("click", ".return", function () {
                            self.closeDialog();
                        });

                //点击短信模版效果
                $doc.on("click", ".messageTpl-detail", function () {
                    var text = $.trim($(this).text());

                    preProcessTemplate.call(self, text, $('#smsContent'));
                });


                $doc.on("blur", "#smsContent", function () {
                    var template = $.trim( $(this).val() );

                    if (template) {
                        preProcessTemplate.call(self, template, $('#smsContent'));
                    }
                });
                //点击充值
                $doc.on("click", ".message-amount-charge", function () {
                    window.location.href = BASE_PATH + "/marketing/ng/center/sms";
                });

                $doc.on("click", ".send", function () {
                    var remindNum = $(".message-num").data("messageNum");

                    if (self.smsData.number > remindNum) {
                        layer.msg("短信余额不足，请充值！", 2, 3);
                        return false;
                    }
                    sendMessage.call(self);
                });
                //点击取消
                $doc.on("click", ".cancel", function () {
                    self.closeDialog();
                });
                // 更多
                $doc.on("click", ".more", function () {
                    Dao.render({
                        tag: "#messageTpl",
                        tpl: "listTpl",
                        fill: "~"
                    }, {result: self.smsTplData && self.smsTplData.data});
                });
            }
        };

        function getTemplate(type, fn) {
            if(type != null) {
                if(typeof type == 'function') {
                    type = type();
                }
                $.get(BASE_PATH + '/marketing/ng/sms/template/get', {
                    type: type || 10
                }, function (result) {
                    if (!result.success || !result.data) {
                        layer.msg(result.message || '获取默认模板失败', 2, 3);
                    }
                    fn(result.data);
                });
            } else {
                fn();
            }
        }

        function getCustomerNum(selectedCustomer) {
            var num = 0;

            for (var i in selectedCustomer) {
                if (selectedCustomer[i] && selectedCustomer[i].mobile) {
                    num++;
                }
            }

            return num;
        }

        //剩余短信数量
        function messageNum() {
            openLoading();

            $.getJSON(BASE_PATH + "/marketing/ng/sms/template/remain", function (json) {
                closeLoading();
                if (json.success && json.data) {
                    $('.message-num').text(json.data)
                            .attr("data-message-num", json.data);

                    if (self.smsData && self.smsData.number > json.data) {
                        $(".message-balance-notice").text("余额不足！");
                    } else {
                        $(".message-balance-notice").text('');
                    }
                }
            });
        }

        function preProcessTemplate(template, $dom) {
            var self = this;

            openLoading();
            $.get(baseUrl + '/template_process', {
                template: template
            }, function (json) {
                if (json.success && json.data) {
                    self.template = json.data;
                    $dom.val(json.data);

                    if (json.data) {
                        if (!self.isAllSend) {
                            getSendSmsNum.call(self, json.data);
                        } else {
                            getAllSendSmsNum.call(self, json.data);
                        }
                    }
                } else {
                    closeLoading();
                    taoqi.failure(json.message || json.errorMsg || '获取预处理模板失败');
                }
            });
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

            openLoading();
            $.get(url, data, function (json) {
                closeLoading();
                if (json.success && json.data) {
                    $.extend(true, self.smsData, json.data);

                    num = json.data.number;
                    if (num > remindNum) {
                        $('.message-balance-notice').text('余额不足！');
                    } else {
                        $('.message-balance-notice').text('');
                    }
                    $('.smsNum strong').text(num);
                    $('.sendTo strong').text(json.data.mobileNumber);
                } else {
                    layer.msg(json.message, 2, 3);
                }
            });
        }

        // 全部发送
        function getAllSendSmsNum(template) {
            var _data = this.allSendData;
            var url = _data.url;
            var t;
            var data;

            if(_data.data && typeof _data.data == 'function') {
                t = _data.data();
            } else {
                t = _data.data;
            }

            data = $.extend(true, {
                template: template
            }, t || {});

            delete data.page;
            delete data.size;

            if(this.change && this.change.type !== undefined) {
                data.noteType = this.change.type;
            }

            if(_data.partUrl && _data.key) {
                 url += '/' + _data.partUrl[_data.key];
            }

            getSmsNum.call(this, url, data);
        }

        //点击发送
        function sendMessage() {
            var self = this;
            var smsContent = $("#smsContent").val();
            var url = baseUrl;
            var data = {
                key: self.smsData.smsUID
            };

            if ($.trim(smsContent) == "") {
                layer.msg("短信内容不能为空！", 2, 3);
                return false;
            }

            if (!self.smsData || self.smsData.smsUID == undefined) {
                layer.msg('获取数据失败, smsUID 不能为空', 2, 3);
                return;
            }

            if (this.position == 1 || this.position == 2 || this.position == 3) { // 客户分析
                url += '/send';
                var refer = $(".user-tag.on").data("refer");
                if (this.isAllSend) {
                    refer += '_all';
                }
                data.refer = refer;
            } else if (this.position == 4) { // 提醒中心
                url += '/send_with_note';
                data.noteType = this.change.type;

                var refer = '';
                if (data.noteType == 0) { // 预约单
                    refer = 'appoint';
                } else if (data.noteType == 1 || data.noteType == 8) { // 保养
                    refer = 'maintain';
                } else if (data.noteType == 2) { // 保险
                    refer = 'insurance';
                } else if (data.noteType == 3) { // 年检
                    refer = 'auditing';
                } else if (data.noteType == 4 || data.noteType == 7) { // 回访
                    refer = 'visit';
                } else if (data.noteType == 5) { // 生日
                    refer = 'birthday';
                } else if (data.noteType == 6) { // 流失
                    refer = 'lost';
                }
                if (this.isAllSend) {
                    refer += '_all';
                }
                data.refer = refer;
            } else if (this.position == 5) { // 精准营销
                url += '/send';
                if (this.isAllSend) {
                    data.refer = 'accurate_all';
                } else {
                    data.refer = 'accurate';
                }
            } else {
                url += '/send';
            }
            openLoading();
            $.ajax({
                url: url,
                data: data,
                beforeSend: function () {
                    $('.send').prop('disabled', true);
                },
                success: function (result) {
                    closeLoading();

                    if (result.success) {
                        layer.msg("短信发送成功", 2, 1);
                        layer.close(self.dialogId);
                    } else {
                        layer.msg(result.message, 2, 3)
                    }
                },
                complete: function () {
                    closeLoading();
                    $('.send').prop('disabled', false);
                }
            });
        }

        function openLoading() {
            if(!_loading) {
                _loading = taoqi.loading('', 6000);
            }
        }

        function closeLoading() {
            if(_loading) {
                taoqi.close(_loading);
                _loading = null;
            }
        }

        window.MarketingSMS = MarketingSMS;
    }(window));
    /*-------------- 营销：短信 end ------------------*/
});
</script>