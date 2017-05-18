<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/maintain/detail.css?9d41992c06e6c3ae84698c5dffebe9bf"/>
<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>
    <div class="Z-right">
        <h3 class="Z-title">客户营销 >  <a href="${BASE_PATH}/marketing/ng/maintain/center">提醒中心</a> > <i>操作记录</i></h3>
        <div class="DetailTab">
            <span data-index="1" class="hover js-tab">操作记录</span>
            <span data-index="2" class="js-tab">收益记录</span>
        </div>
        <div class="Detail_tabcon">
            <div class="Detail_datebox">
                <div class="date">
                    <div class="mb-10">
                        <input class="param" id="startTime" name="sTime" onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'endTime\')}'})" type="text"/> -
                        <input class="param" id="endTime" name="eTime"  onfocus="WdatePicker({minDate:'#F{$dp.$D(\'startTime\')}'})" type="text"/>
                    </div>
                </div>
                <div class="date_sel">
                    <a data-format="Y" href="javascript:;">今年</a>、
                    <a data-format="LM" href="javascript:;">上月</a>、
                    <a data-format="M" href="javascript:;">本月</a>、
                    <a href="javascript:;">全部</a>
                </div>
                <input class="date_btn js-search-btn" type="button" value="查询">
                <#--<div class="number">共<span>28</span>条</div>-->
                <div class="to-excel js-excel">导出Excel</div>
            </div>
            <div class="tableContent" id="tableContent"></div>
        </div>
    </div>
    <!--TODO 分页部分 -->
    <div class="qxy_page">
        <div class="qxy_page_inner"></div>
        <input type="hidden" id="tag" value="${tag}"/>
    </div>
    <div class="tips" style="display: none">

    </div>
</div>

<!--信息导出控制模板 [[-->
<#include "marketing/export-confirm-tpl.ftl">
<!--]] -->

<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>

<script type="text/html" id="JobRecord">
    <table>
        <thead>
        <tr class="tabcon_title">
            <th class="th-126">处理时间</th>
            <th>车牌</th>
            <th class="th-126">提醒类型</th>
            <th class="th-126">最近服务项目</th>
            <th>操作人</th>
            <th class="th-126">回访记录</th>
            <th>提醒方式</th>
        </tr>
        </thead>
        <% if (success) { %>
        <tbody>
        <% if (data && data.content && data.content.length) { %>
        <% for (var i = 0; i < data.content.length; i++) { %>
        <% var item = data.content[i]; %>
        <tr class="tabcon_list">
            <td class="js-show-tips"><%= item.visitTimeStr %></td>
            <td class="js-show-tips"><%= item.carLicense %></td>
            <td class="js-show-tips"><%= item.noteTypeStr %></td>
            <td class="js-show-tips"><%= item.orderServiceNames %></td>
            <td class="js-show-tips"><%= item.visitorName %></td>
            <td class="js-show-tips"><%= item.customerFeedback %></td>
            <td class="js-show-tips"><%= item.visitMethod %></td>
        </tr>
        <% }} %>
        </tbody>
        <% } %>
    </table>
</script>

<script type="text/html" id="EarningsRecord">
    <table>
        <thead>
        <tr class="tabcon_title">
            <th>最近消费时间</th>
            <th>车牌</th>
            <th>车主</th>
            <th>车主电话</th>
            <th>消费金额</th>
            <th>处理时间</th>
            <th>提醒方式</th>
            <th>操作人</th>
        </tr>
        </thead>
        <% if (success) { %>
        <tbody>
        <% if (data && data.content && data.content.length) { %>
        <% for (var i = 0; i < data.content.length; i++) { %>
        <% var item = data.content[i]; %>
        <tr class="tabcon_list">
            <td class="js-show-tips"><%= item.payTimeStr %></td>
            <td class="js-show-tips"><%= item.carLicense %></td>
            <td class="js-show-tips"><%= item.customerName %></td>
            <td class="js-show-tips"><%= item.mobile %></td>
            <td class="js-show-tips"><%= item.totalAmount %></td>
            <td class="js-show-tips"><%= item.operatorTimeStr %></td>
            <td class="js-show-tips"><%= item.noteWayStr %></td>
            <td class="js-show-tips"><%= item.operator %></td>
        </tr>
        <% }} %>
        </tbody>
        <% } %>
    </table>
</script>

