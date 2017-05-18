/**
 * Created by lihaizhong on 2015/2/10.
 * 公告分页的js
 */
$(function () {
    var getPageData = function (pageNum) {
        var param = {
            page: pageNum
        };
        seajs.use(["ajax", "art"], function (ajax,art) {
            $.ajax({
                url: BASE_PATH + '/shop/help/notice_list',
                data: param,
                success: function (json) {
                    if (!json.success) {
                        return;
                    } else {
                        var totalPages = json.data.totalPages;
                        $("#totalPages").attr("value", totalPages);
                        $("#totalPages").html("");
                        $("#totalPages").append(totalPages);
                        //当前的页
                        var currentPage = pageNum;
                        if( totalPages === 0 ){
                            currentPage = 0;
                        }
                        $("#currentPage").attr("value", currentPage);
                        $("#currentPage").html("");
                        $("#currentPage").append(currentPage);
                        $("input[name=pageNum]").val(currentPage);
                        //渲染视图
                        var html = art("noticeData", {"templateData": json.data.content});
                        $("#notice").html("");
                        $("#notice").append(html);
                    }

                }
            });
        });
    }

    $("input[name=pageAction]").on("click", function () {
        var pageNum = Number($("input[name=pageNum]").val());
        var totalPages = Number($("#totalPages").attr("value"));
        if (pageNum > totalPages || pageNum <= 0){
            return;
        }
        getPageData(pageNum);
    });

    //上一页
    $("#prePage").on("click", function () {
        var currentPage = Number($("#currentPage").attr("value"));
        if (currentPage <= 1){
            return;
        }
        getPageData(currentPage - 1);
    });

    //下一页
    $("#nextPage").on("click", function () {
        var currentPage = Number($("#currentPage").attr("value"));
        var totalPages = Number($("#totalPages").attr("value"));
        if (currentPage == totalPages){
            return;
        }
        getPageData(currentPage + 1);
    });

    getPageData(1);
});
