<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/allot-common.css?59e8db8de7ecdcc00ce4f3de8c5464aa"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/allot-list.css?79ebe11779c2fdae908476748d071104"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="aside fl">
    <#include "yqx/page/marketing/left-nav.ftl">
    </div>
    <!-- 左侧导航区 end -->
    <div class="yqx-dialog allot-dialog hide">
        <div class="dialog-title">客户归属调整</div>
            <div class="dialog-content">
                <p>还未分配客户，是否马上分配？</p>
            </div>
            <div class="dialog-footer">
                <button class="js-close yqx-btn yqx-btn-1">取消</button><a
                    href="${BASE_PATH}/marketing/gather/allot/normal-allot-list"
                    style="margin-left: 10px;" class="js-confirm yqx-btn yqx-btn-3">分配客户</a>
        </div>
    </div>
    <!-- 右侧内容区 start -->
    <div class="aside-main fr">
        <!-- 标题 start -->
    <#--客户分配图表页-->
        <div class="headline">
            分配客户
            <div class="btn-box fr">
                <a class="allot-btn btn" href="${BASE_PATH}/marketing/gather/allot/normal-allot-list">
                    分配客户
                </a><a class="yqx-btn yqx-btn-3"
                       href="${BASE_PATH}/marketing/gather/allot/customer-allot-list">
                客户归属调整</a>
            </div>
        </div>
        <div class="box echarts-box">
            <h4>客户分配图表</h4>

            <div class="echarts-container" id="bar">

            </div>
        </div>
        <div class="box">
            <table class="yqx-table">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>员工</th>
                    <th>
                        负责客户数<span class="c-question-i js-show-tips"
                                   data-tips="该员工负责车辆总数"
                            ></span></th>
                    <th>
                        平均年消费<span class="c-question-i js-show-tips"
                            data-tips="自然年的归属客户年平均营业额总和/当前负责的客户数"
                            ></span></th>
                    <th>
                        活跃客户<span class="c-question-i js-show-tips"
                                  data-tips="员工当前负责的活跃客户"
                            ></span></th>
                    <th>
                        休眠客户<span class="c-question-i js-show-tips"
                                  data-tips="员工当前负责的休眠客户"
                            ></span></th>
                    <th>
                        流失客户<span class="c-question-i js-show-tips"
                                  data-tips="员工当前负责的流失客户"
                            ></span></th>
                    <th>
                        业务提醒<span class="c-question-i js-show-tips"
                                  data-tips="员工当前负责客户的业务提醒个数（未处理）"
                            ></span>
                    </th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="fill">

                </tbody>
            </table>
        </div>
    </div>
    <!-- 右侧内容区 end -->
</div>
<div class="yqx-dialog choose-staff-dialog hide">
    <div class="dialog-title">客户归属调整</div>
    <div class="dialog-content">
        <div class="clearfix">
            <label class="col-2" style="line-height: 30px;">请选择员工</label>

            <div class="form-item col-5">
                <input class="yqx-input enter-name js-staff-name"
                       placeholder="员工名字">
            </div>
        </div>
        <ul class="clearfix staff-list">
        </ul>
    </div>
    <div class="dialog-footer">
        <button class="js-cancel yqx-btn yqx-btn-1">取消</button><button
            style="margin-left: 10px;"
            class="js-confirm yqx-btn yqx-btn-3">确定</button>
    </div>
</div>
<!-- 表格模板 start -->
<script type="text/html" id="tableTpl">
    <%for(var i in data) {%>
    <%var t = data[i];%>
    <tr>
        <td><%=(+i + 1)%></td>
        <td class="name js-show-tips">
            <a href="${BASE_PATH}/marketing/gather/allot/customer-allot-list?userId=<%=t.userId%>" class="yqx-link-1"><%=t.userName%></a>
        </td>
        <td>
            <%=t.customerNumber%>
        </td>
        <td>
            <%=t.averageAmount%>
        </td>
        <td>
            <%=t.activeCustomerNumber%>
        </td>
        <td>
            <%=t.lazyCustomerNumber%>
        </td>
        <td>
            <%=t.lostCustomerNumber%>
        </td>
        <td>
            <%=t.noteNumber%>
        </td>
        <td>
            <a href="javascript:;" class="yqx-link-1 js-t-re-bind" data-user-id="<%=t.userId%>">全部调整</a>
            <a href="javascript:;" class="yqx-link-2 js-t-off-bind" data-user-id="<%=t.userId%>">解绑</a>
        </td>
    </tr>
    <%}%>
</script>
<!-- 表格模板 end -->
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/third-plugin/echart/echarts.min.js"></script>
<script src="${BASE_PATH}/static/js/page/marketing/gather/allot/allot-list.js?fdeb6167105a9af2c2fa526a5a5787c6"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">