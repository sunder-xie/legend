<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/analysis/analysis-common.css?2d49f83eb5011c7b9185d5d127e00f7d">
<link rel="stylesheet" href="${BASE_PATH}/resources/css/marketing/exact_marketing.css?51561f9c5c9a59f15d12713b9416235b"/>
<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>
    <div class="main">
        <h3 class="Z-title">客户营销  > <i>精准营销</i></h3>
        <div class="group-row clearfix">
            <div class="panel panel-default w_465 h_238 mr-10 fl">
                <div class="panel-head clearfix">
                    <h2 class="title fl">客户筛选</h2>
                    <div class="panel-tools fr">
                        <button class="btn btn-s-minor w_80" id="T-reset" type="button">重置</button>
                    </div>
                </div>
                <div class="panel-body filterGroup">
                    <input id="uniqueQuery" type="hidden" value="q1"/>
                    <div class="chosen-group">
                        <label class="chosen-label">行为属性</label>
                        <ul class="chosen-list clearfix">
                            <li v-filter="f1" data-filter="f0,f2,f4,f5" data-query="q1,q2" data-name="xfje">消费金额</li>
                            <li v-filter="f1" data-filter="f0,f2,f4,f5" data-query="q1,q2" data-name="dccz">单车产值</li>
                            <li v-filter="f1" data-filter="f0,f2,f4,f5" data-query="q1,q5" data-name="xfcs"> 消费次数</li>
                            <li v-filter="f2" data-filter="f0,f1,f4,f5" data-query="q5" data-name="wddsj">未到店时间</li>
                        </ul>
                    </div>
                    <div class="chosen-group">
                        <label class="chosen-label">客户属性</label>
                        <ul class="chosen-list clearfix">
                            <li v-filter="f3" data-filter="f0,f5" data-query="q4" data-name="cljb">车辆级别</li>
                            <li v-filter="f3" data-filter="f0,f5" data-query="q2" data-name="cllc">行驶里程</li>
                            <li v-filter="f3" data-filter="f0,f5" data-query="q4" data-name="hyjb" data-data='{"text":${memberLevelText}, "value": ${memberLevelId}}'>会员卡类型</li>
                            <li v-filter="f0" data-filter="f0,f1,f2,f3,f4,f5" data-query="q3" data-name="cp">车牌</li>
                            <li v-filter="f0" data-filter="f0,f1,f2,f3,f4,f5" class="filter-car-type" data-query="q3" data-name="cx">车型</li>
                            <li v-filter="f0" data-filter="f0,f5" data-query="q3" data-name="czhm">车主电话</li>
                        </ul>
                    </div>
                    <div class="chosen-group">
                        <label class="chosen-label">系统属性</label>
                        <ul class="chosen-list clearfix">
                            <li v-filter="f4" data-filter="f0,f1,f2,f4,f5" data-query="q1,q4" data-name="xlkh">新老客户</li>
                            <li v-filter="f5" data-filter="f0,f1,f2,f3,f4,f5"
                                class="filter-customer-tag"
                                data-query="q6" data-name="khbq">客户标签</li>
                            <li v-filter="f4" data-filter="f0,f1,f2,f4,f5" data-query="q4" data-name="hyd">活跃度</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="panel panel-search w_307 fl">
                <div class="panel-head">
                    <h2 class="title">搜索条件</h2>
                </div>
                <div class="panel-body search-tools clearfix" id="queryBuilder"></div>
                <div class="panel-foot">
                    <button class="btn btn-s-primary w_80 mr-10" id="S-search" type="button">搜索</button><button
                        class="btn btn-s-minor w_80" id="S-reset" type="button">重置</button>
                </div>
            </div>
        </div>
        <div class="table-wrap">
            <div class="btn-box">
                <div class="panel-head clearfix">
                    <span class="num-box picked-customer fl">
                        已选<span id="selectedNum" class="Z-type-num">0</span>位客户
                    </span>
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
            </div>
            <div class="panel-body" id="tableContent"></div>
        </div>
        <!--TODO 分页部分 -->
        <div class="qxy_page">
            <div class="qxy_page_inner"></div>
            <input type="hidden" id="tag" value="${tag}"/>
        </div>
    </div>
