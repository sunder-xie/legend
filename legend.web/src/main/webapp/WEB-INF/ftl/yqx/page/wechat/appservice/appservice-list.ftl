<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/appservice/appservice-list.css?5def996427e7731253daae41db17441b"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fr">
        <div class="order-head clearfix">
            <h1 class="headline">发服务</h1>
        </div>
        <div class="order-body" id="service-publish">
            <ul class="tab-control clearfix" id="tabForm">
                <input type="hidden" name="parentAppCateId" id="parentAppCateId">
                <input type="hidden" name="isRecommend" id="isRecommend">
                <li class="service-tab js-search" id="tab0"
                    data-is-recommend="1">推荐
                </li>
            <#if serviceCateVoList>
                <#list serviceCateVoList as serviceCateVo>
                    <li class="service-tab js-search" id='tab${serviceCateVo_index+1}'
                        data-parent-app-cate-id="${serviceCateVo.id}">${serviceCateVo.name}</li>
                </#list>
            </#if>
                <button type="hidden" class="js-search-btn"></button>
            </ul>
            <table class="yqx-table" id="publishList"></table>
            <div class="table-footer edit-footer js-edit clearfix" hidden>
            <#if hasSettingFunc>
                <a class="yqx-btn yqx-btn-3 fl marR10" href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-edit">新增服务</a>
            </#if>
                <button class="yqx-btn yqx-btn-3 fl js-add-service">选择自定义服务</button>
                <button class="yqx-btn marR10 fr blue-btn js-save">保存</button>
            </div>
            <table class="yqx-table js-edit" id="unpublishList"></table>
            <div class="yqx-page js-edit" id="paging"></div>
        </div>
    </div>
