$(function () {
    var permission = {};
    seajs.use('dialog', function (dg) {
        dg.titleInit();
    });

    //表格模块初始化
    seajs.use('table', function (tb) {
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/account/search',
            //表格数据目标填充id，必需
            fillid: 'tableTest',
            //分页容器id，必需
            pageid: 'pagingTest',
            //表格模板id，必需
            tplid: 'tableTestTpl',
            //关联查询表单id，可选
            formid: 'formId'
        });
    });

    //动态列表
    seajs.use(['art', 'dialog'], function (tpl, dg) {
        $.ajax({
            url: BASE_PATH + "/account/member/get_all_card_info",
            success: function (result) {
                if (result.success) {
                    var html = tpl('listTpl', result);
                    $('.list').html(html);
                } else {
                    dg.fail(result.message);
                }
            }
        });
    });

    seajs.use([
        'dialog',
        'art'
    ], function (dg) {
        //充值
        $(document).on('click', '.js-func-check', function (event) {
            event.stopPropagation();
            var href = $(this).data('href'),
                name = $(this).data('funcName'),
                errMsg = '您的账号没有此操作权限，请联系管理员进行开通';

            if (permission[name] === false) {
                dg.warn(errMsg);
                return false;
            }

            util.checkFuncList(name, true, function (result) {
                if (result.success) {
                    window.location.href = href;
                } else {
                    permission[name] = false;
                    dg.warn(errMsg);
                    return false;
                }
            })
        });

    });

    $(document).on('click','.js-account-tr',function() {
        var customerId = $(this).data("customerId");
        window.location.href=BASE_PATH+"/account/detail?refer=customer_search&customerId="+customerId;
    });
});
