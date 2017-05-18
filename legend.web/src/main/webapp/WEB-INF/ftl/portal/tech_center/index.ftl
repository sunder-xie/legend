
<#include "layout/portal-header.ftl">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/technology_center/technology_center.css?16bed23434af87680801bae4d2f88432"/>
<div class="banner">
    <div class="tt-box">
        <p class="tt-tit_cn">技术中心</p>

        <p class="tt-tit_eng">Technology Center</p>
    </div>
    <em class="delta"></em>
</div>
<div class="container container1" id="experts_team-box">
    <div class="wrapper">
        <div class="subHead">
            <h2 class="cn">专家团队</h2>

            <p class="summary"><b class="hr"></b><span>强大的专家团队  你永远的技术保障</span><b class="hr"></b></p>
        </div>
        <div class="content">
           <ul class="experts-list"></ul>
        </div>
    </div>
</div>
<div class="container container2" id="repair_database-box">
    <div class="wrapper">
        <div class="subHead">
            <h2 class="cn">维修资料库</h2>

            <p class="summary"><b class="hr"></b><span>已包含13769种车型</span><b class="hr"></b></p>
        </div>
        <div class="content">
            <ul class="car_brand-list"></ul>
        </div>
    </div>
</div>
<div class="container container1" id="special_topic-box">
    <div class="wrapper">
        <div class="subHead">
            <h2 class="cn">技术专题</h2>

            <p class="summary"><b class="hr"></b><span>技术大咖来分享</span><b class="hr"></b></p>
        </div>
        <div class="content topic-content">

        </div>
    </div>
</div>
<div class="container container2" id="technology_train-box">
    <div class="wrapper">
        <div class="subHead">
            <h2 class="cn">技术培训</h2>

            <p class="summary"><b class="hr"></b><span>升职加薪不是梦</span><b class="hr"></b></p>
        </div>
        <div class="content">
            <ul class="tech_train-list">
                <li>
                    <p class="tt_1 tt_class">总裁培训班</p>
                    <p class="tt_summary">互联网化最务实的总裁养成计划</p>
                </li>
                <li>
                    <p class="tt_2 tt_class">服务顾问班</p>
                    <p class="tt_summary">让你的客户一次进店永不流失的秘籍</p>
                </li>
                <li>
                    <p class="tt_3 tt_class">现场管理班</p>
                    <p class="tt_summary">如何快速升级你的门店达到4s店的水准</p>
                </li>
                <li>
                    <p class="tt_4 tt_class">技术总监班</p>
                    <p class="tt_summary">技术总监培训： 5S管理，安全生产，设备资料管理，人员、维修质量管理，高级诊断等。</p>
                </li>
                <li>
                    <p class="tt_5 tt_class">机电维修班</p>
                    <p class="tt_summary">机电技师培训：5S管理，安全生产，发动机、底盘、变速箱、电路维修等。</p>
                </li>
                <li>
                    <p class="tt_6 tt_class">汽车钣金班</p>
                    <p class="tt_summary">钣金技师培训： 5S管理，安全生产，钣金整形技术，钣金矫正工具的使用等。</p>
                </li>
                <li>
                    <p class="tt_7 tt_class">汽车油漆班</p>
                    <p class="tt_summary">油漆技师培训：5S管理，安全生产，油漆调色基础，喷漆技术，打磨设备的使用等。</p>
                </li>
                <li>
                    <p class="tt_8 tt_class">汽车美容班</p>
                    <p class="tt_summary">美容技师培训：5S管理，安全生产，精益洗车，美容产品简介，漆面护理，内饰养护等。</p>
                </li>
            </ul>
        </div>
    </div>
</div>
<div class="container container1" id="repair_equipment-box">
    <div class="subHead">
        <h2 class="cn">维修设备</h2>
        <p class="summary"><b class="hr"></b><span>专业成就品质</span><b class="hr"></b></p>
    </div>
    <div class="content equipments-content">
        <a class="tt_btn left_btn"></a>
        <div class="equipments-box">
            <ul class="equipments-list"></ul>
        </div>
        <a class="tt_btn right_btn"></a>
    </div>

</div>
<!--<div class="footer">
    <ul class="ft_left">
        <li><a href="#">关于淘汽云修</a></li>
        <li><a href="#">官方博</a></li>
        <li><a href="#">诚征英才</a></li>
        <li><a href="#">联系我们</a></li>
        <li><a href="#">开放平台</a></li>
        <li class="ft_none"><a href="#">International Business</a></li>
    </ul>
    <div class="ft_right">
        Copyright © 2014-2015 Tqmall.com 淘汽云修版权所有<br/>
        浙ICP备20120045号
    </div>
