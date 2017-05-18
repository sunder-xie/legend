<div id="carType_select_dialog" id="carType_select_dialog" class="qxy_dialog" style="width:900px;">



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
        <div>
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

</div><script id="car_type_1" type="text/template">


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


</script><script id="car_type_2" type="text/template">


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