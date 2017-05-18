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
    }
    .gd_tab {
        margin-top: 0!important;
    }
    .gd_tab li.item a {
        height:49px;
    }
    .gd_tab li.item a:hover, .gd_tab li.item .current {
        background: url("/legend/resources/images/t1.gif") no-repeat 50%;
    }
    .gd_tab li.itemBtn {
        float: left;
        width:85px;
        height:30px;
        background: #f39800;
        margin:7.5px 10px;
        line-height: 30px;
        text-align: center;
    }

    .itemBtn .appointment {
        color:white;
    }
    .gd_tab li.itemBtn a {
        width: 100%;
        line-height: 30px;
    }
    .redTips:after {
        display: inline;
        content:"*";
        color:red;
        font-size:16px;
        vertical-align: middle;
    }
    .noticeList {
        overflow: hidden;
    }
    .notice,.voidNotice {
        width: 410px;
        height:265px;
        float:left;
        margin:0 10px 10px 0;
        box-sizing: border-box;
        position: relative;
        overflow: hidden;
    }
    .noticeBody {
        height:205px;
        border-top: 1px dotted #e6e6e6;
        border-bottom: 1px solid #cdcdcd;
        position: absolute;
        bottom:0;
        background: white;
        width:100%;
        box-sizing: border-box;
        padding: 20px 8px 0 20px;
    }
    .noticeBody>div {
        /*width:455px;*/
        margin-bottom:10px;
    }
    .noticeBody [type=text],.noticeBody textarea {
        background: white;
        border: 1px solid #c9c9c9;
        border-radius: 5px;
        padding-left: 10px;
        height: 35px;
        /*width:360px;*/
        width: 300px;
    }
    .noticeBody textarea {
        height:60px;
        padding: 5px;
    }
    .noticeBody .Wdate {
        /*width:163px;*/
        width: 133px;
    }
    .voidNotice {
        border: 1px dotted #cdcdcd;
        font-size:120px;
        text-align: center;
        line-height: 250px;
        cursor: pointer;
    }
    .notice img {
        width:100px;
        height:100px;
        margin: 10px 0 0 10px;
    }
    .notice .title {
        text-align: center;
        margin:15px 0;
        color:black;
    }
    .noticeList>:nth-child(2n) {
        margin-right:0;
    }
    .noticeContent {
        float: right;
        margin-top: 12px;
    }
    .noticeContent p {
        margin-bottom:5px;
    }
    .maskBox {
        position: absolute;
        top:0;
        left:0;
        z-index: 5;
        height: 0;
        margin-top: 60px;
        overflow: hidden;
    }
    .activityMask,.activityHoverShow {
        width:410px;
        height:205px;
        background: rgba(0,0,0,0.6);
        filter: opacity(60%);
        overflow: hidden;
        margin-left: -1px;
        /*margin-top:60px;*/
    }
    .activityHoverShow {
        position: absolute;
        top:0;
        left:0;
        background: none;
    }
    /*.heightStart {
        transition:height linear 1s;
        height:205px;
    }
    .heightEnd {
        transition: height linear 1s;
        height:0;
    }*/
    .hoverBody {
        height:205px;
        text-align: center;
    }
    .hoverBody img {
        display: block;
        width: 150px;
        height:150px;
        margin:14px auto;
    }
    .hoverBody p{
        color:white;
        font-size:16px;
        text-align: center;
        margin:0
    }
    .inActivity {
        width:86px;
        height:34px;
        position: absolute;
        right:0;
        font-size:16px;
        background: #ff5f00;
        color:white;
        top: 0px;
        line-height: 34px;
        text-align: center;
    }
    .inActivity:after{
        width:0;
        height:0;
        display:block;
        content:"";
        position: absolute;
        right:0;
        bottom: -10px;
        border-left:43px solid transparent;
        border-right:43px solid transparent;
        border-top:10px solid #ff5f00;
    }
    .gray {
        background: #9e9e9e;
    }
    .gray:after {
        border-top:10px solid #9e9e9e;
    }
    .all {
        width: 100%!important;
    }
    .unBtn {
        cursor: default;
    }
    .noticeType {
        width:130px;
        height:45px;
        float: left;
        text-align: center;
        background: inherit;
        line-height: 32px;
        font-size:18px;
        border-top:1px solid ;
        border-bottom:1px solid ;
        border-left:2px dotted ;
        border-right:2px dotted ;
        padding:5px;
        box-sizing: border-box;
        margin-top:7px;
    }
    .operateBtn {
        float: right;
        background: inherit;
    }
    .operateArea {
        color:white;
    }
    .operateArea>a{
        display: block;
        width:55px;
        height:30px;
        float: right;
        margin-right: 5px;
        background: inherit;
        margin-top: 20px;
        line-height: 30px;
        text-align: center;
        color: inherit;
        border-radius: 5px;
    }
    .typeContent {
        border: 1px dotted white;
    }
    .noticeT1 .operateArea {
        background: #34a8ec!important;
    }
    .noticeT1 .noticeBody {
        border:1px solid #34a8ec!important;
    }
    .noticeT2 .operateArea{
        background: #fda520;
    }
    .noticeT2 .noticeBody{
        border: 1px solid #fda520;
    }
    .noticeT3 .operateArea{
        background: #6881d6;
    }
    .noticeT3 .noticeBody{
        border: 1px solid #6881d6;
    }
    .noticeT4 .operateArea{
        background: #fd6e4d;
    }
    .noticeT4 .noticeBody{
        border: 1px solid #fd6e4d;
    }
    .Wdate {
        background: white!important;
    }
    .startShow h1 {
        margin-top: 25px;
        font-size: 20px;
        font-weight: 600;
    }
    .noticeDes>p {
        margin:10px 0;
        line-height: 1.3;
        font-size: 16px;
    }
    .hide {
        display: none;
    }
    .startEdit>div{
        margin-bottom:10px;
    }
    .refuseReason {
        display: -webkit-box;
        height: 32px;
        line-height: 1.2;
        font-size: 14px;
        color: red;
        overflow: hidden;
        -ms-text-overflow: ellipsis;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
    }

    .scroller {
        width: 407px;
        height: 560px;
        overflow-y: auto;
    }
    .favorableInfo {
        /*width: 377px;*/
        /*height: 500px;*/
        margin: 30px;
    }
    .favorableInfo h2 {
        position: relative;
        height: 40px;
        margin-bottom: 20px;
        line-height: 40px;
        font-size: 24px;
        text-align: center;
        color: #535353;
    }
    .favorableInfo h2:before {
        content: "";
        position: absolute;
        left: 0;
        top: 50%;
        z-index: -1;
        display: block;
        width: 100%;
        border-bottom: 1px solid #ababab;
    }
    .favorableInfo h2 span {
        padding: 8px;
        background: #fff;
    }
    .favorableInfo .info-box {
        margin-bottom: 20px;
    }
    .favorableInfo .info-box:last-child {
        margin-bottom: 0;
    }
    .favorableInfo .info1 {
        margin-bottom: 15px;
        padding-left: 30px;
        overflow: hidden;
    }
    .favorableInfo .info-img {
        float: left;
        margin-left: -30px;
    }
    .favorableInfo .info-title {
        line-height: 26px;
        font-size: 20px;
        font-weight: bold;
        color: #535353;
    }
    .favorableInfo .info2 {
        line-height: 1.2;
        font-size: 16px;
        color: #535353;
    }
    .showCountBox {
        position: relative;
    }
    .showCount {
        position: absolute;
        right: 13px;
        bottom: 5px;
    }
    .Z-title,.Z-title a{
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
        <h3 class="Z-title">客户营销 >  <a href="${BASE_PATH}/marketing/ng/analysis/promotion">门店推广</a> >  <i>发门店公告</i></h3>
        <#include "activity/act_flow.ftl"/>
        <div class="noticeList">

        </div>
    </div>
</div>

<script src="${BASE_PATH}/resources/js/lib/doT.js?970dc9f8d69ff62fb5e4adaf0e1ab3e4"></script>
<script type="text/template" id="temp">
    {{?it.data.length}}
    {{~it.data:v:i}}
    <div class="notice noticeT{{=v.couponType}} ">
        <div class="operateArea">
            <div class="noticeType ">
                <div class="typeContent">
                    {{?v.couponType==1}}
                    满
                    {{??v.couponType==2}}
                    减
                    {{??v.couponType==3}}
                    返
                    {{??v.couponType==4}}
                    赠
                    {{?}}
                </div>
            </div>
                {{?v.couponStatus==1||v.couponStatus==2}}
                <a class="show">预览</a>
                <a class="cancelOpen">撤销发布</a>
                {{??v.couponStatus==0}}
                <a class="edit">编辑</a>
                <a class="open">发布</a>
                <a class="show">预览</a>
                {{??v.couponStatus==3}}
                <a class="edit">编辑</a>
                {{??v.couponStatus==4}}
                <a class="close">下线</a>
                <a class="share">分享</a>
                 <a class="show">预览</a>
                {{??}}
                <a class="save">保存</a>
                {{?}}

        </div>
        <div class="noticeBody" data-coupon-type="{{=v.couponType}}" data-id="{{=v.id}}">
                <div class="startShow {{?!v.id}}hide{{?}} ">
                    {{?v.couponStatus==1 || v.couponStatus==2}}<div class="inActivity">审核中
                    </div>
                    {{??v.couponStatus==3}}
                    <div class="inActivity gray">
                        审核不通过
                    </div>
                    {{??v.couponStatus==4}}
                    <div class="inActivity">
                        活动中
                    </div>
                    {{?}}

                    <h1>{{=v.couponName}}</h1>
                    <div class="noticeDes">
                        <p>{{=v.couponDesc}}</p>

                        <p><small>优惠时间: {{=v.startTimeStr}} 至 {{=(v.endTime <= 4102329000000) ? v.endTimeStr :
                            "永久"}}</small></p>
                    </div>
                    {{?v.couponStatus==3}}
                    <p class="refuseReason">原因:<span>{{=v.reason}}</span></p>
                    {{?}}

                </div>
                <div class="startEdit {{?v.id}}hide{{?}}">
                    <div class="activeName showCountBox">
                        <span class="redTips">活动名称:</span>
                        <input type="text" class="couponName counter" placeholder="
                        {{?v.couponType==1}}
                        会员免费办（限20字）
                        {{??v.couponType==2}}
                        岁末大酬宾（限20字）
                        {{??v.couponType==3}}
                        会员大乱斗（限20字）
                        {{??v.couponType==4}}
                        回馈老客户（限20字）
                        {{?}}
                        " data-maxcount="20" value='{{=(v.couponName != null) ? v.couponName : ""}}'>
                        <span class="showCount"></span>
                    </div>
                    <div class="activeDes showCountBox">
                        <span class="redTips">优惠说明:</span>
                        <textarea class="couponDesc counter" placeholder="
                        {{?v.couponType==1}}
                        凡活动期间进店消费满500者，可免费办理会员卡一张，会员可享受消费9.8折优惠。（限40字）
                        {{??v.couponType==2}}
                        凡活动期间进店消费者，可享受满200立减10，满500立减50。（限40字）
                        {{??v.couponType==3}}
                        活动期间充值会员卡，充200送15，充500送50，充1000送150。（限40字）
                        {{??v.couponType==4}}
                        活动期间，老客户，可享受任意消费每满300元返15元。（例如：满600元则返还30元。）（限40字）
                        {{?}}
                        " data-maxcount="40" cols="30" rows="10">{{=(v.couponDesc != null) ? v.couponDesc : ""}}</textarea>
                        <span class="showCount"></span>
                    </div>
                    <div class="activeTime">
                        <span class="redTips">优惠时间:</span>
                        {{v.s=new Date(v.startTime).toLocaleString().split(' ')[0].replace(/\//g,'-');
                        v.e=new Date(v.endTime).toLocaleString().split(' ')[0].replace(/\//g,'-');
                        }}
                        <input class="Wdate startTimeStr" type="text" style="margin-right: 5px;" onfocus="WdatePicker({minDate: '%y-%M-%d' })"
                               placeholder="默认当天" value='{{=(v.startTime != null) ? v.s : ""}}'/>至<input
                            class="Wdate endTimeStr" type="text" style="margin-left: 5px;" onfocus="WdatePicker({minDate: '%y-%M-%d' })"
                            placeholder="永久" value='{{=(v.endTime != null) ? v.e : ""}}'/>
                    </div>
                </div>
        </div>
        {{?v.couponStatus==4}}
        <div class="maskBox">
            <div class="activityMask"></div>
            <div class="activityHoverShow">
                <div class="hoverBody">
                    <img src="{{=v.codeImgUrl}}" alt="二维码">
                    <p>扫一扫 轻松分享</p>
                </div>
            </div>
        </div>
        {{?}}
    </div>
    {{~}}

    {{??}}
    {{?}}
</script>
<script id="activityNoticeTpl" type="text/html">
    <div class="scroller">
        <div class="favorableInfo">
            <h2>
                <span>店铺信息</span>
            </h2>
            <% for (var i = 0; i < data.length; i++ ) { %>
            <% var item = data[i]; %>
            <% if ( item.id !== undefined ) { %>
            <div class="info-box">
                <div class="info1">
                    <img class="info-img" src="http://tqmall-dandelion-img.oss-cn-hangzhou.aliyuncs.com/images/dandelion/activity/icon/<%= item.couponType %>.png" width="26" height="26"/>
                    <p class="info-title"><%= item.couponName %></p>
                </div>
                <p class="info2"><%= item.couponDesc %><br/>活动时间：<%= item.startTimeStr %>至<%= item.endTimeStr %></p>
            </div>
            <% }} %>
        </div>
    </div>
</script>
<script src="${BASE_PATH}/resources/script/page/marketing/activity/jquery.counter.js?0450fbbb110ec71e3ce63bf754b77166"></script>
<script src="${BASE_PATH}/resources/script/page/marketing/activity/notice_list.js?24e65d651d2ffc6a75dfb8b1d1891d51"></script>
</@>