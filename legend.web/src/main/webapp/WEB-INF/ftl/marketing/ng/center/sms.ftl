<#include "layout/header.ftl" >
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/static/css/common/base.min.css?5698f550fb48dca82818ee31fd541e81" />
<link rel="stylesheet" href="${BASE_PATH}/resources/css/marketing/sms_zmx.css?ae7039531a1726a287f3af5f763fad27"/>
<div class="wrapper">
<#include "marketing/ng/left_nav.ftl"/>

    <div class="Z-right list_box">
        <h3 class="Z-title">
            客户营销 > <i>短信充值</i>

        <#if SESSION_SHOP_IS_TQMALL_VERSION == 'true'>
        <#else>
            <a class="yqx-btn yqx-btn-small yqx-btn-1 btn-title fr" href="${BASE_PATH}/marketing/ng/sms/template">短信模版管理</a>
        </#if>
        </h3>
        <!--帐户充值-->
        <div class="Recharge_box">
            <div class="Recharge_infor">
                <div class="surplus">
                    剩余短信：
                    <span>
                    <#if marketingShopRel?? && marketingShopRel.smsNum??>
                    ${marketingShopRel.smsNum}
                    <#else>
                        0
                    </#if>
                    </span>条
                </div>
                <div class="Recharge">
                    <span class="rec_title">选择充值：</span>
                    <ul class="recharge_list">
                    <#if marketingSmsRechargeTplList??>
                        <#list marketingSmsRechargeTplList as m>
                            <li data-id="${m.id}">
                                <p class="money">${m.rechargeMoney?default("未知")}元</p>
                                <p class="Discount">充值${m.buySmsNum?default("未知")}条<span>再送${m.freeSmsNum?default("未知")}条</span></p>
                                <#if m.flags == "NEW">
                                    <div class="new_tip">新</div>
                                </#if>
                            </li>
                        </#list>
                    </#if>
                    </ul>
                </div>
                <div class="pay_btn"><a class="J-Pay" href="javascript:void(0);">去支付 > </a></div>
            </div>
        </div>


        <!--充值记录-->
        <div class="Record">
            <span data-index="1" class="hover">充值记录</span>
            <span data-index="2">短信发送记录</span>
        </div>
        <div class="Record_tabcon" id="Record_tabcon"></div>
    </div>
<#--详情-->
    <div class="Z-right detail_box hide">
        <h3 class="Z-title">
            客户营销 >
            <a href="${BASE_PATH}/marketing/ng/center/sms">短信充值</a> > <i>短信发送详情</i>
        </h3>
        <div class="Recharge_box">
            <div class="detail_info">
                <h3 class="title">
                    发送内容
                </h3>
                <p id="template"></p>
                <h3 class="title">
                    发送位置
                </h3>
                <p id="position"></p>
                <h3 class="title">发送车主</h3>
                <div class="Record_tabcon" id="detail_tabcon"></div>
                <div class="qxy_page detail_page">
                    <div class="qxy_page_inner"></div>
                </div>
            </div>
        </div>

    </div>
    <!--TODO 分页部分 -->

    <div class="qxy_page list_page">
        <div class="qxy_page_inner"></div>
        <input type="hidden" id="tag" value="${tag}"/>
    </div>
</div>

<script type="text/html" id="sendContent">
    <table class="table">
        <thead>
        <tr class="tabcon_title">
            <th id="sendtime">时间</th>
            <th id="sendmoney">充值金额</th>
            <th id="sendcont">充值内容</th>
            <th>充值状态</th>
            <th id="operator">操作人</th>
        </tr>
        </thead>
        <% if (success) { %>
        <tbody>
        <% if (data && data.content && data.content.length) { %>
        <% for (var i = 0; i < data.content.length; i++) { %>
        <% var item = data.content[i]; %>
        <tr class="tabcon_list">
            <td><%= item.gmtCreateStr %></td>
            <td><%= item.payFee %></td>
            <td><p class="table-content">总计<%= item.smsNum %>条</p></td>
            <td>
                <%if( item.status==0 ){%>
                未支付
                <% }else if( item.status==1 ){%>
                支付成功
                <% }else if( item.status==2 ){%>
                支付失败
                <% } %>
            </td>
            <td><%= item.operator %></td>
        </tr>
        <% }} %>
        </tbody>
        <% } %>
    </table>
</script>

<script type="text/html" id="logContent">
    <table class="table">
        <thead>
        <tr class="tabcon_title">
            <th>时间</th>
            <th>发送数量</th>
            <th>操作人</th>
            <th>操作</th>
        </tr>
        </thead>
        <% if (success) { %>
        <tbody>
        <% if (data && data.content && data.content.length) { %>
        <% for (var i = 0; i < data.content.length; i++) { %>
        <% var item = data.content[i]; %>
        <tr class="tabcon_list">
            <td><%= item.gmtCreateStr %></td>
            <td><%= item.smsNum %></td>
            <td><%= item.operator %></td>
            <td>
                <a href="javascript:void(0);"
                   data-template="<%=item.template%>"
                   data-position="<%=item.position%>"
                   data-id="<%=item.id%>"
                   class="js-detail link-detail">查看详情</a>
            </td>
        </tr>
        <% }} %>
        </tbody>
        <% } %>
    </table>
</script>