</div>-->
<ul class="top_nav">
    <li onclick="window.location.href='#experts_team-box'" class="_experts_team-box cur">专家<br />团队</li>
    <li onclick="window.location.href='#repair_database-box'" class="_repair_database-box">维修<br />资料库</li>
    <li onclick="window.location.href='#special_topic-box'" class="_special_topic-box">技术<br />专题</li>
    <li onclick="window.location.href='#technology_train-box'" class="_technology_train-box">技术<br />培训</li>
    <li onclick="window.location.href='#repair_equipment-box'" class="_repair_equipment-box">维修<br />设备</li>
    <li class="top" onclick="window.location.href='#'">TOP</li>
</ul>
<#include "layout/portal-footer.ftl">
<script id="exportsTpl" type="text/html">
    <%for(var i = 0; i < list.length; i++) {%>
    <li>
        <img class="export_photo" src="<%=list[i].src%>"/>

        <div class="export_info">
            <p class="export_name"><%=list[i].name%></p>

            <p class="export_summary"><%=list[i].summary%></p>
        </div>
    </li>
    <%}%>
</script>
<script id="carBrandTpl" type="text/html">
    <%for(var i = 0; i < list.length; i++) {%>
    <li>
        <img class="car_brand_img" src="<%=list[i].src%>"/>
    </li>
    <%}%>
</script>
<script id="specialTopicTpl" type="text/html">
    <%for(var index in list) {%>
    <%var item = list[index]%>
    <div class="topic_item stb_item">
        <div class="topic_img-box">
            <img class="topic_img" src="<%=item[0].src%>"/>
        </div>
        <div class="topic_info">
            <p class="topic_name"><%=item[0].name%></p>
            <p class="topic_summary"><%=item[0].summary%></p>
        </div>
    </div>
    <% var i = 1, n=4; %>
    <%for(var j=0; j < 2; j++) {%>
    <ul class="topic_item topic-list">
        <%for(; i <  n; i++) {%>
        <li <%if( i == n-1 ) {%>class="last"<%}%>>
        <div class="topic_img-box">
            <img class="topic_img" src="<%=item[i].src%>"/>
        </div>
        <div class="topic_info">
            <p class="topic_name"><%=item[i].name%></p>
            <p class="topic_summary"><%=item[i].summary%></p>
        </div>
        </li>
        <%}%>
        <% n=7; %>
    </ul>
    <%}%>
    <%}%>
</script>
<script id="equipmentsTpl" type="text/html">
    <%for(var i in list) {%>
    <li style="cursor: pointer" onclick="window.open('<%=list[i].url%>')">
        <img src="<%=list[i].src%>"/>
        <p class="part_name"><%=list[i].name%></p>
    </li>
    <%}%>
