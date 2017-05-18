<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/out/out-common.css?10e3e17a63bb9323c82892932093d3e5">
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/warehouse/in/in-edit.css?37152abc0bdd84be5be3092dea091691">
<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/warehouse/warehouse-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-list-title clearfix">
            <h3 class="headline fl"><#if warehouseInVo.status="DRAFT">草稿编辑<#else >采购入库</#if></h3>
        </div>
        <form class="main" id="">
            <div class="show-grid">
                <div class="form-item">
                    <#if warehouseInVo.status="DRAFT">
                        <input type="hidden" name="id" value="${warehouseInVo.id}"/>
                    </#if>
                        <input type="hidden" name="purchaseSn" value="${warehouseInVo.purchaseSn}"/>
                    <label>  <#if warehouseInVo.status="DRAFT">草稿单号<#else >入库单号</#if>
                        <input type="hidden" name="warehouseInSn" value="<#if warehouseInVo.status="DRAFT">${warehouseInVo.warehouseInSn}<#else> ${warehouseInSn}</#if>"/>
                    </label>
                  <i class="order-num blue-num"><#if warehouseInVo.status="DRAFT">${warehouseInVo.warehouseInSn}<#else> ${warehouseInSn}</#if></i>
                </div><div class="form-item">
                    <label class="must"><#if warehouseInVo.status="DRAFT"> 草稿日期<#else >入库日期</#if></label><div class="form-item">
                        <input class="yqx-input js-in-date" name="inTimeStr"
                               data-v-type="required"
                               value="<#if warehouseInVo>${warehouseInVo.inTime?string('yyyy-MM-dd HH:mm')}<#else >${.now?string('yyyy-MM-dd HH:mm')}</#if>">
                        <i class="fa icon-calendar"></i>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item supplier-box">
                    <label class="must">　供应商</label><div class="form-item">
                        <input class="yqx-input js-supplier" placeholder="输入供应商名称查询" value="${warehouseInVo.supplierName}" >
                        <input type="hidden" name="supplierId" data-v-type="required" value="${warehouseInVo.supplierId}"/>
                        <input type="hidden" name="supplierName" value="${warehouseInVo.supplierName}"/>
                    </div>
                    <button class="yqx-btn yqx-btn-3 btn-small fr js-add-supplier">添加供应商</button>
                </div><div class="form-item">
                    <label for="">　联系人</label><input type="text" name="contact" class="yqx-input" value="${warehouseInVo.contact}">
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <label>联系电话</label><div class="form-item">
                    <input class="yqx-input" name="contactMobile"  maxlength="45" value="${warehouseInVo.contactMobile}">
                    </div>
                </div><div class="form-item pay-mode"><label>付款方式</label><input name="payMode" class="yqx-input js-pay-method" value="${warehouseInVo.paymentMode}" disabled>
                    <input type="hidden" name="paymentMode" value="${warehouseInVo.paymentMode}"/>
                <span class="fa icon-angle-down"></span>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <label>发票类型</label><div class="form-item">
                        <input class="yqx-input js-invoice-type" value="${warehouseInVo.invoiceTypeName}" name="invoiceTypeName">
                        <i class="fa icon-angle-down"></i>
                        <input type="hidden" name="invoiceType" value="${warehouseInVo.invoiceType}"/>

                </div>
                </div><div class="form-item">
                <label class="must">　采购人</label><div class="form-item">
                <input type="text" class="yqx-input js-agent" value="${warehouseInVo.purchaseAgentName}">
                <i class="fa icon-angle-down"></i>
                <input type="hidden"
                       data-v-type="required"
                       name="purchaseAgent" value="${warehouseInVo.purchaseAgent}"/>
                <input type="hidden" name="purchaseAgentName" value="${warehouseInVo.purchaseAgentName}"/>
            </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="form-item">
                    <label>　开单人</label><div class="form-item operator-name">
                        <#if warehouseInVo> ${warehouseInVo.operatorName}<#else> ${operatorName}</#if>
                    </div>
                </div>
            </div>
        </form>
        <div class="list-box">
            <div class="nav clearfix">
                <h3 class="fl">入库配件</h3>
                <div class="tool-bar text-minor fr">
                    <div class="tool-item js-batch-add-goods">
                        <i class="icon-plus"></i><i class="i-btn">批量添加配件</i>
                    </div><i class="split-icon ">|</i><div
                        class="tool-item js-new-goods">
                        <i class="icon-plus"></i><i class="i-btn">新建配件资料</i>
                    </div>
                </div>
            </div>
            <table class="yqx-table yqx-table-big yqx-table-hover goods-table">
                <thead>
                    <tr>
                        <th class="text-l tc-1">零件号</th>
                        <th class="text-l tc-2">配件名称</th>
                        <th class="text-l tc-4">入库数量</th>
                        <th class="text-l tc-price">入库单价</th>
                        <th class="text-r tc-price">总金额</th>
                        <th class="text-l tc-6">库存</th>
                        <th class="text-l tc-7">适配车型</th>
                        <th class="text-c">操作</th>
                    </tr>
                </thead>
                <!-- 草稿初始化-->
                <#list warehouseInVo.detailList as detail>
                <tr class="goods-datatr">
                    <!--零件号-->
                    <td class="text-l">
                        <div class="ellipsis-1 js-show-tips">${detail.goodsFormat}
                            <input type="hidden" name="goodsFormat" value="${detail.goodsFormat}"/>
                            <input type="hidden" name="goodsId" value="${detail.goodsId}"/>
                            <input type="hidden" name="goodsSn" value="${detail.goodsSn}"/>
                        </div>
                    </td>
                    <!--配件名称-->
                    <td class="text-l">
                        <div class="ellipsis-2 text-important js-show-tips">${detail.goodsName}
                            <input type="hidden" name="goodsName" value="${detail.goodsName}"/>
                        </div>
                    </td>
                    <!--入库数量-->
                    <td class="text-l">
                        <div class="form-item goods-count has-unit">
                            <input type="text" name="goodsCount" value="${detail.goodsCount}"
                                   class="yqx-input js-goods-num" data-v-type="required | number" maxlength="10"/>
                            <i class="fa goods-fa">${detail.measureUnit}</i>
                        </div>
                    </td>
                    <!--入库单价-->
                    <td class="text-l">
                        <div class="form-item">
                            <input type="text" name="purchasePrice" value="${detail.purchasePrice}"
                                   class="yqx-input money-small-font js-goods-price js-show-tips"
                                   data-v-type="required | price" maxlength="10"/>
                        </div>
                    </td>
                    <!--金额-->
                    <td class="text-r">
                        <div class="form-item">
                            <i class="money-small-font js-show-tips">&yen;<i class="js-goods-amount">${(detail.purchaseAmount?string("0.00"))!}</i>
                            <input type="hidden" class="js-goods-amount" name="purchaseAmount" value="${detail.purchaseAmount}"/>
                        </div>
                    </td>
                    <!--库存数量-->
                    <td class="text-l">${detail.stock}
                        <input type="hidden" name="stock" value="${detail.stock}"/>
                    </td>
                    <!--适配车型-->
                    <td class="text-l">
                        <div class="form-item ellipsis-1 text-minor js-show-tips">${detail.carInfoStr}
                            <input type="hidden" value="${detail.carInfo}" name="carInfo">
                        </div>
                    </td>
                    <td class="text-c">
                        <button class="yqx-link yqx-link-2 js-del-btn">删除</button>
                    </td>
                </tr>

                </#list>
            <!--商品初始化-->
            <#list goodsList as goods>
                <tr class="goods-datatr">
                    <!--零件号-->
                    <td class="text-l">
                        <div class="ellipsis-1 js-show-tips">${goods.format}
                            <input type="hidden" name="goodsFormat" value="${goods.format}"/>
                            <input type="hidden" name="goodsId" value="${goods.id}"/>
                            <input type="hidden" name="goodsSn" value="${goods.goodsSn}"/>
                        </div>
                    </td>
                    <!--配件名称-->
                    <td class="text-l">
                        <div class="ellipsis-2 text-important js-show-tips">${goods.name}
                            <input type="hidden" name="goodsName" value="${goods.name}"/>
                        </div>
                    </td>
                    <!--入库数量-->
                    <td class="text-l">
                        <div class="form-item goods-count has-unit">
                            <input type="text" name="goodsCount" value="0" class="yqx-input text-c js-goods-num"
                                   data-v-type="required | number" maxlength="10"/>
                            <i class="fa goods-fa">${goods.measureUnit}</i>
                        </div>
                    </td>
                    <!--入库单价-->
                    <td class="text-l">
                        <div class="form-item">
                            <input type="text" name="purchasePrice" value="${(goods.inventoryPrice?string("0.00"))!}"
                                   class="yqx-input money-small-font js-goods-price js-show-tips"
                                   data-v-type="required | price" maxlength="10"/>
                        </div>
                    </td>
                    <!--金额-->
                    <td class="text-r">
                        <div>
                            <i class="money-small-font">&yen;<i class="js-goods-amount">0.00</i>
                            <input type="hidden" class="js-goods-amount" name="purchaseAmount" value="0"/>
                        </div>
                    </td>
                    <!--库存-->
                    <td class="text-l">${goods.stock}
                        <input type="hidden" name="stock" value="${goods.stock}"/>
                    </td>
                    <!--适配车型-->
                    <td class="text-l">
                        <div class="form-item ellipsis-1 js-show-tips">${goods.carInfoStr}
                            <input type="hidden" value="${goods.carInfo}" name="carInfo">
                        </div>
                    </td>
                    <td class="text-c">
                        <button class="yqx-link yqx-link-2 js-del-btn">删除</button>
                    </td>
                </tr>

            </#list>
            </table>
            <button class="yqx-btn yqx-btn-3 js-add-goods">选择配件</button>
            <div class="mark-box">
                <div class="show-grid remark-info">
                <div class="form-item">
                    <label>备注</label><input class="yqx-input" name="comment" value="${warehouseInVo.comment}"
                        placeholder="请填写备注信息"/>
                </div>
                </div>
                <div class="show-grid money-count">
                    <div class="form-item">
                        <label>配件合计金额</label><div class="form-item inline-block ">
                        <input class="yqx-input js-goods-total money-small-font" name="goodsAmount"
                               data-v-type="price" value="${(warehouseInVo.goodsAmount?string("0.00"))!}" disabled>
                        </div>
                    </div><i class="fa icon-plus"></i><div class="form-item">
                        <label>税费</label><div class="form-item inline-block">
                    <input class="yqx-input js-money-plus money-small-font" name="tax" data-v-type="price"
                           value="<#if warehouseInVo>${(warehouseInVo.tax?string("0.00"))!}<#else >0.00</#if>">
                    </div>
                    </div><i class="fa icon-plus"></i><div class="form-item">
                        <label>运费</label><div class="form-item inline-block">
                    <input class="yqx-input js-money-plus money-small-font" name="freight" data-v-type="price"
                           value="<#if warehouseInVo>${(warehouseInVo.freight?string("0.00"))!}<#else >0.00</#if>">
                    </div>
                    </div>
                </div>
                <div class="money-total">
                    总计：<i class="money-font"> &yen;<span class="money"><#if warehouseInVo> ${(warehouseInVo.totalAmount?string("0.00"))!}<#else > 0.00</#if></span></i>
                    <input type="hidden" name="totalAmount" value="${warehouseInVo.totalAmount}"/>
                </div>
            </div>
            <div class="form-footer btn-group">
                <div class="fl">

                    <#if warehouseInVo.status="DRAFT">
                        <button class="yqx-btn yqx-btn-2 js-draft-stock" data-id="${warehouseInVo.id}">入库</button>
                        <button class="yqx-btn yqx-btn-2 js-edit-draft" data-id="${warehouseInVo.id}">保存草稿</button>
                    <#elseif warehouseInVo>
                        <button class="yqx-btn yqx-btn-2 js-stock" >入库</button>
                    <#else >
                        <button class="yqx-btn yqx-btn-2 js-stock" >入库</button>
                        <button class="yqx-btn yqx-btn-1  js-save-draft" >保存草稿</button>
                    </#if>

                </div>
                <div class="fr">
                    <button class="yqx-btn yqx-btn-1 js-back">返回</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type=" text/html" id="goodsTpl">
    <%if (json){%>
    <%for (var i=0;i< json.length;i++){%>
    <%var goods = json[i]%>
    <% if(goods.id){%>
    <tr class="goods-datatr">
        <!--零件号-->
        <td class="text-l">
            <div class="form-item ellipsis-1 js-show-tips"><%=goods.format%>
                <input type="hidden" name="goodsFormat" value="<%=goods.format%>"/>
                <input type="hidden" name="goodsId" value="<%=goods.id%>"/>
                <input type="hidden" name="goodsSn" value="<%=goods.goodsSn%>"/>
            </div>
        </td>
        <!--配件名称-->
        <td class="text-l">
            <div class="ellipsis-2 text-important js-show-tips"><%=goods.name%>
                <input type="hidden" name="goodsName" value="<%=goods.name%>"/>
            </div>
        </td>
        <!--入库数量-->
        <td class="text-l">
            <div class="form-item goods-count has-unit">
                <input type="text" name="goodsCount" value="<%=goods.goodsNum || 1%>"
                       class="yqx-input text-c js-goods-num"
                       data-v-type="required | number" maxlength="10"/>
                <i class="fa goods-fa"><%=goods.measureUnit%></i>
            </div>
        </td>
        <!--入库单价-->
        <td class="text-l">
            <div class="form-item">
                <input type="text" name="purchasePrice" value="0.00"
                       class="yqx-input money-small-font js-goods-price"
                       data-v-type="required | price" maxlength="10"/>
            </div>
        </td>
        <!--金额-->
        <td class="text-r">
            <div class="form-item">
                <i class="money-small-font">&yen;<i class="js-goods-amount">0.00</i></i>
                <input type="hidden" class="js-goods-amount" name="purchaseAmount" value="0"/>
            </div>
        </td>
        <!--库存-->
        <td class="text-l"><%=goods.stock%>
            <input type="hidden" name="stock" value="<%=goods.stock%>"/>
        </td>
        <!--适配车型-->
        <td class="text-l">
            <div class="form-item ellipsis-1 js-show-tips"><%=goods.carInfoStr%>
                <input type="hidden" value="<%=goods.carInfo%>" name="carInfo">
            </div>
        </td>
        <td class="text-c">
            <button class="yqx-link yqx-link-2 js-del-btn">删除</button>
        </td>
    </tr>
    <%}%>
    <%}%>
    <%}%>
