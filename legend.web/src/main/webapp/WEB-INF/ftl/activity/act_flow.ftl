<style>
    .flow {
        margin-bottom: 10px;
    }
    .flow li {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
        position: relative;
        float: left;
        width: 25%;
    }
    .flow li:first-child {
        width: 27%;
        border-left: 1px solid #cfcfcf;
    }
    .flow li:last-child {
        width: 48%;
        border-right: 1px solid #cfcfcf;
    }
    .flow li:before, .flow li:after {
        position: absolute;
        top: 0;
        display: block;
        content: "";
        width: 15px;
        height: 48px;
        background: url("${BASE_PATH}/resources/images/flow_sprite.png") 0 0 no-repeat;
    }
    .flow li:before {
        left: 0;
    }
    .flow li:after {
        right: 0;
    }
    .flow li:first-child:before, .flow li:last-child:after {
        display: none;
    }
    .flow li .flow_content {
        height: 46px;
        margin-right: 15px;
        padding-left: 30px;
        border: solid #cfcfcf;
        border-width: 1px 0;
    }
    .flow li:last-child .flow_content {
        margin-right: 0;
        padding-right: 15px;
    }
    .fc1 {
        line-height: 46px;
    }
    .flow li .flow_content:before {
        float: left;
        display: inline-block;
        content: '';
        width: 35px;
        height: 46px;
        margin-right: 5px;
        vertical-align: middle;
        background: url("${BASE_PATH}/resources/images/flow_sprite.png") no-repeat;
    }
    .fc1:before {
        background-position: -2px -58px!important;
    }
    .fc2:before {
        background-position: -2px -100px!important;
    }
    .fc3:before {
        background-position: -2px -142px!important;
    }
    .strong, strong {
        font-size: 14px;
        font-weight: 900;
    }
    .strong {
        margin-top: 9px;
        line-height: 1.2;
    }
</style>
<ul class="flow clearfix">
    <li>
        <div class="flow_content fc1"><strong>发起门店活动</strong></div>
    </li>
    <li>
        <div class="flow_content fc2">
            <p class="strong">活动审核</p>
            <p>需1-2个工作日审核</p>
        </div>
    </li>
    <li>
        <div class="flow_content fc3">
            <p class="strong">活动在车主APP上显示</p>
            <p>系统还会自动通知门店淘汽会员，还可以分享到朋友圈哦！</p>
        </div>
    </li>
</ul>