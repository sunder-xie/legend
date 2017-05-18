<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/base.min.css?5698f550fb48dca82818ee31fd541e81" />
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/analysis/analysis-common.css?2d49f83eb5011c7b9185d5d127e00f7d">
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/maintain/remindCenter.css?d2452de8aa5a274ec8eb8d51d98175b4" xmlns="http://www.w3.org/1999/html"/>

<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>
    <div class="Z-right">
        <h3 class="Z-title">
            客户营销  > <i>提醒中心</i>
            <div class="title-btn">
                <a class="yqx-btn yqx-btn-small yqx-btn-2" href="${BASE_PATH}/marketing/ng/maintain">提醒效果</a>
                <a class="yqx-btn yqx-btn-small yqx-btn-1" href="${BASE_PATH}/marketing/ng/maintain/detail">操作记录</a>
            </div>
        </h3>

        <div class="Z-remind-type">
            <p class="remind-type-title">提醒类别</p>
            <div class="Z-line"></div>
            <div class="remind-type-list">
                <p class="remind-expire">到期提醒</p>
                <div class="remind-expire-list">
                    <span data-index="0" data-name="appointment" class="appointment" data-number="${noteInfo.appointNumber}"><i class="appointment-tag"></i>&nbsp;车主预约单提醒&nbsp;&nbsp;<em>${noteInfo.appointCount}</em></span>
                    <span data-index="1" data-name="maintain" class="maintain" data-number="${noteInfo.maintainNumber}"><i class="maintain-tag"></i>&nbsp;保养到期提醒&nbsp;&nbsp;<em>${noteInfo.maintainCount}</em></span>
                    <span data-index="2" data-name="insurance" class="insurance-expire" data-number="${noteInfo.insuranceNumber}"><i class="insurance-expire-tag"></i>&nbsp;保险到期提醒&nbsp;&nbsp;<em>${noteInfo.insuranceCount}</em></span>
                    <span data-index="3" data-name="inspection" class="yearly-inspection-expire" data-number="${noteInfo.auditingNumber}"><i class="yearly-inspection-expire-tag"></i>&nbsp;年检到期提醒&nbsp;&nbsp;<em>${noteInfo.auditingCount}</em></span>
                </div>
                <p class="remind-guest">客情提醒</p>
                <div class="remind-guest-list">
                    <span data-index="4" data-name="visit" class="visit" data-number="${noteInfo.visitNumber}"><i class="visit-tag"></i>&nbsp;回访提醒&nbsp;&nbsp;<em>${noteInfo.visitCount}</em></span>
                    <span data-index="5" data-name="birthday" class="birthday" data-number="${noteInfo.birthdayNumber}"><i class="birthday-tag"></i>&nbsp;生日提醒&nbsp;&nbsp;<em>${noteInfo.birthdayCount}</em></span>
                    <span data-index="6" data-name="lost" class="lost" data-number="${noteInfo.lostCustomerNumber}"><i class="lost-tag"></i>&nbsp;流失客户提醒&nbsp;&nbsp;<em>${noteInfo.lostCustomerCount}</em></span>
                </div>
            </div>
        </div>
        <div class="Z-type-dataList">
            <div class="Z-dataList-header">
                <div class="num-box picked-customer fl">已选<span id="selectedNum" class="Z-type-num">0</span>位客户</div>
                <div class="Z-btn fl">
                    <button class="js-message yqx-btn yqx-btn-3">发送营销短信</button>
                    <button class="js-message-all yqx-btn yqx-btn-1">全部发送营销短信</button>
                </div>
                <div class="pre-page fr">
                    <ul class="js-pre-page">
                        <li data-value="10" class="selected">10 条/页</li>
                        <li data-value="30">30 条/页</li>
                        <li data-value="50">50 条/页</li>
                    </ul>
                </div>
            </div>
            <div id="content"></div>
            <script id="goodsList" type="text/html">
                <%
                var appointment = isContained(['appointment'], selectedFilter),
                maintain = isContained(['maintain'], selectedFilter),
                insurance = isContained(['insurance'], selectedFilter),
                inspection = isContained(['inspection'], selectedFilter),
                visit = isContained(['visit'], selectedFilter),
                birthday = isContained(['birthday'], selectedFilter),
                lost = isContained(['lost'], selectedFilter);
                %>
                <table class="Z-dataLists">
                    <thead>
                    <tr>
                        <th><input class="Z-select"
                            <%if(goodsList && goodsList.content && isAllSelected(goodsList.content, selectedCustomer)) {%>
                            <%='checked'%>
                            <%}%>
                            type="checkbox"></th>
                        <th>车牌</th>
                        <th>车主</th>
                        <th>车主电话</th>
                        <th>车型</th>
                        <th>最近消费时间</th>
                        <% if( appointment ) { %>
                        <th>下单时间</th>
                        <th>预约时间</th>
                        <th>预约内容</th>
                        <% } if( maintain ) { %>
                        <th>上次保养时间</th>
                        <th>行驶里程</th>
                        <th class="maintain-expire">预计保养到期时间</th>
                    <#--<th>预计保养项目</th>-->
                        <% } if( insurance ) { %>
                        <th>保险到期时间</th>
                        <% } if( inspection ) { %>
                        <th>年检到期时间</th>
                        <% } if( visit ) { %>
                        <th>计划回访时间</th>
                        <% } if( birthday ) { %>
                        <th>生日</th>
                        <% } if( lost ) { %>
                        <th>累计消费次数</th>
                        <% } %>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% if(goodsList) {%>
                    <% var content = goodsList.content;%>
                    <% for(var i=0;i<content.length ;i++) {%>
                        <tr data-id="<%=content[i].id%>">
                            <td>
                                <input class="Z-select-item"
                                <%if(selectedCustomer[content[i].customerCarId]) {%>
                                <%='checked'%>
                                <%}%>
                                data-mobile="<%= content[i].mobile %>"
                                data-customer-car-id="<%= content[i].customerCarId%>"
                                data-note-info-id="<%= content[i].id%>" type="checkbox">
                            </td>
                            <td title="<%= content[i].carLicense %>"><a class="revisit-btn revisitBtn" target="_blank"  href="/legend/shop/customer/info?cid=<%= content[i].customerCarId%>"><%= content[i].carLicense %></a></td>
                            <td class="customName" title="<%= content[i].customerName %>"><%= content[i].customerName %></td>
                            <td title="<%= content[i].mobile %>"><%= content[i].mobile %></td>
                            <td class="carModel" title="<%= content[i].carModel %>"><%= substr(content[i].carModel) %></td>
                            <td title="<%= content[i].lastPayTimeStr %>"><%= content[i].lastPayTimeStr %></td>
                            <% if( appointment ) { %>
                            <td title="<%= content[i].appointCreateTimeStr %>"><%= content[i].appointCreateTimeStr %></td>
                            <td title="<%= content[i].noteTimeStr %>"><%= content[i].noteTimeStr %></td>
                            <td title="<%= content[i].appointContent %>"><%= content[i].appointContent %></td>
                            <% } if( maintain ) { %>
                            <td title="<%= content[i].lastNoteKeepupTimeStr %>"><%= content[i].lastNoteKeepupTimeStr %></td>
                            <td title="<%= content[i].mileage %>"><%= content[i].mileage %></td>
                            <td title="<%= content[i].noteTimeStr %>"><%= content[i].noteTimeStr %></td>
                        <#--<td title="<%= content[i].keepupTerm %>"><%= content[i].keepupTerm %></td>-->
                            <% } if( insurance ) { %>
                            <td title="<%= content[i].noteTimeStr %>"><%= content[i].noteTimeStr %></td>
                            <% } if( inspection ) { %>
                            <td title="<%= content[i].noteTimeStr %>"><%= content[i].noteTimeStr %></td>
                            <% } if( visit ) { %>
                            <td title="<%= content[i].noteTimeStr %>"><%= content[i].noteTimeStr %></td>
                            <% } if( birthday ) { %>
                            <td title="<%= content[i].noteTimeStr %>"><%= content[i].noteTimeStr %></td>
                            <% } if( lost ) { %>
                            <td title="<%= content[i].totalNumber %>"><%= content[i].totalNumber %></td>
                            <% } %>
                            <td><a class="revisit-btn revisitBtn" data-customer-car-id="<%= content[i].customerCarId %>" data-user-data="<%= toStringify(content[i]) %>" href="javascript:;">回访</a></td>
                        </tr>
                        <%}%>
                        <%}%>
                    </tbody>
                </table>
            </script>
        </div>
    </div>
    <div class="qxy_page">
        <div class="qxy_page_inner">
        <#-- 列表分页 -->
        </div>
    </div>
    <input type="hidden" id="tag" value="${tag}"/>

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
                                    <button class="btn btn-s-primary fr js-revisit-btn" data-note-info-id="<%= data.id %>" data-customer-car-id="<%= data.customerCarId %>">保存</button>
                                </div>
                            </div>
                            <p class="pop-box-title mb-10">历史记录</p>
                            <ul class="pop-record" id="historyContent">
                                <% if (data2 && data2.length) { %>
                                <% for (var i = 0; i < data2.length; i++) { %>
                                <% var item = data2[i]; %>
                                <li class="pop-record-item">
                                    <p class="js-record-content mb-5 one-way"><%= item.customerFeedback %></p>
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

</div>
<#include "marketing/ng/analysis/sms_tpl.ftl" >
<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>
<script>
    jQuery(function($){
        var $doc = $(document);
        var _selectedCustomer = {};
        var size;
        var targetFn;
        var customerData = {
            type: 0,
            page: 1,
            obj: $(".appointment")
        };
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
        var urlData = {"page":customerData.page};
        var revisitId;

        /* IE 8 以下版本不支持indexOf（compatible）*/
        if( !('indexOf' in Array.prototype) ) {
            Array.prototype.indexOf = function(seed) {
                var i;
                for(i = 0; i < this.length; i++) {
                    if(this[i] == seed) return i;
                }
                return -1;
            }
        }
        template.helper('isAllSelected', isAllSelected);
        template.helper("toStringify", function(data) {
            if(typeof data == "object") {
                return JSON.stringify(data);
            }
            return data;
        });
        template.helper('isContained', function (data, Arr) {
            for(var i = 0; i < data.length; i++) {
                if( Object.prototype.toString.call(Arr) == "[object Array]" && Arr.indexOf( data[i] ) !== -1 ) {
                    return true;
                }
            }
            return false;
        });
        //字符串截取
        template.helper("substr", function(str, len) {
            len = len || 6;
            if(str != null && str != "") {
                return str.substr(0, len);
            } else {
                return str;
            }

        });

        if(MarketingSMS && typeof MarketingSMS == 'function') {
            new MarketingSMS(_selectedCustomer, 4, {
                url: BASE_PATH + '/shop/marketing/sms/new/calculate_number/note_center',
                data: urlData
            }, customerData, function () {
                return customerData.type + 1;
            });
        }

        //提醒列表数据接口
        function customList(){
            $.getJSON(BASE_PATH+"/marketing/ng/maintain/list?noteType="+customerData.type,
                    urlData,
                    function(json){
                        if(json.success && json.data){
                            renderContent(json.data);

                            targetFn && targetFn();
                        }
                    });
        }
        //实现数据分页
        function renderContent(data) {
            //result处理  筛选客户列表字段
            var selectedFilter = [];
            $('.remind-type-list').find('.on').each(function() {
                var name = $(this).data('name');
                selectedFilter.push(name);
            });
            var templateHtml = template.render('goodsList', {
                'goodsList': data,
                'selectedCustomer': _selectedCustomer,
                selectedFilter: selectedFilter
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

        //鼠标覆盖提醒框时出现绿色浮框效果，点击之后开始请求数据并
        function tabSelect(){
            $(".remind-type-list span").removeClass("on");
            $(this).addClass("on");
            $(".Z-type-dataList").css("border","1px solid #ddd");
            customerData.obj = $(this);
            urlData.page = 1;
            if($(this).data("index")==0){
                customerData.type = 0;
                customList();
            }else if($(this).data("index")==1){
                customerData.type = 1;
                customList();
            }else if($(this).data("index")==2){
                customerData.type = 2;
                customList();
            }else if($(this).data("index")==3){
                customerData.type = 3;
                customList();
            }else if($(this).data("index")==4){
                customerData.type = 4;
                customList();
            }else if($(this).data("index")==5){
                customerData.type = 5;
                customList();
            }else if($(this).data("index")==6){
                customerData.type = 6;
                customList();
            }
            $(".Z-dataList-header:first-child").show();

            for(var i in _selectedCustomer) {
                delete _selectedCustomer[i];
            }
            $('#selectedNum').text(0)
        }
        var userType;
        if($("#tag").val()){
            if($("#tag").val()=='appointment'){
                userType = 0;
            }else if($("#tag").val()=='maintain'){
                userType = 1;
            }else if($("#tag").val()=='insurance'){
                userType = 2;
            }else if($("#tag").val()=='inspection'){
                userType = 3;
            }else if($("#tag").val()=='visit'){
                userType = 4;
            }else if($("#tag").val()=='birthday'){
                userType = 5;
            }else if($("#tag").val()=='lost'){
                userType = 6;
            }
        }else{
            userType = 0;
        }
        // 当URL没有 target 参数时，才初始化获取列表
        if(!getPara('target')) {
            tabSelect.call($(".remind-type-list span").get(userType));
        }
        $doc.on("click",".remind-type-list span",function(){
            tabSelect.call(this);
        });
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
                    noteType = $(".remind-type-list").find(".on").data("index"),
                    noteInfoId = $this.data('noteInfoId');
            var data = {
                customerCarId: customerCarId,
                content: value,
                nextVisitTime: time,
                noteType: noteType,
                noteInfoId: noteInfoId
            };

            if(value == "") {
                layer.msg('内容不能为空！');
                return false;
            }

            if( time == "" ) {
                delete data['nextVisitTime'];
            }
            var refer = '';
            if (data.noteType == 0) { // 预约单
                refer = 'appoint';
            } else if (data.noteType == 1 || data.noteType == 8) { // 保养
                refer = 'maintain';
            } else if (data.noteType == 2) { // 保险
                refer = 'insurance';
            } else if (data.noteType == 3) { // 年检
                refer = 'auditing';
            } else if (data.noteType == 4 || data.noteType == 7) { // 回访
                refer = 'visit';
            } else if (data.noteType == 5) { // 生日
                refer = 'birthday';
            } else if (data.noteType == 6) { // 流失
                refer = 'lost';
            }
            data.refer = refer;
            model.revisitSave(data, function(result) {
                var html;
                if(result.success) {
                    var remainCustomNum = (parseInt($(".remind-type-list .on").find("em").text()))-parseInt(1);
                    $(".remind-type-list .on").find("em").text(remainCustomNum);
                    customList();

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
        // 全选模块
        $doc.on("change", ".Z-select", function() {
            var $this = $(this),
                    checked = $this.prop("checked"),
                    $dataList = $this.parents(".Z-dataLists");

            // 修改作用域(.Z-dataLists)内的所有checkbox
            $dataList.find(".Z-select-item").each(function () {
                if(this.checked != checked) {
                    $(this).click();
                }
            });
        })
            // 局部模块
                .on("change", ".Z-select-item", function() {
                    var $this = $(this),
                            checked = this.checked;
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
                        $(".Z-select").prop("checked", false);
                    }
                    $('#selectedNum').text( getObjectKeyLength(_selectedCustomer) );
                });


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

        template.helper("min", function() {
            var args = Array.prototype.slice.call(arguments, 0);
            return Math.min.apply({}, args);
        });

        clickTarget('target');

        // 从首页跳转过来的，获取URL target 参数，点击相应
        function clickTarget(desc) {
            var val = getPara(desc);
            var id  = getPara('id');
            if(val) {
                $('.' + val).click();
            }

            if(id) {
                targetFn = openTarget.bind(null, id)
            }
        }

        function openTarget(id) {
            var isFind;

            $('.Z-dataLists').find('tbody tr').each(function() {
                var dataId;
                if(isFind) {
                    return;
                }
                dataId = $(this).attr('data-id');

                if(dataId === id) {
                    // tr 里有两个 .revisitBtn
                    $(this).find('.revisitBtn').eq(1).click();
                    targetFn = null;
                    id = null;
                    isFind = true;
                }
            });
        }

        function getPara(name) {
            var reg = new RegExp('(\\?|&)' + name + '=([^&?]*)', 'i');
            var arr = location.search.match(reg);
            if (arr) {
                return arr[2];
            }
            return;
        }

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