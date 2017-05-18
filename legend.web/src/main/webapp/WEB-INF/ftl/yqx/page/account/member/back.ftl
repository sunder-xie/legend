<#include "yqx/layout/header.ftl">
<div class="yqx-wrapper clearfix">
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/back.css?4af0d9b35da6fcf0eef9c43c98a01a3e"/>
    <div class="left-menu">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="right-content">
        <h3 class="Z-title">客户管理 > <a href="${BASE_PATH}/account"> 客户查询</a> > <i>退卡</i></h3>
        <div class="content clearfix">
            <h3>选择会员卡</h3>
            <ul>
            <#list memberCardList as memberCard>
                <li class="js-card <#if memberCard_index == 0>hover</#if>">
                    <input type="hidden" value="${memberCard.memberCardId}" class="card-id">
                    <div class="card-type ellipsis-1">${memberCard.cardTypeName}</div>
                    <div class="card-num">${memberCard.cardNum}</div>
                    <div class="card-time">办卡时间：${memberCard.grantTimeStr}</div>
                    <div class="price"><span>余　　额：</span><span class="money">${memberCard.balance}</span></div>
                    <div class="price"><span>累计收款：</span><span class="money">${memberCard.totalPayAmount}</span></div>
                    <div class="price"><span>累计充值：</span><span class="money">${memberCard.depositAmount}</span></div>
                </li>
            </#list>
            </ul>
        </div>
        <div class="content upgrade-info">
            <div class="input-info">
                <div class="pay-way line">
                    <div class="form-label form-label-must">
                        退款方式
                    </div>
                    <div class="form-item">
                        <input type="text" name="paymentName" class="yqx-input yqx-input-icon js-payment-select" data-v-type="required" value="" placeholder="请选择支付方式">
                        <span class="fa icon-angle-down"></span>
                        <input type="hidden" name="paymentId">
                    </div>
                </div>
                <div class="line">
                    <div class="form-label form-label-must">
                        退款金额
                    </div>
                    <div class="form-item">
                        <input type="text" name="payAmount" class="yqx-input yqx-input-icon" data-v-type="number | required" placeholder="请输入金额">
                        <span class="fa">元</span>
                    </div>
                </div>
                <div class="line">
                    <div class="form-label explain-tile">
                        说    明
                    </div>
                    <div class="form-item explain">
                        <p>退款金额请参考客户的充值记录！</p>
                        <p>退卡后，客户依然存在，但该客户的会员卡将被删除，且无法恢复，请谨慎操作！</p>
                    </div>
                </div>
            </div>
            <div class="remark">
                <div class="form-label w60">
                    备注信息
                </div>
                <div class="form-item w700">
                    <textarea class="yqx-textarea" name="remark" cols="100" rows="3"></textarea>
                </div>
            </div>
            <div class="button">
                <button class="yqx-btn yqx-btn-2 js-retreat">提交</button>
                <button class="yqx-btn yqx-btn-1 fr" onclick="javascript:history.back(-1);">返回</button>
            </div>
        </div>
    </div>　
</div>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/account/member/back.js?3be2b9f75cea58237a21e1dd6051c5b7"></script>
<#include "yqx/layout/footer.ftl">