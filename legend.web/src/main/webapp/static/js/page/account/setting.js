/**
 * zmx  2016-06-12
 * 账户详情 页面
 */
$(function(){

    var $doc = $(document);
    var reverseUrls = {
        vip : BASE_PATH + '/account/member/reverse_grant',
        timesCount: BASE_PATH + '/account/combo/reverse_recharge'
    };

    seajs.use([
        'art',
        "ajax",
        'table',
        'dialog'
    ],function(at,ax,tb,dg){

        //选择tab状态
        tabIndex('flag');
        function tabIndex(name){
            var name = util.getPara(name);
            if(name === '0'|| name==null || name=="") {
                $('.tab-item').eq(0).addClass('current-item').siblings().removeClass('current-item');
                selectTab();
            }else if(name==='1'){
                $('.tab-item').eq(1).addClass('current-item').siblings().removeClass('current-item');
                selectTab();
            }else if(name==='2'){
                $('.tab-item').eq(2).addClass('current-item').siblings().removeClass('current-item');
                selectTab();
            }else if(name==='3'){
                $('.tab-item').eq(3).addClass('current-item').siblings().removeClass('current-item');
                selectTab();
            }
        }

        //状态显示
        //优惠劵类型,0:折扣卷;1:现金券;2:通用券
        at.helper('getStatus', function (status) {
            var msg = [];
            if (status === 0) {
                msg.push('折扣券');
            } else if (status === 1) {
                msg.push('现金券');
            } else if (status === 2) {
                msg.push('通用券');
            }
            return msg.join('');
        });

        //状态显示
        //使用范围,0:全场通用;1.只限服务工时;2.只限指定服务项目打折
        at.helper('getUseRange', function (status) {
            var msg = [];
            if (status === 0) {
                msg.push('全场通用');
            } else if (status === 1) {
                msg.push('只限服务工时');
            } else if (status === 2) {
                msg.push('只限指定服务项目打折');
            }
            return msg.join('');
        });

        //模板填充
        function selectTab(){
            var index = $('.tab .current-item').index(),
                url = BASE_PATH + '/account/coupon/search',
                tpl = 'DiscountTpl',
                tableUrl = BASE_PATH + '/account/coupon/flow/list',
                tableTpl = 'DiscountTableTpl';
                $('.record-title').text('优惠券发放记录');

            if(index == 0){
                url = BASE_PATH + '/account/coupon/search',
                tpl = 'DiscountTpl',
                tableUrl = BASE_PATH + '/account/coupon/flow/list',
                tableTpl ='DiscountTableTpl';
                $('.record-title').text('优惠券发放记录');

            }else if(index == 1){
                url = BASE_PATH + '/account/coupon/suite/list',
                tpl = 'MeterTpl',
                tableUrl = BASE_PATH + '/account/coupon/suite/flow/list',
                tableTpl ='Package';
                $('.record-title').text('套餐发放记录');

            }else if(index == 2){
                url = BASE_PATH + '/account/combo/comboInfo/list',
                tpl = 'timesCountTpl',
                tableUrl = BASE_PATH + '/account/combo/rechargeflow/list',
                tableTpl ='timesCountTableTpl';
                $('.record-title').text('计次卡发放记录');

            }else if(index == 3){
                url = BASE_PATH + '/account/member/get_all_card_info',
                tpl = 'vipTpl',
                tableUrl = BASE_PATH + '/account/member/grantList',
                tableTpl ='vipTableTpl';
                $('.record-title').text('会员卡发放记录');
            }
            //券信息填充
            $.ajax({
               url: url,
               data:{},
               success:function(result){
                   if(result.success){
                       var html = at(tpl,{json:result});
                       $('#tabCon').html(html);
                       used();
                       usedDelete();
                   }else{
                       dg.fail(result.errMsg)
                   }
               }
            });
            //表格填充
            table(tableUrl,tableTpl);
        }

        //停用按钮判断条件
        function used(){
            $('.issued').each(function(){
                var issued = $(this).text();
                if(issued > 0){
                    $(this).parents('.voucher-content').prev('.voucher-title').find('.js-edit').attr('disabled','disabled');
                    $(this).parents('.voucher-content').prev('.voucher-title').find('.js-edit').addClass('disable-btn');
                }else{
                    $(this).parents('.voucher-content').prev('.voucher-title').find('.js-edit').removeAttr('disabled','disabled');
                    $(this).parents('.voucher-content').prev('.voucher-title').find('.js-edit').removeClass('disable-btn');
                }
            });
        };

        //删除按钮判断条件
        function usedDelete(){
            $('.issued').each(function(){
                var issued = $(this).text();
                if(issued > 0){
                    $(this).parents('.voucher-content').prev('.voucher-title').find('.js-delete').attr('disabled','disabled');
                    $(this).parents('.voucher-content').prev('.voucher-title').find('.js-delete').addClass('disable-btn');
                }else{
                    $(this).parents('.voucher-content').prev('.voucher-title').find('.js-delete').removeAttr('disabled','disabled');
                    $(this).parents('.voucher-content').prev('.voucher-title').find('.js-delete').removeClass('disable-btn');
                }
            });
        };

        //tab切换
        selectTab();
        //tab切换
        $doc.on('click','.tab-item',function(){
            $(this).addClass('current-item').siblings('.tab-item').removeClass('current-item');
            selectTab();
        });

        //表格填充
        function table(tableUrl,tableTpl){
            tb.init({
                //表格数据url，必需
                url: tableUrl,
                //表格数据目标填充id，必需
                fillid: 'recordTable',
                //分页容器id，必需
                pageid: 'paging',
                //表格模板id，必需
                tplid: tableTpl,
                //扩展参数,可选
                data: {},
                //关联查询表单id，可选
                formid: null,
                //渲染表格数据完后的回调方法,可选
                callback : null
            });
        }


        var deleteUrl,disableUrl,enableUrl,editUrl;
        function btnUrl(){
            var index = $('.tab .current-item').index();
            if(index == 0){
                //优惠券
                disableUrl = BASE_PATH + '/account/coupon/status/update';
                deleteUrl = BASE_PATH + '/account/coupon/delete';
                editUrl = BASE_PATH + '/account/coupon/create';
                enableUrl = BASE_PATH + '/account/coupon/status/update';
            }else if(index == 1) {
                //套餐
                disableUrl = BASE_PATH + '/account/coupon/suite/status/update';
                deleteUrl = BASE_PATH + '/account/coupon/suite/delete';
                editUrl = BASE_PATH + '/account/coupon/suite/create';
                enableUrl = BASE_PATH + '/account/coupon/suite/status/update';
            }else if(index == 2) {
                //计次卡
                deleteUrl = BASE_PATH + '/account/combo/delete';
                disableUrl = BASE_PATH + '/account/combo/disable';
                enableUrl = BASE_PATH + '/account/combo/enable';
                editUrl = BASE_PATH + '/account/combo/create';
            }else if(index == 3) {
                disableUrl = BASE_PATH + '/account/member/disable';
                deleteUrl = BASE_PATH + '/account/member/delete';
                editUrl = BASE_PATH + '/account/member/edit';
                enableUrl = BASE_PATH + '/account/member/enable';
            }
        }

        //删除按钮
        $doc.on('click','.js-delete',function(){
            var $this = $(this);
            var flag = $this.data('tab');
            var id =$this.parents('.voucher').find('#id').val();
            btnUrl();
            dg.confirm('确定要删除吗?',function(){
                $.ajax({
                    url:deleteUrl,
                    data:{
                        id:id
                    },
                    success:function(result){
                        if(result.success){
                            $this.parents('.voucher').remove();
                            dg.success('删除成功');
                            window.location.href= BASE_PATH + '/account/setting?flag='+ flag;
                        }else{
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            },function(){
                dg.close();
            });
        });


        //停用按钮
        $doc.on('click','.js-disable',function(){
            var $this = $(this);
            var id =$this.parents('.voucher').find('#id').val();
            btnUrl();
            dg.confirm('确定要停用吗',function(){
                $.ajax({
                    url:disableUrl,
                    data:{
                        id:id
                    },
                    success:function(result){
                        if(result.success){
                            dg.success(result.data);
                            selectTab();
                        }else{
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            },function(){
                dg.close();
            });
        });

        //启用按钮
        $doc.on('click','.js-enable',function(){
            var $this = $(this);
            var id =$this.parents('.voucher').find('#id').val();
            btnUrl();
            dg.confirm('确定要启用吗',function(){
                $.ajax({
                    url:enableUrl,
                    data:{
                        id:id
                    },
                    success:function(result){
                        if(result.success){
                            dg.success(result.data);
                            selectTab();
                        }else{
                            dg.fail(result.errorMsg);
                        }
                    }
                });
            },function(){
                dg.close();
            });
        });

        //编辑按钮
        $doc.on('click','.js-edit',function(){
            btnUrl();
            var id =$(this).parents('.voucher').find('#id').val();
            window.location.href = editUrl + "?id=" + id;
        });


    });

    //券信息展开
    $doc.on('click','.js-show-btn',function(){
        var btnText = $(this).find('span'),
            tag = $(this).find('i'),
            showDiv = $(this).parents('.voucher-title').next('.voucher-content');

        if(showDiv.is(":hidden")){
            $('.voucher-content').hide();
            $('.js-show-btn').find('span').text('展开');
            $('.js-show-btn').find('i').removeClass('icon-angle-up').addClass('icon-angle-down');
            showDiv.show();
            btnText.text('收起');
            tag.removeClass('icon-angle-down').addClass('icon-angle-up');
        }else{
            btnText.text('展开');
            tag.removeClass('icon-angle-up').addClass('icon-angle-down');
            showDiv.hide();
        }
    });

    seajs.use('dialog', function(dg) {
        dg.titleInit();
    });

    $doc.on('click','.js-reverse',function(){
        var id = $(this).data("id");
        var target = $(this).data('target');
        var refer = $(this).data('refer');
        var url = BASE_PATH + '/account/coupon/reverse_recharge';
        var self = this;

        if(target && target != '') {
            url = reverseUrls[ target ];
        }
        var data = {};
        data.id = id;

        if (refer && refer != '') {
            data.refer = refer;
        }

        seajs.use('dialog', function(dg) {
            dg.confirm("确认撤销该发放记录?撤销后将无法恢复,请谨慎操作!",function(){
                $.ajax({
                    url: url,
                    data:data,
                    success:function(result){
                        if(result.success){
                            dg.success(result.data);
                            $(self).parent()
                                .empty().text('已撤销');
                        }else{
                            // 接口不同
                            dg.fail(result.errorMsg || result.message);
                        }
                    }
                });
            });
        });
    });

    $doc.on('click', '#recordTable .js-print', function () {
        var flowId = $(this).data("id");
        util.print(BASE_PATH + "/account/combo/grantPrint?id=" + flowId);
    })
});