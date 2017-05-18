<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css"
      href="${BASE_PATH}/static/css/page/ax_insurance/query/detailTBD.css?b291171edafe27180073d95a08f35a55"
      xmlns="http://java.sun.com/jsf/html"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="Z-right">
        <div class="Z-title Z-header">
            <span>投保单详情</span>
            <dl>
                <#if (InsuranceFormDetail.insureStatus == 0)>
                    <dd class="Z-reCal" data-basicid="${InsuranceFormDetail.basicVo.id}">重新计算保费</dd>
                </#if>
                <#if (InsuranceFormDetail.insureStatus == 2 && uploadPhotos)>
                    <dd class="rendImg" data-sn="${InsuranceFormDetail.orderSn}">上传照片</dd>
                <#elseif InsuranceFormDetail.insureStatus == 2>
                    <dd class="goPay" data-sn="${InsuranceFormDetail.orderSn}">去缴费</dd>
                </#if>
                <dd class="Z-back">返回</dd>
            </dl>
        </div>
        <#assign vo = InsuranceFormDetail>
            <div class="Z-group">
                <div class="yqx-group">
                    <!-- group标题 start -->
                    <div class="yqx-group-head">
                        投保单信息
                    </div>
                    <!-- group标题 end -->
                    <!-- group内容 start -->
                    <div class="yqx-group-content Z-tbd-info">
                        <p>
                            <span>投保单号：${vo.outerInsuranceApplyNo}</span>
                            <span>创建时间：${vo.gmtCreate?string("yyyy-MM-dd HH:mm:ss")} </span>
                        </p>
                        <p>
                            <span>投保单状态：
                                <#if (vo.insureStatus == 0)>
                                    暂存
                                <#elseif (vo.insureStatus == 1)>
                                    核保失败
                                <#elseif (vo.insureStatus == 2)>
                                    待缴费
                                <#elseif (vo.insureStatus == 3)>
                                    生成保单
                                </#if>
                            </span>
                            <#if (vo.deductibleAmount?? && vo.deductibleAmount !="")>
                                <#--车主优惠-->
                                <span class="Z-coupon">车主优惠：<i>券</i><em> &yen; ${vo.deductibleAmount}</em></span>
                                <#--保费-->
                                <#if (vo.insureStatus != 0)>
                                    <span class="changeToBlock">保费：<em>&yen; ${vo.insuredFee}</em></span>
                                </#if>
                            <#else>
                                <#if (vo.insureStatus != 0)>
                                    <span>保费：<em>&yen;${vo.insuredFee}</em></span>
                                </#if>
                            </#if>
                            <#--隐藏奖励金-->
                            <#--<#if (vo.insureStatus != 0)>-->
                                <#--<span>奖励金：<i class="insurance-money"></i>-->
                                <#--&lt;#&ndash;投保单详情交强险合商业险投保单状态不为暂存时的部分&ndash;&gt;-->
                                    <#--<em>奖励金：${vo.rewardFee}</em>-->
                                <#--</span>-->
                            <#--</#if>-->
                        </p>
                    <#--投保单详情交强险合商业险投保单状态不为暂存时的部分-->
                        <#--<#if (vo.insureStatus != 0)>-->
                            <#--<p>-->
                                <#--<span>保费：<em>${vo.insuredFee}</em></span>-->
                            <#--</p>-->
                        <#--</#if>-->
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
                        <#if (vo.insuranceType == 1)>
                        <#--投保单详情保障内容交强险部分-->
                            <div class="insurance-detail">
                                <p>
                                    <span>交强险保障项目</span>
                                    <span>保额</span>
                                </p>
                                <#list vo.itemVoList as itemList>
                                    <p>
                                        <span>${itemList.insuranceName}</span>
                                        <span>${itemList.itemCoverageValueDisplay}</span>
                                    </p>
                                </#list>
                            </div>
                            <p class="insurance-time">
                                <span>交强险生效时间：<#if (vo.packageStartTime??)>${vo.packageStartTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                                <span>交强险失效时间：<#if (vo.packageEndTime??)>${vo.packageEndTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                            </p>
                        <#elseif (vo.insuranceType == 2)>
                        <#--投保单详情保障内容商业险部分-->
                            <table>
                                <thead>
                                <tr>
                                    <th></th>
                                    <th>保障项目</th>
                                    <th>保额</th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody class="main-insurance">
                                    <#assign flag1 = 0>
                                    <#list mainList as mList>
                                <tr>
                                    <#if flag1 == 0>
                                        <td rowspan="${mainList?size}">主险</td>
                                    </#if>
                                    <td>${mList.insuranceName}</td>
                                    <td>${mList.insuranceAmount}</td>
                                    <#if (mList.isDeductible) == 0>
                                        <td><input type="checkbox" disabled checked/>不计免赔</td>
                                    <#elseif mList.isDeductible == 1>
                                        <td><input type="checkbox" disabled/>不计免赔</td>
                                    <#else>
                                        <td></td>
                                    </#if>
                                </tr>
                                    <#assign flag1 = 1>
                                </#list>
                                </tbody>

                                <tbody class="main-insurance">
                                    <#assign flag2 = 0>
                                <#list subList as sList>
                                <tr>
                                    <#if flag2 == 0>
                                        <td rowspan="${subList?size}">附加险</td>
                                    </#if>
                                    <td>${sList.insuranceName}</td>
                                    <td>${sList.insuranceAmount}</td>
                                    <#if (sList.isDeductible) == 0>
                                        <td><input type="checkbox" disabled checked/>不计免赔</td>
                                    <#elseif sList.isDeductible == 1>
                                        <td><input type="checkbox" disabled/>不计免赔</td>
                                    <#else>
                                        <td></td>
                                    </#if>
                                </tr>
                                    <#assign flag2 = 1>
                                </#list>
                                </tbody>
                            </table>
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
            <#if (vo.insureStatus != 0)>
            <#--投保单详情交强险投保单状态不为暂存时的部分-->
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
            </#if>
            </div>
        <div class="Z-title Z-footer">
            <dl>
                <#if (InsuranceFormDetail.insureStatus == 0 || InsuranceFormDetail.insureStatus ==1)>
                    <dd class="Z-reCal" data-basicid="${InsuranceFormDetail.basicVo.id}">重新计算保费</dd>
                </#if>
                <#if InsuranceFormDetail.insureStatus == 2 && uploadPhotos>
                    <dd class="rendImg" data-sn="${InsuranceFormDetail.orderSn}">上传照片</dd>
                <#elseif InsuranceFormDetail.insureStatus == 2>
                    <dd class="goPay" data-sn="${InsuranceFormDetail.orderSn}">去缴费</dd>
                </#if>
                <dd class="Z-back">返回</dd>
            </dl>
        </div>
    </div>
</div>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/ax_insurance/query/detailTBD.js?ac25e75c902f64482a3f4a91359467cd"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">