</div>

<#--群发短信 弹窗 -->
<#include "marketing/ng/analysis/sms_tpl.ftl">
<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>

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

<!-- 列表模板 -->
<script id="tableTpl" type="text/html">
    <%
        var ljxfje = isContained(['xfje', 'xlkh', 'hyd'], json['selectedFilter']),
            ljwddsj = isContained(['wddsj'], json['selectedFilter']),
            dccz = isContained(['dccz'], json['selectedFilter']),
            xfcs = isContained(['xfcs', 'xlkh', 'hyd'], json['selectedFilter']),
            hyjb = isContained(['hyjb'], json['selectedFilter']),
            xslc = isContained(['cllc'], json['selectedFilter']),
            cljb = isContained(['cljb'], json['selectedFilter']),
            cx = isContained(['cx'], json['selectedFilter']),
            khbq = isContained(['khbq'], json['selectedFilter'])
    ;
    %>
    <table class="table selectGroup">
        <thead>
        <tr>
            <th><input class="selectAll"
                <%if(data && data.content && isAllSelected(data.content, selectedCustomer)) {%>
                <%='checked'%>
                <%}%>
                       type="checkbox"/></th>
            <th>车牌</th>
            <th>车主</th>
            <th>车主电话</th>
            <th>车型</th>
            <% if(!cx && !khbq) {%>
            <th>最近消费时间</th>
            <%}%>
            <% if( cljb ) { %>
            <th>车辆级别</th>
            <% } if( ljxfje ) { %>
            <th>累计消费金额</th>
            <% } if( ljwddsj ) { %>
            <th>累计未到店时间</th>
            <% } if( dccz ) { %>
            <th>单车产值</th>
            <% } if( xfcs ) { %>
            <th>消费次数</th>
            <% } if( hyjb ) { %>
            <th>会员卡类型</th>
            <% } if( xslc ) { %>
            <th>行驶里程</th>
            <% } %>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <% var item, len, i; %>
        <% var data = json.data%>
        <% if(json.success && data && data.content && (len = data.content.length)) { %>
        <% for(i = 0; i < len; i++) { %>
        <% item = data.content[i] %>
        <tr>
            <td><input class="selectItem" type="checkbox"
                       data-customer-car-id="<%= item.customerCarId %>"
                       data-mobile="<%=item.mobile%>"
                        <%if(selectedCustomer[item.customerCarId]) {%>
                        <%='checked'%>
                        <%}%>
                       value="<%= item.mobile %>"/></td>
            <td><a class="revisit-btn revisitBtn" target="_blank"  href="/legend/shop/customer/info?cid=<%= item.customerCarId%>"><%= item.carLicense %></a></td>
            <td><%= substr(item.customerName); %></td>
            <td><%= item.mobile %></td>
            <td><%= item.carModel %></td>
            <% if(!cx && !khbq) {%>
            <td><%= item.lastPayTimeStr %></td>
            <%}%>
            <% if( cljb ) { %>
            <td><%= item.carLevel %></td>
            <% } if( ljxfje ) { %>
            <td><%= item.totalAmount %></td>
            <% } if( ljwddsj ) { %>
            <td><%= item.lastPayDays %>天</td>
            <% } if( dccz ) { %>
            <td><%= item.average %></td>
            <% } if( xfcs ) { %>
            <td><%= item.totalNumber %></td>
            <% } if( hyjb ) { %>
            <td><%= item.memberCardTypeName %></td>
            <% } if( xslc ) { %>
            <td><%= item.mileage %></td>
            <% } %>
            <td><a class="revisit-btn revisitBtn" data-customer-car-id="<%= item.customerCarId %>"
                   data-user-data="<%= toStringify(item) %>" href="javascript:;">回访</a></td>
        </tr>
        <% }} %>
        </tbody>
    </table>
</script>

