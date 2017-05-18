/**
 * Created by lixiao on 14-11-5.
 */
$(document).ready(function ($) {

    Model.getGoodsCategoryList = function (opt, v) {
        var params = {};
        params[opt.name] = v;
        var url = BASE_PATH + '/shop/goods_category/list_all';
        Dao.xhrRender({
            action: url,
            params: params,
            tag: 'search_catId',
            tpl: opt.tpl,
            fill: '~'
        }, function () {
            handleChoosenSelect(".chosen");
        });
    };

    Model.getGoodsBrandList = function (opt, v) {
        var params = {};
        params[opt.name] = v;
        var url = BASE_PATH + '/shop/goods_brand/list';
        Dao.xhrRender({
            action: url,
            params: params,
            tag: 'search_brandId',
            tpl: opt.tpl,
            fill: '~'
        }, function () {
            handleChoosenSelect(".chosen");
        });
    };

    Model.getCarModelList1 = function (opt, v) {
        var params = {};
        params[opt.name] = v;
        var url = BASE_PATH + '/shop/car_category/brand_letter';
        Dao.xhrRender({
            action: url,
            params: params,
            tag: 'goods_car_model_1',
            tpl: opt.tpl,
            fill: '~'
        }, function () {
            handleChoosenSelect(".chosen");
        });
    };

    Model.renderSearchActGoodsResult = function () {

        var keyword = $("#search_keyword").val();
        var page = $("#page").val();
        var limit = $("#limit").val();

        var start = (page - 1) * limit;
        var pageLoader = taoqi.loading();

        $.ajax({
            url: BASE_PATH + "/shop/buy/activity_goods/list",
            type: "GET",
            dataType: "json",
            data: {
                search_keyword: keyword,
                start: start,
                limit: limit
            },
            success: function (result) {
                taoqi.close(pageLoader);
                if (result.success) {
                    var data = result.data;
                    if (data == null) {
                        $("#pageDiv").html("");
                    } else {
                        var total = data.total;
                        var list = data.list;
                        page = (start / limit) + 1;
                        var totalPage = Math.ceil(total / limit);
                        $("#page").val(page);
                        var templateHtml = template.render('goodsListTpl', {'templateData': list});
                        $('#goodsListContent').html(templateHtml);


                        //分页开始
                        var morePageHtml = "<ul class='pagination'>";
                        morePageHtml += '<li class="disabled"><a>共' + total + '条记录</a></li>';
                        if (!page.first) {
                            morePageHtml += '<li><a class="first_page" href="javascript:void(0)">首页</a></li>';
                            morePageHtml += '<li><a class="prev_page" href="javascript:void(0)">上一页</a></li>';
                            if ($("#front_prev_page")) {
                                $("#front_prev_page").removeClass("disabled").addClass("prev_page").unbind("click");
                            }
                        } else {
                            morePageHtml += '<li class="disabled"><a>首页</a></li>';
                            morePageHtml += '<li class="disabled"><a>上一页</a></li>';
                            if ($("#front_prev_page")) {
                                $("#front_prev_page").removeClass("prev_page").addClass("disabled").unbind("click");
                            }
                        }
                        var i = 1;
                        if (totalPage < 10) {
                            for (; i <= totalPage; i++) {
                                if (i == start + 1) {
                                    morePageHtml += '<li class="active page"><a href="javascript:;" class="page_' + i + '" href="javascript:void(0)">' + i + '</a></li>';
                                } else {
                                    morePageHtml += '<li class="page"><a href="javascript:;" class="page_' + i + '" href="javascript:void(0)">' + i + '</a></li>';
                                }
                            }
                        } else {
                            for (; i <= totalPage; i++) {
                                morePageHtml += "<li class='page'><a href='javascript:;'>" + i + "</a></li>";
                                if (i == 1 || i == totalPage - 1) {
                                    morePageHtml += "<li><span>...</span></li>";
                                }
                            }
                        }
                        if (page != totalPage) {
                            morePageHtml += '<li><a class="next_page" href="javascript:void(0)">下一页</a></li>';
                            morePageHtml += '<li><a class="last_page" href="javascript:void(0)">末页</a></li>';
                            if ($("#front_last_page")) {
                                $("#front_last_page").removeClass("disabled").addClass("last_page").unbind("click");
                            }
                        } else {
                            morePageHtml += '<li class="disabled"><a>下一页</a></li>';
                            morePageHtml += '<li class="disabled"><a>末页</a></li>';
                            if ($("#front_last_page")) {
                                $("#front_last_page").removeClass("last_page").addClass("disabled").unbind("click");
                            }
                        }
                        morePageHtml += '<li><span style="vertical-align: middle;">共' + totalPage + '页,去第 <input class="ui-page-skipTo go_page_num" type="text" value=""/> 页</span></li>';
                        morePageHtml += '<li><a class="go_page" href="javascript:void(0)">跳转</a></li>';
                        morePageHtml += '</ul>';
                        $("#pageDiv").html(morePageHtml);
                        paginate(page, totalPage);
                        $(".page a").unbind("click").click(function () {
                            $("#page").val($(this).html());
                            Model.renderSearchActGoodsResult()
                        });
                        if (page != 1) {
                            $(".first_page").unbind("click").click(function () {
                                $("#page").val(1);
                                Model.renderSearchActGoodsResult()
                            });
                            $(".prev_page").unbind("click").click(function () {
                                $("#page").val(page - 1);
                                Model.renderSearchActGoodsResult()
                            });
                        }

                        if (page != totalPage) {
                            $(".next_page").unbind("click").click(function () {
                                $("#page").val(page + 1);
                                Model.renderSearchActGoodsResult()
                            });
                            $(".last_page").unbind("click").click(function () {
                                $("#page").val(totalPage);
                                Model.renderSearchActGoodsResult()
                            });
                        }

                        $(".go_page").unbind("click").click(function () {
                            var goNum = parseInt($(".go_page_num").val(), 10);
                            $("#page").val(goNum);
                            Model.renderSearchActGoodsResult();
                        });
                    }
                    //分页结束
                }
                else {
                    $("#goodsListContent").html("");

                }
            }

        })

    }
    template.helper('$jsonToString', function (v) {
        return $.toJSON(v);
    });


});

function view() {

    Model.getGoodsCategoryList({
        tpl : "goods_category_template",
        container : "search_catId"
    });

    Model.getGoodsBrandList({
        tpl : "goods_brand_template",
        container : "search_brandId"
    });

    Model.getCarModelList1({
        name : "pid",
        tpl : "car_model_template_1",
        container : "goods_car_model_1"
    }, 0);


    Model.renderSearchActGoodsResult();

    $("#search_btn").click(function () {
        Model.renderSearchActGoodsResult();
    });

    $(".next_page").click(function () {
        $("#page").val(parseInt($("#page").val()) + 1);
        Model.renderSearchActGoodsResult();
    });

    $(".prev_page").click(function () {
        $("#page").val(parseInt($("#page").val()) - 1);
        Model.renderSearchActGoodsResult();
    });

}

App.run(view);
