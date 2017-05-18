/**
 * Created by huage on 2016/12/21.
 */
$(function () {
        addHeadStyle(3);
        var $doc = $(document);
        $doc.on('click','.go-record',function () {
                window.location.href = BASE_PATH + '/smart/bihu/usedView/recharge-list';
        });
        $doc.on('click','.go-recharge',function () {
                var url = BASE_PATH + '/smart/bihu/recharge/index';
                window.location.href = Smart.mode_url(url);
        });
});