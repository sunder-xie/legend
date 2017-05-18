/* created by wurenyuan 2016.04.08 */
$(function() {
    var carId = $.trim($("#id").val());
    var refer =  $.trim($("#refer").val());
    var license =  $.trim($("#license").val());
    var tagBtns = ['<i class="tag-btn btn-ok js-tag-subbtn">添加</i>',
        '<i class="tag-btn btn-cancel js-tag-subbtn">删除</i>'];

    // 隐藏
    var hide = false;

    searchMember(carId);
    searchService(carId);

    //分页
    seajs.use([
        'table',
        'dialog',
        'art',
        'check',
        'select'
    ], function(tb, dg, art,ck,st) {

        var orderUrl = BASE_PATH + '/shop/order/customer',
            goodsUrl = BASE_PATH + '/shop/order/historygoods',
            appointUrl = BASE_PATH + '/shop/appoint/customer/list',
            feedbackUrl = BASE_PATH + '/shop/customer_feedback/getListByCustomerCarId',
            precheckUrl = BASE_PATH + '/shop/customer/carInfo',
            orderHistoryUrl = BASE_PATH + '/shop/order/history/list',
            tqCheckUrl = BASE_PATH + '/shop/customer/get-tqCheck',
            warehouseOutUrl = BASE_PATH + '/shop/warehouse/out/out-refund/info',
            doc = $(document);

        dg.titleInit();

        $(document).on('click','.customer-info',function(){
           var customerId = $(this).find('input[name="customerId"]').val();
           if( customerId != ''){
               window.location.href = BASE_PATH + '/account/detail?refer=car_detail&customerId='+customerId;
           }
        });

        //维修工单表单
        function orderTableFill(url) {
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'orderTable',
                //分页容器id，必需
                pageid: 'orderPage',
                //表格模板id，必需
                tplid: 'orderTpl',
                //扩展参数,可选
                data: {
                    size: 5,
                    search_customerCarId:carId
                },
                //关联查询表单id，可选
                formid: 'orderForm',
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }

        //维修配件表单
        function goodsTableFill(url) {
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'goodsTable',
                //分页容器id，必需
                pageid: 'goodsPage',
                //表格模板id，必需
                tplid: 'goodsTpl',
                //扩展参数,可选
                data: {
                    size: 5,
                    customercarid:carId
                },
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }

        //预约信息表单
        function appointFill(url) {
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'appointTable',
                //分页容器id，必需
                pageid: 'appointPage',
                //表格模板id，必需
                tplid: 'appointTpl',
                //扩展参数,可选
                data: {
                    size: 5,
                    search_customerCarId:carId
                },
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }

        //回访信息表单
        function feedbackFill(url) {
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'feedbackTable',
                //分页容器id，必需
                pageid: 'feedbackPage',
                //表格模板id，必需
                tplid: 'feedbackTpl',
                //扩展参数,可选
                data: {
                    size: 5,
                    search_customerCarId:carId
                },
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }

        //车况信息表单
        function precheckFill(url) {
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'precheckTable',
                //分页容器id，必需
                pageid: 'precheckPage',
                //表格模板id，必需
                tplid: 'precheckTpl',
                //扩展参数,可选
                data: {
                    size: 5,
                    search_customerCarId:carId
                },
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }

        //历史导入的工单表单
        function orderHistoryFill(url) {
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'orderHistoryTable',
                //分页容器id，必需
                pageid: 'orderHistoryPage',
                //表格模板id，必需
                tplid: 'orderHistoryTpl',
                //扩展参数,可选
                data: {
                    size: 5,
                    search_carLicenseLike:license
                },
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }

        //淘汽检测单
        function tqCheckFill(url){
            tb.init({
                url : url,
                //表格数据目标填充id，必需
                fillid: 'tqCheckTable',
                //分页容器id，必需
                pageid: 'tqCheckPage',
                //表格模板id，必需
                tplid: 'tqCheckTpl',
                //扩展参数,可选
                data: {
                    size: 5,
                    customerCarId:carId
                },
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback: null
            })
        }

        //退货记录
        function warehouseOutFill(url){
            tb.init({
                url : url,
                //表格数据目标填充id，必需
                fillid: 'warehouseOutTable',
                //分页容器id，必需
                pageid: 'warehouseOutPage',
                //表格模板id，必需
                tplid: 'warehouseOutTpl',
                //扩展参数,可选
                data: {
                    size: 5,
                    customerCarId:carId
                },
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback: null
            })
        }

        //客户进入获取工单进入的客户详情页，默认显示维修工单
        if(refer == 'customer' || refer == 'order'){
            $(".tab-item").removeClass('current-item');
            $(".js-repair-order").addClass('current-item');
            $(".repair-order").show();
            orderTableFill(orderUrl);
        }else if(refer == 'goods'){
            $(".tab-item").removeClass('current-item');
            $(".js-repair-parts").addClass('current-item');
            $(".repair-parts").show();
            goodsTableFill(goodsUrl);
        }else if(refer == 'appoint'){
            $(".tab-item").removeClass('current-item');
            $(".js-reserve-info").addClass('current-item');
            $(".reserve-info").show();
            appointFill(appointUrl);
        }else if(refer == 'feedback'){
            $(".tab-item").removeClass('current-item');
            $(".js-visit-info").addClass('current-item');
            $(".visit-info").show();
            feedbackFill(feedbackUrl);
        }else if(refer == 'precheck') {
            $(".tab-item").removeClass('current-item');
            $(".js-precheck-info").addClass('current-item');
            $(".precheck-info").show();
            precheckFill(precheckUrl);
        }else {
            $(".tab-item").removeClass('current-item');
            $(".js-order-history").addClass('current-item');
            $(".order-history").show();
            orderHistoryFill(orderHistoryUrl);
        }

        // 维修工单 子tab 点击
        doc.on('click','.subtab', function() {
            location.hash =1;
            var orderTag= $(this).attr('order-tag');
            $("#search_orderTag").val(orderTag);
            $('.subtab-active').removeClass('subtab-active');
            $(this).addClass('subtab-active');
            orderTableFill(orderUrl);
        });

        // 维修工单 工单复制 点击
        doc.on('click','.copy-btn', function() {
            var orderId = $(this).attr('order-id');
            var orderTag = $(this).attr('order-tag');
            var orderTagList = ["综合维修单","洗车单","快修快保单","保险维修单","销售单"];
            //综合维修、快修快保才能复制
            dg.confirm("是否复制<span style='color:green'>" + orderTagList[orderTag-1] + "</span>新建工单?",function(){
                if(orderTag == 1){
                    window.location.href = BASE_PATH + "/shop/order/common-add?refer='customer'&copyOrderId=" + orderId;
                }else if(orderTag == 3){
                    window.location.href = BASE_PATH + "/shop/order/speedily?refer='customer'&copyOrderId=" + orderId;
                }
            });
        })
            //换车主start
            var mobileDialog = null;
            $(document).on('click','.js-change-customer',function(){
            mobileDialog = dg.open({
                    content: $('#changeMobileTpl').html()
                });
                ck.init();
            })
            .on('click','.js-mobile-save',function(){
                if (!ck.check()) {
                    return false;
                }
                var new_mobile = $('#new_mobile').val();
                var old_mobile = $('#old-mobile').data("oldMobile");

                if (new_mobile == old_mobile) {
                    dg.fail("请输入不同的手机号");
                    return false;
                }
                var carId = $('.js-change-customer').find('span').data("carId");
                $.ajax({
                    url:BASE_PATH + "/shop/customer/check_mobile",
                    data:{
                        mobile:new_mobile
                    },
                    success:function(result){
                        if(result.success){
                            changMobile(carId,new_mobile);
                        }else{
                            seajs.use('layer',function(){
                                layer.confirm("提示：您所填写的车主电话已存在客户，是否确认将此车牌绑定到该客户下",
                                    {
                                        btn: ['绑定', '不绑定']
                                    }, function () {
                                        changMobile(carId, new_mobile);
                                    },function(){
                                        $("#new_mobile").val("");
                                        dg.fail("请您填写新的车主电话信息");
                                        //$("#new_mobile").onfocus();
                                    });
                            });
                        }
                    }
                })
            })
            .on('click','.js-mobile-cancel',function(){
                $("#new_mobile").val("");
                //$(".change_mobile").addClass("display-none");
                dg.close(mobileDialog);
            });

        function changMobile(carId,mobile){
            $.ajax({
                url:BASE_PATH + '/shop/customer/change_customer',
                data:{
                    mobile:mobile,
                    carId:carId
                },
                success:function(result){
                    if (result.success) {
                        location.href = BASE_PATH + '/shop/customer/car-detail?id='+result.data.id+'&refer=customer';
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }

            })
        }
        //换手机号end

        // 维修工单，维修配件等 tab 切换
        $('.js-tabs').on('click', function() {
            location.hash =1;//默认第一页
            var data = $(this).attr('data-class');

            $('.current-item').removeClass('current-item');
            $(this).addClass('current-item');

            $('.margin-body').each(function() {
                $(this).hide();
            });
            $('.' + data).show();
            if(data == 'repair-order'){
                orderTableFill(orderUrl);
            }else if(data == 'repair-parts'){
                goodsTableFill(goodsUrl);
            }else if(data == 'reserve-info'){
                appointFill(appointUrl);
            }else if(data == 'visit-info'){
                feedbackFill(feedbackUrl);
            }else if(data == 'precheck-info'){
                precheckFill(precheckUrl);
            }else if(data == 'order-history'){
                orderHistoryFill(orderHistoryUrl);
            }else if(data == 'tq-check'){
                tqCheckFill(tqCheckUrl);
            }else{
                warehouseOutFill(warehouseOutUrl);
            }
        });

        // tag add 点击
        $('#tagAdd').on('click', function(){
            $(this).hide();
            $('.info-tag.input-box').show()
        });
        var tagHTML = '<div class="info-tag tag-green js-tag-edit">'
            + '<i></i>'
            + '</div>';

        //tag 下拉选择
        st.init({
            dom:'.js-tag-input',
            url:BASE_PATH + '/shop/customer/customize-tag',
            showKey: "name",
            showValue: "name",
            canInput:true
        });
        // tag 添加确定按钮
        $('#tagAddBtn').on('click', function(){
            var input = $(this).parent().find('.tag-input');
            var id = $('#id').val();

            var newTag = $(tagHTML);
            newTag.find('i').eq(0)
                .text(input.val());

            addGreenTag(input.val(), carId, { $obj: newTag, new: true});

            input.val('');

            $(this).parent().hide();
            $('#tagAdd').show();
            $(this).prev('.yqx-select-options').remove();
        });

        // tag click
        $('.info-tags').on('click', '.js-tag-edit', function() {
            var tagBtn = $(this).find('.tag-btn');

            if(tagBtn.length){
                tagBtn.remove();
                $(this).css('padding-right', '10px');
                return false;
            }

            $(this).css('padding-right', '0');
            if( !$(this).hasClass('tag-gray') ) {
                $(this).append( $(tagBtns[1]) );
            } else {
                $(this).append( $(tagBtns[0]) );
            }
        });
        // tag 上的按钮：添加与删除
        $('.info-tags').on('click', '.js-tag-subbtn', function() {
            // 删除
            var parent = $(this).parent();

            if( $(this).hasClass('btn-ok') ) {
                addGreenTag(
                    parent.find('i').eq(0).text(),
                    carId,
                    { $obj: parent}
                )
            } else {
                var id = parent.attr('tag-id');
                if(id) {
                    $.ajax({
                        url: BASE_PATH + '/shop/customer/tag/delete',
                        type: 'POST',
                        data: {
                            id: id
                        },
                        success:function(result){
                            if(result.success){
                                parent.remove();
                                dg.success("删除成功");
                            }else{
                                dg.fail(result.errorMsg);
                            }
                        }
                    });
                }
            }
        });

        // 点击隐藏与显示
        $('.close-line').on('click', function () {
            var $tables = $('.detail-table'),
                $trs = $tables.eq(0).find('.detail-tr');

            $trs.each(function(index, e) {
                if(index > 1 && hide) {
                    $(e).slideUp()
                } else if(!hide) {
                    $(e).slideDown();
                }
            });

            $tables.each(function (index, e) {
                if(index && hide)
                    $(e).slideUp();
                else if(!hide) {
                    $(e).slideDown();
                }
            });


            hide = !hide;

            if(hide) {
                $('.close-line').find('span').eq(0)
                    .attr('class', 'icon-angle-up');
            } else {
                $('.close-line').find('span').eq(0)
                    .attr('class', 'icon-angle-down');
            }
        });

        // 图片弹窗
        doc.on('click','.js-imgLayer', function() {
            $.ajax({
                url: BASE_PATH + "/shop/customer/get-car-file",
                type: 'GET',
                data:{
                    customerCarId:carId
                },
                success: function(result) {
                    if (result.success) {
                        var html = art('img-dialog',{json:result});
                        dg.open({
                            area: ['460px','500px'],
                            content:html
                        });
                        imgClick1();
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

        //图片切换
        function imgClick1(){
            doc.on('click','.smallpic li img',function(){
                $(this).parents("li").addClass('on').siblings().removeClass('on');
                var src=$(this).attr('src');
                $('.big-img li img').attr('src',src);
            });

            if($('.smallpic li').length>3){
                var l=0;
                var img_length=$('.smallpic li').length;
                var img_height=$('.smallpic li').outerHeight(true);
                $('.smallpic ul').css('width',img_length*img_height);

                doc.on('click','.js-next-btn',function(){
                    if(l<img_length-3){
                        l++;
                        $('.smallpic ul').animate({top:-l*img_height})
                    }
                })
                doc.on('click','.js-prev-btn',function(){
                    if(l>0){
                        l--;
                        $('.smallpic ul').animate({top:-l*img_height})
                    }
                })

            }
        };

        // 完善客户页面跳转
        $('.edit-customer').click(function(){
            window.location.href = BASE_PATH + "/shop/customer/edit?refer=customer&id=" + carId;
        });

        //综合维修跳转页面
        $("#addOrder").click(function(){
            window.location.href = BASE_PATH + "/shop/order/common-add?refer=car-detail&customerCarId=" + carId;
        });
        //洗车单跳转页面
        $("#addCarwash").click(function(){
            window.location.href = BASE_PATH + "/shop/order/carwash?refer=car-detail&customerCarId=" + carId;
        });
        //快修快保跳转页面
        $("#addSpeedily").click(function(){
            window.location.href = BASE_PATH + "/shop/order/speedily?refer=car-detail&customerCarId=" + carId;
        });
        //销售单跳转页面
        $("#addSell").click(function(){
            window.location.href = BASE_PATH + "/shop/order/sell-good?refer=car-detail&customerCarId=" + carId;
        });

        //预约单详情跳转
        doc.on("click",".js-appoint-detail",function(){
            var id = $(this).attr("appoint-id");
            window.location.href = BASE_PATH + '/shop/appoint/appoint-detail?refer=customer&appointId=' + id;
        });
        //预检单详情跳转
        doc.on("click",".js-precheck-detail",function(){
            var id = $(this).attr("data-precheck-id");
            window.location.href = BASE_PATH + '/shop/precheck/precheck-detail?refer=customer&id=' + id;
        });

        //删除车辆
        doc.on('click','.js-delete-car',function(event) {
            var carId = $(this).find('span').attr("data-id");
            dg.confirm("确认删除该车辆吗", function () {
                delcar(carId);
            });
        });

        //删除车辆
        function delcar(carId){
            $.ajax({
                url: BASE_PATH + '/shop/customer_car/delete',
                type:'POST',
                data: {
                    id: carId
                },
                success: function (result) {
                    if(result.success){
                        dg.success("操作成功",function(){
                            location.href=BASE_PATH + "/shop/customer/list";
                        });
                    }else{
                        dg.fail(result.message);
                    }
                }
            });
        }
        //校验删除客户权限
        if($(".info-btn").hasClass('js-delete-customer')){
            util.checkFunc("客户删除",".js-delete-customer");
        }
        var tipId;
        tips(doc,tipId,"#totalOrderCount","车辆开据所有工单的累计次数之和",dg);
        tips(doc,tipId,"#validOrderCount","车辆开据已确认的工单累计次数之和",dg);
        tips(doc,tipId,"#suspendPaymentCount","车辆开据已挂账车辆类型的工单数之和",dg);
        tips(doc,tipId,"#recent6MonthAmount","车辆结算时间为最近6月的工单的应收金额之和",dg);
        tips(doc,tipId,"#suspendAmount","车辆开据工单的挂账总金额",dg);

        //退货记录，校验仓库权限
        doc.on("click",".js-out-detail",function(){
            var func = util.checkFuncList("仓库");
            if(!func){
                dg.fail("您的账号没有仓库权限，请联系管理员进行开通");
                return false;
            }
            var id = $.trim($(this).data("id"));
            if(id != ''){
                window.location.href = BASE_PATH + "/shop/warehouse/out/out-detail?id=" + id;
            }
        });
    });
});

// 会员信息
function searchMember(carId){
    seajs.use(['dialog','ajax','art'],function(dg, ax, art){
        $.ajax({
            url:BASE_PATH + '/account/discount_info',
            data:{
                carId:carId
            },
            success:function(result){
                if(result.success){
                    var html = art('discountInfoTpl',{json:result});
                    $('#discountInfo').html(html);
                }else{
                    dg.fail(result.message);
                }
            }
        });
    });
}

function addGreenTag (tagName, carId, ele) {
    $.ajax({
        url: BASE_PATH + '/shop/customer/tag/add',
        type: 'POST',
        data: {
            tagName: tagName,
            customerCarId: carId
        },
        success:function(result){
            if(result.success){
                seajs.use('dialog', function(dg) {
                    dg.success("添加成功");
                    window.location.reload();
                });
                // 灰色改变的
                if(!ele.new) {
                    ele.$obj.remove();
                    ele.$obj.removeClass('tag-gray')
                        .addClass('tag-green');

                }
                // 移动位置
                if ($('.tag-green').length)
                    $('.tag-green').last().after(ele.$obj);
                else {
                    $('.info-tag').eq(0).before(ele.$obj);
                }
                // add id
                if(result.data || result.data === 0)
                    ele.$obj.attr('tag-id', result.data);
            }else{
                seajs.use('dialog', function(dg) {
                    dg.fail(result.errorMsg);
                });
            }
        }
    });
}

function searchService(carId){
    seajs.use(['dialog','ajax','art'],function(dg, ax, at){
        $.ajax({
            url: BASE_PATH+"/shop/customer/get_service",
            global: false,
            data: {
                carId : carId
            }
        }).done(function(data){
            var list = data.data;
            //全部分类弹框的搜索
            var html = at('serviceTpl',{"data":list});
            $("#serviceContent").html(html);
        });
    });
}

function tips(doc,tipId,id,message,dg){
    doc.on("mouseenter", id, function() {
        var self = this;
        var msg = message;
        tipId = dg.tips(self, msg);
    }).on("mouseleave", id, function(e) {
        var target = e.target;
        if(target.parentNode === this) return;
        dg.close(tipId);
    });
}