</script>

<!-- 批量添加物料模板 -->
<#include "yqx/tpl/order/batch-add-part-tpl.ftl">
<!-- 新增配件模板 -->
<#include "yqx/tpl/order/new-part-tpl.ftl">
<!-- 添加配件 -->
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<#--添加供应商-->
<#include "yqx/tpl/common/add-supplier-tpl.ftl">
<#include "yqx/tpl/common/goods-type-tpl.ftl">

<#--选择配件模板-->
<script type="text/html" id="add-goods2">
    <% if (json && json.data && json.data.length) { %>
    <%for(var i=0;i < json.data.length;i++){%>
    <%var item=json.data[i];%>
    <tr>
        <td class="col-1 text-l js-show-tips"><%=item.format%></td>
        <td class="col-2 text-l js-show-tips"><%=item.name%></td>
        <td class="col-3 text-l js-show-tips"><#-- 适配车型，显示4个车型，其它隐藏，鼠标移上显示全部。-->
            <% var carInfoList = item.carInfoList;%>
            <% if(carInfoList && carInfoList.length>0){ %>
            <% var carInfoLength = carInfoList.length;%>
            <% var tempHtml="";%>
            <% for(var j=0;j < carInfoLength;j++){%>
            <% var carBrandName = "" ;%>
            <% if(carInfoList[j].carBrandName){%>
            <% carBrandName = carInfoList[j].carBrandName; %>
            <% }%>
            <% tempHtml += carBrandName; %>
            <% if((j+1) < carInfoLength){%>
            <% tempHtml += " | "%>
            <% }%>
            <% }%>
            <span>
            <% for(var j=0;j < carInfoLength;j++){%>
            <% if(j<4){%>
                <%= carInfoList[j].carBrandName;%>
                <% if(j<3 && j < carInfoLength-1){ %><span>|</span><%}%>
            <% }%>
            <%}%>
            <% if(carInfoLength>4){%>...<%}%>
            </span>
            <%}%>
            <% if(item.carInfoStr !=null){%>
            <%=item.carInfoStr;%>
            <%}else{%>
            <%}%></td>
        <td class="col-4 js-show-tips text-r"><%=item.lastInPrice%></td>
        <td class="col2 text-r"><%=item.price%></td>
        <td class="col-6"><%=item.stock%></td>
        <td>
            <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-goods-chosen">选择</a>
            <input type="hidden" value="<%=toStr(item)%>">
        </td>
    </tr>
    <%} }%>
