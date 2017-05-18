function go2tq() {
    var q = $("#search_keyword").val();
    var catSecId = $("#search_catId").val();
    var brandId = $("#search_brandId").val();
    var url;
    if (q != '') {
        url = "http://www.tqmall.com/Search.html?q=" + q + "&catSecId=" + catSecId + "&brandId=" + brandId;
    } else {
        url = "http://www.tqmall.com/List.html?catSecId=" + catSecId + "&brandId=" + brandId;
    }
    window.open(url);
}