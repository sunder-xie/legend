<#--银行卡信息-->
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/ax_insurance/settle/bank-card-detail.css?8bc988ded268f97f101fb8f1c314de49"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">
    <div class="content clearfix">
        <!-- 左侧导航区 start -->
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <!-- 左侧导航区 end -->
        <!-- 右侧内容区 start -->
        <div class="order-right fl">
            <div class="title clearfix">
                <h3 class="fl">银行卡信息</h3>
                <p class="fr">提示:与保险业务相关的门店收入，每月由淘汽档口财务统一打入以下银行账户</p>
            </div>
            <div class="form-box" id="bankInfo">
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        开户银行
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${account.bank}
                        </div>
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${account.bankProvince}
                        </div>
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${account.bankCity}
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        开户支行
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${account.accountBank}
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        收款人
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${account.accountUser}
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        银行卡号
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                        ${account.account}
                        </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        联系电话
                    </div>
                    <div class="form-item">
                        <div class="yqx-text">
                            ${mobile}
                        </div>
                    </div>
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-2 yqx-btn-small js-modify">修改</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goback">返回</button>
            </div>
        </div>
        <!-- 右侧内容区 end -->
    </div>
</div>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/ax_insurance/settle/bank-card-detail.js?0089ecbe946697ac1d911da0a034ffda"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">