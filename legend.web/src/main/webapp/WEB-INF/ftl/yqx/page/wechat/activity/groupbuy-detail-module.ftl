<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/activity/groupBuy-detail-module.css?38fee81f2c685f1407361c149aa587f5"/>
<fieldset class="module-vo" id="moduleGroupBuy">
    <input type="hidden" name="uniqueCode" value="${moduleVo.uniqueCode}">
    <input type="hidden" name="moduleIndex" value="${moduleVo.moduleIndex}">
    <input type="hidden" name="moduleType" value="${moduleVo.moduleType}">
    <i class="fieldset-title groupBuy-choose-title">选择服务</i>
    <div class="form-item groupBuy-scroll-wrapper">
        <div class="groupBuy-item-collection">
        <#list moduleVo.detailServiceVoList as service>
            <div class="groupBuy-item">
                <div class="groupBuy-item-content js-groupBuy-show clearfix">
                    <img src="${service.shopServiceInfo.imgUrl}" class="groupBuy-img fl <#if service.shopServiceInfo.imgUrl>groupBuy-img-transparent</#if>"/>
                    <div class="fl groupBuy-item-text">
                        <h3 title="${service.shopServiceInfo.name!service.name}">${service.shopServiceInfo.name!service.name}</h3>
                        <p class="groupBuy-content-title">服务内容:</p>
                        <p class="groupBuy-content-li">${service.shopServiceInfo.serviceNote!service.serviceNote}</p>
                        <input name="contentList" type="hidden" value="${service.shopServiceInfo.serviceNote!service.serviceNote}" no_submit/>
                    </div>
                </div>
                <div class="groupBuy-item-form js-choose-form">
                    <div class="display-none">
                        <div>
                            <span>已发布数量:${service.realAmount}</span>
                            <div>
                                <input name="oriRealAmount" value="${service.realAmount}" no_submit>
                            </div>
                        </div>
                        <div>
                            <span>已发布虚拟数量:${service.fakeAmount}</span>
                            <div>
                                <input name="oriFakeAmount" value="${service.fakeAmount}" no_submit>
                            </div>
                        </div>
                    </div>
                    <div class="groupBuy-form-row clearfix">
                        <div class="groupBuy-fieldset fl">
                            <span class="groupBuy-form-title long-title">原价</span>
                            <div class="form-item fa-item">
                                <input type="text" name="marketPrice" class="yqx-input groupBuy-form-input"  value="${service.shopServiceInfo.marketPrice}"
                                       data-v-type="number| required | moreThan:servicePrice |maxLength: 5">
                                <span class="fa">元</span>
                            </div>
                        </div>
                        <div class="groupBuy-fieldset fr">
                            <span class="groupBuy-form-title">售价</span>
                            <div class="form-item fa-item">
                                <input type="text" name="servicePrice" class="yqx-input groupBuy-form-input" value="${service.shopServiceInfo.servicePrice!service.servicePrice}"
                                       data-v-type="number| required | maxLength: 5">
                                <span class="fa">元</span>
                            </div>
                        </div>
                    </div>
                    <div class="groupBuy-form-row clearfix">
                        <div class="groupBuy-fieldset fl">
                            <span class="groupBuy-form-title">预付定金</span>
                            <div class="form-item fa-item">
                                <input type="text" name="downPayment" class="yqx-input groupBuy-form-input" value="${service.shopServiceInfo.downPayment!service.downPayment}"
                                       data-v-type="number| required | maxLength: 5| lessThan:groupPrice"
                                <#if wxPayConfVal=='close'>disabled</#if>>
                                <span class="fa">元</span>
                            </div>
                            <#if wxPayConfVal=='close'>
                                <div class="down-payment-tips">
                                    <p>开通微信支付才可以设置预付定金！</p>
                                </div>
                            </#if>
                        </div>
                        <div class="groupBuy-fieldset fr">
                            <span class="groupBuy-form-title">拼团价格</span>
                            <div class="form-item fa-item">
                                <input type="text" name="groupPrice" class="yqx-input groupBuy-form-input" value="${service.groupPrice}" data-v-type="number | required |lessThan:servicePrice |maxLength: 5">
                                <span class="fa">元</span>
                            </div>
                        </div>
                    </div>
                    <div class="groupBuy-form-row clearfix">
                        <div class="groupBuy-fieldset fl">
                            <span class="groupBuy-form-title long-title">数量</span>
                            <div class="form-item">
                                <input type="text" name="realAmount" class="yqx-input groupBuy-form-input" value="${service.realAmount}"
                                       data-v-type="number | required | maxLength: 3| lessThan:fakeAmount | integer:1 | integerMultiple
                                        <#if service.shopServiceInfo>|moreThan:oriRealAmount</#if>">
                            </div>
                        </div>
                        <div class="groupBuy-fieldset fr">
                            <span class="groupBuy-form-title">虚拟数量</span>
                            <div class="form-item">
                                <input type="text" name="fakeAmount" class="yqx-input groupBuy-form-input" value="${service.fakeAmount}"
                                       data-v-type="number | required | maxLength: 3| integer:1
                                        <#if service.shopServiceInfo>|moreThan:oriFakeAmount</#if>">
                            </div>
                        </div>
                    </div>
                    <div class="groupBuy-form-row clearfix">
                        <div class="groupBuy-fieldset fl">
                            <span class="groupBuy-form-title">拼团人数</span>
                            <div class="form-item">
                                <input type="text" name="customerNumber" class="yqx-input groupBuy-form-input"
                                    <#if service.shopServiceInfo && (activityDetailVo.shopActivityStatus == 2 ||activityDetailVo.shopActivityStatus == 0)>
                                       disabled
                                    </#if>
                                       value="${service.customerNumber}" data-v-type="number | required | maxLength: 3| integer:1">
                            </div>
                        </div>
                    </div>
                    <div class="groupBuy-form-row">
                        <div class="groupBuy-long-button yqx-btn-2 js-groupBuy-join "<#if service.shopServiceInfo> hidden</#if>>参加</div>
                        <div class="groupBuy-long-button yqx-btn-3 js-groupBuy-no-join" <#if service.shopServiceInfo == null>hidden</#if>>取消参加</div>
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
<div id="groupBuyServicePop" hidden>
    <div class="groupBuy-bounce-bg"></div>
    <div class="groupBuy-bounce-wrapper">
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
                <i class="fieldset-title">预约定金</i>
                <div class="form-item fa-item">
                    <input class="yqx-input" name="downPayment" data-v-type="number | required | maxLength: 5| lessThan:groupPrice">
                    <span class="fa">元</span>
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">拼团价格</i>
                <div class="form-item fa-item">
                    <input class="yqx-input" name="groupPrice" data-v-type="number | lessThan:servicePrice | required |maxLength: 5">
                    <span class="fa">元</span>
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">数量</i>
                <div class="form-item">
                    <input class="yqx-input" name="realAmount" data-v-type="number | required | maxLength: 3 | lessThan:fakeAmount | integer:1 |integerMultiple">
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">虚拟数量</i>
                <div class="form-item">
                    <input class="yqx-input" name="fakeAmount" data-v-type="number | required| maxLength: 3 | integer:1">
                </div>
            </fieldset>
            <fieldset>
                <i class="fieldset-title">拼团人数</i>
                <div class="form-item">
                    <input class="yqx-input" name="customerNumber" data-v-type="number | required| maxLength: 3 | integer:1">
                </div>
            </fieldset>
        </div>
        <div class="bounce-foot">
            <button class="yqx-btn yqx-btn-3 groupBuy-service-choose-y marR10">确定</button>
            <button class="yqx-btn yqx-btn-1 groupBuy-service-choose-n">取消</button>
        </div>
        <span class="layui-layer-setwin">
            <a class="js-groupBuy-pop-close layui-layer-ico layui-layer-close layui-layer-close2" href="javascript:;"></a>
        </span>
    </div>
