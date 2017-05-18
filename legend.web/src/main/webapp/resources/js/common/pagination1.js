/**
 * Created by sky on 15-7-21.
 */
var templateHtml, params;

function moreData(form, pageNum, extData, tpl, callback) {

    var $content = $("#content"),
        $pageDiv = $("#pageDiv"),
        morePageHtml,
        pNum;

    params = extData;
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

    params.page = pageNum;
    var pageLoader = taoqi.loading();
    form.ajaxSubmit({
        data: params,
        success: function(result) {
            taoqi.close(pageLoader);
            if(!result.success) {
                return false;
            }
            var page = result.data;
            if (page.content.length == 0) {
                var $first_page = $(".first_page"),
                    $prev_page = $(".prev_page"),
                    $next_page = $(".next_page"),
                    $last_page = $(".last_page");
                $content.html("");
                $pageDiv.html("");
                $first_page && $first_page.unbind("click");
                $prev_page && $prev_page.unbind("click");
                $next_page && $next_page.unbind("click");
                $last_page && $last_page.unbind("click");
            } else {
                var data = page.content;

                if (data.length == 0) {
                    $content.html("");
                }
                page.current = eval(page.number);

                if (!page.last) {
                    page.nextNum = eval(page.current + 1 + 1);
                }

                if (!page.first) {
                    page.prevNum = eval(page.current + 1 - 1);
                }
                if (page.totalPages === 0) {
                    return $pageDiv.html('');
                }

                morePageHtml = "<ul class='pagination'>";
                morePageHtml += '<li class="disabled"><a>共' + page.totalElements + '条记录</a></li>';
                if (!page.first) {
                    morePageHtml += '<li><a class="first_page" href="javascript:void(0)">首页</a></li>';
                    morePageHtml += '<li><a class="prev_page" href="javascript:void(0)">上一页</a></li>';
                } else {
                    morePageHtml += '<li class="disabled"><a>首页</a></li>';
                    morePageHtml += '<li class="disabled"><a>上一页</a></li>';
                }
                pNum = Math.min(page.totalPages, 3);
                if(page.first) {
                    for (var i = 0; i < pNum; i++) {
                        morePageHtml += '<li class="'+ (i == 0 ? 'active' : '') +' page">' +
                        '<a href="javascript:;" class="page_' + (pageNum + i) + '" href="javascript:void(0)">' +
                        (pageNum + i) + '</a></li>';
                    }
                } else if(page.last) {
                    for (var i = 0; i < pNum; i++) {
                        morePageHtml += '<li class="'+ (i == Math.ceil(pNum/2) ? 'active' : '') +' page">' +
                        '<a href="javascript:;" class="page_' + (pageNum + i - Math.ceil(pNum/2)) + '" href="javascript:void(0)">' +
                        (pageNum + i - Math.ceil(pNum/2)) + '</a></li>';
                    }
                } else {
                    for (var i = 0; i < pNum; i++) {
                        morePageHtml += '<li class="'+ (i == 1 ? 'active' : '') +' page">' +
                        '<a href="javascript:;" class="page_' + (pageNum + i - 1) + '" href="javascript:void(0)">' +
                        (pageNum + i - 1) + '</a></li>';
                    }
                }
                if (!page.last) {
                    morePageHtml += '<li><a class="next_page" href="javascript:void(0)">下一页</a></li>';
                    morePageHtml += '<li><a class="last_page" href="javascript:void(0)">末页</a></li>';
                } else {
                    morePageHtml += '<li class="disabled"><a>下一页</a></li>';
                    morePageHtml += '<li class="disabled"><a>末页</a></li>';
                }
                morePageHtml += '<li><span>共' + page.totalPages + '页,去第 <input class="ui-page-skipTo go_page_num" type="text" value=""/> 页</span></li>';
                morePageHtml += '<li><a class="go_page" href="javascript:void(0)">跳转</a></li>';
                morePageHtml += '</ul>';
                $pageDiv.html(morePageHtml);

                $(".page").unbind("click").click(function () {
                    var $this = $(this);
                    if($this.hasClass("active")) {
                        return false;
                    }
                    moreData(form, parseInt($this.text(), 10), params, tpl, callback);
                });
                if (!page.first) {
                    $(".first_page").unbind("click").click(function () {
                        moreData(form, 1, params, tpl, callback);
                    });
                    $(".prev_page").unbind("click").click(function () {
                        moreData(form, page.prevNum, params, tpl, callback);
                    });
                } else {
                    $(".first_page").unbind("click");
                    $(".prev_page").unbind("click");
                }

                if (!page.last) {
                    $(".next_page").unbind("click").click(function () {
                        moreData(form, page.nextNum, params, tpl, callback);
                    });
                    $(".last_page").unbind("click").click(function () {
                        moreData(form, page.totalPages, params, tpl, callback);
                    });
                } else {
                    $(".next_page").unbind("click");
                    $(".last_page").unbind("click");
                }

                if (pageNum > page.totalPages && page.totalPages > 0) {
                    SetCookie(COOKIE_PREFIX + "page", 1);
                    moreData(form, 1, params, tpl, callback);
                }

                $(".go_page").unbind("click").click(function () {
                    var goNum = parseInt($(".go_page_num").val(), 10);
                    goNum = isNaN(goNum) ? 1 : goNum;
                    goNum = goNum > page.totalPages ? page.totalPages : goNum;
                    moreData(form, goNum, params, tpl, callback);
                });
                template.helper('$substring', function(s,v) {
                    if(s != null){
                        return s.substring(0,v);
                    }
                    return "";
                });
                if (tpl != null && tpl) {
                    templateHtml = template.render(tpl, {'data': data});
                    $content.html(templateHtml);
                } else {
                    templateHtml = template.render('contentTemplate', {'data': data});
                    $content.html(templateHtml);
                }
                callback&&callback();
            }
        }
    });
}

