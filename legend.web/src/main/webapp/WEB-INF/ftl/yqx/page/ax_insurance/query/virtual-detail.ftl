<!-- 虚拟保单详情-->
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/query/virtual-detail.css?d9771f7b79e0771265a56243344aad29"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="Z-right">
        <div class="Z-title Z-header">
            <span>虚拟投保单详情</span>
            <dl>
                <#--<#if virtualBasic.insuranceVirtualFormDTOList[0] && (virtualBasic.insuranceVirtualFormDTOList[0].insureStatus == 0 || virtualBasic.insuranceVirtualFormDTOList[0].insureStatus == 1)>-->
                <#--<dd class="Z-modify" data-id="${virtualBasic.id}">返回修改</dd>-->
                <#--</#if>-->
                <!-- 代缴费状态才能有去支付按钮-->
                    <#list virtualBasic.insuranceVirtualFormDTOList as list>
                        <#if (list.insuranceType == 2 && list.insureStatus == 0 && list.userServicePackageId)>
                            <a class="Z-reCal">去支付</a>
                        </#if>
                    </#list>
                <dd class="Z-back">返回</dd>
            </dl>
        </div>
    <#assign vo = virtualBasic>
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
                        <span>投保单号：${vo.insuranceOrderSn}</span>
                        <span>创建时间：${vo.gmtCreate?string("yyyy-MM-dd HH:mm:ss")} </span>
                    </p>
                    <p>
                        <#list vo.insuranceVirtualFormDTOList as list1>
                            <#if (list1.insuranceType == 2)>
                                <span>投保单状态：${list1.insuranceVirtualStatusName}</span>
                            </#if>
                        </#list>
                        <#if (vo.deductibleAmount?? && vo.deductibleAmount !="")>
                        <#--车主优惠-->
                            <span class="Z-coupon">车主优惠：<i>券</i><em> &yen;${vo.deductibleAmount}</em></span>
                        </#if>
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
                        <span>车牌号码：${vo.vehicleSn}</span>
                        <span>投保地：${vo.insuredProvince}-${vo.insuredCity}</span>
                    </p>
                    <p>
                        <span>发动机号：${vo.carEngineSn}</span>
                        <span>车架号：${vo.carFrameSn}</span>
                    </p>
                    <p>
                        <span>品牌型号：${vo.carConfigType}</span>
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
                <#list vo.insuranceVirtualFormDTOList as itemFormList>
                <#if (itemFormList.insuranceType == 1)>
                <#--投保单详情保障内容交强险部分-->
                    <div class="insurance-detail">
                        <p>
                            <span>交强险保障项目</span>
                            <span>保额</span>
                        </p>
                        <#list itemFormList.insuranceVirtualItemDTOList as itemList>
                            <p>
                                <span>${itemList.insuranceName}</span>
                                <span>${itemList.itemCoverageValueDisplay}</span>
                            </p>
                        </#list>
                    </div>
                    <p class="insurance-time">
                        <span>交强险生效时间：<#if (itemFormList.packageStartTime??)>${itemFormList.packageStartTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                        <span>交强险失效时间：<#if (itemFormList.packageEndTime??)>${itemFormList.packageEndTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                    </p>
                <#elseif (itemFormList.insuranceType == 2)>
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
                                    <td rowspan="${itemFormList.insuranceVirtualItemDTOList?size}">主险</td>
                                </#if>
                                <td>${mList.insuranceName}</td>
                                <td>${mList.itemCoverageValueDisplay}</td>
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
                                <td><#if sList.insuranceItemCoverageValue != 0>
                                        <!-- 对玻璃特殊处理-->
                                        <#if sList.insuranceCategoryCode== '033006'>
                                            <#if sList.insuranceItemCoverageValue == 1>
                                                进口玻璃
                                            <#elseif sList.insuranceItemCoverageValue == 2>
                                                国产玻璃
                                            <#elseif sList.insuranceItemCoverageValue == 3>
                                                进口特种
                                            <#elseif sList.insuranceItemCoverageValue == 4>
                                                国产特种
                                            </#if>
                                        <#else>
                                        ${sList.insuranceItemCoverageValue}
                                        </#if>
                                    <#else>投保</#if></td>
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
                    <p class="insurance-time js-business" data-package-id="${itemFormList.userServicePackageId}" data-insuranceordersn="${vo.insuranceOrderSn}">
                        <span>商业险生效时间：<#if (itemFormList.packageStartTime??)>${itemFormList.packageStartTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                        <span>商业险失效时间：<#if (itemFormList.packageEndTime??)>${itemFormList.packageEndTime?string("yyyy-MM-dd HH:mm:ss")} </#if></span>
                    </p>
                </#if>
                </#list>
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
                        <span>姓名：${vo.vehicleOwnerName}</span>
                        <span>手机号：${vo.vehicleOwnerPhone}</span>
                    </p>
                    <p>
                        <span>证件类型：
                            <#if (vo.vehicleOwnerCertType == '120001')>
                                居民身份证
                            <#elseif (vo.vehicleOwnerCertType == '120002')>
                                护照
                            <#elseif (vo.vehicleOwnerCertType == '120003')>
                                军人证
                            </#if>
                        </span>
                        <span>证件号码：${vo.vehicleOwnerCertCode}</span>
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
                        <span>姓名：${vo.insuredName}</span>
                        <span>手机号：${vo.insuredPhone}</span>
                    </p>
                    <p>
                        <span>证件类型：
                            <#if (vo.insuredCertType == '120001')>
                                居民身份证
                            <#elseif (vo.insuredCertType == '120002')>
                                护照
                            <#elseif (vo.insuredCertType == '120003')>
                                军人证
                            </#if>
                        </span>
                        <span>证件号码：${vo.insuredCertCode}</span>
                    </p>
                </div>
                <!-- group内容 end -->
            </div>
        </div>
        <div class="Z-title Z-footer">
            <dl>
                <#--<#if virtualBasic.insuranceVirtualFormDTOList[0] && (virtualBasic.insuranceVirtualFormDTOList[0].insureStatus == 0 || virtualBasic.insuranceVirtualFormDTOList[0].insureStatus == 1)>-->
                <#--<dd class="Z-modify" data-id="${virtualBasic.id}">返回修改</dd>-->
                <#--</#if>-->
                <!-- 代缴费状态才能有去支付按钮-->
                <#list virtualBasic.insuranceVirtualFormDTOList as list>
                    <#if (list.insuranceType == 2 && list.insureStatus == 0 && list.userServicePackageId)>
                        <a class="Z-reCal">去支付</a>
                    </#if>
                </#list>
                <dd class="Z-back">返回</dd>
            </dl>
        </div>
    </div>
</div>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/ax_insurance/query/virtual-detail.js?758719316f3c2552ada75371c0d86f73"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">