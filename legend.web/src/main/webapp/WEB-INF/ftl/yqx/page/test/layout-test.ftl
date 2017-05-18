<#include "yqx/layout/header.ftl">
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css?a01cb370caa3a03199d37c3832d2451d"/>
<link type="text/css" rel="stylesheet" href="${BASE_PATH}/static/third-plugin/syntaxhighlighter_3.0.83/styles/shCoreDefault.css"/>
<style>
	.left-nav{position:fixed;left:0;top:70px;height:100%;width:130px;background:#f4f4f4;border-left:1px solid #ccc;}
	.left-nav li a{display:block;line-height:25px;border-bottom:1px solid #ccc;padding-left:10px;color:#111;font-weight:900;}
</style>
<div class="left-nav">
	<ul>
		<li><a href="javascript:;" class="yqx-link">group</a></li>
		<li><a href="javascript:;" class="yqx-link">表单元素</a></li>
		<li><a href="javascript:;" class="yqx-link">表格</a></li>
		<li><a href="javascript:;" class="yqx-link">弹框</a></li>
		<li><a href="javascript:;" class="yqx-link">下拉列表</a></li>
		<li><a href="javascript:;" class="yqx-link">日历</a></li>
		<li><a href="javascript:;" class="yqx-link">车型选择</a></li>
		<li><a href="javascript:;" class="yqx-link">自定义select</a></li>
		<li><a href="javascript:;" class="yqx-link">选择服务</a></li>
		<li><a href="javascript:;" class="yqx-link">验证配置</a></li>
		<li><a href="javascript:;" class="yqx-link">新增服务</a></li>
		<li><a href="javascript:;" class="yqx-link">图片上传</a></li>
		<li><a href="javascript:;" class="yqx-link">选择配件</a></li>
		<li><a href="javascript:;" class="yqx-link">新增配件</a></li>
		<li><a href="javascript:;" class="yqx-link">批量添加配件</a></li>
		<li><a href="javascript:;" class="yqx-link">维修工、洗车工多选</a></li>
		<li><a href="javascript:;" class="yqx-link">公用车牌模板</a></li>
        <li><a href="javascript:;" class="yqx-link">tab 样式</a></li>
        <li><a href="javascript:;" class="yqx-link">提示成功时间倒计时</a></li>
        <li class="js-audio-nav"><a href="javascript:;" class="yqx-link">消息提示</a></li>
	</ul>
</div>
<script>
$(function(){
	$('.left-nav a').click(function(){
		var index = $('.left-nav a').index($(this));
		var top = $('.yqx-wrapper > .yqx-group').eq(index).offset();
		$('html,body').animate({scrollTop:top.top - 80}, 300);
	});
});
</script>
<div class="yqx-wrapper">
	<!-- group start -->
    <div class="yqx-group">
    	<!-- group标题 start -->
        <div class="yqx-group-head">
        	group组
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">

		    <div class="yqx-group">
		    	<!-- group标题 start -->
		        <div class="yqx-group-head">
		        	group例子1
		        </div>
		        <!-- group标题 end -->
		        <!-- group内容 start -->
		        <div class="yqx-group-content">
		        	
		        </div>
		        <!-- group内容 end -->
		    </div>

			<pre class="brush: html;">
			    <div class="yqx-group">
			    	<!-- group标题 start -->
			        <div class="yqx-group-head">
			        	group例子1
			        </div>
			        <!-- group标题 end -->
			        <!-- group内容 start -->
			        <div class="yqx-group-content">
			        	
			        </div>
			        <!-- group内容 end -->
			    </div>
			</pre>

			<!-- group start -->
			<div class="yqx-group">
			    <!-- group标题 start -->
			    <div class="yqx-group-head">
			    	group例子2
			    	<i class="group-head-line"></i>
			    </div>
			    <!-- group标题 end -->
			    <!-- group内容 start -->
			    <div class="yqx-group-content">
			         
			    </div>
			    <!-- group内容 end -->
			</div>
			<!-- group end -->
			
			<pre class="brush: html;">
				<!-- group start -->
				<div class="yqx-group">
				    <!-- group标题 start -->
				    <div class="yqx-group-head">
				    	group例子2
				    	<i class="group-head-line"></i>
				    </div>
				    <!-- group标题 end -->
				    <!-- group内容 start -->
				    <div class="yqx-group-content">
				         
				    </div>
				    <!-- group内容 end -->
				</div>
				<!-- group end -->
			</pre>

			<!-- group start -->
			<div class="yqx-group">
			    <!-- group标题 start -->
			    <div class="yqx-group-head">
			    	group例子3 可收缩的group
			    	<i class="group-head-line"></i>
			    	<i class="group-arrow arrow-up js-arrow-up"></i>
			    </div>
			    <!-- group标题 end -->
			    <!-- group内容 start -->
			    <div class="yqx-group-content">
			         
			    </div>
			    <!-- group内容 end -->
			</div>
			<!-- group end -->
			
			<pre class="brush: html;">
				<!-- group start -->
				<div class="yqx-group">
				    <!-- group标题 start -->
				    <div class="yqx-group-head">
				    	group例子3 可收缩的group
				    	<i class="group-head-line"></i>
				    	<i class="group-arrow arrow-up js-arrow-up"></i>
				    </div>
				    <!-- group标题 end -->
				    <!-- group内容 start -->
				    <div class="yqx-group-content">
				         
				    </div>
				    <!-- group内容 end -->
				</div>
				<!-- group end -->
			</pre>
			<pre class="brush: js;">
				//group事件初始化
				seajs.use('group',function(gp){
					gp.init();
				});
			</pre>

			<!-- group start -->
			<div class="yqx-group">
			    <!-- group标题 start -->
			    <div class="yqx-group-head">
			    	group例子4 带按钮的group
			    	<i class="group-head-line"></i>
			    	<i class="group-arrow arrow-up js-arrow-up"></i>
			    	<a class="group-btn" href="javascript:;"><i class="icon-user-md"></i> 编辑app资料</a>
				    <a class="group-btn" href="javascript:;"><i class="glyphicon glyphicon-star"></i> 编辑门店资料</a>
			    </div>
			    <!-- group标题 end -->
			    <!-- group内容 start -->
			    <div class="yqx-group-content">
			         
			    </div>
			    <!-- group内容 end -->
			</div>
			<!-- group end -->
			
			<pre class="brush: html;">
				<!-- group start -->
				<div class="yqx-group">
				    <!-- group标题 start -->
				    <div class="yqx-group-head">
				    	group例子4 带按钮的group
				    	<i class="group-head-line"></i>
				    	<i class="group-arrow arrow-up js-arrow-up"></i>
				    	<a class="group-btn" href="javascript:;"><i class="icon-user-md"></i> 编辑app资料</a>
				    	<a class="group-btn" href="javascript:;"><i class="glyphicon glyphicon-star"></i> 编辑门店资料</a>
				    </div>
				    <!-- group标题 end -->
				    <!-- group内容 start -->
				    <div class="yqx-group-content">
				         
				    </div>
				    <!-- group内容 end -->
				</div>
				<!-- group end -->
			</pre>

        </div>
        <!-- group内容 end -->
    </div>
	<!-- group end -->

	<!-- 表单元素 start -->
	<div class="yqx-group">
	    <!-- group标题 start -->
	    <div class="yqx-group-head">
	        表单元素
	    </div>
	    <!-- group标题 end -->
	    <!-- group内容 start -->
	    <div class="yqx-group-content">
	    	<div>
	    		<div class="form-label">
	    			车牌
	    		</div>
	    		<div class="form-item">
		    		<input type="text" name="" class="yqx-input" value="" placeholder="">
		    	</div>
		        <div class="form-label form-label-must">
	    			客户名称
	    		</div>
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon" value="" placeholder="">
	         		<span class="fa icon-angle-down"></span>
	         	</div>

	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon" value="" placeholder="">
	         		<span class="fa icon-calendar"></span>
	         	</div>
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon" value="" placeholder="">
	         		<span class="fa">km</span>
	         	</div>
	    	</div>
	    	<pre class="brush: html;">
	    		<div class="form-label">
	    			车牌
	    		</div>
	    		<div class="form-item">
		    		<input type="text" name="" class="yqx-input" value="" placeholder="">
		    	</div>
		        <div class="form-label form-label-must">
	    			客户名称
	    		</div>
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon" value="" placeholder="">
	         		<span class="fa icon-angle-down"></span>
	         	</div>

	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon" value="" placeholder="">
	         		<span class="fa icon-calendar"></span>
	         	</div>
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon" value="" placeholder="">
	         		<span class="fa">km</span>
	         	</div>
	    	</pre>
	    	<br>
	    	<div>
		    	<div class="form-item">
		    		<input type="text" name="" class="yqx-input yqx-input-small" value="" placeholder="">
		    	</div>
		         
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
	         		<span class="fa icon-angle-down icon-small"></span>
	         	</div>

	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
	         		<span class="fa icon-calendar icon-small"></span>
	         	</div>
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
	         		<span class="fa icon-small">km</span>
	         	</div>
	    	</div>
	    	<pre class="brush: html;">
	    		<div class="form-item">
		    		<input type="text" name="" class="yqx-input yqx-input-small" value="" placeholder="">
		    	</div>
		         
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
	         		<span class="fa icon-angle-down icon-small"></span>
	         	</div>

	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
	         		<span class="fa icon-calendar icon-small"></span>
	         	</div>
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="">
	         		<span class="fa icon-small">km</span>
	         	</div>
	    	</pre>
	    	<br>
	    		<p>禁用的input</p>
	    		<br/>
	    	<div>
		    	<div class="form-item">
		    		<input type="text" name="" class="yqx-input yqx-input-small" value="" placeholder="" readonly>
		    	</div>
		         
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="" disabled>
	         		<span class="fa icon-angle-down icon-small"></span>
	         	</div>

	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="" disabled>
	         		<span class="fa icon-calendar icon-small"></span>
	         	</div>
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="" disabled>
	         		<span class="fa icon-small">km</span>
	         	</div>
	    	</div>
	    	<pre class="brush: html;">
	    		<div class="form-item">
		    		<input type="text" name="" class="yqx-input yqx-input-small" value="" placeholder="" readonly>
		    	</div>
		         
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="" disabled>
	         		<span class="fa icon-angle-down icon-small"></span>
	         	</div>

	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="" disabled>
	         		<span class="fa icon-calendar icon-small"></span>
	         	</div>
	         	<div class="form-item">
	         		<input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small" value="" placeholder="" disabled>
	         		<span class="fa icon-small">km</span>
	         	</div>
	    	</pre>
	    	<br>
			<div>
				<div class="form-label">
					文本域
				</div>
				<div class="form-item">
					<textarea class="yqx-textarea" name="" id="" cols="100" rows="3"></textarea>
				</div>
			</div>
			<pre class="brush: html;">
				<div class="form-label">
					文本域
				</div>
				<div class="form-item">
					<textarea class="yqx-textarea" name="" id="" cols="100" rows="3"></textarea>
				</div>
			</pre>
	    	<br/>
	    	<div>
				<div class="form-label">
					纯文字:
				</div>
				<div class="form-item">
					<div class="yqx-text">
						这是一段纯文字
					</div>
				</div>
			</div>
			<pre class="brush: html;">
				<div class="form-label">
					纯文字:
				</div>
				<div class="form-item">
					<div class="yqx-text">
						这是一段纯文字
					</div>
				</div>
			</pre>
	    	<br/>
	    	<div>
	    		<button class="yqx-btn yqx-btn-1">按钮1</button>
	    		<a href="javascript:;" class="yqx-btn yqx-btn-2">按钮2</a>
	    		<button class="yqx-btn yqx-btn-3">按钮3</button>
	    		<button class="yqx-btn yqx-btn-1 yqx-btn-small">按钮1</button>
	    		<button class="yqx-btn yqx-btn-2 yqx-btn-small">按钮2</button>
	    		<button class="yqx-btn yqx-btn-3 yqx-btn-small">按钮3</button>
	    	</div>
	    	<pre class="brush: html;">
	    		<button class="yqx-btn yqx-btn-1">按钮1</button>
	    		<a href="javascript:;"  class="yqx-btn yqx-btn-2">按钮2</a>
	    		<button class="yqx-btn yqx-btn-3">按钮3</button>
	    		<button class="yqx-btn yqx-btn-1 yqx-btn-small">按钮1</button>
	    		<button class="yqx-btn yqx-btn-2 yqx-btn-small">按钮2</button>
	    		<button class="yqx-btn yqx-btn-3 yqx-btn-small">按钮3</button>
	    	</pre>
	    </div>
	    <!-- group内容 end -->
	</div>
	<!-- 表单元素 end -->

	<!-- 表格 start -->
	<div class="yqx-group">
	    <!-- group标题 start -->
	    <div class="yqx-group-head">
	        表格调用
	    </div>
	    <!-- group标题 end -->
	    <!-- group内容 start -->
	    <div class="yqx-group-content" style="background:#fff;">
	    	<!-- 查询表单区域 start -->
	    	<div class="row" style="margin-bottom:10px;">
	    		<div class="form-label">
	    			字段:
	    		</div>
	    		<div class="form-item">
	    			<input class="yqx-input" />
	    		</div>
	         	<button class="yqx-btn yqx-btn-2">搜索</button>
	    	</div>
	    	<!-- 查询表单区域 end -->

	    	<!-- 表格容器 start -->
	        <div id="tableTest"></div>
	        <!-- 表格容器 end -->

	        <!-- 分页容器 start -->
			<div class="yqx-page" id="pagingTest"></div>
			<!-- 分页容器 end -->
			
			<!-- 表格数据模板 start -->
			<script type="text/template" id="tableTestTpl">
		    	<table class="yqx-table" id="tableTest">
		    		<thead>
		    			<tr>
		    				<th>表头</th>
		    				<th>表头</th>
		    				<th>表头</th>
		    				<th>表头</th>
		    				<th>表头</th>
		    			</tr>
		    		</thead>
			    	<%for(var i=0;i<7;i++){%>
			    	<tr>
						<td>单元格</td>
						<td>单元格</td>
						<td>单元格</td>
						<td>单元格</td>
						<td>单元格</td>
					</tr>
					<%}%>
				</table>
		    </script>
		    <!-- 表格数据模板 end -->
		    <pre class="brush: html;">
				<!-- 查询表单区域 start -->
		    	<div class="row" style="margin-bottom:10px;">
		    		<div class="form-label">
		    			字段:
		    		</div>
		    		<div class="form-item">
		    			<input class="yqx-input" />
		    		</div>
		         	<button class="yqx-btn yqx-btn-2">搜索</button>
		    	</div>
		    	<!-- 查询表单区域 end -->

		    	<!-- 表格容器 start -->
		        <div id="tableTest"></div>
		        <!-- 表格容器 end -->

		        <!-- 分页容器 start -->
				<div class="yqx-page" id="pagingTest"></div>
				<!-- 分页容器 end -->
				
				<!-- 表格数据模板 start -->
				<script type="text/template" id="tableTestTpl">
			    	<table class="yqx-table" id="tableTest">
			    		<thead>
			    			<tr>
			    				<th>表头</th>
			    				<th>表头</th>
			    				<th>表头</th>
			    				<th>表头</th>
			    				<th>表头</th>
			    			</tr>
			    		</thead>
				    	<%for(var i=0;i<7;i++){%>
				    	<tr>
							<td>单元格</td>
							<td>单元格</td>
							<td>单元格</td>
							<td>单元格</td>
							<td>单元格</td>
						</tr>
						<%}%>
					</table>
			    </script>
			</pre>
			<pre class="brush: js;">
				//表格模块初始化
				seajs.use('table',function(tb){
					tb.init({
						//表格数据url，必需
			            url: BASE_PATH + '/shop/settlement/list/list?page=1&search_orderStatuss=DDWC&search_payStatus=0',
			            //表格数据目标填充id，必需
			            fillid: 'tableTest',
			            //分页容器id，必需
			            pageid: 'pagingTest',
			            //表格模板id，必需
			            tplid: 'tableTestTpl',
			            //扩展参数,可选
			            data: {},
			            //关联查询表单id，可选
			            formid: null,
			            //渲染表格数据完后的回调方法,可选
			            callback : null
					});
				});
			</pre>
	    </div>
	    <!-- group内容 end -->
	</div>
	<!-- 表单 end -->

	<!-- 弹框 start -->
	<div class="yqx-group">
	    <!-- group标题 start -->
	    <div class="yqx-group-head">
	        弹框例子
	    </div>
	    <!-- group标题 end -->
	    <!-- group内容 start -->
	    <div class="yqx-group-content">
	         <button type="button" class="yqx-btn yqx-btn-1 js-btn-load">加载框</button>

	         <button type="button" class="yqx-btn yqx-btn-2 js-btn-confirm">询问框</button>

	         <button type="button" class="yqx-btn yqx-btn-1 js-btn-warn">警告</button>

	         <button type="button" class="yqx-btn yqx-btn-1 js-btn-success">成功</button>

	         <button type="button" class="yqx-btn yqx-btn-1 js-btn-fail">失败</button>
	         <pre class="brush: js;">
                //加载框
                $(document).on('click','.js-btn-load',function(){
                    seajs.use('dialog',function(dg){
                        dg.load({
                            time: 3*1000    //加载三秒
                        });
                    });
                });

                //确认框
                $(document).on('click','.js-btn-confirm',function(){
                    seajs.use('dialog',function(dg){
                        dg.confirm('我是确认框',function(){
                            alert('我是确定回调');
                        },function(){
                            alert('我是取消回调');
                        });
                    });
                });

                //信息框
                $(document).on('click','.js-btn-warn',function(){
                    seajs.use('dialog',function(dg){
                        dg.warn('这是警告框',function(){
                            //回调方法
                            alert('我是回调');
                        });
                    });
                });

                //信息框
                $(document).on('click','.js-btn-success',function(){
                    seajs.use('dialog',function(dg){
                        dg.success('这是成功框',function(){
                            //回调方法
                            alert('我是回调');
                        });
                    });
                });

                //信息框
                $(document).on('click','.js-btn-fail',function(){
                    seajs.use('dialog',function(dg){
                        dg.fail('这是失败框',function(){
                            //回调方法
                            alert('我是回调');
                        });
                    });
                });
	         </pre>
	    </div>
	    <!-- group内容 end -->
	</div>
	<!-- 弹框 end -->

    <!-- 下拉列表 start -->
    <div class="yqx-group">
        <!-- group标题 start -->
        <div class="yqx-group-head">
            下拉列表例子
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <style>
                .w260 {
                    width: 260px!important;
                }
            </style>
            <!-- 简单的下拉菜单 start -->
            <div class="yqx-downlist-wrap">
                <input type="text" class="yqx-input js-downlist"
                        placeholder="下拉菜单示例1"/>
            </div>
            <input type="text" class="yqx-input w260" id="carModel"
                   placeholder="下拉菜单示例2"/>
            <div class="yqx-downlist-wrap">
            	<input type="text" class="yqx-input js-downlist3"
						placeholder="下拉菜单示例3"/>
			</div>
            <script id="downlistTpl" type="text/html">
                <% if (templateData && templateData.length) { %>
                <ul class="yqx-downlist-content js-downlist-content">
                    <%
                    for(var i = 0; i < templateData.length; i++) {
                    var item = templateData[i];
                    %>
                    <li class="w260 js-downlist-item">
                        <% if (item.importInfo!=null&&item.importInfo!="") { %>
                        <% var title = item.brand + "&nbsp;&nbsp;(" + item.importInfo + ")" + item.model %>
                        <%}else{%>
                        <% var title = item.brand + "&nbsp;&nbsp;" + item.model %>
                        <%}%>
                        <span class="w260" title="<%= title %>"><%= title %></span>
                    </li>
                    <%}%>
                </ul>
                <%} else { %>
                <p class="yqx-dl-no-data">抱歉，未匹配到相应车型<br/>请手动选择车型</p>
                <% } %>
            </script>
            <!-- 简单的下拉菜单 end -->
            <pre class="brush: js;">
				// 示例1 配置
                seajs.use('downlist', function(dl) {
                    dl.init({
                        url: BASE_PATH + '/shop/car_category/car_model'，
                        showKey: 'series',
                        tplId: 'downlistTpl'
                    });
                });
            </pre>
            <pre class="brush: js;">
                // 示例2 配置
                dl.init({
                    url: BASE_PATH + '/shop/car_category/car_model',
                    dom: '#carModel',
                    showKey: 'brand,importInfo,model',
                    hiddenKey: 'brandId,brand,seriesId,series,modelId,model,importInfo',
                    hiddenSelector: 'carBrandId,carBrand,carSeriesId,carSeries,carModelId,carModel,importInfo',
                    tplColsWidth: [100, 50, 180],
                    hasTitle: false,
                    hasInput: false,
                    autoFill: true
                });
            </pre>
            <pre class="brush: js;">
                // 示例3 配置
                seajs.use('downlist', function(dl) {
                    dl.init({
                        url: BASE_PATH + '/shop/car_category/car_model',
                        showKey: 'model',
                        dom: '.js-downlist3',
                        hasTitle: false,
                        hasInput: false,
                        autoFill: true
                    });
                });
            </pre>
            <pre class="brush: js">
				/*
				 * 关键属性列表：
				 * 参考DownList._default，必填项为url, showKey
				 * template :（优先级依次递减）
				 * 1.  获取input的templateId
				 * 2.  默认会有一个template(如果以上都不存在，就会渲染默认template)
				 */
				/* 2016-04-14 modify
                 * 1、删除 showSelector && (showSelector = showKey)
                 * 2、增强 showKey 与 showSelector 配合的赋值功能
                 * 3、在作用域上添加 class="yqx-downlist-wrap"
                 * 4、必填项：dom、url、showKey
                 * ************************************************
				 * showKey与showSelector关系（赋值功能增强）
 				 * 1、showKey.length && !showSelector.length ? '所有showKey按顺序组合成字符串赋值给当前输入框' : '进行下一步'
 				 * 2、showKey.length == showSelector.length ? 'showKey按顺序赋值给showSelector' : '进行下一步'
 				 * 3、showSelector.length == 1 ? '所有showKey按顺序组合成赋值给当前输入框' : '警告'
				 * update 2016-04-17
				 * # 修复undefined !== null 的bug
				 * # 添加内容改变清空所有数据功能（blur事件）
				 * wry update 2016-04-18
				 * # 下拉表头width为null的时候设置其百分比宽度
				 * # scope 设置提醒
				 */

                /**** ?参数说明 ****/

                // 作用域
                scope: 'yqx-downlist-wrap',
                // 绑定元素
                dom: '.js-downlist',    // 建议手动覆盖

                /* 必填项 start */
                //* ajax请求路径（类型：字符串）
                url: "",
                //* 回调函数显示参数的key（类型：以“,”隔开的字符串）
                showKey: "",

                // 回调函数显示参数的选择器（与 showKey 配合使用，类型：以“,”隔开的字符串/数组）
                showSelector: "",
                // 回调函数隐藏参数key（类型：以“,”隔开的字符串）
                hiddenKey: "",
                // 回调函数隐藏参数的选择器（与 hiddenKey 配合使用，若为空，hiddenSelector = hiddenKey，类型：以“,”隔开的字符串/数组）
                hiddenSelector: "",
                // scope作用局内的局部检索ajax参数（以[name=*]获取值，类型：以“,”隔开的字符串）
                searchKey: "",
                // 全局检索ajax参数（类型：对象）
                globalData: null,
                // 选中一项列表后的回调函数（类型：方法）
                callbackFn: null,
                // 清空选中项的回调函数（类型：方法）
                clearCallbackFn: null,
                // 模板填充（类型：字符串）
                tplId: null,
                // 模板列（设置模板的每一列的显示内容与 tplId 互斥，类型：对象）
                tplCols: null,
                // 模板列宽（设置模板每一列的宽度，与 tplCols 配合使用，类型：数组）
                tplColsWidth: null,
                // 是否支持清空（设置后不能将notClearInput设置为true）
                isClear: false,
                // 模板是否有title
                hasTitle: true,
                // 模板是否有输入框（类型：布尔）
                hasInput: true,
                // ajax延迟时间（类型：数字）
                delay: 200,
                // 当数据只有一条时，自动选中第一条（类型：布尔）
                autoFill: false
			</pre>
        </div>
    </div>
    <!-- 下拉列表 end -->

    <!-- 日历 start -->
	<div class="yqx-group">
	    <!-- group标题 start -->
	    <div class="yqx-group-head">
	       	日历
	    </div>
	    <!-- group标题 end -->
	    <!-- group内容 start -->
	    <div class="yqx-group-content">
            <div class="form-label">
                开始结束日期
            </div>
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-icon" id="startDate"/>
                <span class="fa icon-calendar"></span>
            </div>
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-icon" id="endDate"/>
                <span class="fa icon-calendar"></span>
            </div>

            <div class="form-label">
                开始结束日期
            </div>
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-icon" id="start1"/>
                <span class="fa icon-calendar"></span>
            </div>
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-icon" id="end1"/>
                <span class="fa icon-calendar"></span>
            </div>

            <div class="form-label">
                普通日期
            </div>
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-icon datepicker"/>
                <span class="fa icon-calendar"></span>
            </div>

            <div class="form-label">
                普通已配置日期
            </div>
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-icon time"/>
                <span class="fa icon-calendar"></span>
            </div>
			<div class="form-label">
                普通双日历日期
            </div>
            <div class="form-item">
                <input type="text" class="yqx-input yqx-input-icon time1"/>
                <span class="fa icon-calendar"></span>
            </div>
	    </div>

        <pre class="brush: js">
            /***** 开始结束日期的配置项****
             * // 开始日期框的ID
             * start: 'startDate',
             * // 结束日期框的ID
             * end: 'endDate',
             * // 开始日期配置
             * startSettings: '',
             * // 结束日期配置
             * endSettings: '',
             * // 是否联动
             * linkage: true
             * My97配置地址：http://www.my97.net/dp/demo/index.htm
             *****************************/
            seajs.use('date', function(dp) {
                // 开始结束日期
			    dp.dpStartEnd();

                // 时分秒日历
                dp.dpStartEnd({
                    start: 'start1',
                    end: 'end1',
                    startSettings: {
                        dateFmt: 'yyyy-MM-dd HH:mm:ss',
                        maxDate: '#F{$dp.$D(\'end1\')||\'%y-%M-%d %H:%m:%s\'}'
                    },
                    endSettings: {
                        dateFmt: 'yyyy-MM-dd HH:mm:ss',
                        minDate: '#F{$dp.$D(\'start1\')}',
                        maxDate: '%y-%M-%d %H:%m:%s'
                    }
                });

                // 未配置的日期
                dp.datePicker();

                // 配置的日期
                dp.datePicker('.time', {
                    maxDate: '%y-%M-%d'
                });

			    // 双日历
				dp.datePicker('.time1', {
				    doubleCalendar: true
				})
            });
        </pre>
	    <!-- group内容 end -->
	</div>
	<!-- 日历 end -->

	<!-- 车型选择 start -->
	<div class="yqx-group">
	    <!-- group标题 start -->
	    <div class="yqx-group-head">
	       	车型选择
	    </div>
	    <!-- group标题 end -->
	    <!-- group内容 start -->
	    <div class="yqx-group-content">
	    	<div>
				<a href="javascript:;" class="yqx-btn yqx-btn-1 js-car-type">选择按钮</a>
	    	</div>
	    	<div id="callbackData" style="padding:10px;color:#111;word-break:break-all; ">
	    		
	    	</div>
	        <pre class="brush: js;">
	         	//车型选择,需要引入 "yqx/tpl/common/car-type-tpl.ftl"
				carTypeInit({
					dom: '.js-car-type',
					callback: function(data){
						//回调方法
					}
				});
	        </pre>
	    </div>
	    <!-- group内容 end -->
	</div>
	<!-- 车型选择 end -->

	<!-- 自定义select start -->
	<div class="yqx-group">
	    <!-- group标题 start -->
	    <div class="yqx-group-head">
	       	自定义select
	    </div>
	    <!-- group标题 end -->
	    <!-- group内容 start -->
	    <div class="yqx-group-content">
	    	<div>
	    		<div class="form-label">
		    		远程获取数据
		    	</div>
		        <div class="form-item">
				    <input type="text" name="" class="yqx-input yqx-input-icon js-select" value="" placeholder="">
				    <span class="fa icon-angle-down"></span>
				</div>
				<div class="form-label">
		    		js中静态数据
		    	</div>
		        <div class="form-item">
				    <input type="text" name="" class="yqx-input yqx-input-icon js-select2" value="" placeholder="">
				    <span class="fa icon-angle-down"></span>
				</div>
                <div class="form-label">
                    清除选项
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon js-select3" value="" placeholder="">
                    <span class="fa icon-angle-down"></span>
                </div>
	    	</div>
	        <pre class="brush: js;">
	         	//异步获取下拉列表数据
				seajs.use('select', function(st){
					st.init({
						dom: '.js-select',
			            url: BASE_PATH + '/shop/manager/get_manager',
			            showKey: "id",
			            showValue: "name"
					});
				});
				//静态下拉列表数据
				seajs.use('select', function(st){
					st.init({
						dom: '.js-select2',
						showKey: "key",
			            showValue: "value",
			            data: [{
			            	key: 'xxx',
			            	value: 'yyy'
			            },{
			            	key: 'aaa',
			            	value: 'bbb'
			            }]
					});
				});
                // 可清空的下拉列表数据
                seajs.use('select', function(st){
					st.init({
						dom: '.js-select3',
						showKey: "key",
			            showValue: "value",
                        isClear: true,
			            data: [{
			            	key: 'xxx',
			            	value: 'yyy'
			            },{
			            	key: 'aaa',
			            	value: 'bbb'
			            }]
					});
				});
	        </pre>
	    </div>
	    <!-- group内容 end -->
	</div>

	<!-- 选择服务 start -->
	<div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            选择服务
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div>
                <div class="form-label">
					选择服务
                </div>
                <button class="yqx-btn yqx-btn-3" id="service-get">选择服务</button>
            </div>
            <div id="callbackData" style="padding:10px;color:#111;word-break:break-all; ">

            </div>
	        <pre class="brush: js;">
	         	getService({
					// 点击按钮的选择器
					dom: '#material-get'
					// 回调函数，处理选择的数据
					callback: fn,
					// ajax, 选择传入的参数, 可选
					data: {
						type: 1
					}
				});
	        </pre>
        </div>
        <!-- group内容 end -->

	</div>
	<!-- 选择服务 end -->

	<!-- 验证配证和调用 start -->
	<div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            验证配置和调用
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
        	<div style="padding: 10px;line-height: 20px;">
        		内置验证规则有：<br>
        		required（必填）、maxLength（字符串最大长度）、minLength（字符串最小长度）、maxValue（数字最大值）、minValue（数字最小值）、number（正数）、integer（正整数）、email（邮箱）、zip（邮编）、tel（座机电话）、phone（手机号码）、qq（QQ）、vin(vin码)、licence（车牌）、floating（最多保留两位小数）
        	</div>
            <div>
                <div class="form-label">
					不能为空和最大长度限制
                </div>
                <div class="form-item">
                	<input type="text" name="" class="yqx-input" value="" placeholder="" data-v-type="required | maxLength:3" data-label="xxx">
                </div>
                <div class="form-label">
					字符串最小长度限制
                </div>
                <div class="form-item">
                	<input type="text" name="" class="yqx-input" value="" placeholder="" data-v-type="minLength:3" data-label="xxx">
                </div>
                <div class="form-label">
					vin码
                </div>
                <div class="form-item">
                	<input type="text" name="" class="yqx-input" value="" placeholder="" data-v-type="vin" data-label="vin码">
                </div>
                <div class="form-label">
					车牌
                </div>
                <div class="form-item">
                	<input type="text" name="" class="yqx-input" value="" placeholder="" data-v-type="licence" data-label="车牌">
                </div>
            </div>
            <pre class="brush: js;">
	         	//验证初始化
				seajs.use('check', function(ck){
					//可以传一个作用域,如果不传默认的是整个body
					ck.init();
				});
	        </pre>
            <pre class="brush: html;">
            	不能为空和最大长度限制
            	<input type="text" name="" class="yqx-input" value="" placeholder="" data-v-type="required | maxLength:3" data-label="xxx">
            	字符串最小长度限制
            	<input type="text" name="" class="yqx-input" value="" placeholder="" data-v-type="minLength:3" data-label="xxx">
            	vin码
            	<input type="text" name="" class="yqx-input" value="" placeholder="" data-v-type="vin" data-label="vin码">
            	车牌
            	<input type="text" name="" class="yqx-input" value="" placeholder="" data-v-type="licence" data-label="车牌">
            </pre>
	        
        </div>
        <!-- group内容 end -->

	</div>
	<!-- 验证配置和调用 end -->

	<!-- 新增服务 start -->
	<div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            新增服务
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div>
                <button class="yqx-btn yqx-btn-3" id="serviceAddBtn">新增服务</button>
            </div>
            <div id="serviceCallbackData" style="padding:10px;color:#111;word-break:break-all; ">

            </div>
	        <pre class="brush: js;">
	         	//新增服务
	         	//需要引入 include "yqx/tpl/common/service-add-tpl.ftl"
				addServiceInit({
					dom: '#serviceAddBtn',
					callback: function(json){
						$('#serviceCallbackData').html(JSON.stringify(json));
					}
				});
	        </pre>
        </div>
        <!-- group内容 end -->

	</div>
	<!-- 新增服务 end -->

	<!-- 图片上传 start -->
	<div class="yqx-group">
	    <!-- group标题 start -->
	    <div class="yqx-group-head">
	       	图片上传
	    </div>
	    <!-- group标题 end -->
	    <!-- group内容 start -->
	    <div class="yqx-group-content">
	    	<div>
				<input type="file" id="fileBtn"/>
	    	</div>
	    	<div id="fileCallbackData" style="padding:10px;color:#111;word-break:break-all; ">
	    		
	    	</div>
	        <pre class="brush: js;">				
	        	//图片上传
				seajs.use('upload',function(ud){
					ud.init({
						dom: '#fileBtn',
						url: BASE_PATH + '/index/oss/upload_img',//注：根据业务场景url自行替换
						callback: function(json){
							$('#fileCallbackData').html(JSON.stringify(json));
						}
					});
				});
	        </pre>
	    </div>
	    <!-- group内容 end -->
	</div>
	<!-- 图片上传 end -->

	<!-- 选择配件 start -->
	<div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            选择配件
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div>
                <button class="yqx-btn yqx-btn-3" id="goodsChosenBtn">选择配件</button>
            </div>
            <div id="chosenGoodsData" style="padding:10px;color:#111;word-break:break-all; ">

            </div>
	        <pre class="brush: js;">
	         	//选择配件
	         	//需要引入 include "yqx/tpl/common/goods-add-tpl.ftl"
	         	//注意：这里每选择一条就会执行一次回调
				addGoodsInit({
					dom: '#serviceAddBtn',
					callback: function(json){
						$('#chosenGoodsData').html(JSON.stringify(json));
					}
				});
	        </pre>
        </div>
        <!-- group内容 end -->
	</div>
	<!-- 选择配件 end -->
	
	<!-- 新增配件 start -->
	<div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            新增配件
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div>
                <button class="yqx-btn yqx-btn-3" id="goodsAddBtn">新增配件</button>
            </div>
            <div id="goodsAddData" style="padding:10px;color:#111;word-break:break-all; ">

            </div>
	        <pre class="brush: js;">
	         	//新增配件
	         	//需要引入 "yqx/tpl/order/new-part-tpl.ftl"
	         	//需要引入 "yqx/tpl/common/goods-type-tpl.ftl"
				newPart({
					dom: '#goodsAddBtn',
					callback: function(json){
						$('#goodsAddData').html(JSON.stringify(json));
					}
				});
	        </pre>
        </div>
        <!-- group内容 end -->
	</div>
	<!-- 新增配件 end -->

	<!-- 批量添加物料 start -->
	<div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            批量添加物料
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div>
                <button class="yqx-btn yqx-btn-3" id="batchGoodsAddBtn">批量添加物料</button>
            </div>
            <div id="batchGoodsAddData" style="padding:10px;color:#111;word-break:break-all; ">

            </div>
	        <pre class="brush: js;">
	         	//批量添加物料
	         	//需要引入 "yqx/tpl/order/batch-add-part-tpl.ftl"
				batchAddPart({
					dom: '#batchGoodsAddBtn',
					callback: function(json){
						$('#batchGoodsAddData').html(JSON.stringify(json));
					}
				});
	        </pre>
        </div>
        <!-- group内容 end -->
	</div>
	<!-- 批量添加物料 end -->

	<!-- 维修工、洗车工 start -->
	<div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            维修工、洗车工
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div>
            	<div class="form-label form-label-must">
				    维修工左
				</div>
                <div class="form-item">
				    <input type="text" name="" class="yqx-input yqx-input-icon workerNames" value="" placeholder="">
				    <span class="fa icon-angle-down"></span>
				    <input type="hidden" class="workerIds"/>
				</div>
				<div class="form-label form-label-must">
				    维修工左
				</div>
                <div class="form-item">
				    <input type="text" name="" class="yqx-input yqx-input-icon workerNames" value="" placeholder="" >
				    <span class="fa icon-angle-down"></span>
				    <input type="hidden" class="workerIds"/>
				</div>
				<div class="form-label form-label-must">
				    维修工右
				</div>
                <div class="form-item">
				    <input type="text" name="" class="yqx-input yqx-input-icon workerNames2" value="" placeholder="" >
				    <span class="fa icon-angle-down"></span>
				    <input type="hidden" class="workerIds2"/>
				</div>
            </div>
            <pre class="brush: html">
            	<div class="form-item">
				    <input type="text" name="" class="yqx-input yqx-input-icon" value="" placeholder="" id="workerNames">
				    <span class="fa icon-angle-down"></span>
				    <!-- 需要有一个存放维修工id的隐藏域 -->
				    <input type="hidden" id="workerIds"/>
				</div>
            </pre>
	        <pre class="brush: js;">
	        	//居左对齐
	         	//维修工、洗车工
		     	//需要引入 "yqx/tpl/common/worker-multiple-tpl.ftl"
				workerInit({
					dom: '.workerNames',
					hiddenDom: '.workerIds',
					url: BASE_PATH + '/shop/manager/get_manager'
					//可选，默认700px;
					width: '370px',
					//可选，支持（left,right）默认left;
					align: 'left',
				});
				
				//居右对齐
				workerInit({
					dom: '.workerNames2',
					hiddenDom: '.workerIds2',
					width: '400px',
					align: 'right',
					url: BASE_PATH + '/shop/manager/get_manager'
				});
	        </pre>
        </div>
        <!-- group内容 end -->
	</div>
	<!-- 维修工、洗车工 end -->

	<!-- 公用车牌模板 start -->
	<div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            公用车牌模板
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div>
                <div class="form-label">车牌</div>
                <div class="form-item">
                	<input type="text" name="orderInfo.carLicense" class="yqx-input" value="" placeholder="">
                </div>
            </div>
	        <pre class="brush: js;">
	         	//公用车牌模板
	         	//需要引入 include "yqx/tpl/common/car-licence-tpl.ftl"
				//初始化车牌下拉框
			    dl.init({
			        url: BASE_PATH + '/shop/customer/search/mobile',
			        searchKey: 'com_license',
			        tplId: 'carLicenceTpl',
			        showKey: 'license',
			        dom: 'input[name="orderInfo.carLicense"]',
			        hasInput: false,
			        callbackFn: function (obj, item) {
			            //回调函数
			        }
			    });
	        </pre>
        </div>
        <!-- group内容 end -->
	</div>
	<!-- 公用车牌模板 end -->
    <!-- tab 样式 start -->
    <div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            tab 样式
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div style="height: 38px;background: #fff;border-bottom: 1px solid #ddd;">
                <div class="tab-item">啊啊</div>
				<div class="tab-item current-item">哦哦</div>
            </div>
	        <pre class="brush: js;">
				&lt;link rel="stylesheet" href="${BASE_PATH}/static/css/common/order/order-common.css"/&gt;
				&lt;style&gt;
            	&lt;div style="height: 38px;background: #fff;border-bottom: 1px solid #ddd;"&gt;
                    &lt;div class="tab-item"&gt;啊啊&lt;/div&gt;
                    &lt;div class="tab-item current-item"&gt;哦哦&lt;/div&gt;
                &lt;/div&gt;
	        </pre>
        </div>
        <!-- group内容 end -->
    </div>
    <!-- tab 样式 end -->
    <!-- tips start -->
    <div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            tips
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div style="height: 38px;background: #fff;border-bottom: 1px solid #ddd;">
                <div class="js-show-tips ellipsis-1" style="width: 90px;">
					啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊</div>
            </div>
	        <pre class="brush: js;">
				seajs.use('dialog', function(dg) {
					dg.titleInit();
				});
	        </pre>
        </div>
        <!-- group内容 end -->
    </div>
    <!-- tips end -->
	 <!-- time to zero start -->
    <div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            提示时间倒数
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div>
                <button class="yqx-btn yqx-btn-3 time-to-zero">Click</button>
            </div>
	        <pre class="brush: js;">
				seajs.use('dialog', function(dg) {
					dg.success(['将于', 3, '秒后结束']);
				});
	        </pre>
        </div>
        <!-- group内容 end -->
    </div>
    <!-- time to zero end -->

    <!-- 消息提示 start -->
    <div class="yqx-group" >
        <!-- group标题 start -->
        <div class="yqx-group-head">
            提示时间倒数
        </div>
        <!-- group标题 end -->
        <!-- group内容 start -->
        <div class="yqx-group-content">
            <div>
                <button class="yqx-btn yqx-btn-3 js-audio">点击音频提示</button>
            </div>
	        <pre class="brush: js;">
				seajs.use('audio', function(au) {
                    // 这里的音频是一个示例，要不直接复制使用
                    var audio = au.init('http://5.1015600.com/download/ring/000/100/b03c68b73323f085c608552607d55317.mp3');
                    $('.js-audio-nav, .js-audio').on('click', function() {
                        audio.play();
                    });
                });
	        </pre>
        </div>
        <!-- group内容 end -->
    </div>
    <!-- 消息提示 end -->
</div>


<script type="text/javascript" src="${BASE_PATH}/static/third-plugin/syntaxhighlighter_3.0.83/scripts/shCore.js"></script>
<script type="text/javascript" src="${BASE_PATH}/static/third-plugin/syntaxhighlighter_3.0.83/scripts/shBrushJScript.js"></script>
<script type="text/javascript" src="${BASE_PATH}/static/third-plugin/syntaxhighlighter_3.0.83/scripts/shAutoloader.js"></script>

<#include "yqx/tpl/common/get-service-tpl.ftl">
<script>
	// material  物料选择
    var gm = getService({
        dom: '#service-get'
    });
</script>

<#include "yqx/tpl/common/car-type-tpl.ftl">
<#include "yqx/tpl/common/service-add-tpl.ftl">
<#include "yqx/tpl/common/goods-add-tpl.ftl">
<#include "yqx/tpl/order/new-part-tpl.ftl">
<#include "yqx/tpl/common/goods-type-tpl.ftl">
<#include "yqx/tpl/order/batch-add-part-tpl.ftl">
<#include "yqx/tpl/order/worker-multiple-tpl.ftl">	
<#include "yqx/tpl/common/car-licence-tpl.ftl">

<script>
	$(function(){
		var base = '${BASE_PATH}/static/third-plugin/syntaxhighlighter_3.0.83/';
		SyntaxHighlighter.autoloader(
            ['js','jscript','javascript',base+'scripts/shBrushJScript.js'],
            ['bash','shell',base+'scripts/shBrushBash.js'],
            ['css',base+'scripts/shBrushCss.js'],
            ['xml','html',base+'scripts/shBrushXml.js'],
            ['sql',base+'scripts/shBrushSql.js'],
            ['php',base+'scripts/shBrushPhp.js']
        );
        SyntaxHighlighter.config.space = ' ';
        SyntaxHighlighter.defaults['title'] = '相关代码';
        SyntaxHighlighter.defaults['toolbar'] = false;
        SyntaxHighlighter.defaults['quick-code'] = true;
        SyntaxHighlighter.defaults['auto-links'] = false;
        // SyntaxHighlighter.defaults['tab-size'] = 0;
		SyntaxHighlighter.all();

		//group事件初始化
		seajs.use('group',function(gp){
			gp.init();
		});

		//表格
		seajs.use('table',function(tb){
			tb.init({
				//表格数据url，必需
	            url: BASE_PATH + '/shop/settlement/list/list?page=1&search_orderStatuss=DDWC&search_payStatus=0',
	            //表格数据目标填充id，必需
	            fillid: 'tableTest',
	            //分页容器id，必需
	            pageid: 'pagingTest',
	            //表格模板id，必需
	            tplid: 'tableTestTpl',
	            //扩展参数,可选
	            data: {},
	            //关联查询表单id，可选
	            formid: null,
	            //渲染表格数据完后的回调方法,可选
	            callback : null
			});
		});


        /* 下拉列表 start */
        seajs.use('downlist', function(dl) {
            dl.init({
                url: BASE_PATH + '/shop/car_category/car_model',
                showKey: 'series',
                tplId: 'downlistTpl'
            });

            dl.init({
                url: BASE_PATH + '/shop/car_category/car_model',
                dom: '#carModel',
                showKey: 'brand,importInfo,model',
                hiddenKey: 'brandId,brand,seriesId,series,modelId,model,importInfo',
                hiddenSelector: 'carBrandId,carBrand,carSeriesId,carSeries,carModelId,carModel,importInfo',
                tplColsWidth: [100, 50, 180],
                hasTitle: false,
                hasInput: false,
                autoFill: true
            });

            dl.init({
                url: BASE_PATH + '/shop/car_category/car_model',
                showKey: 'model',
                dom: '.js-downlist3',
                hasTitle: false,
				hasInput: false,
                autoFill: true
            });
        });
        /* 下拉列表 end */

        seajs.use('dialog', function(dg) {
			dg.titleInit();

			$('.time-to-zero').click(function(){
                dg.success(['将于', 3, '秒后结束']);

			})
            //加载框
            $(document).on('click','.js-btn-load',function(){
                dg.load({
                    time: 3*1000
                });
            });

            //确认框
            $(document).on('click','.js-btn-confirm',function(){
                dg.confirm('这是确认框',function(){
                    alert('我是确定回调');
                },function(){
                    alert('我是取消回调');
                });
            });

            //信息警告框
            $(document).on('click','.js-btn-warn',function(){
                  dg.warn('这是信息警告框', function(index){
                        //回调方法
                        alert('我是回调');
                    });
            });

            //信息成功框
            $(document).on('click','.js-btn-success',function(){
                dg.success('这是信息成功框', function(index){
                    //回调方法
                    alert('我是回调');
                });
            });

            //信息失败框
            $(document).on('click','.js-btn-fail',function(){
                dg.fail('这是信息失败框', function(index){
                    //回调方法
                    alert('我是回调');
                });
            });
        });






        /* 日历 start */
		seajs.use('date', function(dp) {
			// 未配置开始结束日历
			dp.dpStartEnd();

			// 时分秒日历
			dp.dpStartEnd({
				start: 'start1',
				end: 'end1',
				startSettings: {
                    dateFmt: 'yyyy-MM-dd HH:mm:ss',
                    maxDate: '#F{$dp.$D(\'end1\')||\'%y-%M-%d %H:%m:%s\'}'
				},
				endSettings: {
                    dateFmt: 'yyyy-MM-dd HH:mm:ss',
					minDate: '#F{$dp.$D(\'start1\')}',
                    maxDate: '%y-%M-%d %H:%m:%s'
				}
			});

			// 普通未配置日历
			dp.datePicker();

			// 普通最大日期日历
            dp.datePicker('.time', {
                maxDate: '%y-%M-%d'
            });

            // 双日历
            dp.datePicker('.time1', {
                doubleCalendar: true
            })
		});
        /* 日历 end */

		//车型选择,需要引入 "yqx/tpl/common/car-type-tpl.ftl"
		carTypeInit({
			dom: '.js-car-type',
			callback: function(data){
				$('#callbackData').text(JSON.stringify(data));
			}
		});

        // select
		seajs.use('select', function(st){

            //异步获取下拉列表数据
            st.init({
                dom: '.js-select',
                url: BASE_PATH + '/shop/manager/get_manager',
                showKey: "id",
                showValue: "name"
            });

            // 静态下拉列表数据
			st.init({
				dom: '.js-select2',
				showKey: "key",
	            showValue: "value",
	            data: [{
	            	key: 'xxx',
	            	value: 'yyy'
	            },{
	            	key: 'aaa',
	            	value: 'bbb'
	            }]
			});

            // 可清空的下拉列表
            st.init({
                dom: '.js-select3',
                showKey: "key",
                showValue: "value",
                isClear: true,
                data: [{
                    key: 'xxx',
                    value: 'yyy'
                },{
                    key: 'aaa',
                    value: 'bbb'
                }]
            });
		});

		//验证初始化
		seajs.use('check', function(ck){
			ck.init();
		});

		//新增服务
		addServiceInit({
			dom: '#serviceAddBtn',
			callback: function(json){
				$('#serviceCallbackData').html(JSON.stringify(json));
			}
		});

		//图片上传
		seajs.use('upload',function(ud){
			ud.init({
				dom: '#fileBtn',
				url: BASE_PATH + '/index/oss/upload_img',
				callback: function(json){
					$('#fileCallbackData').html(JSON.stringify(json));
				}
			});
		});
		//选择配件
     	//需要引入 include "yqx/tpl/common/goods-add-tpl.ftl"
		addGoodsInit({
			dom: '#goodsChosenBtn',
			callback: function(json){
				$('#chosenGoodsData').html(JSON.stringify(json));
			}
		});
		//新增配件
     	//需要引入 include "yqx/tpl/order/new-part-tpl.ftl"
		newPart({
			dom: '#goodsAddBtn',
			callback: function(json){
				$('#goodsAddData').html(JSON.stringify(json));
			}
		});
		//批量添加物料
     	//需要引入 "yqx/tpl/common/batch-add-part-tpl.ftl"
		batchAddPart({
			dom: '#batchGoodsAddBtn',
			callback: function(json){
				$('#batchGoodsAddData').html(JSON.stringify(json));
			}
		});
		//维修工、洗车工
     	//需要引入 "yqx/tpl/common/worker-multiple-tpl.ftl"
		workerInit({
			dom: '.workerNames',
			hiddenDom: '.workerIds',
			width: '370px',
			align: 'left',
			url: BASE_PATH + '/shop/manager/get_manager'
		});

		workerInit({
			dom: '.workerNames2',
			hiddenDom: '.workerIds2',
			width: '400px',
			align: 'right',
			url: BASE_PATH + '/shop/manager/get_manager'
		});

		//初始化车牌下拉框
		seajs.use('downlist',function(dl){
			dl.init({
		        url: BASE_PATH + '/shop/customer/search/mobile',
		        searchKey: 'com_license',
		        tplId: 'carLicenceTpl',
		        showKey: 'license',
		        dom: 'input[name="orderInfo.carLicense"]',
		        hasInput: false,
		        callbackFn: function (obj, item) {
		            //回调函数
		        }
		    });
		});

		seajs.use('audio', function(au) {
            // 这里的音频是一个示例，要不直接复制使用
            var audio = au.init('http://5.1015600.com/download/ring/000/100/b03c68b73323f085c608552607d55317.mp3');
            $('.js-audio-nav, .js-audio').on('click', function() {
                audio.play();
            });
        });
	});
</script>



<#include "yqx/layout/footer.ftl">