</div>
<script type="text/template" id="publishTpl">
    <#--编辑状态的table start-->
    <thead class="edit-thead js-edit" hidden>
    <tr>
        <th width="30%">服务</th>
        <th width="27%" class="textL">售价展示</th>
        <th width="8%">市场价</th>
        <th class="down-payment" width="8%">
            预付定金
        <#if isOpenPay == false>
            <div class="down-payment-tips">
                <p>你未开通微信支付，请到财务-微信支付中设置</p>
            </div>
        <#else>
            <#if payStatus == 'close'>
                <div class="down-payment-tips">
                    <p>你未开启微信支付，请到设置-支付方式设置中设置</p>
                </div>
            </#if>
        </#if>
        </th>
        <th width="8%">推荐</th>
        <th width="7%">状态</th>
        <th width="8%">操作</th>
    </tr>
    </thead>
    <tbody class="edit-tbody js-edit" hidden
    <%if(json && json.data && json.data.length){%>
    data-length="<%=json.data.length%>"
    <%}else{%>
    data-length="0"
    <%}%>
    >
    <%if(json && json.data && json.data.length){%>
    <%for(var i=0;i< json.data.length;i++){%>
    <%var item=json.data[i]%>
    <tr data-id="<%=item.id%>" data-index="<%=i%>">
        <input type="hidden" name="id" value="<%=item.id%>">
        <td>
            <div class="arrows" title="拖拽排序"></div>
            <img class="td-img" src="<%=item.imgUrl%>" alt="">
            <div class="td-service">
                <p><%=item.name%></p>
                <span class="appoint-status">已预约<%=item.appointCount%></span>
            </div>
        </td>
        <td class="textL sale-price-td">
            <label class="marR5"><input type="radio" class="radio" name="priceTypeA<%=i%>" value="3" no_submit
                <%if(item.priceType == 3){%>checked<%}%> >
                免费
            </label>
            <label class="marR5"><input type="radio" class="radio" name="priceTypeA<%=i%>" value="2" no_submit
                <%if(item.priceType == 2){%>checked<%}%> >
                到店洽谈
            </label>
            <label>
                <input type="radio" class="radio" name="priceTypeA<%=i%>" value="1" no_submit
                <%if(item.priceType == 1){%>checked<%}%> >
                <div class="input-box">
                    <input type="text" class="yqx-input input js-service-price" name="servicePrice" value="<%=item.servicePrice%>"
                           data-origin="<%=item.servicePrice%>"  data-temp="<%=item.servicePrice%>" data-v-type="number | servicePrice"
                    <%if((item.suiteNum>0)||(item.priceType != 1)){%>
                    readonly
                    <%}%>
                    <%if(item.suiteNum>0){%>
                    disable="true"
                    <%}%>
                    >
                    <span>¥</span>
                </div>
            </label>
            <%if(item.suiteNum>0){%>
            <div class="tips">
                <p>服务套餐价格暂时无法修改</p>
            </div>
            <%}%>
            <input type="hidden" class="input" name="priceType" value="<%=item.priceType%>" data-origin="<%=item.priceType%>">
        </td>
        <td>
            <div class="input-box">
                <input type="text" class="yqx-input input js-market-price" name="marketPrice" value="<%=item.marketPrice%>"
                       data-origin="<%=item.marketPrice%>" data-temp="<%=item.marketPrice%>" data-v-type="number | marketPrice"
                <%if((item.suiteNum>0)||(item.priceType != 1)){%>
                readonly
                <%}%>
                <%if(item.suiteNum>0){%>
                disable="true"
                <%}%>
                >
                <span>¥</span>
            </div>
        </td>
        <td>
            <div class="input-box">
                <input type="text" class="yqx-input input" name="downPayment" value="<%=item.downPayment%>"
                       data-origin="<%=item.downPayment%>" data-v-type="number | downPayment"
                <#if isOpenPay == true && payStatus == 'open'>
                <#else>
                       readonly
                </#if>>
                <span>¥</span>
            </div>
        </td>
        <td>
            <div class="toggle <%if(item.isRecommend == 1){%>active<%}%>"></div>
            <input type="hidden" class="input" name="isRecommend" value="<%=item.isRecommend%>" data-origin="<%=item.isRecommend%>">
        </td>
        <td class="publish-status">已发布</td>
        <td class="operate-td">
            <p><a class="js-cancel-publish" href="javascript:;" data-id="<%=item.id%>">取消发布</a></p>
            <a href="${BASE_PATH}/shop/wechat/appservice/edit?serviceId=<%=item.id%>" target="_blank">编辑</a>
        </td>
    </tr>
    <%}%>
    <%}else{%>
    <tr class="no-data">
        <td colspan="7">暂无数据！</td>
    </tr>
    <%}%>
    </tbody>
    <#--编辑状态的table end-->
</script>

