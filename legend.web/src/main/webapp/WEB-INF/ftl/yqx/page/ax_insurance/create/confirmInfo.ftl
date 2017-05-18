<#--买保险送奖励金模式-->
<#include "yqx/layout/header.ftl">
<link href="${BASE_PATH}/static/css/page/ax_insurance/create/confirmInfo.css?312e3b72f3e818685f06c68b84967b7e" type="text/css" rel="stylesheet">
<input type="hidden" class="modeV" name="modeV" value="${SESSION_ANXIN_INSURANCE_MODEL}">
<div class="wrapper clearfix">
    <div class="content clearfix">
    <#include "yqx/page/ax_insurance/left-nav.ftl">
        <div class="right">
        <#include "yqx/page/ax_insurance/process-nav.ftl">
        <#--确认投保信息-->
            <div class="confirmInfo">
                <p class="choose">确认投保信息</p>
                <div class="all_choose">
                    <div class="allData"></div>
                <#--管理常用地址-->
                <#include "yqx/page/ax_insurance/create/manage-address.ftl">
                <#--服务包-->
                <#if SESSION_ANXIN_INSURANCE_MODEL == 2 || SESSION_ANXIN_INSURANCE_MODEL == 0>
                    <p class="detail_w servicePackN">服务包 <span class="selectServicePack re_selected dis">重新选择</span></p>
                    <div class="servicePack">
                        <div class="SelectedPack clearfix"></div>
                        <div class="backG">
                            <span class="selectServicePack selected dis">选择服务包</span>
                            <p class="sorry dis"><i class="sad-expr"></i><span class="sorry-tip"><em class="sorry-w sorry-Pa">抱歉</em> 由于当前商业保险费<em class="sorry-w">低于1400元</em>，无法匹配到服务包，您可选择返回调整商业险或当前页面直接提交订单</span></p>
                        </div>
                    </div>
                </#if>

                    <!--优惠券选择 start-->
                    <p class="coupon-choose-title">请选择优惠券</p>
                    <ul class="coupon-list">

                    </ul>
                    <!--优惠券选择 end-->

                    <!--重要提示-->
                    <input type="hidden" value="" class="colour-type">
                    <p class="detail_w Insured">重要提示</p>
                    <div class="imp_tips">
                        <div class="imp_tips_in">
                            <label class="agree-protocol-label"><input class="js-agree-protocol" type="checkbox">我已阅读并同意以下声明</label>
                            <div class="protocol-box">
                                <a class="js-protocol-link" href="#accidentsCompulsoryInsurance">&lt;&lt;交强险条款&gt;&gt;</a>
                                、<a class="js-protocol-link" href="#commercialInsurance">&lt;&lt;商业险条款&gt;&gt;</a>
                                <!-- 、<a class="js-protocol-link" href="#">&lt;&lt;浮动告知&gt;&gt;</a> -->
                                、<a class="js-protocol-link" href="#liabilityBook">&lt;&lt;机动车综合商业保险免责说明书&gt;&gt;</a>
                                、<a class="js-third-party-link" href="#servicePackageThirdPartyProtocol">&lt;&lt;服务包协议&gt;&gt;</a>
                            </div>
                        </div>
                    </div>
                    <div class="total_P premium"></div>
                    <div class="btn clearfix">
                        <div class="submit">提交订单</div>
                        <div class="back three-back">返回修改</div>
                    </div>
                </div>
            </div>
            <div class="sendPack-all dis">
                <p class="choose">买保险送服务包 <span class="back-insure">返回投保 ></span></p>
                <div class="all-pack">
                    <h2 class="pack-title">请选择车主喜欢的服务包</h2>
                    <!--服务包list start-->
                    <div class="pack-list clearfix"></div>
                    <!--服务包list end-->
                </div>
            </div>
        </div>

    </div>

