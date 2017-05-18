<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/workshop/team_edit.css?6fc78c920c92f9191cb60bd1f3be592a"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->
    <div class="order-right fl">
        <input type="hidden" id="shopId"/>
        <input type="hidden" id="teamId" val=""/>
        <div class="btn-box">
            <button href="javascript:;" class="yqx-btn yqx-btn-2 js-save">保存</button>
            <button href="javascript:;" class="yqx-btn yqx-btn-2 js-goback">返回</button>
        </div>
        <!-- group start -->
        <div class="yqx-group">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                编辑班组
                <i class="group-head-line"></i>
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content">
                <div class="search-box">
                    <div class="form-label">
                        名称
                    </div>
                    <div class="form-item">
                        <input type="text" name="" id="name" class="yqx-input yqx-input-small" value="" placeholder="">
                    </div>
                    <div class="form-label">
                        备注
                    </div>
                    <div class="form-item">
                        <input type="text" name="" id="remark" class="yqx-input yqx-input-small" value="" placeholder="">
                    </div>
                </div>
                <div class="shareholder-box clearfix">
                    <div class="sharholder fl">
                        <div class="sh-title">未添加成员（勾选完成添加）</div>
                        <div class="sh-con">
                            <ul id="leftBox" class="js-boxa">

                            </ul>
                        </div>
                    </div>
                    <div class="sharholder-pic fl">
                        <i class="icon-chevron-right"></i>
                    </div>
                    <div class="sharholder fr">
                        <div class="sh-title"><span>已添加成员</span>(单击移除)</div>
                        <div class="sh-con">
                            <ul id="rightBox" class="js-boxb">

                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/html" id="LeftBoxTpl">
    <%if(json.data && json.data.shopManagerExtVOList){%>
    <%for(var i=0; i<json.data.shopManagerExtVOList.length;i++){%>
    <%var item = json.data.shopManagerExtVOList[i]%>
    <li data-id="<%=item.id%>"><%=item.managerName%></li>
    <%}}%>
</script>

<script type="text/html" id="rightBoxTpl">
    <%if(json.data && json.data.shopManagerExtVOList){%>
    <%for(var i=0; i<json.data.shopManagerExtVOList.length;i++){%>
    <%var item = json.data.shopManagerExtVOList[i]%>
    <li data-id="<%=item.id%>"><%=item.managerName%></li>
    <%}}%>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/workshop/team_edit.js?618eac01477c110ddd56b580ae823f13"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">