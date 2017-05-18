$(function(){
    $("#btnST").click(function() {
        mkUtil.ddCheck();
        mkUtil.loadTable({});
    });
    $("li[tab="+$("#tab").val()).addClass("tabSel");
    $(".mk-sc-tab li").click(function(e){
        $(".mk-sc-tab li").removeClass("tab-sel")
        $(e.target).addClass("tab-sel");
        $("#pageNum").val(1);
    })

    $(".mk-btn-sms").click(function(){
        var url = BASE_PATH + "/shop/marketing/sms/new/customer/ng";
        var  param = "?";
        var data = mkUtil.getParam();
        for(var d in data){
            param += d + "="+ data[d] + "&";
        }
        if(param == "?"){
            param = "";
        } else {
            param = param.substr(0, param.length - 1);
        }
        window.location = url + param;
    });

})

var mkUtil = {
}
mkUtil.ddCheck = function() {
    sTime = $("#sTime").val();
    eTime = $("#eTime").val();
}

mkUtil.loadTable = function(){

    var data = mkUtil.getParam();

    taoqi.loading();
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: BASE_PATH + '/marketing/list',
        data:data,
        success: function (data) {
            taoqi.close();
            var htmlText = "";
            if (data.success) {
                var arr = data.data.content;
                for(var c in arr){
                    if(c %2 == 1){
                        htmlText += "<tr class='mk_dt_odd'>";
                    } else {
                        htmlText += "<tr>";
                    }
                    var cc = arr[c];
                    $(".mk-dt-head th").each(function(i, e){
                        var dd = cc[$(e).attr("data-opt")];
                        htmlText += "<td>";
                        if(dd != undefined){
                            htmlText += dd;
                        } else {
                            htmlText += '-';
                        }
                        htmlText += "</td>";
                    });
                    htmlText += "</tr>";
                }
                $("#ddFill").html(htmlText);
                $.paging({
                    dom : $(".qxy_page"),
                    itemSize : data.data.totalElements,
                    pageCount : data.data.totalPages,
                    current : data.data.number + 1,
                    backFn : function(e){
                        $("#pageNum").val(e);
                        mkUtil.loadTable();
                    }
                });
            } else {
                taoqi.failure("获取待回访客户列表异常，请稍后再试。");
                return;
            }

        },
        failure: function (data){

        }
    });
}

mkUtil.getParam = function(){
    //过滤参数初始化
    var data = {};
    var sp = "search_";
    data["page"] = $("#pageNum").val();

    /*----获取统计时间---*/
    var sTime = $("#sTime").val();
    if(sTime){
        data[sp+"sTime"] = sTime;
    }
    var eTime = $("#eTime").val();
    if(eTime){
        data[sp+"eTime"] = eTime;
    }

    /*----消费金额----*/
    var minAmount = $("#minAmount").val();
    if(minAmount){
        data[sp+"minAmount"] = minAmount;
    }
    var maxAmount = $("#maxAmount").val();
    if(maxAmount){
        data[sp+"maxAmount"] = maxAmount;
    }

    /*----充值金额----*/
    var minCashAmount = $("#minCashAmount").val();
    if(minCashAmount){
        data[sp+"minCashAmount"] = minCashAmount;
    }
    var maxCashAmount = $("#maxCashAmount").val();
    if(maxCashAmount){
        data[sp+"maxCashAmount"] = maxCashAmount;
    }

    /*---单车产值---*/
    var minCarValue = $("#minCarValue").val();
    if(minCarValue){
        data[sp+"minCarValue"] = minCarValue;
    }
    var maxCarValue = $("#maxCarValue").val();
    if(maxCarValue){
        data[sp+"maxCarValue"] = maxCarValue;
    }

    /*------来店消费次数&服务类型----*/
    var consumeConditon = $("select[id='consumeConditon']").find("option:selected").val();
    if(consumeConditon){
        data[sp+"consumeConditon"] = consumeConditon;
    }

    var consumeCount = $("#consumeCount").val();
    if(consumeCount){
        data[sp+"consumeCount"] = consumeCount;
    }
    var serviceType = $("li[serviceType].tab-sel").attr("serviceType");
    if(serviceType){
        data[sp+"serviceType"] = serviceType;
    }

    /*----未到店时间---*/
    var minNotConsumeDay = $("#minNotConsumeDay").val();
    if(minNotConsumeDay){
        data[sp+"minNotConsumeDay"] = minNotConsumeDay;
    }
    var maxNotConsumeDay = $("#maxNotConsumeDay").val();
    if(maxNotConsumeDay){
        data[sp+"maxNotConsumeDay"] = maxNotConsumeDay;
    }

    /*---行驶里程&购买年限----*/
    var minMileage = $("#minMileage").val();
    if(minMileage){
        data[sp+"minMileage"] = minMileage;
    }
    var maxMileage = $("#maxMileage").val();
    if(maxMileage){
        data[sp+"maxMileage"] = maxMileage;
    }

    var minbuyYear = $("#minbuyYear").val();
    if(minbuyYear){
        data[sp+"minbuyYear"] = minbuyYear;
    }
    var maxbuyYear = $("#maxbuyYear").val();
    if(maxbuyYear){
        data[sp+"maxbuyYear"] = maxbuyYear;
    }

    /*----车辆类型&车价-----*/
    var carType = $("select[id='carType']").find("option:selected").text();
    if(carType && "全部" != carType){
        data[sp+"carType"] = carType;
    }
    var carPrice = $("li[carPrice].tab-sel").text();
    if(carPrice){
        data[sp+"carPrice"] = carPrice;
    }
    return data;
}