<!-- 搜索条件模板 -->
<script id="queryBuilderTpl" type="text/html">
    <% if(data && data.length) { %>
    <% for(var j = 0; j < data.length; j++) { %>
    <% var item = data[j]; %>

    <% if(item.seed == "q1") { %>
    <div class="col-1 mb-10 fl">
        <span class="label">统计时间：</span><input
            class="input w_90 param" id="startTime" name="search_sTime" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}'})" type="text"/> 至 <input
            class="input w_90 param" id="endTime" name="search_eTime"  onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}'})" type="text"/>
    </div>
    <% } else if(item.seed == "q2") { %>
    <div class="col-1 mb-10 fl">
        <span class="label"><%= item.data.label %>：</span><select class="select w_77 mr-5 selfSelect">
        <option></option>
        <% if(item.data.text && item.data.text.length) { %>
        <% for(var i = 0; i < item.data.text.length; i++) { %>
        <% var subItem =  item.data.text[i]%>
        <option value="<%= (subItem === '自定义' ? '###' : item.data.value[i]) %>"><%= subItem %></option>
        <% }} %>
    </select><p class="self-group selfGroup"><input class="input w_48 inputMin param" name="<%= item.data.name[0] %>" type="text"/> 至 <input
            class="input w_48 inputMax param" name="<%= item.data.name[1] %>" type="text"/></p>
    </div>
    <% } else if(item.seed == "q3") { %>
    <div class="col-1 mb-10 fl">
        <span class="label"><%= item.data.label %>：</span><input
            class="input param" name="<%= item.data.name[0] %>" type="text"/>
    </div>
    <% } else if(item.seed == "q4") { %>
    <div class="col-1 mb-10 fl">
        <span class="label"><%= item.data.label %>：</span><select name="<%= item.data.name[0] %>" class="select w_208 param">
        <% if(item.data.text && item.data.text.length) { %>
        <% for(var i = 0; i < item.data.text.length; i++) { %>
        <option value="<%= item.data.value[i] %>"><%= item.data.text[i] %></option>
        <% }} %>
    </select>
    </div>
    <% } else if(item.seed == "q5") { %>
    <div class="col-1 mb-10 fl">
        <span class="label"><%= item.data.label %>：</span><select name="<%= item.data.name[0] %>" class="select w_101 mr-5 param">
        <% if(item.data.text && item.data.text.length) { %>
        <% for(var i = 0; i < item.data.text.length; i++) { %>
        <option value="<%= item.data.value[i] %>"><%= item.data.text[i] %></option>
        <% }} %>
    </select><input class="input w_100 J_input_limit param" name="<%= item.data.name[1] %>" data-limit_type="number" type="text"/>
    </div>
    <% } else if(item.seed == "q6") { %>
    <div class="col-1 mb-10 fl">
        <span class="label"><%= item.data.label %>：</span>
        <div class="filter-select"><input
            class="input param js-filter-input" name="<%= item.data.name[0] %>" type="text"/>
            <dl style="display: none"></dl>
            </div>
    </div>
    <% } %>

    <% }} else { %>

    <div class="col-1 mb-10 fl">
        <span class="label">统计时间：</span><input
            class="input w_90 param" id="startTime" name="search_sTime" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}'})" type="text"/> 至 <input
            class="input w_90 param" id="endTime" name="search_eTime"  onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}'})" type="text"/>
    </div>

    <% } %>
