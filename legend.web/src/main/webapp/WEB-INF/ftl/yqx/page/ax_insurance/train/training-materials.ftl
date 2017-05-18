<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/page/ax_insurance/train/traning-materials.css?120c164292cfca4ffb05122123d4921a"/>
<!-- 样式引入区 end-->
<body>
<div class="yqx-wrapper clearfix">
<#include "yqx/page/ax_insurance/left-nav.ftl">
    <div class="aside-main fl">
        <div class="body-header">
            <h2 class="headline">服务包培训资料</h2>
        </div>
        <div class="order-body">
            <div class="control-group">
                <div class="tags-box max-tags-height js-tags-box" id="packageTagsContainer"></div>
                <div class="divide">
                    <span class="more-data js-more-data">更多</span>
                </div>
            </div>
            <div id="packageDetailContainer"></div>
        </div>
    </div>
</div>

<script type="text/html" id="passwordTpl">
    <style>
        .dialog-login-content {
            width: 200px;
            margin: 0 auto;
        }

        .dialog-login-label {
            line-height: 36px;
        }

        .dialog-login-error {
            height: 30px;
            line-height: 1.4;
            text-align: center;
            color: #f35f62;
        }

        .dialog-login-btns {
            text-align: center;
        }
    </style>
    <article class="yqx-dialog">
        <header class="yqx-dialog-header">
            <h1 class="yqx-dialog-headline">密码</h1>
        </header>
        <section class="yqx-dialog-body">
            <div class="dialog-login-content">
                <label class="dialog-login-label">请输入系统登录密码：</label>
                <input type="text" class="yqx-input yqx-input-block js-dialog-login-pwd" placeholder="请输入" autocomplete="off">
                <p class="dialog-login-error js-dialog-login-err"></p>
                <div class="dialog-login-btns">
                    <button class="yqx-btn yqx-btn-3 js-dialog-password-btn">确认</button>
                    <a href="${BASE_PATH}/home" class="yqx-btn yqx-btn-1 js-go-back-btn">返回</a>
                </div>
            </div>
        </section>
    </article>
</script>

<script type="text/html" id="packageTagsTpl">
    <% if (data && data.length) { %>
    <% for (var i = 0; i < data.length; i++) { %>
    <% var item = data[i]; %>
    <div class="package-tags-group clearfix">
        <div class="package-tags-label"><%= item.suitablePrice + ':' %></div>
        <ul class="package-tags js-package-tags">
            <% if (item.insuranceServicePackageDTOList && item.insuranceServicePackageDTOList.length) { %>
            <% for (var j = 0; j < item.insuranceServicePackageDTOList.length; j++) { %>
            <% subItem = item.insuranceServicePackageDTOList[j]; %>
            <li class="js-package-tag" data-id="<%= subItem.id %>"><%= subItem.packageName %></li>
            <%} }%>
        </ul>
    </div>
    <% } } %>
</script>

<script type="text/html" id="packageDetailTpl">
    <% if (data) { %>
    <div class="package-detail-box">
        <h3 class="title">服务包信息</h3>
        <dl class="package-detail">
            <dd>
                <label class="fl">服务包名称：</label>
                <p class="package-info"><%= data.packageName %></p>
            </dd>
            <dd>
                <label class="fl">服务包描述：</label>
                <p class="package-info"><%= data.description %></p>
            </dd>
            <dd>
                <label class="fl">服务包适配保费区间：</label>
                <p class="package-info"><%= data.suitableStartPrice + "元 ≤ 商业保费 ≤ " + data.suitableEndPrice + "元" %></p>
            </dd>
            <dd>
                <label class="fl">服务包市场价：</label>
                <p class="package-info"><%= data.marketPrice %>元</p>
            </dd>
            <dd>
                <label class="fl">物料：</label>
                <p class="package-info">由淘汽送货上门</p>
            </dd>
            <dd>
                <label class="fl">现金收入：</label>
                <p class="package-info"><%= data.rewardAmount %>元</p>
            </dd>
        </dl>
    </div>
    <div class="package-detail-box">
        <h3 class="title">项目信息</h3>
        <% if (data.serviceItemList && data.serviceItemList.length) { %>
        <% for (var i = 0; i < data.serviceItemList.length; i++) { %>
        <% var item = data.serviceItemList[i]; %>
        <% var materialModelList = item.materialModelList; %>
        <div class="project-box">
            <h4 class="project-title">项目<%= i + 1 %></h4>
            <dl class="package-detail">
                <dd>
                    <label class="fl">项目名称：</label>
                    <p class="package-info"><%= item.itemName %></p>
                </dd>
                <dd>
                    <label>项目市场价：</label>
                    <span class="package-info inline-divide"><%= item.itemPrice %>元</span>
                    <label>型号：</label>
                    <span class="package-info inline-divide">
                        <%if (materialModelList && materialModelList.length) {%>
                        <% var materialModelSize =materialModelList.length; %>
                        <% for (var j = 0; j < materialModelSize; j++) { %>
                        <%= materialModelList[j]%><%if(j != materialModelSize - 1) {%>;<%}%>
                        <%} }%>
                    </span>
                    <label>单位：</label>
                    <span class="package-info inline-divide"><%= item.itemUnit %></span>
                    <label>次数：</label>
                    <span class="package-info"><%= item.serviceTimes %>次</span>
                </dd>
                <dd>
                    <label class="fl">适用车型：</label>
                    <p class="package-info"><%= item.itemCarModel %></p>
                </dd>
                <dd>
                    <label class="fl">内容与工序：</label>
                    <p class="package-info"><%= item.itemContentProcedure %></p>
                </dd>
                <dd>
                    <label class="fl">作用与优点：</label>
                    <p class="package-info"><%= item.itemActionMerit %></p>
                </dd>
                <dd>
                    <label class="fl">视频教程：</label>
                    <div class="package-info">
                        <% if (item.serviceItemVideoList && item.serviceItemVideoList.length) { %>
                        <% for (var j = 0; j < item.serviceItemVideoList.length; j++) { %>
                        <% var subItem = item.serviceItemVideoList[j]; %>
                        <% if(subItem.videoTutorial){%>
                        <a class="link" href="<%= subItem.videoTutorial %>" target="_blank">&lt;&lt;
                            <%= subItem.videoName %>&gt;&gt;</a>
                        <br/>
                        <%}%>
                        <% } } %>
                    </div>
                </dd>
            </dl>
        </div>
        <% } } %>
    </div>
    <% } %>
</script>

<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/ax_insurance/train/training-materials.js?ee21b48e0e8b34fd5f482ac6050201c9"></script>
<!-- 脚本引入区 end -->
<#include "yqx/layout/footer.ftl">