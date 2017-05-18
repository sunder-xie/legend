/**
 * Created by sky on 15/11/23.
 */
(function($) {
    $.fn.counter = function(options) {

        var opts = $.extend({}, $.fn.counter.default, options);

        this.each(function() {

            var maxCount = $(this).data("maxcount"),    // 获得最大字数
                showClass = opts.showClass,
                v = $(this).val().length;

            if (!maxCount) {
                maxCount = opts.maxCount;
            }

            $(this).siblings("." + showClass).text(v + "/" + maxCount);

            $(this).on('input', function() {

                var $this = $(this),
                    value = $this.eq(0).val();
                $this.siblings("." + showClass).text(value.length + "/" + maxCount);
            }).on('blur', function() {

                var $this = $(this),
                    value = $this.eq(0).val();
                // 比较输入内容的长度，如果超过最大范围，则删除超过部分
                if(value.length > maxCount) {
                    value = value.substr(0, maxCount);
                    $this.val(value);
                    taoqi.info("您的字数已超过" + maxCount + "字，请检查！");
                }
                $this.siblings("." + showClass).text(value.length + "/" + maxCount);
            });
        });
    };

    $.fn.counter.default = {
        showClass: "showCount",
        maxCount: "20"
    };
})(jQuery);