<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/activity/service-detail-module.css?d71bcfb9d474c2abe41890dea80dfca1"/>
<fieldset class="module-vo" id="moduleService">
    <input type="hidden" name="uniqueCode" value="${moduleVo.uniqueCode}">
    <input type="hidden" name="moduleIndex" value="${moduleVo.moduleIndex}">
    <input type="hidden" name="moduleType" value="${moduleVo.moduleType}">
    <i class="fieldset-title service-choose-title">选择服务</i>
    <div class="form-item service-scroll-wrapper">
        <div class="service-choose-service">
        <#list moduleVo.detailServiceVoList as service>
             <div class="service-item <#if service.shopServiceInfo??>service-selected firstSelected</#if>">
                <div class="service-item-content js-service-show clearfix">
                    <img src="${service.imgUrl}" class="service-img fl <#if service.imgUrl>service-img-transparent</#if>"/>
                    <div class="fl service-item-text">
                        <h3 title="${service.name}">${service.name}</h3>
                        <p class="service-content-title">服务内容:</p>
                        <p class="service-content-li">${service.shopServiceNote}</p>
                        <input name="contentList" type="hidden" value="${service.shopServiceNote}"/>
                    </div>
                </div>
                <div class="service-item-footer">
                    <#assign defaultPrice = service.servicePrice>
                    <#assign shopPrice = defaultPrice>
                    <#if service.shopServiceInfo??>
                        <#assign shopPrice = service.shopServiceInfo.servicePrice>
                    </#if>
                    <div class="fl">
                        <p class="service-price-box"><#if service.editStatus==1>推荐价格<#else>价格</#if>
                            <strong><small class="service-red-price">￥</small>${shopPrice}</strong>
                        </p>
                        <#if service.agreement>
                            <input type="checkbox" class="service-agree-checkbox marR10" checked <#if service.shopServiceInfo??>disabled</#if>>我已阅读并同意<a class="js-service-agreement" href="javascript:;">《协议》</a>
                        </#if>
                        <div hidden="hidden">
                            <div class="service-agree-wrapper">${service.agreement}
                                <div class="service-agree-footer">
                                    <button class="yqx-btn yqx-btn-3 js-service-agree">同意</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="service-operate fr">
                        <button class="yqx-btn fr js-service-no-join <#if !service.shopServiceInfo>service-hide</#if>">取消参加</button>
                        <button class="yqx-btn fr js-service-join <#if service.shopServiceInfo??>service-hide</#if>">参加</button>
                        <input type="hidden" name="instanceId" value="<#if service.shopServiceInfo??>${service.shopServiceInfo.id}</#if>"/>
                        <input type="hidden" name="tplId" value="${service.id}"/>
                        <input type="hidden" name="editStatus" value="${service.editStatus}"/>
                        <input type="hidden" name="title" value="${service.name}"/>
                        <input type="hidden" name="shopPrice" value="${shopPrice}"/>
                        <input type="hidden" name="defaultPrice" value="${defaultPrice}"/>
                    </div>
                </div>
            </div>
        </#list>
        </div>
    </div>
</fieldset>

<!--价格确认弹窗模板 start-->
<script type="text/template" id="servicePriceTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            <%=data.title%>
        </div>
        <div class="bounce-content">
            <p class="bounce-row"><%=data.content%></p>
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 <%=data.confirmClass%>"  data-default-price="<%=data.defaultPrice%>">确定</button>
            <button class="yqx-btn yqx-btn-1 <%=data.cancelClass%>">取消</button>
        </div>
    </div>
</script>
<!--价格确认弹窗模板 end-->

<!--修改价格弹窗模板 start-->
<script type="text/template" id="serviceModifyPriceTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            <%=data.title%>
        </div>
        <div class="bounce-content">
            淘汽建议的销售价为<%=data.defaultPrice%>元，本次销售价确定为
            <div class="form-item">
                <input class="yqx-input service-bounce-input" id="servicePrice" value="<%=data.defaultPrice%>" data-v-type="required | price">
                <span class="service-yuan">元</span>
            </div>
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 <%=data.confirmClass%>">确定</button>
            <button class="yqx-btn yqx-btn-1 <%=data.cancelClass%>">取消</button>
        </div>
    </div>
</script>
<!--修改价格弹窗模板 end-->

<!--活动内容弹窗模板 start-->
<script type="text/template" id="serviceContentTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            商品/服务
        </div>
        <div class="bounce-content-wrapper clearfix">
            <img class="service-img fl" src="<%=data.src%>" class="fl <%if(data.src){%>service-img-transparent<%}%>"/>
            <div class="fl bounce-service-item ">
                <h3 title="<%=data.title%>"><%=data.title%></h3>
                <div class="clearfix marB4">
                    <p>价格：<strong><small class="service-red-price">￥</small><%=data.price%></strong></p>
                </div>
                <p>服务内容：</p>
                <p class="service-content-li"><%=data.contentList%></p>
            </div>
        </div>
    </div>
</script>
<!--活动内容弹窗模板 end-->
<script src="${BASE_PATH}/static/js/page/wechat/activity/service-detail-module.js?fe4c9b14c5286b28ad377c545c73c1fa"></script>