<script type="text/html" id="detailTpl">
    <table class="table">
        <thead>
        <tr class="tabcon_title">
            <th>序号</th>
            <th>车牌</th>
            <th>车主</th>
            <th>车主电话</th>
        </tr>
        </thead>
        <% if (success) { %>
        <tbody>
        <% if (data && data.content && data.content.length) { %>
        <% for (var i = 0; i < data.content.length; i++) { %>
        <% var item = data.content[i]; %>
        <tr class="tabcon_list">
            <td><%= (i+1) + data.number * data.size %></td>
            <td><%= item.licenses %></td>
            <td><%= item.customerName %></td>
            <td>
                <%= item.mobiles %>
            </td>
        </tr>
        <% }} %>
        </tbody>
        <% } %>
    </table>
</script>

<#include "layout/footer.ftl" >
<script type="text/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>

<script>

    jQuery(function($) {
        var $document = $(document),
                searchData = {
                    page: 1
                },
                loadId, tabIndex, tpl;
        var _detailPage = {
            page: 1,
            size: 10
        };

        /**** ?Ajax全局设置（该设置可以通过 global: false 取消） ****/

        $document.ajaxStart(function() {
            loadId = taoqi.loading();
        }).ajaxStop(function() {
            taoqi.close(loadId);
            loadId = undefined;
        });

        /**** 发送记录 ****/

        var Model = {
            // 查询数据接口
            search: function (tabIndex, success) {
                var url = "";
                switch (tabIndex) {
                    case 1: url = BASE_PATH + '/shop/marketing/sms/list/recharge';break;
                    case 2: url = BASE_PATH + '/shop/marketing/sms/log_list';break;
                }

                $.ajax({
                    url: url,
                    data: searchData,
                    dataType: "json",
                    success: function (result) {
                        success && success(result);
                    }
                });
            }
        };

        /* tab数据填充 */
        function renderContent() {
            Model.search(tabIndex, function(result) {
                var html;

                if(result.success) {
                    html = template(tpl, result);
                    $('#Record_tabcon').html(html);

                    $.paging({
                        dom : $(".qxy_page.list_page"),
                        itemSize : result.data.totalElements,
                        pageCount : result.data.totalPages,
                        current : result.data.number + 1,
                        backFn: function(p) {
                            searchData['page'] = p;
                            renderContent();
                        }
                    });
                } else {
                    layer.msg(result.errorMsg);
                }
            })
        }

        function renderDetailContent(p) {
            var self = this;
            var data = {
                smsLogId: $(this).data('id')
            };
            var _data = $(this).data();

            if(typeof p !== 'string' && typeof p !== 'number') {
                p = 1;
            }
            _detailPage.page = p || 1;
            $.extend(data, _detailPage);

            $('.Z-right.list_box')
                    .add( $('.list_page') )
                    .addClass('hide');
            $('.Z-right.detail_box').removeClass('hide');

            $('#template').text(_data.template);
            $('#position').text(convertPositionToStr(_data.position));

            $.get(BASE_PATH + '/shop/marketing/sms/log_detail',
                    data,
                    function (result) {
                        var html;

                        if (result.success) {
                            html = template('detailTpl', result);
                            $('#detail_tabcon').html(html);

                            $.paging({
                                dom: $(".qxy_page.detail_page"),
                                itemSize: result.data.totalElements,
                                pageCount: result.data.totalPages,
                                current: result.data.number + 1,
                                backFn: function (p) {
                                    renderDetailContent.call(self, p);
                                }
                            });
                        } else {
                            layer.msg(result.message);
                        }

                    });
        }

        $document
                .on('click', '.js-back', function () {
                    $('.Z-right.list_box')
                            .add( $('.list_page') )
                            .removeClass('hide');
                    $('.Z-right.detail_box').addClass('hide');
                })
                .on('click', '.js-detail', renderDetailContent)
            //充值选择
                .on('click', '.recharge_list li', function () {
                    var $this = $(this);
                    $this.addClass("hover").siblings().removeClass("hover");
                })
                .on("click", ".J-Pay", function () {
                    var $changeType = $("ul.recharge_list>.hover");
                    if ($changeType && $changeType.size() == 1) {
                        window.location = "${BASE_PATH}/marketing/ng/center/sms/pay_way?rechargeId=" + $changeType.data("id");
                    } else {
                        taoqi.failure("请选中充值类型.");
                    }
                })
            // tab切换
                .on('click', '.Record span', function() {
                    var $this = $(this);

                    // 样式切换
                    $this.addClass("hover")
                            .siblings('.hover').removeClass('hover');

                    // 模板和调用接口切换
                    tabIndex = $this.data('index');
                    switch (tabIndex) {
                        case 1: tpl = "sendContent";break;
                        case 2: tpl = "logContent";break;
                    }

                    // 初始化分页
                    searchData['page'] = 1;

                    renderContent();
                });

        // 触发第一个tab的点击事件
        $('.Record .hover').trigger('click');

        function convertPositionToStr(position) {
            var str = '';
            switch (position) {
                case 1:
                    str = '客户营销-客户分析-客户类型分析';
                    break;
                case 2:
                    str = '客户营销-客户分析-客户级别分析';
                    break;
                case 3:
                    str = '客户营销-客户分析-客户流失分析';
                    break;
                case 4:
                    str = '客户营销-提醒中心';
                    break;
                case 5:
                    str = '客户营销-精准营销';
                    break;
                case 6:
                    str = '客户营销-微信公众号-关注送券-点击群发短信';
                    break;
            }

            return str;
        }
    });
</script>