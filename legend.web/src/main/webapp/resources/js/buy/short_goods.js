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

    function view() {
        Model.getGoodsCategoryList({
            tpl: "goods_category_template",
            container: "search_catId"
        });

        Model.getGoodsBrandList({
            tpl: "goods_brand_template",
            container: "search_brandId"
        });

        Model.getCarModelList1({
            name: "pid",
            tpl: "car_model_template_1",
            container: "goods_car_model_1"
        }, 0);

        //分页请求使用pagination.js
        var pageNum = 1;
        $("#searchForm").submit(function () {
            pageNum = 1;
            moreData($(this), pageNum, false);
            return false;
        });
        $("#searchForm").submit();

        $("#yun_search_btn").click(function () {
            $("#searchForm").submit();
        });

        $("#tqmall_search_btn").click(function () {
            var q = $("#search_keyword").val();
            var catSecId = $("#search_catId").val();
            var brandId = $("#search_brandId").val();
            var url;
            if (q != '') {
                url = "http://www.tqmall.com/Search.html?q=" + q + "&catSecId=" + catSecId + "&brandId=" + brandId;

            } else {
                var url = "http://www.tqmall.com/List.html?catSecId=" + catSecId + "&brandId=" + brandId;
            }
            window.open(url);

        });


    }

    App.run(view);

});
