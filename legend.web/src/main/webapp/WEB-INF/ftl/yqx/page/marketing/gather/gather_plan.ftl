<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/tpl/revisitDialog/revisit.css">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/allot-common.css?59e8db8de7ecdcc00ce4f3de8c5464aa"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/gather-plan.css?d90065623f9bfed72d730853b1aa900a"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="aside fl">
    <#include "yqx/page/marketing/left-nav.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="aside-main fr">
        <!-- 标题 start -->
        <div class="order-head clearfix">
            <h3 class="headline">集客消费</h3>
        </div>
        <div class="content">
            <div class="tab">
                <span class="js-tab" data-tab-index="0">盘活客户</span>
                <span class="js-tab" data-tab-index="1">老客户拉新</span>
            </div>
            <!--选项卡内容 -->
            <div class="tab-con" id="tabCon">

            </div>
            <div class="table-content">
                <h3 class="js-table-title"></h3>
                <div id="tableCon">

                </div>
                <div class="yqx-page" id="pagingBox">

                </div>
                <div class="btn-box">
                    <button class="yqx-btn yqx-btn-2 yqx-btn-small fl js-message">发送盘活短信</button>
                    <button style="margin-left: 10px;" class="yqx-btn yqx-btn-3 yqx-btn-small fl js-message-all">全部发送盘活短信</button>
                </div>
            </div>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>


<!---盘活老客户 模板-->
<script type="text/html" id="activateTpl">
    <div class="form-box">
        <p class="tip-consumer">您管理客户中有休眠客户<span id="sleepNum2">0</span>人     流失客户<span id="lostNum2">0</span>人，您可以使用短信、电话回访方式发送提醒或者优惠券盘活用户到店消费。</p>
        <div class="form-title">选择盘活客户</div>
        <div class="form-content">
            <div class="customer-info clearfix">
                <div class="form-label fl">客户归属</div>
                <div class="consumer-tab fl">
                <div class="line">
                    <div class="customer-card js-staff-name <#if SESSION_USER_IS_ADMIN == 1>name-hover</#if>">全部员工</div>
                <#list userList as user>
                    <div class="customer-card js-staff-name <#if SESSION_USER_IS_ADMIN != 1 && SESSION_USER_ID == user.userId>name-hover</#if>" data-id="${user.userId}">${user.userName}</div>
                </#list>
                </div>
            </div>
            </div>
            <div class="customer-info clearfix">
                <div class="form-label fl">选择客户</div>
                <div class="consumer-tab fl">
                    <div class="search">
                        <div class="form-item">
                            <input type="text" name="searchKey" class="yqx-input yqx-input-small search-text" value="" placeholder="车牌/车主电话/车主">
                        </div>
                        <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search">搜索</button>
                    </div>
                    <div class="consumer-tab fl js-customerCon">

                    </div>
                </div>
                <div class="customer-info clearfix">
                    <div class="form-label fl">选优惠券</div>
                    <div class="consumer-tab fl">
                        <p class="coupon-tips">每次仅支持发送一种优惠券</p>
                        <div class="form-item">
                            <input type="text" name="" class="yqx-input js-coupon-text coupon-name" value="" placeholder="优惠券名称">
                            <input type="hidden" value="" class="js-coupon-id"/>
                        </div>
                        <a class="yqx-btn yqx-btn-3 js-add-coupon reelect hide"><img src="${BASE_PATH}/static/img/page/marketing/gather/again-ico.png"/> 重选</a>
                        <a class="yqx-btn yqx-btn-1 js-add-coupon coupon">+ 选择优惠券</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

</script>