</script>
<script type="text/javascript">
    $(function () {
        var exports = {
                    list:[
                        {name:"罗河龙", summary:"汽车维修专家技师，从事过林肯、福特、海马、SGMW等车型，有20多年从业经验。", src:BASE_PATH + "/resources/images/technology_center/lhl.jpg"},
                        {name:"郭金", summary:"汽车专家级技师，从事过本田、大众等车型，有10多年从业经验", src:BASE_PATH + "/resources/images/technology_center/gj.jpg"},
                        {name:"刘文江", summary:"宝马专家级认证技师、二级内训师，从事别克，宝马等车型。有10多年从业经验。", src:BASE_PATH + "/resources/images/technology_center/ljw.jpg"},
                        {name:"黄健", summary:"宝马专家级认证技师、从事丰田，宝马等车型，有10多年从业经验。", src:BASE_PATH + "/resources/images/technology_center/hj.jpg"}
                    ]
                },
                carBrands = {
                    list: [
                        {src:BASE_PATH + "/resources/images/technology_center/cp_01.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_02.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_03.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_04.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_05.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_06.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_07.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_08.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_09.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_10.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_11.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_12.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_13.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_14.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_15.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_16.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_17.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_18.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_19.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_20.jpg"},
                        {src:BASE_PATH + "/resources/images/technology_center/cp_21.jpg"}
                    ]
                },
                specialTopics = {
                    list:[
                        [
                            {name:"保养灯消除专题", summary:"根据众多国外最新的汽车维修技术资料编辑整理而成的。34个知名汽车生产厂商的车型资料。", src:BASE_PATH + "/resources/images/technology_center/stb_1.jpg"},
                            {name:"遥控器匹配手册", summary:"汽车防盗系统，是指防止汽车本身或车上的物品被盗所设的系统。", src:BASE_PATH + "/resources/images/technology_center/st_1.jpg"},
                            {name:"汽车正时速查手册", summary:"根据进口汽车的品牌种类和保有量，有针对性地收集整理了70多种...", src:BASE_PATH + "/resources/images/technology_center/st_3.jpg"},
                            {name:"汽车装饰美容", summary:"针对汽车各部位不同材质所需的保养条件，采用不同汽车美容护理...", src:BASE_PATH + "/resources/images/technology_center/st_5.jpg"},
                            {name:"汽车音响解码速查", summary:"当前汽车普遍装备防盗系统和音响系统。当断开音响电源后...", src:BASE_PATH + "/resources/images/technology_center/st_2.jpg"},
                            {name:"汽车波形分析", summary:"主要介绍汽车电喷发动机各传感器、执行器、点火系统等电子元器件...", src:BASE_PATH + "/resources/images/technology_center/st_4.jpg"},
                            {name:"四轮定位专题", summary:"四轮定位是以车辆的四轮参数为依据，通过调整以确保车辆良好的行驶...", src:BASE_PATH + "/resources/images/technology_center/st_6.jpg"},
                        ]
                    ]
                },
                equipments = {
                    list:[
                    <#list techProductList as techProduct>
                        {
                            src:"${techProduct.pic}",
                            name:"${techProduct.name}",
                            url: "${techProduct.tqmallUrl}"
                        },
                    </#list>
//                        {
//                            src:BASE_PATH + "/resources/images/technology_center/equipment_list/2.jpg",
//                            name: "飞鹰 零件车 FYTY903",
//                            url: "http://www.tqmall.com/Goods/detail.html?id=286673"
//                        },
//                        {
//                            src:BASE_PATH + "/resources/images/technology_center/equipment_list/3.jpg",
//                            name: "高昌 埋地小剪平板举升机 GC-3.5S",
//                            url: "http://www.tqmall.com/Goods/detail.html?id=286637"
//                        },
//                        {
//                            src:BASE_PATH + "/resources/images/technology_center/equipment_list/4.jpg",
//                            name: "丰立 气动扳手 FD-2601",
//                            url: "http://www.tqmall.com/Goods/detail.html?id=286675"
//                        },
//                        {
//                            src:BASE_PATH + "/resources/images/technology_center/equipment_list/5.jpg",
//                            name: "南华 尾气分析仪 NHA-506",
//                            url: "http://www.tqmall.com/Goods/detail.html?id=286647"
//                        },
                    ]
                },
                equipmentWidth = 252,
                equipmentLenght = equipments.list.length,
                num = 0;
        init();
        //右侧页内导航 START
        $('.top_nav li').on("click", function () {
            $(this).siblings("li").removeClass("cur");
            if ($(this).hasClass("top")) {
                $('.top_nav li').eq(0).addClass("cur");
            } else {
                $(this).addClass("cur");
            }
        });
        function nav() {
            var curTop = $(window).scrollTop();
            if (curTop < $('.container').eq(0).offset().top) {
                $('.top_nav li').removeClass("cur");
                $('._experts_team-box').addClass("cur");
            } else {
                $('.container').each(function () {
                    if (curTop >= $(this).offset().top - 100) {
                        $('.top_nav li').removeClass("cur");
                        var _li = "._" + $(this).attr("id");
                        $(_li).addClass("cur");
                    }
                });
            }
        }

        $(document).scroll(function () {
            nav();
        });
        //右侧页内导航  END

        function init() {
            //数据填充 START

            $('.experts-list').css("width",exports.list.length*270).html(Dao.html("exportsTpl", exports));
            $('.car_brand-list').html(Dao.html("carBrandTpl", carBrands));
            $('.topic-content').html(Dao.html("specialTopicTpl", specialTopics));
            $('.equipments-list').css("width", equipmentLenght * equipmentWidth).html(Dao.html("equipmentsTpl", equipments));

            //数据填充 END

            nav();
        }

        $('.left_btn').on("click", function() {
            if(num > 0) {
                num--;
                $('.equipments-list').css("margin-left", -num*equipmentWidth + "px");
            }
        });

        $('.right_btn').on("click", function() {
            if(num < equipmentLenght - 4) {
                num++;
                $('.equipments-list').css("margin-left", -num*equipmentWidth + "px");
            }
        });
    });
</script>

