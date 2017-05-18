<#--
    工单打印
    zsy 2016-04-20

    用到的页面：
    yqx/page/order/common-detail.ftl

 -->
<style>

    .btn_group {
        height: 55px;
        line-height: 55px;
        text-align: center;
        background: #232e49;
        font-family: "Microsoft Yahei", "微软雅黑";
        font-size: 16px;
        color: #fff;
    }
    .t_middle {
        text-align: center;
        padding-top: 20px;
    }
    .link-btn {
        display: block;
        width: 150px;
        height: 30px;
        line-height: 30px;
        text-align: center;
        border: 1px solid #9fc527;
        border-radius: 5px;
        -webkit-border-radius: 5px;
        -moz-border-radius: 5px;
        color: #333;
        margin: 10px auto;
    }
    .display-none{
        display: none;
    }
</style>
<!-- 打印dialog start -->
<script type="text/html" id="print-dialog-tpl">
    <div class="tank" data-tpl-ref="order-print-tpl">
        <div class="btn_group">
            打印
        </div>
        <div class="t_middle">
            <#if SESSION_SHOP_WORKSHOP_STATUS == 1 || BPSHARE == 'true' || SESSION_SHOP_JOIN_STATUS == 1>
            <a href="javascript:;" class="link-btn js-common-order-print">综合维修单打印</a>
            <a href="javascript:;" class="link-btn js-simple-common-order-print">简化版综合维修单打印</a>
            </#if>
            <a href="javascript:;" class="link-btn js-common-print">派工单打印</a>
            <a href="javascript:;" class="link-btn js-simple-print">简化版派工单打印</a>
            <a href="javascript:;" class="link-btn js-bj-print display-none">报价单打印</a>
            <a href="javascript:;" class="link-btn js-trialrun-print">试车单打印</a>
        </div>
    </div>
</script>
<!-- 打印dialog end -->