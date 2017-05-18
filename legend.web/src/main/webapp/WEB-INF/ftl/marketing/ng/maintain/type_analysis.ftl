<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/css/marketing/maintain/detail.css?9d41992c06e6c3ae84698c5dffebe9bf"/>
<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>
    <div class="Z-right">
        <h3 class="detail_text">客情类型分析</h3>
        <div class="analysis_box">
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
                <input class="date_btn" type="button" id="searchBtn" value="查询">
            </div>
            <div class="tableContent" id="tableContent">

            </div>
            <script type="text/html" id="remindTable">
                <table>
                <thead>
                <tr class="tabcon_title">
                    <th>提醒类型</th>
                    <th>提醒数量</th>
                    <th>到店客户数</th>
                    <th>消费总数</th>
                    <th>转化率</th>
                </tr>
                </thead>
                <% if (success) { %>
                <tbody>
                <% if (data) { %>
                <% for (var i = 0; i < data.length; i++) { %>
                <% var item = data[i]; %>
                <tr class="tabcon_list">
                    <td><%= item.noteTypeStr %></td>
                    <td><%= item.noteCount %></td>
                    <td><%= item.customerCount %></td>
                    <td><%= item.effectAmount %></td>
                    <td><%= item.takeRates %>%</td>
                </tr>
                <%}}%>
                </tbody>
                </table>
                <%}%>
            </script>
        </div>
    </div>
</div>
<#include "layout/footer.ftl" >


<script>

    jQuery(function($) {

        var $document = $(document),
                loadId;

        /**** ?Ajax全局设置（该设置可以通过 global: false 取消） ****/

        $document.ajaxStart(function() {
            loadId = taoqi.loading();
        }).ajaxStop(function() {
            taoqi.close(loadId);
            loadId = undefined;
        });

        /**** ?Model模块 ****/

        var Model = {
            // 查询数据接口
            search: function(startTime, endTime, success) {
                $.ajax({
                    url: BASE_PATH + '/marketing/ng/maintain/type_analysis/list',
                    data: {
                        search_sTime: startTime,
                        search_eTime: endTime
                    },
                    dataType: "json",
                    success: function(result) {
                        success && success(result);
                    }
                });
            }
        };

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

        $document
            // 查询按钮 -- 查询数据
                .on('click', '#searchBtn', function () {
                    var startTime = $('#startTime').val(),
                            endTime = $('#endTime').val();

                    Model.search(startTime, endTime, function (result) {
                        var html;

                        if (result.success) {
                            // 模板填充
                            html = template('remindTable', result);
                            $('#tableContent').html(html);
                        } else {
                            layer.msg(result.errorMsg, 1, 8);
                        }
                    })
                })
            // 查询tab -- 查询数据
                .on('click', '.date_sel a', function () {
                    var $this = $(this);
                    $this.addClass("hover").siblings().removeClass("hover");
                })
            // 查询tag -- 查询数据(今年、上月、本月、所有)
                .on('click', '.date_sel a', function() {
                    var $this = $(this), time;

                    $this.addClass("hover").siblings().removeClass("hover");

                    time = timeFormat.apply(this);
                    $('#startTime').val(time[0]);
                    $('#endTime').val(time[1]);

                    Model.search(time[0], time[1], function (result) {
                        var html;

                        if (result.success) {
                            // 模板填充
                            html = template('remindTable', result);
                            $('#tableContent').html(html);
                        } else {
                            layer.msg(result.errorMsg, 1, 8);
                        }
                    })
                })
        $('a[data-format="M"]').trigger('click');
    })


</script>