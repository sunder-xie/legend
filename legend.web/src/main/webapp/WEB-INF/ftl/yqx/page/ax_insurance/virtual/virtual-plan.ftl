<!-- 确认虚拟投保页面-->
<#--买服务包送保险-->
<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/page/ax_insurance/virtual/virtual-plan.css?765652e1e3ac3b8eb25516930611e6bd" type="text/css" rel="stylesheet">
<body>
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
        <#include "yqx/page/ax_insurance/virtual-process-nav.ftl">
            <p class="choose">确认投保信息</p>
            <div class="all_choose clearfix">
                <div class="allData">
                    <p class="detail_w">车辆详细信息</p>
                    <div class="car_detail">
                        <div class="dis-inB">
                            <span class="span_w">车牌号码:</span>
                            <input type="hidden" class="vehicleSn" value="${virtualBasic.vehicleSn}">
                        <#if virtualBasic.vehicleSn >
                            <div class="car js-car-license">${virtualBasic.vehicleSn}</div>
                        <#else>
                            <div class="car js-car-license">尚未取得车牌</div>
                        </#if>
                        </div>
                        <div class="dis-inB">
                            <span class="span_w">投保地:</span>
                            <div class="brand addr">${virtualBasic.insuredProvince}-${virtualBasic.insuredCity}</div>
                        </div>
                        <div class="dis-inB">
                            <span class="span_w">发动机号:</span>
                            <div class="car_price">${virtualBasic.carEngineSn}</div>
                        </div>
                        <div class="dis-inB">
                            <span class="span_w">车架号:</span>
                            <div class="car_price">${virtualBasic.carFrameSn}</div>
                        </div>
                        <div class="dis-inB dis-car">
                            <span class="span_w">品牌型号:</span>
                            <div class="car_price">${virtualBasic.carConfigType}</div>
                        </div>
                    </div>
                    <p class="choose_w">保障内容</p>
                <div class="scheme">
                <#assign DTOList = virtualBasic.insuranceVirtualFormDTOList>
                <#if DTOList && DTOList?size gt 0>
                    <#list DTOList as VirtualForm>
                        <#if VirtualForm.insuranceType ==1>
                            <#assign itemDTOList = VirtualForm.insuranceVirtualItemDTOList>
                            <#if itemDTOList && itemDTOList?size gt 0 >
                                <table>
                                    <tr>
                                        <th>交强险保障项目</th>
                                        <th>保额</th>
                                        <th>保费</th>
                                    </tr>
                                    <#list itemDTOList as itemDTOLi>
                                        <tr>
                                            <td>${itemDTOLi.insuranceName}</td>
                                            <td class="touB"><#if itemDTOLi.insuranceItemCoverageValue != 0>${itemDTOLi.insuranceItemCoverageValue}<#else>投保</#if></td>
                                            <td>未脱保</td>
                                        </tr>
                                    </#list>
                                    <tr>
                                        <td>车船税</td>
                                        <td class="touB"></td>
                                        <td>未脱保</td>
                                    </tr>
                                    <tr>
                                        <td>交强险合计</td>
                                        <td class="touB"></td>
                                        <td>未脱保</td>
                                    </tr>
                                </table>
                                <div class="time">
                                    <span class="startW">交强险生效时间</span>
                                    <span class="forceStartT">${VirtualForm.packageStartTime?string("yyyy-MM-dd HH:mm:ss")}</span>
                                    <span class="endW">交强险失效时间</span>
                                    <span class="forceEndT">${VirtualForm.packageEndTime?string("yyyy-MM-dd HH:mm:ss")}</span>
                                </div>
                            </#if>
                        <#elseif VirtualForm.insuranceType ==2>
                            <#assign itemDTOList2 = VirtualForm.insuranceVirtualItemDTOList>
                            <#if itemDTOList2 && itemDTOList2?size gt 0 >
                                <table data-formId = ${VirtualForm.id} class="formId2">
                                    <tr>
                                        <th>商业保障项目</th>
                                        <th class="business_th4">保额</th>
                                        <th class="business_th5">不计免赔</th>
                                        <th>保费</th>
                                    </tr>
                                    <#list itemDTOList2 as itemDTOLi2>
                                        <tr>
                                            <td>${itemDTOLi2.insuranceName}</td>
                                            <td><#if itemDTOLi2.insuranceItemCoverageValue != 0>
                                                <!-- 对玻璃特殊处理-->
                                                <#if itemDTOLi2.insuranceCategoryCode== '033006'>
                                                    <#if itemDTOLi2.insuranceItemCoverageValue == 1>
                                                        进口玻璃
                                                    <#elseif itemDTOLi2.insuranceItemCoverageValue == 2>
                                                        国产玻璃
                                                    <#elseif itemDTOLi2.insuranceItemCoverageValue == 3>
                                                        进口特种
                                                    <#elseif itemDTOLi2.insuranceItemCoverageValue == 4>
                                                        国产特种
                                                    </#if>
                                                <#else>
                                                ${itemDTOLi2.insuranceItemCoverageValue}
                                                </#if>
                                            <#else>投保</#if></td>
                                            <#if itemDTOLi2.isDeductible == 0 >
                                                <td>是</td>
                                            <#elseif itemDTOLi2.isDeductible == 1>
                                                <td>否</td>
                                            <#else >
                                                <td></td>
                                            </#if>
                                            <td>未脱保</td><!-- 写死，数据库保存的是1（脱保状态，以后可能有坑）-->
                                        </tr>
                                    </#list>
                                    <tr>
                                        <td>商业险合计</td>
                                        <td></td>
                                        <td></td>
                                        <td>未脱保</td>
                                    </tr>
                                </table>
                                <div class="time">
                                    <span class="startW">商业险生效时间</span>
                                    <div class="businessStartT">${VirtualForm.packageStartTime?string("yyyy-MM-dd HH:mm:ss")}</div>
                                    <span class="endW">商业险失效时间</span>
                                    <div class="businessEndT">${VirtualForm.packageEndTime?string("yyyy-MM-dd HH:mm:ss")}</div>
                                </div>
                            </div>
                            </#if>
                        </#if>
                    </#list>
                </#if>


                    <!--车主信息-->
                    <p class="detail_w">车主信息</p>
                    <div class="car_detail">
                        <div class="dis-inB">
                            <span class="span_w">姓名:</span>
                            <div class="car carN">${virtualBasic.vehicleOwnerName}</div>
                        </div>
                        <div class="dis-inB">
                            <span class="span_w">手机号:</span>
                            <div class="brand carPhone">${virtualBasic.vehicleOwnerPhone}</div>
                        </div>
                        <div class="dis-inB">
                            <span class="span_w">证件类型:</span>
                            <div class="car carPapers" data-CertType="${virtualBasic.vehicleOwnerCertType}">${virtualBasic.vehicleOwnerCertTypeName}</div>
                        </div>
                        <div class="dis-inB">
                            <span class="span_w">证件号码:</span>
                            <div class="brand papersNum">${virtualBasic.vehicleOwnerCertCode}</div>
                        </div>
                    </div>
                </div>
                <!--被保人信息-->
                <div>
                    <p class="detail_w Insured">被保人信息
                    <#if virtualBasic.insuredProvince =="北京市">
                        <span class="same_carD dis"><input type="checkbox" class="sameDri sameDC dis">与车主一致</span>
                    <#else>
                        <span class="same_carD"><input type="checkbox" class="sameDri sameDC" checked>与车主一致</span>
                    </#if>
                    </p>
                    <div class="car_detail Insure_d info">
                        <div class="all_inf1 <#if virtualBasic.insuredProvince != "北京市">dis</#if>" id="insureInfo">
                            <div>
                                <span class="all_inf_w label-must">姓名</span>
                            <#if virtualBasic.insuredProvince =="北京市">
                                <input type="text" class="all_input recognizeeName" value="${virtualBasic.insuredName}" readonly>
                            <#else>
                                <input type="text" class="all_input recognizeeName">
                            </#if>
                                <span class="all_inf_w label-must">手机号</span>
                                <input type="text" class="all_input recognizeePhone" >
                            </div>
                            <div class="margin">
                                <span class="all_inf_w label-must">证件类型</span>
                                <div class="all_inf_w_btn">
                                    <div class="zj_l recognizeeType" data-code="120001">居民身份证</div><div class="zj_btn s_up"></div>
                                    <ul class="dis"></ul>
                                </div>
                                <span class="all_inf_w label-must">证件号码</span>
                                <input type="text" class="all_input recognizeeNum">
                            </div>
                        </div>
                    <#--点击与车主一致时出现的信息-->
                        <div class="all_inf2 <#if virtualBasic.insuredProvince == "北京市">dis</#if>">
                            <div>
                                <span class="all_inf_w">姓名:</span>
                                <div class="dis-inS same_DN recognizeeName">${virtualBasic.vehicleOwnerName}</div>
                                <span class="all_inf_w">手机号:</span>
                                <div class="dis-inS sameDPhone recognizeePhone">${virtualBasic.vehicleOwnerPhone}</div>
                            </div>
                            <div class="margin">
                                <span class="all_inf_w">证件类型:</span>
                                <div class="dis-inS samePapers recognizeeType" data-code="${virtualBasic.vehicleOwnerCertType}">${virtualBasic.vehicleOwnerCertTypeName}</div>
                                <span class="all_inf_w">证件号码:</span>
                                <div class="dis-inS samePapersNum recognizeeNum">${virtualBasic.vehicleOwnerCertCode}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--投保人信息-->
                <div>
                <p class="detail_w Insured">投保人信息
                <#if virtualBasic.insuredProvince =="北京市">
                    <span class="same"><input type="checkbox" class="sameDri sameInsure" >与被保人一致</span></p>
                <#else >
                    <span class="same"><input type="checkbox" class="sameDri sameInsure" checked>与被保人一致</span></p>
                </#if>
                    <div class="car_detail info">
                        <div class="all_inf1 <#if virtualBasic.insuredProvince != "北京市">dis</#if>">
                            <div>
                                <span class="all_inf_w label-must">姓名</span>
                                <input type="text" class="all_input applicantName">
                                <span class="all_inf_w label-must">手机号</span>
                                <input type="text" class="all_input applicantPhone" >
                            </div>
                            <div class="margin">
                                <span class="all_inf_w label-must">证件类型</span>
                                <div class="all_inf_w_btn">
                                    <div class="zj_l applicantType" data-code="120001">居民身份证</div><div class="zj_btn s_up"></div>
                                    <ul class="dis"></ul>
                                </div>
                                <span class="all_inf_w label-must">证件号码</span>
                                <input type="text" class="all_input applicantNum">
                            </div>
                            <div class="margin">
                                <span class="all_inf_w label-must dis">手机验证码</span>
                                <div class="VCod dis">
                                    <input class="VCode_num" type="hidden" value="1234"><div class="sendCode">发送验证码</div>
                                </div>
                            </div>
                        </div>
                    <#--点击与被保人一致-->
                        <div class="all_inf2 <#if virtualBasic.insuredProvince == "北京市">dis</#if>">
                            <div>
                                <span class="all_inf_w">姓名:</span>
                                <div class="dis-inS same_DN applicantName">${virtualBasic.vehicleOwnerName}</div>
                                <span class="all_inf_w">手机号:</span>
                                <div class="dis-inS sameDPhone applicantPhone">${virtualBasic.vehicleOwnerPhone}</div>
                            </div>
                            <div class="margin">
                                <span class="all_inf_w">证件类型:</span>
                                <div class="dis-inS samePapers applicantType"  data-code="${virtualBasic.vehicleOwnerCertType}">${virtualBasic.vehicleOwnerCertTypeName}</div>
                                <span class="all_inf_w">证件号码:</span>
                                <div class="dis-inS samePapersNum applicantNum">${virtualBasic.vehicleOwnerCertCode}</div>
                            </div>
                            <div class="margin">
                                <span class="all_inf_w label-must dis">手机验证码</span>
                                <div class="VCode dis">
                                    <input class="VCode_num" type="hidden" value="1234"><button class="sendCode">发送验证码</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--配送信息-->
                <div>
                <p class="detail_w Insured">配送信息
                <#if virtualBasic.insuredProvince =="北京市">
                    <span class="same"><input type="checkbox"  class="sameDri insureC2">与被保人一致</span></p>
                <#else >
                    <span class="same"><input type="checkbox"  class="sameDri insureC2" checked>与被保人一致</span></p>
                </#if>
                    <div class="car_detail info">
                        <div class="all_inf1 <#if virtualBasic.insuredProvince != "北京市">dis</#if>">
                            <div>
                                <span class="all_inf_w label-must">收件人姓名</span>
                                <input type="text" class="all_input receiverName">
                                <span class="all_inf_w label-must">手机号</span>
                                <input type="text" class="all_input receiverPhone" >
                            </div>
                            <div class="margin">
                                <span class="all_inf_w">交强险保单类型</span>
                            <#if virtualBasic.insuredProvince == "北京市">
                                <div class="dis-inS forceWarrantyType js-traverse-type" data-code="1">电子保单</div>
                            <#else>
                                <div class="dis-inS forceWarrantyType js-traverse-type" data-code="0">纸质保单</div>
                            </#if>
                            </div>
                            <div class="margin">
                                <span class="all_inf_w label-must">商业险保单类型:</span>
                                <div class="warranty">
                                <#if virtualBasic.insuredProvince == "北京市">
                                    <div class="warrantyType js-traverse-type" data-code="1">电子保单</div>
                                <#else>
                                    <div class="warrantyType zj_l warrantyT js-traverse-type" data-code="0">纸质保单</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class="warrantyTypeUl dis js-traverseU">
                                        <li data-typecode="0">纸质保单</li>
                                        <li data-typecode="1">电子保单</li>
                                    </ul>
                                </#if>
                                </div>
                            </div>
                            <div class="margin">
                                <span class="all_inf_w label-must">发票类型:</span>
                                <div class="warranty">
                                    <div class="warrantyType zj_l warrantyT js-traverse-type receipt-type" data-code="0">纸质发票</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class=" warrantyTypeUl dis js-traverseUl">
                                        <li data-typecode="0">纸质发票</li>
                                        <li data-typecode="1">电子发票</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="margin js-post-address dis">
                                <span class="all_inf_w label-must">收件人地址</span>
                                <div class="prevK">
                                    <div class="prev receiverProvince">请选择省</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class="prevUl dis"></ul>
                                </div>
                                <div class="cityK">
                                    <div class="city receiverCity">请选择市</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class="cityUl dis"></ul>
                                </div>
                                <div class="areaK">
                                    <div class="area receiverArea">请选择区</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class="areaUl dis"></ul>
                                </div>
                            </div>
                            <div class="margin js-post-address dis">
                                <span class="all_inf_w label-must"></span>
                                <input class="detail_addr receiverAddr" placeholder="请输入具体地址">
                            </div>
                            <div class="margin js-mail dis">
                                <span class="all_inf_w label-must">电子邮箱</span>
                                <input class="email-receive" placeholder="请输入有效的电子邮箱地址，用于发送电子保单">
                            </div>
                        </div>
                    <#--点击与被保人一致-->
                        <div class="all_inf2 <#if virtualBasic.insuredProvince == "北京市">dis</#if>">
                            <div>
                                <span class="all_inf_w">收件人姓名:</span>
                                <div class="dis-inS same_DN receiverName">${virtualBasic.vehicleOwnerName}</div>
                                <span class="all_inf_w">手机号:</span>
                                <div class="dis-inS sameDPhone receiverPhone">${virtualBasic.vehicleOwnerPhone}</div>
                            </div>
                            <div class="margin">
                                <span class="all_inf_w">交强险保单类型:</span>
                            <#if virtualBasic.insuredProvince == "北京市">
                                <div class="dis-inS forceWarrantyType js-traverse-type" data-code="1">电子保单</div>
                            <#else>
                                <div class="dis-inS forceWarrantyType js-traverse-type" data-code="0">纸质保单</div>
                            </#if>
                            </div>
                            <div class="margin">
                                <span class="all_inf_w label-must">商业险保单类型:</span>
                                <div class="warranty">
                                <#if virtualBasic.insuredProvince == "北京市">
                                    <div class="warrantyType js-traverse-type" data-code="1">电子保单</div>
                                <#else>
                                    <div class="warrantyType zj_l warrantyT js-traverse-type" data-code="0">纸质保单</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class="warrantyTypeUl dis  js-traverseUl">
                                        <li data-typecode="0">纸质保单</li>
                                        <li data-typecode="1">电子保单</li>
                                    </ul>
                                </#if>
                                </div>
                            </div>
                            <div class="margin">
                                <span class="all_inf_w label-must">发票类型:</span>
                                <div class="warranty">
                                    <div class="warrantyType zj_l warrantyT js-traverse-type receipt-type" data-code="0">纸质发票</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class=" warrantyTypeUl dis js-traverseUl">
                                        <li data-typecode="0">纸质发票</li>
                                        <li data-typecode="1">电子发票</li>
                                    </ul>
                                </div>
                            </div>
                            <div class="margin js-post-address dis">
                                <span class="all_inf_w label-must">收件人地址</span>
                                <div class="prevK">
                                    <div class="prev receiverProvince">请选择省</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class="prevUl dis"></ul>
                                </div>
                                <div class="cityK">
                                    <div class="city receiverCity">请选择市</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class="cityUl dis"></ul>
                                </div>
                                <div class="areaK">
                                    <div class="area receiverArea">请选择区</div><div class="zj_btn s_up choose_btn"></div>
                                    <ul class="areaUl dis"></ul>
                                </div>
                            </div>
                            <div class="margin js-post-address dis">
                                <span class="all_inf_w label-must"></span>
                                <input class="detail_addr receiverAddr" placeholder="请输入具体地址">
                            </div>
                            <div class="margin js-mail dis">
                                <span class="all_inf_w label-must">电子邮箱</span>
                                <input class="email-receive" placeholder="请输入有效的电子邮箱地址，用于发送电子保单">
                            </div>
                        </div>
                    </div>
                </div>
            <#--上份商业险信息-->
                <p class="detail_w Insured">上份商业险信息 <span class="kind-interp">友好提示：以下信息会影响服务包的可选种类，请认真填写</span></p>
                <div class="frontInfo car_detail info">
                    <div>
                        <span class="all_inf_w label-must">商业险保费金额</span>
                        <input type="text" class="all_input frontMoney" >
                    </div>
                </div>
                <!--重要提示-->
                <p class="detail_w Insured">重要提示</p>
                <div class="imp_tips">
                    <div class="imp_tips_in">
                        <label class="agree-protocol-label"><input class="js-agree-protocol" type="checkbox">我已阅读并同意以下声明</label>
                        <div class="protocol-box">
                            <a class="js-protocol-link" href="#accidentsCompulsoryInsurance">&lt;&lt;交强险条款&gt;&gt;</a>
                            、<a class="js-protocol-link" href="#commercialInsurance">&lt;&lt;商业险条款&gt;&gt;</a>
                            <!-- 、<a class="js-protocol-link" href="#">&lt;&lt;浮动告知&gt;&gt;</a>-->
                            、<a class="js-protocol-link" href="#liabilityBook">&lt;&lt;机动车综合商业保险免责说明书&gt;&gt;</a>
                        <#if SESSION_ANXIN_INSURANCE_MODEL == 2 || SESSION_ANXIN_INSURANCE_MODEL == 0>
                            、<a class="js-third-party-link" href="#servicePackageThirdPartyProtocol">&lt;&lt;服务包协议&gt;&gt;</a>
                        </#if>
                        </div>
                    </div>
                </div>
                <div class="btn clearfix">
                    <div class="submit">购买服务包</div>
                    <div class="back">返回修改</div>
                </div>
            </div>
        </div>
    </div>
</div>

<#-- 协议公共弹窗 -->
<#include "yqx/tpl/ax_insurance/protocol-common-dialog-tpl.ftl">
<#-- 交强险条款 -->
<#include "yqx/tpl/ax_insurance/accidents-compulsory-insurance-tpl.ftl">
<#-- 商业险条款 -->
<#include "yqx/tpl/ax_insurance/commercial-insurance-tpl.ftl">
<#-- 浮动告知 -->
<#-- TODO 浮动告知 -->
<#-- 机动车综合商业保险免责说明书 -->
<#include "yqx/tpl/ax_insurance/liability-book-tpl.ftl">
<#-- 服务包三方协议 -->
<#if SESSION_ANXIN_INSURANCE_MODEL == 2 || SESSION_ANXIN_INSURANCE_MODEL == 0>
    <#include "yqx/tpl/ax_insurance/service-package-third-party-protocol-tpl.ftl">
</#if>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/virtual/virtual-plan.js?30aa5437d5080e059bf741b059926c6c"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>
</body>
<#include "yqx/layout/footer.ftl">