/**
 * Created by wjc on 16/5/19.
 * 工单结算收款表
 */

$(function () {
    var tableWidth = 0;
    function setTableWidth(display){
        if(display){
            tableWidth += 14.66;
            $("#table2").css("width",tableWidth+"%");
        }
    }
    var Model = {
        main: function(success) {
            var url = BASE_PATH + "/report/get_config/order_payment";
            $.ajax({
                url: url,
                dataType: 'json',
                success:function(result) {
                    if (result.success) {
                        var datas = result.data;
                        for(var i = 0; i<datas.length;i++){
                            var data = datas[i];
                            var display = data.display;
                            if(!display){
                                $(".dropdown-menu input[data-ref="+data.field+"]").click();
                            }
                            setTableWidth(display);
                        }
                        success && success(result);
                    } else {
                        taoqi.info(result.errorMsg, 2, 3);
                    }
                }
            });
        }
    }
    function GetDateStr(AddDayCount) {
        var dd = new Date();
        dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = dd.getMonth()+1;//获取当前月份的日期
        var d = dd.getDate();
        m=m<10?"0"+m:m;
        d=d<10?"0"+d:d;
        return  y+"-"+m+"-"+d;
    }

    $("#sPayTime").val(GetDateStr(-7));
    $("#ePayTime").val(GetDateStr(0));

    $("#excelBtn").click(function() {
        var url= BASE_PATH + '/shop/stats/order_payment/get_excel?';
        //过滤参数初始化
        var sp = "&search_";
        /*----获取统计时间---*/
        var sPayTime = $("#sPayTime").val();
        if (sPayTime) {
            url += sp + "sPayTime="+sPayTime;
        } else {
            taoqi.info("请选择开始时间");
            return;
        }
        var ePayTime = $("#ePayTime").val();
        if (ePayTime) {
            url += sp + "ePayTime="+ePayTime;
        } else {
            taoqi.info("请选择结束时间");
            return;
        }
        var time1 = new Date(sPayTime.replace("-", "/").replace("-", "/"));
        var time2 = new Date(ePayTime.replace("-", "/").replace("-", "/"));
        var time3 = time2.getTime() - time1.getTime()
        var time4 = 1000*3600*24*61
        if(time3>time4){
            taoqi.info("查询时间不能超过2个月","",3);
            return
        }
        var license = $("#license").val();
        if (license) {
            url += sp + "license="+license;
        }
        var customerName = $("#customerName").val();
        if (customerName) {
            url += sp + "customerName="+customerName;
        }
        var flag = $("#flag").val();
        if (flag) {
            url += sp + "flag="+flag;
        }
        var status = $("#status").val();
        if (status) {
            url += sp + "status="+status;
        }
        var orderSn = $("#orderSn").val();
        if (orderSn) {
            url += sp + "orderSn="+orderSn;
        }
        var serverId = $("#serverId").val();
        if (serverId) {
            url += sp + "serverId="+serverId;
        }

        window.location.href = url;

    });
    $("#searchBtn").click(function() {
        //过滤参数初始化
        var data = {};
        var sp = "search_";
        data["page"] = $("#pageNum").val();
        /*----获取统计时间---*/
        var sTime = $("#sPayTime").val();
        if (sTime) {
            data[sp + "sPayTime"] = sTime;
        } else {
            taoqi.info("请选择开始时间");
            return;
        }
        var eTime = $("#ePayTime").val();
        if (eTime) {
            data[sp + "ePayTime"] = eTime;
        } else {
            taoqi.info("请选择结束时间");
            return;
        }
        var time1 = new Date(sTime.replace("-", "/").replace("-", "/"));
        var time2 = new Date(eTime.replace("-", "/").replace("-", "/"));
        var time3 = time2.getTime() - time1.getTime()
        var time4 = 1000*3600*24*61
        if(time3>time4){
            taoqi.info("查询时间不能超过2个月","",3);
            return
        }
        var license = $("#license").val();
        if (license) {
            data[sp + "license"] = license;
        }
        var customerName = $("#customerName").val();
        if (customerName) {
            data[sp + "customerName"] = customerName;
        }
        var flag = $("#flag").val();
        if (flag) {
            data[sp + "flag"] = flag;
        }
        var status = $("#status").val();
        if (status) {
            data[sp + "status"] = status;
        }
        var orderSn = $("#orderSn").val();
        if (orderSn) {
            data[sp + "orderSn"] = orderSn;
        }
        var serverId = $("#serverId").val();
        if (serverId) {
            data[sp+"serverId"] = serverId;
        }
        function renderHTML(p) {
            taoqi.loading();
            if (p) {
                data['page'] = p;
            }
            $.ajax({
                type: 'GET',
                dataType: 'json',
                url: BASE_PATH + '/shop/stats/order_payment/list',
                data: data,
                success: function (data) {
                    taoqi.close();
                    if (data.success) {
                        var html = template('mainTpl', data);
                        $('#content').html(html);
                        renderPage(data.data);
                        $(".dropdown-menu input[type=checkbox]").each(function () {
                            displayReport.call(this);
                        });
                    } else {
                        taoqi.failure(data.errorMsg);
                        return;
                    }

                }
            });
        }
        function renderPage(data) {
            $.paging({
                dom: $(".qxy_page"),
                itemSize: data.totalElements,
                pageCount: data.totalPages,
                current: data.number + 1,
                backFn: function (p) {
                    renderHTML(p);
                }
            });
        }
        renderHTML($("#pageNum").val());
    });


    var $document = $(document);
    /* 左侧导航栏 start */
    // 新版报表全部展开
    $('.js-new-nav').find('.js-nav-list').show(500);
    $document.on('click', '.js-nav-title', function () {
        $(this).siblings('.js-nav-list').toggle(500);
    });
    /* 左侧导航栏 end */
    //弹框
    $document.on('click','.js-dialog',function(){
             $.layer({
                type: 1,
                title: false,
                area: ['auto', 'auto'],
                border: [0], //去掉默认边框
                shade: [0.5, '#000'],
                shadeClose: false,
                bgcolor: '#fff',
                closeBtn: [1, true], //去掉默认关闭按钮
                shift: 'top',
                page: {
                    html: $('#ins-dialog').html()
                }
            })

    })


    /* 报表展示 start */
    function displayReport() {
        var $this = $(this),
            ref = $this.data('ref'),
            $ref = $('.' + ref),
            checked = $this.prop('checked');
        checked ? $ref.show() : $ref.hide();
    }

    /* 报表展示 end */

    /* 列表项目根据配置更改 start */
    Model.main(function() {
        $("#searchBtn").click();
    });
    /* 列表项目根据配置更改 end */

    /* 表单展示事件 start */
    $document.on('change', '.dropdown-menu input[type=checkbox]', function () {
        displayReport.call(this);
        var datas = [];
        //组装配置bean传给后台
        $(".dropdown-menu input[type=checkbox]").each(function () {
            var $this = $(this)
            data = {
                "field": $this.data('ref'),
                "display": $this.is(':checked')
            }
            datas.push(data);
        });
        var url = BASE_PATH + "/report/save_config/order_payment";
        $.ajax({
            url: url,
            dataType: 'json',
            data: {
                confValue: JSON.stringify(datas)
            },
            success: function (result) {
            }
        });
    });
});
