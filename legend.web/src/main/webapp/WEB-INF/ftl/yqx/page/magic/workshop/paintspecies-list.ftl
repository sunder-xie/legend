<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/paintspecies-list.css?c3b853a8d76d708e2c5c0767c0122a80"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->
    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="table_title clearfix">
            <p class="fl title">面漆种类设置</p>
            <p class="fr yqx-btn yqx-btn-1 add"><span class="icon-add">﹢</span>新增种类</p>
        </div>
        <!-- 表格容器 start -->
        <div id="tablePaint" class="table-paint"></div>
        <!-- 表格容器 end -->

        <!-- 分页容器 start -->
        <div class="yqx-page" id="pagingPaint"></div>
        <!-- 分页容器 end -->
    </div>
</div>
<!-- 表格数据模板 start -->
<script type="text/template" id="tablePaintTpl">
    <table class="yqx-table">
        <thead>
        <tr>
            <th>序号</th>
            <th>名称</th>
            <th>种类</th>
            <th>单价</th>
            <th>备注</th>
            <th>操作</th>
        </tr>
        </thead>
        <%if(json.data && json.data.content){
            var con = json.data.content,
                len = con.length;
            for(var i = 0;i < len; i++){
                var species=con[i];%>
                <tr data-shopid="<%=species.shopId%>">
                    <td><%= i+1%></td>
                    <td><%=species.name%></td>
                    <td><%=species.paintLevelName%></td>
                    <td><%=species.price%></td>
                    <td><%=species.remark%></td>
                    <td><a class="edit op" data-eid="<%=species.id%>" href="">编辑</a> <a class="remove op"  data-eid="<%=species.id%>" href="">删除</a></td>
                </tr>
            <%}%>
        <%}%>
    </table>
</script>

<!--新增面漆级别-->
<script type="text/html" id="addDialog">
    <div class="dialog">
        <div class="dialog-title">
            新增面漆种类
        </div>
        <div class="dialog-con">
            <ul>
                <li>
                    <div class="form-label require">
                        油漆名称
                    </div>
                    <div class="form-item">
                        <input type="text" name="sname" class="yqx-input" value="" placeholder="" data-v-type="required">
                    </div>
                </li>
                <li>
                    <div class="form-label require">
                        油漆级别
                    </div>
                    <div class="form-item">
                        <input type="text" name="paintLevelName" class="yqx-input yqx-input-icon paint-level-select" value="" placeholder="" data-v-type="required">
                        <span class="fa icon-angle-down"></span>
                    </div>
                </li>
                <li>
                    <div class="form-label require">
                        单价
                    </div>
                    <div class="form-item">
                        <input type="text" name="price" class="yqx-input yqx-input-icon" value="" placeholder="" data-v-type="required">
                        <span class="fa">元</span>
                    </div>
                </li>
                <li>
                    <div class="form-label">
                        备注
                    </div>
                    <div class="form-item w380">
                        <textarea class="yqx-textarea" name="remark" id="" cols="100" rows="3"></textarea>
                    </div>
                </li>
            </ul>
        </div>
        <div class="dialog_op">
            <button class="yqx-btn yqx-btn-3 yqx-btn-small save">保存</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-cancel">取消</button>
        </div>
    </div>
</script>

<!--编辑面漆级别-->
<script type="text/html" id="editDialog">
    <div class="dialog">
        <div class="dialog-title">
            修改面漆种类
        </div>
        <div class="dialog-con">
            <%if(json){%>
            <ul>
                <li>
                    <div class="form-label require">
                        油漆名称
                    </div>
                    <div class="form-item">
                        <input type="text" name="sname" class="yqx-input" value="<%=json.name%>" placeholder="" data-v-type="required">
                    </div>
                </li>
                <li>
                    <div class="form-label require">
                        油漆级别
                    </div>
                    <div class="form-item">
                        <input type="text" name="paintLevelName" class="yqx-input yqx-input-icon paint-level-select" value="<%=json.paintLevelName%>" placeholder="" data-v-type="required">
                        <span class="fa icon-angle-down"></span>
                    </div>
                </li>
                <li>
                    <div class="form-label require">
                        单价
                    </div>
                    <div class="form-item">
                        <input type="text" name="price" class="yqx-input yqx-input-icon" value="<%=json.price%>" placeholder="" data-v-type="required">
                        <span class="fa">元</span>
                    </div>
                </li>
                <li>
                    <div class="form-label">
                        备注
                    </div>
                    <div class="form-item w380">
                        <textarea class="yqx-textarea" name="remark" id="" cols="100" rows="3"><%=json.remark%></textarea>
                    </div>
                </li>
            </ul>
            <%}%>
        </div>
        <div class="dialog_op">
            <button class="yqx-btn yqx-btn-3 yqx-btn-small save" data-eid="<%= json.id%>">保存</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-cancel">取消</button>
        </div>
    </div>
</script>
<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/paintspecies-list.js?764b7c8e1f8df874147526c41f8b1816"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">