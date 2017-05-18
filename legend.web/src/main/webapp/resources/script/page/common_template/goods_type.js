/**
 * Created by sky on 15/12/4.
 */

$(function() {

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

    doc.on('hover','.qxy_input',function(){
        var val = $(this).val();
       $(this).attr('title',val)
    });

    //默认部分配件分类的弹框。
    doc.on('click','input.goods_type_input',function(){
        $(this).val("");
        $(this).next().val("");
        $("input[name='customCat']").val("batch_add_goods.js");
        goodsTypeObj = $(this);
        $(".goods_type_part_wind").remove();
        seajs.use(['dialog','ajax','artTemplate'],function(dg,ax,at){
            var data = {"key":""};
            var href = window.location.href;
            if(/goods\/add_page/.test(href) || /goods\/update_not_tq_page/.test(href)){
                // data.customCat = false;
            }
            $.when(
                ax.get({
                    url:BASE_PATH+"/shop/goods_category/std/get_by_key",
                    data:data
                })
            ).done(function(){
                var html = at('goods_type_part_wind',null);
                var position = goodsTypeObj.position();
                var height = goodsTypeObj.height();
                html = $(html).css({top:position.top + height + 2,left:position.left});
                goodsTypeObj.parent().append(html);
            }).done(function(data){

                var html="";
                if(!data.data){
                    html = data.errorMsg;
                }else{
                    html = at('goods_type_part_result',{"data":data.data});
                }
                $(".search_result_scroll").html(html);
            }).done(function(){
                    getHotCategory('');
                });
        });
        return false;
    });

    //搜索按钮和输入框通用事件。
    var search = function(type){
        seajs.use(['dialog','ajax','artTemplate'],function(dg,ax,at){
            var key = $("#goods_search_input").val();
            var data = {"key":key};
            var href = window.location.href;
            if(/goods\/add_page/.test(href) || /goods\/update_not_tq_page/.test(href)){
                // data.customCat = false;
            }
            $.when(
                ax.get({
                    url:BASE_PATH+"/shop/goods_category/std/get_by_key",
                    loadShow:false,
                    data:data
                })
            ).done(function () {
                    getHotCategory(key)
            }).done(function (data) {
                if (data.success) {
                    var list = data.data;
                    if (type == 1) {
                        //type为1是部分配件弹框的搜索
                        var html = at('goods_type_part_result', {"data": list});
                        if (list.length == 0) {
                            html = '<p>暂无匹配内容</p>';
                        }
                        $(".search_result_scroll").html(html);
                    } else {
                        //全部分类弹框的搜索
                        var html = at('searchContentTpl', {"data": list});
                        if (list.length == 0) {
                            html = '<p class="scroller search_scroller">暂无匹配内容</p>';
                        }
                        $("#navContent").html(html);
                    }
                } else {
                    $(".search_result_scroll,#navContent").html(data.errorMsg);
                }
            });
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
        $("input[name='customCat']").val(customCat);
        $(".goods_type_part_wind").remove();
        return false;
    });
    //单击空白关闭配件下拉框。
    doc.click(function(e){
        if(!$(e.target).parents().hasClass("goods_type_part_wind")){
            $(".goods_type_part_wind").remove();
        }
    });


    doc.on('click', '.goods_all_btn', function() {
            $(".goods_type_part_wind").remove();
            seajs.use(['dialog','ajax','artTemplate'],function(dg,ajax,at) {
                //先关闭部分弹框。
                goodsTypeWind && dg.close(goodsTypeWind);
                var data = {pid: 0};
                var href = window.location.href;
                if(/goods\/add_page/.test(href) || /goods\/update_not_tq_page/.test(href)){
                    // data.customCat = false;
                }
                $.when(
                    ajax.get({
                        url: BASE_PATH + "/shop/goods_category/std/get_by_pid",
                        data: data
                    })
                ).done(function(result){
                    var html = at('goods_type_wrap_tpl',null);
                    goodsTypeWind = dg.dialog({
                        html:html
                    });
                }).done(function(result){
                    render({
                        tag: "navSide",
                        tpl: "navSideTpl"
                    }, result);
                });
            });
        })
        // 一级选项
        .on('click', '.nav_side li', function() {

            var $this = $(this),
                pid = $this.data('catid');
                
            $this.siblings(".cur").removeClass("cur")
                .end().addClass("cur");
            seajs.use(['ajax'], function(ajax) {
                ajax.get({
                    url: BASE_PATH + "/shop/goods_category/std/get_by_pid",
                    data: {
                        pid: pid
                    },
                    success: function( result ) {
                        render({
                            tag: "navContent",
                            tpl: "secondNavContentTpl"
                        }, result);
                    }
                });
            })
        })
        // 二级选项
        .on('click', '.secend_nav_list li', function() {

            var $this = $(this),
                pid = $this.data('catid'),
                catName = $this.text();
            $this.siblings(".cur").removeClass("cur")
                .end().addClass("cur");
            seajs.use(['ajax'], function(ajax) {

                ajax.get({
                    url: BASE_PATH + "/shop/goods_category/std/get_by_pid",
                    data: {
                        pid: pid
                    },
                    success: function( result ) {

                        render({
                            tag: "thirdNavContent",
                            tpl: "thirdNavContentTpl"
                        }, {"data":result.data,"catName":catName});
                    }
                });
            })
        })
        // 三级选项
        .on('click', '.category_items .item', function() {
            seajs.use("dialog",function(dg){
                goodsTypeWind && dg.close(goodsTypeWind);
            });
            var id = $(this).data("catid");
            var name = $.trim($(this).text());
            var customCat = $(this).data("customcat");
            goodsTypeObj.val(name).blur();
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
            goodsTypeObj.next("[type='hidden']").val(id);
            $("input[name='customCat']").val(customCat);
        })
        // 搜索按钮
        .on('click', '.goods_all_search_btn', function() {
            search(2);
        })
        //热门分类选择事件
        .on('click','.js-select-goods-category',function(){

        var id = $(this).data("id");
        var name = $(this).data("name");

        goodsTypeObj.val(name).blur();
        goodsTypeObj.next("[type='hidden']").val(id);

        var nextObjName = goodsTypeObj.next("[type='hidden']").attr("name");
        //标准配件
        if(/search_/.test(nextObjName)){
            goodsTypeObj.next("[type='hidden']").attr("name","search_stdCatId");
        }else{
            goodsTypeObj.next("[type='hidden']").attr("name","stdCatId");
            renderDynamicAttr(false, id);
        }
        $("input[name='customCat']").val(false);
        $(".goods_type_part_wind").remove();
        return false;
    });


    function render( options, data ) {

        seajs.use(['artTemplate'], function( template ) {
            var html = template(options.tpl, data);
            $("#" + options.tag).html( html );
        })
    }

    /**
     * 渲染动态行属性
     * @param customCat
     * @param json
     */
    function renderDynamicAttr(customCat,id){

        var href = window.location.href;
        if(/goods\/add_custom_page\/ng/.test(href) || /goods\/update_not_tq_page\/ng/.test(href)) {
            /**
             * custom category attribute render
             */
            if(customCat){
                /**
                 * 发送异步请求获取动态行属性
                 */
                seajs.use(['ajax', 'artTemplate'], function(ajax, artTemplate) {
                    $.when(ajax.get({
                        url:BASE_PATH + "/shop/goods_attributes/get_by_type_id",
                        data:{'type_id':id}
                    })).done(function(data){
                        if(data && data.success){
                            var attr_list = data.data;
                            $("#dynamicAttr").html('');
                            var template = artTemplate.compile($("#attr_template")
                                .html());
                            var html = template({
                                data: attr_list
                            });
                            $("#dynamicAttr").html(html);
                        } else {
                            alert('获取分类属性列表失败.');
                        }
                    })
                });
            } else {
                /**
                 * standard category attribute render
                 */
                seajs.use(['ajax', 'artTemplate'], function(ajax, artTemplate) {
                    $.when(ajax.get({
                        url:BASE_PATH + "/shop/goods_attributes/get_by_cat_ids",
                        data:{'catIds':id}
                    })).done(function(data){
                        if(data && data.success){
                            var attr_list = data.data;
                            $("#dynamicAttr").html('');
                            var template = artTemplate.compile($("#attr_template")
                                .html());
                            var html = template({
                                data: attr_list
                            });
                            $("#dynamicAttr").html(html);
                        } else {
                            alert('获取分类属性列表失败.');
                        }
                    })
                });
            }
        }
    }

    function getHotCategory(cateName){
        seajs.use([
            'artTemplate',
        ],function(at){
            $.ajax({
                url: BASE_PATH + "/shop/goods/hot/category/list",
                data: {
                    cateName: cateName
                }
            }).done(function (data) {
                if (data && data.success) {
                    var html = at('hotCategoryContentTpl', {"json": data});
                    $("#hotCategoryContent").html(html);
                }
            });

        })
    }
});
