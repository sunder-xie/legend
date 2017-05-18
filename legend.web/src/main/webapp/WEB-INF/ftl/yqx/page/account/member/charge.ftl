<#include "yqx/layout/header.ftl">
<div class="yqx-wrapper clearfix">
    <link rel="stylesheet" href="${BASE_PATH}/static/css/page/account/charge.css?86590a2a4bb53fc79828e4328eec8482" type="text/css"/>
    <div class="left-menu">
    <#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    </div>
    <div class="right-content">
        <h3 class="Z-title">客户管理 > <a href="${BASE_PATH}/account"> 客户查询</a> > <i>充值</i></h3>
        <div class="content clearfix">
            <h3>选择会员卡</h3>
            <ul>
            <#list memberCardList as memberCard>
                <li class="js-card <#if memberCard_index == 0>hover</#if>">
                    <input type="hidden" value="${memberCard.memberCardId}" class="card-id">
                    <div class="card-type ellipsis-1">${memberCard.cardTypeName}</div>
                    <div class="card-num">${memberCard.cardNum}</div>
                    <div class="price"><span>余　　额：</span><span class="money">${memberCard.balance}</span></div>
                    <div class="price"><span>过期时间：</span><span>${memberCard.expireDateStr}</span></div>
                </li>
            </#list>
            </ul>
        </div>
        <div class="content">
            <div class="input-info">
                <div class="pay-way">
                    <div class="form-label form-label-must">
                        支付方式
                    </div>
                    <div class="form-item">
                        <input type="text" name="paymentName" class="yqx-input yqx-input-icon js-payment-select" data-v-type="required" value="" placeholder="请选择支付方式">
                        <input name="paymentId" type="hidden">
                        <span class="fa icon-angle-down"></span>
                    </div>
                </div>
                <div class="amount">
                    <div class="form-label form-label-must">
                        会员卡充值金额
                    </div>
                    <div class="form-item">
                        <input type="text" name="amount" class="yqx-input yqx-input-icon" data-v-type="number | required" placeholder="请输入充值金额">
                        <span class="fa">元</span>
                    </div>
                </div>
                <div class="collection">
                    <div class="form-label form-label-must">
                        收款金额
                    </div>
                    <div class="form-item">
                        <input type="text" name="payAmount" class="yqx-input yqx-input-icon" data-v-type="required | number">
                        <span class="fa">元</span>
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
                <button class="yqx-btn yqx-btn-2 submit">提交</button>
                <button class="yqx-btn yqx-btn-1 fr return" onclick="javascript:history.back(-1);">返回</button>
            </div>
        </div>
    </div>　
</div>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/account/member/charge.js?69e1b8ce408825bb2fcd04317797683b"></script>
<#include "yqx/layout/footer.ftl">