</div>
<script type="text/html" id="couponTpl">
    <%if(success && data){%>
    <%if(data.length>0){%>
    <%for(var i = 0;i < data.length;i++){%>
    <%var dataList = data[i];%>
    <li class="coupon-list-li <%if(!dataList.valid){%>disabled<%}%>" data-id="<%=dataList.id%>"
        data-state="<%=dataList.couponStatus%>" data-freezeTime="<%=dataList.gmtCouponFrozen%>"
        data-couponUseMode = '<%=dataList.couponUseMode%>' data-deductibleAmount = <%=dataList.deductibleAmount%>>
        <div class="coupon-list-box">
            <p class="clearfix">
                <span class="coupon-title <%if(!dataList.valid){%>dis-coupon<%}%>">满<%=dataList.satisfyUse%>减<%=dataList.deductibleAmount%></span>
                <span class="question-mark">?</span>
            </p>
            <p class="coupon-time">
                <%=toTime(dataList.validateTime,1)%>-<%=toTime(dataList.gmtCouponExpired,1)%>
            </p>
            <p class="coupon-describe">
                <%=dataList.applicationScope%>
            </p>
        </div>
        <div class="coupon-use-describe">
            <%==dataList.couponRuleDescription%>
        </div>
        <%if(!dataList.valid){%>
        <div class="disable-coupon">
            <img src="${BASE_PATH}/static/img/common/seals/disable.png">
        </div>
        <%}%>
    </li>
    <%}%>
    <%}else{%>
    <p class="no-coupon"><i class="sad-expr"></i><span class="no-coupon-word">车主没有可用的优惠劵</span></p>
    <%}%>
    <%}%>
