/**
 * zmx 2016-05-31
 * 接单JS
 */

$(function(){
    var $doc = $(document);

    seajs.use([
        'select',
        'dialog'
    ],function(st,dg) {
        dg.titleInit();
        //接车人下拉列表
        st.init({
            dom: '.js-trustee',
            url: BASE_PATH + '/shop/manager/get_manager',
            showKey: "id",
            showValue: "name",
             callback:function(showKey,showValue){
                $('.receiver').val(showKey);
                $('.receiveName').val(showValue);
            }
        });


        //接单按钮
        $doc.on('click','.js-accept-order',function(){
            var proxyId = $(".proxyId").val();
            var receiver = $(".receiver").val();
            var receiveName = $(".receiveName").val();
            var remark = $(".remark").val();
            $.ajax({
                type: 'get',
                url: BASE_PATH + "/proxy/acceptProxy",
                data: {
                    proxyOrderId: proxyId,
                    receiver:receiver,
                    receiveName:receiveName,
                    remark:remark
                },
                success: function (result) {
                    if (result.success) {
                        dg.success("操作成功", function () {
                            window.location.href = BASE_PATH + "/proxy/detail?proxyOrderId="+ proxyId;
                        });
                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

    });

    //返回按钮
    $doc.on('click','.js-goback',function(){
        util.goBack();
    });



});
