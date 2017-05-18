//列表页js
$(function(){
    var $document = $(document);
    //选项卡初始化
    $(".tabcon").eq(0).show();
    $document
        //选项卡
        .on('click','.card-tab',function(){
            var $this = $(this),
                $tabcon = $('.tabcon');

            $this.addClass('hover').siblings().removeClass('hover');
            $tabcon.eq($this.index()).show().siblings('.tabcon').hide();

        })

        .on('click','.js-allselect-card',function(){
            var $this = $(this),
                $input =$("input[name='plate']");

            if($this.is(":checked")){
                $input.prop("checked", true);
            }else{
                $input.prop('checked', false);
            }
        })
        .on('click','.js-senior',function(){
            var $senior = $('.senior-box'), parent = $(this).parent();
            var input = $(this).parent().parent().find('.form-item').eq(0);
            var inputParents = $(this).parent().parent().find('.senior-box');
            var arr = [{e: $senior, className: 'hide'},
                {e: input, className:'hide'},
                {e: inputParents.eq(0).find('.form-item'), className: 'senior-width-3'},
                {e: inputParents.eq(1).find('.form-item'), className: 'senior-width-2'},
                {e: parent, className: 'search-absolute-btns'}];
            var obj;

            if(parent.hasClass('search-absolute-btns')) {
                $(this).text('高级搜索');
            } else {
                $(this).text('基本搜索');
                $('input[name=search_searchKey]').val('');
            }
            for(var i in arr) {
                obj = arr[i];
                if(obj.e.hasClass(obj.className)) {
                    obj.e.removeClass(obj.className);
                } else {
                    obj.e.addClass(obj.className);
                }
            }

            //清空查询条件
            $('.senior-box input').val('');
       });

});


/**
 * 2016-04-12
 * 客户列表逻辑
 */

$(function(){
    seajs.use([
        'table',
        'select',
        'date',
        'formData',
        'listStyle',
        'dialog'
    ], function(tb, st, dt,fd,ls, dg){

        var cardOrTable =  ls.getListStyle(),
            listUrl = BASE_PATH + '/shop/customer/search/list',
            localKey = "shop.customer.list.tableColumnSet",
            doc = $(document);


        function tableFill(url){
            var enList = [],
                zhList = [],
                colums = ls.getTableColumn(localKey);
            if(colums) {
                $.each(colums, function(index,element){
                    enList.push(element.enArr);
                    zhList.push(element.zhArr);
                });
            }
            $('.tools-bar.select-btns-right .btn-setting').show();
            //表格渲染
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'listTable',
                //分页容器id，必需
                pageid: 'listTablePage',
                //表格模板id，必需
                tplid: 'listTableTpl',
                //如果模板需要自定义参数，可选
                tpldata: {
                    enList: enList ,
                    zhList: zhList
                },
                //扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'customerListForm',
                //渲染表格数据完后的回调方法,可选
                callback : function() {
                    var page = +$('.yqx-page-inner').find('.current').text() - 1;
                    var i = page * 12;
                    $('.customer-list-table-inner').find('.js-card-order').each(function() {
                        $(this).text(i+=1);
                    });
                }
            });
        }

        function cardFill(url){
            //卡片渲染
            $('.tools-bar.select-btns-right .btn-setting').hide();
            tb.init({
                //表格数据url，必需
                url: url,
                //表格数据目标填充id，必需
                fillid: 'listCard',
                //分页容器id，必需
                pageid: 'listCardPage',
                //表格模板id，必需
                tplid: 'listCardTpl',
                //扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'customerListForm',
                //渲染表格数据完后的回调方法,可选
                callback : null
            });
        }

        if(cardOrTable === "table"){
            //表格渲染
            $('.tools-bar .hover').removeClass('hover');
            $('.js-customer-list-table').addClass('hover');
            tableFill(listUrl);
        }else if(!cardOrTable || cardOrTable === "card"){
            //卡片渲染
            $('.tools-bar .hover').removeClass('hover');
            $('.js-customer-list-card').addClass('hover');
            cardFill(listUrl);
        }
        //单击卡片渲染
        doc.on('click', '.js-customer-list-card', function(){
            cardFill(listUrl);
            ls.setListStyle('card');
            $('#listTable').empty();
        });
        //单击表格渲染
        doc.on('click', '.js-customer-list-table', function(){
            tableFill(listUrl);
            ls.setListStyle('table');
            $('#listCard').empty();
        });

        //卡片点击跳转到客户详情页
        doc.on('click','.card-item',function(){
            var cid = $(this).data("id");
            location.href = BASE_PATH + "/shop/customer/car-detail?refer=customer&id="+cid;
        });

        //设置按钮事件
        doc.off('click.st').on('click.st', '.js-setting-btn', function(){
            var $this = $(this),
                settingBox = $('#listTable').find('.tools-bar'),
                isChecked = {};
            colums = ls.getTableColumn(localKey);
            if(colums){
                $.each(colums, function(index,element){
                    isChecked[element.enArr] = true;
                });
            }

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

        doc.on('click','.js-car-tr',function(){
            var id = $(this).data("content");
            location.href=BASE_PATH+"/shop/customer/car-detail?id="+id+"&refer=customer";
        });

        //表格字段动态配置
        doc.off('click.lb').on('click.lb', '.label-confirm', function(){
            var selectedList = (function(){
                var enArr = [],
                    zhArr = [];
                colums= [];
                $('input:checked', '.setting-box').each(function () {
                    enArr.push($(this).val());
                    zhArr.push($(this).parent().text());
                    var colum = {
                        enArr:$(this).val(),
                        zhArr:$(this).parent().text()
                    };
                    colums.push(colum);
                });
                ls.setTableColumn(localKey,JSON.stringify(colums));
                return {
                    enList: enArr,
                    zhList: zhArr
                }
            })();

            //表格渲染
            tb.init({
                //表格数据url，必需
                url: listUrl,
                //表格数据目标填充id，必需
                fillid: 'listTable',
                //分页容器id，必需
                pageid: 'listTablePage',
                //表格模板id，必需
                tplid: 'listTableTpl',
                //如果模板需要自定义参数，可选
                tpldata: selectedList,
                //是否去远程查询数据，可选
                //remote: false,
                //扩展参数,可选
                data: {size: 12},
                //关联查询表单id，可选
                formid: 'customerListForm',
                //渲染表格数据完后的回调方法,可选
                callback : function() {
                    var page = +$('.yqx-page-inner').find('current').text() - 1;
                    var i = page * 12;
                    $('.customer-list-table-inner').find('.js-card-order').each(function() {
                        $(this).text(i+=1);
                    });
                }
            });
        });

        // 密码登录导出
        exportSecurity.tip({'title':'导出车辆信息'});
        exportSecurity.confirm({
            dom: '.export-excel',
            title: '车辆查询—客户信息',
            callback: function(json){
                _params = fd.get('#customerListForm');
                var url = BASE_PATH+"/shop/customer/export?";
                $.each(_params, function(index,element){
                    url +=index+"="+element+"&";
                });
                url = url.substr(0,url.length-1);
                window.location.href=url;
            }
        });
    });

});