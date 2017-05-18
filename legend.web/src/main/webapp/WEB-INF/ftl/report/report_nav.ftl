<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/rp-common.css?dd7cb8a982e68410bef755700d5a34c4"/>
<link rel="stylesheet" href="${BASE_PATH}/resources/css/report/index.css?adb24f81584fdc4737865b50a51f326e"/>
<link rel="stylesheet" type="text/css" href="${BASE_PATH}/resources/style/modules/paging.css?0a59540bff205589e7dc7ad09496808e"/>
<script type="application/javascript" src="${BASE_PATH}/resources/js/common/paging.js?c4ece14e0f4c534b806efc96ae712410"></script>
<div class="wrapper clearfix">
    <div class="aside">
        <!-- 导航栏 start -->
        <div class="nav js-new-nav">
            <div class="nav-box">
                <h1 class="nav-box-title js-nav-title">经营报表</h1>
                <ul class="nav-box-list js-nav-list">
                    <li id="n10"><a href="${BASE_PATH}/report">营业日报</a></li>
                    <li <#if moduleUrlTab == "order_payment">class="current"</#if> id="n11"><a href="${BASE_PATH}/shop/stats/order_payment">工单结算收款表</a></li>
                    <li <#if moduleUrlTab == "order_info_detail">class="current"</#if> id="n12"><a href="${BASE_PATH}/shop/stats/order_info_detail">工单流水表</a></li>
                </ul>
            </div>
        </div>
        <!-- 下线提醒弹窗 start -->
        <script id="oldTipsTpl" type="text/html">
            <div class="dg-panel">
                <div class="dg-panel-head">
                    <h1 class="dg-panel-title">下线提醒</h1>
                </div>
                <div class="dg-panel-body">
                    <div class="dg-content">
                        <p class="dg-msg">亲，该报表将于2016年5月23日下线，请去新版“<%= msg %>”查看经营毛利，谢谢！</p>
                        <div class="dg-note">（有疑问请联系淘汽云修客服，联系电话400-9937-288转2转3，或添加客服QQ3320478090、3056630970、3274979298）</div>
                        <div class="dg-btns-tool">
                            <a class="btn btn-1" href="<%= newLink %>">新版<%= msg %></a>
                            <a class="btn btn-2" href="<%= oldLink %>" target="_blank">继续看老版</a>
                        </div>
                    </div>
                </div>
            </div>
        </script>
        <!-- 下线提醒弹窗 end -->
        <!-- 导航JS代码 start -->
        <script>
            $(function() {
                $(document).on('click', '.js-jump', function() {
                    var $this = $(this),
                            data = {
                                newLink : $this.data('newLink'),
                                oldLink : $this.data('oldLink'),
                                msg     : $this.data('msg')
                            };

                    $.layer({
                        type: 1,
                        title: false,
                        area: ['auto', 'auto'],
                        border: [0],
                        shadow: ['0.5', '#000'],
                        closeBtn: [1, true],
                        shift: "top",
                        page: {
                            html: template('oldTipsTpl', data)
                        }
                    });
                });
            })
        </script>
        <!-- 导航JS代码 end -->
    </div>