<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/activity/discount-detail-module.css?ee9ff945fd44735d22c4e15110cbf72b"/>
<fieldset class="module-vo" id="moduleDiscount">
    <input type="hidden" name="uniqueCode" value="${moduleVo.uniqueCode}">
    <input type="hidden" name="moduleIndex" value="${moduleVo.moduleIndex}">
    <input type="hidden" name="moduleType" value="${moduleVo.moduleType}">
    <i class="fieldset-title discount-choose-title">选择服务</i>
    <div class="form-item discount-scroll-wrapper">
        <div class="discount-item-collection">
        <#list moduleVo.detailServiceVoList as service>
            <div class="discount-item">
                <div class="discount-item-content js-discount-show clearfix">
                    <img src="${service.shopServiceInfo.imgUrl}" class="discount-img fl <#if service.shopServiceInfo.imgUrl>discount-img-transparent</#if>"/>
                    <div class="fl discount-item-text">
                        <h3 title="${service.shopServiceInfo.name!service.name}">${service.shopServiceInfo.name!service.name}</h3>
                        <p class="discount-content-title">服务内容:</p>
                        <p class="discount-content-li">${service.shopServiceInfo.serviceNote!service.serviceNote}</p>
                        <input name="contentList" type="hidden" value="${service.shopServiceInfo.serviceNote!service.serviceNote}" no_submit/>
                    </div>
                </div>
                <div class="discount-item-form js-choose-form">
                    <div class="discount-form-row clearfix">
                        <div class="discount-fieldset fl">
                            <span class="discount-form-title long-title">原价</span>
                            <div class="form-item fa-item">
                                <input type="text" name="marketPrice" class="yqx-input discount-form-input"  value="${service.shopServiceInfo.marketPrice}"
                                       data-v-type="number| required | moreThan:servicePrice |maxLength: 5">
                                <span class="fa">元</span>
                            </div>
                        </div>
                        <div class="discount-fieldset fr">
                            <span class="discount-form-title">售价</span>
                            <div class="form-item fa-item">
                                <input type="text" name="servicePrice" class="yqx-input discount-form-input" value="${service.shopServiceInfo.servicePrice!service.servicePrice}"
                                       data-v-type="number| required | maxLength: 5">
                                <span class="fa">元</span>
                            </div>
                        </div>
                    </div>
                    <div class="discount-form-row clearfix">
                        <div class="discount-fieldset fl">
                            <span class="discount-form-title">预付定金</span>
                            <div class="form-item fa-item">
                                <input type="text" name="downPayment" class="yqx-input discount-form-input" value="${service.shopServiceInfo.downPayment!service.downPayment}"
                                       data-v-type="number| required | maxLength: 5| lessThan:floorPrice"
                                       <#if wxPayConfVal=='close'>disabled</#if>>
                                <span class="fa">元</span>
                            </div>
                            <#if wxPayConfVal=='close'>
                                <div class="down-payment-tips">
                                    <p>开通微信支付才可以设置预付定金！</p>
                                </div>
                            </#if>
                        </div>
                        <div class="discount-fieldset fr">
                            <span class="discount-form-title">底价</span>
                            <div class="form-item fa-item">
                                <input type="text" name="floorPrice" class="yqx-input discount-form-input" value="${service.floorPrice}" data-v-type="number | required |lessThan:servicePrice |maxLength: 5">
                                <span class="fa">元</span>
                            </div>
                        </div>
                    </div>
                    <div class="discount-form-row clearfix">
                        <div class="discount-fieldset fl">
                            <span class="discount-form-title">虚拟数量</span>
                            <div class="form-item">
                                <input type="text" name="fakeAmount" class="yqx-input discount-form-input" value="${service.fakeAmount}" data-v-type="number | required | maxLength: 3| integer:1">
                            </div>
                        </div>
                        <div class="discount-fieldset fr">
                            <span class="discount-form-title">数量</span>
                            <div class="form-item">
                                <input type="text" name="realAmount" class="yqx-input discount-form-input" value="${service.realAmount}" data-v-type="number | required | maxLength: 3| lessThan:fakeAmount | integer:1">
                            </div>
                        </div>
                    </div>
                    <div class="discount-form-row">
                        <div class="discount-long-button yqx-btn-2 js-discount-join "<#if service.shopServiceInfo> hidden</#if>>参加</div>
                        <div class="discount-long-button yqx-btn-3 js-discount-no-join" <#if service.shopServiceInfo == null>hidden</#if>>取消参加</div>
                    </div>
                    <input type="hidden" name="id" value="<#if service.shopServiceInfo??>${service.shopServiceInfo.id}</#if>"/>
                    <input type="hidden" name="tplId" value="${service.id}" no_submit/>
                </div>
            </div>
        </#list>
        </div>
    </div>
