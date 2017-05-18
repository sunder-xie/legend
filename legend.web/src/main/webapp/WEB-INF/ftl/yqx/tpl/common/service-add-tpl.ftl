<#--
    选择服务公共模板，用到此模板页面，请在下面登记一下
    ch 2016-04-15

    用到的页面：
	yqx/page/order/speedily 快修快保
	yqx/page/order/common-add 综合维修
 -->
<style>
/*新建服务项目 start*/

.create_service{width: 500px;height:435px;background: #fff;}
.create_service .dialog_title{background:#232e49;color:#fff;text-align:center;font-size:16px;font-weight:900;line-height:54px;}
.create_service .must{color:red;}
.create_service ul{padding: 40px 40px 0 40px;}
.create_service li{margin-bottom: 10px;}
.create_service li label{display: inline-block;width: 75px;line-height: 35px;}
.create_service li input.dg_input{height: 35px;border:1px solid #c9c9c9;border-radius: 3px;width: 330px;padding: 0 5px;}
.create_service li select.dg_select{height: 35px;border:1px solid #c9c9c9;border-radius: 3px;width: 342px;}
.create_service li input.service_fee{width:288px;}
.create_service li .service_time_label{margin-left: 15px;}
.create_service li .service_time_input{width:55px;}
.create_service li .unit_input{display: inline-block;float: none;position:relative;}
.create_service .unit_input input,
.create_service .unit_input span{vertical-align: middle;}
.create_service .unit_input span{margin-left:10px;}
.create_service .btn_center{padding-top:20px;}
.create_service ul .yqx-downlist-content {padding: 0;}
.create_service .yqx-downlist-content li {margin: 0;}
.w-330{ width: 340px}
/*新建服务项目 end*/
</style>

<!-- 弹框 新建服务项目 start -->
<script type="text/html" id="create_service">
    <div class="create_service" data-tpl-ref="service-add-tpl">
        <p class="dialog_title">
            新建服务资料
        </p>
        <ul>
            <li><label>服务名称：<span class="must">*</span></label><div class="form-item">
            	<input type="text" class="dg_input" placeholder="例：小保养" name="name" data-v-type="required" data-label="服务名称">
            </div>	
            </li>
            <li>
				<label>服务类别：<span class="must">*</span></label><div class="form-item w-330">
                    <input type="text" name="categoryName" class="yqx-input yqx-input-icon js-service-type" value="" placeholder="请选择" data-v-type="required">
                    <span class="fa icon-angle-down "></span>
                    <input type="hidden" name="categoryId" value="">
                    <#--<select name="categoryId" class="dg_select"  data-label="服务类别">-->
                        <#--<option value="">请选择</option>-->
                    <#--</select>-->
				</div>
            </li>
            <li><label>服务编号：<span class="must">*</span></label><div class="form-item"><input name="serviceSn" type="text" class="dg_input" data-v-type="required" data-label="服务编号" readonly></div></li>
            <li><label>车辆级别：</label><div class="form-item"><input name="carLevelName" type="text" class="dg_input" placeholder="例：10万—30万"  data-label="车辆级别"></div></li>
            <li>
                <label>服务费用：<span class="must">*</span></label><div class="unit_input">
                <input type="text" class="dg_input service_fee" data-v-type="required" name="servicePrice" data-label="服务费用"><span>小时</span>
            </div>
            </li>
        </ul>
        <div class="btn_center text-center">
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-2 js-service-confirm">提交</a>
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 js-service-cancel">取消</a>
        </div>
    </div>
</script>
<!-- 弹框 新建服务项目 end -->

<script>
	function addServiceInit(opt){
		var doc = $(document),
			args = $.extend({
				dom: "",
				callback: function(){}
			},opt),
			dgIndex = null;

        seajs.use('downlist', function(dl){
            dl.init({
                dom: 'input[name="carLevelName"]',
                url: BASE_PATH + '/shop/car_level/get_car_level_by_name',
                showKey: "name",
                notClearInput: true,
				hasInput: false,
                hasTitle: false
            });
        });

		seajs.use([
			'dialog',
			'ajax',
			'art',
			'check',
			'formData',
			'select'
		],function(dg, ax, at, ck, fd,st){
            st.init({
                dom: '.js-service-type',
                url: BASE_PATH + '/shop/shop_service_cate/get_by_name',
                showKey: "id",
                showValue: 'name',
                canInput: true
            });

			//打开服务项目弹窗
			doc.off('click.csb').on("click.csb", args.dom, function(){
				var html = at('create_service',{});
				var createService = ".create_service";
				$.when(
					//请求服务类别数据
					$.ajax({
						url: BASE_PATH+"/shop/shop_service_cate/get_by_name"
					}),
					//生成服务编号数据
					$.ajax({
						url: BASE_PATH + "/shop/sn/generate",
						data: {type:"FW"}
					})
				).done(function(){
					//打开服务弹框
					dgIndex = dg.open({
						area: ['500px', '435px'],
						content: html,
						success: function(){
							//绑定验证
							ck.init('.create_service');
						}
					});
				}).done(function(data1,data2){
					for(var i=0;i<data1[0].data.length;i++){
						var item = data1[0].data[i];
						var option = '<option value="'+item.id+'">'+item.name+'</option>';
						$("select[name='categoryId']",createService).append(option);
					}
					$("input[name='serviceSn']",createService).val(data2[0].data);
				});
			});

			//提交服务项目数据
			doc.off('click.css').on("click.css",".js-service-confirm",function(){
				var scope = ".create_service";
				var result = ck.check(scope);
				var data = fd.get(scope);
				if(!result){
					return;
				}
				$.ajax({
					type: 'post',
					url: BASE_PATH + "/shop/shop_service_info/updateInOrder",
					data: data
				}).done(function(json){
					if(json.success){
						//关闭弹窗
						dgIndex && dg.close(dgIndex);
						dg.success("服务资料新增成功", function(){
							args.callback && args.callback(json);
						});
					}else{
						dg.fail(json.errorMsg);
					}
				});
			});
			//关闭弹窗
			doc.off('click.service-dg-close').on('click.service-dg-close', '.js-service-cancel', function(){
				//关闭弹窗
				dgIndex && dg.close(dgIndex);
			});
		});
		
	}
</script>