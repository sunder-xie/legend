$(function () {
    var _contact = null;
    // 顶部信息
    getTopInfo();

    seajs.use([
        'ajax',
        'table',
        'dialog',
        'select',
        'layer',
        'check',
        'art'
    ], function (ajax, table, dialog, select, layer, check, art) {
        var _current = $('.current-tab');
        var getGoodsList = null;
        var _contactData = {};
        var _goodsList = [],
            _saleList = [],
            pickedGoods = new GoodsList();
        // 继续选择/建议出售，未提交的时候
        var templatePickedGoods = new GoodsList();

        var getSaleList = table.init({
            url: BASE_PATH + '/shop/warehouse/share/querySaleList',
            formid: 'saleListForm',
            fillid: 'saleListFill',
            tplid: 'saleListTpl',
            pageid: 'saleListPage',
            dataType: 'json',
            ajax: {
                type: 'post'
            },
            callback: function (a, json, data) {
                _saleList = json.data.content;

                setUnitWidth($('#saleListFill'), function (max) {
                    $('#saleListFill').parents('.yqx-table')
                        .find('th.sale-num').css('padding-right', max/2)
                        .end()
                        .find('th.stock-num').css('padding-right', max/2)
                });

                if (data && typeof data == 'object' && data.goodsStatus == 9) {
                    $('.yqx-table th.remark').removeClass('hide');
                } else {
                    $('.yqx-table th.remark').addClass('hide');
                }
            }
        });
        var _goodsDialog,
            _contactDialog;

        // 门店联系信息
        getContact();

        dialog.titleInit();

        art.helper('toPrice', function (num) {
            var ret = num.toFixed(2);

            if(num == 0) {
                ret = 0;
            }

            return ret;
        });

        check.regList.number = function (val, name) {
            var msg = [
                "",
                "必须大于0",
                "请输入数字"
            ];
            if ($.trim(val) == "") {
                return {msg: msg[0], result: true};
            }
            if ($.isNumeric(val)) {
                if (val <= 0) {
                    return {msg: name + msg[1], result: false};
                } else {
                    return {msg: msg[0], result: true};
                }
            } else {
                return {msg: msg[2], result: false};
            }
        };


        $('.js-title-tips').hover(function () {
            var tips = $(this).find('.tips-box');
            var t, offsetLeft = 0;

            tips.removeClass('hide');
            if($(this).parents('.loss.title-box').length) {
                t = tips[0].parentNode;
                while(t != document.body && t.offsetParent != document.body) {
                    if(t.clientWidth > 0) {
                        offsetLeft += t.offsetLeft;
                    }
                    t = t.parentNode;
                }

                offsetLeft += t.offsetLeft;
                t = offsetLeft + tips[0].clientWidth + 47 - document.body.clientWidth;

                if(t > 0) {
                    tips.css('width', tips[0].clientWidth - t);
                    tips.css('height', tips.find('i')[0].offsetHeight + 10);
                }
            }
        }, function () {
            $(this).find('.tips-box').addClass('hide')
        });

        /* ------ 选择配件弹框 start ------- */

        // 选择出售配件按钮
        $('.js-choose-dialog').on('click', function () {
            var dialogBox = $('.choose-goods-dialog');

            templatePickedGoods = pickedGoods.copy();

            if(!getGoodsList) {
                getGoodsList = table.init({
                    url: BASE_PATH + '/shop/warehouse/share/queryGoods',
                    formid: 'goodsForm',
                    fillid: 'goodsFill',
                    tplid: 'goodsTpl',
                    pageid: 'goodsPage',
                    dataType: 'json',
                    ajax: {
                        type: 'post'
                    },
                    callback: function (a, json) {
                        _goodsList = json.data.content;

                        checkCheckBox(templatePickedGoods, _goodsList);

                        html = art('pickedGoodsTpl', {
                            json: templatePickedGoods.toTableData()
                        });

                        $('.all-count').text('(' + json.data.totalElements + '项)');

                        dialogBox.find('.table-box.picked tbody').html(html);

                        if(!_goodsDialog) {
                            _goodsDialog = dialog.open({
                                content: dialogBox,
                                end: function () {
                                    $('.choose-goods-dialog .yqx-input').val('');
                                    _goodsDialog = null;
                                }
                            });

                            $('.warehouse-share').addClass('hide');
                            $('.warehouse-goods-list').removeClass('hide');

                            $('.picked-count').text('(' + templatePickedGoods.data.length + '项)');
                        } else {
                            dialogBox.parents('.layui-layer-content')
                                .css('height', +dialogBox.css('height').replace('px', '') + 55)
                        }

                        setUnitWidth(dialogBox, function (max) {
                            $('.choose-goods-dialog .yqx-table th.stock-num').css('padding-right', max/2);
                        });

                        $(window).trigger('resize');
                    }
                });
            } else {
                getGoodsList(1);
            }

        });

        $(document).on('click', '.goods-dialog .js-pick-all', function () {
            var all = this.checked;
            $(this).parents('.yqx-table')
                .find('tbody input.js-pick').each(function () {

                if(this.checked != all) {
                    $(this).trigger('click');
                }
            });
        })
            .on('click', '.goods-dialog .js-goods-confirm', function () {
                var data = templatePickedGoods.getData(true);
                var html = art('goodsListTpl', {data: data});

                pickedGoods = templatePickedGoods;
                templatePickedGoods = new GoodsList();

                $('.warehouse-share').addClass('hide');
                $('.warehouse-goods-list').removeClass('hide');

                $('#goodsListFill').html(html);
                // 滚动到顶部
                document.body.scrollTop = 0;

                $('.pick-num').text(data.length);

                setUnitWidth($('#goodsListFill'), function (max) {
                    $('.warehouse-goods-list .yqx-table th.stock-num').css('padding-right', max/2 + 14);
                });
                $('.choose-goods-dialog .yqx-input').val('');

                dialog.close(_goodsDialog);
            })
            .on('click', '.goods-dialog .js-close-dialog', function () {
                dialog.close(_goodsDialog);
            })

            .on('click', '.goods-dialog .js-pick', function () {
                var index = $(this).parents('tr').data('index');
                var table = $(this).parents('.yqx-table');
                if(this.checked) {
                    if(_goodsList[index].stock == 0) {
                        dialog.warn('不能选择库存为0的配件');
                        this.checked = false;
                    } else {
                        templatePickedGoods.pushGoods(_goodsList[index], index);
                    }

                    if( table.find('.js-pick:checked').length == _goodsList.length) {
                        table.find('.js-pick-all')
                            .prop('checked', true);
                    }
                } else {
                    table.find('.js-pick-all')
                        .prop('checked', false);
                    templatePickedGoods.removeGoods(_goodsList[index]);
                }

                $('.picked-count').text('(' + templatePickedGoods.data.length + '项)');
            })
            .on('click', '.choose-goods-dialog .js-choose-type', function () {
                var target = $(this).data('target');
                var main = $(this).parents('.main');
                var html;

                main.find('.table-box').addClass('hide');
                if(this.checked) {
                    main.find(target).removeClass('hide');
                }

                if(target === '.table-box.picked') {
                    html = art('pickedGoodsTpl', {
                        json: templatePickedGoods.toTableData()
                    });

                    $('#goodsForm').addClass('hide');

                    main.find(target + ' tbody').html(html);
                    $('#goodsPage').addClass('hide');

                    setUnitWidth(main.find(target));
                } else {
                    checkCheckBox(templatePickedGoods, _goodsList);
                    $('#goodsPage').removeClass('hide');
                    $('#goodsForm').removeClass('hide');
                }
                main.parents('.layui-layer-content')
                    .css('height', +main.parents('.yqx-dialog').css('height').replace('px', '') + 55)
                
                $(window).trigger('resize');
            });

        // 出售配件查询
        $(document).on('click', '.js-search-goods', function () {
            getGoodsList(1);
        });

        /* ------ 选择配件弹框 end ------- */
        // 门店联系方式编辑
        $('.js-contact-edit').on('click', openContactDialog);

        // 选择发布配件的全选
        $('.js-onsale-pick-all').on('click', function () {
            var all = this.checked;

            $(this).parents('.yqx-table')
                .find('input[type="checkbox"]').each(function () {
                    if(this.checked != all) {
                        $(this).trigger('click');
                    }
                });
        });
        // 初始页面-开始按钮
        $('.js-start').on('click', function () {
            // 推荐配件
            if($('#isHasGoodsOnSale').val() == 'false' || !$('#isHasGoodsOnSale').val()) {
                getRecommendGoods();
            }
        });

        $('.warehouse-goods-list').on('click', '.js-goods-pick', function () {
            var count = +$('.pick-num').text();
            var id = $(this).data('id');

            if(this.checked) {
                count += 1;
            } else {
                count -= 1;
            }

            pickedGoods.set('checked', id, this.checked);

            if(count < 0 || count !== count) {
                count = 0;
            }

            $('.pick-num').text(count);
        })
            .on('blur', '.js-picked-input', function () {
                var id = $(this).parents('tr').data('id');
                var name = $(this).attr('name');

                pickedGoods.set(name, id, $(this).val());
            });
        // 发布
        $('.js-publish').on('click', function () {
            var data = pickedGoods.getPublishData();
            var flag = true;

            if(data.length == 0) {
                dialog.warn('请选择至少一项配件');
                return;
            }

            if(_contact === null && $('.shop-user').text() == '') {
                dialog.warn('请选择门店联系人');
                return;
            }

            $('.warehouse-goods-list .yqx-table tbody tr').each(function () {
                if($(this).find('.js-goods-pick').prop('checked')
                    && !check.check($(this)) ) {
                    flag = false;
                    return false;
                }
            });

            if(!flag) {
                return;
            }

            $.ajax({
                url: BASE_PATH + '/shop/warehouse/share/publish',
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function (json) {
                    if (json.success) {
                        dialog.success('<div class="publish-text">成功发布 <i class="success">' + json.data.publishNum + '</i> 项配件，'
                            + '审核未通过 <i class="fail">' + json.data.failNum + '</i> 项</div>');

                        $('.warehouse-share').add('.warehouse-share .container')
                            .removeClass('hide');

                        $('.warehouse-goods-list')
                            .find('.yqx-table tbody').empty().end()
                            .add('.warehouse-share .init-box')
                            .addClass('hide');
                        $('.pick-num').text(0);

                        // 重新请求
                        getSaleList(1);
                        getListCount();

                        pickedGoods.removeGoods('all');
                    } else {
                        dialog.fail(json.message || '提交失败');
                    }
                }
            });

        });

        // 保存联系人
        $('.js-contact-confirm').on('click', function () {
            var dialog = $(this).parents('.contact-dialog');
            var id = dialog.find('input[name="userId"]').val();
            var name = dialog.find('input[name="name"]').val();
            var tel = dialog.find('.shop-tel').text();
            var address = dialog.find('.shop-address').text();

            if(!id) {
                dialog.warn('请选择联系人');
                return;
            }
            changeContact(id, name, tel, address);

            dialog.close(_contactDialog);
            _contactDialog = null;
        });

        $('.js-close').on('click', function () {
            $('.choose-goods-dialog .yqx-input').val('');

            dialog.close();
            _contactDialog = null;
            _goodsDialog = null;
        });

        $('.js-to-sale').on('click', function () {
            if(pickedGoods.getData().length) {
                dialog.confirm('是否放弃发布已选配件？', function () {
                    $('.warehouse-share').removeClass('hide');
                    $('.warehouse-goods-list').addClass('hide')
                        .find('.yqx-table tbody').empty();
                    $('.pick-num').text(0);

                    pickedGoods.removeGoods('all');
                }, null,
                ['放弃', '取消']);
            } else {
                $('.warehouse-share').removeClass('hide');
                $('.warehouse-goods-list').addClass('hide')
                    .find('.yqx-table tbody').empty();
                $('.pick-num').text(0);

                pickedGoods.removeGoods('all');
            }
        });

        $('.js-list-tab .list-tab').on('click', function () {
            var status = $(this).attr('data-status') || '';
            _current = $('.current-tab');

            if(_current[0] == this) {
                return;
            }
            _current.removeClass('current-tab');
            $(this).addClass('current-tab');

            $('#saleListForm input[name="goodsStatus"]').val(status);
            getSaleList(1);

        });

        $('#saleListFill').on('click', '.js-change-status', function () {
            var parent = $(this).parents('tr');
            var data = {
                id: parent.data('id'),
                goodsStatus: $(this).data('targetStatus')
            };
            var that = this,
                price, name;

            $.ajax({
                url: BASE_PATH + '/shop/warehouse/share/changeSaleGoodsStatus',
                data: data,
                success: function (json) {
                    if(json.success) {
                        dialog.success('修改成功');
                        $(that).text('出售')
                            .removeClass('js-change-status')
                            .addClass('js-republish')
                            .data('targetStatus', '1');

                        parent.find('.sale-status')
                            .text('待出售')
                            .end().addClass('blocked')
                            .removeClass('in-sale');

                        // 文本改为输入框
                        name = parent.find('.goods-name').remove().text();

                        $('<textarea class="yqx-textarea goods-name" data-v-type="required|maxLength:50">' + name + '</textarea>').appendTo(
                            parent.find('.name .form-item')
                        );

                        price = parent.find('.goods-price').remove().text();

                        $('<input class="goods-price yqx-input" name="goodsPrice" data-v-type="required|number:销售价|maxValue:99999999|price" value="' + price + '">')
                            .appendTo(
                                parent.find('.price .form-item')
                            );
                        getListCount();
                    } else {
                        dialog.fail(json.message || '修改失败');
                    }
                }
            })
        })
        // 重新发布
        .on('click', '.js-republish', function () {
            var parent = $(this).parents('tr');
            var that = this;
            var data;

            // 校验
            if(!check.check(parent)) {
                return;
            }

            // 获取将要提交的数据
            _saleList.some(function (e) {
                if(e.id == parent.data('id')) {
                    data = e;
                    return true;
                }
            });

            if(!data) {
                dialog.warn('无数据');
                return;
            }

            data.goodsName = parent.find('.name .yqx-textarea').val();
            data.goodsPrice = parent.find('input[name="goodsPrice"]').val();

            $.ajax({
                url: BASE_PATH + '/shop/warehouse/share/republish',
                type: 'post',
                contentType: 'application/json',
                data: JSON.stringify(data),
                success: function (json) {
                    if (json.success) {

                        // 审核未通过，有敏感词
                        if(json.data.indexOf('敏感词') == -1) {
                            dialog.success(json.data || '发布成功');
                            $(that).text('停止出售')
                                .removeClass('js-republish')
                                .addClass('js-change-status')
                                .data('targetStatus', '0');

                            parent.find('.sale-status')
                                .text('出售中')
                                .end().addClass('in-sale')
                                .removeClass('blocked blocked-word');

                            parent.find('.remark').text('');

                            // 输入改为文本
                            parent.find('.name .yqx-textarea').remove();
                            parent.find('.price .goods-price').remove();

                            $('<i class="goods-name ellipsis-2 js-show-tips">' + data.goodsName + '</i>').appendTo(
                                parent.find('.name .form-item')
                            );
                            $('<i class="goods-price">' + data.goodsPrice + '</i>').appendTo(
                                parent.find('.price .form-item')
                            );
                        } else {
                            dialog.fail(json.data || '发布失败');
                            $(that).text('重新发布');
                            parent.find('.sale-status').addClass('blocked-word')
                                .html('<p>审核</p><p>未通过</p>');
                        }

                        getListCount();
                    } else {
                        dialog.fail(json.errorMsg || '修改失败');
                    }
                }
            });
        });

        getListCount();

        // 获取 出售配件的数据
        function getListCount() {
            $.ajax({
                url: BASE_PATH + '/shop/warehouse/share/querySaleCount',
                global: false,
                success: function (json) {
                    if(json.success) {
                        setListCount(json.data);
                    }
                }
            });
        }

        // 设置 tab 的数据
        function setListCount(data) {
            for(var key in data) {
                $('#' + key)
                    .find('.count').text('(' + data[key] + '项)')
            }
        }

        // 检查选中项，并修改选中列表 pickedList
        function checkCheckBox(pickedList, list) {
            var count = 0, vaild = 0;

            if(!list || !pickedList) {
                $('.choose-goods-dialog')
                    .find('.js-pick-all')
                    .prop('checked', false);
                return;
            }
            // 匹配选中
            $('.choose-goods-dialog').find('.all .yqx-table tbody tr')
                .each(function () {
                    var index = $(this).data('index'),
                        ele = list[index];

                    if(index == undefined || ele.stock == 0) {
                        return;
                    }

                    var checked = (pickedList.map[ ele.id ] && pickedList.map[ ele.id ].checked) || false;
                    $(this).find('.js-pick').prop('checked', checked);

                    vaild += 1;

                    if (checked) {
                        count++;
                    }
                });

            // 全选的匹配
            $('.choose-goods-dialog')
                .find('.js-pick-all')
                .prop('checked', count == vaild);
        }

        // 修改联系人
        function changeContact(userId, name, tel, address) {
            $.ajax({
                url: BASE_PATH + '/shop/warehouse/share/changeShopContact',
                data: {
                    userId: userId
                },
                success: function (json) {
                    if (!json.success) {
                        dialog.fail(json.errorMsg || '修改联系人失败');
                    } else {
                        dialog.success('保存成功');
                        dialog.close(_contactDialog);

                        // 更新页面的联系人信息
                        updateContact(name, tel, address, userId);
                    }
                }
            });
        }

        // 建议出售
        function getRecommendGoods() {
            // 界面切换
            $('.warehouse-share').addClass('hide');
            $('.warehouse-goods-list').removeClass('hide');

            $.ajax({
                url: BASE_PATH + '/shop/warehouse/share/adviceGoods',
                success: function (json) {
                    if(json.success) {
                        if(json.data == null || (json.data && json.data.length == 0) ) {
                            // 无推荐数据时，出现选择配件弹框
                            $('.js-choose-dialog').trigger('click');
                            return;
                        }
                        // 填充
                        var html = art('recommendGoodsTpl', {
                            json: {
                                data: {
                                    content: json.data
                                }
                            }
                        });
                        var d = $('.recommend-goods-dialog');

                        _goodsList = json.data;

                        d.find('.yqx-table tbody').html(html);

                        dialog.open({
                            content: d,
                            end: function () {
                                if(_contact === null) {
                                    openContactDialog();

                                    // 清除暂时的
                                    templatePickedGoods.removeGoods('all');
                                }
                            }
                        });

                        // 默认全部选中
                        templatePickedGoods.pushGoods(_goodsList);

                        setUnitWidth(d, function (max) {
                            $('.recommend-goods-dialog .yqx-table th.stock-num')
                                .css('padding-right', max/2);
                        });
                    }
                }
            });
        }

        // 联系人弹框
        function openContactDialog(fn) {
            _contactDialog = dialog.open({
                content: $('.contact-dialog'),
                end: function () {
                    _contactDialog = null;
                    fn && typeof fn === 'function' && fn();
                }
            });

            // 无默认的时候
            if(!_contact) {
                // 获取联系人列表
                $.get(BASE_PATH + '/shop/warehouse/share/queryShopContact', function (json) {
                    var id = $('#adminId').val();

                    // 设置管理员的电话和地址
                    if(json.success && json.data) {
                        json.data.some(function (e) {
                            if(e.contactId == id) {
                                $('.contact-dialog')
                                    .find('.shop-tel').text(e.contactMobile)
                                    .end()
                                    .find('.shop-address').text(e.address);
                                return true;
                            }
                        });
                    }
                });
            }
        }

        // 获取默认联系人
        function getContact() {
            $.ajax({
                url: BASE_PATH + '/shop/warehouse/share/checkContact',
                global: false,
                success: function (json) {
                    var data = json.data;
                    if (json.success && json.data) {
                        updateContact(data.contactName, data.contactMobile, data.contactAddress);
                    }

                    // 保存此时的数据
                    // 用以判断是否有联系人
                    // 无则为 null
                    _contact = json.data;

                    // 联系人下拉
                    select.init({
                        url: BASE_PATH + '/shop/warehouse/share/queryShopContact',
                        dom: '.js-shop-user',
                        showKey: 'contactId',
                        showValue: 'contactName',
                        retData: _contactData,
                        // 无联系人时设置管理员
                        selectedKey: data ? null : $('#adminId').val(),
                        callback: function (key, name, data, index) {
                            $(this).parents('.contact-dialog')
                                .find('.shop-tel').text(data[index].contactMobile)
                                .end().find('.shop-address').text(data[index].address)
                        }
                    });
                }
            });
        }
    });

    // 全局更新联系人相关信息
    function updateContact(user, tel, address, userId) {
        $('input.shop-user').val(user);
        $('input[name="userId"]').val(userId);
        $('i.shop-user').text(user);
        $('.shop-tel').text(tel);
        $('.shop-address').text(address);
    }

    // 设置单位的宽度，并将其左对齐
    function setUnitWidth(box, fn) {
        var max = 0;
        box.find('.measure-unit').each(function () {
            var width = this.clientWidth;

            if(width > max) {
                max = width;
            }
        }).css({
            width: max,
            textAlign: 'left'
        });

        if(fn && typeof fn == 'function') {
            fn(max);
        }
    }

    // 顶部的信息
    function getTopInfo() {
        $.ajax({
            url: BASE_PATH + '/shop/warehouse/share/topInfo',
            global: false,
            success: function (json) {
                var t, box;
                if(json.success) {
                    for(var key in json.data) {
                        t = json.data[key];
                        box = $('#' + key);

                        // 格式化，用','号分隔
                        t = t.priceFormat();
                        box.text(t);

                        // 判断是否是两行的
                        if( box.parents('.number')[0].offsetHeight > 27) {
                            box.parents('.text-box').addClass('big-num');
                        } else {
                            box.parents('.text-box').removeClass('big-num');
                        }
                    }
                }
            }
        })
    }

    function GoodsList() {
        this.data = [];
        this.map = {};

        return this;
    }

    GoodsList.prototype = {
        // 推入一堆数据
        pushGoods: function (list, index) {
            var that = this;
            var _list;

            // 是否是数组
            if(typeof list === 'object' && list.length) {
                _list = list;
            } else {
                _list = [list];
            }

            _list.forEach(function (ele) {
                // 用 .map 过滤已存在的
                if (ele && !that.map[ele.id]) {
                    ele.index = index;

                    that.data.push(ele);

                    that.map[ele.id] = {
                        checked: true
                    };
                }
            });
        },
        // 删除数据
        removeGoods: function (list) {
            var that = this;

            // 删除全部
            if(list == 'all') {
                this.data.splice(0);
                this.map = {};
            } else if(!list.length || typeof list == 'string') {
                // 非数组时
                list = [list];
            }

            if(list.length && list instanceof Array) {
                list.forEach(function (e) {
                    if(!e) {
                        return;
                    }

                    var id = e.id || e.goodsId;
                    // 找到对应的数据
                    for (var i in that.data) {
                        if (that.data[i] && that.data[i].id == id) {
                            that.data[i] = null;

                            delete that.map[id];
                            break;
                        }
                    }
                });
            }

            this.data = this.data.filter(function (e) {
                if(e) return e;
            });

            return this.data;
        },
        // 复制
        copy: function () {
            var ret = new GoodsList();

            $.extend(ret.data, this.data);
            $.extend(ret.map, this.map);

            return ret;
        },
        set: function (key, id, value) {
            if (this.map[id]) {
                this.map[id][key] = value;
            }
        },
        // 获取选中的数据, 非默认时返回转换后的数据
        getData: function (notDefault) {
            var data = this.data;
            var map = this.map;

            data = data.filter(function (e) {
                if(map[ e.id ].checked) {
                    return e;
                }
            });
            if(notDefault) {
                data = data.map(function (e) {
                    return {
                        id: e.id,
                        price: map[e.id].goodsPrice || e.price,
                        name: map[e.id].goodsName || e.name,
                        stock: e.stock,
                        saleNumber: map[e.id].saleNumber,
                        measureUnit: e.measureUnit,
                        inventoryPrice: e.inventoryPrice,
                        lastInTimeStr: e.lastInTimeStr
                    }
                });
            }

            return data;
        },
        // art 模板需要的数据
        toTableData: function () {
            return {
                data: {
                    content: this.getData()
                },
                map: this.map
            };
        },
        // 提交发布的数据
        getPublishData: function () {
            var data = this.data.filter(function (e) {
                var tr = $('#goodsList_' + e.id);

                if(tr.find('.js-goods-pick').prop('checked')) {
                    return e;
                }
            });

            return data.map(function (e) {
                var tr = $('#goodsList_' + e.id);
                return {
                    goodsId: e.id,
                    goodsPrice: tr.find('input[name="goodsPrice"]').val(),
                    goodsName: tr.find('textarea[name="goodsName"]').val(),
                    goodsStock: e.stock,
                    saleNumber: tr.find('input[name="saleNumber"]').val(),
                    measureUnit: e.measureUnit,
                    inventoryPrice: e.inventoryPrice,
                    lastInTime: e.lastInTimeStr
                }
            });
        }
    };
});