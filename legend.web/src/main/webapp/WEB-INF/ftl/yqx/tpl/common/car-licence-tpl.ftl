<#-- 
ch 2016-04-19
公用车牌下拉框。

用到的页面，请在下面记录一下。
/shop/order/sell-good 新建销售单
/yqx/page/order/speedily.ftl  新建快修快保单
 -->



<style>
.yqx-dl-no-data {text-align: center;}
.yqx-dl-no-data > a{display:inline-block;margin:4px auto;background:#9fc527;padding: 5px;color:#fff;border-radius:3px;}
</style>
<script type="text/html" id="carLicenceTpl">
	<%if(templateData && templateData.length > 0){%>
	<ul class="yqx-downlist-content js-downlist-content" data-tpl-ref="car-licence-tpl">
		<%for(var i=0;i<templateData.length;i++){%>
		<%var item=templateData[i];%>
		<li class="js-downlist-item">
			<span title="<%=item.license%>" style="width:100%"><%=item.license%></span>
		</li>
		<%}%>
	</ul>
	<%}else{%>
		<div class="downlist-new-license yqx-dl-no-data"><a href="javascript:;">新建车牌</a></div>
	<%}%>
</script>
<script>
	$(function(){
		var $doc = $(document),
			elems = 'input[name="orderInfo.carLicense"],input[name="license"],input[name="plateNumber"]',
			elemsCopy = '';

        $doc
                .on('keyup', elems, function () {
                    elemsCopy = $(this).val();
                })
                .on('click', '.yqx-dl-no-data > a', function () {
                    var url = BASE_PATH + '/shop/customer/edit';
                    window.location.href = url + '?license=' + elemsCopy;
                    return false;
                });
	});
</script>