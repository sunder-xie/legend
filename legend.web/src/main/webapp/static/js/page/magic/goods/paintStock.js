/*
 * create by zmx 2016/11/14
 * 油漆库存
 */

$(function (){
    var doc = $(document);
    seajs.use([
        'table',
        'dialog',
        'select',
        'formData'
    ],function(tb,dg,st,fd){
        //溢出隐藏
        dg.titleInit();
        //页面初始化
        initialize();
        // 配件品牌
        st.init({
            dom: '.js-brand',
            url: BASE_PATH + '/shop/goods_brand/inwarehosue/list',
            showKey: "id",
            pleaseSelect: true,
            showValue: "brandName",
            canInput: true
        });

        doc.on('click','.js-search-btn',function(){
            if( $('.js-brand').val() != '' && $('input[name="search_brandId"]').val() == '' && $('.js-brand').val() != '请选择'){
                $('input[name="search_brandId"]').val('-1')
            }
        });

        doc.on('click','.js-search-btn',function(){
            var data = fd.get('#formData');
            $.ajax({
                url:BASE_PATH + '/goods/paintExt/getAllPaintList',
                data:data,
                success:function(result){
                    if(result.success){
                        $('.goods-ids').val(result.data);
                    }else{
                        $('.goods-ids').val('');
                    }
                }
            })
        });

        //表格模块初始化
        tb.init({
            //表格数据url，必需
            url: BASE_PATH + '/goods/paintExt/list',
            //表格数据目标填充id，必需
            fillid: 'tableCon',
            //分页容器id，必需
            pageid: 'paging',
            //表格模板id，必需
            tplid: 'tableTpl',
            //关联查询表单id，可选
            formid: 'formData',
            //扩展参数,可选
            data: {size: 12},
            callback:function(a, result){
                //回显全部库存数量
                $('.js-all-stock').text(result.data.totalElements);
            }
        });

        //库存盘点跳转（左侧按钮，多选）
        doc.on('click','.js-inventory',function(){
            if(localStorage.toInventoryPaintDialogHide == undefined) {
                stepDialog(1);
            }else{
                stockCheck();
            }
        });

        //全部库存盘点跳转（右侧按钮，全部盘点）
        doc.on('click','.js-stock-select',function(){
            if(localStorage.toInventoryPaintDialogHide == undefined) {
                stepDialog(2);
            }else{
                stockAll();
            }

        });

        //导出
        doc.on('click','.js-export-paintStock',function(){
            //获取查询条件
            var formData = fd.get('#formData');
            location.href = BASE_PATH + "/goods/paintExt/exportPaintStockList?" + $.param(formData);
        });

        //全选
        doc.on('click','.js-select-all',function(){
            var selectAll = $(this).is(':checked');
            if(selectAll){
                $('.js-select').prop('checked',true);
                $('.js-select-num').text('0');
            }else{
                $('.js-select').prop('checked',false)
            }
            //库存数量
            stockNum();
        });
        //单选
        doc.on("click", ".js-select", function () {
            var checkedLen = $('.js-select:checked').length;
            var selectLen = $('.js-select').length;
            if ($(this).is(':checked')) {
                $('.js-select-num').text('0');
            }
            if( checkedLen ==  selectLen){
                $('.js-select-all').prop('checked',true);
            }
            ///库存数量
            stockNum();
        });

        /***********************函数部分***********************/

        //库存数量
        function stockNum(){
            var checkedLen = $('.js-select:checked').length;
            if(checkedLen > 0){
                $('.js-inventory').prop('disabled',false).removeClass('grey-color');
                $('.js-select-num').text(checkedLen)
            }else{
                $('.js-inventory').prop('disabled',true).addClass('grey-color');
                $('.js-select-num').text('0')
            }
        }
        //页面初始化
        function initialize(){
            //获取goodsIds(全部库存用)
            $.ajax({
                url:BASE_PATH + '/goods/paintExt/getAllPaintList',
                success:function(result){
                    if(result.success){
                        $('.goods-ids').val(result.data);
                    }else{
                        $('.goods-ids').val('');
                    }
                }
            });
            //库存总金额
            $.ajax({
                url:BASE_PATH + '/goods/paintExt/totalStockAmount',
                success:function(result){
                    if(result.success){
                        $('.js-stock-money').text(result.data)
                    }
                }
            });
        }

        //油漆盘点流程指导
        function stepDialog(btn){
            var stepDialogCon = null;
            var html = $('#stepDialog').html();
            stepDialogCon = dg.open({
                area:['420px','470px'],
                content:html
            });
            doc.on('click','.js-know',function(){
                if( $('input[name="noTips"]').is(':checked')){
                    localStorage.toInventoryPaintDialogHide = 'false';
                }
                dg.close(stepDialogCon);
                if( btn == 1 ){
                    stockCheck();
                }else if( btn == 2){
                    stockAll()
                }
            });
        }

        //库存预盘点（单选/多选）
        function stockCheck(){
            var goodsIds = [];
            $('.js-select').each(function(){
                if( $(this).is(':checked')){
                    var id = $(this).parents('tr').data('goodsId');
                    goodsIds.push(id)
                }
                return goodsIds;
            });
            window.location.href = BASE_PATH + '/paint/inventory/toInventoryPaint?goodsIds='+ goodsIds;
        }
        //全部库存
        function stockAll(){
            var stockGoodsIds = $('.goods-ids').val();
            window.location.href = BASE_PATH + '/paint/inventory/toInventoryPaint?goodsIds='+ stockGoodsIds;
        }
    });
});