<#include "activity/common.ftl">

<@Xb>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/marketing.css?7786655fd7d9c6c0f4363e56ceb8ab5d"/>
<script type="application/javascript" src="${BASE_PATH}/resources/script/page/marketing/marketing_common.js?2041a738ed7deab02ce7376d6a73bcde"></script>

<style>
    a:hover {
        text-decoration: none;
    }

    .wrapper {
        width: 980px;
        min-height: 500px;
        margin: 90px auto 120px;
    }

    .aside_main {
        float: right;
        width: 830px;
        /*margin-left: 22px;*/
    }

    .gd_tab {
        margin-top: 0 !important;
    }

    .gd_tab li.item a {
        height: 49px;
    }

    .gd_tab li.item a:hover, .gd_tab li.item .current {
        background: url("${BASE_PATH}/resources/images/t1.gif") no-repeat 50%;
    }

    .gd_tab li.itemBtn {
        float: left;
        width: 85px;
        height: 30px;
        background: #fff;
        margin: 7.5px 10px;
        line-height: 30px;
        text-align: center;
    }

    .gd_tab li.itemBtn a {
        width: 100%;
        line-height: 30px;
    }

    .activityList {
        overflow: hidden;
    }

    .activity, .voidActivity {
        width: 270px;
        height: 250px;
        float: left;
        margin: 0 10px 20px 0;
        background: white;
        border: 1px #cdcdcd solid;
        box-sizing: border-box;
        position: relative;
    }

    .activity:hover {
        border-color: #ff5f00;
    }

    .activity a {
        color: #666;
    }

    .activeBody {
        height: 156px;
        border-top: 1px dotted #e6e6e6;
        border-bottom: 1px solid #cdcdcd;
    }

    .voidActivity {
        border: 1px dotted #cdcdcd;
        font-size: 120px;
        text-align: center;
        line-height: 250px;
        cursor: pointer;
    }

    .activity img {
        width: 100px;
        height: 100px;
        margin: 10px 0 0 10px;
    }

    .activity .title {
        /*text-align: center;*/
        /*margin:15px 0;*/
        padding: 18px 15px;
        font-size: 14px;
        color: #333;
    }

    .activityList :nth-child(3n) {
        margin-right: 0;
    }

    .activityContent {
        float: right;
        width: 148px;
        margin-top: 12px;
        line-height: 1.8;
    }

    .activityContent p {
        margin-bottom: 5px;
    }

    .activity .activeBtn, .activity .hoverBtn {
        width: 100%;
        overflow: hidden;
        height: 40px;
        background: #f0f0f0;
    }

    .activity .activeBtn > a, .activity .hoverBtn > a {
        border-right: 1px solid #fff;
        width: 33%;
        height: 100%;
        float: left;
        text-align: center;
        line-height: 40px;
        color: #ff5f00;
    }

    .activity .activeBtn > a {
        border-right-color: #cdcdcd;
    }

    .activity .hoverBtn > a {
        color: #fff;
        background: #ff5f00;
    }

    .activity .activeBtn > a:last-child, .activity .hoverBtn > a:last-child {
        border-right: none;
    }

    .maskBox {
        position: absolute;
        top: 0;
        left: 0;
        z-index: 5;
        width: 270px;
        height: 0;
        overflow: hidden;
    }

    .activityMask, .activityHoverShow {
        width: 270px;
        height: 249px;
        background: rgba(0, 0, 0, 0.6);
        filter: opacity(60%);
        overflow: hidden;
    }

    .activityHoverShow {
        position: absolute;
        left: 0;
        top: 0;
        background: none;
    }

    /*.heightStart {
        transition:height linear 1s;
        height:249px;
    }
    .heightEnd {
        transition: height linear 1s;
        height:0;
    }*/
    .hoverBody {
        height: 194px;
    }

    .hoverBody img {
        display: block;
        width: 150px;
        height: 150px;
        margin: 14px auto;
    }

    .hoverBody p {
        color: white;
        font-size: 16px;
        text-align: center;
        margin: 0
    }

    .inActivity {
        width: 70px;
        height: 34px;
        position: absolute;
        right: 0;
        font-size: 16px;
        text-align: center;
        background: #ff5f00;
        color: white;
        top: 0;
        line-height: 34px;
    }

    .inActivity:after {
        width: 0;
        height: 0;
        display: block;
        content: "";
        position: absolute;
        right: 0;
        bottom: -10px;
        border-left: 35px solid transparent;
        border-right: 35px solid transparent;
        border-top: 10px solid #ff5f00;
    }

    .all {
        width: 100% !important;
    }

    .unBtn {
        cursor: default;
    }
    .Z-title{
        line-height: 45px;
        font-size: 14px;
        color: #666;
        font-family: "宋体", sans-serif, Verdana, Arial, Helvetica, Tahoma;
    }
    .Z-title a:hover{
        color: #617a19;
    }
    .Z-title i{
        color: #617a19;
        font-weight: bold;
        font-style: normal;
    }

</style>
<div class="wrapper clearfix">
    <#include "marketing/ng/left_nav.ftl"/>
    <div class="aside_main">
        <h3 class="Z-title">客户营销 >  <a href="${BASE_PATH}/marketing/ng/analysis/promotion">门店推广</a> >  <i>办单店活动</i></h3>
        <#include "activity/act_flow.ftl"/>
        <div class="activityList">

        </div>
    </div>
</div>

