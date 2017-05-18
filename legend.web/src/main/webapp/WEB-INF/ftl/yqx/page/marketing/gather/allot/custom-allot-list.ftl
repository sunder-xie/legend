<!-- 自定义分配客户页 -->
<#include "yqx/layout/header.ftl">
<!-- 样式引入区 start-->
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/allot-common.css?59e8db8de7ecdcc00ce4f3de8c5464aa"/>
<link rel="stylesheet" href="${BASE_PATH}/static/css/page/marketing/gather/allot/custom-allot-list.css?14bbf953cd546caf07a22e114e9b9c63">
<!-- 样式引入区 end-->

<div class="yqx-wrapper clearfix">

    <!-- 左侧导航区 start -->
    <div class="asdie fl">
    <#include "yqx/page/marketing/left-nav.ftl">
    </div>
    <!-- 左侧导航区 end -->

    <!-- 右侧内容区 start -->
    <div class="aside-main fl">
        <!-- 标题 start -->
        <h3 class="headline">分配客户页</h3>
        <div class="staff-box box clearfix">
            <label>选择员工:</label><div class="staff-list">
               <div class="choose-staff js-add-staff">+</div>
        </div>
        </div>
        <div class="box filter-box clearfix">
            <div class="title">
                <p><strong>自定义分配客户：</strong>待分配客户：<i class="money-font total-allot-num">${freeSummary.sum}</i>个</p>
            </div>
            <div class="filter-prop fl">
                <div class="filter-title">
                    <label>客户筛选</label>
                    <button class="reset-btn js-reset btn">重置</button>
                </div>
                <div class="filter-detail">
                    <ul>
                        <li class="clearfix">
                            <h5>行为属性</h5>
                            <input type="button" value="消费金额"
                                   data-target=".xfje&.tjsj"
                                   class="filter-btn js-filter">
                            <input type="button" value="单车产值"
                                   data-target=".dccz&.tjsj"
                                   class="filter-btn js-filter">
                            <input type="button" value="消费次数"
                                   data-target=".xfcs&.tjsj"
                                   class="filter-btn js-filter">
                            <input type="button" value="未到店天数"
                                   data-target=".wddsj"
                                   class="filter-btn js-filter">
                        </li>
                        <li class="clearfix">
                            <h5>客户属性</h5>

                            <p class="clearfix">
                                <input type="button" value="车辆级别"
                                       data-target=".cljb"
                                       class="filter-btn js-filter">
                                <input type="button" value="行驶里程"
                                       data-target=".xslc"
                                       class="filter-btn js-filter">
                                <input type="button" value="会员卡类型"
                                       data-target=".hyklx"
                                       class="filter-btn js-filter">
                                <input type="button" value="车牌"
                                       data-target=".cp"
                                       class="filter-btn js-filter">
                                <input type="button" value="车型"
                                       data-target=".cx"
                                       class="filter-btn js-filter">
                                <input type="button" value="车主电话"
                                       data-target=".czdh"
                                       class="filter-btn js-filter">
                            </p>

                            <p class="clearfix">
                                <input type="button" value="客户单位"
                                       data-target=".khdw"
                                       class="filter-btn js-filter">
                            </p>
                        </li>
                        <li class="clearfix">
                            <h5>系统属性</h5>
                            <input type="button" value="新老客户"
                                   data-target=".xlkh&.tjsj"
                                   class="filter-btn js-filter">
                            <input type="button" value="客户标签"
                                   data-target=".khbq"
                                   class="filter-btn js-filter">
                            <input type="button" value="活跃度"
                                   data-target=".hyd"
                                   class="filter-btn js-filter">
                        </li>
                    </ul>
                </div>
            </div>
            <div class="filter-condition fl" id="form">
                <div class="title-box">
                    <h4 class="filter-title">搜索条件</h4>
                </div>

                <ul class="condition-container">
                    <li class="tjsj">
                        <div class="col-3">统计时间：</div>

                        <div class="col-9">
                            <div class="form-item col-5">
                                <input class="yqx-input" id="startTime" name="search_sTime" type="text"/>
                            </div>
                            <div class="col-2 center">至</div>
                            <div class="form-item col-5">
                                <input class="yqx-input" id="endTime" name="search_eTime">
                            </div>
                        </div>
                    </li>
                    <li class="hide xfje">
                        <div class="col-3">消费金额：</div>
                        <select class="col-3 js-self-define js-xfje">
                            <option value="">自定义</option>
                            <option value="0-1000">0-1000元</option>
                            <option value="1000-3000">1000-3000元</option>
                            <option value="3000-5000">3000-5000元</option>
                            <option value="5000-10000">5000-10000元</option>
                            <option value="10000">10000元以上</option>
                        </select>

                        <p class="col-1 invisibility">empty</p>

                        <div class="col-5 self-define">
                            <div class="form-item col-5">
                                <input class="yqx-input" name="search_minAmount"
                                       type="text">
                            </div>
                            <div class="col-2 center">至
                            </div>
                            <div class="form-item col-5">
                                <input class="yqx-input w_48 inputMax param"
                                       name="search_maxAmount" type="text">
                            </div>
                        </div>
                    </li>

                    <li class="hide dccz">
                        <div class="col-3">单车产值：</div>
                        <select class="col-3 js-self-define js-dccz">
                            <option value="">自定义</option>
                            <option value="0-100">0-100元</option>
                            <option value="101-300">101-300元</option>
                            <option value="301-500">301-500元</option>
                            <option value="501-700">501-700元</option>
                            <option value="701-1000">701-1000元</option>
                            <option value="1000">1000元以上</option>
                        </select>

                        <p class="col-1 invisibility">empty</p>

                        <div class="col-5 self-define">
                            <div class="form-item col-5">
                                <input class="yqx-input" name="search_minAverage"
                                       type="text">
                            </div>
                            <div class="col-2 center">至</div>
                            <div class="col-5">
                                <input class="yqx-input"
                                       name="search_maxAverage" type="text">
                            </div>
                        </div>
                    </li>

                    <li class="hide xfcs">
                        <div class="col-3">消费次数：</div>
                        <select name="search_numberSign" no_submit="true" class="col-4">
                            <option value="<=">&lt;=</option>
                            <option value=">=">&gt;=</option>
                        </select>

                        <div class="col-1 invisibility">empty</div>
                        <div class="form-item col-4">
                            <input class="yqx-input" name="search_number"
                                   data-v-type="number"
                                   type="text">
                        </div>
                    </li>

                    <li class="hide wddsj">
                        <div class="col-3">未到店天数：</div>
                        <select name="search_daySign" no_submit="true" class="col-4">
                            <option value=">=">&lt;=</option>
                            <option value="<=">&gt;=</option>
                        </select>

                        <div class="col-1 invisibility">empty</div>
                        <div class="form-item col-4">
                            <input class="yqx-input" name="search_day"
                                   data-v-type="number"
                                   type="text">
                        </div>
                    </li>

                    <li class="hide cljb">
                        <div class="col-3">车辆级别：</div>
                        <select name="search_carLevelTag"  no_submit="true" class="col-9">
                            <option value="1">低端（0～10万）</option>
                            <option value="2">中端（10～30万）</option>
                            <option value="3">高端（30万以上）</option>
                        </select>
                    </li>

                    <li class="hide xslc">
                        <div class="col-3">行驶里程：</div>
                        <select class="col-3 js-self-define js-xslc">
                            <option value="">自定义</option>
                            <option value="0-30000">0-3万</option>
                            <option value="30000-50000">3-5万</option>
                            <option value="50000-100000">5-10万</option>
                            <option value="100000">10万以上</option>
                        </select>

                        <p class="col-1 invisibility">empty</p>

                        <div class="col-5 self-define">
                            <div class="form-item col-5">
                                <input class="yqx-input" name="search_minMileage"
                                       type="text">
                            </div>
                            <div class="col-2 center">至</div>
                            <div class="form-item col-5">
                                <input class="yqx-input"
                                       name="search_maxMileage" type="text">
                            </div>
                        </div>
                    </li>

                    <li class="hide hyklx">
                        <div class="col-3">
                            会员卡类型：
                        </div>
                        <select class="col-9" name="search_memberLevelId">
                            <#list memberCardInfoList as memberCardInfo>
                                <option value="${memberCardInfo.id}">${memberCardInfo.typeName}</option>
                            </#list>
                        </select>
                    </li>

                    <li class="hide cp">
                        <div class="col-3">车牌：</div>
                        <div class="col-9 form-item">
                            <input class="yqx-input" name="search_carLicense"
                                   type="text">
                        </div>
                    </li>

                    <li class="hide cx">
                        <div class="col-3">车型：</div>
                        <div class="form-item col-9">
                            <input class="yqx-input param" name="search_carType" type="text">
                        </div>
                    </li>

                    <li class="hide czdh">
                        <div class="col-3">车主电话：</div>
                        <div class="form-item col-9">
                            <input class="yqx-input param" name="search_mobile" type="text">
                        </div>
                    </li>

                    <li class="hide khdw">
                        <div class="col-3">客户单位：</div>
                        <div class="form-item col-9">
                            <input class="yqx-input param" name="search_customerCompany" type="text">
                        </div>
                    </li>

                    <li class="hide xlkh">
                        <div class="col-3">新老客户：</div>
                        <select name="search_tag" no_submit="true" class="col-4">
                            <option value="new">新客户</option>
                            <option value="old">老客户</option>
                        </select>
                    </li>

                    <li class="hide khbq">
                        <div class="col-3">客户标签：</div>

                        <div class="col-6 form-item">
                            <input class="yqx-input param js-filter-input" name="search_customerTag" type="text">
                        </div>
                    </li>

                    <li class="hide hyd">
                        <div class="col-3">活跃度：</div>
                        <select name="search_tag" no_submit="true" class="col-4">
                            <option value="active">活跃</option>
                            <option value="sleep">休眠</option>
                            <option value="lost">流失</option>
                        </select>
                    </li>
                    <input name="size" value="10" type="hidden" class="hide">
                    <input name="search_allot" value="true" type="hidden" class="hide js-search-allot">
                </ul>

                <div class="filter-footer">
                    <button class="js-search-btn search-btn btn">搜索</button>
                    <button class="btn js-reset-condition reset-btn">重置</button>
                </div>
            </div>
        </div>
        <div class="table-box box">
            <div class="table-caption">
                <i class="title">待分配客户列表</i>

                <button class="fr yqx-btn js-allot allot-btn">平均分配</button>
                <div class="page-select-box fr">
                    <i>每页</i><div class="pre-page icon-angle-down">
                        <input class="page-input" value="10条" readonly>
                        <ul class="js-pre-page page-list">
                            <li data-value="10">10条</li>
                            <li data-value="30">30条</li>
                            <li data-value="50">50条</li>
                        </ul>
                    </div>
                </div>
            </div>
            <div id="fill"></div>
            <div class="yqx-page" id="page"></div>
            <input type="hidden" class="need-allot-num"/>
        </div>

        <!-- 标题 end -->
    </div>
    <!-- 右侧内容区 end -->
