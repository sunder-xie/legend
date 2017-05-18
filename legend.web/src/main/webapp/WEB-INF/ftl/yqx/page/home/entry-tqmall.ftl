<#-- tqmall -->
<div class="main">
    <div class="entry tq-entry">
        <h1 class="font-yahei">云修生态区</h1>

        <h3 class="font-yahei">业务入口</h3>

        <div class="guide-back hide">
        </div>
        <#if SESSION_SHOP_LEVEL?number != 10>
        <a class="light-blue wechat entry-item main-item js-check-func"
           data-func-name="微信公众号首页"
           data-href="${BASE_PATH}/shop/wechat?refer=tqmall-home">
            <div class="circle-2">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>微信公众号</h2>

            <h3>互联网+营销解决方案</h3>
            <div class="entry-data-box">
                <div class="has-line">
                    <h4 class="font-arial">${statisticsHome.followerCount}</h4>
                    <p class="font-yahei">本月新增粉丝数</p>
                </div><div class="js-check-func hover-item" title="查看预约单" data-href="${BASE_PATH}/shop/appoint/appoint-list?refer=tqmall-home"
                    data-func-name="接车维修首页">
                    <h4 class="font-arial">${statisticsHome.appointCount}</h4>
                    <p class="font-yahei">待处理预约</p>
                </div>
            </div>
        </a>
        </#if>
        <a class="entry-item shop-manage"
             href="${BASE_PATH}/guide?refer=tqmall-home">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>门店管理</h2>

            <h3 class="js-check-func hover-item"
                title="查看收款单"
                data-func-name="财务首页"
                data-href="${BASE_PATH}/shop/settlement/debit/order-list?refer=tqmall-home">待结算：<i class="font-arial">${statisticsHome.orderCount}</i></h3>
            <div class="qrcode-wrapper">
                <div class="icon"></div>
                <div class="app-qrcode">
                    <div class="angle-bottom angle"></div>
                    <img class="qrcode" src="${BASE_PATH}/static/img/page/home/app-qrcode.png"/>
                </div>
            </div>
        </a>
        <a class="entry-item tqmall js-check-func"
             data-func-name="淘汽档口"
             href="javascript:void(0)"
           data-href="${BASE_PATH}/home/avoid/tqmall/index?refer=tqmall-home" target="_blank">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>淘汽档口</h2>

            <h3>口袋里的汽配城</h3>
        </a>
        <a class="entry-item report js-check-func"
           data-href="${BASE_PATH}/shop/report?refer=tqmall-home" data-func-name="报表首页">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>报表</h2>

            <h3>实时掌握门店经营状况</h3>
        </a>
        <a class="entry-item marketing"
              href="${BASE_PATH}/home/join?refer=tqmall-home#客户营销">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>客户营销</h2>

            <h3>全面挖掘客户价值</h3>
        </a>
        <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11>
        <a class="entry-item tech"
            href="${BASE_PATH}/home/join?refer=tqmall-home#淘汽知道">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>淘汽知道</h2>

            <h3>让天下没有难修的车</h3>
        </a>
        </#if>
        <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11 && SESSION_SHOP_LEVEL?number != 12>
        <a class="entry-item activity"
           href="${BASE_PATH}/home/join?refer=tqmall-home#引流活动">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>引流活动</h2>

            <h3>多渠道集客</h3>
        </a>
        </#if>
        <a class="entry-item setting js-check-func"
             data-func-name="设置首页"
             data-href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-list?refer=tqmall-home">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>设置</h2>

            <h3>系统设置</h3>
        </a>
        <a class="entry-item help-center" href="${BASE_PATH}/shop/help?refer=tqmall-home">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>帮助</h2>

            <h3>点击进入</h3>
        </a>
    </div>
</div>