<script>
    jQuery(function($) {

        var $document = $(document),
                tabIndex = 1,
                loadId, tpl, searchData = {page: 1};

        /**** ?Ajax全局设置（该设置可以通过 global: false 取消） ****/

        $document.ajaxStart(function() {
            loadId = taoqi.loading();
        }).ajaxStop(function() {
            taoqi.close(loadId);
            loadId = undefined;
        });

        /**** ?Model模块 (工作记录)****/

        var Model = {
            // 查询数据接口
            search: function(startTime, endTime, success) {
                var url = "";

                // 选择接口
                switch (tabIndex) {
                    // 工作记录
                    case 1: url = BASE_PATH + '/marketing/ng/maintain/detail/list';break;
                    // 收益记录
                    case 2: url = BASE_PATH + '/marketing/ng/maintain/detail/effectList';break;
                }

                startTime && (searchData['search_sTime'] = startTime);
                endTime && (searchData['search_eTime'] = endTime);

                $.ajax({
                    url: url,
                    data: searchData,
                    dataType: "json",
                    success: function(result) {
                        success && success(result);
                    }
                });
            }
        };

        // 渲染页面
        function renderContent(startTime, endTime) {
            Model.search(startTime, endTime, function(result) {
                var html;
                if(result.success) {
                    html = template(tpl, result);
                    $('#tableContent').html(html);

                    // 分页模块
                    $.paging({
                        dom : $(".qxy_page"),
                        itemSize : result.data.totalElements,
                        pageCount : result.data.totalPages,
                        current : result.data.number + 1,
                        backFn: function(p) {
                            searchData['page'] = p;
                            renderContent(startTime, endTime);
                        }
                    });
                } else {
                    layer.msg(result.errorMsg);
                }
            });
        }

        /*
         * 格式化时间
         * @param $('.date_sel a').data("format");
         * return ['startTime', 'endTime']
         */
        function timeFormat() {
            var $this = $(this),
                    format = $this.data("format"),
                    date = new Date(), _date, time = [], last, next;

            switch (format) {
                // 今年
                case "Y":
                    last = date.getFullYear() + "-1-1";
                    next = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                    break;
                // 本月
                case "M":
                    last = date.getFullYear() + "-" + (date.getMonth() + 1) + "-1";
                    next = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                    break;
                // 上月
                case "LM":
                    _date = new Date(date.setDate(0));  // 这个月的第0天即为上个月的最后一天
                    last = _date.getFullYear() + "-" + (date.getMonth() + 1) + "-1";
                    next = _date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + _date.getDate();
                    break;
                // 所有
                default:
                    last = next = "";
            }

            time.push(last, next);
            return time;
        }

        $('.js-tab').click(function () {
            var index = $(this).data('index');

            if(index == '1') {
                $('.to-excel').show();
            } else {
                $('.to-excel').hide();
            }
        })
        $document
                // 查询列表
                .on('click', '.js-search-btn', function() {
                    var startTime = $('#startTime').val(),
                            endTime = $('#endTime').val();

                    // 选择模板
                    switch (tabIndex) {
                        case 1: tpl = 'JobRecord';break;
                        case 2: tpl = 'EarningsRecord';break;
                    }

                    // 初始化分页
                    searchData = {page: 1};
                    renderContent(startTime, endTime);
                })

                // 查询tag -- 查询数据(今年、上月、本月、所有)
                .on('click', '.date_sel a', function() {
                    var $this = $(this), time;

                    $this.addClass("hover").siblings().removeClass("hover");
                    time = timeFormat.apply(this);
                    $('#startTime').val(time[0]);
                    $('#endTime').val(time[1]);

                    $('.js-search-btn').trigger('click');
                })

                // 客情明细tab切换
                .on('click', '.DetailTab span', function() {
                    var $this = $(this);

                    $this.addClass("hover").siblings().removeClass("hover");
                    // 设置tabIndex类型(切换tab时，调用不同接口有用)
                    tabIndex = $this.data('index');

                    // 清空数据
                    $('#tableContent, .qxy_page_inner').html("");
                    $('#startTime, #endTime').val("");

                    $('.js-search-btn').trigger('click');
                });

                $('.js-search-btn').trigger('click');

        // 导出
        exportSecurity.tip({'title':'导出客情明细'});
        exportSecurity.confirm({
            dom: '.js-excel',
            title: '客情维护—客情明细',
            callback: function(json){
                var sTime = $("#startTime").val();
                var eTime = $("#endTime").val();
                window.location.href = BASE_PATH + '/marketing/ng/maintain/detail/list/export?search_sTime=' + sTime + '&search_eTime=' + eTime;
            }
        });

        titleInit(new _tips());

        function titleInit(_tips) {
            var tips;
            $(document).on('mouseenter', '.js-show-tips', function (event) {
                var a, clientWidth = this.clientWidth,
                        scrollWidth = this.scrollWidth,
                        text = $(this).attr('data-tips');

                // 判断是否超出
                if (clientWidth < scrollWidth) {
                    if (this.tagName === 'INPUT') {
                        tips = _tips.show(this, $.trim(this.value));
                    } else {
                        a = $(this);

                        tips = _tips.show(this, $.trim(a.text()));
                    }
                } else if (text) {
                    tips = _tips.show(this, $.trim(text));
                }

            });

            // 移除时立即清除 dialog
            $(document).on('mouseleave', '.js-show-tips', function(event) {
                _tips.close();
            })
                // 目标元素消失的情况
                    .on('mouseenter', function (event) {
                        if(event.target.className.indexOf('js-show-tips') == -1) {
                            _tips.close();
                        }
                    });
        }

        function _tips() {
            this.ele = $('.tips');
            this.offset = 5;

            this.show = function (target, content) {
                var offsetLeft = 0, t = target;
                var offsetTop = target.clientHeight + this.offset;
                this.ele.text(content);

                // 当本窗口超出右边界的时候
                do {
                    offsetLeft += t.offsetLeft;
                } while((t = t.offsetParent) && t != document.body && offsetLeft >= 0);

                t = target;
                do {
                    offsetTop += t.offsetTop;
                } while((t = t.offsetParent) && t != document.body && offsetTop >= 0);


                this.ele.css({
                    left: offsetLeft,
                    top: offsetTop
                });
                this.ele.show();
                t = offsetLeft + this.ele[0].clientWidth - document.body.clientWidth
                if(t > 0) {
                    this.ele.css('left', offsetLeft + t - 5);
                }
            };

            this.close = function () {
                this.ele.hide();
            }
        }
    });

</script>