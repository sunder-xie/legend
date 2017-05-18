<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/analysis/analysis-common.css?2d49f83eb5011c7b9185d5d127e00f7d">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/analysis/level.css?7975eca7f61b2791548e8ba27521edbe"/>
<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>
    <div class="Z-right">
        <h3 class="Z-title">客户营销  > <a href="${BASE_PATH}/marketing/ng/analysis">客户分析</a> > <i>客户级别分析</i></h3>
        <div class="mk-sc-box">
            <div class="mk-sc-item Z-select-time">
                <input type="text" id="sTime" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'eTime\')||\'new Date()\'}'})" value="" placeholder="开始时间"/>
                <span>至</span>
                <input type="text" id="eTime" onclick="WdatePicker({minDate:'#F{$dp.$D(\'sTime\')}', maxDate:new Date()})" value="" placeholder="结束时间"/>
            </div>
            <div class="mk-sc-item">
                <button class="qxy_btn" id="btnST">统计</button>
            </div>
        </div>
        <ul class="Z-type-userType user-tag-box">
        <#assign totalCount=highCustomerTypeCount?default(0) + middleCustomerTypeCount?default(0) + lowCustomerTypeCount?default(0)>
        <#assign totalAmount=highCustomerTypeConsumeAmount?default(0) + middleCustomerTypeConsumeAmount?default(0) + lowCustomerTypeConsumeAmount?default(0)>
            <li class="Z-high user-tag" data-sTime="${search_sTime}" data-eTime="${search_eTime}" data-tag="high" data-refer="level_high">
                <p class="custom-high">高端客户</p>
                <p class="info">车价30万以上</p>
                <p class="custom-high-num"><span class="num-title">数量</span><span class="num">${highCustomerTypeCount?default(0)} 个</span></p>
                <p class="custom-total-money"><span class="value-title">累计消费金额</span><span class="value">${highCustomerTypeConsumeAmount?default(0)} 元</span></p>
                <p class="custom-percent"><span class="type-title">数量占比</span>
                    <span class="money">
                    <#if totalCount!=0 && highCustomerTypeCount??>
                    ${highCustomerTypeCount/totalCount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <p class="custom-old-type"><span class="type-title">消费金额占比</span>
                    <span class="money">
                    <#if totalAmount!=0 && highCustomerTypeConsumeAmount??>
                    ${highCustomerTypeConsumeAmount/totalAmount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <i class="Z-type-tag"></i>
            </li>
            <li class="Z-middle user-tag" data-tag="middle" data-refer="level_middle">
                <p class="custom-middle">中端客户</p>
                <p class="info">车价10-30万之间</p>
                <p class="custom-middle-num"><span class="num-title">数量</span><span class="num">${middleCustomerTypeCount?default(0)} 个</span></p>
                <p class="custom-total-money"><span class="value-title">累计消费金额</span><span class="value">${middleCustomerTypeConsumeAmount?default(0)} 元</span></p>
                <p class="custom-percent"><span class="type-title">数量占比</span>
                    <span class="money">
                    <#if totalCount!=0 && middleCustomerTypeCount??>
                    ${middleCustomerTypeCount/totalCount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <p class="custom-middle-type"><span class="type-title">消费金额占比</span>
                    <span class="money">
                    <#if totalAmount!=0 && middleCustomerTypeConsumeAmount??>
                    ${middleCustomerTypeConsumeAmount/totalAmount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <i class="Z-type-tag"></i>
            </li>
            <li class="Z-low user-tag" data-tag="low" data-refer="level_low">
                <p class="custom-low">低端客户</p>
                <p class="info">车价10万以下</p>
                <p class="custom-low-num"><span class="num-title">数量</span><span class="num">${lowCustomerTypeCount?default(0)} 个</span></p>
                <p class="custom-total-money"><span class="value-title">累计消费金额</span><span class="value">${lowCustomerTypeConsumeAmount?default(0)} 元</span></p>
                <p class="custom-percent"><span class="type-title">数量占比</span>
                    <span class="money">
                    <#if totalCount!=0 && lowCustomerTypeCount??>
                    ${lowCustomerTypeCount/totalCount*100}%
                    <#else>
                        0%
                    </#if>
                    </span>
                </p>
                <p class="custom-low-type"><span class="type-title">消费金额占比</span>
                    <span class="money">
                    <#if totalAmount!=0 && lowCustomerTypeConsumeAmount??>
                    ${lowCustomerTypeConsumeAmount/totalAmount*100}%
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
                        <ul class="pop-record" id="historyContent">
                            <% if (data2 && data2.length) { %>
                            <% for (var i = 0; i < data2.length; i++) { %>
                            <% var item = data2[i]; %>
                            <li class="pop-record-item">
                                <p class="js-record-content mb-5 one-way" title="<%= item.customerFeedback %>"><%= item.customerFeedback %></p>
                                <p class="clearfix">
                                    <span class="datetime fl"><%= item.visitTimeFormat %> <%= item.visitMethod %></span>
                                    <strong class="user fr"><%= item.visitorName %></strong>
                                </p>
                            </li>
                            <% }} %>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>

<script id="historyTpl" type="text/html">
    <% if ( success && data ) { %>
    <li class="pop-record-item">
        <p class="js-record-content mb-5 one-way" title="<%= data.customerFeedback %>"><%= data.customerFeedback %></p>
        <p class="clearfix">
            <span class="datetime fl"><%= data.visitTimeFormat %> <%= data.visitMethod %></span>
            <strong class="user fr"><%= data.visitorName %></strong>
        </p>
    </li>
    <% } %>
</script>
<#--列表、短信弹框-->
<#include "marketing/ng/analysis/list_tpl.ftl" >
<#include "marketing/ng/analysis/sms_tpl.ftl" >

<!--信息导出控制模板 [[-->
<#include "marketing/export-confirm-tpl.ftl">
<!--]] -->
<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>
<script>
    jQuery(function($){
        var $doc = $(document);
        var _selectedCustomer = [];
        var sTime = $(".Z-high").data("stime");
        var eTime = $(".Z-high").data("etime");
        var customerData = {
            type: "high",
            page: 1,
            obj: $(".Z-high")
        };
        var urlData = {"page":customerData.page};
        var size;
        var revisitId;

        template.helper("min", function() {
            var args = Array.prototype.slice.call(arguments, 0);
            return Math.min.apply({}, args);
        });
        template.helper('isAllSelected', isAllSelected);
        if(sTime||eTime){
            $("#sTime").val(sTime);
            $("#eTime").val(eTime);
        }

        if(sTime || eTime){
            urlData = {"page":customerData.page,"search_sTime":sTime,"search_eTime":eTime};
        }
        console.log(urlData);

        if(MarketingSMS && typeof MarketingSMS == 'function') {
            new MarketingSMS(_selectedCustomer, 2, {
                url: BASE_PATH + '/shop/marketing/sms/new/calculate_number/level',
                partUrl: customerData,
                key: 'type',
                data: urlData
            }, null, 10);
        }

        $(".Z-type-select").addClass("selected");
        $(".Z-type-select i").addClass("select-tag");

        //点击统计按钮
        $doc.on("click","#btnST",function(){
            var search_sTime = $("#sTime").val();
            var search_eTime = $("#eTime").val();
            var checkDate = /\d{4}-\d{2}-\d{2}/;
            var tag = $(".Z-type-userType").find(".on").data("tag");
            if(checkDate.test(search_sTime) || checkDate.test(search_eTime)){
                window.location.href=BASE_PATH+"/marketing/ng/analysis/level?tag="+tag+"&search_sTime="+search_sTime+"&search_eTime="+search_eTime;
            }else if(!search_sTime && !search_eTime){
                window.location.href=BASE_PATH+"/marketing/ng/analysis/level?tag="+tag;
            }
        });
        //自定义分页显示条数
        $doc.on("click",".js-pre-page li",function(){
            var selected = $('.js-pre-page .selected');

            if(selected[0] == this) {
                return;
            }

            selected.removeClass('selected');
            $(this).addClass('selected');

            size = $(this).data('value');
            urlData = {"page":customerData.page,"size":size};
            customList();
        });
        function customList(){
            $.getJSON(BASE_PATH+"/marketing/ng/analysis/level/"+customerData.type,urlData,function(json){
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

        function tabSelect() {
            $(".Z-type-userType li").removeClass("on");
            $(".Z-type-userType li").find("i").removeClass("green-tag");
            $(this).addClass("on");
            $(this).find("i").addClass("green-tag");
            $(".Z-type-dataList").css("border","1px solid #ddd");
            customerData.obj = $(this);
            urlData.page = 1;
            if($(this).index()==0){
                customerData.type = "high";
                customList();
                $('.Z-type-title').html("高端客户");
                $('.Z-type-num').html("车价30万以上");
            }else if($(this).index()==1){
                customerData.type = "middle";
                customList();
                $('.Z-type-title').html("中端客户");
                $('.Z-type-num').html("车价10-30万");
            }else if($(this).index()==2){
                customerData.type = "low";
                customList();
                $('.Z-type-title').html("低端客户");
                $('.Z-type-num').html("车价10万以下");
            }
            $(".Z-dataList-header:first-child").show();

            for(var i in _selectedCustomer) {
                delete _selectedCustomer[i];
            }
            $('#selectedNum').text(0)
        }
        var userType;
        if($("#tag").val()){
            if($("#tag").val()=='high'){
                userType = 0;
            }else if($("#tag").val()=='middle'){
                userType = 1;
            }else if($("#tag").val()=='low'){
                userType = 2;
            }
        }else{
            userType = 0;
        }
        tabSelect.call( $(".Z-type-userType li").get(userType) );

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
                    beforeSend: function() {
                        $('.js-revisit-btn').prop('disabled', true);
                    },
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
                    $text = $('.js-revisit-text'),
                    $time = $('.js-time'),
                    value = $text.val(),
                    time = $time.val(),
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
                var html;
                if(result.success) {
                    // 填充历史记录模板
                    html = template('historyTpl', result);
                    $('#historyContent').prepend(html);

                    // 禁用回访
                    $text.val("").prop('disabled', true);
                    $time.val("").prop('disabled', true);
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

        // 导出
        exportSecurity.tip({'title':'导出客户级别分析'});
        exportSecurity.confirm({
            dom: '.js-excel',
            title: '客户分析-客户级别分析',
            callback: function(json){
                var type = 1;
                if(customerData.type){
                    if(customerData.type=='high'){
                        type = 3;
                    }else if(customerData.type=='middle'){
                        type = 2;
                    }else if(customerData.type=='low'){
                        type = 1;
                    }
                }
                var sTime = $("#sTime").val();
                var eTime = $("#eTime").val();
                window.location.href = BASE_PATH + '/marketing/ng/analysis/level/export?type=' + type + '&search_sTime=' + sTime + '&search_eTime=' + eTime;
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