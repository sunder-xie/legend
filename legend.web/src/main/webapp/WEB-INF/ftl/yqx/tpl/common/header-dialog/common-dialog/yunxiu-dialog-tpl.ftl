<div class="business-entry common-header-entry" data-tpl-ref="common-dialog/yunxiu-dialog-tpl">
    <div class="angle-top"></div>
    <ul>
        <li>
            <a href="${BASE_PATH}/home?refer=yunxiu-dialog">
                <div class="be-circle">
                    <div class="be-icon be-home"></div>
                </div>
                <p>云修生态</p>
            </a>
        </li>
        <li>
            <a class="js-insurance-item" data-anxin-model="${SESSION_ANXIN_INSURANCE_MODEL}"
               href="${BASE_PATH}/insurance/anxin/flow/insurance-flow?refer=yunxiu-dialog">
            <#--<a class="js-insurance-item"data-anxin-model="${SESSION_ANXIN_INSURANCE_MODEL}" href="${BASE_PATH}/insurance/anxin/train/training-materials?refer=yunxiu-dialog">-->
                <div class="be-circle">
                    <div class="be-icon be-insurance"></div>
                </div>
                <p>汽车保险</p>
            </a>
            <#include "yqx/tpl/common/ansurance-permission-tpl.ftl">
        </li>
        <li>
            <a href="${BASE_PATH}/guide?refer=yunxiu-dialog">
                <div class="be-circle">
                    <div class="be-icon be-shop-manage"></div>
                </div>
                <p>门店管理</p>
            </a>
        </li>
    <#if SESSION_SHOP_LEVEL?number != 10>
        <li><a class="js-check-func"
               data-href="${BASE_PATH}/shop/wechat?refer=yunxiu-dialog" data-func-name="微信公众号">
            <div class="be-circle">
                <div class="be-icon be-wechat"></div>
            </div>
            <p>微信公众号</p>
        </a>
        </li>
    </#if>
        <li><a class="js-check-func"
               data-href="${BASE_PATH}/marketing?refer=yunxiu-dialog" data-func-name="客户营销">
            <div class="be-circle">
                <div class="be-icon be-marketing"></div>
            </div>
            <p>客户营销</p>
        </a>
        </li>
    <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11 && SESSION_SHOP_LEVEL?number != 12>
        <li><a class="js-check-func"
               data-href="${BASE_PATH}/shop/activity/main?refer=yunxiu-dialog" data-func-name="在线引流首页">
            <div class="be-circle">
                <div class="be-icon be-activity"></div>
            </div>
            <p>引流活动</p>
        </a>
        </li>
    </#if>
    <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11>
        <li>
            <a href="${BASE_PATH}/shop/tech/book?refer=yunxiu-dialog">
                <div class="be-circle">
                    <div class="be-icon be-tech"></div>
                </div>
                <p>淘汽知道</p>
            </a>
        </li>
    </#if>
        <li>
            <a href="${BASE_PATH}/home/avoid/epc/index?refer=yunxiu-dialog" target="_blank">
                <div class="be-circle">
                    <div class="be-icon be-epc"></div>
                </div>
                <p>汽配管家</p>
            </a>
        </li>
        <li class="app-wrapper">
            <a href="javascript:void(0)" class="js-header-qrcode js-app" data-src="app">
                <div class="be-circle">
                    <div class="be-icon be-app"></div>
                </div>
                <p class="app-text">商家APP</p>
            </a>
        </li>
        <li class="li-subscription">
            <a href="javascript:void(0)" class="js-header-qrcode js-wx" data-src="subscription">
                <div class="be-circle">
                    <div class="be-icon be-subscription"></div>
                </div>
                <p>淘汽云修订阅号</p>
            </a>
        </li>
        <li>
            <a href="${BASE_PATH}/shop/help?refer=yunxiu-dialog">
                <div class="be-circle">
                    <div class="be-icon be-help"></div>
                </div>
                <p>帮助</p>
            </a>
        </li>
    </ul>
</div>