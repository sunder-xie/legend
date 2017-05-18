/**
 *
 * Created by ende on 16/11/18.
 */
$(function () {
    var PRINT_SELECTOR = {
        'new': ['.new-print-btn', '.new-print-tips'],
        'old': ['.old-print-btn', '.old-print-tips']
    };

    var printVersion = new PrintVersion();

    printVersion.getPrintVersion();

    // 打印版本改变
    $('#changePrintVersionBtn').on('click', function () {
        printVersion.changePrintVersion();
    });

    function PrintVersion () {
        this.version = 'old';

        this.getPrintVersion =  function () {
            var that = this;
            $.ajax({
                url: BASE_PATH + '/shop/conf/get-print-version',
                success: function (json) {
                    if(json.success) {
                        that.version = json.data;

                        setPrintVersion(that.version);
                    }
                }
            });
        };
        this.changePrintVersion =  function () {
            var that = this;
            $.ajax({
                url: BASE_PATH + '/shop/conf/change-print-version',
                success: function (json) {
                    if(json.success) {
                        that.version = json.data;

                        that.setPrintVersion(that.version);
                    }
                }
            });
        };

        function setPrintVersion(version) {
            var selector = PRINT_SELECTOR[version].join(',');

            if(version !== 'new') {
                $(PRINT_SELECTOR.new.join(',')).addClass('hide');
            } else if(version !== 'old') {
                $(PRINT_SELECTOR.old.join(',')).addClass('hide');
            }

            $(selector).removeClass('hide');
        }

        return this;
    }


});
