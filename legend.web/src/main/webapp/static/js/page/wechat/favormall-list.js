/**
 * Created by zz on 2016/9/6.
 */
var givingStatus = [{
    key: '0',
    value: '仅展示'
},{
    key: '1',
    value: '赠送中'
},{
    key: '2',
    value: '赠送完成'
},{
    key: '3',
    value: '已取消'
}];
var couponStatus = [{
    key: '1',
    value: '赠送中'
},{
    key: '2',
    value: '赠送完成'
},{
    key: '3',
    value: '已取消'
}];
var coupon = $('#new-coupon'),member = $('#new-member');
seajs.use(['select','table','dialog','art','check'], function(st,table,dg,art,ck){
    dg.titleInit();
    ck.init();
    var status;
    var name = util.getPara('flag');
    if(name === "1"){
        $('.list-tab').eq(1).addClass('current-tab').siblings().removeClass('current-tab');
        couponTab();
        status = couponStatus;
    }else{
        status = givingStatus;
    }
    st.init({
        dom: '.js-status',
        showKey: "key",
        showValue: "value",
        isClear: true,
        data: status
    });

    //tab切换
    $(document).on('click','.list-tab',function(){
        var tab = $(this).data('type');
        var data;
        $(this).addClass('current-tab').siblings('.list-tab').removeClass('current-tab');
        $('.yqx-select-options').remove();
        $('input[name="search_givingStatus"]').val('');
        $('.js-status').val('');
        if(tab == '1'){
            cardTab();
            data = givingStatus;
        }else{
            couponTab();
            data = couponStatus;
        }
        st.init({
            dom: '.js-status',
            showKey: "key",
            showValue: "value",
            isClear: true,
            data: data
        });
    });
    if(!!!util.getPara('flag')){
        $('.current-tab').trigger('click');
    }
    $(document).on('click', '.js-is-fold', function () {
            var $this = $(this),
                text;
            $this.closest('tr').next().find('.card-detail').slideToggle('slow');
            text = ($this.text() == "展开" ? "收起" : "展开");
            $this.text(text);
            if(text == '收起'){
                $this.addClass('red-link');
            }else{
                $this.removeClass('red-link');
            }
        });

    //赠送优惠券弹窗
    $(document).on('click','.js-send',function(){
        var id = $(this).parent().siblings('.config-id').val();
        var receiveNumber = $(this).parent().siblings().find('.receive-num').text();
        var html = art('couponSendTpl',{});
        dg.open({
            area: ['400px', 'auto'],
            content: html
        });
        $('input[name="id"]').val(id);
        $('input[name="receiveNumber"]').val(receiveNumber);
    });
    //关闭弹窗
    $(document).on('click','.js-cancel',function(){
        dg.close();
    });
    //弹窗保存按钮
    $(document).on('click','.js-modifymenu-confirm',function(){
        var id = $('input[name="id"]').val();
        var givingNumber = $('input[name="givingNumber"]').val();
        var receiveNumber = $('input[name="receiveNumber"]').val();
        var data = {id:id,givingNumber:givingNumber,givingStatus:1};
        if(parseInt(givingNumber) <= parseInt(receiveNumber)){
            dg.fail('设置数量必须大于已领取数量！');
            return;
        }
        $.ajax({
            url:BASE_PATH + '/shop/wechat/op/save-favormall-coupon',
            data: JSON.stringify(data),
            type: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            success:function(result){
                if(result.success){
                    dg.success('保存成功!');
                    location.href = BASE_PATH + '/shop/wechat/favormall-list?flag=1';
                }else{
                    dg.fail(result.message);
                }
            }
        });
    });

    //取消赠送
    $(document).on('click','.js-cancel-send',function(){
        var configId = $(this).parent().siblings('.config-id').val();
        var givingNumber = $(this).parent().siblings('.coupon-givingNumber').val();
        var couponTypeId = $(this).parent().siblings('.coupon-couponTypeId').val();
        dg.confirm('是否要取消赠送？',function(){
            var data = {id:configId,isCanceled:1,givingNumber:givingNumber,couponTypeId:couponTypeId};
            $.ajax({
                url:BASE_PATH + '/shop/wechat/op/save-favormall-coupon',
                data: JSON.stringify(data),
                type: 'POST',
                dataType: 'json',
                contentType: 'application/json',
                success:function(result){
                    if(result.success){
                        dg.success('取消赠送成功!');
                        location.href = BASE_PATH + '/shop/wechat/favormall-list?flag=1';
                    }else{
                        dg.fail(result.message);
                    }
                }
            });
        });
    });

    //会员卡tab
    function cardTab(){
        member.show();
        coupon.hide();
        table.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/wechat/op/qry-favormall-card-list',
            //表格数据目标填充id，必需
            fillid: 'tableList',
            //分页容器id，必需
            pageid: 'paging',
            //表格模板id，必需
            tplid: 'member-table-tpl',
            //关联查询表单id，可选
            formid: 'searchForm',
            isfirstfill: true
        });
    }
    //优惠券tab
    function couponTab(){
        coupon.show();
        member.hide();
        table.init({
            //表格数据url，必需
            url: BASE_PATH + '/shop/wechat/op/qry-favormall-coupon-list',
            //表格数据目标填充id，必需
            fillid: 'tableList',
            //分页容器id，必需
            pageid: 'paging',
            //表格模板id，必需
            tplid: 'coupon-table-tpl',
            //关联查询表单id，可选
            formid: 'searchForm',
            isfirstfill: true
        });
    }
});
