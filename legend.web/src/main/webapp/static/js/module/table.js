/**
 * ch 2016-03-24
 * 表格模块
 */
define(function(require,exports,module){

    var ajax = require('ajax'),
        page = require('paging'),
        art = require('art'),
        fd = require('formData'),
        dg = require('dialog');

    var defaults = {
        //表格数据url，必需
        url: null,
        //表格数据目标填充id，必需
        fillid: null,
        //分页容器id，必需
        pageid: null,
        //表格模板id，必需
        tplid: null,
        // 分页的可选模板
        pageTplId: null,
        //如果模板需要自定义参数,可选
        tpldata: {},
        //扩展参数,可选
        data: {},
        //是否去远程查询数据，可选
        remote: true,
        //传入ajax的参数,可覆盖所有原有的 ajax 参数，
        // 包括上面的 data
        ajax: null,
        // 是否接受的是 json 格式的
        // ajax 的 data 类型
        // dataType: json
        dataType: null,
        //关联查询表单id，可选
        formid: null,
        // 是否提交空数据
        formSubmit: false,
        // 请求 json.success == false 的时候
        // 是否还填充模板，用于更新先前的数据，显示‘暂无数据’
        fallback: false,
        // 多个关联的 form
        // 用逗号隔开，限 id, 不需要带'#'
        relativeForm: null,
        // ajxa page相关字段当前页，条数限制
        pageType: null,
        // 是否首次加载，可选
        isfirstfill: true,
        // 是否返回的是字符串，而不是直接添加到dom
        fullString: false,
        // 是否开启查询条件保存功能
        enableSearchCache: false,
        // function 参数：要设置的参数
        beforeSetSearchCache: null,
        // 是否开启hash功能
        enabledHash: true,
        //渲染表格数据完后的回调方法,可选
        // fullString = true 时，返回 html 字符串
        callback: null
    };

    // page 不作 localStorage 保存
    var _page = null;

    exports.init = function(option){
        var args = $.extend({}, defaults, option);

        if(args.enableSearchCache) {
            var store = searchCache(option.formid);
        }

        if(!_page) {
            var t = location.hash ? +location.hash.replace('#', '') : 1;
            _page = t === t ? t : 1;
            t = null;
        }

        var pageinit = function(curr, data){
            page.init({
                //容器。值支持id,
                dom: $('#'+args.pageid),
                //总记录数
                itemSize : data.totalCount,
                //总页数
                pageCount: data.totalPages,
                //当前页
                current : curr || 1,
                // 分页的模板 id
                pageTplId: args.pageTplId,
                //点击分页后的回调
                backFn: function(p){
                    send(p);
                }
            });
        };

        var send = function(curr){
            curr = curr || 1;
            var html = '', json, $fill;
            var ajaxObj, page = {
                // 当前第几页
                page: curr
                // 默认不传 size
            }, arr;
            var _data;

            if(args.remote){
                var data = {};
                if(args.formid){
                    data = $.extend({}, args.data, fd.get('#'+args.formid, args.formSubmit));
                    if(args.relativeForm) {
                        arr = args.relativeForm.split(',');

                        arr = arr.map(function (e) {
                            return fd.get('#' + e, args.formSubmit);
                        });

                        data = $.extend.bind(null, data).apply(null, arr);
                    }
                }else{
                    if(typeof args.data !== 'function') {
                        data = args.data;
                    } else {
                        data = args.data && args.data();
                    }
                }

                if(!args.url) {
                    return;
                }

                if(args.pageType == 'new') {
                    page = {
                        pageIndex: curr,
                        pageSize: data.size || args.size || 12
                    };

                    delete data.size;
                }

                ajaxObj = $.extend({
                    type : 'get',
                    url : args.url,
                    data : $.extend(page, data)
                }, args.ajax);

                _data = ajaxObj.data;

                if(args.dataType && args.dataType.toLowerCase() === 'json') {
                    ajaxObj.data = JSON.stringify(ajaxObj.data);
                    ajaxObj.contentType = 'application/json';
                }

                $.ajax(ajaxObj).done(function(json){
                    if(json.success){
                        // 总记录数
                        page.totalCount = json.data.totalElements || json.data.totalCount || json.data[args.totalElements] || 0;
                        // 总页数
                        if(json.data.totalPages === 0 || json.data.totalPages) {
                            page.totalPages = json.data.totalPages;
                        } else {
                            page.totalPages = json.data[args.totalPages];
                        }

                        // 新的 java 基类不会返回总页书
                        if(!page.totalPages && page.totalPages !== 0) {
                            page.totalPages = Math.ceil(page.totalCount / page.pageSize) || 0;
                        }
                        html = art(args.tplid,$.extend({
                            json: json,
                            prams: _data
                        }, args.tpldata));

                        if(!args.fullString)
                            $("#"+args.fillid).html(html).data("json",json);

                        pageinit(curr, page);
                        // 替换原来路径，防止产生新路径
                        var newUrl = location.href.replace(/\#.*/, '') + '#' + curr;
                        location.replace(newUrl);

                        args.callback && args.callback(html, json, _data);
                        // 保存当前搜索的字段的值及页码
                        store && store.save();
                    } else {
                        dg.fail(json.errorMsg || json.message || '未请求到列表数据！');
                        if(args.fallback) {
                            html = art(args.tplid, $.extend({json:json}, args.tpldata));

                            $("#"+args.fillid).html(html).data("json",json);
                        }
                    }
                });
            }else{
                $fill = $("#"+args.fillid);
                json = $fill.data('json');
                html = art(args.tplid,$.extend({json:json}, args.tpldata));
                $fill.html(html).data("json",json);
                pageinit(curr,json.data.totalPages,json.data.totalElements);
            }
        };

        /* modify by sky 2016-04-25 判断formid是否存在 */
        var selectors = args.formid != null ? '#'+args.formid+' .js-search-btn' : '.js-search-btn';

        //搜索
        $(document).off('click.table', selectors)
            .on('click.table', selectors, function() {
                if (!args.isfirstfill) {
                    store && store.set();
                    args.isfirstfill = true;
                }

                send(1);
            });

        store && store.set(args.beforeSetSearchCache);
        args.isfirstfill&&send(_page);

        _page = null;

        return send;
    };

    function searchCache(id) {
        var TIME_OUT = 10 * 60 * 1000;
        var key = location.pathname + id;
        var data;
        var _searchData, now = Date.now();

        if(!id && typeof id !== 'string') {
            return;
        }

        if(localStorage.legendTableSearch) {
            try {
                _searchData = JSON.parse(localStorage.legendTableSearch);
            } catch (e) {}
        }
        _searchData = typeof _searchData == 'object' && _searchData
            ? _searchData
            : {};

        // 清除过期数据
        for(var i in _searchData) {
            if(now - _searchData[i].time > TIME_OUT) {
                delete _searchData[i];
            }
        }

        data = _searchData[key] || {
            arr: null,
            time: Date.now()
        };

        function setStorage() {
            _searchData[key] = data;

            localStorage.legendTableSearch = JSON.stringify(_searchData);
        }

        return {
            set: function (fn) {
                if(data.arr) {
                    if(fn && typeof fn === 'function') {
                        fn(data.arr, id);
                    }
                    $('#' + id).find('input').each(function (i) {
                        var t;
                        if ((t = data.arr[i])) {
                            $(this).val(t.value);
                        }
                    });
                }
            },
            save: function () {
                data.arr = [];

                $('#' + id).find('input').each(function (i) {
                    if ($.trim(this.value)) {
                        data.arr[i] = {
                            value: this.value,
                            name: this.name
                        };
                    }
                });

                data.time = Date.now();

                setStorage();
            }
        }
    }

});