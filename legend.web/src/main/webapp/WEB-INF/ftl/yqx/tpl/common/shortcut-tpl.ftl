<#-- Created by sky on 2017/2/21. -->

<div class="extension-box js-shortcut-box" data-tpl-ref="shortcut-tpl">
    <div class="extension-icon shortcut-icon"></div><p class="extension-text">快捷创建</p>
</div>
<script src="${BASE_PATH}/static/js/common/extension/shortcut.js?878fd56397559c6d8d2dd84725f632f7"></script>
<script>
    $(function () {
        $(document).on('click', '.js-shortcut-box', function () {
            Components.getShortcutDialog(false);
        })
    })
</script>

