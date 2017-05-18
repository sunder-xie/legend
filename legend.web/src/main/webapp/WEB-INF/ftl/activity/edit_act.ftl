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
    .right {
        margin-left: 162px;
    }
    .gd_tab {
        margin-top: 0!important;
    }
    .gd_tab li.item a {
        height:49px;
    }
    .gd_tab li.item a:hover, .gd_tab li.item .current {
        background: url("${BASE_PATH}/resources/images/t1.gif") no-repeat 50%;
    }
    .gd_tab li.itemBtn {
        float: left;
        width:85px;
        height:30px;
        background: #fff;
        margin:7.5px 10px;
        line-height: 30px;
        text-align: center;
    }
    .operateBtns {
        /*margin-left: 22px;*/
        overflow: hidden;
    }
    .operateBtns>a {
        width: 80px;
        height:40px;
        margin-right:10px;
        display: block;
        float: left;
        background: #f77c7e;
        border-radius: 5px;
        line-height: 40px;
        text-align: center;
        color:white;
        font-size:16px;
    }
    .operateBtns>a:last-child {
        background: #90b027;
    }
    .activityEdit {
        border:1px solid #cdcdcd;
        padding:20px;
        /*margin: 10px 0 10px 22px;*/
        margin: 10px 0;
        overflow: hidden;
    }
    .activityEdit [type=text],.activityEdit textarea {
        background: white;
        border:1px solid #c9c9c9;
        border-radius:5px;
        padding-left: 10px;
    }
    .activityEdit>div {
        border-bottom:1px solid #dcdcdc;
    }
    .activityEdit>div:last-child {
        padding-bottom: 0;
        border-bottom: 0;
    }
    .redTips:after {
        display: inline;
        content:"*";
        color:red;
        font-size:16px;
        vertical-align: middle;
    }
    .activityInfo [type=text],.activityInfo textarea {
        width:365px;
        height:36px;
        font-size:18px;
    }
    .activityInfo textarea {
        height:50px;
    }
    .activityInfo>div {
        margin-bottom:10px;
    }
    .banner,.activitySet {
        padding:15px 0;
        overflow: hidden;
    }
    .imgList {
        margin: 10px 0 0 70px;
    }
    .uploadImg {
        width: 216px;
        height:120px;
        font-size:80px;
        line-height: 120px;
        text-align: center;
        cursor: pointer;
        border: 1px dotted #dcdcdc;
        background: white;
    }
    .img_del_btn {
        position: absolute;
        top: -10px;
        right: -10px;
        z-index: 222;
        display: none;
        width: 20px;
        height: 20px;
        background: url("${BASE_PATH}/resources/images/img_del_btn.png") no-repeat center;
    }
    .Wdate {
        background: white!important;
    }
    #startTime,#endTime {
        width:165px;
        height:35px;
        font-size:16px;
    }
    .activitySet>div{
        margin: 10px 0;
        float: left;
        width: 100%;
    }
    .activityTypeList {
        overflow: hidden;
    }
    .activityTypeList span,.activityTypeList>div {
        display: inline-block;
        vertical-align: middle;
    }
    .typeList {
        overflow: hidden;
    }
    .activityType {
        float: left;
        width: 90px;
        height:37px;
        background: white;
        border-radius: 5px;
        line-height: 35px;
        text-align: center;
        color:black;
        font-size:16px;
        margin-right: 10px;
        box-sizing: content-box;
        border: 1px solid #cdcdcd;
        cursor: pointer;
    }
    .typeChecked {
        background: url('/legend/resources/images/typeChecked.png') no-repeat;
    }
    #edui1 {
        z-index: 5!important;
    }
    .activitySet>.activityIndex {
        margin-bottom: 0;
    }
    .wordEdit {
        float: right;
        width: 868px;
    }
    .uploadImg {
        position: relative;
    }
    .uploadImg input[type=file] {
        position: absolute;
        left: 0;
        top: 0;
        z-index: 111;
        width: 100%;
        height: 100%;
        opacity: 0;
        filter: opacity(0);
        cursor: pointer;
    }
    .uploadImg .bannerImg {
        position: absolute;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        z-index: 11;
        display: none;
        max-width: 100%;
        max-height: 100%;
        margin: auto;
    }
    .operateBtns>a.gray {
        color: #fff;!important;
        background: #b1b1b1;!important;
    }
    .refuseReason {
        margin: 10px 0;
        font-size: 14px;
        color: #f00;
    }
