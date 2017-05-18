$(document).ready(function ($) {

    $(document).on('click', '.qsrk_btn', function () {
       var uid = $(this).attr("uid");
        var orderSn = $(this).attr("orderSn");
        window.location.href= BASE_PATH+"/shop/warehouse/stock_in?uid="+uid+"&orderSn="+orderSn;
    });
    $(document).on('click', '.cancel_order_btn', function () {
        var uid = $(this).attr("uid");
        var orderId = $(this).attr("orderId");
        $.ajax({
            url: BASE_PATH + "/shop/buy/my_order",
            type: "GET",
            dataType: "json",
            data: {
                uid: uid,
                orderId: orderId
            },
            cache:false,
            success: function (result) {

            }
        });
    });


    $('.statusKey').click(function () {
       var key = $(this).attr('key');
        $("#status").val(key);
        $(this).addClass("tishi_quanbu");
        $(this).siblings().removeClass("tishi_quanbu");
        Model.renderSearchOrderResult();
    });

	$(document).on('click', '.js-confirm-receive', function () {
	    var data = $(this).data(), that = this;
	    seajs.use('dialog', function (dialog) {
	        dialog.confirm('请仔细查看商品列表，确认商品已全部到货', function () {
                dialog.close();

	            $.get(BASE_PATH + '/shop/buy/confirm_revice', data, function (json) {
	                if(json && json.success) {
	                    dialog.info('确认成功');

                        // 配货中的数目
                        var phz = +$('#PHZ b').text().replace(/[()]/g, '');
                        // 已签收的数目
                        var yqs = +$('#YQS b').text().replace(/[()]/g, '');

                        $('#PHZ b').text('(' + (phz - 1) + ')');
                        $('#YQS b').text('(' + (yqs + 1) + ')');
                        $('.qxy_link.current').click();
	                }
	            })
	        });
	    })
	});

});

Model.renderSearchOrderResult = function () {

    var keyword = $("#search_keyword").val();
    var status = $("#status").val();
    var page = $("#page").val();
    var limit = $("#limit").val();

    var start = (page - 1) * limit;
    var pageLoader = taoqi.loading();

    $.ajax({
        url: BASE_PATH + "/shop/buy/my_order",
        type: "GET",
        dataType: "json",
        data: {
            search_keyword: keyword,
            status: status,
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
                    var templateHtml = template.render('orderListTpl', {'templateData': list});
                    $('#orderListContent').html(templateHtml);

                    $.each($(".hiddenOrderSn"), function (key, obj) {
                        var $hiddenOrderSn = $(this);
                        $.ajax({
                            url: BASE_PATH + "/shop/buy/get_warehouse_in_by_purchase_sn",
                            type: 'GET',
                            dataType: 'JSON',
                            data: {
                                orderSn: this.value
                            },
                            success: function (data) {
                                if (data && data.success == true) {
                                    var result = data.data;
                                    if (result > 0) {
                                        $hiddenOrderSn.next().hide();
                                        $hiddenOrderSn.next().next().show();
                                    } else {
                                        $hiddenOrderSn.next().show();
                                        $hiddenOrderSn.next().next().hide();
                                    }

                                }
                            }
                        });
                    });

                    $.each($(".hiddenTqmallGoodsId"), function (key, obj) {
                        var $hiddenTqmallGoodsId = $(this);
                        var hiddenTqmallGoodsId = $(this).attr('tqmall_goods_id');
                        $.ajax({
                            url: BASE_PATH + "/shop/buy/goods/getGoodsByTqmallGoodsId",
                            type: 'GET',
                            dataType: 'JSON',
                            data: {
                                tqmallGoodsId: hiddenTqmallGoodsId
                            },
                            success: function (data) {
                                if (data && data.success == true) {
                                    var result = data.data;
                                    if(result){
                                        $hiddenTqmallGoodsId.html("<span style='color:red'>配件库已存在</span>");
                                    }else{
                                        $hiddenTqmallGoodsId.html("<span style='color:red;cursor:pointer' onclick=\"window.open('/legend/shop/goods/add_page/ng?goodsId="+hiddenTqmallGoodsId+"')\" >配件库里不存在,请点击添加</span>");
                                    }
                                }
                            }
                        });
                    });

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
                        Model.renderSearchOrderResult()
                    });
                    if (page != 1) {
                        $(".first_page").unbind("click").click(function () {
                            $("#page").val(1);
                            Model.renderSearchOrderResult()
                        });
                        $(".prev_page").unbind("click").click(function () {
                            $("#page").val(page - 1);
                            Model.renderSearchOrderResult()
                        });
                    }

                    if (page != totalPage) {
                        $(".next_page").unbind("click").click(function () {
                            $("#page").val(page + 1);
                            Model.renderSearchOrderResult()
                        });
                        $(".last_page").unbind("click").click(function () {
                            $("#page").val(totalPage);
                            Model.renderSearchOrderResult()
                        });
                    }

                    $(".go_page").unbind("click").click(function () {
                        var goNum = parseInt($(".go_page_num").val(), 10);
                        $("#page").val(goNum);
                        Model.renderSearchOrderResult();
                    });
                }
                //分页结束
            }
            else {
                $("#orderListContent").html("");

            }
        }

    })

}
template.helper('$jsonToString', function (v) {
    return $.toJSON(v);
});


function view() {

    Model.renderSearchOrderResult();

    $("#search_btn").click(function () {
        Model.renderSearchOrderResult();
    });

    $(".next_page").click(function () {
        $("#page").val(parseInt($("#page").val()) + 1);
        Model.renderSearchOrderResult();
    });

    $(".prev_page").click(function () {
        $("#page").val(parseInt($("#page").val()) - 1);
        Model.renderSearchOrderResult();
    });

}

App.run(view);


