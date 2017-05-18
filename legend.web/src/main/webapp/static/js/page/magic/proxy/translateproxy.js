/**
 * zmx 2016-05-12
 * 新建委托单JS
 */

$(function(){
    var $doc = $(document);

    seajs.use([
        'select',
        'art',
        'ajax',
        'formData',
        'dialog'
    ],function(st,at,ax,fd,dg){

        //受托方下拉列表
        st.init({
            dom: '.js-trustee',
            url: BASE_PATH + '/share/shareTags',
            showKey: "shopId",
            showValue: "shopName",
            callback: function(showKey) {
                $(".proxyShopId").val(showKey);
                var orderId = $(".orderId").val();
                $.ajax({
                    url: BASE_PATH + '/share/shopInfo',
                    data: {
                        id: showKey,
                        orderId:orderId
                    },
                    success: function (result) {
                        var html = at('wtTpl',{json:result});
                        $('#wtCon').html(html);
                        $("input[name='proxyAmount']").each(function(i,e){
                            //$(this).val(result.data.proxyAmounts[i]);
                            $(this).val(result.data.orderServicesVos[i].proxyAmount);
                            $(this).parents("tr").find("input[name='serviceHour']").val(result.data.orderServicesVos[i].serviceHour);
                            $(this).parents("tr").find("input[name='sharePrice']").val(result.data.orderServicesVos[i].sharePrice);
                            total();
                        });
                    }
                });
            }
        });

        st.init({
            dom: '.js-hour',
            callback: function(key) {
                var proxyShopId = $(".proxyShopId").val();
                if(proxyShopId == ''){
                    dg.warn("请选择受托方");
                    return;
                }
                //修改工时费时改变价格
                var serviceHour = $(this).parents("tr").find("input[name='serviceHour']").val();
                var proxyAmount = $(this).parents("tr").find("input[name='proxyAmount']");
                $.ajax({
                    url:BASE_PATH + '/share/modifyPrice',
                    data:{
                        id:$(".proxyShopId").val(),
                        sharePrice: key,
                        serviceHour: serviceHour
                    },
                    success:function(result){
                        if (result.success) {
                            proxyAmount.val(result.data);
                            total();
                        } else {
                            dg.fail(result.message);
                        }
                    }
                });
            }
        });


        //计算价格
        function total(){
            var proxyCount = 0,
                proxyAmount = 0,
                serverCount = 0,
                serverAmount =0,
                numConut = 0;

            $('input[type="checkbox"]:checked').not('.js-selectall').each(function() {
                var thisProxyPrice = $(this).parents("tr").find("input[name='proxyAmount']").val(),
                    thisServerPrice = $(this).parents("tr").find(".servicePrice").text();

                    //委托单合计
                    proxyAmount = Number(thisProxyPrice);
                    proxyCount += proxyAmount;
                    //订单合计
                    serverAmount = Number(thisServerPrice);
                    serverCount += serverAmount;
                    //数量合计
                    numConut++;
            });

            $(".js-proxytotal").text(proxyCount.toFixed(2));
            $(".js-ordertotal").text(serverCount.toFixed(2));
            $(".js-numtotal").text(numConut);
        }
        total();

        //被选择时改变价格
        $doc.on('change', 'input[type="checkbox"]', function() {
            var proxyShopId = $(".proxyShopId").val();
            if(proxyShopId == ''){
                dg.warn("请选择受托方");
                return;
            }
            total();
        });
        //修改委托金额时改变价格
        $doc.on("blur","input[name='proxyAmount']",function(){
            total();
        });

        //修改工时改价格
        $doc.on("blur","input[name='serviceHour']",function(){
            var proxyShopId = $(".proxyShopId").val();
            if(proxyShopId == ''){
                dg.warn("请选择受托方");
                return;
            }
            var serviceHour = $(this).val();
            var sharePrice = $(this).parents("tr").find("input[name='sharePrice']").val();
            var proxyAmount = $(this).parents("tr").find("input[name='proxyAmount']");
            $.ajax({
                url:BASE_PATH + '/share/modifyPrice',
                data:{
                    id:$(".proxyShopId").val(),
                    serviceHour:serviceHour,
                    sharePrice:sharePrice
                },
                success:function(result){
                    if (result.success) {
                        proxyAmount.val(result.data);
                        total();
                    } else {
                        dg.fail(result.message);
                    }
                }
            });
        });



        //委托按钮
        $doc.on('click','.js-wt-btn',function(){
            var num = $(".serviceNum").text();
            var proxyShopId = $(".proxyShopId").val();
            if(num < 1){
                dg.warn("请选择委托服务");
                return;
            }
            if(proxyShopId == ""){
                dg.warn("请选择受托方");
                return;
            }

            //表单数据
            var FormData = {
                proxyShopId: $(".proxyShopId").val(),
                proxyShopName:$(".proxyShopName").val(),
                carLicense : $(".carLicense").text().trim(),
                carInfo: $(".carInfo").text(),
                carYearPower: $(".carYearPower").text(),
                carColor : $(".carColor").text(),
                expectTimeStr : $(".expectTime").text(),
                vin : $(".vin").text(),
                remark : $(".remark").text(),
                shareName : $(".shareName").text(),
                shareMobile : $(".shareMobile").text(),
                shareAddr : $(".shareAddr").text(),
                shopId:$(".shopId").val(),
                orderId:$(".orderId").val(),
                serviceSa:$(".receiverName").val(),
                customerId:$(".customerId").val(),
                customerCarId:$(".customerCarId").val(),
                shopName:$(".shopName").val(),
                contactName:$(".contactName").val(),
                contactMobile:$(".contactMobile").val(),
                proxyAddress:$(".proxyAddress").val(),
                amount:$(".amount").text(),
                proxyAmount:$(".totalProxyAmount").text(),
                serviceNum:$(".serviceNum").text(),
                serviceSaId:$(".serviceSaId").val()
            };

            //表格数据
            var TableData = [];
            $('input[type="checkbox"]:checked').not('.js-selectall').each(function() {
                var $scope = $(this).parents('tr');
                TableData.push({
                    serviceType: $(".serviceType", $scope).text(),
                    serviceName: $(".serviceName", $scope).text(),
                    serviceHour: $(".serviceHour", $scope).val(),
                    sharePrice: $(".sharePrice", $scope).val(),
                    serviceAmount: $(".servicePrice", $scope).text(),
                    proxyAmount: $(".proxyAmount", $scope).val(),
                    serviceNote: $(".serviceNote", $scope).val(),
                    serviceSn: $(".serviceSn", $scope).val(),
                    serviceId: $(".serviceId", $scope).val(),
                    shopId:$(".shopId").val(),
                    surfaceNum:$(".surfaceNum",$scope).val()
                });
            });

            FormData['proxyServicesInfoList'] = TableData;

            $.ajax({
                type: 'post',
                url: BASE_PATH + '/proxy/addProxy',
                data: JSON.stringify(FormData),
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        dg.success('委托成功', function () {
                            window.location.href = BASE_PATH + "/proxy/detail?proxyOrderId="+result.data;
                        });

                    } else {
                        dg.fail(result.errorMsg);
                    }
                }
            });
        });

        //更新委托单
        $doc.on('click','.js-bc-btn',function(){
            var num = $(".serviceNum").text();
            if(num < 1){
                dg.warn("请选择委托服务");
                return;
            }
            //表单数据
            var FormData = {
                id:$(".proxyId").val(),
                amount:$(".amount").text(),
                proxyAmount:$(".totalProxyAmount").text(),
                serviceNum:$(".serviceNum").text(),
                proxySn:$(".proxySn").val()
            };
            //表格数据
            var TableData = [];
            $('input[type="checkbox"]:checked').not('.js-selectall').each(function() {
                var $scope = $(this).parents('tr');
                TableData.push({
                    id:$(".proxyServiceId",$scope).val(),
                    serviceHour: $(".serviceHour", $scope).val(),
                    sharePrice: $(".sharePrice", $scope).val(),
                    serviceAmount: $(".servicePrice", $scope).text(),
                    proxyAmount: $(".proxyAmount", $scope).val(),
                    serviceNote: $(".serviceNote", $scope).val(),
                    serviceType: $(".serviceType", $scope).text(),
                    serviceName: $(".serviceName", $scope).text(),
                    serviceSn: $(".serviceSn", $scope).val(),
                    serviceId: $(".serviceId", $scope).val(),
                    shopId: $(".shopId").val(),
                    surfaceNum: $(".surfaceNum", $scope).val()
                });
            });

            FormData['proxyServicesInfoList'] = TableData;
            $.ajax({
                type: 'post',
                url: BASE_PATH + '/proxy/updateProxy',
                data: JSON.stringify(FormData),
                contentType: 'application/json',
                dataType: 'json',
                success: function (result) {
                    if (result.success) {
                        dg.success('更新委托单成功', function () {
                            window.location.href = BASE_PATH + "/proxy/detail?proxyOrderId="+result.data;
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

    //全选按钮
    $doc.on("change", ".js-selectall", function () {
        var trAll = $(this);

        if (!trAll.is(':checked')) {
            //全取消
            $("input[type='checkbox']").prop("checked", false);
            $("input[type='checkbox']").parents('tr').removeClass('hover')
        } else {
            //全选
            $("input[type='checkbox']").prop("checked", true);
            $("input[type='checkbox']").parents('tr').addClass('hover')
            };
    });
    $doc.on("click",".select-list",function(){
        var $this = $(this);
        if( $this.is(':checked') ){
            $this.parents('tr').addClass('hover');
        }else{
            $('.js-selectall').prop("checked", false);
            $this.parents('tr').removeClass('hover');
        }


    });



});
