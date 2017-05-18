<#-- 
    选择车型弹框公共模板，用到此模板页面，请在下面登记一下
    ch 2016-04-08
    
    用到的页面：
    新增客户页面: yqx/page/customer/edit.ftl
    洗车单完善页面: yqx/page/order/carwash-perfect.ftl
 -->

<style>
/*---车型选择样式---start---*/
#carType_select_dialog{width:100%;display:none;}
.carType_wraper{padding: 10px 20px;}
.car_select-content{height: 280px; overflow: auto;}
.car_select_show{margin-bottom:10px;}
.car_select_text{float:left;height:30px;line-height:30px;font-size:16px;}
.car_select_show span{display:block;padding:0 10px;border:1px solid #cdcdcd;float:left;margin-right:40px;height:30px;line-height:30px;font-size:14px;position:relative;}
.car_select_show span i{font-style:normal;cursor:pointer;position:absolute;height:30px;width:30px;line-height:30px;text-align:center;font-size:14px;right:-32px;top:-1px;border: 1px solid #cdcdcd;color: #111;}
.car_select_show span i:hover{color:red;}
.car_select{border-bottom:2px solid #d10000;height:40px;}
.car_select li{float:left;height:40px;line-height:40px;padding:0 20px;border:1px solid #CECBCE;cursor:pointer;margin-right:5px;border-bottom:none;font-size:16px;font-weight:700;color:#bbb;}
.car_select li.current{border:2px solid #d10000; border-bottom:0;background:#fff;color:#d10000;}
.car_select li i{display:inline-block;background:#bbb;color:#fff;font-style:normal;width:16px;height:16px;line-height:18px;font-size:14px;vertical-align:middle;text-align:center;border-radius:4px;margin-top:-4px;margin-right:5px;}
.car_select li.current i{color:#fff;background:#d10000;}
.car_type_cont{padding:10px;display:none;height:300px;}
.car_type_cont.current{display: block;}
.car_type_item div.car_type{display:block;float:left;height:40px;line-height:40px;margin:5px;border:1px solid #ebebeb;padding:0 10px;color:#111;cursor:pointer;}
.car_type_item div.car_type img{margin:0;vertical-align:middle;}
.car_type_item div.car_type span{vertical-align:middle;}
.car_type_cont ul li{float:left;}
.car_type_item div.car_type:hover{border-color:#d10000;}
.car_type_index,
.car_type_item{float:left;}
.car_type_index{line-height:40px;height:40px;width:5%;font-size:14px;font-weight:700;}
.letter_box{line-height:40px;font-size:14px;}
.letter_box span{
    box-sizing: border-box;
    display:inline-block;width:21px;height:22px;background:#eee;line-height:22px;text-align:center;color:#888;cursor:pointer;margin-right:0px;border:1px solid #eee;}
.letter_box span.hot{width:40px;margin-left:5px;}
.letter_box span.current{background:#d10000;color:#fff;border-color:#d10000;}
.letter_box span:hover{border-color:#d10000;}
/*---车型选择样式---end---*/
</style>



<div id="carType_select_dialog" data-tpl-ref="car-type-tpl">
    <div class="carType_wraper">
        <div class="car_select_show clearfix">
            <div class="car_select_text">
                已选车型：
            </div>
        </div>
        <ul class="car_select clearfix">
            <li class="current" data-txt="选择品牌"><i>1</i>选择品牌</li>
            <li class="hide" data-txt="选择车系"><i>2</i>选择车系</li>
            <li class="hide" data-txt="选择车型"><i>3</i>选择车型</li>
        </ul>
        <div class="car_select-content">
            <div class="car_type_cont current">
                <div class="letter_box clearfix">
                    品牌字母选择:
                    <span keyword="hot" class="current hot">常用</span>
                    <span keyword="A">A</span>
                    <span keyword="B">B</span>
                    <span keyword="C">C</span>
                    <span keyword="D">D</span>
                    <span keyword="F">F</span>
                    <span keyword="G">G</span>
                    <span keyword="H">H</span>
                    <span keyword="J">J</span>
                    <span keyword="K">K</span>
                    <span keyword="L">L</span>
                    <span keyword="M">M</span>
                    <span keyword="N">N</span>
                    <span keyword="O">O</span>
                    <span keyword="P">P</span>
                    <span keyword="Q">Q</span>
                    <span keyword="R">R</span>
                    <span keyword="S">S</span>
                    <span keyword="T">T</span>
                    <span keyword="W">W</span>
                    <span keyword="X">X</span>
                    <span keyword="Y">Y</span>
                    <span keyword="Z">Z</span>
                    <span keyword="其它" class="hot">其它</span>
                </div>
                <div class="car_type_cont_inner"></div>
            </div>
            <div class="car_type_cont">
                <div class="car_type_cont_inner"></div>
            </div>
            <div class="car_type_cont">
                <div class="car_type_cont_inner"></div>
            </div>
        </div>
    </div>
</div>

<script id="car_type_1" type="text/template">
    <%if(json.data != null){%>
    <div class="car_type_item clearfix">
        <% for (var i in json.data){
        var item = json.data[i];%>
        <div class="car_type" data-pid="<%=item.carBrandId%>">
            <img src="http://tqmall-image.oss-cn-hangzhou.aliyuncs.com/<%=item.logo%>" height="30" />
            <span><b style="font-size:14px;"><%=item.carBrand%></b></span>
        </div>
        <%}%>
    </div>
    <%}%>
</script>

<script id="car_type_2" type="text/template">
    <%if(json.data != null){%>
    <div class="car_type_item clearfix">
        <div class="car_type_box clearfix">
            <%for (var i in json.data){
            var item = json.data[i];%>
            <div class="car_type" data-pid="<%=item.id%>">
                <%if(item.importInfo==null || item.importInfo==""){%>
                <span><b><%=item.name%></b></span>
                <%}else{%>
                <%if(type==2){%>
                <span importinfo="<%=item.importInfo%>"><i style="font-style:normal;font-size:12px;color:#999;">(<%=item.importInfo%>)</i> <b><%=item.name%></b></span>
                <%}else{%>
                <span><b><%=item.name%></b></span>
                <%}%>
                <%}%>
            </div>
            <%}%>
        </div>
    </div>
    <%}%>
</script>


<script>
    function carTypeInit(option){
        seajs.use([
            'ajax',
            'art',
            'dialog'
        ],function(ax, at, dg){
            var $doc = $(document);
            var _btn = null;

            var car_title = $(".car_select li");
            var car_type = $(".car_type_cont");
            //全部品牌数据缓存。
            var PinPaiDataCache = null;
            //热门品牌数据缓存。
            var HotPinPaiDataCache = null;

            var dIndex = null;

            var args = $.extend({
                dom: '',
                callback: null
            },option);
            //车型导航选择
            $doc.off("click.car_select").on("click.car_select",".car_select li",function(){
                var $this = $(this);
                var index = $this.index();
                $this.addClass('current').siblings().removeClass('current');
                car_type.removeClass('current').eq(index).addClass('current');
            });
            //车型选择
            $doc.off("click.car_type").on("click.car_type",".car_type",function(){
                var $this = $(this);
                var pid = $this.data("pid");
                var parents = $this.parents(".car_type_cont");
                var text = $("span",$this).text();
                var index = car_type.index(parents);
                var size = car_title.size();
                var importInfo = $("span",$this).attr("importinfo");
                //已选择的车型类别数量
                var selectedList = $(".car_select_show span");
                var selectedSize = selectedList.size();
                var selectedHtml = '';

                if(importInfo!=null&&importInfo!=""&&importInfo!=undefined){
                    selectedHtml = '<span txt="'+text+'" importinfo="'+importInfo+'" pid="'+pid+'">'+text+'<i>×</i></span>';
                }else{
                    selectedHtml = '<span txt="'+text+'" pid="'+pid+'">'+text+'<i>×</i></span>';
                }
                //请求下一级数据。
                var reqNextData = function(){
                    $.ajax({
                        url:BASE_PATH+"/shop/car_category/ng",
                        data:{"pid":pid},
                        success:function(json){
                            var html = at("car_type_2",{"json":json});
                            car_type.eq(index+1).find(".car_type_cont_inner").html(html);
                        }
                    });
                }
                if((index+1) < selectedSize || ((index+1) == selectedSize && (index+1)!=3)){
                    //已选择数据，做更改操作
                    selectedList.eq(index).nextAll("span").remove();
                    selectedList.eq(index).replaceWith(selectedHtml);
                    reqNextData();

                    //tab内容
                    car_type.eq(index+1)
                        .addClass('current')
                        .siblings()
                        .removeClass('current');
                    //tab标签
                    car_title
                        .eq(index+1)
                        .addClass('current')
                        .siblings()
                        .removeClass('current');

                    car_title
                        .eq(index+1)
                        .nextAll()
                        .hide();

                }else if((index+1) == selectedSize ){
                    //最后一级，只删除当前数据，不请求下一级数据
                    selectedList.eq(index).nextAll("span").remove();
                    selectedList.eq(index).replaceWith(selectedHtml);
                }else{
                    //没选择数据，新增操作
                    if((index+1) < size){
                        $(".car_select_show").append(selectedHtml);
                        //不是最后一级
                        car_type.eq(index+1)
                            .addClass('current')
                            .siblings()
                            .removeClass('current');

                        car_title
                            .eq(index+1)
                            .prevAll()
                            .removeClass('current')
                            .show();

                        car_title
                            .eq(index+1)
                            .addClass('current')
                            .show();

                        //请求下一级数据，并渲染填充html
                        $.ajax({
                            url:BASE_PATH+"/shop/car_category/ng",
                            data:{"pid":pid}
                        }).done(function(json){
                            var html = "";
                            if(index+1 == 1){
                                html = at("car_type_2",{"json":json,"type":2});
                            }else{
                                html = at("car_type_2",{"json":json,"type":0});
                            }
                            car_type.eq(index+1).find(".car_type_cont_inner").html(html);
                        });
                    }else if((index+1) == size){
                        $(".car_select_show").append(selectedHtml);
                        //最后一级，取值，关闭弹窗
                        var str = "";
                        var importInfo = "";
                        var callbackData = {};
                        $("span",".car_select_show").each(function(i){
                            var _txt = $(this).attr("txt");
                            var _id = $(this).attr("pid");

                            if(i==1){
                                var _importInfo = $(this).attr("importinfo");
                                if(_importInfo!=null&&_importInfo!=""&&_importInfo!=undefined){
                                    str += "(" + _importInfo + ") ";
                                    callbackData.importInfo = _importInfo;
                                }
                            }else{
                                str += _txt + " ";
                                callbackData.importInfo = '';
                            }
                            //车型数据分开回填
                            //带前辍的字段是为了兼容客户管理编辑页面的字段
                            switch(i){
                                case 0:
                                    callbackData.carBrandId = _id;
                                    callbackData.carBrand = _txt;
                                    break;
                                case 1:
                                    callbackData.carSeriesId = _id;
                                    callbackData.carSeries = _txt;
                                    break;
                                case 2:
                                    callbackData.carModelId = _id;
                                    callbackData.carModel = _txt;
                                    break;
                            }
                        });
                        //车型数据连起来回填
                        callbackData.carInfoStr = $.trim(str);

                        dIndex && dg.close(dIndex);
                        
                        //选择完成执行回调方法
                        args.callback && args.callback.call(_btn, callbackData);
                    }
                }
            });
            //已选择的删掉
            $doc.off("click.car_select").on("click.car_select",".car_select_show i",function(){
                var index = $(".car_select_show i").index($(this));
                var parents = $(this).parents("span");
                parents.nextAll("span").remove();
                parents.remove();

                //tab内容
                car_type.eq(index)
                    .addClass('current')
                    .siblings()
                    .removeClass('current');
                //tab标签
                car_title
                    .eq(index)
                    .addClass('current')
                    .siblings()
                    .removeClass('current')

                car_title
                    .eq(index)
                    .nextAll()
                    .hide();
            });

            //删除掉热门品牌图片地址host
            var changeImgUrlData = function(json){
                var _json = null;
                if(json && json.data){
                    for(var i=0;i<json.data.length;i++){
                        var imgUrl = json.data[i].logo;
                        if(imgUrl != null){
                            imgUrl = imgUrl.substring(imgUrl.indexOf(".com/")+5);
                            json.data[i].logo = imgUrl;
                        }else{
                            delete json.data[i];
                        }
                    }
                    _json = json;
                }
                return _json;
            }

            //字母过滤品牌
            $doc.off("click.letter").on("click.letter",".letter_box span",function(){
                $(this).addClass('current').siblings().removeClass('current');
                var keyword = $(this).attr("keyword");
                if(keyword == "hot"){
                    //热门车型
                    if(HotPinPaiDataCache){
                        var html = at("car_type_1",{"json":HotPinPaiDataCache});
                        car_type.eq(0).find(".car_type_cont_inner").html(html);
                    }
                }else{
                    //根据字母筛选车型
                    if(PinPaiDataCache){
                        var obj = {};
                        obj.data = PinPaiDataCache.data[keyword];

                        //处理字母筛选数据与常用车型数据格式和字段名不一致问题
                        for(var i=0;i<obj.data.length;i++){
                            obj.data[i].carBrandId = obj.data[i].id;
                            obj.data[i].carBrand = obj.data[i].name;
                        }
                        var html = at("car_type_1",{"json":obj});
                        car_type.eq(0).find(".car_type_cont_inner").html(html);
                    }
                }
            });

            //首次访问加载热门数据
            at.helper('$toStr', function (obj) {
                return JSON.stringify(obj);
            });
            //获取热门或者常用车型品牌
            $.ajax({
                url : BASE_PATH + "/shop/car_category/comm_brand"
            }).done(function(json){
                HotPinPaiDataCache = changeImgUrlData(json);
                var html = at("car_type_1",{"json":HotPinPaiDataCache});
                car_type.eq(0).find(".car_type_cont_inner").html(html);
            });
            //获取所有车型品牌
            $.ajax({
                url:BASE_PATH + '/shop/car_category/brand_letter'
            }).done(function(json){
                if(json.success){
                    PinPaiDataCache = json;
                }
            });

            //绑定车型选择框
            $doc.off("click.carType").on("click.carType",args.dom,function(){
                _btn = this;
                dIndex = dg.open({
                    content: $("#carType_select_dialog"),
                    area: ['900px', 'auto'],
                    end: function(){
                        clearCarDataHistory();
                    }
                });
            });

            //车型选择框初始化
            function clearCarDataHistory(){
                //清除已选车型数据
                $("span",".car_select_show").remove();
                //只保留第一级tab标签。
                $("li:gt(0)",".car_select").hide();
                $("li",".car_select").eq(0).addClass("current");
                $(".car_type_cont:gt(0)").removeClass('current');
                $(".car_type_cont").eq(0).addClass('current');
                //品牌筛选定位到第一级。
                $("span",".letter_box").removeClass('current').eq(0).addClass('current');

                $(".letter_box span:eq(0)").trigger('click');
            }

        });
    };
</script>