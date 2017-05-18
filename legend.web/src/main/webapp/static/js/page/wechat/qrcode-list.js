/**
 *
 * Created by ende on 16/6/20.
 */

$(function() {
    var url = BASE_PATH + '/shop/wechat/op/qry-qrcode-list';
    var desc = 'qrcode';
    var time = 100;

    seajs.use(['ajax', 'art', 'table', 'layer', 'dialog'], function (ajax, art, table, layer, dialog) {
        dialog.titleInit();

        table.init({
            url: url,
            fillid: desc + 'Fill',
            tplid:  desc + 'Tpl',
            pageid: desc + 'Page',
            data: {size: 12},
            fullString: true,
            callback: function (html) {
                var $e = $(html);
                $e.find('.qr-body img').on('load', resetImg);
                $('#' + desc + 'Fill').append($e);
            }
        });

        $(document).on('click', '.js-preview', function() {
            var src = $(this).attr('src');
            var height = 500;
            // 取整
            var width = (height / this.naturalHeight * this.naturalWidth) >> 0;

            height += 'px';
            width  += 'px';

            layer.open({
                type: 1,
                title: false,
                closeBtn: 1,
                area: [width, height],
                skin: 'layui-layer-nobg', //没有背景色
                shadeClose: true,
                content: '<img src=' + src + '  style="width:' + width
                + ';height:' + height + ';"' + '>'
            });
        });


        $(document).on('click', '.js-download', function (e) {
            var iframe, src;
            if( !('download' in this) ){
                src    = $(this).attr('src') || $(this).attr('href');
                iframe = $('<iframe></iframe>').attr('src', src).hide();
                $(document.body).append(iframe);
            }
        });
    });

    function resetImg() {
        var width = this.naturalWidth;
        var height = this.naturalHeight;

        $(this).css({
            width: (this.clientHeight / height * width) >> 0 || 177
        })
    }


});