</script>
<script>
    var _selectedCustomer = {};
    // 客户筛选模块
    jQuery(function($) {
        /*
        * __ 模块文档 __
        *
        * 功能：
        *   1、实现客户筛选标签，排除互斥标签
        *   2、实现客户筛选标签，所有条件相应改变
        *
        * 实现：
        *   1、html标签属性（查看html标签）： v-filter属性 作用是将标签归类，v-filter 的值可以是任意，如果值相同，即为同一模块。
        *       data-filter属性 作用是排除模块(即选中此模块时，将data-filter中的模块置为不可选中状态，可以排除自身)，
        *       data-filter 的值为 v-filter 所对应的值
        *   2、html标签属性（查看html标签）data-query 为搜索条件中的需要调用的搜索模块（即输入选择框等）。
        *       搜索模块的模板为 queryBuilderTpl。
        *   3、html中的隐藏域 uniqueQuery 为搜索模块（这里主要是时间选择输入框）中只能唯一出现的模块（如时间选择模块）。
        *   4、selectedFilter是指选中的html标签中的data-name属性的值。模板isContained方法中，显示该字段需要选中的标签
        */
        var $doc = $(document),
                $filterGroup = $('.filterGroup');

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

        // 数据初始化(如果html标签中的data-data属性有值，则会填充data-data的值)
        function dataInitial() {
            var $xfje = $('[data-name=xfje]'),
                    $dccz = $('[data-name=dccz]'),
                    $xfcs = $('[data-name=xfcs]'),
                    $wddsj = $('[data-name=wddsj]'),
                    $cljb = $('[data-name=cljb]'),
                    $cllc = $('[data-name=cllc]'),
                    $hyjb = $('[data-name=hyjb]'),
                    $cp = $('[data-name=cp]'),
                    $czhm = $('[data-name=czhm]'),
                    $xlkh = $('[data-name=xlkh]'),
                    $cx  = $('.filter-car-type'),
                    $customerTag = $('.filter-customer-tag'),
                    $hyd = $('[data-name=hyd]');

            var value;


            // 消费金额
            value = $xfje.data('data') || {};
            value = $.extend(true, {}, {
                "label": "消费金额",
                "name": ["search_minAmount", "search_maxAmount"],
                "text": ["自定义", "0-1000元", "1000-3000元", "3000-5000元", "5000-10000元", "10000元以上"],
                "value": ["", "0-1000", "1000-3000", "3000-5000", "5000-10000", "10000"]
            }, value);
            $xfje.data('data', value);

            // 单车产值
            value = $dccz.data('data') || {};
            value = $.extend(true, {}, {
                "label": "单车产值",
                "name": ["search_minAverage", "search_maxAverage"],
                "text": ["自定义", "0-100元", "101-300元", "301-500元", "501-700元", "701-1000元", "1000元以上"],
                "value": ["", "0-100", "101-300", "301-500", "501-700", "701-1000", "1000"]
            }, value);
            $dccz.data('data', value);

            // 到店次数
            value = $xfcs.data('data') || {};
            value = $.extend(true, {}, {
                "label": "消费次数",
                "name": ["search_numberSign", "search_number"],
                "text": ["<=", ">="],
                "value": ["<=", ">="]
            }, value);
            $xfcs.data("data", value);

            // 未到店时间
            value = $wddsj.data('data') || {};
            value = $.extend(true, {}, {
                "label": "未到店月数",
                "name": ["search_daySign", "search_day"],
                "text": ["<=", ">="],
                "value": [">=", "<="]
            }, value);
            $wddsj.data('data', value);

            // 车辆级别
            value = $cljb.data('data') || {};
            value = $.extend(true, {}, {
                "label": "车辆级别",
                "name": ["search_carLevelTag"],
                "text": ["低端（0～10万）", "中端（10～30万）", "高端（30万以上）"],
                "value": ["1", "2", "3"]
            }, value);
            $cljb.data('data', value);

            // 车辆里程
            value = $cllc.data('data') || {};
            value = $.extend(true, {}, {
                "label": "行驶里程",
                "name": ["search_minMileage", "search_maxMileage"],
                "text": ["自定义", "0-3万", "3-5万", "5-10万", "10万以上"],
                "value": ["", "0-30000", "30000-50000", "50000-100000", "100000"]
            }, value);
            $cllc.data('data', value);

            // 会员级别
            value = $hyjb.data('data') || {};
            value = $.extend(true, {}, {
                "label": "会员卡类型",
                "name": ["search_memberLevelId"],
                "text": [],
                "value": [],
            }, value);
            $hyjb.data('data', value);

            // 车牌
            value = $cp.data('data') || {};
            value = $.extend(true, {}, {
                "label": "车牌",
                "name": ["search_carLicense"]
            }, value);
            $cp.data('data', value);

            // 车型
            value = $cx.data('data') || {};
            value = $.extend(true, {}, {
                "label": "车型",
                "name" : ["search_carType"]
            }, value);
            $cx.data('data', value);

            // 车主号码
            value = $czhm.data('data') || {};
            value = $.extend(true, {}, {
                "label": "车主电话",
                "name": ["search_mobile"]
            }, value);
            $czhm.data('data', value);

            // 新老客户
            value = $xlkh.data('data') || {};
            value = $.extend(true, {}, {
                "label": "新老客户",
                "name": ["search_tag"],
                "text": ["新客户", "老客户"],
                "value": ["new", "old"]
            }, value);
            $xlkh.data('data', value);

            // 客户标签
            value = $customerTag.data('data') || {};
            value = $.extend(true, {}, {
                "label": "客户标签",
                "name" : ["search_customerTag"]
            }, value);
            $customerTag.data('data', value);

            // 活跃度
            value = $hyd.data('data') || {};
            value = $.extend(true, {}, {
                "label": "活跃度",
                "name": ["search_tag"],
                "text": ["活跃", "休眠", "流失"],
                "value": ["active", "sleep", "lost"]
            }, value);
            $hyd.data('data', value);
        }

        /* 过滤筛选条件 */
        function filterSystem() {
            var filter = [],    // 过滤器
                    query = [], // 搜索条件
                    queryData = [],  // 查询数据
                    uniqueQuery = $('#uniqueQuery').val().split(','),
                    html, elems;
            // 初始化chosen
            $filterGroup.find('.chosen-list li.disabled').removeClass('disabled');

            // 获取需要过滤的元素以及需要查询的条件
            $filterGroup.find('.chosen-list li.selected').each(function() {
                var $this = $(this),
                        _filter = $this.data('filter').split(','),
                        _query = $this.data('query').split(','),
                        _fl = _filter.length,
                        _ql = _query.length,
                        _max = Math.max(_fl, _ql),
                        i, tmp;

                for(i = 0; i < _max; i++) {
                    // 列出互斥模块
                    if( _fl > i ) {
                        tmp = _filter[i];
                        if(filter.indexOf(tmp) == -1) {
                            filter.push('[v-filter="' + tmp + '"]');
                        }
                    }

                    // 列出搜索模块
                    if( _ql > i ) {
                        tmp = _query[i];
                        // 排除唯一数据且已经存在的情况
                        if( !(uniqueQuery.indexOf(tmp) != -1 && query.indexOf(tmp) != -1) )
                        // 判断query是否已经存在唯一数据
                            if( uniqueQuery.indexOf(tmp) != -1 ) {
                                queryData.push({seed: tmp});
                            } else {
                                queryData.push({seed: tmp, data: $this.data('data')});
                            }

                        query.push(tmp);
                    }
                }
            });

            // 过滤器(过滤所有互斥条件)
            elems = filter.join(',');
            $(elems).not('.selected').addClass('disabled');

            // 条件查询
            html = createElement( queryData );
            $('#queryBuilder').html( html );
        }

        /* 创建元素 */
        function createElement(data) {
            return template('queryBuilderTpl', {data: data});
        }

        /* 初始化 */
        $("#startTime, #endTime").off('focus');
        dataInitial();
        filterSystem();

        /* 客户筛选的tag */
        $filterGroup.on('click', '.chosen-list li', function() {
            var $this = $(this);
            // 样式调整
            if( !$this.hasClass('disabled') ) {
                $this.toggleClass('selected');
                if( $this.hasClass('filter-customer-tag') && $this.hasClass('selected') ) {
                    setCustomerTag();
                }
                filterSystem();
            }


        });

        /* 筛选重置 */
        $doc.on('click', '#T-reset', function() {
            $filterGroup.find('.chosen-list li').removeClass('disabled selected');
            filterSystem();
        });

        /* 自定义单选框联动 */
        $doc.on('change', '.selfSelect', function () {
            var $this = $(this),
                    $selfGroup = $this.siblings('.selfGroup'),
                    val = $this.val(),
                    value = val.split('-'), min, max;
            if (val === "###") {
                $selfGroup.find('.inputMin, .inputMax').val('');
                $selfGroup.css('display', 'inline-block');
            } else {
                min = value[0] != undefined ? value[0] : '';
                max = value[1] != undefined ? value[1] : '';
                $selfGroup.find('.inputMin').val(min);
                $selfGroup.find('.inputMax').val(max);
                $selfGroup.hide();
            }
        });

        // 客户标签的过滤搜索
        $doc.on('click', '.filter-select dd', function () {
            var e = $(this).text();
            $(this).parent().hide()
                    .parent()
                    .find('input').val(e);
        }).on('input', '.js-filter-input', function () {
            var val = $(this).val().toLowerCase();
            var dd = $(this).parent().find('dd');

            dd.each(function () {
                var text = $(this).text().toLowerCase();

                if(text.indexOf(val) > -1) {
                    $(this).show();
                } else {
                    $(this).hide();
                }
            })
        }).on('click', '.js-filter-input', function () {
            $(this).parent().find('dl').show();
        })
    });

    template.helper('isContained', function (data, Arr) {
        if(Object.prototype.toString.call(Arr) == "[object Array]") {
            for(var i = 0; i < data.length; i++) {
                if(Arr.indexOf( data[i] ) !== -1 ) {
                    return true;
                }
            }
        }
        return false;
    });

    template.helper("min", function() {
        var args = Array.prototype.slice.call(arguments, 0);
        return Math.min.apply({}, args);
    });

    template.helper("substr", function(str, len) {
        len = len || 6;
        if(str != null && str != "") {
            return str.substr(0, len);
        } else {
            return str;
        }

    });

    template.helper("toStringify", function(data) {
        if(typeof data == "object") {
            return JSON.stringify(data);
        }
        return data;
    });

    // 获取客户标签
    function setCustomerTag () {
        $.ajax({
            url: BASE_PATH + '/shop/customer/tag/value_list',
            success: function (json) {
                var ele = $('.filter-select').find('dl');
                var str = '';
                json.data.forEach(function (e, i) {
                    str += '<dd>' + e + '</dd>'
                });

                $(str).appendTo(ele);
            }
        })
    }

    jQuery(function($) {
        var $doc = $(document),
                searchData = {},
                loadId, messageId, revisitId, moreResult;

        /**** ?Ajax全局设置 ****/

        $doc.ajaxStart(function() {
            loadId = taoqi.loading();
        }).ajaxStop(function() {
            taoqi.close(loadId);
            loadId = undefined;
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

        /**** ?业务处理模块(model) ****/

        var model = {
            search: function(success) {
                $.ajax({
                    url: BASE_PATH + '/marketing/ng/center/accurate/list',
                    data: searchData,
                    dataType: 'json',
                    success: function(result) {
                        success && success(result);
                    }
                });
            },
            getMessageTemplate: function(success) {
                var url1 = BASE_PATH + '/marketing/ng/sms/template/list',
                        url2 = BASE_PATH + '/marketing/ng/sms/template/remain';
                $.when(
                        $.ajax(url1, {dataType: "json"}),
                        $.ajax(url2)
                ).then(function(result1, result2) {
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
            sendMessage: function(params, success) {
                var $save = $('.send');
                $.ajax({
                    url: BASE_PATH + '/shop/marketing/sms/new/send/ng',
                    type: 'post',
                    data: params,
                    dataType: 'json',
                    global: false,
                    beforeSend: function() {
                        $save.prop('disabled', true);
                        loadId = taoqi.loading("短信发送中...");
                    },
                    success: function (result) {
                        success && success(result);
                    },
                    complete: function() {
                        $save.prop('disabled', false);
                        taoqi.close(loadId);
                    }
                });
            },
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
                var $save = $('.js-revisit-btn');
                $.ajax({
                    url: BASE_PATH + '/marketing/ng/maintain/saveFeedback',
                    data: data,
                    type: "post",
                    dataType: "json",
                    beforeSend: function() {
                        $save.prop('disabled', true);
                    },
                    success: function(result) {
                        success && success(result);
                    }
                })
            }
        };

        /**** ?view层渲染模块 ****/

        /* 分页内容渲染 */
        function renderContent() {
            // 调用搜索接口
            model.search(function(result) {
                var html, selectedFilter = [];
                if( result.success ) {
                    //result处理  筛选客户列表字段
                    $('.filterGroup').find('.chosen-list li.selected').each(function() {
                        var name = $(this).data('name');
                        selectedFilter.push(name);
                    });

                    result['selectedFilter'] = selectedFilter;

                    html = template('tableTpl', {
                        json: result,
                        selectedCustomer: _selectedCustomer
                    });
                    $('#tableContent').html(html);
                    $.paging({
                        dom : $(".qxy_page"),
                        itemSize : result.data.totalElements,
                        pageCount : result.data.totalPages,
                        current : result.data.number + 1,
                        backFn: function(p) {
                            searchData['page'] = p;
                            renderContent();
                        }
                    });
                } else {
                    layer.msg(result.errorMsg);
                }
            });
        }

        /* 计算短信内容 */
        function calcWordNumber() {
            var rem = this.val().length,
                    tiao = Math.ceil(rem/70);

            if(!tiao) { tiao = 1; }
            tiao *= $(".sendTo strong").text();

            $('.smsNum strong').text(tiao);
        }

        /* 计算剩余短信条数 */
        function calcRestMessage() {
            var jsRestMessage = $('.js-rest-message'),
                    jsFeeMessage = $('.js-fee-message'),
                    restNum = jsRestMessage.data('number'),
                    feeNum = jsFeeMessage.text(),
                    calcNum;

            if( isNaN(restNum) || isNaN(feeNum) || ( calcNum = (restNum - feeNum) ) < 0 ) {
//                jsRestMessage.text(0);
                return false;
            } else {
//                jsRestMessage.text(calcNum);
                return true;
            }
        }

        template.helper('isAllSelected', isAllSelected);
        template.helper('console', console);
        /**** ?事件绑定模块 ****/

        /* 菜单切换 */
        $doc.on('click', '.menu-content', function() {
            $(this).parent().toggleClass('_active');

        });

        /* 单选和全选 */
        $doc.on('change', '.selectAll', function() {
            var $this = $(this),
                    checked = $this.prop("checked"),
                    $dataList = $this.parents(".selectGroup");

            // 修改作用域(.Z-dataLists)内的所有checkbox
            $dataList.find(".selectItem").each(function () {
                if(this.checked != checked) {
                    $(this).click();
                }
            });

        }).on('change', '.selectItem', function() {
            var $this = $(this),
                    checked = this.checked;
            var id = $(this).data('customerCarId');
            var len = $this.parents(".selectGroup").find(".selectItem").length;

            // 判断是否选中
            if( checked ) {
                _selectedCustomer[id] = $(this).data();

                // 检查是否所有的checkbox都已经选中
                if( $this.parents(".selectGroup").find(".selectItem:checked").length == len ) {
                    $(".selectAll").prop("checked", true);
                }
            } else {
                delete _selectedCustomer[id];
                // 取消全选
                $(".selectAll").prop("checked", false);
            }
            $('#selectedNum').text( getObjectKeyLength(_selectedCustomer) );
        });

        //自定义分页显示条数
        $doc.on("click",".js-pre-page li",function(){
            var selected = $('.js-pre-page .selected');

            if(selected[0] == this) {
                return;
            }

            selected.removeClass('selected');
            $(this).addClass('selected');

            $('#S-search').click();
        });
        /* 搜索和重置 */
        $doc.on('click', '#S-search', function() {
            // 初始化
            searchData = {};

            // 遍历参数
            $('#queryBuilder').find('.param').each(function() {
                var $this = $(this),
                        name = $this.attr('name'),
                        value = $this.val();

                if(value !== "" && value !== undefined) {
                    searchData[name] = value;
                }
            });

            searchData['page'] = 1;
            searchData['size'] = $('.js-pre-page .selected').data('value');
            renderContent();

        }).on('click', '#S-reset', function() {
            $('#queryBuilder').find('input[type=text], select').val('');

        });

        function vaildCustomerNum() {
            var num = 0;
            $('.selectItem:checked').each(function() {
                var mobile = $(this).val();
                if( mobile ) {
                    num++;
                }
            });
            return num;
        }

        /* 调用短信弹窗 */
        if(MarketingSMS && typeof MarketingSMS == 'function') {
            new MarketingSMS(_selectedCustomer, 5, {
                url: BASE_PATH + '/shop/marketing/sms/new/calculate_number/marketing_accurate',
                data: function () {
                    return searchData;
                }
            }, null, 10);
        }

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
                        refer: 'accurate'
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
        })

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