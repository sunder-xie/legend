<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/productionLineprocess-list.css?1b917c7ca3bbf21794df5a147dc4a5aa"/><!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->
    <div class="order-right fl">
        <div class="title">
            生产线管理
            <button class="yqx-btn yqx-btn-1 yqx-btn-small fr js-add-productionLine">新增</button>
        </div>
        <!-- 表格容器 start -->
        <div id="productionTableCon" class="production-table"></div>
        <!-- 表格容器 end -->
    </div>
</div>

<!-- 表格数据模板 start -->
<script type="text/template" id="productionTableTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>名称</th>
            <th>类型</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <% if(json.success && json.data){%>
        <% var content = json.data %>
        <%for(var i=0;i < content.length;i++){%>
        <% var item = content[i] %>
        <tr>
            <td>
                <div class="max-width js-show-tips"><%= item.name %></div>
            </td>
            <td><%= item.typeName %></td>
            <td>
                <div class="max-width js-show-tips"><%= item.remark %></div>
            </td>
            <td>
                <%if (item.type == 2 || item.type==4){%>
                <a href="javascript:;" class="color-blue js-scheduling" data-eid="<%= item.id %>">排班</a>
                <%}else{%>
                <a href="javascript:;" class="color-blue Z-manager-btn" data-eid="<%= item.id %>">排班</a>
                <%}%>
                <a href="javascript:;" class="color-green js-edit" data-eid="<%= item.id %>">编辑</a>
                <a href="javascript:;" class="color-red js-del" data-eid="<%= item.id %>">删除</a>
            </td>
        </tr>
        <%}}%>
        </tbody>
    </table>
</script>

<!--排班管理-事故线-->
<script type="text/html" id="schedulingDialogAccident">
<div class="dialog">
    <div class="dialog-title"><%= json.data.lineName %>排班管理</div>
    <div class="dialog-con">
        <div class="con-detail">
        <%if(json.success && json.data){%>
        <%for(var i=0; i < json.data.plineProcessVOList.length; i++){%>
        <%var item = json.data.plineProcessVOList[i]%>
        <div class="form-box">
            <div class="form-label">
                <%=item.processName%>
            </div>
            <div class="form-item-box">
                <%if( item.lineProcessManagerVOList.length > 0){%>
                <%for(var j=0; j < item.lineProcessManagerVOList.length; j++){%>
                <%var subItem = item.lineProcessManagerVOList[j]%>
                <div class="form-btn-box">
                    <input type="hidden" value="<%= subItem.id %>" name="lineProcessManagerId"/>
                    <input type="hidden" value="<%= item.id %>" name="lineProcessId"/>
                    <input type="hidden" value="<%= subItem.isDeleted %>" name="isDeleted"/>
                    <div class="form-item">
                        <input type="hidden" value="<%=subItem.teamId%>" name="teamId"/>
                        <input type="hidden" value="<%=subItem.teamName%>" name="teamName"/>
                        <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small teamName" value="<%=subItem.teamName%>" placeholder="请选择" disabled>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <a href="javascript:;" class="color-grey js-remove-team">删除</a>
                </div>
                <%}%>
                <%}else{%>
                <div class="form-btn-box">
                    <input type="hidden" value="" name="id"/>
                    <input type="hidden" value="<%= item.id %>" name="lineProcessId"/>
                    <input type="hidden" value="" name="isDeleted"/>
                    <div class="form-item">
                        <input type="hidden" value="" name="teamId"/>
                        <input type="hidden" value="" name="teamName"/>
                        <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small teamName" value="" placeholder="请选择" disabled>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <a href="javascript:;" class="color-grey js-remove-team">删除</a>
                </div>
                <%}%>
            </div>
            <a href="javascript:;" class="color-green js-add-team add-team">新增</a>
        </div>
        <%}}%>
        </div>
        <div class="dialog-btn-box">
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-scheduling-save">保存</button>
        </div>
    </div>
</div>
</script>

<!--添加事故线班组模板-->
<script type="text/html" id="teamSelect">
    <div class="form-btn-box">
        <input type="hidden" value="" name="lineProcessManagerId"/>
        <input type="hidden" value="<%= lineProcessId %>" name="lineProcessId"/>
        <input type="hidden" value="" name="isDeleted"/>
        <div class="form-item">
            <input type="hidden" value="" name="teamId"/>
            <input type="hidden" value="" name="teamName"/>
            <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small teamName" value="" placeholder="请选择" disabled>
            <span class="fa icon-angle-down icon-small"></span>
        </div>
        <a href="javascript:;" class="color-grey js-remove-team">删除</a>
    </div>
</script>


