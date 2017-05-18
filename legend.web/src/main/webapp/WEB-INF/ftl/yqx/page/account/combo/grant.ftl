<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/combo/grant.css?4bf6e2e4cdfb0601867de1bfd5cdb431"/>
<div class="yqx-wrapper clearfix">
    <div class="left-menu fl">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="aside-main">
        <div class="member-header clearfix">
            <h1 class="headline fl">
                计次卡办理
            </h1>
        </div>
        <div class="member-search-form">
            <div class="tab-title">车牌查询</div>
            <div class="member-search-box">
                <div class="form-item">
                    <input type="text" class="yqx-input js-license-select search" name="license" data-v-type="licence"  value="${license?default('')}" placeholder="请输入车牌..."/>
                    <input name="carId" type="hidden" value="${carId}">
                </div>
            </div>
        </div>
        <input type="hidden" name="accountId">
        <div class="member-info">
            <div class="personal-info clearfix">
                <input type="hidden" name="customerId">
                <div class="form-label">账户信息</div>
                <div class="form-label">车主：<span class="customerName"></span></div>
                <div class="form-label">车主电话：<span class="mobile"></span></div>
                <button class="edit-btn fr">编辑</button>
            </div>
            <div class="license clearfix">
                <div class="form-label license-label">
                    拥有车辆
                </div>
                <div class="carLicense"></div>
            </div>
            <div class="card-num">
                <div class="consultant fr">
                    <div class="form-label">
                        服务顾问
                    </div>
                    <div class="form-item">
                        <input type="text" name="receiverName" class="yqx-input yqx-input-icon yqx-input-small js-combo-receiver" value="${SESSION_USER_NAME}" placeholder="请选择服务顾问">
                        <input type="hidden" name="receiverId" value="${SESSION_USER_ID}"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="choose-type">
            <div class="js-choose-type"></div>
            <div class="button">
                <button class="yqx-btn yqx-btn-2 js-submit-btn">提交</button>
                <a href="${BASE_PATH}/account" class="yqx-btn yqx-btn-1 fr">返回</a>
            </div>
        </div>
    </div>
</div>


<script type="text/template" id="collection">
    <div class="collection-bounce">
        <div class="bounce-title">
            收款
        </div>
        <div class="bounce-content">
            <div class="collection-title">
                <span class="bold">计次卡办理</span>—收款
            </div>
            <div class="combo-type">
                <div class="form-label">计次卡类型：</div>
                <div class="form-item">
                    <input type="text" class="yqx-input" value="<%=type%>" disabled>
                    <input type="hidden" name="comboInfoId" value="<%=id%>">
                </div>
            </div>
            <div class="receivable-amount">
                <div class="form-label">应收金额：</div>
                <div class="form-item">
                    <input type="text" name="amount" class="yqx-input yqx-input-icon" value="<%=amount%>" disabled>
                    <span class="fa">元</span>
                </div>
            </div>
            <div class="collection">
                <div class="form-label">收款：</div>
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon js-payment-select" name="paymentName" data-v-type="required" placeholder="请选择支付方式"/>
                    <input name="paymentId" type="hidden">
                    <span class="fa icon-angle-down"></span>
                </div>
                <div class="form-item">
                    <input type="text" name="payAmount" class="yqx-input yqx-input-icon" data-v-type="number| required" placeholder="金额">
                    <span class="fa">元</span>
                </div>
            </div>
            <button class="yqx-btn yqx-btn-2 js-submit">提交</button>
        </div>
    </div>
</script>

