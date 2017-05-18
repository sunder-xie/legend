<style>
    .add-supplier-dialog.yqx-dialog .form-item {
        width: 260px;
    }

    .add-supplier-dialog .must {
        position: relative;
    }

    .add-supplier-dialog .must:after {
        position: absolute;
        content: '*';
        color: red;
        right: -7px;
        top: -4px;
    }

    .add-supplier-dialog.yqx-dialog li {
        margin-bottom: 10px;
    }
</style>
<script id="supplierTpl" type="text/html">
    <div class="yqx-dialog add-supplier-dialog" data-tpl="add-supplier-tpl">
        <div class="dialog-title">
            添加供应商
        </div>
        <ul class="dialog-content">
            <li>
                <label class="must">供应商名称</label><div class="form-item">
                <input type="text" name="supplierName" class="yqx-input"
                        data-v-type="required">
                    </div>
            </li>
            <li>
                <label class="must">供应商编号</label><div class="form-item">
                    <input type="text" placeholder="例：GY141217110700"
                    class="yqx-input" name="supplierSn"
                            data-v-type="required">
                </div>
            </li>
            <li>
                <label class="must">供应商类别</label><div class="form-item">
                <input type="text" class="yqx-input js-supplier-category">
                <input type="hidden" name="category" data-v-type="required">
                <span class="fa icon-angle-down"></span>
                </div>
            </li>
            <li>
                <label class="must">　结算方式</label><div class="form-item">
                <input type="text" class="yqx-input js-supplier-pay">
                       <input type="hidden" name="payMethod" data-v-type="required">
                <span class="fa icon-angle-down"></span>
            </div>
            </li>
            <li><label>　发票类型</label><div class="form-item">
                <input type="text" class="yqx-input js-supplier-invoice">
                <input type="hidden" name="invoiceType">
                <span class="fa icon-angle-down"></span>
            </div>
            </li>
            <li>
                <label>　发票抬头</label><div class="form-item">
                    <input type="text" name="companyName" class="yqx-input">
                </div>
            </li>
            <li>
                <label>　　联系人</label><div class="form-item">
                    <input type="text" maxlength="45" name="contact" class="yqx-input">
                </div>
            </li>

            <li><label>　联系电话</label><div class="form-item">
                <input type="text" name="mobile" class="yqx-input" maxlength="45"
                >
                    </div>
            </li>
        </ul>
        <div class="dialog-footer">
            <button class="yqx-btn yqx-btn-3 js-supplier-submit">提 交</button>
            <button class="yqx-btn yqx-btn-1 js-supplier-cancel">取 消</button>
        </div>
    </div>
</script>
<script>
    function addNewSupplier(opt) {
        var t;

        this.callback = opt.callback;
        //添加供应商数据
        if(!this.isInit) {
            addNewSupplier.prototype.isInit = true;
            this.init();
        }

        if(typeof opt.dom == 'string') {
            $(document).on('click', opt.dom, this.showDialog.bind(this));
        } else if(opt.dom instanceof Array) {
            for(var i in opt.dom) {
                t = opt.dom[i];

                $(document).on('click', t, this.showDialog.bind(this));
            }
        }

        return this;
    }

    addNewSupplier.prototype.html = (function () {
        return $('#supplierTpl').html();
    }());

    addNewSupplier.prototype.init = addNewSupplier.init || function () {
        var self = this;

        seajs.use(['ajax', 'dialog', 'formData', 'select'], function (ajax, dialog, formData, select) {
            $(document).on('click', '.add-supplier-dialog .js-supplier-submit', submit);
            $(document).on('click', '.add-supplier-dialog .js-supplier-cancel', self.closeDialog.bind(self));

            select.init({
                dom: '.js-supplier-pay',
                showKey: 'id',
                showValue: 'name',
                data: [{
                    id: 0,
                    name: '日结'
                }, {
                    id: 1,
                    name: '月结'
                }, {
                    id: 2,
                    name: '季结'
                }]
            });

            select.init({
                dom: '.js-supplier-invoice',
                url: BASE_PATH + '/shop/setting/supplier/get_invoice_type',
                showKey: 'code',
                showValue: 'name',
            });

            select.init({
                dom: '.js-supplier-category',
                showKey: 'id',
                showValue: 'name',
                data: [{
                    id: 1,
                    name: '一级分类'
                }, {
                    id: 2,
                    name: '二级分类'
                }]
            });

            function submit() {
                var params = formData.get('.add-supplier-dialog');
                seajs.use('check', function(ck){
                    if(!ck.check('.dialog-content')) {
                        return;
                    }
                confirm(params);
                });
            }


            function confirm(params) {
                $.ajax({
                    url: BASE_PATH + '/shop/setting/supplier/save',
                    contentType: 'application/json',
                    dataType: 'JSON',
                    type: 'post',
                    data: JSON.stringify(params),
                    success: function (data) {
                        if (!data.success) {
                            dialog.fail(data.message);
                        } else {
                            dialog.success('添加成功');
                            $.ajax({
                                url: BASE_PATH + '/shop/setting/supplier/supplier-detail',
                                type: 'GET',
                                data: {id : data.data},
                                success: function (data) {
                                    self.callback && self.callback(data);
                                    self.closeDialog();
                                }
                            });
                        }
                    }
                });
            }
        });
    };

    addNewSupplier.prototype.showDialog = function (e) {
        var self = this;
        seajs.use('dialog', function (dialog) {
            self.dialog = dialog.open({
                content: self.html,
                area: ['400px']
            });
            self.wrapper = $('.yqx-dialog.add-supplier-dialog');
        });

        $.ajax({
            url: BASE_PATH + "/shop/setting/supplier/get_supplier_sn",
            type:'GET',
            success: function (data) {
                if (data && data.success) {
                    self.wrapper.find('input[name=supplierSn]').val(data.data);
                }
            }
        });

        e.preventDefault();
    };
    addNewSupplier.prototype.closeDialog = function () {
        var self = this;
        seajs.use('dialog', function (dialog) {
            dialog.close(self.dialog);
        });
    };
</script>