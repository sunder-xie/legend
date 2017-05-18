<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/serviceInfo/serviceInfo-edit.css?b12dbbb4426b2033e46c38b044ebba11"/>
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
            <h3 class="headline">服务资料<small>－<#if shopServiceInfoDTO.id>编辑<#else>新增</#if>服务<#if shopServiceInfoDTO.suiteNum == 2>套餐</#if></small></h3>
        </div>
        <div class="content">
            <input type="hidden" class="js-serviceGoodsSuiteId" value="${shopServiceInfoDTO.serviceGoodsSuiteDTO.id}">
            <div class="form-box" id="shopServiceInfo">
                <div class="title-box clearfix">
                    <p class="title fl">基本信息</p>
                    <p class="number fr">服务编号：<input type="text" disabled value="${shopServiceInfoDTO.serviceSn}" name="serviceSn"/></p>
                </div>
                <input type="hidden" name="id" class="js-serviceInfoId" value="${shopServiceInfoDTO.id}"/>
                <input type="hidden" name="editStatus" value="${shopServiceInfoDTO.editStatus}"/>
                <#--服务类型：1:常规服务,2:其它费用服务-->
                <input type="hidden" name="type" value="1"/>
                <input type="hidden" name="suiteNum" value="${shopServiceInfoDTO.suiteNum}"/>
                <input type="hidden" name="flags" value="${shopServiceInfoDTO.flags}"/>
                    <div class="form-label form-label-must">
                        服务名称:
                    </div>
                    <div class="form-item">
                        <input type="text" name="name" class="yqx-input yqx-input-small js-name" value="${shopServiceInfoDTO.name}" placeholder="" data-v-type="required">
                    </div>
                    <div class="form-label form-label-must">
                        服务类别:
                    </div>
                    <div class="form-item">
                        <input type="text" name="categoryName" class="yqx-input yqx-input-small js-service-type" value="${shopServiceInfoDTO.categoryName}" placeholder="" data-v-type="required">
                        <input type="hidden" name="categoryId" value="${shopServiceInfoDTO.categoryId}"/>
                        <input type="hidden" name="cateTag" value=""/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="form-label">
                        车辆级别:
                    </div>
                    <div class="form-item">
                        <input type="text" name="carLevelName" class="yqx-input yqx-input-small js-car-level" value="${shopServiceInfoDTO.carLevelName}" placeholder="">
                        <input type="hidden" name="carLevelId" value="${shopServiceInfoDTO.carLevelId}"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                <#--非套餐类服务才设置服务费用,共享中心结算价,面漆数-->
                <#if shopServiceInfoDTO.suiteNum != 2>
                    <div class="form-label form-label-must">
                        服务费用:
                    </div>
                    <div class="form-item">
                        <input type="text" name="servicePrice" class="yqx-input yqx-input-small" value="<#if shopServiceInfoDTO.servicePrice>${shopServiceInfoDTO.servicePrice}<#else>0</#if>" placeholder="" data-v-type="required | floating | maxLength:10">
                    </div>
                    <#if SESSION_SHOP_JOIN_STATUS==1||BPSHARE=='true'||SESSION_SHOP_WORKSHOP_STATUS==1>
                        <div class="form-label form-label-must">
                            共享中心结算价:
                        </div>
                        <div class="form-item">
                            <input type="text" name="sharePrice" class="yqx-input yqx-input-small" value="<#if shopServiceInfoDTO.sharePrice>${shopServiceInfoDTO.sharePrice}<#else>0</#if>" placeholder="" data-v-type="required | floating">
                        </div>
                        <div class="item-group <#if shopServiceInfoDTO.cateTag != 5 >hide</#if>">
                            <div class="form-label form-label-must">
                                面漆数:
                            </div>
                            <div class="form-item">
                                <input type="text" name="surfaceNum" class="yqx-input yqx-input-small" value="${shopServiceInfoDTO.surfaceNum}" placeholder="" data-v-type="required | number">
                            </div>
                        </div>
                    </#if>
                </#if>
            </div>
            <#if shopServiceInfoDTO.suiteNum == 2>
            <div class="title-box clearfix">
                <p class="title">添加服务</p>
            </div>
            <div class="table-con">
                <table class="yqx-table yqx-table-hover">
                    <thead>
                    <tr>
                        <th>服务编码</th>
                        <th>服务项目</th>
                        <th>服务类别</th>
                        <th>工时费（元）</th>
                        <th>工时</th>
                        <th>工时金额（元）</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="orderServiceTB">
                    <#list shopServiceInfoList as shopServiceInfo>
                        <tr class="service-list" data-id="${shopServiceInfo.id}">
                            <input type="hidden" value="${shopServiceInfo.id}" name="serviceId"/><!--校验是否添加重复服务时使用-->
                            <input type="hidden" value="${shopServiceInfo.id}" name="id"/>
                            <input type="hidden" value="${shopServiceInfo.categoryId}" name="categoryId"/>
                            <input type="hidden" value="${shopServiceInfo.carLevelId}" name="carLevelId"/>
                            <input type="hidden" value="${shopServiceInfo.suiteGoodsNum}" name="suiteGoodsNum"/>
                            <input type="hidden" value="${shopServiceInfo.marketPrice}" name="marketPrice"/>
                            <input type="hidden" value="${shopServiceInfo.serviceCatName}" name="serviceCatName"/>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceSn" class="yqx-input yqx-input-small" value="${shopServiceInfo.serviceSn}" placeholder="" disabled>
                                </div>
                            </td>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="name" class="yqx-input yqx-input-small" value="${shopServiceInfo.name}" placeholder="" disabled>
                                </div>
                            </td>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="categoryName" class="yqx-input yqx-input-small" value="${shopServiceInfo.categoryName}" placeholder="" disabled>
                                </div>
                            </td>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="servicePrice" class="yqx-input yqx-input-small" value="${shopServiceInfo.servicePrice}" placeholder="" disabled>
                                </div>
                            </td>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceNum" class="yqx-input yqx-input-small" value="${shopServiceInfo.serviceNum}" placeholder="" data-v-type="required | number">
                                </div>
                            </td>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="serviceAmount" class="yqx-input yqx-input-small" value="${shopServiceInfo.serviceAmount}" placeholder="" disabled>
                                </div>
                            </td>
                            <td>
                                <a href="javascript:;" class="yqx-link-2 js-trdel">删除</a>
                            </td>
                        </tr>
                    </#list>
                    </tbody>
                </table>
                <div class="btn">
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-get-service">选择服务</button>
                </div>
            </#if>
                <div class="title-box clearfix">
                    <p class="title">添加配件</p>
                </div>
                <div class="table-con">
                    <table class="yqx-table yqx-table-hover">
                        <thead>
                        <tr>
                            <th>配件编号</th>
                            <th>配件名称</th>
                            <th>零件号</th>
                            <th>单价</th>
                            <th>数量</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="orderGoodsTB">
                        <#list goodsList as goods>
                        <tr class="goods-datatr" data-id="${goods.id}">
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsSn" class="yqx-input yqx-input-small" value="${goods.goodsSn}" placeholder="" readonly="" disabled>
                                </div>
                                <input type="hidden" name="id" value="${goods.id}"/>
                                <input type="hidden" name="measureUnit" value="${goods.measureUnit}"/>
                                <input type="hidden" name="brandId" value="${goods.brandId}"/>
                                <input type="hidden" name="imgUrl" value="${goods.imgUrl}"/>
                                <input type="hidden" name="goodsAmount" value="${goods.goodsAmount}"/>
                            </td>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="name" class="yqx-input yqx-input-small js-parts-name" value="${goods.name}" placeholder="" readonly="" disabled>
                                </div>
                            </td>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="format" class="yqx-input yqx-input-small" value="${goods.format}" placeholder="" readonly="" disabled>
                                </div>
                            </td>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="price" class="yqx-input yqx-input-small" value="${goods.price}" placeholder="" readonly="" disabled>
                                </div>
                            </td>
                            <td>
                                <div class="form-item">
                                    <input type="text" name="goodsNum" class="yqx-input yqx-input-small" value="${goods.goodsNum}" placeholder="" data-v-type="required | number">
                                </div>
                            </td>
                            <td>
                                <a href="javascript:;" class="yqx-link-2 js-trdel">删除</a>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                    <div class="btn">
                    <#if shopServiceInfoDTO.suiteNum != 2>
                        <button class="yqx-btn yqx-btn-3 yqx-btn-small" id="goodsAddBtn" >选择配件</button>
                    </#if>
                    </div>
                    <div class="show-grid">
                        <div class="form-label">
                            线上店信息:
                        </div>
                        <div class="form-item">
                            <label>
                                <input type="checkbox" class="yqx-checkbox" name="appPublishStatus" value="<#if shopServiceInfoDTO.appPublishStatus == 1>1<#else>0</#if>" <#if shopServiceInfoDTO.appPublishStatus == 1> checked</#if> >发布到车主端APP
                            </label>
                        </div>
                    </div>
                    <div class="owner-service <#if shopServiceInfoDTO.appPublishStatus != 1>owner-hide</#if>" id="appServiceInfo">
                        <div class="show-grid">
                            <div class="form-label form-label-must">
                                车主服务类型:
                            </div>
                            <div class="form-item">
                                <input type="text"  class="yqx-input yqx-input-icon yqx-input-small js-carName" value="${shopServiceInfoDTO.appCateName}" placeholder="" data-v-type="required">
                                <span class="fa icon-angle-down icon-small"></span>
                                <input type="hidden" value="" class="default-pic"/>
                                <input type="hidden" value="${shopServiceInfoDTO.appCateId}" class="" name="appCateId"/>
                            </div>
                            <div class="form-label form-label-must">
                                推荐状态:
                            </div>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-recommend" value="<#if shopServiceInfoDTO.isRecommend==1>推荐<#else>不推荐</#if>" placeholder="" data-v-type="required">
                                <span class="fa icon-angle-down icon-small"></span>
                                <input type="hidden" name="isRecommend" value="<#if shopServiceInfoDTO.isRecommend>${shopServiceInfoDTO.isRecommend}<#else>0</#if>"/>
                            </div>
                        </div>
                        <div class="show-grid">
                            <div class="form-label form-label-must">
                                价格显示方式:
                            </div>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-price-type"
                                       value="<#if shopServiceInfoDTO.priceType == 1>正常显示 <#elseif shopServiceInfoDTO.priceType == 2>到店洽谈<#elseif shopServiceInfoDTO.priceType == 3>免费</#if>" placeholder="" data-v-type="required">
                                <span class="fa icon-angle-down icon-small"></span>
                                <input type="hidden" name="priceType" value="${shopServiceInfoDTO.priceType}"/>
                            </div>
                            <div class="form-label">
                                市场价:
                            </div>
                            <div class="form-item">
                                <input type="text" name="marketPrice" class="yqx-input yqx-input-small js-market-price" value="${shopServiceInfoDTO.marketPrice}" placeholder="" data-v-type="floating | marketPriceCheck | number">
                            </div>
                        </div>
                        <div class="show-grid">
                            <div class="form-label form-label-must">
                                预付金额:
                            </div>
                            <div class="form-item">
                                <input type="text" name="downPayment" class="yqx-input yqx-input-icon yqx-input-small" value="${shopServiceInfoDTO.downPayment}" placeholder="" data-v-type="required | floating | downPaymentCheck | number">
                                <span class="fa icon-small">元</span>
                            </div>
                        </div>
                        <div class="show-grid">
                            <div class="form-label pic-label">
                                服务图片:
                            </div>
                            <div class="picture-box js-single-img">
                                <img src="${shopServiceInfoDTO.imgUrl}" class="service-img" id="service-img" data-default-img="${shopServiceInfoDTO.imgUrl}">
                            </div>
                            <div class="picture-add">
                                <input type="file" id="fileBtn" class="file-btn">
                                <p class="upload-text">上传图片</p>
                                <img src="${BASE_PATH}/static/img/page/setting/shopSetting/upload-add.png"/>
                            </div>
                            <div class="pic-explain">
                                <p class="default-hide"><a href="javascript:;" class="yqx-link-2 js-default-pic">我要默认图片</a></p>
                                <p>支持图片格式包括：<p>
                                <p>JIG、JPEG、PNG、JPG，大小不超过5M</p>
                            </div>
                        </div>

                        <div class="show-grid">
                            <div class="form-label pic-label">

                            </div>
                            <div class="img-group" id="imgGroup">
                                <#list thirdServiceInfoList as thirdServiceInfo>
                                <div class="picture-box img-item m-top js-picture-box horizontal-div" draggable="true">
                                    <div class="set-first">
                                        <div class="back"></div>
                                        <p class="js-set-first">置顶</p>
                                    </div>
                                    <a href="javascript:;" class="close js-remove-pic"></a>
                                    <img src="${thirdServiceInfo.imgUrl}" class="service-img1"/>
                                </div>
                                </#list>
                                <div class="picture-box img-item hide m-top js-picture-box horizontal-div" draggable="true">
                                    <div class="set-first">
                                        <div class="back"></div>
                                        <p class="js-set-first">置顶</p>
                                    </div>
                                    <img src="" class="service-img1"/>
                                    <a href="javascript:;" class="close js-remove-pic"></a>
                                </div>
                                    <div class="picture-add m-top">
                                        <input type="file" id="fileBtn1" class="file-btn">
                                        <p>上传图片</p>
                                        <img src="${BASE_PATH}/static/img/page/setting/shopSetting/upload-add.png"/>
                                    </div>
                                    <div style="clear:both" id="msg"></div>
                            </div>
                        </div>
                        <div class="show-grid">
                            <div class="form-label pic-label">
                            </div>
                            <div class="pic-explain">
                                <p>图片可以拖动调整顺序，最多上传十张</p>
                                <p>支持图片格式包括：JIG、JPEG、PNG、JPG，大小不超过5M</p>
                            </div>
                        </div>
                    </div>
                    <div class="show-grid">
                        <div class="form-label">
                            服务描述:
                        </div>
                        <div class="form-item">
                            <textarea class="yqx-textarea service-info" name="serviceNote" id="service_note">${shopServiceInfoDTO.serviceNote}</textarea>
                            <p class="service-note-tips">此服务描述用于车主APP和门店微信公众号中解释说明，请慎重填写</p>
                        </div>
                    </div>
                    <div class="btn-box">
                        <p>服务费用：&yen;<span class="js-service-price">
                            <#if shopServiceInfoDTO.servicePrice>
                                ${shopServiceInfoDTO.servicePrice}
                            <#else>
                                0.00
                            </#if>
                        </span> + 配件金额: &yen;<span class="js-parts-price">
                        <#if shopServiceInfoDTO.servicePrice && shopServiceInfoDTO.suiteAmount>
                            ${shopServiceInfoDTO.suiteAmount - shopServiceInfoDTO.servicePrice}
                        <#else>
                            0.00
                        </#if>
                        </span> = 服务定价 &yen;<span class="js-service-amount">
                        <#if shopServiceInfoDTO.suiteAmount>
                            ${shopServiceInfoDTO.suiteAmount}
                        <#elseif shopServiceInfoDTO.servicePrice>
                            ${shopServiceInfoDTO.servicePrice}
                        <#else>
                            0.00
                        </#if>
                        </span></p>
                        <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save">保存</button>
                        <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-goBack">返回</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!---->
