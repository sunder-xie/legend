<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/index.css?bf2886759b7fe33e333b3d4d443041dd"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/third-plugin/Font-Awesome-3.2.1/css/font-awesome.min.css" />
<div class="yqx-help-box hide">
    <div class="yqx-help-back margin-neg-100"></div>
    <div class="yqx-help yqx-help-1 js-help settlement-help hide" data-settlement-index="1"
         data-index="1" data-selector=".left-nav .member .nav-sub-title">
        <img src="${BASE_PATH}/static/img/page/settlement/guide/guide-2.png">
        <i class="next"></i>
    </div>
</div>
<div class="guide-box">
    <div class="guide-wrap">
        <h3>找到对的人 让营销事半功倍</h3>
        <ul>
            <li>
                <img src="${BASE_PATH}/static/img/page/marketing/guide-ico1.png"/>
                <h4>了解客户</h4>
                <p>【客户分析】让您全方位地了解您的客户</p>
            </li>
            <li>
                <img src="${BASE_PATH}/static/img/page/marketing/guide-ico2.png"/>
                <h4>瞄准目标客户</h4>
                <p class="long-tip">【精准营销】为你提供十几种客户特征，您可以自由组合，锁定目标客户。</p>
            </li>
            <li>
                <img src="${BASE_PATH}/static/img/page/marketing/guide-ico3.png"/>
                <h4>激活客户</h4>
                <p class="long-tip">会员卡、优惠券、营销短信、电话回访，营销方式应有尽有。</p>
            </li>
            <li>
                <img src="${BASE_PATH}/static/img/page/marketing/guide-ico4.png"/>
                <h4>效果分析</h4>
                <p>营销效果怎么样？激活客户、营业额增收告诉你。</p>
            </li>
        </ul>
        <a href="javascript:;" class="pack js-pack"><i class="icon-angle-up"></i>收起</a>
    </div>
