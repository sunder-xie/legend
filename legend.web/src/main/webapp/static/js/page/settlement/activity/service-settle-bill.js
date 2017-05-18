/**
 * Created by zsy on 16-03-02.
 */
$(function () {
    $.fn.selectGroup = function (options) {
        // 设置参数
        var setting = $.extend({}, $.fn.selectGroup.default, options);

        // 循环每一个selectGroup
        return $(this).each(function () {
            var $scope = $(this);
            $scope
                .on("change", setting.allSelectClass, function () {     // 设置全选状态
                    var $this = $(this),
                        checked = $this.prop("checked");
                    $(setting.itemSelectClass, $scope).prop("checked", checked);
                })
                .on("change", setting.itemSelectClass, function () {    // 设置单选状态
                    var $this = $(this),
                        checked = $this.prop("checked");
                    // 判断单选是否为选中状态
                    if (checked) {
                        // 修改全选框状态，遍历所有的单选，检查是否有未选中的单选
                        $(setting.allSelectClass, $scope).prop("checked", true);
                        $(setting.itemSelectClass, $scope).each(function (idx, elem) {
                            var echecked = $(elem).prop("checked");
                            if (!echecked) {
                                $(setting.allSelectClass, $scope).prop("checked", false);
                                return false;
                            }
                        })
                    } else {
                        $(setting.allSelectClass, $scope).prop("checked", false);
                    }
                })
        })
    }

    // 设置插件默认值
    $.fn.selectGroup.default = {
        allSelectClass: ".selectAll",
        itemSelectClass: ".selectItem"
    }

    //分页
    seajs.use([
        'ajax',
        'table',
        'dialog',
        'date',
        'select'
    ], function (ajax, tb, dg, date, st) {
        //初始化时间
        getTime(tb, dg);

        //搜索按钮
        $(".js-search").click(function () {
            searchForm(tb);
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

        //初始化状态
        st.init({
            dom: '#search_shopConfirmStatus',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '',
                value: '请选择'
            }, {
                key: '0',
                value: '待确定'
            }, {
                key: '1',
                value: '已确定'
            }]
        });

        $(".groupSelect").selectGroup();

        $(document)
            .on("click", ".link_ok", function () {
                var billId = $(this).data("id");
                var ids = [];
                ids.push(billId);
                dg.confirm("确认对账吗?", function () {
                    checkSettle(ids, dg, tb);
                });
            }).on("click", "#batchOk", function () {
                var ids = [];
                $(".selectItem").each(function () {
                    var $this = $(this),
                        billId = $this.val(),
                        checked = $this.prop("checked");
                    checked && ids.push(billId);
                });
                if (ids.length == 0) {
                    dg.warn("请选择需要确认的账单");
                    return;
                }
                dg.confirm("确认对账吗?", function () {
                    checkSettle(ids, dg, tb);
                })
            }).on("click", ".bill_export", function () {
                var search_auditPassStartTime = $.trim($(".auditPassStartTime").val());
                var search_auditPassEndTime = $.trim($(".auditPassEndTime").val());
                var search_shopActId = $.trim($("#search_shopActId").val());
                var search_shopConfirmStatus = $.trim($("#search_shopConfirmStatus").val());
                var search_keywords = $.trim($("#search_keywords").val());
                var actTplId = $.trim($("#actTplId").val());
                if (actTplId == '') {
                    actTplId = 1;
                }
                window.location.href = BASE_PATH + "/shop/settlement/activity/settle_list?excel=" + actTplId
                + "&search_shopActId=" + search_shopActId
                + "&search_shopConfirmStatus=" + search_shopConfirmStatus
                + "&search_keywords=" + search_keywords
                + "&search_auditPassStartTime=" + search_auditPassStartTime
                + "&search_auditPassEndTime=" + search_auditPassEndTime;
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

    });
});

function checkSettle(ids, dg, tb) {
    $.ajax({
        type: "POST",
        dataType: "json",
        url: BASE_PATH + '/shop/settlement/activity/check_settle',
        data: {ids: ids.join(',')},
        success: function (result) {
            if (result.success) {
                searchForm(tb);
            } else if (result.code == 20022003) {
                bankBindWithLayer();
                $("#mobile").val(result.errorMsg);
            } else {
                dg.fail(result.errorMsg)
            }
        }
    });

}


function getTime(tb, dg) {
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
                    searchForm(tb);
                }
            },
            error: function (a, b, c) {
//            console.log(a, b, c);
            }
        })
    } else {
        searchForm(tb);
    }
}
function searchForm(tb) {
    tb.init({
        //表格数据url，必需
        url: BASE_PATH + "/shop/settlement/activity/settle_list",
        //表格数据目标填充id，必需
        fillid: 'content',
        //分页容器id，必需
        pageid: 'contentPage',
        //表格模板id，必需
        tplid: 'contentTemplate',
        //扩展参数,可选
        data: null,
        //关联查询表单id，可选
        formid: 'searchForm',
        //渲染表格数据完后的回调方法,可选
        callback: null
    });

}