</script>
<script type="text/html" id="allDataTpl">
    <%if(success&&data){%>
    <%if(data.insuredProvince == "北京市"){%>
    <input type="hidden" id="province">
    <%}%>
    <p class="detail_w">车辆详细信息</p>
    <div class="car_detail">
        <div class="dis-inB">
            <span class="span_w">车牌号码:</span>
            <%if(data.vehicleSn){%>
            <div class="car"><%=data.vehicleSn%></div>
            <%}else{%>
            <div class="car">尚未取得车牌</div>
            <%}%>
        </div>
        <div class="dis-inB">
            <span class="span_w">投保地:</span>
            <div class="brand addr"><%=data.insuredProvince%>-<%=data.insuredCity%></div>
        </div>
        <div class="dis-inB">
            <span class="span_w">发动机号:</span>
            <div class="car_price"><%=data.carEngineSn%></div>
        </div>
        <div class="dis-inB">
            <span class="span_w">车架号:</span>
            <div class="car_price"><%=data.carFrameSn%></div>
        </div>
        <div class="dis-inB dis-car">
            <span class="span_w">品牌型号:</span>
            <div class="car_price"><%=data.carConfigType%></div>
        </div>
    </div>
    <p class="choose_w">保障内容</p>
    <div class="scheme">
        <%if(data.insuranceFormDTOList && data.insuranceFormDTOList.length){%>
        <% var DTOList = data.insuranceFormDTOList%>
        <%for(var j = 0;j < DTOList.length;j++){%>
        <%if(DTOList[j].insuranceType == 1){%>
        <input type="hidden" id="jqUploadImg" value="<%=DTOList[j].hasInsuranced%>">
        <%if(DTOList[j].itemDTOList && DTOList[j].itemDTOList.length ){%>
        <% var itemDTOList = DTOList[j].itemDTOList[j]%>
        <table data-formId="<%=DTOList[j].id%>" class="table1" data-outerInsuranceApplyNo="<%=DTOList[j].outerInsuranceApplyNo%>">
            <tr>
                <th>交强险保障项目</th>
                <th>保额</th>
                <th>保费</th>
            </tr>
            <tr>
                <td><%= itemDTOList.insuranceName%></td>
                <td class="touB">&yen;<%= itemDTOList.insuranceAmount%></td>
                <td>&yen;<span><%= toFixed(itemDTOList.insuranceFee)%></span></td>
            </tr>
            <tr>
                <td>车船税</td>
                <td></td>
                <td>&yen;<span><%= toFixed(DTOList[j].insuredTax)%></span></td>
            </tr>
            <tr>
                <td>交强险合计</td>
                <td></td>
                <td>&yen;<span><%= toFixed(DTOList[j].insuredTax+itemDTOList.insuranceFee)%></span></td>
            </tr>
            <tr>
                <td>折扣率</td>
                <td></td>
                <td><span><%= toFixed(DTOList[j].discountRatio)%></span></td>
            </tr>
        </table>
        <div class="time">
            <span class="startW">交强险生效时间</span>
            <span class="forceStartT"><%= toTime(DTOList[j].packageStartTime)%></span>
            <span class="endW">交强险失效时间</span>
            <span class="forceEndT"><%= toTime(DTOList[j].packageEndTime)%></span>
        </div>
        <%}%>
        <%}else if(DTOList[j].insuranceType == 2){%>
        <input type="hidden" id="syUploadImg" value="<%=DTOList[j].hasInsuranced%>">
        <%if(DTOList[j].itemDTOList && DTOList[j].itemDTOList.length){%>
        <% var itemDTOList2 = DTOList[j].itemDTOList%>
        <table data-formId="<%=DTOList[j].id%>" class="table2" data-outerInsuranceApplyNo="<%=DTOList[j].outerInsuranceApplyNo%>">
            <tr>
                <th>商业保障项目</th>
                <th class="business_th4">保额</th>
                <th class="business_th5">不计免赔</th>
                <th>保费</th>
                <th>合计</th>
            </tr>
            <% for(var i=0;i < itemDTOList2.length;i++){%>
            <tr>
                <td><%=itemDTOList2[i].insuranceName%></td>
                <td>
                    <%if(itemDTOList2[i].insuranceAmount>0){%>
                    &yen;<span><%=itemDTOList2[i].insuranceAmount%></span>
                    <%}%>
                </td>
                <%if(itemDTOList2[i].isDeductible==0){%>
                <td>&yen;<span><%=itemDTOList2[i].deductibleAmount%></span></td>
                <%}else if(itemDTOList2[i].isDeductible==1){%>
                <td></td>
                <%}else{%>
                <td></td>
                <%}%>
                <td>&yen;<span><%= toFixed(itemDTOList2[i].insuranceFee)%></span></td>
                <td>&yen;<span><%= toFixed(itemDTOList2[i].insuranceFee+itemDTOList2[i].deductibleAmount)%></span></td>
            </tr>
            <%}%>
            <tr>
                <td>商业险合计</td>
                <td></td>
                <td></td>
                <td></td>
                <td>&yen;<span><%=DTOList[j].insuredFee%></span></td>
            </tr>

            <tr>
                <td>折扣率:<%= toFixed(DTOList[j].discountRatio)%></td>
                <td>NCD系数:<%= DTOList[j].noClaimData%></td>
                <td>交通违法系数:<%= toFixed(DTOList[j].trafficIllegalCoefficient)%></td>
                <td>自主核保系数:<%= toFixed(DTOList[j].autonomyCheckCoefficient)%></td>
                <td>自主渠道系数:<%= toFixed(DTOList[j].autonomyChannelCoefficient)%></td>
            </tr>
        </table>
        <div class="time">
            <span class="startW">商业险生效时间</span>
            <div class="businessStartT"><%= toTime(DTOList[j].packageStartTime)%></div>
            <span class="endW">商业险失效时间</span>
            <div class="businessEndT"><%= toTime(DTOList[j].packageEndTime)%></div>
        </div>
        <%}%>
        <%}%>
        <%}%>
        <%}%>
    </div>
    <!--车主信息-->
    <p class="detail_w">车主信息</p>
    <div class="car_detail">
        <div class="dis-inB">
            <span class="span_w">姓名:</span>
            <div class="car carN"><%=data.vehicleOwnerName%></div>
        </div>
        <div class="dis-inB">
            <span class="span_w">手机号:</span>
            <div class="brand carPhone"><%=data.vehicleOwnerPhone%></div>
        </div>
        <div class="dis-inB">
            <span class="span_w">证件类型:</span>
            <div class="car carPapers" data-CertType="<%=data.vehicleOwnerCertType%>"><%=data.vehicleOwnerCertName%></div>
        </div>
        <div class="dis-inB">
            <span class="span_w">证件号码:</span>
            <div class="brand papersNum"><%=data.vehicleOwnerCertCode%></div>
        </div>
    </div>
    <!--被保人信息-->
    <div>
        <p class="detail_w Insured">被保人信息
            <%if(data.insuredProvince == "北京市"){%>
            <span class="same_carD dis"><input type="checkbox" class="sameDri sameDC">与车主一致</span>
            <%}else{%>
            <span class="same_carD"><input type="checkbox" class="sameDri sameDC" checked>与车主一致</span>
            <%}%>
        </p>
        <div class="car_detail Insure_d info">
            <div class="all_inf1 <%if(data.insuredProvince != '北京市'){%>dis<%}%>" id="insureInfo">
                <div>
                    <span class="all_inf_w label-must">姓名</span>
                    <input type="text" class="all_input recognizeeName" dynamic_name="insuredName" value="<%if(data.insuredProvince == "北京市"){%><%=data.insuredName%><%}%>" <%if(data.insuredProvince == "北京市"){%>readonly<%}%>>
                    <span class="all_inf_w label-must">手机号</span>
                    <input type="text" class="all_input recognizeePhone Mobile" dynamic_name="insuredMobile">
                </div>
                <div class="margin">
                    <span class="all_inf_w label-must">证件类型</span>
                    <div class="all_inf_w_btn">
                        <div class="zj_l recognizeeType paperType" data-code="120001">居民身份证</div><div class="zj_btn s_up"></div>
                        <ul class="dis"></ul>
                    </div>
                    <span class="all_inf_w label-must">证件号码</span>
                    <input type="text" class="all_input recognizeeNum papersCode" dynamic_name="insuredCertfCde">
                </div>
            </div>
        <#--点击与车主一致时出现的信息-->
            <div class="all_inf2  <%if(data.insuredProvince == '北京市'){%>dis<%}%>">
                <div>
                    <span class="all_inf_w">姓名:</span>
                    <div class="dis-inS same_DN recognizeeName"><%=data.vehicleOwnerName%></div>
                    <span class="all_inf_w">手机号:</span>
                    <div class="dis-inS sameDPhone recognizeePhone"><%=data.vehicleOwnerPhone%></div>
                </div>
                <div class="margin">
                    <span class="all_inf_w">证件类型:</span>
                    <div class="dis-inS samePapers recognizeeType" data-code="<%=data.vehicleOwnerCertType%>"><%=data.vehicleOwnerCertName%></div>
                    <span class="all_inf_w">证件号码:</span>
                    <div class="dis-inS samePapersNum recognizeeNum"><%=data.vehicleOwnerCertCode%></div>
                </div>
            </div>
        </div>
    </div>
    <!--投保人信息-->
    <div>
        <p class="detail_w Insured">投保人信息
            <%if(data.insuredProvince == "北京市"){%>
            <span class="same"><input type="checkbox" class="sameDri sameInsure">与被保人一致</span></p>
        <%}else{%>
        <span class="same"><input type="checkbox" class="sameDri sameInsure" checked>与被保人一致</span></p>
        <%}%>
        <div class="car_detail info">
            <div class="all_inf1 <%if(data.insuredProvince != '北京市'){%>dis<%}%>">
                <div>
                    <span class="all_inf_w label-must">姓名</span>
                    <input type="text" class="all_input applicantName" dynamic_name="applicantName">
                    <span class="all_inf_w label-must">手机号</span>
                    <input type="text" class="all_input applicantPhone Mobile" dynamic_name="applicantMobile">
                </div>
                <div class="margin">
                    <span class="all_inf_w label-must">证件类型</span>
                    <div class="all_inf_w_btn">
                        <div class="zj_l applicantType paperType" data-code="120001">居民身份证</div><div class="zj_btn s_up"></div>
                        <ul class="dis"></ul>
                    </div>
                    <span class="all_inf_w label-must">证件号码</span>
                    <input type="text" class="all_input applicantNum papersCode" dynamic_name="applicantCertfCde">
                </div>
                <%if(data.insuredProvince == "北京市"){%>
                <div class="margin">
                    <span class="all_inf_w label-must">手机验证码</span>
                    <div class="VCode">
                        <input class="VCode_num" type="text"><div class="sendCode">发送验证码</div>
                    </div>
                </div>
                <%}%>
            </div>
        <#--点击与被保人一致-->
            <div class="all_inf2 <%if(data.insuredProvince == '北京市'){%> dis <%}%> ">
                <div>
                    <span class="all_inf_w">姓名:</span>
                    <div class="dis-inS same_DN applicantName" dynamic_name="applicantName"><%=data.vehicleOwnerName%></div>
                    <span class="all_inf_w">手机号:</span>
                    <div class="dis-inS sameDPhone applicantPhone" dynamic_name="applicantMobile"><%=data.vehicleOwnerPhone%></div>
                </div>
                <div class="margin">
                    <span class="all_inf_w">证件类型:</span>
                    <div class="dis-inS samePapers applicantType" data-code="<%=data.vehicleOwnerCertType%>"><%=data.vehicleOwnerCertName%></div>
                    <span class="all_inf_w">证件号码:</span>
                    <div class="dis-inS samePapersNum applicantNum" dynamic_name="applicantCertfCde"><%=data.vehicleOwnerCertCode%></div>
                </div>
                <%if(data.insuredProvince == "北京市"){%>
                <div class="margin">
                    <span class="all_inf_w label-must">手机验证码</span>
                    <div class="VCode">
                        <input class="VCode_num" type="text"><div class="sendCode">发送验证码</div>
                    </div>
                </div>
                <%}%>
            </div>
        </div>
    </div>
    <!--配送信息-->
    <div>
        <p class="detail_w Insured">配送信息
            <span class="manage-address">管理常用地址</span>
            <%if(data.insuredProvince == "北京市"){%>
            <span class="same"><input type="checkbox"  class="sameDri insureC2">与被保人一致</span></p>
        <%}else{%>
        <span class="same"><input type="checkbox"  class="sameDri insureC2">与被保人一致</span></p>
        <%}%>
        <div class="car_detail info" id="delivery-inf">
            <div class="all_inf1">
                <div>
                    <span class="all_inf_w label-must">收件人姓名:</span>
                    <input type="text" class="all_input receiverName">
                    <span class="all_inf_w label-must">手机号:</span>
                    <input type="text" class="all_input receiverPhone" >
                </div>
                <div class="margin">
                    <span class="all_inf_w">交强险保单类型:</span>
                    <%if(data.insuredProvince == '北京市'){%>
                    <div class="dis-inS forceWarrantyType js-traverse-type" data-code="1">电子保单</div>
                    <%}else{%>
                    <div class="dis-inS forceWarrantyType js-traverse-type" data-code="0">纸质保单</div>
                    <%}%>
                </div>
                <div class="margin">
                    <span class="all_inf_w label-must">商业险保单类型:</span>
                    <div class="warranty">
                        <%if(data.insuredProvince == '北京市'){%>
                        <div class="warrantyType js-traverse-type" data-code="1">电子保单</div>
                        <%}else{%>
                        <div class="warrantyType zj_l warrantyT js-traverse-type" data-code="0">纸质保单</div><div class="zj_btn s_up choose_btn"></div>
                        <ul class="warrantyTypeUl dis js-traverseUl">
                            <li data-typecode="0">纸质保单</li>
                            <li data-typecode="1">电子保单</li>
                        </ul>
                        <%}%>
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
            <div class="all_inf2 dis ">
                <div>
                    <span class="all_inf_w">收件人姓名:</span>
                    <div class="dis-inS same_DN receiverName"><%=data.vehicleOwnerName%></div>
                    <span class="all_inf_w">手机号:</span>
                    <div class="dis-inS sameDPhone receiverPhone"><%=data.vehicleOwnerPhone%></div>
                </div>
                <div class="margin">
                    <span class="all_inf_w">交强险保单类型:</span>
                    <%if(data.insuredProvince == '北京市'){%>
                    <div class="dis-inS forceWarrantyType js-traverse-type" data-code="1">电子保单</div>
                    <%}else{%>
                    <div class="dis-inS forceWarrantyType js-traverse-type" data-code="0">纸质保单</div>
                    <%}%>
                </div>
                <div class="margin">
                    <span class="all_inf_w label-must">商业险保单类型:</span>
                    <div class="warranty">
                        <%if(data.insuredProvince == '北京市'){%>
                        <div class="warrantyType js-traverse-type" data-code="1">电子保单</div>
                        <%}else{%>
                        <div class="warrantyType zj_l warrantyT js-traverse-type" data-code="0">纸质保单</div><div class="zj_btn s_up choose_btn"></div>
                        <ul class=" warrantyTypeUl dis js-traverseUl">
                            <li data-typecode="0">纸质保单</li>
                            <li data-typecode="1">电子保单</li>
                        </ul>
                        <%}%>
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
    <%}%>