<!---老客户拉新 模板-->
<script type="text/html" id="oldClientsTpl">
    <div class="form-box">
        <p class="tip-consumer">您可以使用微信方式发送优惠券至老客户或者朋友圈，老客户获得优惠券后可以将优惠券发送给好友</p>
        <div class="form-title">老客户拉新发送优惠券</div>
        <div class="form-content">
            <div class="customer-info clearfix">
                <div class="customer-info clearfix">
                    <div class="form-label fl">客户归属</div>
                    <div class="consumer-tab fl">
                        <div class="line">
                            <div class="customer-card js-staff-name <#if SESSION_USER_IS_ADMIN == 1>name-hover</#if>">全部员工</div>
                        <#list userList as user>
                            <div class="customer-card js-staff-name <#if SESSION_USER_IS_ADMIN != 1 && SESSION_USER_ID == user.userId>name-hover</#if>" data-id="${user.userId}">${user.userName}</div>
                        </#list>
                        </div>
                    </div>
                <div class="form-label fl">选择客户</div>
                <div class="consumer-tab fl">
                    <div class="search">
                        <div class="form-item">
                            <input type="text" name="searchKey" class="yqx-input yqx-input-small search-text" value="" placeholder="车牌/车主电话/车主">
                        </div>
                        <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search">搜索</button>
                    </div>
                    <div class="consumer-tab fl js-customerCon">

                    </div>
                </div>
                <div class="">设置赠送优惠券</div>
                <div class="coupon-contact">选优惠券<span class="coupon-tips" style="margin-left: 20px;">每次仅支持发送一种优惠券。</span></div>
                <div class="coupon-contact">
                    <div class="form-item" style="width: 220px;">
                        <input type="text" name="" class="yqx-input js-coupon-text coupon-name" value="" placeholder="优惠券名称">
                        <input type="hidden" value="" class="js-coupon-id"/>
                    </div>
                    <a class="yqx-btn yqx-btn-3 js-add-coupon reelect hide"><img src="${BASE_PATH}/static/img/page/marketing/gather/again-ico.png"/> 重选</a>
                    <a class="yqx-btn yqx-btn-1 js-add-coupon coupon">+ 选择优惠券</a>
                </div>
                <div class="coupon-contact">
                    每位老客户共送优惠券
                </div>
                <div class="coupon-contact">
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small js-totalCouponNum" value="" placeholder="" data-v-type="integer | required | maxValue:99">
                    </div>
                    张
                </div>
                <div class="coupon-contact">
                    每位新/老客户可领优惠券
                </div>
                <div class="coupon-contact">
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small js-perAccountNum" value="1" placeholder="" data-v-type="integer | required | minValue:0">
                    </div>
                    张
                </div>
                <div class="coupon-contact m-top">
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-qrcodeView">生成优惠券二维码</button>
                </div>
            </div>
        </div>
    </div>
   </div>
</script>



<!--选择客户 模板-->
<script type="text/html" id="customerNumTpl">
    <%if(json.success && json.data){%>
    <div class="line">
        <div class="customer-card js-card card-hover" data-type="hasMobileNum">有车主电话客户（<span><%=json.data.hasMobileNum%></span>）</div>
        <div class="customer-card js-card" data-type="hasMemberNum">会员客户（<span><%=json.data.hasMemberNum%></span>）</div>
        <div class="customer-card js-card" data-type="noneMemberNum">非会员客户（<span><%=json.data.noneMemberNum%></span>）</div>
    </div>
    <div class="line">
        <div class="customer-card js-card" data-type="sleepNum">休眠客户（<span><%=json.data.sleepNum%></span>）</div>
        <div class="customer-card js-card" data-type="lostNum">流失客户（<span><%=json.data.lostNum%></span>）</div>
        <div class="customer-card js-card" data-type="activeNum">活跃客户（<span><%=json.data.activeNum%></span>）</div>
    </div>
    <div class="line">
        <div class="customer-card js-card" data-type="auditingNoteNum">年检到期提醒（<span><%=json.data.auditingNoteNum%></span>）</div>
            <div class="customer-card js-card" data-type="maintainNoteNum">保养到期提醒（<span><%=json.data.maintainNoteNum%></span>）</div>
        <div class="customer-card js-card" data-type="insuranceNoteNum">保险到期提醒（<span><%=json.data.insuranceNoteNum%></span>）</div>
        <div class="customer-card js-card" data-type="birthdayNoteNum">生日提醒（<span><%=json.data.birthdayNoteNum%></span>）</div>
        <div class="customer-card js-card" data-type="lostNoteNum">流失客户提醒（<span><%=json.data.lostNoteNum%></span>）</div>
        <div class="customer-card js-card" data-type="visitNoteNum">回访提醒（<span><%=json.data.visitNoteNum%></span>）</div>
    </div>
    <%}%>
