/**
 * Created by twg on 15/6/4.
 */
function appendCount(){
    var startTime = $("#d4311").val();
    var endTime = $("#d4312").val();
    seajs.use(["ajax","artTemplate"],function(ajax,art){
        ajax.post({
            url: BASE_PATH + "/shop/stats/stats_repair_score/get_stats_count",
            data: {"search_startTime":startTime,"search_endTime":endTime},
            success: function(json){
                console.log(json);
                var trHtml = [
                    '<tr>',
                    '<td colspan="2">合计</td>',
                    '<td><%= result.hours%></td>',
                    '<td><%= result.payAmount%></td>',
                    '<td><%= result.paidAmount%></td>',
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