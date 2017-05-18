/**
 * Created by sky on 16/8/12.
 */

$(function () {
    seajs.use(['table', 'dialog', 'ajax'], function (tb, dg) {
        var _template,
            _smsData,
            _selectedNum = 0;
        // 列表选中
        var selectedCustomer = {};

        // 群发列表信息
        tb.init({
            url: BASE_PATH + '/shop/marketing/sms/new/wechat_customer_list',
            fillid: 'customerInfo',
            pageid: 'pageBox',
            tplid: 'customerListTpl',
            callback: function (a, json) {
                var t, all = true;
                if(json.success && json.data.content) {
                    for(var i in json.data.content) {
                        t = json.data.content[i];

                        if(selectedCustomer[ t.id ] === true) {
                            $('.js-select-item[data-id="' + t.id + '"]')
                                .prop('checked', true);
                        } else {
                            all = false;
                        }
                    }

                    if(i === undefined) {
                        all = false;
                    }

                    $('.js-select-all').prop('checked', all);
                }
            }
        });

        dg.titleInit();

        /**** ?ajax请求接口 ****/
        var URLs = {
            needNum: '/shop/marketing/sms/new/calculate_number',
            needAll: '/shop/marketing/sms/new/calculate_number/wechat'
        }, pathOption, carIdList = [], dialogId;

        var Model = {

            getSMS: function (smsTemplate) {
                var data = {
                    template: smsTemplate
                };

                if (pathOption == 'needNum') {
                    // 群发短信部分
                    data.carIds = carIdList.join(',');
                    data.position = 6;
                }

                return $.ajax({
                    url: BASE_PATH + URLs[pathOption],
                    data: data
                });
            },
            sendSMS: function (smsUID) {
                return $.ajax({
                    url: BASE_PATH + '/shop/marketing/sms/new/send',
                    data: {
                        key: smsUID
                    }
                });
            },
            getSMSNum: function () {
                return $.ajax(BASE_PATH + '/shop/marketing/sms/new/get_has_customer_num');
            }
        };

        /**** ?发送短信弹窗 ****/
        function smsDialog(sendPeople) {
            // 获取当前剩余短信条数
            var rest = $('#restSMSNum').text();
            // 打开弹窗
            dialogId = dg.open({
                content: $('#smsDialogTpl').html(),
                end: function () {
                    _smsData = null;
                    _template = null;
                }
            });

            if(!_template) {
                preProcessTemplate();
            }
            $('#smsTemplate').val(_template);
            // 更新弹窗数据
            $('#dgRestSMSNum').text(rest);
            $('#sendPeople').text(sendPeople);
        }

        /**** ?事件绑定 ****/
        $(document)
            // 群发短信（处理群发用户）
            .on('click', '.js-send-part', function () {
                var hasMobile,
                    cNum;
                var t = getObjectKeyLength(selectedCustomer);

                // 设置请求的接口
                pathOption = 'needNum';
                carIdList = t.carIds;

                cNum = t.count;
                hasMobile = t.hasMobile;

                // 判断是否有用户有手机号码
                if (!hasMobile) {
                    dg.warn(cNum ? '您选择的车辆没有手机号码，无法发送短信！' : '请选择用户！');
                    return false;
                }
                smsDialog(carIdList.length);
            })
            // 全部群发短信
            .on('click', '.js-send-all', function () {
                // 设置请求的接口
                pathOption = 'needAll';
                Model.getSMSNum()
                    .done(function (result) {
                        if (result.success) {
                            smsDialog(result.data);
                        } else {
                            dg.fail(result.message);
                        }
                    });
            })
            // 发送短信
            .on('click', '.js-send-btn', function () {
                var smsTemplate = $('#smsTemplate').val();
                var result = _smsData;
                var data = _smsData.data, rest;

                if (result.success) {
                    rest = $('#restSMSNum').text() || 0;
                    if (data.number > rest) {
                        dg.confirm("您的短信余额不足，请充值", function () {
                            location.href = BASE_PATH + '/marketing/ng/center/sms';
                        }, ['同意', '取消']);
                    } else {
                        Model.sendSMS(data.smsUID)
                            // 调用发送短信功能成功
                            .done(function (result) {
                                if (!result) return;
                                if (result.success) {
                                    $('#restSMSNum').text(rest - result.data);
                                    dg.success('已发送成功' + result.data + '条短信！');
                                    dg.close(dialogId);
                                } else {
                                    dg.fail(result.message);
                                }
                            });
                    }
                } else {
                    dg.fail(result.message);
                }
            })
            .on('blur', '#smsTemplate', function () {
                var template = $.trim($(this).val());
                if (template) {
                    preProcessTemplate(template);
                }
            })
            // 关闭弹窗
            .on('click', '.js-cancel-btn', function () {
                dg.close(dialogId);
            });

        // 单选全选组件
        $('.js-select-group')
            .on('change', '.js-select-all', function () {
                var $el = $(this),
                    $gp = $el.parents('.js-select-group'),
                    checked = $el.prop('checked');
                $gp.find('.js-select-item').each(function () {
                    if(this.checked != checked) {
                        $(this).click();
                    }
                });
            })
            .on('change', '.js-select-item', function () {
                var $el = $(this),
                    $gp = $el.parents('.js-select-group'),
                    checked = $el.prop('checked'),
                    id  = $(this).data('id'),
                    allChecked = true;

                if (checked) {
                    selectedCustomer[id] = $(this).data();
                    $gp.find('.js-select-item').not(this).each(function () {
                        var _checked = $(this).prop('checked');
                        if (!_checked) {
                            return (allChecked = false);
                        }
                    });
                    _selectedNum += 1;
                } else {
                    delete selectedCustomer[id];
                    allChecked = false;

                    _selectedNum -= 1;
                }
                $('#selectedNum').text(_selectedNum);
                $gp.find('.js-select-all').prop('checked', allChecked);
            });

        function preProcessTemplate (temp) {
            var $dom = $('#smsTemplate');
            var template = temp
                || '亲爱的车主${客户姓名}，${门店名称}送了您一张优惠券，点击领取：${领劵链接}，关注【${门店微信公众号名称}】公众号领取';

            $.get(BASE_PATH + '/shop/marketing/sms/new/template_process', {
                template: template
            }, function (json) {
                if (json.success && json.data) {
                    _template = json.data;

                    $dom.val(json.data);
                    Model.getSMS(json.data)
                        .done(function (result) {
                            var rest;
                            if (result.success) {
                                // 本次发送对象
                                $('#sendPeople').text(result.data.mobileNumber);
                                // 预估发送短信数目
                                $('#sendNum').text(result.data.number);

                                rest = $('#restSMSNum').text() || 0;
                                if (result.data.number > rest) {
                                    dg.confirm("您的短信余额不足，请充值", function () {
                                        location.href = BASE_PATH + '/marketing/ng/center/sms';
                                    }, ['同意', '取消']);
                                }
                            } else {
                                dg.fail(result.message);
                            }

                            _smsData = result;
                        });
                } else {
                    _template = template;
                    $dom.val(template);

                    dg.fail(json.message || '获取模板失败');
                }
            });
        }

        function getObjectKeyLength(obj) {
            var count = 0,
                hasMobile = false,
                arr = [];

            for(var key in obj) {
                if(obj.hasOwnProperty(key)) {
                    count++;
                    arr.push(key);

                    if(obj[key].mobile) {
                        hasMobile = true;
                    }
                }
            }

            return {
                count: count,
                hasMobile: hasMobile,
                carIds: arr
            };
        }
    });
});