<script src="${BASE_PATH}/resources/js/lib/doT.js?970dc9f8d69ff62fb5e4adaf0e1ab3e4"></script>
<script type="text/template" id="temp">
    {{?it.data.length}}
    {{~it.data:v:i}}
    <div class="activity">
        <a href="edit_act?actId={{=v.id}}" title="编辑活动">
            <h1 class="title ">
                {{=v.actName}}
                {{?v.actStatus==4}}
                <div class="inActivity">
                    活动中
                </div>
                {{?}}

            </h1>
            <input type="hidden" class="id" value="{{=v.id}}">

            <div class="activeBody">
                <img src="{{=v.imgUrl}}" onerror="this.src='${BASE_PATH}/resources/images/technology_center/search_default.jpg'" alt="活动图片">

                <div class="activityContent">
                    <p>活动时间：<span>{{=v.startTimeStr}}－{{=(v.endTime <= 4102329000000) ? v.endTimeStr :
                                "永久"}}</span></p>

                    <p>地址：{{=v.address}}</p>
                </div>
            </div>
        </a>

        <div class="activeBtn">
            {{?v.actStatus==0}}
            <a class="release">发布活动</a>
            <a class="edit" data-actid="{{=v.id}}">编辑活动</a>
            <a class="delete">删除活动</a>
            {{??v.actStatus==4}}
            <a class="share">分享活动</a>
            <a class="offline">下线活动</a>
            <a class="delete">删除活动</a>
            {{??v.actStatus==2||v.actStatus==1}}
            <a class="checking all unBtn">活动审核中...</a>
            {{??v.actStatus==3}}
            <a class="noPass">审核不通过</a>
            <a class="edit" data-actid="{{=v.id}}">编辑活动</a>
            <a class="delete">删除活动</a>
            {{??}}
            <a class="error">活动状态异常</a>
            {{?}}
        </div>
        {{?v.actStatus==4}}
        <div class="maskBox">
            <div class="activityMask"></div>
            <div class="activityHoverShow">
                <div class="hoverBody">
                    <img src="{{=v.codeImgUrl}}" alt="二维码">

                    <p>扫一扫 轻松分享</p>
                </div>
                <div class="hoverBtn">
                    <a class="share">分享活动</a>
                    <a class="offline">下线活动</a>
                    <a class="delete">删除活动</a>
                </div>
            </div>
        </div>
        {{?}}
    </div>
    {{~}}
    {{?}}
    <div class="voidActivity">
        +
    </div>
</script>
<script>
    $(function () {
        // 全局加载...
        var loading;
        $.ajaxSetup({
            beforeSend: function () {
                loading = taoqi.loading();
            },
            complete: function () {
                taoqi.close(loading);
            }
        });

        $.post('list/ng', function (result) {

            if (result.success) {
                var temp = doT.template($('#temp').html());
                $('.activityList').html(temp(result));
            }
        });

        /*$(document).on('mouseenter','.activity .title,.activity .activeBody',function(){
            $p=$(this).parent();
            var $add=$p.find('.hoverBtn');
            if(!$add.find('div').length){
                $add.append($p.find('.activeBtn>a').clone(false).css('color','white')).css('background','#ff5f00');
            }
            $(this).parent().find('.activityMask,.activityHoverShow').removeClass('heightEnd').addClass('heightStart');
        });
        $(document).on('mouseleave','.activity',function(){
            $(this).parent().find('.activityMask,.activityHoverShow').addClass('heightEnd');
        });*/

        $(document)
                .on('mouseenter', '.activity', function () {

                    var $mask = $(this).find(".maskBox");
                    $mask.stop();
                    $mask.animate({
                        height: "249px"
                    }, {
                        duration: "slow"
                    });
                })
                .on('mouseleave', '.activity', function () {

                    var $mask = $(this).find(".maskBox");
                    $mask.animate({
                        height: "0"
                    }, {
                        duration: "slow"
                    });
                })
                .on('click', '.edit', function () {

                    var actId = $(this).data("actid");
                    taoqi.loading();
                    location.href = "edit_act?actId=" + actId;
                });

        $(document).on('click', '.voidActivity', function () {
            window.open('add_act', '_target');
        });

        //发布活动
        $(document).on('click', '.release', function () {
            var id = $(this).parents('.activity').find('.id').val();//活动ID
            layer.confirm("您确定要发布该活动吗?", function () {
                $.ajax({
                    type: "GET",
                    url: BASE_PATH + "/shop/cz_app/activity/release_act/ng",
                    data: {
                        actId: id
                    },
                    success: function (result) {
                        if (result.success != true) {
                            layer.msg(result.errorMsg, 3, 3);
                            return false;
                        } else {
                            layer.msg(result.data, 2, 1, function () {
                                history.go(0);
                            });
                        }
                    }
                });
            });
            return false;
        });

        //删除活动
        $(document).on('click', '.delete', function () {
            var id = $(this).parents('.activity').find('.id').val();//活动ID
            layer.confirm("您确定要删除该活动吗?", function () {
                $.ajax({
                    type: "GET",
                    url: BASE_PATH + "/shop/cz_app/activity/del_act/ng",
                    data: {
                        actId: id
                    },
                    success: function (result) {
                        if (result.success != true) {
                            layer.msg(result.errorMsg, 3, 3);
                            return false;
                        } else {
                            layer.msg(result.data, 2, 1, function () {
                                history.go(0);
                            });
                        }
                    }
                });
            });
            return false;
        });

        //下线活动
        $(document).on('click', '.offline', function () {
            var id = $(this).parents('.activity').find('.id').val();//活动ID
            layer.confirm("您确定要下线该活动吗?", function () {
                $.ajax({
                    type: "GET",
                    url: BASE_PATH + "/shop/cz_app/activity/off_act/ng",
                    data: {
                        actId: id
                    },
                    success: function (result) {
                        if (result.success != true) {
                            layer.msg(result.errorMsg, 3, 3);
                            return false;
                        } else {
                            layer.msg(result.data, 2, 1, function () {
                                history.go(0);
                            });
                        }
                    }
                });
            });
            return false;
        });

    });
</script>
</@>