/**
 * zmx  2016-04-08
 * 预约单查询
 */

$(function(){
    var $document = $(document);

    seajs.use([
        'art',
        'table',
        'select',
        'date',
        'dialog',
        'ajax',
        'listStyle'
    ], function(at,tb, st, dt, dg, ax,ls) {
        //状态显示
        // -1 作废, 0 待确认, 1已确认 , 2已生成工单  3,4,5 已取消
        at.helper('getStatus', function (status) {
            var msg = [];
            if (status === -1) {
                msg.push('无效');
            } else if (status === 0) {
                msg.push('待确认');
            } else if (status === 1) {
                msg.push('已确认');
            } else if (status === 2) {
                msg.push('已开单');
            } else if (status == 3 || status == 4 || status == 5) {
                msg.push('已取消');
            }
            return msg.join('');
        });


        st.init({
            dom: '.js-down-payment',
            showKey: "key",
            showValue: "value",
            data: [{
                key: '',
                value: '请选择'
            }, {
                key: 'true',
                value: '有预付定金'
            }, {
                key:'false',
                value:'无预付定金'
            }]
        });

        function cardFill(url) {
            //卡片渲染
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'cardCon',
                //分页容器id，必需
                pageid: 'cardPage',
                //表格模板id，必需
                tplid: 'cardTpl',
                //扩展参数,可选
                data: {},
                //关联查询表单id，可选
                formid: 'orderListForm'
            });
        }

        function tableFill(url) {
            var enList = [],
                zhList = [],
                colums = ls.getTableColumn(tcs_localkey);
            $.each(colums, function(index,element){
                enList.push(element.field);
                zhList.push(element.name);
            });

            //表格渲染
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'tableCon',
                //分页容器id，必需
                pageid: 'tablePage',
                //表格模板id，必需
                tplid: 'tableTpl',
                //如果模板需要自定义参数，可选
                tpldata: {
                    enList: enList,
                    zhList: zhList
                },
                //goback扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'orderListForm',
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        }

        //单击卡片tab渲染
        $document.on('click', '.js-card', function () {
            cardFill(listUrl);
            ls.setListStyle('card');
        });
        //单击表格tab渲染
        $document.on('click', '.js-table', function () {
            tableFill(listUrl);
            ls.setListStyle('table');
        });

        /* 日历 start */
        seajs.use('date', function(dp) {
            dp.dpStartEnd({
                startSettings: {
                    maxDate: '#F{$dp.$D(\'endDate\')}'
                },
                endSettings: {
                    minDate: '#F{$dp.$D(\'startDate\')}'
                }
            });
        });
        /* 日历 end */


        //设置 按钮事件
        $document.off('click.st').on('click.st', '.js-setting-btn', function () {
            var $this = $(this),
                settingBox = $this.siblings('.js-setting-box'),
                isChecked = {},
                colums = ls.getTableColumn(tcs_localkey);
            $.each(colums, function(index,element){
                isChecked[element.field] = true;
            });
            if (settingBox.is(':hidden')) {
                settingBox.slideDown('fast', function () {
                    //回显选择的表格字段
                    $(':checkbox', settingBox).each(function (i) {
                        var val = $(this).val();
                        if (isChecked[val]) {
                            $(this).prop("checked", "checked");
                        }
                    });
                });
            } else {
                settingBox.slideUp('fast');
            }
        });

        //表格字段动态配置
        $document.off('click.lb').on('click.lb', '.label-confirm', function () {
            var isColumnsChanged = false;
            var selectedList = (function () {
                var enArr = [],
                    zhArr = [],
                    colums= [];
                $('input:checked', '.js-setting-box').each(function () {
                    enArr.push($(this).val());
                    zhArr.push($(this).parent().text());
                    var colum = {
                        field:$(this).val(),
                        name:$(this).parent().text(),
                        display:true
                    };
                    colums.push(colum);
                });
                if(JSON.stringify(ls.getTableColumn(tcs_localkey))!=JSON.stringify(colums)){
                    isColumnsChanged = true;
                    ls.setTableColumn(tcs_localkey,colums);
                }
                return {
                    enList: enArr,
                    zhList: zhArr
                }
            })();

            //表格渲染
            if(!isColumnsChanged){//列没有变化,表格不需要重新渲染
                $('.js-setting-box').slideUp('fast');
                return;
            }
            tb.init({
                //表格数据url，必需
                url: listUrl,
                //表格数据目标填充id，必需
                fillid: 'tableCon',
                //分页容器id，必需
                pageid: 'tablePage',
                //表格模板id，必需
                tplid: 'tableTpl',
                //如果模板需要自定义参数，可选
                tpldata: selectedList,
                //是否去远程查询数据，可选
                //remote: false,
                //扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'orderListForm',
                //渲染表格数据完后的回调方法,可选
                callback: null
            });
        });

        //导出
        $document.on('click','.js-export',function(){
            var params = {};
            seajs.use('formData',function(f){
                params = f.get('#orderListForm');
            });
            exportFile(params,BASE_PATH + '/shop/appoint/appoint-list/export');
        });

        function exportFile(params,url){
            var inputs = '';
            $.each(params, function(index,element){
                inputs+='<input type="hidden" name="'+ index +'" value="'+ element +'" />';
            });
            $('<form action="'+ url +'" method="post">'+inputs+'</form>').appendTo('body').submit().remove();
        }

        //开单 弹窗
        $document.on('click','.js-orderlist-dialog',function(){
            var appointId = '';
            var customerCarId = '';
            if (ls.getListStyle() === "card") {
                appointId = $(this).parents(".js-infor-btn").data('appointId');
                customerCarId = $(this).parents(".js-infor-btn").data('customerCarId');
            } else if (ls.getListStyle() === "table") {
                appointId = $(this).parents(".js-td-operate").data('appointId');
                customerCarId = $(this).parents(".js-td-operate").data('customerCarId');
            }
            var appoint = {
                appointId:appointId,
                customerCarId:customerCarId
            }
            var html = at('orderlist-dialog',{appoint: appoint});

            dg.open({
                area: ['300px','230px'],
                content: html
            });
            return false;
        });

        //开洗车单
        $document.on('click','.js-carwash',function() {
            var customerCarId = $(this).data('customerCarId');
            var appointId = $(this).data('appointId');
            window.location.href = BASE_PATH + "/shop/order/carwash?refer=appoint-list&appointId=" + appointId + "&customerCarId=" + customerCarId;
        });
        //开快修快保单
        $document.on('click','.js-fast',function() {
            var appointId = $(this).data('appointId');
            window.location.href = BASE_PATH+"/shop/order/speedily?refer=appoint-list&category=3&appointId="+appointId;
        });
        //开综合维修单
        $document.on('click','.js-zh',function() {
            var appointId = $(this).data('appointId');
            window.location.href = BASE_PATH+"/shop/order/common-add?refer=appoint-list&category=1&appointId="+appointId;
        });



        //取消 弹窗
        var chanelDialog = null;
        $document.on('click','.js-chanel-dialog',function(){
            var appointId = '';
            if (ls.getListStyle() === "card") {
                appointId = $(this).parents(".js-infor-btn").data('appointId');
            } else if (ls.getListStyle() === "table") {
                appointId = $(this).parents(".js-td-operate").data('appointId');
            }
            var html = at('chanel-dialog', {appointId: appointId});
            chanelDialog = dg.open({
                area: ['440px','auto'],
                content: html
            });
            return false;
        });

        //点击取消关闭弹窗
        $document.on('click', '.js-chanel-close', function () {
            dg.close(chanelDialog);
        });


        //提交 取消预约
        $(document).on('click','.js-cancelAppoint',function() {
            var appointId = $(this).data('appointId');

            var cancelReason = $(".t_middle div.hover").text();
            if (cancelReason == "") {
                dg.warn("请选择取消预约的原因！");
                return false;
            }
            $.ajax({
                type: 'post',
                url: BASE_PATH + "/shop/appoint/cancel-appoint",
                data: {
                    appointId: appointId,
                    cancelReason: cancelReason
                },
                success: function (result) {
                    if (result.success) {
                        dg.close(chanelDialog);
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/shop/appoint/appoint-list";
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

        //确认预约
        $(document).on("click",".js_confirm_btn",function() {
            var appointId = '',customerCarId='';
            if (ls.getListStyle() === "card") {
                appointId = $(this).parents(".js-infor-btn").data('appointId');
                customerCarId = $(this).parents(".js-infor-btn").data('customerCarId');
            } else if (ls.getListStyle() === "table") {
                appointId = $(this).parents(".js-td-operate").data('appointId');
                customerCarId = $(this).parents(".js-td-operate").data('customerCarId');
            }
            if(customerCarId==null||customerCarId==''||customerCarId==0){//缺少客户(车辆)信息
                dg.confirm("此预约单缺少客户(车辆)信息,需要编辑完善后才能确认预约.", function () {
                    window.location.href = BASE_PATH + "/shop/appoint/appoint-edit?appointId="+ appointId;
                }, function () {
                    return false;
                });
            } else{
                dg.confirm("您确定要确认该预约单吗?", function () {
                    $.ajax({
                        type: 'post',
                        url: BASE_PATH + "/shop/appoint/confirm-appoint",
                        data: {
                            appointId: appointId
                        },
                        success: function (result) {
                            if (result.success) {
                                dg.success("操作成功", function () {
                                    window.location.href = BASE_PATH + "/shop/appoint/appoint-list";
                                });
                            } else {
                                dg.fail(result.errorMsg);
                            }
                        }
                    });
                }, function () {
                    return false;
                });
            }
            return false;
        });



        //卡片详情页跳转
        $document.on('click','.js-infor-btn',function(){
            var appointId = $(this).data('appointId');
            window.location.href = BASE_PATH+"/shop/appoint/appoint-detail?appointId="+appointId;

        });

        //表格详情页跳转
        $document.on('click','.js-inforlink',function(){
            var appointId = $(this).data('appointTableid');
            window.location.href = BASE_PATH+"/shop/appoint/appoint-detail?appointId="+appointId;

        });


        //取消原因选中效果
        $document.on('click', '.js-hover', function () {
            var $this = $(this);
            $this.addClass('hover').siblings().removeClass('hover');
        });


        //选项卡
        $document.on('click', '.card-tab', function () {
            var $this = $(this),
                $tabcon = $('.tabcon');

            $this.addClass('hover').siblings().removeClass('hover');
            $tabcon.eq($this.index()).show().siblings('.tabcon').hide();

        });


        //展开高级搜索
        $document.on('click', '.js-senior', function () {
            var $senior = $('.senior-box');

            if( $senior.is(':hidden') ) {
                $(".js-senior").html("基本搜索");
                $(".js-search-zh").hide();
            } else {
                $(".js-senior").html("高级搜索");
                $(".js-search-zh").show();
            }
            //清空查询条件
            $('.senior-box input').val('');

            $senior.toggle();
        });


        var listStyle = null,
            listUrl = BASE_PATH + '/shop/appoint/appoint-list/get',
            tcs_localkey = "shop.appoint.appoint-list.tableColumnSet";

        //init
        listStyle = ls.getListStyle();
        if (!listStyle || listStyle === "card") {
            //卡片渲染
            cardFill(listUrl);
            $(".js-card").addClass('hover');
            $(".js-cardcon").show();
        } else {
            //表格渲染
            tableFill(listUrl);
            $(".js-table").addClass('hover');
            $(".js-tabcon").show();
        }
    })
});