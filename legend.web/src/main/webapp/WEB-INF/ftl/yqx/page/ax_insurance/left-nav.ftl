<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/insurance_left_nav.css?48683b38b339dddbf95455e3126d90c3"/>
<div class="Z-left">
    <ul class="left-nav">
        <li class="Z-nav">车险</li>
        <li class="Z-nav-item">
            <dl>
            <#if SESSION_ANXIN_INSURANCE_MODEL == 2 || SESSION_ANXIN_INSURANCE_MODEL == 0>
                <dd class="left_nav_send_insurance"><a class="clearSession"
                                                       href="${BASE_PATH}/insurance/anxin/virtual/flow/activity-description">送保险活动</a>
                </dd>
            </#if>
                <dd class="clearSession"><a href="${BASE_PATH}/insurance/anxin/view/insure-list">查询保单</a></dd>
                <dd class="clearSession creat">
                    <a href="${BASE_PATH}/insurance/offline/list">录入保单</a>
                </dd>
                 <dd class="clearSession creat">
                    <a href="${BASE_PATH}/insurance/anxin/flow/insurance-flow">创建保单</a>
                </dd>
                <div class="creat-Insurance">
                    <a class="clearSession creat-billing"
                       href="${BASE_PATH}/insurance/anxin/flow/insurance-flow">手工开单</a>
                </div>

                <div id="province_nav" class="hide">
                    <dd class="clearSession creat">
                        <a href="${BASE_PATH}/insurance/anxin/cashCoupon/cashIntroductionHome">现金券</a>
                    </dd>
                    <div class="creat-Insurance">
                        <a class="clearSession creat-billing"
                           href="${BASE_PATH}/insurance/anxin/cashCoupon/cashIntroductionHome">获得现金券</a>
                        <a class="clearSession creat-billing" href="${BASE_PATH}/insurance/anxin/cashCoupon/cashCouponList">查询现金券</a>
                    </div>
                </div>
            <#if SESSION_ANXIN_INSURANCE_MODEL == 2 || SESSION_ANXIN_INSURANCE_MODEL == 0>
                <dd><a href="${BASE_PATH}/insurance/anxin/settle/service-list">服务券</a></dd>
                <dd><a class="dialog-toggle ks-training-materials"
                       href="${BASE_PATH}/insurance/anxin/train/training-materials">服务包培训资料</a></dd>
                <#--<dd><a class="dialog-toggle" href="${BASE_PATH}/insurance/anxin/balance">淘汽对账-服务包</a></dd>-->
            </#if>
                <#--<dd><a class="clearSession" href="${BASE_PATH}/insurance/anxin/coupon/coupon-settle">淘汽对账-活动补贴</a></dd>-->
                <#--<dd><a class="dialog-toggle clearSession" href="${BASE_PATH}/insurance/anxin/view/bounty">淘汽对账-奖励金</a></dd>-->
                <dd><a class="dialog-toggle clearSession" href="${BASE_PATH}/insurance/settle/view/index">对账</a></dd>

            <#if SESSION_USER_IS_ADMIN == 1>
                <dd><a class="clearSession" href="${BASE_PATH}/insurance/anxin/settle/bank-card-routing">门店收款账户</a></dd>
            </#if>
            <#if SESSION_ANXIN_INSURANCE_MODEL == 2 || SESSION_ANXIN_INSURANCE_MODEL == 0>
                <dd class="clearSession"><a href="${BASE_PATH}/shop/help?type=1&id=126">帮助</a></dd>
            </#if>
            </dl>
        </li>
    </ul>
</div>

<script type="text/html" id="loginTpl">
    <article class="yqx-dialog">
        <header class="yqx-dialog-header">
            <h1 class="yqx-dialog-headline">密码</h1>
        </header>
        <section class="yqx-dialog-body">
            <div class="dialog-login-content">
                <label class="dialog-login-label">请输入系统登录密码：</label>
                <input type="text" class="yqx-input yqx-input-block ks-dialog-login-pwd" placeholder="请输入"
                       autocomplete="off">

                <p class="dialog-login-error ks-dialog-login-err"></p>

                <div class="dialog-login-btns">
                    <button class="yqx-btn yqx-btn-3 ks-dialog-login-btn">确认</button>
                    <a href="${BASE_PATH}/home" class="yqx-btn yqx-btn-1 ks-go-back-btn">返回</a>
                </div>
            </div>
        </section>
    </article>
</script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/insurance_left_nav.js?ada2c34378b4b0fd4b59c35132fedd29"></script>