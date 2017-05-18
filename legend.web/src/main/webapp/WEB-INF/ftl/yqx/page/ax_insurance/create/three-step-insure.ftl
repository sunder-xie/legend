<!--第三步投保页面 公共-->
<div class="all_choose clearfix" xmlns="http://xmlns.jcp.org/jsf/html">
    <p class="detail_w">车辆详细信息</p>
    <div class="car_detail">
        <div>
            <span class="span_w">车牌:</span>
            <div class="car"><#if insuranceBasic.vehicleSn>${insuranceBasic.vehicleSn}<#else >
                尚未取得车牌</#if></div>
        </div>
        <div>
            <span class="span_w">品牌:</span>
            <div class="tdText" id="brand" data-vehicleCode="${insuranceBasic.vehicleCode}"
                 data-seatNum="${insuranceBasic.seatNumber}"
                 data-cProdPlace="${insuranceBasic.isProdplace}"
                 data-yearPattern="${insuranceBasic.yearPattern}">${insuranceBasic.carConfigType}</div>
            <div class="re_choose">重新选择车型</div>
        </div>
        <div>
            <span class="span_w">新车购置价:</span>
            <div class="car_price">${insuranceBasic.newCarPurcharsePrice}</div>
        </div>
    </div>
    <p class="choose_w">方案选择</p>
    <div class="scheme">
        <div class="scheme_con">
            <p class="force">交通强制保险</p>
            <p>系统查询到的历史交强险信息（以下信息仅供参考，发现错误请自行修改）</p>
            <#if insuranceBasic && insuranceBasic.insuranceFormDTOList>
                <#list insuranceBasic.insuranceFormDTOList as DtoList>
                    <#if DtoList.insuranceType ==1 >
                        <#if DtoList.gmtCreate>
                            <p class="insurance-query-style">上份交强险到期时间：${DtoList.gmtCreate?string("yyyy-MM-dd")}</p>
                        <#else>
                            <p class="insurance-query-style">抱歉，未查询到信息</p>
                        </#if>
                    </#if>
                </#list>
            <#else>
                <p class="insurance-query-style">抱歉，未查询到信息</p>
            </#if>
            <#list listInsuranceCategory as li>
            <#if li.insuranceCategoryType==1>
            <table class="force_table">
            <tr>
            <th></th>
            <th>保障项目</th>
            <th>保额</th>
            </tr>
            <tr>
            <td class="check">
                <input type="checkbox" class="forceCheck" <#if li.isCheck>checked</#if> >
            </td>
            <td data-code="${li.insuranceCategoryCode}" class="forceName">${li.insuranceCategoryName}</td>
            <#list li.insuranceItemCoverageVoList as value>
            <td class="touB forceTouB" data-id="${value.id}">${value.insuranceItemCoverageDisplay}</td>
            </#list>
            </tr>
            </table>
            </#if>
            </#list>
            <#assign buspackageStartTime="">
            <#assign buspackageEndTime="">
            <#assign forcepackageStartTime="">
            <#assign forcepackageEndTime="">
            <#assign insuranceFormDTOList=[]>
            <#if mode==3>
                <#assign insuranceFormDTOList= insuranceBasic.insuranceVirtualFormDTOList>
            <#else >
                <#assign insuranceFormDTOList=insuranceBasic.insuranceFormDTOList >
            </#if>
            <div class="times">
                <#if insuranceFormDTOList>
                    <#list insuranceFormDTOList as value>
                        <#if value.insuranceType==2>
                            <#assign buspackageStartTime=value.packageStartTime>
                            <#assign buspackageEndTime=value.packageEndTime>
                        </#if>
                        <#if value.insuranceType==1>
                            <#assign forcepackageStartTime=value.packageStartTime>
                            <#assign forcepackageEndTime=value.packageEndTime>
                        </#if>
                    </#list>
                </#if>
                <span class="startW">交强险生效时间</span>
                <#if forcepackageStartTime>
                    <input type="text" class="startT datepicker2 forceStartT" data-time="${forcepackageStartTime?string("yyyy-MM-dd HH:mm:ss")}" />
                <#else >
                    <input type="text" class="startT datepicker2 forceStartT" data-time="" />
                </#if>
                <span class="endW">交强险失效时间</span>
                <div class="endT forceEndT" readonly>
                    <#if forcepackageEndTime>${(forcepackageEndTime?string("yyyy-MM-dd HH:mm:ss"))}</#if>
                </div>
            </div>
            <p class="business">商业保险</p>
            <p>系统查询到的历史商业险信息（以下信息仅供参考，发现错误请自行修改）</p>
            <#if insuranceBasic && insuranceBasic.insuranceFormDTOList>
                <#list insuranceBasic.insuranceFormDTOList as DtoListTwo>
                    <#if DtoListTwo.insuranceType ==2 >
                        <#if DtoListTwo.gmtCreate>
                            <p class="insurance-query-style">上份商业险到期时间：${DtoListTwo.gmtCreate?string("yyyy-MM-dd")}</p>
                        <#else>
                            <p class="insurance-query-style">抱歉，未查询到信息</p>
                        </#if>
                    </#if>
                </#list>
            <#else>
                <p class="insurance-query-style">抱歉，未查询到信息</p>
            </#if>
            <table class="business_table">
                <tr>
                    <th class="business_th1"></th>
                    <th class="business_th2"></th>
                    <th>保障项目</th>
                    <th class="business_th4">保额</th>
                    <th class="business_th5"></th>
                </tr>
                <!--主险-->
            <#assign flag =0>
            <#assign itemCoverageDisplay="">
            <#assign itemCoverageValue=0 />
            <#list listInsuranceCategory as lis>
                <#if lis.insuranceCategoryType == 2>
                    <#if lis.insuranceSubcategoryType ==1>
                        <tr>
                            <#if flag ==0>
                                <td rowspan="5" class="main">主险</td>
                            </#if>
                            <#if lis.insuranceCategoryCode =="033001">
                               <!--返回修改的时候使用-->
                                <#if lis.isCheck>
                                   <td><input type="checkbox" class="mainCheck CarDamag" checked="checked"></td>
                                <#else >
                                    <td><input type="checkbox" class="mainCheck CarDamag"></td>
                                </#if>
                            <#elseif lis.insuranceCategoryCode =="033002"||lis.insuranceCategoryCode =="033003"||lis.insuranceCategoryCode =="033004">
                                <#if lis.isCheck>
                                    <td><input type="checkbox" class="mainCheck mainCheckMental noCarDamag" checked="checked"></td>
                                <#else >
                                    <td><input type="checkbox" class="mainCheck mainCheckMental noCarDamag"></td>
                                </#if>
                            <#else>
                                <#if lis.isCheck>
                                    <td><input type="checkbox" class="mainCheck noCarDamag" checked="checked"></td>
                                <#else>
                                    <td><input type="checkbox" class="mainCheck noCarDamag"></td>
                                </#if>
                            </#if>
                            <td data-code="${lis.insuranceCategoryCode}" class="textB">${lis.insuranceCategoryName}</td>
                            <#if lis.insuranceItemCoverageVoList?size <= 1>
                                <td class="touB"
                                    data-id="${lis.insuranceItemCoverageVoList[0].id}">${lis.insuranceItemCoverageVoList[0].insuranceItemCoverageDisplay}</td>
                            <#else>
                                <#list lis.insuranceItemCoverageVoList as CoverageVoList>
                                <#if CoverageVoList.defaultChoose =='0'>
                                    <td class="touB" data-BPrice="${CoverageVoList.insuranceItemCoverageValue}" data-id="${CoverageVoList.id}">
                                        <div class="touB_jp">
                                            <#if lis.insuranceCategoryCode=="033004">
                                            <ul class="touB_ul dis">
                                                <#list lis.insuranceItemCoverageVoList as value>
                                                    <li data-id="${value.id}"
                                                        data-price="${value.insuranceItemCoverageValue}">${value.insuranceItemCoverageDisplay}/座,共<span class="totalSeats"></span>座</li>
                                                    <!--编辑的时候使用-->
                                                    <#if value.isCheck>
                                                        <#assign itemCoverageDisplay=value.insuranceItemCoverageDisplay >
                                                        <#assign itemCoverageValue=value.insuranceItemCoverageValue >
                                                    </#if>
                                                </#list>
                                                <input type="hidden" id="chengke" value="${itemCoverageValue}" />
                                            </ul>
                                            <#if itemCoverageDisplay!="">
                                                <div class="bPrice">${itemCoverageDisplay}/座,共<span class="totalSeats"></span>座</div>
                                            <#else >
                                                <div class="bPrice">${CoverageVoList.insuranceItemCoverageDisplay}/座,共<span class="totalSeats"></span>座</div>
                                            </#if>
                                            </#if>
                                            <#if lis.insuranceCategoryCode=="033003">
                                                <ul class="touB_ul dis">
                                                    <!--重新将元素赋值为空-->
                                                    <#assign itemCoverageDisplay="">
                                                    <#assign itemCoverageValue=0>
                                                    <#list lis.insuranceItemCoverageVoList as value>
                                                        <li data-id="${value.id}"
                                                            data-price="${value.insuranceItemCoverageValue}">${value.insuranceItemCoverageDisplay}</li>
                                                        <#if value.isCheck>
                                                            <#assign itemCoverageDisplay=value.insuranceItemCoverageDisplay >
                                                            <#assign itemCoverageValue=value.insuranceItemCoverageValue >
                                                        </#if>
                                                    </#list>
                                                    <input type="hidden" id="siji" value="${itemCoverageValue}" />
                                                </ul>
                                                <#if itemCoverageDisplay!="">
                                                    <div class="bPrice">${itemCoverageDisplay}</div>
                                                <#else >
                                                    <div class="bPrice">${CoverageVoList.insuranceItemCoverageDisplay}</div>
                                                </#if>
                                            </#if>
                                            <#if lis.insuranceCategoryCode=="033002">
                                                <ul class="touB_ul dis">
                                                    <!--重新将元素赋值为空-->
                                                    <#assign itemCoverageDisplay="">
                                                    <#assign itemCoverageValue=0>
                                                    <#list lis.insuranceItemCoverageVoList as value>
                                                        <li data-id="${value.id}"
                                                            data-price="${value.insuranceItemCoverageValue}">${value.insuranceItemCoverageDisplay}</li>
                                                        <#if value.isCheck>
                                                            <#assign itemCoverageDisplay=value.insuranceItemCoverageDisplay >
                                                            <#assign itemCoverageValue=value.insuranceItemCoverageValue >
                                                        </#if>
                                                    </#list>
                                                    <input type="hidden" id="sanze" value="${itemCoverageValue}" />
                                                </ul>
                                                <#if itemCoverageDisplay!="">
                                                    <div class="bPrice">${itemCoverageDisplay}</div>
                                                <#else >
                                                    <div class="bPrice">${CoverageVoList.insuranceItemCoverageDisplay}</div>
                                                </#if>
                                            </#if>
                                            <div class="btn s_up2"></div>
                                        </div>
                                    </td>
                                </#if>
                                </#list>
                            </#if>
                            <td class="inputC">
                                <#if lis.isDeductible>
                                    <input type="checkbox" checked="checked">不计免赔
                                <#else>
                                    <input type="checkbox">不计免赔
                                </#if>
                            </td>
                        </tr>
                        <#assign flag=1>
                    </#if>
                </#if>
            </#list>
                <!--附加险-->
            <#assign flag =0>
            <#list listInsuranceCategory as lis>
                <#if lis.insuranceCategoryType == 2>
                    <#if lis.insuranceSubcategoryType ==2>
                        <tr>
                            <#if flag ==0>
                                <td rowspan="10" class="additional">附加险</td>
                            </#if>
                            <#if lis.insuranceCategoryCode=="033011">
                               <#if lis.isCheck>
                                   <td><input type="checkbox" class="additionalCheck mentalComforts" checked="checked"></td>
                               <#else>
                                   <td><input type="checkbox" class="additionalCheck mentalComforts"></td>
                               </#if>
                            <#else>
                               <#if lis.isCheck>
                                   <td><input type="checkbox" class="additionalCheck additionalCheckAnoth" checked="checked"></td>
                               <#else >
                                   <td><input type="checkbox" class="additionalCheck additionalCheckAnoth"></td>
                               </#if>
                            </#if>
                            <td data-code="${lis.insuranceCategoryCode}" class="textB">${lis.insuranceCategoryName}</td>
                            <#if lis.insuranceItemCoverageVoList?size <= 1>
                                <td class="touB"
                                    data-id="${lis.insuranceItemCoverageVoList[0].id}">${lis.insuranceItemCoverageVoList[0].insuranceItemCoverageDisplay}</td>
                            <#else>
                                <#list lis.insuranceItemCoverageVoList as CoverageVoList>
                                    <#if CoverageVoList.defaultChoose =='0'>
                                        <td class="touB" data-BPrice="${CoverageVoList.insuranceItemCoverageValue}" data-id="${CoverageVoList.id}">
                                            <div class="touB_jp">
                                        <#if lis.insuranceCategoryCode=="033006">
                                                <ul class="touB_ul dis">
                                                    <!--重新将元素赋值为空-->
                                                    <#assign itemCoverageDisplay="">
                                                    <#assign itemCoverageValue=0>
                                                    <#list lis.insuranceItemCoverageVoList as value>
                                                        <li data-id="${value.id}"
                                                            data-price="${value.insuranceItemCoverageValue}">${value.insuranceItemCoverageDisplay}</li>
                                                        <#if value.isCheck>
                                                            <#assign itemCoverageDisplay=value.insuranceItemCoverageDisplay>
                                                            <#assign itemCoverageValue=value.insuranceItemCoverageValue >
                                                        </#if>
                                                    </#list>
                                                    <input type="hidden" id="boli" value="${itemCoverageValue}" />
                                                </ul>
                                                <#if itemCoverageDisplay!="">
                                                    <div class="bPrice">${itemCoverageDisplay}</div>
                                                <#else >
                                                    <div class="bPrice">${CoverageVoList.insuranceItemCoverageDisplay}</div>
                                                </#if>
                                        </#if>
                                        <#if lis.insuranceCategoryCode=="033014">
                                                <ul class="touB_ul dis">
                                                    <!--重新将元素赋值为空-->
                                                    <#assign itemCoverageDisplay="">
                                                    <#assign itemCoverageValue=0>
                                                    <#list lis.insuranceItemCoverageVoList as value>
                                                        <li data-id="${value.id}"
                                                            data-price="${value.insuranceItemCoverageValue}">${value.insuranceItemCoverageDisplay}</li>
                                                        <#if value.isCheck>
                                                            <#assign itemCoverageDisplay=value.insuranceItemCoverageDisplay>
                                                            <#assign itemCoverageValue=value.insuranceItemCoverageValue >
                                                        </#if>
                                                    </#list>
                                                    <input type="hidden" id="huahen" value="${itemCoverageValue}" />
                                                </ul>
                                                <#if itemCoverageDisplay!="">
                                                    <div class="bPrice">${itemCoverageDisplay}</div>
                                                <#else >
                                                    <div class="bPrice">${CoverageVoList.insuranceItemCoverageDisplay}</div>
                                                </#if>
                                        </#if>
                                                <div class="btn s_up2"></div>
                                            </div>
                                        </td>
                                    </#if>
                                </#list>
                            </#if>
                            <#if lis.insuranceCategoryCode=="033006"||lis.insuranceCategoryCode=="033012"||lis.insuranceCategoryCode=="033013">
                                <td></td>
                            <#else>
                                <td class="inputC">
                                    <#if lis.isDeductible>
                                        <input type="checkbox" checked="checked">不计免赔
                                    <#else>
                                        <input type="checkbox">不计免赔
                                    </#if>
                                </td>
                            </#if>
                        </tr>
                        <#assign flag=1>
                    </#if>
                </#if>
            </#list>
            </table>
            <div class="times">
                <span class="startW">商业险生效时间</span>
                <#if buspackageStartTime>
                    <input type="text" class="startT datepicker2 businessStartT" data-time="${buspackageStartTime?string("yyyy-MM-dd HH:mm:ss")}">
                <#else >
                    <input type="text" class="startT datepicker2 businessStartT" data-time="">
                </#if>
                <span class="endW">商业险失效时间</span>
                <div class="endT businessEndT">
                    <#if buspackageEndTime>
                        ${(buspackageEndTime?string("yyyy-MM-dd HH:mm:ss"))}
                    </#if>
                </div>
            </div>
        </div>
    </div>
    <div class="next-Step">下一步</div>
    <div class="next_btn">保费计算</div>
    <div class="back second-back">返回修改</div>
</div>