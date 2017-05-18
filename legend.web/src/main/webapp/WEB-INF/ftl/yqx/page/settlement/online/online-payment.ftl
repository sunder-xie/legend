<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet"
      href="${BASE_PATH}/static/css/common/settlement/settlement-common.css?f7fbd5813e531726466ffbd40fce89fe"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/settlement/online/online-payment.css?02df1d493ca9dcb7f14de15a27611d8d">
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/settlement/settlement-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h3 class="headline fl">微信支付
            </h3>
        </div>
        <div class="order-content">
            <div class="apply-banner"></div>
            <ul class="process-box font-yahei">
                <li class="process-start process">
                    <i class="circle-num">1</i><i>支付开通申请</i>
                </li><li class="process-end process">
                    <i class="circle-num">2</i><i>支付开通完成</i>
                </li>
            </ul>
            <div class="apply-content apply-info">
                <h4 class="apply-head">微信支付</h4>
                <ul class="description-box margin-left-65">
                    <li>
                        <div class="apply-icon clock">
                            <div class="icon"></div>
                        </div><div class="apply-description">
                            <h5>1.方便快捷</h5>
                            <p>微信在线预约支付</p>
                            <p>到店扫码支付</p>
                        </div>
                    </li><li>
                        <div class="apply-icon person">
                            <div class="icon"></div>
                        </div><div class="apply-description">
                            <h5>2.支付即关注</h5>
                            <p>消费者即可关注公众号</p>
                            <p>强效吸粉，高转化营销</p>
                        </div>
                    </li>
                </ul>
                <div class="btn-box">
                    <button class="yqx-btn yqx-btn-3 btn-apply js-apply" id="apply">立即申请</button>
                </div>
                <div class="description-note font-yahei">
                    <p>备注：a.申请微信支付前，请确保您有已通过认证微信服务号，并已对接云修系统；</p>
                    <p>　　　b.客户通过微信支付付款成功后，微信将收取0.6%的手续费。</p>
                </div>
            </div>
            <div class="apply-content apply-info">
                <h4 class="apply-head">微信支付教程</h4>
                <ul class="description-box guide font-yahei">
                    <li>
                        <ul class="detail-guide">
                            <h6>未开通微信支付</h6>
                            <li>1.请下载以下模板，填写后发送给服务顾问或云修顾问；</li>
                            <li>
                                <a class="yqx-btn download-btn" href="http://tqmall-flash.oss-cn-hangzhou.aliyuncs.com/image/orig_147735906702630965.xlsx" download="支付申请模板.xlsx" id="download_template">下载模板</a>
                            </li>
                            <li>2.提供营业执照原件照片（带彩色盖章件）；</li>
                            <li>3.企业类型请提供组织机构代码证原件（带彩色盖章件）；</li>
                            <li>4.负责人身份证正、反面照片；</li>
                            <li>5.提交资料后配合云修顾问或服务顾问进行注册申请。</li>
                        </ul>
                    </li><li class="margin-left-77">
                        <ul class="detail-guide">
                            <h6>已开通微信支付</h6>
                            <li>提供微信支付APPID；</li>
                            <li>获取方式请见：<a
                                    id="info"
                                    href="http://dwz.cn/4si88F"
                                    target="_blank">http://dwz.cn/4si88F</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
            <div class="apply-content apply-success hide">
                <ul class="description-box guide font-yahei">
                    <li>
                        <img class="wechat-icon" src="${BASE_PATH}/static/img/page/settlement/online/online-payment/wechat-icon.png">
                    </li><li>
                        <h6 style="color: #333;">微信支付</h6>
                        <p>微信支付已开通</p>
                        <p class="wechat-account">
                            微信收款账号：<i id="wechatAccount"></i>
                        </p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/settlement/online/online-payment.js?09618551c8440b572efda0faac5d51d9"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">