<#--未发布的服务列表模板 start-->
<script type="text/template" id="unpublishTpl">
    <colgroup width="30%"></colgroup>
    <colgroup width="27%" class="textL"></colgroup>
    <colgroup width="8%"></colgroup>
    <colgroup width="8%"></colgroup>
    <colgroup width="8%"></colgroup>
    <colgroup width="7%"></colgroup>
    <colgroup width="8%"></colgroup>
    <tbody>
    <%if(json && json.data && json.data.content && json.data.content.length){%>
    <%for(var i=0;i< json.data.content.length;i++){%>
    <%var item=json.data.content[i]%>
    <tr data-id="<%=item.id%>">
        <input type="hidden" name="id" value="<%=item.id%>">
        <td>
            <img class="td-img" src="<%=item.imgUrl%>" alt="">
            <div class="td-service">
                <p><%=item.name%></p>
                <span class="appoint-status" hidden>已预约0<%=item.appointCount%></span>
            </div>
        </td>
        <td class="textL sale-price-td">
            <label class="marR5"><input type="radio" class="radio" name="priceTypeB<%=i%>" value="3" no_submit
                <%if(item.priceType == 3){%>checked<%}%> >
                免费
            </label>
            <label class="marR5"><input type="radio" class="radio" name="priceTypeB<%=i%>" value="2" no_submit
                <%if(item.priceType == 2){%>checked<%}%> >
                到店洽谈</label>
            <label>
                <input type="radio" class="radio" name="priceTypeB<%=i%>" value="1" no_submit
                <%if(item.priceType == 1){%>checked<%}%> >
                <div class="input-box">
                    <input type="text" class="yqx-input js-service-price input" name="servicePrice" value="<%=item.servicePrice%>"
                           data-origin="<%=item.servicePrice%>" data-temp="<%=item.servicePrice%>" data-v-type="number | servicePrice"
                    <%if((item.suiteNum>0)||(item.priceType != 1)){%>
                    readonly
                    <%}%>
                    <%if(item.suiteNum>0){%>
                    disable="true"
                    <%}%>
                    >
                    <span>¥</span>
                </div>
            </label>
            <%if(item.suiteNum>0){%>
            <div class="tips">
                <p>服务套餐价格暂时无法修改</p>
            </div>
            <%}%>
            <input type="hidden" class="input" name="priceType" value="<%=item.priceType%>" data-origin="<%=item.priceType%>">
        </td>
        <td>
            <div class="input-box">
                <input type="text" class="yqx-input input js-market-price" name="marketPrice" value="<%=item.marketPrice%>"
                       data-origin="<%=item.marketPrice%>" data-temp="<%=item.marketPrice%>"  data-v-type="number | marketPrice"
                <%if((item.suiteNum>0)||(item.priceType != 1)){%>
                readonly
                <%}%>
                <%if(item.suiteNum>0){%>
                disable="true"
                <%}%>
                >
                <span>¥</span>
            </div>
        </td>
        <td>
            <div class="input-box">
                <input type="text" class="yqx-input input" name="downPayment" value="<%=item.downPayment%>"
                       data-origin="<%=item.downPayment%>"  data-v-type="number | downPayment"
                <#if isOpenPay == true && payStatus == 'open'>
                <#else>
                       readonly
                </#if>>
                <span>¥</span>
            </div>
        </td>
        <td>
            <div class="toggle <%if(item.isRecommend == 1){%>active<%}%>"></div>
            <input type="hidden" class="input" name="isRecommend" value="<%=item.isRecommend%>" data-origin="<%=item.isRecommend%>">
        </td>
        <td class="publish-status">未发布</td>
        <td class="operate-td">
            <p><a class="js-confirm-publish" href="javascript:;" data-id="<%=item.id%>">发布</a></p>
            <a href="${BASE_PATH}/shop/wechat/appservice/edit?serviceId=<%=item.id%>" target="_blank">编辑</a>
        </td>
    </tr>
    <%}%>
    <%}else{%>
    <tr>
        <td colspan="7">暂无数据！</td>
    </tr>
    <%}%>
    </tbody>
</script>
<#--未发布的服务列表模板 end-->

<!--二维码弹窗模板 start-->
<script type="text/template" id="qrcodeTpl">
    <div class="yqx-dialog">
        <header class="yqx-dialog-header">
            <h3 class="dialog-title"> 扫描下方二维码即可预览最新效果</h3>
        </header>
        <section class="dialog-content">
            <div id="qrcodeView"></div>
        </section>
        <div class="dialog-footer preview-foot">
            <button class="yqx-btn yqx-btn-3 js-preview-confirm">确定</button>
        </div>
    </div>
</script>
<!--二维码弹窗模板 end-->
<script src="${BASE_PATH}/static/third-plugin/qrcode/jquery.qrcode.min.js"></script>
<script src="${BASE_PATH}/static/third-plugin/jquery-ui.min.js"></script>
<script src="${BASE_PATH}/static/js/page/wechat/appservice/appservice-list.js?d7a99981f5a111f7b5ffe8930fc62280"></script>
<#include "yqx/layout/footer.ftl" >
