<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wechat-coupon.css?3814e3b83403966a9525f81f9924909c">

<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">关注送券
                <a class="yqx-btn yqx-btn-default help fr clearfix" href="${BASE_PATH}/shop/help?id=92">
                    <i class="question-icon"></i><i>帮助中心</i>
                </a>
            </h1>
        </div>
        <div class="order-body-wrapper" hidden="hidden">
            <div class="order-body">
                <h2>开启关注送券功能
                    <div class="toggle" id="toggle-true">
                        <small class="fl">已开启</small>
                        <span class="fr"></span>
                    </div>
                    <div class="toggle" id="toggle-false" hidden="hidden">
                        <span class="fl"></span>
                        <small class="fr">未开启</small>
                    </div>
                </h2>
                <div class="content js-setting">
                    <div class="left-box box switch">
                        <input type="hidden" name="id" id="id">
                        <fieldset>
                            <i class="fieldset-title">选择卡券</i>
                            <div class="form-item">
                                <input class="yqx-input" id="couponType" placeholder="请选择卡券" data-v-type="required">
                                <input type="hidden" id="couponTypeId" name="couponTypeId">
                            </div>
                            <a id="manageCoupon" href="${BASE_PATH}/account/setting" target="_blank">管理优惠券</a>
                            <a id="addNew" href="${BASE_PATH}/account/coupon/create" target="_blank"><strong>+</strong>新增优惠券</a>
                        </fieldset>
                        <fieldset>
                            <i class="fieldset-title">送券数量</i>
                            <div class="form-item">
                                <input class="yqx-input faInput" placeholder="请输入送券数量" id="couponNum" name="originNumber"  data-v-type="required | couponMinValue:10 | couponMaxValue:9999999">
                                <span class="fa">张</span>
                            </div>
                        </fieldset>
                        <fieldset>
                            <i class="fieldset-title">送券时间</i>
                            <div class="form-item" id="giveTime">
                                <label class="marR20"><input class="radio" type="radio" name="isLongTime" value="1" checked> 长期</label>
                                <label><input class="radio" type="radio" name="isLongTime" value="0" > 自定义时间</label>
                            </div>
                        </fieldset>
                        <fieldset class="give-time" hidden="hidden">
                            <i class="fieldset-title transparent">送券时间</i>
                            <div class="form-item">
                                <input class="yqx-input time-select" placeholder="送券开始时间" id="startTime" name="startTime" data-v-type="required">
                                <span class="fa icon-calendar"></span>
                            </div>
                            至
                            <div class="form-item">
                                <input class="yqx-input time-select" placeholder="送券结束时间" id="endTime" name="endTime" data-v-type="required">
                                <span class="fa icon-calendar"></span>
                            </div>
                        </fieldset>
                        <div>
                            <button class="yqx-btn-3 yqx-btn js-save">活动开始</button>
                        </div>
                    </div>
                    <div class="right-box box" id="intro">
                        <h3>关注送券活动介绍</h3>
                        <ol>
                            <li>每次关注送券只能设置一种类型。</li>
                            <li>如果送券类型里没有你想要送的优惠券，可点击<strong>“新增优惠券”</strong>去新增。</li>
                            <li>可以群发短信通知老客户来关注微信公众号领取优惠券。</li>
                            <li>关注送券的效果，可以在统计中查看。</li>
                        </ol>
                    </div>
                </div>
                <div class="panel-footer switch">
                    <img class="note-img" src="${BASE_PATH}/static/img/page/wechat/note.png">
                    <button class="yqx-btn yqx-btn-1 sms-btn js-sms-btn">点击群发短信</button>
                    <div class="note">
                        <p>设置关注送券后，</p>
                        <p>可群发短信通知老客户来领券，增加微信公众号粉丝数哦，并可让您的老客户返店。</p>
                    </div>
                </div>
            </div>
            <div class="order-body statistics">
                <h3>关注送券统计
                </h3>
                <div id="searchForm">
                    <fieldset class="chooseBox">
                        <i class="fieldset-title transparent">送券时间</i>
                        <div class="form-item">
                            <input class="yqx-input" placeholder="送券开始时间" id="startTime1" name="search_startTime">
                            <span class="fa icon-calendar"></span>
                        </div>
                        至
                        <div class="form-item">
                            <input class="yqx-input" placeholder="送券结束时间" id="endTime1" name="search_endTime">
                            <span class="fa icon-calendar"></span>
                        </div>
                        <i>卡券</i>
                        <div class="form-item">
                            <input class="yqx-input" id="couponType1" value="全部">
                            <input type="hidden" name="search_couponTypeId">
                        <#--<span class="fa icon-angle-down"></span>-->
                        </div>
                        <button class="yqx-btn-3 yqx-btn js-search">查询</button>
                    </fieldset>
                </div>
                <div class="content clearfix">
                    <div id="echart-box">
                        <img class="tag" src="${BASE_PATH}/static/img/page/wechat/tag.png">
                        <div id="echart"></div>
                    </div>
                    <div class="legend">
                        <ul>
                            <li><i class="first"></i>发放券总数：<span>0</span>张</li>
                            <li><i class="second"></i>已使用券总数：<span>0</span>张</li>
                            <li><i class="third"></i>未使用券总数：<span>0</span>张</li>
                        </ul>
                        <button class="yqx-btn yqx-btn-2 yqx-btn-small detail js-detail">查看详情</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/third-plugin/echart/echarts.min.js"></script>
<script src="${BASE_PATH}/static/js/page/wechat/wechat-coupon.js?87eef4bf675aa99796731883f6324a07"></script>
<#include "yqx/layout/footer.ftl" >