</script>

<script type="text/html" id="add-goods-tpl2">
    <div class="add-goods-dg">
        <div class="dialog-title">选择配件</div>
        <div class="search-box text-center">
            <div class="form-item">
                <input type="text" class="yqx-input format" name="goodsFormat" placeholder="零件号"/>
            </div><div class="form-item">
            <input type="text" class="yqx-input format" name="carInfoLike" placeholder="车辆型号"/>
        </div><div class="form-item">
            <input type="text" class="yqx-input goods-name" name="goodsName" placeholder="配件名称"/>
        </div><a href="javascript:;" class="yqx-btn yqx-btn-3 js-goods-search-btn">搜索</a>
        </div>
        <div class="search-result-head">
            <table class="yqx-table yqx-table-hidden yqx-table-hover">
                <thead>
                <tr>
                    <th class="col-1 text-l">零件号</th>
                    <th class="col-2 text-l">配件名称</th>
                    <th class="col-3 text-l">适配车型</th>
                    <th class="col-4 text-r">最近入库价</th>
                    <th class="col2 text-r">售价</th>
                    <th class="col-6">库存</th>
                    <th>操作</th>
                </tr>
                </thead>
            </table>
        </div>
        <div class="search-result">
            <table class="yqx-table">
                <tbody id="goods-result-list"></tbody>
            </table>
        </div>
    </div>
</script>

<script src="${BASE_PATH}/static/js/page/warehouse/in/in-edit.js?83f59d5f214a9fe822150756b6b659de"></script>
<#include "yqx/layout/footer.ftl">
