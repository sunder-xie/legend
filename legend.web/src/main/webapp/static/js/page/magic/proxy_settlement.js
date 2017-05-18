/*
 * hyx 2016-05-12
 * 对账单页面
 */

$(function () {
    var $doc = $(document),
        url = BASE_PATH + '/proxy/settlement/commision/bill',
        fillId = "clientInfo",
        formId = "formInfo1",
        tplId = 'tableClientTpl0',
        pageNum = 1,
        paymentType = 0,
        proxyShopId = 0,
        shopId = 0,
        shopFlag = 1,
        flag = 1,
        index = 0;

    //车牌下拉
    seajs.use(['downlist'], function (dl) {
        dl.init({
            url: BASE_PATH + '/api/customer/proxy/settlement/carLicense',
            searchKey: "keyWord",
            tplId: 'carLicenceTpl',
            showKey: 'license',
            dom: 'input[name="carLicense"]',
            hasInput: false,
            notClearInput: true
        });
        //委托方下拉
        dl.init({
            url: BASE_PATH + '/proxy/settlement/share/list',
            searchKey: "shopNameKey",
            tplId:'entrustingTpl',
            showKey:'shopName',
            hiddenKey:'shopId',
            hiddenSelector: 'input[name=proxyShopId]',
            scope: 'order-right',
            dom: '.js-wt-select',
            hasInput: false,
            notClearInput: true
        });

        //受托方下拉
        dl.init({
            url: BASE_PATH + '/proxy/settlement/shop/list',
            searchKey: "shopNameKey",
            tplId: 'principalTpl',
            showKey:'name',
            hiddenKey:'id',
            hiddenSelector:'input[name=proxyShopId]',
            scope:'order-right',
            dom: '.js-st-select',
            hasInput: false,
            notClearInput: true,
        });
    });


    seajs.use('dialog', function(dg) {
        dg.titleInit();
    });
    seajs.use([
        'date',
        'ajax',
        'select',
        'table',
        'ajax',
        'dialog'
    ], function (dp, ax, st, tb, ax, dg) {
        // 交车时间插件
        dp.dpStartEnd({
            start: 'carStartTime',
            end: 'carEndTime'
        });

        dp.dpStartEnd({
            start: 'carStartDate',
            end: 'carEndDate'
        });

        // 委托时间插件
        dp.dpStartEnd({
            start: 'wtStartTime',
            end: 'wtEndTime'
        });

        dp.dpStartEnd({
            start: 'wtStartDate',
            end: 'wtEndDate'
        });

        //委托单导出
        $doc.on('click', '.js-wt-export', function () {
            var proxyStartTime = $(".js-formInfo1").find("input[name='proxyStartTime']").val();
            var proxyEndTime = $(".js-formInfo1").find("input[name='proxyEndTime']").val();
            var completeStartTime = $(".js-formInfo1").find("input[name='completeStartTime']").val();
            var completeEndTime = $(".js-formInfo1").find("input[name='completeEndTime']").val();
            var carLicense = $(".js-formInfo1").find("input[name='carLicense']").val();
            var proxyshopIdST = $(".wt-proxy-shop").val();
            window.location.href = BASE_PATH + '/proxy/settlement/export/proxy?'
                + 'proxyStartTime='
                + proxyStartTime
                + '&proxyEndTime='
                + proxyEndTime
                + '&completeStartTime='
                + completeStartTime
                + '&completeEndTime='
                + completeEndTime
                + '&carLicense='
                + carLicense
                + '&proxyShopId='
                + proxyshopIdST
                + '&paymentType='
                + paymentType
                + '&shopFlag=1';
        });

        //受托单导出
        $doc.on('click', '.js-st-export', function () {
            var proxyStartTime = $(".js-formInfo2").find("input[name='proxyStartTime']").val();
            var proxyEndTime = $(".js-formInfo2").find("input[name='proxyEndTime']").val();
            var completeStartTime = $(".js-formInfo2").find("input[name='completeStartTime']").val();
            var completeEndTime = $(".js-formInfo2").find("input[name='completeEndTime']").val();
            var carLicense = $(".js-formInfo2").find("input[name='carLicense']").val();
            var proxyshopIdST = $(".st-proxy-shop").val();
            window.location.href = BASE_PATH + '/proxy/settlement/export/proxy?'
                + 'proxyStartTime='
                + proxyStartTime
                + '&proxyEndTime='
                + proxyEndTime
                + '&completeStartTime='
                + completeStartTime
                + '&completeEndTime='
                + completeEndTime
                + '&carLicense='
                + carLicense
                + '&proxyShopId='
                + proxyshopIdST
                + '&paymentType='
                + paymentType
                + '&shopFlag=2';
        });

        //表格渲染
        function tableFill(url, tplId, remote, formId) {
            if (remote == null) {
                remote = true;
            }

            //表格渲染
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'tableInfo',
                //分页容器id，必需
                pageid: 'paging',
                //表格模板id，必需
                tplid: tplId,
                //扩展参数,可选
                data: {
                    paymentType: paymentType
                },
                //关联查询表单id，可选
                formid: formId,
                remote: remote

            });
        }

        //结算 模板填充
        function settlementTab(index, remote, formId) {
            if ($('#chooseTable>div.current-item').data("tag") == "client") {
                tplId = 'tableClientTpl' + index;
                tableFill(url, tplId, remote, formId)
            } else {
                tplId = 'tableServerTpl' + index;
                tableFill(url, tplId, remote, formId)
            }
        }

        //数据查询
        function search(index, remote) {
            var tag = $('.tab-item.current-item').data('tag');

            $("#tableInfo").empty();
            $("#paging").empty();
            if (tag == "client") {
                $(".js-formInfo1").show().removeAttr('no_submit');
                $(".js-formInfo2").hide().attr('no_submit', true);
                url = BASE_PATH + '/proxy/settlement/commision/bill?shopFlag=1';
                settlementTab(index, remote, 'formInfo1');
                $('.js-headline').text('委托方对账单');
            } else if (tag == "server") {
                $(".js-formInfo2").show().removeAttr('no_submit');
                $(".js-formInfo1").hide().attr('no_submit', true);
                url = BASE_PATH + '/proxy/settlement/commision/bill?shopFlag=2';
                settlementTab(index, remote, 'formInfo2');
                $('.js-headline').text('受托方对账单');
            }
        }

        search(index);

        //单击对帐单选项卡
        $doc.on('click', '#chooseTable>div', function () {
            $(this).addClass('current-item').siblings().removeClass('current-item');
            search(index);
        });

        //单击结算选项卡
        $doc.on('click', '#tableTag>div', function () {
            var index = $(this).index();
            paymentType = index;
            $(this).addClass('current-item').siblings().removeClass('current-item');
            location.hash="";
            search(index);
        });

        //确认按钮
        $doc.on('click', '.js-comfirm', function () {
            var tableId = $(this).data('tableId');
            $.ajax({
                url: BASE_PATH + '/proxy/settlement/confirm',
                type: 'get',
                data: {
                    id: tableId
                },
                success: function (result) {
                    if (result.success) {
                        dg.success('操作成功', function () {
                            search(index)
                        });
                    } else {
                        dg.fail(result.errorMsg)
                    }
                }
            });
        });


        //批量结算
        $doc.on('click', '.js-settlement-btn', function () {
            var str = (function () {
                var temp = '';
                $('.js-checkbox:checked').each(function () {
                    temp += $(this).val() + ',';
                });
                if (temp.indexOf(',') != -1) {
                    temp = temp.substring(0, temp.length - 1);
                }
                return temp;
            })();
            if (str == '') {
                dg.fail('请至少选择一条记录');
                return false;
            }

            $.ajax({
                url: BASE_PATH + '/proxy/settlement/all/confirm',
                type: 'get',
                data: {
                    ids: str
                },
                success: function (result) {
                    if (result.success) {
                        dg.success(result.data);
                        search(index);
                    } else {
                        dg.fail(result.errorMsg)
                    }
                }
            });
        });
    });

    $("#tableTag>div").eq(0).trigger('click');

    //查看汇总跳转页面
    $doc.on('click', '.look1', function () {
        window.location.href = BASE_PATH + "/proxy/settlement/detail?flag=1";
    });
    $doc.on('click', '.look2', function () {
        window.location.href = BASE_PATH + "/proxy/settlement/detail?flag=2";
    });

    //全选
    $doc.on('click', '.js-select-all', function () {
        var trAll = $(this);

        if (!trAll.is(':checked')) {
            //全取消
            $("input[type='checkbox']").prop("checked", false);
        } else {
            //全选
            $("input[type='checkbox']").prop("checked", true);
        }
        ;
    });

    $doc.on("click", ".yqx-table input[type='checkbox']", function () {
        if (!($(this).is(':checked'))) {
            $('.js-select-all').prop("checked", false);
        }
    });

});