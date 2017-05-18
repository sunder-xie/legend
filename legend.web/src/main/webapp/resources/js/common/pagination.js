/**
 * Created by lixiao on 14-9-9.
 */
var templateHtml;

function paginate(page, total) {
    page =  parseInt(page);
    if (total >= 10) {
        $('.pagination .page').hide();
        $('.pagination .page').eq(0).show();
        $('.pagination .page').eq(total - 1).show();
        if (page <= 4) {
            $('.pagination span').eq(0).hide();
            $('.pagination span').eq(1).show();
            $('.pagination .page').eq(page - 1).show().prevAll('.page').show();
            $('.pagination .page').eq(page).show();
            $('.pagination .page').eq(page + 1).show();
        }else if (page > 4 && page <= total - 3) {
            $('.pagination span').show();
            //  $('.pagination .page').eq(page - 3).show();
            $('.pagination .page').eq(page - 2).show();
            $('.pagination .page').eq(page - 1).show();
            $('.pagination .page').eq(page).show();
            $('.pagination .page').eq(page + 1).show();
        } else {
            $('.pagination span').eq(1).hide();
            $('.pagination span').eq(0).show();
            $('.pagination .page').eq(page - 1).show().nextAll('.page').show();
            $('.pagination .page').eq(page - 2).show();
            //  $('.pagination .page').eq(page - 3).show();
        }
    }
    $('.pagination .page').eq(page - 1).addClass('active').siblings().removeClass('active');
}

function moreData(form, pageNum, tpl, callback) {

    if (pageNum == "" || pageNum < 1) {
        var cookiePage = GetCookie(COOKIE_PREFIX + "page");
        if (cookiePage == "") {
            pageNum = 1;
            SetCookie(COOKIE_PREFIX + "page", pageNum);
        } else {
            pageNum = cookiePage;
        }
    } else {
        SetCookie(COOKIE_PREFIX + "page", pageNum);
    }

    $("form input[name='page']").val(pageNum);
    var pageLoader = taoqi.loading();
    form.ajaxSubmit({
        success: function (result) {
            taoqi.close(pageLoader);
            var page = result.data;
            if (page == null) {
                $('#content').html("");
                $("#pageDiv").html("");
            } else {
                var data = page.content;
                if (data.length == 0) {
                    $('#content').html("");
                }
                page.current = eval(page.number);

                if (!page.last) {
                    page.nextNum = eval(page.current + 1 + 1);
                }

                if (!page.first) {
                    page.prevNum = eval(page.current + 1 - 1);
                }
                if (page.totalPages == 0) {
                    return $("#pageDiv").html('');
                }

                var morePageHtml = "<ul class='pagination'>";
                morePageHtml += '<li class="disabled"><a>共' + page.totalElements + '条记录</a></li>';
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
                if (page.totalPages < 10) {
                    for (; i <= page.totalPages; i++) {
                        if (i == page.number + 1) {
                            morePageHtml += '<li class="active page"><a href="javascript:;" class="page_' + i + '" href="javascript:void(0)">' + i + '</a></li>';
                        } else {
                            morePageHtml += '<li class="page"><a href="javascript:;" class="page_' + i + '" href="javascript:void(0)">' + i + '</a></li>';
                        }
                    }
                } else {
                    for (; i <= page.totalPages; i++) {
                        morePageHtml += "<li class='page'><a href='javascript:;'>" + i + "</a></li>";
                        if (i == 1 || i == page.totalPages - 1) {
                            morePageHtml += "<li><span>...</span></li>";
                        }
                    }
                }
                if (!page.last) {
                    morePageHtml += '<li><a class="next_page" href="javascript:void(0)">下一页</a></li>';
                    morePageHtml += '<li><a class="last_page" href="javascript:void(0)">末页</a></li>';
                    if ($("#front_next_page")) {
                        $("#front_next_page").removeClass("disabled").addClass("next_page").unbind("click");
                    }
                } else {
                    morePageHtml += '<li class="disabled"><a>下一页</a></li>';
                    morePageHtml += '<li class="disabled"><a>末页</a></li>';
                    if ($("#front_next_page")) {
                        $("#front_next_page").removeClass("next_page").addClass("disabled").unbind("click");
                    }
                }
                morePageHtml += '<li><span>共' + page.totalPages + '页,去第 <input class="ui-page-skipTo go_page_num" type="text" value=""/> 页</span></li>';
                morePageHtml += '<li><a class="go_page" href="javascript:void(0)">跳转</a></li>';
                morePageHtml += '</ul>';
                $("#pageDiv").html(morePageHtml);
                paginate(pageNum, page.totalPages);
                $(".page", '.pagination').unbind("click").click(function () {
                    moreData($("#searchForm"), parseInt($(this).text(), 10), tpl, callback);
                });
                if (!page.first) {
                    $(".first_page").unbind("click").click(function () {
                        moreData($("#searchForm"), 1, tpl, false, callback);
                    });
                    $(".prev_page").unbind("click").click(function () {
                        moreData($("#searchForm"), page.prevNum, tpl, false,callback);
                    });
                }

                if (!page.last) {
                    $(".next_page").unbind("click").click(function () {
                        moreData($("#searchForm"), page.nextNum, tpl, false,callback);
                    });
                    $(".last_page").unbind("click").click(function () {
                        moreData($("#searchForm"), page.totalPages, tpl, false,callback);
                    });
                }
                if (pageNum > page.totalPages && page.totalPages > 0) {
                    SetCookie(COOKIE_PREFIX + "page", 1);
                    moreData($("#searchForm"), 1, tpl, false,callback);
                }

                $(".go_page").unbind("click").click(function () {
                    var goNum = parseInt($(".go_page_num").val(), 10);
                    goNum = isNaN(goNum) ? 1 : goNum;
                    moreData($("#searchForm"), goNum, tpl, false,callback);
                });
                template.helper('$substring', function(s,v) {
                    if(s != null){
                        return s.substring(0,v);
                    }
                    return "";
                });
                if (tpl != null && tpl) {
                    templateHtml = template.render(tpl, {'templateData': data});
                    $('#content').html(templateHtml);
                } else {
                    templateHtml = template.render('contentTemplate', {'templateData': data});
                    $('#content').html(templateHtml);
                }
                if (callback) {
                    callback();
                }
            }
        },
        cache: false
    });
}
