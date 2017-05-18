<#--第二步步骤 开始-->
<input type="hidden" class="modeV" name="modeV" value="${SESSION_ANXIN_INSURANCE_MODEL}">

<div class="allCInfo clearfix">
    <div class="query-result">
        <p>
            <i class="icon-express-smile vertical-align-m"></i>
            <span class="vertical-align-m text-bolder">系统查询到的历史车辆信息</span>
            <span class="explain-word vertical-align-m">（以下信息仅供参考，发现错误请自行修改）</span>
        </p>

        <div class="query-contain">
            <#if insuranceBasic>
                <p><span>品牌车型: </span><#if insuranceBasic.carConfigType>${insuranceBasic.carConfigType}<#else>无</#if></p>
                <p><span>座位数: </span><#if insuranceBasic.seatNumber>${insuranceBasic.seatNumber}<#else>无</#if></p>
                <p><span>发动机号: </span><#if insuranceBasic.carEngineSn>${insuranceBasic.carEngineSn}<#else>无</#if></p>
                <p><span>车架号: </span><#if insuranceBasic.carFrameSn>${insuranceBasic.carFrameSn}<#else>无</#if></p>
                <p><span>车辆登记日期: </span><#if insuranceBasic.carEgisterDate>${insuranceBasic.carEgisterDate?string('yyyy-MM-dd')}<#else>无</#if></p>
            <#else>
                <p>
                    <span class="color-red">抱歉</span> 未查询到信息
                </p>
            </#if>
        </div>

    </div>

    <p>车辆详细信息</p>
    <div class="cDetailInfo">
        <div class="div have newCar-receipt dis">
            <span class="label-must">新车购置发票号</span>
            <input type="text" class="engine_num input yqx-input newCarNum"
                   value="${insuranceBasic.newCarBillSn}">
        </div>
        <div class="div have foc receipt-time dis">
            <span class="label-must">发票开具日期</span>
            <input type="text" class="engine_num invoiceDate dataInput datepicker3"
                   value="<#if insuranceBasic.newCarBillTime>${insuranceBasic.newCarBillTime?string('yyyy-MM-dd')}</#if>"
                   readonly><i class="fa icon-calendar  timeChoose"></i>
        </div>
        <div class="div">
            <span class="ma-l label-must">品牌型号</span>
        <#if insuranceBasic.vehicleCode>
            <div class="tdText js-car-type ma-l" data-vehicleCode="${insuranceBasic.vehicleCode}"
                 data-seatNum="${insuranceBasic.seatNumber}"
                 data-cProdPlace="${insuranceBasic.isProdplace}"
                 data-yearPattern="${insuranceBasic.yearPattern}">${insuranceBasic.carConfigType}</div>
            <div class="choose-btn">重新选择</div>
        <#else>
            <div class="tdText js-car-type ma-l"></div>
            <div class="choose-btn">请选择</div>
        </#if>
        </div>
        <div class="div">
            <span class="label-must">座位数</span>
            <input type="text" name="seatNumber" class="input yqx-input"
                   value=" <#if insuranceBasic.vehicleCode && insuranceBasic.seatNumber>${insuranceBasic.seatNumber}</#if>">
            <span class="v_note">注：若平台返回座位数有误，请自行修正</span>
        </div>
        <div class="div">
            <span class="label-must">发动机号</span>
            <input type="text" class="engine_num input yqx-input motorNum"
                   value="${insuranceBasic.carEngineSn}">
        </div>
        <div class="div">
            <span class="label-must">车架号</span>
            <input type="text" class="frame_num input yqx-input" value="${insuranceBasic.carFrameSn}">
        </div>
        <div class="div foc">
            <span class="label-must">车辆登记日期</span>
        <#--<input type="text" class="Car_reg_d input yqx-input">-->
            <input type="text" class="engine_num dataInput datepicker3 registerDate"
                   value="<#if insuranceBasic.carEgisterDate>${insuranceBasic.carEgisterDate?string('yyyy-MM-dd')}</#if>"
                   readonly><i class="fa icon-calendar  timeChoose"></i>
        </div>
        <div class="OrTCar">
            <input type="checkbox" class="transferCar"
                   <#if insuranceBasic.hasTransfered == 1>checked</#if>> 我的是过户车
        </div>
        <div class="div foc transferTime <#if insuranceBasic.hasTransfered == 0 || !insuranceBasic.hasTransfered>dis</#if>">
            <span class="label-must">过户日期</span>
            <input type="text" class="engine_num invoiceDate dataInput datepicker ownershipTime"
                   value="<#if insuranceBasic.transferTime>${insuranceBasic.transferTime?string('yyyy-MM-dd')}</#if>"
                   readonly><i class="fa icon-calendar  timeChoose"></i>
        </div>
    </div>
    <P>车主信息</P>
    <div class="COwnerInfo">
        <div class="div">
            <span class="label-must">车主姓名</span>
            <input type="text" class="COwnerName input yqx-input"
                   value="${insuranceBasic.vehicleOwnerName}">
        </div>
        <div class="div papers-type">
            <span class="label-must">证件类型</span>
            <div class="papers-type-div">
            <#if insuranceBasic.vehicleOwnerCertType>
                <input class="papers js-card-type"
                     data-typeCode="${insuranceBasic.vehicleOwnerCertType}" value="${insuranceBasic.vehicleOwnerCertTypeName}" readonly>
                <!--证件类型-->
            <#else >
                <input class="papers js-card-type" data-typeCode="120001" value="居民身份证" readonly><!--证件类型-->
            </#if>
                <div class="brand-btn s_up v-t-M"></div><!--
                            -->
                <ul class="ulStyle"></ul>
            </div>
        </div>
        <div class="div">
            <span class="label-must">证件号码</span>
            <input type="text" class="IDNumber input yqx-input"
                   value="${insuranceBasic.vehicleOwnerCertCode}">
        </div>
        <div class="div">
            <span class="label-must">车主手机号</span>
            <input type="text" class="OwnerPhNum input yqx-input"
                   value="${insuranceBasic.vehicleOwnerPhone}">
        </div>
        <div class="div recognizeeN <#if insuranceBasic.insuredProvince != '北京市'>dis</#if>">
            <span class="label-must">被保人姓名</span>
            <input type="text" class="insuredName input yqx-input"
                   value="${insuranceBasic.insuredName}">
        </div>

    </div>
    <div class="nextStep">下一步</div>
    <div class="back first-back">返回修改</div>
</div>
