<#-- Created by sky on 16/11/24. -->


<nav class="hor-nav" role="navigation" data-ref-url="tpl/buy/nav">
    <input type="hidden" id="horNavActiveTab" value="${buyTab}">
    <ul class="nav-list clearfix">
        <li class="nav-item" data-tab="order_list">
            <a href="${BASE_PATH}/shop/buy">采购订单</a>
        </li>
        <li class="nav-item" data-tab="purchase_detail">
            <a href="${BASE_PATH}/shop/yunxiu/purchase/detail">采购金明细</a>
        </li>
        <li class="nav-item" data-tab="short_goods">
            <a href="${BASE_PATH}/shop/buy/short_goods">缺件配件</a>
        </li>
    </ul>
</nav>

<script>
    $(function () {
        var activeTab = $('#horNavActiveTab').val();
        $('.nav-item', '.hor-nav').each(function () {
            var $this = $(this),
                tab = $this.data('tab');
            if (activeTab == tab) {
                $this.addClass('active');
                return false;
            }
        });
    });
</script>

