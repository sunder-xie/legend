<style>
    .link-download {
        width: 140px;
        padding: 10px;
        font-size: 12px;
        color: #333;
        background: url("${BASE_PATH}/static/img/common/order/download_03.png") 98px 13px no-repeat #fff;
        border: 1px solid #ddd;
    }
    .material{border-bottom: 1px solid #ddd;}
    .material .material-title{font-weight: bold; color: #333; line-height: 20px;}
    .material .material-english{font-family: arial, verdana, sans-serif; color: #666; line-height: 20px;}
    .material-list{ margin-top: 10px;}
    .material-list li{ color: #333; line-height: 25px; background: url("${BASE_PATH}/static/img/common/order/download-ico1.png") no-repeat right;}
    .material-list li:hover{color: #85af1d;background: url("${BASE_PATH}/static/img/common/order/download-ico2.png") no-repeat right; }
</style>

<#if SESSION_SHOP_IS_TQMALL_VERSION == 'true'>
    <#if subModule == "account-index"|| subModule =="member-grant"||subModule == "combo-grant"||subModule == "account-setting" || subModule == "account-flow">
        <div role="link" class="link-download">
            <div class="material">
                <p class="material-title">培训资料下载</p>
                <p class="material-english">Download</p>
            </div>
            <ul class="material-list">
                <a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/images/201608/source_img/original_p_147159576260223378.ppt"><li class="js-management">客户管理资料</li></a>
            </ul>
        </div>
    </#if>
    <#if moduleUrl=='wechat'>
        <#--<div role="link" class="link-download">-->
            <#--<div class="material">-->
                <#--<p class="material-title">培训资料下载</p>-->
                <#--<p class="material-english">Download</p>-->
            <#--</div>-->
            <#--<ul class="material-list">-->
                <#--<a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/wechat.ppt"><li class="js-wechat">微信公众号资料</li></a>-->
            <#--</ul>-->
        <#--</div>-->
    </#if>
<#else>
    <div role="link" class="link-download">
        <div class="material">
            <p class="material-title">培训资料下载</p>
            <p class="material-english">Download</p>
        </div>
        <ul class="material-list">
            <a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/accountMarketing.ppt"><li class="js-marketing">客户营销资料</li></a>
            <a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/accountManagement.ppt"><li class="js-management">客户管理资料</li></a>
            <a href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/wechat.ppt"><li class="js-wechat">微信公众号资料</li></a>
        </ul>
    </div>
</#if>

<script>
    $(function(){
        // 点击展开隐藏列表
//        $(document).on('click', '.js-link-download', function () {
//            // 下载ppt
//            window.location.href = 'http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/legend/ppt/jiecheweixiu.ppt';
//        });
    });
</script>