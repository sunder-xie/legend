<#--新增录入页面-->
<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" type="text/css" rel="stylesheet">
<link href="${BASE_PATH}/static/css/page/ax_insurance/offline_insurance/new-create.css?f346977eb27873aafd2a350db16c2298" type="text/css" rel="stylesheet">
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
            <div class="contain-wrap">
                <p class="big-title">新增录入
                    <a class="goBack" href="${BASE_PATH}/insurance/offline/list"><span> 返回 ></span></a>
                </p>
                <div class="main-wrap">
                    <p class="note">
                        注： 带*为必填项，请认真填写，否则会影响审核的通过率<br/>
                    </p>
                    <div class="input-li">
                        <label class="form-label-must">车牌号</label>
                        <input class="yqx-input input" placeholder='请输入(未取得车牌可备注"新车")' id="license-number">
                    </div>
                    <p class="medium-title">商业险信息<span>(未购买可不填)</span></p>
                    <div class="Info-wrap Info-wrap-sy">
                        <div class="input-li">
                            <label class="form-label-must">商业险保费</label>
                            <input class="yqx-input input must-input sy-must" placeholder="请输入金额"
                                   data-valName="commercialFee" data-v-type="floating">
                        </div>
                        <div class="input-li">
                            <label >商业险保单号</label>
                            <input class="yqx-input input selective-sy selective-input" placeholder="请输入单号" data-valName="commercialFormNo">
                        </div>
                    </div>
                    <p class="medium-title"> 交强险信息<span>(未购买可不填)</span></p>
                    <div class="Info-wrap Info-wrap-jq">
                        <div class="input-li">
                            <label class="form-label-must">交强险保费</label>
                            <input class="yqx-input input must-input jq-must" placeholder="请输入金额"
                                   data-valName="forcibleFee" data-v-type="floating">
                        </div>
                        <div class="input-li">
                            <label class="form-label-must">车船税</label>
                            <input class="yqx-input input must-input jq-must" placeholder="请输入金额"
                                   data-valName="vesselTaxFee" data-v-type="floating">
                        </div>
                        <div class="input-li">
                            <label >交强险保单号</label>
                            <input class="yqx-input input selective-jq selective-input" placeholder="请输入单号" data-valName="forcibleFormNo">
                        </div>
                    </div>
                    <button class="offline-btn cancel-btn">取消</button>
                    <button class="offline-btn confirm-btn">提交</button>
                </div>


            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/offline_insurance/new-create.js?55685f7e394d0965952593a395f46e06"></script>
<#include "yqx/layout/footer.ftl">