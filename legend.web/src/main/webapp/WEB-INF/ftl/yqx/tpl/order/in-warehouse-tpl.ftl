<#-- 
    转入库公共模板，用到此模板页面，请在下面登记一下
    ch 2016-04-08
    
    用到的页面：
    yqx/page/order/sell-good.ftl 新建销售单
    yqx/page/order/speedily.ftl 快修快保
 -->


<style>
/*转入库弹窗 start*/
.in-warehouse .must{color:red;}
.in-warehouse .dialog-title{background:#232e49;color:#fff;text-align:center;font-size:16px;font-weight:900;line-height:54px;}
.in-warehouse{width: 100%;height:100%;background: #fff;}
.in-warehouse table{width:100%;}
.in-warehouse table td,.in-warehouse table th{vertical-align: middle;text-align: left;padding-left: 10px;position: relative}
.in-warehouse table th{height:30px;border-bottom:1px solid #d6d6d6;background: #f5f2e6;}
.in-warehouse table td{height:50px;border-bottom:1px solid #c5c9b8;background: #fff;color: #333;}
.in-warehouse table td input{height:30px;border:1px solid #c9c9c9;border-radius: 3px;padding:0 5px;width: 70%;background: #fff;}
.in-warehouse table tr.gray td{background: #f5f5f5;}
.in-warehouse .thead{margin-bottom: -30px;}
.in-warehouse .tbody {height:305px;padding-top: 30px;overflow-y: scroll;}
.in-warehouse .dialog-btn{text-align:center;}
.in-warehouse .dialog-btn a{margin:15px;}
.in-warehouse .yqx-input { width: 100%;}
.in-warehouse .select-input.yqx-input {
    padding-right: 30px;
}
/*转入库弹窗 end*/
</style>

<!-- 弹框 转入库 start -->
<script type="text/html" id="in-warehouse-tpl">
    <div class="in-warehouse" data-tpl-ref="in-warehouse-tpl">
        <p class="dialog-title">
            转入库
        </p>
        <div class="thead">
            <table>
                <tHead>
                <tr>
                    <th width="100" style="width:100px;">零件号</th>
                    <th width="230" style="width:230px;">配件名称</th>
                    <th width="160">供应商</th>
                    <th width="70">入库单价<span class="must">*</span></th>
                    <th width="60">数量</th>
                    <th width="98">总金额</th>
                </tr>
                </tHead>
            </table>
        </div>
        <div class="tbody">
            <table>
                <%for(var i=0;i<data.length;i++){%>
                <%var item = data[i]%>
                <% if(i%2!=0){ %>
                <tr class="gray">
                    <% } else{ %>
                <tr>
                    <% } %>
                    <td width="100" style="width:100px;">
                        <input type="hidden" name="goodsId" value="<%=item.goodsId%>"/>
                        <%=item.goodsFormat%>
                    </td>
                    <td width="230" style="width:230px;">
                        <%=item.goodsName%>
                    </td>
                    <td width="160">
                        <div class="form-item">
                        <input class="yqx-input js-dialog-inwarehouse-supplier select-input"
                               data-v-type="required"
                               placeholder="输入供应商名称查询">
                        <span class="fa icon-angle-down icon-small"></span>
                        <input type="hidden" name="supplierId">
                    </td>
                    <td width="70">
                        <div class="form-item">
                            <input type="text" name="purchasePrice" class="J_input_limit" data-v-type="required|price|maxLength:8" data-label="入库单价"/>
                        </div>
                    </td>
                    <td width="60">
                        <div class="form-item">
                            <input type="text" name="goodsCount" value="<%=item.goodsNumber%>" maxlength="5" data-v-type="number" class="J_input_limit"/>
                        </div>
                    </td>
                    <td width="80"><span class="purchaseAmount">0</span></td>
                </tr>
                <%}%>
            </table>
        </div>
        <div class="btn-center dialog-btn">
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-2 js-in-warehouse-submit">提交</a>
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 js-in-warehouse-cancel">取消</a>
        </div>
    </div>
</script>
<!-- 弹框 转入库 end -->


<script>
    //打开转入库弹窗
    function inWarehouse(option){
        var defaultOpt = {
                //转入口触发按钮
                dom: '',
                //需要转入库的配件列表选择器,所需要的参数
                list: '',
                //回调函数
                callback: null,
                goodsData: null
            },
            doc = $(document);
        seajs.use([
            "dialog",
            "ajax",
            'select',
            "art",
            'formData',
            'check'
        ],function(dg,ax,select, at,fd, ck){
            var args = $.extend({},defaultOpt,option),
                dgIndex = null;
            if(!args.dom) return;

            select.init({
                canInput: true,
                url: BASE_PATH+"/shop/setting/supplier/get_supplier_list",
                showKey: 'id',
                isClear: true,
                showValue: 'supplierName',
                dom: '.js-dialog-inwarehouse-supplier'
            });

            //触发对象点击后弹出转入库框
            doc.on("click", args.dom, function(){
                var selectedList = $('input[type="checkbox"]:checked',args.list);
                var data;

                if(typeof args.goodsData == 'function') {
                    data = args.goodsData();
                } else if(args.goodsData && args.goodsData.length) {
                    data = args.goodsData;
                }

                if(selectedList.size()==0 && data == null){
                    dg.warn("请至少选择一个配件");
                    return;
                }

                $.ajax({
                    url: BASE_PATH+"/shop/setting/supplier/get_supplier_list"
                }).done(function(json){
                    var list = data || (function(){
                        var arr = [];
                        selectedList.each(function(){
                            var parent = $(this).parents('tr');
                            var obj = fd.get(parent);
                            arr.push(obj);
                        });
                        return arr;  
                    })();

                    var html = at('in-warehouse-tpl',{"data":list,"json":json});
                    //打开配件弹框
                    dgIndex = dg.open({
                        area: ['700px', '450px'],
                        content: html
                    });


                    ck.insertMsg('.in-warehouse');
                });
            });
            //转入库弹窗中金额变化事件
            doc.on("blur", "input[name='purchasePrice'],input[name='goodsCount']", function(){
                var tr = $(this).parents("tr");
                var price = $.trim($("input[name='purchasePrice']",tr).val());
                var number = Number($("input[name='goodsCount']",tr).val());

                if(price == "" || number == ""){
                    return;
                }else{
                    if($.isNumeric(price)){
                        price = Number(price);
                        $(".purchaseAmount",tr).html(number.Cheng(price));
                    }else{
                        dg.warn("请输入数字");
                        $(this).val("").focus();
                    }
                }
            });
            //转入库数据提交
            doc.on("click", ".js-in-warehouse-submit", function(){
                var scope = ".in-warehouse";
                var data = [], supplier = true;

                if(!ck.check(scope)) {
                    return false;
                }

                $("input[name='purchasePrice']",".in-warehouse").each(function(){
                    var purchasePrice = $.trim($(this).val());
                    var tr = $(this).parents("tr");
                    if(purchasePrice == "" || !$.isNumeric(purchasePrice)){
                        $(this).val("").focus();
                        return false;
                    }
                    var goods = fd.get(tr);
                    if(goods.supplierId == null || goods.supplierId == '') {
                        supplier = false;
                        tr.find('input[name=supplierName]').val('').trigger('input');
                    }
                    data.push(goods);
                });

                if(!supplier) {
                    dg.warn('请选择供应商');
                    return;
                }

                $.ajax({
                    type: "post",
                    url: BASE_PATH+"/shop/warehouse/in/blue/batch",
                    contentType : 'application/json',
                    data: JSON.stringify(data)
                }).done(function(json){
                    if(json.success){
                        dgIndex && dg.close(dgIndex);
                        dg.success("入库成功");
                        args.callback && args.callback(json["data"]);
                    }else{
                        dg.fail("入库失败<br/>"+json.errorMsg);
                    }
                });
            });
            //转入库弹窗关闭事件绑定
            doc.on("click", ".js-in-warehouse-cancel", function(){
                dgIndex && dg.close(dgIndex);
            });

        });
    }
</script>