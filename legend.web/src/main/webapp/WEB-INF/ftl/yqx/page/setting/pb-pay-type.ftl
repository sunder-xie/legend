<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/pb-pay-type.css?84107620bb19cb9515cac2b6de662129"/>
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
            <h3 class="headline">付款类型</h3>
        </div>
        <div class="content clearfix">
            <div class="title">
                请从以下选项中选择您门店有的付款类型
            </div>
            <div class="form-box fl">
                <h3 class="type-title">变动费用</h3>
                <div class="pay-type-box" id="changeCostCon">

                </div>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small m-top js-pay-btn">新增变动费用</button>
                <div class="add-pay-type">
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small pay-type-name" value="" placeholder="请输入收款类型名称" data-v-type="required">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-pay-save" data-cost-type="1">保存</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-pay-cancel">取消</button>
                </div>

                <h3 class="type-title">固定费用</h3>
                <div class="pay-type-box" id="fixedCostCon">

                </div>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small m-top js-pay-btn">新增固定费用</button>
                <div class="add-pay-type">
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small pay-type-name" value="" placeholder="请输入收款类型名称" data-v-type="required">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-pay-save" data-cost-type="2">保存</button>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-pay-cancel">取消</button>
                </div>
            </div>
            <div class="pay-picture fr">
                <p>将用于付款时的费用类型</p>
                <div class="pay-type-picture">
                    <img src="${BASE_PATH}/static/img/page/setting/pay-type.png"/>
                </div>
            </div>
        </div>
    </div>
</div>

<!--变动费用-->
<script type="text/html" id="changeCostTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <%if(item.costType == 1){%>
    <div class="col-6">
        <div class="form-label js-show-tips">
            <%=item.typeName%>
        </div>
        <%if(item.shopId != 0){%>
        <div class="on-off <%if(item.showStatus == 0){%>red<%}else{%>green<%}%>" data-id="<%=item.id%>" data-show-type="<%=item.showStatus%>">
            <span class="fl">已开启</span>
            <span class="fl">未开启</span>
            <a href="javascript:;" class="on-off-btn <%if(item.showStatus == 0){%>off-btn<%}%> js-on-btn"></a>
        </div>
        <%}%>
    </div>
    <%}%>
    <%}}%>
</script>
<!--固定费用-->
<script type="text/html" id="fixedCostTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <%if(item.costType == 2){%>
    <div class="col-6">
        <div class="form-label js-show-tips">
            <%=item.typeName%>
        </div>
        <%if(item.shopId != 0){%>
        <div class="on-off <%if(item.showStatus == 0){%>red<%}else{%>green<%}%>" data-id="<%=item.id%>" data-show-type="<%=item.showStatus%>">
            <span class="fl">已开启</span>
            <span class="fl">未开启</span>
            <a href="javascript:;" class="on-off-btn <%if(item.showStatus == 0){%>off-btn<%}%> js-on-btn"></a>
        </div>
        <%}%>
    </div>
    <%}%>
    <%}}%>
</script>


<script type="text/html" id="payTypeTpl">
    <div class="col-6">
        <div class="form-label add-pay-type-name js-show-tips">

        </div>
        <div class="on-off" data-id="" data-show-type="">
            <span class="fl">已开启</span>
            <span class="fl">未开启</span>
            <a href="javascript:;" class="on-off-btn js-on-btn"></a>
        </div>
    </div>
</script>

<script src="${BASE_PATH}/static/js/page/setting/pb-pay-type.js?26f340115529f5b989a7cec1636dc57c"></script>
<#include "yqx/layout/footer.ftl">