</div>
<!-- 表格模板 start -->
<script id="tpl" type="text/template">
    <%
    var ljxfje = isContained(['xfje', 'xlkh', 'hyd']),
    ljwddsj = isContained(['wddsj']),
    dccz = isContained(['dccz']),
    xfcs = isContained(['xfcs', 'xlkh', 'hyd']),
    hyjb = isContained(['hyjb']),
    xslc = isContained(['xslc']),
    cljb = isContained(['cljb']),
    cx = isContained(['cx']),
    khbq = isContained(['khbq'])
    ;
    %>
    <table class="yqx-table">
        <thead>
        <tr>
            <th class="car-license">车牌</th>
            <th class="width-100">车主</th>
            <th class="width-100">车主电话</th>
            <th>车型</th>
            <% if(!cx && !khbq) {%>
            <th>最近消费时间</th>
            <%}%>
            <% if( cljb ) { %>
            <th>车辆级别</th>
            <% } if( ljxfje ) { %>
            <th>累计消费金额</th>
            <% } if( ljwddsj ) { %>
            <th>累计未到店时间</th>
            <% } if( dccz ) { %>
            <th>单车产值</th>
            <% } if( xfcs ) { %>
            <th>消费次数</th>
            <% } if( hyjb ) { %>
            <th>会员卡类型</th>
            <% } if( xslc ) { %>
            <th>行驶里程</th>
            <% } %>
        </tr>
        </thead>
        <tbody>
        <% var item, len, i; %>
        <% if(json.success && json.data && json.data.content && (len = json.data.content.length)) { %>
        <% for(i = 0; i < len; i++) { %>
        <% item = json.data.content[i] %>
        <tr>
            <td class="car-license">
                <a class="revisit-btn" target="_blank"
                   href="/legend/shop/customer/info?cid=<%= item.customerCarId%>">
                    <%= item.carLicense %>
                </a>
            </td>
            <td class="js-show-tips ellipsis-1 width-100"><%= item.customerName %></td>
            <td class="width-100"><%= item.mobile %></td>
            <td class="js-show-tips ellipsis-1">
                <%= item.carModel %></td>
            <% if(!cx && !khbq) {%>
            <td><%= item.lastPayTimeStr %></td>
            <%}%>
            <% if( cljb ) { %>
            <td><%= item.carLevel %></td>
            <% } if( ljxfje ) { %>
            <td><%= item.totalAmount %></td>
            <% } if( ljwddsj ) { %>
            <td><%= item.lastPayDays %></td>
            <% } if( dccz ) { %>
            <td><%= item.average %></td>
            <% } if( xfcs ) { %>
            <td><%= item.totalNumber %></td>
            <% } if( hyjb ) { %>
            <td><%= item.memberCardTypeName %></td>
            <% } if( xslc ) { %>
            <td><%= item.mileage %></td>
            <% } %>
        </tr>
        <% }} %>
        </tbody>
    </table>
</script>
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

<div class="yqx-dialog allot-result-dialog hide">
    <div class="dialog-title">分配结果</div>
    <div class="dialog-content">
        <p class="result">成功分配客户<i class="num"></i>个</p>
        <div class="table-box">
        <table class="yqx-table table-striped-inverse">
            <thead>
            <tr>
                <th>员工</th>
                <th>本次分配</th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>
            </div>
    </div>
    <div class="dialog-footer">
        <button class="js-confirm yqx-btn yqx-btn-3">确定</button>
    </div>
</div>
<!-- 脚本引入区 start -->
<script src="${BASE_PATH}/static/js/page/marketing/gather/allot/custom-allot-list.js?025919e664838beee82b31e6fe2ec839"></script>
<#include "yqx/layout/footer.ftl">