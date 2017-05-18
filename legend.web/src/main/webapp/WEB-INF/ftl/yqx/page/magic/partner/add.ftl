<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/magic/partner/add.css?32b37f7f73cd288c628374e3c2501209"/>
<!-- 样式引入区 end-->
<div class="yqx-wrapper clearfix">
    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->
    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="btn-box">
            <button href="javascript:;" class="yqx-btn yqx-btn-2 js-save">保存</button>
            <button href="javascript:;" class="yqx-btn yqx-btn-2 js-goback">返回</button>
        </div>
        <!-- group start -->
        <div class="yqx-group">
            <!-- group标题 start -->
            <div class="yqx-group-head">
                添加股东
                <i class="group-head-line"></i>
            </div>
            <!-- group标题 end -->
            <!-- group内容 start -->
            <div class="yqx-group-content">
                <div class="search-box">
                    <div class="form-label">
                        名称
                    </div>
                    <div class="form-item search-form-width">
                        <input type="text" name="" id="search_name" class="yqx-input yqx-input-small" value="" placeholder="">
                    </div>
                    <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search">搜索</button>
                </div>
                <div class="shareholder-box clearfix">
                    <div class="sharholder fl">
                        <div class="sh-title">名称</div>
                        <div class="sh-con">
                            <ul id="leftBox" class="js-boxa">

                            </ul>
                        </div>
                    </div>
                    <div class="sharholder-pic fl">
                        <i class="icon-chevron-right"></i>
                    </div>
                    <div class="sharholder fr">
                        <div class="sh-title"><span>已选成员</span>(单机移除)</div>
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
    <%if(unJoinPartner){%>
    <%for(var i=0;i<unJoinPartner.length;i++){%>
    <%var item = unJoinPartner[i];%>
        <li partnerId="<%=item.id%>"><%=item.name%></li>
    <%}%>
    <%}%>
</script>

<!-- 脚本引入区 start -->
<script  src="${BASE_PATH}/static/js/page/magic/partner/add.js?5ff431c9587d7287df2609b582832b96"></script>
<!-- 脚本引入区 end -->

<#include "yqx/layout/footer.ftl">