<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/reg-base.css?9f840e9f3c3f39c54bcbe9b7350edbe9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wx-check.css?66f5f06d3adc5d2e6d8dc0f001424a1f"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl">淘汽云修"微信公众号"开通流程</h1>
            <div class="order-process clearfix fr">
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">申请</p>
                </div>
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">审核</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">3</div>
                    <p class="order-step-title">授权</p>
                </div>
            </div>
        </div>
        <div class="order-body">
        <#if shopWechatVo.shopStatus == 2>
            <div class="content-box">
                <h3>审核中<i class="check-icon"></i></h3>
                <ul class="steps">
                    <li><span>01.</span>
                        <p>您的申请已经提交，正在处理中，请耐心等待。</p></li>
                    <li>
                        <span>02.</span>
                        <p>如认证资料完整，5-7个工作日完成受理
                        </p>
                    </li>
                    <li><span>03.</span>
                        <p>受理期间若有微信电话确认，请配合完成</p>
                    </li>
                    <li><span>04.</span>
                        <p>试用期间，仅开放部分城市，如有疑问，请与云修顾问联系！</p>
                    </li>
                    <li><span>05.</span>
                        <p class="last">如有其它疑问，请致电云修商家客服热线：<i class="icon-phone"></i>400-9937-288</p>
                    </li>
                </ul>
                <div class="tow-dimension-code">
                    <img src="${BASE_PATH}/static/img/page/wechat/apply/apply_2d_code.png" alt=""/>
                    <div class="code-text">
                        <strong>扫描关注
                        [淘汽云修] 官方公众号</strong>
                        <p>了解更多门店微信公众号内容</p>
                    </div>
                </div>
            </div>
        <#elseif shopWechatVo.shopStatus == 5>
            <div class="content-box pass">
                <h3>恭喜您，审核通过<i class="pass-icon"></i></h3>
                <p>下一步将跳转去微信公众平台进行扫码授权，</p>
                <p>授权成功后，您的微信公众号将对接淘汽云修的服务，原有的功能及菜单将被统一替换。</p>
                <button class="yqx-btn yqx-btn-3 js-grant">确认</button>
            </div>
        </#if>
            <div class="content-box affirm" hidden="hidden">
                <h3>授权确认<i class="affirm-icon"></i></h3>
                <p>您确认已经授权成功了么？</p>
                <button class="yqx-btn yqx-btn-3 js-yes marR10">已授权</button>
                <button class="yqx-btn yqx-btn-2 js-no">授权中断</button>
            </div>
        </div>
    </div>
</div>
<!--确认弹窗-->
<script type="text/html" id="grantConfirmTpl">
    <div class="dialog">
        <div class="dialog-title">提示</div>
        <div class="dialog-con">
            <div class="dialog-text">即将去微信公众平台进行扫描授权，
                扫描确认后，原有的功能及菜单将被统一替换。</div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-go-confirm marR20">确认</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small js-go-cancel">取消</button>
            </div>
        </div>
    </div>
</script>
<script src="${BASE_PATH}/static/js/page/wechat/apply/wx-check.js?5b1cd4560d09da9dc267b44827489b2a"></script>
<#include "yqx/layout/footer.ftl" >