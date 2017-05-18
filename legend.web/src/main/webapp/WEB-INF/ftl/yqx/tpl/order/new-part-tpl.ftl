<#-- 
    新增配件公共模板，用到此模板页面，请在下面登记一下
    ch 2016-04-08
    
    用到的页面：
    yqx/page/order/sell-good.ftl 新建销售
    yqx/page/order/speedily.ftl 快修快保

 -->

<style>
/*新建配件资料 start*/
.create_part .must{color:red;}
.create_part{width: 500px;background: #fff;}
.create_part .dialog-title{background:#232e49;color:#fff;text-align:center;font-size:16px;font-weight:900;line-height:54px;}

.create_part ul{padding: 4.716% 8% 0 8%;}
.create_part li{margin-bottom: 10px;}
.create_part li label{display: inline-block;width: 75px;line-height: 35px;
    height: 35px;
    white-space: nowrap;
}
.create_part li input.yqx-input{height: 36px;width: 330px;}
.create_part li select.dg_select{height: 36px;border:1px solid #c9c9c9;border-radius: 3px;width: 344px;}
.create_part li input.price{width: 155px;margin-right: 20px;}
.create_part li input.measureUnit{width: 65px;}
.create_part .dialog_btn .qxy_btn{margin-top: 10px;}
.create_part .chosen-container{margin-right: 0;}
.create_part .dialog-btn{text-align:center;}

.create_part .yqx-downlist-wrap ul {padding: 0}
.create_part .yqx-downlist-wrap li {margin: 0}
/*新建配件资料 end*/
</style>
<!-- 弹框 新建配件资料 start -->
<script type="text/html" id="create_part">
    <div class="create_part create-goods-dialog" data-tpl-ref="new-part-tpl">
        <p class="dialog-title">
            新建配件资料
        </p>
        <ul>
            <li><label class="form-label form-label-must">配件类别：</label><div class="form-item"><input type="text" placeholder="请选择配件类别" name="goodsTypeText" class="goods_type_input yqx-input" readonly="readonly" data-label="配件类别">
                <input type="hidden" name="catId">
                <input type="hidden" name="customCat">
                <input type="hidden" name="goodsCat">
                </div>
            </li>
            <li><label class="form-label">适配车型：</label><div class="form-item"><input type="text" class="yqx-input price js-show-tips" placeholder="例：宝马"  label="适配车型" id="carModel">
                <input type="hidden" name="carInfo"/>
            </div>
                <label class="form-label form-label">车型别名：</label><div class="form-item"><input type="text" class="yqx-input measureUnit" id="alias"></div></li>
            </li>
            <li><label class="form-label">选择品牌：</label><div class="form-item">
                <input type="text" name="brandName" class="yqx-input yqx-input-icon js-brand-select" value="" placeholder="">
                <input type="hidden" name="brandId">
                <span class="fa icon-angle-down"></span>
            </div></li>
            <li><label class="form-label form-label-must">配件名称：</label><div class="form-item"><input type="text" class="yqx-input" name="name" placeholder="例：壳牌 红壳红喜力汽机油" data-v-type="required" data-label="配件名称"></div></li>
            <li><label class="form-label form-label-must">配件编号：</label><div class="form-item"><input type="text" class="yqx-input" name="goodsSn" placeholder="例：1010200073" label="配件编号" disabled></div></li>
            <li><label class="form-label form-label-must">零件号：</label><div class="form-item"><input type="text" class="yqx-input" name="format" placeholder="例：HX3 SL/CF 15W-40 4L" data-v-type="required" data-label="零件号"></div></li>
            <li><label class="form-label">仓库货位：</label><div class="form-item">
                <input type="text" name="depot" class="yqx-input yqx-input-icon js-depot" value="" placeholder="">
                <span class="fa icon-angle-down"></span>
            </div></li>
            <li><label class="form-label form-label-must">零售单价：</label><div class="form-item"><input type="text" name="price" data-v-type="required | number" class="yqx-input price" data-label="零售单价"></div>
            <label class="form-label form-label-must">配件单位：</label><div class="form-item"><input type="text" class="yqx-input measureUnit" placeholder="例：瓶" name="measureUnit" data-v-type="required" data-label="配件单位"></div></li>
            <li><label class="form-label form-label-must">预警库存：</label><div class="form-item"><input type="text" name="shortageNumber" class="yqx-input" data-v-type="required" value="0" data-label="预警库存"></div></li>
        </ul>
        <!-- 配件类型 -->
        <input type="hidden" name="tqmallStatus" value="4"/>
        <!-- 配件类别 -->
        <input type="hidden" name="goodsType" value="0"/>
        <div class="btn_center dialog-btn">
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-2 qxy_green_btn create_part_submit">提交</a>
            <a href="javascript:void(0);" class="yqx-btn yqx-btn-1 create_part_cancel">取消</a>
        </div>
    </div>
</script>
<!-- 配件类别模板 start -->
<script type="text/html" id="part_cat_list">
    <%for(var item in json.data){%>
    <optgroup label="<%= item%>">
        <%for(var i=0;i<json.data[item].length;i++){%>
        <% var groupItem = json.data[item][i]%>
        <option value="<%= groupItem.id%>">
            <%= groupItem.catName%>
        </option>
        <%}%>
    </optgroup>
    <%}%>
</script>
<!-- 配件类别模板 end -->
<!-- 弹框 新建配件资料 end -->
<script>
    function newPart(option){
        var defaultOpt = {
                //转入口触发按钮
                dom: '',
                //回调函数
                callback: null
            },
            doc = $(document),
            args = $.extend({},defaultOpt,option),
            dgIndex = null;

        seajs.use('downlist', function(dl){
            dl.init({
                dom: '.create-goods-dialog input[name="measureUnit"]',
                url: BASE_PATH + '/shop/goods_unit/get_by_name',
                showKey: "name",
                hasTitle: false,
                hasInput: false,
                notClearInput: true
            });

        });

        seajs.use([
            'dialog',
            'ajax',
            'art',
            'formData',
            'check',
            'downlist',
            'select'
        ], function(dg, ax, at, fd, ck, dl,st){
            // 仓库货位
            st.init({
                dom: '.js-depot',
                url: BASE_PATH + '/shop/goods/get_depot_list',
                showValue: "name",
                canInput:true
            });
            //打开新增配件弹框。
            doc.on("click", args.dom, function(){
                var html = at('create_part',{});
                var createPart = ".create_part";
                $.when(
                    //生成配件编号数据
                    $.ajax({
                        url: BASE_PATH + "/shop/sn/generate",
                        data: {type:"PJ"}
                    })
                ).done(function(){
                    //打开配件弹框
                    dgIndex = dg.open({
                        area: ['500px', '535px'],
                        content: html
                    });
                }).done(function(data2){
                    //配件编号数据
                    $("input[name='goodsSn']",createPart).val(data2.data);
                    ck.insertMsg('.create_part');
                });
            });
            //提交配件数据
            doc.on("click",".create_part_submit",function(){
                var scope = ".create_part";
                var result;
                var data = fd.get(scope);

                // 提交的时候再添加对配件类别的 required
                $('.create_part').find('input[name="goodsTypeText"]')
                        .attr('data-v-type', 'required');

                result = ck.check(scope);
                if(!result){
                    $('.create_part').find('input[name="goodsTypeText"]')
                            .removeAttr('data-v-type');
                    return;
                }
                $.ajax({
                    type: 'post',
                    url: BASE_PATH + "/shop/goods/basicgoods/save",
                    data: data
                }).done(function(json){
                    if(json.success){
                        //关闭弹窗
                        dgIndex && dg.close(dgIndex);
                        dg.success("配件资料新增成功", function(){
                            args["callback"] && args["callback"](json.data);
                        });
                    }else{
                        dg.fail(json.errorMsg);
                    }
                });
            })//新增配件弹窗关闭事件绑定
            .on("click", ".create_part_cancel", function(){
                dgIndex && dg.close(dgIndex);
            })
            .on('blur','#alias',function(){
                var alias = $(this).val();
                if (alias) {
                    var str = $('.create-goods-dialog input[name="carInfo"]').val();
                    var carInfo = {};
                    if(str != ''){
                        carInfo = JSON.parse(str);
                    }
                    carInfo.carAlias = alias;
                    $('.create-goods-dialog input[name="carInfo"]').val(JSON.stringify(carInfo));
                }
            });
            /* 动态下拉列表初始化 start */
            // 车辆选择
            var dlCarBrand = dl.init({
                url: BASE_PATH + '/shop/car_category/car_model',
                dom: '#carModel',
                showKey: 'brand,importInfo,model',
                hiddenKey: 'brandId',
                tplColsWidth: [80, 50, 180],
                hasTitle: false,
                hasInput: false,
                autoFill: true,
                callbackFn: function($input, data, $scope) {
                    var alias = $('#alias').val();
                    var str = {"carBrandId":data.brandId,"carBrandName":data.brand,"carSeriesId":data.seriesId,"carSeriesName":data.series,"carTypeId":data.modelId,"carTypeName":data.model,"carAlias":alias,"importInfo":data.importInfo}
                    $('.create-goods-dialog input[name="carInfo"]').val(JSON.stringify(str));
                },
                clearCallbackFn: function($input, $scope) {
                    // 车型清空
                    $('.create-goods-dialog input[name="carInfo"]').val('');
                }
            });
            st.init({
                dom: '.js-brand-select',
                url: BASE_PATH + '/shop/goods_brand/shop_list',
                showKey: "id",
                canInput: true,
                showValue: "brandName"
            });

        });
        
    }
</script>