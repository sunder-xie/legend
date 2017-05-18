<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/paymentType/pay-type.css?c23965f01cfa00ba23b9e7ba14de41a3"/>
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
            <h3 class="headline">付款类型设置</h3>
        </div>
        <div class="content clearfix">
            <div class="title">
                <i></i>管理门店中的付款类型
            </div>
            <div class="form-box fl">
                <div class="pay-type-box" id="listCon">

                </div>
                <button class="yqx-btn yqx-btn-1 yqx-btn-small m-top js-pay-btn">新增付款类型</button>
                <div class="add-pay-type">
                    <div class="form-item">
                        <input type="text" name="" class="yqx-input yqx-input-small pay-type-name" value="" placeholder="请输入付款类型名称" data-v-type="required">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-pay-save">保存</button>
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
<script type="text/html" id="listTpl">
    <%if(json.success && json.data){%>
    <%for(var i=0; i< json.data.length; i++){%>
    <%var item = json.data[i]%>
    <div class="col-6">
        <div class="form-label js-show-tips">
            <%=item.typeName%>
        </div>
        <%if(item.shopId != 0 ){%>
        <div class="on-off" data-id="<%=item.id%>" data-show-status="<%=item.showStatus%>" <%if(item.showStatus == 0){%>style="background:#fd461e"<%}%>>
        <span class="fl">已开启</span>
        <span class="fl">未开启</span>
        <%if(item.showStatus == 1){%>
        <a href="javascript:;" class="on-off-btn js-on-btn"></a>
        <%}else{%>
        <a href="javascript:;" class="on-off-btn off-btn js-on-btn"></a>
        <%}}%>
    </div>
    </div>
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

<script src="${BASE_PATH}/static/js/page/setting/pay-type.js?117c1a98fcce143ba97f75f48f0463a8"></script>
<#include "yqx/layout/footer.ftl">