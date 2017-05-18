$(document).ready(function ($) {
    //获取URL中参数的值
    var getQueryString = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURI(r[2]); return null;
    }
    //把带过来查询关键字写入到搜索框内。
    $(".search_tech_keyword").val(getQueryString("keyWords"));

    //更改技术标题，因标题是份公共文件。暂时用js处理
    $(".title-english").replaceWith('<span style="margin:0 8px;font-size:18px;">-</span><span style="font-weight:900;color:#000;font-size:18px;">搜索列表</span>');
    //模板渲染时截断字符串，v是值，n是截断几位字符。
    template.helper('$substr', function(v,n) {
        if(v == null){
            return "";
        }
        if(!v || v.length < n){
            return v;
        }
        return v.substr(0,n) +'...';
    });

    var loadi = null

    $(".search_tech_keyword").keyup(function(e){
        if(e.keyCode == 13){
            //回车查询
            select();
        }
    });

    $(document).on('click', '.search_tech-btn', function(){
        //点击按钮查询
        select();
    });

    var reqData = function(page,pageSize){
        var keyWords = $(".search_tech_keyword").val();
        $.ajax({
            url:BASE_PATH+"/shop/tech/search/list",
            type:"get",
            data:{"page":page,"search_keywords":keyWords,"size":pageSize},
            beforeSend:function(){
                loadi = layer.load('正在加载，请稍候...',0);
            },
            success:function(json){
                var isSuccess =json["success"];
                var data =json["data"];
                if(isSuccess && data !=null){
                    success(data,pageSize,page);
                }else{
                    loadi && layer.close(loadi);
                    return ;
                }
            }
        });
    }
    
    var select = function(){
        reqData(1,12);
    }

    var success = function(json,pageSize,page){
        loadi && layer.close(loadi);
        $("#content,#pageDiv .qxy_page_inner").empty();
        if(json.response.list && json.response.list.length > 0){
            var html = template('contentTemplate',{"templateData":json.response.list});
            $("#content").html(html);
            $.paging({
                itemSize : json.response.total,
                pageCount : Math.ceil(json.response.total/pageSize),
                current : page,
                backFn : function(p){
                    var scrollTop = $("#content").offset().top;
                    $(document).scrollTop(scrollTop - 90);
                    reqData(p,pageSize);
                }
            });
        }else{
            var keyWords = $(".search_tech_keyword").val();
            var html = '<p>您输入的关键词<span style="color:#f00"> \''+keyWords+'\' </span>未搜索到结果</p>'
            $("#content").html(html);
        }
    }
    //页面首次进入查询一次。
    select();
});