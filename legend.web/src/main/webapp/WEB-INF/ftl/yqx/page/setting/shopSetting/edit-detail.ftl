<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/shopSetting/edit-detail.css?764265cbb6d053dc95c2caa257c281fc"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">门店信息</h3>
        </div>
        <div class="content">
            <div class="company-info clearfix">
                <div class="company-pic fl">
                    <img src="${BASE_PATH}/static/img/page/setting/shopSetting/shop-ico.png"/>
                </div>
            <#if SESSION_SHOP_LEVEL?number == 10 || SESSION_SHOP_LEVEL?number == 11 || SESSION_SHOP_LEVEL?number == 12>
                <div class="company-box fl shop-width">
                    <p>注册公司名</p>
                <#if crmShop>
                    <h3 class="ellipsis-1 js-show-tips">${crmShop.companyFormalName}</h3>
                    <p>注册电话:<span>${crmShop.contactsMobile}</span></p>
                <#else>
                    <h3>暂无门店注册信息</h3>
                </#if>
                </div>
                <div class="company-box fl yun-width">
                    <p>系统版本</p>
                    <h3>云修系统${shopModel}</h3>
                    <p class="ellipsis-1 js-show-tips">系统有效期:${gmtCreate} — ${expireTime}</p>
                </div>
                <div class="shop-box fl shop-box-width">
                    <p>门店汇款账号</p>
                    <h3>
                        <#if financeAccount>
                        ${financeAccount.bank}:${financeAccount.account}
                            <#else>
                            暂无银行卡信息
                        </#if>
                    </h3>
                    <a href="javascript:;" class="edit-bank"> <img src="${BASE_PATH}/static/img/page/setting/shopSetting/edit-ico.png"/>去编辑</a>
                </div>
            <#else>
                <div class="company-box fl">
                    <p>注册公司名</p>
                    <#if crmShop>
                        <h3 class="ellipsis-1 js-show-tips">${crmShop.companyFormalName}</h3>
                        <p>注册电话:<span>${crmShop.contactsMobile}</span></p>
                    <#else>
                        <h3>暂无门店注册信息</h3>
                    </#if>
                </div>

                <div class="shop-box fl">
                    <p>门店汇款账号</p>
                    <h3>
                        <#if financeAccount>
                        ${financeAccount.bank}:${financeAccount.account}
                        <#else>
                            暂无银行卡信息
                        </#if>
                    </h3>
                    <a href="javascript:;" class="edit-bank"> <img src="${BASE_PATH}/static/img/page/setting/shopSetting/edit-ico.png"/>去编辑</a>
                </div>
            </#if>
            </div>
            <div class="form-box1" id="shop">
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        门店名称:
                    </div>
                    <div class="form-item w-210">
                        <input type="text" name="name" class="yqx-input yqx-input-small"
                               value="${shop.name}" placeholder="" data-v-type="required">
                    </div>
                    <span>展示到手机端，尽量不超过八个字</span>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        打印抬头:
                    </div>
                    <div class="form-item w-210">
                        <input type="text" name="companyName" class="yqx-input yqx-input-small"
                               value="${shop.companyName}" placeholder="" data-v-type="required">
                    </div>
                    <span>打印时显示的门店名称，请谨慎填写</span>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        联系人:
                    </div>
                    <div class="form-item">
                        <input type="text" name="contact" class="yqx-input yqx-input-small"
                               value="${shop.contact}" placeholder="" data-v-type="required">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        门店电话:
                    </div>
                    <div class="form-item">
                        <input type="text" name="tel"
                               class="yqx-input yqx-input-small"
                               value="${shop.tel}" placeholder="" data-v-type="required">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        门店地址:
                    </div>
                    <div class="form-item">
                        <input type="text" name="provinceName"
                               class="yqx-input yqx-input-icon yqx-input-small js-province"
                               value="${shop.provinceName}" placeholder="省" data-v-type="required">
                        <input type="hidden" value="${shop.province}" name="province">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-item city-box">
                        <input type="text" name="cityName" class="yqx-input yqx-input-icon yqx-input-small js-city"
                               value="${shop.cityName}" placeholder="市" data-v-type="required">
                        <input type="hidden" value="${shop.city}" name="city">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-item district-box">
                        <input type="text" name="districtName" class="yqx-input yqx-input-icon yqx-input-small js-district"
                               value="${shop.districtName}" placeholder="区/县" data-v-type="required">
                        <span class="fa icon-angle-down icon-small"></span>
                        <input type="hidden" name="district" value="${shop.district}"
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-item street-box">
                        <input type="text" name="streetName" class="yqx-input yqx-input-icon yqx-input-small js-street"
                               value="${shop.streetName}" placeholder="街道" data-v-type="required">
                        <input type="hidden" name="street" value="${shop.street}">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        详细地址:
                    </div>
                    <div class="form-item w-578">
                        <input type="text" name="address"
                               class="yqx-input yqx-input-small"
                               value="${shop.address}" placeholder="" data-v-type="required">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label">
                        线上店信息:
                    </div>
                    <div class="form-item" style="width: 400px;">
                        <label style="width: 400px;">
                            <input type="checkbox" class="issue js-issue" <#if NOSHOP>disabled</#if>>编辑手机车主端App资料<#if NOSHOP>(app门店不存在,暂不可编辑)</#if>
                        </label>
                    </div>
                </div>
            </div>
            <div class="form-box2" id="crmShop" <#if isCrm == true>style="display: block"</#if>>
                <input type="hidden" name="customerId" value="${shop.userGlobalId}" >
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        营业时间:
                    </div>
                    <div class="form-item ">
                        <input type="text" name="businessTimeBeginStr"
                               class="clockpicker yqx-input yqx-input-icon yqx-input-small"
                               value="${crmShop.businessTimeBeginStr}" placeholder="开始时间" data-v-type="required">
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                    -
                    <div class="form-item">
                        <input type="text" name="businessTimeEndStr"
                               class="clockpicker yqx-input yqx-input-icon yqx-input-small"
                               value="${crmShop.businessTimeEndStr}" placeholder="结束时间" data-v-type="required">
                        <span class="fa icon-calendar icon-small"></span>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        接待人姓名:
                    </div>
                    <div class="form-item">
                        <input type="text" name="saName"
                               class="yqx-input yqx-input-small" value="${crmShop.saName}" placeholder="" data-v-type="required">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        接待人员手机:
                    </div>
                    <div class="form-item">
                        <input type="text" name="saMobilephone" class="yqx-input yqx-input-small"
                               value="${crmShop.saMobilephone}" placeholder="" data-v-type="required | phone">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label pic-label">
                        上传接待照片:
                    </div>
                    <#if crmShop.saImg>
                        <div class="picture-box">
                        <div class="img-box">
                            <img src="${crmShop.saImg}">
                            <span class="close-i js-close"></span>
                            </div>
                        </div>
                    </#if>
                    <div class="picture-add js-img-add <#if crmShop.saImg>hide</#if>" data-type="s">
                        <input type="hidden" name="saImg" value="${crmShop.saImg}">
                        <input type="file" class="hide js-file">
                        <p>上传图片</p>
                        <img src="${BASE_PATH}/static/img/page/setting/shopSetting/upload-add.png"/>
                    </div>
                    <div class="pic-explain">
                        <p>支持图片格式包括：<p>
                        <p>JIG、JPEG、PNG、JPG，大小不超过2M</p>
                    </div>

                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        门店员工数:
                    </div>
                    <div class="form-item w-100">
                        <input type="text" name="headCount"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${crmShop.headCount}" placeholder="" data-v-type="required">
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        平均从业时间:
                    </div>
                    <div class="form-item w-100">
                        <input type="text" name="workingTime"
                               class="yqx-input yqx-input-icon yqx-input-small"
                               value="${crmShop.workingTime}" placeholder="" data-v-type="required">
                        <span class="fa icon-small">年</span>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        专修品牌:
                    </div>
                    <div class="car-brand-form-item">
                        <select label="专修品牌" id="majorCarBrand" class="car-brand">
                            <script type="text/html" id="brandContentTpl">
                                <option value="">请选择</option>
                                <%for(var group in templateData){%>
                                <optgroup label="<%= group%>">
                                    <%var length = templateData[group].length%>
                                    <%for(var i = 0;i < length;i++){%>
                                    <% var brand = templateData[group][i]%>
                                    <option value="<%= brand.id%>"><%= brand.name%></option>
                                    <%}%>
                                </optgroup>
                                <%}%>
                            </script>
                        </select>
                        <div class="field_box"></div>
                        <input type="hidden" name="majorCarBrand" label="专修品牌" value="${crmShop.majorCarBrand}"/>
                    </div>
                </div>
                <div class="show-grid customer-file-box">
                    <div class="form-label pic-label">
                        上传图片:
                    </div><div class="pic-explain" style="line-height: initial">
                        首张图片将处于车主版APP首图位置，建议上传门头、设备、技师、接待室、车间、Logo等
                    </div>
                    <div class="file-box">
                    <#list crmShop.customerFilePathDTOList as item>
                        <div class="picture-box" data-id="${item.id}">
                            <div class="img-box">
                            <img src="${item.imgUrl}">
                            <span class="js-close close-i" data-target="1"></span>
                            <div class="img_sort">
                                <span class="left_sort js-img-sort" data-sort="left"></span>
                                <span class="right_sort js-img-sort" data-sort="right"></span>
                            </div>
                                </div>
                            <div class="form-item">
                                <input class="yqx-input yqx-input-small"
                                       placeholder="请输入名称"
                                       value="${item.remarks}">
                            </div>
                        </div>
                    </#list>
                    <div class="picture-add js-img-add">
                        <p>上传图片</p>
                        <input type="file" class="hide js-file">
                        <img src="${BASE_PATH}/static/img/page/setting/shopSetting/upload-add.png"/>
                    </div>
                    </div>
                </div>
                <div class="show-grid">
                    <div class="form-label form-label-must">
                        门店经纬度:
                    </div>
                    <span>经度:</span>
                    <div class="form-item w-100">
                        <input type="text" name="longitude"
                               class="yqx-input yqx-input-small js-longitude"
                               value="${crmShop.longitude}" placeholder="" data-v-type="required">
                    </div>
                    <span>纬度:</span>
                    <div class="form-item w-100">
                        <input type="text" name="latitude" class="yqx-input yqx-input-small js-latitude"
                               value="${crmShop.latitude}" placeholder="" data-v-type="required">
                    </div>
                    <div id="map"></div>
                </div>
            </div>
            <div class="btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goBack">返回</button>
            </div>
        </div>
    </div>
</div>
<#--百度地图-->
<script src="https://api.map.baidu.com/api?v=2.0&ak=cSGY6d9xfcvZdNIdGA38Y9zZ&s=1"></script>

<link rel="stylesheet" href="${BASE_PATH}/static/third-plugin/clockpicker/jquery-clockpicker.min.css?e362d103abc4cb8e3412a0cdb41a6c14"/>
<script src="${BASE_PATH}/static/third-plugin/clockpicker/jquery-clockpicker.min.js?e362d103abc4cb8e3412a0cdb41a6c14"></script>
<script src="${BASE_PATH}/static/js/page/setting/shopSetting/edit-detail.js?8b731b1f81123da2133f1e6e32248c86"></script>
<#include "yqx/layout/footer.ftl">