</div>
<div class="wrapper">
    <#include "marketing/ng/left_nav.ftl"/>
    <div class="Z-right">
        <h3 class="Z-title">客户营销  > <i>客户分析</i></h3>
        <ul class="Z-lost">
            <li class="custom-total" onclick="window.location='${BASE_PATH}/marketing/ng/analysis/lost'"><i></i><span class="custom-total-title">客户总数</span><span class="custom-total-num">${totalCustomerCount?default(0)}<em>位</em></span></li>
            <li class="custom-lost" onclick="window.location='${BASE_PATH}/marketing/ng/analysis/lost'"><i></i><span class="custom-lost-title">已流失客户</span><span class="custom-lost-num">${lostCustomerCount?default(0)}<em>位</em></span></li>
            <li class="custom-lose" onclick="window.location='${BASE_PATH}/marketing/ng/analysis/lost'"><i></i><span class="custom-lose-title">损失</span><span class="custom-lose-num">${lostAmountOneYear?default(0)}<em>元/年</em></span></li>
        </ul>
        <div class="Z-type">
            <div class="custom-type">
                <p class="custom-type-title">客户类型分析</p>
                <p class="custom-type-status"></p>
                <p class="custom-type-no-data">亲，您还未使用系统开单<br/>赶紧去开单吧！</p>
                <button class="custom-type-detail" onclick="window.location='${BASE_PATH}/marketing/ng/analysis/type'">查看详情</button>
                <button class="custom-type-bill" onclick="window.location='${BASE_PATH}/shop/reception'">去开单</button>
            </div>
            <i class="Z-type-tag"></i>
            <div id="Z-type-analysis" class="custom-type-analysis">
                <div class="custom-new-old">
                    <div id="custom-percent" data-newer="${newCustomerTypeCount?default(0)}" data-older="${oldCustomerTypeCount?default(0)}">
                        <div id="custom-percent-newer"></div>
                        <div id="custom-percent-older"></div>
                    </div>
                    <div class="custom-new">
                        <span class="custom-new-tag"></span>
                        <span class="custom-new-num"><em class="display">新客户</em>：${newCustomerTypeCount?default(0)}个，占<em class="newer-percent">0</em>%</span>
                        <p class="custom-new-introduce">第一次进店消费的客户</p>
                    </div>
                    <div class="custom-old">
                        <span class="custom-old-tag"></span>
                        <span class="custom-old-num"><em class="display">老客户</em>：${oldCustomerTypeCount?default(0)}个，占<em class="older-percent">0</em>%</span>
                        <p class="custom-old-introduce">第二次进店消费的客户</p>
                    </div>
                </div>
                <div class="custom-activity-level">
                    <div id="custom-activity-percent" data-activity="${activeCustomerTypeCount?default(0)}" data-dormant="${sleepCustomerTypeCount?default(0)}" data-loss="${lostCustomerTypeCount?default(0)}">
                        <div id="custom-percent-activity"></div>
                        <div id="custom-percent-dormant"></div>
                        <div id="custom-percent-loss"></div>
                    </div>
                    <div class="custom-activity">
                        <span class="custom-activity-tag"></span>
                        <span class="custom-activity-num"><em class="display">活跃客户</em>：${activeCustomerTypeCount?default(0)}个，占<em class="activity-percent">0</em>%</span>
                        <p class="custom-activity-introduce">近3个月内有消费</p>
                    </div>
                    <div class="custom-dormant">
                        <span class="custom-dormant-tag"></span>
                        <span class="custom-dormant-num"><em class="display">休眠客户</em>：${sleepCustomerTypeCount?default(0)}个，占<em class="dormant-percent">0</em>%</span>
                        <p class="custom-dormant-introduce">近3个月无消费，近6个月有消费</p>
                    </div>
                    <div class="custom-loss">
                        <span class="custom-loss-tag"></span>
                        <span class="custom-loss-num"><em class="display">流失客户</em>：${lostCustomerTypeCount?default(0)}个，占<em class="loss-percent">0</em>%</span>
                        <p class="custom-loss-introduce">6个月以上无消费</p>
                    </div>
                </div>
                <i class="no-data"></i>
            </div>
        </div>

        <div class="Z-lose m-bot">
            <div class="custom-level">
                <p class="custom-level-title">客户流失分析</p>
                <p class="custom-level-status">${lostAnalysisStr}</p>
                <p class="custom-level-no-data">亲，您还没有客户<br/>赶紧去导入客户吧！</p>
                <button class="custom-level-detail" onclick="window.location='${BASE_PATH}/marketing/ng/analysis/lost'">查看详情</button>
                <button class="custom-level-import" onclick="window.location='${BASE_PATH}/init/import/customerCar'">去导入</button>
            </div>
            <i class="Z-level-tag"></i>
            <div class="custom-level-analysis">
                <div id="num-washed" data-high="${highFrequencyCustomerLostCount?default(0)}" data-middle="${middleFrequencyCustomerLostCount?default(0)}" data-low="${lowFrequencyCustomerLostCount?default(0)}"></div>
                <div class="num-washed-info">
                    <div class="washed-box">
                        <p><i></i><em class="display">高等质量客户流失: </em>${highFrequencyCustomerLostCount?default(0)}位</p>
                        <p><i></i><em class="display">中等质量客户流失：</em>${middleFrequencyCustomerLostCount?default(0)}位</p>
                        <p><i></i><em class="display">低等质量客户流失：</em>${lowFrequencyCustomerLostCount?default(0)}位</p>
                    </div>
                    <div class="washed-total">
                        <p><em class="display">流失客户总数</em></p>
                        <h3>${totalCustomerLostCount?default(0)}</h3>
                    </div>
                </div>
                <i class="no-data"></i>
            </div>
            <div class="Z-separation-line"></div>
            <ul class="custom-type-notice">
                <li class="custom-type-high1">高等质量流失客户：累计消费次数>=5次</li>
                <li class="custom-type-middle">中等质量流失客户：3次<=累计消费次数<5次</li>
                <li class="custom-type-low">低等质量流失客户：累计消费次数<3次</li>
            </ul>
        </div>

        <div class="Z-lose">
            <div class="custom-level">
                <p class="custom-level-title">客户级别分析</p>
                <p class="custom-level-status">${analysisStr}</p>
                <p class="custom-level-no-data">亲，您还没有客户<br/>赶紧去导入客户吧！</p>
                <button class="custom-level-detail" onclick="window.location='${BASE_PATH}/marketing/ng/analysis/level'">查看详情</button>
                <button class="custom-level-import" onclick="window.location='${BASE_PATH}/init/import/customerCar'">去导入</button>
            </div>
            <i class="Z-level-tag"></i>
            <div class="custom-level-analysis">
                <div id="num-percent" data-high="${highCustomerTypeCount?default(0)}" data-middle="${middleCustomerTypeCount?default(0)}" data-low="${lowCustomerTypeCount?default(0)}"></div>
                <div id="purchase-percent" data-high="${highCustomerTypeConsumeAmount?default(0)}" data-middle="${middleCustomerTypeConsumeAmount?default(0)}" data-low="${lowCustomerTypeConsumeAmount?default(0)}"></div>
                <i class="no-data"></i>
            </div>
            <div class="Z-separation-line"></div>
            <ul class="custom-type-notice">
                <li class="custom-type-high">高端客户：车价30万以上</li>
                <li class="custom-type-middle">中端客户：车价10-30万</li>
                <li class="custom-type-low">低端客户：车价10万以下</li>
            </ul>
        </div>
    </div>
</div>
<div class="guide js-guide">
    <i class="fa icon-list-ul"></i>
    <p>营销指导</p>
</div>
<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/echarts/echarts.js?f3da14a808d4e3fdfd3988e0e6de055f"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/marketing/index.js?f6dacf6595b0af949dd1b7827d00c140"></script>