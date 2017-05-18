<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/activity/activity-game-detail.css?7a8f230de7739185b8ba2a2b0a1fcc5d"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <input type="hidden" id="shopActivityStatus" value="${gameActivityDetailVo.gameStatus}"/>
    <input type="hidden" id="tplStartTime" value="${gameActivityDetailVo.tplStartTimeStr}"/>
    <input type="hidden" id="tplEndTime" value="${gameActivityDetailVo.tplEndTimeStr}"/>
    <input type="hidden" id="shopWechatStatus" value="${shopWechatVo.shopStatus}"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl">活动管理</h1>
            <div class="operate fr">
                <a href="${BASE_PATH}/account/setting" target="_blank"><i class="game-detail-icon" id="manageCoupon"></i>管理优惠券</a>
                <a href="${BASE_PATH}/account/coupon/create" target="_blank"><i class="game-detail-icon" id="addNew"></i>新增优惠券</a>
            </div>
        </div>
        <div class="order-body clearfix">
            <aside class="fl">
                <h2>活动设置</h2>
            </aside>
            <div class="body-content clearfix fl">
                <div class="choose-box">
                    <input type="hidden" id="gameId" name="gameId" value="${gameActivityDetailVo.gameId}"/>
                    <input type="hidden" name="serviceTmplId" value="${gameActivityDetailVo.serviceTmplId}"/>
                    <input type="hidden" name="comboName" value="${gameActivityDetailVo.comboName}"/>
                    <input type="hidden" name="comboEffectivePeriodDays" value="${gameActivityDetailVo.comboEffectivePeriodDays}"/>
                    <input type="hidden" name="comboInfoId" value="${gameActivityDetailVo.comboInfoId}"/>
                    <fieldset>
                        <i class="fieldset-title outer-fieldset-title">活动名称</i>
                        <div class="form-item">${gameActivityDetailVo.gameName}</div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title outer-fieldset-title">活动时间</i>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small js-select require time-select" placeholder="开始时间" id="startTime" data-v-type="required"
                                   value="${gameActivityDetailVo.startTimeStr}" data-origin="${gameActivityDetailVo.startTimeStr}">
                            <span class="fa icon-calendar"></span>
                        </div>
                        -
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small js-select require time-select" placeholder="结束时间" id="endTime" data-v-type="required"
                                   value="${gameActivityDetailVo.endTimeStr}" data-origin="${gameActivityDetailVo.endTimeStr}">
                            <span class="fa icon-calendar"></span>
                        </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title outer-fieldset-title">云修19项检测</i>
                        <div class="form-item">
                            <input type="radio" class="js-radio" name="comboStatus" value="1" data-origin="${gameActivityDetailVo.comboStatus}" <#if gameActivityDetailVo.comboStatus==1>checked</#if>/><span class="check-box-text">开启</span>
                            <input type="radio" class="js-radio" name="comboStatus" value="0" data-origin="${gameActivityDetailVo.comboStatus}" <#if gameActivityDetailVo.comboStatus==0>checked</#if>/><span class="check-box-text">关闭</span>
                            <a id="view-checking">点此查看[云修19项检测]</a>
                        </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title outer-fieldset-title" id="setPrize">设置奖品</i>
                        <div class="form-item scroll-wrapper">
                            <#list gameActivityDetailVo.gameCouponDTOs as gameCoupon>
                            <div class="prize-item">
                                <input type="hidden" class="scoreLowlimit" value="${gameCoupon.scoreLowlimit}">
                                <input type="hidden" class="scoreToplimit" value="${gameCoupon.scoreToplimit}">
                                <h3>得分区间：${gameCoupon.scoreLowlimit}分-${gameCoupon.scoreToplimit}分</h3>
                                <fieldset>
                                    <i class="fieldset-title">选择卡券</i>
                                    <div class="form-item">
                                        <input class="yqx-input yqx-input-small input-middle js-select js-choose-coupon require" value="${gameCoupon.couponName}" placeholder="请选择卡券" data-v-type="required"
                                                data-origin="${gameCoupon.couponName}">
                                        <span class="fa icon-angle-down"></span>
                                        <input type="hidden" class="couponInfoId" value="${gameCoupon.couponInfoId}"/>
                                    </div>
                                </fieldset>
                                <fieldset>
                                    <i class="fieldset-title">送券数量</i>
                                    <div class="form-item">
                                        <input class="yqx-input yqx-input-small input-short require js-input" value="${gameCoupon.couponCount}" data-origin="${gameCoupon.couponCount}" data-v-type="required | integer | couponMinValue:1 | maxValue:9999999">
                                        <span class="fa">张</span>
                                    </div>
                                </fieldset>
                            </div>
                            </#list>
                        </div>
                    </fieldset>
                    <fieldset>
                        <i class="fieldset-title outer-fieldset-title">排行榜文字说明</i>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small input-long js-input" placeholder="如：排行榜第一的可以额外获得大保养一次" name="rankDesc" value="${gameActivityDetailVo.rankDesc}" data-origin="${gameActivityDetailVo.rankDesc}">
                        </div>
                    </fieldset>
                </div>
                <div class="tip-box">
                    <h3 class="tip-title"><i class="game-detail-icon tip-img"></i>注意说明</h3>
                    <p class="tip-item"><strong>01.</strong>确认参加活动前，请先设置活动奖品</p>
                </div>
                <div class="content-footer" hidden="hidden">
                    <#if gameActivityDetailVo.gameStatus == 0 ||gameActivityDetailVo.gameStatus == 2>
                        <button class="yqx-btn yqx-btn-3 js-publish marR10 disable">发布</button>
                        <button class="yqx-btn yqx-btn-1 js-preview marR10 disable">预览</button>
                    <#elseif gameActivityDetailVo.gameStatus == 3>
                        <button class="yqx-btn yqx-btn-3 js-publish marR10 disable">再次发布</button>
                        <button class="yqx-btn yqx-btn-1 js-preview marR10">预览</button>
                        <button class="yqx-btn yqx-btn-1 js-end">结束本期活动</button>
                    <#elseif gameActivityDetailVo.gameStatus == 1>
                        <button class="yqx-btn yqx-btn-3 js-publish marR10">再次发布</button>
                    </#if>
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
            <p><%=data.content%></p>
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
        <div class="bounce-item-content" id="qrcodeView">
            <div></div>
        </div>
    </div>
    <div class="bounce-foot preview-foot">
        <button class="yqx-btn yqx-btn-3 js-confirm-qrcode">确定</button>
    </div>
