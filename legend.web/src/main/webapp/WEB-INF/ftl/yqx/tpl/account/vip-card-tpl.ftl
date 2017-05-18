<#-- Created by sky on 2017/3/7. -->

<style data-ref-tpl="vip-card-tpl.ftl">
    .vip-card-panel {
        width: 100%;
        background: #f9f9f9;
        border: 1px solid #d2d2d2;
        border-radius: 2px;
    }

    .vip-card-header {
        padding: 0 10px;
        line-height: 38px;
        background: #eee;
        border-bottom: 1px solid #d2d2d2;
    }

    .vip-item {
        color: #5f7c00;
    }

    .vip-control {
        color: #666;
    }

    .vip-control:before {
        content: "∧";
        float: left;
        display: block;
        margin-right: 3px;
        transform: scale(1.3, 0.6);
    }

    .vip-control:after {
        content: "收起";
    }

    .vip-control.collapse:before {
        content: "∨";
    }

    .vip-control.collapse:after {
        content: "展开";
    }

    .vip-card-body .vip-card-content {
        border: 0;
        border-radius: 0;
    }

    .vip-card-content {
        padding: 15px;
        background: #f9f9f9;
        border: 1px solid #d2d2d2;
        border-radius: 2px;
    }

    .curr-vip-box, .other-vip-box {
        margin: -10px -10px 0 0;
    }

    .other-vip-control-box {
        margin: 15px 0;
    }

    .other-vip-control-box .form-label {
        width: 130px;
    }

    /* 会员卡样式 */
    .vip-card {
        position: relative;
        float: left;
        width: 320px;
        margin: 10px 10px 0 0;
        background: #fff;
        cursor: pointer;
    }

    .vip-card-sm {
        width: 260px;
        /*width: 223px;*/
    }

    .vip-card:before,
    .vip-card:after {
        content: "";
        position: absolute;
        left: 0;
        z-index: 10;
        width: 100%;
        height: 2px;
        background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAUAAAACCAYAAACQahZdAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3NpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTMyIDc5LjE1OTI4NCwgMjAxNi8wNC8xOS0xMzoxMzo0MCAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDowNzNlMjc1ZS0yYTA4LTRiMDItODVmNC1kNzhkZTdhZTViYTkiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RTk3MDA2QkUwMDJEMTFFNzgzNTdCRUUyMDA3NTA3OEUiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RTk3MDA2QkQwMDJEMTFFNzgzNTdCRUUyMDA3NTA3OEUiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6ZDhmOWUzMGYtYWY5NS00MmZlLTk2NmYtOGU1OTg1YTg3YjRhIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjA3M2UyNzVlLTJhMDgtNGIwMi04NWY0LWQ3OGRlN2FlNWJhOSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PtjUMIgAAAAvSURBVHjaYrh06VLw////GYD0BCD+BsRbGYHEfwYgYGVlZVBRUWF49OgRA0CAAQAuERX7x6WMAAAAAABJRU5ErkJggg==") left top repeat-x;
    }

    .vip-card:before {
        top: -2px;
    }

    .vip-card:after {
        bottom: -2px;
        transform: rotate(180deg);
    }

    .vip-card-sm:before,
    .vip-card-sm:after {
        content: none;
    }

    .vip-card-inner {
        height: 148px;
        border-left: 1px solid #d2d2d2;
        border-right: 1px solid #d2d2d2;
    }

    .vip-card-sm .vip-card-inner {
        height: 128px;
        border: 1px solid #d2d2d2;
    }

    .vip-left {
        position: relative;
        width: 85px;
        height: inherit;
        color: #666;
        background: #eee;
    }

    .vip-value {
        position: absolute;
        top: 50%;
        left: 0;
        transform: translateY(-50%);
        width: 100%;
        text-align: center;
    }

    .vip-left strong {
        position: relative;
        margin-bottom: 15px;
        padding-left: 8px;
        font: 20px Helvetica Neue, sans-serif;
        color: #ff1d00;
    }

    .vip-left sup {
        position: absolute;
        top: 2px;
        left: 0;
        font-size: 12px;
    }

    .vip-right {
        height: inherit;
        padding: 15px 20px;
        line-height: 1.7;
        overflow: hidden;
    }

    .vip-right h3, .vip-right h4 {
        font-size: 14px;
    }

    .vip-card .font-yahei {
        font-weight: bold;
        -webkit-font-smoothing: antialiased;
    }

    .vip-minor {
        color: #666;
    }

    .vip-money {
        color: #f40;
    }

    .vip-privilege {
        height: 34px;
        max-height: 34px;
        line-height: 1.5;
    }

    /* 会员卡选中样式 */
    .vip-card.selected .vip-card-inner:before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        z-index: 10;
        box-sizing: border-box;
        width: 100%;
        height: 100%;
        background: url("${BASE_PATH}/static/img/common/selected.png") bottom right no-repeat;
        border: 2px solid #5f7c00;
    }

    .vip-card.selected .vip-card-name {
        color: #928c2f;
    }

    .vip-card.selected .vip-value strong {
        color: #5f7c00;
    }

    /* 会员卡置灰样式 */
    .vip-card.disabled * {
        color: #888;
    }

    .vip-card.disabled .vip-primary {
        color: #444;
    }
