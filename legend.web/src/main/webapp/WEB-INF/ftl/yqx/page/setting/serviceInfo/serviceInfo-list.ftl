<#include "yqx/layout/header.ftl">

<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/common/setting/setting-common.css?24d0a2d0c7ce2e1062bbd962e59547a8"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/setting/serviceInfo/serviceInfo-list.css?266b7077938ef0e7e1e5d8433457ca04"/>
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="order-left fl">
    <#include "yqx/tpl/setting/setting-nav-tpl.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="order-right fl">
        <div class="order-head">
            <h3 class="headline">服务资料</h3>
        </div>
        <div class="content">
            <div class="form-box" id="formData">
                <div class="form-item">
                    <input type="text" class="yqx-input yqx-input-icon yqx-input-small js-service-category" placeholder="服务类别">
                    <input type="hidden" name="categoryId">
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options">
                        <dl>
                            <dd class="yqx-select-clean">请选择</dd>
                        <#list shopServiceCateList as shopServiceCate >
                            <dd class="yqx-select-option js-show-tips" data-key="${shopServiceCate.id}">${shopServiceCate.name}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-car-level" placeholder="车辆级别">
                    <input type="hidden" name="carLevelId">
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options">
                        <dl>
                            <dd class="yqx-select-clean">请选择</dd>
                        <#list carLevelList as carLevel >
                            <dd class="yqx-select-option js-show-tips" data-key="${carLevel.id}">${carLevel.name}</dd>
                        </#list>
                        </dl>
                    </div>
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-service-type" placeholder="服务类型">
                    <input type="hidden" name="type">
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options">
                        <dl>
                            <dd class="yqx-select-clean">请选择</dd>
                            <dd class="yqx-select-option js-show-tips" data-key="1">服务资料</dd>
                            <dd class="yqx-select-option js-show-tips" data-key="2">费用资料</dd>
                        </dl>
                    </div>
                </div>
                <div class="form-item">
                    <input type="text" name="" class="yqx-input yqx-input-icon yqx-input-small js-service-flags" placeholder="服务标签">
                    <input type="hidden" name="flags">
                    <span class="fa icon-angle-down icon-small"></span>
                    <div class="yqx-select-options">
                        <dl>
                            <dd class="yqx-select-clean">请选择</dd>
                            <dd class="yqx-select-option js-show-tips" data-key="CZFW">车主服务</dd>
                            <dd class="yqx-select-option js-show-tips" data-key="BZFW">标准服务</dd>
                            <dd class="yqx-select-option js-show-tips" data-key="TQFW">淘汽服务</dd>
                        <#if SESSION_SHOP_IS_TQMALL_VERSION=='false'>
                            <dd class="yqx-select-option js-show-tips" data-key="PABQ">平安补漆</dd>
                            <dd class="yqx-select-option js-show-tips" data-key="PABY">平安保养</dd>
                            <#if SESSION_SHOP_JOIN_STATUS==1||BPSHARE=='true'||SESSION_SHOP_WORKSHOP_STATUS==1>
                                <dd class="yqx-select-option js-show-tips" data-key="BPFW">钣喷服务</dd>
                            </#if>
                        </#if>
                        </dl>
                    </div>
                </div>
                <div class="form-item">
                    <input type="text" name="keywords" class="yqx-input yqx-input-small" value="" placeholder="服务编码/名称">
                </div>
                <button class="yqx-btn yqx-btn-3 yqx-btn-small js-search-btn">查询</button>
            </div>
            <div class="btn-box">
                <a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-edit" class="yqx-btn yqx-btn-4 yqx-btn-small">新增服务</a>
                <a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-edit?suiteNum=2" class="yqx-btn yqx-btn-3 yqx-btn-small">新增服务套餐</a>
                <a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-fee-edit" class="yqx-btn yqx-btn-2 yqx-btn-small">新增费用资料</a>

                <div class="manage fr">
                    <img src="${BASE_PATH}/static/img/page/goods/img-02.png"><a href="${BASE_PATH}/shop/setting/serviceInfo/serviceCate-list">服务类别管理</a>
                </div>
            </div>
            <!-- 表格容器 start -->
            <div class="table-con">
                <table class="yqx-table yqx-table-hover">
                    <thead>
                    <tr>
                        <th class="text-l w-340">服务标签/服务名称</th>
                        <th class="text-l" width="130">服务编码</th>
                        <th class="text-l">车辆级别</th>
                        <th class="text-l">类别</th>
                        <th class="text-r">价格</th>
                        <th class="text-c">操作</th>
                    </tr>
                    </thead>
                    <tbody id="tableListCon">
                    </tbody>
                </table>
            </div>
            <!-- 表格容器 end -->

            <!-- 分页容器 start -->
            <div class="yqx-page" id="pagingCon"></div>
            <!-- 分页容器 end -->
        </div>
    </div>
