<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/wechat-common.css?0219a05beda33d460b58eb20349af16b"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/activity/wechat-activity-management.css?a6e11148fa10402a40785427dc9b7d12"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline inline-block">活动管理</h1>
            <a class="yqx-btn yqx-btn-1 yqx-btn-small fr add-coupon" href="${BASE_PATH}/account/coupon/create" target="_blank">新增优惠券</a>
            <a class="yqx-btn yqx-btn-1 yqx-btn-small fr manage-coupon" href="${BASE_PATH}/account/setting" target="_blank">管理优惠券</a>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-data-statistic">数据统计</button>
        </div>
        <div class="order-body">
            <h2>开启抽奖活动
                <div class="toggle" id="toggle-true" hidden="hidden">
                    <small class="fl">已开启</small>
                    <span class="fr"></span>
                </div>
                <div class="toggle" id="toggle-false">
                    <span class="fl"></span>
                    <small class="fr">未开启</small>
                </div>
            </h2>
            <div class="setting-content js-setting" style="display: none">
                <div class="activity-time">
                    <div class="form-label w80 vertical-top">
                        活动时间
                    </div>
                    <div class="form-item">
                        <div class="form-item">
                            <div class="radio-width inline-block"><input value="1" class="radio js-period" type="radio" name="period" checked/> 长期</div>
                            <div class="radio-width inline-block"><input value="2" class="radio js-period" type="radio" name="period" /> 自定义时间</div>
                        </div>
                        <div class="period js-period-setting">
                            <div class="form-item">
                                <input id="startDate" type="text" class="yqx-input yqx-input-icon yqx-input-small" placeholder="开始时间" disabled>
                                <span class="fa icon-calendar icon-small"></span>
                            </div>
                            -
                            <div class="form-item">
                                <input id="endDate" type="text" class="yqx-input yqx-input-icon yqx-input-small" placeholder="结束时间" disabled>
                                <span class="fa icon-calendar icon-small"></span>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="times-setting">
                    <div class="form-label w80">
                        抽奖设置
                    </div>
                    <div class="form-item">
                        <div class="radio-width inline-block"><input class="radio js-times-number" type="radio" name="times-number" value="1" checked/>只抽一次</div>
                        <div class="radio-width inline-block"><input class="radio js-times-number" type="radio" name="times-number" value="2"/>每天一次</div>
                    </div>
                </div>
                <div class="number-setting">
                    <div class="form-label w80">
                        设置奖品数量
                    </div>
                    <div class="form-item">
                        <div class="radio-width inline-block"><input class="radio js-prize-number" type="radio" name="prize-number" value="1" data-num="4" checked/> 4个奖品</div>
                        <div class="radio-width inline-block"><input class="radio js-prize-number" type="radio" name="prize-number" value="2" data-num="6"/> 6个奖品</div>
                    </div>
                </div>
                <div class="table">
                    <div class="form-label w80"></div>
                    <div id="prizeTable" class="inline-block table-width"></div>
                </div>
                <div class="participation-probability">
                    <div class="form-label w80">
                        谢谢参与
                    </div>
                    <div class="form-item">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-probability" placeholder="输入数字得奖概率"
                               data-v-type="required | integer | maxValue:100">
                        <span class="fa icon-small">%</span>
                    </div>
                    <div class="form-label">
                        输入数字0-100
                    </div>
                </div>
                <div class="advanced-setup">
                    <div class="form-label w80">
                        高级配置
                    </div>
                    <div class="form-label js-toggle-icon arrow-down"></div>
                    <div class="advanced-setting-box">
                        <div class="more-times-setting">
                            <div class="inline-block w32"><input class="js-share-times" type="checkbox">分享后 抽奖次数+1</div>
                            <div class="inline-block w32"><input class="js-order-times" type="checkbox">工单完成后抽奖次数+1</div>
                            <div class="inline-block w32"><input class="js-appointment-times" type="checkbox">预约成功后抽奖次数+1</div>
                        </div>
                        <div class="lottery-interface">
                            <div class="form-label vertical-top">
                                抽奖界面
                            </div>
                            <div class="form-item">
                                <div class="choose-interface">
                                    <input class="js-style" type="radio" name="lottery-style" value="0" checked/>紫色风情
                                    <button class="js-view-style view-style fr">查看样式</button>
                                </div>
                                <div class="choose-interface">
                                    <input class="js-style" type="radio" name="lottery-style" value="1" />小清新风格
                                    <button class="js-view-style view-style fr">查看样式</button>
                                </div>
                                <div class="choose-interface">
                                    <input class="js-style" type="radio" name="lottery-style" value="2" />红色黏土风格
                                    <button class="js-view-style view-style fr">查看样式</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="button">
                    <div class="form-label w80"></div>
                    <button class="yqx-btn yqx-btn-3 js-release">发布</button>
                    <button class="yqx-btn yqx-btn-1 js-preview">预览</button>
                </div>
            </div>
        </div>
    </div>
    <input type="hidden" id="shopId" value="${shopWechatVo.ucShopId}">
    <input type="hidden" id="lotteryPreviewUrl" value="${lotteryPreviewUrl}">
    <input type="hidden" id="actId">
</div>

