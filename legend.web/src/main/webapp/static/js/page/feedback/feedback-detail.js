$(function () {
    var maxWidth = 678;
    $(document).on('click', '.js-pic-view', function () {
        var src = $(this).attr('data-src');
        var img = $('<img src=' + src +'>').addClass('hide').appendTo(document.body);
        img.on('load', function () {
            util.imgZoomBigger.call(this, {maxWidth: 600, maxHeight: 400});
        });
    });
    $('.history-text').each(function () {
        $(this).find('img').each(function () {
            var width = this.naturalWidth, height = this.naturalHeight;
            if(this.naturalWidth > maxWidth) {
                width = maxWidth;
                height = (width / this.naturalWidth * this.naturalHeight) >> 0;

                $(this).css({
                    width: width,
                    height: height
                });
            }
        });

        $(this).removeClass('hide');
    });
});