</style>

<input type="hidden" id="useGuestAccount" value="${USE_GUEST_ACCOUNT}">

<script type="text/html" id="vip-card-penal-tpl" data-ref-tpl="vip-card-tpl.ftl">
    <% if (useGuestAccount || (cardList && cardList.length)) { %>
    <section class="vip-card-panel">
        <div class="vip-card-header clearfix">
            <div class="fl js-vip-title">
                <span class="vip-label">已选择会员卡：</span><strong class="vip-item js-vip-name"></strong>
            </div>
            <div class="fl js-vip-msg-title hide">
                <span class="vip-label js-vip-msg"></span>
            </div>
            <span class="vip-control fr collapse js-vip-control"></span>
        </div>
        <div class="vip-card-body hide js-vip-card-body">
            <% include('vip-card-content-tpl') %>
        </div>
    </section>
    <% } %>
</script>

<script type="text/html" id="vip-card-content-tpl" data-ref-tpl="vip-card-tpl.ftl">
    <% if (useGuestAccount || (cardList && cardList.length)) { %>
    <div class="vip-card-content">
        <div class="curr-vip-box">
            <% include('vip-card-box-tpl', {list: cardList, type: type}) %>
        </div>
        <% if (useGuestAccount) { %>
        <div class="other-vip-control-box">
            <div class="form-label">
                使用其他客户会员卡：
            </div>
            <div class="form-item">
                <input type="text" class="yqx-input js-mobile-input" value="<%= telephone %>" placeholder="请输入客户电话" <% if (disabledInput) {%>disabled<% }%>>
            </div>
            <div class="form-item">
                <span class="form-text js-vip-other-msg hide">该客户无可用会员卡</span>
            </div>
        </div>
        <div class="other-vip-box js-other-vip-box">
            <% if (guestCardList && guestCardList.length) { %>
                <% include('vip-card-box-tpl', {list: guestCardList, type: type}) %>
            <% } %>
        </div>
        <% } %>
    </div>
    <% } %>
</script>

<script type="text/html" id="vip-card-box-tpl">
    <% if (list && list.length) { %>
        <% var len = list.length %>
        <div class="vip-box-show clearfix">
            <% for (var i = 0; i < len; i++) { %>
                <% include('vip-card-tpl', {item: list[i], type: type}) %>
            <% } %>
        </div>
    <% } %>
</script>

