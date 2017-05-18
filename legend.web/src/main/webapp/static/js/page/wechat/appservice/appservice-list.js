/**
 * Created by lby on 16/10/8.
 */

$(function() {
    var myDialog,
        publishedIds = [],
        editStatus = false;

    seajs.use(['table', 'dialog', 'art', 'formData', 'check'], function (table, dialog, art, formData, check) {
        if(history.state){
            changeTab($('#'+ history.state.id));
        }else{
            changeTab($('#tab0'));
        }
        var servicePrice = function (val) {
            var msg = [
                "",
                "售价不能大于市场价"
            ];
            if (+val > +$(this).closest('tr').find('.js-market-price').val()) {
                return {msg: msg[1], result: false};
            } else {
                return {msg: msg[0], result: true};
            }
        };
        var marketPrice = function (val) {
            var msg = [
                "",
                "市场价不能小于售价"
            ];
            if (+val < +$(this).closest('tr').find('.js-service-price').val()) {
                return {msg: msg[1], result: false};
            } else {
                return {msg: msg[0], result: true};
            }
        };
        var downPayment = function (val) {
            var msg = [
                "",
                "预付定金不能大于售价"
            ];
            if (+val > +$(this).closest('tr').find('.js-service-price').val()) {
                return {msg: msg[1], result: false};
            } else {
                return {msg: msg[0], result: true};
            }
        };

        check.helper('servicePrice', servicePrice);
        check.helper('marketPrice', marketPrice);
        check.helper('downPayment', downPayment);


        $(document)
            //tab切换
            .on('click', '.js-search', function(){
                var $this = $(this);
                if(editStatus){
                    if(isEdit()){
                        dialog.confirm('是否保存当前编辑的内容?',
                            function(){
                                save(function(){
                                    changeTab($this);
                                })
                            },
                            function(){
                                changeTab($this);
                            },
                            ['是', '否']
                        );
                    }else{
                        changeTab($this);
                    }
                }else{
                    changeTab($this);
                }

            })
            .on(
            {
                'mouseover': function () {
                    $(this).find('.arrows').addClass('current');
                },
                'mouseout': function () {
                    $(this).find('.arrows').removeClass('current');
                }
            },
            '.edit-tbody tr'
        )

            //活动置顶切换
            .on('click', '.toggle', function (event) {
                var $this = $(this);
                if ($this.hasClass('active')){
                    $this.removeClass('active').next().val('0');
                }else{
                    $this.addClass('active').next().val('1');
                }
                event.stopPropagation();
            })
            //radio被点击
            .on('change', '[type="radio"]', function () {
                var $this = $(this),
                    $tr = $this.closest('tr'),
                    $serviceInput = $tr.find('.js-service-price:not([disable="true"])'),
                    $marketInput = $tr.find('.js-market-price:not([disable="true"])');

                $tr.find('input[name="priceType"]').val($this.val());

                if($this[0].value == '1' ){
                    $serviceInput.focus().attr('readonly', false);
                    $marketInput.attr('readonly', false);
                }else{
                    $serviceInput.attr('readonly', true);
                    $marketInput.attr('readonly', true);
                }
                if(+$serviceInput.val() > +$marketInput.val()){
                    $marketInput.val($serviceInput.val());
                }
            })
            //售价展示input
            .on('click', '.js-service-price', function(){
                $(this).parent().prev().click();
            })
            //默认价格0
            .on('blur', '.yqx-input', function(){
                var $this = $(this);
                if($.trim($this.val()) === ''){
                    $this.val('0');
                }
            })
            //发布
            .on('click', '.js-confirm-publish', function(){
                $('.edit-tbody .no-data').remove();
                var $this = $(this),
                    $tr = $this.closest('tr'),
                    $tr2,
                    $radio;
                $this.text('取消发布').removeClass('js-confirm-publish').addClass('js-cancel-publish');
                $tr.find('.publish-status').text('待发布');
                $tr2 = $tr.clone();
                $tr2.find('.appoint-status').show();
                $radio = $tr2.find('.radio');
                $radio.attr('name', $radio[0].name+'copy');
                if($('#isRecommend').val() != '1'){
                    $tr2.find('.td-img').before('<div class="arrows" title="拖拽排序"></div>');
                }
                $tr2.appendTo('.edit-tbody');
                publishedIds.push($this.data('id'));
            })
            //取消发布
            .on('click', '#publishList .js-cancel-publish', function(){
                var $this = $(this),
                    id = $this.data('id');
                if(publishedIds.indexOf(id) >= 0){
                    var $tr = $('#unpublishList').find('tr[data-id='+id+']');
                    $tr.find('.publish-status').text('未发布');
                    $tr.find('.js-cancel-publish').removeClass('js-cancel-publish').addClass('js-confirm-publish').text('发布');
                    $this.closest('tr').remove();
                    var index = publishedIds.indexOf(id);
                    publishedIds.splice(index, 1);
                }else {
                    cancelPublish(id, function () {
                        var $tbody = $('.edit-tbody'),
                            $tr = $this.closest('tr'),
                            length = $tbody.data('length');
                        $tr.nextAll('tr').each(function(){
                            var index = $(this).data('index');
                            $(this).data('index', index-1);
                        });
                        $tr.remove();
                        $tbody.data('length', length - 1);

                        $('.js-search-btn').trigger('click');
                    });
                }
            })
            .on('click', '#unpublishList .js-cancel-publish', function(){
                var $this = $(this),
                    id = $this.data('id');
                $this.text('发布').removeClass('js-cancel-publish').addClass('js-confirm-publish')
                    .closest('tr').find('.publish-status').text('未发布');
                $('#publishList').find('tr[data-id='+id+']').remove();

                var index = publishedIds.indexOf(id);
                publishedIds.splice(index, 1);
            })
            //保存
            .on('click', '.js-save', function () {
                save(function(){
                    location.reload();
                })
            })
            //新增自定义服务
            .on('click', '.js-add-service', function(){
                if(isEdit()){
                    dialog.confirm('是否保存当前编辑的内容?', function() {
                            save(function(){
                                location.href = BASE_PATH +'/shop/wechat/appservice/edit';
                            })
                        },function(){
                            location.href = BASE_PATH +'/shop/wechat/appservice/edit';
                        },
                        ['是','否'])
                }else{
                    location.href = BASE_PATH +'/shop/wechat/appservice/edit';
                }
            });

        function changeTab ($this) {
            if($this[0].id == 'tab0'){
                $('#isRecommend').val('1');
                $('#parentAppCateId').val('');
            }else{
                $('#isRecommend').val('');
                $('#parentAppCateId').val($this.data('parentAppCateId'));
            }

            table.init({
                url: BASE_PATH + '/shop/wechat/appservice/op/get-published-appService-list',
                fillid: 'publishList',
                tplid: 'publishTpl',
                formid: 'tabForm',
                callback: function () {
                    history.replaceState({id: $this[0].id}, '', location.href);
                    $this.addClass('current-tab').siblings().removeClass('current-tab');
                    publishedIds = [];
                    showEdit();
                    if($('#isRecommend').val()=='1'){
                        $('#publishList').find('.arrows').hide();
                    }
                }
            });

            $('#unpublishList').html('');
            table.init({
                url: BASE_PATH + '/shop/wechat/appservice/op/get-Prepublished-appService-page',
                fillid: 'unpublishList',
                pageid: 'paging',
                tplid: 'unpublishTpl',
                formid: 'tabForm',
                enabledHash: false,
                callback: function(){
                    $.each($('.js-confirm-publish'), function(){
                        var $this = $(this);
                        if(publishedIds.indexOf($this.data('id')) >= 0){
                            $this.text('取消发布').removeClass('js-confirm-publish').addClass('js-cancel-publish')
                                .closest('tr').find('.publish-status').text('待发布');
                        }
                    })
                }
            });
        }

        //保存
        function save(callback){
            if (!check.check($('.edit-tbody'), true)) {
                return;
            }
            var saveList = [],
                $tr = $('.edit-tbody tr:not(".no-data")');

            if ($tr.length == 0){
                saveList[{}];
            }else {
                $tr.each(function (i, e) {
                    var data = formData.get($(this), true);
                    data.sort = i;
                    data.appPublishStatus = 1;
                    saveList.push(data);
                });
            }

            $.ajax({
                url: BASE_PATH + '/shop/wechat/appservice/op/save-appServiceList',
                data: JSON.stringify(saveList),
                type: 'post',
                contentType: 'application/json',
                success: function (result) {
                    if (result && result.success) {
                        dialog.success('保存成功', function () {
                            callback && callback(result);
                        });

                    } else {
                        dialog.fail(result.errorMsg || '保存失败');
                    }
                }
            });
        }
    });

    //编辑状态
    function showEdit(){
        $('.js-edit').show();
        if($('#isRecommend').val() != '1'){
            $(".edit-tbody").sortable({axis: "y" , cursor: "move" ,opacity: 0.8, scroll:false}).disableSelection();
        }
        editStatus = true;
    }
    //检查是否有编辑
    function isEdit(){
        var flag = false,
            $tbody = $('.edit-tbody'),
            $trs = $tbody.find('tr:not(".no-data")');
        if($trs.size() != $tbody.data('length')){
            flag = true;
            return flag;
        }
        $.each($trs, function(){
            if($(this).index() != $(this).data('index')){
                flag = true;
                return false;
            }
        });
        if(!flag) {
            $.each($('#publishList .input'), function(){
                if($.trim($(this).val()) != $.trim($(this).data('origin'))){
                    flag = true;
                    return false;
                }
            })
        }
        return flag;
    }

    //取消发布
    function cancelPublish(id, callback){
        $.ajax({
            type: 'post',
            url: BASE_PATH + '/shop/wechat/appservice/op/cancel-publish-appService',
            data: {serviceId: id},
            success: function () {
                callback && callback();
            }
        });
    }
});

