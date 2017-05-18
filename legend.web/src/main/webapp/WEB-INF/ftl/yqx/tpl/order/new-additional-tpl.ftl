<#--
    添加附加公共模板，用到此模板页面，请在下面登记一下
    ch 2016-04-15

    用到的页面：
    新建综合维修
 -->
<style>
    /*新建服务项目 start*/

    .create_additional{width: 500px;height:435px;background: #fff;}
    .create_additional .dialog_title{background:#232e49;color:#fff;text-align:center;font-size:16px;font-weight:900;line-height:54px;}
    .create_additional .must{color:red;}
    .create_additional .create_additonal_ul{padding: 40px 40px 0 40px;}
    .create_additional li{margin-bottom: 10px;}
    .create_additional li label{display: inline-block;width: 75px;line-height: 35px;}
    .create_additional li input.dg_input{height: 35px;border:1px solid #c9c9c9;border-radius: 3px;width: 330px;padding: 0 5px;}
    .create_additional li select.dg_select{height: 35px;border:1px solid #c9c9c9;border-radius: 3px;width: 342px;}
    .create_additional li input.service_fee{width:288px;}
    .create_additional li .service_time_label{margin-left: 15px;}
    .create_additional li .service_time_input{width:55px;}
    .create_additional li .unit_input{display: inline-block;float: none;position:relative;}
    .create_additional .unit_input input,
    .create_additional .unit_input span{vertical-align: middle;}
    .create_additional .unit_input span{margin-left:10px;}
    .create_additional .btn_center{padding-top:20px;}
    /*新建服务项目 end*/
</style>

<!-- 弹框 新建服务项目 start -->
<script type="text/html" id="create_additional">
    <div class="create_additional" data-tpl-ref="new-additional-tpl">
        <p class="dialog_title">
           添加附加资料
        </p>
        <ul class="create_additonal_ul">
            <li><label>附加名称：<span class="must">*</span></label><div class="form-item">
                <input type="text" class="dg_input" placeholder="例：拖车费" name="name" data-v-type="required" data-label="费用名称">
            </div>
            </li>
            <li><label>附加编号：<span class="must">*</span></label><div class="form-item"><input name="serviceSn" readonly="readonly" type="text" class="dg_input" data-v-type="required" data-label="附加编号" readonly></div></li>
            <li><label>车辆级别：<span class="must">*</span></label><div class="form-item"><input name="carLevelName" type="text" class="dg_input" placeholder="例：10万—30万" data-v-type="required" data-label="车辆级别"></div></li>
            <li>
                <label>费用：<span class="must">*</span></label><div class="unit_input">
                <input type="text" class="dg_input service_fee" data-v-type="required" name="servicePrice" data-label="费用"><span>元</span>
            </div>
            </li>
        </ul>
        <div class="btn_center text-center">
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-2 js-additional-confirm">提交</a>
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 js-additional-cancel">取消</a>
        </div>
    </div>
</script>
<!-- 弹框 新建服务项目 end -->

<script>
    function newAdditional(opt){
        var doc = $(document),
                args = $.extend({
                    dom: "",
                    callback: function(){}
                },opt),
                dgIndex = null;

        seajs.use('select', function(st){
            st.init({
                dom: 'input[name="carLevelName"]',
                url: BASE_PATH + '/shop/car_level/get_car_level_by_name',
                showKey: "name",
                showValue: "name"
            });
        });

        seajs.use([
            'dialog',
            'ajax',
            'art',
            'check',
            'formData'
        ],function(dg, ax, at, ck, fd){
            //打开服务项目弹窗
            doc.on("click", args.dom, function(){
                var html = at('create_additional',{});
                var createService = ".create_additional";
                $.when(
                        //请求服务类别数据
                        $.ajax({
                            url: BASE_PATH+"/shop/shop_service_cate/get_by_name"
                        }),
                        //生成服务编号数据
                        $.ajax({
                            url: BASE_PATH + "/shop/sn/generate",
                            data: {type:"FY"}
                        })
                ).done(function(){
                            //打开服务弹框
                            dgIndex = dg.open({
                                area: ['500px', '435px'],
                                content: html,
                                success: function(){
                                    //绑定验证
                                    ck.init('.create_additional');
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
            doc.on("click",".js-additional-confirm",function(){
                var scope = ".create_additional";
                var result = ck.check(scope);
                var data = fd.get(scope);
                if(!result){
                    return;
                }
                data.categoryName = (function(){
                    return $("option:selected","select[name='categoryId']").text();
                })();
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + "/shop/fees/updateInOrder",
                    data: data
                }).done(function(json){
                    if(json.success){
                        //关闭弹窗
                        dgIndex && dg.close(dgIndex);
                        dg.success("附加资料新增成功", function(){
                            args.callback && args.callback(json);
                        });
                    }else{
                        dg.fail(json.errorMsg);
                    }
                });
            });
            //关闭弹窗
            doc.on('click', '.js-additional-cancel', function(){
                //关闭弹窗
                dgIndex && dg.close(dgIndex);
            });
        });

    }
</script>