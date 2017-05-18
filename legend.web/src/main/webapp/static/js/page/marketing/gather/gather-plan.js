/**
 * Create by zmx 2016/12/16
 * 集客方案
 */

$(function () {
    var doc = $(document),
        _tabIndex,
        _searchTable, _customerList,
        timer;
    var _data = {
        'auditingNoteNum': '年检到期时间',
        'maintainNoteNum':'保养到期时间',
        'insuranceNoteNum':'保险到期时间',
        'birthdayNoteNum':'生日时间'
    };

    var _noteType = {
        'auditingNoteNum': 3,
        'maintainNoteNum': 1,
        'insuranceNoteNum': 2,
        'birthdayNoteNum': 5,
        'lostNoteNum': 6,
        'visitNoteNum': 4
    };

    // 默认短信模板类型
    var _smsType = {
        'activeNum': 9,
        'sleepNum': 8,
        'lostNum': 7,
        'auditingNoteNum': 4,
        'maintainNoteNum': 2,
        'insuranceNoteNum': 3,
        'birthdayNoteNum': 6,
        'lostNoteNum': 7,
        'visitNoteNum': 5
    };

    seajs.use([
        'art',
        'formData',
        'table',
        'dialog',
        'check',
        'revisitDialog'
    ], function (at, fd, tb, dg, ck, revisitDialog) {
        var _param = getAllUrlParam();
        var tag = _param.tag,
            _tab = _param.tab,
            _customerCarId = _param.customerCarId;
        var _selectedCustomer = {};

        if(tag) {
            if (tag == 'inspection')
                tag = 'auditing';
            tag += 'NoteNum';
        }

        if(_param.customerCarId && _param.customerId
            && _param.visitDialog == 'true') {
            revisitDialog.show(_param.customerCarId, _param.customerId, _param.customerName,
                _param.mobile, _param.license, _param.carModel, null, null, _param.id);
        }

        // 短信
        new MarketingSMS(function () {
            return _selectedCustomer;
        }, {
            base: getSMSData,
            all: getSendAllData
        }, {
            callback: function () {
                getCustomerTypeNum(null, $('.js-card.card-hover').data('type'));
            },
            position: 7,
            beforeDialog: function () {
                var text = $.trim(
                    $('.customer-card.card-hover').text()
                ).replace(/\d/g, '').replace(/[（）]/g, '');

                return {
                    title: text,
                    customer: text
                }
            },
            append: function () {
                var coupon = $.trim($('.js-coupon-text').val());

                return coupon ? '并赠送了您一张' + coupon + '优惠券' : '';
            },
            type: 'post'
        }, {
            url: BASE_PATH + '/marketing/gather/plan/sms',
            allUrl: function () {
                var isSearch = !!$.trim($('input[name="searchKey"]').val());
                return isSearch ? BASE_PATH + '/marketing/gather/plan/search/customer/sms'
                    : BASE_PATH + '/marketing/gather/plan/all/customer/sms'
            }
        }, function () {
            return _smsType[$('.js-card.card-hover').data('type')];
        });

        at.helper('getSelectedCustomer', function () {
            return _selectedCustomer;
        });

        at.helper('getNote', function () {
            var key = $('.card-hover').data('type');
            return _data[key];
        });
        //验证
        ck.init();
        //tab切换
        doc.on('click', '.js-tab', function () {
            $(this).addClass('tab-hover').siblings().removeClass('tab-hover');
            _tabIndex = $(this).index();
            var html;
            if( _tabIndex == 0){
                html = $('#activateTpl').html();
                $('.btn-box').show();
            }else if( _tabIndex == 1){
                html = $('#oldClientsTpl').html();
                $('.btn-box').hide();
            }
            $('#tabCon').html(html);
            var userId = $('.name-hover').data("id");
            //选择客户
            if(tag) {
                getCustomerTypeNum(userId, tag);
                tag = undefined;
            } else {
                getCustomerTypeNum(userId);
            }
            //表格
            getCustomerList();
        });

        //客户归属
        doc.on('click', '.js-staff-name', function () {
            $(this).addClass('name-hover').siblings().removeClass('name-hover');
            $('input[name="searchKey"]').val('');

            var userId = $('.name-hover').data("id");
            var customerType = $('.card-hover').data('type');

            _selectedCustomer = {};
            //选择客户
            getCustomerTypeNum(userId,customerType);
        });

        //判断从哪里进入页面
        function initialize() {
            var currentName = $('.js-staff-name').hasClass('name-hover') ? 1 : 0;
            if( currentName == 0){
                $('.js-staff-name').eq(0).addClass('name-hover')
            }
            var refer = util.getPara('refer');
            var maintain = util.getPara('maintain');

            if(refer != '' || maintain != ''){
                $('.js-staff-name').removeClass('name-hover');
                $('.js-staff-name').eq(0).addClass('name-hover');
            }
        }
        initialize();
        if(tag) {
            $('.js-tab').eq(_tab).trigger('click');
        } else {
            $('.js-tab').eq(0).trigger('click');
        }

        //选择客户
        doc.on('click', '.js-card', function () {

            $('.js-card').removeClass('card-hover');
            $(this).addClass('card-hover');
            $('input[name="searchKey"]').val('');
            $('.js-totalCouponNum').val('').prop('disabled',false);
            $('.js-perAccountNum').val('1').prop('disabled',false);

            if(_tabIndex == 0){
                $('.js-table-title').text($('.card-hover').text());
            }else if(_tabIndex == 1){
                $('.js-table-title').text('向老客户发送优惠券');
            }

            _selectedCustomer = {};
            //表格
            getCustomerList();
        });

        //全选
        doc.on('click','.js-select-all',function(){
            var checked = this.checked;
            $('.yqx-table .js-select').each(function () {
                 if(this.checked !== checked) {
                     $(this).click();
                 }
            });
        });
        doc.on('change','.js-select',function(){
            var sel = $('.js-select').length;
            var checkedSel = $('.js-select:checked').length;
            var data = fd.get( $(this).parents('tr') );

            _selectedCustomer[data.customerCarId] = this.checked ? data : false;
            if(sel == checkedSel){
                $('.js-select-all').prop('checked',true)
            }else{
                $('.js-select-all').prop('checked',false);
            }
        });

        //搜索框
        doc.on('blur','input[name="searchKey"]',function(){
            if($.trim( $(this).val() ))
                $('.js-card').removeClass('card-hover');
        });
        //搜索按钮
        doc.on('click','.js-search',function(){
            var searchKey = $.trim($('input[name="searchKey"]').val());

            if(!searchKey) {
                dg.warn('搜索内容不能为空');
                return;
            }
            $('.js-card').removeClass('card-hover');
            if(!_searchTable) {
                _searchTable = tb.init({
                    //表格数据url，必需
                    url: BASE_PATH + '/marketing/gather/plan/customer/search',
                    //表格数据目标填充id，必需
                    fillid: 'tableCon',
                    //表格模板id，必需
                    tplid: 'tableTpl',
                    pageid: 'pagingBox',
                    //扩展参数,可选
                    data: getSearchTableData,
                    //关联查询表单id，可选
                    formid: null,
                    //渲染表格数据完后的回调方法,可选
                    callback: function (result) {
                        if (_tabIndex == 0) {
                            $('.js-check-tr').show();
                            $('.js-operation-tr').show();

                            if(_customerCarId) {
                                $('.js-operation-tr[data-customer-car-id="' + _customerCarId + '"] a').trigger('click');
                                _customerCarId = undefined;
                            }
                            $('.js-table-title').text('盘活客户')
                        } else if (_tabIndex == 1) {
                            $('.js-check-tr').hide();
                            $('.js-operation-tr').hide();
                        }
                        $('.js-send-tr').hide();
                    }
                });
            } else {
                _searchTable(1);
            }
        });
        //生成二维码
        doc.on('click','.js-qrcodeView',function(){
            var totalCouponNum  = $('.js-totalCouponNum').val();
            var perAccountNum = $('.js-perAccountNum').val();
            var coupon = $('.js-coupon-id').val();
            if(!ck.check()){
                return;
            }
            if( coupon == ''){
                dg.fail('请选择优惠券');
                return;
            }
            if(Number(totalCouponNum) < Number(perAccountNum)){
                dg.fail('每位客户赠送数量不能小于每个账号限制使用！');
                return;
            }
            dg.success('生成二维码成功');
            $('.js-totalCouponNum').prop('disabled','true');
            $('.js-perAccountNum').prop('disabled','true');
            $('.js-send-tr').show();
        });
        //重新选择优惠券
        doc.on('click','.js-add-coupon',function(){
            $('.js-totalCouponNum').prop('disabled',false);
            $('.js-perAccountNum').prop('disabled',false);
        });
        //二维码查看
        doc.on('click','.js-ticket',function(){
                var $this = $(this);
                var codeImg = $(this).find('.js-code').html();

                if( codeImg == ''){
                    var data = {
                        customerId:$(this).parents('tr').find('input[name="customerId"]').val(),
                        customerCarId:$(this).parents('tr').find('input[name="customerCarId"]').val(),
                        couponInfoId : $('.js-coupon-id').val(),
                        totalCouponNum : $('.js-totalCouponNum').val(),
                        perAccountNum : $('.js-perAccountNum').val(),
                        userId:$('input[name="userId"]').val(),
                        userName:$('input[name="userName"]').val()
                    };
                    $.ajax({
                        type:'post',
                        url: BASE_PATH +'/marketing/gather/plan/gather-coupon/save',
                        data: JSON.stringify(data),
                        dataType: 'json',
                        contentType: "application/json",
                        success:function(result){
                            if(result.success){
                                var url = basePath() +'/mobile/html/gather/gather.html?gatherCouponConfigId='+result.data.id;
                                $this.find('.js-code').qrcode({
                                    width: 114,
                                    height: 114,
                                    text: url
                                });
                                $this.find('.code').show();
                            }else{
                                $this.find('.code').hide();
                            }
                        }
                    });
                }else{
                    $this.find('.code').show();
                }
        });
        doc.on('mouseout','.js-ticket',function(e){
            if(!timer)
            timer = setTimeout(function(){
                $('.code').hide();
                timer = null;
            },500);

            e.stopPropagation();
        });
        doc.on('mouseover','.js-code-box',function(){
            clearTimeout(timer);
            timer = null;
            $(this).show();
        });

        doc.on('mouseout','.js-code-box',function(){
            clearTimeout(timer);
            timer = null;
            $('.code').hide();
        });


        /**************************弹窗部分待修改******************************/
        //电话回访记录
        revisitDialog({
            dom:'.js-return-record',
            callback: function () {
                getCustomerTypeNum(null, $('.js-card.card-hover').data('type'));
            }
        });

        //选择优惠券
        getCouponList({
            dom: '.js-add-coupon',
            single:true,
            singleClose: true,
            callback: function (data) {
                $('.js-coupon-text').val(data[0] ? data[0].couponName : '')
                    .toggleClass('border-deep-g', data[0]);
                $('.js-coupon-id').val(data[0] ? data[0].id : '');

                $('.reelect').toggleClass('hide', !data[0]);
                $('.coupon').toggleClass('hide', !!data[0]);
            }
        });

        /****************************函数部分*********************************/
        /**
         * js获取浏览器地址栏的basePath
         * @returns {string}
         */
        function basePath(){
            var curWwwPath = window.document.location.href;
            var pathName = window.document.location.pathname;
            var pos = curWwwPath.indexOf(pathName);
            var rootUrl = curWwwPath.substring(0, pos);
            return rootUrl+BASE_PATH;
        }

        // 查询不同类型客户数
        function getCustomerTypeNum(userId,customerType) {
            var data = {};
            if (userId) {
                data.userId = userId;
            }
            $.ajax({
                type: 'get',
                url: BASE_PATH + "/marketing/gather/plan/customer/type/num",
                data: data
            }).done(function (result) {
                if (result.success) {
                    var html = at('customerNumTpl', {json: result});
                    $('.js-customerCon').html(html);
                    $('#sleepNum2').text(result.data.sleepNum);
                    $('#lostNum2').text(result.data.lostNum);
                    if(_tabIndex == 0){
                        $('.js-table-title').text($('.card-hover').text());
                    }else if(_tabIndex == 1){
                        $('.js-table-title').text('向老客户发送优惠券');
                    }


                    if(customerType){
                        $('.js-card').removeClass('card-hover');
                        $('.js-card[data-type="' + customerType + '"]').addClass('card-hover');
                        if(_tabIndex == 0){
                            $('.js-table-title').text($('.card-hover').text());
                        }else if(_tabIndex == 1){
                            $('.js-table-title').text('向老客户发送优惠券');
                        }
                    }
                }
                //表格
                getCustomerList();
            });
        }
        //点击搜索时传参
        function getSearchTableData() {
            var searchKey = $('input[name="searchKey"]').val();
            var userId = $('.name-hover').data('id');

            return {
                search_userIds: userId,
                search_searchKey: searchKey
            }
        }

        //表格数据传参
        function getCustomerData() {
            var userId = $('.name-hover').data("id");
            var customerType = $('.card-hover').data('type');
            var ret = {
                customerType: customerType,
                userId: userId
            };

            if(!ret.customerType) {
                ret.customerType = 'hasMobileNum';
            }
            return ret;
        }

        // 查询客户列表
        function getCustomerList() {
            if(!_customerList) {
                _customerList = tb.init({
                    //表格数据url，必需
                    url: BASE_PATH + '/marketing/gather/plan/customer/list',
                    //表格数据目标填充id，必需
                    fillid: 'tableCon',
                    //表格模板id，必需
                    tplid: 'tableTpl',
                    pageid: 'pagingBox',
                    //扩展参数,可选
                    data: getCustomerData,
                    //关联查询表单id，可选
                    formid: null,
                    //渲染表格数据完后的回调方法,可选
                    callback: function () {
                        if (_tabIndex == 0) {
                            $('.js-check-tr').show();
                            $('.js-operation-tr').show();

                            if(_customerCarId) {
                                $('.js-operation-tr[data-customer-car-id="' + _customerCarId + '"] a').trigger('click');
                                _customerCarId = undefined;
                            }
                        } else if (_tabIndex == 1) {
                            $('.js-check-tr').hide();
                            $('.js-operation-tr').hide();
                        }
                        $('.js-send-tr').hide();
                    }
                });
            } else {
                _customerList(1);
            }
        }

        function getSMSData() {
            return {
                couponInfoId: $('.js-coupon-id').val(),
                noteType: _noteType[$('.card-hover.js-card').data('type')],
                userId: $('.name-hover').data('id')
            };
        }

        function getSendAllData() {
            var ret = {
                userId: $('.name-hover').data('id'),
            };
            var key = $.trim( $('input[name="searchKey"]').val() );

            if(key) {
                ret.searchKey = key;
            } else {
                ret.customerType = $('.card-hover.js-card').data('type');
            }

            return ret;
        }
    });

    function getAllUrlParam() {
        var ret = {}, index;
        var str = $.trim(location.search.replace(/^\?/, ''));
        str = str.split('&');
        for(var i in str) {
            index = str[i].indexOf('=');

            ret[str[i].slice(0, index)] = decodeURIComponent(str[i].slice(index + 1));
        }

        return ret;
    }
});