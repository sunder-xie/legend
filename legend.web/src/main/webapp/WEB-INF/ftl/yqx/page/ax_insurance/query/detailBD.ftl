<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/query/detailBD.css?095d5e33972901a23a739549c7771d25"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="Z-right">
        <div class="Z-title Z-header">
            <span>保单详情</span>
            <dl>
                <dd class="Z-back">返回</dd>
            </dl>
        </div>
    <#assign vo = InsuranceFormDetail>
        <div class="Z-group">
            <div class="yqx-group">
                <!-- group标题 start -->
                <div class="yqx-group-head">
                    保单信息
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content Z-tbd-info">
                    <p>
                        <span>保单号：${vo.outerInsuranceFormNo}</span>
                        <#if (vo.quitStatus == 0)>
                            <span>缴费时间：<#if vo.payTime??>${vo.payTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                        <#elseif (vo.quitStatus == 1)>
                            <span>创建时间：<#if vo.gmtCreate??>${vo.gmtCreate?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                        </#if>
                    </p>
                    <p>
                        <span>保单状态：
                            <#if (vo.quitStatus == 0)>
                                未退保
                            <#elseif (vo.quitStatus == 1)>
                                已退保
                            </#if>
                        </span>
                        <#if (vo.deductibleAmount?? && vo.deductibleAmount !="")>
                        <#--车主优惠-->
                            <span class="Z-coupon">车主优惠：<i>券</i><em>&yen;${vo.deductibleAmount}</em></span>
                        <#--退保金额-->
                            <#if (vo.quitStatus == 1)>
                                <span class="changeToBlock">退保金额：<em> &yen;1000.00</em></span>
                            </#if>
                        <#else>
                            <#if (vo.quitStatus == 1)>
                                <span class="changeToBlock">退保金额：<em> &yen;1000.00</em></span>
                            </#if>
                        </#if>
                        <#--<#if (vo.quitStatus == 1)>-->
                            <#--<span>退保金额：<em>1000.00</em></span>-->
                        <#--&lt;#&ndash;隐藏奖励金&ndash;&gt;-->
                        <#--<#elseif (vo.quitStatus == 0)>-->
                            <#--<span>奖励金：<i class="insurance-money"></i>-->
                                    <#--<em>奖励金：700.00</em>-->
                            <#--</span>-->
                        <#--</#if>-->
                        <#--<span>保费：<em>${vo.insuredFee}</em></span>-->
                    </p>
                    <p>
                        <span>保费：<em>&yen;${vo.insuredFee}</em></span>
                    </p>
                </div>
                <!-- group内容 end -->
            </div>
            <div class="yqx-group">
                <!-- group标题 start -->
                <div class="yqx-group-head">
                    车辆详细信息
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content Z-car-info">
                    <p>
                        <span>车牌号码：${vo.basicVo.vehicleSn}</span>
                        <span>投保地：${vo.basicVo.insuredProvince}-${vo.basicVo.insuredCity}</span>
                    </p>
                    <p>
                        <span>发动机号：${vo.basicVo.carEngineSn}</span>
                        <span>车架号：${vo.basicVo.carFrameSn}</span>
                    </p>
                    <p>
                        <span>品牌型号：${vo.basicVo.carConfigType}</span>
                    </p>
                </div>
                <!-- group内容 end -->
            </div>
            <div class="yqx-group">
                <!-- group标题 start -->
                <div class="yqx-group-head">
                    保障内容
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content Z-insurance-info">
                    <table>
                        <thead>
                        <tr>
                            <th>保障项目</th>
                            <th>保额</th>
                            <#if (vo.insuranceType == 2)>
                                <th>不计免赔</th>
                            </#if>
                            <th>保费</th>
                        </tr>
                        </thead>
                        <tbody class="main-insurance">
                            <#list vo.itemVoList as itemList>
                                <tr>
                                    <td>${itemList.insuranceName}</td>
                                    <td>${itemList.insuranceAmount}</td>
                                    <#if (vo.insuranceType == 2)>
                                        <td>${itemList.isDeductible}</td>
                                    </#if>
                                    <td>${itemList.insuranceFee}</td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                    <#if (vo.insuranceType == 1)>
                        <p class="insurance-time">
                            <span>交强险生效时间：<#if (vo.packageStartTime??)>${vo.packageStartTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                            <span>交强险失效时间：<#if (vo.packageEndTime??)>${vo.packageEndTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                        </p>
                    <#elseif (vo.insuranceType == 2)>
                        <p class="insurance-time">
                            <span>商业险生效时间：<#if (vo.packageStartTime??)>${vo.packageStartTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                            <span>商业险失效时间：<#if (vo.packageEndTime??)>${vo.packageEndTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                        </p>
                    </#if>
                </div>
                <!-- group内容 end -->
            </div>
            <div class="yqx-group">
                <!-- group标题 start -->
                <div class="yqx-group-head">
                    车主信息
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content Z-owner-info">
                    <p>
                        <span>姓名：${vo.basicVo.vehicleOwnerName}</span>
                        <span>手机号：${vo.basicVo.vehicleOwnerPhone}</span>
                    </p>
                    <p>
                        <span>证件类型：
                            <#if (vo.basicVo.vehicleOwnerCertType == '120001')>
                                居民身份证
                            <#elseif (vo.basicVo.vehicleOwnerCertType == '120002')>
                                护照
                            <#elseif (vo.basicVo.vehicleOwnerCertType == '120003')>
                                军人证
                            </#if>
                        </span>
                        <span>证件号码：${vo.basicVo.vehicleOwnerCertCode}</span>
                    </p>
                </div>
                <!-- group内容 end -->
            </div>
            <div class="yqx-group">
                <!-- group标题 start -->
                <div class="yqx-group-head">
                    被保人信息
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content Z-sured-info">
                    <p>
                        <span>姓名：${vo.basicVo.insuredName}</span>
                        <span>手机号：${vo.basicVo.insuredPhone}</span>
                    </p>
                    <p>
                        <span>证件类型：
                            <#if (vo.basicVo.insuredCertType == '120001')>
                                居民身份证
                            <#elseif (vo.basicVo.insuredCertType == '120002')>
                                护照
                            <#elseif (vo.basicVo.insuredCertType == '120003')>
                                军人证
                            </#if>
                        </span>
                        <span>证件号码：${vo.basicVo.insuredCertCode}</span>
                    </p>
                </div>
                <!-- group内容 end -->
            </div>

            <div class="yqx-group">
                <!-- group标题 start -->
                <div class="yqx-group-head">
                    投保人信息
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content Z-sured-info">
                    <p>
                        <span>姓名：${vo.basicVo.applicantName}</span>
                        <span>手机号：${vo.basicVo.applicantPhone}</span>
                    </p>
                    <p>
                        <span>证件类型：
                            <#if (vo.basicVo.applicantCertType == '120001')>
                                居民身份证
                            <#elseif (vo.basicVo.applicantCertType == '120002')>
                                护照
                            <#elseif (vo.basicVo.applicantCertType == '120003')>
                                军人证
                            </#if>
                        </span>
                        <span>证件号码：${vo.basicVo.applicantCertCode}</span>
                    </p>
                </div>
                <!-- group内容 end -->
            </div>
            <div class="yqx-group">
                <!-- group标题 start -->
                <div class="yqx-group-head">
                    配送信息
                </div>
                <!-- group标题 end -->
                <!-- group内容 start -->
                <div class="yqx-group-content Z-sured-info">
                    <p>
                        <span>收件人姓名：${vo.basicVo.receiverName}</span>
                        <span>手机号：${vo.basicVo.receiverPhone}</span>
                    </p>
                    <p>
                        <span>收件人地址：${vo.basicVo.receiverProvince} ${vo.basicVo.receiverCity} ${vo.basicVo.receiverArea} ${vo.basicVo.receiverAddr}</span>
                    </p>
                </div>
                <!-- group内容 end -->
            </div>
        </div>
        <div class="Z-title Z-footer">
            <dl>
                <dd class="Z-back">返回</dd>
            </dl>
        </div>
    </div>
</div>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/ax_insurance/query/detailBD.js?7e8e568d952d77e1a1864d2bb2b19c11"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">