</script>


<#-- 盘活客户列表 -->
<script type="text/template" id="tableTpl">
    <%if(json.data && json.data.content) {%>
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="js-check-tr" style="width: 20px;"><input type="checkbox" class="js-select-all"/></th>
            <th class="text-l">车牌</th>
            <th width="120" class="text-l">车主</th>
            <th class="text-l">车主电话</th>
            <th class="text-l" width="120">客户归属</th>
            <th class="text-l">最近消费时间</th>
            <th class="text-r">累计消费金额</th>
            <th class="text-r">累计消费次数</th>
            <%if(getNote()) {%>
            <th class="text-l"><%=getNote()%></th>
            <%}%>
            <th class="js-operation-tr text-c">操作</th>
            <th class="js-send-tr text-c">发送微信优惠券</th>
        </tr>
        </thead>
        <tbody>
    <% var t%>
    <% var selectedCustomer = getSelectedCustomer();%>
    <% for(var i in json.data.content) {%>
    <% t = json.data.content[i];%>
        <tr class="js-list">
            <input type="hidden" name="customerId" value="<%=t.customerId%>"/>
            <input type="hidden" name="customerCarId" value="<%=t.customerCarId%>"/>
            <input type="hidden" name="noteInfoId" value="<%=t.noteInfoId%>"/>
            <input type="hidden" name="carModel" value="<%=t.carModel%>"/>
            <input type="hidden" name="userId" value="<%=t.userId%>"/>
            <input type="hidden" name="userName" value="<%=t.userName%>"/>
            <input type="hidden" name="mobile" value="<%=t.customerMobile%>"/>
            <td class="js-check-tr" width="50">
                <input type="checkbox"
                       <%if(selectedCustomer[t.customerCarId]) {%>
                        checked
                        <%}%>
                       class="js-select"/>
            </td>
            <td class="text-l p-left car-license" ><%=t.carLicense%></td>
            <td class="js-show-tips text-l car-name ellipsis-1" width="120"><%=t.customerName%></td>
            <td class="text-l car-mobile"><%=t.customerMobile%></td>
            <td class="text-l ellipsis-1" width="120"><%=t.userName%></td>
            <td class="text-l"><%=t.lastConsumeTime%></td>
            <td class="text-r p-right money-font">&yen;<%=t.totalConsumeAmount%></td>
            <td class="text-r p-right"><%=t.totalConsumeCount%></td>
            <%if(getNote()) {%>
            <td><%=t.noteTime%></td>
            <%}%>
            <td class="js-operation-tr text-c"
                data-customer-car-id="<%=t.customerCarId%>"
                    ><a href="javascript:;" class="yqx-link-1 js-return-record">电话回访记录</a></td>
            <td class="js-send-tr text-c">
                <a href="javascript:;" class="ticket js-ticket yqx-link-1">
                    发送优惠券

                    <div class="code js-code-box">
                        <h3><img src="${BASE_PATH}/static/img/page/marketing/gather/ticket-ico.png"/><%=t.carLicense%></h3>

                        <div class="code-img js-code"></div>
                        <p>微信扫描发送优惠券</p>
                    </div>
                </a>
            </td>
        </tr>
    <%}%>
    </tbody>
    </table>
    <%if(i == null) {%>
        <div class="no-data">暂无数据</div>
    <%}%>
    <%}%>
</script>

<!--选择优惠券 模板-->
<#include "yqx/page/marketing/common/coupon-tpl.ftl">
<!--短信 模板-->
<#include "yqx/page/marketing/common/sms-tpl.ftl">

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/marketing/gather/gather-plan.js?bf2b87f478287553312daf5448d0302e"></script>
<script src="${BASE_PATH}/static/third-plugin/qrcode/jquery.qrcode.min.js"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">