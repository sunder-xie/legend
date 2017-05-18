//模块功能，获取input下拉框的数据
define("legend/resources/online/script/downlist-debug", [ "./ajax-debug", "./dialog-debug", "../libs/layer/layer-debug", "./../libs/artTemplate/artTemplate-debug" ], function(require, exports, module) {
    var ajax = require("./ajax-debug");
    var tpl = require("./../libs/artTemplate/artTemplate-debug");
    var dialog = require("./dialog-debug");
    //var jsonSql = require("jsonSql");
    var list = ".qxy_input_downlist";
    var li = ".list_content li";
    var timer = null;
    var speed = 200;
    //onkeyup延时触发ajax的时间,单位毫秒
    //下拉框兼容ie6\7
    var low_ie = util.isLowIE().isLowIE;
    //编译模板，获取html
    var getHtml = function(obj, json) {
        var hasTemplate = obj.siblings('script[type="text/html"]') || [];
        var template;
        if (hasTemplate.size() < 1) {
            var dataColsJson = JSON.parse(obj.attr("data_cols"));
            var dataColsLen = 0;
            var widthCount = 0;
            for (var name in dataColsJson) {
                //计算下拉框有几列
                dataColsLen++;
            }
            if (dataColsJson["width"]) {
                //减去宽度属性
                for (var i = 0; i < dataColsJson["width"].length; i++) {
                    widthCount += dataColsJson["width"][i];
                }
                dataColsLen -= 1;
            }
            template = [ '<div class="qxy_input_downlist"' + (dataColsJson["width"] ? "" : 'style="width:' + dataColsLen * 150 + 'px"') + ">", '<ul class="list_title">', //循环下拉列表头
            "<% var index=0; %>", "<li " + (dataColsJson["width"] ? 'style="width:' + widthCount + 'px"' : "") + "><% for(var name in dataColsJson){ %>", '<% if(name == "width") {continue;} %>', '<b style="width:', //此处百分比减-1为了兼容ie7
            '<%= (dataColsJson["width"] && dataColsJson["width"][index]+"px;") || ~~(100/dataColsLen) + "%;" %>">', "<%= dataColsJson[name]%></b>", "<% index++ %>", "<%}%></li>", "</ul>", '<ul class="list_content">', //循环下拉列表内容体
            "<%for(var i=0;i<templateData.length;i++){", "var item = templateData[i];", "%>", "<% index=0; %>", "<li><% for(var name in dataColsJson){ %>", '<% if(name == "width") {continue;} %>', '<span style="width:', //此处百分比减-1为了兼容ie7
            '<%= (dataColsJson["width"] && dataColsJson["width"][index]+"px;") || ~~(100/dataColsLen) + "%;" %>">', "<%= item[name]%></span>", "<% index++ %>", "<%}%></li>", "<%}%>", "</ul>", "</div>" ].join("");
            var temp = tpl.compile(template);
            var html = temp({
                templateData: json,
                dataColsJson: dataColsJson,
                dataColsLen: dataColsLen
            });
            return html;
        } else {
            template = hasTemplate.html();
            var temp = tpl.compile(template);
            var html = temp({
                templateData: json
            });
            return html;
        }
    };
    //单击显示下拉列表，每次单击都会从服务器获取新的数据
    $("body").on("click", "input[service_url]", function(e) {
        var $this = $(this);
        var url = $this.attr("service_url");
        var showName = $this.attr("name");
        //输入框提交name(key)
        var showKey = $this.attr("show_key");
        //点击下拉列表每一行后，input内要填入该行的哪一个字段
        var hiddenName = $this.attr("hidden_name");
        //输入框对应的隐藏id
        var hiddenKey = $this.attr("hidden_key");
        //隐藏id对应json里的key
        var searchKey = $this.attr("search_key");
        //要去对应json里搜索的关键字的key
        var remote = $this.attr("remote") || false;
        //是否在服务端进行模糊匹配
        var callbacks = $this.attr("callback_fn");
        //单击li或回车li后的回调方法
        searchKey = searchKey && searchKey.split(",");
        callbacks = eval(callbacks);
        //请求队列序号
        var queueNum = 0;
        var successFnNum = 0;
        var ajaxReq = function(data) {
            ++queueNum;
            ajax.get({
                url: url,
                data: data,
                cache: true,
                beforeSend: beforeSend,
                loadShow: false,
                success: function(json) {
                    ++successFnNum;
                    //console.log(queueNum+"----"+successFnNum);
                    if (queueNum != successFnNum) {
                        return;
                    }
                    success(json);
                }
            });
        };
        //从服务器根据关键字获取数据列表
        var keyupQuery = function(data) {
            //onkeyup延迟speed毫秒再触发ajax请求
            timer != null && window.clearTimeout(timer);
            timer = setTimeout(function() {
                ajaxReq(data);
            }, speed);
        };
        //input单击查询下拉框
        var clickQuery = function(data) {
            ajaxReq(data);
        };
        //input下拉框加载图标
        var beforeSend = function() {};
        //根据配置的字段列表，去获取对应的值，并返回json.用作查询条件
        var getDatas = function(keys) {
            var datas = {};
            var extDatas = $this.attr("ext_data");
            //外部定义的查询条件方法
            var formItem = $this.parents(".form_item");
            if (extDatas) {
                extDatas = extDatas.split(",");
                for (var i = 0; i < extDatas.length; i++) {
                    datas[keys[i]] = $("[name = '" + extDatas[i] + "']", formItem).val();
                }
                return datas;
            }
            if (keys.length == 1) {
                datas[keys[0]] = $this.val();
                return datas;
            }
            for (var i = 0; i < keys.length; i++) {
                var val = $.trim($("[name='" + keys[i] + "']", formItem).val());
                datas[keys[i]] = val;
            }
            return datas;
        };
        //服务器模糊匹配后的回调
        var success = function(json) {
            var hasTemplate = $this.siblings('script[type="text/html"]') || [];
            colseList();
            if (json.success == false) {
                return;
            }
            $this.data("listData", json.data);
            /*----兼容ie7 start----*/
            if (low_ie) {
                $this.parents().css({
                    "z-index": 15
                });
            }
            /*----兼容ie7 end----*/
            $this.after(getHtml($this, json.data));
            $this.next(list).css({
                "min-width": $this.outerWidth() - 2
            });
            //如果下拉列表无数据同时无新增按钮将不显示。
            if ($(li, list).size() <= 0 && hasTemplate.size() < 1) {
                colseList();
            }
            //缓存每条数据到li上
            $(li, list).each(function(i) {
                $(this).data("itemData", json.data[i]);
            });
            //单击下拉列表某一项,填值到input
            $(document).off("click", li).on("click", li, function() {
                enterLi($(this));
            });
        };
        //单击li或回车li后续动作
        var enterLi = function(obj) {
            var item = obj.data("itemData");
            var $list = obj.parents(list);
            //modify by wanghui 20150514
            //if(item[showKey] == $this.val()){
            //dialog.info("请勿重复选择！",5);
            //return;
            //}
            $list.siblings("input[name='" + showName + "']").val(item[showKey]).blur();
            $list.siblings("input[name='" + hiddenName + "']").val(item[hiddenKey]);
            if (typeof callbacks == "function") {
                callbacks($this, item);
            }
        };
        //先关闭其它的下拉框
        colseList();
        e.stopPropagation();
        //获取服务器数据
        clickQuery(getDatas(searchKey));
        var current = 0;
        //keyup查询
        $this.off("keyup").on("keyup", function(e) {
            var listSize = $(li).size();
            var liHeight = $(li).eq(0).height();
            switch (e.keyCode) {
              case 38:
                //键盘向上
                current -= 1;
                current <= 0 && (current = listSize);
                $(li).eq(current - 1).addClass("current").siblings().removeClass("current");
                $(".list_content", list).scrollTop(current * liHeight);
                //滚动条滚动
                break;

              case 40:
                //键盘向下
                current >= listSize && (current = 0);
                $(li).eq(current).addClass("current").siblings().removeClass("current");
                $(".list_content", list).scrollTop(current * liHeight);
                //滚动条滚动
                current += 1;
                break;

              case 13:
                //回车事件
                if ($(list).size() > 0) {
                    $("li.current", list).size() > 0 && enterLi($("li.current", list));
                    colseList();
                } else {
                    keyupQuery(getDatas(searchKey));
                    current = 0;
                }
                break;

              default:
                keyupQuery(getDatas(searchKey));
                current = 0;
            }
        });
    });
    var colseList = function() {
        if (low_ie) {
            $(list).parents().css({
                "z-index": "0"
            });
        }
        $(list).remove();
    };
    //单击空白，关闭下拉列表
    $(document).click(function() {
        colseList();
    });
});