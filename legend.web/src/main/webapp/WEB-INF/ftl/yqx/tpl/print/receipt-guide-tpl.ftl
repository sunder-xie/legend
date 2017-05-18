<style>
    
.print-receipt-dialog {
    width: 744px;
    display: none;
}

.print-receipt-dialog strong {
    font-family: SimHei, Simsun, Tahoma, Arial, Helvetica Neue, Hiragino Sans GB, sans-serif;
    font-weight: 900;
}

.print-receipt-dialog .dialog-content>*{
    float: left;
}

.print-receipt-dialog .dialog-content {
    margin-bottom: 0;
    padding: 13px 13px 0;
    line-height: 18px;
}

.print-receipt-dialog .dialog-content .more {
    margin-top: 25px;
    color: #666;
}

.print-receipt-dialog .dialog-content>img {
    width: 216px;
    height: 445px;
    margin-right: 16px;
}

.print-receipt-dialog .footer {
    float: none;
    margin-top: 20px;
    padding: 0 13px;
}

.print-receipt-dialog .footer .box {
    padding: 15px 0 13px;
    border-top: 1px solid #ddd;
}
</style>
<div class="yqx-dialog print-receipt-dialog" id="receiptGuide" data-tpl="receipt-guide-tpl">
    <div class="dialog-title">设置打印格式</div>
    <div class="dialog-content clearfix">
        <img src="${BASE_PATH}/static/img/page/setting/print-setting/receipt-guide-1.png">
        <img src="${BASE_PATH}/static/img/page/setting/print-setting/receipt-guide-2.png">
        <div class="guide-text">
            <p>首先准备一台小票打印机，在小票打印预览</p>
            <p>时请根据以下格式设置小票：</p>
            <ul style="margin-top: 24px;">
                <li>
                    1.确认<strong>[目标打印机]</strong>为小票打印机；
                </li>
                <li>
                    2.点击<strong>[更多设置]</strong>；
                </li>
                <li>
                    <p>3.确认<strong>[纸张尺寸]</strong>设置为58mm×210mm；如</p>
                    <p>果没有此选项，请安装小票机对应的驱动，</p>
                    <p>具体需要询问打印机售后；</p>
                </li>
                <li>
                    4.确认<strong>[边距]</strong>设置为无；
                </li>
                <li>
                    <p>5.确认<strong>[简化网页]</strong>、<strong>[页眉和页脚]</strong>、</p>
                    <p style="padding-left: 10px;"><strong>[背景图形]</strong>没有选择；</p>
                </li>
                <li>
                    6.然后点击[打印]。
                </li>
            </ul>
            <div class="more">
                <p>如果打印有问题，请按照以下步骤测试你的</p>
                <p>小票打印是否正常工作：</p>
                <ul>
                    <li>
                        1.点击<strong>[开始]</strong>菜单；
                    </li>
                    <li>
                        2.点击<strong>[设置]</strong>；
                    </li>
                    <li>
                        3.选择<strong>[打印机和传真]</strong>；
                    </li>
                    <li>
                        4.点击打印设备，点击鼠标右键选择<strong>[属性]</strong>；
                    </li>
                    <li>
                        5.点击<strong>[打印测试页]</strong>。
                    </li>
                </ul>
                <p>如果测试小票打印不正常，请联系打印机售</p>
                <p>后解决。</p>
                <p>如果测试小票正常，请联系云修客服：</p>
                <p>400-9937-288，转2转3</p>
            </div>
        </div>
    </div>
    <div class="footer clearfix">
        <div class="box clearfix">
            <button class="js-cancel yqx-btn yqx-btn-1 fl">取消</button>
            <button class="js-confirm yqx-btn-2 fr yqx-btn">试打印</button>
        </div>
    </div>
</div>
<script>
    seajs.use(['dialog'], function (dialog) {
        var _guideDialog = $('#receiptGuide');
        var _index;
        var com = window.Components = window.Components || {};
        var isShowed = localStorage.isShowedPrintReceiptGuide == 'true';
        var _url;

        com.receiptGuideDialog = function (url) {
            if(!isShowed || url === true) {
                _index = dialog.open({
                    content: _guideDialog
                });

                _url = url === true ? null : url;

                return isShowed = localStorage.isShowedPrintReceiptGuide = true;
            }
        };

        $('.print-receipt-dialog .js-confirm').click(function () {
            dialog.close(_index);
            util.print(_url || (BASE_PATH + '/shop/print-config/try-receipt'));
        });
        $('.print-receipt-dialog .js-cancel').click(function () {
            dialog.close(_index);
        });
    });
</script>
