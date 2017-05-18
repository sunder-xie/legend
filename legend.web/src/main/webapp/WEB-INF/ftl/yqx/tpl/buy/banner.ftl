<#-- Created by sky on 16/11/24. -->


<header class="banner-box clearfix" id="bannerBox" role="banner">
    <div class="roll-banner-box fr" data-ref-url="tpl/buy/banner">
        <ul class="roll-banner-list js-banner-list" id="bannerListBox"></ul>
        <ol class="roll-banner-ctrl js-banner-ctrl"></ol>
    </div>
    <div class="static-banner-box fl">
        <a href="${BASE_PATH}/shop/yunxiu/purchase/avoid_login/OPT_YUNXIU_TQMALL_HOME" target="_blank">
            <img src="${BASE_PATH}/static/img/page/buy/banner-tqpj.jpg">
        </a>
    </div>
</header>
<section class="row-static-banner-box">
    <a href="${BASE_PATH}/shop/yunxiu/purchase/avoid_login/OPT_YUNXIU_PURCHASE" target="_blank">
        <img src="${BASE_PATH}/static/img/page/buy/row-banner-cgj.jpg">
    </a>
</section>

<script type="text/html" id="bannerItemTpl">
    <% if (data && data.length) { %>
    <% for (var i = 0; i < data.length; i++) { %>
    <% var item = data[i]; %>
    <li>
        <a href="<%= item.customRedirectUrl %>" target="_blank">
            <img src="<%= item.bannerImgUrl %>">
        </a>
    </li>
    <% } } %>
</script>

<script data-ref-tpl="banner">
    $(function () {
        var Banner = function (options) {
            // 当前的索引
            this.index = options.initIndex || 0;
            // banner作用域
            this.scope = options.scope || document;
            // banner父级的类名
            this.bannerScopeCls = options.bannerScopeCls;
            // 控制器父级的类名
            this.ctrlScopeCls = options.ctrlScopeCls;
            // 自动切换间隔时间
            this.delay = options.delay || 1000;
            this._init();
        };

        Banner.prototype = {
            constructor: Banner,
            _init: function () {
                var i, ctrls = '';
                this.banners = $(this.bannerScopeCls).children();
                this.length = this.banners.length;
                if (!this.length) {
                    return;
                }

                for (i = 0; i < this.length; i++) {
                    ctrls += '<li></li>';
                }

                this.ctrls = $(ctrls);
                $(this.ctrlScopeCls).html(this.ctrls);

                this._switchBanner();
                this._switchCtrl();
                this._autoSwitch();
            },
            _setIndex: function (index) {
                if (index != null && index < this.length) {
                    this.index = index;
                } else {
                    if (this.index < this.length - 1) {
                        this.index++;
                    } else {
                        this.index = 0;
                    }
                }
            },
            _switchBanner: function () {
                this.banners.fadeOut(600)
                        .eq(this.index).fadeIn(600);
            },
            _switchCtrl: function () {
                this.ctrls.removeClass('active')
                        .eq(this.index).addClass('active');
            },
            // 自动切换
            _autoSwitch: function () {
                var self = this;
                self.timer = setTimeout(function () {
                    self._setIndex();
                    self._switchBanner();
                    self._switchCtrl();
                    self._autoSwitch();
                }, self.delay);
            },
            // 手动切换
            manualSwitch: function () {
                var self = this;
                $(self.scope)
                        .off('click', self.ctrlScopeCls + ' li')
                        .on('click', self.ctrlScopeCls + ' li', function () {
                    if ($(this).hasClass('active')) {
                        return;
                    }
                    self.timer && clearTimeout(self.timer);
                    self._setIndex($(this).index());
                    self._switchBanner();
                    self._switchCtrl();
                    self._autoSwitch();
                });
            }
        };

        function startBanner(tpl) {
            $.ajax({
                global: false,
                url: BASE_PATH + '/shop/yunxiu/purchase/getBannerList'
            }).done(function (result) {
                var html;
                if (result.success) {
                    html = tpl('bannerItemTpl', result);
                    $('#bannerListBox').html(html);
                    new Banner({
                        scope: '#bannerBox',
                        bannerScopeCls: '.js-banner-list',
                        ctrlScopeCls: '.js-banner-ctrl',
                        delay: 4500
                    }).manualSwitch();
                }
            });
        }

        try {
            seajs.use(['art', 'ajax'], function (tpl) {
                if (tpl && tpl.compile) {
                    startBanner(tpl);
                } else {
                    seajs.use('artTemplate', function (tpl) {
                        startBanner(tpl);
                    });
                }
            });
        } catch (ex) {
            startBanner(template);
        }

    });
</script>

