<#-- Created by sky on 2017/3/9. -->

<script data-ref-tpl="broadcast-tpl.ftl">
    (function () {

        var model = {
            carWash: function (params) {
                return $.ajax({
                    method: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify(params),
                    url: BASE_PATH + '/shop/settlement/debit/discount/discount-carwash-order'
                });
            },
            other: function (params) {
                return $.ajax({
                    method: 'post',
                    contentType: 'application/json',
                    data: JSON.stringify(params),
                    url: BASE_PATH + '/shop/settlement/debit/discount/discount-order'
                })
            }
        };

        var Broadcast = function () {
            this.rooms = {};
            this.uid = -1;
            this.params = {};
        };

        Broadcast._getRandom = function () {
            var ran = '' + parseInt(Math.random()*100);
            return ran.length == 1 ? '0' + ran : ran;
        };

        Broadcast.prototype._getTopic = function (token) {
            return token.substr(0, token.length - 4);
        };

        Broadcast.prototype.topic = 'message';

        /**
         * 订阅房间
         * @param topic
         * @param func
         * @returns {*}
         */
        Broadcast.prototype.subscribe = function (topic, func) {
            var token = topic + Broadcast._getRandom() + Broadcast._getRandom();

            if (!this.rooms[topic]) {
                this.rooms[topic] = {
                    result: null,
                    observers: []
                };
            }

            this.rooms[topic].observers.push({
                token: token,
                func: func
            });

            return token;
        };

        /**
         * 取消订阅房间
         * @param token
         */
        Broadcast.prototype.unsubscribe = function (token) {
            var self = this,
                    topic = this._getTopic(token),
                    room = self.rooms[topic];

            if (topic == null) {
                return;
            }

            room.observers.some(function (obj, idx) {
                if (obj.token == token) {
                    self.rooms[topic].observers.splice(idx, 1);
                    // 返回为true时，跳出
                    return true;
                }
            });
        };

        /**
         *  数据缓存
         *  @params params
         */
        Broadcast.prototype.getParams = function (params) {
            // 对所有数据进行深拷贝
            //ISSUE discountSelectedBo浅拷贝会替换整个discountSelectedBo对象
            $.extend(true, this.params, params);

            if (params && params.discountSelectedBo) {
                // 对discountSelectedBo进行单独的浅拷贝
                //ISSUE 优惠券取消失败
                $.extend(this.params.discountSelectedBo, params.discountSelectedBo);
            }
        };

        /**
         * 分发消息
         * @param topic
         * @param type 0: 洗车单 1: 其他
         * @param params
         */
        Broadcast.prototype.distribute = function (topic, type, params) {
            var self = this, dfd;

            // 如果请求正在进行，不读取缓存文件
            if (dfd && type == 0) {
                return;
            }

            // 整合会员卡和优惠券的参数
            self.getParams(params);

            seajs.use('ajax', function () {
                switch (type) {
                    case 0:
                        dfd = $.Deferred();
                        break;
                    case 1:
                        // @params orderId, discountSelectedBo
                        dfd = model.other(self.params);
                        break;
                    case 2:
                        // @params carLicense, carWashServiceId, carWashAmount, discountSelectedBo
                        dfd = model.carWash(self.params);
                        break;
                }

                dfd.done(function (result) {
                    dfd = null;

                    if (result.success) {

                        self.preParams = $.extend(true, {}, self.params);

                        if (type != 0) {
                            self.rooms[topic].result = result;
                        }
                    } else {

                        seajs.use('dialog', function (dg) {
                            dg.fail(result.message);
                        });

                        // 参数重置到上一次请求参数
                        self.params = $.extend(true, {}, self.preParams);

                        // 如果后台错误，返回上一次请求数据
                        result = self.rooms[topic].result;

                        // 如果result尚未保存，则不执行下面的操作
                        if (!result) {
                            return;
                        }
                    }

                    self.rooms[topic].observers.forEach(function (obj) {

                        obj.func(result, self.params);
                    });
                });

                if (type == 0) {
                    dfd.resolve(self.rooms[topic].result);
                }
            });
        };

        Components.$broadcast = new Broadcast();
    })();
</script>

