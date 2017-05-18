<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/allot-common.css?59e8db8de7ecdcc00ce4f3de8c5464aa"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/normal-allot-list.css?ec9ed280efc199a6e03fa5efceed716b"/>
<!-- 样式引入区 end-->


<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="aside fl">
    <#include "yqx/page/marketing/left-nav.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="aside-main fl">
        <!-- 标题 start -->
        <div class="headline">
            <i>标准分配客户页</i>
            <div class="fr">
                <a href="${BASE_PATH}/marketing/gather/allot/custom-allot-list" class="allot-btn btn">自定义分配</a>
            </div><i class="fr" style="font-size: 12px;color: #666;">根据门店需要自行分配</i>
        </div>

        <div class="box staff-box">
            <label class="box-title">选择员工:</label><div class="staff-list clearfix">
        <#list allotUserVoList as allotUser>
            <i class="staff-btn btn" data-user-id="${allotUser.userId}">${allotUser.userName}<i title="删除" class="staff-del-btn js-del-staff"></i></i>
        </#list><div class="choose-staff js-add-staff">+</div>
            </div>
        </div>
        <div class="box customer-list">
            <h4 class="box-title">标准化分配客户：</h4>
            <div class="list-box clearfix">
                <div class="col-6 num-list">
                    <p>活跃客户：<i class="red num"><span class="js-active">${freeSummary.active}</span></i>个</p>
                    <p>休眠客户：<i class="red num"><span class="js-lazy">${freeSummary.lazy}</span></i>个</p>
                    <p>流失客户：<i class="red num"><span class="js-lost">${freeSummary.lost}</span></i>个</p>
                    <p>
                        <button class="yqx-btn yqx-btn-3 js-one">一键分配</button>
                    </p>
                </div>
                <div class="col-6 info-list">
                    <p class="top-line">待分配客户：<i class="red" id="needAllotNum"><span class="js-sum">${freeSummary.sum}</span></i> 个</p>
                    <p style="color: #333">按序号逐一执行</p>
                    <p>① 活跃客户（最近3个月到店维修过），客户归属接待次数最多的员工</p>
                    <p>② 休眠客户（3到6个月之内来店消费过），平均分配</p>
                    <p>③ 流失客户（6个月之内没有来店消费过），平均分配</p>
                </div>
            </div>
        </div>
        <div class="box table-box">
            <h4 class="box-title">本次分配结果</h4>
            <table class="yqx-table">
                <thead>
                    <tr>
                        <th>序号</th>
                        <th>员工</th>
                        <th>活跃客户</th>
                        <th>休眠客户</th>
                        <th>流失客户</th>
                        <th>本次分配</th>
                        <th>负责客户总数</th>
                    </tr>
                </thead>
                <tbody id="fill">

                </tbody>
            </table>
        </div>
        <!-- 标题 end -->
    </div>
    <!-- 右侧内容区 end -->
</div>
<!-- 表格模板 start -->
<!-- 表格模板 end -->
<div class="yqx-dialog choose-staff-dialog hide">
    <div class="dialog-title">选择员工</div>
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
        <button class="js-confirm yqx-btn yqx-btn-3">确定</button>
    </div>
</div>
<!-- 表格模板 start -->
<script type="text/html" id="tableTpl">
    <%for(var i in data) {%>
    <%var t = data[i];%>
    <tr>
        <td><%=(+i + 1)%></td>
        <td class="js-show-tips">
            <%=t.userName%>
        </td>
        <td>
            <%=t.active%>
        </td>
        <td>
            <%=t.lazy%>
        </td>
        <td>
            <%=t.lost%>
        </td>
        <td>
            <%=t.allot%>
        </td>
        <td>
            <%=t.totalAllot%>
        </td>
    </tr>
    <%}%>
</script>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/marketing/gather/allot/normal-allot-list.js?29f2ee7139e507d2bf37a782a739a4df"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">