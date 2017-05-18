<#-- 
    批量添加物料公共模板，用到此模板页面，请在下面登记一下
    ch 2016-04-08
    
    用到的页面：
    yqx/page/order/sell-good.ftl 新建销售单
    yqx/page/order/speedily.ftl 快修快保
 -->

<style>
/*批量添加配件 start*/
.batch_add_part{width: 100%;height:100%;}
.batch_add_part .dialog-title{background:#232e49;color:#fff;text-align:center;font-size:16px;font-weight:900;line-height:54px;}
.batch_add_part table{width: 100%;}
.batch_add_part table td,.batch_add_part table th{vertical-align: middle;text-align: left;padding-left: 5px;}
.batch_add_part .table th{height:25px;border-bottom:1px solid #d6d6d6;background: #fff;}
.batch_add_part .table td{height:30px;border-bottom:1px solid #c5c9b8;background: #fff;color: #333;}
.batch_add_part .table tr.hui td{background: #f5f5f5;}
.batch_add_part .thead{background: #fff;}
.batch_add_part .tbody{display: block; height:190px;overflow-y: scroll;}
.batch_add_part .search_box{background: #fff;padding-bottom: 15px;border-bottom: 1px solid #dcdcdc;}
.batch_add_part .search_box > ul{float: left;width: 690px;margin-left: 20px;}
.batch_add_part .search_box > ul >li{float: left;width: 230px;margin: 8px 0 0px 0;}
.batch_add_part .search_box > ul >li label{display: inline-block;width: 60px;height:32px;line-height: 32px;}
.batch_add_part .batch_search_btn {display: block;float: left;margin-top: 5px; width: 75px;height: 75px;line-height:75px;font-size:30px;text-align:center;padding:0;}

.batch_add_part .brand-item {
    width: 114px;
    height: 30px;;
}
.search_box input {height:30px;line-height: 30px;border:1px solid #c9c9c9;border-radius: 3px;}
.search_box input {width:130px;padding: 0 5px;border-radius: 4px;}
.search_box .chosen-container-single .chosen-single {height: 30px;line-height: 30px;}

.selected_part{height:100px;border-top:1px solid #d6d6d6;border-bottom:1px solid #d6d6d6;overflow-y: scroll;}
.batch_add_part .dialog_btn .qxy_btn{margin-top: 10px;}
#selected_part_count{color: red;font-weight: 900;}
.selected_part_title{height:25px;line-height: 25px;padding-left:20px; border-top:1px solid #d6d6d6;background: #f5f2e6;}
.selected_part tbody td{height:30px;border-bottom:1px solid #c5c9b8;background: #fff;color: #333;}
.batch_add_part .dialog-btn{text-align:center;}
.batch_add_part .dialog-btn a{margin:15px;}
/*批量添加配件 end*/
</style>

<!-- 弹框 批量添加物料 start -->
<script type="text/html" id="batch_add_part">
    <div class="batch_add_part check_box_scope" data-tpl-ref="batch-add-part-tpl">
        <p class="dialog-title">
            批量添加配件
        </p>
        <div class="search_box clearfix">
            <ul class="clearfix">
                <li><label>配件类别:</label><input type="text" placeholder="请选择配件类别" name="goodsTypeText" class="goods_type_input" readonly="readonly" no_submit="true">
                    <input type="hidden" name="search_catId">
                    <input type="hidden" name="customCat">
                </li>
                <input type="hidden" name="search_onsaleStatus" value="1">
                <li>
                    <label>配件品牌:</label><div class="form-item brand-item">
                    <input class="js-brand" placeholder="请输入搜索配件品牌">
                    <input type="hidden" name="search_brandId">
                    </div>
                </li>
                <li><label>配件名称:</label><input type="text" name="search_goodsNameLike"></li>
                <li><label>车辆型号:</label><input type="text" name="search_carInfoLike"></li>
                <li><label>零件号:</label><input type="text" name="search_goodsFormatLike"></li>
                <li>
                    <label>仓库货位:</label><div class="form-item brand-item">
                    <input class="js-batch-add-part-depot" name="search_depotLike" placeholder="请输入仓库货位">
                </div>
                </li>
            </ul>
            <a href="javascript:;" class="batch_search_btn yqx-btn yqx-btn-3 icon-search">
            </a>
        </div>
        <div class="thead">
            <table class="table">
                <tHead>
                <tr>
                    <th style="padding-left:20px;">配件名称</th>
                    <th width="200">零件号</th>
                    <th width="115">单价</th>
                    <th width="50"></th>
                </tr>
                </tHead>
            </table>
        </div>
        <div class="tbody">
            <table class="table">
                <tbody></tbody>
            </table>
        </div>
        <div class="selected_part_title">
            已选择配件列表(<span id="selected_part_count">0</span>)
        </div>
        <div class="selected_part">
            <table>
                <tbody>

                </tbody>
            </table>
        </div>
        <div class="btn_center dialog-btn">
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-2 batch_add_part_submit">提交</a>
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 batch_add_part_cancel">取消</a>
        </div>
    </div>
</script>

<!-- 配件品牌模板 end -->
<!-- 批量添加物料搜索结果页模板 start -->
<script type="text/html" id="search_result_tpl">
    <%for(var i=0;i<json.data.content.length;i++){%>
    <% var item = json.data.content[i];%>
    <% var item_json = $toJsonString(item);%>
    <% if(i%2!=0){ %>
    <tr class="hui" data-id="<%=item.id%>">
        <% } else{ %>
    <tr data-id="<%=item.id%>">
        <% } %>
        <td style="padding-left:20px;"><%=item.name%><input type="hidden" class="item_json" value="<%=item_json%>"></td>
        <td width="200"><%=item.format%></td>
        <td width="100">￥<%= item.price%></td>
        <td width="50"><a href="javascript:;" class="qxy_link batch_add">添加</a></td>
    </tr>
    <%}%>
</script>
<!-- 批量添加物料搜索结果页模板 end -->
<!-- 批量添加物料已选择模板 start -->
<script type="text/html" id="search_checked_tpl">
    <tr data-id="<%=item.id%>">
        <% var item_json = $toJsonString(item);%>
        <td style="padding-left:20px;"><%=item.name%><input type="hidden" class="item_json" value="<%=item_json%>"></td>
        <td width="200"><%=item.format%></td>
        <td width="100">￥<%= item.price%></td>
        <td width="50"><a href="javascript:;" class="qxy_link batch_del">删除</a></td>
    </tr>
</script>
<!-- 批量添加物料已选择模板 end -->
<!-- 弹框 批量添加物料 end -->

<script>
    
    function batchAddPart(option){
        var defaultOpt = {
            dom: '',
            callback: null
        },
        args = $.extend({},defaultOpt,option),
        doc = $(document),
        dgIndex = null;

        seajs.use([
            'dialog',
            'art',
            'select',
            'ajax',
            'formData'
        ],function(dg, at, select, ax, fd){

            //打开批量添加配件弹窗
            doc.on("click", args.dom, function() {
                var html = at('batch_add_part', {data: option});

                //打开配件弹框
                dgIndex = dg.open({
                    area: ['800px', '570px'],
                    content: html
                });

                select.init({
                    dom: '.batch_add_part .js-brand',
                    url: BASE_PATH + '/shop/goods_brand/shop_list',
                    showKey: 'id',
                    pleaseSelect: true,
                    canInput: true,
                    showValue: 'brandName'
                });
                select.init({
                    dom: '.batch_add_part .js-batch-add-part-depot',
                    url: BASE_PATH + '/shop/goods/get_depot_list',
                    showKey: 'name',
                    canInput: true,
                    showValue: 'name'
                });

            });
            //批量添加物料查询事件
            doc.on("click",".batch_search_btn",function(){
                var data = fd.get(".search_box");
                data["size"] = 1000;
                data["page"] = 1;
                $.ajax({
                    url: BASE_PATH + "/shop/goods/search/json",
                    data: data
                }).done(function(json){
                    at.helper("$toJsonString",function(data){
                        return JSON.stringify(data);
                    });
                    var html = at("search_result_tpl",{"json":json});
                    $("tbody:eq(0)",".batch_add_part").html(html);
                });
            });
            //批量添加物料搜索结果区域，单击tr选择一个配件
            doc.on("click",".batch_add_part .tbody tr",function(){
                var data = $.parseJSON($(".item_json",$(this)).val());
                var id = data.id;
                var scope = ".selected_part", html;
                var size = $("tr[data-id='"+id+"']",scope).size();
                var goodsId = [];

                $('.yqx-table input[name="goodsId"]').each(function () {
                    goodsId.push( $(this).val() );
                });

                if(size > 0 || goodsId.indexOf(data.id + '') > -1) {
                    dg.confirm('已添加此配件，继续添加该配件吗', function () {
                        html = at('search_checked_tpl',{"item":data});
                        $("tbody",scope).append(html);
                        $("#selected_part_count").text($("tr",scope).size());
                    });
                    return;
                }
                html = at('search_checked_tpl',{"item":data});
                $("tbody",scope).append(html);
                $("#selected_part_count").text($("tr",scope).size());
            });
            //批量添加物料已选择区域，单击tr删除一个配件
            doc.on("click",".selected_part tr",function(){
                $(this).remove();
                $("#selected_part_count").text($("tr",".selected_part").size());
            });
            //确认批量添加物料，并回显到弹框下面动态行
            doc.on("click",".batch_add_part_submit",function(){
                var scope = ".selected_part tbody";
                var list = $("tr",scope);
                var dataList = [];
                if(list.size() < 1){
                    dg.warn("请至少选择一个配件");
                    return;
                }
                dgIndex && dg.close(dgIndex);
                list.each(function(){
                    var item = $(this).find(".item_json").val();
                    item = $.parseJSON(item);
                    dataList.push(item);
                });
                args.callback && args.callback(dataList);
            });
            //批量添加物料弹窗关闭事件绑定
            doc.on("click", ".batch_add_part_cancel", function(){
                dgIndex && dg.close(dgIndex);
            });

        });
        
    }
</script>