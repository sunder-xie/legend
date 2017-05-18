<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/favormall-card-edit.css?2c4ca3035ff20f24e5f51fe72049c2ea"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="aside-main">
        <div class="order-right">
            <div class="order-head clearfix">
                <h1 class="headline">卡券商城设置<span class="font-normal">-<#if wechatFavormallCardVo.id>编辑<#else>新增</#if>会员卡</span></h1>
            </div>
        </div>
        <input type="hidden" name="givingNumberOld" value="${wechatFavormallCardVo.givingNumber}">
        <input type="hidden" name="givingAmountOld" value="${wechatFavormallCardVo.givingAmount}">
        <input type="hidden" name="receiveNumber" value="${wechatFavormallCardVo.receiveNumber}">
        <div class="order-body js-card-config">
            <input type="hidden" name="id" value="${wechatFavormallCardVo.id}">
            <input type="hidden" name="givingStatus" value="${wechatFavormallCardVo.givingStatus}">
            <div class="choose-card">
                <div class="form-label form-label-must w100">
                    选择会员卡
                </div>
                <div class="form-item">
                    <input type="text" <#if wechatFavormallCardVo.id>disabled</#if> class="yqx-input yqx-input-icon js-choose-card" data-v-type='required' value="${wechatFavormallCardVo.typeName}">
                    <input type="hidden" name="cardTypeId" value="${wechatFavormallCardVo.cardTypeId}">
                    <span class="fa icon-angle-down"></span>
                </div>
                <div class="form-label explain">
                    只可以上架售价和会员余额为0的会员卡
                </div>
                <div class="card-btn">
                    <a class="yqx-btn yqx-btn-1 yqx-btn-small" href="${BASE_PATH}/account/setting?flag=3" target="_blank">管理会员卡</a>
                    <a class="yqx-btn yqx-btn-1 yqx-btn-small" href="${BASE_PATH}/account/member/create" target="_blank">新增会员卡</a>
                </div>
            </div>
            <div class="card-explain">
                <div class="form-label w100">
                    会员卡优惠说明
                </div>
                <div class="form-item">
                    <textarea class="yqx-textarea" cols="50" rows="3" name="privilegeDesc" placeholder="例如：国庆大聚惠，凡在10月1日至7日办理会员卡，即享充值500送300的优惠，工时费还打8折，数量有限，赶紧来办理吧~~">${wechatFavormallCardVo.privilegeDesc}</textarea>
                </div>
                <div class="form-label explain">
                    说明好会员卡的优惠值，<br>可吸引更多车主办理会员卡哦
                </div>
            </div>
            <div class="is-send">
                <div class="form-label w100">
                    是否赠送
                </div>
                <div class="form-item">
                    <label class="marR20">
                        <input class="radio" type="radio" name="isGiving" value="1" <#if wechatFavormallCardVo.isGiving==1 ||wechatFavormallCardVo.isGiving==null>checked</#if>/> 赠送
                    </label>
                    <label>
                        <input class="radio" type="radio" name="isGiving" value="0" <#if wechatFavormallCardVo.isGiving==0>checked</#if>/> 仅做展示
                    </label>
                </div>
                <div class="card-setting <#if wechatFavormallCardVo.isGiving==0>display-none</#if>">
                    <div class="form-label form-label-must">
                        赠送数量
                    </div>
                    <div class="form-item">
                        <input type="text" name="givingNumber" class="yqx-input yqx-input-icon" value="${wechatFavormallCardVo.givingNumber}"
                               data-v-type='required | integer'>
                        <span class="fa">张</span>
                    </div>
                    <div class="form-label form-label-must">
                        会员卡额度
                    </div>
                    <div class="form-item">
                        <input type="text" <#if wechatFavormallCardVo.givingAmount>disabled</#if> name="givingAmount" class="yqx-input yqx-input-icon" value="${wechatFavormallCardVo.givingAmount!"0.00"}"
                               data-v-type='required | price'>
                        <span class="fa">元</span>
                        <#if wechatFavormallCardVo.givingStatus !=3 && wechatFavormallCardVo.isGiving == 1>
                            <div class="tip">赠送金额无法修改，如需修改请重新新增会员卡</div>
                        </#if>
                    </div>
                </div>

            </div>
            <#if wechatFavormallCardVo.givingStatus!=3>
                <button class="yqx-btn yqx-btn-3 save js-save">保存</button>
                <#if wechatFavormallCardVo.isGiving==0>
                    <button class="yqx-btn yqx-btn-3 js-cancel">取消展示</button>
                <#else>
                    <#if wechatFavormallCardVo.givingStatus==1 || wechatFavormallCardVo.givingStatus==2>
                        <button class="yqx-btn yqx-btn-3 js-cancel">取消赠送</button>
                    </#if>
                </#if>
            </#if>
        </div>
    </div>
</div>
<script src="${BASE_PATH}/static/js/page/wechat/favormall-card-edit.js?6a6e8a6ea5c07f0190c15c265b292e10"></script>
<#include "yqx/layout/footer.ftl" >