</fieldset>
<#--选择服务弹窗-->
<div id="discountServicePop" hidden>
    <div class="discount-bounce-bg"></div>
    <div class="discount-bounce-wrapper">
        <h3 class="bounce-title">选择车主服务</h3>
        <div class="bounce-content js-choose-form">
            <fieldset>
                <i class="fieldset-title">选择发布的车主服务</i>
                <div class="form-item fa-item yqx-downlist-wrap">
                    <input class="yqx-input js-appcar-service-select input-long" name="serviceName" placeholder="请输入"  data-v-type="required">
                    <input type="hidden" name="shopServiceId">
                    <input type="hidden" name="serviceNote">
                    <input type="hidden" name="imgUrl">
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">原价</i>
                <div class="form-item fa-item">
                    <input class="yqx-input" name="marketPrice" data-v-type="number | moreThan:servicePrice |required |maxLength: 5">
                    <span class="fa">元</span>
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">售价</i>
                <div class="form-item fa-item">
                    <input class="yqx-input" name="servicePrice" data-v-type="number |required |maxLength: 5">
                    <span class="fa">元</span>
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">底价</i>
                <div class="form-item fa-item">
                    <input class="yqx-input" name="floorPrice" data-v-type="number | lessThan:servicePrice | required |maxLength: 5">
                    <span class="fa">元</span>
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">预约定金</i>
                <div class="form-item fa-item">
                    <input class="yqx-input" name="downPayment" data-v-type="number | required | maxLength: 5| lessThan:floorPrice">
                    <span class="fa">元</span>
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">数量</i>
                <div class="form-item">
                    <input class="yqx-input" name="realAmount" data-v-type="number | required | maxLength: 3 | lessThan:fakeAmount | integer:1">
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">虚拟数量</i>
                <div class="form-item">
                    <input class="yqx-input" name="fakeAmount" data-v-type="number | required| maxLength: 3 | integer:1">
                </div>
            </fieldset>
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 discount-service-choose-y marR10">确定</button>
            <button class="yqx-btn yqx-btn-1 discount-service-choose-n">取消</button>
        </div>
        <span class="layui-layer-setwin">
            <a class="js-discount-pop-close layui-layer-ico layui-layer-close layui-layer-close2" href="javascript:;"></a>
        </span>
    </div>
</div>
<#--选择服务弹窗 end-->

<#--新增服务模板 start-->
<script type="text/template" id="discountAddService">
    <div class="discount-item">
        <div class="discount-item-content js-discount-show clearfix">
            <img src="<%=data.imgUrl%>" class="discount-img fl <%if(data.imgUrl){%>discount-img-transparent<%}%>"/>
            <div class="fl discount-item-text">
                <h3 title="<%=data.serviceName%>"><%=data.serviceName%></h3>
                <p class="discount-content-title">服务内容:</p>
                <p class="discount-content-li"><%=data.serviceNote%></p>
                <input name="contentList" type="hidden" value="<%=data.serviceNote%>" no_submit/>
            </div>
        </div>
        <div class="discount-item-form js-choose-form">
            <div class="discount-form-row clearfix">
                <div class="discount-fieldset fl">
                    <span class="discount-form-title long-title">原价</span>
                    <div class="form-item fa-item">
                        <input type="text" name="marketPrice" class="yqx-input discount-form-input" value="<%=data.marketPrice%>"data-v-type="number| required | moreThan:servicePrice |maxLength: 5">
                        <span class="fa">元</span>
                    </div>
                </div>
                <div class="discount-fieldset fr">
                    <span class="discount-form-title">售价</span>
                    <div class="form-item fa-item">
                        <input type="text" name="servicePrice" class="yqx-input discount-form-input" value="<%=data.servicePrice%>"
                               data-v-type="number| required | maxLength: 5">
                        <span class="fa">元</span>
                    </div>
                </div>
            </div>
            <div class="discount-form-row clearfix">
                <div class="discount-fieldset fl">
                    <span class="discount-form-title">预约定金</span>
                    <div class="form-item fa-item">
                        <input type="text" name="downPayment" class="yqx-input discount-form-input" value="<%=data.downPayment%>" data-v-type="number| required |maxLength: 5 | lessThan:floorPrice">
                        <span class="fa">元</span>
                    </div>
                </div>
                <div class="discount-fieldset fr">
                    <span class="discount-form-title">底价</span>
                    <div class="form-item fa-item">
                        <input type="text" name="floorPrice" class="yqx-input discount-form-input" value="<%=data.floorPrice%>" data-v-type="number| required | lessThan:servicePrice |maxLength: 5">
                        <span class="fa">元</span>
                    </div>
                </div>
            </div>
            <div class="discount-form-row clearfix">
                <div class="discount-fieldset fl">
                    <span class="discount-form-title">虚拟数量</span>
                    <div class="form-item">
                        <input type="text" name="fakeAmount" class="yqx-input discount-form-input" value="<%=data.fakeAmount%>" data-v-type="number | required |maxLength: 3 |integer:1">
                    </div>
                </div>
                <div class="discount-fieldset fr">
                    <span class="discount-form-title">数量</span>
                    <div class="form-item">
                        <input type="text" name="realAmount" class="yqx-input discount-form-input" value="<%=data.realAmount%>" data-v-type="number | required| maxLength: 3 | lessThan:fakeAmount | integer:1">
                    </div>
                </div>
            </div>
            <div class="discount-form-row">
                <div class="discount-long-button yqx-btn-2 js-discount-join">参加</div>
                <div class="discount-long-button yqx-btn-3 js-discount-no-join" hidden>取消参加</div>
            </div>
            <input type="hidden" name="id" value="<%=data.shopServiceId%>"/>
            <input type="hidden" name="tplId" no_submit/>
        </div>
    </div>
</script>
<#--新增服务模板 end-->

<!--活动内容弹窗模板 start-->
<script type="text/template" id="discountContentTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            商品/服务
        </div>
        <div class="bounce-content-wrapper clearfix">
            <img src="<%=data.src%>" class="discount-img fl <%if(data.src){%>discount-img-transparent<%}%>"/>
            <div class="fl bounce-discount-text">
                <h3 title="<%=data.title%>"><%=data.title%></h3>
                <p class="discount-content-title">服务内容:</p>
                <p class="discount-content-li"><%=data.contentList%></p>
            </div>
        </div>
    </div>
</script>
<!--活动内容弹窗模板 end-->
<script src="${BASE_PATH}/static/js/page/wechat/activity/discount-detail-module.js?36103dc931a77cac15390b452e4cd547"></script>





