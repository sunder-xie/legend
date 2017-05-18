/**
 * Created by zsy on 16-06-02.
 */
$(document).ready(function ($) {

    seajs.use([
        'ajax',
        'dialog',
        'art',
        'date',
        'select'
    ], function (ajax, dg, art, date, st) {
        //初始化时间
        getTime(dg, art);

        //搜索按钮
        $(".js-search").click(function () {
            searchForm(art);
        });

        //查看详情
        $(document).on("click", ".go_detail", function () {
            var actTplId = $.trim($(this).attr("act-tpl-id"));
            var auditPassStartTime = $.trim($(".auditPassStartTime").val());
            var auditPassEndTime = $.trim($(".auditPassEndTime").val());
            var url = BASE_PATH + "/shop/settlement/activity/detail?actTplId=" + actTplId
                + "&auditPassStartTime=" + auditPassStartTime
                + "&auditPassEndTime=" + auditPassEndTime;
            window.location.href = url;
        });

        $(document).on("click", ".link_ok", function () {
            var actTplId = $(this).data("id");
            dg.confirm("确认对账吗？", function () {
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    data: {
                        actId: actTplId
                    },
                    url: BASE_PATH + "/shop/settlement/activity/act_check_settle",
                    success: function (result) {
                        if (result.success) {
                            searchForm(art);
                            dg.success("确认对账成功");
                        } else if (result.code == 20022003) {
                            //展示绑定银行卡
                            bankBindWithLayer();
                            $("#mobile").val(result.errorMsg);
                        } else {
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            });
        });

        $(document).on("click", ".get_code", function () {
            if ($(this).hasClass('locks')) {
                return;
            }
            $(this).addClass('locks');
            var t = 60;
            var _this = this;
            $(this).val('重获验证码({0})'.format(t));
            var tid = setInterval(function () {
                if (--t) {
                    $(_this).val('重获验证码({0})'.format(t));
                } else {
                    clearInterval(tid);
                    $(_this).val('获取验证码');
                    $(_this).removeClass('locks');
                }
            }, 1000);
            var mobile = $("#mobile").val()
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: BASE_PATH + '/shop/settlement/activity/get_code',
                data: {
                    mobile: mobile
                },
                success: function (data) {
                    if (data.success != true) {
                        dg.fail(data.errorMsg);
                        return;
                    }
                    else {
                        dg.success("亲，验证码获取成功，请接收验证码短信！短信可能被当做垃圾短信拦截，如果未收到短信，请到垃圾短信收件箱中查找！");
                        return false;
                    }
                },
                error: function (a, b, c) {
                }
            });
        });
        //初始化时间插件
        /* 日历 start */
        date.dpStartEnd({
            start: 'd4311',
            end: 'd4312',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'d4312\')||\'%y-%M-%d\'}',
                doubleCalendar: true
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'d4311\')}',
                maxDate: '%y-%M-%d',
                doubleCalendar: true
            }
        });
        /* 日历 end */
        //初始化活动
        st.init({
            dom: ".js-act-type",
            url: BASE_PATH + '/shop/settlement/activity/get-act-list',
            showKey: "id",
            pleaseSelect: true,
            showValue: "actName"
        });
    });
});
function getTime(dg, art) {
    var search_auditPassStartTime = $.trim($(".auditPassStartTime").val());
    var search_auditPassEndTime = $.trim($(".auditPassEndTime").val());
    if (search_auditPassStartTime == '' && search_auditPassEndTime == '') {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: BASE_PATH + '/shop/settlement/activity/get_time',
            success: function (result) {
                if (result.success != true) {
                    dg.fail(result.errorMsg);
                    return false;
                } else {
                    var data = result.data;
                    var auditPassStartTime = data.auditPassStartTime;
                    var auditPassEndTime = data.auditPassEndTime;
                    $(".auditPassStartTime").val(auditPassStartTime);
                    $(".auditPassEndTime").val(auditPassEndTime);
                    searchForm(art)
                    return false;
                }
            },
            error: function (a, b, c) {
            }
        })
    } else {
        searchForm(art)
    }
}

function searchForm(art) {
    //提交数据。
    $.ajax({
        type: 'POST',
        url: BASE_PATH + "/shop/settlement/activity/month_settle_list",
        data: {
            search_auditPassStartTime: $(".auditPassStartTime").val(),
            search_auditPassEndTime: $(".auditPassEndTime").val(),
            search_shopActId: $(".search_shopActId").val()
        },
        success: function (result) {
            var html = art('contentTemplate', {'templateData': result.data});
            $('#content').html(html);
        }
    });
}
