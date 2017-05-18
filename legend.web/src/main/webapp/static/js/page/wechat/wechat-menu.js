/**
 *
 * Created by ende on 16/6/20.
 */

$(function () {
    var originData = [];
    var customMenu = [];
    var defaultMenus = [[],[],[]];
    var deleteIndex = [];
    var url = BASE_PATH + '/shop/wechat/op/qry-menu';
    var save = BASE_PATH + '/shop/wechat/op/save-menu';
    var saveMunu =  BASE_PATH + '/shop/wechat/op/save-custom-menu';
    var deleteMunu =  BASE_PATH + '/shop/wechat/op/del-custom-btn';
    var saveClick = true;
    var confirmDelete, confirmSave, modifyMenu;


    seajs.use(['art', 'dialog', 'ajax', 'formData', 'check'], function (art, dialog, ajax, formData, check) {
        check.init();
        $.ajax({
            url: url,
            success: function (json) {
                if (json && json.success && json.data) {

                    originData = json.data;
                    originData.customMenu.forEach(function (e, i) {

                        // 取整
                        var t = +e.windex / 10 >> 0;
                        if (+e.windex % 10 === 0) {
                            // 其中的数据是 data 的索引
                            customMenu[t-1] = {
                                name: e.kname,
                                children: []
                            };
                        } else {
                            if (customMenu[t-1]) {
                                customMenu[t-1].children.push(i);
                            }
                        }
                    });

                    originData.defaultMenu.forEach(function(e,i){
                        var t = +e.windex / 10 >> 0; //取整
                        if (+e.windex % 10 !== 0) {
                            defaultMenus[t - 1].push(i);
                        }
                    })

                    setView(customMenu, defaultMenus , originData);
                    //三角形按钮样式控制
                    chooseBtnCss();

                } // if (json && json.success)
            } // success
        }); // ajax

        function setView(customMenu, defaultMenus,  originData) {
            var html = art('menuTpl', {
                customMenu: customMenu,
                defaultMenus: defaultMenus,
                originData: originData
            });

            $('.order-body').append($(html));
            
            setEvent(originData);

            $('.js-back').on('click', function () {
                location.reload();
            });
        }

        function setEvent(data) {
            $('.js-tab').on('click', function () {
                var $this = $(this);
                var width = 65;
                var target = $this.data('target');
                var index  = 0, left;

                // tab 切换 class
                $('.current').removeClass('current').addClass('hide');
                $(target).removeClass('hide').addClass('current');

                $('.active').removeClass('active');
                $this.addClass('active');

                $('.official-menu').hide();

                if ($('.isEdit').length){ //如果当前是编辑状态
                    $($this.data('default')).show();
                }

                index = + $('.menu-body h3').index( $this );
                left  = width  + 131 * index - (index ? 10 : 0);
                // 三角形位置改变
                $(target).find('.angle-box').css({
                    left: left + 'px'
                });
                chooseBtnCss();
            });

            $('.js-save').on('click', function () {
                if (!check.check(null, false)) {
                    return;
                }
                var html = art('saveMenuTpl', {});
                confirmSave = dialog.open({
                    area: ['400px', 'auto'],
                    content: html
                });
            })


        //保存的弹窗确认按钮
        $(document)
            .on('click', '.js-save-confirm', function () {
            var saveData = $.extend(true, [], data.customMenu);
            $('.editMenu').each(function(){
                var index = $(this).data('index');
                saveData[index].kname = $(this).val();
            })
            deleteIndex.sort(function(a,b){ //从大到小排列，否则 saveData执行splice时每删除一项，后面的项index会变
                return b-a;
            });
            deleteIndex.forEach(function(e, i){
                saveData.splice(e, 1);
            })

            if (!saveClick) {
                return;
            }
            saveClick = false;

            $.ajax({
                url: save,
                data: JSON.stringify({button: saveData}),
                contentType: 'application/json',
                dataType: 'json',
                type: 'post',
                success: function (json) {
                    if (json && json.success) {
                        dialog.success('保存成功',function(){
                            location.href = BASE_PATH + '/shop/wechat/wechat-menu';
                        });

                    } else {
                        dialog.fail(json.errorMsg || '保存失败');
                    }
                    saveClick = true;
                },
                error: function () {
                    dialog.fail('保存失败');
                    saveClick = true;
                }
            })

            dialog.close(confirmSave);
        })
            .on('click', '.js-save-cancel', function () {
                dialog.close(confirmSave);
            })


            //删除自定义菜单弹窗
            .on('click', '.js-delete', function () {
                var index = $(this).data('index');
                var id = data.customNoUseMenu[index].id;
                var html = art('deleteMenuTpl', {id: id});
                $(this).addClass('deleteIt');
                confirmDelete = dialog.open({
                    area: ['400px', 'auto'],
                    content: html,
                    end: function(){
                        $('.deleteIt').removeClass('deleteIt');
                    }
                });
            })
            .on('click', '.js-delete-confirm', function () {
                var id = $(this).data('id');
                $.ajax({
                    url: deleteMunu,
                    data: {btnId: id},
                    dataType: 'json',
                    type: 'post',
                    success: function (json) {
                        if (json && json.success) {
                            dialog.success('保存成功');
                            $('.deleteIt').parent().hide();
                        } else {
                            dialog.fail(json.errorMsg || '保存失败');
                        }
                    },
                    error: function () {
                        dialog.fail('保存失败');
                    },
                    complete: function(){
                        dialog.close(confirmDelete);
                    }
                })


            })
            .on('click', '.js-delete-cancel', function () {
                dialog.close(confirmDelete);
            })

            //右边菜单里的修改弹窗
            .on('click', '.js-modify',function(){
                var index = $(this).data('index');
                var fillData = data.customNoUseMenu[index];
                var html = art('modifyCustomMenuTpl', {data: fillData, type: 1});
                $(this).addClass('modifyIt');
                modifyMenu = dialog.open({
                    area: ['450px', 'auto'],
                    content: html,
                    end: function(){
                        $('.modifyIt').removeClass('modifyIt');
                    }
                });
            })
            
            //添加菜单弹窗
            .on('click', '.js-addMenu', function () {
                var html = art('modifyCustomMenuTpl', {type: 0});
                modifyMenu = dialog.open({
                    area: ['450px', 'auto'],
                    content: html
                });
            })
            .on('click', '.js-modifyMenu-confirm', function () {
                var type = $(this).data('type');
                var saveData = formData.get('#modifyCustomMenu', true);
                if (!check.check(null, false)) {
                    return;
                }
                $.ajax({
                    url: saveMunu,
                    data: JSON.stringify(saveData),
                    contentType: 'application/json',
                    dataType: 'json',
                    type: 'post',
                    success: function (json) {
                        if (json && json.success) {
                            dialog.success('保存成功');
                            var item = json.data;
                            if(type == 0) { //如果是新增
                                data.customNoUseMenu.push(item); //把新增的菜单项添加到自定义未使用菜单数组
                                var newIndex = data.customNoUseMenu.length - 1; //设置新的index
                                data.customNoUseMenu[newIndex].menuType = 1; // 自定义菜单的menuType设置为1
                                var fillData = {
                                    value: item.kname,
                                    index: newIndex
                                }
                                var html = art('addCustomTpl', fillData);
                                $('.custom-menu-list').append(html);
                            }else if(type == 1){ //如果是修改
                                var index = $('.modifyIt').data('index');
                                data.customNoUseMenu[index] = item;
                                $('.modifyIt').prev().text(item.kname);
                            }
                            chooseBtnCss();
                        }

                        else {
                            dialog.fail(json.errorMsg || '保存失败');
                        }
                    },
                    error: function () {
                        dialog.fail('保存失败');
                    },
                    complete: function(){
                        dialog.close(modifyMenu);
                    }

                })

            })
            .on('click', '.js-modifyMenu-cancel', function () {
                dialog.close(modifyMenu);
            });

            //编辑按钮
            $('.js-edit').on('click',function(){
                var $this = $(this);
                var target = $this.data('target');
                $(target).show();
                $('.custom-menu, .menu-edit, .menu-footer').show();
                $('.menu-selector, .js-edit').hide();
                $this.addClass('isEdit');
                //控制三角按钮样式
                chooseBtnCss();
            })

            //点击左边菜单里的删除
            $('.menu-header').on('click', '.js-remove',function(){
                var $this = $(this);
                var index = $this.data('index');
                var item =  data.customMenu[index];
                deleteIndex.push(index);
                $this.parent().nextAll('.menu-edit').each(function(){
                    var index = $(this).data('index');
                    data.customMenu[index].windex -= 1;
                })

                //官方菜单
                if(item.menuType == 0){
                    var target = $this.data('target');
                    $.each($(target).find('.js-default-add'),function(){
                        var index = $(this).data('index');
                        if (data.defaultMenu[index].kkey == item.kkey && data.defaultMenu[index].menuUrl == item.menuUrl){
                            $(this).removeClass('disabled choosed');
                        }
                    })
                //自定义菜单
                }else{
                    //如果是从未使用的自定义菜单列表里选的
                    if($this.data('from')|| $this.data('from')==0){
                        var fromOrder = $this.data('from');
                        $('.custom-menu-item').eq(fromOrder).show();
                    }else{
                        data.customNoUseMenu.push(item); //把选中的菜单项添加到自定义菜单数组
                        var newIndex = data.customNoUseMenu.length - 1; //设置新的index
                        var fillData = {
                            value: item.kname,
                            index: newIndex
                        }
                        var html = art('addCustomTpl', fillData);
                        $('.custom-menu-list').append(html);
                    }
                }
                $this.closest('.menu-edit').remove();
                chooseBtnCss();
            })
                //点击向上移动的箭头
                .on('click', '.up-icon', function(){
                    if($(this).hasClass('disable')) {
                        return;
                    }
                    var thisParent = $(this).closest('.menu-edit');
                    var prevParent = thisParent.prevAll(':visible:first');
                    var currentIndex = thisParent.data('index');
                    var prevIndex = prevParent.data('index');

                    var temp = data.customMenu[currentIndex].windex;
                    data.customMenu[currentIndex].windex = data.customMenu[prevIndex].windex;
                    data.customMenu[prevIndex].windex = temp;

                    thisParent.data('index', prevIndex).find('a').data('index', prevIndex);
                    prevParent.data('index', currentIndex).find('a').data('index', currentIndex);

                    var currentValue = thisParent.find('input').val();
                    var prevValue = prevParent.find('input').val();
                    thisParent.find('input').val(prevValue).data('index', prevIndex);
                    prevParent.find('input').val(currentValue).data('index', currentIndex);

                })
                .on('click', '.down-icon', function(){
                    $(this).closest('.menu-edit').nextAll().find('.up-icon').eq(0).trigger('click');
                })


            //点击添加菜单的小三角
            $('.menu-manage').on('click', '.left-icon', function(){
                if($(this).hasClass('disable')) {
                    return;
                }
                var index = $(this).data('index');
                var item = $(this).hasClass('js-default-add')? data.defaultMenu[index] : data.customNoUseMenu[index];
                data.customMenu.push(item);  //把选中的菜单项添加到自定义菜单数组
                var newIndex = data.customMenu.length - 1; //设置新的index
                var newItem = data.customMenu[newIndex];
                var prevItemIndex = $('.menu-header.current').find('.menu-edit').last().data('index');
                newItem.windex = prevItemIndex ? data.customMenu[prevItemIndex].windex + 1 : +$('.menu-header.current').find('.firstWindex').val(); //设置新项的windex
                (newItem.menuType == 0) && (newItem.id = null); //如果是官方菜单，设置新项的id为null
                var fillData = {
                    target: '#'+ $('.official-menu:visible').attr('id'),
                    value: item.kname,
                    index: newIndex
                }
                //如果是官方菜单
                if($(this).hasClass('js-default-add')){
                    $(this).addClass('disable choosed');
                //如果是自定义菜单
                }else{
                    fillData.from = $(this).parent().index();
                    $(this).parent().hide();
                }
                var html = art('addMenuItemTpl', fillData);
                $('.menu-header.current').append(html);

                //检查菜单是否已经有5个了
                chooseBtnCss();
            })
            

        }

    });

    //三角形按钮样式控制
    function chooseBtnCss(){
        var current = $('.menu-header.current');
        var size = current.find('.menu-edit').size();
        //右边置灰
        if(size == 5){
            $('.menu-manage').find('.move-icons:visible').addClass('disable');
        }else{
            $('.menu-manage').find('.move-icons:visible:not(".choosed")').removeClass('disable');
        }
        //上下选择置灰
        current.find('.menu-edit').each(function(i, e){
            if(i == 0){
                $(this).find('.up-icon').addClass('disable');
            }else if(i == (size-1)){
                $(this).find('.down-icon').addClass('disable');
            }else{
                $(this).find('.move-icons').removeClass('disable');
            }
        });
    }
});