</div>

<script type="text/html" id="tableTpl">
   <%if(json.success && !!json.data.content && !!json.data.content.length){%>
   <%for(var i =0 ; i< json.data.content.length; i++){%>
   <%var item = json.data.content[i]%>
    <tr data-id="<%=item.id%>" class="js-tr-edit">
        <td class="text-l w-340">
            <div class="pic">
                <%if(item.type == '2'){%>
                <img src="${BASE_PATH}/static/img/page/setting/service-ico4.png"/>
                <%}else{%>
                    <%if (item.flags == 'TQFW'){%>
                    <img src="${BASE_PATH}/static/img/page/setting/service-ico8.png"/>
                    <%}else if(item.flags == 'CZFW'){%>
                    <img src="${BASE_PATH}/static/img/page/setting/service-ico3.png"/>
                    <%}else if(item.flags == 'BZFW'){%>
                    <img src="${BASE_PATH}/static/img/page/setting/service-ico2.png"/>
                    <%}else if(item.flags == 'PABQ'){%>
                    <img src="${BASE_PATH}/static/img/page/setting/service-ico7.png"/>
                    <%}else if(item.flags == 'PABY'){%>
                    <img src="${BASE_PATH}/static/img/page/setting/service-ico7.png"/>
                    <%}else if(item.flags == 'TMFW'){%>
                    <img src="${BASE_PATH}/static/img/page/setting/service-ico6.png"/>
                    <%}else if(item.flags == 'BPFW'){%>
                    <img src="${BASE_PATH}/static/img/page/setting/service-ico1.png"/>
                    <%}else{%>
                    <img src="${BASE_PATH}/static/img/page/setting/service-ico5.png"/>
                    <%}%>
                <%}%>
            </div>
            <div class="text">
                <h3 class="js-show-tips ellipsis-1"><%=item.name%></h3>
                <%if (item.type==2){%>
                <p>附加费用</p>
                <%} else if (item.flags == 'TQFW'){%>
                <p>淘汽服务</p>
                <%}else if(item.flags == 'CZFW'){%>
                <p>车主服务</p>
                <%}else if(item.flags == 'BZFW'){%>
                <p>标准服务</p>
                <%}else if(item.flags == 'PABQ'){%>
                <p>平安补漆</p>
                <%}else if(item.flags == 'PABY'){%>
                <p>平安保养</p>
                <%}else if(item.flags == 'TMFW'){%>
                <p>天猫服务</p>
                <%}else if(item.flags == 'BPFW'){%>
                <p>钣喷服务</p>
                <%}else if(item.flags == 'MDFW'){%>
                <p>门店服务</p>
                <%}else if(item.flags == 'TAFW'){%>
                <p>天安服务</p>
                <%}else if(item.flags == 'JSFW'){%>
                <p>寄售服务</p>
                <%}%>
            </div>
        </td>
        <td class="text-l js-show-tips ellipsis-1" width="130"><%=item.serviceSn%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.carLevelName%></td>
        <td class="text-l js-show-tips ellipsis-1"><%=item.categoryName%></td>
        <td class="text-r js-show-tips ellipsis-1">
            <%if(item.suiteAmount){%>
                <p>套餐价</p>
                &yen;<%=item.suiteAmount%>
            <%}else{%>
                &yen;<%=item.servicePrice%>
            <%}%>
        </td>
        <td class="text-c">
            <%if(item.type == '2'){%>
            <p><a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-fee-edit?id=<%=item.id%>" class="yqx-link-3 js-edit">编辑</a></p>
            <%}else{%>
            <p><a href="${BASE_PATH}/shop/setting/serviceInfo/serviceInfo-edit?id=<%=item.id%>" class="yqx-link-3 js-edit">编辑</a></p>
            <%}%>
            <%if (item.deleteStatus==0){%>
            <p><a href="javascript:;" class="yqx-link-2 js-delete">删除</a></p>
            <%}%>
        </td>
    </tr>
    <%}} else{%>
    <tr>
        <td colspan="6">暂无数据！</td>
    </tr>
    <%}%>
</script>

<script src="${BASE_PATH}/static/js/page/setting/serviceInfo/serviceInfo-list.js?0c56a7c991e62615ef86ee175dbc3b7c"></script>
<#include "yqx/layout/footer.ftl">
