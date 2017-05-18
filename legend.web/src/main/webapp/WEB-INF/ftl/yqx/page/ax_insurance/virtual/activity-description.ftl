<!-- 活动介绍页-->
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/virtual/activity-description.css?428db71a98827821b93e86961a1bf7ee"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="aside-main main-container">
        <div class="display">
            <img class="img-1" src="${BASE_PATH}/static/img/page/ax_insurance/ins-act-1.png">
            <img class="img-2" src="${BASE_PATH}/static/img/page/ax_insurance/ins-act-2.jpg">
            <div class="btns">
                <a href="${BASE_PATH}/insurance/anxin/flow/insurance-flow?mode=3" class="yqx-btn yqx-btn-2">参加活动</a>
            </div>
        </div>
        <div class="description">
            <h3 class="description-title">车主须知：</h3>
            <ol class="description-list">
                <li>1、参加【买服务包送保险】活动，车主需先预付<strong class="mark">服务包总金额的60%</strong>。</li>
                <li>2、车主预付完<strong class="mark">服务包总金额的60%</strong>后，可以获得一张与服务包预付金额等值的商业险抵价劵，可在后续投保时抵商业险保费。</li>
                <li>3、下次投保时，车主使用商业险抵价劵并支付一定的差额后才可完成投保。</li>
                <li>4、差额的组成包含<strong class="mark">服务包总金额的40%、交强险保费金额、车船税金额、商业险保费差额</strong>。</li>
                <li>
                    5、差额计算公式如下：
                    <div class="sense">
                        <label class="fl mark">场景1：</label>
                        <div class="sense-info">
                            <p class="condition">商业险保费≥服务包总金额</p>
                            <p>应付差额＝商业险保费－服务包预付金额＋其他保费</p>
                        </div>
                    </div>
                    <div class="sense">
                        <label class="fl mark">场景2：</label>
                        <div class="sense-info">
                            <p class="condition">商业险保费＜服务包总金额</p>
                            <p>应付差额＝服务包总金额－服务包预付金额＋其他保费</p>
                        </div>
                    </div>
                </li>
            </ol>
        </div>
    </div>
</div>
</body>
    <!-- 脚本引入区 start -->

    <!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">