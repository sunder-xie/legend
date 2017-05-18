<div class="business-entry common-header-entry tqmall-common-header-entry">
    <div class="angle-top"></div>
    <ul>
        <li>
            <a href="${BASE_PATH}/home?refer=tqmall-dialog">
                <div class="be-circle">
                    <div class="be-icon be-home"></div>
                </div>
                <p>云修生态</p>
            </a>
        </li>
            <#if SESSION_SHOP_LEVEL?number != 10>
            <li><a class="js-check-func" data-href="${BASE_PATH}/shop/wechat?refer=tqmall-dialog"
            data-func-name="微信公众号首页">
                    <div class="be-circle">
                        <div class="be-icon be-wechat"></div>
                    </div>
                    <p>微信公众号</p>
                </a>
            </li>
            </#if>
            <li>
                <a href="${BASE_PATH}/guide?refer=tqmall-dialog">
                    <div class="be-circle">
                        <div class="be-icon be-shop-manage"></div>
                    </div>
                    <p>门店管理</p>
                </a>
            </li>
            <li><a class="js-check-func" data-href="${BASE_PATH}/home/avoid/tqmall/index?refer=tqmall-dialog"
                   target="_blank"
                data-func-name="淘汽档口">
                    <div class="be-circle">
                        <div class="be-icon be-tqmall"></div>
                    </div>
                    <p>淘汽档口</p>
                </a>
            </li>
            <li><a class="js-check-func" data-href="${BASE_PATH}/shop/report?refer=tqmall-dialog"
                data-func-name="报表首页">
                    <div class="be-circle">
                        <div class="be-icon be-report"></div>
                    </div>
                    <p>报表</p>
                </a>
            </li>
            <li>
                <a href="${BASE_PATH}/home/join?refer=tqmall-dialog#客户营销">
                    <div class="be-circle">
                        <div class="be-icon be-marketing"></div>
                    </div>
                    <p>客户营销</p>
                </a>
            </li>
            <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11>
            <li>
                <a href="${BASE_PATH}/home/join?refer=tqmall-dialog#淘汽知道">
                    <div class="be-circle">
                        <div class="be-icon be-tech"></div>
                    </div>
                    <p>淘汽知道</p>
                </a>
            </li>
            </#if>
            <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11 && SESSION_SHOP_LEVEL?number != 12>
            <li class="js-header-join">
                <a href="${BASE_PATH}/home/join?refer=tqmall-dialog#引流活动">
                    <div class="be-circle">
                        <div class="be-icon be-activity"></div>
                    </div>
                    <p>引流活动</p>
                </a>
            </li>
            </#if>
            <li><a class="js-check-func" data-href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-list?refer=tqmall-dialog"
                data-func-name="设置首页">
                    <div class="be-circle">
                        <div class="be-icon be-setting"></div>
                    </div>
                    <p>设置</p>
                </a>
            </li>
            <li class="app-wrapper" >
                <a href="javascript:void(0)" class="js-header-qrcode" data-src="app">
                    <div class="be-circle">
                        <div class="be-icon be-app"></div>
                    </div>
                    <p class="app-text">商家APP</p>
                </a>
            </li>
            <li class="li-help">
                <a href="${BASE_PATH}/shop/help?refer=tqmall-dialog">
                    <div class="be-circle">
                        <div class="be-icon be-help"></div>
                    </div>
                    <p>帮助中心</p>
                </a>
            </li>
        </ul>
</div>