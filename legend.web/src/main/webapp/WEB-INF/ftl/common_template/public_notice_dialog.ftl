<link rel="stylesheet" href="${BASE_PATH}/resources/style/page/common_tpl_style/publicNotice_tpl.css?839742dd1b227e1b49b88ae3199360b4"/>

<script type="text/javascript" src="${BASE_PATH}/resources/js/home/public_notice_dialog.js?397cc8418022043da6f877f3eeb285dd"></script>


<script id="publicNoticeDialogContentTpl" type="text/html">
    <div id="public_notice_dialog" class="clearfix">
        <div class="btn_group">
            <span class="notice-close-btn">×</span>
        </div>
        <div class="public_notice_dialog_content" id="public_notice_dialog_content">
            <div class="public_notice_dialog_content_inner">
                <p class="public_notice_title"><%= data.noticeTitle %></p>
                <div><%== data.noticeContent %></div>
            </div>
        </div>
        <div class="public_notice_dialog_btns">
            <a href="javascript:;"></a>
        </div>
    </div>
</script>
