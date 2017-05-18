<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/partner/stockholder.css?b5076ac2da0dbf1b401d9176d9dd6f3d"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/modules/paging.css?0a59540bff205589e7dc7ad09496808e"/>
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
                股东管理
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
                        <input type="text" name="name" id ="search_name" class="yqx-input yqx-input-small" value="" placeholder="">
                    </div>
                    <div class="form-label">
                        状态
                    </div>
                    <div class="form-item">
                        <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-downlist" value="未加入" placeholder="">
                        <input type="hidden"  name="partnerStatus" id="search_partnerStatus"
                               value="0"/>
                        <span class="fa icon-angle-down icon-small"></span>
                    </div>
                    <div class="search-btns fr">
                        <a href="javascript:;" class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">搜索</a>
                        <a href="${BASE_PATH}/share/partner/add" class="yqx-btn yqx-btn-1 yqx-btn-small">添加</a>
                    </div>
                </div>
                <div id="tableDetail" class="table-detail">

                </div>
                <!--TODO 分页部分 -->
                <div class="qxy_page">
                    <div class="qxy_page_inner"></div>
                    <input type="hidden" id="tag" value="${tag}"/>
                </div>
            </div>
        </div>
    </div>
</div>
<#--表格渲染-->
<script type="text/template" id="tableTpl">
    <div class="order-list-table-inner">
        <div class="table-scroll-box">
            <table class="yqx-table yqx-table-hover">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>股东名称</th>
                    <th>结算比例</th>
                    <th>联系人</th>
                    <th>联系电话</th>
                    <th>联系地址</th>
                    <th>创建时间</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr class="js-info-btn" data-partner-id="<%=item.id%>">
                    <td><%=json.data.size*(json.data.number)+i+1%></td>
                    <td><div class="partner-ellipsis" title="<%=item.name%>"><%=item.name%></div></td>
                    <td><%=item.rate%></td>
                    <td><div class="partner-ellipsis" title="<%=item.contactName%>"><%=item.contactName%></div></td>
                    <td><%=item.mobile%></td>
                    <td><div class="partner-ellipsis" title="<%=item.address%>"><%=item.address%></div></td>
                    <td><div class="partner-ellipsis" title="<%=item.gmtCreateStr%>"><%=item.gmtCreateStr%></div></td>
                    <td><div class="partner-ellipsis" title="<%=item.note%>"><%=item.note%></div></td>
                    <td>
                        <%if(item.partnerStatus && item.partnerStatus != 0){%>
                        <a href="javascript:;" class="edit js-edit">编辑</a>
                        <a href="javascript:;" class="exit js-exit">退出</a>
                        <%}%>
                    </td>
                </tr>
                <%}%>
                <%}%>
                </tbody>
            </table>
        </div>
    </div>
</script>
<#--表格渲染-加入-->
<script type="text/template" id="tableTplJoin">
    <div class="order-list-table-inner">
        <div class="table-scroll-box">
            <table class="yqx-table yqx-table-hover">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>股东名称</th>
                    <th>联系人</th>
                    <th>联系电话</th>
                    <th>联系地址</th>
                    <th>退出时间</th>
                    <th>退出原因</th>
                    <th>备注</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <%if(json.data && json.data.content){%>
                <%for(var i=0;i<json.data.content.length;i++){%>
                <%var item = json.data.content[i];%>
                <tr class="js-info-btn" data-partner-id="<%=item.id%>">
                    <td><%=json.data.size*(json.data.number)+i+1%></td>
                    <td><div class="partner-ellipsis" title="<%=item.name%>"><%=item.name%></div></td>
                    <td><div class="partner-ellipsis" title="<%=item.contactName%>"><%=item.contactName%></div></td>
                    <td><div class="partner-ellipsis" title="<%=item.mobile%>"><%=item.mobile%></div></td>
                    <td><div class="partner-ellipsis" title="<%=item.address%>"><%=item.address%></div></td>
                    <td><div class="partner-ellipsis" title="<%=item.exitTimeStr%>"><%=item.exitTimeStr%></div></td>
                    <td><div class="partner-ellipsis" title="<%=item.reason%>"><%=item.reason%></div></td>
                    <td><div class="partner-ellipsis" title="<%=item.note%>"><%=item.note%></div></td>
                    <td><a href="javascript:;" class="edit js-join">加入</a></td>
                </tr>
                <%}%>
                <%}%>
                </tbody>
            </table>
        </div>

    </div>
</script>
<#--退出弹窗-->
<script type="text/html" id="exitTpl">
    <div class="tank">
        <div class="btn_group">
            退出
        </div>
        <div class="t-middle">
            <div class="form-label exit-title">
                请选择退出的理由:
            </div>
            <div class="textarea-box">
                <textarea class="yqx-textarea" id="quitReason" cols="10" rows="5"></textarea>
            </div>
        </div>
        <div class="t-bottom">
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-sub" data-partner-id="<%=partnerId%>">提交</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-close">返回</button>
        </div>
    </div>
</script>
<script type="text/html" id="editTpl">
    <div class="tank">
        <div class="btn_group">
            编辑
        </div>
        <div class="t_middle">
            <ul>
                <li>
                    <div class="form-label dialog-lable-width">
                        股东名称:
                    </div>
                    <div class="form-item dialog-form-width">
                        <div class="yqx-text">
                            <%=name%>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="form-label dialog-lable-width">
                        结算比例:
                    </div>
                    <div class="form-item dialog-form-width">
                        <input type="text" name="" id="rate" class="yqx-input yqx-input-small js-rate-input" value="<%=rate%>" placeholder="">
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
            <button class="yqx-btn yqx-btn-3 yqx-btn-small js-save" data-partner-id="<%=partnerId%>">提交</button>
            <button class="yqx-btn yqx-btn-1 yqx-btn-small js-back">返回</button>
        </div>
    </div>
</script>

<#--编辑弹窗-->
<!-- 脚本引入区 start -->
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>
<script  src="${BASE_PATH}/static/js/page/magic/partner/stockholder.js?78a048000e6c1e355dcf4de42e525e3e"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">