<!--排班管理（快修线、快喷线）-->
<script type="text/html" id="schedulingDialog">
    <div class="dialog">
        <div class="dialog-title"><%= json.data.lineName %>排班管理</div>
        <div class="dialog-con">
            <div class="con-detail">
            <% if(json.success && json.data){%>
            <% var list = json.data %>
            <% var plineProcessVOList = list.plineProcessVOList %>
            <% for(var i=0;i < plineProcessVOList.length;i++){%>
            <div class="form-box">
                <div class="form-label">
                    <%= plineProcessVOList[i].processName %>
                </div>
                <div class="form-item-box">
                    <% var lineProcessManagerVOList = plineProcessVOList[i].lineProcessManagerVOList %>
                    <% if(lineProcessManagerVOList.length == 0){%>
                    <div class="form-btn-box" data-lineprocessid="<%= plineProcessVOList[i].id%>" data-line-process-manager-id="">
                        <input type="hidden" value="" name="isDeleted"/>
                        <div class="form-item form-item-quick">
                            <input type='text' name='paintLevelName' class='yqx-input yqx-input-icon yqx-input-small item-type-select' value='' placeholder='' readonly>
                            <div class='yqx-select-options' style='width: 169px; display: none;'></div>
                            <span class='fa item-type-down icon-small icon-angle-down'></span>
                        </div>
                        <span>-</span>
                        <div class='form-item form-item-quick'>
                            <input type='text' name='paintLevelName' class='yqx-input yqx-input-icon yqx-input-small item-members-select' value='' placeholder='' readonly>
                            <div class='yqx-select-options' style='width: 169px; display: none;'></div>
                            <span class='fa member-type-down icon-small icon-angle-down'></span>
                        </div>
                        <a href="javascript:;" class="color-grey Z-scheduling-del">删除</a>
                    </div>
                    <%}else{%>
                    <% for(var j=0;j < lineProcessManagerVOList.length; j++){%>
                    <div class="form-btn-box" data-line-process-manager-id="<%= lineProcessManagerVOList[j].id%>" data-lineprocessid="<%= lineProcessManagerVOList[j].lineProcessId%>">
                        <input type="hidden" value="" name="isDeleted"/>
                        <div class="form-item form-item-quick">
                            <input type="text" name="paintLevelName" class="yqx-input yqx-input-icon yqx-input-small item-type-select" value="<%= lineProcessManagerVOList[j].teamName%>" data-teamid="<%= lineProcessManagerVOList[j].teamId%>" placeholder="" readonly>
                            <div class='yqx-select-options' style='width: 169px; display: none;'></div>
                            <span class="fa icon-angle-down icon-small item-type-down"></span>
                        </div>
                        <span>-</span>
                        <div class="form-item form-item-quick">
                            <input type="text" name="paintLevelName" class="yqx-input yqx-input-icon yqx-input-small item-members-select" value="<%= lineProcessManagerVOList[j].managerName%>" data-managerid="<%= lineProcessManagerVOList[j].managerId%>" placeholder="" readonly>
                            <div class='yqx-select-options' style='width: 169px; display: none;'></div>
                            <span class="fa member-type-down icon-small icon-angle-down"></span>
                        </div>
                        <a href="javascript:;" class="color-grey Z-scheduling-del">删除</a>
                    </div>
                    <%}}%>
                </div>
                <a href="javascript:;" class="color-green Z-scheduling-add">新增</a>
            </div>
            <%}}%>
            </div>
            <div class="dialog-btn-box">
                <button class="yqx-btn yqx-btn-3 yqx-btn-small Z-save">保存</button>
            </div>
        </div>
    </div>
</script>

<!--排班管理（快修线、快喷线）添加事故线班组模板-->
<script type="text/html" id="teamSelectQuick">
    <div class="form-btn-box" data-lineprocessid="<%= lineProcessId %>" data-line-process-manager-id="">
        <input type="hidden" value="" name="isDeleted"/>
        <div class="form-item form-item-quick">
            <input type='text' name='paintLevelName' class='yqx-input yqx-input-icon yqx-input-small item-type-select' value='' placeholder='' readonly>
            <div class='yqx-select-options' style='width: 169px; display: none;'></div>
            <span class='fa item-type-down icon-small icon-angle-down'></span>
        </div>
        <span> - </span>
        <div class='form-item form-item-quick'>
            <input type='text' name='paintLevelName' class='yqx-input yqx-input-icon  yqx-input-small item-members-select' value='' placeholder='' readonly>
            <div class='yqx-select-options' style='width: 169px; display: none;'></div>
            <span class='fa member-type-down icon-small icon-angle-down'></span>
        </div>
        <a href="javascript:;" class="color-grey Z-scheduling-del">删除</a>
    </div>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/productionLineprocess-list.js?44abd30ee3c368eeb4a51beb293c1e6a"></script>
<script  src="${BASE_PATH}/static/js/page/magic/workshop/productionline-list.js?cb871a812f4b846d90eb59a2ec553225"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">