</script>
<#--保费-->
<script id="premiumTpl" type="text/html">
    <%if(success&&data){%>
    <p class="incomeTotal">交强险保费: <i class="jq-insurance">0.00</i></p>
    <p class="incomeTotal">商业险保费: <i class="sy-insurance">0.00</i></p>
    <p class="incomeTotal">保费合计: <i class="total_Price"><%= data.insuredTotalFee%></i></p>
    <p class="incomeTotal">优惠: <i class="coupon-amount">0.00</i><span class="withStore">（优惠金额由门店与车主线下结算)</span></p>
    <span class="triangle dis"></span>
    <span class="incentive dis">奖励金:<em class="incentive_price"><%= data.rewardTotalFee%></em></span>
    <%}%>
</script>
<#--待选择的服务包模版-->
<script id="servicePackTpl" type="text/html">
    <%if(success&&data){%>
    <h2 class="pack-tips">友情提示：<%=errorMsg%></h2>
    <%for(var i = 0;i< data.length;i++){%>
    <div>
        <input type="checkbox" class="pack-check fl">
        <div class="pack-content fl" data-Json="<%= JsonStringify(data[i])%>">
            <div class="consume-title clearfix">
                <h3 class="fl"><%if(data[i].recommend){%><span class="recommend">推荐</span><%}%>
                    <span class="pack-name font-yahei"><%=data[i].packageName%></span>
                    <span class="summary"><%=data[i].description%></span></h3>
                <p class="fr">市场售价：<span class="money mark"><%= toFixed(data[i].marketPrice)%></span></p>
            </div>
            <table>
                <thead>
                <tr>
                    <th class="col1">项目名称</th>
                    <th class="col2">型号</th>
                    <th class="col3">单位</th>
                    <th class="col4">市场价</th>
                    <th class="col5">次数</th>
                </tr>
                </thead>
                <tbody>
                <%if(data[i].serviceItems && data[i].serviceItems.length){%>
                <%var serviceItem = data[i].serviceItems%>
                <%for(var j = 0;j < serviceItem.length;j++){%>
                <% var serviceItemMaterialBOList = serviceItem[j].serviceItemMaterialBOList; %>
                <tr>
                    <%if(serviceItem[j].id == '49'){%>
                    <input type="hidden" value="49" class="color-type"/>
                    <%}%>
                    <td><%=serviceItem[j].serviceName%></td>
                    <td class="max-text js-show-tips ellipsis-1"><%if(serviceItem[j].serviceMaterialModel){%><%=serviceItem[j].serviceMaterialModel%><%}else{%>/<%}%></td>
                    <td><%=serviceItem[j].serviceItemUnit%></td>
                    <td>&yen;<%=serviceItem[j].servicePrice%></td>
                    <td><%=serviceItem[j].serviceTimes%>次</td>
                </tr>

                <%}%>
                <tr>
                    <td colspan="5" class="promt-note">
                        <%== setMark(data[i].promtNote) %>
                    </td>
                </tr>
                <%}%>
                </tbody>
            </table>
        </div>
    </div>
    <%}}%>
