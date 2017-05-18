<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/appoint/appoint-edit.css?f8a60605de58ca686d304b6f85190c0a"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/order/order-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline fl"><#if appointDetailFacVo.appointVo.id>编辑预约单<#else>新建预约单</#if></h1>
            <!-- 工作进度 start -->
            <div class="order-process clearfix fr">
                <div class="order-step order-step-finish">
                    <div class="order-step-circle">1</div>
                    <p class="order-step-title">开预约单</p>
                </div>
                <div class="order-step">
                    <div class="order-step-circle">2</div>
                    <p class="order-step-title">开工单</p>
                </div>
            </div>
            <!-- 工作进度 end -->
        </div>
        <!-- 新建预约单信息 start -->
        <div class="order-form clearfix js-appoint-box">
            <div class="show-grid">
                <div class="col-3">
                    <input hidden="true" name="id" value="${appointDetailFacVo.appointVo.id}">
                    <input hidden="true" name="customerCarId" value="${appointDetailFacVo.appointVo.customerCarId}">
                    <div class="form-label form-label-must">
                        车牌
                    </div><div class="form-item yqx-downlist-wrap">
                        <input type="text" name="license" class="yqx-input" value="${appointDetailFacVo.appointVo.license}" placeholder="请输入车牌" data-v-type="required | licence" data-label="车牌">
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label form-label-must">
                        车主
                    </div><div class="form-item">
                        <input type="text" name="customerName" class="yqx-input" value="${appointDetailFacVo.appointVo.customerName}" placeholder="请输入车主" data-v-type="required | maxLength:50" data-label="车主">
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label form-label-must">
                        车主电话
                    </div><div class="form-item">
                        <input type="text" name="mobile" class="yqx-input" value="${appointDetailFacVo.appointVo.mobile}" placeholder="请输入车主电话" data-v-type="required | phone" data-label="车主电话">
                    </div>
                </div>
                <div class="col-3">
                    <div class="form-label form-label-must">
                        预约登记人
                    </div><div class="form-item">
                        <input type="text" name="registrantName" class="yqx-input yqx-input-icon js-select-registrant"
                               value="<#if appointDetailFacVo.appointVo.registrantName>${appointDetailFacVo.appointVo.registrantName}<#else>${SESSION_USER_NAME}</#if>"
                               placeholder="" data-v-type="required" data-label="预约登记人">
                        <input type="hidden" name="registrantId"
                               value="<#if appointDetailFacVo.appointVo.registrantId>${appointDetailFacVo.appointVo.registrantId}<#else>${SESSION_USER_ID}</#if>">
                        <span class="fa icon-angle-down"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must form-label-text">
                        预约单号
                    </div><div class="form-item form-data-width">
                        <input type="text" name="appointSn" class="yqx-input" value="${appointDetailFacVo.appointVo.appointSn}" placeholder="请输入预约单号"
                               data-v-type="required | maxLength:50 | exist" data-v-url="${BASE_PATH}/shop/appoint/is-exist-appointSn" data-v-name="appointSn" data-label="预约单号"
                                <#if appointDetailFacVo.appointVo.id>disabled=""</#if>>
                    </div>
                </div>
                <div class="col-6">
                    <div class="form-label form-label-must">
                        预约时间
                    </div><div class="form-item form-data-width">
                        <input type="text" class="yqx-input yqx-input-icon js-appoint-time" name="appointTimeFormat" value="${appointDetailFacVo.appointVo.appointTimeFormat}" placeholder="请输入预约时间" data-v-type="required" data-label="预约时间"/>
                        <span class="fa icon-calendar"></span>
                    </div>
                </div>
            </div>
            <div class="show-grid">
                <div class="col-6">
                    <div class="form-label form-label-must">
                        预约内容
                    </div><div class="form-item form-data-width">
                        <input type="text" name="appointContent" class="yqx-input js-show-tips" value="${appointDetailFacVo.appointVo.appointContent}" placeholder="请输入预约内容" data-v-type="required | maxLength:500" data-label="预约内容">
                    </div>
                </div>
            </div>
        </div>
        <!-- 新建预约单信息 end -->

        <!--预约单列表 start-->
        <div class="reservationform-box">
            <div class="res-tab">
                <div class="left-tab js-tab">
                    <a href="javascript:;" class="tab-item current-item">服务项目</a><a href="javascript:;" class="js-newgoods tab-item">配件项目
                    <i class="tip js-tip">有新配件<i class="tip-angl"></i></i>
                        </a>
                </div>
            </div>
            <!--服务项目选项卡 start-->
            <div class="res-list tabcon">
                <table class="serves-box">
                    <thead>
                    <tr>
                        <th><span class="form-label ">服务名称</span></th>
                        <th><span class="form-label ">服务类别</span></th>
                        <th><span class="form-label ">工时费</span></th>
                        <th><span class="form-label ">工时</span></th>
                        <th>金额</th>
                        <th>优惠</th>
                        <th>服务备注</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="serviceBox">
                    <#list appointDetailFacVo.appointServiceVoList as appointServiceVo>
                    <tr>
                        <td>
                            <div class="form-item">
                                <input type="hidden" name="id" value="${appointServiceVo.id}">
                                <input type="hidden" name="appointId" value="${appointServiceVo.appointId}">
                                <input type="hidden" name="serviceId" value="${appointServiceVo.serviceId}">
                                <input type="hidden" name="suiteNum" value="${appointServiceVo.suiteNum}">
                                <input type="text" name="serviceName" class="js-show-tips yqx-input yqx-input-small serves-name" value="${appointServiceVo.serviceName}" placeholder="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="categoryName" class="js-show-tips yqx-input yqx-input-small serves-type" value="${appointServiceVo.categoryName}" placeholder="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="servicePrice" class="yqx-input yqx-input-small work-money" value="${appointServiceVo.servicePrice}" placeholder="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-small work-hour" value="1" placeholder="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="serviceAmount" class="yqx-input yqx-input-small money js-show-tips" value="<#if appointServiceVo.servicePrice>#{appointServiceVo.servicePrice;m2M2}</#if>" placeholder="" readonly="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="discountAmount" class="yqx-input yqx-input-small money js-show-tips" value="<#if appointServiceVo.discountAmount>#{appointServiceVo.discountAmount;m2M2}</#if>" placeholder="" readonly="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="serviceNote" class="yqx-input yqx-input-small seres-remarks js-show-tips" value="${appointServiceVo.serviceNote}" placeholder="" data-v-type="maxLength:500" data-label="服务备注">
                            </div>
                        </td>
                        <td><a href="javascript:;" class="dellist-btn js-delbtn">删除</a></td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
                <div class="add-server-btn">
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small add-server" id="select-server-btn">选择服务</a>
                </div>
            </div>
            <!--服务项目选项卡 end-->

            <!--配件物料选项卡 start-->
            <div class="res-list tabcon">
                <table class="goods-box">
                    <thead>
                    <tr>
                        <th><span class="form-label ">零件号</span></th>
                        <th><span class="form-label ">配件名称</span></th>
                        <th><span class="form-label ">售价</span></th>
                        <th><span class="form-label ">数量</span></th>
                        <th>金额</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="goodsBox" class="js-goods-infor">
                    <#list appointDetailFacVo.goodsList as goods>
                    <tr>
                        <td>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-small part-number" value="${goods.name}" placeholder="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-small goods-name js-show-tips" value="${goods.goodsSn}" placeholder="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-small goods-models" value="<#if goods.price>#{goods.price;m2M2}</#if>" placeholder="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="" class="yqx-input yqx-input-small goods-sale" value="${goods.goodsNum}" placeholder="" disabled="">
                            </div>
                        </td>
                        <td>
                            <div class="form-item">
                                <input type="text" name="goodsAmount" class="js-show-tips yqx-input yqx-input-icon yqx-input-small number" value="<#if goods.goodsAmount>#{goods.goodsAmount;m2M2}</#if>" placeholder="" disabled="">
                            </div>
                        </td>
                        <td><a href="javascript:;" class="dellist-btn js-goods-delbtn">删除</a></td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            <!--配件物料选项卡 end-->
            <div class="remarks">
                <div class="form-label">
                    备注
                </div><div class="form-item">
                    <textarea class="yqx-textarea remarks-text js-show-tips" name="comment" id="" placeholder="请输入备注" data-v-type="maxLength:200" data-label="备注">${appointDetailFacVo.appointVo.comment}</textarea>
                </div>
            </div>
            <div class="total" id="amount">
                <p>服务费用：<span class="js-service-amount"></span>元 + 配件费用：<span class="js-goods-amount"></span>元</p>
                <p>总计：<span class="js-amount"></span>元
                <#if appointDetailFacVo.appointVo.downPayment gt 0.00>
                    <i class="pre-payment">
                        预付定金:<span>#{appointDetailFacVo.appointVo.downPayment;m2M2}</span>元
                    </i></#if>
                </p>
            </div>
            <div class="btn-box">
                <a href="javascript:;" class="yqx-btn yqx-btn-2 js-save-btn">保存</a>
                <a href="javascript:;" class="yqx-btn nav-btn yqx-btn-1 js-comeback fr">返回</a>
            </div>
        </div>
        <!--预约单列表 end-->

    </div>
    <!-- 右侧内容区 end -->
