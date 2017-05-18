/**
 * 2016-05-12 zmx
 * 委拖单查询
 */

$(function () {
    seajs.use([
        'table',
        'select',
        'date',
        'listStyle',
        'dialog',
        'formData',
        'art'
    ], function (tb, st, dt, li,dg,fd,at) {
        var listUrl = BASE_PATH + '/proxy/searchProxyList/authorize',
            doc = $(document);
        dg.titleInit();
        //选项卡初始化
        $(".js-entrust").show();
        $(".js-entrusted").hide();
        var tabletpl = 'TableEntrustTpl',
            cardtpl = 'CardEntrustTpl';
        search();

        function cardortable(remote, formId){
            var cardOrTable = li.getListStyle();
            if (!cardOrTable || cardOrTable === "table") {
                //表格渲染
                $('.js-order-list-card').removeClass('hover');
                $('.js-order-list-table').addClass('hover');
                tableFill(listUrl, tabletpl, remote, formId);
            } else if (cardOrTable === "card") {
                //卡片渲染
                $('.js-order-list-table').removeClass('hover');
                $('.js-order-list-card').addClass('hover');
                cardFill(listUrl, cardtpl, remote, formId);
            }
        }

        function search(remote) {
            var $tab = $('.js-tab-item.current-item').data('tab');
            if($tab=="entrust"){
                $("#orderListContent").empty();

                $(".js-entrust").show().removeAttr('no_submit');
                $(".js-entrusted").hide().attr('no_submit', true);
                listUrl = BASE_PATH + '/proxy/searchProxyList/authorize';
                cardortable(remote, 'authorizeForm');
                $('.js-headline').text('委托单查询');
            }else if($tab=="entrusted"){
                $("#orderListContent").empty();

                $(".js-entrust").hide().attr('no_submit', true);
                $(".js-entrusted").show().removeAttr('no_submit');
                listUrl = BASE_PATH + '/proxy/searchProxyList/trustee';
                cardortable(remote, 'trusteeForm');
                $('.js-headline').text('受托单查询');
            }
        }

        // 密码登录导出
        exportSecurity.tip({'title':'委托单信息'});
        exportSecurity.confirm({
            dom: '.js-export-excel',
            title: '委托单信息',
            callback: function(json){
                var $tab = $('.js-tab-item.current-item').data('tab');
                if($tab=="entrust"){
                    var authorizeData = fd.get("#authorizeForm");
                    location.href = "exportProxyList/authorize?" + $.param(authorizeData);
                }else if($tab=="entrusted"){
                    var trusteeData = fd.get("#trusteeForm");
                    location.href = "exportProxyList/trustee?" + $.param(trusteeData);
                }
            }
        });

        //选项卡切换
        doc.on('click', '.js-tab-item', function(){
            $(this).addClass('current-item').siblings().removeClass('current-item');
            search();
        });


        //单击卡片渲染
        doc.on('click', '.js-order-list-card', function () {
            li.setListStyle("card");
            search();

        });

        //单击表格渲染
        doc.on('click', '.js-order-list-table', function () {
            li.setListStyle("table");
            search();

        });

        function tableFill(url,tabletpl, remote,formId) {
            if (remote == null) {
                remote = true;
            }
            //表格渲染
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'orderListContent',
                //分页容器id，必需
                pageid: 'orderListPage',
                //表格模板id，必需
                tplid: tabletpl,
                //扩展参数,可选
                data: {size: 12},
                formid: formId,
                remote: remote,
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }

        function cardFill(url,cardtpl, remote,formId) {
            if (remote == null) {
                remote = true;
            }
            //卡片渲染
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'orderListContent',
                //分页容器id，必需
                pageid: 'orderListPage',
                //表格模板id，必需
                tplid: cardtpl,
                //扩展参数,可选
                data: {size: 12},
                formid: formId,
                remote: remote,
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }


        //委托单状态下拉列表
        st.init({
            dom: '.js-proxylist',
            url: BASE_PATH + '/proxy/getProxyStatus',
            showKey: "code",
            showValue: "name",
            isClear: true
        });

        //日历绑定
        dt.dpStartEnd({
            start: 'start1',
            end: 'end1',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'end1\')||\'%y-%M-%d\'}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'start1\')}',
                maxDate: '%y-%M-%d'
            }
        });

        //日历绑定
        dt.dpStartEnd({
            start: 'start0',
            end: 'end0',
            startSettings: {
                dateFmt: 'yyyy-MM-dd',
                maxDate: '#F{$dp.$D(\'end0\')||\'%y-%M-%d\'}'
            },
            endSettings: {
                dateFmt: 'yyyy-MM-dd',
                minDate: '#F{$dp.$D(\'start0\')}',
                maxDate: '%y-%M-%d'
            }
        });
    });

    //列表页点击进入详情页
    $(document).on('click', '.js-inforlink', function () {
        var orderId = $(this).data('orderTableid');
        window.location.href = BASE_PATH + "/proxy/detail?proxyOrderId="+orderId;
    });
});


