<!--绑定银行卡弹框 -->
<script type="text/html" id="bandBindTpl">
    <style>
        .pop_panel {
            width: 413px;
            background: #fff;
        }
        .pop_header {
            border-bottom: 1px solid #e8e8e8;
        }
        .pop_content {
            padding: 18px 20px;
        }
        .pop_footer {
            padding: 10px;
            text-align: center;
            border-top: 1px solid #e8e8e8;
        }
        .highlight {
            color: #ff7800;
        }
        .w_217 {
            width: 217px;
        }
        .w_117 {
            width: 117px;
        }
        .w_89 {
            width: 89px;
        }

        .mr_10 {
            margin-right: 10px;
        }
        .pop_header h1 {
            line-height: 41px;
            font-size: 12px;
            font-weight: 700;
            text-align: center;
            color: #444;
        }
        .pop_row {
            margin-bottom: 15px;
        }
        .pop_row:last-child {
            margin-bottom: 0;
        }
        .pop_label {
            display: inline-block;
            width: 82px;
            padding-right: 17px;
            line-height: 37px;
            font-size: 12px;
            text-align: right;
            color: #535353;
        }
        .pop_input {
            height: 35px;
            text-indent: 10px;
            background: #fff;
            border: 1px solid #e8e8e8;
            -webkit-border-radius: 3px;
            -moz-border-radius: 3px;
            border-radius: 3px;
        }
        .pop_tips{
            color: #f00;
            font-size: 10px;
            padding-top: 5px;
            padding-left: 95px;
            display: inline-block;
        }
        .pop_select {
            height: 37px;
            background: #f8f8f8;
        }
        .pop_note {
            line-height: 2;
        }
        .pop_btn {
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            margin-right: 10px;
            padding: 0 10px;
            line-height: 37px;
            font-size: 12px;
            color: #fff;
            -webkit-border-radius: 3px;
            -moz-border-radius: 3px;
            border-radius: 3px;
        }
        .pop_btn:last-child {
            margin-right: 0;
        }
        .pop_btn_default {
            line-height: 35px;
            color: #333;
            background: #fff;
            border: 1px solid #e8e8e8;
        }
        .pop_btn_primary {
            background: #8fb027;
        }
        .text-danger {
            min-height: 14px;
            margin-left: 92px;
            line-height: 1.2;
            font-size: 12px;
            color: #f00;
        }
        .area {
            width: 107px;
        }
        .bank {
            width: 220px;
        }
        .pop_row select{
            display: inline-block;
            border: 1px solid #e8e8e8;
            width: 105px;
            height: 30px;
        }


        .dialog{background: #fff;}
        .dialog-title{height:55px;line-height:55px;text-align:center;background:#232e49;font-size: 16px;color: #fff;}
        .dialog-con{text-align: center;padding: 10px;}
        .link-btn{display: block;width: 150px;height: 30px;line-height: 30px;text-align: center;border: 1px solid #9fc527;border-radius: 5px;-webkit-border-radius: 5px;-moz-border-radius: 5px;color: #333;margin: 10px auto;  }

    </style>

    <div class="pop_panel dialog">
        <div class="dialog-title">
            完善银行卡信息
        </div>
        <div class="dialog-con">
        <form action="${BASE_PATH}/shop/settlement/activity/bind" method="post" id="bankForm">
            <div class="pop_content">
                <div class="pop_row">
                    <div class="form-label pop_label label_required">开户银行</div>
                    <div class="form-item">
                        <div class="form-item w_217">
                            <input type="text" name="bank" id="bank" class="yqx-input yqx-input-icon">
                            <span class="fa icon-angle-down"></span>
                        </div>
                    </div>
                </div>
                <div class="pop_row">
                    <div class="form-label pop_label label_required">开户省市</div>
                    <#--<select name="bankProvince" id="bankProvince" class="pop_input area"  parent=""  child="bankCity" onchange="areaChangeCallback(this);" ></select>-->
                    <#--<select name="bankCity" id="bankCity" class="pop_input area" parent="bankProvince"   child="" onchange="areaChangeCallback(this);" ></select>-->
                    <div class="form-item">
                        <select class="form-control page-search-control col-md-4 col-xs-4 provinceId" id="bankProvince">
                            <option value="">请选择省</option>
                        </select>
                        <select class="form-control page-search-control col-md-4 col-xs-4 cityId" id="bankCity">
                            <option value="">请选择市</option>
                        </select>
                    </div>
                </div>

                <div class="pop_row">
                    <span class="pop_label label_required">开户支行</span><input
                        class="pop_input w_217" type="text" name="accountBank" id="accountBank"  placeholder="例：杭州高新支行"/>
                </div>
                <div class="pop_row">
                </div>
                <div class="pop_row">
                    <span class="pop_label label_required">收 款 人</span><input
                        class="pop_input w_217" type="text" name="accountUser" id="accountUser"/>
                </div>
                <div class="pop_row">
                    <span class="pop_label label_required">银行卡号</span><input
                        class="pop_input w_217" type="text" name="account" id="account"/>
                </div>
                <div class="pop_row">
                    <span class="pop_label label_required">联系电话</span><input
                        class="pop_input w_117 mr_10" type="text" name="mobile" id="mobile"
                        disabled="disabled"/><input
                        type="button" value="获取验证码" class="pop_btn pop_btn_primary w_89 get_code"/>
                </div>
                <div class="pop_row">
                    <span class="pop_label label_required">验 证 码</span><input
                        class="pop_input w_217" type="text" name="identifyingCode" id="identifyingCode"/>
                </div>
                <div class="pop_row">
                <#--<p class="pop_note">账单生成日期：每月15日，账单账期：上个自然月</p>-->
                    <p>如有疑问请拨打：<strong class="highlight">400-9937-288</strong></p>
                </div>
                <div class="pop_row">
                    <p class="text-danger errormsg"></p>
                </div>
            </div>
        </form>

        <div class="pop_footer">
            <button class="pop_btn pop_btn_primary w_117" id="bankOk">确　定</button>
            <button
                    class="pop_btn pop_btn_default w_117" id="bankCancle">取　消
            </button>
        </div>
        </div>
    </div>
</script>