</style>
<div class="wrapper">

    <#include "marketing/ng/left_nav.ftl"/>
    <div class="right">
        <div class="operateBtns">
            <#if activity.actStatus == 0>
                <a class="activityOperate open">发布</a>
                <a class="activityOperate edit">保存</a>
            <#elseif activity.actStatus == 1 || activity.actStatus == 2>
                <a class="activityOperate checking gray">审核中</a>
            <#elseif activity.actStatus == 3>
                <a class="activityOperate noPass gray">审核不通过</a>
                <a class="activityOperate edit">保存</a>
            <#elseif activity.actStatus == 4 >
                <a class="activityOperate activity gray">活动中</a>
                <a class="activityOperate offline">下线</a>
            </#if>
            <a class="activityOperate delete">删除</a>
            <a class="activityOperate show preview">预览效果</a>
        </div>
        <#if activity.actStatus == 3 >
            <p class="refuseReason">原因:<span>${activity.reason}</span></p>
        </#if>

        <div class="activityEdit">
            <div class="activityInfo">
                <div>
                    <span class="redTips">活动名称：</span>
                    <input type="hidden" id="id" value="${activity.id}">
                    <input type="hidden" id="actStatus" value="${activity.actStatus}">
                    <input type="text" class="counter" data-maxcount="20" id="actName" value="${activity.actName}">
                    <span class="showCount">限20字</span>
                </div>
                <div>
                    <span class="redTips">活动简介：</span>
                    <textarea rows="3" cols="20" class="counter" data-maxcount="40" id="actDesc">${activity.actDesc}</textarea>
                    <span class="showCount">限40字</span>
                </div>
            </div>
            <div class="banner">
                <span >活动广告图：</span>
                <span>作为活动Banner显示转发时显示消息中</span>
                <div class="imgList">
                    <div class="uploadImg">
                        <a href="javascript:;" class="img_del_btn" <#if activity.imgUrl!=null>style="display:block;"</#if>></a>
                        <input type="file" id="uploadBanner"/>
                        <img class="bannerImg" id="bannerImg" <#if activity.imgUrl!=null>style="display:block;"</#if>
                             src="${activity.imgUrl}"/>
                        +
                    </div>
                </div>
            </div>
            <div class="activitySet">
                <div class="activityTime">
                    <span class="redTips">活动时间：</span>
                    <input id="startTime" class="Wdate" type="text" onfocus="WdatePicker({minDate: '%y-%M-%d' })" placeholder="默认当天" value="${activity.startTime?string('yyyy-MM-dd')}" />
                    至
                    <input id="endTime" class="Wdate" type="text" onfocus="WdatePicker({minDate: '%y-%M-%d' })"
                           placeholder="永久"  value="<#if activity.endTimeLong &gt; 4102329000000>永久<#else>${activity.endTime?string('yyyy-MM-dd')}</#if> "/>
                </div>
                <div class="activityTypeList">
                    <span class="redTips">活动分类：</span>
                    <div class="typeList">
                        <#list activityTypeList as t>
                            <#assign ok=false>
                            <#list activity.actCateList as t2>
                                <#if t2.cateId==t.id>
                                    <#assign ok=true>
                                    <#break>
                                </#if>
                            </#list>
                            <div class="activityType <#if ok>typeChecked</#if>" data-id="${t.id}">${t.name?trim}</div>
                        </#list>
                    </div>
                </div>
                <div class="activityIndex">
                    <span class="redTips">活动首页：</span>
                    <div>
                        <input type="hidden" id="isEdit" value="<#if activity.detailDesc != ''>1<#else>0</#if>"/>
                        <script type="text/plain" id="detailDesc">${activity.detailDesc}</script>
                    </div>
                </div>
            </div>
        </div>
        <div class="operateBtns">
            <#if activity.actStatus == 0>
                <a class="activityOperate open">发布</a>
                <a class="activityOperate edit">保存</a>
            <#elseif activity.actStatus == 1 || activity.actStatus == 2>
                <a class="activityOperate checking gray">审核中</a>
            <#elseif activity.actStatus == 3>
                <a class="activityOperate noPass gray">审核不通过</a>
                <a class="activityOperate edit">保存</a>
                <div><span>${activity.reson}</span></div>
            <#elseif activity.actStatus == 4 >
                <a class="activityOperate activity gray">活动中</a>
                <a class="activityOperate offline">下线</a>
            </#if>
            <a class="activityOperate delete">删除</a>
            <a class="activityOperate show preview">预览效果</a>
        </div>
    </div>
