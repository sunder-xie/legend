/**
 * Created by twg on 15/6/4.
 */
function appendCount(){
    var startTime = $("#d4311").val();
    var endTime = $("#d4312").val();
    seajs.use(["ajax","artTemplate"],function(ajax,art){
        ajax.post({
            url: BASE_PATH + "/shop/stats/stats_repair_car/get_stats_count",
            data: {"search_startTime":startTime,"search_endTime":endTime},
            success: function(json){
                console.log(json);
                var trHtml = [
                    '<tr>',
                    '<%if(result.settlementPostscript == "合计"){%>',
                    '<td colspan="5">合计</td>',
                    '<%}%>',
                    '<%for(var i=0;i<result.managerServiceList.length;i++){%>',
                    '<%var item=result.managerServiceList[i];%>',
                    '<td><%= item.serviceAmount%></td>',
                    '<%}%>',
                    '<td><%= result.goodsAmount%></td>',
                    '<td><%= result.goodsManagerAmount%></td>',
                    '<td><%= result.faxAmount%></td>',
                    '<td><%= result.otherAmount%></td>',
                    '<td><%= result.totalAmount%></td>',
                    '<td><%= result.payAmount%></td>',
                    '<td><%= result.discountAmount%></td>',
                    '<td><%= result.invoiceName%></td>',
                    '<td><%= result.inventoryAmount%></td>',
                    '<td><%= result.paymentName%></td>',
                    '</tr>'
                ].join("");
                var temp = art.compile(trHtml);
                var html = temp({"result":json});
                console.log(html);
                $("tbody",".stats_list").append(html);
            }
        });
    });
}