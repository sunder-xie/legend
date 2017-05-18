<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/promotion.css?df387be3d31dff5e834562f22050f5a2"/>

<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>
    <div class="Z-right">
        <h3 class="Z-title">客户营销  > <i>门店推广</i></h3>
        <div class="info-link-box clearfix">
            <div class="pic-box wx js-wx-promotion">
                <a href="${BASE_PATH}/shop/wechat/activity-list">
                    <h3>微信推广</h3>
                    <p>门店活动实时推广</p>
                    <p>提高客户转化率</p>
                </a>
            </div>
            <div class="pic-box pyq">
                <a href="${BASE_PATH}/shop/activity">
                    <h3>朋友圈推广</h3>
                    <p>带动客户自发宣传</p>
                    <p>开拓新客户</p>
                </a>
            </div>
            <div class="pic-box">
                <div class="shop">
                    <a href="${BASE_PATH}/shop/cz_app/activity/act_list">
                    <div class="shop-activity">
                        <p>Do Exclusive Activity</p>
                    </div>
                    <div class="shop-activity-info">
                        <h3>办单店活动</h3>
                        <p>专属于您的门店</p>
                    </div>
                    </a>
                </div>
                <div class="shop">
                    <a href="${BASE_PATH}/shop/cz_app/notice/notice_list">
                    <div class="shop-notice">
                        <p>Hair Shop Announcement</p>
                    </div>
                    <div class="shop-activity-info">
                        <h3>发门店公告</h3>
                        <p>一键告知</p>
                    </div>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<#include "layout/footer.ftl" >
