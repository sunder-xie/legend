<#--第一步-->
<div class="enter_info">
    <div class="form">
        <!--选择投保地区-->
        <span class="choose_name label-must">投保地区</span>
        <div class="province">
            <input class="choose_prov cho_add"
                   data-code="${insuranceBasic.insuredProvinceCode}"
                   placeholder="请选择" value="<#if insuranceBasic>${insuranceBasic.insuredProvince}</#if>" readonly>
            <div class="choose_btn s_up"></div>
            <ul class="prov"></ul>
        </div>
        <div class="address choose_address">
            <input class="choose_region cho_add"
                   data-code="${insuranceBasic.insuredCityCode}"
                   placeholder="请选择" value="<#if insuranceBasic>${insuranceBasic.insuredCity}</#if>" readonly>
            <div class="choose_btn s_up"></div>
            <ul class="region"></ul>
        </div>
        <span class="choose_name text_align label-must">车牌号</span>
        <input type="text" class="car_num bc" value="${insuranceBasic.vehicleSn}"
               <#if insuranceBasic&&insuranceBasic.hasLicense==0>readonly</#if>>
        <label class="noCar"><input type="checkbox" class="checkbox" <#if insuranceBasic.hasLicense==0>checked</#if>>
            尚未取得车牌</label>
        <#if !bihu>
            <div class="carName-div">
                <span class="choose_name label-must">车主姓名</span>
                <input type="text" class="CarName yqx-input" placeholder="请输入车主姓名">
            </div>
        </#if>

    </div>
</div>
<script type="text/javascript" src="${BASE_PATH}/static/js/page/ax_insurance/create/firstepInsure.js?3232be61d14681470bb6138a20e05436"></script>