</script>
<!--二维码弹窗模板 end-->

<!--19项弹窗模板 start-->
<script type="text/template" id="checkTpl">
    <div class="collection-bounce">
        <div class="bounce-title">云修19项检测</div>
        <div class="table-bounce-content clearfix">
            <table class="fl">
                <thead>
                    <tr>
                        <th class="td-left">区域</th>
                        <th>检查内容</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td rowspan="6" class="td-left">驾驶室内区域检查</td>
                        <td>仪表显示状况</td>
                    </tr>
                    <tr>
                        <td>后方车灯状况</td>
                    </tr>
                    <tr>
                        <td>前方车灯状况</td>
                    </tr>
                    <tr>
                        <td>风窗玻璃洗涤状况</td>
                    </tr>
                    <tr>
                        <td>雨刮状况</td>
                    </tr>
                    <tr>
                        <td>★驾驶室内空气质量</td>
                    </tr>
                    <tr>
                        <td rowspan="7" class="td-left">发动机舱区域检查</td>
                        <td>★机油油量油质状况</td>
                    </tr>
                    <tr>
                        <td>机滤使用状况</td>
                    </tr>
                    <tr>
                        <td>清洗液状况</td>
                    </tr>
                    <tr>
                        <td>冷却液状况</td>
                    </tr>
                    <tr>
                        <td>制动液状况</td>
                    </tr>
                    <tr>
                        <td>★蓄电池状况</td>
                    </tr>
                    <tr>
                        <td>△空气滤芯状况</td>
                    </tr>
                </tbody>
            </table>
            <table class="fr">
                <thead>
                    <tr>
                        <th class="td-left">区域</th>
                        <th>检查内容</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td rowspan="4" class="td-left">车轮区域检查</td>
                        <td>轮胎/气门嘴状况</td>
                    </tr>
                    <tr>
                        <td>制动片状况</td>
                    </tr>
                    <tr>
                        <td>制动盘状况</td>
                    </tr>
                    <tr>
                        <td>备胎状况</td>
                    </tr>
                    <tr>
                        <td rowspan="2" class="td-left">底盘及整体区域检查</td>
                        <td>底盘配件状况</td>
                    </tr>
                    <tr>
                        <td>车门及全车外观状况</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</script>
<!--19项弹窗模板 end-->

<script src="${BASE_PATH}/static/third-plugin/qrcode/jquery.qrcode.min.js"></script>
<script src="${BASE_PATH}/static/js/page/wechat/activity/activity-game-detail.js?35f99306768cbf3f3d40759bfab72837"></script>

<#include "yqx/layout/footer.ftl" >