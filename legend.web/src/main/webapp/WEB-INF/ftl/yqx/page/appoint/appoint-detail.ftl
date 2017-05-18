<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/appoint/appoint-detail.css?baa25a41e3c13bf65a6d2d721fa160fe"/>
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
            <h1 class="headline fl">预约单详情</h1>
        </div>
        <!--预约单信息 start-->
        <div class="order-message">
            <div class="hidden">
                <div class="js-appointId"  data-appoint-id="${appointDetailFacVo.appointVo.id}"></div>
                <div class="js-customerCarId"  data-customer-car-id="${appointDetailFacVo.appointVo.customerCarId}"></div>
            </div>
            <div class="detail-row">
                <div class="form-label detail-title order-bold">预约单号：</div>
                <div class="form-item">
                    <div class="yqx-text detail-width order-bold">
                    ${appointDetailFacVo.appointVo.appointSn}
                    </div>
                </div>
                <div class="form-label detail-title order-bold">预约时间：</div>
                <div class="form-item">
                    <div class="yqx-text detail-width order-bold">
                    ${appointDetailFacVo.appointVo.appointTimeFormat}
                    </div>
                </div>
            </div>
            <div class="detail-row">
                <div class="form-label detail-title">车牌：</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                        ${appointDetailFacVo.appointVo.license}
                    </div>
                </div>
                <div class="form-label detail-title">车型：</div>
                <div class="form-item">
                    <div class="yqx-text detail-message js-show-tips">
                        ${appointDetailFacVo.appointVo.carInfo}
                    </div>
                </div>
                <div class="form-label detail-title">预约登记人：</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${appointDetailFacVo.appointVo.registrantName}
                    </div>
                </div>
            </div>
            <div class="detail-row">
                <div class="form-label detail-title">车主：</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${appointDetailFacVo.appointVo.customerName}
                    </div>
                </div>
                <div class="form-label detail-title">车主电话：</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${appointDetailFacVo.appointVo.mobile}
                    </div>
                </div>
                <div class="form-label detail-title">预约登记时间：</div>
                <div class="form-item">
                    <div class="yqx-text detail-message">
                    ${appointDetailFacVo.appointVo.gmtModifiedFormat}
                    </div>
                </div>
            </div>
            <div class="detail-row">
                <div class="form-label detail-title">预约内容：</div>
                <div class="form-item">
                    <div class="yqx-text detail-content js-show-tips">
                    ${appointDetailFacVo.appointVo.appointContent}
                    </div>
                </div>
            </div>
            <#if appointDetailFacVo.appointVo.status==-1><div class="seal-box void"></div></#if>
            <#if appointDetailFacVo.appointVo.status==0><div class="seal-box no-confirm"></div></#if>
            <#if appointDetailFacVo.appointVo.status==1><div class="seal-box has-confirm"></div></#if>
            <#if appointDetailFacVo.appointVo.status==2><div class="seal-box has-order"></div></#if>
            <#if appointDetailFacVo.appointVo.status==3||appointDetailFacVo.appointVo.status==4||appointDetailFacVo.appointVo.status==5>
                <div class="seal-box has-chanel"></div>
            </#if>

        </div>
        <!--预约单信息 end-->

        <!--预约单列表 start-->
        <div class="order-infor">
            <#if appointDetailFacVo.appointServiceVoList>
            <div class="order-infor-tab">服务项目</div>
            <div class="serves-list">
                <table>
                    <thead>
                    <tr>
                        <th class="form-label-must">服务名称</th>
                        <th class="form-label-must">服务类别</th>
                        <th class="th-right form-label-must">工时费</th>
                        <th class="th-right form-label-must">工时</th>
                        <th class="th-right th-boundary">金额</th>
                        <th class="th-right th-boundary">优惠</th>
                        <th>服务备注</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list appointDetailFacVo.appointServiceVoList as appointServiceVo>
                    <tr>
                        <td><div class="servers-name ellipsis-1 js-show-tips">${appointServiceVo.serviceName}</div></td>
                        <td>${appointServiceVo.categoryName}</td>
                        <td class="td-right">${appointServiceVo.servicePrice}</td>
                        <td class="td-right">1</td>
                        <td class="td-right td-boundary"><#if appointServiceVo.servicePrice>#{appointServiceVo.servicePrice;m2M2}</#if></td>
                        <td class="td-right td-boundary"><#if appointServiceVo.discountAmount>#{appointServiceVo.discountAmount;m2M2}</#if></td>
                        <td><div class="servers-name js-show-tips">${appointServiceVo.serviceNote}</div></td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            </#if>
            <#if appointDetailFacVo.goodsList>
            <div class="order-infor-tab">配件项目</div>
            <div class="serves-list">
                <table>
                    <thead>
                    <tr>
                        <th colspan="2">配件名称</th>
                        <th>零件号</th>
                        <th class="th-right">售价</th>
                        <th class="th-right">数量</th>
                        <th class="th-right th-boundary">金额</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list appointDetailFacVo.goodsList as goods>
                    <tr>
                        <td colspan="2"><div class="servers-name js-show-tips ellipsis-1">${goods.name}</div></td>
                        <td >${goods.goodsSn}</td>
                        <td class="td-right"><#if goods.price>#{goods.price;m2M2}</#if></td>
                        <td class="td-right">${goods.goodsNum}</td>
                        <td class="td-right td-boundary"><#if goods.goodsAmount>#{goods.goodsAmount;m2M2}</#if></td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
            </#if>
            <#if appointDetailFacVo.appointVo.comment>
                <div class="comment clearfix">
                    <div class="comment-title fl" >备注：</div>
                    <div class="bz-content fl js-show-tips">${appointDetailFacVo.appointVo.comment}</div>
                </div>
            </#if>
            <div class="total pay-total">
                <p>服务费用：<span><#if appointDetailFacVo.appointVo.totalServiceAmount>#{appointDetailFacVo.appointVo.totalServiceAmount;m2M2}<#else>0.00</#if></span>元
                    + 配件费用：<span><#if appointDetailFacVo.appointVo.totalGoodsAmount>#{appointDetailFacVo.appointVo.totalGoodsAmount;m2M2}<#else>0.00</#if></span>元</p>
                <p>
                总计：<span><#if appointDetailFacVo.appointVo.appointAmount>#{appointDetailFacVo.appointVo.appointAmount;m2M2}<#else>0.00</#if></span>元
                    <#if appointDetailFacVo.appointVo.downPayment gt 0.00>
                    <i class="pre-payment">
                        预付定金:<span>#{appointDetailFacVo.appointVo.downPayment;m2M2}</span>元
                    </i></#if>
                </p>
            </div>
            <div class="save-btn-box">
                <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-comeback fr">返回</a>
                <#if appointDetailFacVo.appointVo.status==-1><#--无效-->
                    <button class="yqx-btn yqx-btn-2 yqx-btn-small js-delete">删除</button>
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-copy fr">复制</a>
                </#if>
                <#if appointDetailFacVo.appointVo.status==0><#--待确认-->
                    <a href="javascript:;" class="yqx-btn yqx-btn-2 yqx-btn-small js-confirm">确认</a>
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-chanel-dialog fr">取消</a>
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-copy fr">复制</a>
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-edit fr">编辑</a>
                </#if>
                <#if appointDetailFacVo.appointVo.status==1><#--已确认-->
                    <#if BPSHARE != 'true'>
                    <a href="javascript:;" class="yqx-btn nav-btn btn-blue js-carwash"><i class="fa icon-plus btn-plus"></i>开洗车单</a>
                    <a href="javascript:;" class="yqx-btn nav-btn btn-org js-fast"><i class="fa icon-plus btn-plus"></i>开快修快保单</a>
                    </#if>
                    <!-- 档口店不显示综合维修单 -->
                    <#if SESSION_SHOP_IS_TQMALL_VERSION == 'false'>
                    <a href="javascript:;" class="yqx-btn nav-btn yqx-btn-3 js-zh"><i class="fa icon-plus btn-plus"></i>开综合维修单</a>
                    </#if>
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-chanel-dialog fr">取消</a>
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-copy fr">复制</a>
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-edit fr">编辑</a>
                </#if>
                <#if appointDetailFacVo.appointVo.status==2><#--已开单-->
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-copy fr">复制</a>
                </#if>
                <#if appointDetailFacVo.appointVo.status==3||appointDetailFacVo.appointVo.status==4||appointDetailFacVo.appointVo.status==5><#--已取消-->
                    <button class="yqx-btn yqx-btn-2 yqx-btn-small js-invalid">无效</button>
                    <a href="javascript:;" class="yqx-btn yqx-btn-1 yqx-btn-small js-copy fr">复制</a>
                </#if>
            </div>
        </div>
        <!--预约单列表 end-->
    </div>
    <!-- 右侧内容区 end -->
</div>


<!--取消dialog start -->
<script type="text/html" id="chanel-dialog">
    <div class="tank">
        <div class="btn_group">
            取消原因
        </div>
        <div class="t_middle">
            <div class="link-chanel-btn js-hover">时间冲突</div>
            <div class="link-chanel-btn js-hover">物料不够</div>
            <div class="link-chanel-btn js-hover">和车主协商一致取消</div>
            <div class="link-chanel-btn js-hover">其他</div>
        </div>
        <div class="t-bottom">
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-cancelAppoint" data-appoint-id="<%=appointId%>">提交</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-chanel-close">取消</button>
        </div>
    </div>
</script>
<!-- 取消dialog end -->


<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/appoint/appoint-detail.js?0bd70f944f0836f92b6e5def8d9af45c"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">