<script type="text/html" id="vip-card-tpl" data-ref-tpl="vip-card-tpl.ftl">
    <div class="vip-card js-vip-card
        <% if (type == 2) { %>vip-card-sm<% } %>
        <% if (item.selected) { %>selected<% } %>
        <% if (!item.available && type != 2) { %>disabled<% } %>"
         data-card-info="<%= $stringify(item) %>" data-card-id="<%= item.cardId %>"
         data-card-name="<%= item.cardName %>">

        <div class="vip-card-inner clearfix">
            <% if (type != 2) { %>
            <div class="vip-left fl">
                <div class="vip-value">
                    <strong><sup>&yen;</sup><%= item.finalDiscount || 0 %></strong>
                    <p>本单可优惠</p>
                </div>
            </div>
            <% } %>

            <div class="vip-right">
                <h3 class="font-yahei vip-primary vip-card-name"><%= item.cardName %></h3>
                <h4 class="font-yahei vip-primary"><%= item.cardNumber %></h4>
                <div class="vip-minor"><%= item.customerName + ' ' + item.mobile %></div>
                <div class="vip-minor">
                    <span>余额：</span>
                    <span class="vip-money"><%= $toFixed(item.balance) %></span>
                </div>
                <% if (type == 2) { %>
                <div class="vip-minor">
                    过期时间：<%= item.expireDateStr %>
                </div>
                <% } else { %>
                <div class="vip-minor vip-privilege ellipsis-2 js-show-tips">
                    会员特权：<%= item.discountDesc %>
                </div>
                <% } %>
            </div>
        </div>
    </div>
</script>

