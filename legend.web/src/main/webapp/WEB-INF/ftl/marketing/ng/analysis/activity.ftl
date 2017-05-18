<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/analysis/analysis-common.css?2d49f83eb5011c7b9185d5d127e00f7d">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/activity.css?45df95f2231d7805b2dda920dd00e428"
      xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html"/>
<style>

</style>
<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>
    <div class="Z-right">
        <h3 class="Z-title">客户营销  > <a href="${BASE_PATH}/marketing/ng/analysis">客户分析</a> > <i> 客户类型分析</i></h3>
        <ul class="type-select">
            <li class="Z-type-select">
                <span>新老客户分析</span>
            </li>
            <li class="Z-activity-select">
                <span>活跃度分析</span>
            </li>
        </ul>
        <ul class="Z-type-userType user-tag-box">
        <#assign totalCount=activeCustomerTypeCount?default(0) + sleepCustomerTypeCount?default(0) + lostCustomerTypeCount?default(0)>
        <#assign totalAmount=activeCustomerTypeConsumeAmount?default(0) + sleepCustomerTypeConsumeAmount?default(0) + lostCustomerTypeConsumeAmount?default(0)>
            <li class="Z-activity user-tag" data-refer="active">
                <p class="custom-activity">活跃客户</p>
                <p class="info">最近3个月来店消费过</p>
                <p class="custom-activity-num"><span class="num-title">数量</span><span class="num">${activeCustomerTypeCount?default(0)} 个</span></p>
                <p class="custom-total-money"><span class="value-title">累计消费金额</span><span class="value">${activeCustomerTypeConsumeAmount?default(0)} 元</span></p>
                <p class="custom-percent"><span class="type-title">数量占比</span>
                    <span class="money">
                    <#if totalCount!=0 && activeCustomerTypeCount??>
                    ${activeCustomerTypeCount/totalCount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <p class="custom-activity-type"><span class="type-title">消费金额占比</span>
                    <span class="money">
                    <#if totalAmount!=0 && activeCustomerTypeConsumeAmount??>
                    ${activeCustomerTypeConsumeAmount/totalAmount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <i class="Z-type-tag"></i>
            </li>
            <li class="Z-dormant user-tag" data-refer="sleep">
                <p class="custom-dormant">休眠客户</p>
                <p class="info">3到6个月之内来店消费过</p>
                <p class="custom-dormant-num"><span class="num-title">数量</span><span class="num">${sleepCustomerTypeCount?default(0)} 个</span></p>
                <p class="custom-total-money"><span class="value-title">累计消费金额</span><span class="value">${sleepCustomerTypeConsumeAmount?default(0)} 元</span></p>
                <p class="custom-percent"><span class="type-title">数量占比</span>
                    <span class="money">
                    <#if totalCount!=0 && sleepCustomerTypeCount??>
                    ${sleepCustomerTypeCount/totalCount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <p class="custom-dormant-type"><span class="type-title">消费金额占比</span>
                    <span class="money">
                    <#if totalAmount!=0 && sleepCustomerTypeConsumeAmount??>
                    ${sleepCustomerTypeConsumeAmount/totalAmount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <i class="Z-type-tag"></i>
            </li>
            <li class="Z-lost user-tag" data-refer="lost">
                <p class="custom-lost">流失客户</p>
                <p class="info">6个月之内没有来店消费过</p>
                <p class="custom-lost-num"><span class="num-title">数量</span><span class="num">${lostCustomerTypeCount?default(0)} 个</span></p>
                <p class="custom-total-money"><span class="value-title">累计消费金额</span><span class="value">${lostCustomerTypeConsumeAmount?default(0)} 元</span></p>
                <p class="custom-percent"><span class="type-title">数量占比</span>
                    <span class="money">
                    <#if totalCount!=0 && lostCustomerTypeCount??>
                    ${lostCustomerTypeCount/totalCount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <p class="custom-lost-type"><span class="type-title">消费金额占比</span>
                    <span class="money">
                    <#if totalAmount!=0 && lostCustomerTypeConsumeAmount??>
                    ${lostCustomerTypeConsumeAmount/totalAmount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <i class="Z-type-tag"></i>
            </li>
        </ul>
        <div class="Z-type-dataList">
            <div class="Z-dataList-header">
                <div class="picked-customer fl">已选<span id="selectedNum">0</span>位客户</div>
                <div class="Z-btn fl">
                    <button class="js-message yqx-btn yqx-btn-3">发送营销短信</button>
                    <button class="js-message-all yqx-btn yqx-btn-1">全部发送营销短信</button>
                </div>
                <div class="to-excel js-excel fr">导出Excel</div>
                <div class="pre-page fr">
                    <ul class="js-pre-page">
                        <li data-value="10" class="selected">10 条/页</li>
                        <li data-value="30">30 条/页</li>
                        <li data-value="50">50 条/页</li>
                    </ul>
                </div>
            </div>
            <div id="content"></div>
        </div>
    </div>
    <div class="qxy_page">
        <div class="qxy_page_inner">
        <#-- 列表分页 -->
        </div>
    </div>

    <input type="hidden" id="tag" value="${tag}"/>
</div>

<!-- 回访记录弹窗模板 -->
<script id="revisitTpl" type="text/html">
    <div class="pop-box w_654">
        <div class="pop-head">
            <h1 class="pop-title"><strong><%= data.customerName!=null?data.customerName:"" %></strong><span
                    class="highlight"><%= data.mobile!=null?data.mobile:"" %></span><span><%= data.carLicense!=null?data.carLicense:"" %></span><span
                    ><%= data.carModel!=null?data.carModel:"" %></span></h1>
        </div>
        <div class="pop-body h_500">
            <div class="pop-content clearfix">
                <div class="fl col-2">
                    <div class="pop-inner">
                        <p class="pop-box-title mb-10">消费记录</p>
                        <% if ( data1 && data1.length ) { %>
                        <ul class="pop-record">
                            <% for (var i = 0; i < data1.length; i++) { %>
                            <% var item = data1[i]; %>
                            <li class="pop-record-item js-show-more">
                                <p class="clearfix"><a class="link-default fl" href="${BASE_PATH}/shop/order/detail?orderId=<%= item.orderId %>" target="_blank"><%= item.orderSn %></a><span class="datetime fr"><%= item.orderTime %></span></p>
                                <div class="service-box">
                                    <p>服务项目</p>
                                    <% if (item.orderServicesList && item.orderServicesList.length) { %>
                                    <% for (var j = 0; j < item.orderServicesList.length; j++) { %>
                                    <% var subItem = item.orderServicesList[j]; %>
                                    <span>&lt;<%= subItem.serviceName %>&gt;</span>
                                    <% }} %>
                                </div>
                            </li>
                            <% } %>
                        </ul>
                        <% } %>
                    </div>
                </div>
                <div class="fr col-2">
                    <div class="pop-inner">
                        <p class="pop-box-title mb-10">回访记录</p>
                        <div class="revisit-box mb-10">
                            <div class="revisit-content mb-10">
                                <textarea class="revisit-text js-revisit-text" placeholder="沟通记录"></textarea>
                            </div>
                            <div class="revisit-bottom clearfix">
                                <div class="fl">
                                    <span class="label1">下次沟通时间：</span><input
                                        class="input time js-time" onfocus="WdatePicker({minDate: new Date()})" type="text"/>
                                </div>
                                <button class="btn btn-s-primary fr js-revisit-btn" data-customer-car-id="<%= data.customerCarId %>">保存</button>
                            </div>
                        </div>
                        <p class="pop-box-title mb-10">历史记录</p>
                        <% if (data2 && data2.length) { %>
                        <ul class="pop-record">
                            <% for (var i = 0; i < data2.length; i++) { %>
                            <% var item = data2[i]; %>
                            <li class="pop-record-item">
                                <p class="js-record-content mb-5 one-way" title="<%= item.customerFeedback %>"><%= item.customerFeedback %></p>
                                <p class="clearfix">
                                    <span class="datetime fl"><%= item.visitTimeFormat %> <%= item.visitMethod %></span>
                                    <strong class="user fr"><%= item.visitorName %></strong>
                                </p>
                            </li>
                            <% } %>
                        </ul>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>
<#--列表、短信-->
<#include "marketing/ng/analysis/list_tpl.ftl">
<#include "marketing/ng/analysis/sms_tpl.ftl">

<!--信息导出控制模板 [[-->
<#include "marketing/export-confirm-tpl.ftl">
<!--]] -->

<#include "layout/footer.ftl" >
<script type="application/javascript" src="${BASE_PATH}/resources/script/page/marketing/marketing_common.js?2041a738ed7deab02ce7376d6a73bcde"></script>
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>
<script>
    jQuery(function($){
        var $doc = $(document);
        var size;
        var revisitId;
        var _selectedCustomer = {};
        var customerData = {
                    type: "active",
                    page: 1,
                    obj: $(".Z-activity")
                },
                urlData = {"page":customerData.page};

        template.helper('isAllSelected', isAllSelected);

        template.helper("min", function() {
            var args = Array.prototype.slice.call(arguments, 0);
            return Math.min.apply({}, args);
        });

        if(MarketingSMS && typeof MarketingSMS == 'function') {
            new MarketingSMS(_selectedCustomer, 1, {
                url: BASE_PATH + '/shop/marketing/sms/new/calculate_number/type',
                partUrl: customerData,
                data: urlData,
                key: 'type'
            }, null , function () {
                var type;
                switch(customerData.type) {
                    case 'active':
                        type = 9;
                        break;
                    case 'sleep':
                        type = 8;
                        break;
                    case 'lost':
                        type = 7;
                        break;
                    default:
                        type = 10;
                }

                return type;
            });
        }

        //选择新老客户或者活跃度tab
        $doc.on("click",".type-select li",function(){
            $(".type-select li").removeClass("selected");
            $(".type-select li i").removeClass("select-tag");
            $(this).addClass("selected");
            $(this).find("i").addClass("select-tag");
            if($(this).index()==0){
                window.location.href=BASE_PATH+"/marketing/ng/analysis/type?tag=older";
            }else if($(this).index()==1){
                window.location.href=BASE_PATH+"/marketing/ng/analysis/type/activity?tag=activity";
            }
        });
        $(".Z-activity-select").addClass("selected");
        $(".Z-activity-select i").addClass("select-tag");

        //自定义分页显示条数
        $doc.on("change",".js-pre-page",function(){
            size = $(".js-pre-page option:selected").val();
            urlData = {"page":customerData.page,"size":size};
            customList();
        });
        //高中低质量用户数据接口
        function customList(){
            $.getJSON(BASE_PATH+"/marketing/ng/analysis/type/activity/"+customerData.type,urlData,function(json){
                if(json.success && json.data){
                    renderContent(json.data);
                }
            });
        }
        //实现数据分页
        function renderContent(data) {
            var templateHtml = template.render('goodsList', {
                'goodsList': data,
                'selectedCustomer': _selectedCustomer
            });
            $('#content').html(templateHtml);
            $.paging({
                dom : $(".qxy_page"),
                itemSize : data.totalElements,
                pageCount : data.totalPages,
                current : data.number + 1,
                backFn : function(p){
                    urlData.page = p;
                    customList();
                }
            });
        }
        function tabSelect(){
            $(".Z-type-userType li").removeClass("on");
            $(".Z-type-userType li").find("i").removeClass("green-tag");
            $(this).addClass("on");
            $(this).find("i").addClass("green-tag");
            $(".Z-type-dataList").css("border","1px solid #ddd");
            customerData.obj = $(this);
            urlData.page = 1;

            if($(this).index()==0){
                customerData.type = "active";
                customList();
                $('.Z-type-title').html("活跃客户");
            }else if($(this).index()==1){
                customerData.type = "sleep";
                customList();
                $('.Z-type-title').html("休眠客户");
            }else if($(this).index()==2){
                customerData.type = "lost";
                customList();
                $('.Z-type-title').html("流失客户");
            }
            $(".Z-dataList-header:first-child").show();

            for(var i in _selectedCustomer) {
                delete _selectedCustomer[i];
            }
            $('#selectedNum').text(0)
        }
        var userType;
        if($("#tag").val()){
            if($("#tag").val()=='activity'){
                userType = 0;
            }else if($("#tag").val()=='dormant'){
                userType = 1;
            }else if($("#tag").val()=='loss'){
                userType = 2;
            }
            tabSelect.call( $(".Z-type-userType li").get(userType) );
        }
        //鼠标覆盖高中等质量三个框时出现绿色浮框效果，点击之后开始请求数据并
        $doc.on("click",".Z-type-userType li",function(){
            tabSelect.call(this);
        })

        // 全选模块
        $doc.on("change", ".Z-select", function() {
            var $this = $(this),
                    checked = $this.prop("checked"),
                    $dataList = $this.parents(".Z-dataLists");
            // 修改作用域(.Z-dataLists)内的所有checkbox
            $dataList.find(".Z-select-item").each(function () {
                if(this.check != checked) {
                    $(this).click();
                }
            });
        })
            // 局部模块
                .on("change", ".Z-select-item", function() {
                    var $this = $(this),
                            checked = $this.prop("checked");
                    var id = $(this).data('customerCarId');
                    var len = $this.parents(".Z-dataLists").find(".Z-select-item").length;

                    // 判断是否选中
                    if( checked ) {
                        _selectedCustomer[id] = $(this).data();

                        // 检查是否所有的checkbox都已经选中
                        if( $this.parents(".Z-dataLists").find(".Z-select-item:checked").length == len ) {
                            $(".Z-select").prop("checked", true);
                        }
                    } else {
                        delete _selectedCustomer[id];
                        // 取消全选
                        $(".Z-select").prop("checked", checked);
                    }
                    $('#selectedNum').text( getObjectKeyLength(_selectedCustomer) );
                });
        template.helper("toStringify", function(data) {
            if(typeof data == "object") {
                return JSON.stringify(data);
            }
            return data;
        });

        /**** ?layer全局设置 ****/

        function dialog(html, end) {
            return $.layer({
                type: 1,
                title: false,
                area: ['auto', 'auto'],
                offset : ['20px',''],
                border: [0], //去掉默认边框
                shade: [0.5, '#000'],
                shadeClose: false,
                bgcolor: '#fff',
                closeBtn: [1, true], //去掉默认关闭按钮
                shift: 'top',
                end: function () {
                    end && end();
                },
                page: {
                    html: html
                }
            });
        }

        //回访纪录接口
        var model = {
            revisit: function(customerCarId, success) {
                var data = {
                    customerCarId: customerCarId
                };

                $.when(
                        /*! 消费记录 */
                        $.ajax(BASE_PATH + '/marketing/ng/maintain/consumeRecord', {data: data}),
                        /*! 回访记录 */
                        $.ajax(BASE_PATH + '/marketing/ng/maintain/feedbackList', {data: data})
                )
                        .then(function (result1, result2) {
                            result1 = result1[0];
                            result2 = result2[0];

                            if(result1.success && result2.success) {
                                success && success(result1, result2);
                            } else {
                                result1.success || layer.msg(result1.errorMsg);
                                result2.success || layer.msg(result2.errorMsg);
                            }
                        });
            },
            revisitSave: function(data, success) {
                $.ajax({
                    url: BASE_PATH + '/marketing/ng/maintain/saveFeedback',
                    data: data,
                    type: "post",
                    dataType: "json",
                    success: function(result) {
                        success && success(result);
                    }
                })
            }
        };

        /* 调用回访弹窗 */
        $doc.on('click', '.revisitBtn', function() {
            var $this = $(this),
                    customerCarId = $this.data('customerCarId'),
                    userData = $this.data('userData');
            model.revisit(customerCarId, function(result1, result2) {
                var data = {
                            data: userData,
                            data1: result1.data,
                            data2: result2.data
                        },
                        html = template('revisitTpl', data);

                revisitId = dialog(html);

                // 展示消费记录、历史记录的第一条数据
                $('.js-show-more:first').find('.service-box').addClass('show');
                $('.js-record-content:first').removeClass('one-way');
            });

            // 限制输入内容
        }).on('input', '.js-revisit-text', function() {
            var $this = $(this),
                    value = $this.val(),
                    max = 70;
            if (value != "" && value.length > max) {
                value = value.substr(0, max);
                $this.val(value);
            }

            // 记录回访记录
        }).on('click', '.js-revisit-btn', function() {
            var $this = $(this),
                    value = $('.js-revisit-text').val(),
                    time = $('.js-time').val(),
                    customerCarId = $this.data('customerCarId'),
                    data = {
                        customerCarId: customerCarId,
                        content: value,
                        nextVisitTime: time,
                        refer: $(".user-tag.on").data("refer")
                    };

            if(value == "") {
                layer.msg('内容不能为空！');
                return false;
            }

            if( time == "" ) {
                delete data['nextVisitTime'];
            }

            model.revisitSave(data, function(result) {
                if(result.success) {
                    layer.close(revisitId);
                    layer.msg('保存成功！', 1, 9);
                } else {
                    layer.msg(result.errorMsg);
                }
            });

            // 展示消费记录的服务项目
        }).on('click', '.js-show-more', function() {
            $(this).find('.service-box').toggleClass('show').end()
                    .siblings('li').find('.service-box').removeClass('show');

            // 历史记录展示
        }).on('click', '.js-record-content', function() {
            $(this).toggleClass("one-way")
                    .parent().siblings('li').find('.js-record-content').addClass('one-way');
        });

        // 导出Excel
        exportSecurity.tip({'title':'导出客户活跃度分析'});
        exportSecurity.confirm({
            dom: '.js-excel',
            title: '客户类型分析-活跃度分析',
            callback: function(json){
                var type = 1;
                if(customerData.type){
                    if(customerData.type=='active'){
                        type = 1;
                    }else if(customerData.type=='sleep'){
                        type = 2;
                    }else if(customerData.type=='lost'){
                        type = 3;
                    }
                }
                window.location.href = BASE_PATH + '/marketing/ng/analysis/type/activity/export?type=' + type;
            }
        });

        function getObjectKeyLength(obj) {
            var count = 0;
            for(var key in obj) {
                if(obj.hasOwnProperty(key)) {
                    count++;
                }
            }

            return count;
        }

        function isAllSelected(content, data) {
            var ret = false;
            var i = 0, id;
            if(content && typeof content == 'object' && content.length) {
                for (; i < content.length; i++) {
                    id = content[i].customerCarId;

                    if(!data[id]) {
                        return false;
                    } else {
                        ret = true;
                    }
                }
            }

            return ret;
        }
    });
</script>