<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/allot-common.css?59e8db8de7ecdcc00ce4f3de8c5464aa"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/import-page.css?2056269f9d188228b59135846f25eec6"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="aside fl">
    <#include "yqx/page/marketing/left-nav.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="aside-main fl">
        <!-- 标题 start -->
        <div class="order-head clearfix">
            <h3 class="headline fl">导入客户会员</h3>
            <a class="yqx-btn yqx-btn-1 yqx-btn-small fr" href="${BASE_PATH}/shop/customer/edit">新建客户车辆</a>
        </div>
        <div class="content clearfix">
            <div class="left-box fl">
                <div class="export-box">
                    <div class="export-pic green fl">
                        <img src="${BASE_PATH}/static/img/page/marketing/export-ico1.png"/>
                        <p>Vehicle</p>
                        <p>Information</p>
                    </div>
                    <div class="export-text fl">
                        <h3>导入客户车辆</h3>
                        <p>车辆信息可以一键导入</p>
                        <a href="${BASE_PATH}/init/import/customerCar" class="yqx-btn yqx-btn-1 export-btn">去导入</a>
                    </div>
                </div>
                <div class="export-box export-top">
                    <div class="export-pic blue fl">
                        <img src="${BASE_PATH}/static/img/page/marketing/export-ico2.png"/>
                        <p>Member</p>
                        <p>Information</p>
                    </div>
                    <div class="export-text fl">
                        <h3>导入会员</h3>
                        <p>门店会员可以一键导入</p>
                        <a href="${BASE_PATH}/init/import/memberCard" class="yqx-btn yqx-btn-1 export-btn">去导入</a>
                    </div>
                </div>
            </div>
            <div class="right-box fr">
                <h3>两大优势</h3>
                <div>
                    <p><i></i>大大缩短接车维修开单时间</p>
                    <p><i></i>对导入客户进行营销，将市场竞争的压力传递到每个职工。把职工的思想传递到经营者，并化成参与竞争的优势力量。</p>
                </div>
                <div class="export-help">
                    <p>如何导出驰加、博士德等系统客户、会员？</p>
                    <a href="${BASE_PATH}/shop/help?type=&id=134" class="help-download yqx-link-3"><img src="${BASE_PATH}/static/img/page/marketing/download-grey.png"/> 案例指引下载</a>
                </div>
            </div>
        </div>

    </div>
    <!-- 右侧内容区 end -->
</div>
