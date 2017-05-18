/**
 * Created by zz on 2016/11/24.
 */
seajs.use(['check','formData','art','dialog'],function(ck,formData,art,dg){
    var couponList = [];
    var coupon = $('.js-coupon-join');
    var thisModule = $('#moduleCouponDiscount');
    dg.titleInit();
    coupon.each(function() {
        if ($(this).hasClass('yqx-btn-1')) {
            var couponNum = $(this).siblings('.coupon-num').find('.js-coupon-num').val(),
                couponId = $(this).siblings('.coupon-id').val(),
                couponSource = $(this).siblings('.coupon-source').val();
            couponList.push({couponTotal: couponNum, id: couponId, couponSource: couponSource});
        }
    });
    ck.init();
    ck.regList.integerNotZero = function(val, v) {
        var reg = /^\d+$/;
        var msg = [
            "",
            "请输入大于0的整数"
        ];
        if ($.trim(val) == "") {
            return {msg: msg[0], result: true};
        }

        if (!reg.test(val) || val <= 0) {
            return {msg: '请输入大于0的整数', result: false};
        } else {
            return {msg: '', result: true};
        }
    };

    $(document)
        //参加/取消参加切换
        .on('click','.js-coupon-join',function(){
            var $this = $(this);
            var couponNum = $this.siblings('.coupon-num').find('.js-coupon-num').val(),
                couponId = $this.siblings('.coupon-id').val(),
                couponSource = $this.siblings('.coupon-source').val(),
                couponNumInput = $this.siblings('.coupon-num').find('.js-coupon-num'),
                receivedCouponCount = $this.siblings('.received-coupon-count').val();
            if($this.hasClass('yqx-btn-2')){
                if (!ck.check($this.parent('.coupon-operation'))) {
                    return;
                }
                if(receivedCouponCount != null && receivedCouponCount != '' && receivedCouponCount > couponNum){
                    dg.fail('发券已发放' + receivedCouponCount + '张，修改数量不能少于发放数量！');
                    return;
                }
                $this.removeClass('yqx-btn-2').addClass('yqx-btn-1').text('取消参加');
                couponNumInput.attr('disabled','true');
                couponList.push({couponTotal:couponNum,id:couponId,couponSource:couponSource});
            } else {
                couponNumInput.removeAttr('disabled');
                $this.removeClass('yqx-btn-1').addClass('yqx-btn-2').text('参加');
                for(var i=0;i<couponList.length;i++){
                    if(couponList[i].couponTotal == couponNum && couponList[i].id == couponId && couponList[i].couponSource == couponSource){
                        couponList.splice(i,1);
                    }
                }
            }
        })
        //卡券说明弹窗
        .on('click','.js-coupon-content',function(){
            var couponRuleDescription = $(this).find('.coupon-rule-description').html();
            var html = art('couponContentTpl', {content:couponRuleDescription});
            dg.open({
                area: ['500px', 'auto'],
                content: html
            });
        });

    module.moduleCouponDiscount = {
        getModuleVo: function(){
            var moduleVo = {
                uniqueCode: thisModule.find('[name="uniqueCode"]').val(),
                moduleType: thisModule.find('[name="moduleType"]').val(),
                moduleIndex: thisModule.find('[name="moduleIndex"]').val(),
                couponVoList: couponList
            };
            return moduleVo;
        },
        checkValid: function(){
            var submitItem = $('.coupon-item:has(".yqx-btn-1")',thisModule);
            if(!ck.check(submitItem)){
                return false;
            }
            return true;
        },
        isEmpty: function(){
            if($('.yqx-btn-1', thisModule).length){
                return false;
            }
            return true;
        }
    }
});