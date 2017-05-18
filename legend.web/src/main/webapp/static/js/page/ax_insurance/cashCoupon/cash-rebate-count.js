/**
 * Created by huage on 2017/3/11.
 */

$(function () {
    seajs.use('dialog',function (dg) {
        innit_click(dg);
        innit_input(dg);
    });

    //点击事件
    function innit_click(dg) {
        var doc = $(document);
        doc
            .on('click',function () {
                $('.selectBox').siblings('ul').addClass('dis-none').end().siblings('i').removeClass('s_down').addClass('s_up');
        })
            .on('click','.selectBox',function () {
            var $this = $(this),
                $sibling_icon = $this.siblings('i');
            if($sibling_icon.hasClass('s_up')){     //点击下拉框
                $this.siblings('ul').removeClass('dis-none').end().siblings('i').addClass('s_down').removeClass('s_up');
                return false;
            }
            $this.siblings('ul').addClass('dis-none').end().siblings('i').removeClass('s_down').addClass('s_up');
        })
            .on('click','.select-ul li',function () {
                var $this = $(this),
                    this_text = $this.text(),
                    $parent = $this.parent('.select-ul');
                $parent.addClass('dis-none').siblings('input.selectBox').val(this_text)
                    .siblings('i').removeClass('s_down').addClass('s_up');
                if($parent.siblings('.rebate-selectBox')){
                    var compulsory = $this.data('compulsory'),
                        business = $this.data('business');
                    $parent.siblings('.rebate-selectBox').data('business',business).data('compulsory',compulsory);
                }
            })
            .on('click','.rebate-checkbox',function () {
                var $this = $(this),
                    this_prop = $this.prop('checked'),
                    val = $this.val();
                if(this_prop){
                    $this.parent('.checkbox-li').siblings('.checkbox-li').find('.rebate-checkbox').prop('checked',false);
                    //用val来控制底下展示对应的。注意！
                    $('.rebate-li-div').eq(val-1).removeClass('dis-none').siblings('.rebate-li-div').addClass('dis-none');
                }else{
                    var len = $('.rebate-checkbox:checked').length;
                    if(len <= 0){
                        return false;
                    }
                }
            })
            .on('click','.count-button',function () {
                var index = $('.rebate-checkbox:checked').val(),
                    face_value = $('.face_value').data('val'),
                    compulsoryFee,businessFee;
                if(index==1){
                        compulsoryFee = $('#compulsoryFee').val();
                        businessFee = $('#businessFee').val();
                }else{
                    var rebate_select =  $('.rebate-selectBox');
                    compulsoryFee = rebate_select.data('compulsory');
                    businessFee = rebate_select.data('business');
                }
                if(!compulsoryFee && !businessFee){
                    dg.warn('请输入保费');
                    return false;
                }
                count(compulsoryFee,businessFee,face_value);
            })
    }

    //输入事件
    function innit_input(dg) {
        $('.fee-box').keyup(function () {
            var $this_val = $(this).val();
            if( /[^\d.]/g.test($this_val)){
                dg.warn('请输入正确的保费格式');
                var val = $this_val.replace(/[^\d.]/g,'');
                $(this).val(val)
            }
        })
    }

    // 计算保费
    function count(compulsoryFee,businessFee,face_value) {
        var forcibleRebateRatio = $('#forcibleRebateRatio').val();
        var commercialRebateRatio = $('#commercialRebateRatio').val();


        var all= (compulsoryFee*forcibleRebateRatio/100+(businessFee-face_value)*commercialRebateRatio/100+face_value).toFixed(2);
        var business_rate = Math.ceil((((businessFee-face_value)*commercialRebateRatio/100+face_value)/businessFee)*100);


        if(!businessFee || businessFee == 0){
            business_rate = 0;
        }
        var attr = '<p>按以上保费条件，您若用保单兑换现金劵，将总共获得返利 ：</p>'+
                    '<p class="red_color">'+all+'元，其中商业险返利比例高达'+business_rate+'%</p>'
        $('.count-result-box').html(attr);
    }

});