<script data-ref-tpl="vip-card-tpl.ftl">
    (function () {
        var COLLAPSE_CN = 'collapse',
                SELECTED_CN = 'selected',
                DISABLED_CN = 'disabled',
                HIDE_CN = 'hide',
                isHidden = true,
                topic = Components.$broadcast.topic,
                tpl;

        seajs.use(['art'], function (art) {
            /* ^artTemplate扩展方法 */
            art.helper('$stringify', function (json) {
                return JSON.stringify(json);
            });

            art.helper('$toFixed', function (money) {
                return money.toFixed(2);
            });
            /* $artTemplate扩展方法 */
            tpl = art;
        });

        function ChooseVIPCard(options) {
            this.setting = $.extend({}, ChooseVIPCard._default, options);
            this._init();
        }

        ChooseVIPCard.rearrange = function (src, order) {
            var dist = [], len;

            len = order.length;
            // 重排数据不存在
            if (!len) {
                // 获取数据顺序
                $.each(src, function () {
                    order.push(this.cardId);
                });
                return src;
            }

            // 根据srcOrder排序
            $.each(src, function () {
                for (var i = 0; i < len; i++) {
                    if (this.cardId == order[i]) {
                        dist[i] = this;
                        break;
                    }
                }
            });

            return dist;
        };

        /**
         * 内部初始化数据
         */
        ChooseVIPCard.prototype._init = function () {
            this.$scope = $(this.setting.scope);
            this.selectedDfd = null;
            this.inputDfd = null;
            this.token = null;
            this.init();
            this._bindEvent();
        };

        /** OUT ***
         * 初始化数据
         */
        ChooseVIPCard.prototype.init = function () {
            this.autoSelected = this.setting.autoSelected;
            this.srcCardOrder = [];
            this.srcGuestCardOrder = [];
            if (this.setting.tplType === 3) {
                this.setting.tplType = 1;
            }
        };

        /** OUT ***
         * 更新视图，根据tplType判断视图的模型
         * @param data
         */
        ChooseVIPCard.prototype.updateView = function (data) {
            var self = this, html;

            switch(self.setting.tplType) {
                    // 渲染完整的模板
                case 1:
                    html = tpl('vip-card-penal-tpl', data);
                    self.$scope.html(html);
                    self.setting.tplType = 3;
                    return;
                case 2:
                    html = tpl('vip-card-content-tpl', data);
                    self.$scope.html(html);
                    return;
                case 3:
                    html = tpl('vip-card-content-tpl', data);
                    self.$scope.find('.js-vip-card-body').html(html);
                    return;
            }
        };

        /**
         * 绑定控制器（收起/展开）
         */
        ChooseVIPCard.prototype._bindCtrlEvent = function () {
            var self = this;
            self.$scope
                    // 控制卡券的隐藏域展示
                    .off('click.vip', '.js-vip-control')
                    .on('click.vip', '.js-vip-control', function () {
                        self.$scope.find('.js-vip-card-body').toggleClass(HIDE_CN);
                        $(this).toggleClass(COLLAPSE_CN);
                        isHidden = !isHidden;
                    });
        };

        /**
         * 绑定卡券切换事件
         */
        ChooseVIPCard.prototype._bindChooseCardEvent = function () {
            var self = this;
            self.$scope
                    // 控制卡券的选择
                    .off('click.vip', '.js-vip-card')
                    .on('click.vip', '.js-vip-card', function () {
                        var $this = $(this),
                                cardInfo,
                                selectedSelector = '.' + SELECTED_CN,
                                selected,
                                params;

                        // 屏蔽不能选中的状态
                        if ($this.hasClass(DISABLED_CN)) {
                            return false;
                        }

                        // 判断之前是否选中
                        if ($this.hasClass(SELECTED_CN)) {
                            $this.removeClass(SELECTED_CN);
                            selected = false;
                        } else {
                            self.$scope.find(selectedSelector).removeClass(SELECTED_CN);
                            $this.addClass(SELECTED_CN);
                            selected = true;
                        }

                        // 已选中的状态
                        if (selected) {
                            cardInfo = $this.data('cardInfo');

                            try {
                                if (!cardInfo instanceof Object) {
                                    cardInfo = $.parseJSON(cardInfo);
                                }
                            } catch (ex) {
                                console&&console.warn('会员卡信息读取失败！');
                            }
                        }

                        if (!self.setting.localSelect) {
                            params = {
                                selectedCard: cardInfo ? { cardId: cardInfo.cardId } : null
                            };

                            self.trigger(params);
                        }

                        if (self.selectedDfd) {
                            return false;
                        }

                        self.selectedDfd = $.Deferred();
                        self.selectedDfd.done(function () {

                            self.selectedDfd = null;
                            // CALLBACK 选中回调函数
                            self.setting.selectedCardFn.call(self, cardInfo, selected,
                                    self.$scope.find('.js-vip-card').index(
                                            self.$scope.find('.js-vip-card' + selectedSelector)
                                    )
                            );
                        });

                        // 如果仅本地选择，直接执行回调函数
                        self.setting.localSelect && self.selectedDfd.resolve();
                    });
        };

        /**
         * 绑定其他人卡券输入框事件
         */
        ChooseVIPCard.prototype._bindInputEvent = function () {
            var self = this;

            function callback (value, isReplace) {
                if (self.inputDfd) {
                    return false;
                }

                self.inputDfd = $.Deferred();

                self.inputDfd.done(function () {

                    self.inputDfd = null;
                    self.setting.inputTelFn.call(self, value, isReplace);
                });
            }

            self.$scope
                    .off('input.vip', '.js-mobile-input')
                    .on('input.vip', '.js-mobile-input', function () {
                        var $$this = this,
                                value = $$this.value,
                                errMsg = '请输入正确的手机号码！',
                                mobileReg = /^(?:\+\d{2})?1[1-9]\d{9}$/gi,
                                lastTelephone = self.$scope.lastTelephone,
                                discountSelectedBo = {};

                        // 不正确的手机号码
                        if (!mobileReg.test(value)) {
                            seajs.use('check', function (ck) {
                                ck.showCustomMsg(errMsg, $$this);
                            });
                            $$this.value = value.replace(/\D+/gi, '');
                            return false;
                        } else if (value !== lastTelephone) {
                            discountSelectedBo['guestMobile'] = value;
                            if (lastTelephone) {
                                seajs.use('dialog', function (dg) {
                                    dg.confirm('更换手机号码将重置卡券信息', function () {
                                        discountSelectedBo['selectedCard'] = null;
                                        discountSelectedBo['selectedComboList'] = null;
                                        discountSelectedBo['selectedCouponList'] = null;
                                        self.trigger(discountSelectedBo);
                                        callback(value, true);
                                    });
                                });
                                return false;
                            } else {
                                self.trigger(discountSelectedBo);
                            }

                            callback(value, false);
                        }
                    })
                    .off('blur.vip', '.js-mobile-input')
                    .on('blur.vip', '.js-mobile-input', function () {
                        var $$this = this,
                            lastTelephone = self.$scope.lastTelephone;
                        if (lastTelephone == null) {
                            $$this.value = '';
                        } else if ($$this.value != lastTelephone) {
                            $$this.value = lastTelephone;
                        }
                    });

        };

        /**
         * 绑定视图更新事件
         */
        ChooseVIPCard.prototype._bindUpdateEvent = function () {
            var self = this;
            self.unbindUpdateView();
            self.token = Components.$broadcast
                    .subscribe(topic, function (result, params) {
                        var reorderCardList,
                            reorderGuestCardList,
                            data,
                            cardName,
                            selectedIndex,
                            $selectedCard,
                            $mobileInput,
                            lastTelephone;

                        if (result.success) {
                            data = result.data;

                            // 手机号改变，客户卡券排序清除
                            if (self.$scope.lastTelephone != params.discountSelectedBo.guestMobile) {
                                self.srcGuestCardOrder = [];
                            }

                            self.$scope.lastTelephone = params && params.discountSelectedBo.guestMobile;

                            // 用户会员卡排序
                            if (data.sortedCardList && data.sortedCardList.length) {
                                reorderCardList = ChooseVIPCard.rearrange(data.sortedCardList, self.srcCardOrder);
                            }

                            // 其他手机号用户会员卡排序
                            if (data.sortedGuestCardList && data.sortedGuestCardList.length) {
                                reorderGuestCardList = ChooseVIPCard.rearrange(data.sortedGuestCardList, self.srcGuestCardOrder);
                            }

                            // 视图更新
                            self.updateView({
                                cardList: reorderCardList,
                                guestCardList: reorderGuestCardList,
                                type: self.setting.tplType,
                                telephone: self.$scope.lastTelephone,
                                disabledInput: self.setting.disabledInput,
                                useGuestAccount: self.setting.useGuestAccount
                            });

                            // 选中的会员卡名称
                            cardName = self.$scope.find('.selected').data('cardName');

                            // 无可用会员卡
                            if ((!self.$scope.lastTelephone && !(reorderCardList && reorderCardList.length))
                                || self.$scope.lastTelephone && !(reorderGuestCardList && reorderGuestCardList.length) && !(reorderCardList && reorderCardList.length)) {
                                self.$scope.find('.js-vip-msg').text('无可用会员卡');
                                self.$scope.find('.js-vip-msg-title').show();
                                self.$scope.find('.js-vip-title').hide();

                            // 已选择会员卡
                            } else if (cardName != null) {
                                self.$scope.find('.js-vip-name').text(cardName);
                                self.$scope.find('.js-vip-title').show();
                                self.$scope.find('.js-vip-msg-title').hide();

                            // 未选择会员卡
                            } else {
                                self.$scope.find('.js-vip-msg').text('未选择会员卡');
                                self.$scope.find('.js-vip-msg-title').show();
                                self.$scope.find('.js-vip-title').hide();
                            }

                            // 其他客户无可用会员卡
                            if (self.$scope.lastTelephone && !(reorderGuestCardList && reorderGuestCardList.length)) {
                                self.$scope.find('.js-vip-other-msg').show();
                            } else {
                                self.$scope.find('.js-vip-other-msg').hide();
                            }

                            // 初始化完成自动选择指定会员卡
                            if (self.autoSelected) {
                                // 选择会员卡的位置
                                selectedIndex = self.autoSelected - 1;
                                $selectedCard = self.$scope.find('.js-vip-card').eq(selectedIndex);

                                // 0 表示清除自动选择功能，防止循环调用
                                if ($selectedCard.length) {
                                    $selectedCard.click();
                                    self.autoSelected = 0;
                                    return;
                                } else {
                                    self.autoSelected = 0;
                                }
                            }

                            self.setting.updateViewFn.call(self, data, params);

                            self.selectedDfd && self.selectedDfd.resolve();
                            self.inputDfd && self.inputDfd.resolve();
                        } else {
                            $mobileInput = self.$scope.find('.js-mobile-input');
                            lastTelephone = self.$scope.lastTelephone;
                            $mobileInput.val(lastTelephone || '');
                        }

                    });
        };

        /** OUT ***
         * 解绑视图更新
         */
        ChooseVIPCard.prototype.unbindUpdateView = function () {
            if (this.token) {
                Components.$broadcast.unsubscribe(this.token);
                this.$scope.empty();
            }
        };

        /**
         * 绑定所有事件
         */
        ChooseVIPCard.prototype._bindEvent = function () {
            this._bindCtrlEvent();
            this._bindChooseCardEvent();
            this._bindInputEvent();
            this._bindUpdateEvent();
        };

        /** OUT ***
         * 触发视图更新
         */
        ChooseVIPCard.prototype.trigger = function (discountSelectedBo) {
            var data = $.extend({}, this.setting.params(), {discountSelectedBo: discountSelectedBo});

            Components.$broadcast.distribute(topic, this.setting.type, data);
        };

        /** OUT ***
         * 是否存在有效会员卡
         */
        ChooseVIPCard.prototype.hasAvailableCard = function () {
            return this.setting.useGuestAccount || (this.srcCardOrder && this.srcCardOrder.length);
        };

        /** OUT ***
         * 获得选中的值
         * @returns {JSON/String}
         */
        ChooseVIPCard.prototype.getSelectedData = function () {
            var cardInfo = this.$scope.find('.js-vip-card.selected').data('cardInfo');

            try {
                if (cardInfo == null) {
                    return null;
                }

                return $.parseJSON(cardInfo);
            } catch (ex) {
                return cardInfo;
            }
        };

        /**
         * 会员卡禁用/解禁状态
         * @param disabled 是否禁用
         * @params ...exclude 会员卡的index列表
         */
        ChooseVIPCard.prototype.disabledCard = function (disabled) {
            var self = this,
                    exclude = Array.prototype.slice.call(arguments, 1),
                    len = exclude.length;
            self.$scope.find('.js-vip-card').each(function (i, v) {
                var $this = $(this),
                    data = $this.data('cardInfo');

                // 如果卡券本身不可选
                if (!data.available) {
                    $this.addClass(DISABLED_CN);
                    return true;
                }

                if (disabled) {
                    if (len && exclude.indexOf(i) != -1) {
                        $this.removeClass(DISABLED_CN);
                    } else {
                        $this.addClass(DISABLED_CN);
                    }
                } else {
                    if (len && exclude.indexOf(i) != -1) {
                        $this.addClass(DISABLED_CN);
                    } else {
                        $this.removeClass(DISABLED_CN);
                    }
                }
            });
        };

        ChooseVIPCard._default = {
            /*
            * 模板渲染的根路径
            */
            scope: null,
            /*
            * 请求的url
            * 0: 缓存
            * 1: 洗车单
            * 2: 其他
            */
            type: 1,
            /*
            * 模板渲染
            * 1: 包含控制器
            * 2: 只包含卡片内容
            */
            tplType: 1,
            /**
             * 默认选中，数字表示选中第几项
             */
            autoSelected: 0,
            /**
             * 禁用手机号码输入框
             */
            disabledInput: false,
            /**
             * 是否使用其他客户会员卡
             * 注：该字段与disabledInput的差别在于
             *      disabledInput只是不能输入手机号码，可以展现内容
             *      useGuestAccount将去除输入框和其他用户会员卡的展示区域
             */
            useGuestAccount: $('#useGuestAccount').val() == 'yes',
            /**
             * 是否仅本地选择会员卡
             */
            localSelect: false,
            /*
            * ajax请求参数
            */
            params: function () {
            },
            /**
             * 手机号码校验成功，触发视图更新
             * @pram tel
             */
            inputTelFn: function (tel) {
            },
            /**
             * 选中会员卡时回调
             * @param data 选中会员卡的基本信息
             * @param selected 判断是否选中
             * @param index 当前会员卡位置
             */
            selectedCardFn: function (data, selected, index) {
            },
            /**
             * 更新视图触发
             * @param data 会员卡的所有信息
             * @param params ajax传参信息
             */
            updateViewFn: function (data, params) {
            }
        };

        Components.ChooseVIPCard = ChooseVIPCard;
    })();
</script>