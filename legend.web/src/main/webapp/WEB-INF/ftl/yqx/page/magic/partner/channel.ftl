<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/partner/channel.css?148b287d52594d2e9ebe4dbeca861093"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->
    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <!-- group start -->
        <div class="yqx-group">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                渠道商管理
                <i class="group-head-line"></i>
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content">
                <div id="formInfo">
                    <div class="form-label">
                        名称
                    </div>
                    <div class="form-item">
                        <input type="text" name="channelName" class="yqx-input yqx-input-small" value="" placeholder="">
                    </div>
                    <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">搜索</a>
                    <button class="yqx-btn yqx-btn-1 yqx-btn-small js-add">新增</button>
                </div>
                <div id="tableDetail" class="table-detail">

                </div>
            </div>
        </div>
    </div>
</div>
<#--表格渲染-->
<script type="text/template" id="tableTpl">
    <div class="table-scroll-box">
    <table class="yqx-table yqx-table-hover">
        <thead>
        <tr>
            <th>序号</th>
            <th>渠道商名称</th>
            <th>渠道商类型</th>
            <th>联系人</th>
            <th>联系电话</th>
            <th>联系地址</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <%if(json.data && json.data.content){%>
        <%for(var i=0;i<json.data.content.length;i++){%>
        <%var item = json.data.content[i];%>
        <tr class="js-info-btn" data-channel-id="<%=item.id%>">
            <td><%=json.data.size*(json.data.number)+i+1%></td>
            <td><div class="channel-ellipsis" title="<%=item.channelName%>"><%=item.channelName%></div></td>
            <td><%=item.channelType%></td>
            <td><div class="channel-ellipsis" title="<%=item.contactName%>"><%=item.contactName%></div></td>
            <td><%=item.mobile%></td>
            <td><div class="channel-ellipsis" title="<%=item.address%>"><%=item.address%></div></td>
            <td><div class="channel-ellipsis" title="<%=item.note%>"><%=item.note%></div></td>
            <td><a href="javascript:;" class="edit js-del">删除</a>
                <a href="javascript:;" class="exit js-edit">修改</a>
            </td>
        </tr>
        <%}%>
        <%}%>
        </tbody>
    </table>
</div>
    <!-- 分页容器 start -->
    <div class="yqx-page" id="tablePage"></div>
    <!-- 分页容器 end -->
</script>
<!--新增弹窗-->
<script type="text/html" id="editTpl">
    <div class="tank">
        <div class="btn_group">
            新增渠道商
        </div>
        <div class="t_middle">
            <ul>
                <li>
                    <div class="form-label dialog-lable-width">
                        渠道商名称:
                    </div>
                    <div class="form-item dialog-form-width">
                        <input type="text" name="" id="channelName"  class="yqx-input yqx-input-small" value="<%=channelName%>" placeholder="">
                    </div>
                </li>
                <li>
                    <div class="form-label dialog-lable-width">
                        渠道商类型:
                    </div>
                    <div class="form-item dialog-form-width">
                        <input type="text" name="" id="channelType" class="yqx-input yqx-input-icon yqx-input-small js-downlist" value="<%=channelType%>" placeholder="">
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                </li>
                <li>
                    <div class="form-label dialog-lable-width">
                        联系人:
                    </div>
                    <div class="form-item dialog-form-width">
                        <input type="text" name="" id="contactName" class="yqx-input yqx-input-small" value="<%=contactName%>" placeholder="">
                    </div>
                </li>
                <li>
                    <div class="form-label dialog-lable-width">
                        联系电话:
                    </div>
                    <div class="form-item dialog-form-width">
                        <input type="text" name="" id="mobile" class="yqx-input yqx-input-small" value="<%=mobile%>" placeholder="">
                    </div>
                </li>
                <li>
                    <div class="form-label dialog-lable-width">
                        联系地址:
                    </div>
                    <div class="form-item dialog-form-width">
                        <input type="text" name="" id="address" class="yqx-input yqx-input-small" value="<%=address%>" placeholder="">
                    </div>
                </li>
                <li>
                    <div class="form-label dialog-lable-width">
                        备注:
                    </div>
                    <div class="form-item dialog-form-width">
                        <textarea class="yqx-textarea" name="" id="note" cols="50" rows="3"><%=note%></textarea>
                    </div>
                </li>
            </ul>
        </div>
        <div class="t-bottom">
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save" data-channel-id="<%=channelId%>">提交</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-back">返回</button>
        </div>
    </div>
</script>

<#--编辑弹窗-->
<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/partner/channel.js?bedfaddbfe581ed0a11b633bb900da2c"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">