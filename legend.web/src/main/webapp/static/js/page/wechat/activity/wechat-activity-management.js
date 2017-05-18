/**
 * Created by zz on 2016/10/17.
 */
seajs.use(['art','date','check','dialog','select', 'ajax'],function(art,date,ck,dg,select,ajax){
    var couponIdArray = [],couponTypeArray = [];
    var arrayIndex = 0,
        currentPage = 1;
    init();
    dg.titleInit();

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

    $(document).on('mouseover','.js-coupon-list',function(){
        arrayIndex = $(this).data('index');
    });
    select.init({
        dom: '.js-coupon',
        url: BASE_PATH + '/shop/wechat/lottery/search',
        params: getCouponType,
        showKey: "id",
        showValue: "couponName",
        noDataCallback: function () {
            if(getCouponType().category == 1){
                dg.msg("您还没有卡券，点击新增优惠券去设置吧~", 1);
            } else {
                dg.msg("暂无可选保险优惠券",1);
            }
        },
        select:function(){
            var id = $(this).data('key'),
                formerId = $(this).parents('.yqx-select-options').siblings('input[name="couponId"]').val(),
                couponType = $(this).parents('.yqx-select-options').parents().find('input[name="couponType"]').val();
            var flag = 0;
            for(var i=0;i<couponIdArray.length;i++){
                if(couponIdArray[i].couponType == couponType){
                    if(couponIdArray[i].couponId == id){
                        if(id != formerId){
                            dg.warn('优惠券已选择，请选择其他优惠券');
                            $(this).parents('.form-item').find('input').val('');
                            for(var j=0;j<couponIdArray.length;j++){
                                if(couponIdArray[j].couponId == formerId){
                                    couponIdArray.splice(j,1);
                                }
                            }
                            flag = 1;
                            return false;
                        }
                    }
                }
            }
            if(flag != 1){
                for(var k=0;k<couponIdArray.length;k++){
                    if(couponIdArray[k].couponId == formerId){
                        couponIdArray.splice(k,1);
                    }
                }
                couponIdArray.push({couponId:id,couponType:getCouponType().category});
            }
        }
    });

    // 优惠券类型选择
    select.init({
        dom: '.js-coupon-type',
        showKey: "key",
        showValue: "value",
        selectedKey:'门店优惠券',
        data: [{
            key: '1',
            value: '门店优惠券'
        },{
            key: '2',
            value: '保险优惠券'
        }],
        callback:function(){
            var value = parseInt($(this).data('key'));
            var couponItem = $(this).parents('.js-coupon-list').find('.js-coupon');
            setCouponType(value);
            if(couponItem.val() != ''){
                couponItem.val('');
            }
            couponItem.siblings('.yqx-select-options').remove();
        }
    });

    //时间控制
    date.dpStartEnd({
        startSettings: {
            minDate:'%y-%M-%d',
            maxDate: '#F{$dp.$D(\'endDate\')}'
        },
        endSettings: {
            minDate: '#F{$dp.$D(\'startDate\') || \'%y-%M-%d %H:%m:%s\'}'
        }
    });

    $(document).on('click','.js-prize-number',function(){
        couponIdArray= [];
        tableInit();
    });

    //长期/自定义时间切换
    $(document).on('click','.js-period',function(){
        var date = $('#startDate,#endDate');
        $('.js-period:checked').val() == '2'?date.removeAttr('disabled'):date.val('').attr('disabled',true);
    });

    //查看中奖概率
    $(document).on('click','.js-view-probability',function(){
        if(!ck.check()){
            return;
        }

        calculateProbability();
    });

    //改变奖品值查看按钮改变
    $(document).on('change','.js-set-number,.js-participation',function(){
        var probabilityBtn = $('.js-view-probability');
        if(probabilityBtn.attr('disabled')){
            probabilityBtn.removeClass('yqx-btn-1').addClass('yqx-btn-3').removeAttr('disabled');
        }
    });

    //关闭抽奖
    $(document).on('click', '#toggle-true', function () {
        dg.confirm("关闭后抽奖活动会结束，确定要关闭？", function () {
            $.ajax({
                url:BASE_PATH + '/shop/wechat/lottery/close',
                type:'POST',
                dataType:'json',
                contentType:'application/json',
                success:function(result){
                    if(result && result.success){
                        toggleShow();
                    } else {
                        dg.warn("关闭抽奖活动失败，请稍后重试");
                    }
                }
            });
        });
    });

    //开启抽奖
    $(document).on('click', '#toggle-false', function () {
        toggleShow();
        $('.js-setting').find('input').not('input:radio').val("");
        $('input:radio:even').prop('checked', true);
        tableInit();
        $('.js-view-probability').removeClass('yqx-btn-1').addClass('yqx-btn-3').removeAttr('disabled');
        $('#startDate,#endDate').val('').attr('disabled',true);
        $('.js-share-times,.js-order-times,.js-appointment-times').prop('checked',false);
        $('.js-style').eq(0).prop('checked',true);
        couponIdArray = [];
    });

    // 高级配置展示切换
    $(document).on('click', '.js-toggle-icon', function () {
        var $this = $(this);
        if($this.hasClass('arrow-down')){
            $this.removeClass('arrow-down').addClass('arrow-up');
            $('.advanced-setting-box').show();
        } else if($this.hasClass('arrow-up')) {
            $this.removeClass('arrow-up').addClass('arrow-down');
            $('.advanced-setting-box').hide();
        }
    });

    //发布
    $(document).on('click','.js-release',function(){
        if(!ck.check()){
            return;
        }
        if($('#actId').val()!=''){
            var html = art('updateTpl', {});
            dg.open({
                area: ['400px', '200px'],
                content: html
            });
        }else{
            pub();
        }
    });

    $(document).on('click','.js-confirm',function(){
        pub();
    });
    function pub() {
        calculateProbability();
        if(calculateProbability() == 0){
            return;
        }

        var activityLotteryPrizeVOList = [];
        var prizeOrderIndex = 1;

        $('.table-tr').each(function(){
            var prizeName = $(this).find('.js-coupon').val();
            var couponId = $(this).find("input[ name ='couponId']").val();
            var prizeNum = $(this).find('.js-set-number').val();
            var probabilityOfPrize = $(this).find('.js-prize-probability').val();
            var prizeType = $(this).find("input[name='couponType']").val();

            activityLotteryPrizeVOList.push({prizeName:prizeName,couponId:couponId,prizeNum:prizeNum,prizeType:prizeType,probabilityOfPrize:probabilityOfPrize,prizeOrderIndex:prizeOrderIndex++});
        });

        var data = {
            lotteryTimeType:parseInt($('.js-period:checked').val()),
            lotteryStartTimeStr:$('#startDate').val(),
            lotteryEndTimeStr:$('#endDate').val(),
            timesSetting:parseInt($('.js-times-number:checked').val()),
            prizeNumType:parseInt($('.js-prize-number:checked').val()),
            probabilityOfThanks:$('.js-probability').val(),
            switchStatus:1,
            pubStatus:1,
            shareSwitch:$('.js-share-times').is(':checked')?1:0,
            orderFinishSwitch:$('.js-order-times').is(':checked')?1:0,
            appointSuccessSwitch:$('.js-appointment-times').is(':checked')?1:0,
            lotteryStyle:parseInt($('.js-style:checked').val()),
            joinAgainSwitch:parseInt($('input[name="newLottery"]:checked').val()),
            activityLotteryPrizeVOList:activityLotteryPrizeVOList
        };
        if(data.lotteryTimeType==2&&(data.lotteryEndTimeStr==''||data.lotteryStartTimeStr=='')) {
            dg.warn('请填写活动开始时间和结束时间');
            return;
        }

        $.ajax({
            url:BASE_PATH + '/shop/wechat/lottery/save',
            data:JSON.stringify(data),
            type:'POST',
            dataType:'json',
            contentType:'application/json',
            success:function(result){
                if(result && result.success){
                    dg.success("发布成功");
                    window.location.reload();
                } else {
                    dg.warn(result.message);
                }


            }
        });
    }
    $(document).on('click','.js-cancel',function(){
        dg.close();
    });

    //预览
    $(document).on('click','.js-preview',function(){
        if(!ck.check()){
            return;
        }

        preview();
    });

    // 数据统计按钮
    $(document).on('click','.js-data-statistic',function(){
        $.ajax({
            url:BASE_PATH + '/shop/wechat/op/qry-lottery-statistic',
            success:function(result){
                if(result&&result.success){
                    if(result.data){
                        var html = art('reportTpl', {data:result.data});
                        dg.open({
                            area: ['650px', '480px'],
                            content: html
                        });
                    } else {
                        dg.fail('暂无数据');
                    }

                } else {
                    dg.fail(result.message);
                }
            }
        })
    });

    // 数据统计弹窗查看按钮
    $(document).on('click','.js-customer-detail',function(){
        var $this = $(this),
            lotteryPrizeId = $this.find('.lottery-prize-id').val();
        if(lotteryPrizeId==undefined)
            lotteryPrizeId=0;
        if($this.hasClass('no-count')){
            return;
        }
        currentPage=1;
        getCustomerDetail(lotteryPrizeId);
    });

    // 查看上一页
    $(document).off('click', '.js-pre-page').on('click', '.js-pre-page', function(){
        var prizeId = $(this).parents().siblings('.lottery-prize-id').val();
        if($(this).hasClass('disable')){
            return;
        }
        --currentPage;
        getCustomerDetail(prizeId);
    });

    // 查看下一页
    $(document).off('click', '.js-next-page').on('click', '.js-next-page', function(){
        var prizeId = $(this).parents().siblings('.lottery-prize-id').val();
        if($(this).hasClass('disable')){
            return;
        }
        ++currentPage;
        getCustomerDetail(prizeId);
    });

    // 查看样式
    $(document).on('click','.js-view-style',function(){
        var style = $(this).siblings('.js-style').val(),
            styleImg;
        if(style == '0') {
            styleImg = 'static/img/page/wechat/lottery-style1.png';
        } else if (style == '1') {
            styleImg = 'static/img/page/wechat/lottery-style2.png';
        } else {
            styleImg = 'static/img/page/wechat/lottery-style3.png';
        }
        var html = art('LotteryStyleTpl',{data:styleImg});
        dg.open({
            area: ['640px','700px'],
            content:html
        })
    });

    //页面初始化
    function init(){
        ck.init();
        tableInit();
        initOpeningLottery();
    }

    //表格初始化
    function tableInit(){
        var prizeNumber = parseInt($('.js-prize-number:checked').data('num'));
        var data = {
            number:prizeNumber
        };
        document.getElementById('prizeTable').innerHTML = art('tableTpl', data);
    }

    function initOpeningLottery(){
        $.ajax({
            url:BASE_PATH + '/shop/wechat/lottery/get',
            type:'GET',
            dataType:'json',
            contentType:'application/json',
            success:function(result){
                if(result && result.success){
                    if(result.data){
                        var tableTr,
                            date = $('#startDate,#endDate');
                        toggleShow();
                        if(result.data.lotteryTimeType == 2){
                            $('.js-period').eq(1).attr('checked','checked');
                            date.removeAttr('disabled');
                            $('#startDate').val(result.data.lotteryStartTimeStr);
                            $('#endDate').val(result.data.lotteryEndTimeStr);
                        }
                        if(result.data.prizeNumType == 2){
                            $('.js-prize-number').eq(1).attr('checked','checked');
                            tableInit();
                        }
                        if(result.data.timesSetting == 2){
                            $('.js-times-number').eq(1).attr('checked','checked');
                        }
                        tableTr = $('.table-tr');

                        $('.js-probability').val(result.data.probabilityOfThanks);
                        $('#lotteryId').val(result.data.id);
                        for(var i=0;i<result.data.activityLotteryPrizeVOList.length;i++){
                            tableTr.eq(i).find('input[name="couponType"]').val(result.data.activityLotteryPrizeVOList[i].prizeType);
                            if(result.data.activityLotteryPrizeVOList[i].prizeType == 1){
                                tableTr.eq(i).find('.js-coupon-type').val('门店优惠券');
                            } else {
                                tableTr.eq(i).find('.js-coupon-type').val('保险优惠券');

                            }
                            tableTr.eq(i).find('.js-coupon').val(result.data.activityLotteryPrizeVOList[i].prizeName).
                                siblings('input[name="couponId"]').val(result.data.activityLotteryPrizeVOList[i].couponId);
                            tableTr.eq(i).find('.js-set-number').val(result.data.activityLotteryPrizeVOList[i].prizeNum);
                            couponIdArray.push({couponType:tableTr.eq(i).find('input[name="couponType"]').val(),couponId:tableTr.eq(i).find('input[name="couponId"]').val()});
                        }
                        if(result.data.shareSwitch == 1){
                            $('.js-share-times').attr('checked','checked');
                        }
                        if(result.data.orderFinishSwitch == 1){
                            $('.js-order-times').attr('checked','checked');
                        }
                        if(result.data.appointSuccessSwitch == 1){
                            $('.js-appointment-times').attr('checked','checked');
                        }
                        $('.js-style').eq(result.data.lotteryStyle).attr('checked','checked');
                        $("input[name='couponType']").each(function(){
                            couponTypeArray.push(parseInt($(this).val()));
                        });
                        $('#actId').val(result.data.id);
                    }else{
                        $('.js-preview').hide();
                    }
                }else{

                }
            }
        });
    }

    function toggleShow() {
        $('.js-setting, .toggle').toggle();
        $('#intro').toggleClass('right-box');
    }

    function calculateProbability() {
        var num = [],sum=0,
            prizeNum = $('.js-set-number'),
            prizeProbability = $('.js-prize-probability'),
            participationProbability = parseInt($('.js-probability').val());
        $(this).removeClass('yqx-btn-3').addClass('yqx-btn-1').attr('disabled','true');
        for(var i=0;i<prizeNum.length;i++){
            num.push(parseInt(prizeNum.eq(i).val()));
            sum += parseInt(prizeNum.eq(i).val());
        }
        if(sum != 0){
            for(var j=0;j<prizeNum.length;j++){
                if(num[j] == 0){
                    prizeProbability.eq(j).val('0.00');
                } else {
                    prizeProbability.eq(j).val(((100-participationProbability)*num[j]/sum).toFixed(2));
                }
            }
        } else {
            dg.warn('奖品数量不能都为0');
        }
        return sum;
    }

    function preview(){
        var prizeNameStr = '';
        var previewUrl = $('#lotteryPreviewUrl').val();
        var shopId = $('#shopId').val();
        var html = art('qrcodeTpl', {url: previewUrl});

        $('.table-tr').each(function(){
            var prizeName = $(this).find('.js-coupon').val();

            prizeNameStr += (prizeName + ";");
        });

        previewUrl = previewUrl + "?shopId=" + shopId + "&prizeNameStr=" + encodeURI(prizeNameStr);

        var myDialog = dg.open({
            area: ['340px', '410px'],
            content: html,
            end: function(){
            }
        });
        //Render in canvas
        $("#qrcodeView div").qrcode({
            width: 270,
            height: 270,
            text: previewUrl
        });
    }

    function getCouponType() {
        return {
            couponType:1,
            category:couponTypeArray[arrayIndex]
        };
    }

    function setCouponType(key) {
        couponTypeArray[arrayIndex] = key;
    }

    function getCustomerDetail(lotteryPrizeId){
        $.ajax({
            url:BASE_PATH + '/shop/wechat/op/qry-lottery-users?lotteryPrizeId=' + lotteryPrizeId + '&page=' + currentPage,
            success:function(result){
                if(result&&result.success){
                    var html = art('detailTpl', {data:result.data,lotteryPrizeId:lotteryPrizeId});
                    $('.customerDetail').html(html);
                    if(currentPage == 1){
                        $('.js-pre-page').addClass('disable');
                    }
                    if(result.data.lastPage){
                        $('.js-next-page').addClass('disable');
                    }
                } else {
                    dg.fail(result.errorMsg || '查询失败');
                }
            }
        })
    }
});