<script type="text/html" id="imgTpl">
    <div class="picture-box">
        <a href="javascript:;" class="close"></a>
        <img src="<%=getImage%>"/>
    </div>
</script>

<!--服务项目-->
<script type="text/html" id="serviceTpl">
    <%if (json){%>
    <%for(var i in json) { %>
    <%var orderService = json[i]%>
    <tr class="service-list" data-id="<%=orderService.id%>">
        <input type="hidden" value="<%=orderService.id%>" name="serviceId"/><!--校验是否添加重复服务时使用-->
        <input type="hidden" value="<%=orderService.id%>" name="id"/>
        <input type="hidden" value="<%=orderService.categoryId%>" name="categoryId"/>
        <input type="hidden" value="<%=orderService.carLevelId%>" name="carLevelId"/>
        <input type="hidden" value="<%=orderService.suiteGoodsNum%>" name="suiteGoodsNum"/>
        <input type="hidden" value="<%=orderService.marketPrice%>" name="marketPrice"/>
        <input type="hidden" value="<%=orderService.serviceCatName%>" name="serviceCatName"/>
        <input type="hidden" value="<%=orderService.suiteNum%>" name="suiteNum"/>
        <input type="hidden" value="<%=orderService.serviceCatName%>" name="categoryName"/>
        <td>
            <div class="form-item">
                <input type="text" name="serviceSn" class="yqx-input yqx-input-small" value="<%=orderService.serviceSn%>" placeholder="" readonly="" disabled>
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="name" class="yqx-input yqx-input-small" value="<%=orderService.name%>" placeholder="" readonly="" disabled>
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="categoryName" class="yqx-input yqx-input-small" value="<%=orderService.serviceCatName%>" placeholder="" readonly="" disabled>
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="servicePrice" class="yqx-input yqx-input-small" value="<%=orderService.servicePrice%>" placeholder="" readonly="" disabled>
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="serviceNum" class="yqx-input yqx-input-small" value="1" placeholder="" data-v-type="required | number">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="serviceAmount" class="yqx-input yqx-input-small" value="<%=orderService.serviceAmount%>" placeholder="" readonly="" disabled>
            </div>
        </td>
        <td>
            <a href="javascript:;" class="yqx-link-2 js-trdel">删除</a>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>


<!--配件-->
<script type=" text/html" id="goodsTpl">
    <%if (json){%>
    <%for (var i=0;i< json.length;i++){%>
    <%var orderGoods = json[i]%>
    <tr class="goods-datatr" data-id="<%=orderGoods.id%>">
        <td>
            <div class="form-item">
                <input type="text" name="goodsSn" value="<%=orderGoods.goodsSn%>"
                       class="yqx-input yqx-input-small js-show-tips" disabled/>
            </div>
            <input type="hidden" name="goodsId" value="<%=orderGoods.id%>"/>
            <input type="hidden" name="id" value="<%=orderGoods.id%>"/>
            <input type="hidden" name="measureUnit" value="<%=orderGoods.measureUnit%>"/>
            <input type="hidden" name="brandId" value="<%=orderGoods.brandId%>"/>
            <input type="hidden" name="imgUrl" value="<%=orderGoods.imgUrl%>"/>
            <input type="hidden" name="goodsAmount" value="<%=orderGoods.goodsAmount%>"/>
            <input type="hidden" name="goodsSoldPrice" value="<%=orderGoods.price%>"/>
            <input type="hidden" name="imgUrl" value="<%=orderGoods.imgUrl%>"/>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="name" class="yqx-input yqx-input-small js-parts-name" value="<%=orderGoods.name%>" placeholder="" readonly="" disabled>
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="format" class="yqx-input yqx-input-small" value="<%=orderGoods.format%>" placeholder="" readonly="" disabled>
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="price" class="yqx-input yqx-input-small" value="<%=orderGoods.price%>" placeholder="" readonly="" disabled>
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="goodsNum" class="yqx-input yqx-input-small" value="<%if(orderGoods.goodsNum){%><%=orderGoods.goodsNum%><%}else{%>1<%}%>" placeholder="" data-v-type="required | number">
            </div>
        </td>
        <td>
            <a href="javascript:;" class="yqx-link-2 js-trdel">删除</a>
        </td>
    </tr>
    <%}%>
    <%}%>
</script>

<!-- 添加服务模版 -->
<#include "yqx/tpl/common/get-service-tpl.ftl">
<!-- 添加物料模版 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<script src="${BASE_PATH}/static/js/page/setting/serviceInfo/serviceInfo-edit.js?d879e474618f98308fb3216cc13ec650"></script>
<#include "yqx/layout/footer.ftl">