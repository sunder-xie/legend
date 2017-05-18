<div class="main">
    <div class="entry">
        <h1 class="font-yahei">云修生态区</h1>

        <h3 class="font-yahei">业务入口</h3>

        <div class="guide-back hide home-guide">
        </div>
        <a class="light-blue shop-manage entry-item main-item" href="${BASE_PATH}/guide?refer=yunxiu-home">
            <div class="circle-2">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>门店管理</h2>

            <h3>全国性的汽修连锁品牌</h3>

            <h3>只为更好的服务中国车主</h3>

            <p class="entry-enter">点击进入</p>
        </a>
    <a class="entry-item insurance js-insurance-item " href="${BASE_PATH}/insurance/anxin/flow/insurance-flow?refer=yunxiu-home" data-anxin-model="${SESSION_ANXIN_INSURANCE_MODEL}">
        <!--<a class="entry-item insurance js-insurance-item" href="${BASE_PATH}/insurance/anxin/train/training-materials?refer=yunxiu-home" data-anxin-model="${SESSION_ANXIN_INSURANCE_MODEL}">-->
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>汽车保险</h2>

            <h3>做最让车主安心的车险</h3>
        </a>
        <#if SESSION_SHOP_LEVEL?number != 10>
        <a class="entry-item wechat js-check-func" href="javascript:void(0)" data-href="${BASE_PATH}/shop/wechat?refer=yunxiu-home"
           data-func-name="微信公众号">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>微信公众号</h2>

            <h3>互联网+营销解决方案</h3>
        </a>
        </#if>
        <a class="entry-item marketing js-check-func" href="javascript:void(0)"
           data-href="${BASE_PATH}/marketing?refer=yunxiu-home" data-func-name="客户营销">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>客户营销</h2>

            <h3>全面挖掘客户价值</h3>
        </a>
        <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11 && SESSION_SHOP_LEVEL?number != 12>
        <a class="entry-item activity js-check-func" href="javascript:void(0)"
           data-href="${BASE_PATH}/shop/activity/main?refer=yunxiu-home" data-func-name="在线引流首页">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>引流活动</h2>

            <h3>多渠道集客</h3>
        </a>
        </#if>
        <#if SESSION_SHOP_LEVEL?number != 10 && SESSION_SHOP_LEVEL?number != 11>
        <a class="entry-item tech" href="${BASE_PATH}/shop/tech/book?refer=yunxiu-home">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>淘汽知道</h2>

            <h3>让天下没有难修的车</h3>
        </a>
        </#if>
        <a class="entry-item app" href="javascript:void(0)">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>商家App</h2>

            <h3>移动办公，一手掌握</h3>

            <div class="app-qrcode">
                <div class="angle-bottom angle"></div>
                <img class="qrcode" src="${BASE_PATH}/static/img/page/home/app-qrcode.png">
            </div>
        </a>
        <a class="entry-item epc"
           target="_blank"
           href="${BASE_PATH}/home/avoid/epc/index?refer=yunxiu-home">
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>汽配管家</h2>

            <h3>把你的全车件需求告诉我</h3>
        </a>
        <div class="entry-item see-more js-hover" data-target=".see-more-wrapper" >
            <div class="circle-1">
                <div class="transform-box">
                    <div class="icon"></div>
                </div>
            </div>
            <h2>更多</h2>

            <h3>查看更多生态业务</h3>

        </div>
        <div class="see-more-wrapper hide">
            <div class="angle-bottom"></div>
            <a class="entry-item help-center has-line" href="${BASE_PATH}/shop/help?refer=yunxiu-home">
                <div class="circle-1">
                    <div class="transform-box">
                        <div class="icon"></div>
                    </div>
                </div>
                <h2>帮助</h2>

                <h3>点击进入</h3>
            </a>
            <a class="entry-item yx-center " href="javascript:void(0)">
                <div class="circle-1">
                    <div class="transform-box">
                        <div class="icon"></div>
                    </div>
                </div>
                <h2>淘汽云修订阅号</h2>

                <h3>门店一站式资讯服务平台</h3>

                <div class="app-qrcode">
                    <div class="angle-bottom angle"></div>
                    <img class="qrcode" src="${BASE_PATH}/static/img/page/home/subscription-qrcode.jpg">
                </div>
            </a>
        </div>
    </div>
</div>
