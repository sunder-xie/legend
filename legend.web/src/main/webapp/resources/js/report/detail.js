/**
 * Created by sky on 16/4/15.
 */

$(function() {
    var $document = $(document),
        tplData = {};

    /* 表单字段展示 start */
    function changeField() {
        var isShow = [],
            html;
        $('#refTable input[type=checkbox]').each(function(i, v) {
            var $this = $(this),
                checked = $this.prop('checked');

            isShow.push(checked);
        });
        tplData['isShow'] = isShow;
        html = template('tableTpl', tplData);
        $('#tableContent').html(html);
    }

    changeField();
    $document.on('change', '#refTable input[type=checkbox]', function() {
        changeField();
    });
    /* 表单字段展示 end */


});