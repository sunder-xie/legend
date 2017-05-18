<#include "layout/ng-header.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/lib/artTemplate/artTemplate.js"></script>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/page/buy/purchase-detail.css?1708a184823096ef04f368954bd95a38"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/page/banner.css?ce49e105e2f15bdeea782c45ef0a778c"/>
<div class="qxy_wrapper">
<#include "buy/buy_nav.ftl" >
	<div class="search_box clearfix">
		<div class="col1 fl">
			<a href="javascript:;" class="condition">今天</a>
			<span class="line">|</span>
			<a href="javascript:;" class="blue condition">最近一个月</a>
			<span class="line">|</span>
			<a href="javascript:;" class="red condition">半年</a>
			<span class="line">|</span>
			<a href="javascript:;" class="condition">一年</a>
		</div>
		<div class="col2 fr">
			<span>起止时间：</span>
			<input type="text" name="" value="" class="qxy_picker" placeholder="开始时间" max="#F{$dp.$D(\'d4312\')}" id="d4311">
			<span>-</span>
			<input type="text" name="" value="" class="qxy_picker" placeholder="结束时间" min="#F{$dp.$D(\'d4311\')}" id="d4312">
			<a href="javascript:;" title="" class="search_btn"></a>
		</div>
	</div>
	<div class="search_result">
		<dl class="flow-data">
		<#-- 明细列表 -->
		</dl>
	</div>
	<div class="qxy_page">
    	<div class="qxy_page_inner">
    		<#-- 列表分页 -->
    	</div>
    </div>
</div>
<script id="flow-tpl" type="text/html">
	<dt>
		<ul class="clearfix">
			<li class="col1">创建时间</li>
			<li class="col2">名称|交易号</li>
			<li class="col3">金额|明细</li>
		</ul>
	</dt>
	<%for(i=0;i<data.length;i++) {
	var v = data[i];
	%>
		<dd>
			<ul class="clearfix">
				<li class="col1">
				<%=$dateFormat(v.gmtCreate,'yyyy.MM.dd')%>
				<br/>
				<span class="color1"><%=$dateFormat(v.gmtCreate,'hh:mm')%></span>
				</li>
				<li class="col2">
					<p class="color2"><%=v.tradeDesc%></p>
					<p class="color1">订单编号：<%=v.orderSn%></p>
					<p class="color1">流水号：<%=v.piplineNumber%></p>
				</li>
				<li class="col3">
					<%if(v.way==1) {%>
                    	<span class="jia">-<%=v.amount%></span>
					<%} else {%>
						<span class="jian">+<%=v.amount%></span>
					<%}%>
				</li>
			</ul>
		</dd>
	<%}%>
</script>
<script type="text/javascript" src="${BASE_PATH}/resources/script/page/buy/purchase_detail.js?7d257621acfabb55f2437c4842d8edba"></script>
<script src="${BASE_PATH}/resources/js/lib/jquery.lunbo.js?0d26aceb12e57e42a097d447fb0528b6"></script>
<script type="text/javascript">
    $(function(){
        $(".banner").lunbo({
            interval:4500
        });
    });
</script>
<#include "layout/ng-footer.ftl" >