</div>
<!--服务项目-->
<script type="text/html" id="serviceTpl">
    <%if (json){%>
    <%for (var i=0;i< json.length;i++){%>
    <%var item = json[i]%>
        <tr>
        <td>
            <div class="form-item">
                <input type="hidden" name="id" value="">
                <input type="hidden" name="appointId" value="">
                <input type="hidden" name="serviceId" value="<%=item.id%>">
                <input type="hidden" name="suiteNum" value="<%=item.suiteNum%>">
                <input type="text" name="serviceName" class="js-show-tips yqx-input yqx-input-small serves-name" value="<%=item.name%>" placeholder="" disabled="">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="categoryName" class="yqx-input yqx-input-small serves-type js-show-tips" value="<%=item.serviceCatName%>" placeholder="" disabled="">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="servicePrice" class="yqx-input yqx-input-small js-number work-money" value="<%=item.servicePrice%>" placeholder="" disabled="">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="" class="yqx-input yqx-input-small work-hour js-float-1" value="1" placeholder="" disabled="">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="serviceAmount" class="yqx-input yqx-input-small money js-show-tips" value="<%=item.servicePrice*1%>" placeholder="" readonly="" disabled="">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="discountAmount" class="yqx-input yqx-input-small money js-show-tips" value="0.00" placeholder="" disabled="disabled">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="serviceNote" class="yqx-input yqx-input-small seres-remarks js-show-tips" value="<%=item.serviceNote%>" placeholder="" data-v-type="maxLength:500" data-label="服务备注">
            </div>
        </td>
        <td><a href="javascript:;" class="dellist-btn js-delbtn">删除</a></td>
    </tr>
    <%}}%>
