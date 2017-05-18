<#include "yqx/layout/header.ftl" >
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/wechat/rescue-assessment-list.css?d796283fb8a0920e593f35a5188fffc9"/>
<link rel="stylesheet" href="${BASE_PATH}/static/third-plugin/layer/skin/layer.ext.css"/>
<div class="yqx-wrapper clearfix">
<#include "yqx/tpl/account/account-nav-tpl.ftl"/>
    <div class="order-right fl">
        <div class="order-head clearfix">
            <h1 class="headline">救援定损
                <a class="yqx-btn yqx-btn-default help clearfix" href="${BASE_PATH}/shop/help?id=92">
                    <i class="question-icon"></i><i>帮助中心</i>
                </a>
            </h1>
        </div>
        <div class="order-body">
            <!-- 查询表单区域 start -->
            <div id="searchForm">
                <div class="js-list-tab">
                    <span class="list-tab current-tab" data-type="1">救援申请</span>
                    <span class="list-tab" data-type="2">定损申请</span>
                </div>
                <div class="search-box">
                    <fieldset>
                        <div class="form-item">
                            <input class="yqx-input yqx-input-small" id="" name="search_userMobile" placeholder="请输入手机号">
                        </div>
                        <div class="form-item">
                            <input type="text" class="yqx-input yqx-input-small js-status" placeholder="状态">
                            <input type="hidden" name="search_processStatus">
                            <i class="fa icon-angle-down"></i>
                        </div>
                        <button class="yqx-btn js-search-btn yqx-btn-3">确定</button>
                    </fieldset>
                </div>
            </div>
            <table class="yqx-table" id="tableList"></table>
            <!-- 分页容器 start -->
            <div class="yqx-page" id="paging"></div>
            <!-- 分页容器 end -->
            <div id="map-bounce" hidden>
                <div class="bounce-bg"></div>
                <div class="bounce-wrapper">
                    <h3 class="bounce-title">地图
                        <span class="layui-layer-setwin">
                            <a class="js-map-close layui-layer-ico layui-layer-close layui-layer-close2" href="javascript:;"></a>
                        </span>
                    </h3>
                    <div id="map"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 救援表格模板 start -->
<script type="text/template" id="rescue-table-tpl">
    <thead>
    <tr>
        <th width="60">序号</th>
        <th>申请人</th>
        <th>申请时间</th>
        <th>状态</th>
        <th class="td-operate">操作</th>
    </tr>
    </thead>
    <tbody>
    <%if(json.data && json.data.content && json.data.content.length){%>
        <%for(var i=0;i< json.data.content.length;i++){%>
            <%var item=json.data.content[i]%>
                <tr>
                    <td><%=i+1%></td>
                    <td><%= item.userMobile%></td>
                    <td><%= item.applyTimeStr%></td>
                    <td class="processStatus"><%= ['未处理','已确认','申请已取消'][item.processStatus]%></td>
                    <td class="td-operate">
                        <div>
                            <a class="js-is-fold" href="javascript:;">展开</a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="5" class="apply-detail-box">
                        <div class="apply-detail"  hidden="hidden">
                            <div class="apply-tag"></div>
                            <h3>用户地址</h3>
                            <p><%= item.userAddress%>
                                <a class="js-view-map" href="javascript:;" data-longitude="<%=item.userLongitude%>"
                                   data-latitude="<%=item.userLatitude%>">[地图查看]</a>
                            </p>
                            <div class="apply-detail-footer">
                                <%if(item.processStatus==0){%>
                                    <button class="yqx-btn yqx-btn-3 marR10 js-confirm-rescue" data-id="<%= item.id%>">确认</button>
                                    <button class="yqx-btn yqx-btn-1 marR10 js-cancel-rescue" data-id="<%= item.id%>">取消</button>
                                <%}else{%>
                                    <button class="yqx-btn disable marR10"><%= (item.processStatus == 1 ?'已确认': '申请已取消')%></button>
                                <%}%>
                            </div>
                        </div>
                    </td>
                </tr>
        <%}%>
    <%}else{%>
        <tr>
            <td colspan="5">暂无数据！</td>
        </tr>
    <%}%>
    </tbody>
</script>
<!-- 救援表格模板 end -->

<!-- 定损表格模板 start -->
<script type="text/template" id="assessment-table-tpl">
    <thead>
    <tr>
        <th width="60">序号</th>
        <th>申请人</th>
        <th>申请时间</th>
        <th>状态</th>
        <th class="td-operate">操作</th>
    </tr>
    </thead>
    <tbody>
    <%if(json.data && json.data.content && json.data.content.length){%>
        <%for(var i=0;i< json.data.content.length;i++){%>
            <%var item=json.data.content[i]%>
                <tr>
                    <td><%=i+1%></td>
                    <td><%= item.userMobile%></td>
                    <td><%= item.applyTimeStr%></td>
                    <td class="processStatus"><%= ['未处理','已确认','申请已取消'][item.processStatus]%></td>
                    <td class="td-operate">
                        <div>
                            <a class="js-is-fold" href="javascript:;">展开</a>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td colspan="5" class="apply-detail-box">
                        <div class="apply-detail"  hidden="hidden">
                            <div class="apply-tag"></div>
                            <h3>车辆照片</h3>
                            <% if(item.imageUrls != null){%>
                                <% var imgArr = item.imageUrls.split(',');%>
                            <%}%>
                            <div class="clearfix">
                                <%if (!!imgArr && imgArr[0]!=""){%>
                                    <div class="car-photos-group" id="car-photos<%=i%>">
                                    <%for(var j = 0; j < imgArr.length; j++){%>
                                        <img class="car-photo-item" layer-src="<%=imgArr[j]%>" src="<%=imgArr[j]%>" alt=""/>
                                    <%}%>
                                    </div>
                                <%}else{%>
                                    <img class="car-photo-default"/>
                                <%}%>
                            </div>

                            <div class="apply-detail-footer">
                                <%if(item.processStatus==0){%>
                                    <button class="yqx-btn yqx-btn-3 marR10 js-confirm-assessment" data-id="<%= item.id%>">确认</button>
                                    <button class="yqx-btn yqx-btn-1 marR10 js-cancel-assessment" data-id="<%= item.id%>">取消</button>
                                <%}else{%>
                                    <button class="yqx-btn disable marR10"><%= (item.processStatus == 1 ?'已确认': '申请已取消')%></button>
                                <%}%>
                            </div>
                        </div>
                    </td>
                </tr>
        <%}%>
    <%}else{%>
        <tr>
            <td colspan="5">暂无数据！</td>
        </tr>
    <%}%>
    </tbody>
</script>
<!-- 定损表格模板 end -->
<script src="https://api.map.baidu.com/api?v=2.0&amp;ak=cSGY6d9xfcvZdNIdGA38Y9zZ&amp;s=1"></script>
<script src="${BASE_PATH}/static/js/page/wechat/rescue-assessment-list.js?0275951842c935a354818f457652c1c9"></script>
<#include "yqx/layout/footer.ftl" >