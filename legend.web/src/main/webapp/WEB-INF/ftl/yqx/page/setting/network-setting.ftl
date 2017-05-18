<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/network-setting.css?c904ff6af5ffb03b70148535daee6751"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">安全登录设置<small>－环境控制设置</small></h3>
        </div>
        <div class="content">
            <div class="network-title">
                设置流程
            </div>
            <div class="process-box clearfix">
                <div class="process fl">
                    <div class="number">1</div>
                    <div class="line"></div>
                    <div class="step">第一步</div>
                    <div class="step-text">
                        <p>将手机连接至门店WIFI</p>
                    </div>
                    <div class="wifi-ico ico1"></div>
                </div>
                <div class="process fl">
                    <div class="number">2</div>
                    <div class="line"></div>
                    <div class="step">第二步</div>
                    <div class="step-text">
                        <p>登录商家版APP</p>
                        <p>商家APP下载二维码</p>
                    </div>
                    <div class="wifi-ico ico2"></div>
                </div>
                <div class="process fl">
                    <div class="number">3</div>
                    <div class="line"></div>
                    <div class="step">第三步</div>
                    <div class="step-text">
                        <p>打开个人－网络设置</p>
                    </div>
                    <div class="wifi-ico ico3"></div>
                </div>
                <div class="process fl">
                    <div class="number">4</div>
                    <div class="line"></div>
                    <div class="step">第四步</div>
                    <div class="step-text">
                        <p>获取当前WIFI环境</p>
                    </div>
                    <div class="wifi-ico ico4"></div>
                </div>
                <div class="process fl">
                    <div class="number">5</div>
                    <div class="line"></div>
                    <div class="step">第五步</div>
                    <div class="step-text">
                        <p>选中需要设置的WIFI</p>
                    </div>
                    <div class="wifi-ico ico5"></div>
                </div>
            </div>
            <div class="network-title b-bot">
                WIFI环境
            </div>
        <#list networkConfig as net>
            <div class="environment">
                <div class="shop-wifi">门店WIFI：</div>

                    <div class="shop-info">${net.wifiName}</div>
                    <div class="shop-text">${net.macAddress}</div>

            </div>
        </#list>
            <div class="btn-box clearfix">
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goBack">返回</button>
            </div>
        </div>
    </div>
</div>
<#include "yqx/layout/footer.ftl">
<script>
    $(function(){
        $(document).on('click','.js-goBack',function(){
            window.location.href = BASE_PATH +'/shop/security-login/level-setting';
        })
    })

</script>
