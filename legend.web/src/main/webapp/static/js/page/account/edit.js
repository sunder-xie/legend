/**
 * Created by ende on 16/7/8.
 */
$(function () {
    seajs.use(['dialog', 'formData', 'ajax', 'table', 'check', 'date'], function (dialog, formData, ajax, table, check, dt) {
        var clickSave = true,
            cardNum,
            repeatCardMsg,
            urls = {
                save: BASE_PATH + '/account/edit',
                cardNumber: BASE_PATH + "/account/member/number/exists",
                currentNumber: BASE_PATH + "/account/member/get-current-cardnumber"
            };

        dt.datePicker('.js-date-2');

        /** jquery .on start **/
        $('.js-save').on('click', function () {
            var data = formData.get('.cutomer', true),
                membercard = [];

            if (repeatCardMsg) {
                dialog.warn(repeatCardMsg);
                return;
            }

            if (!check.check()) {
                return;
            }

            $('div').each(function () {
                if ($(this).is('.card-box')) {
                    var cardid = $("input[name='id']", this).val();
                    var cardNumber = $("input[name='cardNumber']", this).val();
                    membercard.push({id: cardid, cardNumber: cardNumber});
                }
            });

            var ops = {customer: data, memberCards: membercard};

            $.ajax({
                url: urls.save,
                dataType: 'json',
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify(ops),
                success: function (json) {
                    if (json && json.success) {
                        dialog.success('保存成功', function () {
                            location.href = BASE_PATH + '/account/detail?customerId=' + data.id;
                        });
                    } else {
                        dialog.fail(json.message || '保存失败');
                        clickSave = true;
                    }
                }
            })
        });

        $('.js-back').on('click', function () {
            util.goBack(-1);
        });

        /*显示最近的会员卡号*/
        $('.js-card-number')
            .on('click', function () {
                var $this = $(this);

                // 选中文本内容
                $this.select();

                if (cardNum) {

                    $this.siblings('.js-card-tips').show();
                } else {

                    $.ajax({
                        url: urls.currentNumber,
                        success: function (result) {
                            if (result.success) {
                                cardNum = result.data;
                                $('.js-recent-card-num').text(cardNum);
                                $this.siblings('.js-card-tips').show();
                            }
                        }
                    });
                }
            })
            .on('blur', function () {
                var $this = $(this),
                    cardNumber = $this.val(),
                    oldnum = $this.parent("div").data("oldnumber");
                $('.js-recent-card-num').parent().hide();
                if (cardNumber && cardNumber != oldnum) {
                    $.ajax({
                        url: urls.cardNumber,
                        global: false,
                        data: {
                            cardNumber: cardNumber
                        },
                        success: function (result) {
                            if (!result.success) {
                                repeatCardMsg = result.message;
                                check.showCustomMsg(repeatCardMsg, $this);
                                $this.addClass('input-warning');
                            } else {
                                repeatCardMsg = null;
                                $this.removeClass('input-warning');
                            }
                        }
                    });
                }
            });
        /** jquery .on end   **/

    });
});