<script type="text/template" id="chooseTpl">
    <div class="choose-title">选择计次卡类型</div>
    <%if(data.length > 0 ){%>
    <%for(var i=0;i< data.length;i++){%>
    <%var item=data[i]%>
    <div class="cardTpl">
        <input type="radio" value="<%=item.id%>" name="id"/>
        <div class="info-item">
            <img src="${BASE_PATH}/static/img/page/account/combo.png">
            <span class="ml35 bold">计次卡&#12288;&#12288;<em class="comboName"><%=item.comboName%></em></span>
            <div class="unfold fr">
                <span class="fa icon-angle-down icon-small"></span>
                展开
            </div>
            <div class="fold fr">
                <span class="fa icon-angle-up icon-small"></span>
                收起
            </div>
            <div class="price fr bold">售价：<span class="red money-font">&yen;<span class="salePrice"><%=item.salePrice%></span></span></div>
        </div>
        <div class="detail js-detail-box">
            <%if(item.content&&item.content.length){%>
            <div class="service-item">
                <div class="service-title">
                    服务项目：
                </div>
                <div class="service-content">
                    <%for(var j=0;j< item.content.length;j++){%>
                    <%var serviceItem=item.content[j]%>
                    <input type="hidden" value="<%=serviceItem.serviceId%>">
                    <div class="content-item"><span class="service-name js-show-tips"><%=serviceItem.serviceName%></span><span class="service-count"><%=serviceItem.serviceCount%>次</span></div>
                    <%}%>
                </div>
            </div>
            <%}%>
            <% if(item.customizeTime == 1){ %>
            <div class="service-item">
                <div class="service-title">
                    生效时间：
                </div>
                <div class="service-content">
                    <%=item.effectiveDateStr%>
                </div>
            </div>
            <div class="service-item">
                <div class="service-title">
                    过期时间：
                </div>
                <div class="service-content">
                    <%=item.expireDateStr%>
                </div>
            </div>
            <% }else if(item.customizeTime == 0){ %>
            <div class="service-project clearfix">
                <div class="show-grid">
                    <div class="voucher-label service-title">
                        生效时间：
                    </div>
                    <div class="voucher-item service-content">
                        发放后立即生效可用
                    </div>
                </div>
                <div class="show-grid">
                    <div class="voucher-label service-title">
                        过期时间：
                    </div>
                    <div class="voucher-item service-content">
                        发放后<%=item.effectivePeriodDays%>天
                    </div>
                </div>
            </div>
            <%}%>
            <div class="service-item" style="margin-top: 10px;">
                <div class="service-title">
                    备注：
                </div>
                <div class="service-content">
                    <div class="content-item"><%=item.remark%></div>
                </div>
            </div>
        </div>
    </div>
    <%}%>
    <%}else{%>
    <div class="nodata clearfix">
        <div class="nodata-l fl">
            <p>亲，您还没有计次卡类型</p>
            <p>赶紧去创建计次卡类型吧！</p>
            <a href="${BASE_PATH}/account/combo/create"  class="yqx-btn yqx-btn-2 yqx-btn-small">新建计次卡类型</a>
        </div>
        <div class="nodata-r fr">
            <i class="triangle-left"></i>
            <div class="nodata-tips">
                <p>暂无数据</p>
            </div>
        </div>
    </div>
    <%}%>
</script>
<script type="text/html" id="carLicenceTpl">
    <%if(templateData && templateData.length > 0){%>
    <ul class="yqx-downlist-content js-downlist-content" data-tpl-ref="car-licence-tpl">
        <%for (var i = 0; i < templateData.length; i++) {%>
        <%var item = templateData[i];%>
        <li class="js-downlist-item">
            <span title="<%=item.id%>" style="width:100%"><%=item.license%></span>
        </li>
        <%}%>
    </ul>
    <%}else{%>
    <div class="downlist-new-car yqx-dl-no-data js-new-car">
        车牌不存在,请先<a href="#">新建车辆</a>
    </div>
    <%}%>
</script>
<script type="text/template" id="newLisenceTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            添加车辆
        </div>
        <div class="bounce-content">
            <div class="collection-title">
                <span class="bold">输入车牌</span>
            </div>
            <div class="combo-type">
                <div class="form-label">车牌：</div>
                <div class="form-item">
                    <input type="text" class="yqx-input" placeholder="例：浙A88888">
                </div>
            </div>
            <button class="yqx-btn yqx-btn-2 js-submit">提交</button>
        </div>
    </div>
</script>


<script type="text/template" id="completeCarTpl">
    <div class="account-no-exist-dg">
        <div class="member-card-title">
            账户不存在
        </div>
        <div class="content">
            <p class="mgt40">该车辆<span class="highlight"><%= carLicense%></span>未绑定任何账户,请先完善该车辆信息中的<span class="highlight">车主电话</span></p>
            <p><a class="complete_info" href="${BASE_PATH}/shop/customer/edit?refer=customer&id=<%=carId%>">完善信息</a></p>
        </div>
    </div>
</script>


<!--新增车辆 模板-->
<script type="text/html" id="addlicenseTpl">
    <div class="dialog">
        <div class="dialog-title">新增车辆</div>
        <div class="dialog-con">
            <div class="car-infor">
                <div class="show-grid">
                    <div class="form-label">
                        车牌
                    </div>
                    <div class="form-item">
                        <input type="text" name="license" class="yqx-input yqx-input-small js-search">
                    </div>
                </div>
                <div class="show-grid license-show">
                    <div class="tip mb10"></div>
                    <div class="mb10" id="un-bundle-tip">车牌 <span class="carlicense-number"></span>已绑定其它账户（<a href="javascript:;" class="un-bundle-mobile"></a>），<a href="javascript:;" class="link-bd js-link-bd">请先去该账户解绑！</a></div>
                </div>
            </div>
            <div class="dialog-btn">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-bundle">提交</button>
            </div>
        </div>
    </div>
</script>
<script src="${BASE_PATH}/static/js/page/account/combo/grant.js?632a4e9e81668c02278cfff014d3f952"></script>

<#include "yqx/layout/footer.ftl">