</script>
<!--物料-->
<script type="text/html" id="goodsTpl">
    <%if (json){%>
    <%for (var i=0;i< json.length;i++){%>
    <%var item = json[i]%>
    <tr>
        <td>
            <div class="form-item">
                <input type="text" name="" class="yqx-input yqx-input-small part-number" value="<%=item.goodsSn%>" placeholder="" disabled="">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="" class="yqx-input yqx-input-small goods-name js-showt-tips" value="<%=item.name%>" placeholder="" disabled="">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="" class="yqx-input yqx-input-small goods-models js-float-2 js-show-tips" value="<%=item.price%>" placeholder="" disabled="">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="" class="yqx-input yqx-input-small goods-sale js-float-1" value="<%=item.goodsNum%>" placeholder="" disabled="">
            </div>
        </td>
        <td>
            <div class="form-item">
                <input type="text" name="goodsAmount" class="yqx-input yqx-input-icon yqx-input-small number js-show-tips" value="<%=item.price*item.goodsNum%>" placeholder="" disabled="">
            </div>
        </td>
        <td><a href="javascript:;" class="dellist-btn js-goods-delbtn">删除</a></td>
    </tr>
    <%}}%>
</script>


<!-- 选择服务模板 -->
<#include "yqx/tpl/common/get-service-tpl.ftl">
<!-- 车牌下拉框 -->
<#include "yqx/tpl/common/car-licence-tpl.ftl">

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/common/order/order-common.js?e5348c923294f749a5bda61fbea107a5"></script>
<script  src="${BASE_PATH}/static/js/page/appoint/appoint-edit.js?c14814029b0cc04726644ddcca7c339c"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">