<script type="text/template" id="tableTpl">
    <table class="yqx-table" id="tableTest">
        <thead>
        <tr>
            <th>序号</th>
            <th>奖品类型</th>
            <th>奖品名称</th>
            <th>奖品数量</th>
            <th>中奖概率
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-view-probability">查看</button>
            </th>
        </tr>
        </thead>
        <%for(var i=0;i<number;i++){%>
        <tr class="table-tr js-coupon-list" data-index="<%=i%>">
            <td>奖品<%=i+1%></td>
            <td>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-coupon-type" data-v-type="required">
                    <input type="hidden" name="couponType">
                    <span class="fa icon-small icon-angle-down"></span>
                </div>
            </td>
            <td>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-coupon" data-v-type="required">
                    <input type="hidden" name="couponId">
                    <span class="fa icon-small icon-angle-down"></span>
                </div>
            </td>
            <td>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-small yqx-input-small js-set-number" placeholder="输入奖品数量"
                            data-v-type="required | integer">
                </div>
            </td>
            <td>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-prize-probability" disabled>
                    <span class="fa icon-small">%</span>
                </div>
            </td>
        </tr>
        <%}%>
    </table>
</script>

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
</script>
<!--二维码弹窗模板 end-->

<#--数据统计弹窗模板 start-->
<script type="text/template" id="reportTpl">
    <%if(data){%>
    <div class="reportCheck">
        <h2 >活动已进行<%= data.durationDay||0%>天</h2>
        <div class="statistics report-box fl">
            <h3 class="headline">抽奖统计
                <div class="fr js-customer-detail"><%=data.userTotal||0%>人</div>
            </h3>
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>奖品名称</th>
                    <th>奖品剩余</th>
                    <th>获奖用户</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(data.info && data.info.length){%>
                <%for(var i = 0; i < data.info.length; i++){%>
                <%var item = data.info[i]%>
                <tr>
                    <td><%=i+1%></td>
                    <td class="js-show-tips ellipsis-1" title="<%=item.prizeName%>"><%=item.prizeName%></td>
                    <td><%=item.restNum%></td>
                    <td><%=item.userNum%></td>
                    <td class="js-customer-detail <%if(item.userNum == 0){%> no-count <%}%>" data-id="<%=item.serviceId%>">
                        <input type="hidden" class="lottery-prize-id" value="<%= item.id%>">
                        查看
                    </td>
                </tr>
                <%}%>
                <%}else{%>
                <tr>
                    <td colspan="4">暂无数据！</td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
        <div class="customerDetail report-box fr">

        </div>
    </div>
    <%}%>
</script>
<script type="text/template" id="detailTpl">
    <input class="lottery-prize-id" type="hidden" value="<%= lotteryPrizeId%>">
    <h3 class="headline">用户详情</h3>
    <table class="yqx-table" >
        <thead>
        <tr>
            <th>序号</th>
            <% if(lotteryPrizeId==0){%>
            <th>微信昵称</th>
            <%} else {%>
            <th>手机号码</th>
            <%}%>
        </tr>
        </thead>
        <tbody>
        <%if(data && data.content && data.content.length){%>
        <%var content = data.content;%>
        <%for(var i=0; i < content.length; i++){%>
        <%var item = content[i]%>
        <tr>
            <td><%=i+1%></td>
            <td class="js-show-tips ellipsis-1">
                <% if(lotteryPrizeId==0){%>
                    <%=item.userNickname%>
                <%} else {%>
                    <%=item.userMobile%>
                <%}%>
            </td>

        </tr>
        <%}%>
        <%}else{%>
        <tr>
            <td colspan="2">暂无数据！</td>
        </tr>
        <%}%>
        </tbody>
    </table>
    <div class="pageControl clearfix marT10">
        <button class="yqx-btn yqx-btn-1 js-pre-page page-btn fl">上一页</button>
        <button class="yqx-btn yqx-btn-3 js-next-page page-btn fr">下一页</button>
    </div>
</script>
<#--数据统计弹窗模板 end-->

<#--修改后确认弹窗 start-->
<script type="test/template" id="updateTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            修改后是否作为一个新的抽奖活动
        </div>
        <div class="bounce-body">
            <div class="option"><input type="radio" name="newLottery" value="1" checked>是（参加过抽奖的车主还可以抽奖一次）</div>
            <div class="option"><input type="radio" name="newLottery" value="0">否（参加过抽奖的车主不能抽取奖品了）</div>
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 js-confirm">确定</button>
            <button class="yqx-btn yqx-btn-1 js-cancel">取消</button>
        </div>
    </div>
</script>
<#--修改后确认弹窗 end-->

<#--查看样式弹窗 start-->
<script type="test/template" id="LotteryStyleTpl">
    <div class="collection-bounce">
        <img class="style-img" src="${BASE_PATH}/<%=data%>">
    </div>
</script>
<#--查看样式弹窗 end-->

<script src="${BASE_PATH}/static/third-plugin/qrcode/jquery.qrcode.min.js"></script>
<script src="${BASE_PATH}/static/js/page/wechat/activity/wechat-activity-management.js?1bcbfdf577dba7ddd79fc7fe644186e2"></script>
<#include "yqx/layout/footer.ftl" >