/**
 * Created by ZhuangQianQian on 16/8/22.
 */
$(function(){
    var $doc = $(document);
    seajs.use([
        'dialog'
    ],function(dg){
        $doc.on("mouseover",".insurance-money",function(){
            $(this).next().show();
        }).on("mouseout",".insurance-money",function(){
            $(this).next().hide();
        }).on("click",".Z-reCal",function(){
            var token = new Date().getTime();
            var data = {
                basicId : $(this).data('basicid'),
                token : token
            };
            $.ajax({
                url : BASE_PATH+'/insurance/anxin/flow/repeatCalculate',
                data : data,
                type : 'post',
                success : function(json){
                    if(json.success){
                        sessionStorage.setItem('result_data',JSON.stringify(json));
                        window.location.href = BASE_PATH+"/insurance/anxin/flow/confirm-info";
                    }else{
                        dg.fail(json.errorMsg);
                    }
                }
            })
            //window.location.href=BASE_PATH+"/insurance/anxin/api/getCategoryInfo";
        }).on("click",".Z-back",function(){
            window.history.go(-1);
        }).on('click','.rendImg',function () {
            var $this = $(this),
                sn = $this.data('sn'),
                url = BASE_PATH+'/insurance/anxin/flow/insurance-result?orderSn='+sn;
            payInfo(url,sn);
        }).on('click','.goPay',function () {
            var $this = $(this),
                sn = $this.data('sn'),
                url = BASE_PATH+'/insurance/anxin/pay/choose?sn='+sn;
            payInfo(url,sn);
        });

        //点击上传照片或者去缴费
        function payInfo(url,sn) {
            $.ajax({
                url:BASE_PATH+'/insurance/anxin/flow/pay-info',
                data:{
                    orderSn:sn
                },
                success:function (result) {
                    if(result.success){
                        var time = result.data.beforeTime,
                            nowTime = new Date().getTime(),
                            sy = result.data.commercialInsuredFee,
                            jq = result.data.forcibleInsuredFee,
                            shouldPay = result.data.insuredTotalFee;
                        if(nowTime>time){
                            dg.confirm('抱歉，由于当前保单的缴费时间晚于保单的生效时间，无法进行支付，请重新投保！',function () {

                            },function () {

                            },['我知道了']);
                            return;
                        }
                        var attr = '<p>以下为待支付信息，请确认是否继续投保:</p>';
                        if(sy){
                            attr+= '<p>商业险:<span class="color-word">'+sy.toFixed(2)+'</span></p>'
                        }
                        if(jq){
                            attr+= '<p>交强险(含车船税):<span class="color-word">'+jq.toFixed(2)+'</span></p>'
                        }
                        attr+='<p>应付金额:<span class="color-word">'+shouldPay.toFixed(2)+'</span></p>';
                        dg.confirm(attr,function () {
                            window.location.href =url;
                        },function () {

                        },['确认']);
                    }else{
                        dg.fail('获取支付信息失败');
                    }
                }
            });
        }
    });
});