</script>
<#--选中的服务包模版渲染-->
<script id="selectedPackTpl" type="text/html">
    <%if(data){%>
    <input type="hidden" id="isSelectedPackage" value="0">
    <div class="pack-content fl pack-content-choose" data-id="<%=data.id%>">
        <div class="consume-title clearfix">
            <h3 class="fl"><%if(data.recommend){%><span class="recommend">推荐</span><%}%>
                <span class="pack-name font-yahei"><%=data.packageName%></span>
                <span class="summary"><%=data.description%></span></h3>
            <p class="fr">市场售价：<span class="money"><%= toFixed(data.marketPrice)%></span></p>
        </div>
        <table>
            <thead>
            <tr>
                <th class="col1">项目名称</th>
                <th class="col2">型号</th>
                <th class="col3">单位</th>
                <th class="col4">市场价</th>
                <th class="col5">次数</th>
            </tr>
            </thead>
            <tbody>
            <%if(data.serviceItems && data.serviceItems.length){%>
            <%var serviceItem = data.serviceItems%>
            <%for(var j = 0;j < serviceItem.length;j++){%>
            <% var serviceItemMaterialBOList = serviceItem[j].serviceItemMaterialBOList; %>
            <tr>
                <%if(serviceItem[j].id == '49'){%>
                <input type="hidden" value="49" class="color-type"/>
                <%}%>
                <td><%=serviceItem[j].serviceName%></td>
                <td class="max-text js-show-tips ellipsis-1"><%if(serviceItem[j].serviceMaterialModel){%>
                    <%=serviceItem[j].serviceMaterialModel%><%}else{%>/<%}%></td>
                <td><%=serviceItem[j].serviceItemUnit%></td>
                <td>&yen;<%=serviceItem[j].servicePrice%></td>
                <td><%=serviceItem[j].serviceTimes%>次</td>
            </tr>
            <%}%>
            <%}%>
            </tbody>
        </table>
    </div>
    <%}%>
</script>

<!--防冻液颜色确认-->
<script type="text/html" id="dialogTpl">
    <div class="yqx-dialog">
        <div class="yqx-dialog-header">
            <h1 class="yqx-dialog-headline">防冻液颜色确认</h1>
        </div>
        <div class="yqx-dialog-body">
            <div class="tip-text">您选择的服务包内容包含“更换防冻液”项目，
                请与车主确认当前车辆使用的防冻液颜色</div>
            <div class="btn-box">
                <a href="javascript:;" data-color="1">使用红色</a>
                <a href="javascript:;" data-color="2">使用绿色</a>
            </div>
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-color-confim">确认</button>
        </div>
    </div>
</script>

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
<#include "yqx/tpl/ax_insurance/service-package-third-party-protocol-tpl.ftl">
<#include "yqx/tpl/ax_insurance/service-package-third-party-protocol-with-reward-tpl.ftl">
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/create/confirmInfo.js?5c77a48f63fd3ca17d898b6a24234844"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/smart/common/smart.js?4abf167c5df3e2fa7854a453b2cec085"></script>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/common/insurance_common.js?4dab749d19d83d53e7cc1f74932b1755"></script>
<#include "yqx/layout/footer.ftl">