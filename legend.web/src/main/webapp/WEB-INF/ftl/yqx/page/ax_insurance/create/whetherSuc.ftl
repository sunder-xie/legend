<#include "yqx/layout/header.ftl">
    <link href="${BASE_PATH}/static/css/common/base.min.css?d705b093d18aa6e933c0789c3eb6a59d" type="text/css" rel="stylesheet">
    <link href="${BASE_PATH}/static/css/page/ax_insurance/create/whetherSuc.css?280cd3ded4080087c70e3ee9e753262b" type="text/css" rel="stylesheet">
<body>
    <div class="wrapper clearfix">
        <div class="content clearfix">
        <#include "yqx/page/ax_insurance/left-nav.ftl">
            <div class="right">
            <#include "yqx/page/ax_insurance/process-nav.ftl">
                <div>
                    <p>上传照片</p>
                    <div class="allSec">
                        <div class="secLeft">
                            <input type="hidden" id="orderSn" value="${orderSn}">
                            <span class="cong">上传说明</span>
                            <div class="S_word">保险公司需要对车辆进行验车，请按以下要求上传照片!</div>
                            <div class="detail">
                                <span class="qip">1</span>
                                <span class="word">通过手机微信等扫码工具扫右侧二维码</span>
                            </div>
                            <div class="detail">
                                <span class="qip">2</span>
                                <span class="word">在手机上提交照片</span>
                            </div>
                            <div class="detail">
                                <span class="qip">3</span>
                                <span class="word">在电脑上等待上传完成</span>
                            </div>
                        </div>
                        <div class="secRight">
                            <div class="QR_code"></div>
                        </div>
                        <div class="QR_p"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script type="text/javascript" src="${BASE_PATH}/resources/js/lib/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/create/whetherSuc.js?a146f49674482a7466f6756cadbbe6d9"></script>
</body>
<#include "yqx/layout/footer.ftl">