</div>
<script id="editorExample" type="text/html">
    <p style="color: #999;">活动首页，多为优势，突出活动亮点<br/>例如：XXXX店双十一活动，回报新老客户<br/><span
            >　　</span>到店既送XXX，作保养减100，XXXX<br/><span
            >　　</span>更多5重好礼等你来拿！！！<br/>
        <br/>活动项目介绍，可新增多个项目，突出项目优惠<br/>例如：588保养，活动期间再减100<br/><span>　　</span
                >正品云修机油，高端车专用机油，原价600/4L<br/><span>　　</span
                >博士机滤，大牌品质保障，原价80/只<br/><span>　　</span
                >现价只要588，前100位到店在减100<br/><span>　　</span
                >本活动适合30w以上车型<br/><br/>活动规则说明，一般介绍活动时间，地点，以及适合用户<br/>例如：云修XXXX门店，欢迎新老客户到店！<br/><span>　　</span
                >活动时间：11.7日至11.15日<br/><span>　　</span
                >地址：晋江市XXx镇XXx路XXX号<br/><span>　　</span>活动范围：30w以上车型</p>
    <p style="color: #f00;">为了活动更有效果，也可上传图片</p>
</script>
<script src="${BASE_PATH}/resources/script/page/member/upload.js"></script>
<script type="text/javascript" charset="utf-8" src="${BASE_PATH}/resources/js/lib/ueditor/editor_config.js?66bf8a51b1f6240287de9fc04c626c46"></script>
<script type="text/javascript" charset="utf-8" src="${BASE_PATH}/resources/js/lib/ueditor/editor_all.js?646060dcec29e8a5f22640486aab8b00"></script>
<script type="text/javascript" charset="utf-8" src="${BASE_PATH}/resources/js/lib/ueditor/editor_config.js?3d75d77916b277aa0ba4992d17f7254a"></script>
<script type="text/javascript" charset="utf-8" src="${BASE_PATH}/resources/js/lib/ueditor/editor_all.js?5de9295c0318a06251988ce969a9a2fd"></script>
<script src="${BASE_PATH}/resources/js/lib/jQuery-File-Upload-9.9.3/js/vendor/jquery.ui.widget.js"></script>
<script src="${BASE_PATH}/resources/js/lib/jQuery-File-Upload-9.9.3/js/jquery.iframe-transport.js"></script>
<script src="${BASE_PATH}/resources/js/lib/jQuery-File-Upload-9.9.3/js/jquery.fileupload.js"></script>
<script src="${BASE_PATH}/resources/js/lib/My97DatePicker/WdatePicker.js"></script>
<script src="${BASE_PATH}/resources/js/lib/doT.js?970dc9f8d69ff62fb5e4adaf0e1ab3e4"></script>
<script src="${BASE_PATH}/resources/script/page/marketing/activity/jquery.counter.js?0450fbbb110ec71e3ce63bf754b77166"></script>
<script src="${BASE_PATH}/resources/script/page/marketing/activity/add_act.js?5f7a675948106fb7367e312166ea865d"></script>
</@>