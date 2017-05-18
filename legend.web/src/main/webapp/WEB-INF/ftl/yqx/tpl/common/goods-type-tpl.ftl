<#-- 
    配件分类公共模板，用到此模板页面，请在下面登记一下
    ch 2016-04-08
    
    用到的页面：
    yqx/page/order/sell-good.ftl 新建销售单批量添加物料、新增配件都有用到
 -->

<style>
/* 物料分类 start */
.qxy_dialog{padding: 0;}
.pop_goods_type_wrap {width: 980px; background: #f5f5f5; }
/* 头部 start */
.pop_goods_type_wrap .pop_head {padding: 15px 15px 0 15px; line-height: 36px; }
.pop_goods_type_wrap .head_title {font-size: 18px; font-weight: 700; color: #333; }
.pop_goods_type_wrap .search_input {float: right; width: 392px; height: 36px; padding: 0 43px 0 5px; line-height: 36px; background: url("/legend/static/img/tpl/common/input_l.png") left center no-repeat; }
.pop_goods_type_wrap .search_input input[type=text] {width: 100%; height: 34px; line-height: 34px; border: solid #DAD9D9; border-width: 1px 0; background: #fff; vertical-align: top; }
.pop_goods_type_wrap .search_input .goods_all_search_btn {float: right; width: 43px; height: 36px; margin-right: -43px; background: url("/legend/static/img/tpl/common/input_s.png") center no-repeat; cursor: pointer; vertical-align: top; }
/* 头部 end */

/*  主体 start */
.pop_goods_type_wrap .pop_main {position: relative; height: 490px; padding-left:133px; }
/* 一级导航栏 start */
.pop_goods_type_wrap .nav_side {position: absolute; left: 3px; top: 0; width: 130px; height: 100%; }
.pop_goods_type_wrap .nav_side li {border: 2px solid #f5f5f5; cursor: pointer; }
.pop_goods_type_wrap .nav_side li p {padding: 1px 16px; line-height: 1.2; font-size: 12px; font-weight: 700; color: #333; }
.pop_goods_type_wrap .nav_side li p:hover{text-decoration: underline;color: #8cb422;}
.pop_goods_type_wrap .nav_side .cur {border: 2px solid #8cb422; cursor: default; }
.pop_goods_type_wrap .nav_side .cur p {margin-right: -4px; background: #fff; }
/* 一级导航栏 start */

.pop_goods_type_wrap .nav_content {height: 446px; padding: 20px; background: #fff; border: 2px solid #8cb422; overflow: hidden; }
.pop_goods_type_wrap .scroller {height: 100%; overflow: auto; }
.grid-hor-2 {float: left; width: 50%; }
/* 二级导航栏 start */
.pop_goods_type_wrap .second_nav {min-height: 100%; padding-right: 20px; border-right: 1px solid #e5e5e5; }
.pop_goods_type_wrap .secend_nav_list {overflow: hidden; }
.pop_goods_type_wrap .secend_nav_list li {float: left; margin: 0 6px 10px 0; padding: 0 5px; line-height: 20px; font-size: 12px; color: #666; cursor: pointer; }
.pop_goods_type_wrap .secend_nav_list .cur {color: #fff; background: #d95b5c; cursor: default; }
/* 二级导航栏 end */

/* 三级结果 start */
.pop_goods_type_wrap .category_box {margin: 0 0 20px 20px; }
.pop_goods_type_wrap .category_box:last-child {margin-bottom: 0; }
.pop_goods_type_wrap .category_box .category {margin-bottom: 3px; padding-bottom: 12px; font-size: 16px; font-weight: 900; color: #333; border-bottom: 1px solid #e5e5e5; }
.pop_goods_type_wrap .category_box .category_items {/*height: 72px;*/ overflow: hidden; }
.pop_goods_type_wrap .category_box .category_items .item {float: left; margin: 12px 12px 0 0; font-size: 12px; color: #666; cursor: pointer; }
.pop_goods_type_wrap .category_box .category_items .item:hover {color: #d01113; }
/* 三级结果 end */

.cat_item2 .item{float: left;padding: 0 8px;cursor: pointer}
.cat_item2 .category{margin-bottom: 3px; padding-bottom: 12px; font-size: 16px; font-weight: 900; color: #333; border-bottom: 1px solid #e5e5e5; }
.search_result_scroll .cat_item2{padding: 0;}

/*添加分类 start*/
.info-cat{
    display: inline-block;
    font-style: normal;
    font-size: 12px;
    line-height: 22px;
    text-align: center;
    cursor: pointer;
    border: 1px solid rgb(123, 160, 4);
    border-radius: 3px;
    height: 22px;
    color: rgb(123, 160, 4);
    padding: 0 10px;
}
.cat-add {
    background: rgb(251, 251, 251);
    border-color: rgb(191, 191, 191);
    color: rgb(191, 191, 191);
    cursor: pointer;
    width: 55px;
    margin-left: 5px;
}
.cat-add:hover {
    border-color: rgb(123, 160, 4);
    color: rgb(123, 160, 4);
    opacity: .8;
}

.cat-input {
    border: 0;
    height: 100%;
    line-height: 100%;
    width: 80px;
    text-indent: 1px;
}

.input-box {
    background: none;
    padding: 0;
    color: #fff;
}
i.cat-btn {
    font-style: normal;
    background: #9fc527;
    padding: 0 3px;
    height: 100%;
    color: #fff;
    display: inline-block;
    cursor: pointer;
}
.goods_type_part_wind .category-box, .goods_type_part_wind .hot-category {
    white-space: initial;
    text-align: left;
}
.hot-category i.strong-i,.category-box i.strong-i{ display: block; width:100%; height:30px;font-style: normal;line-height:30px;color: #f76c6d;}
.strong-i.type{color: #a0c338 !important;}
i.type-color{ color: #8fb614;}
/*添加分类 end*/

/* 搜索结果 start */
.pop_goods_type_wrap .search_scroller .headline {margin-bottom: 15px; font-size: 12px; color: #666; }
.pop_goods_type_wrap .search_categroy_box .category_items {padding-left: 120px; } 
.pop_goods_type_wrap .search_categroy_box .category_items .title {float: left; width: 100px; margin: 12px 0 0 -120px; font-weight: 600; color: #333; border-right: 1px solid #e5e5e5; }
/* 搜索结果 end */

/* 主体 end */

/* 物料分类 end */


/* 部分配件分类弹窗 start */
input.goods_type_input{background-color: #fff;}
.goods_type_part_wind{width: 645px;background: #f5f5f5;padding:10px; position: absolute;z-index: 30;border:2px solid #8fb027;}
.goods_type_part_wind .goods_type_search_box{margin:0 auto 10px auto;position: relative;padding:0;border:none;background: none;}
.goods_type_part_wind .goods_type_search_box input{width:75%;height:34px;vertical-align: middle;padding-left:10px;border:1px solid #dad9d9;background: #fff;line-height: 34px;}
.goods_type_part_wind .goods_type_search_box a.goods_search_btn{display: inline-block;width: 49px;height:34px; background: #a0c338; color: #fff; vertical-align: middle; text-align: center; line-height: 36px;}
.goods_type_part_wind .goods_type_search_box a.goods_all_btn{display: inline-block; width: 64px; height: 34px; color: #333; line-height: 34px; background: #fff; border: 1px solid #d2d2d2; text-align: center;vertical-align: middle;}
.goods_type_part_wind .search_result{background: #fff;width:280px;margin:0 auto;padding: 10px 10px 0 10px;}
.goods_type_part_wind .search_result table{width: 100%;}
.goods_type_part_wind .search_result thead td{
    font-weight: normal;
}
.goods_type_part_wind .search_result thead tr{
    border-bottom: 1px #DEDEDE solid;
}
.goods_type_part_wind .search-box {
    max-height: 400px;
    overflow-y: auto;
}
.goods_type_part_wind .search_result td,
.goods_type_part_wind .search_result th{height:25px;line-height:25px;color: #666;vertical-align: middle;font-weight: 700;}
.goods_type_part_wind .search_result tbody tr:hover td{color: #d01113;}
.goods_type_part_wind .search_result tbody td{padding: 0 5px;border-bottom: 1px solid #f5f5f5;cursor: pointer;}
.goods_type_part_wind .search_result .search_result_scroll{max-height: 240px;overflow-y: hidden;}
.search_result_fixed{padding-right:10px;border-bottom: 1px solid #dad9d9;}

.tips-phone{line-height: 25px;background: url("/legend/static/img/tpl/common/wenhao.gif") no-repeat left 7px;padding-left: 17px;}
.tips-phone b{font-size: 16px;color: #333;font-weight: 700;}
.pop_goods_type_wrap .tips-phone{text-align: right;padding-right: 7px;background-position: 526px center;}
.tips-phone span{ color: #fa7b7c;}
/* 部分配件分类弹窗 end */
.layui-layer-page .layui-layer-content{overflow: visible;}

/* 热门分类样式 start */
.hot-category .i-service,.category-box .i-service{
    display: inline-block;
    width: 100px;
    height: 15px;
    line-height: 15px;
    padding: 0 10px;
    margin-bottom: 5px;
    font-style: normal;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: pointer;
    border:none;
}

.i-service:hover {
    color: #9fc527;
}

.i-selected {
    color: #9fc527;
}
/* 热门分类样式 end */


/*添加分类 start*/
.add-type{ display: block; width: 62px; height: 28px; line-height: 28px; text-align: center; background: #fff; border: 1px solid #d2d2d2; color: #333;}
.js-cat-save{display: inline-block;width: 49px;height:36px; background: #a0c338; color: #fff; vertical-align: middle; text-align: center; line-height: 36px;}

/*添加分类 end*/
 .hot-category .search-box{ display: block; width: 622px; padding: 10px; max-height: 245px; background: #fff; border: 1px solid #d2d2d2; overflow-y: scroll;}
 .hot-category .search-box li {
     display: block;
     border: none;
     line-height: 28px;
     color: #333;
     cursor: pointer;
 }
 .hot-category .search-box li:hover{ background: #f5f5f5; color: #7b9f1a;}
    .add-color{ background: #9fc527; border:1px solid #8cb019; color: #fff;}
.hot-category li input.add-type-width{ width: 200px;margin-right: 5px}
    .add-type-box{ display: none;}
    .wrap{ width: 100%; max-height: 340px; overflow-y: scroll;}
</style>

<!-- 全部分类弹框模板 start -->
<script type="text/html" id="goods_type_wrap_tpl">
    <div class="pop_goods_type_wrap" data-tpl-ref="goods-type-tpl">
        <div class="pop_head">
            <div class="search_input">
                <input type="text" placeholder="搜索类别..." flag="2" id="goods_search_input"/><button class="goods_all_search_btn"></button>
            </div>
            <h1 class="head_title">全部类别</h1>
        </div>
        <p class="tips-phone">找不到您想要的配件分类?拨打电话：<b>0571-57874656</b>（工作日9:00-18:00）</p>
        <div class="pop_main">
            <ul class="nav_side" id="navSide">

            </ul>
            <div class="nav_content" id="navContent">

            </div>
        </div>
    </div>
</script>
<!-- 全部分类弹框模板 end -->
<!-- 1级配件分类模板 start -->
<script id="navSideTpl" type="text/html">
    <% if ( success ) { %>
    <% for (var i = 0; i < data.length; i++) { %>
    <% var item = data[i]; %>
    <li data-catid="<%= item.catId %>"><p><%= item.catName %></p></li>
    <% }} %>
</script>
<!-- 1级配件分类模板 end -->
<!-- 2级配件分类模板 start -->
<script id="secondNavContentTpl" type="text/html">
    <div class="scroller grid-hor-2">
        <div class="second_nav">
            <ul class="secend_nav_list">
                <% for (var i = 0; i < data.length; i++) { %>
                <% var item = data[i]; %>
                <li data-catid="<%= item.catId %>"><%= item.catName %></li>
                <% } %>
            </ul>
        </div>
    </div>
    <div class="scroller grid-hor-2" id="thirdNavContent"></div>
</script>
<!-- 2级配件分类模板 end -->
<!-- 3级配件分类模板 start -->
<script id="thirdNavContentTpl" type="text/html">
    <div class="category_box">
        <p class="category"><%=catName%></p>
        <ul class="category_items">
            <% for (var i = 0; i < data.length; i++) { %>
            <%  var item = data[i];
                if(item.customCat){
            %>
               <li class="item" data-catid="<%= item.catId %>" data-customcat="true"><%= item.catName %></li>
            <%}else{%>
                <li class="item" data-catid="<%= item.catId %>" data-customcat="false"><%= item.catName %></li>
            <%}%>
        <% } %>
        </ul>
    </div>
</script>
<!-- 3级配件分类模板 end -->
<!-- 全部分类搜索结果模板 start -->
<script id="searchContentTpl" type="text/html">
    <div class="scroller search_scroller">
        <h3 class="headline">全部类别下搜索结果：</h3>
        <%for(var x=0;x<data.length;x++){
            var item1 = data[x];
        %>
            <div class="category_box search_categroy_box">
                <h3 class="category"><%=item1.name%></h3>
                <%for(var y=0;y<item1.list.length;y++){
                    var item2 = item1.list[y];
                %>
                    <dl class="category_items">
                        <dt class="title"><%=item2.name%></dt>
                        <%for(var z=0;z<item2.list.length;z++){
                            var item3 = item2.list[z];
                            if(item3.customCat){
                        %>
                            <dd class="item" data-catid="<%=item3.id%>" customcat="true"><%=item3.name%></dd>
                            <%}else{%>
                            <dd class="item" data-catid="<%=item3.id%>" customcat="false"><%=item3.name%></dd>
                            <%}%>
                        <%}%>
                    </dl>
                <%}%>
            </div>
        <%}%>
    </div>
</script>
<!-- 全部分类搜索结果模板 end -->


<script type="text/html" id="goods_type_part_wind">
    <div class="goods_type_part_wind" data-tpl="goods-type-tpl">
        <div class="goods_type_search_box">
            <input type="text" placeholder="搜索类别" id="goods_search_input" flag="1"/><a href="javascript:;" class="goods_search_btn">查询</a>
            <a href="javascript:;" class="goods_all_btn">全部分类</a>
        </div>
        <div class="wrap">
            <div class="hot-category" id="hotCategoryContent">
            </div>
            <div id="search_result">
                <div class="search_result_scroll">
                </div>
            </div>
        </div>
        <div class="hot-category" id="seachCategoryContent">
        </div>
    </div>
</script>
<script type="text/html" id="goods_type_part_result">
<div class="cat_item2 category_items">
    <div class="category-box">
        <i class="strong-i type">自定义分类:</i>
        <% for(var x=0;x < data.length;x++){
        var item = data[x];
        %>
        <i class="i-service js-select-goods-category js-show-tips" data-id="<%= item.catId%>" data-customcat="true" data-name="<%= item.catName%>"><%= item.catName %></i>
        <%}%>
    </div>
</div>
<a href="javascript:;" class="add-type js-add-type">添加分类</a>
<div class="add-type-box">
    <div class="form-item">
        <input type="text" name="catName" class="yqx-input yqx-input-small add-type-width" value="" placeholder="">
    </div>
    <a href="javascript:;" class="js-cat-save">添加</a>
</div>
</script>
<script type="text/html" id="hot-category-tpl">
    <%if(json && json.data && json.data.length > 0){%>
            <%var cateLength = json.data.length;%>
            <i class="strong-i">热门分类:</i>
            <%for(var i = 0; i < cateLength; i++){%>
            <% var goodsCategory = json.data[i];%>
            <i class="i-service js-select-goods-category js-show-tips" data-id="<%= goodsCategory.tqmallCatId%>" data-customcat="false" data-name="<%= goodsCategory.catName%>"><%= goodsCategory.catName%></i>
            <%}%>
    <%}%>
</script>
<script type="text/html" id="search-category-tpl">
    <ul class="search-box">
        <%  for(var x=0;x < data.length;x++){
            var item = data[x];
            if(item.list){
                var list1 = item.list;
                for(var i = 0 ; i < list1.length ; i++ ){
                    var list2 = list1[i].list;
                    if(list2){
                    for(var j = 0 ; j < list2.length ; j++){
                        var item2 = list2[j];
                        if(item2.customCat){%>
                            <li class="item js-select-goods-category" data-id="<%= item2.id %>" data-customcat="true" data-name="<%= item2.name%>" ><%= item2.name %></li>
                        <%}else{%>
                            <li class="item js-select-goods-category" data-id="<%= item2.id %>" data-customcat="false" data-name="<%= item2.name%>" ><%= item2.name %></li>
                    <%}}}}}}%>
    </ul>
</script>

<script>
    $(function() {
        seajs.use('dialog', function(dg) {
            dg.titleInit();
        });

        seajs.use([
            'dialog',
            'ajax',
            'art'
        ],function(dg, ax, at){
            var doc = $(document);
            //搜索延时器对象
            var searchDelay = null;
            //onkeyup时间延时
            var searchDelayTime = 200;
            //配件分类弹框索引
            var goodsTypeWind = null;
            //触发配件弹框的的对象
            var goodsTypeObj = null;
            //是否显示自定义分类
            var customCat = false;

            //默认部分配件分类的弹框。
            doc.on('click','input.goods_type_input',function(){
                $(this).val("");
                $(this).next().val("");

                goodsTypeObj = $(this);

                goodsTypeObj.parent().find("input[name='customCat']").val("");
                $(".goods_type_part_wind").remove();
                var data = {"key":""};
                var href = window.location.href;
                if(/goods\/add_page/.test(href) || /goods\/update_not_tq_page/.test(href)){
                    data.customCat = false;
                }
                $.when(
                    $.ajax({
                        url:BASE_PATH+"/shop/goods_category/std/get_by_pid?pid=-2",
                        data:data
                    })
                ).done(function(){
                    var html = at('goods_type_part_wind',{});
                    var position = goodsTypeObj.position();
                    var height = goodsTypeObj.height(), offsetLeft = 0, t;
                    html = $(html).css({top:position.top + height + 2,left:position.left});
                    goodsTypeObj.parent().append(html);

                    t = html[0];
                    // 当本窗口超出右边界的时候
                    do {
                        offsetLeft += t.offsetLeft;
                    } while((t = t.offsetParent) && t != document.body && offsetLeft >= 0);

                    if(offsetLeft + html[0].clientWidth > document.body.clientWidth) {
                        t = offsetLeft + html[0].clientWidth - document.body.clientWidth;
                        html.css('left', position.left - t);
                    }

                    //初始化热门分类
                    getHotCategory();
                }).done(function(data){
                    var html="";
                    if(!data.data){
                        html = data.errorMsg;
                    }else{
                        html = at('goods_type_part_result',{"data":data.data});
                    }
                    $(".search_result_scroll").html(html);
                });
                // 不能return false
                // 会导致其他下拉列表的插件无法及时关闭
            });

            //搜索按钮和输入框通用事件。
            var search = function(type){
                var key = $("#goods_search_input").val();
                var data = {"key":key};
                var href = window.location.href;
                if(/goods\/add_page/.test(href) || /goods\/update_not_tq_page/.test(href)){
                    data.customCat = false;
                }
                if(key == ''){
                    $('#hotCategoryContent').show();
                    $('#search_result').show();
                    $('#seachCategoryContent').hide();
                }else{
                    $('#hotCategoryContent').hide();
                    $('#search_result').hide();
                    $('#seachCategoryContent').show();
                }

                $.ajax({
                    url: BASE_PATH+"/shop/goods_category/std/get_by_key",
                    global: false,
                    data: data
                }).done(function(data){
                    if(data.success){
                        var list = data.data;
                        if(type == 1){
                            //type为1是部分配件弹框的搜索
                            var html2 = at('search-category-tpl',{"data":list});
                            if(list.length == 0){
                                html2 = '<p class="tips-phone">暂无匹配内容<br/>'+
                                '是否添加“<span>'+key+'</span>”为自定义分类'+
                                '</p>'+
                                '<a href="javascript:;" class="add-type add-color js-add-type">添加</a>'+
                                       '<div class="add-type-box">'+
                                            '<div class="form-item">'+
                                                '<input type="text" name="catName" class="yqx-input yqx-input-small add-type-width" value="'+key+'" placeholder="">'+
                                            '</div>'+
                                            '<a href="javascript:;" class="js-cat-save">添加</a>'+
                                        '</div>';
                            }
                            $("#seachCategoryContent").html(html2);
                        }else{
                            //全部分类弹框的搜索
                            var html = at('searchContentTpl',{"data":list});
                            if(list.length == 0){
                                html = '<p class="scroller search_scroller">暂无匹配内容</p>';
                            }
                            $("#navContent").html(html);

                        }
                    }else{
                        $(".search_result_scroll,#navContent").html(data.errorMsg);
                    }
                });
            }

            //部分配件分类弹框的搜索按钮事件
            doc.on('click','.goods_search_btn',function(){
                search(1);
            });
            //部分配件分类搜索框输入事件
            doc.on('keyup','input#goods_search_input',function(){
                searchDelay && window.clearTimeout(searchDelay);
                $(".nav_side li.cur").removeClass('cur');
                var flag = $(this).attr("flag");
                searchDelay = setTimeout(function(){
                   search(flag); 
                },searchDelayTime);
            });
            //部分配件分类点选搜索结果值回填事件
            doc.on('click','.search_result_scroll tbody tr',function(){
                var id = $(this).data("id");
                var name = $(this).data("name");
                var customCat = $(this).data("customcat");
                
                goodsTypeObj.val(name).blur();
                goodsTypeObj.next("[type='hidden']").val(id);

                var nextObjName = goodsTypeObj.next("[type='hidden']").attr("name");
                if(customCat){
                    //自定义配件
                    if(/search_/.test(nextObjName)){
                        goodsTypeObj.next("[type='hidden']").attr("name","search_catId");
                    }else{
                        goodsTypeObj.next("[type='hidden']").attr("name","catId");
                        renderDynamicAttr(true, id);
                    }
                }else{
                    //标准配件
                    if(/search_/.test(nextObjName)){
                        goodsTypeObj.next("[type='hidden']").attr("name","search_stdCatId");
                    }else{
                        goodsTypeObj.next("[type='hidden']").attr("name","stdCatId");
                        renderDynamicAttr(false, id);
                    }
                }
                goodsTypeObj.parent().find('input[name=customCat]').val(customCat);
                $(".goods_type_part_wind").remove();

                selectCallback.call(goodsTypeObj[0], id, name, customCat);
                return false;
            });
            //单击空白关闭配件下拉框。
            doc.click(function(e){
                var $target = $(e.target),
                    $comb = $($target.parents('.goods_type_part_wind'), $target.siblings('.goods_type_part_wind'));
                if(!$comb.length){
                    $(".goods_type_part_wind").remove();
                }
            });


            doc.on('click', '.goods_all_btn', function() {
                $(".goods_type_part_wind").remove();
                //先关闭部分弹框。
                goodsTypeWind && dg.close(goodsTypeWind);
                var data = {pid: 0};
                var href = window.location.href;
                if(/goods\/add_page/.test(href) || /goods\/update_not_tq_page/.test(href)){
                    data.customCat = false;
                }
                $.ajax({
                    url: BASE_PATH + "/shop/goods_category/std/get_by_pid",
                    data: data
                }).done(function(result){
                    var html = at('goods_type_wrap_tpl',{});
                    goodsTypeWind = dg.open({
                        content:html
                    });
                }).done(function(result){
                    render({
                        tag: "navSide",
                        tpl: "navSideTpl"
                    }, result);
                });
            });
            // 一级选项
            doc.on('click', '.nav_side li', function() {
                var $this = $(this),
                    pid = $this.data('catid');
                $this.siblings(".cur").removeClass("cur")
                    .end().addClass("cur");
                $.ajax({
                    url: BASE_PATH + "/shop/goods_category/std/get_by_pid",
                    data: {pid: pid}
                }).done(function(result){
                    render({
                        tag: "navContent",
                        tpl: "secondNavContentTpl"
                    }, result);
                });
            });
            // 二级选项
            doc.on('click', '.secend_nav_list li', function() {
                var $this = $(this),
                    pid = $this.data('catid'),
                    catName = $this.text();
                $this.siblings(".cur").removeClass("cur").end().addClass("cur");
                $.ajax({
                    url: BASE_PATH + "/shop/goods_category/std/get_by_pid",
                    data: {pid: pid}
                }).done(function(result){
                    render({
                        tag: "thirdNavContent",
                        tpl: "thirdNavContentTpl"
                    }, {"data":result.data,"catName":catName});
                });
            });
            // 三级选项
            doc.on('click', '.category_items .item', function() {
                var id = $(this).data("catid");
                var name = $.trim($(this).text());
                var customCat = $(this).data("customcat");
                goodsTypeWind && dg.close(goodsTypeWind);
                goodsTypeObj.val(name).blur();
                var nextObjName = goodsTypeObj.next("[type='hidden']").attr("name");
                $(".goods_type_part_wind").remove();

                if(customCat){
                    //自定义配件
                    if(/search_/.test(nextObjName)){
                        goodsTypeObj.next("[type='hidden']").attr("name","search_catId");
                    }else{
                        goodsTypeObj.next("[type='hidden']").attr("name","catId");
                        renderDynamicAttr(true, id);
                    }
                }else{
                    //标准配件
                    if(/search_/.test(nextObjName)){
                        goodsTypeObj.next("[type='hidden']").attr("name","search_stdCatId");
                    }else{
                        goodsTypeObj.next("[type='hidden']").attr("name","stdCatId");
                        renderDynamicAttr(false, id);
                    }
                }
                goodsTypeObj.next("[type='hidden']").val(id);
                goodsTypeObj.parent().find('input[name=customCat]').val(customCat);

                selectCallback.call(goodsTypeObj[0], id, name, customCat);
            });
            // 搜索按钮
            doc.on('click', '.goods_all_search_btn', function() {
                search(2);
            });
            //添加分类
            doc.on('click','.js-add-type',function(){
                $('.add-type-box').show();
                $(this).hide();
            });
            //确定添加分类
            doc.on('click','.js-cat-save', function(){
                var catName = $(this).parent().find('input[name=catName]').val()
                if(!catName){
                    dg.fail("请输入分类名称");
                    return;
                }
                var catBO = {catName:catName};
                $.ajax({
                    url:BASE_PATH + "/shop/goods_category/goods-category-save",
                    type:"post",
                    dataType:"json",
                    contentType: "application/json",
                    data: JSON.stringify(catBO),
                    success:function(result){
                        if(result.success){
                            var cat = result.data;
                            selectGoodsCategory(cat.id, cat.catName, true);
                        }else{
                            dg.fail("您所添加的配件类别名称已存在，请勿重复添加");
                        }
                    }
                });

            });

            function render( options, data ) {
                var html = at(options.tpl, data);
                $("#" + options.tag).html( html );
            }

            /**
             * 渲染动态行属性
             * @param customCat
             * @param json
             */
            function renderDynamicAttr(customCat,id){
                /** .xml页面用路径判断是否带出自定义属性
                 *  .ftl页面用customGoods值为true，自动带出自定义属性
                 *  .ftl页面用customGoods值为false，不自动带出自定义属性
                 * */
                var href = window.location.href;
                var customGoods = $('input.goods_type_input').data('customGoods');
                 if(/goods\/add_custom_page\/ng/.test(href) || /goods\/update_not_tq_page\/ng/.test(href) || customGoods) {
                    /**
                     * custom category attribute render
                     */
                    if(customCat){
                        /**
                         * 发送异步请求获取动态行属性
                         */
                        $.ajax({
                            url:BASE_PATH + "/shop/goods_attributes/get_by_type_id",
                            data:{'type_id':id}
                        }).done(function(data){
                            if(data && data.success){
                                var attr_list = data.data;
                                $("#dynamicAttr").html('');

                                var html =at('attr_template',{"data":attr_list});
                                $("#dynamicAttr").html(html);
                            } else {
                                dg.fail('获取分类属性列表失败.');
                            }
                        });
                    } else {
                        /**
                         * standard category attribute render
                         */
                        $.ajax({
                            url:BASE_PATH + "/shop/goods_attributes/get_by_cat_ids",
                            data:{'catIds':id}
                        }).done(function(data){
                            if(data && data.success){
                                var attr_list = data.data;
                                $("#dynamicAttr").html('');

                                var html =at('attr_template',{"data":attr_list});
                                $("#dynamicAttr").html(html);
                            } else {
                                dg.fail('获取分类属性列表失败.');
                            }
                        });
                    }
                }
            }

            /**
             * 获取配件热门分类
             * @param cateName
             */
            function getHotCategory(cateName){
                $.ajax({
                    url:BASE_PATH + "/shop/goods/hot/category/list",
                    data:{
                        cateName:cateName
                    }
                }).done(function(data){
                    if(data && data.success){
                        var html = at('hot-category-tpl',{"json":data});
                        $("#hotCategoryContent").html(html);
                    }
                });
            }
            function searchCategory(cateName){
                $.ajax({
                    url:BASE_PATH + "/shop/goods_category/std/get_by_key",
                    data:{
                        key:cateName
                    }
                }).done(function(data){
                    if(data && data.success){
                        var html = at('search-category-tpl',{"json":data});
                        $("#seachCategoryContent").html(html);
                    }
                });
            }

            //分类点击效果
            doc.on('click','.js-select-goods-category',function(){

                var id = $(this).data("id");
                var name = $(this).data("name");
                var customCat = $(this).data("customcat");

                selectGoodsCategory(id, name, customCat);
                return false;
            });

            function selectGoodsCategory(id, name, customCat) {
                goodsTypeObj.val(name).blur();
                goodsTypeObj.next("[type='hidden']").val(id);

                var nextObjName = goodsTypeObj.next("[type='hidden']").attr("name");

                if(customCat){
                    //自定义配件
                    if(/search_/.test(nextObjName)){
                        goodsTypeObj.next("[type='hidden']").attr("name","search_catId");
                    }else{
                        goodsTypeObj.next("[type='hidden']").attr("name","catId");
                        renderDynamicAttr(true, id);
                    }
                }else{
                    //标准配件
                    if(/search_/.test(nextObjName)){
                        goodsTypeObj.next("[type='hidden']").attr("name","search_stdCatId");
                    }else{
                        goodsTypeObj.next("[type='hidden']").attr("name","stdCatId");
                        renderDynamicAttr(false, id);
                    }
                }
                goodsTypeObj.parent().find("input[name='customCat']").val(customCat)
                        .end().find("input[name='goodsCat']").val(name);
                $(".goods_type_part_wind").remove();

                selectCallback.call(goodsTypeObj[0], id, name, customCat);
            }

        });

        function selectCallback(id, name, customCat) {
            // 选择类别后的回调函数
            // 因为这个弹框的原因，因此调的是全局函数 goodsTypeSelectCallback
            try {
                goodsTypeSelectCallback.call(this, id, name, customCat);
            } catch(e) {

            }
        }
    });
</script>