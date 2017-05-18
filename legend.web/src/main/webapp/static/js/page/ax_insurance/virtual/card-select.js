/**
 * Created by sky on 16/9/21.
 */

$(function () {
    // 判断是否是添加新卡
    var isNewCard = false,
        // 判断银行卡号与银行是否匹配
        isMatched = true,
        // 卡号类型|默认是储蓄卡(1)
        newPayMethod = 1,
        newCardType = 0,
        matchError;
    var Model = {
        // 获取支持的银行卡列表
        getSupportedCardList: function () {
            var url = BASE_PATH + '/insurance/anxin/virtual/flow/support-card-list';
            return $.get(url);
        },
        // 获取支持的信用卡列表
        getSupportedCreditList: function () {
            var url = BASE_PATH + '/insurance/anxin/virtual/flow/support-credit-card-list';
            return $.get(url);
        },
        // 获取过去已支付过的银行卡列表
        getPayedCardList: function () {
            var url = BASE_PATH + '/insurance/anxin/virtual/flow/card-list';
            return $.get(url);
        },
        // 匹配银行卡信息是否一致
        matchedCardInfo: function (params) {
            var url = BASE_PATH + '/insurance/anxin/virtual/flow/check-card';
            return $.ajax({
                url: url,
                data: params,
                global: false
            });
        },
        // 确定支付
        toPay: function (params) {
            var url = BASE_PATH + '/insurance/anxin/virtual/flow/unionpay';
            return $.ajax({
                type: 'POST',
                url: url,
                contentType: 'application/json',
                data: JSON.stringify(params)
            });
        }
    };

    /**
     * 获得可识别的卡号
     * @param cardNo
     * @returns {string}
     */
    function getCard(cardNo) {
        return typeof cardNo == "string" ? cardNo.replace(/[^\d]/g, '') : '';
    }

    /**
     * 获得游标位置
     * @param len
     * @param newLen
     * @returns {*}
     */
    function getCursorPosition(len, newLen) {
        var previousValueLength = this.previousValueLength || 0,
            pos;
        newLen = newLen || len;
        if (previousValueLength > len) {
            // 删除内容
            pos = this.selectionStart;
        } else {
            // 增加内容
            pos = newLen;
        }
        this.previousValueLength = newLen;
        return pos;
    }

    (function initialize() {
        seajs.use(['dialog', 'ajax'], function (dg) {
            dg.titleInit();

            // 获得已经支付过的银行卡信息
            Model.getPayedCardList()
                .done(function (result) {
                    seajs.use('art', function (tpl) {
                        var html = tpl('payedCardListTpl', result);
                        var index = 0; // 选择储蓄卡
                        $('#payContainer').html(html);
                        $('.js-pay-btn').removeAttr('disabled');
                        $('.js-card-box').hide().eq(index).show();
                        $('.js-card-type-tabs li').eq(index).addClass('active');
                    });
                    if (!result.success) {
                        seajs.use('dialog', function (dg) {
                            dg.fail(result.errorMsg);
                        })
                    }
                });
        })
    })();


    $(document)
        // 银行卡类型选择
        .on('click', '.js-card-type-tabs li', function () {
            var $this = $(this),
                _index = $this.index();
            $('.js-card-box').hide().eq(_index).show()
                .find('.active').removeClass('active');
            $this.siblings('li').removeClass('active')
                .end().addClass('active');
        })
        // 银行卡选择
        .on('click', '.js-bank-item', function () {
            var $this = $(this);
            $this.siblings('.active').removeClass('active')
                .end().addClass('active');
        })
        // 添加新银行卡
        .on('click', '.js-add-card-btn', function () {
            var SupportedObj;
            newCardType = $(this).data('cardType');
            SupportedObj = newCardType == 0 ? Model.getSupportedCardList() : Model.getSupportedCreditList();

            SupportedObj.done(function (result) {
                if (result.success) {
                    seajs.use(['art', 'check'], function (tpl, ck) {
                        var html = tpl('supportedCardListTpl', {cardType : newCardType, data: result.data});
                        $('#payContainer').html(html);
                        isNewCard = true;
                        newPayMethod = newCardType == 0 ? 1 : 2;
                        ck.init();
                    })
                } else {
                    seajs.use('dialog', function (dg) {
                        dg.fail(result.errorMsg);
                    })
                }
            });
        })
        .on('click', '.js-show-more', function () {
            var $this = $(this),
                type = $this.data('type');

            if (type == 0) {
                $('.js-add-bank-list').removeClass('limit-6-rows');
                $this.text('收起').data('type', 1);
            } else {
                $('.js-add-bank-list').addClass('limit-6-rows');
                $this.text('更多').data('type', 0);
            }
        })
        // 银行卡输入体验优化
        .on('input', '.js-card-no-input', function () {
            var $el = $(this),
                value = $el.val(),
                len = value.length,
                pos;
            if ( !(/^\d+$/g.test(value)) ) {
                 value = value.replace(/[^\d]+/g, '');
            }
            if (len > 4) {
                value = $.trim(value.replace(/\d{4}/g, '$& '));
            }
            pos = getCursorPosition.call(this, len, value.length);
            $el.val( value );
            this.setSelectionRange(pos, pos);
        })
        // 银行卡号与银行校验
        .on('blur', '.js-card-no-input', function () {
            var data = {
                cardNo: getCard($(this).val())
            };
            var $selectedCard = $('.js-bank-item.active');

            if (data.cardNo === "") {
                return false;
            }

            if (!$selectedCard.length) {
                seajs.use('dialog', function (dg) {
                    dg.warn('请选择一张银行卡！');
                });
                return false;
            }

            data['bankCode'] = $selectedCard.data('bankCode');
            data['cardType'] = newCardType;

            Model.matchedCardInfo(data)
                .done(function (result) {
                    if (result.success) {
                        isMatched = true;
                    } else {
                        isMatched = false;
                        matchError = result.errorMsg;
                        seajs.use('dialog', function (dg) {
                            dg.fail(matchError);
                        })
                    }
                })
        })
        // 确认支付
        .on('click', '.js-pay-btn', function () {
            var data = {
                orderSn: $('[name="orderSn"]').text(),
                totalFee: $('[name="totalFee"]').text()
            };
            var $selectedCard = $('.js-bank-item.active');
            var errMsg;
            var checked;

            if (!$selectedCard.length) {
                seajs.use('dialog', function (dg) {
                    dg.warn('请选择一张银行卡！');
                });
                return;
            }

            if (isNewCard) {    // 新建的卡号信息
                data['cardNo'] = getCard($('[name="cardNo"]').val());
                data['acctName'] = $('[name="acctName"]').val();
                data['idNo'] = $('[name="idNo"]').val();
                data['payMethod'] = newPayMethod;
                data['isEncrypt'] = false;

                if (data['cardNo'] === "") {
                    errMsg = '银行卡号不能为空！'
                } else if (data['acctName'] === "") {
                    errMsg = '姓名不能为空！';
                } else if (data['idNo'] === "") {
                    errMsg = '身份证号不能为空！';
                }

                if (errMsg) {
                    seajs.use('dialog', function (dg) {
                        dg.warn(errMsg);
                    });
                    return;
                }

                if (!isMatched) {
                    seajs.use('dialog', function (dg) {
                        dg.fail(matchError);
                    });
                    return;
                }

                seajs.use('check', function (ck) {
                    // 验证身份证号码是否正确
                    checked = ck.check();
                });

                if (!checked) {
                    return;
                }
            } else {    // 已支付过的卡号信息
                data['noAgree'] = $selectedCard.data('noAgree');
                data['idNo'] = $selectedCard.data('idNo');
                data['acctName'] = $selectedCard.data('acctName');
                data['cardNo'] = $selectedCard.data('cardNo');
                data['payMethod'] = $selectedCard.data('payMethod');
                data['isEncrypt'] = true;
            }
            Model.toPay(data)
                .done(function (result) {
                    if (!result.success) {
                        seajs.use('dialog', function (dg) {
                            dg.fail(result.errorMsg);
                        })
                    }else{
                            $('#newMain').html(result.data);

                    }
                });
        });
});