</div>
<#--选择服务弹窗 end-->

<#--新增服务模板 start-->
<script type="text/template" id="groupBuyAddService">
    <div class="groupBuy-item">
        <div class="groupBuy-item-content js-groupBuy-show clearfix">
            <img src="<%=data.imgUrl%>" class="groupBuy-img fl <%if(data.imgUrl){%>groupBuy-img-transparent<%}%>"/>
            <div class="fl groupBuy-item-text">
                <h3 title="<%=data.serviceName%>"><%=data.serviceName%></h3>
                <p class="groupBuy-content-title">服务内容:</p>
                <p class="groupBuy-content-li"><%=data.serviceNote%></p>
                <input name="contentList" type="hidden" value="<%=data.serviceNote%>" no_submit/>
            </div>
        </div>
        <div class="groupBuy-item-form js-choose-form">
            <div class="groupBuy-form-row clearfix">
                <div class="groupBuy-fieldset fl">
                    <span class="groupBuy-form-title long-title">原价</span>
                    <div class="form-item fa-item">
                        <input type="text" name="marketPrice" class="yqx-input groupBuy-form-input"  value="<%=data.marketPrice%>"
                               data-v-type="number| required | moreThan:servicePrice |maxLength: 5">
                        <span class="fa">元</span>
                    </div>
                </div>
                <div class="groupBuy-fieldset fr">
                    <span class="groupBuy-form-title">售价</span>
                    <div class="form-item fa-item">
                        <input type="text" name="servicePrice" class="yqx-input groupBuy-form-input" value="<%=data.servicePrice%>"
                               data-v-type="number| required | maxLength: 5">
                        <span class="fa">元</span>
                    </div>
                </div>
            </div>
            <div class="groupBuy-form-row clearfix">
                <div class="groupBuy-fieldset fl">
                    <span class="groupBuy-form-title">预约定金</span>
                    <div class="form-item fa-item">
                        <input type="text" name="downPayment" class="yqx-input groupBuy-form-input" value="<%=data.downPayment%>"
                               data-v-type="number| required | maxLength: 5| lessThan:groupPrice">
                        <span class="fa">元</span>
                    </div>
                </div>
                <div class="groupBuy-fieldset fr">
                    <span class="groupBuy-form-title">拼团价格</span>
                    <div class="form-item fa-item">
                        <input type="text" name="groupPrice" class="yqx-input groupBuy-form-input" value="<%=data.groupPrice%>" data-v-type="number | required |lessThan:servicePrice |maxLength: 5">
                        <span class="fa">元</span>
                    </div>
                </div>
            </div>
            <div class="groupBuy-form-row clearfix">
                <div class="groupBuy-fieldset fl">
                    <span class="groupBuy-form-title long-title">数量</span>
                    <div class="form-item">
                        <input type="text" name="realAmount" class="yqx-input groupBuy-form-input" value="<%=data.realAmount%>" data-v-type="number | required | maxLength: 3| lessThan:fakeAmount | integer:1 |integerMultiple">
                    </div>
                </div>
                <div class="groupBuy-fieldset fr">
                    <span class="groupBuy-form-title">虚拟数量</span>
                    <div class="form-item">
                        <input type="text" name="fakeAmount" class="yqx-input groupBuy-form-input" value="<%=data.fakeAmount%>" data-v-type="number | required | maxLength: 3| integer:1">
                    </div>
                </div>
            </div>
            <div class="groupBuy-form-row clearfix">
                <div class="groupBuy-fieldset fl">
                    <span class="groupBuy-form-title">拼团人数</span>
                    <div class="form-item">
                        <input type="text" name="customerNumber" class="yqx-input groupBuy-form-input" value="<%=data.customerNumber%>" data-v-type="number | required | maxLength: 3| integer:1">
                    </div>
                </div>
            </div>
            <div class="groupBuy-form-row">
                <div class="groupBuy-long-button yqx-btn-2 js-groupBuy-join">参加</div>
                <div class="groupBuy-long-button yqx-btn-3 js-groupBuy-no-join" hidden>取消参加</div>
            </div>
            <input type="hidden" name="id" value="<%=data.shopServiceId%>"/>
            <input type="hidden" name="tplId" no_submit/>
        </div>
    </div>
</script>
<#--新增服务模板 end-->

<!--活动内容弹窗模板 start-->
<script type="text/template" id="groupBuyContentTpl">
    <div class="collection-bounce">
        <div class="bounce-title">
            商品/服务
        </div>
        <div class="bounce-content-wrapper clearfix">
            <img src="<%=data.src%>" class="groupBuy-img fl <%if(data.src){%>groupBuy-img-transparent<%}%>"/>
            <div class="fl bounce-groupBuy-text">
                <h3 title="<%=data.title%>"><%=data.title%></h3>
                <p class="groupBuy-content-title">服务内容:</p>
                <p class="groupBuy-content-li"><%=data.contentList%></p>
            </div>
        </div>
    </div>
</script>

<!--活动内容弹窗模板 end-->
<script src="${BASE_PATH}/static/js/page/wechat/activity/groupBuy-detail-module.js?55c2f9175597f78c6475d825bf821d86"></script>





