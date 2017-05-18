<#--
   zmx  2016/6/21
   历史工单模板
 -->
<style>
    .max-text{ max-width: 150px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;}
</style>

<script type="text/html" id="historyTableTpl">
    <div class="dialog">
        <div class="dialog-title">挂账工单</div>
        <div class="dialog-con">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>工单编号</th>
                    <th>开单日期</th>
                    <th>工单类型</th>
                    <th>服务顾问</th>
                    <th class="text-r">总计</th>
                    <th class="text-r">应收金额</th>
                    <th class="text-r">实收金额</th>
                    <th class="text-r">挂账金额</th>
                    <th>备注</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data){%>
                <%for(var i=0;i<json.data.length;i++){%>
                <%var item = json.data[i];%>
                <tr class="history-order js-linkToDetail" data-content="<%=item.id%>">
                    <td><div class="max-text js-show-tips"><%=item.orderSn%></div></td>
                    <td><div class="max-text js-show-tips"><%=item.gmtCreateStr %></div></td>
                    <td><%=item.orderTagName%></td>
                    <td><div class="max-text js-show-tips"><%=item.receiverName%></div></td>
                    <td class="money-font text-r">&yen;<%=item.orderAmount%></td>
                    <td class="money-font text-r">&yen;<%=item.payAmount%></td>
                    <td class="money-font text-r">&yen;<%=item.payedAmount%></td>
                    <td class="money-font text-r">&yen;<%=item.signAmount%></td>
                    <td><div class="max-text js-show-tips"><%=item.postscript%></div></td>
                </tr>
                <%}}%>
                </tbody>
            </table>
        </div>
    </div>
</script>

<script>
    $(function(){
        var $doc =$(document);
        seajs.use([
           'art',
           'dialog'
        ],function(at,dg){
            //历史挂账工单 弹框
            $doc.on('click','.js-history-order',function(){
                var carId = $('#customerCarId').val();
                $.ajax({
                    url: BASE_PATH + "/shop/order/get-sign-order",
                    data:{
                        customerCarId: carId
                    },
                    success:function(result){
                        if(result.success){
                            var html = at('historyTableTpl',{json:result});
                            dg.open({
                                area: ['800px','400px'],
                                content:html
                            })
                        }
                    }
                });
            });
        })
    })
</script>
