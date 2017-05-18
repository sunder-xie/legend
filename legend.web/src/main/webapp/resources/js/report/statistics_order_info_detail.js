/**
 * Created by th on 16/5/19.
 * 工单明细表
 */

$(function () {
    var tableWidth = 0;
    function setTableWidth(display){
        if(display){
            tableWidth += 80;
            $(".table2").css("width",tableWidth+"px");
        }
    }
    /* 加载表单展示 */
    var Model = {
        main: function(success) {
            var url = BASE_PATH + "/report/get_config/order_info_detail";
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

    $("#createTimeStart").val(GetDateStr(-7));
    $("#createTimeEnd").val(GetDateStr(0));

    /* 列表项目根据配置更改 start */
    Model.main(function() {
        $("#searchBtn").click();
    });

    /* 报表展示 start */
    function displayReport() {
        var $this = $(this),
            ref = $this.data('ref'),
            $ref = $('.' + ref),
            checked = $this.prop('checked');
        checked ? $ref.show() : $ref.hide();
    }
    var $document = $(document);
    /* 表单展示事件 start */
    $document.on('change', '.dropdown-menu input[type=checkbox]', function () {
        displayReport.call(this);
        var datas = [];
        //组装配置bean传给后台
        $(".dropdown-menu input[type=checkbox]").each(function () {
            var $this = $(this)
            data = {
                "field": $this.data('ref'),
                "name": $this.parent().text(),
                "display": $this.is(':checked')
            }
            datas.push(data);
        });
        var url = BASE_PATH + "/report/save_config/order_info_detail";
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


    $.ajax({
        url:BASE_PATH + '/shop/stats/staff/attendance/getshopemployee',
        type:"get",  //post请求
        success:function(data){
            //请求成功之后填充数据
            //可以把data看成一个数组
            for(var i =0; i<data.data.length; i++){
                //console.log(data.data)
                $("#receiver").append("<option value='"+data.data[i].id+"'>" + data.data[i].name+ "</option>")
                $("#worker").append("<option value='"+data.data[i].id+"'>" + data.data[i].name+ "</option>")
                $("#saler").append("<option value='"+data.data[i].id+"'>" + data.data[i].name+ "</option>")
            }
        }
    })


    $("#searchBtn").click(function() {
        //过滤参数初始化
        var data = {};
        var sp = "search_";
        data["page"] = $("#pageNum").val();
        /*----获取统计时间---*/
        var createTimeStart = $("#createTimeStart").val();
        if (createTimeStart) {
            data[sp + "createTimeStart"] = createTimeStart;
        }
        var createTimeEnd = $("#createTimeEnd").val();
        if (createTimeEnd) {
            data[sp + "createTimeEnd"] = createTimeEnd;
        }
        var time1 = new Date(createTimeStart.replace("-", "/").replace("-", "/"));
        var time2 = new Date(createTimeEnd.replace("-", "/").replace("-", "/"));
        var time3 = time2.getTime() - time1.getTime()
        var time4 = 1000*3600*24*60
        if(time3>time4){
            taoqi.info("查询时间不能超过2个月","",3);
            return
        }


        var carLicense = $("#carLicense").val();
        if (carLicense) {
            data[sp + "carLicense"] = carLicense;
        }
        var customerName = $("#customerName").val();
        if (customerName) {
            data[sp + "customerName"] = customerName;
        }
        var orderTag = $("#orderTag").val();
        if (orderTag) {
            data[sp + "orderTag"] = orderTag;
        }
        var receiver = $("#receiver").val();
        if (receiver) {
            data[sp + "receiver"] = receiver;
        }
        var worker = $("#worker").val();
        if (worker) {
            data[sp + "worker"] = worker;
        }
        var orderStatus = $("#orderStatus").val();
        if (orderStatus) {
            data[sp + "orderStatus"] = orderStatus;
        }
        var orderSn = $("#orderSn").val();
        if (orderSn) {
            data[sp + "orderSn"] = orderSn;
        }
        var saler = $("#saler").val();
        if (saler) {
            data[sp + "saler"] = saler;
        }

        var payTimeStart = $("#payTimeStart").val();
        if (payTimeStart) {
            data[sp + "payTimeStart"] = payTimeStart;
        }
        var payTimeEnd = $("#payTimeEnd").val();
        if (payTimeEnd) {
            data[sp + "payTimeEnd"] = payTimeEnd;
        }
        var time5 = new Date(payTimeStart.replace("-", "/").replace("-", "/"));
        var time6 = new Date(payTimeEnd.replace("-", "/").replace("-", "/"));
        var time7 = time6.getTime() - time5.getTime();
        var time8 = 1000*3600*24*60
        if(time7>time8){
            taoqi.info("结算查询时间不能超过2个月","",3);
            return
        }
        function renderHTML(p) {
            taoqi.loading();
            if (p) {
                data['page'] = p;
            }
            $.ajax({
                type: 'GET',
                dataType: 'json',
                url: BASE_PATH + '/shop/stats/order_info_detail/list/list',
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
                        taoqi.failure(data.message);
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


    $("#excelBtn").click(function() {
       var url= BASE_PATH + '/shop/stats/order_info_detail/get_excel?a=1';

        //过滤参数初始化
        var data = {};
        var sp = "&search_";
        data["page"] = $("#pageNum").val();
        /*----获取统计时间---*/
        var createTimeStart = $("#createTimeStart").val();
        if (createTimeStart) {
            url += sp + "createTimeStart="+createTimeStart;
        }
        var createTimeEnd = $("#createTimeEnd").val();
        if (createTimeEnd) {
            url += sp + "createTimeEnd="+createTimeEnd;
        }

        var time1 = new Date(createTimeStart.replace("-", "/").replace("-", "/"));
        var time2 = new Date(createTimeEnd.replace("-", "/").replace("-", "/"));
        var time3 = time2.getTime() - time1.getTime()
        var time4 = 1000*3600*24*60
        if(time3>time4){
            taoqi.info("查询时间不能超过2个月","",3);
            return
        }

        var carLicense = $("#carLicense").val();
        if (carLicense) {
            url += sp + "carLicense="+carLicense;
        }
        var customerName = $("#customerName").val();
        if (customerName) {
            url += sp + "customerName="+customerName;
        }
        var orderTag = $("#orderTag").val();
        if (orderTag) {
            url += sp + "orderTag="+orderTag;
        }
        var receiver = $("#receiver").val();
        if (receiver) {
            url += sp + "receiver="+receiver;
        }
        var worker = $("#worker").val();
        if (worker) {
            url += sp + "worker="+worker;
        }
        var orderStatus = $("#orderStatus").val();
        if (orderStatus) {
            url += sp + "orderStatus="+orderStatus;
        }
        var saler = $("#saler").val();
        if (saler) {
            url +=sp + "saler=" +saler;
        }
        var orderSn = $("#orderSn").val();
        if (orderSn) {
            url += sp + "orderSn="+ orderSn;
        }

        var payTimeStart = $("#payTimeStart").val();
        if (payTimeStart) {
            url += sp + "payTimeStart="+ payTimeStart;
        }
        var payTimeEnd = $("#payTimeEnd").val();
        if (payTimeEnd) {
            url += sp + "payTimeEnd="+payTimeEnd;
        }
        var time5 = new Date(payTimeStart.replace("-", "/").replace("-", "/"));
        var time6 = new Date(payTimeEnd.replace("-", "/").replace("-", "/"));
        var time7 = time6.getTime() - time5.getTime();
        var time8 = 1000*3600*24*60
        if(time7>time8){
            taoqi.info("结算查询时间不能超过2个月","",3);
            return
        }
        window.location.href = url;

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
});
