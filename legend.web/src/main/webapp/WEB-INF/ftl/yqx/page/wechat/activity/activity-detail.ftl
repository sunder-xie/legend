<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/activity/activity-detail.css?e5d328883c9ef08c64205da8fce2f106"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <input type="hidden" id="shopActivityStatus" value="${activityDetailVo.shopActivityStatus}"/>
    <input type="hidden" id="tplStartTime" value="${activityDetailVo.tplStartTimeStr}"/>
    <input type="hidden" id="tplEndTime" value="${activityDetailVo.tplEndTimeStr}"/>
    <input type="hidden" id="id" value="${activityDetailVo.id}"/>
    <input type="hidden" id="actId" value="${activityDetailVo.actId}"/>
    <input type="hidden" id="shopWechatStatus" value="${shopWechatVo.shopStatus}"/>
    <input type="hidden" id="isAdmin" value="${isAdmin}">

    <#assign appService = false>
    <#list activityDetailVo.pageVoList as pageVo>
        <#list pageVo.moduleVoList as moduleVo>
            <#if moduleVo.moduleType == 'Discount' || moduleVo.moduleType == 'GroupBuy'>
                <#assign appService = true>
           </#if>
        </#list>
    </#list>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">活动管理
                <div class="manage-buttons fr">
                    <#if appService>
                        <a class="yqx-btn yqx-btn-1 js-head-choose-service" href="javascript:;"><i>选择车主服务</i></a>
                        <a class="yqx-btn yqx-btn-1 marR10" href="${BASE_PATH}/shop/wechat/appservice/edit">
                            <i class="icon-plus marR5"></i><i>新增车主服务</i>
                        </a>
                    </#if>
                </div>
            </h1>
        </div>
        <div class="order-body clearfix">
            <aside class="act-tab fl">
                <h2 class="activity-manage">活动管理</h2>
            </aside>
            <div class="body-content clearfix fl">
                <div class="choose-box">
                    <fieldset>
                        <i class="fieldset-title">活动名称</i>
                        <div class="form-item">${activityDetailVo.actName}</div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title">活动时间</i>
                        <div class="form-item">
                            <input class="yqx-input time-select" placeholder="开始时间"
                                   value="<#if activityDetailVo.shopStartTimeStr>${activityDetailVo.shopStartTimeStr}<#else>${activityDetailVo.tplStartTimeStr}</#if>"
                                   id="startTime" name="startTime" data-v-type="required"
                            <#if activityDetailVo.shopActivityStatus == 2 ||activityDetailVo.shopActivityStatus == 0>
                                   disabled
                            </#if>>
                            <span class="fa icon-calendar"></span>
                        </div>
                        -
                        <div class="form-item">
                            <input class="yqx-input time-select" placeholder="结束时间"
                                   value="<#if activityDetailVo.shopEndTimeStr>${activityDetailVo.shopEndTimeStr}<#else>${activityDetailVo.tplEndTimeStr}</#if>"
                                   id="endTime" name="endTime" data-v-type="required"
                            <#if activityDetailVo.shopActivityStatus == 2 ||activityDetailVo.shopActivityStatus == 0>
                                   disabled
                            </#if>>
                            <span class="fa icon-calendar"></span>
                        </div>
                    </fieldset>
                    <#list activityDetailVo.pageVoList as pageVo>
                    <div class="pageVoInfo" hidden>
                        <input type="hidden" name="uniqueCode" value="${pageVo.uniqueCode}">
                        <input type="hidden" name="pageIndex" value="${pageVo.pageIndex}">
                    </div>
                        <#list pageVo.moduleVoList as moduleVo>
                            <#if moduleVo.moduleType == 'Service'>
                                <#include "yqx/page/wechat/activity/service-detail-module.ftl"/>
                            <#elseif moduleVo.moduleType == 'Discount'>
                                <#include "yqx/page/wechat/activity/discount-detail-module.ftl"/>
                            <#elseif moduleVo.moduleType == 'GroupBuy'>
                                <#include "yqx/page/wechat/activity/groupbuy-detail-module.ftl"/>
                            <#elseif moduleVo.moduleType == 'DiscountCoupon'>
                                <#include "yqx/page/wechat/activity/discount-coupon-detail-module.ftl"/>
                            </#if>
                        </#list>
                    </#list>
                </div>
                <div class="tip-box">
                <#list activityDetailVo.pageVoList as pageVo>
                    <#list pageVo.moduleVoList as moduleVo>
                        <#if moduleVo.moduleType != 'DiscountCoupon'>
                            <p class="textC"><button class="yqx-btn yqx-btn-3 js-set-payment">支付设置</button></p>
                        </#if>
                    </#list>
                </#list>
                    <div class="tip-box-content">
                        <div class="tip-box-text">
                            <i class="tip-img"></i>
                            <p class="tips">确认参加活动前，
                                请先选择活动对应参加的商品或者服务
                            </p>
                        </div>
                    </div>
                </div>
                <div class="content-footer">
                    <div hidden="hidden">
                    <#if activityDetailVo.shopActivityStatus == -1 || activityDetailVo.shopActivityStatus == 1>
                        <button class="yqx-btn yqx-btn-3 js-publish">发布</button>
                        <button class="yqx-btn yqx-btn-1 js-preview">预览</button>
                    <#elseif activityDetailVo.shopActivityStatus == 2>
                        <button class="yqx-btn yqx-btn-3 js-publish">再次发布</button>
                        <button class="yqx-btn yqx-btn-1 js-end">结束本期活动</button>
                    <#elseif activityDetailVo.shopActivityStatus == 0>
                        <button class="yqx-btn yqx-btn-3 js-publish">再次发布</button>
                    </#if>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!--公共弹窗模板 start-->
<script type="text/template" id="bounceTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            <%=data.title%>
        </div>
        <div class="bounce-content">
            <p class="bounce-row"><%=data.content%></p>
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 <%=data.confirmClass%>">确定</button>
            <button class="yqx-btn yqx-btn-1 <%=data.cancelClass%>">取消</button>
        </div>
    </div>
</script>
<!--公共弹窗模板 end-->

<!--二维码弹窗模板 start-->
<script type="text/template" id="qrcodeTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            扫描下方二维码即可预览最新效果
        </div>
        <div class="bounce-content-wrapper" id="qrcodeView">
            <div></div>
        </div>
    </div>
    <div class="bounce-foot qrcode-preview-foot">
        <button class="yqx-btn yqx-btn-3 js-confirm-qrcode">确定</button>
    </div>
</script>
<!--二维码弹窗模板 end-->

<script src="${BASE_PATH}/static/third-plugin/qrcode/jquery.qrcode.min.js"></script>
<script src="${BASE_PATH}/static/js/page/wechat/activity/activity-detail.js?71d4991c542a4f7c579248c4dfba5cdd"></script>
<#include "yqx/layout/footer.ftl" >